package org.dice_research.raki.verbalizer.pipeline.data.output;

import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;

import org.dice_research.raki.verbalizer.pipeline.io.RakiIO;
import org.dllearner.utilities.owl.ManchesterOWLSyntaxOWLObjectRendererImplExt;
import org.json.JSONArray;
import org.json.JSONObject;
import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;

/**
 *
 * @author Rene Speck
 *
 */
public class OutputJsonTrainingData extends AOutput {

  private final OWLObjectRenderer manchesterRenderer =
      new ManchesterOWLSyntaxOWLObjectRendererImplExt();
  private final OWLObjectRenderer dlRenderer = new DLSyntaxObjectRenderer();

  protected JSONArray data = null;
  Path file;

  /**
   *
   * @param file
   */
  public OutputJsonTrainingData(final Path file) {
    this.file = file;
  }

  @Override
  public Object write(final Map<OWLAxiom, String> verb) {
    data = new JSONArray();

    int id = 0;
    for (final Entry<OWLAxiom, String> entry : verb.entrySet()) {

      final OWLAxiom axiom = entry.getKey();
      final String nl = entry.getValue();

      final JSONObject o = new JSONObject();
      final JSONArray axioms = new JSONArray();
      axioms.put(manchesterRenderer.render(axiom));
      for (final OWLClassExpression e : axiom.getNestedClassExpressions()) {
        axioms.put(manchesterRenderer.render(e));
      }
      o.put("manchester", axioms);
      o.put("owl", axiom.toString());
      o.put("nl", nl == null ? "" : nl);
      o.put("id", id++);
      o.put("dl", dlRenderer.render(axiom));

      data.put(o);
    }

    return RakiIO.write(file, data.toString(2).getBytes());
  }

  /**
   * <code>
  
   public Object write(final Map<OWLAxiom, SimpleEntry<String, String>> verb) {
     data = new JSONArray();
  
     int id = 0;
     for (final Entry<OWLAxiom, SimpleEntry<String, String>> entry : verb.entrySet()) {
  
       final OWLAxiom axiom = entry.getKey();
       // final String dl = entry.getValue().getKey();
       final String nl = entry.getValue().getValue();
  
       final JSONObject o = new JSONObject();
       final JSONArray axioms = new JSONArray();
       axioms.put(manchesterRenderer.render(axiom));
       for (final OWLClassExpression e : axiom.getNestedClassExpressions()) {
         axioms.put(manchesterRenderer.render(e));
       }
       o.put("manchester", axioms);
       o.put("owl", axiom.toString());
       // o.put("dl", dl == null ? "" : dl);
       o.put("nl", nl == null ? "" : nl);
       o.put("id", id++);
       o.put("dl", dlRenderer.render(axiom));
  
       data.put(o);
     }
  
     return RakiIO.write(file, data.toString(2).getBytes());
   }
    </code>
   */
  public JSONArray getData() {
    return data;
  }
}
