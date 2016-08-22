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
 * More tests for {@link GedcomWriter}, checking 5.5 vs 5.5.1 compatibility - focused on fax numbers.
 * 
 * @author frizbog
 */
public class GedcomWriter551FaxTest {

    /**
     * Test compatibility check with the corporation data with 5.5 vs 5.5.1
     * 
     * @throws IOException
     *             if the data can't be written to the tmp dir
     * @throws GedcomWriterException
     *             if the data is malformed - should never happen, because the code under test is checking for this
     */
    @Test
    public void testCorpInSourceSystemWith551FaxGood() throws IOException, GedcomWriterException {
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
    public void testCorpInSourceSystemWith55FaxBad() throws IOException, GedcomWriterException {
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
    public void testCorpInSourceSystemWith55FaxGood() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setSourceSystem(new SourceSystem());
        Corporation c = new Corporation();
        g.getHeader().getSourceSystem().setCorporation(c);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // No faxes, should be fine
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
    public void testRepositoryWith551FaxGood() throws IOException, GedcomWriterException {
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
    public void testRepositoryWith55FaxBad() throws IOException, GedcomWriterException {
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
    public void testRepositoryWith55FaxGood() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        Repository r = new Repository();
        r.setXref("@R1@");
        g.getRepositories().put(r.getXref(), r);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // No faxes on repository, should be ok
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
    public void testSubmitterWith551FaxGood() throws IOException, GedcomWriterException {
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
    public void testSubmitterWith55FaxBad() throws IOException, GedcomWriterException {
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
    public void testSubmitterWith55FaxGood() throws IOException, GedcomWriterException {
        Gedcom g = TestHelper.getMinimalGedcom();
        g.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        Submitter s = new Submitter();
        s.setName(new StringWithCustomTags("test"));
        s.setXref("@S1@");
        g.getSubmitters().put(s.getXref(), s);
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        assertTrue(gw.lines.isEmpty());

        // No faxes on submitter, should be good
        gw.write("tmp/delete-me.ged");
    }

}
