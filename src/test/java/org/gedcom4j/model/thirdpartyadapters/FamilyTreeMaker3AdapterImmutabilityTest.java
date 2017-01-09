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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.List;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.CustomFact;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.InMemoryGedcom;
import org.gedcom4j.model.IGedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualReference;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.Source;
import org.gedcom4j.parser.GedcomParser;
import org.junit.Before;
import org.junit.Test;

/**
 * Test all the methods from {@link FamilyTreeMaker3Adapter} for immutability of collections returned.
 * 
 * @author frizbog
 */
@SuppressWarnings("PMD.TooManyMethods")
public class FamilyTreeMaker3AdapterImmutabilityTest {

    /** Jesse, one of the people in the GEDCOM test file. */
    private Individual jesse;

    /** The main family (consisting of jesse, june, and john) in the GEDCOM test file. */
    private Family family1;

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
        GedcomParser gp = new GedcomParser(new InMemoryGedcom());
        gp.load("sample/ftmcustomtags.ged");
        final IGedcom g = gp.getGedcom();

        final Individual john = g.getIndividuals().get("@I1@");
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

        final Multimedia photo = g.getMultimedia().get("@M1@");
        assertNotNull(photo);

        final Source source = g.getSources().get("@S1@");
        assertNotNull(source);
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getCauseOfDeath(Individual)} for immutability
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCauseOfDeathImmutable() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> cod = a.getCauseOfDeath(jesse);
        assertNotNull(cod);
        cod.clear();
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getCircumcision(Individual)} for immutability
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCircumcisionImmutable() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> cfs = a.getCircumcision(jesse);
        assertNotNull(cfs);
        cfs.clear();
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getDestinations(Individual)} for immutability
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testDestinationImmutable() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> cfs = a.getDestinations(jesse);
        assertNotNull(cfs);
        cfs.clear();
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getElected(Individual)} for immutability
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testElectedImmutable() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> cfs = a.getElected(jesse);
        assertNotNull(cfs);
        cfs.clear();
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getEmployment(Individual)} for immutability
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testEmploymentImmutable() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> cfs = a.getEmployment(jesse);
        assertNotNull(cfs);
        cfs.clear();
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getExcommunication(Individual)} for immutability
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testExcommunicationImmutable() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> cfs = a.getExcommunication(jesse);
        assertNotNull(cfs);
        cfs.clear();
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getFatherRelationship(IndividualReference)} for immutability
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testFatherRelationshipImmutable() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        IndividualReference ir = new IndividualReference(jesse);
        List<CustomFact> cfs = a.getFatherRelationship(ir);
        assertNotNull(cfs);
        cfs.clear();
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getFuneral(Individual)} for immutability
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testFuneralImmutable() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> cfs = a.getFuneral(jesse);
        assertNotNull(cfs);
        cfs.clear();
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getHeights(Individual)} for immutability
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testHeightImmutable() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> cfs = a.getHeights(jesse);
        assertNotNull(cfs);
        cfs.clear();
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getInitiatory(Individual)} for immutability
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testInitiatoryImmutable() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> cfs = a.getInitiatory(jesse);
        assertNotNull(cfs);
        cfs.clear();
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getMedical(Individual)} for immutability
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMedicalImmutable() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> cfs = a.getMedical(jesse);
        assertNotNull(cfs);
        cfs.clear();
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getMilitaryId(Individual)} for immutability
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMilitaryIdImmutable() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> cfs = a.getMilitaryId(jesse);
        assertNotNull(cfs);
        cfs.clear();
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getMilitary(Individual)} for immutability
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMilitaryImmutable() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> cfs = a.getMilitary(jesse);
        assertNotNull(cfs);
        cfs.clear();
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getMission(Individual)} for immutability
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMissionImmutable() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> cfs = a.getMission(jesse);
        assertNotNull(cfs);
        cfs.clear();
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getMotherRelationship(IndividualReference)} for immutability
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMotherRelationshipImmutable() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        IndividualReference ir = new IndividualReference(jesse);
        List<CustomFact> cfs = a.getMotherRelationship(ir);
        assertNotNull(cfs);
        cfs.clear();
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getNamesake(Individual)} for immutability
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNamesakeImmutable() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> cfs = a.getNamesake(jesse);
        assertNotNull(cfs);
        cfs.clear();
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getOrdinance(Individual)} for immutability
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testOrdinanceImmutable() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> cfs = a.getOrdinance(jesse);
        assertNotNull(cfs);
        cfs.clear();
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getOrigin(Individual)} for immutability
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testOriginImmutable() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> cfs = a.getOrigin(jesse);
        assertNotNull(cfs);
        cfs.clear();
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getPhoto(Individual)} for immutability
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testPhotoImmutable() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> cfs = a.getPhoto(jesse);
        assertNotNull(cfs);
        cfs.clear();
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getSeparation(Family)} for immutability
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSeparationImmutable() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> cfs = a.getSeparation(family1);
        assertNotNull(cfs);
        cfs.clear();
    }

    /**
     * Test {@link FamilyTreeMaker3Adapter#getWeight(Individual)} for immutability
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testWeightImmutable() {
        FamilyTreeMaker3Adapter a = new FamilyTreeMaker3Adapter();
        List<CustomFact> cfs = a.getWeight(jesse);
        assertNotNull(cfs);
        cfs.clear();
    }
}