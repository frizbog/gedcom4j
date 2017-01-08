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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.IGedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.query.Finder;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link GenerationCounter}
 * 
 * @author frizbog
 */
public class GenerationCounterTest {

    /**
     * Class under test
     */
    GenerationCounter classUnderTest = new GenerationCounter();

    /**
     * A finder test fixture for the test
     */
    private Finder finder;

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
     * Test when people are siblings.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGenerationCount0Part1() {
        Individual sally = getPerson("Struthers", "Sally");
        // Sammy is Sally's brother
        Individual sammy = getPerson("Struthers", "Sammy");
        assertNotNull(sally);
        assertNotNull(sammy);

        // Next line throws exception
        classUnderTest.getGenerationCount(sammy, sally);
    }

    /**
     * Test when people are siblings.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGenerationCount0Part2() {
        Individual sally = getPerson("Struthers", "Sally");
        // Sammy is Sally's brother
        Individual sammy = getPerson("Struthers", "Sammy");
        assertNotNull(sally);
        assertNotNull(sammy);

        // Next line throws exception
        classUnderTest.getGenerationCount(sally, sammy);
    }

    /**
     * Test when people are 1 generation apart. Includes negative test where the ancestor/descendant are swapped.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGenerationCount1() {
        /* Sally is the descendant */
        Individual sally = getPerson("Struthers", "Sally");
        /* Steven is Sally's father */
        Individual steven = getPerson("Struthers", "Steven");
        assertNotNull(sally);
        assertNotNull(steven);
        assertEquals(1, classUnderTest.getGenerationCount(sally, steven));

        // Next line throws exception
        classUnderTest.getGenerationCount(steven, sally);
    }

    /**
     * Test when people are 2 generations apart. Includes negative test where the ancestor/descendant are swapped.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGenerationCount2() {
        Individual robert = getPerson("Andrews", "Robert");
        // Steven is Robert's grandfather
        Individual steven = getPerson("Struthers", "Steven");
        assertNotNull(robert);
        assertNotNull(steven);
        assertEquals(2, classUnderTest.getGenerationCount(robert, steven));

        // Next line throws exception
        classUnderTest.getGenerationCount(steven, robert);
    }

    /**
     * Test when people are several generations apart. Includes negative test where the ancestor/descendant are swapped.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGenerationCount3() {
        Individual alex = getPerson("Zucco", "Alex");
        // Kenneth is Alex's great-great-great-grandfather
        Individual kenneth = getPerson("Struthers", "Kenneth");
        assertNotNull(alex);
        assertNotNull(kenneth);
        assertEquals(5, classUnderTest.getGenerationCount(alex, kenneth));

        // Next line throws exception
        classUnderTest.getGenerationCount(kenneth, alex);
    }

    /**
     * Test when people are the same person.
     */
    @Test
    public void testSamePerson() {
        Individual sally = getPerson("Struthers", "Sally");
        assertNotNull(sally);
        assertEquals(0, classUnderTest.getGenerationCount(sally, sally));
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
