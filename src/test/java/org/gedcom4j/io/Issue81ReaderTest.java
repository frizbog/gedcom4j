/*
 * Copyright (c) 2009-2015 Matthew R. Harrah
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
package org.gedcom4j.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.gedcom4j.model.*;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.parser.GedcomParserException;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for issue 81, where characters with diacritical marks were being reported as not loading correctly from files.
 * 
 * @author frizbog
 */
public class Issue81ReaderTest {

    /**
     * The gedcom loaded from the sample file
     */
    private Gedcom g;

    /**
     * Load the gedcom, assert that everything loaded ok. We'll inspect things later.
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if the file can't be parsed
     */
    @Before
    public void setUp() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/issue81.ged");
        for (String e : gp.errors) {
            System.err.println(e);
        }
        for (String w : gp.warnings) {
            System.err.println(w);
        }
        assertEquals(0, gp.errors.size());
        assertEquals(0, gp.warnings.size());
        g = gp.gedcom;
        assertEquals(2, g.individuals.size());
    }

    /**
     * Test for proper concatenation of strings in CONC tags when the diacritic appears at end of line
     */
    @Test
    public void testConcDiacriticAtEndOfLine() {
        Individual i = g.individuals.get("@I002@");
        assertNotNull(i);
        assertEquals(2, i.events.size());
        IndividualEvent e = i.events.get(1);
        assertEquals(1, e.notes.size());
        Note note = e.notes.get(0);
        assertEquals(6, note.lines.size());

        assertEquals("", note.lines.get(0));

        assertEquals("", note.lines.get(1));

        assertEquals("", note.lines.get(2));

        assertEquals("", note.lines.get(3));

        assertEquals("", note.lines.get(4));

        assertEquals(" ", note.lines.get(5));
    }

    /**
     * Test diacritical marks in an event subtype (name)
     */
    @Test
    public void testEventName() {
        Individual i = g.individuals.get("@I002@");
        assertNotNull(i);
        assertEquals(2, i.events.size());
        assertEquals("lib\u00E9r\u00E9e", i.events.get(0).subType.value);
        assertEquals("histoire de m\u00E9m\u00E9", i.events.get(1).subType.value);
    }

    /**
     * Test the corporation name in the source system of the header
     */
    @Test
    public void testHeaderCorporation() {
        assertEquals("BSD Concept \u00A9", g.header.sourceSystem.corporation.businessName);
        assertEquals("BSD Concept ©", g.header.sourceSystem.corporation.businessName);
    }

    /**
     * Simple test for name
     */
    @Test
    public void testIndividual1Name() {
        Individual i = g.individuals.get("@I001@");
        assertNotNull(i);
        PersonalName n = i.names.get(0);
        assertEquals("Dolor\u00E1s", n.givenName.value);
        assertEquals("Dolor\u00E1s/./", n.basic);
    }

    /**
     * Slightly more complicated test for name
     */
    @Test
    public void testIndividual2Name() {
        Individual i = g.individuals.get("@I002@");
        assertNotNull(i);
        PersonalName n = i.names.get(0);
        assertEquals("Therese", n.givenName.value);
        assertEquals("VACQU\u00E2", n.surname.value);
        assertEquals("Therese/VACQU\u00E2/", n.basic);
    }
}
