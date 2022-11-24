package org.dice_research.raki.verbalizer.pipeline;

import org.dice_research.raki.verbalizer.pipeline.data.input.IRAKIInput;
import org.dice_research.raki.verbalizer.pipeline.data.output.IOutput;
import org.dice_research.raki.verbalizer.pipeline.planner.DocumentPlanner;

public class Pipeline implements IVerbalizerPipeline {

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

    final DocumentPlanner documentPlanner = new DocumentPlanner(input, output);
    documentPlanner.build();
    documentPlanner.results();
    return this;
  }

  @Override
  public IOutput<?> getOutput() {
    return output;
  }
}
