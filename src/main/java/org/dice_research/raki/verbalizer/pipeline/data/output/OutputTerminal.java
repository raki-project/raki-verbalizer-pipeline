package org.dice_research.raki.verbalizer.pipeline.data.output;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.OWLAxiom;

/**
 *
 * @author Rene Speck
 *
 */
public class OutputTerminal extends AOutput {

  protected static final Logger LOG = LogManager.getLogger(OutputTerminal.class);

  /**
   * Prints bytes to the console and returns true.
   */
  @Override
  public Object write(final Map<OWLAxiom, String> verb) {

    final StringBuilder sb = new StringBuilder();

    for (final Entry<OWLAxiom, String> entry : verb.entrySet()) {
      sb.append(entry.getKey()).append(" ").append(entry.getValue()).append(System.lineSeparator());

    }
    LOG.info(sb);
    return true;
  }
}
