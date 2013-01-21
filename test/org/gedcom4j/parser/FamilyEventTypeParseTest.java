/*
 * Copyright (c) 2009-2013 Matthew R. Harrah
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
package org.gedcom4j.parser;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.gedcom4j.model.Family;
import org.gedcom4j.model.FamilyEvent;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.parser.GedcomParserException;

import junit.framework.TestCase;


/**
 * A test specifically for issue 2 (see gedcom4j.googlecode.com)
 * 
 * @author frizbog1
 */
public class FamilyEventTypeParseTest extends TestCase {

    /**
     * Test fixture - loaded GEDCOM
     */
    private Gedcom g;

    /**
     * Positive test case for google code issue 2
     * 
     * @throws GedcomParserException
     *             if the file cannot be parsed
     * @throws IOException
     *             if there is an error reading the data
     */
    public void testIssue2() throws IOException, GedcomParserException {
        int familyCount = 0;
        for (Family fam : g.families.values()) {
            familyCount++;
            assertNotNull(fam.events);
            for (FamilyEvent event : fam.events) {
                assertNotNull(event.type);
            }
        }
        assertEquals("There are 7 families in the stress test file", 7, familyCount);
    }

    /**
     * Set up test fixture by loading stress test file into a {@link Gedcom} struture
     * 
     * @throws GedcomParserException
     *             if the file cannot be parsed
     * @throws IOException
     *             if there is an error reading the data
     */
    @Override
    protected void setUp() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.verbose = true;
        gp.load("sample/TGC551.ged");
        assertTrue(gp.errors.isEmpty());
        g = gp.gedcom;
    }
}
