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
package org.gedcom4j.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.model.CustomFact;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.IGedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.parser.GedcomParser;
import org.junit.Test;

/**
 * Test for issue 95, in which {@link GedcomWriter} was writing custom event tags twice.
 * 
 * @author frizbog
 *
 */
public class Issue95Test {

    /**
     * Test for Issue 95, in which {@link GedcomWriter} was writing custom event tags twice. Reads a file with a single custom
     * individual event tag, rewrites it to a string, and counts the number of individual event tags in the output file.
     * 
     * @throws IOException
     *             if the file cannot be read
     * @throws GedcomParserException
     *             if the file cannot be parsed
     * @throws GedcomWriterException
     *             if the gedcom data cannot be written
     */
    @Test
    @SuppressWarnings({ "PMD.AvoidPrintStackTrace", "PMD.SystemPrintln" })
    public void testIssue95() throws IOException, GedcomParserException, GedcomWriterException {
        GedcomParser gp = new GedcomParser(new Gedcom());
        gp.load("sample/issue95.ged");
        assertTrue(gp.getErrors().isEmpty());
        assertTrue(gp.getWarnings().isEmpty());
        IGedcom g = gp.getGedcom();
        Individual i = g.getIndividuals().get("@I1@");
        assertNotNull(i);

        // Let's start by making sure everything loaded as expected
        assertEquals(2, i.getEvents().size());
        for (IndividualEvent ev : i.getEvents()) {
            if (ev.getType() == IndividualEventType.BIRTH) {
                assertEquals("4 JUL 1776", ev.getDate().getValue());
                assertEquals(1, ev.getCustomFacts().size());
                CustomFact cf = ev.getCustomFacts().get(0);
                assertEquals("_METHOD", cf.getTag());
                assertEquals("Hatched from egg", cf.getDescription().getValue());
            } else if (ev.getType() == IndividualEventType.DEATH) {
                assertEquals("Suffolk, VA, USA", ev.getPlace().getPlaceName());
                assertTrue(ev.getCustomFacts() == null || ev.getCustomFacts().isEmpty());
            } else {
                fail("Unexpected Individual Event type " + ev.getType());
            }
        }

        // Now let's write the gedcom to a string for examination
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GedcomWriter gw = new GedcomWriter(g);
        try {
            gw.write(baos);
        } catch (GedcomWriterException e) {
            e.printStackTrace();
            System.out.println(gw.getValidator().getResults());
        }
        String gcAsString = baos.toString();

        // Custom tag should appear once, as should birth date
        assertEquals(1, countOccurrences(gcAsString, "2 _METHOD"));
        assertEquals(1, countOccurrences(gcAsString, "2 DATE"));

        // Place was used twice - once for birth, once for death
        assertEquals(2, countOccurrences(gcAsString, "2 PLAC"));

    }

    /**
     * Helper method to find the number of occurrences of a substring
     * 
     * @param string
     *            the string to look in
     * @param lookingFor
     *            the string to look for
     * @return the number of times <code>lookingFor</code> appears in string
     */
    private int countOccurrences(String string, String lookingFor) {
        // Count the number of times the _METHOD custom tag appears
        int count = 0;
        int lastPos = 0;
        while (lastPos >= 0) {
            lastPos = string.indexOf(lookingFor, lastPos);
            if (lastPos >= 0) {
                count++;
                lastPos += lookingFor.length();
            }
        }
        return count;
    }
}
