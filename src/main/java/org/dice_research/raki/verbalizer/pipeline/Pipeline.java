package org.dice_research.raki.verbalizer.pipeline;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dice_research.raki.verbalizer.pipeline.data.input.IRAKIInput;
import org.dice_research.raki.verbalizer.pipeline.data.output.IOutput;
import org.dice_research.raki.verbalizer.pipeline.io.RakiIO;
import org.dice_research.raki.verbalizer.pipeline.planner.DocumentPlanner;
import org.dice_research.raki.verbalizer.pipeline.ui.CommandLineBridge;
import org.semanticweb.owlapi.model.OWLAxiom;

public class Pipeline implements IVerbalizerPipeline {

  private final static String currentPath = Paths.get("").toAbsolutePath().toString();

  protected IOutput<?> output = null;
  protected IRAKIInput input = null;

  /**
   * Singleton instance.
   */
  protected static IVerbalizerPipeline instance;

  private Pipeline() {}

  public static synchronized IVerbalizerPipeline getInstance() {
    if (instance == null) {
      instance = new Pipeline();
    }
    return instance;
  }

  public static final Path tmp;
  static {
    tmp = Paths.get(System.getProperty("java.io.tmpdir").concat(File.separator).concat("raki"));
    if (!tmp.toFile().exists()) {
      tmp.toFile().mkdirs();
    }
  }

  @Override
  public Pipeline setInput(final IRAKIInput input) {
    this.input = input;
    return this;
  }

  @Override
  public Pipeline setOutput(final IOutput<?> output) {
    this.output = output;
    return this;
  }

  @Override
  public Pipeline run() {
    if (output == null || input == null) {
      throw new UnsupportedOperationException("Output or Input not set.");
    }
    switch (input.getType()) {
      case MODEL:
        return runsModel();
      case RULES:
        return runsRules();
      case NOTSET:
        throw new UnsupportedOperationException("Input type not set.");
    }
    return this;
  }

  @Override
  public IOutput<?> getOutput() {
    return output;
  }

  protected Pipeline runsModel() {

    final Path file;
    {
      file = Paths.get(tmp.toFile().getAbsolutePath()//
          .concat("/")//
          .concat(String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()))//
          .concat(".txt")//
      );
    }
    final SimpleEntry<List<OWLAxiom>, List<String>> in = PipelineHelper//
        .modelInput(input.getAxioms());
    final String lines = String.join(System.lineSeparator(), in.getValue());

    RakiIO.write(file, lines.getBytes());

    // TODO: add to parameter
    final String src = file.toFile().getAbsolutePath();
    final String oputput = file.toFile().getAbsolutePath().concat(".out");
    final String model = currentPath.concat("/demo/model_step_10000.pt");
    try {
      final String rtn = new CommandLineBridge()//
          .setCommand("conda run --name openNMT2 onmt_translate")//
          .setArguments(new StringBuilder()//
              .append("-model ").append(model)//
              .append(" -src ").append(src)//
              .append(" -output ").append(oputput)//
              .append(" -verbose")//
              .toString())
          .run();
      LOG.debug("command log: {}", rtn);
    } catch (final Exception e) {
      LOG.error(e.getLocalizedMessage(), e);

    }
    List<String> verblines = new ArrayList<>();
    try {
      verblines = Files.readAllLines(Paths.get(oputput));
    } catch (final IOException e) {
      LOG.error(e.getLocalizedMessage(), e);
    }
    final List<OWLAxiom> axioms = in.getKey();

    final Map<OWLAxiom, String> map = new HashMap<>();
    for (int i = 0; i < axioms.size() && i < verblines.size(); i++) {
      map.put(axioms.get(i), verblines.get(i));
    }

    final Object success = output.write(map);
    if (success == null) {
      LOG.error("Couldn't write results.");
    }

    return this;
  }

  protected Pipeline runsRules() {
    final DocumentPlanner documentPlanner = new DocumentPlanner(input, output);
    documentPlanner.build();
    documentPlanner.results();
    return this;
  }
}
