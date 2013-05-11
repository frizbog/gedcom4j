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

import org.gedcom4j.model.CitationWithSource;
import org.gedcom4j.model.CitationWithoutSource;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.FileReference;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.Note;
import org.gedcom4j.model.PersonalName;
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
     * Test loading a file.
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if there's a problem parsing the data
     */
    public void testLoadTGC55C() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.verbose = true;
        gp.load("sample/TGC55C.ged");
        checkTGC55C(gp);
    }

    /**
     * Test loading a file.
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if there's a problem parsing the data
     */
    public void testLoadTGC55CLF() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.verbose = true;
        gp.load("sample/TGC55CLF.ged");
        checkTGC55C(gp);
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

    /**
     * The same sample file is used several times, this helper method ensures
     * consistent assertions for all tests using the same file
     * 
     * @param gp
     *            the {@link GedcomParser}
     */
    private void checkTGC55C(GedcomParser gp) {
        Individual indi;
        PersonalName name;
        Note note;
        Multimedia multimedia;
        FileReference fileReference;
        CitationWithSource citWithSource;
        CitationWithoutSource citWithoutSource;
        Source source;

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

        // ===============================================================
        // Individual @PERSON1@
        // ===============================================================
        indi = g.individuals.get("@PERSON1@");

        assertEquals("@PERSON1@", indi.xref);

        assertEquals(3, indi.citations.size());
        assertEquals(2, indi.names.size());
        assertEquals(2, indi.notes.size());

        // Name 0
        name = indi.names.get(0);
        assertEquals("Joseph Tag /Torture/", name.basic);
        assertEquals("Torture, Joseph \"Joe\"", name.toString());

        assertEquals(1, name.citations.size());
        assertEquals(1, name.notes.size());

        // Name 0 - Citation 0
        assertTrue(name.citations.get(0) instanceof CitationWithSource);
        citWithSource = (CitationWithSource)name.citations.get(0);
        source = citWithSource.source;

        assertEquals("@SOURCE1@", source.xref);
        assertEquals("42", citWithSource.whereInSource.toString());

        assertEquals(0, citWithSource.multimedia.size());
        assertEquals(0, citWithSource.notes.size());

        // Name 0 - Note 0
        note = name.notes.get(0);
        assertEquals(5, note.lines.size());
        assertEquals("These are notes about the first NAME structure in this record. These notes are embedded in the INDIVIDUAL record itself.", note.lines.get(0));

        // Name 1
        name = indi.names.get(1);
        assertEquals("William John /Smith/", name.basic);
        assertEquals("William John /Smith/", name.toString());

        assertEquals(1, name.citations.size());
        assertEquals(1, name.notes.size());

        // Name 1 - Citation 0
        assertTrue(name.citations.get(0) instanceof CitationWithSource);
        citWithSource = (CitationWithSource)name.citations.get(0);
        source = citWithSource.source;

        assertEquals("@SOURCE1@", source.xref);
        assertEquals("55", citWithSource.whereInSource.toString());

        assertEquals(1, citWithSource.multimedia.size());
        assertEquals(1, citWithSource.notes.size());

        // Name 1 - Multimedia 0
        multimedia = citWithSource.multimedia.get(0);
        assertEquals(0, multimedia.citations.size());
        assertEquals(1, multimedia.fileReferences.size());
        assertEquals(1, multimedia.notes.size());

        // Name 1 - Multimedia 0 - FileReference 0
        fileReference = multimedia.fileReferences.get(0);
        assertEquals("jpeg", fileReference.format.toString());
        assertEquals(null, fileReference.mediaType);
        assertEquals("ImgFile.JPG", fileReference.referenceToFile.toString());
        assertEquals(null, fileReference.title);

        // Name 1 - Multimedia 0 - Note 0
        note = multimedia.notes.get(0);
        assertEquals(1, note.lines.size());
        assertEquals("These are some notes of this multimedia link in the NAME structure.", note.lines.get(0));

        // Name 1 - Citation 0 - Note 0
        note = citWithSource.notes.get(0);
        assertEquals(3, note.lines.size());
        assertEquals("This source citation has all fields possible in a source citation to a separate SOURCE record. Besides the link to the SOURCE record there are possible fields about this citation (e.g., PAGE, TEXT, etc.)", note.lines.get(0));

        // Name 1 - Note 0
        note = name.notes.get(0);
        assertEquals(3, note.lines.size());
        assertEquals("This is a second personal NAME structure in a single INDIVIDUAL record which is allowed in GEDCOM. This second NAME structure has all possible fields for a NAME structure.", note.lines.get(0));

        // Note 0
        note = indi.notes.get(0);
        assertEquals(40, note.lines.size());
        assertEquals("Comments on \"Joseph Tag Torture\" INDIVIDUAL Record.", note.lines.get(0));

        // Note 1
        note = indi.notes.get(1);
        assertEquals(3, note.lines.size());
        assertEquals("This is a second set of notes for this single individual record. It is embedded in the INDIVIDUAL record instead of being in a separate NOTE record.", note.lines.get(0));

        // Citation 0
        assertTrue(indi.citations.get(0) instanceof CitationWithSource);
        citWithSource = (CitationWithSource)indi.citations.get(0);
        source = citWithSource.source;

        assertEquals("@SOURCE1@", source.xref);
        assertEquals("42", citWithSource.whereInSource.toString());

        assertEquals(0, citWithSource.multimedia.size());
        assertEquals(1, citWithSource.notes.size());

        // Citation 0 - Note 0
        note = citWithSource.notes.get(0);
        assertEquals(1, note.lines.size());
        assertEquals("A source note.", note.lines.get(0));

        // Citation 1
        assertTrue(indi.citations.get(1) instanceof CitationWithSource);
        citWithSource = (CitationWithSource)indi.citations.get(1);
        source = citWithSource.source;

        assertEquals("@SR2@", source.xref);
        assertEquals(null, citWithSource.whereInSource);

        assertEquals(0, citWithSource.multimedia.size());
        assertEquals(1, citWithSource.notes.size());

        // Citation 1 - Note 0
        note = citWithSource.notes.get(0);
        assertEquals(1, note.lines.size());
        assertEquals("This is a second source citation in this record.", note.lines.get(0));

        // Citation 2
        assertTrue(indi.citations.get(2) instanceof CitationWithoutSource);
        citWithoutSource = (CitationWithoutSource)indi.citations.get(2);

        assertEquals(1, citWithoutSource.notes.size());

        // Citation 2 - Note 0
        note = citWithoutSource.notes.get(0);
        assertEquals(1, note.lines.size());
        assertEquals("How does software handle embedded SOURCE records on import? Such source citations are common in old GEDCOM files. More modern GEDCOM files should use source citations to SOURCE records.", note.lines.get(0));
    }
}
