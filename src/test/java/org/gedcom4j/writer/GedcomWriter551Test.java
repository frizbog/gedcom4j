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

package org.gedcom4j.writer;

import static org.junit.Assert.*;

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.model.*;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.validate.GedcomValidationFinding;
import org.gedcom4j.validate.Severity;
import org.junit.Test;

/**
 * Test some specific stuff for GEDCOM 5.5.1
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
    public void testBlobWith551() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        gw.setAutorepair(false);
        assertTrue(gw.lines.isEmpty());

        Multimedia m = new Multimedia();
        m.setEmbeddedMediaFormat(new StringWithCustomTags("bmp"));
        m.setXref("@M1@");
        g.getMultimedia().put(m.getXref(), m);
        m.getBlob(true).add("Blob data only allowed with 5.5");
        try {
            gw.write("tmp/delete-me.ged");
            for (GedcomValidationFinding f : gw.getValidationFindings()) {
                System.out.println(f);
            }
            fail("Should have gotten a GedcomException about the blob data");
        } catch (@SuppressWarnings("unused") GedcomWriterException expectedAndIgnored) {
            boolean foundBlobError = false;
            for (GedcomValidationFinding f : gw.getValidationFindings()) {
                if (f.getSeverity() == Severity.ERROR && f.getProblemDescription().toLowerCase().contains("blob")) {
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
        m.setEmbeddedMediaFormat(null);
        gw.write("tmp/delete-me.ged");

    }

    /**
     * Test compatibility check with the corporation data with 5.5 vs 5.5.1
     * 
     * @throws IOException
     *             if the data can't be written to the tmp dir
     * @throws GedcomWriterException
     *             if the data is malformed - should never happen, because the code under test is checking for this
     */
    @Test
    public void testCorpInSourceSystemWith55Email() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();

        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setSourceSystem(new SourceSystem());
        Corporation c = new Corporation();
        g.getHeader().getSourceSystem().setCorporation(c);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // Email addresses
        c.getEmails(true).add(new StringWithCustomTags("Not allowed under 5.5"));
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about the corporation having an email");
        } catch (@SuppressWarnings("unused") GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Switch to 5.5.1, all should be fine
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
        gw.write("tmp/delete-me.ged");

        // clear emails and switch back to 5.5, all should be fine
        c.getEmails().clear();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        gw.write("tmp/delete-me.ged");
    }

    /**
     * Test compatibility check with the corporation data with 5.5 vs 5.5.1
     * 
     * @throws IOException
     *             if the data can't be written to the tmp dir
     * @throws GedcomWriterException
     *             if the data is malformed - should never happen, because the code under test is checking for this
     */
    @Test
    public void testCorpInSourceSystemWith55Fax() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setSourceSystem(new SourceSystem());
        Corporation c = new Corporation();
        g.getHeader().getSourceSystem().setCorporation(c);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());
        // Fax numbers
        c.getFaxNumbers(true).add(new StringWithCustomTags("Not allowed under 5.5"));
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about the corporation having a fax number");
        } catch (@SuppressWarnings("unused") GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Switch to 5.5.1, all should be fine
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
        gw.write("tmp/delete-me.ged");

        // clear fax numbers and switch back to 5.5, all should be fine
        c.getFaxNumbers().clear();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        gw.write("tmp/delete-me.ged");
    }

    /**
     * Test compatibility check with the corporation data with 5.5 vs 5.5.1
     * 
     * @throws IOException
     *             if the data can't be written to the tmp dir
     * @throws GedcomWriterException
     *             if the data is malformed - should never happen, because the code under test is checking for this
     */
    @Test
    public void testCorpInSourceSystemWith55Www() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setSourceSystem(new SourceSystem());
        Corporation c = new Corporation();
        g.getHeader().getSourceSystem().setCorporation(c);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // WWW urls
        c.getWwwUrls(true).add(new StringWithCustomTags("Not allowed under 5.5"));
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about the corporation having a www url");
        } catch (@SuppressWarnings("unused") GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Switch to 5.5.1, all should be fine
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
        gw.write("tmp/delete-me.ged");

        // clear URLs and switch back to 5.5, all should be fine
        c.getWwwUrls().clear();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
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
        gw.validationSuppressed = false;
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
        e.getPlace().setLatitude(new StringWithCustomTags("+50\u00B0 3' 1.49\""));
        e.getPlace().setLongitude(new StringWithCustomTags("+19\u00B0 56' 21.48\""));

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
    public void testMultilineCopyrightWith55() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().getCopyrightData(true).add("One line is ok");
        g.getHeader().getCopyrightData(true).add("Two lines is bad");
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about multi-line copyright data not being compatible with 5.5");
        } catch (@SuppressWarnings("unused") GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Switch to 5.5.1, all should be fine
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
        gw.write("tmp/delete-me.ged");

        // Switch back to 5.5, remove the extra line, all should be fine
        g.getHeader().getCopyrightData().remove(1);
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
        fr.setReferenceToFile(new StringWithCustomTags("C:/foo.gif"));
        fr.setTitle(new StringWithCustomTags("Foo"));
        fr.setFormat(new StringWithCustomTags("gif"));
        fr.setMediaType(new StringWithCustomTags("disk"));
        m1.getFileReferences(true).add(fr);
        fr = new FileReference();
        fr.setReferenceToFile(new StringWithCustomTags("C:/bar.png"));
        fr.setFormat(new StringWithCustomTags("png"));
        fr.setTitle(new StringWithCustomTags("Bar"));
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
        gw.validationSuppressed = true;
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
        pnv.setVariationType(new StringWithCustomTags("Typed it like it sounds, duh"));
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
        pnv.setVariationType(new StringWithCustomTags("Removed the slashes from the O's"));
        gw.write("tmp/delete-me.ged");

    }

    /**
     * Test compatibility check with the repository data with 5.5 vs 5.5.1
     * 
     * @throws IOException
     *             if the data can't be written to the tmp dir
     * @throws GedcomWriterException
     *             if the data is malformed - should never happen, because the code under test is checking for this
     */
    @Test
    public void testRepositoryWith55Email() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        Repository r = new Repository();
        r.setXref("@R1@");
        g.getRepositories().put(r.getXref(), r);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // Email addresses
        r.getEmails(true).add(new StringWithCustomTags("Not allowed under 5.5"));
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about the repository having an email");
        } catch (@SuppressWarnings("unused") GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Switch to 5.5.1, all should be fine
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
        gw.write("tmp/delete-me.ged");
        r.getEmails().clear();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        gw.write("tmp/delete-me.ged");
    }

    /**
     * Test compatibility check with the repository data with 5.5 vs 5.5.1
     * 
     * @throws IOException
     *             if the data can't be written to the tmp dir
     * @throws GedcomWriterException
     *             if the data is malformed - should never happen, because the code under test is checking for this
     */
    @Test
    public void testRepositoryWith55Fax() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        Repository r = new Repository();
        r.setXref("@R1@");
        g.getRepositories().put(r.getXref(), r);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // Fax numbers
        r.getFaxNumbers(true).add(new StringWithCustomTags("Not allowed under 5.5"));
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about the repository having a fax number");
        } catch (@SuppressWarnings("unused") GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Switch to 5.5.1, all should be fine
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
        gw.write("tmp/delete-me.ged");
        r.getFaxNumbers().clear();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        gw.write("tmp/delete-me.ged");
    }

    /**
     * Test compatibility check with the repository data with 5.5 vs 5.5.1
     * 
     * @throws IOException
     *             if the data can't be written to the tmp dir
     * @throws GedcomWriterException
     *             if the data is malformed - should never happen, because the code under test is checking for this
     */
    @Test
    public void testRepositoryWith55Www() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        Repository r = new Repository();
        r.setXref("@R1@");
        g.getRepositories().put(r.getXref(), r);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // WWW urls
        r.getWwwUrls(true).add(new StringWithCustomTags("Not allowed under 5.5"));
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about the repository having a www url");
        } catch (@SuppressWarnings("unused") GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Switch to 5.5.1, all should be fine
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
        gw.write("tmp/delete-me.ged");
        r.getWwwUrls().clear();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        gw.write("tmp/delete-me.ged");
    }

    /**
     * Test compatibility check with the submitter data with 5.5 vs 5.5.1
     * 
     * @throws IOException
     *             if the data can't be written to the tmp dir
     * @throws GedcomWriterException
     *             if the data is malformed - should never happen, because the code under test is checking for this
     */
    @Test
    public void testSubmitterWith55Email() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        Submitter s = new Submitter();
        s.setName(new StringWithCustomTags("test"));
        s.setXref("@S1@");
        g.getSubmitters().put(s.getXref(), s);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // Email addresses
        s.getEmails(true).add(new StringWithCustomTags("Not allowed under 5.5"));
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about the submitter having an email");
        } catch (@SuppressWarnings("unused") GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Switch to 5.5.1, all should be fine
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
        gw.write("tmp/delete-me.ged");
        s.getEmails().clear();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        gw.write("tmp/delete-me.ged");
    }

    /**
     * Test compatibility check with the submitter data with 5.5 vs 5.5.1
     * 
     * @throws IOException
     *             if the data can't be written to the tmp dir
     * @throws GedcomWriterException
     *             if the data is malformed - should never happen, because the code under test is checking for this
     */
    @Test
    public void testSubmitterWith55Fax() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        Submitter s = new Submitter();
        s.setName(new StringWithCustomTags("test"));
        s.setXref("@S1@");
        g.getSubmitters().put(s.getXref(), s);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // Fax numbers
        s.getFaxNumbers(true).add(new StringWithCustomTags("Not allowed under 5.5"));
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about the submitter having a fax number");
        } catch (@SuppressWarnings("unused") GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Switch to 5.5.1, all should be fine
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
        gw.write("tmp/delete-me.ged");
        s.getFaxNumbers().clear();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        gw.write("tmp/delete-me.ged");
    }

    /**
     * Test compatibility check with the submitter data with 5.5 vs 5.5.1
     * 
     * @throws IOException
     *             if the data can't be written to the tmp dir
     * @throws GedcomWriterException
     *             if the data is malformed - should never happen, because the code under test is checking for this
     */
    @Test
    public void testSubmitterWith55Www() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        Submitter s = new Submitter();
        s.setName(new StringWithCustomTags("test"));
        s.setXref("@S1@");
        g.getSubmitters().put(s.getXref(), s);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // WWW urls
        s.getWwwUrls(true).add(new StringWithCustomTags("Not allowed under 5.5"));
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about the submitter having a www url");
        } catch (@SuppressWarnings("unused") GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Switch to 5.5.1, all should be fine
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
        gw.write("tmp/delete-me.ged");
        s.getWwwUrls().clear();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
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
    public void testUtf8With55() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().getCharacterSet().setCharacterSetName(new StringWithCustomTags("UTF-8"));
        assertTrue(gw.lines.isEmpty());
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about UTF-8 not being compatible with 5.5");
        } catch (@SuppressWarnings("unused") GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Switch to 5.5.1, all should be fine
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
        gw.write("tmp/delete-me.ged");
    }

}
