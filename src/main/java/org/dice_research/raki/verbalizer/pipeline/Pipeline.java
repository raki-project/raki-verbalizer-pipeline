package org.dice_research.raki.verbalizer.pipeline;

import org.aksw.owl2nl.data.IInput;
import org.dice_research.raki.verbalizer.pipeline.data.output.IOutput;
import org.dice_research.raki.verbalizer.pipeline.planner.DocumentPlanner;

public class Pipeline implements IVerbalizerPipeline {

  protected IOutput output = null;
  protected IInput input = null;

  protected static Pipeline instance;

  private Pipeline() {}

  public static synchronized Pipeline getInstance() {
    if (instance == null) {
      instance = new Pipeline();
    }
    return instance;
  }

  @Override
  public Pipeline run() {
    if (output == null || input == null) {
      throw new UnsupportedOperationException("Output or Input not set.");
    }

    // verbalized axioms
    final DocumentPlanner documentPlanner = new DocumentPlanner(input, output);
    documentPlanner.build();
    documentPlanner.results();
    return this;
  }

  @Override
  public Pipeline setInput(final IInput input) {
    this.input = input;
    return this;
  }

  @Override
  public Pipeline setOutput(final IOutput output) {
    this.output = output;
    return this;
  }

  public IOutput getOutput() {
    return output;
  }
}
