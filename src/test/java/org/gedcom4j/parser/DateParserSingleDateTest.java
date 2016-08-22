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

import java.util.Calendar;

import org.gedcom4j.parser.DateParser.ImpreciseDatePreference;
import org.junit.Test;

/**
 * Test class for {@link DateParser} methods dealing with single Gregorian dates.
 * 
 * @author frizbog
 */
public class DateParserSingleDateTest extends AbstractDateParserTest {

    /**
     * Test parsing a single date with month, day, and year
     */
    @Test
    public void testParseSingleDateFull() {
        assertDate(dp.parse("17 JUL 2016"), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("31 DEC 2016"), 2016, Calendar.DECEMBER, 31);
        assertDate(dp.parse("1 JUL 932"), 932, Calendar.JULY, 1);
        // 2016 was a leap year
        assertDate(dp.parse("29 FEB 2016"), 2016, Calendar.FEBRUARY, 29);
        // 1900 was NOT a leap year, so Feb 29 of that year is parsed as Mar 1.
        assertDate(dp.parse("29 FEB 1900"), 1900, Calendar.MARCH, 1);
        assertDate(dp.parse("29 feb 1900"), 1900, Calendar.MARCH, 1);
        assertDate(dp.parse("29 Feb 1900"), 1900, Calendar.MARCH, 1);
    }

    /**
     * Test parsing a single date with month and year but no day, using precise preference
     */
    @Test
    public void testParseSingleDateNoDayPrecise() {
        assertDate(dp.parse("JUL 2016"), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("JUL 932"), 932, Calendar.JULY, 1);
        // 2016 was a leap year
        assertDate(dp.parse("FEB 2016"), 2016, Calendar.FEBRUARY, 1);
        // 1900 was NOT a leap year, so Feb 29 of that year is parsed as Mar 1.
        assertDate(dp.parse("FEB 1900"), 1900, Calendar.FEBRUARY, 1);
        assertDate(dp.parse("feb 1900"), 1900, Calendar.FEBRUARY, 1);
        assertDate(dp.parse("Feb 1900"), 1900, Calendar.FEBRUARY, 1);
    }

    /**
     * Test parsing a single date with month and year but no day, using precise preference
     */
    @Test
    public void testParseSingleDateNoDayPreferEarliest() {
        assertDate(dp.parse("JUL 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("JUL 932", ImpreciseDatePreference.FAVOR_EARLIEST), 932, Calendar.JULY, 1);
        // 2016 was a leap year
        assertDate(dp.parse("FEB 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.FEBRUARY, 1);
        // 1900 was NOT a leap year, despite being divisible by 4 - only ever four centuries is a leap year. So Feb 28 would be last
        // day in Feb.
        assertDate(dp.parse("FEB 1900", ImpreciseDatePreference.FAVOR_EARLIEST), 1900, Calendar.FEBRUARY, 1);
        assertDate(dp.parse("feb 1900", ImpreciseDatePreference.FAVOR_EARLIEST), 1900, Calendar.FEBRUARY, 1);
        assertDate(dp.parse("Feb 1900", ImpreciseDatePreference.FAVOR_EARLIEST), 1900, Calendar.FEBRUARY, 1);
    }

    /**
     * Test parsing a single date with month and year but no day, using precise preference
     */
    @Test
    public void testParseSingleDateNoDayPreferLatest() {
        assertDate(dp.parse("JUL 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 31);
        assertDate(dp.parse("JUL 932", ImpreciseDatePreference.FAVOR_LATEST), 932, Calendar.JULY, 31);
        // 2016 was a leap year
        assertDate(dp.parse("FEB 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.FEBRUARY, 29);
        // 1900 was NOT a leap year, despite being divisible by 4 - only ever four centuries is a leap year. So Feb 28 would be last
        // day in Feb.
        assertDate(dp.parse("FEB 1900", ImpreciseDatePreference.FAVOR_LATEST), 1900, Calendar.FEBRUARY, 28);
        assertDate(dp.parse("feb 1900", ImpreciseDatePreference.FAVOR_LATEST), 1900, Calendar.FEBRUARY, 28);
        assertDate(dp.parse("Feb 1900", ImpreciseDatePreference.FAVOR_LATEST), 1900, Calendar.FEBRUARY, 28);
    }

    /**
     * Test parsing a single date with month and year but no day, using precise preference
     */
    @Test
    public void testParseSingleDateNoDayPreferMidpoint() {
        assertDate(dp.parse("JUL 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 15);
        assertDate(dp.parse("JUL 932", ImpreciseDatePreference.FAVOR_MIDPOINT), 932, Calendar.JULY, 15);
        // 2016 was a leap year
        assertDate(dp.parse("FEB 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.FEBRUARY, 14);
        // 1900 was NOT a leap year, despite being divisible by 4 - only ever four centuries is a leap year. So Feb 28 would be last
        // day in Feb.
        assertDate(dp.parse("FEB 1900", ImpreciseDatePreference.FAVOR_MIDPOINT), 1900, Calendar.FEBRUARY, 14);
        assertDate(dp.parse("feb 1900", ImpreciseDatePreference.FAVOR_MIDPOINT), 1900, Calendar.FEBRUARY, 14);
        assertDate(dp.parse("Feb 1900", ImpreciseDatePreference.FAVOR_MIDPOINT), 1900, Calendar.FEBRUARY, 14);
    }

    /**
     * Test parsing a single date with month and year but no day, using precise preference
     */
    @Test
    public void testParseSingleDateYearOnlyPrecise() {
        assertDate(dp.parse("2016"), 2016, Calendar.JANUARY, 1);
        assertDate(dp.parse("932"), 932, Calendar.JANUARY, 1);
        assertDate(dp.parse("2016"), 2016, Calendar.JANUARY, 1);
        assertDate(dp.parse("1900"), 1900, Calendar.JANUARY, 1);
    }

    /**
     * Test parsing a single date with month and year but no day, using precise preference
     */
    @Test
    public void testParseSingleDateYearOnlyPreferEarliest() {
        assertDate(dp.parse("2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JANUARY, 1);
        assertDate(dp.parse("932", ImpreciseDatePreference.FAVOR_EARLIEST), 932, Calendar.JANUARY, 1);
        assertDate(dp.parse("2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JANUARY, 1);
        assertDate(dp.parse("1900", ImpreciseDatePreference.FAVOR_EARLIEST), 1900, Calendar.JANUARY, 1);
    }

    /**
     * Test parsing a single date with month and year but no day, using precise preference
     */
    @Test
    public void testParseSingleDateYearOnlyPreferLatest() {
        assertDate(dp.parse("2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.DECEMBER, 31);
        assertDate(dp.parse("932", ImpreciseDatePreference.FAVOR_LATEST), 932, Calendar.DECEMBER, 31);
        assertDate(dp.parse("2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.DECEMBER, 31);
        assertDate(dp.parse("1900", ImpreciseDatePreference.FAVOR_LATEST), 1900, Calendar.DECEMBER, 31);
    }

    /**
     * Test parsing a single date with month and year but no day, using precise preference
     */
    @Test
    public void testParseSingleDateYearOnlyPreferMidpoint() {
        assertDate(dp.parse("2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("932", ImpreciseDatePreference.FAVOR_MIDPOINT), 932, Calendar.JULY, 1);
        assertDate(dp.parse("2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("1900", ImpreciseDatePreference.FAVOR_MIDPOINT), 1900, Calendar.JULY, 1);
    }

}
