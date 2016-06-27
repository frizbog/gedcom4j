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
package org.gedcom4j.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.query.Finder;
import org.junit.Test;

/**
 * @author frizbog1
 * 
 */
public class IndividualTest {

    /**
     * Test method for {@link org.gedcom4j.model.Individual#equals(java.lang.Object)} .
     */
    @Test
    public void testEqualsObject() {
        Individual i1 = new Individual();
        Individual i2 = new Individual();
        assertTrue(i1.equals(i2));
        i1.address = new Address();
        assertFalse(i1.equals(i2));
        i2.address = new Address();
        assertTrue(i1.equals(i2));
    }

    /**
     * Test method for {@link org.gedcom4j.model.Individual#formattedName()}.
     */
    @Test
    public void testFormattedName() {
        Individual i = new Individual();
        addBasicName(i, "Bob /Dylan/");
        assertEquals("Bob /Dylan/", i.formattedName());
        addBasicName(i, "Robert Allen /Zimmerman/");
        assertEquals("Bob /Dylan/ aka Robert Allen /Zimmerman/", i.formattedName());
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
        assertTrue(gp.errors.isEmpty());
        assertTrue(gp.warnings.isEmpty());
        Gedcom g = gp.gedcom;
        assertNotNull(g);
        assertEquals("There are supposed to be 43 people in the gedcom - are you using the right file/file version?", 43, g.individuals.size());
        assertEquals("There are supposed to be 18 families in the gedcom - are you using the right file/file version?", 18, g.families.size());

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
     * {@link org.gedcom4j.model.Individual#getAttributesOfType(org.gedcom4j.model.IndividualAttributeType)} .
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
        assertTrue(gp.errors.isEmpty());
        assertTrue(gp.warnings.isEmpty());
        Gedcom g = gp.gedcom;
        assertNotNull(g);
        assertEquals("There are supposed to be 43 people in the gedcom - are you using the right file/file version?", 43, g.individuals.size());
        assertEquals("There are supposed to be 18 families in the gedcom - are you using the right file/file version?", 18, g.families.size());

        Individual alex = getPerson(g, "Zucco", "Alex");
        Set<Individual> descendants = alex.getDescendants();
        assertNotNull(descendants);
        assertEquals(0, descendants.size());
        Individual steven = getPerson(g, "Struthers", "Steven");
        descendants = steven.getDescendants();
        assertNotNull(descendants);
        assertEquals(8, descendants.size());
    }

    /**
     * Test method for {@link org.gedcom4j.model.Individual#getEventsOfType(org.gedcom4j.model.IndividualEventType)} .
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
        f.family = new Family();
        f.family.husband = i;
        i.familiesWhereSpouse.add(f);
        assertNotNull(i.getSpouses());
        assertTrue("Should still be empty because there is no wife in the family that this guy's a spouse in", i.getSpouses().isEmpty());
        f.family.wife = new Individual();
        addBasicName(f.family.wife, "Anna //");
        assertNotNull(i.getSpouses());
        assertEquals("Ok, now there's a wife, should be exactly one spouse", 1, i.getSpouses().size());

        // Add a second family and spouse
        f = new FamilySpouse();
        f.family = new Family();
        f.family.husband = i;
        i.familiesWhereSpouse.add(f);
        assertNotNull(i.getSpouses());
        assertEquals("Should still be just one spouse because there is no wife in the 2nd family that this guy's a spouse in", 1, i.getSpouses().size());

        f.family.wife = new Individual();
        addBasicName(f.family.wife, "Elizabeth /Hofstadt/");
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
        i1.address = new Address();
        assertFalse(i1.hashCode() == i2.hashCode());
        i2.address = new Address();
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
        assertEquals("Bob /Dylan/", i.formattedName());
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
        f.family = new Family();
        f.family.husband = i;
        i.familiesWhereSpouse.add(f);
        f.family.wife = new Individual();
        addBasicName(f.family.wife, "Anna //");
        // Add a second family and spouse
        f = new FamilySpouse();
        f.family = new Family();
        f.family.husband = i;
        i.familiesWhereSpouse.add(f);

        f.family.wife = new Individual();
        addBasicName(f.family.wife, "Elizabeth /Hofstadt/");
        assertEquals("Donald /Draper/, spouse of Anna //, spouse of Elizabeth /Hofstadt/", i.toString());
    }

    /**
     * Helper method to add attributes of a specific type to an individual
     * 
     * @param i
     *            the individual to add to
     * @param t
     *            the type of attribute
     */
    private void addAttributeOfType(Individual i, IndividualAttributeType t) {
        IndividualAttribute e = new IndividualAttribute();
        e.type = t;
        e.description = new StringWithCustomTags("Random text for uniqueness " + Math.random());
        i.attributes.add(e);
    }

    /**
     * Helper method to add a basic name to an individual
     * 
     * @param i
     *            the individual
     * @param string
     *            the name
     */
    private void addBasicName(Individual i, String string) {
        PersonalName pn = new PersonalName();
        pn.basic = string;
        i.names.add(pn);

    }

    /**
     * Helper method to add events of a specific type to an individual
     * 
     * @param i
     *            the individual to add to
     * @param t
     *            the type of event
     */
    private void addEventOfType(Individual i, IndividualEventType t) {
        IndividualEvent e = new IndividualEvent();
        e.type = t;
        e.description = new StringWithCustomTags("Random text for uniqueness " + Math.random());
        i.events.add(e);
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
    private Individual getPerson(Gedcom gedcom, String surname, String givenName) {
        Individual result = new Finder(gedcom).findByName(surname, givenName).get(0);
        assertNotNull("Couldn't find " + givenName + " " + surname + " by name in the gedcom", result);
        return result;
    }

}
