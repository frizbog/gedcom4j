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
package org.gedcom4j.comparators;

import static org.junit.Assert.assertTrue;

import org.gedcom4j.model.Individual;
import org.gedcom4j.model.PersonalName;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link IndividualByLastNameFirstNameComparator}
 * 
 * @author frizbog1
 * 
 */
public class IndividualByLastNameFirstNameComparatorTest {

    /**
     * The comparator being tested
     */
    IndividualByLastNameFirstNameComparator c;

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
        c = new IndividualByLastNameFirstNameComparator();
        i1 = new Individual();
        i2 = new Individual();
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when both
     * individuals have multiple names.
     */
    @Test
    public void testBothHaveMultipleNames() {
        addBasicName(i1, "Bob /Martin/");
        addBasicName(i1, "Aaron /Aardvark/");
        addBasicName(i2, "Bob /Marley/");
        addBasicName(i2, "Ziggy /Zoots/");

        assertTrue("Bob Martin comes after Bob Marley, and Ziggy Zoots and Aaron Aardvark get ignored", c.compare(i1, i2) > 0);
        assertTrue("Bob Marley comes before Bob Martin, and Ziggy Zoots and Aaron Aardvark get ignored", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when neither
     * individual has any names.
     */
    @Test
    public void testNobodyHasNames() {
        assertTrue("Individuals with no names should compare equal", c.compare(i1, i2) == 0);
        assertTrue("Individuals with no names should compare equal", c.compare(i2, i1) == 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when one
     * individual has multiple names.
     */
    @Test
    public void testOneHasMultipleNames() {
        addBasicName(i1, "Bob /Martin/");
        addBasicName(i2, "Bob /Marley/");
        addBasicName(i2, "Ziggy /Zoots/");

        assertTrue("Bob Martin comes after Bob Marley, and Ziggy Zoots gets ignored", c.compare(i1, i2) > 0);
        assertTrue("Bob Marley comes before Bob Martin, and Ziggy Zoots gets ignored", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when one
     * individual has no names and the other does
     */
    @Test
    public void testOneHasNoName() {
        addBasicName(i1, "Bob /Marley/");

        assertTrue("The individual with a name should always come after the individual with no names", c.compare(i1, i2) > 0);
        assertTrue("The individual with a name should always come after the individual with no names", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when last names
     * differ.
     */
    @Test
    public void testSurnamesDiffer() {
        addBasicName(i1, "Bob /Martin/");
        addBasicName(i2, "Bob /Marley/");

        assertTrue("Bob Martin comes after Bob Marley", c.compare(i1, i2) > 0);
        assertTrue("Bob Marley comes before Bob Martin", c.compare(i2, i1) < 0);
    }

    /**
     * Test for {@link IndividualByLastNameFirstNameComparator#compare(Individual, Individual)} . Tests when last names
     * differ.
     */
    @Test
    public void testSurnamesSame() {
        addBasicName(i1, "Ziggy /Marley/");
        addBasicName(i2, "Bob /Marley/");

        assertTrue("Ziggy Marley comes after Bob Marley", c.compare(i1, i2) > 0);
        assertTrue("Bob Marley comes before Ziggy Marley", c.compare(i2, i1) < 0);
    }

    /**
     * Helper method for readability, that adds a basic name to an individual
     * 
     * @param i
     *            the individual who is getting their name added
     * @param string
     *            the basic name to add to the individual
     */
    private void addBasicName(Individual i, String string) {
        PersonalName n = new PersonalName();
        n.basic = string;
        i.getNames().add(n);
    }

}
