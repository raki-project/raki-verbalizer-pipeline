package org.dice_research.raki.verbalizer.pipeline.examples;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.raki.verbalizer.pipeline.Pipeline;
import org.dice_research.raki.verbalizer.pipeline.data.input.RAKIInput;
import org.dice_research.raki.verbalizer.pipeline.data.output.IOutput;
import org.dice_research.raki.verbalizer.pipeline.data.output.OutputJavaObjects;
import org.dice_research.raki.verbalizer.pipeline.data.output.OutputJsonTrainingData;
import org.dice_research.raki.verbalizer.pipeline.ui.RAKICommandLineInterface;
import org.json.JSONArray;
import org.semanticweb.owlapi.model.OWLAxiom;

import simplenlg.lexicon.Lexicon;

public class Example {

  protected static final Logger LOG = LogManager.getLogger(RAKICommandLineInterface.class);

  public static void main(final String[] args) {
    exampleA();
    // exampleB();
  }

  /**
   *
   */
  public static void exampleA() {
    final String ontology = "koala.owl";
    final String axioms = "koala.owl";
    final String output = "out.txt";

    final RAKIInput in = new RAKIInput();
    in//
        .setAxioms(Paths.get(axioms))//
        .setOntology(Paths.get(ontology))//
        .setLexicon(Lexicon.getDefaultLexicon());

    final IOutput<JSONArray> out = new OutputJsonTrainingData(Paths.get(output));
    // final IOutput out = new OutputTerminal();
    Pipeline.getInstance().setInput(in).setOutput(out).run();//
    // .getOutput()//
    // .getResults();

    out.getResults().forEach(LOG::info);
  }

  /**
   *
   */
  public static void exampleB() {

    final RAKIInput in = new RAKIInput();
    {
      final Path axioms = Paths.get("koala.owl");
      in.setAxioms(axioms);
      in.setOntology(axioms);
    }

    final IOutput<Map<OWLAxiom, String>> out = new OutputJavaObjects();
    Pipeline.getInstance().setInput(in).setOutput(out).run();//
    // .getOutput()//
    // .getResults();

    out.getResults().entrySet().forEach(LOG::info);
  }
}
