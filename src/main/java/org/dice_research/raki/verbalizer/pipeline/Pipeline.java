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
import java.util.Set;

import org.dice_research.raki.verbalizer.pipeline.data.input.IRAKIInput;
import org.dice_research.raki.verbalizer.pipeline.data.output.IOutput;
import org.dice_research.raki.verbalizer.pipeline.io.RakiIO;
import org.dice_research.raki.verbalizer.pipeline.planner.DocumentPlanner;
import org.dice_research.raki.verbalizer.pipeline.ui.RAKIPythonBridge;
import org.dllearner.utilities.owl.ManchesterOWLSyntaxOWLObjectRendererImplExt;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;

public class Pipeline implements IVerbalizerPipeline {

  private final String currentPath = Paths.get("").toAbsolutePath().toString();
  private final String openNMTPath = currentPath.concat("/OpenNMT");

  protected IOutput<?> output = null;
  protected IRAKIInput input = null;

  protected static Pipeline instance;

  private Pipeline() {}

  public static synchronized Pipeline getInstance() {
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

  public IOutput<?> getOutput() {
    return output;
  }

  protected SimpleEntry<List<OWLAxiom>, List<String>> inputToManchester(
      final Set<OWLAxiom> owlAxioms) {

    final List<OWLAxiom> axiomsList = new ArrayList<>();
    final List<String> verbList = new ArrayList<>();

    // final OWLObjectRenderer dlR = new DLSyntaxObjectRenderer();
    final OWLObjectRenderer manchesterR = new ManchesterOWLSyntaxOWLObjectRendererImplExt();
    {
      for (final OWLAxiom axiom : owlAxioms) {
        final StringBuilder line = new StringBuilder();
        final List<String> axioms = new ArrayList<>();
        axioms.add(manchesterR.render(axiom));
        for (final OWLClassExpression e : axiom.getNestedClassExpressions()) {
          axioms.add(manchesterR.render(e));
        }
        line.append(String.join("; ", axioms));
        axiomsList.add(axiom);
        verbList.add(line.toString());
      }
    }
    return new SimpleEntry<>(axiomsList, verbList);
  }

  protected Pipeline runsModel() {

    final String scriptPath = openNMTPath.concat("/").concat("translate.py");
    // TODO: add to parameter
    final String model = currentPath.concat("/demo/model_step_1000.pt");
    final String beam_size = "5";

    final Path file;
    {
      file = Paths.get(tmp.toFile().getAbsolutePath()//
          .concat("/")//
          .concat(String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()))//
          .concat(".txt")//
      );
    }

    final SimpleEntry<List<OWLAxiom>, List<String>> in = inputToManchester(input.getAxioms());
    final String lines = String.join(System.lineSeparator(), in.getValue());

    RakiIO.write(file, lines.getBytes());

    final String src = file.toFile().getAbsolutePath();
    final String oputput = file.toFile().getAbsolutePath().concat(".out");

    final String arguments = new StringBuilder()//
        .append("-model ").append(model)//
        .append("-src ").append(src)//
        .append("-output ").append(oputput)//
        .append("-replace_unk")//
        .append("-verbose")//
        .append("-beam_size ").append(beam_size)//
        .toString();

    try {
      new RAKIPythonBridge()//
          .setArguments(arguments)//
          .setScriptPath(scriptPath)//
          .run();

      final List<String> verblines = Files.readAllLines(Paths.get(oputput));
      final List<OWLAxiom> axioms = in.getKey();

      final Map<OWLAxiom, String> map = new HashMap<>();
      for (int i = 0; i < axioms.size(); i++) {
        map.put(axioms.get(i), verblines.get(i));
      }

      final Object success = output.write(map);
      if (success == null) {
        LOG.error("Couldn't write results.");
      }

    } catch (final IOException e) {
      LOG.error(e.getLocalizedMessage(), e);
    }
    return this;
  }

  protected Pipeline runsRules() {
    final DocumentPlanner documentPlanner = new DocumentPlanner(input, output);
    documentPlanner.build();
    documentPlanner.results();
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
  public Pipeline setInput(final IRAKIInput input) {
    this.input = input;
    return this;
  }

  @Override
  public Pipeline setOutput(final IOutput<?> output) {
    this.output = output;
    return this;
  }
}
