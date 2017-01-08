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
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.CustomFact;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.IGedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.Place;
import org.gedcom4j.model.Source;
import org.gedcom4j.parser.GedcomParser;
import org.junit.Before;
import org.junit.Test;

/**
 * Combined negative tests for {@link FamilyTreeMaker3Adapter}
 * 
 * @author frizbog
 */
@SuppressWarnings("PMD.TooManyMethods")
public class FamilyTreeMaker3AdapterNegativeTest {

    /** Jesse, one of the people in the GEDCOM test file. */
    private Individual jesse;

    /** Another family in the GEDCOM test file. */
    private Family family2;

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
        GedcomParser gp = new GedcomParser();
        gp.load("sample/ftmcustomtags.ged");
        final IGedcom g = gp.getGedcom();

        final Individual john = g.getIndividuals().get("@I1@");
        assertNotNull(john);

        jesse = g.getIndividuals().get("@I2@");
        assertNotNull(jesse);

        final Individual june = g.getIndividuals().get("@I3@");
        assertNotNull(june);

        final Family family1 = g.getFamilies().get("@F1@");
        assertNotNull(family1);
        assertSame(jesse, family1.getHusband().getIndividual());
        assertSame(june, family1.getWife().getIndividual());

        family2 = g.getFamilies().get("@F2@");
        assertNotNull(family2);

        final Multimedia photo = g.getMultimedia().get("@M1@");
        assertNotNull(photo);

        final Source source = g.getSources().get("@S1@");
        assertNotNull(source);
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getCauseOfDeath(Individual)} and
     * {@link FamilyTreeMaker3Adapter#setCausesOfDeath(Individual, List)}
     */
    @Test
    public void testCauseOfDeathNegative() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> cod = a.getCauseOfDeath(jesse);
        assertNotNull(cod);
        assertEquals(0, cod.size());

        List<CustomFact> cod2 = new ArrayList<>();
        CustomFact cf = a.newCauseOfDeathCustomFact();
        cf.setDescription("Frying Pan");
        cod2.add(cf);
        a.setCausesOfDeath(jesse, cod2);

        cod = a.getCauseOfDeath(jesse);
        assertNotNull(cod);
        assertEquals(1, cod.size());
        assertEquals("Frying Pan", cod.get(0).getDescription().getValue());

    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getCircumcision(Individual)} and
     * {@link FamilyTreeMaker3Adapter#setCircumcision(Individual, List)}
     */
    @Test
    public void testCircumcisionNegative() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> c = a.getCircumcision(jesse);
        assertNotNull(c);
        assertEquals(0, c.size());

        List<CustomFact> c2 = new ArrayList<>();
        CustomFact cf = a.newCircumcisionCustomFact();
        cf.setDate("01 JAN 1990");
        cf.setDescription("Frying Pan");
        Place p = new Place();
        p.setPlaceName("Wyoming, USA");
        cf.setPlace(p);
        c2.add(cf);
        a.setCircumcision(jesse, c2);

        c = a.getCircumcision(jesse);
        assertNotNull(c);
        assertEquals(1, c.size());
        assertEquals("Frying Pan", c.get(0).getDescription().getValue());
        assertEquals("01 JAN 1990", c.get(0).getDate().getValue());
        assertEquals("Wyoming, USA", c.get(0).getPlace().getPlaceName());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getDestinations(Individual)} and
     * {@link FamilyTreeMaker3Adapter#setDestinations(Individual, List)}
     */
    @Test
    public void testDestinationNegative() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> d = a.getDestinations(jesse);
        assertNotNull(d);
        assertEquals(0, d.size());

        List<CustomFact> d2 = new ArrayList<>();
        CustomFact cf = a.newDestinationCustomFact();
        cf.setDate("01 JAN 1990");
        cf.setDescription("Frying Pan");
        Place p = new Place();
        p.setPlaceName("Wyoming, USA");
        cf.setPlace(p);
        d2.add(cf);
        a.setDestinations(jesse, d2);

        d = a.getDestinations(jesse);
        assertNotNull(d);
        assertEquals(1, d.size());
        assertEquals("Frying Pan", d.get(0).getDescription().getValue());

    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getElected(Individual)} and {@link FamilyTreeMaker3Adapter#setElected(Individual, List)}
     */
    @Test
    public void testElectedNegative() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> el = a.getElected(jesse);
        assertNotNull(el);
        assertEquals(0, el.size());

        List<CustomFact> el2 = new ArrayList<>();
        CustomFact cf = a.newElectedCustomFact();
        cf.setDate("01 JAN 1990");
        cf.setDescription("Frying Pan");
        Place p = new Place();
        p.setPlaceName("Wyoming, USA");
        cf.setPlace(p);
        el2.add(cf);
        a.setElected(jesse, el2);

        el = a.getElected(jesse);
        assertNotNull(el);
        assertEquals(1, el.size());
        assertEquals("Frying Pan", el.get(0).getDescription().getValue());
        assertEquals("01 JAN 1990", el.get(0).getDate().getValue());
        assertEquals("Wyoming, USA", el.get(0).getPlace().getPlaceName());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getEmployment(Individual)} and
     * {@link FamilyTreeMaker3Adapter#setEmployment(Individual, List)}
     */
    @Test
    public void testEmploymentNegative() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> em = a.getEmployment(jesse);
        assertNotNull(em);
        assertEquals(0, em.size());

        List<CustomFact> em2 = new ArrayList<>();
        CustomFact cf = a.newEmploymentCustomFact();
        cf.setDate("01 JAN 1990");
        cf.setDescription("Frying Pan");
        Place p = new Place();
        p.setPlaceName("Wyoming, USA");
        cf.setPlace(p);
        em2.add(cf);
        a.setEmployment(jesse, em2);

        em = a.getEmployment(jesse);
        assertNotNull(em);
        assertEquals(1, em.size());
        assertEquals("Frying Pan", em.get(0).getDescription().getValue());
        assertEquals("01 JAN 1990", em.get(0).getDate().getValue());
        assertEquals("Wyoming, USA", em.get(0).getPlace().getPlaceName());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getExcommunication(Individual)} and
     * {@link FamilyTreeMaker3Adapter#setExcommunication(Individual, List)}
     */
    @Test
    public void testExcommunicationNegative() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> ex = a.getExcommunication(jesse);
        assertNotNull(ex);
        assertEquals(0, ex.size());

        List<CustomFact> ex2 = new ArrayList<>();
        CustomFact cf = a.newExcommunicationCustomFact();
        cf.setDate("01 JAN 1990");
        cf.setDescription("Frying Pan");
        Place p = new Place();
        p.setPlaceName("Wyoming, USA");
        cf.setPlace(p);
        ex2.add(cf);
        a.setExcommunication(jesse, ex2);

        ex = a.getExcommunication(jesse);
        assertNotNull(ex);
        assertEquals(1, ex.size());
        assertEquals("Frying Pan", ex.get(0).getDescription().getValue());
        assertEquals("01 JAN 1990", ex.get(0).getDate().getValue());
        assertEquals("Wyoming, USA", ex.get(0).getPlace().getPlaceName());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getFuneral(Individual)} and {@link FamilyTreeMaker3Adapter#setFuneral(Individual, List)}
     */
    @Test
    public void testFuneralNegative() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> fun = a.getFuneral(jesse);
        assertNotNull(fun);
        assertEquals(0, fun.size());

        List<CustomFact> fun2 = new ArrayList<>();
        CustomFact cf = a.newFuneralCustomFact();
        cf.setDate("01 JAN 1990");
        cf.setDescription("Frying Pan");
        Place p = new Place();
        p.setPlaceName("Wyoming, USA");
        cf.setPlace(p);
        fun2.add(cf);
        a.setFuneral(jesse, fun2);

        fun = a.getFuneral(jesse);
        assertNotNull(fun);
        assertEquals(1, fun.size());
        assertEquals("Frying Pan", fun.get(0).getDescription().getValue());
        assertEquals("01 JAN 1990", fun.get(0).getDate().getValue());
        assertEquals("Wyoming, USA", fun.get(0).getPlace().getPlaceName());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getHeights(Individual)} and {@link FamilyTreeMaker3Adapter#setHeights(Individual, List)}
     */
    @Test
    public void testHeightNegative() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> i = a.getHeights(jesse);
        assertNotNull(i);
        assertEquals(0, i.size());

        List<CustomFact> i2 = new ArrayList<>();
        CustomFact cf = a.newHeightCustomFact();
        cf.setDate("01 JAN 1990");
        cf.setDescription("Frying Pan");
        Place p = new Place();
        p.setPlaceName("Wyoming, USA");
        cf.setPlace(p);
        i2.add(cf);
        a.setHeights(jesse, i2);

        i = a.getHeights(jesse);
        assertNotNull(i);
        assertEquals(1, i.size());
        assertEquals("Frying Pan", i.get(0).getDescription().getValue());
        assertEquals("01 JAN 1990", i.get(0).getDate().getValue());
        assertEquals("Wyoming, USA", i.get(0).getPlace().getPlaceName());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getInitiatory(Individual)} and
     * {@link FamilyTreeMaker3Adapter#setInitiatory(Individual, List)}
     */
    @Test
    public void testInitiatoryNegative() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> i = a.getInitiatory(jesse);
        assertNotNull(i);
        assertEquals(0, i.size());

        List<CustomFact> i2 = new ArrayList<>();
        CustomFact cf = a.newInitiatoryCustomFact();
        cf.setDate("01 JAN 1990");
        cf.setDescription("Frying Pan");
        Place p = new Place();
        p.setPlaceName("Wyoming, USA");
        cf.setPlace(p);
        i2.add(cf);
        a.setInitiatory(jesse, i2);

        i = a.getInitiatory(jesse);
        assertNotNull(i);
        assertEquals(1, i.size());
        assertEquals("Frying Pan", i.get(0).getDescription().getValue());
        assertEquals("01 JAN 1990", i.get(0).getDate().getValue());
        assertEquals("Wyoming, USA", i.get(0).getPlace().getPlaceName());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getMedical(Individual)} and {@link FamilyTreeMaker3Adapter#setMedical(Individual, List)}
     */
    @Test
    public void testMedicalNegative() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> m = a.getMedical(jesse);
        assertNotNull(m);
        assertEquals(0, m.size());

        List<CustomFact> m2 = new ArrayList<>();
        CustomFact cf = a.newMedicalCustomFact();
        cf.setDate("01 JAN 1990");
        cf.setDescription("Frying Pan");
        Place p = new Place();
        p.setPlaceName("Wyoming, USA");
        cf.setPlace(p);
        m2.add(cf);
        a.setMedical(jesse, m2);

        m = a.getMedical(jesse);
        assertNotNull(m);
        assertEquals(1, m.size());
        assertEquals("Frying Pan", m.get(0).getDescription().getValue());
        assertEquals("01 JAN 1990", m.get(0).getDate().getValue());
        assertEquals("Wyoming, USA", m.get(0).getPlace().getPlaceName());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getMilitaryId(Individual)} and
     * {@link FamilyTreeMaker3Adapter#setMilitaryId(Individual, List)}
     */
    @Test
    public void testMilitaryIdNegative() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> m = a.getMilitaryId(jesse);
        assertNotNull(m);
        assertEquals(0, m.size());

        List<CustomFact> m2 = new ArrayList<>();
        CustomFact cf = a.newMilitaryIdCustomFact();
        cf.setDate("01 JAN 1990");
        cf.setDescription("Frying Pan");
        Place p = new Place();
        p.setPlaceName("Wyoming, USA");
        cf.setPlace(p);
        m2.add(cf);
        a.setMilitaryId(jesse, m2);

        m = a.getMilitaryId(jesse);
        assertNotNull(m);
        assertEquals(1, m.size());
        assertEquals("Frying Pan", m.get(0).getDescription().getValue());
        assertEquals("01 JAN 1990", m.get(0).getDate().getValue());
        assertEquals("Wyoming, USA", m.get(0).getPlace().getPlaceName());

    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getMilitary(Individual)} and
     * {@link FamilyTreeMaker3Adapter#setMilitary(Individual, List)}
     */
    @Test
    public void testMilitaryNegative() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> m = a.getMilitary(jesse);
        assertNotNull(m);
        assertEquals(0, m.size());

        List<CustomFact> m2 = new ArrayList<>();
        CustomFact cf = a.newMilitaryCustomFact();
        cf.setDate("01 JAN 1990");
        cf.setDescription("Frying Pan");
        Place p = new Place();
        p.setPlaceName("Wyoming, USA");
        cf.setPlace(p);
        m2.add(cf);
        a.setMilitary(jesse, m2);

        m = a.getMilitary(jesse);
        assertNotNull(m);
        assertEquals(1, m.size());
        assertEquals("Frying Pan", m.get(0).getDescription().getValue());
        assertEquals("01 JAN 1990", m.get(0).getDate().getValue());
        assertEquals("Wyoming, USA", m.get(0).getPlace().getPlaceName());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getMission(Individual)} and {@link FamilyTreeMaker3Adapter#setMission(Individual, List)}
     */
    @Test
    public void testMissionNegative() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> m = a.getMission(jesse);
        assertNotNull(m);
        assertEquals(0, m.size());

        List<CustomFact> m2 = new ArrayList<>();
        CustomFact cf = a.newMissionCustomFact();
        cf.setDate("01 JAN 1990");
        cf.setDescription("Frying Pan");
        Place p = new Place();
        p.setPlaceName("Wyoming, USA");
        cf.setPlace(p);
        m2.add(cf);
        a.setMission(jesse, m2);

        m = a.getMission(jesse);
        assertNotNull(m);
        assertEquals(1, m.size());
        assertEquals("Frying Pan", m.get(0).getDescription().getValue());
        assertEquals("01 JAN 1990", m.get(0).getDate().getValue());
        assertEquals("Wyoming, USA", m.get(0).getPlace().getPlaceName());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getNamesake(Individual)} and
     * {@link FamilyTreeMaker3Adapter#setNamesake(Individual, List)}
     */
    @Test
    public void testNamesakeNegative() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> n = a.getNamesake(jesse);
        assertNotNull(n);
        assertEquals(0, n.size());

        List<CustomFact> n2 = new ArrayList<>();
        CustomFact cf = a.newNamesakeCustomFact();
        cf.setDate("01 JAN 1990");
        cf.setDescription("Frying Pan");
        Place p = new Place();
        p.setPlaceName("Wyoming, USA");
        cf.setPlace(p);
        n2.add(cf);
        a.setNamesake(jesse, n2);

        n = a.getNamesake(jesse);
        assertNotNull(n);
        assertEquals(1, n.size());
        assertEquals("Frying Pan", n.get(0).getDescription().getValue());
        assertEquals("01 JAN 1990", n.get(0).getDate().getValue());
        assertEquals("Wyoming, USA", n.get(0).getPlace().getPlaceName());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getOrdinance(Individual)} and
     * {@link FamilyTreeMaker3Adapter#setOrdinance(Individual, List)}
     */
    @Test
    public void testOrdinanceNegative() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> o = a.getOrdinance(jesse);
        assertNotNull(o);
        assertEquals(0, o.size());

        List<CustomFact> o2 = new ArrayList<>();
        CustomFact cf = a.newOrdinanceCustomFact();
        cf.setDate("01 JAN 1990");
        cf.setDescription("Frying Pan");
        Place p = new Place();
        p.setPlaceName("Wyoming, USA");
        cf.setPlace(p);
        o2.add(cf);
        a.setOrdinance(jesse, o2);

        o = a.getOrdinance(jesse);
        assertNotNull(o);
        assertEquals(1, o.size());
        assertEquals("Frying Pan", o.get(0).getDescription().getValue());
        assertEquals("01 JAN 1990", o.get(0).getDate().getValue());
        assertEquals("Wyoming, USA", o.get(0).getPlace().getPlaceName());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getOrigin(Individual)} and {@link FamilyTreeMaker3Adapter#setOrigin(Individual, List)}
     */
    @Test
    public void testOriginNegative() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> o = a.getOrigin(jesse);
        assertNotNull(o);
        assertEquals(0, o.size());

        List<CustomFact> o2 = new ArrayList<>();
        CustomFact cf = a.newOriginCustomFact();
        cf.setDate("01 JAN 1990");
        cf.setDescription("Frying Pan");
        Place p = new Place();
        p.setPlaceName("Wyoming, USA");
        cf.setPlace(p);
        o2.add(cf);
        a.setOrigin(jesse, o2);

        o = a.getOrigin(jesse);
        assertNotNull(o);
        assertEquals(1, o.size());
        assertEquals("Frying Pan", o.get(0).getDescription().getValue());
        assertEquals("01 JAN 1990", o.get(0).getDate().getValue());
        assertEquals("Wyoming, USA", o.get(0).getPlace().getPlaceName());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getPhoto(Individual)} and {@link FamilyTreeMaker3Adapter#setPhoto(Individual, List)}
     */
    @Test
    public void testPhotoNegative() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> o = a.getPhoto(jesse);
        assertNotNull(o);
        assertEquals(0, o.size());

        List<CustomFact> o2 = new ArrayList<>();
        CustomFact cf = a.newPhotoCustomFact();
        cf.setDate("01 JAN 1990");
        cf.setDescription("Frying Pan");
        Place p = new Place();
        p.setPlaceName("Wyoming, USA");
        cf.setPlace(p);
        o2.add(cf);
        a.setPhoto(jesse, o2);

        o = a.getPhoto(jesse);
        assertNotNull(o);
        assertEquals(1, o.size());
        assertEquals("Frying Pan", o.get(0).getDescription().getValue());
        assertEquals("01 JAN 1990", o.get(0).getDate().getValue());
        assertEquals("Wyoming, USA", o.get(0).getPlace().getPlaceName());
    }

    /**
     * Negative test for {@link FamilyTreeMaker3Adapter#getSeparation(Family)},
     * {@link FamilyTreeMaker3Adapter#newSeparationCustomFact()}, and {@link FamilyTreeMaker3Adapter#setSeparation(Family, List)}
     */
    @Test
    public void testSeparationNegative() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> separations = a.getSeparation(family2);
        assertNotNull(separations);
        assertEquals(0, separations.size());
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getWeight(Individual)} and {@link FamilyTreeMaker3Adapter#setWeight(Individual, List)}
     */
    @Test
    public void testWeightNegative() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> w = a.getWeight(jesse);
        assertNotNull(w);
        assertEquals(0, w.size());

        List<CustomFact> w2 = new ArrayList<>();
        CustomFact cf = a.newWeightCustomFact();
        cf.setDate("01 JAN 1990");
        cf.setDescription("Frying Pan");
        Place p = new Place();
        p.setPlaceName("Wyoming, USA");
        cf.setPlace(p);
        w2.add(cf);
        a.setWeight(jesse, w2);

        w = a.getWeight(jesse);
        assertNotNull(w);
        assertEquals(1, w.size());
        assertEquals("Frying Pan", w.get(0).getDescription().getValue());
        assertEquals("01 JAN 1990", w.get(0).getDate().getValue());
        assertEquals("Wyoming, USA", w.get(0).getPlace().getPlaceName());
    }
}