/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
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

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.*;
import org.junit.Test;

import junit.framework.TestCase;

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
        gp.load("sample/TGC551.ged");
        assertTrue(gp.errors.isEmpty());
        checkTGC551LF(gp);
    }

    /**
     * Test loading a sample file ... another stress-test file. This one should have warnings.
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if there's a problem parsing the data
     */
    public void testLoad2() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        assertTrue(gp.errors.isEmpty());
        gp.load("sample/allged.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue(gp.warnings.isEmpty());
        assertEquals("There is exactly 1 custom tag on the file as a whole", 1, gp.gedcom.getCustomTags().size());
        assertEquals("There is exactly 1 custom tag in the header", 1, gp.gedcom.getHeader().getCustomTags().size());
        Gedcom g = gp.gedcom;
        assertFalse(g.getSubmitters().isEmpty());
        Submitter submitter = g.getSubmitters().values().iterator().next();
        assertNotNull(submitter);
        assertEquals("/Submitter-Name/", submitter.getName().getValue());
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
        gp.load("sample/a31486.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue(gp.warnings.isEmpty());
        Gedcom g = gp.gedcom;

        // Check submitter
        assertFalse(g.getSubmitters().isEmpty());
        Submitter submitter = g.getSubmitters().values().iterator().next();
        assertNotNull(submitter);
        assertEquals(new StringWithCustomTags("UNSPECIFIED"), submitter.getName());

        // Check header
        assertEquals("6.00", g.getHeader().getSourceSystem().versionNum.getValue());
        assertEquals("(510) 794-6850", g.getHeader().getSourceSystem().corporation.getPhoneNumbers().get(0).getValue());

        // There are two sources in this file, and their names should be as
        // shown
        assertEquals(2, g.getSources().size());
        for (Source s : g.getSources().values()) {
            assertTrue(s.title.get(0).equals("William Barnett Family.FTW") || s.title.get(0).equals("Warrick County, IN WPA Indexes"));
        }

        assertEquals(17, g.getFamilies().size());
        assertEquals(64, g.getIndividuals().size());

        // Check a specific family
        Family family = g.getFamilies().get("@F1428@");
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
        // Different line end char seq than the other file
        gp.load("sample/TGC551LF.ged");
        checkTGC551LF(gp);
    }

    /**
     * Test loading a minimal GEDCOM 5.5 file that only has a submitter. This test uses a file which indents lines by
     * their tag level, even though the spec says not to. However, the spec also says to ignore leading spaces on lines,
     * so we're doing that. See issue 57.
     * 
     * @throws IOException
     * @throws GedcomParserException
     */
    public void testLoadIndentedMinimal55File() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/minimal55indented.ged");
        // No problems detected, right?
        assertTrue(gp.errors.isEmpty());
        assertTrue(gp.warnings.isEmpty());

        // And the data is as we expected, right?
        Gedcom g = gp.gedcom;
        assertNotNull(g);
        assertTrue(g.getIndividuals().isEmpty());
        assertTrue(g.getFamilies().isEmpty());
        assertTrue(g.getMultimedia().isEmpty());
        assertTrue(g.getSources().isEmpty());
        assertNotNull(g.getSubmitters());
        assertEquals(1, g.getSubmitters().size());
    }

    /**
     * Test loading a minimal GEDCOM 5.5 file that only has a submitter.
     * 
     * @throws IOException
     * @throws GedcomParserException
     */
    public void testLoadMinimal55File() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/minimal55.ged");
        // No problems detected, right?
        assertTrue(gp.errors.isEmpty());
        assertTrue(gp.warnings.isEmpty());

        // And the data is as we expected, right?
        Gedcom g = gp.gedcom;
        assertNotNull(g);
        assertTrue(g.getIndividuals().isEmpty());
        assertTrue(g.getFamilies().isEmpty());
        assertTrue(g.getMultimedia().isEmpty());
        assertTrue(g.getSources().isEmpty());
        assertNotNull(g.getSubmitters());
        assertEquals(1, g.getSubmitters().size());
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
        InputStream stream = null;
        BufferedInputStream bis = null;
        try {
            stream = new FileInputStream("sample/TGC551LF.ged");
            bis = new BufferedInputStream(stream);
            gp.load(bis);
            checkTGC551LF(gp);

        } finally {
            if (bis != null) {
                bis.close();
            }
            if (stream != null) {
                stream.close();
            }
        }
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
        gp.load("sample/TGC55CLF.ged");
        checkTGC55C(gp);
    }

    /**
     * The same sample file is used several times, this helper method ensures consistent assertions for all tests using
     * the same file
     * 
     * @param gp
     *            the {@link GedcomParser}
     */
    private void checkTGC551LF(GedcomParser gp) {
        assertTrue(gp.errors.isEmpty());
        assertTrue(gp.warnings.isEmpty());
        Gedcom g = gp.gedcom;
        assertNotNull(g.getHeader());
        assertEquals(3, g.getSubmitters().size());
        Submitter submitter = g.getSubmitters().get("@SUBMITTER@");
        assertNotNull(submitter);
        assertEquals("John A. Nairn", submitter.getName().getValue());

        assertEquals(7, g.getFamilies().size());
        assertEquals(2, g.getSources().size());
        assertEquals(1, g.getMultimedia().size());
        assertEquals(15, g.getIndividuals().size());
    }

    /**
     * The same sample file is used several times, this helper method ensures consistent assertions for all tests using
     * the same file
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
        assertNotNull(g.getHeader());
        assertEquals(3, g.getSubmitters().size());
        Submitter submitter = g.getSubmitters().get("@SUBMITTER@");
        assertNotNull(submitter);
        assertEquals("John A. Nairn", submitter.getName().getValue());

        assertEquals(7, g.getFamilies().size());
        assertEquals(2, g.getSources().size());
        assertEquals(1, g.getMultimedia().size());
        assertEquals(15, g.getIndividuals().size());

        // ===============================================================
        // Individual @PERSON1@
        // ===============================================================
        indi = g.getIndividuals().get("@PERSON1@");

        assertEquals("@PERSON1@", indi.getXref());

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
        citWithSource = (CitationWithSource) name.citations.get(0);
        source = citWithSource.getSource();

        assertEquals("@SOURCE1@", source.getXref());
        assertEquals("42", citWithSource.getWhereInSource().toString());

        assertEquals(0, citWithSource.getMultimedia().size());
        assertEquals(0, citWithSource.getNotes().size());

        // Name 0 - Note 0
        note = name.notes.get(0);
        assertEquals(5, note.lines.size());
        assertEquals("These are notes about the first NAME structure in this record. These notes are embedded in the INDIVIDUAL record itself.", note.lines.get(
                0));

        // Name 1
        name = indi.names.get(1);
        assertEquals("William John /Smith/", name.basic);
        assertEquals("William John /Smith/", name.toString());

        assertEquals(1, name.citations.size());
        assertEquals(1, name.notes.size());

        // Name 1 - Citation 0
        assertTrue(name.citations.get(0) instanceof CitationWithSource);
        citWithSource = (CitationWithSource) name.citations.get(0);
        source = citWithSource.getSource();

        assertEquals("@SOURCE1@", source.getXref());
        assertEquals("55", citWithSource.getWhereInSource().toString());

        assertEquals(1, citWithSource.getMultimedia().size());
        assertEquals(1, citWithSource.getNotes().size());

        // Name 1 - Multimedia 0
        multimedia = citWithSource.getMultimedia().get(0);
        assertEquals(0, multimedia.citations.size());
        assertEquals(1, multimedia.fileReferences.size());
        assertEquals(1, multimedia.getNotes().size());

        // Name 1 - Multimedia 0 - FileReference 0
        fileReference = multimedia.fileReferences.get(0);
        assertEquals("jpeg", fileReference.format.toString());
        assertEquals(null, fileReference.mediaType);
        assertEquals("ImgFile.JPG", fileReference.referenceToFile.toString());
        assertEquals(null, fileReference.title);

        // Name 1 - Multimedia 0 - Note 0
        note = multimedia.getNotes().get(0);
        assertEquals(1, note.lines.size());
        assertEquals("These are some notes of this multimedia link in the NAME structure.", note.lines.get(0));

        // Name 1 - Citation 0 - Note 0
        note = citWithSource.getNotes().get(0);
        assertEquals(3, note.lines.size());
        assertEquals(
                "This source citation has all fields possible in a source citation to a separate SOURCE record. Besides the link to the SOURCE record there are possible fields about this citation (e.g., PAGE, TEXT, etc.)",
                note.lines.get(0));

        // Name 1 - Note 0
        note = name.notes.get(0);
        assertEquals(3, note.lines.size());
        assertEquals(
                "This is a second personal NAME structure in a single INDIVIDUAL record which is allowed in GEDCOM. This second NAME structure has all possible fields for a NAME structure.",
                note.lines.get(0));

        // Note 0
        note = indi.notes.get(0);
        assertEquals(40, note.lines.size());
        assertEquals("Comments on \"Joseph Tag Torture\" INDIVIDUAL Record.", note.lines.get(0));

        // Note 1
        note = indi.notes.get(1);
        assertEquals(3, note.lines.size());
        assertEquals(
                "This is a second set of notes for this single individual record. It is embedded in the INDIVIDUAL record instead of being in a separate NOTE record.",
                note.lines.get(0));

        // Citation 0
        assertTrue(indi.citations.get(0) instanceof CitationWithSource);
        citWithSource = (CitationWithSource) indi.citations.get(0);
        source = citWithSource.getSource();

        assertEquals("@SOURCE1@", source.getXref());
        assertEquals("42", citWithSource.getWhereInSource().toString());

        assertEquals(0, citWithSource.getMultimedia().size());
        assertEquals(1, citWithSource.getNotes().size());

        // Citation 0 - Note 0
        note = citWithSource.getNotes().get(0);
        assertEquals(1, note.lines.size());
        assertEquals("A source note.", note.lines.get(0));

        // Citation 1
        assertTrue(indi.citations.get(1) instanceof CitationWithSource);
        citWithSource = (CitationWithSource) indi.citations.get(1);
        source = citWithSource.getSource();

        assertEquals("@SR2@", source.getXref());
        assertEquals(null, citWithSource.getWhereInSource());

        assertEquals(0, citWithSource.getMultimedia().size());
        assertEquals(1, citWithSource.getNotes().size());

        // Citation 1 - Note 0
        note = citWithSource.getNotes().get(0);
        assertEquals(1, note.lines.size());
        assertEquals("This is a second source citation in this record.", note.lines.get(0));

        // Citation 2
        assertTrue(indi.citations.get(2) instanceof CitationWithoutSource);
        citWithoutSource = (CitationWithoutSource) indi.citations.get(2);

        assertEquals(1, citWithoutSource.getNotes().size());

        // Citation 2 - Note 0
        note = citWithoutSource.getNotes().get(0);
        assertEquals(1, note.lines.size());
        assertEquals(
                "How does software handle embedded SOURCE records on import? Such source citations are common in old GEDCOM files. More modern GEDCOM files should use source citations to SOURCE records.",
                note.lines.get(0));
    }
}
