package org.dice_research.raki.verbalizer.pipeline;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.raki.verbalizer.pipeline.data.input.IRAKIInput;
import org.dice_research.raki.verbalizer.pipeline.data.output.IOutput;

public interface IVerbalizerPipeline {

  Logger LOG = LogManager.getLogger(IVerbalizerPipeline.class);

  IVerbalizerPipeline setInput(final IRAKIInput input);

  IVerbalizerPipeline setOutput(final IOutput<?> output);

  IVerbalizerPipeline run();

}
