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
package org.gedcom4j.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.exception.WriterCancelledException;
import org.gedcom4j.io.reader.GedcomFileReader;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.InMemoryGedcom;
import org.gedcom4j.model.Header;
import org.gedcom4j.model.IGedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.NoteRecord;
import org.gedcom4j.parser.GedcomParser;
import org.junit.Before;
import org.junit.Test;

/**
 * A test for the {@link GedcomWriter} class. The majority of the testing done by this class is done by reading the torture test
 * file, writing it out to a temp file, reading the temp file back in, and comparing that the readback data is equivalent to what we
 * wrote from. Additionally, there are string-based tests on the contents of the written file, just in case the parser is part of
 * the problem.
 * 
 * @author frizbog1
 * 
 */
@SuppressWarnings("PMD.TooManyMethods")
public class GedcomWriterTest {

    /**
     * The name of the file used for stress-testing the parser
     */
    private static final String SAMPLE_STRESS_TEST_FILENAME = "sample/TGC551.ged";

    /**
     * The original gedcom structure read in from the torture test file.
     */
    private final IGedcom gedcomOrig;

    /**
     * The read-back gedcom structure read in from the file we write as part of the test.
     */
    private final IGedcom gedcomReadback;

    /**
     * Lines that are read back from the files we write during the test
     */
    private List<String> readbackLines;

    /**
     * Constructor. Does some test fixture initialization once for the whole class rather than in setUp().
     * 
     * @throws IOException
     *             if there's a file i/o error
     * @throws GedcomParserException
     *             if a gedcom file won't parse
     * @throws GedcomWriterException
     *             if a gedcom data structure can't be written (usually due to invalid data, shouldn't happen)
     */
    @SuppressWarnings("PMD.SystemPrintln")
    public GedcomWriterTest() throws IOException, GedcomParserException, GedcomWriterException {
        // Load a file
        GedcomParser p = new GedcomParser(new InMemoryGedcom());
        p.load(SAMPLE_STRESS_TEST_FILENAME);
        gedcomOrig = p.getGedcom();

        GedcomWriter gw = new GedcomWriter(gedcomOrig);
        gw.setValidationSuppressed(true);
        File tmpDir = new File("tmp");
        tmpDir.mkdirs();
        File tempFile = new File("tmp/gedcom4j.writertest.ged");
        gw.write(tempFile);

        try (FileInputStream byteStream = new FileInputStream(tempFile);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(byteStream);) {
            GedcomFileReader gfr = new GedcomFileReader(new GedcomParser(new InMemoryGedcom()), bufferedInputStream);
            readbackLines = new ArrayList<>();
            String s = gfr.nextLine();
            while (s != null) {
                readbackLines.add(s);
                s = gfr.nextLine();
            }
        }

        // Reload the file we just wrote
        p = new GedcomParser(new InMemoryGedcom());
        p.load(tempFile.getAbsolutePath());
        for (String s : p.getErrors()) {
            System.err.println(s);
        }
        assertTrue(p.getErrors().isEmpty());
        assertTrue(p.getWarnings().isEmpty());
        gedcomReadback = p.getGedcom();
        for (String e : p.getErrors()) {
            System.err.println(e);
        }
        assertTrue(p.getErrors().isEmpty());
    }

    /**
     * Set up the test fixtures. Load a file, rewrite it, reload the written file, so comparisons can be made.
     * 
     * @see junit.framework.TestCase#setUp()
     * @throws Exception
     *             if anything goes wrong
     */
    @Before
    public void setUp() throws Exception {
        // Make sure we actually have test fixtures to work with
        assertNotNull(gedcomOrig);
        assertNotNull(gedcomReadback);
    }

    /**
     * Test that the families are written out and read back the same
     */
    @Test
    public void testFamilies() {
        Map<String, Family> fm1 = gedcomOrig.getFamilies();
        Map<String, Family> fm2 = gedcomReadback.getFamilies();
        assertNotSame(fm1, fm2);
        assertEquals(fm1.keySet(), fm2.keySet());
        for (Entry<String, Family> e : fm1.entrySet()) {
            Family f1 = e.getValue();
            Family f2 = fm2.get(e.getKey());
            assertEquals("Family " + f1.getXref() + " should be the same but isn't", f1, f2);
        }
    }

    /**
     * Check if headers are written out and read back the same
     */
    @Test
    public void testHeader() {
        Header h1 = gedcomOrig.getHeader();
        Header h2 = gedcomReadback.getHeader();
        assertNotSame(h1, h2);
        assertNotNull(h1);

        assertLineSequence("Header not as expected in readback", readbackLines, "0 HEAD", "1 SOUR GEDitCOM", "2 VERS 2.9.4",
                "2 NAME GEDitCOM", "2 CORP RSAC Software", "3 ADDR 7108 South Pine Cone Street", "4 CONT Salt Lake City, UT 84121",
                "4 CONT USA");

        assertLineSequence("Readback file with line breaks in CONC/CONT lines as expected", readbackLines,
                "1 NOTE This file demonstrates all tags that are allowed in GEDCOM 5.5. "
                        + "Here are some comments about the HEADER record and commen",
                "2 CONC ts about where to look for information on the other 9 types of "
                        + "GEDCOM records. Most other records will have their own notes that",
                "2 CONC  describe what to look for in that record and what to hope " + "the importing software will find.",
                "2 CONT ", "2 CONT Many applications will fail to import these notes. The notes are "
                        + "therefore also provided with the files as a plain-text \"", "2 CONC Read-Me\" file.");
        assertLineSequence("File as read back does not preserve whitespace as expected", readbackLines,
                "2 CONT INDIVIDUAL Records:", "2 CONT      This file has a small number of INDIVIDUAL records. "
                        + "The record named \"Joseph Tag Torture\" has all possible tags for ",
                "2 CONC an INDIVIDUAL record. All remaining  individuals have less tags. "
                        + "Some test specific features; for example:", "2 CONT ", "2 CONT      Name: Standard GEDCOM Filelinks",
                "2 CONT      Name: Nonstandard Multimedia Filelinks");

    }

    /**
     * Test that the individuals written and read back match the originals
     */
    @Test
    public void testIndividuals() {
        Map<String, Individual> f1 = gedcomOrig.getIndividuals();
        Map<String, Individual> f2 = gedcomReadback.getIndividuals();
        assertNotSame(f1, f2);
        assertEquals(f1.keySet().size(), f2.keySet().size());
    }

    /**
     * Test for embedded multimedia objects at the top level of the gedcom structure
     */
    @Test
    public void testMultimedia() {
        assertNotSame(gedcomOrig.getMultimedia(), gedcomReadback.getMultimedia());
        assertLineSequence("Multimedia via reference not found as expected", readbackLines, "2 FORM PICT",
                "2 TITL Macintosh PICT file", "2 FILE ImgFile.PIC", "1 OBJE", "2 FORM PNTG", "2 TITL Macintosh MacPaint file",
                "2 FILE ImgFile.MAC", "1 OBJE");
        assertLineSequence("Embedded and encoded multimedia not found as expected", readbackLines, "0 @M1@ OBJE", "1 FORM PICT",
                "1 TITL Dummy Multimedia Object", "1 NOTE Here are some notes on this multimedia object.",
                "2 CONT If decoded it should be an image of a flower.", "1 BLOB",
                "2 CONT .HM.......k.1..F.jwA.Dzzzzw............A....1.........0U.66..E.8",
                "2 CONT .......A..k.a6.A.......A..k.........../6....G.......0../..U.....",
                "2 CONT .w1/m........HC0..../...zzzzzzzz..5zzk..AnA..U..W6U....2rRrRrRrR",
                "2 CONT .Dw...............k.1.......1..A...5ykE/zzzx/.g//.Hxzk6/.Tzy/.k1",
                "2 CONT /Dw/.Tvz.E5zzUE9/kHz.Tw2/DzzzEEA.kE2zk5yzk2/zzs21.U2/Dw/.Tw/.Tzy",
                "2 CONT /.fy/.HzzkHzzzo21Ds00.E2.UE2.U62/.k./Ds0.UE0/Do0..E8/UE2.U62.U9w", "2 CONT /.Tx/.20.jg2/jo2..9u/.0U.6A.zk",
                "1 REFN User Reference Number", "2 TYPE User Reference Type", "1 RIN 1", "1 CHAN", "2 DATE 14 Jan 2001",
                "3 TIME 14:10:31");
        assertEquals(gedcomOrig.getMultimedia(), gedcomReadback.getMultimedia());
    }

    /**
     * Check that the notes are written out and readback equivalently.
     */
    @Test
    @SuppressWarnings("PMD.SystemPrintln")
    public void testNotes() {
        assertNotSame(gedcomOrig.getNotes(), gedcomReadback.getNotes());
        assertLineSequence("Note with xref and text on same line not found as expected", readbackLines,
                "0 @N19@ NOTE A note about this LDS spouse sealing source.", "1 CHAN", "2 DATE 12 Mar 2000", "3 TIME 12:32:13");
        assertEquals(gedcomOrig.getNotes().keySet(), gedcomReadback.getNotes().keySet());
        for (String xref : gedcomOrig.getNotes().keySet()) {
            NoteRecord n1 = gedcomOrig.getNotes().get(xref);
            NoteRecord n2 = gedcomReadback.getNotes().get(xref);
            assertEquals(n1.getLines().size(), n2.getLines().size());
            String prevLine = null;
            for (int i = 0; i < n1.getLines().size(); i++) {
                String l1 = n1.getLines().get(i);
                String l2 = n2.getLines().get(i);
                if (!l1.equals(l2)) {
                    System.out.println("On line following \"" + prevLine + "\":");
                    System.out.println("Should be " + l1);
                    System.out.println("Actual is " + l2);
                    for (int j = 0; j < l2.length(); j++) {
                        System.out.print(String.format("%s %04x == %s %04x", l1.charAt(j), (int) l1.charAt(j), l2.charAt(j),
                                (int) l2.charAt(j)));
                        if (l1.charAt(j) != l2.charAt(j)) {
                            System.out.print("  <<======");
                        }
                        System.out.println();
                    }
                }
                prevLine = l1;
                assertEquals("Note " + xref + " line " + i + " should equal but don't - preceding line in note was " + prevLine, l1,
                        l2);
            }
            assertEquals("Note " + xref + " should be equal, but isn't", n1, n2);
        }
    }

    /**
     * Test the writing of repositories.
     */
    @Test
    public void testRepositories() {
        assertLineSequence("Repository @R1@ not read back as expected", readbackLines, "0 @R1@ REPO",
                "1 NAME Family History Library", "1 ADDR 35 North West Temple", "2 CONT Salt Lake City, UT 84111", "2 CONT USA",
                "2 ADR1 35 North West Temple", "2 ADR2 Across the street from Temple Square", "2 CITY Salt Lake City",
                "2 STAE Utah", "2 POST 84111", "2 CTRY USA", "1 NOTE @N2@", "1 REFN User Ref Number", "2 TYPE Sample", "1 RIN 1",
                "1 PHON +1-801-240-2331 (information)", "1 PHON +1-801-240-1278 (gifts & donations)",
                "1 PHON +1-801-240-2584 (support)", "1 CHAN", "2 DATE 12 Mar 2000", "3 TIME 10:36:02");
        assertEquals("Lists of repositories should be equal", gedcomOrig.getRepositories(), gedcomReadback.getRepositories());
    }

    /**
     * Check that the sources are written out and readback identically.
     */
    @Test
    public void testSources() {
        assertNotSame(gedcomOrig.getSources(), gedcomReadback.getSources());
        assertLineSequence("Source @SR2@ not read back as expected", readbackLines, "0 @SR2@ SOUR", "1 AUTH Second Source Author",
                "1 TITL All I Know About GEDCOM, I Learned on the Internet", "1 ABBR What I Know About GEDCOM", "1 NOTE @N16@",
                "1 RIN 2", "1 CHAN", "2 DATE 11 Jan 2001", "3 TIME 16:21:39");
        assertEquals(gedcomOrig.getSources(), gedcomReadback.getSources());
    }

    /**
     * Test for {@link GedcomWriter#splitLinesOnBreakingCharacters(List)}
     * 
     * @throws WriterCancelledException
     *             if the writer operation is cancelled
     */
    @Test
    public void testSplitLines() throws WriterCancelledException {
        AbstractEmitter<IGedcom> gw = new GedcomWriter(null);
        List<String> original = new ArrayList<>();
        original.add("This is a test");
        List<String> result = gw.splitLinesOnBreakingCharacters(original);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("This is a test", result.get(0));

        original.add("This has a line break\nright in the middle");
        result = gw.splitLinesOnBreakingCharacters(original);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("This is a test", result.get(0));
        assertEquals("This has a line break", result.get(1));
        assertEquals("right in the middle", result.get(2));

        original.add("\nThis has\r\nlots of\rbreaking characters\n\rall over\nthe place\n");
        result = gw.splitLinesOnBreakingCharacters(original);
        assertNotNull(result);
        assertEquals(9, result.size());
        assertEquals("This is a test", result.get(0));
        assertEquals("This has a line break", result.get(1));
        assertEquals("right in the middle", result.get(2));
        assertEquals("", result.get(3));
        assertEquals("This has", result.get(4));
        assertEquals("lots of", result.get(5));
        assertEquals("breaking characters", result.get(6));
        assertEquals("all over", result.get(7));
        assertEquals("the place", result.get(8));
    }

    /**
     * Test if the gedcom files have the right submitter/submission records.
     */
    @Test
    public void testSubmitterSubmissions() {
        assertEquals(gedcomOrig.getSubmission(), gedcomReadback.getSubmission());
        assertEquals(gedcomOrig.getSubmitters(), gedcomReadback.getSubmitters());
        assertLineSequence("Submission @SUBMISSION@ not read back as expected", readbackLines, "0 @SUBMISSION@ SUBN",
                "1 SUBM @SUBMITTER@", "1 FAMF NameOfFamilyFile", "1 TEMP Abbreviated Temple Code", "1 ANCE 1", "1 DESC 1",
                "1 ORDI yes", "1 RIN 1");
        assertLineSequence("Primary submitter @SUBMITTER@ not read back as expected", readbackLines, "0 @SUBMITTER@ SUBM",
                "1 NAME John A. Nairn", "1 ADDR Submitter address line 1", "2 CONT Submitter address line 2",
                "2 CONT Submitter address line 3", "2 CONT Submitter address line 4", "2 ADR1 Submitter address line 1",
                "2 ADR2 Submitter address line 2", "2 CITY Submitter address city", "2 STAE Submitter address state",
                "2 POST Submitter address ZIP code", "2 CTRY Submitter address country", "1 OBJE", "2 FORM jpeg",
                "2 TITL Submitter Multimedia File", "2 FILE ImgFile.JPG", "2 NOTE @N1@", "1 LANG English",
                "1 PHON Submitter phone number 1", "1 PHON Submitter phone number 2", "1 PHON Submitter phone number 3 (last one!)",
                "1 RFN Submitter Registered RFN", "1 RIN 1", "1 CHAN", "2 DATE 7 Sep 2000", "3 TIME 8:35:36");
        assertLineSequence("File as read back does not have the expected secondary submitter", readbackLines, "0 @SM2@ SUBM",
                "1 NAME Secondary Submitter", "1 ADDR Secondary Submitter Address 1", "2 CONT Secondary Submitter Address 2",
                "1 LANG English", "1 RIN 2", "1 CHAN", "2 DATE 12 Mar 2000", "3 TIME 10:38:33");

    }

    /**
     * Test writing a gedcom structure for the degenerate case when the data is empty. Writes the data, reads it back, and compares
     * it to a fixed string containing the expected contents.
     * 
     * @throws IOException
     *             if some file somewhere can't be read or written
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     * @throws GedcomParserException
     *             if the file load was cancelled or included malformed data
     */
    @Test
    public void testWriteEmptyGedcom() throws IOException, GedcomWriterException, GedcomParserException {
        // Write an empty file
        IGedcom g = new InMemoryGedcom();
        GedcomWriter gw = new GedcomWriter(g);
        gw.setValidationSuppressed(true);
        File tempFile = new File("tmp/gedcom4j.emptywritertest.ged");
        gw.write(tempFile);

        // Read back the empty file and check its contents
        assertLineSequence("Empty file contents not as expected", readBack(tempFile), "0 HEAD", "1 SOUR UNSPECIFIED",
                "1 SUBM @SUBMITTER@", "1 FILE gedcom4j.emptywritertest.ged", "1 GEDC", "2 VERS 5.5.1", "2 FORM LINEAGE-LINKED",
                "1 CHAR ANSEL", "0 @SUBMISSION@ SUBN", "0 TRLR");

    }

    /**
     * <p>
     * Assert that a sequence of strings appears, in order, in the supplied {@link List} of strings.
     * </p>
     * 
     * @param failureMessage
     *            The message to use if there is a failure
     * 
     * @param lookIn
     *            the {@link List} of Strings to be searched
     * @param lookFor
     *            the strings to find in <code>lookIn</code>
     */
    @SuppressWarnings("PMD.SystemPrintln")
    private void assertLineSequence(String failureMessage, List<String> lookIn, String... lookFor) {
        int indexOf = lookIn.indexOf(lookFor[0]);
        if (indexOf < 0) {
            System.out.println("\n====");
            System.out.println("Looking for: ");
            System.out.println(lookFor[0]);
            System.out.println("Looking in:");
            for (String l : lookIn) {
                System.out.println(l);
            }
        }
        assertTrue(failureMessage + ": first string being looked for (\"" + lookFor[0]
                + "\") was not found in in the list being searched", indexOf >= 0);

        boolean matches = true; // optimistic
        for (int i = 0; i < lookFor.length; i++) {
            if (!lookFor[i].equals(lookIn.get(i + indexOf))) {
                matches = false;
                break;
            }
        }

        if (!matches) {
            System.out.println("\n----------------------------------");
            System.out.println(failureMessage);
            System.out.println("Line sequence mismatch");
            System.out.println("Starting at line " + indexOf);
            for (int i = 0; i < lookFor.length; i++) {
                if (!lookIn.get(indexOf + i).equals(lookFor[i])) {
                    System.out.println("+" + i);
                    System.out.println("lookIn : |" + lookIn.get(indexOf + i) + "|");
                    System.out.println("lookFor: |" + lookFor[i] + "|");
                    System.out.println();
                }
            }
        }
        assertTrue(failureMessage, matches);
    }

    /**
     * Read back the lines of a file
     * 
     * @param fileToRead
     *            the file to read
     * @return the lines of the file
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if the file load was cancelled or was malformed
     */
    private List<String> readBack(File fileToRead) throws IOException, GedcomParserException {
        try (FileInputStream fis = new FileInputStream(fileToRead); BufferedInputStream bis = new BufferedInputStream(fis);) {
            GedcomFileReader gfr = new GedcomFileReader(new GedcomParser(new InMemoryGedcom()), bis);
            List<String> result = new ArrayList<>();
            String s = gfr.nextLine();
            while (s != null) {
                result.add(s);
                s = gfr.nextLine();
            }

            return result;
        }
    }

}
