package org.dice_research.raki.verbalizer.pipeline.planner;

import java.util.Map;

import org.aksw.owl2nl.data.IInput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.raki.verbalizer.pipeline.data.output.IOutput;
import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * This class defines the template of a document and provides all information for the
 * SentencePlanner {@link sentencePlanner}. This class decides about the information and the order
 * of the information to be verbalized.
 *
 * @author Rene Speck
 *
 */
public class DocumentPlanner implements IPlanner<String> {

  protected static final Logger LOG = LogManager.getLogger(DocumentPlanner.class);

  protected SentencePlanner sentencePlanner = null;
  protected IOutput<?> output = null;
  protected IInput input = null;

  public DocumentPlanner(final IInput input, final IOutput<?> output) {
    this.output = output;
    this.input = input;

    sentencePlanner = new SentencePlanner(input);
  }

  @Override
  public IPlanner<String> build() {
    sentencePlanner.build();
    return this;
  }

  @Override
  public String results() {
    // final Map<OWLAxiom, SimpleEntry<String, String>> map = new HashMap<>();

    final Map<OWLAxiom, String> resutls = sentencePlanner.results();
    /**
     * <code>
    for (final Entry<OWLAxiom, String> result : resutls.entrySet()) {
      LOG.info(result);

       map.put(result.getKey(), //
           new SimpleEntry<>(//
               input.getAxiomsMap().get(result.getKey()), //
               // input.getAxiomsMap().get(result.getKey()), //
               result.getValue()//
           ));

    } </code>
     **/
    // write verbalized axioms to file success
    final Object success = output.write(resutls);
    if (success == null) {
      LOG.error("Couldn't write results.");
    }
    return "";
  }
}
