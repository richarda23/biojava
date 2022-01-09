package org.biojava.nbio.ontology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OntologyTest {

    Ontology anyOntology;
    @BeforeEach
    void before() {
        anyOntology = new Ontology.Impl("ontoname", "ontodesc");
    }

    @Test
    void searchingTriple() throws AlreadyExistsException {
        Triple triple = createAnyTriple("s","o","p");
        assertTrue(anyOntology.containsTerm(triple.getSubject().getName()));
        assertTrue(anyOntology.containsTerm(triple.getObject().getName()));
        assertTrue(anyOntology.containsTerm(triple.getPredicate().getName()));

        assertTrue(anyOntology.containsTriple(triple.getSubject(), triple.getObject(), triple.getPredicate() ));
    }

    @Test
    void filterTriple() throws AlreadyExistsException {
        Triple triple = createAnyTriple("s","o","p");
        Triple triple2= anyOntology.createTriple(triple.getSubject(),
                anyOntology.createTerm("o2"), anyOntology.createTerm("p2"),null, null);

        // can filter by any 1 or more term in a triple
        // both triples have the same subject
        assertEquals(2, anyOntology.getTriples(triple.getSubject(), null, null).size());
        assertEquals(1, anyOntology.getTriples(null, triple.getObject(), null).size());
        assertEquals(1, anyOntology.getTriples(null, null, triple.getPredicate()).size());
    }

    @Test
    void containsTriple() throws AlreadyExistsException {
        Triple triple = createAnyTriple("s", "o", "p");
        Triple triple2 = createAnyTriple("s2", "o2", "p2");
        assertTrue(anyOntology.containsTriple(triple.getSubject(), triple.getObject(), triple.getPredicate()));
        assertFalse(anyOntology.containsTriple(triple2.getSubject(), triple.getObject(), triple.getPredicate()));
    }

    private Triple createAnyTriple(String subjectName, String objectName, String predName) throws AlreadyExistsException {
        Term subject = anyOntology.createTerm(subjectName);
        Term pred = anyOntology.createTerm(predName);
        Term object = anyOntology.createTerm(objectName);
        Triple triple = anyOntology.createTriple(subject, object, pred, null, null);
        return triple;
    }

}