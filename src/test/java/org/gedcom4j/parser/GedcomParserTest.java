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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.gedcom4j.Options;
import org.gedcom4j.exception.GedcomParserException;
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
import org.gedcom4j.model.StringWithCustomTags;
import org.gedcom4j.model.Submitter;
import org.junit.Test;

/**
 * Tests for the {@link GedcomParser} class
 * 
 * @author frizbog1
 * 
 */
@SuppressWarnings("PMD.TooManyMethods")
public class GedcomParserTest {

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
        assertNotNull(gp.getErrors());
        assertEquals(1, gp.getErrors().size());
        assertNotNull(gp.getErrors().get(0));
        assertTrue(gp.getErrors().get(0).contains("Line 28: Cannot handle tag BUST"));
    }

    /**
     * Test loading a file...it's a stress test file.
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if there's a problem parsing the data
     */
    @Test
    public void testLoad1() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/TGC551.ged");
        assertTrue(gp.getErrors().isEmpty());
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
    @Test
    public void testLoad2() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        assertTrue(gp.getErrors().isEmpty());
        gp.load("sample/allged.ged");
        assertTrue(gp.getErrors().isEmpty());
        assertTrue(gp.getWarnings().isEmpty());
        assertEquals("There is exactly 1 custom tag on the file as a whole", 1, gp.getGedcom().getCustomTags().size());
        assertEquals("There is exactly 1 custom tag in the header", 1, gp.getGedcom().getHeader().getCustomTags().size());
        Gedcom g = gp.getGedcom();
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
    @Test
    public void testLoad3() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/a31486.ged");
        assertTrue(gp.getErrors().isEmpty());
        assertTrue(gp.getWarnings().isEmpty());
        Gedcom g = gp.getGedcom();

        // Check submitter
        assertFalse(g.getSubmitters().isEmpty());
        Submitter submitter = g.getSubmitters().values().iterator().next();
        assertNotNull(submitter);
        assertEquals(new StringWithCustomTags("UNSPECIFIED"), submitter.getName());

        // Check header
        assertEquals("6.00", g.getHeader().getSourceSystem().getVersionNum().getValue());
        assertEquals("(510) 794-6850", g.getHeader().getSourceSystem().getCorporation().getPhoneNumbers().get(0).getValue());

        // There are two sources in this file, and their names should be as
        // shown
        assertEquals(2, g.getSources().size());
        for (Source s : g.getSources().values()) {
            assertTrue(s.getTitle().get(0).equals("William Barnett Family.FTW") || s.getTitle().get(0).equals(
                    "Warrick County, IN WPA Indexes"));
        }

        assertEquals(17, g.getFamilies().size());
        assertEquals(64, g.getIndividuals().size());

        // Check a specific family
        Family family = g.getFamilies().get("@F1428@");
        assertNotNull(family);
        assertEquals(3, family.getChildren().size());
        assertEquals("Lawrence Henry /Barnett/", family.getHusband().getNames().get(0).getBasic());
        assertEquals("Velma //", family.getWife().getNames().get(0).getBasic());

    }

    /**
     * Test loading a file with a different line terminator sequence.
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if there's a problem parsing the data
     */
    @Test
    public void testLoad4() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        // Different line end char seq than the other file
        gp.load("sample/TGC551LF.ged");
        checkTGC551LF(gp);
    }

    /**
     * Test loading a minimal GEDCOM 5.5 file that only has a submitter. This test uses a file which indents lines by their tag
     * level, even though the spec says not to. However, the spec also says to ignore leading spaces on lines, so we're doing that.
     * See issue 57.
     * 
     * @throws IOException
     *             if the data cannot be written
     * @throws GedcomParserException
     *             if the data cannot be parsed
     */
    @Test
    public void testLoadIndentedMinimal55File() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/minimal55indented.ged");
        // No problems detected, right?
        assertTrue(gp.getErrors().isEmpty());
        assertTrue(gp.getWarnings().isEmpty());

        // And the data is as we expected, right?
        Gedcom g = gp.getGedcom();
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
     *             if the data cannot be written
     * @throws GedcomParserException
     *             if the data cannot be parsed
     */
    @Test
    public void testLoadMinimal55File() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/minimal55.ged");
        // No problems detected, right?
        assertTrue(gp.getErrors().isEmpty());
        assertTrue(gp.getWarnings().isEmpty());

        // And the data is as we expected, right?
        Gedcom g = gp.getGedcom();
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
    @Test
    public void testLoadStream() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        try (InputStream stream = new FileInputStream("sample/TGC551LF.ged");
                BufferedInputStream bis = new BufferedInputStream(stream)) {
            gp.load(bis);
            checkTGC551LF(gp);
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
    @Test
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
    @Test
    public void testLoadTGC55CLF() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/TGC55CLF.ged");
        checkTGC55C(gp);
    }

    /**
     * The same sample file is used several times, this helper method ensures consistent assertions for all tests using the same
     * file
     * 
     * @param gp
     *            the {@link GedcomParser}
     */
    private void checkTGC551LF(GedcomParser gp) {
        assertTrue(gp.getErrors().isEmpty());
        assertTrue(gp.getWarnings().isEmpty());
        Gedcom g = gp.getGedcom();
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
     * The same sample file is used several times, this helper method ensures consistent assertions for all tests using the same
     * file
     * 
     * @param gp
     *            the {@link GedcomParser}
     */
    @SuppressWarnings({ "checkstyle:methodlength", "PMD.NcssMethodCount", "PMD.ExcessiveMethodLength" })
    private void checkTGC55C(GedcomParser gp) {
        Individual indi;
        PersonalName name;
        Note note;
        Multimedia multimedia;
        FileReference fileReference;
        CitationWithSource citWithSource;
        CitationWithoutSource citWithoutSource;
        Source source;

        assertTrue(gp.getErrors().isEmpty());
        assertTrue(gp.getWarnings().isEmpty());
        Gedcom g = gp.getGedcom();
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

        assertEquals(3, indi.getCitations().size());
        assertEquals(2, indi.getNames().size());
        assertEquals(2, indi.getNotes().size());

        // Name 0
        name = indi.getNames().get(0);
        assertEquals("Joseph Tag /Torture/", name.getBasic());
        assertEquals("Torture, Joseph \"Joe\"", name.toString());

        assertEquals(1, name.getCitations().size());
        assertEquals(1, name.getNotes().size());

        // Name 0 - Citation 0
        assertTrue(name.getCitations().get(0) instanceof CitationWithSource);
        citWithSource = (CitationWithSource) name.getCitations().get(0);
        source = citWithSource.getSource();

        assertEquals("@SOURCE1@", source.getXref());
        assertEquals("42", citWithSource.getWhereInSource().toString());

        if (Options.isCollectionInitializationEnabled()) {
            assertEquals(0, citWithSource.getMultimedia().size());
            assertEquals(0, citWithSource.getNotes().size());
        } else {
            assertNull(citWithSource.getMultimedia());
            assertNull(citWithSource.getNotes());
        }

        // Name 0 - Note 0
        note = name.getNotes().get(0);
        assertEquals(5, note.getLines().size());
        assertEquals(
                "These are notes about the first NAME structure in this record. These notes are embedded in the INDIVIDUAL record itself.",
                note.getLines().get(0));

        // Name 1
        name = indi.getNames().get(1);
        assertEquals("William John /Smith/", name.getBasic());
        assertEquals("William John /Smith/", name.toString());

        assertEquals(1, name.getCitations().size());
        assertEquals(1, name.getNotes().size());

        // Name 1 - Citation 0
        assertTrue(name.getCitations().get(0) instanceof CitationWithSource);
        citWithSource = (CitationWithSource) name.getCitations().get(0);
        source = citWithSource.getSource();

        assertEquals("@SOURCE1@", source.getXref());
        assertEquals("55", citWithSource.getWhereInSource().toString());

        assertEquals(1, citWithSource.getMultimedia().size());
        assertEquals(1, citWithSource.getNotes().size());

        // Name 1 - Multimedia 0
        multimedia = citWithSource.getMultimedia().get(0);
        if (Options.isCollectionInitializationEnabled()) {
            assertEquals(0, multimedia.getCitations().size());
        } else {
            assertNull(multimedia.getCitations());
        }
        assertEquals(1, multimedia.getFileReferences().size());
        assertEquals(1, multimedia.getNotes().size());

        // Name 1 - Multimedia 0 - FileReference 0
        fileReference = multimedia.getFileReferences().get(0);
        assertEquals("jpeg", fileReference.getFormat().toString());
        assertEquals(null, fileReference.getMediaType());
        assertEquals("ImgFile.JPG", fileReference.getReferenceToFile().toString());
        assertEquals(null, fileReference.getTitle());

        // Name 1 - Multimedia 0 - Note 0
        note = multimedia.getNotes().get(0);
        assertEquals(1, note.getLines().size());
        assertEquals("These are some notes of this multimedia link in the NAME structure.", note.getLines().get(0));

        // Name 1 - Citation 0 - Note 0
        note = citWithSource.getNotes().get(0);
        assertNotNull(note);
        assertNotNull(note.getLines());
        assertEquals(3, note.getLines().size());
        assertEquals("This source citation has all fields possible in a source citation to a separate SOURCE record. "
                + "Besides the link to the SOURCE record there are possible fields about this citation (e.g., PAGE, TEXT, etc.)",
                note.getLines().get(0));

        // Name 1 - Note 0
        note = name.getNotes().get(0);
        assertEquals(3, note.getLines().size());
        assertEquals("This is a second personal NAME structure in a single INDIVIDUAL record which is allowed in GEDCOM. "
                + "This second NAME structure has all possible fields for a NAME structure.", note.getLines().get(0));

        // Note 0
        note = indi.getNotes().get(0);
        assertEquals(40, note.getLines().size());
        assertEquals("Comments on \"Joseph Tag Torture\" INDIVIDUAL Record.", note.getLines().get(0));

        // Note 1
        note = indi.getNotes().get(1);
        assertEquals(3, note.getLines().size());
        assertEquals("This is a second set of notes for this single individual record. "
                + "It is embedded in the INDIVIDUAL record instead of being in a separate NOTE record.", note.getLines().get(0));

        // Citation 0
        assertTrue(indi.getCitations().get(0) instanceof CitationWithSource);
        citWithSource = (CitationWithSource) indi.getCitations().get(0);
        source = citWithSource.getSource();

        assertEquals("@SOURCE1@", source.getXref());
        assertEquals("42", citWithSource.getWhereInSource().toString());

        assertEquals(0, citWithSource.getMultimedia(true).size());
        assertEquals(1, citWithSource.getNotes().size());

        // Citation 0 - Note 0
        note = citWithSource.getNotes().get(0);
        assertEquals(1, note.getLines().size());
        assertEquals("A source note.", note.getLines().get(0));

        // Citation 1
        assertTrue(indi.getCitations().get(1) instanceof CitationWithSource);
        citWithSource = (CitationWithSource) indi.getCitations().get(1);
        source = citWithSource.getSource();

        assertEquals("@SR2@", source.getXref());
        assertEquals(null, citWithSource.getWhereInSource());

        assertEquals(0, citWithSource.getMultimedia(true).size());
        assertEquals(1, citWithSource.getNotes().size());

        // Citation 1 - Note 0
        note = citWithSource.getNotes().get(0);
        assertEquals(1, note.getLines().size());
        assertEquals("This is a second source citation in this record.", note.getLines().get(0));

        // Citation 2
        assertTrue(indi.getCitations().get(2) instanceof CitationWithoutSource);
        citWithoutSource = (CitationWithoutSource) indi.getCitations().get(2);

        assertEquals(1, citWithoutSource.getNotes().size());

        // Citation 2 - Note 0
        note = citWithoutSource.getNotes().get(0);
        assertEquals(1, note.getLines().size());
        assertEquals(
                "How does software handle embedded SOURCE records on import? Such source citations are common in old GEDCOM files. "
                        + "More modern GEDCOM files should use source citations to SOURCE records.", note.getLines().get(0));
    }
}
