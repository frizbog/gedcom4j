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
package org.gedcom4j.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.*;
import org.gedcom4j.query.Finder;
import org.junit.Test;

/**
 * These are tests for the Gedcom Parser that are specific to GEDCOM 5.5.1. This class may eventually be combined with
 * {@link GedcomParserTest} once the branch is merged into the trunk.
 * 
 * @author frizbog
 * 
 */
public class GedcomParser551Test {

    /**
     * Test that header copyright data is now multi-line capable
     * 
     * @throws IOException
     *             if there is a problem reading/writing the file
     * @throws GedcomParserException
     *             if there is a problem parsing the GEDCOM
     */
    @Test
    public void testContinuationForCopyright() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/john_of_sea_20101009.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue("There should be a warning because the file says it's 5.5 but has 5.5.1 tags in it", !gp.warnings.isEmpty());
        Gedcom g = gp.gedcom;
        assertNotNull(g);
        assertNotNull(g.getHeader());
        assertNotNull(g.getHeader().copyrightData);
        assertEquals(3, g.getHeader().copyrightData.size());
        assertEquals("License: Creative Commons Attribution-ShareAlike 3.0", g.getHeader().copyrightData.get(1));
    }

    /**
     * Test for parsing of the new "EMAIL" tag in 5.5.1
     * 
     * @throws IOException
     *             if there is a problem reading/writing the file
     * @throws GedcomParserException
     *             if there is a problem parsing the GEDCOM
     */
    @Test
    public void testEmail() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/5.5.1 sample 3.ged");
        // assertTrue(gp.errors.isEmpty());
        // assertTrue(gp.warnings.isEmpty());
        Gedcom g = gp.gedcom;
        assertNotNull(g);
        assertNotNull(g.getSubmitters());
        assertEquals(1, g.getSubmitters().entrySet().size());
        Submitter s = g.getSubmitters().get("@SUBM01@");
        assertNotNull(s);
        assertEquals("Matt /Harrah/", s.name.getValue());
        assertNotNull(s.emails);
        assertEquals(2, s.emails.size());
        assertEquals("frizbog@charter.net", s.emails.get(1).getValue());
    }

    /**
     * Test for parsing of the new "FACT" tag in 5.5.1
     * 
     * @throws IOException
     *             if there is a problem reading/writing the file
     * @throws GedcomParserException
     *             if there is a problem parsing the GEDCOM
     */
    @Test
    public void testFact() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/john_of_sea_20101009.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue("There should be a warning because the file says it's 5.5 but has 5.5.1 tags in it", !gp.warnings.isEmpty());
        Gedcom g = gp.gedcom;
        Finder f = new Finder(g);
        List<Individual> found = f.findByName("Moor", "Mary");
        assertNotNull(found);
        assertEquals(1, found.size());
        Individual mary = found.get(0);
        assertNotNull(mary);
        List<IndividualAttribute> facts = mary.getAttributesOfType(IndividualAttributeType.FACT);
        assertNotNull(facts);
        assertEquals(1, facts.size());
        IndividualAttribute fact = facts.get(0);
        assertNotNull(fact);
        assertEquals(IndividualAttributeType.FACT, fact.type);
        assertEquals("Place from", fact.subType.getValue());
        assertEquals("Combe Florey", fact.description.getValue());
    }

    /**
     * Test for parsing of the new "FAX" tag in 5.5.1
     * 
     * @throws IOException
     *             if there is a problem reading/writing the file
     * @throws GedcomParserException
     *             if there is a problem parsing the GEDCOM
     */
    @Test
    public void testFax() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/5.5.1 sample 3.ged");
        // assertTrue(gp.errors.isEmpty());
        // assertTrue(gp.warnings.isEmpty());
        Gedcom g = gp.gedcom;
        assertNotNull(g);
        assertNotNull(g.getHeader());
        assertNotNull(g.getHeader().sourceSystem);
        assertNotNull(g.getHeader().sourceSystem.corporation);
        Corporation c = g.getHeader().sourceSystem.corporation;
        assertEquals("The Church of Jesus Christ of Latter-day Saints", c.businessName);
        assertNotNull(c.faxNumbers);
        assertEquals(1, c.faxNumbers.size());
        assertEquals("800-555-1212", c.faxNumbers.get(0).toString());
        assertNotNull(c.emails);
        assertTrue(c.emails.isEmpty());
    }

    /**
     * Test for parsing of the new "FONE" tag on a personal name in 5.5.1
     * 
     * @throws IOException
     *             if there is a problem reading/writing the file
     * @throws GedcomParserException
     *             if there is a problem parsing the GEDCOM
     */
    @Test
    public void testFoneName() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/5.5.1 sample 2.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue("There should be a warning because the file says it's 5.5 but has 5.5.1 tags in it", !gp.warnings.isEmpty());
        Gedcom g = gp.gedcom;
        Finder f = new Finder(g);
        List<Individual> found = f.findByName("Pinter", "Anonymus" /* sic */);
        assertNotNull(found);
        assertEquals(1, found.size());
        Individual dude = found.get(0);
        assertNotNull(dude);
        assertEquals(1, dude.names.size());
        PersonalName pn = dude.names.get(0);
        assertNotNull(pn);
        assertNotNull(pn.phonetic);
        assertEquals(1, pn.phonetic.size());
        NameVariation pnv = pn.phonetic.get(0);
        assertEquals("Anonymus /Pinter/", pnv.variation);
        assertNull(pnv.variationType);
    }

    /**
     * Test parsing the FONE tag on places in GEDCOM 5.5.1
     * 
     * @throws IOException
     *             if there is a problem reading/writing the file
     * @throws GedcomParserException
     *             if there is a problem parsing the GEDCOM
     */
    @Test
    public void testFonePlace() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/5.5.1 sample 4.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue("There should be a warning because the file says it's 5.5 but has 5.5.1 tags in it", !gp.warnings.isEmpty());
        Gedcom g = gp.gedcom;
        assertNotNull(g);
        assertEquals(1, g.getIndividuals().size());
        Individual i = g.getIndividuals().get("@I1@");
        assertEquals(1, i.events.size());
        IndividualEvent e = i.events.get(0);
        assertEquals(IndividualEventType.BIRTH, e.type);
        assertNotNull(e.place);
        Place p = e.place;
        assertEquals("Tarnowie, Krak\u00F3w, Poland", e.place.placeName);
        assertNotNull(p.notes);
        assertEquals(1, p.notes.size());
        assertNotNull(p.phonetic);
        assertEquals(1, p.phonetic.size());
        NameVariation nv = p.phonetic.get(0);
        assertNotNull(nv);
        assertEquals("Tarr-now, Krack-ow, Poh-land", nv.variation);
        assertEquals("guessing", nv.variationType.toString());
    }

    /**
     * Test parsing the MAP, LATI, and LONG tags on places in GEDCOM 5.5.1
     * 
     * @throws IOException
     *             if there is a problem reading/writing the file
     * @throws GedcomParserException
     *             if there is a problem parsing the GEDCOM
     */
    @Test
    public void testMapLatLong() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/5.5.1 sample 4.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue("There should be a warning because the file says it's 5.5 but has 5.5.1 tags in it", !gp.warnings.isEmpty());
        Gedcom g = gp.gedcom;
        assertNotNull(g);
        assertEquals(1, g.getIndividuals().size());
        Individual i = g.getIndividuals().get("@I1@");
        assertEquals(1, i.events.size());
        IndividualEvent e = i.events.get(0);
        assertEquals(IndividualEventType.BIRTH, e.type);
        assertNotNull(e.place);
        Place p = e.place;
        assertEquals("Tarnowie, Krak\u00F3w, Poland", e.place.placeName);
        assertNotNull(p.notes);
        assertEquals(1, p.notes.size());

        assertEquals("+50\u00B0 3' 1.49\"", p.latitude.toString());
        assertEquals("+19\u00B0 56' 21.48\"", p.longitude.toString());
    }

    /**
     * Test the new handling for BLOBs in the two versions of GEDCOM
     * 
     * @throws IOException
     *             if there is a problem reading/writing the file
     * @throws GedcomParserException
     *             if there is a problem parsing the GEDCOM
     * 
     */
    @Test
    public void testMultimediaChanges1() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();

        // 5.5 data, has a blob - OK
        gp.load("sample/TGC551.ged");
        assertTrue(gp.errors.isEmpty());
        // Any warnings issued should NOT be about BLOBs
        for (String w : gp.warnings) {
            assertTrue(!w.contains("BLOB"));
        }
        assertNotNull(gp.gedcom);
    }

    /**
     * Test the new handling for BLOBs in the two versions of GEDCOM
     * 
     * @throws IOException
     *             if there is a problem reading/writing the file
     * @throws GedcomParserException
     *             if there is a problem parsing the GEDCOM
     * 
     */
    @Test
    public void testMultimediaChanges2() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();

        // 5.5.1 data with a BLOB - illegal
        gp.load("sample/5.5.1 sample 4.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue(!gp.warnings.isEmpty());
        boolean found = false;
        for (String w : gp.warnings) {
            if (w.contains("BLOB")) {
                found = true;
            }
        }
        assertTrue("Should have found a warning about the BLOB data", found);
        assertNotNull(gp.gedcom);
    }

    /**
     * Test the new handling for BLOBs in the two versions of GEDCOM
     * 
     * @throws IOException
     *             if there is a problem reading/writing the file
     * @throws GedcomParserException
     *             if there is a problem parsing the GEDCOM
     * 
     */
    @Test
    public void testMultimediaChanges3() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();

        // 5.5 data, has a blob - OK
        gp.load("sample/5.5.1 sample 1.ged");
        assertTrue(gp.errors.isEmpty());
        // Any warnings issued should NOT be about BLOBs
        for (String w : gp.warnings) {
            assertTrue(!w.contains("BLOB"));
        }
        assertNotNull(gp.gedcom);
    }

    /**
     * Test the file references sections for a multimedia object
     * 
     * 
     * @throws IOException
     *             if there is a problem reading/writing the file
     * @throws GedcomParserException
     *             if there is a problem parsing the GEDCOM
     * 
     */
    @Test
    public void testMultimediaFileRef() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/5.5.1 sample 5.ged");
        assertTrue(gp.errors.isEmpty());
    }

    /**
     * Test the new religious affiliation tag added to family events in GEDCOM 5.5.1
     * 
     * @throws IOException
     *             if there is a problem reading/writing the file
     * @throws GedcomParserException
     *             if there is a problem parsing the GEDCOM
     * 
     */
    @Test
    public void testReligionOnFamilyEventDetail() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/5.5.1 sample 1.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue(gp.warnings.isEmpty());
        Gedcom g = gp.gedcom;
        assertNotNull(g);

        Family f = g.getFamilies().get("@F1@");
        assertNotNull(f);

        // Negative test
        FamilyEvent divorce = f.events.get(0);
        assertNotNull(divorce);
        assertEquals(FamilyEventType.DIVORCE, divorce.type);
        assertEquals("2 Sep 1880", divorce.date.getValue());
        assertNull(divorce.religiousAffiliation);

        // Positive test
        FamilyEvent marriage = f.events.get(1);
        assertNotNull(marriage);
        assertEquals(FamilyEventType.MARRIAGE, marriage.type);
        assertEquals("25 Oct 1875", marriage.date.getValue());
        assertEquals("Civil", marriage.religiousAffiliation.getValue());
    }

    /**
     * Test the new religious affiliation tag added to family events in GEDCOM 5.5.1
     * 
     * @throws IOException
     *             if there is a problem reading/writing the file
     * @throws GedcomParserException
     *             if there is a problem parsing the GEDCOM
     * 
     */
    @Test
    public void testReligionOnIndividualEventDetail() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/5.5.1 sample 1.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue(gp.warnings.isEmpty());
        Gedcom g = gp.gedcom;
        assertNotNull(g);

        // Positive test
        Individual elizabeth = g.getIndividuals().get("@I75@");
        assertNotNull(elizabeth);
        List<IndividualEvent> christenings = elizabeth.getEventsOfType(IndividualEventType.CHRISTENING);
        assertNotNull(christenings);
        assertEquals(1, christenings.size());
        IndividualEvent c = christenings.get(0);
        assertNotNull(c);
        assertNotNull(c.place);
        assertEquals("Peel Hospital, Selkirkshire ", c.place.placeName);
        assertEquals("Episcopalian", c.religiousAffiliation.getValue());

        // Negative test
        Individual annie = g.getIndividuals().get("@I76@");
        assertNotNull(annie);
        List<IndividualEvent> births = annie.getEventsOfType(IndividualEventType.BIRTH);
        assertNotNull(births);
        assertEquals(1, births.size());
        IndividualEvent b = births.get(0);
        assertNotNull(b);
        assertEquals("1889", b.date.getValue());
        assertNull(b.religiousAffiliation);
    }

    /**
     * Test the new restriction tag on Families added in GEDCOM 5.5.1
     * 
     * @throws IOException
     *             if there is a problem reading/writing the file
     * @throws GedcomParserException
     *             if there is a problem parsing the GEDCOM
     * 
     */
    @Test
    public void testRestrictionOnEvent() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/5.5.1 sample 3.ged");
        for (String s : gp.errors) {
            System.err.println(s);
        }
        assertTrue(gp.errors.isEmpty());
        assertTrue("There should be a warning because the file says it's 5.5 but has 5.5.1 tags in it", !gp.warnings.isEmpty());
        Gedcom g = gp.gedcom;
        assertNotNull(g);
        assertNotNull(g.getFamilies());
        Family f = g.getFamilies().get("@F1@");
        assertNotNull(f);
        assertNotNull(f.events);
        assertEquals(1, f.events.size());
        FamilyEvent e = f.events.get(0);
        assertEquals("locked", e.restrictionNotice.getValue());
        f = g.getFamilies().get("@F2@");
        assertNotNull(f);
        assertNull(f.restrictionNotice);
        assertNotNull(f.events);
        assertEquals(1, f.events.size());
        e = f.events.get(0);
        assertNull(e.restrictionNotice);
    }

    /**
     * Test the new restriction tag on Families added in GEDCOM 5.5.1
     * 
     * @throws IOException
     *             if there is a problem reading/writing the file
     * @throws GedcomParserException
     *             if there is a problem parsing the GEDCOM
     * 
     */
    @Test
    public void testRestrictionOnFamily() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/5.5.1 sample 3.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue("There should be a warning because the file says it's 5.5 but has 5.5.1 tags in it", !gp.warnings.isEmpty());
        Gedcom g = gp.gedcom;
        assertNotNull(g);
        assertNotNull(g.getFamilies());
        Family f = g.getFamilies().get("@F1@");
        assertNotNull(f);
        assertEquals("locked", f.restrictionNotice.getValue());
        f = g.getFamilies().get("@F2@");
        assertNotNull(f);
        assertNull(f.restrictionNotice);
    }

    /**
     * Test for parsing of the new "ROMN" tag on a personal name in 5.5.1
     * 
     * @throws IOException
     *             if there is a problem reading/writing the file
     * @throws GedcomParserException
     *             if there is a problem parsing the GEDCOM
     */
    @Test
    public void testRomnName() throws IOException, GedcomParserException {

        GedcomParser gp = new GedcomParser();
        gp.load("sample/5.5.1 sample 3.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue("There should be a warning because the file says it's 5.5 but has 5.5.1 tags in it", !gp.warnings.isEmpty());
        Gedcom g = gp.gedcom;
        Individual ladislaus = g.getIndividuals().get("@I2797@");
        assertNotNull(ladislaus);
        assertEquals(1, ladislaus.names.size());
        PersonalName pn = ladislaus.names.get(0);
        assertNotNull(pn);
        assertNotNull(pn.romanized);
        assertEquals(1, pn.romanized.size());
        NameVariation pnv = pn.romanized.get(0);
        assertEquals("Walter /Borgula/", pnv.variation);
        assertNull(pnv.variationType);
    }

    /**
     * Test parsing the ROMN tag on places in GEDCOM 5.5.1
     * 
     * @throws IOException
     *             if there is a problem reading/writing the file
     * @throws GedcomParserException
     *             if there is a problem parsing the GEDCOM
     */
    @Test
    public void testRomnPlace() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/5.5.1 sample 4.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue("There should be a warning because the file says it's 5.5 but has 5.5.1 tags in it", !gp.warnings.isEmpty());
        Gedcom g = gp.gedcom;
        assertNotNull(g);
        assertEquals(1, g.getIndividuals().size());
        Individual i = g.getIndividuals().get("@I1@");
        assertEquals(1, i.events.size());
        IndividualEvent e = i.events.get(0);
        assertEquals(IndividualEventType.BIRTH, e.type);
        assertNotNull(e.place);
        Place p = e.place;
        assertEquals("Tarnowie, Krak\u00F3w, Poland", e.place.placeName);
        assertNotNull(p.notes);
        assertEquals(1, p.notes.size());
        assertNotNull(p.romanized);
        assertEquals(1, p.romanized.size());
        NameVariation nv = p.romanized.get(0);
        assertNotNull(nv);
        assertEquals("Tarnow, Cracow, Poland", nv.variation);
        assertEquals("Google translate", nv.variationType.getValue());
    }

    /**
     * Test for parsing of the new STAT sub-tag on FAMC tags in 5.5.1
     * 
     * @throws IOException
     *             if there is a problem reading/writing the file
     * @throws GedcomParserException
     *             if there is a problem parsing the GEDCOM
     */
    @Test
    public void testStatusOnFamilyChild() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/5.5.1 sample 1.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue(gp.warnings.isEmpty());
        Gedcom g = gp.gedcom;
        assertNotNull(g);

        // Positive test
        Individual george = g.getIndividuals().get("@I3@");
        assertNotNull(george);
        assertEquals(1, george.familiesWhereChild.size());
        FamilyChild fc1 = george.familiesWhereChild.get(0);
        assertNotNull(fc1);
        assertNotNull(fc1.family);
        assertEquals("@F3@", fc1.family.xref);
        assertEquals("proven", fc1.status.getValue());

        // Negative test
        Individual anne = g.getIndividuals().get("@I4@");
        assertNotNull(anne);
        assertEquals(1, anne.familiesWhereChild.size());
        FamilyChild fc2 = anne.familiesWhereChild.get(0);
        assertNotNull(fc2);
        assertNotNull(fc2.family);
        assertEquals("@F2@", fc2.family.xref);
        assertNull(fc2.status);
    }

    /**
     * Test for parsing of the new "WWW" tag in 5.5.1
     * 
     * @throws IOException
     *             if there is a problem reading/writing the file
     * @throws GedcomParserException
     *             if there is a problem parsing the GEDCOM
     */
    @Test
    public void testWWW() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/5.5.1 sample 3.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue("There should be a warning because the file says it's 5.5 but has 5.5.1 tags in it", !gp.warnings.isEmpty());
        Gedcom g = gp.gedcom;
        assertNotNull(g);
        assertNotNull(g.getSubmitters());
        assertEquals(1, g.getSubmitters().entrySet().size());
        Submitter s = g.getSubmitters().get("@SUBM01@");
        assertNotNull(s);
        assertEquals("Matt /Harrah/", s.name.getValue());
        assertNotNull(s.wwwUrls);
        assertEquals(2, s.wwwUrls.size());
        assertEquals("https://www.facebook.com/Gedcom4j", s.wwwUrls.get(1).getValue());
    }
}
