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
package com.mattharrah.gedcom4j.writer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import junit.framework.TestCase;

import com.mattharrah.gedcom4j.model.Family;
import com.mattharrah.gedcom4j.model.Gedcom;
import com.mattharrah.gedcom4j.model.Header;
import com.mattharrah.gedcom4j.model.Individual;
import com.mattharrah.gedcom4j.parser.GedcomParser;
import com.mattharrah.gedcom4j.parser.GedcomParserException;

/**
 * A test for the {@link GedcomWriter} class. The majority of the testing done
 * by this class is done by reading the torture test file, writing it out to a
 * temp file, reading the temp file back in, and comparing that the readback
 * data is equivalent to what we wrote from. Additionally, there are
 * string-based tests on the contents of the written file, just in case the
 * parser is part of the problem.
 * 
 * @author frizbog1
 * 
 */
public class GedcomWriterTest extends TestCase {

    /**
     * The name of the file used for stress-testing the parser
     */
    private static final String SAMPLE_STRESS_TEST_FILENAME = "sample/TGC551.ged";
    /**
     * The original gedcom structure read in from the torture test file.
     */
    private Gedcom gedcomOrig;
    /**
     * The read-back gedcom structure read in from the file we write as part of
     * the test.
     */
    private Gedcom gedcomReadback;
    /**
     * The entire contents of the written out file, as a string.
     */
    private String writtenFileAsString;

    /**
     * Constructor. Does some test fixture initialization once for the whole
     * class rather than in setUp().
     * 
     * @throws IOException
     *             if there's a file i/o error
     * @throws GedcomParserException
     *             if a gedcom file won't parse
     * @throws GedcomWriterException
     *             if a gedcom data structure can't be written (usually due to
     *             invalid data, shouldn't happen)
     */
    public GedcomWriterTest() throws IOException, GedcomParserException,
            GedcomWriterException {
        // Load a file
        GedcomParser p = new GedcomParser();
        p.load(SAMPLE_STRESS_TEST_FILENAME);
        gedcomOrig = p.gedcom;

        GedcomWriter gw = new GedcomWriter(gedcomOrig);
        File tempFile = new File("tmp/gedcom4j.writertest.ged");
        gw.write(tempFile);

        writtenFileAsString = loadFileIntoString(tempFile);

        // Reload the file we just wrote
        p = new GedcomParser();
        p.load(tempFile.getAbsolutePath());
        gedcomReadback = p.gedcom;
        for (String e : p.errors) {
            System.err.println(e);
        }
        assertTrue(p.errors.isEmpty());
    }

    /**
     * Test that the families are written out and read back the same
     */
    public void testFamilies() {
        Map<String, Family> f1 = gedcomOrig.families;
        Map<String, Family> f2 = gedcomReadback.families;
        assertNotSame(f1, f2);
        assertEquals(f1.keySet().size(), f2.keySet().size());
        assertTrue(writtenFileAsString.contains("0 @F6@ FAM\n"
                + "1 HUSB @I14@\n1 WIFE @I13@\n1 CHIL @PERSON5@\n"
                + "1 NOTE @N37@\n1 RIN 6\n1 CHAN\n"
                + "2 DATE 11 Jan 2001\n3 TIME 16:51:48\n"));
        assertEquals(f1, f2);
    }

    /**
     * Check if headers are written out and read back the same
     */
    public void testHeader() {
        Header h1 = gedcomOrig.header;
        Header h2 = gedcomReadback.header;
        assertNotSame(h1, h2);
        assertNotNull(h1);

        assertTrue(
                "File as read back does not start as expected",
                writtenFileAsString
                        .startsWith("0 HEAD\n"
                                + "1 SOUR GEDitCOM\n"
                                + "2 VERS 2.9.4\n"
                                + "2 NAME GEDitCOM\n"
                                + "2 CORP RSAC Software\n"
                                + "3 ADDR 7108 South Pine Cone Street\n"
                                + "4 CONT Salt Lake City, UT 84121\n"
                                + "4 CONT USA\n"
                                + "4 ADR1 RSAC Software\n"
                                + "4 ADR2 7108 South Pine Cone Street\n"
                                + "4 CITY Salt Lake City\n"
                                + "4 STAE UT\n"
                                + "4 POST 84121\n"
                                + "4 CTRY USA\n"
                                + "3 PHON +1-801-942-7768\n"
                                + "3 PHON +1-801-555-1212\n"
                                + "3 PHON +1-801-942-1148 (FAX) (last one!)\n"
                                + "2 DATA Name of source data\n"
                                + "3 DATE 1 JAN 1998\n"
                                + "3 COPR Copyright of source data\n"
                                + "1 DEST ANSTFILE\n"
                                + "1 DATE 1 JAN 1998\n"
                                + "2 TIME 13:57:24.80\n"
                                + "1 SUBM @SUBMITTER@\n"
                                + "1 SUBN @SUBMISSION@\n"
                                + "1 FILE gedcom4j.writertest.ged\n"
                                + "1 COPR Ã 1997 by H. Eichmann, parts Ã 1999-2000 by J. A. Nairn.\n"
                                + "1 GEDC\n2 VERS 5.5\n"
                                + "2 FORM LINEAGE-LINKED\n1 CHAR ANSEL\n"
                                + "2 VERS ANSI Z39.47-1985\n1 LANG English\n"
                                + "1 PLAC\n2 FORM City, County, State, Country\n"));
        assertTrue(
                "File as read back does not deal with line breaks in continuation lines as expected",
                writtenFileAsString
                        .contains("1 NOTE This file demonstrates all tags that are allowed in GEDCOM 5.5. "
                                + "Here are some comments about the HEADER record and\n"
                                + "2 CONC comments about where to look for information on the other 9 types of "
                                + "GEDCOM records. Most other records will have their own\n"
                                + "2 CONC notes that describe what to look for in that record and what to hope the "
                                + "importing software will find.\n"
                                + "2 CONT \n"
                                + "2 CONT Many applications will fail to import these notes. The notes are therefore "
                                + "also provided with the files as a plain-text\n"
                                + "2 CONC \"Read-Me\" file."));
        assertTrue(
                "File as read back does not preserve whitespace as expected",
                writtenFileAsString
                        .contains("2 CONT \n"
                                + "2 CONT      Name: Standard GEDCOM Filelinks\n"
                                + "2 CONT      Name: Nonstandard Multimedia Filelinks\n"
                                + "2 CONT      Name: General Custom Filelinks\n"
                                + "2 CONT      Name: Extra URL Filelinks\n"
                                + "2 CONT           These records link to"));

    }

    /**
     * Test that the individuals written and read back match the originals
     */
    public void testIndividuals() {
        Map<String, Individual> f1 = gedcomOrig.individuals;
        Map<String, Individual> f2 = gedcomReadback.individuals;
        assertNotSame(f1, f2);
        assertEquals(f1.keySet().size(), f2.keySet().size());
    }

    /**
     * Test for embedded multimedia objects at the top level of the gedcom
     * structure
     */
    public void testMultimedia() {
        assertNotSame(gedcomOrig.multimedia, gedcomReadback.multimedia);
        assertTrue(writtenFileAsString.contains("1 OBJE\n2 FORM jpeg\n"
                + "2 TITL Submitter Multimedia File\n2 FILE ImgFile.JPG\n"
                + "2 NOTE @N1@\n1 "));
        assertTrue(writtenFileAsString
                .contains("0 @M1@ OBJE\n"
                        + "1 FORM PICT\n"
                        + "1 TITL Dummy Multimedia Object\n"
                        + "1 NOTE Here are some notes on this multimedia object.\n"
                        + "2 CONT If decoded it should be an image of a flower.\n"
                        + "1 BLOB\n"
                        + "2 CONT .HM.......k.1..F.jwA.Dzzzzw............A....1.........0U.66..E.8\n"
                        + "2 CONT .......A..k.a6.A.......A..k.........../6....G.......0../..U.....\n"
                        + "2 CONT .w1/m........HC0..../...zzzzzzzz..5zzk..AnA..U..W6U....2rRrRrRrR\n"
                        + "2 CONT .Dw...............k.1.......1..A...5ykE/zzzx/.g//.Hxzk6/.Tzy/.k1\n"
                        + "2 CONT /Dw/.Tvz.E5zzUE9/kHz.Tw2/DzzzEEA.kE2zk5yzk2/zzs21.U2/Dw/.Tw/.Tzy\n"
                        + "2 CONT /.fy/.HzzkHzzzo21Ds00.E2.UE2.U62/.k./Ds0.UE0/Do0..E8/UE2.U62.U9w\n"
                        + "2 CONT /.Tx/.20.jg2/jo2..9u/.0U.6A.zk\n"
                        + "1 REFN User Reference Number\n"
                        + "2 TYPE User Reference Type\n1 RIN 1\n"
                        + "1 CHAN\n2 DATE 14 Jan 2001\n3 TIME 14:10:31"));
        assertEquals(gedcomOrig.multimedia, gedcomReadback.multimedia);
    }

    /**
     * Check that the notes are written out and readback equivalently.
     */
    public void testNotes() {
        assertNotSame(gedcomOrig.notes, gedcomReadback.notes);
        assertTrue(writtenFileAsString
                .contains("0 @N19@ NOTE A note about this LDS spouse sealing source.\n"
                        + "1 CHAN\n2 DATE 12 Mar 2000\n3 TIME 12:32:13\n"));
        assertEquals(gedcomOrig.notes, gedcomReadback.notes);
    }

    /**
     * Test the writing of repositories.
     */
    public void testRepositories() {
        assertTrue(
                "The file as read back should have repository @R1@ in the expected format",
                writtenFileAsString.contains("0 @R1@ REPO\n"
                        + "1 NAME Family History Library\n"
                        + "1 ADDR 35 North West Temple\n"
                        + "2 CONT Salt Lake City, UT 84111\n2 CONT USA\n"
                        + "2 ADR1 35 North West Temple\n"
                        + "2 ADR2 Across the street from Temple Square\n"
                        + "2 CITY Salt Lake City\n2 STAE Utah\n"
                        + "2 POST 84111\n2 CTRY USA\n1 NOTE @N2@\n"
                        + "1 REFN User Ref Number\n2 TYPE Sample\n"
                        + "1 RIN 1\n"
                        + "1 PHON +1-801-240-2331 (information)\n"
                        + "1 PHON +1-801-240-1278 (gifts & donations)\n"
                        + "1 PHON +1-801-240-2584 (support)\n1 CHAN\n"
                        + "2 DATE 12 Mar 2000\n3 TIME 10:36:02"));
        assertEquals("Lists of repositories should be equal",
                gedcomOrig.repositories, gedcomReadback.repositories);
    }

    /**
     * Check that the sources are written out and readback identically.
     */
    public void testSources() {
        assertNotSame(gedcomOrig.sources, gedcomReadback.sources);
        assertTrue(writtenFileAsString.contains("0 @SR2@ SOUR\n"
                + "1 AUTH Second Source Author\n"
                + "1 TITL All I Know About GEDCOM, I Learned on the Internet\n"
                + "1 ABBR What I Know About GEDCOM\n1 NOTE @N16@\n"
                + "1 RIN 2\n1 CHAN\n2 DATE 11 Jan 2001\n3 TIME 16:21:39"));
        assertEquals(gedcomOrig.sources, gedcomReadback.sources);
    }

    /**
     * Test if the gedcom files have the right submitter/submission records.
     */
    public void testSubmitterSubmissions() {
        assertEquals(gedcomOrig.submission, gedcomReadback.submission);
        assertEquals(gedcomOrig.submitters, gedcomReadback.submitters);
        assertTrue(
                "File as read back does not contain submission record as expected",
                writtenFileAsString.contains("0 @SUBMISSION@ SUBN\n"
                        + "1 SUBM @SUBMITTER@\n1 FAMF NameOfFamilyFile\n"
                        + "1 TEMP Abbreviated Temple Code\n1 ANCE 1\n"
                        + "1 DESC 1\n1 ORDI yes\n1 RIN 1\n"));
        assertTrue(
                "File as read back is does not have the primary submitter included as expected",
                writtenFileAsString.contains("0 @SUBMITTER@ SUBM\n"
                        + "1 NAME John A. Nairn\n"
                        + "1 ADDR Submitter address line 1\n"
                        + "2 CONT Submitter address line 2\n"
                        + "2 CONT Submitter address line 3\n"
                        + "2 CONT Submitter address line 4\n"
                        + "2 ADR1 Submitter address line 1\n"
                        + "2 ADR2 Submitter address line 2\n"
                        + "2 CITY Submitter address city\n"
                        + "2 STAE Submitter address state\n"
                        + "2 POST Submitter address ZIP code\n"
                        + "2 CTRY Submitter address country\n" + "1 OBJE\n"
                        + "2 FORM jpeg\n"
                        + "2 TITL Submitter Multimedia File\n"
                        + "2 FILE ImgFile.JPG\n" + "2 NOTE @N1@\n"
                        + "1 LANG English\n"
                        + "1 PHON Submitter phone number 1\n"
                        + "1 PHON Submitter phone number 2\n"
                        + "1 PHON Submitter phone number 3 (last one!)\n"
                        + "1 RFN Submitter Registered RFN\n"
                        + "1 RIN 1\n1 CHAN\n"
                        + "2 DATE 7 Sep 2000\n3 TIME 8:35:36"));
        assertTrue(
                "File as read back does not have the expected secondary submitter",
                writtenFileAsString
                        .contains("0 @SM2@ SUBM\n"
                                + "1 NAME Secondary Submitter\n"
                                + "1 ADDR Secondary Submitter Address 1\n"
                                + "2 CONT Secondary Submitter Address 2\n1 LANG English\n"
                                + "1 RIN 2\n1 CHAN\n2 DATE 12 Mar 2000\n3 TIME 10:38:33"));

    }

    /**
     * Test writing a gedcom structure for the degenerate case when the data is
     * empty. Writes the data, reads it back, and compares it to a fixed string
     * containing the expected contents.
     * 
     * @throws IOException
     *             if some file somewhere can't be read or written
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    public void testWriteEmptyGedcom() throws IOException,
            GedcomWriterException {
        // Write an empty file
        Gedcom g = new Gedcom();
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = true;
        File tempFile = new File("tmp/gedcom4j.emptywritertest.ged");
        gw.write(tempFile);

        // Read back the empty file and check its contents
        String string = loadFileIntoString(tempFile);

        assertEquals("0 HEAD\n1 FILE gedcom4j.emptywritertest.ged\n"
                + "1 GEDC\n2 VERS 5.5\n2 FORM LINEAGE-LINKED\n"
                + "1 CHAR ANSEL\n0 @SUBMISSION@ SUBN\n0 TRLR\n", string);

    }

    /**
     * Set up the test fixtures. Load a file, rewrite it, reload the written
     * file, so comparisons can be made.
     * 
     * @see junit.framework.TestCase#setUp()
     * @throws Exception
     *             if anything goes wrong
     */
    @Override
    protected void setUp() throws Exception {
        // Make sure we actually have test fixtures to work with
        assertNotNull(gedcomOrig);
        assertNotNull(gedcomReadback);
        assertNotNull(writtenFileAsString);
    }

    /**
     * Loads a file into a string for easy test assertions
     * 
     * @param file
     *            the file
     * @return the file's contents in a string
     * @throws IOException
     *             if the file can't be read or written
     */
    private String loadFileIntoString(File file) throws IOException {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        FileReader fileReader = new FileReader(file);
        BufferedReader br = new BufferedReader(fileReader);
        try {
            String s = br.readLine();
            while (s != null) {
                pw.println(s);
                s = br.readLine();
            }
            pw.flush();
            String string = sw.toString();
            return string;
        } finally {
            br.close();
            pw.close();
        }
    }
}
