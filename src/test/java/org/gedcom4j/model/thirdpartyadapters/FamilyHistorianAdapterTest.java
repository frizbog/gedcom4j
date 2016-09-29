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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.CustomFact;
import org.gedcom4j.model.FamilyEvent;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualAttribute;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.model.Place;
import org.gedcom4j.model.StringWithCustomFacts;
import org.gedcom4j.model.enumerations.FamilyEventType;
import org.gedcom4j.model.enumerations.IndividualAttributeType;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.parser.GedcomParser;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link FamilyHistorianAdapter}
 * 
 * @author frizbog
 */
@SuppressWarnings("PMD.TooManyMethods")
public class FamilyHistorianAdapterTest {

    /**
     * The test gedcom we read from the sample file that HAS custom tags
     */
    private Gedcom gedcomWithCustomTags;

    /**
     * The main person in the sample file with most of the custom facts
     */
    private Individual tom;

    /**
     * The class under test
     */
    private final FamilyHistorianAdapter fha = new FamilyHistorianAdapter();

    /**
     * The test gedcom we read from the sample file that DOES NOT have custom tags, for negative testing
     */
    private Gedcom gedcomWithoutCustomTags;

    /**
     * Sets up each test
     * 
     * @throws GedcomParserException
     *             if the sample file cannot be parsed
     * @throws IOException
     *             if the sample file cannot be read
     */
    @Before
    public void setUp() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/famhistcustomtags.ged");
        gedcomWithCustomTags = gp.getGedcom();
        assertNotNull(gedcomWithCustomTags);

        gp = new GedcomParser();
        gp.load("sample/famhistnocustomtags.ged");
        gedcomWithoutCustomTags = gp.getGedcom();
        assertNotNull(gedcomWithoutCustomTags);

        assertNotSame(gedcomWithCustomTags, gedcomWithoutCustomTags);

        tom = gedcomWithCustomTags.getIndividuals().get("@I1@");
        assertNotNull(tom);
    }

    /**
     * Test for {@link FamilyHistorianAdapter#addNamedList(Gedcom, CustomFact)}
     */
    @Test
    public void testAddNamedList() {
        List<CustomFact> lists = fha.getNamedLists(gedcomWithoutCustomTags);
        assertNotNull(lists);
        assertEquals(0, lists.size());

        CustomFact namedList = fha.newNamedList("Frying Pans");
        fha.addNamedList(gedcomWithoutCustomTags, namedList);

        lists = fha.getNamedLists(gedcomWithoutCustomTags);
        assertNotNull(lists);
        assertEquals(1, lists.size());

        lists = fha.getNamedList(gedcomWithoutCustomTags, "Frying Pans");
        assertNotNull(lists);
        assertEquals(1, lists.size());
    }

    /**
     * Test for {@link FamilyHistorianAdapter#removeUnrelatedWitnesses(IndividualEvent)},
     * {@link FamilyHistorianAdapter#newUnrelatedWitness(String)}, and
     * {@link FamilyHistorianAdapter#addUnrelatedWitness(IndividualEvent, CustomFact)}
     */
    @Test
    public void testClearAddUnrelatedWitnesses() {
        IndividualEvent birth = tom.getEventsOfType(IndividualEventType.BIRTH).get(0);
        List<CustomFact> witnessNames = fha.getUnrelatedWitnesses(birth);
        assertNotNull(witnessNames);
        assertEquals(1, witnessNames.size());

        fha.removeUnrelatedWitnesses(birth);
        witnessNames = fha.getUnrelatedWitnesses(birth);
        assertNotNull(witnessNames);
        assertEquals(0, witnessNames.size());

        CustomFact uw = fha.newUnrelatedWitness("Robert Roberts");
        fha.addUnrelatedWitness(birth, uw);

        witnessNames = fha.getUnrelatedWitnesses(birth);
        assertNotNull(witnessNames);
        assertEquals("Robert Roberts", witnessNames.get(0).getDescription().getValue());
    }

    /**
     * Positive test for {@link FamilyHistorianAdapter#getNamedList(Gedcom, String)}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetNamedListImmutable() {
        List<CustomFact> lists = fha.getNamedList(gedcomWithCustomTags, "Key Individuals");
        assertNotNull(lists);
        assertEquals(1, lists.size());
        lists.clear();
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getNamedList(Gedcom, String)}
     */
    @Test
    public void testGetNamedListNegative() {
        List<CustomFact> lists = fha.getNamedList(gedcomWithoutCustomTags, "Key Individuals");
        assertNotNull(lists);
        assertEquals(0, lists.size());

        lists = fha.getNamedList(gedcomWithCustomTags, "My Favorite Frying Pans");
        assertNotNull(lists);
        assertEquals(0, lists.size());
    }

    /**
     * Positive test for {@link FamilyHistorianAdapter#getNamedList(Gedcom, String)}
     */
    @Test
    public void testGetNamedListPositive() {
        List<CustomFact> lists = fha.getNamedList(gedcomWithCustomTags, "Key Individuals");
        assertNotNull(lists);
        assertEquals(1, lists.size());
        assertEquals("Key Individuals", lists.get(0).getDescription().getValue());
    }

    /**
     * Positive test for {@link FamilyHistorianAdapter#getNamedLists(Gedcom)}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetNamedListsImmutable() {
        List<CustomFact> lists = fha.getNamedLists(gedcomWithCustomTags);
        assertNotNull(lists);
        assertEquals(1, lists.size());
        lists.clear();
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getNamedLists(Gedcom)}
     */
    @Test
    public void testGetNamedListsNegative() {
        List<CustomFact> lists = fha.getNamedLists(gedcomWithoutCustomTags);
        assertNotNull(lists);
        assertEquals(0, lists.size());
    }

    /**
     * Positive test for {@link FamilyHistorianAdapter#getNamedLists(Gedcom)}
     */
    @Test
    public void testGetNamedListsPositive() {
        List<CustomFact> lists = fha.getNamedLists(gedcomWithCustomTags);
        assertNotNull(lists);
        assertEquals(1, lists.size());
        assertEquals("Key Individuals", lists.get(0).getDescription().getValue());
    }

    /**
     * Positive test for {@link FamilyHistorianAdapter#getRootIndividual(Gedcom)}
     */
    @Test
    public void testGetRootIndividualPositive() {
        Individual individual = fha.getRootIndividual(gedcomWithCustomTags);
        assertNotNull(individual);
        assertEquals(tom, individual);
        assertSame(tom, individual);
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getNameUsed(PersonalName)} and
     * {@link FamilyHistorianAdapter#setNameUsed(PersonalName, String)}
     */
    @Test
    public void testGetSetNameUsedNegative() {
        PersonalName pn = new PersonalName();
        List<CustomFact> nameUsed = fha.getNameUsed(pn);
        assertNotNull(nameUsed);
        assertEquals(0, nameUsed.size());
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getNameUsed(PersonalName)} and
     * {@link FamilyHistorianAdapter#setNameUsed(PersonalName, String)}
     */
    @Test
    public void testGetSetNameUsedPositive() {
        PersonalName pn = tom.getNames().get(0);
        assertNotNull(pn);
        List<CustomFact> nameUsed = fha.getNameUsed(pn);
        assertNotNull(nameUsed);
        assertEquals(1, nameUsed.size());
        assertEquals("Tony", nameUsed.get(0).getDescription().getValue());

        fha.setNameUsed(pn, "Billy Jo Bob");
        nameUsed = fha.getNameUsed(pn);
        assertEquals("Billy Jo Bob", nameUsed.get(0).getDescription().getValue());
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getOtherPlaceName(IndividualEvent)} and
     * {@link FamilyHistorianAdapter#setOtherPlaceName(IndividualEvent, String)}
     */
    @Test
    public void testGetSetOtherPlaceNamePositive() {
        Place here = new Place();
        here.setPlaceName("Earth");

        IndividualEvent ie = new IndividualEvent();
        ie.setType(IndividualEventType.IMMIGRATION);
        ie.setPlace(here);

        assertNull(fha.getOtherPlaceName(ie));

        fha.setOtherPlaceName(ie, "Cete Alpha V");
        assertEquals("Cete Alpha V", fha.getOtherPlaceName(ie));

        fha.setOtherPlaceName(ie, null);
        assertNull(fha.getOtherPlaceName(ie));

    }

    /**
     * Test for {@link FamilyHistorianAdapter#getFactSetSentenceTemplate(org.gedcom4j.model.CustomFact)} and
     * {@link FamilyHistorianAdapter#setFactSetSentenceTemplate(org.gedcom4j.model.CustomFact, String)}
     */
    @Test
    public void testGetSetSentenceCustomFact() {
        CustomFact cf = new CustomFact("_ATTR");
        cf.setType(new StringWithCustomFacts("Victory"));

        assertNull(fha.getFactSetSentenceTemplate(cf));

        fha.setFactSetSentenceTemplate(cf, "The quick brown fox jumps over the lazy dog.");
        assertEquals("The quick brown fox jumps over the lazy dog.", fha.getFactSetSentenceTemplate(cf));

        fha.setFactSetSentenceTemplate(cf, null);
        assertNull(fha.getFactSetSentenceTemplate(cf));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getFactSetSentenceTemplate(org.gedcom4j.model.AbstractEvent)} and
     * {@link FamilyHistorianAdapter#setFactSetSentenceTemplate(org.gedcom4j.model.AbstractEvent, String)}
     */
    @Test
    public void testGetSetSentenceFamilyEvent() {
        FamilyEvent fe = new FamilyEvent();
        fe.setType(FamilyEventType.EVENT);

        assertNull(fha.getFactSetSentenceTemplate(fe));

        fha.setFactSetSentenceTemplate(fe, "The quick brown fox jumps over the lazy dog.");
        assertEquals("The quick brown fox jumps over the lazy dog.", fha.getFactSetSentenceTemplate(fe));

        fha.setFactSetSentenceTemplate(fe, null);
        assertNull(fha.getFactSetSentenceTemplate(fe));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getFactSetSentenceTemplate(org.gedcom4j.model.AbstractEvent)} and
     * {@link FamilyHistorianAdapter#setFactSetSentenceTemplate(org.gedcom4j.model.AbstractEvent, String)}
     */
    @Test
    public void testGetSetSentenceIndividualAttribute() {
        IndividualAttribute ia = new IndividualAttribute();
        ia.setType(IndividualAttributeType.FACT);

        assertNull(fha.getFactSetSentenceTemplate(ia));

        fha.setFactSetSentenceTemplate(ia, "The quick brown fox jumps over the lazy dog.");
        assertEquals("The quick brown fox jumps over the lazy dog.", fha.getFactSetSentenceTemplate(ia));

        fha.setFactSetSentenceTemplate(ia, null);
        assertNull(fha.getFactSetSentenceTemplate(ia));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getFactSetSentenceTemplate(org.gedcom4j.model.AbstractEvent)} and
     * {@link FamilyHistorianAdapter#setFactSetSentenceTemplate(org.gedcom4j.model.AbstractEvent, String)}
     */
    @Test
    public void testGetSetSentenceIndividualEvent() {
        IndividualEvent ie = new IndividualEvent();
        ie.setType(IndividualEventType.EVENT);

        assertNull(fha.getFactSetSentenceTemplate(ie));

        fha.setFactSetSentenceTemplate(ie, "The quick brown fox jumps over the lazy dog.");
        assertEquals("The quick brown fox jumps over the lazy dog.", fha.getFactSetSentenceTemplate(ie));

        fha.setFactSetSentenceTemplate(ie, null);
        assertNull(fha.getFactSetSentenceTemplate(ie));
    }

    /**
     * Negative test case for {@link FamilyHistorianAdapter#getUID(Gedcom)} and
     * {@link FamilyHistorianAdapter#setUID(Gedcom, String)}
     */
    @Test
    public void testGetSetUIDNegative() {
        assertNull(fha.getUID(gedcomWithoutCustomTags));
        fha.setUID(gedcomWithoutCustomTags, "FryingPan");
        assertEquals("FryingPan", fha.getUID(gedcomWithoutCustomTags));
    }

    /**
     * Positive test case for {@link FamilyHistorianAdapter#getUID(Gedcom)} and
     * {@link FamilyHistorianAdapter#setUID(Gedcom, String)}
     */
    @Test
    public void testGetSetUIDPositive() {
        assertEquals("{C2159006-9E8E-4149-87DB-36E5F6D08A37}", fha.getUID(gedcomWithCustomTags));
        fha.setUID(gedcomWithCustomTags, "FryingPan");
        assertEquals("FryingPan", fha.getUID(gedcomWithCustomTags));
    }

    /**
     * Negative test case for {@link FamilyHistorianAdapter#getVariantExportFormat(Gedcom)} and
     * {@link FamilyHistorianAdapter#setVariantExportFormat(Gedcom, String)}
     */
    @Test
    public void testGetSetVariantExportFormatNegative() {
        assertNull(fha.getVariantExportFormat(gedcomWithoutCustomTags));
        fha.setVariantExportFormat(gedcomWithoutCustomTags, "FryingPan");
        assertEquals("FryingPan", fha.getVariantExportFormat(gedcomWithoutCustomTags));
    }

    /**
     * Positive test case for {@link FamilyHistorianAdapter#getVariantExportFormat(Gedcom)} and
     * {@link FamilyHistorianAdapter#setVariantExportFormat(Gedcom, String)}
     */
    @Test
    public void testGetSetVariantExportFormatPositive() {
        assertEquals("DSR", fha.getVariantExportFormat(gedcomWithCustomTags));
        fha.setVariantExportFormat(gedcomWithCustomTags, "FryingPan");
        assertEquals("FryingPan", fha.getVariantExportFormat(gedcomWithCustomTags));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getUnrelatedWitnesses(IndividualEvent)}
     */
    @Test
    public void testGetUnrelatedWitnesses() {
        IndividualEvent birth = tom.getEventsOfType(IndividualEventType.BIRTH).get(0);
        List<CustomFact> witnessNames = fha.getUnrelatedWitnesses(birth);
        assertNotNull(witnessNames);
        assertEquals(1, witnessNames.size());
        CustomFact w1 = witnessNames.get(0);
        assertEquals("Wally Witness", w1.getDescription().getValue());
        assertEquals("Disinterested Observer", w1.getCustomFactsWithTag("ROLE").get(0).getDescription().getValue());
        assertEquals("It was sorta awkward", w1.getNotes().get(0).getLines().get(0));
    }

    /**
     * Test that the results from {@link FamilyHistorianAdapter#getUnrelatedWitnesses(IndividualEvent)} are immutable
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetWitnessNamesImmutable() {
        IndividualEvent birth = tom.getEventsOfType(IndividualEventType.BIRTH).get(0);
        List<CustomFact> witnessNames = fha.getUnrelatedWitnesses(birth);
        assertNotNull(witnessNames);
        witnessNames.clear();
    }

    /**
     * Test for {@link FamilyHistorianAdapter#isEditingEnabled(CustomFact)} and
     * {@link FamilyHistorianAdapter#setEditingEnabled(CustomFact, boolean)}
     */
    @Test
    public void testNamedListEditingEnabled() {
        CustomFact namedList = fha.getNamedList(gedcomWithCustomTags, "Key Individuals").get(0);
        assertTrue(fha.isEditingEnabled(namedList));
        fha.setEditingEnabled(namedList, false);
        assertFalse(fha.isEditingEnabled(namedList));
        fha.setEditingEnabled(namedList, true);
        assertTrue(fha.isEditingEnabled(namedList));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#removeNamedList(Gedcom, String)}
     */
    @Test
    public void testRemoveNamedList() {
        List<CustomFact> lists = fha.getNamedLists(gedcomWithCustomTags);
        assertNotNull(lists);
        assertEquals(1, lists.size());

        fha.removeNamedList(gedcomWithCustomTags, "Key Individuals");

        lists = fha.getNamedList(gedcomWithCustomTags, "Key Individuals");
        assertNotNull(lists);
        assertEquals(0, lists.size());
    }

    /**
     * Test for {@link FamilyHistorianAdapter#removeNamedLists(Gedcom)}
     */
    @Test
    public void testRemoveNamedLists() {
        List<CustomFact> lists = fha.getNamedLists(gedcomWithCustomTags);
        assertNotNull(lists);
        assertEquals(1, lists.size());

        fha.removeNamedLists(gedcomWithCustomTags);

        lists = fha.getNamedLists(gedcomWithCustomTags);
        assertNotNull(lists);
        assertEquals(0, lists.size());
    }

    /**
     * Negative test {@link FamilyHistorianAdapter#getRootIndividual(Gedcom)} and
     * {@link FamilyHistorianAdapter#setRootIndividual(Gedcom, Individual)}
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetRootIndividualNegative1() {
        Individual individual = fha.getRootIndividual(gedcomWithoutCustomTags);
        assertNull(individual);

        fha.setRootIndividual(gedcomWithoutCustomTags, tom);
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getRootIndividual(Gedcom)} and positive test for
     * {@link FamilyHistorianAdapter#setRootIndividual(Gedcom, Individual)}
     */
    @Test
    public void testSetRootIndividualNegative2() {
        Individual individual = fha.getRootIndividual(gedcomWithoutCustomTags);
        assertNull(individual);

        Individual tomsMom = gedcomWithoutCustomTags.getIndividuals().get("@I3@");
        fha.setRootIndividual(gedcomWithoutCustomTags, tomsMom);

        assertEquals(tomsMom, fha.getRootIndividual(gedcomWithoutCustomTags));
        assertSame(tomsMom, fha.getRootIndividual(gedcomWithoutCustomTags));
    }
}
