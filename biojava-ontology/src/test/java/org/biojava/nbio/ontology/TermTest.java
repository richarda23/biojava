package org.biojava.nbio.ontology;

import org.biojava.nbio.ontology.utils.Annotation;
import org.biojava.nbio.ontology.utils.SmallAnnotation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TermTest {
    Ontology anyOntology;
    @BeforeEach
    void before() {
        anyOntology = new Ontology.Impl("ontoname", "ontodesc");
    }

    @Test
    void equalsHashcode() throws AlreadyExistsException {
        Ontology.Impl ontology = new Ontology.Impl("ontoname", "ontodesc");
        Ontology.Impl ontology2 = new Ontology.Impl("ontoname", "ontodesc");
        Term subject = new Term.Impl(ontology, "gene");
        Term subject2 = new Term.Impl(ontology, "gene");
        Term otherOntoSubject = new Term.Impl(ontology2, "gene");
        assertEquals(subject, subject2);
        assertEquals(subject.hashCode(), subject2.hashCode());

        assertEquals(subject2, subject);
        assertEquals(subject2.hashCode(), subject.hashCode());

        // non-equal objects can have the same hashcode (the same term in a different ontology).
        // This is allowed but not ideal as it increases chance of hash-collisions
        assertNotEquals(otherOntoSubject, subject);
    }

    @Test
    void noDuplicateTerms() throws AlreadyExistsException {
        Term t1 = anyOntology.createTerm("t1");
        assertThrows(AlreadyExistsException.class, ()->anyOntology.createTerm("t1"));
    }

    @Test
    void noDuplicateVariables() throws AlreadyExistsException {
        Variable v1 = anyOntology.createVariable("v1", "v1desc");
        assertThrows(AlreadyExistsException.class, ()->anyOntology.createVariable("v1", "v1Desc"));
    }

    // no way to set annotation through Term interface
    @Test
    void annotationIsMutable() throws AlreadyExistsException {
        Term.Impl t1 = new Term.Impl(anyOntology, "t1");
        Annotation ann = new SmallAnnotation();
        ann.setProperty("createdBy", "bob");
        t1.setAnnotation(ann);

        ann.removeProperty("createdBy");
        assertFalse(t1.getAnnotation().containsProperty("createdBy"));
    }


}