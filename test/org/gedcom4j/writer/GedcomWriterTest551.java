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

package org.gedcom4j.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.gedcom4j.model.Corporation;
import org.gedcom4j.model.FileReference;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.IndividualEventType;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.model.Place;
import org.gedcom4j.model.Repository;
import org.gedcom4j.model.SourceSystem;
import org.gedcom4j.model.Submitter;
import org.gedcom4j.model.SupportedVersion;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.parser.GedcomParserException;
import org.gedcom4j.writer.GedcomWriter;
import org.gedcom4j.writer.GedcomWriterException;
import org.junit.Test;


/**
 * Test some specific stuff for GEDCOM 5.5.1
 * 
 * @author frizbog
 * 
 */
public class GedcomWriterTest551 {

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
        Gedcom g = new Gedcom();
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5_1;
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        Multimedia m = new Multimedia();
        m.embeddedMediaFormat = "bmp";
        g.multimedia.put("@M1@", m);
        m.blob.add("Blob data only allowed with 5.5");
        try {
            gw.write("tmp/delete-me.ged");
            fail("Should have gotten a GedcomException about the blob data");
        } catch (GedcomWriterException expectedAndIgnored) {
            ; // Good!
        }

        // Set to 5.5 and all should be fine
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        gw.write("tmp/delete-me.ged");

        // Set back to 5.5.1, clear the blob, and all should be fine
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        m.blob.clear();
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
        Gedcom g = new Gedcom();
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        g.header.sourceSystem = new SourceSystem();
        Corporation c = new Corporation();
        g.header.sourceSystem.corporation = c;
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // Email addresses
        c.emails.add("Not allowed under 5.5");
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
        Gedcom g = new Gedcom();
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        g.header.sourceSystem = new SourceSystem();
        Corporation c = new Corporation();
        g.header.sourceSystem.corporation = c;
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());
        // Fax numbers
        c.faxNumbers.add("Not allowed under 5.5");
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
        Gedcom g = new Gedcom();
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        g.header.sourceSystem = new SourceSystem();
        Corporation c = new Corporation();
        g.header.sourceSystem.corporation = c;
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // WWW urls
        c.wwwUrls.add("Not allowed under 5.5");
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
        Gedcom g = new Gedcom();
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
        e.place.latitude = "+50\u00B0 3' 1.49\"";
        e.place.longitude = "+19\u00B0 56' 21.48\"";

        // Write the test data
        gw.write("tmp/writertest551.ged");

        // Read it back
        GedcomParser gp = new GedcomParser();
        gp.verbose = true;
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
        assertEquals("+50\u00B0 3' 1.49\"", p.latitude);
        assertEquals("+19\u00B0 56' 21.48\"", p.longitude);
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
        Gedcom g = new Gedcom();
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
        Gedcom g1 = new Gedcom();
        Multimedia m1 = new Multimedia();
        m1.xref = "@M0@";
        g1.multimedia.put(m1.xref, m1);
        FileReference fr = new FileReference();
        fr.referenceToFile = "C:/foo.gif";
        fr.title = "Foo";
        fr.format = "gif";
        fr.mediaType = "disk";
        m1.fileReferences.add(fr);
        fr = new FileReference();
        fr.referenceToFile = "C:/bar.png";
        fr.format = "png";
        fr.title = "Bar";
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
        assertEquals("C:/foo.gif", fr.referenceToFile);
        assertEquals("gif", fr.format);
        assertEquals("disk", fr.mediaType);
        assertEquals("Foo", fr.title);
        fr = m2.fileReferences.get(1);
        assertEquals("C:/bar.png", fr.referenceToFile);
        assertEquals("png", fr.format);
        assertNull(fr.mediaType);
        assertEquals("Bar", fr.title);

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
        Gedcom g = new Gedcom();
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        Repository r = new Repository();
        g.repositories.put("@R1@", r);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // Email addresses
        r.emails.add("Not allowed under 5.5");
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
        Gedcom g = new Gedcom();
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        Repository r = new Repository();
        g.repositories.put("@R1@", r);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // Fax numbers
        r.faxNumbers.add("Not allowed under 5.5");
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
        Gedcom g = new Gedcom();
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        Repository r = new Repository();
        g.repositories.put("@R1@", r);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // WWW urls
        r.wwwUrls.add("Not allowed under 5.5");
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
        Gedcom g = new Gedcom();
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        Submitter s = new Submitter();
        g.submitters.put("@S1@", s);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // Email addresses
        s.emails.add("Not allowed under 5.5");
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
        Gedcom g = new Gedcom();
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        Submitter s = new Submitter();
        g.submitters.put("@S1@", s);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // Fax numbers
        s.faxNumbers.add("Not allowed under 5.5");
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
        Gedcom g = new Gedcom();
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        Submitter s = new Submitter();
        g.submitters.put("@S1@", s);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // WWW urls
        s.wwwUrls.add("Not allowed under 5.5");
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
        Gedcom g = new Gedcom();
        g.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        g.header.characterSet.characterSetName = "UTF-8";
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
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
