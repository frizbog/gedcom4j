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
import org.gedcom4j.model.AbstractEvent;
import org.gedcom4j.model.CustomFact;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.FamilyEvent;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.IGedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualAttribute;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.NoteStructure;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.model.Place;
import org.gedcom4j.model.Repository;
import org.gedcom4j.model.Source;
import org.gedcom4j.model.StringWithCustomFacts;
import org.gedcom4j.model.Submitter;
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
@SuppressWarnings({ "PMD.TooManyMethods", "PMD.ExcessivePublicCount", "PMD.GodClass", "PMD.ExcessiveClassLength",
        "PMD.ExcessiveImports" })
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
     * Test for DNA markers with null dna fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddDnaMarkersBadDna() {
        CustomFact bad = new CustomFact("_WRONGTAG");
        fha.addDnaMarker(new Individual(), bad);
    }

    /**
     * Test for DNA markers with bad dna fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddDnaMarkersBadDna2() {
        CustomFact bad = new CustomFact("_ATTR");
        // Type is empty
        fha.addDnaMarker(new Individual(), bad);
    }

    /**
     * Test for DNA markers with bad dna fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddDnaMarkersBadDna3() {
        CustomFact bad = new CustomFact("_ATTR");
        bad.setType("WRONG TYPE");
        fha.addDnaMarker(new Individual(), bad);
    }

    /**
     * Test for DNA markers with bad dna fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddDnaMarkersNullDna() {
        fha.addDnaMarker(new Individual(), null);
    }

    /**
     * Test for elected custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddElectedBadFact() {
        CustomFact bad = new CustomFact("_WRONGTAG");
        fha.addElected(new Individual(), bad);
    }

    /**
     * Test for elected custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddElectedBadFact2() {
        CustomFact bad = new CustomFact("_ATTR");
        // Type is empty
        fha.addElected(new Individual(), bad);
    }

    /**
     * Test for elected custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddElectedBadFact3() {
        CustomFact bad = new CustomFact("_ATTR");
        bad.setType("WRONG TYPE");
        fha.addElected(new Individual(), bad);
    }

    /**
     * Test for elected custom fact with null fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddElectedNullFact() {
        fha.addElected(new Individual(), null);
    }

    /**
     * Test for employment custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddEmploymentBadFact() {
        CustomFact bad = new CustomFact("_WRONGTAG");
        fha.addEmployment(new Individual(), bad);
    }

    /**
     * Test for employment custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddEmploymentBadFact2() {
        CustomFact bad = new CustomFact("_ATTR");
        // Type is null
        fha.addEmployment(new Individual(), bad);
    }

    /**
     * Test for employment custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddEmploymentBadFact3() {
        CustomFact bad = new CustomFact("_ATTR");
        bad.setType("WRONG TYPE");
        fha.addEmployment(new Individual(), bad);
    }

    /**
     * Test for employment custom fact with null fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddEmploymentNullFact() {
        fha.addEmployment(new Individual(), null);
    }

    /**
     * Test for height custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddHeightBadFact() {
        CustomFact bad = new CustomFact("_WRONGTAG");
        fha.addHeight(new Individual(), bad);
    }

    /**
     * Test for height custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddHeightBadFact2() {
        CustomFact bad = new CustomFact("_ATTR");
        // Null type
        fha.addHeight(new Individual(), bad);
    }

    /**
     * Test for height custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddHeightBadFact3() {
        CustomFact bad = new CustomFact("_ATTR");
        bad.setType("WRONG TYPE");
        fha.addHeight(new Individual(), bad);
    }

    /**
     * Test for height custom fact with null fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddHeightNullFact() {
        fha.addHeight(new Individual(), null);
    }

    /**
     * Test for medical condition custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMedicalConditionBadFact() {
        CustomFact bad = new CustomFact("_WRONGTAG");
        fha.addMedicalCondition(new Individual(), bad);
    }

    /**
     * Test for medical condition custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMedicalConditionBadFact2() {
        CustomFact bad = new CustomFact("_ATTR");
        // type is null
        fha.addMedicalCondition(new Individual(), bad);
    }

    /**
     * Test for medical condition custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMedicalConditionBadFact3() {
        CustomFact bad = new CustomFact("_ATTR");
        bad.setType("BAD TYPE");
        fha.addMedicalCondition(new Individual(), bad);
    }

    /**
     * Test for medical condition custom fact with null fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMedicalConditionNullFact() {
        fha.addMedicalCondition(new Individual(), null);
    }

    /**
     * Test for military ID custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMilitaryIdBadFact() {
        CustomFact bad = new CustomFact("_WRONGTAG");
        fha.addMilitaryId(new Individual(), bad);
    }

    /**
     * Test for military ID custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMilitaryIdBadFact2() {
        CustomFact bad = new CustomFact("_ATTR");
        // type is null
        fha.addMilitaryId(new Individual(), bad);
    }

    /**
     * Test for military ID custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMilitaryIdBadFact3() {
        CustomFact bad = new CustomFact("_ATTR");
        bad.setType("BAD TYPE");
        fha.addMilitaryId(new Individual(), bad);
    }

    /**
     * Test for military ID custom fact with null fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMilitaryIdNullFact() {
        fha.addMilitaryId(new Individual(), null);
    }

    /**
     * Test for military service custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMilitaryServiceBadFact() {
        CustomFact bad = new CustomFact("_WRONGTAG");
        fha.addMilitaryService(new Individual(), bad);
    }

    /**
     * Test for military service custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMilitaryServiceBadFact2() {
        CustomFact bad = new CustomFact("_ATTR");
        // type is null
        fha.addMilitaryService(new Individual(), bad);
    }

    /**
     * Test for military service custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMilitaryServiceBadFact3() {
        CustomFact bad = new CustomFact("_ATTR");
        bad.setType("BAD TYPE");
        fha.addMilitaryService(new Individual(), bad);
    }

    /**
     * Test for military service custom fact with null fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMilitaryServiceNullFact() {
        fha.addMilitaryService(new Individual(), null);
    }

    /**
     * Test for mission custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMissionBadFact() {
        CustomFact bad = new CustomFact("_WRONGTAG");
        fha.addMission(new Individual(), bad);
    }

    /**
     * Test for mission custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMissionBadFact2() {
        CustomFact bad = new CustomFact("_ATTR");
        // Null type
        fha.addMission(new Individual(), bad);
    }

    /**
     * Test for mission custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMissionBadFact3() {
        CustomFact bad = new CustomFact("_ATTR");
        bad.setType("BAD TYPE");
        fha.addMission(new Individual(), bad);
    }

    /**
     * Test for mission custom fact with null fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMissionNullFact() {
        fha.addMission(new Individual(), null);
    }

    /**
     * Test for {@link FamilyHistorianAdapter#addNamedList(IGedcom, CustomFact)}
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
     * Test for named list custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddNamedListBadFact() {
        CustomFact bad = new CustomFact("_WRONGTAG");
        fha.addNamedList(gedcomWithCustomTags, bad);
    }

    /**
     * Test for named list custom fact with null fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddNamedListNullFact() {
        fha.addNamedList(gedcomWithCustomTags, null);
    }

    /**
     * Test for namesake custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddNamesakeBadFact() {
        CustomFact bad = new CustomFact("_WRONGTAG");
        fha.addNamesake(new Individual(), bad);
    }

    /**
     * Test for namesake custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddNamesakeBadFact2() {
        CustomFact bad = new CustomFact("_ATTR");
        // type is null
        fha.addNamesake(new Individual(), bad);
    }

    /**
     * Test for namesake custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddNamesakeBadFact3() {
        CustomFact bad = new CustomFact("_ATTR");
        bad.setType("BAD TYPE");
        fha.addNamesake(new Individual(), bad);
    }

    /**
     * Test for namesake custom fact with null fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddNamesakeNullFact() {
        fha.addNamesake(new Individual(), null);
    }

    /**
     * Test for ordinance custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddOrdinanceBadFact() {
        CustomFact bad = new CustomFact("_WRONGTAG");
        fha.addOrdinance(new Individual(), bad);
    }

    /**
     * Test for ordinance custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddOrdinanceBadFact2() {
        CustomFact bad = new CustomFact("_ATTR");
        // null type
        fha.addOrdinance(new Individual(), bad);
    }

    /**
     * Test for ordinance custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddOrdinanceBadFact3() {
        CustomFact bad = new CustomFact("_ATTR");
        bad.setType("BAD TYPE");
        fha.addOrdinance(new Individual(), bad);
    }

    /**
     * Test for ordinance custom fact with null fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddOrdinanceNullFact() {
        fha.addOrdinance(new Individual(), null);
    }

    /**
     * Test for place custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddPlaceBadFact() {
        CustomFact bad = new CustomFact("_WRONGTAG");
        fha.addPlaceRecord(gedcomWithCustomTags, bad);
    }

    /**
     * Test for place custom fact with null fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddPlaceNullFact() {
        fha.addPlaceRecord(gedcomWithCustomTags, null);
    }

    /**
     * Test for unrelated witness custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddUnrelatedWitnessBadFact() {
        CustomFact bad = new CustomFact("_WRONGTAG");
        fha.addUnrelatedWitness(new IndividualEvent(), bad);
    }

    /**
     * Test for unrelated witness custom fact with null fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddUnrelatedWitnessNullFact() {
        fha.addUnrelatedWitness(new IndividualEvent(), null);
    }

    /**
     * Test for witness reference custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddWitnessReferenceBadFact() {
        CustomFact bad = new CustomFact("_WRONGTAG");
        fha.addWitnessReference(gedcomWithCustomTags, new IndividualEvent(), bad);
    }

    /**
     * Test for witness reference custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddWitnessReferenceBadFact2() {
        CustomFact bad = new CustomFact("_SHAR");
        // No description
        fha.addWitnessReference(gedcomWithCustomTags, new IndividualEvent(), bad);
    }

    /**
     * Test for witness reference custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddWitnessReferenceBadFact3() {
        CustomFact bad = new CustomFact("_SHAR");
        bad.setDescription("Bad description");
        fha.addWitnessReference(gedcomWithCustomTags, new IndividualEvent(), bad);
    }

    /**
     * Test for witness reference custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddWitnessReferenceBadFact4() {
        CustomFact bad = new CustomFact("_SHAR");
        bad.setDescription("@BAD XREF@");
        fha.addWitnessReference(gedcomWithCustomTags, new IndividualEvent(), bad);
    }

    /**
     * Test for witness reference custom fact with bad fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddWitnessReferenceBadFact5() {
        CustomFact bad = new CustomFact("_SHAR");
        bad.setDescription(""); // Empty string
        fha.addWitnessReference(gedcomWithCustomTags, new IndividualEvent(), bad);
    }

    /**
     * Test for witness reference custom fact with null fact
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddWitnessReferenceNullFact() {
        fha.addWitnessReference(gedcomWithCustomTags, new IndividualEvent(), null);
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
     * Test for {@link FamilyHistorianAdapter#removeWitnessReferences(IndividualEvent)},
     * {@link FamilyHistorianAdapter#newUnrelatedWitness(String)}, and
     * {@link FamilyHistorianAdapter#addUnrelatedWitness(IndividualEvent, CustomFact)}
     */
    @Test
    public void testClearAddWitnessReferences() {

        IndividualEvent birth = tom.getEventsOfType(IndividualEventType.BIRTH).get(0);
        List<CustomFact> witnessRefs = fha.getWitnessReferences(birth);
        assertNotNull(witnessRefs);
        assertEquals(1, witnessRefs.size());

        fha.removeWitnessReferences(birth);
        witnessRefs = fha.getWitnessReferences(birth);
        assertNotNull(witnessRefs);
        assertEquals(0, witnessRefs.size());

        Individual theresa = gedcomWithCustomTags.getIndividuals().get("@I3@");
        CustomFact wr = fha.newWitnessReference(theresa);
        fha.addWitnessReference(gedcomWithCustomTags, birth, wr);

        witnessRefs = fha.getWitnessReferences(birth);
        assertNotNull(witnessRefs);
        assertEquals("@I3@", witnessRefs.get(0).getDescription().getValue());
    }

    /**
     * Test for DNA markers
     */
    @Test
    public void testDnaMarkers() {
        List<CustomFact> dnaMarkers = fha.getDnaMarkers(tom);
        assertNotNull(dnaMarkers);
        assertEquals(1, dnaMarkers.size());
        CustomFact dnaMarker = dnaMarkers.get(0);
        assertEquals("_ATTR", dnaMarker.getTag());
        assertEquals("xxx", dnaMarker.getDescription().getValue());
        assertEquals("DNA Markers", dnaMarker.getType().getValue());

        fha.removeDnaMarkers(tom);
        dnaMarkers = fha.getDnaMarkers(tom);
        assertNotNull(dnaMarkers);
        assertEquals(0, dnaMarkers.size());

        CustomFact n = fha.newDnaMarker("frying pan");
        fha.addDnaMarker(tom, n);

        dnaMarkers = fha.getDnaMarkers(tom);
        assertNotNull(dnaMarkers);
        assertEquals(1, dnaMarkers.size());
        dnaMarker = dnaMarkers.get(0);
        assertEquals("_ATTR", dnaMarker.getTag());
        assertEquals("frying pan", dnaMarker.getDescription().getValue());
        assertEquals("DNA Markers", dnaMarker.getType().getValue());
    }

    /**
     * Test for Elected
     */
    @Test
    public void testElected() {
        List<CustomFact> electeds = fha.getElected(tom);
        assertNotNull(electeds);
        assertEquals(1, electeds.size());
        CustomFact elected = electeds.get(0);
        assertEquals("_ATTR", elected.getTag());
        assertEquals("grand poobah", elected.getDescription().getValue());
        assertEquals("Elected", elected.getType().getValue());

        fha.removeElected(tom);
        electeds = fha.getElected(tom);
        assertNotNull(electeds);
        assertEquals(0, electeds.size());

        CustomFact n = fha.newElected("frying pan");
        fha.addElected(tom, n);

        electeds = fha.getElected(tom);
        assertNotNull(electeds);
        assertEquals(1, electeds.size());
        elected = electeds.get(0);
        assertEquals("_ATTR", elected.getTag());
        assertEquals("frying pan", elected.getDescription().getValue());
        assertEquals("Elected", elected.getType().getValue());
    }

    /**
     * Test for Employment
     */
    @Test
    public void testEmployment() {
        List<CustomFact> employments = fha.getEmployment(tom);
        assertNotNull(employments);
        assertEquals(1, employments.size());
        CustomFact employment = employments.get(0);
        assertEquals("_ATTR", employment.getTag());
        assertEquals("waiter", employment.getDescription().getValue());
        assertEquals("Employment", employment.getType().getValue());

        fha.removeEmployment(tom);
        employments = fha.getEmployment(tom);
        assertNotNull(employments);
        assertEquals(0, employments.size());

        CustomFact n = fha.newEmployment("frying pan");
        fha.addEmployment(tom, n);

        employments = fha.getEmployment(tom);
        assertNotNull(employments);
        assertEquals(1, employments.size());
        employment = employments.get(0);
        assertEquals("_ATTR", employment.getTag());
        assertEquals("frying pan", employment.getDescription().getValue());
        assertEquals("Employment", employment.getType().getValue());
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getFamilyStatus(Family)} and
     * {@link FamilyHistorianAdapter#setFamilyStatus(Family, String)}
     */
    @Test
    public void testFamilyStatusNegative() {
        Family family = gedcomWithoutCustomTags.getFamilies().get("@F1@");
        assertNull(fha.getFamilyStatus(family));
        fha.setFamilyStatus(family, "Common Law");
        assertEquals("Common Law", fha.getFamilyStatus(family));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getFamilyStatus(Family)} and
     * {@link FamilyHistorianAdapter#setFamilyStatus(Family, String)}
     */
    @Test
    public void testFamilyStatusNegative2() {
        Family family = gedcomWithoutCustomTags.getFamilies().get("@F1@");
        family.getCustomFacts(true).add(new CustomFact("_STAT")); // Force a bad family status
        assertNull(fha.getFamilyStatus(family));
        fha.setFamilyStatus(family, "Common Law");
        assertEquals("Common Law", fha.getFamilyStatus(family));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getFamilyStatus(Family)} and
     * {@link FamilyHistorianAdapter#setFamilyStatus(Family, String)}
     */
    @Test
    public void testFamilyStatusPositive() {
        Family family = gedcomWithCustomTags.getFamilies().get("@F1@");
        assertEquals("Never Married", fha.getFamilyStatus(family));
        fha.setFamilyStatus(family, null);
        assertNull(fha.getFamilyStatus(family));
        fha.setFamilyStatus(family, "Common Law");
        assertEquals("Common Law", fha.getFamilyStatus(family));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#addEmail(org.gedcom4j.model.AbstractEvent, String)},
     * {@link FamilyHistorianAdapter#getEmails(org.gedcom4j.model.AbstractEvent)}, and
     * {@link FamilyHistorianAdapter#removeEmails(org.gedcom4j.model.AbstractEvent)}
     */
    @Test
    public void testGetAddRemoveEmailsToAttribute() {
        IndividualAttribute ia = tom.getAttributesOfType(IndividualAttributeType.FACT).get(0);
        assertEquals("Tomato", ia.getSubType().getValue());
        List<String> emails = fha.getEmails(ia);
        assertNotNull(emails);
        assertEquals(1, emails.size());
        assertEquals("info@internet.com", emails.get(0));

        fha.removeEmails(ia);
        emails = fha.getEmails(ia);
        assertNotNull(emails);
        assertEquals(0, emails.size());

        assertNotNull(fha.addEmail(ia, "matt@gedcom4j.org"));

        emails = fha.getEmails(ia);
        assertNotNull(emails);
        assertEquals(1, emails.size());
        assertEquals("matt@gedcom4j.org", emails.get(0));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#addEmail(org.gedcom4j.model.AbstractEvent, String)},
     * {@link FamilyHistorianAdapter#getEmails(org.gedcom4j.model.AbstractEvent)}, and
     * {@link FamilyHistorianAdapter#removeEmails(org.gedcom4j.model.AbstractEvent)}
     */
    @Test
    public void testGetAddRemoveEmailsToCustomFact() {
        List<CustomFact> dnaMarkers = fha.getDnaMarkers(tom);
        assertNotNull(dnaMarkers);
        assertEquals(1, dnaMarkers.size());

        CustomFact dnaMarker = dnaMarkers.get(0);

        List<String> emails = fha.getEmails(dnaMarker);
        assertNotNull(emails);
        assertEquals(1, emails.size());
        assertEquals("info@internet.com", emails.get(0));

        fha.removeEmails(dnaMarker);
        emails = fha.getEmails(dnaMarker);
        assertNotNull(emails);
        assertEquals(0, emails.size());

        assertNotNull(fha.addEmail(dnaMarker, "matt@gedcom4j.org"));

        emails = fha.getEmails(dnaMarker);
        assertNotNull(emails);
        assertEquals(1, emails.size());
        assertEquals("matt@gedcom4j.org", emails.get(0));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#addEmail(org.gedcom4j.model.AbstractEvent, String)},
     * {@link FamilyHistorianAdapter#getEmails(org.gedcom4j.model.AbstractEvent)}, and
     * {@link FamilyHistorianAdapter#removeEmails(org.gedcom4j.model.AbstractEvent)}
     */
    @Test
    public void testGetAddRemoveEmailsToEvent() {
        IndividualEvent birth = tom.getEventsOfType(IndividualEventType.BIRTH).get(0);
        List<String> emails = fha.getEmails(birth);
        assertNotNull(emails);
        assertEquals(1, emails.size());
        assertEquals("info@internet.com", emails.get(0));

        fha.removeEmails(birth);
        emails = fha.getEmails(birth);
        assertNotNull(emails);
        assertEquals(0, emails.size());

        assertNotNull(fha.addEmail(birth, "matt@gedcom4j.org"));

        emails = fha.getEmails(birth);
        assertNotNull(emails);
        assertEquals(1, emails.size());
        assertEquals("matt@gedcom4j.org", emails.get(0));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#addEmail(Repository, String)}, {@link FamilyHistorianAdapter#getEmails(Repository)},
     * and {@link FamilyHistorianAdapter#removeEmails(Repository)}
     */
    @Test
    public void testGetAddRemoveEmailsToRepository() {

        Repository repository = gedcomWithCustomTags.getRepositories().get("@R0000@");

        List<String> emails = fha.getEmails(repository);
        assertNotNull(emails);
        assertEquals(1, emails.size());
        assertEquals("info@internet.com", emails.get(0));

        fha.removeEmails(repository);
        emails = fha.getEmails(repository);
        assertNotNull(emails);
        assertEquals(0, emails.size());

        assertNotNull(fha.addEmail(repository, "matt@gedcom4j.org"));

        emails = fha.getEmails(repository);
        assertNotNull(emails);
        assertEquals(1, emails.size());
        assertEquals("matt@gedcom4j.org", emails.get(0));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#addEmail(Submitter, String)}, {@link FamilyHistorianAdapter#getEmails(Submitter)}, and
     * {@link FamilyHistorianAdapter#removeEmails(Submitter)}
     */
    @Test
    public void testGetAddRemoveEmailsToSubmitter() {

        Submitter submitter = gedcomWithCustomTags.getSubmitters().get("@SUBM001@");

        List<String> emails = fha.getEmails(submitter);
        assertNotNull(emails);
        assertEquals(1, emails.size());
        assertEquals("info@internet.com", emails.get(0));

        fha.removeEmails(submitter);
        emails = fha.getEmails(submitter);
        assertNotNull(emails);
        assertEquals(0, emails.size());

        assertNotNull(fha.addEmail(submitter, "matt@gedcom4j.org"));

        emails = fha.getEmails(submitter);
        assertNotNull(emails);
        assertEquals(1, emails.size());
        assertEquals("matt@gedcom4j.org", emails.get(0));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#addWebUrl(org.gedcom4j.model.AbstractEvent, String)},
     * {@link FamilyHistorianAdapter#getWebUrls(org.gedcom4j.model.AbstractEvent)}, and
     * {@link FamilyHistorianAdapter#removeWebUrls(org.gedcom4j.model.AbstractEvent)}
     */
    @Test
    public void testGetAddRemoveWebUrlsToAttribute() {
        IndividualAttribute ia = tom.getAttributesOfType(IndividualAttributeType.FACT).get(0);
        assertEquals("Tomato", ia.getSubType().getValue());
        List<String> faxes = fha.getWebUrls(ia);
        assertNotNull(faxes);
        assertEquals(1, faxes.size());
        assertEquals("http://www.internet.com", faxes.get(0));

        fha.removeWebUrls(ia);
        faxes = fha.getWebUrls(ia);
        assertNotNull(faxes);
        assertEquals(0, faxes.size());

        assertNotNull(fha.addWebUrl(ia, "http://gedcom4j.org"));

        faxes = fha.getWebUrls(ia);
        assertNotNull(faxes);
        assertEquals(1, faxes.size());
        assertEquals("http://gedcom4j.org", faxes.get(0));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#addWebUrl(org.gedcom4j.model.AbstractEvent, String)},
     * {@link FamilyHistorianAdapter#getWebUrls(org.gedcom4j.model.AbstractEvent)}, and
     * {@link FamilyHistorianAdapter#removeWebUrls(org.gedcom4j.model.AbstractEvent)}
     */
    @Test
    public void testGetAddRemoveWebUrlsToCustomFact() {
        List<CustomFact> dnaMarkers = fha.getDnaMarkers(tom);
        assertNotNull(dnaMarkers);
        assertEquals(1, dnaMarkers.size());

        CustomFact dnaMarker = dnaMarkers.get(0);

        List<String> faxes = fha.getWebUrls(dnaMarker);
        assertNotNull(faxes);
        assertEquals(1, faxes.size());
        assertEquals("http://www.internet.com", faxes.get(0));

        fha.removeWebUrls(dnaMarker);
        faxes = fha.getWebUrls(dnaMarker);
        assertNotNull(faxes);
        assertEquals(0, faxes.size());

        assertNotNull(fha.addWebUrl(dnaMarker, "http://gedcom4j.org"));

        faxes = fha.getWebUrls(dnaMarker);
        assertNotNull(faxes);
        assertEquals(1, faxes.size());
        assertEquals("http://gedcom4j.org", faxes.get(0));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#addWebUrl(org.gedcom4j.model.AbstractEvent, String)},
     * {@link FamilyHistorianAdapter#getWebUrls(org.gedcom4j.model.AbstractEvent)}, and
     * {@link FamilyHistorianAdapter#removeWebUrls(org.gedcom4j.model.AbstractEvent)}
     */
    @Test
    public void testGetAddRemoveWebUrlsToEvent() {
        IndividualEvent birth = tom.getEventsOfType(IndividualEventType.BIRTH).get(0);
        List<String> faxes = fha.getWebUrls(birth);
        assertNotNull(faxes);
        assertEquals(1, faxes.size());
        assertEquals("http://www.internet.com", faxes.get(0));

        fha.removeWebUrls(birth);
        faxes = fha.getWebUrls(birth);
        assertNotNull(faxes);
        assertEquals(0, faxes.size());

        assertNotNull(fha.addWebUrl(birth, "http://gedcom4j.org"));

        faxes = fha.getWebUrls(birth);
        assertNotNull(faxes);
        assertEquals(1, faxes.size());
        assertEquals("http://gedcom4j.org", faxes.get(0));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#addWebUrl(Repository, String)}, {@link FamilyHistorianAdapter#getWebUrls(Repository)},
     * and {@link FamilyHistorianAdapter#removeWebUrls(Repository)}
     */
    @Test
    public void testGetAddRemoveWebUrlsToRepository() {

        Repository repository = gedcomWithCustomTags.getRepositories().get("@R0000@");

        List<String> faxes = fha.getWebUrls(repository);
        assertNotNull(faxes);
        assertEquals(1, faxes.size());
        assertEquals("http://www.internet.com", faxes.get(0));

        fha.removeWebUrls(repository);
        faxes = fha.getWebUrls(repository);
        assertNotNull(faxes);
        assertEquals(0, faxes.size());

        assertNotNull(fha.addWebUrl(repository, "http://gedcom4j.org"));

        faxes = fha.getWebUrls(repository);
        assertNotNull(faxes);
        assertEquals(1, faxes.size());
        assertEquals("http://gedcom4j.org", faxes.get(0));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#addWebUrl(Submitter, String)}, {@link FamilyHistorianAdapter#getWebUrls(Submitter)},
     * and {@link FamilyHistorianAdapter#removeWebUrls(Submitter)}
     */
    @Test
    public void testGetAddRemoveWebUrlsToSubmitter() {

        Submitter submitter = gedcomWithCustomTags.getSubmitters().get("@SUBM001@");

        List<String> faxes = fha.getWebUrls(submitter);
        assertNotNull(faxes);
        assertEquals(1, faxes.size());
        assertEquals("http://www.internet.com", faxes.get(0));

        fha.removeWebUrls(submitter);
        faxes = fha.getWebUrls(submitter);
        assertNotNull(faxes);
        assertEquals(0, faxes.size());

        assertNotNull(fha.addWebUrl(submitter, "http://gedcom4j.org"));

        faxes = fha.getWebUrls(submitter);
        assertNotNull(faxes);
        assertEquals(1, faxes.size());
        assertEquals("http://gedcom4j.org", faxes.get(0));
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getEmails(AbstractEvent)}
     */
    @Test
    public void testGetEmailsAbstractEventBad() {
        AbstractEvent ae = new IndividualEvent();
        ae.getCustomFacts(true).add(new CustomFact("_EMAIL"));
        List<String> emails = fha.getEmails(ae);
        assertNotNull(emails);
        assertTrue("The email custom fact should not be there because there's no description", emails.isEmpty());
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getEmails(CustomFact)}
     */
    @Test
    public void testGetEmailsCustomFactBad() {
        CustomFact customFact = new CustomFact("_SOMETHING");
        customFact.getCustomFacts(true).add(new CustomFact("_EMAIL"));
        List<String> emails = fha.getEmails(customFact);
        assertNotNull(emails);
        assertTrue("The email custom fact should not be there because there's no description", emails.isEmpty());
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getEmails(Repository)}
     */
    @Test
    public void testGetEmailsRepositoryBad() {
        Repository rep = new Repository();
        rep.getCustomFacts(true).add(new CustomFact("_EMAIL"));
        List<String> emails = fha.getEmails(rep);
        assertNotNull(emails);
        assertTrue("The email custom fact should not be there because there's no description", emails.isEmpty());
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getEmails(Submitter)}
     */
    @Test
    public void testGetEmailsSubmitterBad() {
        Submitter rep = new Submitter();
        rep.getCustomFacts(true).add(new CustomFact("_EMAIL"));
        List<String> emails = fha.getEmails(rep);
        assertNotNull(emails);
        assertTrue("The email custom fact should not be there because there's no description", emails.isEmpty());
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getFactSetSentenceTemplate(AbstractEvent)} and
     * {@link FamilyHistorianAdapter#getFactSetSentenceTemplate(CustomFact)}
     */
    @Test
    public void testGetFactSentenceTemplateNegative1() {
        assertNull(fha.getFactSetSentenceTemplate(new IndividualEvent()));
        assertNull(fha.getFactSetSentenceTemplate(new CustomFact("_FOO")));
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getFactSetSentenceTemplate(AbstractEvent)} and
     * {@link FamilyHistorianAdapter#getFactSetSentenceTemplate(CustomFact)}
     */
    @Test
    public void testGetFactSentenceTemplateNegative2() {
        IndividualEvent e = new IndividualEvent();
        e.getCustomFacts(true).add(new CustomFact("_SENT"));
        assertNull("Should be null because of no description", fha.getFactSetSentenceTemplate(e));

        CustomFact cf = new CustomFact("_FOO");
        e.getCustomFacts(true).add(new CustomFact("_SENT"));
        assertNull(fha.getFactSetSentenceTemplate(cf));
        assertNull("Should be null because of no description", fha.getFactSetSentenceTemplate(cf));
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getFlags(Individual)}
     */
    @Test
    public void testGetFlagsEmpty() {
        Individual i = new Individual();
        assertNull(fha.getFlags(i));
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getMultimediaDate(Multimedia)},
     * {@link FamilyHistorianAdapter#getMultimediaFile(Multimedia)}, {@link FamilyHistorianAdapter#getMultimediaKeys(Multimedia)},
     * and {@link FamilyHistorianAdapter#getMultimediaNote(Multimedia)}
     */
    @Test
    public void testGetMultimediaXNegative() {
        Multimedia m = new Multimedia();
        m.getCustomFacts(true).add(new CustomFact("_DATE"));
        m.getCustomFacts(true).add(new CustomFact("_FILE"));
        m.getCustomFacts(true).add(new CustomFact("_NOTE"));
        m.getCustomFacts(true).add(new CustomFact("_KEYS"));
        assertNull(fha.getMultimediaDate(m));
        assertNull(fha.getMultimediaFile(m));
        assertNull(fha.getMultimediaNote(m));
        assertNull(fha.getMultimediaKeys(m));
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getNamedList(IGedcom, String)}
     */
    @Test
    public void testGetNamedListBad() {
        gedcomWithoutCustomTags.getCustomFacts(true).add(new CustomFact("_LIST"));
        List<CustomFact> l = fha.getNamedList(gedcomWithoutCustomTags, "NOT EXIST");
        assertNotNull(l);
        assertTrue(l.isEmpty());
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getNamedList(IGedcom, String)}
     */
    @Test
    public void testGetNamedListBad2() {
        CustomFact list = new CustomFact("_LIST");
        list.setDescription("");
        gedcomWithoutCustomTags.getCustomFacts(true).add(list);
        List<CustomFact> l = fha.getNamedList(gedcomWithoutCustomTags, "FOO");
        assertNotNull(l);
        assertTrue(l.isEmpty());
    }

    /**
     * Positive test for {@link FamilyHistorianAdapter#getNamedList(IGedcom, String)}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetNamedListImmutable() {
        List<CustomFact> lists = fha.getNamedList(gedcomWithCustomTags, "Key Individuals");
        assertNotNull(lists);
        assertEquals(1, lists.size());
        lists.clear();
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getNamedList(IGedcom, String)}
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
     * Negative test for {@link FamilyHistorianAdapter#getNamedList(IGedcom, String)}
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetNamedListNegativeNullListName() {
        fha.getNamedList(gedcomWithoutCustomTags, null);
    }

    /**
     * Positive test for {@link FamilyHistorianAdapter#getNamedList(IGedcom, String)}
     */
    @Test
    public void testGetNamedListPositive() {
        List<CustomFact> lists = fha.getNamedList(gedcomWithCustomTags, "Key Individuals");
        assertNotNull(lists);
        assertEquals(1, lists.size());
        assertEquals("Key Individuals", lists.get(0).getDescription().getValue());
    }

    /**
     * Positive test for {@link FamilyHistorianAdapter#getNamedLists(IGedcom)}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetNamedListsImmutable() {
        List<CustomFact> lists = fha.getNamedLists(gedcomWithCustomTags);
        assertNotNull(lists);
        assertEquals(1, lists.size());
        lists.clear();
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getNamedLists(IGedcom)}
     */
    @Test
    public void testGetNamedListsNegative() {
        List<CustomFact> lists = fha.getNamedLists(gedcomWithoutCustomTags);
        assertNotNull(lists);
        assertEquals(0, lists.size());
    }

    /**
     * Positive test for {@link FamilyHistorianAdapter#getNamedLists(IGedcom)}
     */
    @Test
    public void testGetNamedListsPositive() {
        List<CustomFact> lists = fha.getNamedLists(gedcomWithCustomTags);
        assertNotNull(lists);
        assertEquals(1, lists.size());
        assertEquals("Key Individuals", lists.get(0).getDescription().getValue());
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getMultimediaDate(Multimedia)},
     * {@link FamilyHistorianAdapter#getMultimediaFile(Multimedia)}, {@link FamilyHistorianAdapter#getMultimediaKeys(Multimedia)},
     * and {@link FamilyHistorianAdapter#getMultimediaNote(Multimedia)}
     */
    @Test
    public void testGetNoteStructureXNegative() {
        NoteStructure n = new NoteStructure();
        n.getCustomFacts(true).add(new CustomFact("_AREA"));
        n.getCustomFacts(true).add(new CustomFact("_ASID"));
        n.getCustomFacts(true).add(new CustomFact("_CAPT"));
        n.getCustomFacts(true).add(new CustomFact("_EXCL"));
        assertNull(fha.getMultimediaNoteArea(n));
        assertNull(fha.getMultimediaNoteASID(n));
        assertNull(fha.getMultimediaNoteCaption(n));
        assertNull(fha.getMultimediaNoteExclusion(n));
    }

    /**
     * Positive test for {@link FamilyHistorianAdapter#getRootIndividual(IGedcom)}
     */
    @Test
    public void testGetRootIndividualPositive() {
        Individual individual = fha.getRootIndividual(gedcomWithCustomTags);
        assertNotNull(individual);
        assertEquals(tom, individual);
        assertSame(tom, individual);
    }

    /**
     * Test {@link FamilyHistorianAdapter#getFlags(Individual)} and {@link FamilyHistorianAdapter#setFlags(Individual, CustomFact)}
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetSetFlagsNegative() {
        CustomFact flags = fha.getFlags(tom);
        assertNotNull(flags);
        assertEquals(2, flags.getCustomFacts().size());

        CustomFact newFlags = fha.newNamedList("This is intentionally wrong!");
        assertNotNull(newFlags);
        fha.setFlags(tom, newFlags);
    }

    /**
     * Test {@link FamilyHistorianAdapter#getFlags(Individual)} and {@link FamilyHistorianAdapter#setFlags(Individual, CustomFact)}
     */
    @Test
    public void testGetSetFlagsPositive() {
        CustomFact flags = fha.getFlags(tom);
        assertNotNull(flags);
        assertEquals(2, flags.getCustomFacts().size());

        CustomFact newFlags = fha.newFlags();
        assertNotNull(newFlags);
        fha.setFlags(tom, newFlags);

        flags = fha.getFlags(tom);
        assertNotNull(flags);
        assertNull(flags.getCustomFacts());
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
    @Test(expected = IllegalArgumentException.class)
    public void testGetSetOtherPlaceNameNegative() {
        assertNull(fha.getOtherPlaceName(null));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getOtherPlaceName(IndividualEvent)}
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetSetOtherPlaceNameNegative2() {
        assertNull(fha.getOtherPlaceName(new IndividualEvent()));
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
     * Test for {@link FamilyHistorianAdapter#getOtherPlaceName(IndividualEvent)} and
     * {@link FamilyHistorianAdapter#setOtherPlaceName(IndividualEvent, String)}
     */
    @Test
    public void testGetSetOtherPlaceNamePositive2() {
        Place here = new Place();
        here.setPlaceName("Earth");

        IndividualEvent ie = new IndividualEvent();
        ie.setType(IndividualEventType.EMIGRATION);
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

        cf.getCustomFacts(true).add(new CustomFact("_UNRELATED"));

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

        fe.getCustomFacts(true).add(new CustomFact("_UNRELATED"));

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

        ia.getCustomFacts(true).add(new CustomFact("_UNRELATED"));

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

        ie.getCustomFacts(true).add(new CustomFact("_UNRELATED"));

        assertNull(fha.getFactSetSentenceTemplate(ie));

        fha.setFactSetSentenceTemplate(ie, "The quick brown fox jumps over the lazy dog.");
        assertEquals("The quick brown fox jumps over the lazy dog.", fha.getFactSetSentenceTemplate(ie));

        fha.setFactSetSentenceTemplate(ie, null);
        assertNull(fha.getFactSetSentenceTemplate(ie));
    }

    /**
     * Negative test case for {@link FamilyHistorianAdapter#getUID(IGedcom)} and
     * {@link FamilyHistorianAdapter#setUID(IGedcom, String)}
     */
    @Test
    public void testGetSetUIDNegative() {
        assertNull(fha.getUID(gedcomWithoutCustomTags));
        fha.setUID(gedcomWithoutCustomTags, "FryingPan");
        assertEquals("FryingPan", fha.getUID(gedcomWithoutCustomTags));
    }

    /**
     * Positive test case for {@link FamilyHistorianAdapter#getUID(IGedcom)} and
     * {@link FamilyHistorianAdapter#setUID(IGedcom, String)}
     */
    @Test
    public void testGetSetUIDPositive() {
        assertEquals("{C2159006-9E8E-4149-87DB-36E5F6D08A37}", fha.getUID(gedcomWithCustomTags));
        fha.setUID(gedcomWithCustomTags, "FryingPan");
        assertEquals("FryingPan", fha.getUID(gedcomWithCustomTags));
    }

    /**
     * Negative test case for {@link FamilyHistorianAdapter#getVariantExportFormat(IGedcom)} and
     * {@link FamilyHistorianAdapter#setVariantExportFormat(IGedcom, String)}
     */
    @Test
    public void testGetSetVariantExportFormatNegative() {
        assertNull(fha.getVariantExportFormat(gedcomWithoutCustomTags));
        fha.setVariantExportFormat(gedcomWithoutCustomTags, "FryingPan");
        assertEquals("FryingPan", fha.getVariantExportFormat(gedcomWithoutCustomTags));
    }

    /**
     * Negative test case for {@link FamilyHistorianAdapter#getVariantExportFormat(IGedcom)} and
     * {@link FamilyHistorianAdapter#setVariantExportFormat(IGedcom, String)}
     */
    @Test
    public void testGetSetVariantExportFormatNegative2() {
        gedcomWithoutCustomTags.getHeader().setGedcomVersion(null);
        assertNull(fha.getVariantExportFormat(gedcomWithoutCustomTags));
        fha.setVariantExportFormat(gedcomWithoutCustomTags, "FryingPan");
        assertEquals("FryingPan", fha.getVariantExportFormat(gedcomWithoutCustomTags));
    }

    /**
     * Positive test case for {@link FamilyHistorianAdapter#getVariantExportFormat(IGedcom)} and
     * {@link FamilyHistorianAdapter#setVariantExportFormat(IGedcom, String)}
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
        assertEquals("It was sorta awkward", w1.getNoteStructures().get(0).getLines().get(0));
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getWebUrls(AbstractEvent)}
     */
    @Test
    public void testGetWebUrlsAbstractEventBad() {
        AbstractEvent ae = new IndividualEvent();
        ae.getCustomFacts(true).add(new CustomFact("_WEB"));
        List<String> urls = fha.getWebUrls(ae);
        assertNotNull(urls);
        assertTrue("The web url custom fact should not be there because there's no description", urls.isEmpty());
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getWebUrls(CustomFact)}
     */
    @Test
    public void testGetWebUrlsCustomFactBad() {
        CustomFact customFact = new CustomFact("_SOMETHING");
        customFact.getCustomFacts(true).add(new CustomFact("_WEB"));
        List<String> urls = fha.getWebUrls(customFact);
        assertNotNull(urls);
        assertTrue("The web url custom fact should not be there because there's no description", urls.isEmpty());
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getWebUrls(Repository)}
     */
    @Test
    public void testGetWebUrlsRepositoryBad() {
        Repository rep = new Repository();
        rep.getCustomFacts(true).add(new CustomFact("_WEB"));
        List<String> urls = fha.getWebUrls(rep);
        assertNotNull(urls);
        assertTrue("The web url custom fact should not be there because there's no description", urls.isEmpty());
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getWebUrls(Submitter)}
     */
    @Test
    public void testGetWebUrlsSubmitterBad() {
        Submitter rep = new Submitter();
        rep.getCustomFacts(true).add(new CustomFact("_WEB"));
        List<String> urls = fha.getWebUrls(rep);
        assertNotNull(urls);
        assertTrue("The web url custom fact should not be there because there's no description", urls.isEmpty());
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
     * Test for {@link FamilyHistorianAdapter#getWitnessReferences(IndividualEvent)}
     */
    @Test
    public void testGetWitnessReferences() {
        IndividualEvent birth = tom.getEventsOfType(IndividualEventType.BIRTH).get(0);
        List<CustomFact> witnessRefs = fha.getWitnessReferences(birth);
        assertNotNull(witnessRefs);
        assertEquals(1, witnessRefs.size());
        CustomFact w1 = witnessRefs.get(0);
        assertEquals("@I3@", w1.getDescription().getValue());
        assertEquals("Birthing Coach", w1.getCustomFactsWithTag("ROLE").get(0).getDescription().getValue());
        assertEquals("Passed out during delivery", w1.getNoteStructures().get(0).getLines().get(0));
    }

    /**
     * Test for Height
     */
    @Test
    public void testHeight() {
        List<CustomFact> heights = fha.getHeight(tom);
        assertNotNull(heights);
        assertEquals(1, heights.size());
        CustomFact height = heights.get(0);
        assertEquals("_ATTR", height.getTag());
        assertEquals("5' 1\"", height.getDescription().getValue());
        assertEquals("Height", height.getType().getValue());

        fha.removeHeight(tom);
        heights = fha.getHeight(tom);
        assertNotNull(heights);
        assertEquals(0, heights.size());

        CustomFact n = fha.newHeight("frying pan");
        fha.addHeight(tom, n);

        heights = fha.getHeight(tom);
        assertNotNull(heights);
        assertEquals(1, heights.size());
        height = heights.get(0);
        assertEquals("_ATTR", height.getTag());
        assertEquals("frying pan", height.getDescription().getValue());
        assertEquals("Height", height.getType().getValue());
    }

    /**
     * Test for Medical Condition
     */
    @Test
    public void testMedicalCondition() {
        List<CustomFact> medicalConditions = fha.getMedicalCondition(tom);
        assertNotNull(medicalConditions);
        assertEquals(1, medicalConditions.size());
        CustomFact medicalCondition = medicalConditions.get(0);
        assertEquals("_ATTR", medicalCondition.getTag());
        assertEquals("Caries", medicalCondition.getDescription().getValue());
        assertEquals("Medical Condition", medicalCondition.getType().getValue());

        fha.removeMedicalCondition(tom);
        medicalConditions = fha.getMedicalCondition(tom);
        assertNotNull(medicalConditions);
        assertEquals(0, medicalConditions.size());

        CustomFact n = fha.newMedicalCondition("frying pan");
        fha.addMedicalCondition(tom, n);

        medicalConditions = fha.getMedicalCondition(tom);
        assertNotNull(medicalConditions);
        assertEquals(1, medicalConditions.size());
        medicalCondition = medicalConditions.get(0);
        assertEquals("_ATTR", medicalCondition.getTag());
        assertEquals("frying pan", medicalCondition.getDescription().getValue());
        assertEquals("Medical Condition", medicalCondition.getType().getValue());
    }

    /**
     * Test for Military ID
     */
    @Test
    public void testMilitaryId() {
        List<CustomFact> militaryIds = fha.getMilitaryId(tom);
        assertNotNull(militaryIds);
        assertEquals(1, militaryIds.size());
        CustomFact militaryId = militaryIds.get(0);
        assertEquals("_ATTR", militaryId.getTag());
        assertEquals("99999", militaryId.getDescription().getValue());
        assertEquals("Military ID", militaryId.getType().getValue());

        fha.removeMilitaryId(tom);
        militaryIds = fha.getMilitaryId(tom);
        assertNotNull(militaryIds);
        assertEquals(0, militaryIds.size());

        CustomFact n = fha.newMilitaryId("frying pan");
        fha.addMilitaryId(tom, n);

        militaryIds = fha.getMilitaryId(tom);
        assertNotNull(militaryIds);
        assertEquals(1, militaryIds.size());
        militaryId = militaryIds.get(0);
        assertEquals("_ATTR", militaryId.getTag());
        assertEquals("frying pan", militaryId.getDescription().getValue());
        assertEquals("Military ID", militaryId.getType().getValue());
    }

    /**
     * Test for Military Service
     */
    @Test
    public void testMilitaryService() {
        List<CustomFact> militaryServices = fha.getMilitaryService(tom);
        assertNotNull(militaryServices);
        assertEquals(1, militaryServices.size());
        CustomFact militaryService = militaryServices.get(0);
        assertEquals("_ATTR", militaryService.getTag());
        assertEquals("Coast Guard", militaryService.getDescription().getValue());
        assertEquals("Military Service", militaryService.getType().getValue());

        fha.removeMilitaryService(tom);
        militaryServices = fha.getMilitaryService(tom);
        assertNotNull(militaryServices);
        assertEquals(0, militaryServices.size());

        CustomFact n = fha.newMilitaryService("frying pan");
        fha.addMilitaryService(tom, n);

        militaryServices = fha.getMilitaryService(tom);
        assertNotNull(militaryServices);
        assertEquals(1, militaryServices.size());
        militaryService = militaryServices.get(0);
        assertEquals("_ATTR", militaryService.getTag());
        assertEquals("frying pan", militaryService.getDescription().getValue());
        assertEquals("Military Service", militaryService.getType().getValue());
    }

    /**
     * Test for Mission
     */
    @Test
    public void testMission() {
        List<CustomFact> missions = fha.getMission(tom);
        assertNotNull(missions);
        assertEquals(1, missions.size());
        CustomFact mission = missions.get(0);
        assertEquals("_ATTR", mission.getTag());
        assertEquals("QWERTY", mission.getDescription().getValue());
        assertEquals("Mission (LDS)", mission.getType().getValue());

        fha.removeMission(tom);
        missions = fha.getMission(tom);
        assertNotNull(missions);
        assertEquals(0, missions.size());

        CustomFact n = fha.newMission("frying pan");
        fha.addMission(tom, n);

        missions = fha.getMission(tom);
        assertNotNull(missions);
        assertEquals(1, missions.size());
        mission = missions.get(0);
        assertEquals("_ATTR", mission.getTag());
        assertEquals("frying pan", mission.getDescription().getValue());
        assertEquals("Mission (LDS)", mission.getType().getValue());
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getMultimediaDate(Multimedia)} and
     * {@link FamilyHistorianAdapter#setMultimediaDate(Multimedia, String)}
     */
    @Test
    public void testMultimediaDate() {
        Multimedia mm = gedcomWithCustomTags.getMultimedia().get("@O1@");
        assertEquals("7 JUL 1977", fha.getMultimediaDate(mm));

        fha.setMultimediaDate(mm, null);
        assertNull(fha.getMultimediaDate(mm));

        fha.setMultimediaDate(mm, "4 JUL 1976");
        assertEquals("4 JUL 1976", fha.getMultimediaDate(mm));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getMultimediaDate(Multimedia)} and
     * {@link FamilyHistorianAdapter#setMultimediaDate(Multimedia, String)}
     */
    @Test
    public void testMultimediaDateNegative() {
        Multimedia mm = gedcomWithCustomTags.getMultimedia().get("@O1@");
        mm.getCustomFacts(true).add(new CustomFact("_DATE")); // Add a bad date custom fact without description
        assertEquals("7 JUL 1977", fha.getMultimediaDate(mm));

        fha.setMultimediaDate(mm, null);
        assertNull(fha.getMultimediaDate(mm));

        fha.setMultimediaDate(mm, "4 JUL 1976");
        assertEquals("4 JUL 1976", fha.getMultimediaDate(mm));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getMultimediaFile(Multimedia)} and
     * {@link FamilyHistorianAdapter#setMultimediaFile(Multimedia, String)}
     */
    @Test
    public void testMultimediaFile() {
        Multimedia mm = gedcomWithCustomTags.getMultimedia().get("@O1@");
        assertEquals("photo.jpg", fha.getMultimediaFile(mm));

        fha.setMultimediaFile(mm, null);
        assertNull(fha.getMultimediaFile(mm));

        fha.setMultimediaFile(mm, "photo.png");
        assertEquals("photo.png", fha.getMultimediaFile(mm));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getMultimediaFile(Multimedia)} and
     * {@link FamilyHistorianAdapter#setMultimediaFile(Multimedia, String)}
     */
    @Test
    public void testMultimediaFileNegative() {
        Multimedia mm = gedcomWithCustomTags.getMultimedia().get("@O1@");
        mm.getCustomFacts(true).add(new CustomFact("_FILE")); // Add a bad file custom fact without description
        assertEquals("photo.jpg", fha.getMultimediaFile(mm));

        fha.setMultimediaFile(mm, null);
        assertNull(fha.getMultimediaFile(mm));

        fha.setMultimediaFile(mm, "picture.png");
        assertEquals("picture.png", fha.getMultimediaFile(mm));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getMultimediaKeys(Multimedia)} and
     * {@link FamilyHistorianAdapter#setMultimediaKeys(Multimedia, String)}
     */
    @Test
    public void testMultimediaKeys() {
        Multimedia mm = gedcomWithCustomTags.getMultimedia().get("@O1@");
        assertEquals("Picture", fha.getMultimediaKeys(mm));

        fha.setMultimediaKeys(mm, null);
        assertNull(fha.getMultimediaKeys(mm));

        fha.setMultimediaKeys(mm, "Movie");
        assertEquals("Movie", fha.getMultimediaKeys(mm));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getMultimediaKeys(Multimedia)} and
     * {@link FamilyHistorianAdapter#setMultimediaKeys(Multimedia, String)}
     */
    @Test
    public void testMultimediaKeysNegative() {
        Multimedia mm = gedcomWithCustomTags.getMultimedia().get("@O1@");
        mm.getCustomFacts(true).add(new CustomFact("_KEYS")); // Add a bad keys custom fact without description
        assertEquals("Picture", fha.getMultimediaKeys(mm));

        fha.setMultimediaKeys(mm, null);
        assertNull(fha.getMultimediaKeys(mm));

        fha.setMultimediaKeys(mm, "Photo");
        assertEquals("Photo", fha.getMultimediaKeys(mm));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getMultimediaNote(Multimedia)} and
     * {@link FamilyHistorianAdapter#setMultimediaNote(Multimedia, String)}
     */
    @Test
    public void testMultimediaNote() {
        Multimedia mm = gedcomWithCustomTags.getMultimedia().get("@O1@");
        assertEquals("Picture is a bit small and may not be him.", fha.getMultimediaNote(mm));

        fha.setMultimediaNote(mm, null);
        assertNull(fha.getMultimediaNote(mm));

        fha.setMultimediaNote(mm, "Black and white");
        assertEquals("Black and white", fha.getMultimediaNote(mm));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getMultimediaNoteArea(NoteStructure)} and
     * {@link FamilyHistorianAdapter#setMultimediaNoteArea(NoteStructure, String)}
     */
    @Test
    public void testMultimediaNoteArea() {
        Multimedia mm = gedcomWithCustomTags.getMultimedia().get("@O1@");
        assertNotNull(mm.getNoteStructures());
        assertEquals(1, mm.getNoteStructures().size());

        NoteStructure n = mm.getNoteStructures().get(0);

        assertEquals("{154,46,243,127}", fha.getMultimediaNoteArea(n));

        fha.setMultimediaNoteArea(n, null);
        assertNull(fha.getMultimediaNoteArea(n));

        fha.setMultimediaNoteArea(n, "Chess");
        assertEquals("Chess", fha.getMultimediaNoteArea(n));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getMultimediaNoteASID(NoteStructure)} and
     * {@link FamilyHistorianAdapter#setMultimediaNoteASID(NoteStructure, String)} (whatever an ASID is...)
     */
    @Test
    public void testMultimediaNoteASID() {
        Multimedia mm = gedcomWithCustomTags.getMultimedia().get("@O1@");
        assertNotNull(mm.getNoteStructures());
        assertEquals(1, mm.getNoteStructures().size());

        NoteStructure n = mm.getNoteStructures().get(0);

        assertEquals("1", fha.getMultimediaNoteASID(n));

        fha.setMultimediaNoteASID(n, null);
        assertNull(fha.getMultimediaNoteASID(n));

        fha.setMultimediaNoteASID(n, "3");
        assertEquals("3", fha.getMultimediaNoteASID(n));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getMultimediaNoteCaption(NoteStructure)} and
     * {@link FamilyHistorianAdapter#setMultimediaNoteCaption(NoteStructure, String)}
     */
    @Test
    public void testMultimediaNoteCaption() {
        Multimedia mm = gedcomWithCustomTags.getMultimedia().get("@O1@");
        assertNotNull(mm.getNoteStructures());
        assertEquals(1, mm.getNoteStructures().size());

        NoteStructure n = mm.getNoteStructures().get(0);

        assertEquals("Y", fha.getMultimediaNoteCaption(n));

        fha.setMultimediaNoteCaption(n, null);
        assertNull(fha.getMultimediaNoteCaption(n));

        fha.setMultimediaNoteCaption(n, "N");
        assertEquals("N", fha.getMultimediaNoteCaption(n));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getMultimediaNoteExclusion(NoteStructure)} and
     * {@link FamilyHistorianAdapter#setMultimediaNoteExclusion(NoteStructure, String)}
     */
    @Test
    public void testMultimediaNoteExclusion() {
        Multimedia mm = gedcomWithCustomTags.getMultimedia().get("@O1@");
        assertNotNull(mm.getNoteStructures());
        assertEquals(1, mm.getNoteStructures().size());

        NoteStructure n = mm.getNoteStructures().get(0);

        assertEquals("ALL", fha.getMultimediaNoteExclusion(n));

        fha.setMultimediaNoteExclusion(n, null);
        assertNull(fha.getMultimediaNoteExclusion(n));

        fha.setMultimediaNoteExclusion(n, "Synchronicity");
        assertEquals("Synchronicity", fha.getMultimediaNoteExclusion(n));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getMultimediaNote(Multimedia)} and
     * {@link FamilyHistorianAdapter#setMultimediaNote(Multimedia, String)}
     */
    @Test
    public void testMultimediaNoteNegative() {
        Multimedia mm = gedcomWithCustomTags.getMultimedia().get("@O1@");
        mm.getCustomFacts(true).add(new CustomFact("_NOTE")); // Add a bad note custom fact without description
        assertEquals("Picture is a bit small and may not be him.", fha.getMultimediaNote(mm));

        fha.setMultimediaNote(mm, null);
        assertNull(fha.getMultimediaNote(mm));

        fha.setMultimediaNote(mm, "This is a note");
        assertEquals("This is a note", fha.getMultimediaNote(mm));
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
     * Negative test for {@link FamilyHistorianAdapter#isEditingEnabled(CustomFact)} and
     * {@link FamilyHistorianAdapter#setEditingEnabled(CustomFact, boolean)}
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNamedListEditingEnabledNegative() {
        CustomFact cf = new CustomFact("_WRONGTAG");
        fha.setEditingEnabled(cf, false);
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#isEditingEnabled(CustomFact)}
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNamedListEditingEnabledNegative2() {
        CustomFact cf = new CustomFact("_WRONGTAG");
        fha.isEditingEnabled(cf);
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#isEditingEnabled(CustomFact)}
     */
    @Test
    public void testNamedListEditingEnabledNegative3() {
        CustomFact cf = new CustomFact("_LIST");
        CustomFact flag = new CustomFact("_FLAG");
        flag.setDescription("WONTMATCH");
        cf.getCustomFacts(true).add(flag);
        assertFalse(fha.isEditingEnabled(cf));
    }

    /**
     * Test for Namesake
     */
    @Test
    public void testNamesake() {
        List<CustomFact> namesakes = fha.getNamesake(tom);
        assertNotNull(namesakes);
        assertEquals(1, namesakes.size());
        CustomFact namesake = namesakes.get(0);
        assertEquals("_ATTR", namesake.getTag());
        assertEquals("Tom Thompson (a complete stranger)", namesake.getDescription().getValue());
        assertEquals("Namesake", namesake.getType().getValue());

        fha.removeNamesake(tom);
        namesakes = fha.getNamesake(tom);
        assertNotNull(namesakes);
        assertEquals(0, namesakes.size());

        CustomFact n = fha.newNamesake("frying pan");
        fha.addNamesake(tom, n);

        namesakes = fha.getNamesake(tom);
        assertNotNull(namesakes);
        assertEquals(1, namesakes.size());
        namesake = namesakes.get(0);
        assertEquals("_ATTR", namesake.getTag());
        assertEquals("frying pan", namesake.getDescription().getValue());
        assertEquals("Namesake", namesake.getType().getValue());
    }

    /**
     * Test {@link FamilyHistorianAdapter#newPlace(String, String)} with a bad xref
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNewPlaceNegativeBadXref() {
        fha.newPlace("bad", "asdf");
    }

    /**
     * Test {@link FamilyHistorianAdapter#newPlace(String, String)} with a blank place name
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNewPlaceNegativeBlankPlaceName() {
        fha.newPlace("@P1@", " ");
    }

    /**
     * Test {@link FamilyHistorianAdapter#newPlace(String, String)} with a null place name
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNewPlaceNegativeNullPlaceName() {
        fha.newPlace("@P1@", null);
    }

    /**
     * Test {@link FamilyHistorianAdapter#newPlace(String, String)} with a null xref
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNewPlaceNegativeNullXref() {
        fha.newPlace(null, "asdf");
    }

    /**
     * Test {@link FamilyHistorianAdapter#newPlace(String, String)} with the place name and xref transposed
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNewPlaceNegativeXrefPlacenameTransposed() {
        fha.newPlace("Virginia", "@P1@");
    }

    /**
     * Test {@link FamilyHistorianAdapter#newPlace(String, String)} with good parameters
     */
    @Test
    public void testNewPlacePositive() {
        assertNotNull(fha.newPlace("@P1@", "Florida"));
    }

    /**
     * Test for Ordinances
     */
    @Test
    public void testOrdinances() {
        List<CustomFact> ordinances = fha.getOrdinances(tom);
        assertNotNull(ordinances);
        assertEquals(1, ordinances.size());
        CustomFact ordinance = ordinances.get(0);
        assertEquals("_ATTR", ordinance.getTag());
        assertEquals("wasd", ordinance.getDescription().getValue());
        assertEquals("Ordinance", ordinance.getType().getValue());

        fha.removeOrdinances(tom);
        ordinances = fha.getOrdinances(tom);
        assertNotNull(ordinances);
        assertEquals(0, ordinances.size());

        CustomFact n = fha.newOrdinance("frying pan");
        fha.addOrdinance(tom, n);

        ordinances = fha.getOrdinances(tom);
        assertNotNull(ordinances);
        assertEquals(1, ordinances.size());
        ordinance = ordinances.get(0);
        assertEquals("_ATTR", ordinance.getTag());
        assertEquals("frying pan", ordinance.getDescription().getValue());
        assertEquals("Ordinance", ordinance.getType().getValue());
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getPlaceRecords(IGedcom)},
     * {@link FamilyHistorianAdapter#getPlaceRecord(IGedcom, String)},
     * {@link FamilyHistorianAdapter#removePlaceRecord(IGedcom, String)},
     * {@link FamilyHistorianAdapter#removePlaceRecords(IGedcom)},
     * {@link FamilyHistorianAdapter#addPlaceRecord(IGedcom, CustomFact)}, and
     * {@link FamilyHistorianAdapter#newPlace(String, String)}
     */
    @Test
    public void testPlaceRecords() {
        List<CustomFact> places = fha.getPlaceRecords(gedcomWithCustomTags);
        assertNotNull(places);
        assertEquals(11, places.size());

        CustomFact p1 = fha.getPlaceRecord(gedcomWithCustomTags, "@P1@");
        assertEquals(places.get(0), p1);
        assertEquals("Ansted, WV, USA", p1.getDescription().getValue());

        fha.removePlaceRecord(gedcomWithCustomTags, p1.getXref());
        assertNull(fha.getPlaceRecord(gedcomWithCustomTags, "@P1@"));

        places = fha.getPlaceRecords(gedcomWithCustomTags);
        assertNotNull(places);
        assertEquals(10, places.size());

        fha.addPlaceRecord(gedcomWithCustomTags, p1);
        assertSame(p1, fha.getPlaceRecord(gedcomWithCustomTags, "@P1@"));
        assertEquals("Ansted, WV, USA", p1.getDescription().getValue());

        places = fha.getPlaceRecords(gedcomWithCustomTags);
        assertNotNull(places);
        assertEquals(11, places.size());

        fha.removePlaceRecords(gedcomWithCustomTags);
        assertNull(fha.getPlaceRecord(gedcomWithCustomTags, "@P1@"));

        places = fha.getPlaceRecords(gedcomWithCustomTags);
        assertNotNull(places);
        assertEquals(0, places.size());

        CustomFact newPlace = fha.newPlace("@P999@", "Cucamonga");
        fha.addPlaceRecord(gedcomWithCustomTags, newPlace);
        p1 = fha.getPlaceRecord(gedcomWithCustomTags, "@P999@");
        assertEquals(p1, newPlace);
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getPlaceRecords(IGedcom)}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testPlaceRecordsImmutable() {
        List<CustomFact> places = fha.getPlaceRecords(gedcomWithCustomTags);
        assertNotNull(places);
        assertEquals(11, places.size());
        places.clear();
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getPlaceRecord(IGedcom, String)}
     */
    @Test
    public void testPlaceRecordsNegative() {
        gedcomWithoutCustomTags.getCustomFacts(true).add(new CustomFact("_PLAC"));
        assertNull(fha.getPlaceRecord(gedcomWithCustomTags, "@NOTTHERE@"));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#removeNamedList(IGedcom, String)}
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
     * Test for {@link FamilyHistorianAdapter#removeNamedList(IGedcom, String)}
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNamedListNegative() {
        fha.removeNamedList(gedcomWithCustomTags, null);
    }

    /**
     * Test for {@link FamilyHistorianAdapter#removeNamedList(IGedcom, String)}
     */
    @Test
    public void testRemoveNamedListNegative2() {
        assertEquals(0, fha.removeNamedList(gedcomWithoutCustomTags, "FOO"));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#removeNamedList(IGedcom, String)}
     */
    @Test
    public void testRemoveNamedListNegative3() {
        gedcomWithoutCustomTags.getCustomFacts(true).add(new CustomFact("_QWERTY"));
        assertEquals(0, fha.removeNamedList(gedcomWithoutCustomTags, "irrelevant"));
    }

    /**
     * Test for {@link FamilyHistorianAdapter#removeNamedLists(IGedcom)}
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
     * Negative test for {@link FamilyHistorianAdapter#removePlaceRecord(IGedcom, String)}
     */
    @Test
    public void testRemovePlaceRecordNegative1() {
        fha.removePlaceRecord(gedcomWithoutCustomTags, "@PLACETHATDOESNTEXIST@");
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#removePlaceRecord(IGedcom, String)}
     */
    @Test
    public void testRemovePlaceRecordNegative2() {
        gedcomWithoutCustomTags.getCustomFacts(true);
        fha.removePlaceRecord(gedcomWithoutCustomTags, "@PLACETHATDOESNTEXIST@");
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#removePlaceRecord(IGedcom, String)}
     */
    @Test
    public void testRemovePlaceRecordNegative3() {
        gedcomWithoutCustomTags.getCustomFacts(true).add(new CustomFact("_IRRELEVANT"));
        fha.removePlaceRecord(gedcomWithoutCustomTags, "@PLACETHATDOESNTEXIST@");
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#removePlaceRecord(IGedcom, String)}
     */
    @Test
    public void testRemovePlaceRecordNegative4() {
        CustomFact cf = new CustomFact("_PLAC");
        cf.setXref("@P0@");
        gedcomWithoutCustomTags.getCustomFacts(true).add(cf);
        fha.removePlaceRecord(gedcomWithoutCustomTags, "@PLACETHATDOESNTEXIST@");
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#setFlags(Individual, CustomFact)}
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetFlagsNegative() {
        CustomFact flags = new CustomFact("_WRONGTAG");
        fha.setFlags(new Individual(), flags);
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#setFlags(Individual, CustomFact)}
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetFlagsNegative2() {
        fha.setFlags(new Individual(), new CustomFact("_WRONGTAG"));
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#setOtherPlaceName(IndividualEvent, String)}
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetOtherPlaceNameNegative1() {
        fha.setOtherPlaceName(null, "Irrelevant");
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#setOtherPlaceName(IndividualEvent, String)}
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetOtherPlaceNameNegative2() {
        IndividualEvent e = new IndividualEvent();
        e.setType(IndividualEventType.ADOPTION);
        fha.setOtherPlaceName(null, "Irrelevant");
    }

    /**
     * Negative test {@link FamilyHistorianAdapter#getRootIndividual(IGedcom)} and
     * {@link FamilyHistorianAdapter#setRootIndividual(IGedcom, Individual)}
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetRootIndividualNegative1() {
        Individual individual = fha.getRootIndividual(gedcomWithoutCustomTags);
        assertNull(individual);

        fha.setRootIndividual(gedcomWithoutCustomTags, tom);
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getRootIndividual(IGedcom)} and positive test for
     * {@link FamilyHistorianAdapter#setRootIndividual(IGedcom, Individual)}
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

    /**
     * Negative test for {@link FamilyHistorianAdapter#setRootIndividual(IGedcom, Individual)}
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetRootIndividualNegative3() {
        fha.setRootIndividual(gedcomWithoutCustomTags, null);
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#setVariantExportFormat(IGedcom, String)}
     */
    @Test
    public void testSetVariantExportFormatNegative() {
        gedcomWithoutCustomTags.getHeader().setGedcomVersion(null);
        fha.setVariantExportFormat(gedcomWithoutCustomTags, "irrelevant");
    }

    /**
     * Test for {@link FamilyHistorianAdapter#getSourceType(Source)} and
     * {@link FamilyHistorianAdapter#setSourceType(Source, String)}
     */
    @Test
    public void testSourceType() {
        Source src = gedcomWithCustomTags.getSources().get("@S1@");
        assertEquals("Hearsay", fha.getSourceType(src));

        fha.setSourceType(src, null);
        assertNull(fha.getSourceType(src));

        fha.setSourceType(src, "Napkin drawing");
        assertEquals("Napkin drawing", fha.getSourceType(src));
    }

    /**
     * Negative test for {@link FamilyHistorianAdapter#getSourceType(Source)}
     */
    @Test
    public void testSourceTypeNegative() {
        Source src = new Source();
        src.getCustomFacts(true).add(new CustomFact("_TYPE"));
        assertNull(fha.getSourceType(src));
    }
}
