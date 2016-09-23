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
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.io.reader.GedcomFileReader;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.validate.Validator;
import org.gedcom4j.validate.Validator.Finding;
import org.junit.Test;

/**
 * Test for writing custom tags out (after reading them)
 * 
 * @author frizbog
 */
public class CustomFactsWriterTest {

    /**
     * Test loading a file full of custom tags, rewriting it, and comparing
     * 
     * @throws IOException
     *             if the file cannot be read
     * @throws GedcomParserException
     *             if the file cannot be parsed
     * @throws GedcomWriterException
     *             if the file cannot be (completely) written
     */
    @Test
    @SuppressWarnings("PMD.SystemPrintln")
    public void test() throws IOException, GedcomParserException, GedcomWriterException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/ftmcustomtags.ged");
        Gedcom g = gp.getGedcom();
        assertNotNull(g);

        assertEquals(1, gp.getErrors().size());
        assertEquals(2, gp.getWarnings().size());

        g.getIndividuals().get("@I1@").getEventsOfType(IndividualEventType.EVENT).get(0).setDescription((String) null);
        g.getMultimedia().get("@M1@").getFileReferences().get(0).setFormat("jpg");

        Validator v = new Validator(g);
        v.validate();
        for (Finding f : v.getResults().getAllFindings()) {
            System.out.println(f);
            System.out.println(f.getStackTrace());
        }

        GedcomWriter gw = new GedcomWriter(g);
        gw.write("tmp/ftmcustomtags.ged");

        String original = readFileAsString(new File("sample/ftmcustomtags.ged"));
        String replica = readFileAsString(new File("tmp/ftmcustomtags.ged"));

        assertTrue(replica.contains("1 _MISN It was swell\n" + "2 DATE BET 01 JAN AND 31 AUG 1977\n"
                + "2 PLAC Pocatello, Bannock, Idaho, USA"));
        assertTrue(replica.contains("1 _DNA 99% Human\n" + "2 DATE 31 OCT 1990\n"
                + "2 PLAC Poughkeepsie, Dutchess, New York, USA"));
        assertTrue(replica.contains("1 _FUN Nobody showed up\n" + "2 DATE 04 JUL 2000\n"
                + "2 PLAC Honolulu, Honolulu, Hawaii, USA"));
        assertTrue(replica.contains("1 _ORDI Frying pans were not involved\n" + "2 DATE 09 SEP 1979\n"
                + "2 PLAC Temecula, Riverside, California, USA"));
        assertTrue(replica.contains("1 _MILTID 1234567\n" + "2 DATE 06 JUN 1966\n"
                + "2 PLAC Walla Walla, Walla Walla, Washington, USA"));
        assertTrue(replica.contains("1 _EMPLOY Keeling moose and squirrel\n" + "2 DATE 1981\n"
                + "2 PLAC Frostbite Falls, Minnesota, USA"));
        assertTrue(replica.contains("1 _WEIG 165lb\n" + "2 SOUR @S1@\n" + "3 PAGE p1\n" + "3 DATA\n"
                + "4 TEXT “John weighs 165 lbs.”\n" + "3 QUAY 3\n" + "3 _JUST Because it was an awesome napkin.\n"
                + "3 _LINK http://gedcom4j.org"));
        assertTrue(replica.contains("1 _INIT Wore a blue tie\n" + "2 DATE 03 MAR 1970\n"
                + "2 PLAC Kalamazoo, Kalamazoo, Michigan, USA"));
        assertTrue(replica.contains("1 _DEST Alpha Centauri"));
        assertTrue(replica.contains("1 _NAMS Mr. Phil Philanganes (the Phamous Photographer)"));
        assertTrue(replica.contains("1 _MDCL Decapitated, but recovered\n" + "2 DATE 08 AUG 1978\n"
                + "2 PLAC San Antonio, Bexar, Texas, USA"));
        assertTrue(replica.contains("1 _ELEC Mayor of Munchkin City\n" + "2 DATE 08 NOV 1992\n"
                + "2 PLAC Orlando, Brevard, Florida, USA"));
        assertTrue(replica.contains("1 _HEIG 6' 2\""));
        assertTrue(replica.contains("1 _CIRC Dr. Streck performed\n" + "2 DATE 08 JAN 1950\n"
                + "2 PLAC Colonial Heights, Independent Cities, Virginia, USA"));
        assertTrue(replica.contains("1 _ORIG Cabbage Patch"));
        assertTrue(replica.contains("1 BIRT\n" + "2 DATE 01 JAN 1950"));
        assertTrue(replica.contains("1 _MILT Garbage Pail Scrubber\n" + "2 DATE BET 06 JUN 1966 AND 07 JUL 1967\n"
                + "2 PLAC Fort Bragg, Cumberland, North Carolina, USA"));
        assertTrue(replica.contains("1 _EXCM Church of the Flying Spaghetti Monster\n" + "2 DATE 22 NOV 1982\n"
                + "2 PLAC Washington City, District Of Columbia, District of Columbia, USA"));
        assertTrue(replica.contains("1 DEAT\n" + "2 DATE 30 JUN 2000"));
        assertTrue(replica.contains("1 _DCAUSE Alien abduction"));
        assertTrue(replica.contains("1 _PHOTO @M1@"));
        assertTrue(replica.contains("0 @I3@ INDI\n" + "1 NAME June /Smith/\n" + "1 SEX F\n" + "1 FAMS @F1@"));
        assertTrue(replica.contains("0 @I2@ INDI\n" + "1 NAME Jesse /Johnson/\n" + "1 SEX M\n" + "1 FAMS @F1@"));
        assertTrue(replica.contains("0 @F1@ FAM\n" + "1 HUSB @I2@\n" + "1 WIFE @I3@\n" + "1 CHIL @I1@"));
        assertTrue(replica.contains("2 _FREL Step"));
        assertTrue(replica.contains("2 _MREL Guardian"));
        assertTrue(replica.contains("1 _SEPR There was a big fight\n" + "2 DATE 05 MAY 1955\n"
                + "2 PLAC Glad Valley, Ziebach, South Dakota, USA"));
        assertTrue(replica.contains("0 @S1@ SOUR\n" + "1 TITL Napkin Drawing"));
        assertTrue(replica.contains("0 @M1@ OBJE\n" + "1 FILE Vitruvian_man.jpg\n" + "2 TITL Vitruvian_man\n"
                + "2 DATE 9/19/16, 11:40:36 AM\n"));
        assertTrue(replica.contains("1 OBJE @M1@"));
        assertTrue(replica.contains("1 FAMS @F2@"));
        assertTrue(replica.contains("1 FAMC @F1@"));
        assertTrue(replica.contains(""));
        assertTrue(replica.contains(""));
        assertTrue(replica.contains(""));
        assertTrue(replica.contains(""));
        assertTrue(replica.contains(""));
        assertTrue(replica.contains(""));
        assertTrue(replica.contains(""));
        assertTrue(replica.contains(""));

        assertEquals(original.length(), replica.length());

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
    private String readFileAsString(File fileToRead) throws IOException, GedcomParserException {
        StringBuilder result = new StringBuilder(10000);
        try (FileInputStream fis = new FileInputStream(fileToRead); BufferedInputStream bis = new BufferedInputStream(fis);) {
            GedcomFileReader gfr = new GedcomFileReader(new GedcomParser(), bis);
            String s = gfr.nextLine();
            while (s != null) {
                result.append(s);
                s = gfr.nextLine();
                if (s != null) {
                    // For purposes of this test, normalize all line terminators to a single CR
                    result.append("\n");
                }
            }

            return result.toString();
        }
    }

}
