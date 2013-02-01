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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.gedcom4j.model.Family;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Source;
import org.gedcom4j.model.Submitter;
import org.junit.Test;

/**
 * Tests for the {@link GedcomParser} class
 * 
 * @author frizbog1
 * 
 */
public class GedcomParserTest extends TestCase {

    /**
     * Test for a bad custom tag
     * 
     * 
     * @throws IOException
     *             if there is a problem reading/writing the file
     * @throws GedcomParserException
     *             if there is a problem parsing the GEDCOM
     * 
     */
    @Test
    public void testBadCustomTag() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.verbose = true;
        gp.load("sample/Bad_custom_tag.ged");
        assertNotNull(gp.errors);
        assertEquals(1, gp.errors.size());
        assertNotNull(gp.errors.get(0));
        assertTrue(gp.errors.get(0).contains("Line 28: Cannot handle tag BUST"));
    }

    /**
     * Test loading a file...it's a stress test file.
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if there's a problem parsing the data
     */
    public void testLoad1() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.verbose = true;
        gp.load("sample/TGC551.ged");
        assertTrue(gp.errors.isEmpty());
        checkTGC551LF(gp);
    }

    /**
     * Test loading a sample file ... another stress-test file. This one should
     * have warnings.
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if there's a problem parsing the data
     */
    public void testLoad2() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.verbose = true;
        assertTrue(gp.errors.isEmpty());
        gp.load("sample/allged.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue(gp.warnings.isEmpty());
        assertEquals("There is exactly 1 custom tag on the file as a whole", 1, gp.gedcom.customTags.size());
        assertEquals("There is exactly 1 custom tag in the header", 1, gp.gedcom.header.customTags.size());
        Gedcom g = gp.gedcom;
        assertFalse(g.submitters.isEmpty());
        Submitter submitter = g.submitters.values().iterator().next();
        assertNotNull(submitter);
        assertEquals("/Submitter-Name/", submitter.name.value);
    }

    /**
     * Test loading another sample file
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if there's a problem parsing the data
     */
    public void testLoad3() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.verbose = true;
        gp.load("sample/a31486.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue(gp.warnings.isEmpty());
        Gedcom g = gp.gedcom;

        // Check submitter
        assertFalse(g.submitters.isEmpty());
        Submitter submitter = g.submitters.values().iterator().next();
        assertNotNull(submitter);
        assertNull(submitter.name);

        // Check header
        assertEquals("6.00", g.header.sourceSystem.versionNum.value);
        assertEquals("(510) 794-6850", g.header.sourceSystem.corporation.phoneNumbers.get(0).value);

        // There are two sources in this file, and their names should be as
        // shown
        assertEquals(2, g.sources.size());
        for (Source s : g.sources.values()) {
            assertTrue(s.title.get(0).equals("William Barnett Family.FTW")
                    || s.title.get(0).equals("Warrick County, IN WPA Indexes"));
        }

        assertEquals(17, g.families.size());
        assertEquals(64, g.individuals.size());

        // Check a specific family
        Family family = g.families.get("@F1428@");
        assertNotNull(family);
        assertEquals(3, family.children.size());
        assertEquals("Lawrence Henry /Barnett/", family.husband.names.get(0).basic);
        assertEquals("Velma //", family.wife.names.get(0).basic);

    }

    /**
     * Test loading a file with a different line terminator sequence.
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if there's a problem parsing the data
     */
    public void testLoad4() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.verbose = true;
        // Different line end char seq than the other file
        gp.load("sample/TGC551LF.ged");
        checkTGC551LF(gp);
    }

    /**
     * Test for loading file from stream.
     * 
     * @throws IOException
     *             if the file can't be read from the stream
     * @throws GedcomParserException
     *             if the parsing goes wrong
     */
    public void testLoadStream() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.verbose = true;
        InputStream stream = new FileInputStream("sample/TGC551LF.ged");
        gp.load(new BufferedInputStream(stream));
        checkTGC551LF(gp);
    }

    /**
     * The same sample file is used several times, this helper method ensures
     * consistent assertions for all tests using the same file
     * 
     * @param gp
     *            the {@link GedcomParser}
     */
    private void checkTGC551LF(GedcomParser gp) {
        assertTrue(gp.errors.isEmpty());
        assertTrue(gp.warnings.isEmpty());
        Gedcom g = gp.gedcom;
        assertNotNull(g.header);
        assertEquals(3, g.submitters.size());
        Submitter submitter = g.submitters.get("@SUBMITTER@");
        assertNotNull(submitter);
        assertEquals("John A. Nairn", submitter.name.value);

        assertEquals(7, g.families.size());
        assertEquals(2, g.sources.size());
        assertEquals(1, g.multimedia.size());
        assertEquals(15, g.individuals.size());
    }
}
