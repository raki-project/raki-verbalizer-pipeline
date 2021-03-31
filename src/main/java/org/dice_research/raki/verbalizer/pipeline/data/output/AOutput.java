package org.dice_research.raki.verbalizer.pipeline.data.output;

import java.util.Map;

import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Pre-implements the {@link #IOutput} interface by throwing a UnsupportedOperationException
 * exception for each method.
 *
 * @author Rene Speck
 *
 */
public abstract class AOutput implements IOutput {

  @Override
  public Object write(final byte[] bytes) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object write(final Object object) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object write(final Map<OWLAxiom, String> verb) {
    throw new UnsupportedOperationException();
  }

  // public Object write(final Map<OWLAxiom, SimpleEntry<String, String>> verb) {
  // throw new UnsupportedOperationException();
  // }
}