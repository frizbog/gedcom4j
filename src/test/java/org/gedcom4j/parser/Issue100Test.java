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
package org.gedcom4j.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.Note;
import org.gedcom4j.query.Finder;
import org.junit.Test;

/**
 * Test for fix to Issue 100, where the parser can be relaxed for text with line breaks that are not done according to
 * standard (CONT or CONC tags).
 * 
 * @author frizbog
 */
public class Issue100Test {

    /**
     * Test when strict line breaks are relaxed. Should have problems loading the non-standard file.
     * 
     * @throws IOException
     *             if the file cannot be loaded
     * @throws GedcomParserException
     *             if the file cannot be parsed
     */
    @Test
    public void testRelaxed() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.setStrictLineBreaks(false);
        gp.load("sample/Sopranos.ged");
        Gedcom g = gp.getGedcom();
        assertEquals(2, gp.getWarnings().size());
        assertTrue(gp.getWarnings().contains(
                "Line 26 did not begin with a level and tag, so it was treated as a non-standard continuation of the previous line."));
        assertTrue(gp.getWarnings().contains(
                "Line 27 did not begin with a level and tag, so it was treated as a non-standard continuation of the previous line."));

        assertNotNull(g);
        Finder f = new Finder(g);
        List<Individual> matches = f.findByName(null, "Soprano", "Anthony John", "Sr.");
        assertEquals(1, matches.size());
        Individual i = matches.get(0);
        assertNotNull(i.getNotes());
        assertEquals(1, i.getNotes().size());
        Note n = i.getNotes().get(0);
        assertNotNull(n.getLines());
        assertEquals(3, n.getLines().size());
        assertEquals("This note was deliberately hand-edited to put a line break in the text without a CONT line.", n.getLines().get(0));
        assertEquals("This is the next line of the text that should have been tagged as a CONTinuation.", n.getLines().get(1));
        assertEquals("This is another line of continuation text that should have been tagged.", n.getLines().get(2));
    }

    /**
     * Test when strict line breaks are enforced. Should have problems loading the non-standard file.
     * 
     * @throws IOException
     *             if the file cannot be loaded
     * @throws GedcomParserException
     *             if the file cannot be parsed
     */
    @Test
    public void testStrict() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.setStrictLineBreaks(true);
        try {
            gp.load("sample/Sopranos.ged");
            fail("Should have gotten a " + GedcomParserException.class.getName());
        } catch (GedcomParserException e) {
            assertEquals("Line 26 does not begin with a 1 or 2 digit number for the level followed by a space: "
                    + "This is the next line of the text that should have been tagged as a CONTinuation.", e.getMessage());
        }
    }
}