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
package org.gedcom4j.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.gedcom4j.Options;
import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.AbstractNameVariation;
import org.gedcom4j.model.Corporation;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.FamilyChild;
import org.gedcom4j.model.FamilyEvent;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualAttribute;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.model.Place;
import org.gedcom4j.model.PlaceNameVariation;
import org.gedcom4j.model.Submitter;
import org.gedcom4j.model.enumerations.FamilyEventType;
import org.gedcom4j.model.enumerations.IndividualAttributeType;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.query.Finder;
import org.junit.Test;

/**
 * These are tests for the Gedcom Parser that are specific to GEDCOM 5.5.1. This class may eventually be combined with
 * {@link GedcomParserTest} once the branch is merged into the trunk.
 * 
 * @author frizbog
 * 
 */
@SuppressWarnings("PMD.TooManyMethods")
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
        assertTrue(gp.getErrors().isEmpty());
        assertTrue("There should be a warning because the file says it's 5.5 but has 5.5.1 tags in it", !gp.getWarnings()
                .isEmpty());
        Gedcom g = gp.getGedcom();
        assertNotNull(g);
        assertNotNull(g.getHeader());
        assertNotNull(g.getHeader().getCopyrightData());
        assertEquals(3, g.getHeader().getCopyrightData().size());
        assertEquals("License: Creative Commons Attribution-ShareAlike 3.0", g.getHeader().getCopyrightData().get(1));
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
        // assertTrue(gp.getErrors().isEmpty());
        // assertTrue(gp.getWarnings().isEmpty());
        Gedcom g = gp.getGedcom();
        assertNotNull(g);
        assertNotNull(g.getSubmitters());
        assertEquals(1, g.getSubmitters().entrySet().size());
        Submitter s = g.getSubmitters().get("@SUBM01@");
        assertNotNull(s);
        assertEquals("Matt /Harrah/", s.getName().getValue());
        assertNotNull(s.getEmails());
        assertEquals(2, s.getEmails().size());
        assertEquals("frizbog@charter.net", s.getEmails().get(1).getValue());
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
        assertTrue(gp.getErrors().isEmpty());
        assertTrue("There should be a warning because the file says it's 5.5 but has 5.5.1 tags in it", !gp.getWarnings()
                .isEmpty());
        Gedcom g = gp.getGedcom();
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
        assertEquals(IndividualAttributeType.FACT, fact.getType());
        assertEquals("Place from", fact.getSubType().getValue());
        assertEquals("Combe Florey", fact.getDescription().getValue());
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
        // assertTrue(gp.getErrors().isEmpty());
        // assertTrue(gp.getWarnings().isEmpty());
        Gedcom g = gp.getGedcom();
        assertNotNull(g);
        assertNotNull(g.getHeader());
        assertNotNull(g.getHeader().getSourceSystem());
        assertNotNull(g.getHeader().getSourceSystem().getCorporation());
        Corporation c = g.getHeader().getSourceSystem().getCorporation();
        assertEquals("The Church of Jesus Christ of Latter-day Saints", c.getBusinessName());
        assertNotNull(c.getFaxNumbers());
        assertEquals(1, c.getFaxNumbers().size());
        assertEquals("800-555-1212", c.getFaxNumbers().get(0).toString());
        if (Options.isCollectionInitializationEnabled()) {
            assertNotNull(c.getEmails());
            assertTrue(c.getEmails().isEmpty());
        } else {
            assertNull(c.getEmails());
        }
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
        assertTrue(gp.getErrors().isEmpty());
        assertTrue("There should be a warning because the file says it's 5.5 but has 5.5.1 tags in it", !gp.getWarnings()
                .isEmpty());
        Gedcom g = gp.getGedcom();
        Finder f = new Finder(g);
        List<Individual> found = f.findByName("Pinter", "Anonymus" /* sic */);
        assertNotNull(found);
        assertEquals(1, found.size());
        Individual dude = found.get(0);
        assertNotNull(dude);
        assertEquals(1, dude.getNames().size());
        PersonalName pn = dude.getNames().get(0);
        assertNotNull(pn);
        assertNotNull(pn.getPhonetic());
        assertEquals(1, pn.getPhonetic().size());
        AbstractNameVariation pnv = pn.getPhonetic().get(0);
        assertEquals("Anonymus /Pinter/", pnv.getVariation());
        assertNull(pnv.getVariationType());
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
        assertTrue(gp.getErrors().isEmpty());
        assertTrue("There should be a warning because the file says it's 5.5 but has 5.5.1 tags in it", !gp.getWarnings()
                .isEmpty());
        Gedcom g = gp.getGedcom();
        assertNotNull(g);
        assertEquals(1, g.getIndividuals().size());
        Individual i = g.getIndividuals().get("@I1@");
        assertEquals(1, i.getEvents().size());
        IndividualEvent e = i.getEvents().get(0);
        assertEquals(IndividualEventType.BIRTH, e.getType());
        assertNotNull(e.getPlace());
        Place p = e.getPlace();
        assertEquals("Tarnowie, Krak\u00F3w, Poland", p.getPlaceName());
        assertNotNull(p.getNotes());
        assertEquals(1, p.getNotes().size());
        assertNotNull(p.getPhonetic());
        assertEquals(1, p.getPhonetic().size());
        AbstractNameVariation nv = p.getPhonetic().get(0);
        assertNotNull(nv);
        assertTrue(nv instanceof PlaceNameVariation);
        assertEquals("Tarr-now, Krack-ow, Poh-land", nv.getVariation());
        assertEquals("guessing", nv.getVariationType().toString());
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
        assertTrue(gp.getErrors().isEmpty());
        assertTrue("There should be a warning because the file says it's 5.5 but has 5.5.1 tags in it", !gp.getWarnings()
                .isEmpty());
        Gedcom g = gp.getGedcom();
        assertNotNull(g);
        assertEquals(1, g.getIndividuals().size());
        Individual i = g.getIndividuals().get("@I1@");
        assertEquals(1, i.getEvents().size());
        IndividualEvent e = i.getEvents().get(0);
        assertEquals(IndividualEventType.BIRTH, e.getType());
        assertNotNull(e.getPlace());
        Place p = e.getPlace();
        assertEquals("Tarnowie, Krak\u00F3w, Poland", e.getPlace().getPlaceName());
        assertNotNull(p.getNotes());
        assertEquals(1, p.getNotes().size());

        assertEquals("+50\u00B0 3' 1.49\"", p.getLatitude().toString());
        assertEquals("+19\u00B0 56' 21.48\"", p.getLongitude().toString());
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
        assertTrue(gp.getErrors().isEmpty());
        // Any warnings issued should NOT be about BLOBs
        for (String w : gp.getWarnings()) {
            assertTrue(!w.contains("BLOB"));
        }
        assertNotNull(gp.getGedcom());
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
        assertTrue(gp.getErrors().isEmpty());
        assertTrue(!gp.getWarnings().isEmpty());
        boolean found = false;
        for (String w : gp.getWarnings()) {
            if (w.contains("BLOB")) {
                found = true;
            }
        }
        assertTrue("Should have found a warning about the BLOB data", found);
        assertNotNull(gp.getGedcom());
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
        assertTrue(gp.getErrors().isEmpty());
        // Any warnings issued should NOT be about BLOBs
        for (String w : gp.getWarnings()) {
            assertTrue(!w.contains("BLOB"));
        }
        assertNotNull(gp.getGedcom());
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
    @SuppressWarnings("PMD.SystemPrintln")
    public void testMultimediaFileRef() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/5.5.1 sample 5.ged");
        for (String e : gp.getErrors()) {
            System.out.println(e);
        }
        assertTrue(gp.getErrors().isEmpty());
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
        assertTrue(gp.getErrors().isEmpty());
        assertTrue(gp.getWarnings().isEmpty());
        Gedcom g = gp.getGedcom();
        assertNotNull(g);

        Family f = g.getFamilies().get("@F1@");
        assertNotNull(f);

        // Negative test
        FamilyEvent divorce = f.getEvents().get(0);
        assertNotNull(divorce);
        assertEquals(FamilyEventType.DIVORCE, divorce.getType());
        assertEquals("2 Sep 1880", divorce.getDate().getValue());
        assertNull(divorce.getReligiousAffiliation());

        // Positive test
        FamilyEvent marriage = f.getEvents().get(1);
        assertNotNull(marriage);
        assertEquals(FamilyEventType.MARRIAGE, marriage.getType());
        assertEquals("25 Oct 1875", marriage.getDate().getValue());
        assertEquals("Civil", marriage.getReligiousAffiliation().getValue());
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
        assertTrue(gp.getErrors().isEmpty());
        assertTrue(gp.getWarnings().isEmpty());
        Gedcom g = gp.getGedcom();
        assertNotNull(g);

        // Positive test
        Individual elizabeth = g.getIndividuals().get("@I75@");
        assertNotNull(elizabeth);
        List<IndividualEvent> christenings = elizabeth.getEventsOfType(IndividualEventType.CHRISTENING);
        assertNotNull(christenings);
        assertEquals(1, christenings.size());
        IndividualEvent c = christenings.get(0);
        assertNotNull(c);
        assertNotNull(c.getPlace());
        assertEquals("Peel Hospital, Selkirkshire ", c.getPlace().getPlaceName());
        assertEquals("Episcopalian", c.getReligiousAffiliation().getValue());

        // Negative test
        Individual annie = g.getIndividuals().get("@I76@");
        assertNotNull(annie);
        List<IndividualEvent> births = annie.getEventsOfType(IndividualEventType.BIRTH);
        assertNotNull(births);
        assertEquals(1, births.size());
        IndividualEvent b = births.get(0);
        assertNotNull(b);
        assertEquals("1889", b.getDate().getValue());
        assertNull(b.getReligiousAffiliation());
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
    @SuppressWarnings("PMD.SystemPrintln")
    public void testRestrictionOnEvent() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/5.5.1 sample 3.ged");
        for (String s : gp.getErrors()) {
            System.err.println(s);
        }
        assertTrue(gp.getErrors().isEmpty());
        assertTrue("There should be a warning because the file says it's 5.5 but has 5.5.1 tags in it", !gp.getWarnings()
                .isEmpty());
        Gedcom g = gp.getGedcom();
        assertNotNull(g);
        assertNotNull(g.getFamilies());
        Family f = g.getFamilies().get("@F1@");
        assertNotNull(f);
        assertNotNull(f.getEvents());
        assertEquals(1, f.getEvents().size());
        FamilyEvent e = f.getEvents().get(0);
        assertEquals("locked", e.getRestrictionNotice().getValue());
        f = g.getFamilies().get("@F2@");
        assertNotNull(f);
        assertNull(f.getRestrictionNotice());
        assertNotNull(f.getEvents());
        assertEquals(1, f.getEvents().size());
        e = f.getEvents().get(0);
        assertNull(e.getRestrictionNotice());
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
        assertTrue(gp.getErrors().isEmpty());
        assertTrue("There should be a warning because the file says it's 5.5 but has 5.5.1 tags in it", !gp.getWarnings()
                .isEmpty());
        Gedcom g = gp.getGedcom();
        assertNotNull(g);
        assertNotNull(g.getFamilies());
        Family f = g.getFamilies().get("@F1@");
        assertNotNull(f);
        assertEquals("locked", f.getRestrictionNotice().getValue());
        f = g.getFamilies().get("@F2@");
        assertNotNull(f);
        assertNull(f.getRestrictionNotice());
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
        assertTrue(gp.getErrors().isEmpty());
        assertTrue("There should be a warning because the file says it's 5.5 but has 5.5.1 tags in it", !gp.getWarnings()
                .isEmpty());
        Gedcom g = gp.getGedcom();
        Individual ladislaus = g.getIndividuals().get("@I2797@");
        assertNotNull(ladislaus);
        assertEquals(1, ladislaus.getNames().size());
        PersonalName pn = ladislaus.getNames().get(0);
        assertNotNull(pn);
        assertNotNull(pn.getRomanized());
        assertEquals(1, pn.getRomanized().size());
        AbstractNameVariation pnv = pn.getRomanized().get(0);
        assertEquals("Walter /Borgula/", pnv.getVariation());
        assertNull(pnv.getVariationType());
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
        assertTrue(gp.getErrors().isEmpty());
        assertTrue("There should be a warning because the file says it's 5.5 but has 5.5.1 tags in it", !gp.getWarnings()
                .isEmpty());
        Gedcom g = gp.getGedcom();
        assertNotNull(g);
        assertEquals(1, g.getIndividuals().size());
        Individual i = g.getIndividuals().get("@I1@");
        assertEquals(1, i.getEvents().size());
        IndividualEvent e = i.getEvents().get(0);
        assertEquals(IndividualEventType.BIRTH, e.getType());
        assertNotNull(e.getPlace());
        Place p = e.getPlace();
        assertEquals("Tarnowie, Krak\u00F3w, Poland", e.getPlace().getPlaceName());
        assertNotNull(p.getNotes());
        assertEquals(1, p.getNotes().size());
        assertNotNull(p.getRomanized());
        assertEquals(1, p.getRomanized().size());
        AbstractNameVariation nv = p.getRomanized().get(0);
        assertNotNull(nv);
        assertEquals("Tarnow, Cracow, Poland", nv.getVariation());
        assertEquals("Google translate", nv.getVariationType().getValue());
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
        assertTrue(gp.getErrors().isEmpty());
        assertTrue(gp.getWarnings().isEmpty());
        Gedcom g = gp.getGedcom();
        assertNotNull(g);

        // Positive test
        Individual george = g.getIndividuals().get("@I3@");
        assertNotNull(george);
        assertEquals(1, george.getFamiliesWhereChild().size());
        FamilyChild fc1 = george.getFamiliesWhereChild().get(0);
        assertNotNull(fc1);
        assertNotNull(fc1.getFamily());
        assertEquals("@F3@", fc1.getFamily().getXref());
        assertEquals("proven", fc1.getStatus().getValue());

        // Negative test
        Individual anne = g.getIndividuals().get("@I4@");
        assertNotNull(anne);
        assertEquals(1, anne.getFamiliesWhereChild().size());
        FamilyChild fc2 = anne.getFamiliesWhereChild().get(0);
        assertNotNull(fc2);
        assertNotNull(fc2.getFamily());
        assertEquals("@F2@", fc2.getFamily().getXref());
        assertNull(fc2.getStatus());
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
        assertTrue(gp.getErrors().isEmpty());
        assertTrue("There should be a warning because the file says it's 5.5 but has 5.5.1 tags in it", !gp.getWarnings()
                .isEmpty());
        Gedcom g = gp.getGedcom();
        assertNotNull(g);
        assertNotNull(g.getSubmitters());
        assertEquals(1, g.getSubmitters().entrySet().size());
        Submitter s = g.getSubmitters().get("@SUBM01@");
        assertNotNull(s);
        assertEquals("Matt /Harrah/", s.getName().getValue());
        assertNotNull(s.getWwwUrls());
        assertEquals(2, s.getWwwUrls().size());
        assertEquals("https://www.facebook.com/Gedcom4j", s.getWwwUrls().get(1).getValue());
    }

}
