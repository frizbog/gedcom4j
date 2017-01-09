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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.IGedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.Source;
import org.junit.Test;

/**
 * Test Issue 129, which was an enhancement to add the ability to ignore custom tags when parsing files
 * 
 * @author frizbog
 *
 */
public class Issue129Test {

    /**
     * Test parsing with custom tags ignored
     * 
     * @throws GedcomParserException
     *             if the file cannot be parsed
     * @throws IOException
     *             if the file cannot be read
     */
    @Test
    public void testIgnored() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser(new Gedcom());
        gp.setIgnoreCustomTags(true);

        // Load file full of custom tags
        gp.load("sample/customtagstorture.ged");
        IGedcom g = gp.getGedcom();
        assertNull(g.getHeader().getCustomFacts());
        assertNull(g.getHeader().getCharacterSet().getCustomFacts());
        for (Individual i : g.getIndividuals().values()) {
            assertNull(i.getCustomFacts());
        }
        for (Family f : g.getFamilies().values()) {
            assertNull(f.getCustomFacts());
        }
        for (Source s : g.getSources().values()) {
            assertNull(s.getCustomFacts());
        }
    }

    /**
     * Test parsing with custom tags NOT ignored
     * 
     * @throws GedcomParserException
     *             if the file cannot be parsed
     * @throws IOException
     *             if the file cannot be read
     */
    @Test
    public void testNotIgnored() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser(new Gedcom());
        gp.setIgnoreCustomTags(false);

        // Load file full of custom tags
        gp.load("sample/customtagstorture.ged");
        IGedcom g = gp.getGedcom();
        assertNotNull(g.getHeader().getCustomFacts());
        assertNotNull(g.getHeader().getCharacterSet().getCustomFacts());
        for (Individual i : g.getIndividuals().values()) {
            assertNotNull(i.toString(), i.getCustomFacts());
        }
        for (Family f : g.getFamilies().values()) {
            assertNotNull(f.getCustomFacts());
        }
        for (Source s : g.getSources().values()) {
            assertNotNull(s.getCustomFacts());
        }
    }

}
