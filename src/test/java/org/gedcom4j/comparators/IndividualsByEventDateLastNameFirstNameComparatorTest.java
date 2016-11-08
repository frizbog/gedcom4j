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
package org.gedcom4j.comparators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.parser.DateParser.ImpreciseDatePreference;
import org.junit.Test;

/**
 * Test for {@link IndividualsByEventDateLastNameFirstNameComparator}
 * 
 * @author frizbog
 */
public class IndividualsByEventDateLastNameFirstNameComparatorTest {

    /**
     * Test when individuals have same birthdate and name
     */
    @Test
    public void testEquals() {
        Individual i1 = new Individual();
        PersonalName pn1 = new PersonalName();
        pn1.setBasic("John /Johnson/");
        i1.getNames(true).add(pn1);
        IndividualEvent b1 = new IndividualEvent();
        b1.setType(IndividualEventType.BIRTH);
        b1.setDate("01 JAN 1990");
        i1.getEvents(true).add(b1);

        // Make a copy
        Individual i2 = new Individual(i1);

        assertNotSame(i1, i2);

        assertEquals(0, new IndividualsByEventDateLastNameFirstNameComparator(IndividualEventType.BIRTH,
                ImpreciseDatePreference.FAVOR_EARLIEST).compare(i1, i2));
    }

    /**
     * Test when individuals have no birthdates
     */
    @Test
    public void testNoBirthdates() {
        Individual i1 = new Individual();
        PersonalName pn1 = new PersonalName();
        pn1.setBasic("John /Johnson/");
        i1.getNames(true).add(pn1);

        // Make a copy
        Individual i2 = new Individual(i1);

        assertNotSame(i1, i2);

        assertEquals(0, new IndividualsByEventDateLastNameFirstNameComparator(IndividualEventType.BIRTH,
                ImpreciseDatePreference.FAVOR_EARLIEST).compare(i1, i2));
    }

    /**
     * Test when individuals have different birthdate but same name
     */
    @Test
    public void testNotEqualBirthDates() {
        Individual i1 = new Individual();
        PersonalName pn1 = new PersonalName();
        pn1.setBasic("John /Johnson/");
        i1.getNames(true).add(pn1);
        IndividualEvent b1 = new IndividualEvent();
        b1.setType(IndividualEventType.BIRTH);
        b1.setDate("01 JAN 1990");
        i1.getEvents(true).add(b1);

        // Make a copy
        Individual i2 = new Individual(i1);
        // And change the birth date
        i2.getEvents().get(0).setDate("02 JAN 1990");

        assertNotSame(i1, i2);

        assertEquals(-1, new IndividualsByEventDateLastNameFirstNameComparator(IndividualEventType.BIRTH,
                ImpreciseDatePreference.FAVOR_EARLIEST).compare(i1, i2));
        assertEquals(1, new IndividualsByEventDateLastNameFirstNameComparator(IndividualEventType.BIRTH,
                ImpreciseDatePreference.FAVOR_EARLIEST).compare(i2, i1));
    }

    /**
     * Test when individuals have same birthdate but different names
     */
    @Test
    public void testNotEqualNames() {
        Individual i1 = new Individual();
        PersonalName pn1 = new PersonalName();
        pn1.setBasic("John /Johnson/");
        i1.getNames(true).add(pn1);
        IndividualEvent b1 = new IndividualEvent();
        b1.setType(IndividualEventType.BIRTH);
        b1.setDate("01 JAN 1990");
        i1.getEvents(true).add(b1);

        // Make a copy
        Individual i2 = new Individual(i1);
        // And change the name
        i2.getNames().get(0).setBasic("Ken /Klein/");

        assertNotSame(i1, i2);

        assertEquals(-1, new IndividualsByEventDateLastNameFirstNameComparator(IndividualEventType.BIRTH,
                ImpreciseDatePreference.FAVOR_EARLIEST).compare(i1, i2));
        assertEquals(1, new IndividualsByEventDateLastNameFirstNameComparator(IndividualEventType.BIRTH,
                ImpreciseDatePreference.FAVOR_EARLIEST).compare(i2, i1));
    }

    /**
     * Test when one individual has birth date but the other does not
     */
    @Test
    public void testOneWithoutBirthDate() {
        Individual i1 = new Individual();
        PersonalName pn1 = new PersonalName();
        pn1.setBasic("John /Johnson/");
        i1.getNames(true).add(pn1);
        IndividualEvent b1 = new IndividualEvent();
        b1.setType(IndividualEventType.BIRTH);
        b1.setDate("01 JAN 1990");
        i1.getEvents(true).add(b1);

        // Make a copy
        Individual i2 = new Individual(i1);
        // And remove the birth date
        i2.getEvents().clear();

        assertNotSame(i1, i2);

        assertEquals(1, new IndividualsByEventDateLastNameFirstNameComparator(IndividualEventType.BIRTH,
                ImpreciseDatePreference.FAVOR_EARLIEST).compare(i1, i2));
        assertEquals(-1, new IndividualsByEventDateLastNameFirstNameComparator(IndividualEventType.BIRTH,
                ImpreciseDatePreference.FAVOR_EARLIEST).compare(i2, i1));
    }

}
