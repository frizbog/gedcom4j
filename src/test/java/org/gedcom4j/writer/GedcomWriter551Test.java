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
        gw.autorepair = false;
        assertTrue(gw.lines.isEmpty());

        Multimedia m = new Multimedia();
        m.embeddedMediaFormat = new StringWithCustomTags("bmp");
        m.xref = "@M1@";
        g.multimedia.put(m.xref, m);
        m.blob.add("Blob data only allowed with 5.5");
        try {
            gw.write("tmp/delete-me.ged");
            for (GedcomValidationFinding f : gw.validationFindings) {
                System.out.println(f);
            }
            fail("Should have gotten a GedcomException about the blob data");
        } catch (GedcomWriterException expectedAndIgnored) {
            boolean foundBlobError = false;
            for (GedcomValidationFinding f : gw.validationFindings) {
                if (f.severity == Severity.ERROR && f.problemDescription.toLowerCase().contains("blob")) {
                    foundBlobError = true;
                }
            }
            assertTrue("Should have had a validation finding about the blob error", foundBlobError);
        }

        // Set to 5.5 and all should be fine
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        gw.write("tmp/delete-me.ged");

        // Set back to 5.5.1, clear the blob and embedded format, and all should
        // be fine
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5_1;
        m.blob.clear();
        m.embeddedMediaFormat = null;
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

        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        g.header.sourceSystem = new SourceSystem();
        Corporation c = new Corporation();
        g.header.sourceSystem.corporation = c;
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // Email addresses
        c.emails.add(new StringWithCustomTags("Not allowed under 5.5"));
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about the corporation having an email");
        } catch (GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Switch to 5.5.1, all should be fine
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5_1;
        gw.write("tmp/delete-me.ged");

        // clear emails and switch back to 5.5, all should be fine
        c.emails.clear();
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
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
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        g.header.sourceSystem = new SourceSystem();
        Corporation c = new Corporation();
        g.header.sourceSystem.corporation = c;
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());
        // Fax numbers
        c.faxNumbers.add(new StringWithCustomTags("Not allowed under 5.5"));
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about the corporation having a fax number");
        } catch (GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Switch to 5.5.1, all should be fine
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5_1;
        gw.write("tmp/delete-me.ged");

        // clear fax numbers and switch back to 5.5, all should be fine
        c.faxNumbers.clear();
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
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
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        g.header.sourceSystem = new SourceSystem();
        Corporation c = new Corporation();
        g.header.sourceSystem.corporation = c;
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // WWW urls
        c.wwwUrls.add(new StringWithCustomTags("Not allowed under 5.5"));
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about the corporation having a www url");
        } catch (GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Switch to 5.5.1, all should be fine
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5_1;
        gw.write("tmp/delete-me.ged");

        // clear URLs and switch back to 5.5, all should be fine
        c.wwwUrls.clear();
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
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
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5_1;
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());
        Individual i = new Individual();
        i.xref = "@I1@";
        g.individuals.put(i.xref, i);
        PersonalName pn = new PersonalName();
        pn.basic = "Joe /Fryingpan/";
        i.names.add(pn);
        IndividualEvent e = new IndividualEvent();
        i.events.add(e);
        e.type = IndividualEventType.BIRTH;
        e.place = new Place();
        e.place.placeName = "Krakow, Poland";
        e.place.latitude = new StringWithCustomTags("+50\u00B0 3' 1.49\"");
        e.place.longitude = new StringWithCustomTags("+19\u00B0 56' 21.48\"");

        // Write the test data
        gw.write("tmp/writertest551.ged");

        // Read it back
        GedcomParser gp = new GedcomParser();
        gp.load("tmp/writertest551.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue(gp.warnings.isEmpty());

        // Look for expected data
        g = gp.gedcom;
        assertNotNull(g);
        i = g.individuals.get("@I1@");
        assertNotNull(i);
        assertEquals(1, i.events.size());
        e = i.events.get(0);
        assertEquals(IndividualEventType.BIRTH, e.type);
        Place p = e.place;
        assertNotNull(p);
        assertEquals("Krakow, Poland", p.placeName);
        // while we're here...
        assertTrue(p.romanized.isEmpty());
        assertTrue(p.phonetic.isEmpty());
        // ok, back to task at hand
        assertEquals("+50\u00B0 3' 1.49\"", p.latitude.value);
        assertEquals("+19\u00B0 56' 21.48\"", p.longitude.value);
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
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        g.header.copyrightData.add("One line is ok");
        g.header.copyrightData.add("Two lines is bad");
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about multi-line copyright data not being compatible with 5.5");
        } catch (GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Switch to 5.5.1, all should be fine
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5_1;
        gw.write("tmp/delete-me.ged");

        // Switch back to 5.5, remove the extra line, all should be fine
        g.header.copyrightData.remove(1);
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
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
        m1.xref = "@M0@";
        g1.multimedia.put(m1.xref, m1);
        FileReference fr = new FileReference();
        fr.referenceToFile = new StringWithCustomTags("C:/foo.gif");
        fr.title = new StringWithCustomTags("Foo");
        fr.format = new StringWithCustomTags("gif");
        fr.mediaType = new StringWithCustomTags("disk");
        m1.fileReferences.add(fr);
        fr = new FileReference();
        fr.referenceToFile = new StringWithCustomTags("C:/bar.png");
        fr.format = new StringWithCustomTags("png");
        fr.title = new StringWithCustomTags("Bar");
        m1.fileReferences.add(fr);

        // Write it
        GedcomWriter gw = new GedcomWriter(g1);
        gw.write("tmp/writertest551.ged");

        // Read it back
        GedcomParser gp = new GedcomParser();
        gp.load("tmp/writertest551.ged");
        assertNotNull(gp.gedcom);

        // See if we read back what we originally built
        Gedcom g2 = gp.gedcom;
        assertEquals(1, g2.multimedia.size());
        Multimedia m2 = g2.multimedia.get("@M0@");
        assertNotNull(m2);
        assertNull(m2.embeddedMediaFormat);
        assertNull(m2.changeDate);
        assertTrue(m2.notes.isEmpty());
        assertEquals(2, m2.fileReferences.size());

        fr = m2.fileReferences.get(0);
        assertEquals("C:/foo.gif", fr.referenceToFile.value);
        assertEquals("gif", fr.format.value);
        assertEquals("disk", fr.mediaType.value);
        assertEquals("Foo", fr.title.value);
        fr = m2.fileReferences.get(1);
        assertEquals("C:/bar.png", fr.referenceToFile.value);
        assertEquals("png", fr.format.value);
        assertNull(fr.mediaType);
        assertEquals("Bar", fr.title.value);

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
        i.xref = "@I0001@";
        g.individuals.put(i.xref, i);
        PersonalName pn = new PersonalName();
        pn.basic = "Bj\u00F8rn /J\u00F8rgen/";
        i.names.add(pn);

        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = true;
        assertTrue(gw.lines.isEmpty());

        // Try basic scenario first before perturbing name with variations
        gw.write("tmp/delete-me.ged");

        // Add a malformed phonetic variation
        PersonalNameVariation pnv = new PersonalNameVariation();
        pn.phonetic.add(pnv);
        try {
            gw.write("tmp/delete-me.ged");
            fail("Expected to get a GedcomWriterException due to missing field on personal name variation");
        } catch (GedcomWriterException expected) {
            assertTrue(expected.getMessage().toLowerCase().contains("required value for tag fone"));
        }
        // Now fix it
        pnv.variation = "Byorn /Yorgen/";
        gw.write("tmp/delete-me.ged");
        // Now fiddle with it further
        pnv.variationType = new StringWithCustomTags("Typed it like it sounds, duh");
        gw.write("tmp/delete-me.ged");

        // Add a bad romanized variation
        pnv = new PersonalNameVariation();
        pn.romanized.add(pnv);
        try {
            gw.write("tmp/delete-me.ged");
            fail("Expected to get a GedcomWriterException due to missing field on personal name variation");
        } catch (GedcomWriterException expected) {
            assertTrue(expected.getMessage().toLowerCase().contains("required value for tag romn"));
        }
        // Now Fix it
        pnv.variation = "Bjorn /Jorgen/";
        gw.write("tmp/delete-me.ged");
        // Now fiddle with it further
        pnv.variationType = new StringWithCustomTags("Removed the slashes from the O's");
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
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        Repository r = new Repository();
        r.xref = "@R1@";
        g.repositories.put(r.xref, r);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // Email addresses
        r.emails.add(new StringWithCustomTags("Not allowed under 5.5"));
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about the repository having an email");
        } catch (GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Switch to 5.5.1, all should be fine
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5_1;
        gw.write("tmp/delete-me.ged");
        r.emails.clear();
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
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
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        Repository r = new Repository();
        r.xref = "@R1@";
        g.repositories.put(r.xref, r);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // Fax numbers
        r.faxNumbers.add(new StringWithCustomTags("Not allowed under 5.5"));
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about the repository having a fax number");
        } catch (GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Switch to 5.5.1, all should be fine
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5_1;
        gw.write("tmp/delete-me.ged");
        r.faxNumbers.clear();
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
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
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        Repository r = new Repository();
        r.xref = "@R1@";
        g.repositories.put(r.xref, r);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // WWW urls
        r.wwwUrls.add(new StringWithCustomTags("Not allowed under 5.5"));
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about the repository having a www url");
        } catch (GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Switch to 5.5.1, all should be fine
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5_1;
        gw.write("tmp/delete-me.ged");
        r.wwwUrls.clear();
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
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
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        Submitter s = new Submitter();
        s.name = new StringWithCustomTags("test");
        s.xref = "@S1@";
        g.submitters.put(s.xref, s);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // Email addresses
        s.emails.add(new StringWithCustomTags("Not allowed under 5.5"));
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about the submitter having an email");
        } catch (GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Switch to 5.5.1, all should be fine
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5_1;
        gw.write("tmp/delete-me.ged");
        s.emails.clear();
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
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
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        Submitter s = new Submitter();
        s.name = new StringWithCustomTags("test");
        s.xref = "@S1@";
        g.submitters.put(s.xref, s);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // Fax numbers
        s.faxNumbers.add(new StringWithCustomTags("Not allowed under 5.5"));
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about the submitter having a fax number");
        } catch (GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Switch to 5.5.1, all should be fine
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5_1;
        gw.write("tmp/delete-me.ged");
        s.faxNumbers.clear();
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
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
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        Submitter s = new Submitter();
        s.name = new StringWithCustomTags("test");
        s.xref = "@S1@";
        g.submitters.put(s.xref, s);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // WWW urls
        s.wwwUrls.add(new StringWithCustomTags("Not allowed under 5.5"));
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about the submitter having a www url");
        } catch (GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Switch to 5.5.1, all should be fine
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5_1;
        gw.write("tmp/delete-me.ged");
        s.wwwUrls.clear();
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
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
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        g.header.characterSet.characterSetName = new StringWithCustomTags("UTF-8");
        assertTrue(gw.lines.isEmpty());
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about UTF-8 not being compatible with 5.5");
        } catch (GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Switch to 5.5.1, all should be fine
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5_1;
        gw.write("tmp/delete-me.ged");
    }

}
