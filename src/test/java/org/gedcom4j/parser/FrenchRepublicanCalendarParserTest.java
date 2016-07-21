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
 * Test for {@link FrenchRepublicanCalendarParser}
 * 
 * @author frizbog
 */
public class FrenchRepublicanCalendarParserTest {

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
     * The class being tested
     */
    private final FrenchRepublicanCalendarParser classUnderTest = new FrenchRepublicanCalendarParser();

    /**
     * Test {@link FrenchRepublicanCalendarParser#convertFrenchRepublicanDateToGregorian(int, String, int)}
     */
    @Test
    public void testConvertFrenchRepublicanDateToGregorian() {
        assertEquals(getDate(1792, Calendar.SEPTEMBER, 22), classUnderTest.convertFrenchRepublicanDateToGregorian(1, "VEND", 1));
        assertEquals(getDate(1792, Calendar.OCTOBER, 22), classUnderTest.convertFrenchRepublicanDateToGregorian(1, "BRUM", 1));
        assertEquals(getDate(1792, Calendar.NOVEMBER, 21), classUnderTest.convertFrenchRepublicanDateToGregorian(1, "FRIM", 1));
        assertEquals(getDate(1792, Calendar.DECEMBER, 21), classUnderTest.convertFrenchRepublicanDateToGregorian(1, "NIVO", 1));
        assertEquals(getDate(1793, Calendar.JANUARY, 20), classUnderTest.convertFrenchRepublicanDateToGregorian(1, "PLUV", 1));
        assertEquals(getDate(1793, Calendar.FEBRUARY, 19), classUnderTest.convertFrenchRepublicanDateToGregorian(1, "VENT", 1));

        assertEquals("Consolidation of the Revolutionary government on 14 Frimaire, year II (December 4, 1793)", getDate(1793,
                Calendar.DECEMBER, 4), classUnderTest.convertFrenchRepublicanDateToGregorian(2, "FRIM", 14));

        assertEquals("Robespierre found guilty on 9 Thermidor An II (27 July 1794)", getDate(1794, Calendar.JULY, 27),
                classUnderTest.convertFrenchRepublicanDateToGregorian(2, "THER", 9));

        assertEquals("Legislation that accelerated the Reign of Terror on 22 Prairial, year II (June 10, 1794)", getDate(1794,
                Calendar.JUNE, 10), classUnderTest.convertFrenchRepublicanDateToGregorian(2, "PRAI", 22));

        assertEquals("Ecole Normale Superieure established on 9 Brumaire An III (30 October 1794)", getDate(1794, Calendar.OCTOBER,
                30), classUnderTest.convertFrenchRepublicanDateToGregorian(3, "BRUM", 9));

        assertEquals("Insurrection of the sansculottes on 1 Prairial, year III (May 20, 1795)", getDate(1795, Calendar.MAY, 20),
                classUnderTest.convertFrenchRepublicanDateToGregorian(3, "PRAI", 1));

        assertEquals("Failed coup and incidence of Napoleon's 'whiff of grapeshot' on 13 Vendémiaire An IV (5 October 1795)",
                getDate(1795, Calendar.OCTOBER, 5), classUnderTest.convertFrenchRepublicanDateToGregorian(4, "VEND", 13));

        assertEquals("The coup against monarchist restorationists on 18 Fructidor An V (4 September 1797)", getDate(1797,
                Calendar.SEPTEMBER, 4), classUnderTest.convertFrenchRepublicanDateToGregorian(5, "FRUC", 18));

        assertEquals("Coup in which 106 left–wing deputies were deprived of their seats on 22 Floréal Year VI (11 May 1798)",
                getDate(1798, Calendar.MAY, 11), classUnderTest.convertFrenchRepublicanDateToGregorian(6, "FLOR", 22));

        assertEquals("Coup backed militarily by General Joubert on 30 Prairial Year VII (18 June 1799)", getDate(1799,
                Calendar.JUNE, 18), classUnderTest.convertFrenchRepublicanDateToGregorian(7, "PRAI", 30));

        assertEquals("Napoleon's coup was on 18 Brumaire An VIII (9 November 1799)", getDate(1799, Calendar.NOVEMBER, 9),
                classUnderTest.convertFrenchRepublicanDateToGregorian(8, "BRUM", 18));
    }

    /**
     * Test {@link FrenchRepublicanCalendarParser#convertFrenchRepublicanDateToGregorian(int, String, int)}
     */
    @Test
    public void testConvertFrenchRepublicanDateToGregorian2() {

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
        c.set(y, m, d, 0, 0, 0);
        return c.getTime();
    }

}
