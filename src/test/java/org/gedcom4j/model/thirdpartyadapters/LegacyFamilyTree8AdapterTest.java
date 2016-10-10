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
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.Address;
import org.gedcom4j.model.CustomFact;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.FamilyEvent;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.Source;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.model.thirdpartyadapters.LegacyFamilyTree8Adapter.AddressMailingList;
import org.gedcom4j.parser.GedcomParser;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link LegacyFamilyTree8Adapter}.
 * 
 * @author frizbog
 */
@SuppressWarnings("PMD.TooManyMethods")
public class LegacyFamilyTree8AdapterTest {

    /**
     * Class under test
     */
    private final LegacyFamilyTree8Adapter lfta = new LegacyFamilyTree8Adapter();

    /**
     * The test gedcom we read from the sample file that HAS custom tags
     */
    @SuppressWarnings("PMD.SingularField")
    private Gedcom gedcomWithCustomTags;

    /**
     * The test gedcom we read from the sample file that DOES NOT have custom tags, for negative testing
     */
    private Gedcom gedcomWithoutCustomTags;

    /**
     * The main person in the test file
     */
    private Individual will;

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
        gp.load("sample/legacycustomtags.ged");
        gedcomWithCustomTags = gp.getGedcom();
        assertNotNull(gedcomWithCustomTags);

        gp = new GedcomParser();
        gp.load("sample/legacynocustomtags.ged");
        gedcomWithoutCustomTags = gp.getGedcom();
        assertNotNull(gedcomWithoutCustomTags);

        assertNotSame(gedcomWithCustomTags, gedcomWithoutCustomTags);

        will = gedcomWithCustomTags.getIndividuals().get("@I2@");
        assertNotNull(will);
    }

    /**
     * 
     */
    @Test
    public void testAddNewRemoveRemoveAllToDos() {
        List<CustomFact> toDos = lfta.getToDos(will);
        assertNotNull(toDos);
        assertEquals(2, toDos.size());

        CustomFact cf1 = toDos.get(0);
        CustomFact cf2 = toDos.get(1);

        assertEquals(2, lfta.removeToDos(will));

        toDos = lfta.getToDos(will);
        assertNotNull(toDos);
        assertEquals(0, toDos.size());

        // Reversing order on purpose
        lfta.addToDo(will, cf2);
        lfta.addToDo(will, cf1);

        toDos = lfta.getToDos(will);
        assertNotNull(toDos);
        assertEquals(2, toDos.size());

        assertEquals(cf2, toDos.get(0));
        assertEquals(cf1, toDos.get(1));

        assertEquals(1, lfta.removeTodo(will, cf2));
        toDos = lfta.getToDos(will);
        assertNotNull(toDos);
        assertEquals(1, toDos.size());
        assertEquals(cf1, toDos.get(0));

        CustomFact cf3 = lfta.newToDo();
        lfta.addToDo(will, cf3);

        toDos = lfta.getToDos(will);
        assertNotNull(toDos);
        assertEquals(2, toDos.size());
    }

    /**
     * Negative test for {@link LegacyFamilyTree8Adapter#getAddressSortValue(Address)}
     */
    @Test
    public void testAddressSortValueNegative() {
        Individual willNoCustomTags = gedcomWithoutCustomTags.getIndividuals().get("@I2@");
        IndividualEvent milSvc = willNoCustomTags.getEventsOfType(IndividualEventType.EVENT).get(0);
        assertEquals("Military Service", milSvc.getSubType().getValue());
        Address addr = milSvc.getAddress();
        assertNotNull(addr);
        assertNull(lfta.getAddressSortValue(addr));
    }

    /**
     * Positive test for {@link LegacyFamilyTree8Adapter#getAddressSortValue(Address)} and
     * {@link LegacyFamilyTree8Adapter#setAddressSortValue(Address, String)}
     */
    @Test
    public void testAddressSortValuePositive() {
        IndividualEvent milSvc = will.getEventsOfType(IndividualEventType.EVENT).get(0);
        assertEquals("Military Service", milSvc.getSubType().getValue());
        Address addr = milSvc.getAddress();
        assertNotNull(addr);

        assertEquals("Main", lfta.getAddressSortValue(addr));
        lfta.setAddressSortValue(addr, (String) null);
        assertNull(lfta.getAddressSortValue(addr));
        lfta.setAddressSortValue(addr, "zzzzzSortMeLastzzzzzz");
        assertEquals("zzzzzSortMeLastzzzzzz", lfta.getAddressSortValue(addr));
    }

    /**
     * Test for {@link LegacyFamilyTree8Adapter#getAddressEmail(Address)} and
     * {@link LegacyFamilyTree8Adapter#setAddressEmail(Address, String)}
     */
    @Test
    public void testGetSetAddressEmail() {
        Address addr = will.getEventsOfType(IndividualEventType.DEATH).get(0).getAddress();

        String s = lfta.getAddressEmail(addr);
        assertEquals("matt@gedcom4j.org", s);

        lfta.setAddressEmail(addr, null);
        assertNull(lfta.getAddressEmail(addr));

        lfta.setAddressEmail(addr, "support@gedcom4j.org");
        assertEquals("support@gedcom4j.org", lfta.getAddressEmail(addr));
    }

    /**
     * Test for {@link LegacyFamilyTree8Adapter#getAddressListFlag(Address, AddressMailingList)} and
     * {@link LegacyFamilyTree8Adapter#setAddressListFlag(Address, AddressMailingList, String)}
     */
    @Test
    public void testGetSetAddressListFlag() {
        Address addr = will.getEventsOfType(IndividualEventType.BIRTH).get(0).getAddress();

        for (AddressMailingList ml : AddressMailingList.values()) {

            String s = lfta.getAddressListFlag(addr, ml);
            assertEquals("Y", s);

            lfta.setAddressListFlag(addr, ml, null);
            assertNull(lfta.getAddressListFlag(addr, ml));

            lfta.setAddressListFlag(addr, ml, "N");
            assertEquals("N", lfta.getAddressListFlag(addr, ml));
        }
    }

    /**
     * Test for {@link LegacyFamilyTree8Adapter#getAddressPrivateFlag(Address)} and
     * {@link LegacyFamilyTree8Adapter#setAddressPrivateFlag(Address, String)}
     */
    @Test
    public void testGetSetAddressPrivateFlag() {
        Address addr = will.getEventsOfType(IndividualEventType.DEATH).get(0).getAddress();

        String s = lfta.getAddressPrivateFlag(addr);
        assertEquals("Y", s);

        lfta.setAddressPrivateFlag(addr, null);
        assertNull(lfta.getAddressPrivateFlag(addr));

        lfta.setAddressPrivateFlag(addr, "N");
        assertEquals("N", lfta.getAddressPrivateFlag(addr));
    }

    /**
     * Test for {@link LegacyFamilyTree8Adapter#getFamilyEventPrivateFlag(FamilyEvent)} and
     * {@link LegacyFamilyTree8Adapter#setFamilyEventPrivateFlag(FamilyEvent, String)}
     */
    @Test
    public void testGetSetFamilyEventPrivateFlag() {
        FamilyEvent marriage = gedcomWithCustomTags.getFamilies().get("@F1@").getEvents().get(0);

        String s = lfta.getFamilyEventPrivateFlag(marriage);
        assertEquals("Y", s);

        lfta.setFamilyEventPrivateFlag(marriage, null);
        assertNull(lfta.getFamilyEventPrivateFlag(marriage));

        lfta.setFamilyEventPrivateFlag(marriage, "N");
        assertEquals("N", lfta.getFamilyEventPrivateFlag(marriage));
    }

    /**
     * Test for {@link LegacyFamilyTree8Adapter#getFamilyMemberPreferredFlag(Family, Individual)} and
     * {@link LegacyFamilyTree8Adapter#setFamilyMemberPreferredFlag(Family, Individual, String)}
     */
    @Test
    public void testGetSetFamilyMemberPreferredFlagDad() {
        Family f = gedcomWithCustomTags.getFamilies().get("@F3@");
        Individual dad = f.getHusband().getIndividual();

        String s = lfta.getFamilyMemberPreferredFlag(f, dad);
        assertEquals("Y", s);

        lfta.setFamilyMemberPreferredFlag(f, dad, null);
        assertNull(lfta.getFamilyMemberPreferredFlag(f, dad));

        lfta.setFamilyMemberPreferredFlag(f, dad, "N");
        assertEquals("N", lfta.getFamilyMemberPreferredFlag(f, dad));
    }

    /**
     * Test for {@link LegacyFamilyTree8Adapter#getFamilyMemberPreferredFlag(Family, Individual)} and
     * {@link LegacyFamilyTree8Adapter#setFamilyMemberPreferredFlag(Family, Individual, String)}
     */
    @Test
    public void testGetSetFamilyMemberPreferredFlagKid() {
        Family f = gedcomWithCustomTags.getFamilies().get("@F1@");
        Individual kid1 = f.getChildren().get(0).getIndividual();
        Individual kid2 = f.getChildren().get(1).getIndividual();

        assertEquals("Y", lfta.getFamilyMemberPreferredFlag(f, kid1));
        assertNull(lfta.getFamilyMemberPreferredFlag(f, kid2));

        lfta.setFamilyMemberPreferredFlag(f, kid1, null);
        assertNull(lfta.getFamilyMemberPreferredFlag(f, kid1));
        assertNull(lfta.getFamilyMemberPreferredFlag(f, kid2));

        lfta.setFamilyMemberPreferredFlag(f, kid1, "N");
        assertEquals("N", lfta.getFamilyMemberPreferredFlag(f, kid1));
        assertNull(lfta.getFamilyMemberPreferredFlag(f, kid2));
    }

    /**
     * Test for {@link LegacyFamilyTree8Adapter#getFamilyMemberPreferredFlag(Family, Individual)} and
     * {@link LegacyFamilyTree8Adapter#setFamilyMemberPreferredFlag(Family, Individual, String)}
     */
    @Test
    public void testGetSetFamilyMemberPreferredFlagMom() {
        Family f = gedcomWithCustomTags.getFamilies().get("@F3@");
        Individual mom = f.getWife().getIndividual();

        String s = lfta.getFamilyMemberPreferredFlag(f, mom);
        assertEquals("Y", s);

        lfta.setFamilyMemberPreferredFlag(f, mom, null);
        assertNull(lfta.getFamilyMemberPreferredFlag(f, mom));

        lfta.setFamilyMemberPreferredFlag(f, mom, "N");
        assertEquals("N", lfta.getFamilyMemberPreferredFlag(f, mom));
    }

    /**
     * Test for {@link LegacyFamilyTree8Adapter#getIndividualEventPrivateFlag(IndividualEvent)} and
     * {@link LegacyFamilyTree8Adapter#setIndividualEventPrivateFlag(IndividualEvent, String)}
     */
    @Test
    public void testGetSetIndividualEventPrivateFlag() {
        IndividualEvent birth = will.getEventsOfType(IndividualEventType.BIRTH).get(0);

        String s = lfta.getIndividualEventPrivateFlag(birth);
        assertEquals("Y", s);

        lfta.setIndividualEventPrivateFlag(birth, null);
        assertNull(lfta.getIndividualEventPrivateFlag(birth));

        lfta.setIndividualEventPrivateFlag(birth, "N");
        assertEquals("N", lfta.getIndividualEventPrivateFlag(birth));
    }

    /**
     * Test for {@link LegacyFamilyTree8Adapter#getMultimediaDate(Multimedia)} and
     * {@link LegacyFamilyTree8Adapter#setMultimediaDate(Multimedia, String)}
     */
    @Test
    public void testGetSetMultimediaDate() {
        Multimedia mm = will.getMultimedia().get(0).getMultimedia();

        String s = lfta.getMultimediaDate(mm);
        assertEquals("4 Apr 2013", s);

        lfta.setMultimediaDate(mm, null);
        assertNull(lfta.getMultimediaDate(mm));

        lfta.setMultimediaDate(mm, "01 Jan 1986");
        assertEquals("01 Jan 1986", lfta.getMultimediaDate(mm));
    }

    /**
     * Test for {@link LegacyFamilyTree8Adapter#getMultimediaPrimaryFlag(Multimedia)} and
     * {@link LegacyFamilyTree8Adapter#setMultimediaPrimaryFlag(Multimedia, String)}
     */
    @Test
    public void testGetSetMultimediaPrimaryFlag() {
        Multimedia mm = will.getMultimedia().get(0).getMultimedia();

        String s = lfta.getMultimediaPrimaryFlag(mm);
        assertEquals("Y", s);

        lfta.setMultimediaPrimaryFlag(mm, null);
        assertNull(lfta.getMultimediaPrimaryFlag(mm));

        lfta.setMultimediaPrimaryFlag(mm, "N");
        assertEquals("N", lfta.getMultimediaPrimaryFlag(mm));
    }

    /**
     * Test for {@link LegacyFamilyTree8Adapter#getMultimediaScrapbookTag(Multimedia)} and
     * {@link LegacyFamilyTree8Adapter#setMultimediaScrapbookTag(Multimedia, String)}
     */
    @Test
    public void testGetSetMultimediaScrapbookTag() {
        Multimedia mm = will.getMultimedia().get(0).getMultimedia();

        String s = lfta.getMultimediaScrapbookTag(mm);
        assertEquals("Y", s);

        lfta.setMultimediaScrapbookTag(mm, null);
        assertNull(lfta.getMultimediaScrapbookTag(mm));

        lfta.setMultimediaScrapbookTag(mm, "N");
        assertEquals("N", lfta.getMultimediaScrapbookTag(mm));
    }

    /**
     * Test for {@link LegacyFamilyTree8Adapter#getMultimediaSound(Multimedia)} and
     * {@link LegacyFamilyTree8Adapter#setMultimediaSound(Multimedia, String)}
     */
    @Test
    public void testGetSetMultimediaSound() {
        Multimedia mm = will.getMultimedia().get(0).getMultimedia();

        String s = lfta.getMultimediaSound(mm);
        assertEquals("Alarm01.wav", s);

        lfta.setMultimediaSound(mm, null);
        assertNull(lfta.getMultimediaSound(mm));

        lfta.setMultimediaSound(mm, "funkytown.mp3");
        assertEquals("funkytown.mp3", lfta.getMultimediaSound(mm));
    }

    /**
     * Test for {@link LegacyFamilyTree8Adapter#getMultimediaType(Multimedia)} and
     * {@link LegacyFamilyTree8Adapter#setMultimediaType(Multimedia, String)}
     */
    @Test
    public void testGetSetMultimediaType() {
        Multimedia mm = will.getMultimedia().get(0).getMultimedia();

        String s = lfta.getMultimediaType(mm);
        assertEquals("PHOTO", s);

        lfta.setMultimediaType(mm, null);
        assertNull(lfta.getMultimediaType(mm));

        lfta.setMultimediaType(mm, "MOVIE");
        assertEquals("MOVIE", lfta.getMultimediaType(mm));
    }

    /**
     * Test {@link LegacyFamilyTree8Adapter#getSourceInItalicsFlag(Source)} and
     * {@link LegacyFamilyTree8Adapter#setSourceInItalicsFlag(Source, String)}
     */
    @Test
    public void testGetSetSourceInItalics() {
        Source src = gedcomWithCustomTags.getSources().get("@S2@");

        String s = lfta.getSourceInItalicsFlag(src);
        assertEquals("Y", s);

        lfta.setSourceInItalicsFlag(src, null);
        assertNull(lfta.getSourceInItalicsFlag(src));

        lfta.setSourceInItalicsFlag(src, "N");
        assertEquals("N", lfta.getSourceInItalicsFlag(src));

    }

    /**
     * Test {@link LegacyFamilyTree8Adapter#getSourceInParensFlag(Source)} and
     * {@link LegacyFamilyTree8Adapter#setSourceInParensFlag(Source, String)}
     */
    @Test
    public void testGetSetSourceInParens() {
        Source src = gedcomWithCustomTags.getSources().get("@S2@");

        String s = lfta.getSourceInParensFlag(src);
        assertEquals("Y", s);

        lfta.setSourceInParensFlag(src, null);
        assertNull(lfta.getSourceInParensFlag(src));

        lfta.setSourceInParensFlag(src, "N");
        assertEquals("N", lfta.getSourceInParensFlag(src));

    }

    /**
     * Test {@link LegacyFamilyTree8Adapter#getSourceInQuotesFlag(Source)} and
     * {@link LegacyFamilyTree8Adapter#setSourceInQuotesFlag(Source, String)}
     */
    @Test
    public void testGetSetSourceInQuotes() {
        Source src = gedcomWithCustomTags.getSources().get("@S2@");

        String s = lfta.getSourceInQuotesFlag(src);
        assertEquals("Y", s);

        lfta.setSourceInQuotesFlag(src, null);
        assertNull(lfta.getSourceInQuotesFlag(src));

        lfta.setSourceInQuotesFlag(src, "N");
        assertEquals("N", lfta.getSourceInQuotesFlag(src));

    }

    /**
     * Test {@link LegacyFamilyTree8Adapter#getToDos(org.gedcom4j.model.HasCustomFacts)}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetTodosImmutable() {
        List<CustomFact> todos = lfta.getToDos(will);
        assertNotNull(todos);
        assertEquals(2, todos.size());
        todos.clear();
    }

    /**
     * Negative test for {@link LegacyFamilyTree8Adapter#getIndividualUID(Individual)}
     */
    @Test
    public void testIndividualUIDNegative() {
        Individual willNoCustomTags = gedcomWithoutCustomTags.getIndividuals().get("@I2@");
        assertNull(lfta.getIndividualUID(willNoCustomTags));
    }

    /**
     * Positive test for {@link LegacyFamilyTree8Adapter#getIndividualUID(Individual)} and
     * {@link LegacyFamilyTree8Adapter#setIndividualUID(Individual, String)}
     */
    @Test
    public void testIndividualUIDPositive() {
        assertEquals("A0DFC922CDC44040B6B22B58106216AB9916", lfta.getIndividualUID(will));

        lfta.setIndividualUID(will, (String) null);
        assertNull(lfta.getIndividualUID(will));
        lfta.setIndividualUID(will, "12345678-1234-1234-1234-1234567890AB");
        assertEquals("12345678-1234-1234-1234-1234567890AB", lfta.getIndividualUID(will));
    }

    /**
     * Test for {@link LegacyFamilyTree8Adapter#isAddressTagged(Address)} and
     * {@link LegacyFamilyTree8Adapter#setAddressTagged(Address, boolean)}
     */
    @Test
    public void testIsSetAddressTag() {
        Address addr = will.getEventsOfType(IndividualEventType.BIRTH).get(0).getAddress();

        assertTrue(lfta.isAddressTagged(addr));

        lfta.setAddressTagged(addr, false);
        assertFalse(lfta.isAddressTagged(addr));
        assertEquals(0, addr.getCustomFactsWithTag("_TAG").size());

        lfta.setAddressTagged(addr, true);
        assertTrue(lfta.isAddressTagged(addr));
    }

    /**
     * Test for {@link LegacyFamilyTree8Adapter#isFamilyHadNoChildren(Family)} and
     * {@link LegacyFamilyTree8Adapter#setFamilyHadNoChildren(Family, boolean)}
     */
    @Test
    public void testIsSetFamilyHadNoChildren() {
        Family family = gedcomWithCustomTags.getFamilies().get("@F3@");

        assertTrue(lfta.isFamilyHadNoChildren(family));

        lfta.setFamilyHadNoChildren(family, false);
        assertFalse(lfta.isFamilyHadNoChildren(family));

        lfta.setFamilyHadNoChildren(family, true);
        assertTrue(lfta.isFamilyHadNoChildren(family));
    }

    /**
     * Test for {@link LegacyFamilyTree8Adapter#isIndividualTagged(Individual)} and
     * {@link LegacyFamilyTree8Adapter#setIndividualTagged(Individual, boolean)}
     */
    @Test
    public void testIsSetIndividualTag() {
        assertTrue(lfta.isIndividualTagged(will));

        lfta.setIndividualTagged(will, false);
        assertFalse(lfta.isIndividualTagged(will));
        assertEquals(0, will.getCustomFactsWithTag("_TAG").size());

        lfta.setIndividualTagged(will, true);
        assertTrue(lfta.isIndividualTagged(will));
    }

    /**
     * Negative test for {@link LegacyFamilyTree8Adapter#getNameAtAddress(Address)}
     */
    @Test
    public void testNameAtAddressNegative() {
        Individual willNoCustomTags = gedcomWithoutCustomTags.getIndividuals().get("@I2@");
        IndividualEvent milSvc = willNoCustomTags.getEventsOfType(IndividualEventType.EVENT).get(0);
        assertEquals("Military Service", milSvc.getSubType().getValue());
        Address addr = milSvc.getAddress();
        assertNotNull(addr);
        assertNull(lfta.getNameAtAddress(addr));
    }

    /**
     * Positive test for {@link LegacyFamilyTree8Adapter#getNameAtAddress(Address)} and
     * {@link LegacyFamilyTree8Adapter#setNameAtAddress(Address, String)}
     */
    @Test
    public void testNameAtAddressPositive() {
        IndividualEvent milSvc = will.getEventsOfType(IndividualEventType.EVENT).get(0);
        assertEquals("Military Service", milSvc.getSubType().getValue());
        Address addr = milSvc.getAddress();
        assertNotNull(addr);

        assertEquals("Will Williams", lfta.getNameAtAddress(addr));
        lfta.setNameAtAddress(addr, (String) null);
        assertNull(lfta.getNameAtAddress(addr));
        lfta.setNameAtAddress(addr, "Bill Williams");
        assertEquals("Bill Williams", lfta.getNameAtAddress(addr));
    }

    /**
     * Test for {@link LegacyFamilyTree8Adapter#getToDoCategory(CustomFact)} and
     * {@link LegacyFamilyTree8Adapter#setToDoCategory(CustomFact, String)}
     */
    @Test
    public void testToDoCategory() {
        CustomFact toDo = lfta.newToDo();
        assertNull(lfta.getToDoCategory(toDo));

        lfta.setToDoCategory(toDo, "License");
        assertEquals("License", lfta.getToDoCategory(toDo));

        assertEquals("Birth Certificate", lfta.getToDoCategory(lfta.getToDos(will).get(0)));
    }

    /**
     * Test for {@link LegacyFamilyTree8Adapter#getToDoClosedDate(CustomFact)} and
     * {@link LegacyFamilyTree8Adapter#setToDoClosedDate(CustomFact, String)}
     */
    @Test
    public void testToDoClosedDate() {
        CustomFact toDo = lfta.newToDo();
        assertNull(lfta.getToDoClosedDate(toDo));

        lfta.setToDoClosedDate(toDo, "08 OCT 2016");
        assertEquals("08 OCT 2016", lfta.getToDoClosedDate(toDo));

        assertEquals("1 Oct 2016", lfta.getToDoClosedDate(lfta.getToDos(will).get(1)));
    }

    /**
     * Test for {@link LegacyFamilyTree8Adapter#getToDoDescription(CustomFact)} and
     * {@link LegacyFamilyTree8Adapter#setToDoDescription(CustomFact, String)}. Note that Legacy Family Tree 8 puts the description
     * of the to-do in a child "DESC" tag (without the leading underscore - a tag normally reserved for DESCendands) and not in the
     * value to the right of the tag (which is represented in the {@link CustomFact#getDescription()} field, which could be a source
     * of confusion)
     */
    @Test
    public void testToDoDescription() {
        CustomFact toDo = lfta.newToDo();
        assertNull(lfta.getToDoDescription(toDo)); // The ToDo description child element
        assertNull(toDo.getDescription()); // Note: NOT the custom tag description (value to right of tag)

        lfta.setToDoDescription(toDo, "License");
        assertEquals("License", lfta.getToDoDescription(toDo));
        assertNull(toDo.getDescription());

        CustomFact willToDo1 = lfta.getToDos(will).get(0);
        assertEquals("Obtain", lfta.getToDoDescription(willToDo1));
        assertNull(willToDo1.getDescription());
    }

    /**
     * Test for {@link LegacyFamilyTree8Adapter#getToDoLocality(CustomFact)} and
     * {@link LegacyFamilyTree8Adapter#setToDoLocality(CustomFact, String)}
     */
    @Test
    public void testToDoLocality() {
        CustomFact toDo = lfta.newToDo();
        assertNull(lfta.getToDoLocality(toDo));

        lfta.setToDoLocality(toDo, "Cucamonga");
        assertEquals("Cucamonga", lfta.getToDoLocality(toDo));

        assertEquals("Somewhere over the rainbow", lfta.getToDoLocality(lfta.getToDos(will).get(0)));
    }

}
