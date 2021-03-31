package org.dice_research.raki.verbalizer.pipeline.ui;

import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.raki.verbalizer.pipeline.Pipeline;
import org.dice_research.raki.verbalizer.pipeline.data.input.RAKIInput;
import org.dice_research.raki.verbalizer.pipeline.data.output.OutputJsonTrainingData;

import gnu.getopt.Getopt;
import simplenlg.lexicon.Lexicon;

public class RAKICommandLineInterface {
  protected static final Logger LOG = LogManager.getLogger(RAKICommandLineInterface.class);

  public static void main(final String[] args) {
    LOG.info("\n==============================\nParsing arguments...");
    String axioms = null;
    String output = null;
    String ontology = null;

    final Getopt g = new Getopt("Verbalizer Pipeline", args, "a:x o:x s:x");
    int c;
    while ((c = g.getopt()) != -1) {
      switch (c) {
        case 'a':
          axioms = String.valueOf(g.getOptarg());
          break;
        case 'o':
          ontology = String.valueOf(g.getOptarg());
          break;
        case 's':
          output = String.valueOf(g.getOptarg());
          break;
        default:
          LOG.info("getopt() returned " + c + "\n");
      }
    }

    LOG.info("\n==============================\nRunning Pipeline ...");
    try {
      final RAKIInput in = new RAKIInput();
      in//
          .setAxioms(Paths.get(axioms))//
          .setOntologyPath(Paths.get(ontology))//
          .setLexicon(Lexicon.getDefaultLexicon());

      Pipeline//
          .getInstance()//
          .setInput(in)//
          .setOutput(new OutputJsonTrainingData(Paths.get(output)))//
          // .setOutput(new OutputTerminal())//
          .run();
    } catch (final Exception e) {
      LOG.error(e.getLocalizedMessage(), e);
    }
    LOG.info("\n==============================\nPipeline exit.");
  }
}
