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

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.FamilyEvent;
import org.gedcom4j.model.Gedcom;
import org.junit.Before;
import org.junit.Test;

/**
 * Stress test parsing for Family Events
 * 
 * @author frizbog1
 */
public class FamilyEventTypeParseTest {

    /**
     * Test fixture - loaded GEDCOM
     */
    private Gedcom g;

    /**
     * Set up test fixture by loading stress test file into a {@link Gedcom} structure
     * 
     * @throws GedcomParserException
     *             if the file cannot be parsed
     * @throws IOException
     *             if there is an error reading the data
     */
    @Before
    public void setUp() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/TGC551.ged");
        assertTrue(gp.getErrors().isEmpty());
        g = gp.getGedcom();
    }

    /**
     * Positive test case for google code issue 2
     * 
     * @throws GedcomParserException
     *             if the file cannot be parsed
     * @throws IOException
     *             if there is an error reading the data
     */
    @Test
    public void testIssue2() throws IOException, GedcomParserException {
        int familyCount = 0;
        for (Family fam : g.getFamilies().values()) {
            familyCount++;
            if (fam.getEvents() != null) {
                for (FamilyEvent event : fam.getEvents()) {
                    assertNotNull(event.getType());
                }
            }
        }
        assertEquals("There are 7 families in the stress test file", 7, familyCount);
    }
}
