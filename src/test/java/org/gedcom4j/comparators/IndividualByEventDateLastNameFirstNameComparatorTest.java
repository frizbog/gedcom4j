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

import static org.junit.Assert.assertTrue;

import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.model.StringWithCustomTags;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.parser.DateParser.ImpreciseDatePreference;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link IndividualsByEventDateLastNameFirstNameComparator}
 * 
 * @author frizbog1
 * 
 */
@SuppressWarnings("PMD.TooManyMethods")
public class IndividualByEventDateLastNameFirstNameComparatorTest {

    /**
     * Helper method for readability, that adds a basic name to an individual
     * 
     * @param i
     *            the individual who is getting their name added
     * @param string
     *            the basic name to add to the individual
     */
    private static void addBasicName(Individual i, String string) {
        PersonalName n = new PersonalName();
        n.setBasic(string);
        i.getNames(true).add(n);
    }

    /**
     * Helper method for readability, that adds a birth date to an individual
     * 
     * @param i
     *            the individual that we're adding a birth date to
     * @param birthDateString
     *            the string value to use for the birth date
     */
    private static void addBirthDate(Individual i, String birthDateString) {
        IndividualEvent bd = new IndividualEvent();
        bd.setType(IndividualEventType.BIRTH);
        bd.setDate(new StringWithCustomTags(birthDateString));
        i.getEvents(true).add(bd);
    }

    /**
     * The comparator being tested
     */
    IndividualsByEventDateLastNameFirstNameComparator c;

    /**
     * Test fixture - one individual
     */
    Individual i1;

    /**
     * Test fixture - second individual
     */
    Individual i2;

    /**
     * Set up test fixtures
     */
    @Before
    public void setUp() {
        c = new IndividualsByEventDateLastNameFirstNameComparator(IndividualEventType.BIRTH,
                ImpreciseDatePreference.FAVOR_EARLIEST);
        i1 = new Individual();
        i2 = new Individual();
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when both individuals have
     * multiple names...only the first name matters.
     */
    @Test
    public void testNoBirthDatesBothHaveMultipleNames() {
        addBasicName(i1, "Bob /Martin/");
        addBasicName(i1, "Aaron /Aardvark/");
        addBasicName(i2, "Bob /Marley/");
        addBasicName(i2, "Ziggy /Zoots/");

        assertTrue("Bob Martin comes after Bob Marley, and Ziggy Zoots and Aaron Aardvark get ignored", c.compare(i1, i2) > 0);
        assertTrue("Bob Marley comes before Bob Martin, and Ziggy Zoots and Aaron Aardvark get ignored", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when neither individual has
     * any names.
     */
    @Test
    public void testNoBirthDatesNobodyHasNames() {
        assertTrue("Individuals with no names should compare equal", c.compare(i1, i2) == 0);
        assertTrue("Individuals with no names should compare equal", c.compare(i2, i1) == 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when one individual has
     * multiple names.
     */
    @Test
    public void testNoBirthDatesOneHasMultipleNames() {
        addBasicName(i1, "Bob /Martin/");
        addBasicName(i2, "Bob /Marley/");
        addBasicName(i2, "Ziggy /Zoots/");

        assertTrue("Bob Martin comes after Bob Marley, and Ziggy Zoots gets ignored", c.compare(i1, i2) > 0);
        assertTrue("Bob Marley comes before Bob Martin, and Ziggy Zoots gets ignored", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when one individual has no
     * names and the other does
     */
    @Test
    public void testNoBirthDatesOneHasNoName() {
        addBasicName(i1, "Bob /Marley/");

        assertTrue("The individual with a name should always come after the individual with no names", c.compare(i1, i2) > 0);
        assertTrue("The individual with a name should always come after the individual with no names", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when last names differ.
     */
    @Test
    public void testNoBirthDatesSurnamesDiffer() {
        addBasicName(i1, "Bob /Martin/");
        addBasicName(i2, "Bob /Marley/");

        assertTrue("Bob Martin comes after Bob Marley", c.compare(i1, i2) > 0);
        assertTrue("Bob Marley comes before Bob Martin", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when last names differ.
     */
    @Test
    public void testNoBirthDatesSurnamesSame() {
        addBasicName(i1, "Ziggy /Marley/");
        addBasicName(i2, "Bob /Marley/");

        assertTrue("Ziggy Marley comes after Bob Marley", c.compare(i1, i2) > 0);
        assertTrue("Bob Marley comes before Ziggy Marley", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when both individuals have
     * multiple names...only the first name matters.
     */
    @Test
    public void testOneBirthDateBothHaveMultipleNames() {
        addBasicName(i1, "Bob /Martin/");
        addBasicName(i1, "Aaron /Aardvark/");
        addBasicName(i2, "Bob /Marley/");
        addBasicName(i2, "Ziggy /Zoots/");

        addBirthDate(i1, "1 DEC 1950");

        assertTrue("Names don't matter when only one person has a birth date", c.compare(i1, i2) > 0);
        assertTrue("Names don't matter when only one person has a birth date", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when neither individual has
     * any names.
     */
    @Test
    public void testOneBirthDateNobodyHasNames() {
        addBirthDate(i1, "1 DEC 1950");

        assertTrue("Names don't matter when only one person has a birth date", c.compare(i1, i2) > 0);
        assertTrue("Names don't matter when only one person has a birth date", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when one individual has
     * multiple names.
     */
    @Test
    public void testOneBirthDateOneHasMultipleNames() {
        addBasicName(i1, "Bob /Martin/");
        addBasicName(i2, "Bob /Marley/");
        addBasicName(i2, "Ziggy /Zoots/");

        addBirthDate(i1, "1 DEC 1950");

        assertTrue("Names don't matter when only one person has a birth date", c.compare(i1, i2) > 0);
        assertTrue("Names don't matter when only one person has a birth date", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when one individual has no
     * names and the other does
     */
    @Test
    public void testOneBirthDateOneHasNoName() {
        addBasicName(i1, "Bob /Marley/");

        addBirthDate(i1, "1 DEC 1950");

        assertTrue("Names don't matter when only one person has a birth date", c.compare(i1, i2) > 0);
        assertTrue("Names don't matter when only one person has a birth date", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when last names differ.
     */
    @Test
    public void testOneBirthDateSurnamesDiffer() {
        addBasicName(i1, "Bob /Martin/");
        addBasicName(i2, "Bob /Marley/");

        addBirthDate(i1, "1 DEC 1950");

        assertTrue("Names don't matter when only one person has a birth date", c.compare(i1, i2) > 0);
        assertTrue("Names don't matter when only one person has a birth date", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when last names differ.
     */
    @Test
    public void testOneBirthDateSurnamesSame() {
        addBasicName(i1, "Ziggy /Marley/");
        addBasicName(i2, "Bob /Marley/");

        addBirthDate(i1, "1 DEC 1950");

        assertTrue("Names don't matter when only one person has a birth date", c.compare(i1, i2) > 0);
        assertTrue("Names don't matter when only one person has a birth date", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when both individuals have
     * multiple names...only the first name matters.
     */
    @Test
    public void testOtherBirthDateBothHaveMultipleNames() {
        addBasicName(i1, "Bob /Martin/");
        addBasicName(i1, "Aaron /Aardvark/");
        addBasicName(i2, "Bob /Marley/");
        addBasicName(i2, "Ziggy /Zoots/");

        addBirthDate(i2, "1 DEC 1950");

        assertTrue("Names don't matter when only one person has a birth date", c.compare(i1, i2) < 0);
        assertTrue("Names don't matter when only one person has a birth date", c.compare(i2, i1) > 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when neither individual has
     * any names.
     */
    @Test
    public void testOtherBirthDateNobodyHasNames() {
        addBirthDate(i2, "1 DEC 1950");

        assertTrue("Names don't matter when only one person has a birth date", c.compare(i1, i2) < 0);
        assertTrue("Names don't matter when only one person has a birth date", c.compare(i2, i1) > 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when one individual has
     * multiple names.
     */
    @Test
    public void testOtherBirthDateOneHasMultipleNames() {
        addBasicName(i1, "Bob /Martin/");
        addBasicName(i2, "Bob /Marley/");
        addBasicName(i2, "Ziggy /Zoots/");

        addBirthDate(i2, "1 DEC 1950");

        assertTrue("Names don't matter when only one person has a birth date", c.compare(i1, i2) < 0);
        assertTrue("Names don't matter when only one person has a birth date", c.compare(i2, i1) > 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when one individual has no
     * names and the other does
     */
    @Test
    public void testOtherBirthDateOneHasNoName() {
        addBasicName(i1, "Bob /Marley/");

        addBirthDate(i2, "1 DEC 1950");

        assertTrue("Names don't matter when only one person has a birth date", c.compare(i1, i2) < 0);
        assertTrue("Names don't matter when only one person has a birth date", c.compare(i2, i1) > 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when last names differ.
     */
    @Test
    public void testOtherBirthDateSurnamesDiffer() {
        addBasicName(i1, "Bob /Martin/");
        addBasicName(i2, "Bob /Marley/");

        addBirthDate(i2, "1 DEC 1950");

        assertTrue("Names don't matter when only one person has a birth date", c.compare(i1, i2) < 0);
        assertTrue("Names don't matter when only one person has a birth date", c.compare(i2, i1) > 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when last names differ.
     */
    @Test
    public void testOtherBirthDateSurnamesSame() {
        addBasicName(i1, "Ziggy /Marley/");
        addBasicName(i2, "Bob /Marley/");

        addBirthDate(i2, "1 DEC 1950");

        assertTrue("Names don't matter when only one person has a birth date", c.compare(i1, i2) < 0);
        assertTrue("Names don't matter when only one person has a birth date", c.compare(i2, i1) > 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when both individuals have
     * multiple names...only the first name matters.
     */
    @Test
    public void testSameBirthDatesBothHaveMultipleNames() {
        addBasicName(i1, "Bob /Martin/");
        addBasicName(i1, "Aaron /Aardvark/");
        addBasicName(i2, "Bob /Marley/");
        addBasicName(i2, "Ziggy /Zoots/");

        addBirthDate(i1, "1 JUL 1950");
        addBirthDate(i2, "1 JUL 1950");

        assertTrue("Bob Martin comes after Bob Marley, and Ziggy Zoots and Aaron Aardvark get ignored", c.compare(i1, i2) > 0);
        assertTrue("Bob Marley comes before Bob Martin, and Ziggy Zoots and Aaron Aardvark get ignored", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when neither individual has
     * any names.
     */
    @Test
    public void testSameBirthDatesNobodyHasNames() {

        addBirthDate(i1, "1 JUL 1950");
        addBirthDate(i2, "1 JUL 1950");

        assertTrue("Individuals with no names should compare equal", c.compare(i1, i2) == 0);
        assertTrue("Individuals with no names should compare equal", c.compare(i2, i1) == 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when one individual has
     * multiple names.
     */
    @Test
    public void testSameBirthDatesOneHasMultipleNames() {
        addBasicName(i1, "Bob /Martin/");
        addBasicName(i2, "Bob /Marley/");
        addBasicName(i2, "Ziggy /Zoots/");

        addBirthDate(i1, "1 JUL 1950");
        addBirthDate(i2, "1 JUL 1950");

        assertTrue("Bob Martin comes after Bob Marley, and Ziggy Zoots gets ignored", c.compare(i1, i2) > 0);
        assertTrue("Bob Marley comes before Bob Martin, and Ziggy Zoots gets ignored", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when one individual has no
     * names and the other does
     */
    @Test
    public void testSameBirthDatesOneHasNoName() {
        addBasicName(i1, "Bob /Marley/");

        addBirthDate(i1, "1 JUL 1950");
        addBirthDate(i2, "1 JUL 1950");

        assertTrue("The individual with a name should always come after the individual with no names", c.compare(i1, i2) > 0);
        assertTrue("The individual with a name should always come after the individual with no names", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when last names differ.
     */
    @Test
    public void testSameBirthDatesSurnamesDiffer() {
        addBasicName(i1, "Bob /Martin/");
        addBasicName(i2, "Bob /Marley/");

        addBirthDate(i1, "1 JUL 1950");
        addBirthDate(i2, "1 JUL 1950");

        assertTrue("Bob Martin comes after Bob Marley", c.compare(i1, i2) > 0);
        assertTrue("Bob Marley comes before Bob Martin", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when last names differ.
     */
    @Test
    public void testSameBirthDatesSurnamesSame() {
        addBasicName(i1, "Ziggy /Marley/");
        addBasicName(i2, "Bob /Marley/");

        addBirthDate(i1, "1 JUL 1950");
        addBirthDate(i2, "1 JUL 1950");

        assertTrue("Ziggy Marley comes after Bob Marley", c.compare(i1, i2) > 0);
        assertTrue("Bob Marley comes before Ziggy Marley", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when both individuals have
     * multiple names...only the first name matters.
     */
    @Test
    public void testSamePreferredBirthDatesBothHaveMultipleNames() {
        addBasicName(i1, "Bob /Martin/");
        addBasicName(i1, "Aaron /Aardvark/");
        addBasicName(i2, "Bob /Marley/");
        addBasicName(i2, "Ziggy /Zoots/");

        addBirthDate(i1, "1 JUL 1950");
        addBirthDate(i1, "1 DEC 1950"); // Will be ignored
        addBirthDate(i2, "1 JUL 1950");
        addBirthDate(i2, "1 DEC 2001"); // Will be ignored

        assertTrue("Bob Martin comes after Bob Marley, and Ziggy Zoots and Aaron Aardvark get ignored", c.compare(i1, i2) > 0);
        assertTrue("Bob Marley comes before Bob Martin, and Ziggy Zoots and Aaron Aardvark get ignored", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when neither individual has
     * any names.
     */
    @Test
    public void testSamePreferredBirthDatesNobodyHasNames() {
        addBirthDate(i1, "1 JUL 1950");
        addBirthDate(i1, "1 DEC 1950"); // Will be ignored
        addBirthDate(i2, "1 JUL 1950");
        addBirthDate(i2, "1 DEC 2001"); // Will be ignored

        assertTrue("Individuals with no names should compare equal", c.compare(i1, i2) == 0);
        assertTrue("Individuals with no names should compare equal", c.compare(i2, i1) == 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when one individual has
     * multiple names.
     */
    @Test
    public void testSamePreferredBirthDatesOneHasMultipleNames() {
        addBasicName(i1, "Bob /Martin/");
        addBasicName(i2, "Bob /Marley/");
        addBasicName(i2, "Ziggy /Zoots/");

        addBirthDate(i1, "1 JUL 1950");
        addBirthDate(i1, "1 DEC 1950"); // Will be ignored
        addBirthDate(i2, "1 JUL 1950");
        addBirthDate(i2, "1 DEC 2001"); // Will be ignored

        assertTrue("Bob Martin comes after Bob Marley, and Ziggy Zoots gets ignored", c.compare(i1, i2) > 0);
        assertTrue("Bob Marley comes before Bob Martin, and Ziggy Zoots gets ignored", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when one individual has no
     * names and the other does
     */
    @Test
    public void testSamePreferredBirthDatesOneHasNoName() {
        addBasicName(i1, "Bob /Marley/");

        addBirthDate(i1, "1 JUL 1950");
        addBirthDate(i1, "1 DEC 1950"); // Will be ignored
        addBirthDate(i2, "1 JUL 1950");
        addBirthDate(i2, "1 DEC 2001"); // Will be ignored

        assertTrue("The individual with a name should always come after the individual with no names", c.compare(i1, i2) > 0);
        assertTrue("The individual with a name should always come after the individual with no names", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when last names differ.
     */
    @Test
    public void testSamePreferredBirthDatesSurnamesDiffer() {
        addBasicName(i1, "Bob /Martin/");
        addBasicName(i2, "Bob /Marley/");

        addBirthDate(i1, "1 JUL 1950");
        addBirthDate(i1, "1 DEC 1950"); // Will be ignored
        addBirthDate(i2, "1 JUL 1950");
        addBirthDate(i2, "1 DEC 2001"); // Will be ignored

        assertTrue("Bob Martin comes after Bob Marley", c.compare(i1, i2) > 0);
        assertTrue("Bob Marley comes before Bob Martin", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when last names differ.
     */
    @Test
    public void testSamePreferredBirthDatesSurnamesSame() {
        addBasicName(i1, "Ziggy /Marley/");
        addBasicName(i2, "Bob /Marley/");

        addBirthDate(i1, "1 JUL 1950");
        addBirthDate(i1, "1 DEC 1950"); // Will be ignored
        addBirthDate(i2, "1 JUL 1950");
        addBirthDate(i2, "1 DEC 2001"); // Will be ignored

        assertTrue("Ziggy Marley comes after Bob Marley", c.compare(i1, i2) > 0);
        assertTrue("Bob Marley comes before Ziggy Marley", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when both individuals have
     * multiple names...only the first name matters.
     */
    @Test
    public void testTwoDiffBirthDatesBothHaveMultipleNames() {
        addBasicName(i1, "Bob /Martin/");
        addBasicName(i1, "Aaron /Aardvark/");
        addBasicName(i2, "Bob /Marley/");
        addBasicName(i2, "Ziggy /Zoots/");

        addBirthDate(i1, "30 JUN 1950");
        addBirthDate(i2, "1 JUL 1950");

        assertTrue("Bob Martin born before Bob Marley, names don't matter", c.compare(i1, i2) < 0);
        assertTrue("Bob Martin born before Bob Marley, names don't matter", c.compare(i2, i1) > 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when neither individual has
     * any names.
     */
    @Test
    public void testTwoDiffBirthDatesNobodyHasNames() {

        addBirthDate(i1, "30 JUN 1950");
        addBirthDate(i2, "1 JUL 1950");

        assertTrue("Bob Martin born before Bob Marley, names don't matter", c.compare(i1, i2) < 0);
        assertTrue("Bob Martin born before Bob Marley, names don't matter", c.compare(i2, i1) > 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when one individual has
     * multiple names.
     */
    @Test
    public void testTwoDiffBirthDatesOneHasMultipleNames() {
        addBasicName(i1, "Bob /Martin/");
        addBasicName(i2, "Bob /Marley/");
        addBasicName(i2, "Ziggy /Zoots/");

        addBirthDate(i1, "30 JUN 1950");
        addBirthDate(i2, "1 JUL 1950");

        assertTrue("Bob Martin born before Bob Marley, names don't matter", c.compare(i1, i2) < 0);
        assertTrue("Bob Martin born before Bob Marley, names don't matter", c.compare(i2, i1) > 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when one individual has no
     * names and the other does
     */
    @Test
    public void testTwoDiffBirthDatesOneHasNoName() {
        addBasicName(i1, "Bob /Marley/");

        addBirthDate(i1, "30 JUN 1950");
        addBirthDate(i2, "1 JUL 1950");

        assertTrue("Bob Martin born before Bob Marley, names don't matter", c.compare(i1, i2) < 0);
        assertTrue("Bob Martin born before Bob Marley, names don't matter", c.compare(i2, i1) > 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when last names differ.
     */
    @Test
    public void testTwoDiffBirthDatesSurnamesDiffer() {
        addBasicName(i1, "Bob /Martin/");
        addBasicName(i2, "Bob /Marley/");

        addBirthDate(i1, "30 JUN 1950");
        addBirthDate(i2, "1 JUL 1950");

        assertTrue("Bob Martin born before Bob Marley, names don't matter", c.compare(i1, i2) < 0);
        assertTrue("Bob Martin born before Bob Marley, names don't matter", c.compare(i2, i1) > 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when last names differ.
     */
    @Test
    public void testTwoDiffBirthDatesSurnamesSame() {
        addBasicName(i1, "Ziggy /Marley/");
        addBasicName(i2, "Bob /Marley/");

        addBirthDate(i1, "30 JUN 1950");
        addBirthDate(i2, "1 JUL 1950");

        assertTrue("Bob Martin born before Bob Marley, names don't matter", c.compare(i1, i2) < 0);
        assertTrue("Bob Martin born before Bob Marley, names don't matter", c.compare(i2, i1) > 0);
    }

}
