package org.biojava.nbio.ontology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

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
    @DisplayName("getTerms is encapsulated")
    void getTerms() throws AlreadyExistsException {
        Triple triple = createAnyTriple("s","o","p");
        // 3 terms + 1 triple
        Set<Term> terms = anyOntology.getTerms();
        assertEquals(4, terms.size());

        terms.remove(triple.getSubject());
        assertEquals(3, terms.size());
        // still 4 terms in the ontology
        assertEquals(4, anyOntology.getTerms().size());



    }

    @Test
    void containsTriple() throws AlreadyExistsException {
        Triple triple = createAnyTriple("s", "o", "p");
        Triple triple2 = createAnyTriple("s2", "o2", "p2");
        assertTrue(anyOntology.containsTriple(triple.getSubject(), triple.getObject(), triple.getPredicate()));
        assertFalse(anyOntology.containsTriple(triple2.getSubject(), triple.getObject(), triple.getPredicate()));
    }

    @Test
    void deleteTerm() throws AlreadyExistsException {
        Term t = anyOntology.createTerm("t1");
        assertTrue(anyOntology.containsTerm(t.getName()));
        anyOntology.deleteTerm(t);
        assertFalse(anyOntology.containsTerm(t.getName()));
    }

    @Test
    @DisplayName("Delete triple keeps terms")
    void deleteTriple() throws AlreadyExistsException {
        Triple triple = createAnyTriple("s1" ,"o1", "p1");
        assertTrue(anyOntology.containsTerm(triple.getName()));
        anyOntology.deleteTerm(triple);
        assertFalse(anyOntology.containsTerm(triple.getName()));

        assertTrue(anyOntology.containsTerm(triple.getSubject().getName()));
        assertTrue(anyOntology.containsTerm(triple.getObject().getName()));
        assertTrue(anyOntology.containsTerm(triple.getPredicate().getName()));
    }

    @Test
    @DisplayName("Deleting term keeps triple with that term")
    void deleteTermFromTriple() throws AlreadyExistsException {
        Triple triple = createAnyTriple("s1" ,"o1", "p1");
        anyOntology.deleteTerm(triple.getSubject());
        assertTrue(anyOntology.containsTerm(triple.getName()));
        assertFalse(anyOntology.containsTerm(triple.getSubject().getName()));
        // we just deleted this subject term but triples containing the subject are still present
        // is this correct?
        assertEquals(1, anyOntology.getTriples(triple.getSubject(),null, null).size());
    }

    @Test
    @DisplayName("Synonyms are encapsulated in terms")
    void createTermWithSynonyms() throws AlreadyExistsException {
        String [] synonyms = new String[]{"alias1", "alias2"};
        Term t = anyOntology.createTerm("s1", "desc", synonyms);
        assertEquals("alias2", t.getSynonyms()[1]);
        synonyms[1] = "XXXX";
        assertEquals("alias2", t.getSynonyms()[1]);

    }

    private Triple createAnyTriple(String subjectName, String objectName, String predName) throws AlreadyExistsException {
        Term subject = anyOntology.createTerm(subjectName);
        Term pred = anyOntology.createTerm(predName);
        Term object = anyOntology.createTerm(objectName);
        Triple triple = anyOntology.createTriple(subject, object, pred, null, null);
        return triple;
    }

}