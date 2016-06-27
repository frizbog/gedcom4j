/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.gedcom4j.relationship;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Set;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.query.Finder;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link AncestryCalculator} class
 * 
 * @author frizbog1
 */
public class AncestryCalculatorTest {

    /**
     * The gedcom to work with for testing
     */
    private Gedcom g;

    /**
     * A finder test fixture for the test
     */
    private Finder finder;

    /**
     * Ancestry calculator test fixture
     */
    private final AncestryCalculator anc = new AncestryCalculator();

    /**
     * Determines whether to write noise out to System.out. Useful to change to true temporarily for debugging this test
     * but should be always set to false when checked into repository.
     */
    private static final boolean VERBOSE = false;

    /**
     * Set up test fixtures
     * 
     * @throws IOException
     *             if the gedcom can't be read
     * @throws GedcomParserException
     *             if the gedcom can't be parsed
     */
    @Before
    public void setup() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();

        gp.load("sample/RelationshipTest.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue(gp.warnings.isEmpty());
        g = gp.gedcom;
        assertNotNull(g);
        assertEquals("There are supposed to be 43 people in the gedcom - are you using the right file/file version?",
                43, g.individuals.size());
        assertEquals("There are supposed to be 18 families in the gedcom - are you using the right file/file version?",
                18, g.families.size());
        finder = new Finder(g);
    }

    /**
     * Test the {@link AncestryCalculator#getExtendedAncestry(Individual)} method
     */
    @Test
    public void testExtendedAncestors1() {
        Individual alex = getPerson("Zucco", "Alex");
        Set<Individual> extendedAncestry = anc.getExtendedAncestry(alex);
        assertNotNull(extendedAncestry);
        assertEquals("Alex has 20 ancestors (including 1 step!) in the gedcom", 20, extendedAncestry.size());
    }

    /**
     * Test the {@link AncestryCalculator#getExtendedAncestry(Individual)} method
     */
    @Test
    public void testExtendedAncestors2() {
        Individual james = getPerson("Andrews", "James");
        Set<Individual> extendedAncestry = anc.getExtendedAncestry(james);
        assertNotNull(extendedAncestry);
        assertEquals("James Andrews has no ancestors in the gedcom", 0, extendedAncestry.size());
    }

    /**
     * Test the {@link AncestryCalculator#getExtendedAncestry(Individual)} method
     */
    @Test
    public void testExtendedAncestors3() {
        Individual sylvia = getPerson("Jackson", "Sylvia");
        Set<Individual> extendedAncestry = anc.getExtendedAncestry(sylvia);
        assertNotNull(extendedAncestry);
        assertEquals("Sylvia Jackson has 3 ancestors in the gedcom", 3, extendedAncestry.size());
    }

    /**
     * Test extended ancestors for a parent-child relationship - the parent's ancestors are the child's, but not all the
     * child's ancestors are the parent's
     */
    @Test
    public void testExtendedAncestors4() {
        Individual robert = getPerson("Andrews", "Robert");
        Individual theresa = getPerson("Andrews", "Theresa");

        Set<Individual> robertAncestors = anc.getExtendedAncestry(robert);
        if (VERBOSE) {
            System.out.println("Ancestors of Robert Andrews");
            dumpIndividuals(robertAncestors);
        }
        Set<Individual> theresaAncestors = anc.getExtendedAncestry(theresa);
        if (VERBOSE) {
            System.out.println("Ancestors of Theresa Andrews");
            dumpIndividuals(theresaAncestors);
        }
        assertTrue("Theresa is Robert's child, so all of Robert's ancestors are also Theresa's ancestors",
                theresaAncestors.containsAll(robertAncestors));
        assertTrue("Theresa is Robert's child, so Theresa has ancestors that are not Robert's",
                theresaAncestors.size() > robertAncestors.size());
    }

    /**
     * Test when people are siblings.
     */
    @Test
    public void testGenerationCount0() {
        Individual sally = getPerson("Struthers", "Sally");
        // Sammy is Sally's brother
        Individual sammy = getPerson("Struthers", "Sammy");
        assertNotNull(sally);
        assertNotNull(sammy);
        try {
            anc.getGenerationCount(sammy, sally);
            fail("Expected an IllegalArgumentException since sally is not an ancestor of sammy - they are brother and sister");
        } catch (IllegalArgumentException desired) {
            // Yay! It worked!
        }
        try {
            anc.getGenerationCount(sally, sammy);
            fail("Expected an IllegalArgumentException since sammy is not an ancestor of sally - they are brother and sister");
        } catch (IllegalArgumentException desired) {
            // Yay! It worked!
        }
    }

    /**
     * Test when people are 1 generation apart. Includes negative test where the ancestor/descendant are swapped.
     */
    @Test
    public void testGenerationCount1() {
        Individual sally = getPerson("Struthers", "Sally");
        Individual steven = getPerson("Struthers", "Steven");
        assertNotNull(sally);
        assertNotNull(steven);
        assertEquals(1, anc.getGenerationCount(sally, steven));
        try {
            anc.getGenerationCount(steven, sally);
            fail("Expected an IllegalArgumentException since sally is a descendant of steven, not an ancestor");
        } catch (IllegalArgumentException desired) {
            // Yay! It worked!
        }
    }

    /**
     * Test when people are 2 generations apart. Includes negative test where the ancestor/descendant are swapped.
     */
    @Test
    public void testGenerationCount2() {
        Individual robert = getPerson("Andrews", "Robert");
        // Steven is Robert's grandfather
        Individual steven = getPerson("Struthers", "Steven");
        assertNotNull(robert);
        assertNotNull(steven);
        assertEquals(2, anc.getGenerationCount(robert, steven));
        try {
            anc.getGenerationCount(steven, robert);
            fail("Expected an IllegalArgumentException since robert is a descendant of steven, not an ancestor");
        } catch (IllegalArgumentException desired) {
            // Yay! It worked!
        }
    }

    /**
     * Test when people are several generations apart. Includes negative test where the ancestor/descendant are swapped.
     */
    @Test
    public void testGenerationCount3() {
        Individual alex = getPerson("Zucco", "Alex");
        // Kenneth is Alex's great-great-great-grandfather
        Individual kenneth = getPerson("Struthers", "Kenneth");
        assertNotNull(alex);
        assertNotNull(kenneth);
        assertEquals(5, anc.getGenerationCount(alex, kenneth));
        try {
            anc.getGenerationCount(kenneth, alex);
            fail("Expected an IllegalArgumentException since alex is a descendant of kenneth, not an ancestor");
        } catch (IllegalArgumentException desired) {
            // Yay! It worked!
        }
    }

    /**
     * Test degenerate case for a married couple that has no common ancestors
     */
    @Test
    public void testLowestCommonAncestor1() {
        Individual ulysses = getPerson("Jackson", "Ulysses");
        Individual abigail = getPerson("Wood", "Abigail");

        Set<Individual> lowestCommonAncestors = anc.getLowestCommonAncestors(ulysses, abigail);
        assertEquals("Ulysses and Abigail have no common ancestors", 0, lowestCommonAncestors.size());
    }

    /**
     * Test simple case for a brother and sister of the same two parents and no known grandparents
     */
    @Test
    public void testLowestCommonAncestor2() {
        Individual sally = getPerson("Struthers", "Sally");
        Individual sammy = getPerson("Struthers", "Sammy");

        Set<Individual> lowestCommonAncestors = anc.getLowestCommonAncestors(sally, sammy);
        assertEquals("Sammy and Sally (brother and sister) have two common ancestors (their parents)", 2,
                lowestCommonAncestors.size());
        Individual steven = getPerson("Struthers", "Steven");
        Individual gladys = getPerson("Knight", "Gladys");
        assertTrue("Steven is a common ancestor (their dad)", lowestCommonAncestors.contains(steven));
        assertTrue("Gladys is a common ancestor (their mom)", lowestCommonAncestors.contains(gladys));
    }

    /**
     * Test simple case for a person and his parent(s) - the grandparent(s) should be in common - the grandparents have
     * no parents in the gedcom
     */
    @Test
    public void testLowestCommonAncestor3() {
        Individual robert = getPerson("Andrews", "Robert");
        Individual sammy = getPerson("Struthers", "Sammy");

        Set<Individual> lowestCommonAncestors = anc.getLowestCommonAncestors(robert, sammy);
        assertEquals(
                "Robert (son) and Sammy (father) have two ancestors in common: Sammy's parents/Robert's grandparents",
                2, lowestCommonAncestors.size());
        Individual steven = getPerson("Struthers", "Steven");
        Individual gladys = getPerson("Knight", "Gladys");
        assertTrue("Steven is a common ancestor (Sammy's dad and Robert's grandfather)",
                lowestCommonAncestors.contains(steven));
        assertTrue("Gladys is a common ancestor (Sammy's mom and Robert's grandmother)",
                lowestCommonAncestors.contains(gladys));
    }

    /**
     * Test simple case for a daughter and her father - the her paternal grandparents should be in common - the
     * grandparents DO have parents in the gedcom - so the paternal GREAT grandparents should NOT be in the list, as
     * they are not the lowest common ancestors.
     */
    @Test
    public void testLowestCommonAncestor4() {
        Individual robert = getPerson("Andrews", "Robert");
        Individual theresa = getPerson("Andrews", "Theresa");

        if (VERBOSE) {
            System.out.println("Any match of these will be fine:");
            for (Individual i : anc.getExtendedAncestry(robert)) {
                System.out.println("\t" + i.names.get(0).basic);
            }
        }
        Set<Individual> lowestCommonAncestors = anc.getLowestCommonAncestors(robert, theresa);
        assertEquals("Robert (father) and Theresa (daughter) should have two lowest common ancestors:"
                + " James Andrews, and Sally Struthers", 2, lowestCommonAncestors.size());
        Individual sally = getPerson("Struthers", "Sally");
        Individual james = getPerson("Andrews", "James");
        assertTrue("Sally is a common ancestor (Robert's mom and Theresa's grandmother)",
                lowestCommonAncestors.contains(sally));
        assertTrue("James is a common ancestor (Robert's dad and Theresa's grandfather)",
                lowestCommonAncestors.contains(james));
        assertFalse("Steven Struthers (Robert's grandfather) is a common ancestor, but not a LOWEST common ancestor",
                lowestCommonAncestors.contains(getPerson("Struthers", "Steven")));
        assertFalse("Gladys Knight (Robert's grandmother) is a common ancestor, but not a LOWEST common ancestor",
                lowestCommonAncestors.contains(getPerson("Knight", "Gladys")));
    }

    /**
     * Helper method that dumps out a set of individuals
     * 
     * @param people
     *            the set of {@link Individual}s to dump out
     */
    private void dumpIndividuals(Set<Individual> people) {
        if (!VERBOSE) {
            return;
        }

        int i = 1;
        for (Individual individual : people) {
            System.out.println(i++ + ": " + individual);
        }
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
