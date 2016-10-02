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

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.Address;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.parser.GedcomParser;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link LegacyFamilyTree8Adapter}.
 * 
 * @author frizbog
 */
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

}
