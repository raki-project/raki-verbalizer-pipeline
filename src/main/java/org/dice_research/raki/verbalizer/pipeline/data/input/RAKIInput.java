package org.dice_research.raki.verbalizer.pipeline.data.input;

import java.io.File;
import java.nio.file.Path;
import java.util.Set;

import org.aksw.owl2nl.data.AInput;
import org.aksw.owl2nl.data.IInput;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.vocabulary.RDFS;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

abstract class AInputExtended extends AInput {
  Path ontologyFile;
  Path axiomsFile;

  @Override
  public IInput setOntologyIRI(final IRI ontology) {
    try {
      super.setOntologyIRI(ontology);

      final File file = new File(ontology.getShortForm());
      OWLManager.createOWLOntologyManager().saveOntology(owlOntology, IRI.create(file.toURI()));
      ontologyFile = file.toPath();
    } catch (final OWLOntologyStorageException e) {
      LOG.error(e.getLocalizedMessage(), e);
    }
    return this;
  }

  @Override
  public IInput setOntologyPath(final Path ontology) {
    ontologyFile = ontology;
    return super.setOntologyPath(ontology);
  }
}


/**
 *
 * @author rspeck
 *
 */
public class RAKIInput extends AInputExtended implements IRAKIInput {

  protected Set<OWLAxiom> axioms = null;

  protected Model tboxModel = null;

  protected Model axiomsModel = null;

  protected String getlabel(final IRI iri, final String lang, final Model model) {
    String label = null; // with lang
    String tmplabel = null; // without lang, 1st occurrence

    final Resource resource = model.getResource(iri.toString());

    if (resource != null && resource.hasProperty(RDFS.label)) {
      final NodeIterator ni = model.listObjectsOfProperty(resource, RDFS.label);
      while (ni.hasNext()) {
        final RDFNode n = ni.next();
        if (tmplabel == null) {
          tmplabel = n.asLiteral().getLexicalForm();
        }
        if (lang.equals(n.asLiteral().getLanguage())) {
          label = n.asLiteral().getLexicalForm();
          break;
        }
      } // end while
    } // end if
    return label == null ? tmplabel : label;
  }

  private void init() {
    if (axiomsFile != null) {
      axiomsModel = ModelFactory.createDefaultModel()//
          .read(axiomsFile.toFile().getPath(), Lang.RDFXML.getName());
      if (axiomsModel == null) {
        LOG.error("Could not read axioms.");
      }
    }
    if (ontologyFile != null) {
      tboxModel = ModelFactory.createDefaultModel()//
          .read(ontologyFile.toFile().getPath(), Lang.RDFXML.getName());

      if (tboxModel == null) {
        LOG.error("Could not read tbox.");
      }
    }
  }

  @Override
  public String getEnglishLabel(final IRI iri) {

    if (tboxModel == null) {
      init();
    }

    final boolean useLabelsInAxioms = true;
    final String lang = "en";
    String label = getlabel(iri, lang, tboxModel);
    if (useLabelsInAxioms && label == null) {
      label = getlabel(iri, lang, axiomsModel);
    }
    return label;
  }

  @Override
  public Set<OWLAxiom> getAxioms() {
    return axioms;
  }

  @Override
  public IRAKIInput setAxioms(final Path axiomsPath) {
    axiomsFile = axiomsPath;
    try {
      axioms = OWLManager//
          .createOWLOntologyManager()//
          .loadOntologyFromOntologyDocument(axiomsPath.toFile())//
          .getAxioms();
    } catch (final OWLOntologyCreationException e) {
      LOG.error(e.getLocalizedMessage(), e);
    }
    return this;
  }

  @Override
  public IRAKIInput setAxioms(final IRI axiomsIRI) {
    try {
      final OWLOntology axiomsOnto = OWLManager.createOWLOntologyManager().loadOntology(axiomsIRI);
      axioms = axiomsOnto.getAxioms();

      final File file = new File(axiomsIRI.getShortForm());
      OWLManager.createOWLOntologyManager().saveOntology(axiomsOnto, IRI.create(file.toURI()));
      axiomsFile = file.toPath();

    } catch (final OWLOntologyCreationException | OWLOntologyStorageException e) {
      LOG.error(e.getLocalizedMessage(), e);
    }
    return this;
  }
}
