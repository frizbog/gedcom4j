/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package org.gedcom4j.relationship;

import static org.gedcom4j.relationship.RelationshipName.AUNT;
import static org.gedcom4j.relationship.RelationshipName.FATHER;
import static org.gedcom4j.relationship.RelationshipName.GRANDFATHER;
import static org.gedcom4j.relationship.RelationshipName.GRANDSON;
import static org.gedcom4j.relationship.RelationshipName.GREAT_GRANDDAUGHTER;
import static org.gedcom4j.relationship.RelationshipName.GREAT_GRANDFATHER;
import static org.gedcom4j.relationship.RelationshipName.GREAT_GREAT_GRANDFATHER;
import static org.gedcom4j.relationship.RelationshipName.GREAT_GREAT_GRANDMOTHER;
import static org.gedcom4j.relationship.RelationshipName.GREAT_GREAT_GRANDSON;
import static org.gedcom4j.relationship.RelationshipName.GREAT_GREAT_GREAT_GRANDFATHER;
import static org.gedcom4j.relationship.RelationshipName.GREAT_GREAT_GREAT_GRANDSON;
import static org.gedcom4j.relationship.RelationshipName.SISTER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.InMemoryGedcom;
import org.gedcom4j.model.IGedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.query.Finder;
import org.junit.Before;
import org.junit.Test;

/**
 * @author frizbog1
 */
public class RelationshipCalculatorTest {

    /**
     * Determines whether to write noise out to System.out. Useful to change to true temporarily for debugging this test but should
     * be always set to false when checked into repository.
     */
    private static final boolean VERBOSE = false;

    /**
     * A finder test fixture for the test
     */
    private Finder finder;

    /**
     * {@link RelationshipCalculator} test fixture
     */
    private final RelationshipCalculator rc = new RelationshipCalculator();

    /**
     * Set up test fixtures
     * 
     * @throws IOException
     *             if the gedcom file can't be read
     * @throws GedcomParserException
     *             if the gedcom can't be parsed
     */
    @Before
    public void setUp() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser(new InMemoryGedcom());
        gp.load("sample/RelationshipTest.ged");
        assertTrue(gp.getErrors().isEmpty());
        assertTrue(gp.getWarnings().isEmpty());

        final IGedcom g = gp.getGedcom();
        assertNotNull(g);
        assertEquals("There are supposed to be 43 people in the gedcom - are you using the right file/file version?", 43, g
                .getIndividuals().size());
        assertEquals("There are supposed to be 18 families in the gedcom - are you using the right file/file version?", 18, g
                .getFamilies().size());
        finder = new Finder(g);
    }

    /**
     * Test for {@link RelationshipCalculator}, for aunts/uncles
     */
    @Test
    @SuppressWarnings("PMD.SystemPrintln")
    public void testAuntsUncles() {
        Individual alex = getPerson("Zucco", "Alex");
        Individual theresa = getPerson("Andrews", "Theresa");

        rc.calculateRelationships(alex, theresa, true);
        assertNotNull(rc.getRelationshipsFound());
        if (VERBOSE) {
            System.out.println("Relationships between " + alex + " and " + theresa);
            for (Relationship r : rc.getRelationshipsFound()) {
                System.out.println("   " + r);
            }
        }
        assertEquals("Theres is alex's aunt, one relationship", 1, rc.getRelationshipsFound().size());
        Relationship r = rc.getRelationshipsFound().get(0);
        assertEquals(alex, r.getIndividual1());
        assertEquals(theresa, r.getIndividual2());
        assertNotNull(r.getChain());
        assertEquals("The relationship should be one item long", 1, r.getChain().size());
        assertEquals("The relationship should be an aunt/nephew one", AUNT, r.getChain().get(0).getName());

    }

    /**
     * Test method for
     * {@link RelationshipCalculator#calculateRelationships(org.gedcom4j.model.Individual, org.gedcom4j.model.Individual, boolean)}
     * .
     */
    @Test
    @SuppressWarnings("PMD.SystemPrintln")
    public void testCalculateRelationshipGrandparents() {
        Individual robert = getPerson("Andrews", "Robert");
        Individual steven = getPerson("Struthers", "Steven");

        rc.calculateRelationships(robert, steven, true);
        assertNotNull(rc.getRelationshipsFound());
        if (VERBOSE) {
            System.out.println("Relationships between " + robert + " and " + steven);
            for (Relationship r1 : rc.getRelationshipsFound()) {
                System.out.println("   " + r1);
            }
        }
        assertEquals("Steven is Robert's grandfather - there should be one relationship", 1, rc.getRelationshipsFound().size());
        Relationship r = rc.getRelationshipsFound().get(0);
        assertEquals(robert, r.getIndividual1());
        assertEquals(steven, r.getIndividual2());
        assertNotNull(r.getChain());
        assertEquals("The relationship length should be one hop long after collapsing", 1, r.getChain().size());
        assertEquals("The relationship should be an grandfather/grandson one", GRANDFATHER, r.getChain().get(0).getName());

        rc.calculateRelationships(steven, robert, true);
        assertNotNull(rc.getRelationshipsFound());
        if (VERBOSE) {
            System.out.println("Relationships between " + robert + " and " + steven);
            for (Relationship r1 : rc.getRelationshipsFound()) {
                System.out.println("   " + r1);
            }
        }
        assertEquals("Steven is Robert's grandfather - there should be one relationship", 1, rc.getRelationshipsFound().size());
        r = rc.getRelationshipsFound().get(0);
        assertEquals(steven, r.getIndividual1());
        assertEquals(robert, r.getIndividual2());
        assertNotNull(r.getChain());
        assertEquals("The relationship length should be one hop long after collapsing", 1, r.getChain().size());
        assertEquals("The relationship should be an grandfather/grandson one", GRANDSON, r.getChain().get(0).getName());

    }

    /**
     * Test method for
     * {@link RelationshipCalculator#calculateRelationships(org.gedcom4j.model.Individual, org.gedcom4j.model.Individual, boolean)}
     * .
     */
    @Test
    @SuppressWarnings("PMD.SystemPrintln")
    public void testCalculateRelationshipGreatGrandparents() {
        Individual nancy = getPerson("Andrews", "Nancy");
        Individual steven = getPerson("Struthers", "Steven");

        rc.calculateRelationships(nancy, steven, true);
        assertNotNull(rc.getRelationshipsFound());
        if (VERBOSE) {
            System.out.println("Relationships between " + nancy + " and " + steven);
            for (Relationship r : rc.getRelationshipsFound()) {
                System.out.println("   " + r);
            }
        }
        assertEquals("Steven is Nancy's great-grandfather - there should be one relationship", 1, rc.getRelationshipsFound()
                .size());
        Relationship r = rc.getRelationshipsFound().get(0);
        assertEquals(nancy, r.getIndividual1());
        assertEquals(steven, r.getIndividual2());
        assertNotNull(r.getChain());
        assertEquals("The relationship length should be two hops long after collapsing", 1, r.getChain().size());
        assertEquals("The relationship should be a great-grandfather/great-granddaughter one", GREAT_GRANDFATHER, r.getChain().get(
                0).getName());

        rc.calculateRelationships(steven, nancy, true);
        assertNotNull(rc.getRelationshipsFound());
        if (VERBOSE) {
            System.out.println("Relationships between " + nancy + " and " + steven);
            for (Relationship r1 : rc.getRelationshipsFound()) {
                System.out.println("   " + r1);
            }
        }
        assertEquals("Steven is Nancy's great-grandfather - there should be one relationship", 1, rc.getRelationshipsFound()
                .size());
        r = rc.getRelationshipsFound().get(0);
        assertEquals(steven, r.getIndividual1());
        assertEquals(nancy, r.getIndividual2());
        assertNotNull(r.getChain());
        assertEquals("The relationship length should be 1 hop long after collapsing", 1, r.getChain().size());
        assertEquals("The relationship should be a great-grandfather/great-granddaughter one", GREAT_GRANDDAUGHTER, r.getChain()
                .get(0).getName());
    }

    /**
     * Test method for
     * {@link RelationshipCalculator#calculateRelationships(org.gedcom4j.model.Individual, org.gedcom4j.model.Individual, boolean)}
     * .
     */
    @Test
    @SuppressWarnings("PMD.SystemPrintln")
    public void testCalculateRelationshipGreatGreatGrandparents() {
        Individual alex = getPerson("Zucco", "Alex");
        Individual steven = getPerson("Struthers", "Steven");

        rc.calculateRelationships(alex, steven, true);
        assertNotNull(rc.getRelationshipsFound());
        if (VERBOSE) {
            System.out.println("Relationships between " + alex + " and " + steven);
            for (Relationship r : rc.getRelationshipsFound()) {
                System.out.println("   " + r);
            }
        }
        assertEquals("Steven is Alex's great-great-grandfather - there should be one relationship", 1, rc.getRelationshipsFound()
                .size());
        Relationship r = rc.getRelationshipsFound().get(0);
        assertEquals(alex, r.getIndividual1());
        assertEquals(steven, r.getIndividual2());
        assertNotNull(r.getChain());
        assertEquals("The relationship length should be two hops long after collapsing", 1, r.getChain().size());
        assertEquals("The relationship should be a great-great-grandfather/great-great-grandfather one", GREAT_GREAT_GRANDFATHER, r
                .getChain().get(0).getName());

        rc.calculateRelationships(steven, alex, true);
        assertNotNull(rc.getRelationshipsFound());
        if (VERBOSE) {
            System.out.println("Relationships between " + alex + " and " + steven);
            for (Relationship r1 : rc.getRelationshipsFound()) {
                System.out.println("   " + r1);
            }
        }
        assertEquals("Steven is Alex's great-great-grandfather - there should be one relationship", 1, rc.getRelationshipsFound()
                .size());
        r = rc.getRelationshipsFound().get(0);
        assertEquals(steven, r.getIndividual1());
        assertEquals(alex, r.getIndividual2());
        assertNotNull(r.getChain());
        assertEquals("The relationship length should be one hop long after collapsing", 1, r.getChain().size());
        assertEquals("The relationship should be a great-great-grandfather/great-great-grandfather one", GREAT_GREAT_GRANDSON, r
                .getChain().get(0).getName());
    }

    /**
     * Test method for
     * {@link RelationshipCalculator#calculateRelationships(org.gedcom4j.model.Individual, org.gedcom4j.model.Individual, boolean)}
     * .
     */
    @Test
    @SuppressWarnings("PMD.SystemPrintln")
    public void testCalculateRelationshipGreatGreatGreatGrandparents() {
        Individual alex = getPerson("Zucco", "Alex");
        Individual kenneth = getPerson("Struthers", "Kenneth");

        rc.calculateRelationships(alex, kenneth, true);
        assertNotNull(rc.getRelationshipsFound());
        if (VERBOSE) {
            System.out.println("Relationships between " + alex + " and " + kenneth);
            for (Relationship r : rc.getRelationshipsFound()) {
                System.out.println("   " + r);
            }
        }
        assertEquals("Kenneth is Alex's great-great-great-grandfather - there should be one relationship", 1, rc
                .getRelationshipsFound().size());
        Relationship r = rc.getRelationshipsFound().get(0);
        assertEquals(alex, r.getIndividual1());
        assertEquals(kenneth, r.getIndividual2());
        assertNotNull(r.getChain());
        assertEquals("The relationship length should be two hops long after collapsing", 1, r.getChain().size());
        assertEquals("The relationship should be a great-great-great-grandfather/great-great-great-grandfather one",
                GREAT_GREAT_GREAT_GRANDFATHER, r.getChain().get(0).getName());

        rc.calculateRelationships(kenneth, alex, true);
        assertNotNull(rc.getRelationshipsFound());
        if (VERBOSE) {
            System.out.println("Relationships between " + alex + " and " + kenneth);
            for (Relationship r1 : rc.getRelationshipsFound()) {
                System.out.println("   " + r1);
            }
        }
        assertEquals("Kenneth is Alex's great-great-great-grandfather - there should be one relationship", 1, rc
                .getRelationshipsFound().size());
        r = rc.getRelationshipsFound().get(0);
        assertEquals(kenneth, r.getIndividual1());
        assertEquals(alex, r.getIndividual2());
        assertNotNull(r.getChain());
        assertEquals("The relationship length should be 1 hops long after collapsing", 1, r.getChain().size());
        assertEquals("The relationship should be a great-great-great-grandfather/great-great-great-grandfather one",
                GREAT_GREAT_GREAT_GRANDSON, r.getChain().get(0).getName());
    }

    /**
     * Test method for
     * {@link RelationshipCalculator#calculateRelationships(org.gedcom4j.model.Individual, org.gedcom4j.model.Individual, boolean)}
     * .
     */
    @Test
    @SuppressWarnings("PMD.SystemPrintln")
    public void testCalculateRelationshipManyGenerations() {
        Individual alex = getPerson("Zucco", "Alex");
        Individual abigail = getPerson("Wood", "Abigail");

        rc.calculateRelationships(alex, abigail, true);
        assertNotNull(rc.getRelationshipsFound());
        if (VERBOSE) {
            System.out.println("Relationships between " + alex + " and " + abigail);
            for (Relationship r : rc.getRelationshipsFound()) {
                System.out.println("   " + r);
            }
        }
        assertEquals("Abigail is Alex's great-great-grandmother - there are several ways to get there but one relationship", 1, rc
                .getRelationshipsFound().size());
        Relationship r = rc.getRelationshipsFound().get(0);
        assertEquals(alex, r.getIndividual1());
        assertEquals(abigail, r.getIndividual2());
        assertNotNull(r.getChain());
        assertEquals("The relationship length should be four hops long (after collapsing)", 1, r.getChain().size());
        assertEquals("The relationship should be an great-great-grandmother/great-great-grandson one", GREAT_GREAT_GRANDMOTHER, r
                .getChain().get(0).getName());

        rc.calculateRelationships(abigail, alex, true);
        assertNotNull(rc.getRelationshipsFound());
        if (VERBOSE) {
            System.out.println("Relationships between " + alex + " and " + abigail);
            for (Relationship r1 : rc.getRelationshipsFound()) {
                System.out.println("   " + r1);
            }
        }
        assertEquals("Abigail is Alex's great-great-grandmother - there are several ways to get there but one relationship", 1, rc
                .getRelationshipsFound().size());
        r = rc.getRelationshipsFound().get(0);
        assertEquals(abigail, r.getIndividual1());
        assertEquals(alex, r.getIndividual2());
        assertNotNull(r.getChain());
        assertEquals("The relationship length should be one hops long (after collapsing)", 1, r.getChain().size());
        assertEquals("The relationship should be an great-great-grandmother/great-great-grandson one", GREAT_GREAT_GRANDSON, r
                .getChain().get(0).getName());

    }

    /**
     * Test method for
     * {@link RelationshipCalculator#calculateRelationships(org.gedcom4j.model.Individual, org.gedcom4j.model.Individual, boolean)}
     * .
     */
    @Test
    @SuppressWarnings("PMD.SystemPrintln")
    public void testCalculateRelationshipSelf() {
        Individual alex = getPerson("Zucco", "Alex");

        rc.calculateRelationships(alex, alex, true);
        assertNotNull(rc.getRelationshipsFound());
        if (VERBOSE) {
            System.out.println("Relationships between " + alex + " and " + alex);
            for (Relationship r : rc.getRelationshipsFound()) {
                System.out.println("   " + r);
            }
        }
        assertEquals("There are no relationships to oneself", 0, rc.getRelationshipsFound().size());

    }

    /**
     * Test method for
     * {@link RelationshipCalculator#calculateRelationships(org.gedcom4j.model.Individual, org.gedcom4j.model.Individual, boolean)}
     * .
     */
    @Test
    @SuppressWarnings("PMD.SystemPrintln")
    public void testCalculateRelationshipSiblings() {
        Individual alex = getPerson("Zucco", "Alex");
        Individual betsy = getPerson("Zucco", "Betsy");

        rc.calculateRelationships(alex, betsy, true);
        assertNotNull(rc.getRelationshipsFound());
        if (VERBOSE) {
            System.out.println("Relationships between " + alex + " and " + betsy);
            for (Relationship r : rc.getRelationshipsFound()) {
                System.out.println("   " + r);
            }
        }
        assertEquals("Betsy is Alex's sister", 1, rc.getRelationshipsFound().size());
        Relationship r = rc.getRelationshipsFound().get(0);
        assertEquals(alex, r.getIndividual1());
        assertEquals(betsy, r.getIndividual2());
        assertNotNull(r.getChain());
        assertEquals("The relationship length should be one hops long after collapsing", 1, r.getChain().size());
        assertEquals("The relationship should be an sister/brother one", SISTER, r.getChain().get(0).getName());

    }

    /**
     * Test method for
     * {@link RelationshipCalculator#calculateRelationships(org.gedcom4j.model.Individual, org.gedcom4j.model.Individual, boolean)}
     * .
     */
    @Test
    @SuppressWarnings("PMD.SystemPrintln")
    public void testCalculateRelationshipSonFather() {
        Individual james = getPerson("Andrews", "James");
        Individual robert = getPerson("Andrews", "Robert");

        rc.calculateRelationships(robert, james, true);
        assertNotNull(rc.getRelationshipsFound());
        if (VERBOSE) {
            System.out.println("Relationships between " + robert + " and " + james);
            for (Relationship r : rc.getRelationshipsFound()) {
                System.out.println("   " + r);
            }
        }
        assertEquals("James is robert's father - there should be one relationship", 1, rc.getRelationshipsFound().size());
        Relationship r = rc.getRelationshipsFound().get(0);
        assertEquals(robert, r.getIndividual1());
        assertEquals(james, r.getIndividual2());
        assertNotNull(r.getChain());
        assertEquals("The relationship length should be one hop long", 1, r.getChain().size());
        assertEquals("The relationship should be an father/son one", FATHER, r.getChain().get(0).getName());
    }

    /**
     * Helper method to get a person and assert they exist
     * 
     * @param surname
     *            the surname of the person we want
     * @param givenName
     *            the given name of the person we want
     * @return the person
     */
    private Individual getPerson(String surname, String givenName) {
        Individual result = finder.findByName(surname, givenName).get(0);
        assertNotNull("Couldn't find " + givenName + " " + surname + " by name in the gedcom", result);
        return result;
    }
}
