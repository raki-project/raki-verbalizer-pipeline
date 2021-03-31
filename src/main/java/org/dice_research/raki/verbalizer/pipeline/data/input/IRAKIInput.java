package org.dice_research.raki.verbalizer.pipeline.data.input;

import java.nio.file.Path;

import org.aksw.owl2nl.data.IInput;
import org.semanticweb.owlapi.model.IRI;

public interface IRAKIInput extends IInput {

  IRAKIInput setAxioms(Path axioms);

  IRAKIInput setAxioms(IRI axioms);
}
