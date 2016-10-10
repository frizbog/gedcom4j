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
package org.gedcom4j.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.Calendar;
import java.util.Date;

import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.junit.Test;

/**
 * Test for {@link IndividualFactory}
 * 
 * @author frizbog
 */
public class IndividualFactoryTest {

    /**
     * Test for
     * {@link IndividualFactory#create(org.gedcom4j.model.Gedcom, String, String, Sex, java.util.Date, String, java.util.Date, String)}
     */
    @Test
    public void testCreate() {
        Gedcom g = new Gedcom();
        @SuppressWarnings("deprecation")
        Individual i = new IndividualFactory().create(g, "Robert", "Tarantino", Sex.MALE, new Date(67, Calendar.MAY, 1), "Idaho",
                new Date(99, Calendar.OCTOBER, 31), "Virginia");

        assertNotNull(i);
        assertNotNull(i.getXref());

        Individual i2 = g.getIndividuals().get(i.getXref());
        assertNotNull(i2);
        assertSame(i, i2);

        assertEquals("M", i.getSex().getValue());

        assertEquals("1 MAY 1967", i.getEventsOfType(IndividualEventType.BIRTH).get(0).getDate().getValue());
        assertEquals("31 OCT 1999", i.getEventsOfType(IndividualEventType.DEATH).get(0).getDate().getValue());
    }

}
