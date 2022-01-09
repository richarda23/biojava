package org.biojava.nbio.ontology.io;

import org.biojava.nbio.ontology.*;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TabDelimParserTest {

    BufferedReader getOntoTermsReader(){
        InputStream inStream = TabDelimParserTest.class.getResourceAsStream("/ontology/core.onto");
        return new BufferedReader( new InputStreamReader(inStream));
    }
    @Test
    void parseTDTriples() throws OntologyException, IOException {
        BufferedReader reader = getOntoTermsReader();
        OntologyFactory ontologyFactory = OntoTools.getDefaultFactory();
        TabDelimParser parser = new TabDelimParser();
        Ontology coreTerms = parser.parse(reader, ontologyFactory);
        assertEquals(66, coreTerms.getTerms().size());

        Term subject = coreTerms.getTerm("reflexive");
        Term relation = coreTerms.getTerm("is-a");
        Term object = coreTerms.getTerm("relation");

        assertTrue(coreTerms.containsTriple(subject, relation, object));


    }

}