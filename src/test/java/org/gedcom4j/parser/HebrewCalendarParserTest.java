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
package org.gedcom4j.parser;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test for {@link HebrewCalendarParser}
 * 
 * @author frizbog
 */
public class HebrewCalendarParserTest {

    /**
     * Save the Time Zone used on this machine - we're going to tinker during the test
     */
    private static TimeZone saveTimeZone;

    /**
     * Restore the TimeZone
     */
    @AfterClass
    public static void afterClass() {
        TimeZone.setDefault(saveTimeZone);
    }

    /**
     * Save a copy of the TimeZone
     */
    @BeforeClass
    public static void beforeClass() {
        saveTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    /**
     * Class under test
     */
    private final HebrewCalendarParser classUnderTest = new HebrewCalendarParser();

    /**
     * Test method for {@link org.gedcom4j.parser.HebrewCalendarParser#convertHebrewDateToGregorian(int, java.lang.String, int)}.
     */
    @Test
    public void testConvertHebrewDateToGregorian() {
        assertEquals(getDate(2016, Calendar.JULY, 19), classUnderTest.convertHebrewDateToGregorian(5776, HebrewMonth.TAMUZ
                .getGedcomAbbrev(), 13));
        assertEquals(getDate(1977, Calendar.MAY, 1), classUnderTest.convertHebrewDateToGregorian(5737, HebrewMonth.IYAR
                .getGedcomAbbrev(), 13));
        assertEquals(getDate(1980, Calendar.FEBRUARY, 29), classUnderTest.convertHebrewDateToGregorian(5740, HebrewMonth.ADAR
                .getGedcomAbbrev(), 12));
        assertEquals(getDate(1917, Calendar.NOVEMBER, 11), classUnderTest.convertHebrewDateToGregorian(5678, HebrewMonth.CHESHVAN
                .getGedcomAbbrev(), 26));
        assertEquals(getDate(1776, Calendar.JULY, 4), classUnderTest.convertHebrewDateToGregorian(5536, HebrewMonth.TAMUZ
                .getGedcomAbbrev(), 17));
    }

    /**
     * Helper method to get a gregorian date based on year, month, and day
     * 
     * @param y
     *            gregorian year
     * @param m
     *            gregorian month
     * @param d
     *            day
     * @return gregorian date
     */
    private Date getDate(int y, int m, int d) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.clear();
        c.set(y, m, d, 0, 0);
        return c.getTime();
    }

}
