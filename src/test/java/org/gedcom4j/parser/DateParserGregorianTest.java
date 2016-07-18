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
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.Date;

import org.gedcom4j.parser.DateParser.ImpreciseDatePreference;
import org.junit.Test;

/**
 * Test case for DateParser. All test cases are for Gregorian dates.
 * 
 * @author frizbog
 */
public class DateParserGregorianTest {

    /**
     * Test parsing a single date with month, day, and year
     */
    @Test
    public void testParseApproximateSingleDateFull() {
        DateParser dp = new DateParser();
        assertDate(dp.parse("ABT 17 JUL 2016"), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("ABT 1 JUL 932"), 932, Calendar.JULY, 1);
        // 2016 was a leap year
        assertDate(dp.parse("ABT 29 FEB 2016"), 2016, Calendar.FEBRUARY, 29);
        // 1900 was NOT a leap year, so Feb 29 of that year is parsed as Mar 1.
        assertDate(dp.parse("ABT 29 FEB 1900"), 1900, Calendar.MARCH, 1);
        assertDate(dp.parse("APPX 29 FEB 1900"), 1900, Calendar.MARCH, 1);
        assertDate(dp.parse("APPROX 29 FEB 1900"), 1900, Calendar.MARCH, 1);
        assertDate(dp.parse("APPX. 29 FEB 1900"), 1900, Calendar.MARCH, 1);
        assertDate(dp.parse("APPROX. 29 FEB 1900"), 1900, Calendar.MARCH, 1);

        assertDate(dp.parse("ABT. 17 JUL 2016"), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("ABT. 1 JUL 932"), 932, Calendar.JULY, 1);
        // 2016 was a leap year
        assertDate(dp.parse("ABT. 29 FEB 2016"), 2016, Calendar.FEBRUARY, 29);
        // 1900 was NOT a leap year, so Feb 29 of that year is parsed as Mar 1.
        assertDate(dp.parse("ABT. 29 FEB 1900"), 1900, Calendar.MARCH, 1);

        assertDate(dp.parse("CAL 17 JUL 2016"), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("CAL 1 JUL 932"), 932, Calendar.JULY, 1);
        // 2016 was a leap year
        assertDate(dp.parse("CAL 29 FEB 2016"), 2016, Calendar.FEBRUARY, 29);
        // 1900 was NOT a leap year, so Feb 29 of that year is parsed as Mar 1.
        assertDate(dp.parse("CAL 29 FEB 1900"), 1900, Calendar.MARCH, 1);

        assertDate(dp.parse("CAL. 17 JUL 2016"), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("CAL. 1 JUL 932"), 932, Calendar.JULY, 1);
        // 2016 was a leap year
        assertDate(dp.parse("CAL. 29 FEB 2016"), 2016, Calendar.FEBRUARY, 29);
        // 1900 was NOT a leap year, so Feb 29 of that year is parsed as Mar 1.
        assertDate(dp.parse("CAL. 29 FEB 1900"), 1900, Calendar.MARCH, 1);

        assertDate(dp.parse("EST 17 JUL 2016"), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("EST 1 JUL 932"), 932, Calendar.JULY, 1);
        // 2016 was a leap year
        assertDate(dp.parse("EST 29 FEB 2016"), 2016, Calendar.FEBRUARY, 29);
        // 1900 was NOT a leap year, so Feb 29 of that year is parsed as Mar 1.
        assertDate(dp.parse("EST 29 FEB 1900"), 1900, Calendar.MARCH, 1);

        assertDate(dp.parse("est. 17 JUL 2016"), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("est. 1 JUL 932"), 932, Calendar.JULY, 1);
        // 2016 was a leap year
        assertDate(dp.parse("est. 29 FEB 2016"), 2016, Calendar.FEBRUARY, 29);
        // 1900 was NOT a leap year, so Feb 29 of that year is parsed as Mar 1.
        assertDate(dp.parse("est. 29 FEB 1900"), 1900, Calendar.MARCH, 1);

    }

    /**
     * Test parsing a single date with month and year but no day, using precise preference
     */
    @Test
    public void testParseApproximateSingleDateNoDayPreferEarliest() {
        DateParser dp = new DateParser();
        assertDate(dp.parse("ABT JUL 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("Abt. JUL 932", ImpreciseDatePreference.FAVOR_EARLIEST), 932, Calendar.JULY, 1);
        assertDate(dp.parse("Appx. JUL 932", ImpreciseDatePreference.FAVOR_EARLIEST), 932, Calendar.JULY, 1);
        assertDate(dp.parse("Approx. JUL 932", ImpreciseDatePreference.FAVOR_EARLIEST), 932, Calendar.JULY, 1);
        assertDate(dp.parse("About JUL 932", ImpreciseDatePreference.FAVOR_EARLIEST), 932, Calendar.JULY, 1);
        // 2016 was a leap year
        assertDate(dp.parse("Est. FEB 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.FEBRUARY, 1);
        // 1900 was NOT a leap year, despite being divisible by 4 - only ever four centuries is a leap year. So Feb 28 would be last
        // day in Feb.
        assertDate(dp.parse("cal feb 1900", ImpreciseDatePreference.FAVOR_EARLIEST), 1900, Calendar.FEBRUARY, 1);
    }

    /**
     * Test parsing a single date with month and year but no day, using precise preference
     */
    @Test
    public void testParseApproximateSingleDateNoDayPreferLatest() {
        DateParser dp = new DateParser();
        assertDate(dp.parse("About JUL 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 31);
        assertDate(dp.parse("Appx jul 932", ImpreciseDatePreference.FAVOR_LATEST), 932, Calendar.JULY, 31);
        // 2016 was a leap year
        assertDate(dp.parse("Cal Feb 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.FEBRUARY, 29);
        // 1900 was NOT a leap year, despite being divisible by 4 - only ever four centuries is a leap year. So Feb 28 would be last
        // day in Feb.
        assertDate(dp.parse("Est. feb 1900", ImpreciseDatePreference.FAVOR_LATEST), 1900, Calendar.FEBRUARY, 28);
    }

    /**
     * Test parsing a single date with month and year but no day, using precise preference
     */
    @Test
    public void testParseApproximateSingleDateNoDayPreferMidpoint() {
        DateParser dp = new DateParser();
        assertDate(dp.parse("Appx JUL 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 15);
        assertDate(dp.parse("APPROX JUL 932", ImpreciseDatePreference.FAVOR_MIDPOINT), 932, Calendar.JULY, 15);
        // 2016 was a leap year
        assertDate(dp.parse("ABT FEB 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.FEBRUARY, 14);
        // 1900 was NOT a leap year, despite being divisible by 4 - only ever four centuries is a leap year. So Feb 28 would be last
        // day in Feb.
        assertDate(dp.parse("CALC FEB 1900", ImpreciseDatePreference.FAVOR_MIDPOINT), 1900, Calendar.FEBRUARY, 14);
    }

    /**
     * Test parsing a single date with month and year but no day, using precise preference
     */
    @Test
    public void testParseApproximateSingleDateNoDayPreferPRECISE() {
        DateParser dp = new DateParser();
        assertDate(dp.parse("ABT JUL 2016", ImpreciseDatePreference.PRECISE), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("Abt. JUL 932", ImpreciseDatePreference.PRECISE), 932, Calendar.JULY, 1);
        assertDate(dp.parse("Appx. JUL 932", ImpreciseDatePreference.PRECISE), 932, Calendar.JULY, 1);
        assertDate(dp.parse("Approx. JUL 932", ImpreciseDatePreference.PRECISE), 932, Calendar.JULY, 1);
        assertDate(dp.parse("About JUL 932", ImpreciseDatePreference.PRECISE), 932, Calendar.JULY, 1);
        // 2016 was a leap year
        assertDate(dp.parse("Est. FEB 2016", ImpreciseDatePreference.PRECISE), 2016, Calendar.FEBRUARY, 1);
        // 1900 was NOT a leap year, despite being divisible by 4 - only ever four centuries is a leap year. So Feb 28 would be last
        // day in Feb.
        assertDate(dp.parse("cal feb 1900", ImpreciseDatePreference.PRECISE), 1900, Calendar.FEBRUARY, 1);
    }

    /**
     * Test parsing a single date with month and year but no day, using precise preference
     */
    @Test
    public void testParseApproximateSingleDateYearOnlyPrecise() {
        DateParser dp = new DateParser();
        assertDate(dp.parse("abt 2016"), 2016, Calendar.JANUARY, 1);
        assertDate(dp.parse("Appx 932"), 932, Calendar.JANUARY, 1);
        assertDate(dp.parse("Est 2016"), 2016, Calendar.JANUARY, 1);
        assertDate(dp.parse("Calc 1900"), 1900, Calendar.JANUARY, 1);
    }

    /**
     * Test parsing a single date with month and year but no day, using precise preference
     */
    @Test
    public void testParseApproximateSingleDateYearOnlyPreferEarliest() {
        DateParser dp = new DateParser();
        assertDate(dp.parse("Calc 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JANUARY, 1);
        assertDate(dp.parse("Est 932", ImpreciseDatePreference.FAVOR_EARLIEST), 932, Calendar.JANUARY, 1);
        assertDate(dp.parse("appx 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JANUARY, 1);
        assertDate(dp.parse("Approx 1900", ImpreciseDatePreference.FAVOR_EARLIEST), 1900, Calendar.JANUARY, 1);
    }

    /**
     * Test parsing a single date with month and year but no day, using precise preference
     */
    @Test
    public void testParseApproximateSingleDateYearOnlyPreferLatest() {
        DateParser dp = new DateParser();
        assertDate(dp.parse("About 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.DECEMBER, 31);
        assertDate(dp.parse("Calc. 932", ImpreciseDatePreference.FAVOR_LATEST), 932, Calendar.DECEMBER, 31);
        assertDate(dp.parse("appx 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.DECEMBER, 31);
        assertDate(dp.parse("Approx. 1900", ImpreciseDatePreference.FAVOR_LATEST), 1900, Calendar.DECEMBER, 31);
    }

    /**
     * Test parsing a single date with month and year but no day, using precise preference
     */
    @Test
    public void testParseApproximateSingleDateYearOnlyPreferMidpoint() {
        DateParser dp = new DateParser();
        assertDate(dp.parse("Est. 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("Est 932", ImpreciseDatePreference.FAVOR_MIDPOINT), 932, Calendar.JULY, 1);
        assertDate(dp.parse("est 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("EST 1900", ImpreciseDatePreference.FAVOR_MIDPOINT), 1900, Calendar.JULY, 1);
    }

    /**
     * Test parsing a single date with month and year but no day, using precise preference
     */
    @Test
    public void testParseInterpretedDates() {
        DateParser dp = new DateParser();
        assertDate(dp.parse("INT 17 JUL 2016"), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("INT 17 JUL 932"), 932, Calendar.JULY, 17);
        assertDate(dp.parse("INT FEB 2016"), 2016, Calendar.FEBRUARY, 1);
        assertDate(dp.parse("INT 1900"), 1900, Calendar.JANUARY, 1);

        assertDate(dp.parse("Int. JUL 2016"), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("Int. JUL 932"), 932, Calendar.JULY, 1);
        assertDate(dp.parse("Int. FEB 2016"), 2016, Calendar.FEBRUARY, 1);
        assertDate(dp.parse("Int. 1900"), 1900, Calendar.JANUARY, 1);

        assertDate(dp.parse("Int. JUL 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("Int. JUL 932", ImpreciseDatePreference.FAVOR_EARLIEST), 932, Calendar.JULY, 1);
        assertDate(dp.parse("Int. FEB 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.FEBRUARY, 1);
        assertDate(dp.parse("Int. 1900", ImpreciseDatePreference.FAVOR_EARLIEST), 1900, Calendar.JANUARY, 1);

        assertDate(dp.parse("Int. JUL 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 31);
        assertDate(dp.parse("Int. JUL 932", ImpreciseDatePreference.FAVOR_LATEST), 932, Calendar.JULY, 31);
        // 2016 was a leap year
        assertDate(dp.parse("Int. FEB 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.FEBRUARY, 29);
        assertDate(dp.parse("Int. 1900", ImpreciseDatePreference.FAVOR_LATEST), 1900, Calendar.DECEMBER, 31);

        assertDate(dp.parse("Int. JUL 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 15);
        assertDate(dp.parse("Int. JUL 932", ImpreciseDatePreference.FAVOR_MIDPOINT), 932, Calendar.JULY, 15);
        assertDate(dp.parse("Int. FEB 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.FEBRUARY, 14);
        assertDate(dp.parse("Int. 1900", ImpreciseDatePreference.FAVOR_MIDPOINT), 1900, Calendar.JULY, 1);
    }

    /**
     * Test parsing a single date with month, day, and year
     */
    @Test
    public void testParseSingleDateFull() {
        DateParser dp = new DateParser();
        assertDate(dp.parse("17 JUL 2016"), 2016, Calendar.JULY, 17);
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
        DateParser dp = new DateParser();
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
        DateParser dp = new DateParser();
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
        DateParser dp = new DateParser();
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
        DateParser dp = new DateParser();
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
        DateParser dp = new DateParser();
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
        DateParser dp = new DateParser();
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
        DateParser dp = new DateParser();
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
        DateParser dp = new DateParser();
        assertDate(dp.parse("2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("932", ImpreciseDatePreference.FAVOR_MIDPOINT), 932, Calendar.JULY, 1);
        assertDate(dp.parse("2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("1900", ImpreciseDatePreference.FAVOR_MIDPOINT), 1900, Calendar.JULY, 1);
    }

    /**
     * Test handling for dates that are not compliant with the GEDCOM spec in their format
     */
    @Test
    public void testParseUnsupportedFormats() {
        DateParser dp = new DateParser();
        assertNull("Slashes not supported", dp.parse("07/17/2016"));
        assertNull("Hyphens not supported", dp.parse("2016-07-17"));
        assertNull("Field order not correct", dp.parse("2016 JUL 17"));
        assertNull("No month has 41 days", dp.parse("41 JUL 2016"));
        assertNull("Two digit years not supported", dp.parse("5 JUL 15"));
    }

    /**
     * Assert that a given date has the expected year, month, and day values
     * 
     * @param d
     *            the date to test
     * @param year
     *            the expected year
     * @param month
     *            the expected month
     * @param day
     *            the expected day
     */
    private void assertDate(Date d, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int y = c.get(Calendar.YEAR);
        assertEquals("Expected year of " + year + " but found " + y + " in date " + d, year, y);
        int m = c.get(Calendar.MONTH);
        assertEquals("Expected month of " + month + " but found " + m + " in date " + d, month, m);
        int dy = c.get(Calendar.DAY_OF_MONTH);
        assertEquals("Expected day of " + day + " but found " + dy + " in date " + d, day, dy);
        assertEquals("Expected no hour in date " + d, 0, c.get(Calendar.HOUR_OF_DAY));
        assertEquals("Expected no minutes in date " + d, 0, c.get(Calendar.MINUTE));
        assertEquals("Expected no seconds in date " + d, 0, c.get(Calendar.SECOND));
        assertEquals("Expected no milliseconds in date " + d, 0, c.get(Calendar.MILLISECOND));
    }

}
