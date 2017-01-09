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
package org.gedcom4j.model.thirdpartyadapters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.AbstractCitation;
import org.gedcom4j.model.CitationWithSource;
import org.gedcom4j.model.CustomFact;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.IGedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualReference;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.Source;
import org.gedcom4j.parser.GedcomParser;
import org.junit.Before;
import org.junit.Test;

/**
 * Positive tests for {@link FamilyTreeMaker3Adapter}
 * 
 * @author frizbog
 */
@SuppressWarnings("PMD.TooManyMethods")
public class FamilyTreeMaker3AdapterPositiveTest {

    /**
     * The gedcom test fixture we're going to work with
     */
    private IGedcom g;

    /** John, one of the people in the GEDCOM test file. */
    private Individual john;

    /** Jesse, one of the people in the GEDCOM test file. */
    private Individual jesse;

    /** The main family (consisting of jesse, june, and john) in the GEDCOM test file. */
    private Family family1;

    /** The photo in the GEDCOM test file. */
    private Multimedia photo;

    /** The source in the GEDCOM test file. */
    private Source source;

    /**
     * Setup test
     * 
     * @throws GedcomParserException
     *             if the gedcom cannot be parsed
     * @throws IOException
     *             if the file cannot be read
     */
    @Before
    public void setUp() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser(new Gedcom());
        gp.load("sample/ftmcustomtags.ged");
        g = gp.getGedcom();

        john = g.getIndividuals().get("@I1@");
        assertNotNull(john);

        jesse = g.getIndividuals().get("@I2@");
        assertNotNull(jesse);

        final Individual june = g.getIndividuals().get("@I3@");
        assertNotNull(june);

        family1 = g.getFamilies().get("@F1@");
        assertNotNull(family1);
        assertSame(jesse, family1.getHusband().getIndividual());
        assertSame(june, family1.getWife().getIndividual());

        final Family family2 = g.getFamilies().get("@F2@");
        assertNotNull(family2);

        photo = g.getMultimedia().get("@M1@");
        assertNotNull(photo);

        source = g.getSources().get("@S1@");
        assertNotNull(source);
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getCauseOfDeath(Individual)} and
     * {@link FamilyTreeMaker3Adapter#setCausesOfDeath(Individual, List)}
     */
    @Test
    public void testCauseOfDeathPositive() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> cod = a.getCauseOfDeath(john);
        assertNotNull(cod);
        assertEquals(1, cod.size());
        assertEquals("Alien abduction", cod.get(0).getDescription().getValue());

        a.setCausesOfDeath(john, new ArrayList<CustomFact>());
        cod = a.getCauseOfDeath(jesse);
        assertNotNull(cod);
        assertEquals(0, cod.size());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getCircumcision(Individual)} and
     * {@link FamilyTreeMaker3Adapter#setCircumcision(Individual, List)}
     */
    @Test
    public void testCircumcisionPositive() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> c = a.getCircumcision(john);
        assertNotNull(c);
        assertEquals(1, c.size());
        assertEquals("Dr. Streck performed", c.get(0).getDescription().getValue());
        assertEquals("08 JAN 1950", c.get(0).getDate().getValue());
        assertEquals("Colonial Heights, Independent Cities, Virginia, USA", c.get(0).getPlace().getPlaceName());

        a.setCircumcision(john, new ArrayList<CustomFact>());
        c = a.getCircumcision(jesse);
        assertNotNull(c);
        assertEquals(0, c.size());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getDestinations(Individual)} and
     * {@link FamilyTreeMaker3Adapter#setDestinations(Individual, List)}
     */
    @Test
    public void testDestinationPositive() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> d = a.getDestinations(john);
        assertNotNull(d);
        assertEquals(1, d.size());
        assertEquals("Alpha Centauri", d.get(0).getDescription().getValue());

        a.setDestinations(john, new ArrayList<CustomFact>());
        d = a.getDestinations(jesse);
        assertNotNull(d);
        assertEquals(0, d.size());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getElected(Individual)} and {@link FamilyTreeMaker3Adapter#setElected(Individual, List)}
     */
    @Test
    public void testElectedPositive() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> el = a.getElected(john);
        assertNotNull(el);
        assertEquals(1, el.size());
        assertEquals("Mayor of Munchkin City", el.get(0).getDescription().getValue());
        assertEquals("08 NOV 1992", el.get(0).getDate().getValue());
        assertEquals("Orlando, Brevard, Florida, USA", el.get(0).getPlace().getPlaceName());

        a.setElected(john, new ArrayList<CustomFact>());
        el = a.getElected(jesse);
        assertNotNull(el);
        assertEquals(0, el.size());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getEmployment(Individual)} and
     * {@link FamilyTreeMaker3Adapter#setEmployment(Individual, List)}
     */
    @Test
    public void testEmploymentPositive() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> em = a.getEmployment(john);
        assertNotNull(em);
        assertEquals(1, em.size());
        assertEquals("Keeling moose and squirrel", em.get(0).getDescription().getValue());
        assertEquals("1981", em.get(0).getDate().getValue());
        assertEquals("Frostbite Falls, Minnesota, USA", em.get(0).getPlace().getPlaceName());

        a.setEmployment(john, new ArrayList<CustomFact>());
        em = a.getEmployment(jesse);
        assertNotNull(em);
        assertEquals(0, em.size());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getExcommunication(Individual)} and
     * {@link FamilyTreeMaker3Adapter#setExcommunication(Individual, List)}
     */
    @Test
    public void testExcommunicationPositive() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> ex = a.getExcommunication(john);
        assertNotNull(ex);
        assertEquals(1, ex.size());
        assertEquals("Church of the Flying Spaghetti Monster", ex.get(0).getDescription().getValue());
        assertEquals("22 NOV 1982", ex.get(0).getDate().getValue());
        assertEquals("Washington City, District Of Columbia, District of Columbia, USA", ex.get(0).getPlace().getPlaceName());

        a.setExcommunication(john, new ArrayList<CustomFact>());
        ex = a.getExcommunication(jesse);
        assertNotNull(ex);
        assertEquals(0, ex.size());
    }

    /**
     * Positive test for {@link FamilyTreeMaker3Adapter#getFatherRelationship(IndividualReference)},
     * {@link FamilyTreeMaker3Adapter#newFatherRelationshipCustomFact()}, and
     * {@link FamilyTreeMaker3Adapter#setFatherRelationship(IndividualReference, List)}
     */
    @Test
    public void testFatherRelationshipPositive() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        Family f = john.getFamiliesWhereChild().get(0).getFamily();
        assertSame(family1, f);
        assertNotNull(f.getChildren());
        assertEquals(1, f.getChildren().size());

        IndividualReference c = f.getChildren().get(0);
        assertSame(john, c.getIndividual());

        List<CustomFact> fatherRelationship = a.getFatherRelationship(c);
        assertNotNull(fatherRelationship);
        assertEquals(1, fatherRelationship.size());

        CustomFact frel = fatherRelationship.get(0);
        assertEquals("Step", frel.getDescription().getValue());

        List<CustomFact> frels = new ArrayList<>();
        CustomFact frel2 = a.newFatherRelationshipCustomFact();
        frel2.setDescription("Adopted");
        frels.add(frel2);
        a.setFatherRelationship(c, frels);

        assertEquals("Adopted", a.getFatherRelationship(c).get(0).getDescription().getValue());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getFuneral(Individual)} and {@link FamilyTreeMaker3Adapter#setFuneral(Individual, List)}
     */
    @Test
    public void testFuneralPositive() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> fun = a.getFuneral(john);
        assertNotNull(fun);
        assertEquals(1, fun.size());
        assertEquals("Nobody showed up", fun.get(0).getDescription().getValue());
        assertEquals("04 JUL 2000", fun.get(0).getDate().getValue());
        assertEquals("Honolulu, Honolulu, Hawaii, USA", fun.get(0).getPlace().getPlaceName());

        a.setFuneral(john, new ArrayList<CustomFact>());
        fun = a.getFuneral(jesse);
        assertNotNull(fun);
        assertEquals(0, fun.size());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getHeights(Individual)} and {@link FamilyTreeMaker3Adapter#setHeights(Individual, List)}
     */
    @Test
    public void testHeightPositive() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> h = a.getHeights(john);
        assertNotNull(h);
        assertEquals(1, h.size());
        assertEquals("6' 2\"", h.get(0).getDescription().getValue());

        a.setHeights(john, new ArrayList<CustomFact>());
        h = a.getHeights(jesse);
        assertNotNull(h);
        assertEquals(0, h.size());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getInitiatory(Individual)} and
     * {@link FamilyTreeMaker3Adapter#setInitiatory(Individual, List)}
     */
    @Test
    public void testInitiatoryPositive() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> i = a.getInitiatory(john);
        assertNotNull(i);
        assertEquals(1, i.size());
        assertEquals("Wore a blue tie", i.get(0).getDescription().getValue());
        assertEquals("03 MAR 1970", i.get(0).getDate().getValue());
        assertEquals("Kalamazoo, Kalamazoo, Michigan, USA", i.get(0).getPlace().getPlaceName());

        a.setInitiatory(john, new ArrayList<CustomFact>());
        i = a.getInitiatory(jesse);
        assertNotNull(i);
        assertEquals(0, i.size());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getMedical(Individual)} and {@link FamilyTreeMaker3Adapter#setMedical(Individual, List)}
     */
    @Test
    public void testMedicalPositive() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> m = a.getMedical(john);
        assertNotNull(m);
        assertEquals(1, m.size());
        assertEquals("Decapitated, but recovered", m.get(0).getDescription().getValue());
        assertEquals("08 AUG 1978", m.get(0).getDate().getValue());
        assertEquals("San Antonio, Bexar, Texas, USA", m.get(0).getPlace().getPlaceName());

        a.setMedical(john, new ArrayList<CustomFact>());
        m = a.getMedical(jesse);
        assertNotNull(m);
        assertEquals(0, m.size());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getMilitaryId(Individual)} and
     * {@link FamilyTreeMaker3Adapter#setMilitaryId(Individual, List)}
     */
    @Test
    public void testMilitaryIdPositive() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> m = a.getMilitaryId(john);
        assertNotNull(m);
        assertEquals(1, m.size());

        assertEquals("1234567", m.get(0).getDescription().getValue());
        assertEquals("06 JUN 1966", m.get(0).getDate().getValue());
        assertEquals("Walla Walla, Walla Walla, Washington, USA", m.get(0).getPlace().getPlaceName());

        a.setMilitaryId(john, new ArrayList<CustomFact>());
        m = a.getMilitaryId(jesse);
        assertNotNull(m);
        assertEquals(0, m.size());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getMilitary(Individual)} and
     * {@link FamilyTreeMaker3Adapter#setMilitary(Individual, List)}
     */
    @Test
    public void testMilitaryPositive() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> m = a.getMilitary(john);
        assertNotNull(m);
        assertEquals(1, m.size());
        assertEquals("Garbage Pail Scrubber", m.get(0).getDescription().getValue());
        assertEquals("BET 06 JUN 1966 AND 07 JUL 1967", m.get(0).getDate().getValue());
        assertEquals("Fort Bragg, Cumberland, North Carolina, USA", m.get(0).getPlace().getPlaceName());

        a.setMilitary(john, new ArrayList<CustomFact>());
        m = a.getMilitary(jesse);
        assertNotNull(m);
        assertEquals(0, m.size());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getMission(Individual)} and {@link FamilyTreeMaker3Adapter#setMission(Individual, List)}
     */
    @Test
    public void testMissionPositive() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> m = a.getMission(john);
        assertNotNull(m);
        assertEquals(1, m.size());
        assertEquals("It was swell", m.get(0).getDescription().getValue());
        assertEquals("BET 01 JAN AND 31 AUG 1977", m.get(0).getDate().getValue());
        assertEquals("Pocatello, Bannock, Idaho, USA", m.get(0).getPlace().getPlaceName());

        a.setMission(john, new ArrayList<CustomFact>());
        m = a.getMission(jesse);
        assertNotNull(m);
        assertEquals(0, m.size());
    }

    /**
     * Positive test for {@link FamilyTreeMaker3Adapter#getMotherRelationship(IndividualReference)},
     * {@link FamilyTreeMaker3Adapter#newMotherRelationshipCustomFact()}, and
     * {@link FamilyTreeMaker3Adapter#setMotherRelationship(IndividualReference, List)}
     */
    @Test
    public void testMotherRelationshipPositive() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        Family f = john.getFamiliesWhereChild().get(0).getFamily();
        assertSame(family1, f);
        assertNotNull(f.getChildren());
        assertEquals(1, f.getChildren().size());

        IndividualReference c = f.getChildren().get(0);
        assertSame(john, c.getIndividual());

        List<CustomFact> motherRelationship = a.getMotherRelationship(c);
        assertNotNull(motherRelationship);
        assertEquals(1, motherRelationship.size());

        CustomFact mrel = motherRelationship.get(0);
        assertEquals("Guardian", mrel.getDescription().getValue());

        List<CustomFact> mrels = new ArrayList<>();
        CustomFact mrel2 = a.newMotherRelationshipCustomFact();
        mrel2.setDescription("Adopted");
        mrels.add(mrel2);
        a.setMotherRelationship(c, mrels);

        assertEquals("Adopted", a.getMotherRelationship(c).get(0).getDescription().getValue());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getNamesake(Individual)} and
     * {@link FamilyTreeMaker3Adapter#setNamesake(Individual, List)}
     */
    @Test
    public void testNamesakePositive() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> n = a.getNamesake(john);
        assertNotNull(n);
        assertEquals(1, n.size());
        assertEquals("Mr. Phil Philanganes (the Phamous Photographer)", n.get(0).getDescription().getValue());
        assertNull(n.get(0).getDate());
        assertNull(n.get(0).getPlace());

        a.setNamesake(john, new ArrayList<CustomFact>());
        n = a.getNamesake(jesse);
        assertNotNull(n);
        assertEquals(0, n.size());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getOrdinance(Individual)} and
     * {@link FamilyTreeMaker3Adapter#setOrdinance(Individual, List)}
     */
    @Test
    public void testOrdinancePositive() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> o = a.getOrdinance(john);
        assertNotNull(o);
        assertEquals(1, o.size());
        assertEquals("Frying pans were not involved", o.get(0).getDescription().getValue());
        assertEquals("09 SEP 1979", o.get(0).getDate().getValue());
        assertEquals("Temecula, Riverside, California, USA", o.get(0).getPlace().getPlaceName());

        a.setOrdinance(john, new ArrayList<CustomFact>());
        o = a.getOrdinance(jesse);
        assertNotNull(o);
        assertEquals(0, o.size());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getOrigin(Individual)} and {@link FamilyTreeMaker3Adapter#setOrigin(Individual, List)}
     */
    @Test
    public void testOriginPositive() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> o = a.getOrigin(john);
        assertNotNull(o);
        assertEquals(1, o.size());
        assertEquals("Cabbage Patch", o.get(0).getDescription().getValue());
        assertNull(o.get(0).getDate());
        assertNull(o.get(0).getPlace());

        a.setOrigin(john, new ArrayList<CustomFact>());
        o = a.getOrigin(jesse);
        assertNotNull(o);
        assertEquals(0, o.size());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getPhoto(Individual)} and {@link FamilyTreeMaker3Adapter#setPhoto(Individual, List)}
     */
    @Test
    public void testPhotoPositive() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> p = a.getPhoto(john);
        assertNotNull(p);
        assertEquals(1, p.size());
        CustomFact thePhoto = p.get(0);
        assertEquals("@M1@", thePhoto.getDescription().getValue());
        assertNull(thePhoto.getDate());
        assertNull(thePhoto.getPlace());

        assertSame(photo, g.getMultimedia().get(thePhoto.getDescription().getValue()));

        a.setPhoto(john, new ArrayList<CustomFact>());
        p = a.getPhoto(jesse);
        assertNotNull(p);
        assertEquals(0, p.size());
    }

    /**
     * Positive test for {@link FamilyTreeMaker3Adapter#getSeparation(Family)},
     * {@link FamilyTreeMaker3Adapter#newSeparationCustomFact()}, and {@link FamilyTreeMaker3Adapter#setSeparation(Family, List)}
     */
    @Test
    public void testSeparationPositive() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> separations = a.getSeparation(family1);
        assertNotNull(separations);
        assertEquals(1, separations.size());
        CustomFact theSep = separations.get(0);
        assertNotNull(theSep);
        assertEquals("There was a big fight", theSep.getDescription().getValue());
        assertEquals("05 MAY 1955", theSep.getDate().getValue());
        assertEquals("Glad Valley, Ziebach, South Dakota, USA", theSep.getPlace().getPlaceName());

        CustomFact sep = a.newSeparationCustomFact();
        sep.setDescription("Never happened");
        List<CustomFact> newSeps = new ArrayList<>();
        newSeps.add(sep);
        a.setSeparation(family1, newSeps);

        assertEquals("Never happened", a.getSeparation(family1).get(0).getDescription().getValue());
        assertNull(a.getSeparation(family1).get(0).getPlace());
        assertNull(a.getSeparation(family1).get(0).getDate());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getWeight(Individual)} and {@link FamilyTreeMaker3Adapter#setWeight(Individual, List)}
     */
    @Test
    public void testWeightPositive() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> w = a.getWeight(john);
        assertNotNull(w);
        assertEquals(1, w.size());
        CustomFact wt = w.get(0);
        assertEquals("165lb", wt.getDescription().getValue());
        assertNull(wt.getDate());
        assertNull(wt.getPlace());

        // Check the citations and stuff
        assertEquals(1, wt.getCitations().size());
        AbstractCitation c = wt.getCitations().get(0);
        assertTrue(c instanceof CitationWithSource);
        CitationWithSource cws = (CitationWithSource) c;
        assertNotNull(cws.getSource());
        assertEquals(cws.getSource(), source);
        assertSame(cws.getSource(), source);
        assertEquals("3", cws.getCertainty().getValue());
        assertEquals("p1", cws.getWhereInSource().getValue());
        assertEquals("Because it was an awesome napkin.", a.getCertaintyJustification(cws).get(0).getDescription().getValue());
        assertNotNull(a.getWebLink(cws));
        assertEquals("http://gedcom4j.org", a.getWebLink(cws).get(0).getDescription().getValue());

        a.setWeight(john, new ArrayList<CustomFact>());
        w = a.getWeight(jesse);
        assertNotNull(w);
        assertEquals(0, w.size());
    }
}