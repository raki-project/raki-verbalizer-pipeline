package org.dice_research.raki.verbalizer.pipeline.data.output;

import java.util.Map;

import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Writes the given input to a file or terminal or ...
 *
 * @author Rene Speck
 *
 */
// TODO: use a templete instead of objects
public interface IOutput {

  /**
   * Writes the given input to a file or terminal or ...
   *
   * @param bytes
   * @return something in case if an error null
   */
  Object write(final byte[] bytes);

  /**
   * Writes the given input to a file or terminal or ...
   *
   * @param bytes
   * @return something in case if an error null
   */
  Object write(final Object object);

  /**
   * Writes the given input to a file or terminal or ...
   *
   * @param bytes
   * @return something in case if an error null
   */
  // Object write(Map<OWLAxiom, SimpleEntry<String, String>> verb);
  Object write(final Map<OWLAxiom, String> verb);

}
