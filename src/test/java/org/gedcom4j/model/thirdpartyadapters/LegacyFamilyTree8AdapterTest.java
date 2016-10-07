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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.List;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.Address;
import org.gedcom4j.model.CustomFact;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.enumerations.IndividualEventType;
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
        assertEquals("Alarm01.wav", s);

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
     * Test for {@link LegacyFamilyTree8Adapter#getToDoDescription(CustomFact)} and
     * {@link LegacyFamilyTree8Adapter#setToDoDescription(CustomFact, String)}. Note that Legacy Family Tree 8 puts the description
     * of the to-do in a child "DESC" tag (without the leading underscore - a tag normally reserved for DESCendands) and not in the
     * value to the right of the tag.
     */
    @Test
    public void testToDoDescription() {
        CustomFact toDo = lfta.newToDo();
        assertNull(lfta.getToDoDescription(toDo));
        assertNull(toDo.getDescription());

        lfta.setToDoDescription(toDo, "License");
        assertEquals("License", lfta.getToDoDescription(toDo));
        assertNull(toDo.getDescription());

        CustomFact willToDo1 = lfta.getToDos(will).get(0);
        assertEquals("Birth Certificate", lfta.getToDoDescription(willToDo1));
        assertNull(willToDo1.getDescription());
    }
}
