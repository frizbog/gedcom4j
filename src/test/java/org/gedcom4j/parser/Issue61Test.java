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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.gedcom4j.Options;
import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.CustomFact;
import org.gedcom4j.model.IGedcom;
import org.gedcom4j.model.Individual;
import org.junit.Test;

/**
 * Test for <a href="https://github.com/frizbog/gedcom4j/issues/61">issue 61</a> which allows unknown tags to be treated as custom
 * tags, as an option.
 * 
 * @author frizbog
 */
public class Issue61Test {

    /**
     * Test loading a file with bad tags using loose custom tag handling.
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if there's a parsing problem
     */
    @Test
    public void testIssue61Loose() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.setStrictCustomTags(false);
        gp.load("sample/Harry_Potter.ged");
        IGedcom g = gp.getGedcom();
        assertNotNull(g);
        assertTrue(gp.getErrors().isEmpty());
        for (Individual i : g.getIndividuals().values()) {
            assertNotNull(i);
            assertNotNull(i.getCustomFacts());
            assertFalse("Individual " + i + " has no custom facts", i.getCustomFacts().isEmpty());
            for (CustomFact ct : i.getCustomFacts()) {
                assertTrue("Custom tag should be WAND or MUGL, but is " + ct.getDescription(), "WAND".equals(ct.getTag()) || "MUGL"
                        .equals(ct.getTag()));
                if ("WAND".equals(ct.getTag())) {
                    assertNotNull(ct.getDescription());
                    assertFalse(ct.getDescription().trim().length() == 0);
                }
                if ("MUGL".equals(ct.getTag())) {
                    assertNull(ct.getDescription());
                }
            }
        }
    }

    /**
     * Test loading a file with bad tags using strict custom tag handling.
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if there's a parsing problem
     */
    @Test
    public void testIssue61Strict() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.setStrictCustomTags(true);
        gp.load("sample/Harry_Potter.ged");
        IGedcom g = gp.getGedcom();
        assertNotNull(g);
        assertTrue(!gp.getErrors().isEmpty());
        for (String e : gp.getErrors()) {
            // These are the bad tags that were deliberately introduced
            assertTrue(e.contains("WAND") || e.contains("MUGL"));
        }
        for (Individual i : g.getIndividuals().values()) {
            assertNotNull(i);
            if (Options.isCollectionInitializationEnabled()) {
                assertNotNull(i.getCustomFacts());
                assertTrue(i.getCustomFacts().isEmpty());
            } else {
                assertNull(i.getCustomFacts());
            }
        }
    }
}
