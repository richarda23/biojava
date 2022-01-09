package org.biojava.nbio.ontology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TripleTest {

    Ontology anyOntology;
    @BeforeEach
    void before() {
        anyOntology = new Ontology.Impl("ontoname", "ontodesc");
    }

    @Test
    void equalsHashCode () throws AlreadyExistsException {

        Term subject = anyOntology.createTerm("s");
        Term subject2 = anyOntology.createTerm("s2");
        Term predicate = anyOntology.createTerm("p");
        Term object = anyOntology.createTerm("object");


        Triple triple = new Triple.Impl(subject, object, predicate, "triple", "desc");
        Triple tripleCopy = new Triple.Impl(subject, object, predicate, "triple", "desc");
        Triple other = new Triple.Impl(subject2, object, predicate, "triple", "desc");

        assertEquals(triple, tripleCopy);
        assertEquals(triple.hashCode(), tripleCopy.hashCode());      assertEquals(triple, tripleCopy);
        assertNotEquals(triple.hashCode(), other.hashCode());
        assertNotEquals(triple, other);

        assertEquals(object, triple.getObject());
        assertEquals(predicate, triple.getPredicate());
    }

}