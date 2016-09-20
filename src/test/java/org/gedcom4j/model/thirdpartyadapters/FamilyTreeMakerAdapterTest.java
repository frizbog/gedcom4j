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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.Source;
import org.gedcom4j.parser.GedcomParser;
import org.junit.Before;
import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @author frizbog
 *
 */
@SuppressWarnings("PMD.TooManyMethods")
public class FamilyTreeMakerAdapterTest {

    /**
     * The gedcom test fixture we're going to work with
     */
    private Gedcom g;

    /** John, one of the people in the GEDCOM test file. */
    private Individual john;

    /** Jesse, one of the people in the GEDCOM test file. */
    private Individual jesse;

    /** June, one of the people in the GEDCOM test file. */
    private Individual june;

    /** The family in the GEDCOM test file. */
    private Family family;

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
        GedcomParser gp = new GedcomParser();
        gp.load("sample/ftmcustomtags.ged");
        g = gp.getGedcom();
        john = g.getIndividuals().get("@I1@");
        jesse = g.getIndividuals().get("@I2@");
        june = g.getIndividuals().get("@I3@");
        family = g.getFamilies().get("@F1@");
        photo = g.getMultimedia().get("@M1@");
        source = g.getSources().get("@S1@");
    }

    /**
     * Test {@link FamilyTreeMakerAdapter#getCauseOfDeath(Individual)}
     */
    @Test
    public void testCauseOfDeathNegative() {
        FamilyTreeMakerAdapter a = new FamilyTreeMakerAdapter();
        List<String> cod = a.getCauseOfDeath(jesse);
        assertNotNull(cod);
        assertEquals(0, cod.size());

        a.setCausesOfDeath(jesse, Arrays.asList(new String[] { "Frying Pan" }));
        cod = a.getCauseOfDeath(jesse);
        assertNotNull(cod);
        assertEquals(1, cod.size());
        assertEquals("Frying Pan", cod.get(0));

    }

    /**
     * Test {@link FamilyTreeMakerAdapter#getCauseOfDeath(Individual)}.
     */
    @Test
    public void testCauseOfDeathPositive() {
        FamilyTreeMakerAdapter a = new FamilyTreeMakerAdapter();
        List<String> cod = a.getCauseOfDeath(john);
        assertNotNull(cod);
        assertEquals(1, cod.size());
        assertEquals("Alien abduction", cod.get(0));

        a.setCausesOfDeath(john, new ArrayList<String>());
        cod = a.getCauseOfDeath(jesse);
        assertNotNull(cod);
        assertEquals(0, cod.size());
    }

    /**
     * Test {@link FamilyTreeMakerAdapter#getCircumcision(Individual)}
     */
    @Test
    public void testCircumcisionNegative() {
        FamilyTreeMakerAdapter a = new FamilyTreeMakerAdapter();
        List<DatePlaceDescription> c = a.getCircumcision(jesse);
        assertNotNull(c);
        assertEquals(0, c.size());

        List<DatePlaceDescription> c2 = new ArrayList<>();
        DatePlaceDescription dpd = new DatePlaceDescription();
        dpd.setDate("01 JAN 1990");
        dpd.setDescription("Frying Pan");
        dpd.setPlace("Wyoming, USA");
        c2.add(dpd);

        a.setCircumcision(jesse, c2);
        c = a.getCircumcision(jesse);
        assertNotNull(c);
        assertEquals(1, c.size());
        assertEquals("Frying Pan", c.get(0).getDescription());
        assertEquals("01 JAN 1990", c.get(0).getDate());
        assertEquals("Wyoming, USA", c.get(0).getPlace());
    }

    /**
     * Test {@link FamilyTreeMakerAdapter#getCircumcision(Individual)}
     */
    @Test
    public void testCircumcisionPositive() {
        FamilyTreeMakerAdapter a = new FamilyTreeMakerAdapter();
        List<DatePlaceDescription> c = a.getCircumcision(john);
        assertNotNull(c);
        assertEquals(1, c.size());
        assertEquals("Dr. Streck performed", c.get(0).getDescription());
        assertEquals("08 JAN 1950", c.get(0).getDate());
        assertEquals("Colonial Heights, Independent Cities, Virginia, USA", c.get(0).getPlace());

        a.setCircumcision(john, new ArrayList<DatePlaceDescription>());
        c = a.getCircumcision(jesse);
        assertNotNull(c);
        assertEquals(0, c.size());
    }

    /**
     * Test {@link FamilyTreeMakerAdapter#getDestinations(Individual)}
     */
    @Test
    public void testDestinationNegative() {
        FamilyTreeMakerAdapter a = new FamilyTreeMakerAdapter();
        List<String> d = a.getDestinations(jesse);
        assertNotNull(d);
        assertEquals(0, d.size());

        a.setDestination(jesse, Arrays.asList(new String[] { "Frying Pan" }));
        d = a.getDestinations(jesse);
        assertNotNull(d);
        assertEquals(1, d.size());
        assertEquals("Frying Pan", d.get(0));

    }

    /**
     * Test {@link FamilyTreeMakerAdapter#getDestinations(Individual)}.
     */
    @Test
    public void testDestinationPositive() {
        FamilyTreeMakerAdapter a = new FamilyTreeMakerAdapter();
        List<String> d = a.getDestinations(john);
        assertNotNull(d);
        assertEquals(1, d.size());
        assertEquals("Alpha Centauri", d.get(0));

        a.setDestination(john, new ArrayList<String>());
        d = a.getDestinations(jesse);
        assertNotNull(d);
        assertEquals(0, d.size());
    }

    /**
     * Test {@link FamilyTreeMakerAdapter#getElected(Individual)}
     */
    @Test
    public void testElectedNegative() {
        FamilyTreeMakerAdapter a = new FamilyTreeMakerAdapter();
        List<DatePlaceDescription> el = a.getElected(jesse);
        assertNotNull(el);
        assertEquals(0, el.size());

        List<DatePlaceDescription> el2 = new ArrayList<>();
        DatePlaceDescription dpd = new DatePlaceDescription();
        dpd.setDate("01 JAN 1990");
        dpd.setDescription("Frying Pan");
        dpd.setPlace("Wyoming, USA");
        el2.add(dpd);
        a.setElected(jesse, el2);

        el = a.getElected(jesse);
        assertNotNull(el);
        assertEquals(1, el.size());
        assertEquals("Frying Pan", el.get(0).getDescription());
        assertEquals("01 JAN 1990", el.get(0).getDate());
        assertEquals("Wyoming, USA", el.get(0).getPlace());
    }

    /**
     * Test {@link FamilyTreeMakerAdapter#getElected(Individual)}
     */
    @Test
    public void testElectedPositive() {
        FamilyTreeMakerAdapter a = new FamilyTreeMakerAdapter();
        List<DatePlaceDescription> el = a.getElected(john);
        assertNotNull(el);
        assertEquals(1, el.size());
        assertEquals("Mayor of Munchkin City", el.get(0).getDescription());
        assertEquals("08 NOV 1992", el.get(0).getDate());
        assertEquals("Orlando, Brevard, Florida, USA", el.get(0).getPlace());

        a.setElected(john, new ArrayList<DatePlaceDescription>());
        el = a.getElected(jesse);
        assertNotNull(el);
        assertEquals(0, el.size());
    }

    /**
     * Test {@link FamilyTreeMakerAdapter#getEmployment(Individual)}
     */
    @Test
    public void testEmploymentNegative() {
        FamilyTreeMakerAdapter a = new FamilyTreeMakerAdapter();
        List<DatePlaceDescription> em = a.getEmployment(jesse);
        assertNotNull(em);
        assertEquals(0, em.size());

        List<DatePlaceDescription> em2 = new ArrayList<>();
        DatePlaceDescription dpd = new DatePlaceDescription();
        dpd.setDate("01 JAN 1990");
        dpd.setDescription("Frying Pan");
        dpd.setPlace("Wyoming, USA");
        em2.add(dpd);
        a.setEmployment(jesse, em2);

        em = a.getEmployment(jesse);
        assertNotNull(em);
        assertEquals(1, em.size());
        assertEquals("Frying Pan", em.get(0).getDescription());
        assertEquals("01 JAN 1990", em.get(0).getDate());
        assertEquals("Wyoming, USA", em.get(0).getPlace());
    }

    /**
     * Test {@link FamilyTreeMakerAdapter#getEmployment(Individual)}
     */
    @Test
    public void testEmploymentPositive() {
        FamilyTreeMakerAdapter a = new FamilyTreeMakerAdapter();
        List<DatePlaceDescription> em = a.getEmployment(john);
        assertNotNull(em);
        assertEquals(1, em.size());
        assertEquals("Keeling moose and squirrel", em.get(0).getDescription());
        assertEquals("1981", em.get(0).getDate());
        assertEquals("Frostbite Falls, Minnesota, USA", em.get(0).getPlace());

        a.setEmployment(john, new ArrayList<DatePlaceDescription>());
        em = a.getEmployment(jesse);
        assertNotNull(em);
        assertEquals(0, em.size());
    }

    /**
     * Test {@link FamilyTreeMakerAdapter#getExcommunication(Individual)}
     */
    @Test
    public void testExcommunicationNegative() {
        FamilyTreeMakerAdapter a = new FamilyTreeMakerAdapter();
        List<DatePlaceDescription> ex = a.getExcommunication(jesse);
        assertNotNull(ex);
        assertEquals(0, ex.size());

        List<DatePlaceDescription> ex2 = new ArrayList<>();
        DatePlaceDescription dpd = new DatePlaceDescription();
        dpd.setDate("01 JAN 1990");
        dpd.setDescription("Frying Pan");
        dpd.setPlace("Wyoming, USA");
        ex2.add(dpd);
        a.setExcommunication(jesse, ex2);

        ex = a.getExcommunication(jesse);
        assertNotNull(ex);
        assertEquals(1, ex.size());
        assertEquals("Frying Pan", ex.get(0).getDescription());
        assertEquals("01 JAN 1990", ex.get(0).getDate());
        assertEquals("Wyoming, USA", ex.get(0).getPlace());
    }

    /**
     * Test {@link FamilyTreeMakerAdapter#getExcommunication(Individual)}
     */
    @Test
    public void testExcommunicationPositive() {
        FamilyTreeMakerAdapter a = new FamilyTreeMakerAdapter();
        List<DatePlaceDescription> ex = a.getExcommunication(john);
        assertNotNull(ex);
        assertEquals(1, ex.size());
        assertEquals("Church of the Flying Spaghetti Monster", ex.get(0).getDescription());
        assertEquals("22 NOV 1982", ex.get(0).getDate());
        assertEquals("Washington City, District Of Columbia, District of Columbia, USA", ex.get(0).getPlace());

        a.setExcommunication(john, new ArrayList<DatePlaceDescription>());
        ex = a.getExcommunication(jesse);
        assertNotNull(ex);
        assertEquals(0, ex.size());
    }

    /**
     * Test {@link FamilyTreeMakerAdapter#getFuneral(Individual)}
     */
    @Test
    public void testFuneralNegative() {
        FamilyTreeMakerAdapter a = new FamilyTreeMakerAdapter();
        List<DatePlaceDescription> fun = a.getFuneral(jesse);
        assertNotNull(fun);
        assertEquals(0, fun.size());

        List<DatePlaceDescription> fun2 = new ArrayList<>();
        DatePlaceDescription dpd = new DatePlaceDescription();
        dpd.setDate("01 JAN 1990");
        dpd.setDescription("Frying Pan");
        dpd.setPlace("Wyoming, USA");
        fun2.add(dpd);
        a.setFuneral(jesse, fun2);

        fun = a.getFuneral(jesse);
        assertNotNull(fun);
        assertEquals(1, fun.size());
        assertEquals("Frying Pan", fun.get(0).getDescription());
        assertEquals("01 JAN 1990", fun.get(0).getDate());
        assertEquals("Wyoming, USA", fun.get(0).getPlace());
    }

    /**
     * Test {@link FamilyTreeMakerAdapter#getFuneral(Individual)}
     */
    @Test
    public void testFuneralPositive() {
        FamilyTreeMakerAdapter a = new FamilyTreeMakerAdapter();
        List<DatePlaceDescription> fun = a.getFuneral(john);
        assertNotNull(fun);
        assertEquals(1, fun.size());
        assertEquals("Nobody showed up", fun.get(0).getDescription());
        assertEquals("04 JUL 2000", fun.get(0).getDate());
        assertEquals("Honolulu, Honolulu, Hawaii, USA", fun.get(0).getPlace());

        a.setFuneral(john, new ArrayList<DatePlaceDescription>());
        fun = a.getFuneral(jesse);
        assertNotNull(fun);
        assertEquals(0, fun.size());
    }
}
