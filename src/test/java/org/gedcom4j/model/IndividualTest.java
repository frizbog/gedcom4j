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
package org.gedcom4j.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.enumerations.IndividualAttributeType;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.query.Finder;
import org.junit.Test;

/**
 * @author frizbog1
 * 
 */
@SuppressWarnings({ "PMD.TooManyMethods", "PMD.SystemPrintln" })
public class IndividualTest {

    /**
     * Helper method to add attributes of a specific type to an individual
     * 
     * @param i
     *            the individual to add to
     * @param t
     *            the type of attribute
     */
    private static void addAttributeOfType(Individual i, IndividualAttributeType t) {
        IndividualAttribute e = new IndividualAttribute();
        e.setType(t);
        e.setDescription("Random text for uniqueness " + Math.random());
        i.getAttributes(true).add(e);
    }

    /**
     * Helper method to add a basic name to an individual
     * 
     * @param i
     *            the individual
     * @param string
     *            the name
     */
    private static void addBasicName(Individual i, String string) {
        PersonalName pn = new PersonalName();
        pn.setBasic(string);
        i.getNames(true).add(pn);

    }

    /**
     * Helper method to add events of a specific type to an individual
     * 
     * @param i
     *            the individual to add to
     * @param t
     *            the type of event
     */
    private static void addEventOfType(Individual i, IndividualEventType t) {
        IndividualEvent e = new IndividualEvent();
        e.setType(t);
        e.setDescription("Random text for uniqueness " + Math.random());
        i.getEvents(true).add(e);
    }

    /**
     * Helper method to get a person and assert they exist
     * 
     * @param gedcom
     *            the gedcom we're searching over
     * @param surname
     *            the surname of the person we want
     * @param givenName
     *            the given name of the person we want
     * @return the person
     */
    private static Individual getPerson(Gedcom gedcom, String surname, String givenName) {
        Individual result = new Finder(gedcom).findByName(surname, givenName).get(0);
        assertNotNull("Couldn't find " + givenName + " " + surname + " by name in the gedcom", result);
        return result;
    }

    /**
     * Test method for {@link org.gedcom4j.model.Individual#equals(java.lang.Object)} .
     */
    @Test
    public void testEqualsObject() {
        Individual i1 = new Individual();
        assertFalse(i1.equals(null));
        assertFalse(i1.equals(new Corporation()));
        assertEquals(i1, i1);

        Individual i2 = new Individual();
        assertEquals(i1, i2);

        i2.getAliases(true).add(new StringWithCustomFacts("Tim"));
        assertFalse(i1.equals(i2));
        i1.getAliases(true).add(new StringWithCustomFacts("Tim"));
        assertEquals(i1, i2);
        i1.getAliases().clear();
        assertFalse(i1.equals(i2));
        i2.getAliases().clear();
        assertEquals(i1, i2);

        i2.getAncestorInterest(true).add(new Submitter());
        assertFalse(i1.equals(i2));
        i1.getAncestorInterest(true).add(new Submitter());
        assertEquals(i1, i2);
        i1.getAncestorInterest().clear();
        assertFalse(i1.equals(i2));
        i2.getAncestorInterest().clear();
        assertEquals(i1, i2);

        i2.setAncestralFileNumber("i1");
        assertFalse(i1.equals(i2));
        i1.setAncestralFileNumber("i1");
        assertEquals(i1, i2);
        i2.setAncestralFileNumber((String) null);
        assertFalse(i1.equals(i2));
        i1.setAncestralFileNumber((String) null);
        assertEquals(i1, i2);

        i2.getAssociations(true).add(new Association());
        assertFalse(i1.equals(i2));
        i1.getAssociations(true).add(new Association());
        assertEquals(i1, i2);
        i1.getAssociations().clear();
        assertFalse(i1.equals(i2));
        i2.getAssociations().clear();
        assertEquals(i1, i2);

        i2.getAttributes(true).add(new IndividualAttribute());
        assertFalse(i1.equals(i2));
        i1.getAttributes(true).add(new IndividualAttribute());
        assertEquals(i1, i2);
        i1.getAttributes().clear();
        assertFalse(i1.equals(i2));
        i2.getAttributes().clear();
        assertEquals(i1, i2);

        i2.setChangeDate(new ChangeDate());
        assertFalse(i1.equals(i2));
        i1.setChangeDate(new ChangeDate());
        assertEquals(i1, i2);
        i2.setChangeDate((ChangeDate) null);
        assertFalse(i1.equals(i2));
        i1.setChangeDate((ChangeDate) null);
        assertEquals(i1, i2);

        i2.getCitations(true).add(new CitationWithSource());
        assertFalse(i1.equals(i2));
        i1.getCitations(true).add(new CitationWithSource());
        assertEquals(i1, i2);
        i1.getCitations().clear();
        assertFalse(i1.equals(i2));
        i2.getCitations().clear();
        assertEquals(i1, i2);

        i2.getDescendantInterest(true).add(new Submitter());
        assertFalse(i1.equals(i2));
        i1.getDescendantInterest(true).add(new Submitter());
        assertEquals(i1, i2);
        i1.getDescendantInterest().clear();
        assertFalse(i1.equals(i2));
        i2.getDescendantInterest().clear();
        assertEquals(i1, i2);

        i2.getEvents(true).add(new IndividualEvent());
        assertFalse(i1.equals(i2));
        i1.getEvents(true).add(new IndividualEvent());
        assertEquals(i1, i2);
        i1.getEvents().clear();
        assertFalse(i1.equals(i2));
        i2.getEvents().clear();
        assertEquals(i1, i2);

        i2.getFamiliesWhereChild(true).add(new FamilyChild());
        assertFalse(i1.equals(i2));
        i1.getFamiliesWhereChild(true).add(new FamilyChild());
        assertEquals(i1, i2);
        i1.getFamiliesWhereChild().clear();
        assertFalse(i1.equals(i2));
        i2.getFamiliesWhereChild().clear();
        assertEquals(i1, i2);

        i2.getFamiliesWhereSpouse(true).add(new FamilySpouse());
        assertFalse(i1.equals(i2));
        i1.getFamiliesWhereSpouse(true).add(new FamilySpouse());
        assertEquals(i1, i2);
        i1.getFamiliesWhereSpouse().clear();
        assertFalse(i1.equals(i2));
        i2.getFamiliesWhereSpouse().clear();
        assertEquals(i1, i2);

        i2.getLdsIndividualOrdinances(true).add(new LdsIndividualOrdinance());
        assertFalse(i1.equals(i2));
        i1.getLdsIndividualOrdinances(true).add(new LdsIndividualOrdinance());
        assertEquals(i1, i2);
        i1.getLdsIndividualOrdinances().clear();
        assertFalse(i1.equals(i2));
        i2.getLdsIndividualOrdinances().clear();
        assertEquals(i1, i2);

        i2.getMultimedia(true).add(new MultimediaReference());
        assertFalse(i1.equals(i2));
        i1.getMultimedia(true).add(new MultimediaReference());
        assertEquals(i1, i2);
        i1.getMultimedia().clear();
        assertFalse(i1.equals(i2));
        i2.getMultimedia().clear();
        assertEquals(i1, i2);

        i2.getNames(true).add(new PersonalName());
        assertFalse(i1.equals(i2));
        i1.getNames(true).add(new PersonalName());
        assertEquals(i1, i2);
        i1.getNames().clear();
        assertFalse(i1.equals(i2));
        i2.getNames().clear();
        assertEquals(i1, i2);

        i2.setPermanentRecFileNumber("1");
        assertFalse(i1.equals(i2));
        i1.setPermanentRecFileNumber("1");
        assertEquals(i1, i2);
        i2.setPermanentRecFileNumber((String) null);
        assertFalse(i1.equals(i2));
        i1.setPermanentRecFileNumber((String) null);
        assertEquals(i1, i2);

        i2.setRecIdNumber("1");
        assertFalse(i1.equals(i2));
        i1.setRecIdNumber("1");
        assertEquals(i1, i2);
        i2.setRecIdNumber((String) null);
        assertFalse(i1.equals(i2));
        i1.setRecIdNumber((String) null);
        assertEquals(i1, i2);

        i2.setRestrictionNotice("none");
        assertFalse(i1.equals(i2));
        i1.setRestrictionNotice("none");
        assertEquals(i1, i2);
        i2.setRestrictionNotice((String) null);
        assertFalse(i1.equals(i2));
        i1.setRestrictionNotice((String) null);
        assertEquals(i1, i2);

        i2.setSex("Male");
        assertFalse(i1.equals(i2));
        i1.setSex("Male");
        assertEquals(i1, i2);
        i2.setSex((String) null);
        assertFalse(i1.equals(i2));
        i1.setSex((String) null);
        assertEquals(i1, i2);

        i2.getSubmitters(true).add(new Submitter());
        assertFalse(i1.equals(i2));
        i1.getSubmitters(true).add(new Submitter());
        assertEquals(i1, i2);
        i1.getSubmitters().clear();
        assertFalse(i1.equals(i2));
        i2.getSubmitters().clear();
        assertEquals(i1, i2);

        i2.getUserReferences(true).add(new UserReference());
        assertFalse(i1.equals(i2));
        i1.getUserReferences(true).add(new UserReference());
        assertEquals(i1, i2);
        i1.getUserReferences().clear();
        assertFalse(i1.equals(i2));
        i2.getUserReferences().clear();
        assertEquals(i1, i2);

        i2.setXref("23");
        assertFalse(i1.equals(i2));
        i1.setXref("23");
        assertEquals(i1, i2);
        i2.setXref((String) null);
        assertFalse(i1.equals(i2));
        i1.setXref((String) null);
        assertEquals(i1, i2);
    }

    /**
     * Test method for {@link org.gedcom4j.model.Individual#getFormattedName()}.
     */
    @Test
    public void testFormattedName() {
        Individual i = new Individual();
        addBasicName(i, "Bob /Dylan/");
        assertEquals("Bob /Dylan/", i.getFormattedName());
        addBasicName(i, "Robert Allen /Zimmerman/");
        assertEquals("Bob /Dylan/ aka Robert Allen /Zimmerman/", i.getFormattedName());
    }

    /**
     * Test method for {@link org.gedcom4j.model.Individual#getAncestors()}.
     * 
     * @throws GedcomParserException
     *             if the sample gedcom won't parse
     * @throws IOException
     *             if there's a problem reading the sample file
     */
    @Test
    public void testGetAncestors() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/RelationshipTest.ged");
        assertTrue(gp.getErrors().isEmpty());
        assertTrue(gp.getWarnings().isEmpty());
        Gedcom g = gp.getGedcom();
        assertNotNull(g);
        assertEquals("There are supposed to be 43 people in the gedcom - are you using the right file/file version?", 43, g
                .getIndividuals().size());
        assertEquals("There are supposed to be 18 families in the gedcom - are you using the right file/file version?", 18, g
                .getFamilies().size());

        Individual robert = getPerson(g, "Andrews", "Robert");
        Set<Individual> ancestors = robert.getAncestors();
        assertNotNull(ancestors);
        assertEquals(8, ancestors.size());
        Individual kenneth = getPerson(g, "Struthers", "Kenneth");
        ancestors = kenneth.getAncestors();
        assertNotNull(ancestors);
        assertEquals(0, ancestors.size());

    }

    /**
     * Test method for
     * {@link org.gedcom4j.model.Individual#getAttributesOfType(org.gedcom4j.model.enumerations.IndividualAttributeType)} .
     */
    @Test
    public void testGetAttributesOfType() {
        Individual i = new Individual();
        addAttributeOfType(i, IndividualAttributeType.OCCUPATION);
        addAttributeOfType(i, IndividualAttributeType.RELIGIOUS_AFFILIATION);
        addAttributeOfType(i, IndividualAttributeType.RESIDENCE);
        addAttributeOfType(i, IndividualAttributeType.RESIDENCE);
        List<IndividualAttribute> events = i.getAttributesOfType(IndividualAttributeType.OCCUPATION);
        assertNotNull(events);
        assertEquals(1, events.size());
        events = i.getAttributesOfType(IndividualAttributeType.RESIDENCE);
        assertNotNull(events);
        assertEquals(2, events.size());
        events = i.getAttributesOfType(IndividualAttributeType.SOCIAL_SECURITY_NUMBER);
        assertNotNull(events);
        assertEquals(0, events.size());
    }

    /**
     * Test method for {@link org.gedcom4j.model.Individual#getDescendants()}.
     * 
     * @throws GedcomParserException
     *             if the sample file can't be parsed
     * @throws IOException
     *             if the sample file can't be read
     */
    @Test
    public void testGetDescendants() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/RelationshipTest.ged");
        assertTrue(gp.getErrors().isEmpty());
        assertTrue(gp.getWarnings().isEmpty());
        Gedcom g = gp.getGedcom();
        assertNotNull(g);
        assertEquals("There are supposed to be 43 people in the gedcom - are you using the right file/file version?", 43, g
                .getIndividuals().size());
        assertEquals("There are supposed to be 18 families in the gedcom - are you using the right file/file version?", 18, g
                .getFamilies().size());

        Individual alex = getPerson(g, "Zucco", "Alex");
        Set<Individual> descendants = alex.getDescendants();
        assertNotNull(descendants);
        assertEquals(0, descendants.size());
        Individual steven = getPerson(g, "Struthers", "Steven");
        descendants = steven.getDescendants();
        assertNotNull(descendants);
        for (Individual d : descendants) {
            System.out.println(d);
        }
        assertEquals(7, descendants.size());
    }

    /**
     * Test method for {@link org.gedcom4j.model.Individual#getEventsOfType(org.gedcom4j.model.enumerations.IndividualEventType)} .
     */
    @Test
    public void testGetEventsOfType() {
        Individual i = new Individual();
        addEventOfType(i, IndividualEventType.ADOPTION);
        addEventOfType(i, IndividualEventType.CENSUS);
        addEventOfType(i, IndividualEventType.CENSUS);
        addEventOfType(i, IndividualEventType.CENSUS);
        List<IndividualEvent> events = i.getEventsOfType(IndividualEventType.ADOPTION);
        assertNotNull(events);
        assertEquals(1, events.size());
        events = i.getEventsOfType(IndividualEventType.CENSUS);
        assertNotNull(events);
        assertEquals(3, events.size());
        events = i.getEventsOfType(IndividualEventType.BAR_MITZVAH);
        assertNotNull(events);
        assertEquals(0, events.size());
    }

    /**
     * Test method for {@link org.gedcom4j.model.Individual#getSpouses()}.
     */
    @Test
    public void testGetSpouses() {
        Individual i = new Individual();
        assertNotNull(i.getSpouses());
        assertTrue(i.getSpouses().isEmpty());
        FamilySpouse f = new FamilySpouse();
        f.setFamily(new Family());
        f.getFamily().setHusband(new IndividualReference(i));
        i.getFamiliesWhereSpouse(true).add(f);
        assertNotNull(i.getSpouses());
        assertTrue("Should still be empty because there is no wife in the family that this guy's a spouse in", i.getSpouses()
                .isEmpty());
        f.getFamily().setWife(new IndividualReference(new Individual()));
        addBasicName(f.getFamily().getWife().getIndividual(), "Anna //");
        assertNotNull(i.getSpouses());
        assertEquals("Ok, now there's a wife, should be exactly one spouse", 1, i.getSpouses().size());

        // Add a second family and spouse
        f = new FamilySpouse();
        f.setFamily(new Family());
        f.getFamily().setHusband(new IndividualReference(i));
        i.getFamiliesWhereSpouse(true).add(f);
        assertNotNull(i.getSpouses());
        assertEquals("Should still be just one spouse because there is no wife in the 2nd family that this guy's a spouse in", 1, i
                .getSpouses().size());

        f.getFamily().setWife(new IndividualReference(new Individual()));
        addBasicName(f.getFamily().getWife().getIndividual(), "Elizabeth /Hofstadt/");
        assertNotNull(i.getSpouses());
        assertEquals("Ok, now there's a wife in the 2nd family, should be exactly two spouses", 2, i.getSpouses().size());
    }

    /**
     * Test method for {@link org.gedcom4j.model.Individual#hashCode()}.
     */
    @Test
    public void testHashCode() {
        Individual i1 = new Individual();
        Individual i2 = new Individual();
        assertTrue(i1.hashCode() == i2.hashCode());
        i1.setAddress(new Address());
        assertFalse(i1.hashCode() == i2.hashCode());
        i2.setAddress(new Address());
        assertTrue(i1.hashCode() == i2.hashCode());
    }

    /**
     * Test method for {@link org.gedcom4j.model.Individual#toString()}.
     */
    @Test
    public void testToString1() {
        Individual i = new Individual();
        assertEquals("Unknown name", i.toString());
    }

    /**
     * Test method for {@link org.gedcom4j.model.Individual#toString()}.
     */
    @Test
    public void testToString2() {
        Individual i = new Individual();
        addBasicName(i, "Bob /Dylan/");
        assertEquals("Bob /Dylan/", i.getFormattedName());
        addBasicName(i, "Robert Allen /Zimmerman/");
        assertEquals("Bob /Dylan/ aka Robert Allen /Zimmerman/", i.toString());
    }

    /**
     * Test method for {@link org.gedcom4j.model.Individual#toString()}.
     */
    @Test
    public void testToString3() {
        Individual i = new Individual();
        addBasicName(i, "Donald /Draper/");
        FamilySpouse f = new FamilySpouse();
        f.setFamily(new Family());
        f.getFamily().setHusband(new IndividualReference(i));
        i.getFamiliesWhereSpouse(true).add(f);
        f.getFamily().setWife(new IndividualReference(new Individual()));
        addBasicName(f.getFamily().getWife().getIndividual(), "Anna //");
        // Add a second family and spouse
        f = new FamilySpouse();
        f.setFamily(new Family());
        f.getFamily().setHusband(new IndividualReference(i));
        i.getFamiliesWhereSpouse(true).add(f);

        f.getFamily().setWife(new IndividualReference(new Individual()));
        addBasicName(f.getFamily().getWife().getIndividual(), "Elizabeth /Hofstadt/");
        assertEquals("Donald /Draper/, spouse of Anna //, spouse of Elizabeth /Hofstadt/", i.toString());
    }

}
