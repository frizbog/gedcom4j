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
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Test to verify fix for issue 88 where {@link Individual#toString()} was not functioning correctly
 */
public class Issue88Test {

    /**
     * Test {@link Individual#toString()} where there is only a birth event
     */
    @Test
    public void testIssue88BirthOnly() {

        // Set up an individual with a birth and death date
        Individual i = new Individual();
        PersonalName name = new PersonalName();
        name.setBasic("Bob /ROBERTS/");
        assertNull(i.getNames());
        i.getNames(true).add(name);
        IndividualEvent birth = new IndividualEvent();
        birth.setType(IndividualEventType.BIRTH);
        birth.setDate("1/1/1950");
        assertNull(i.getEvents());
        i.getEvents(true).add(birth);

        assertEquals("Bob /ROBERTS/, b.1/1/1950", i.toString());
    }

    /**
     * Test {@link Individual#toString()} where there is both a birth event and a death event
     */
    @Test
    public void testIssue88BothDates() {

        // Set up an individual with a birth and death date
        Individual i = new Individual();
        PersonalName name = new PersonalName();
        name.setBasic("Bob /ROBERTS/");
        assertNull(i.getNames());
        i.getNames(true).add(name);
        IndividualEvent birth = new IndividualEvent();
        birth.setType(IndividualEventType.BIRTH);
        birth.setDate("1/1/1950");
        assertNull(i.getEvents());
        i.getEvents(true).add(birth);
        IndividualEvent death = new IndividualEvent();
        death.setType(IndividualEventType.DEATH);
        death.setDate("12/31/1999");
        i.getEvents(true).add(death);

        assertEquals("Bob /ROBERTS/, b.1/1/1950, d.12/31/1999", i.toString());
    }

    /**
     * Test {@link Individual#toString()} where there is only a death event
     */
    @Test
    public void testIssue88DeathOnly() {

        // Set up an individual with a birth and death date
        Individual i = new Individual();
        PersonalName name = new PersonalName();
        name.setBasic("Bob /ROBERTS/");
        assertNull(i.getNames());
        i.getNames(true).add(name);
        IndividualEvent death = new IndividualEvent();
        death.setType(IndividualEventType.DEATH);
        death.setDate("12/31/1999");
        assertNull(i.getEvents());
        i.getEvents(true).add(death);

        assertEquals("Bob /ROBERTS/, d.12/31/1999", i.toString());
    }

}
