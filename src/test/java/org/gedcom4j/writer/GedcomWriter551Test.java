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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.model.FileReference;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.model.PersonalNameVariation;
import org.gedcom4j.model.Place;
import org.gedcom4j.model.TestHelper;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.model.enumerations.SupportedVersion;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.validate.Severity;
import org.gedcom4j.validate.Validator;
import org.gedcom4j.validate.Validator.Finding;
import org.junit.Test;

/**
 * Test some miscellaneous spec-specific stuff for GEDCOM 5.5.1 vs GEDCOM 5.5.
 * 
 * @author frizbog
 * 
 */
public class GedcomWriter551Test {

    /**
     * Test that Blob data is disallowed with 5.5.1
     * 
     * @throws IOException
     *             if the data can't be written to the tmp dir
     * @throws GedcomWriterException
     *             if the data is malformed - should never happen, because the code under test is checking for this
     */
    @Test
    @SuppressWarnings("PMD.SystemPrintln")
    public void testBlobWith551() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        GedcomWriter gw = new GedcomWriter(g);
        gw.setValidationSuppressed(false);
        gw.setAutoRepairResponder(Validator.AUTO_REPAIR_NONE);
        assertTrue(gw.lines.isEmpty());

        Multimedia m = new Multimedia();
        m.setEmbeddedMediaFormat("bmp");
        m.setXref("@M1@");
        g.getMultimedia().put(m.getXref(), m);
        m.getBlob(true).add("Blob data only allowed with 5.5");
        try {
            gw.write("tmp/delete-me.ged");
            if (!gw.getValidator().getResults().getAllFindings().isEmpty()) {
                System.out.println(this.getClass().getName() + " found " + gw.getValidator().getResults().getAllFindings().size()
                        + " validation findings:");
                for (Finding f : gw.getValidator().getResults().getAllFindings()) {
                    System.out.println(f);
                }
            }
            fail("Should have gotten a GedcomException about the blob data");
        } catch (@SuppressWarnings("unused") GedcomWriterException expectedAndIgnored) {
            boolean foundBlobError = false;
            for (Finding f : gw.getValidator().getResults().getAllFindings()) {
                if (f.getSeverity() == Severity.ERROR && f.getFieldNameOfConcern().contains("blob")) {
                    foundBlobError = true;
                }
            }
            assertTrue("Should have had a validation finding about the blob error", foundBlobError);
        }

        // Set to 5.5 and all should be fine
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        gw.write("tmp/delete-me.ged");

        // Set back to 5.5.1, clear the blob and embedded format, and all should
        // be fine
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
        m.getBlob().clear();
        m.setEmbeddedMediaFormat((String) null);
        gw.write("tmp/delete-me.ged");

    }

    /**
     * Test map coordinates on places - new in GEDCOM 5.5.1
     * 
     * @throws IOException
     *             if the data can't be written to the tmp dir
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     * @throws GedcomParserException
     *             if the data cannot be read back
     */
    @Test
    public void testMapCoords() throws IOException, GedcomWriterException, GedcomParserException {
        // Build up the test data
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
        GedcomWriter gw = new GedcomWriter(g);
        gw.setValidationSuppressed(false);
        assertTrue(gw.lines.isEmpty());
        Individual i = new Individual();
        i.setXref("@I1@");
        g.getIndividuals().put(i.getXref(), i);
        PersonalName pn = new PersonalName();
        pn.setBasic("Joe /Fryingpan/");
        i.getNames(true).add(pn);
        IndividualEvent e = new IndividualEvent();
        i.getEvents(true).add(e);
        e.setType(IndividualEventType.BIRTH);
        e.setPlace(new Place());
        e.getPlace().setPlaceName("Krakow, Poland");
        e.getPlace().setLatitude("+50\u00B0 3' 1.49\"");
        e.getPlace().setLongitude("+19\u00B0 56' 21.48\"");

        // Write the test data
        gw.write("tmp/writertest551.ged");

        // Read it back
        GedcomParser gp = new GedcomParser();
        gp.load("tmp/writertest551.ged");
        assertTrue(gp.getErrors().isEmpty());
        assertTrue(gp.getWarnings().isEmpty());

        // Look for expected data
        g = gp.getGedcom();
        assertNotNull(g);
        i = g.getIndividuals().get("@I1@");
        assertNotNull(i);
        assertEquals(1, i.getEvents().size());
        e = i.getEvents().get(0);
        assertEquals(IndividualEventType.BIRTH, e.getType());
        Place p = e.getPlace();
        assertNotNull(p);
        assertEquals("Krakow, Poland", p.getPlaceName());
        // while we're here...
        assertTrue(p.getRomanized() == null || p.getRomanized().isEmpty());
        assertTrue(p.getPhonetic() == null || p.getPhonetic().isEmpty());
        // ok, back to task at hand
        assertEquals("+50\u00B0 3' 1.49\"", p.getLatitude().getValue());
        assertEquals("+19\u00B0 56' 21.48\"", p.getLongitude().getValue());
    }

    /**
     * Test compatibility check with multi-line copyright and 5.5/5.5.1 data
     * 
     * @throws IOException
     *             if the data can't be written to the tmp dir
     * @throws GedcomWriterException
     *             if the data is malformed - should never happen, because the code under test is checking for this
     */
    @Test
    public void testMultilineCopyrightWith551Good() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().getCopyrightData(true).add("One line is ok");
        g.getHeader().getCopyrightData(true).add("Two lines is bad");
        GedcomWriter gw = new GedcomWriter(g);
        gw.setValidationSuppressed(false);
        assertTrue(gw.lines.isEmpty());

        // Switch to 5.5.1, all should be fine
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
        gw.write("tmp/delete-me.ged");

    }

    /**
     * Test compatibility check with multi-line copyright and 5.5/5.5.1 data
     * 
     * @throws IOException
     *             if the data can't be written to the tmp dir
     * @throws GedcomWriterException
     *             if the data is malformed - should never happen, because the code under test is checking for this
     */
    @Test(expected = GedcomWriterException.class)
    public void testMultilineCopyrightWith55Bad() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().getCopyrightData(true).add("One line is ok");
        g.getHeader().getCopyrightData(true).add("Two lines is bad");
        GedcomWriter gw = new GedcomWriter(g);
        gw.setValidationSuppressed(false);
        assertTrue(gw.lines.isEmpty());
        gw.write("tmp/delete-me.ged");

    }

    /**
     * Test compatibility check with multi-line copyright and 5.5/5.5.1 data
     * 
     * @throws IOException
     *             if the data can't be written to the tmp dir
     * @throws GedcomWriterException
     *             if the data is malformed - should never happen, because the code under test is checking for this
     */
    @Test
    public void testMultilineCopyrightWith55Good() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().getCopyrightData(true).add("One line is ok");
        GedcomWriter gw = new GedcomWriter(g);
        gw.setValidationSuppressed(false);
        assertTrue(gw.lines.isEmpty());

        // Only one line of copyright data, should be fine
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        gw.write("tmp/delete-me.ged");

    }

    /**
     * Test writing multimedia stuff in 5.5.1 format
     * 
     * @throws IOException
     *             if data cannot be written
     * @throws GedcomWriterException
     *             if the data cannot be written as a gedcom
     * @throws GedcomParserException
     *             if the written gedcom cannot be read back
     */
    @Test
    public void testMultimedia551() throws IOException, GedcomWriterException, GedcomParserException {
        // Create some data
        Gedcom g1 = TestHelper.getMinimalGedcom();
        Multimedia m1 = new Multimedia();
        m1.setXref("@M0@");
        g1.getMultimedia().put(m1.getXref(), m1);
        FileReference fr = new FileReference();
        fr.setReferenceToFile("C:/foo.gif");
        fr.setTitle("Foo");
        fr.setFormat("gif");
        fr.setMediaType("disk");
        m1.getFileReferences(true).add(fr);
        fr = new FileReference();
        fr.setReferenceToFile("C:/bar.png");
        fr.setFormat("png");
        fr.setTitle("Bar");
        m1.getFileReferences(true).add(fr);

        // Write it
        GedcomWriter gw = new GedcomWriter(g1);
        gw.write("tmp/writertest551.ged");

        // Read it back
        GedcomParser gp = new GedcomParser();
        gp.load("tmp/writertest551.ged");
        assertNotNull(gp.getGedcom());

        // See if we read back what we originally built
        Gedcom g2 = gp.getGedcom();
        assertEquals(1, g2.getMultimedia().size());
        Multimedia m2 = g2.getMultimedia().get("@M0@");
        assertNotNull(m2);
        assertNull(m2.getEmbeddedMediaFormat());
        assertNull(m2.getChangeDate());
        assertTrue(m2.getNotes(true).isEmpty());
        assertEquals(2, m2.getFileReferences().size());

        fr = m2.getFileReferences().get(0);
        assertEquals("C:/foo.gif", fr.getReferenceToFile().getValue());
        assertEquals("gif", fr.getFormat().getValue());
        assertNotNull(fr.getMediaType());
        assertEquals("disk", fr.getMediaType().getValue());
        assertEquals("Foo", fr.getTitle().getValue());
        fr = m2.getFileReferences().get(1);
        assertEquals("C:/bar.png", fr.getReferenceToFile().getValue());
        assertEquals("png", fr.getFormat().getValue());
        assertNull(fr.getMediaType());
        assertEquals("Bar", fr.getTitle().getValue());

    }

    /**
     * Test personal name variations
     * 
     * @throws IOException
     *             if data cannot be written
     * @throws GedcomWriterException
     *             if the data cannot be written as a gedcom
     */
    @Test
    public void testPersonalNameVariations() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        Individual i = new Individual();
        i.setXref("@I0001@");
        g.getIndividuals().put(i.getXref(), i);
        PersonalName pn = new PersonalName();
        pn.setBasic("Bj\u00F8rn /J\u00F8rgen/");
        i.getNames(true).add(pn);

        GedcomWriter gw = new GedcomWriter(g);
        gw.setValidationSuppressed(true);
        assertTrue(gw.lines.isEmpty());

        // Try basic scenario first before perturbing name with variations
        gw.write("tmp/delete-me.ged");

        // Add a malformed phonetic variation
        PersonalNameVariation pnv = new PersonalNameVariation();
        pn.getPhonetic(true).add(pnv);
        try {
            gw.write("tmp/delete-me.ged");
            fail("Expected to get a GedcomWriterException due to missing field on personal name variation");
        } catch (GedcomWriterException expected) {
            assertTrue(expected.getMessage().toLowerCase().contains("required value for tag fone"));
        }
        // Now fix it
        pnv.setVariation("Byorn /Yorgen/");
        gw.write("tmp/delete-me.ged");
        // Now fiddle with it further
        pnv.setVariationType("Typed it like it sounds, duh");
        gw.write("tmp/delete-me.ged");

        // Add a bad romanized variation
        pnv = new PersonalNameVariation();
        pn.getRomanized(true).add(pnv);
        try {
            gw.write("tmp/delete-me.ged");
            fail("Expected to get a GedcomWriterException due to missing field on personal name variation");
        } catch (GedcomWriterException expected) {
            assertTrue(expected.getMessage().toLowerCase().contains("required value for tag romn"));
        }
        // Now Fix it
        pnv.setVariation("Bjorn /Jorgen/");
        gw.write("tmp/delete-me.ged");
        // Now fiddle with it further
        pnv.setVariationType("Removed the slashes from the O's");
        gw.write("tmp/delete-me.ged");

    }

    /**
     * Test compatibility check with UTF-8 encoding and 5.5/5.5.1 data
     * 
     * @throws IOException
     *             if the data can't be written to the tmp dir
     * @throws GedcomWriterException
     *             if the data is malformed - should never happen, because the code under test is checking for this
     */
    @Test
    public void testUtf8With551() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        GedcomWriter gw = new GedcomWriter(g);
        gw.setValidationSuppressed(false);
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().getCharacterSet().setCharacterSetName("UTF-8");
        assertTrue(gw.lines.isEmpty());

        // Switch to 5.5.1, all should be fine
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
        gw.write("tmp/delete-me.ged");
    }

    /**
     * Test compatibility check with UTF-8 encoding and 5.5/5.5.1 data
     * 
     * @throws IOException
     *             if the data can't be written to the tmp dir
     * @throws GedcomWriterException
     *             if the data is malformed - should never happen, because the code under test is checking for this
     */
    @Test(expected = GedcomWriterException.class)
    public void testUtf8With55Bad() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        GedcomWriter gw = new GedcomWriter(g);
        gw.setValidationSuppressed(false);
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().getCharacterSet().setCharacterSetName("UTF-8");
        assertTrue(gw.lines.isEmpty());
        gw.write("tmp/delete-me.ged");
    }

    /**
     * Test compatibility check with UTF-8 encoding and 5.5/5.5.1 data
     * 
     * @throws IOException
     *             if the data can't be written to the tmp dir
     * @throws GedcomWriterException
     *             if the data is malformed - should never happen, because the code under test is checking for this
     */
    @Test
    public void testUtf8With55Good() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        GedcomWriter gw = new GedcomWriter(g);
        gw.setValidationSuppressed(false);
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        assertTrue(gw.lines.isEmpty());

        // Character set is default, should be good
        gw.write("tmp/delete-me.ged");
    }

}
