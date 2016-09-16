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

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.model.Corporation;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Repository;
import org.gedcom4j.model.SourceSystem;
import org.gedcom4j.model.StringWithCustomTags;
import org.gedcom4j.model.Submitter;
import org.gedcom4j.model.SupportedVersion;
import org.gedcom4j.model.TestHelper;
import org.junit.Test;

/**
 * Test case for {@link GedcomWriter} and support for v5.5 and v5.5.1. Focused on WWW Urls.
 * 
 * @author frizbog
 */
public class GedcomWriter551WwwTest {

    /**
     * Test compatibility check with the corporation data with 5.5 vs 5.5.1
     * 
     * @throws IOException
     *             if the data can't be written to the tmp dir
     * @throws GedcomWriterException
     *             if the data is malformed - should never happen, because the code under test is checking for this
     */
    @Test
    public void testCorpInSourceSystemWith551WwwGood() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setSourceSystem(new SourceSystem());
        Corporation c = new Corporation();
        g.getHeader().getSourceSystem().setCorporation(c);
        GedcomWriter gw = new GedcomWriter(g);
        gw.setValidationSuppressed(false);
        assertTrue(gw.lines.isEmpty());
    
        // WWW urls
        c.getWwwUrls(true).add(new StringWithCustomTags("Not allowed under 5.5"));
    
        // Switch to 5.5.1, all should be fine
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
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
    @Test(expected = GedcomWriterException.class)
    public void testCorpInSourceSystemWith55WwwBad() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setSourceSystem(new SourceSystem());
        Corporation c = new Corporation();
        g.getHeader().getSourceSystem().setCorporation(c);
        GedcomWriter gw = new GedcomWriter(g);
        gw.setValidationSuppressed(false);
        assertTrue(gw.lines.isEmpty());
    
        // WWW urls
        c.getWwwUrls(true).add(new StringWithCustomTags("Not allowed under 5.5"));
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
    public void testCorpInSourceSystemWith55WwwGood() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setSourceSystem(new SourceSystem());
        Corporation c = new Corporation();
        g.getHeader().getSourceSystem().setCorporation(c);
        GedcomWriter gw = new GedcomWriter(g);
        gw.setValidationSuppressed(false);
        assertTrue(gw.lines.isEmpty());
    
        // No www URL's, all should be fine
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
    public void testRepositoryWith551WwwGood() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        Repository r = new Repository();
        r.setXref("@R1@");
        g.getRepositories().put(r.getXref(), r);
        GedcomWriter gw = new GedcomWriter(g);
        gw.setValidationSuppressed(false);
        assertTrue(gw.lines.isEmpty());
    
        // WWW urls
        r.getWwwUrls(true).add(new StringWithCustomTags("Not allowed under 5.5"));
    
        // Switch to 5.5.1, all should be fine
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
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
    @Test(expected = GedcomWriterException.class)
    public void testRepositoryWith55WwwBad() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        Repository r = new Repository();
        r.setXref("@R1@");
        g.getRepositories().put(r.getXref(), r);
        GedcomWriter gw = new GedcomWriter(g);
        gw.setValidationSuppressed(false);
        assertTrue(gw.lines.isEmpty());
    
        // WWW urls
        r.getWwwUrls(true).add(new StringWithCustomTags("Not allowed under 5.5"));
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
    public void testRepositoryWith55WwwGood() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        Repository r = new Repository();
        r.setXref("@R1@");
        g.getRepositories().put(r.getXref(), r);
        GedcomWriter gw = new GedcomWriter(g);
        gw.setValidationSuppressed(false);
        assertTrue(gw.lines.isEmpty());
    
        // Repository has no www urls, should be ok
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
    public void testSubmitterWith551WwwGood() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        Submitter s = new Submitter();
        s.setName(new StringWithCustomTags("test"));
        s.setXref("@S1@");
        g.getSubmitters().put(s.getXref(), s);
        GedcomWriter gw = new GedcomWriter(g);
        gw.setValidationSuppressed(false);
        assertTrue(gw.lines.isEmpty());
    
        // WWW urls
        s.getWwwUrls(true).add(new StringWithCustomTags("Not allowed under 5.5"));
    
        // Switch to 5.5.1, all should be fine
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
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
    @Test(expected = GedcomWriterException.class)
    public void testSubmitterWith55WwwBad() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        Submitter s = new Submitter();
        s.setName(new StringWithCustomTags("test"));
        s.setXref("@S1@");
        g.getSubmitters().put(s.getXref(), s);
        GedcomWriter gw = new GedcomWriter(g);
        gw.setValidationSuppressed(false);
        assertTrue(gw.lines.isEmpty());
    
        // WWW urls
        s.getWwwUrls(true).add(new StringWithCustomTags("Not allowed under 5.5"));
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
    public void testSubmitterWith55WwwGood() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        Submitter s = new Submitter();
        s.setName(new StringWithCustomTags("test"));
        s.setXref("@S1@");
        g.getSubmitters().put(s.getXref(), s);
        GedcomWriter gw = new GedcomWriter(g);
        gw.setValidationSuppressed(false);
        assertTrue(gw.lines.isEmpty());
    
        // No WWW Urls on submitter, should be good
        gw.write("tmp/delete-me.ged");
    }

}
