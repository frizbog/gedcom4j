/*
 * Copyright (c) 2009-2012 Matthew R. Harrah
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
package com.mattharrah.gedcom4j.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;

import com.mattharrah.gedcom4j.model.Individual;
import com.mattharrah.gedcom4j.model.Note;

/**
 * Test for Issue 32, related to CONC/CONT lines
 * 
 * @author frizbog1
 */
public class Issue32Test {

    /**
     * Test for Issue 32, related to CONC/CONT lines
     * 
     * @throws IOException
     *             if the file cannot be read
     * @throws GedcomParserException
     *             if the file cannot be parsed
     */
    @Test
    public void test() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/issue32.ged");
        assertNotNull(gp.gedcom);
        assertNotNull(gp.gedcom.individuals);
        assertEquals("There are ten individuals in the test gedcom", 10,
                gp.gedcom.individuals.size());
        boolean foundJohn = false;
        boolean foundMary = false;
        for (Entry<String, Individual> i : gp.gedcom.individuals.entrySet()) {
            if (i.getKey().equalsIgnoreCase("@I1@")) {
                foundJohn = true;
                Individual john = i.getValue();
                assertNotNull(john);
                checkJohn(john.notes);
            }
            if (i.getKey().equalsIgnoreCase("@I2@")) {
                foundMary = true;
                Individual mary = i.getValue();
                assertNotNull(mary);
                checkMary(mary.notes);
            }
        }
        assertTrue("Didn't find john", foundJohn);
        assertTrue("Didn't find mary", foundMary);
    }

    /**
     * Check that the notes on John are as expected based on the gedcom
     * 
     * @param notes
     *            the notes
     */
    private void checkJohn(List<Note> notes) {
        assertNotNull(notes);
        assertEquals(1, notes.size());
        Note note = notes.get(0);
        assertNotNull(note);
        assertNotNull(note.lines);
        assertEquals(1, note.lines.size());
        assertEquals(
                "Lorem ipsum dolor sit amet, consectetur adipisicing elit, "
                        + "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
                        + "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris "
                        + "nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in "
                        + "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla "
                        + "pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa "
                        + "qui officia deserunt mollit anim id est laborum.",
                note.lines.get(0));
    }

    /**
     * Check that the notes on Mary are as expected based on the gedcom
     * 
     * @param notes
     */
    private void checkMary(List<Note> notes) {
        assertNotNull(notes);
        assertEquals(1, notes.size());
        Note note = notes.get(0);
        assertNotNull(note);
        assertNotNull(note.lines);
        assertEquals(3, note.lines.size());
        assertEquals(
                "Lorem ipsum dolor sit amet, consectetur adipisicing elit, "
                        + "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ",
                note.lines.get(0));
        assertEquals(
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris "
                        + "nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in "
                        + "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla "
                        + "pariatur. ", note.lines.get(1));
        assertEquals(
                "Excepteur sint occaecat cupidatat non proident, sunt in culpa "
                        + "qui officia deserunt mollit anim id est laborum.",
                note.lines.get(2));

    }
}
