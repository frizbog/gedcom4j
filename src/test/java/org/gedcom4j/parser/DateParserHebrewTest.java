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
 * Test for {@link DateParser} methods dealing with Hebrew calendar.
 * 
 * @author frizbog
 */
public class DateParserHebrewTest extends AbstractDateParserTest {

    /**
     * Test Hebrew Date Period
     */
    @Test
    public void testParseHebrewDatePeriod() {
        assertDate(dp.parse("@#DHEBREW@ FROM 12 TMZ 5776 TO 24 TMZ 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2016,
                Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ FROM 12 TMZ 5776 TO 24 TMZ 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016,
                Calendar.JULY, 24);
        assertDate(dp.parse("@#DHEBREW@ FROM 12 TMZ 5776 TO 24 TMZ 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016,
                Calendar.JULY, 30);

        assertDate(dp.parse("@#DHEBREW@ FROM TMZ 5776 TO AAV 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY,
                7);
        assertDate(dp.parse("@#DHEBREW@ FROM TMZ 5776 TO AAV 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.AUGUST,
                5);
        assertDate(dp.parse("@#DHEBREW@ FROM TMZ 5776 TO AAV 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.SEPTEMBER,
                3);

        assertDate(dp.parse("@#DHEBREW@ FROM 5776 TO 5777", ImpreciseDatePreference.FAVOR_EARLIEST), 2015, Calendar.SEPTEMBER, 14);
        assertDate(dp.parse("@#DHEBREW@ FROM 5776 TO 5777", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.SEPTEMBER, 16);
        assertDate(dp.parse("@#DHEBREW@ FROM 5776 TO 5777", ImpreciseDatePreference.FAVOR_LATEST), 2017, Calendar.SEPTEMBER, 20);
    }

    /**
     * Test Hebrew Date Range
     */
    @Test
    public void testParseHebrewDateRange() {
        assertDate(dp.parse("@#DHEBREW@ BET 12 TMZ 5776 AND 24 TMZ 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2016,
                Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ BET 12 TMZ 5776 AND 24 TMZ 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016,
                Calendar.JULY, 24);
        assertDate(dp.parse("@#DHEBREW@ BET 12 TMZ 5776 AND 24 TMZ 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016,
                Calendar.JULY, 30);

        assertDate(dp.parse("@#DHEBREW@ BET TMZ 5776 AND AAV 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY,
                7);
        assertDate(dp.parse("@#DHEBREW@ BET TMZ 5776 AND AAV 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.AUGUST,
                5);
        assertDate(dp.parse("@#DHEBREW@ BET TMZ 5776 AND AAV 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.SEPTEMBER,
                3);

        assertDate(dp.parse("@#DHEBREW@ BET 5776 AND 5777", ImpreciseDatePreference.FAVOR_EARLIEST), 2015, Calendar.SEPTEMBER, 14);
        assertDate(dp.parse("@#DHEBREW@ BET 5776 AND 5777", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.SEPTEMBER, 16);
        assertDate(dp.parse("@#DHEBREW@ BET 5776 AND 5777", ImpreciseDatePreference.FAVOR_LATEST), 2017, Calendar.SEPTEMBER, 20);
    }

    /**
     * Test Hebrew calendar, parsing single dates (including approximate ones), with preference for earlier dates when imprecise
     */
    @Test
    public void testParseHebrewSingleDatesFavorEarliest() {
        assertDate(dp.parse("@#DHEBREW@ 12 TMZ 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ TMZ 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 7);
        assertDate(dp.parse("@#DHEBREW@ 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2015, Calendar.SEPTEMBER, 14);

        assertDate(dp.parse("@#DHEBREW@ ABT 12 TMZ 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ EST 12 TMZ 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ CAL 12 TMZ 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ BEF 12 TMZ 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ AFT 12 TMZ 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ FROM 12 TMZ 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ TO 12 TMZ 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ INT 12 TMZ 5776 (Because)", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY,
                18);

        assertDate(dp.parse("@#DHEBREW@ ABT TMZ 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 7);
        assertDate(dp.parse("@#DHEBREW@ EST TMZ 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 7);
        assertDate(dp.parse("@#DHEBREW@ CAL TMZ 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 7);
        assertDate(dp.parse("@#DHEBREW@ BEF TMZ 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 7);
        assertDate(dp.parse("@#DHEBREW@ AFT TMZ 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 7);
        assertDate(dp.parse("@#DHEBREW@ FROM TMZ 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 7);
        assertDate(dp.parse("@#DHEBREW@ TO TMZ 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 7);
        assertDate(dp.parse("@#DHEBREW@ INT TMZ 5776 (Because)", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 7);

        assertDate(dp.parse("@#DHEBREW@ ABT 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2015, Calendar.SEPTEMBER, 14);
        assertDate(dp.parse("@#DHEBREW@ EST 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2015, Calendar.SEPTEMBER, 14);
        assertDate(dp.parse("@#DHEBREW@ CAL 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2015, Calendar.SEPTEMBER, 14);
        assertDate(dp.parse("@#DHEBREW@ BEF 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2015, Calendar.SEPTEMBER, 14);
        assertDate(dp.parse("@#DHEBREW@ AFT 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2015, Calendar.SEPTEMBER, 14);
        assertDate(dp.parse("@#DHEBREW@ FROM 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2015, Calendar.SEPTEMBER, 14);
        assertDate(dp.parse("@#DHEBREW@ TO 5776", ImpreciseDatePreference.FAVOR_EARLIEST), 2015, Calendar.SEPTEMBER, 14);
        assertDate(dp.parse("@#DHEBREW@ INT 5776 (Because)", ImpreciseDatePreference.FAVOR_EARLIEST), 2015, Calendar.SEPTEMBER, 14);
    }

    /**
     * Test Hebrew calendar, parsing single dates (including approximate ones), with preference for later dates when imprecise
     */
    @Test
    public void testParseHebrewSingleDatesFavorLatest() {
        assertDate(dp.parse("@#DHEBREW@ 12 TMZ 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ TMZ 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.AUGUST, 4);
        assertDate(dp.parse("@#DHEBREW@ 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.OCTOBER, 2);

        assertDate(dp.parse("@#DHEBREW@ ABT 12 TMZ 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ EST 12 TMZ 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ CAL 12 TMZ 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ BEF 12 TMZ 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ AFT 12 TMZ 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ FROM 12 TMZ 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ TO 12 TMZ 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ INT 12 TMZ 5776 (Because)", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 18);

        assertDate(dp.parse("@#DHEBREW@ ABT TMZ 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.AUGUST, 4);
        assertDate(dp.parse("@#DHEBREW@ EST TMZ 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.AUGUST, 4);
        assertDate(dp.parse("@#DHEBREW@ CAL TMZ 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.AUGUST, 4);
        assertDate(dp.parse("@#DHEBREW@ BEF TMZ 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.AUGUST, 4);
        assertDate(dp.parse("@#DHEBREW@ AFT TMZ 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.AUGUST, 4);
        assertDate(dp.parse("@#DHEBREW@ FROM TMZ 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.AUGUST, 4);
        assertDate(dp.parse("@#DHEBREW@ TO TMZ 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.AUGUST, 4);
        assertDate(dp.parse("@#DHEBREW@ INT TMZ 5776  (Because)", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.AUGUST, 4);

        assertDate(dp.parse("@#DHEBREW@ ABT 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.OCTOBER, 2);
        assertDate(dp.parse("@#DHEBREW@ EST 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.OCTOBER, 2);
        assertDate(dp.parse("@#DHEBREW@ CAL 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.OCTOBER, 2);
        assertDate(dp.parse("@#DHEBREW@ BEF 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.OCTOBER, 2);
        assertDate(dp.parse("@#DHEBREW@ AFT 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.OCTOBER, 2);
        assertDate(dp.parse("@#DHEBREW@ FROM 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.OCTOBER, 2);
        assertDate(dp.parse("@#DHEBREW@ TO 5776", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.OCTOBER, 2);
        assertDate(dp.parse("@#DHEBREW@ INT 5776 (Because)", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.OCTOBER, 2);
    }

    /**
     * Test Hebrew calendar, parsing single dates (including approximate ones), with preference for later dates when imprecise
     */
    @Test
    public void testParseHebrewSingleDatesFavorMidpoint() {
        assertDate(dp.parse("@#DHEBREW@ 12 TMZ 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ TMZ 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DHEBREW@ 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.MARCH, 24);

        assertDate(dp.parse("@#DHEBREW@ ABT 12 TMZ 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ EST 12 TMZ 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ CAL 12 TMZ 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ BEF 12 TMZ 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ AFT 12 TMZ 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ FROM 12 TMZ 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ TO 12 TMZ 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ INT 12 TMZ 5776 (Because)", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY,
                18);

        assertDate(dp.parse("@#DHEBREW@ ABT TMZ 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DHEBREW@ EST TMZ 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DHEBREW@ CAL TMZ 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DHEBREW@ BEF TMZ 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DHEBREW@ AFT TMZ 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DHEBREW@ FROM TMZ 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DHEBREW@ TO TMZ 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DHEBREW@ INT TMZ 5776  (Because)", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 20);

        assertDate(dp.parse("@#DHEBREW@ ABT 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.MARCH, 24);
        assertDate(dp.parse("@#DHEBREW@ EST 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.MARCH, 24);
        assertDate(dp.parse("@#DHEBREW@ CAL 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.MARCH, 24);
        assertDate(dp.parse("@#DHEBREW@ BEF 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.MARCH, 24);
        assertDate(dp.parse("@#DHEBREW@ AFT 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.MARCH, 24);
        assertDate(dp.parse("@#DHEBREW@ FROM 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.MARCH, 24);
        assertDate(dp.parse("@#DHEBREW@ TO 5776", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.MARCH, 24);
        assertDate(dp.parse("@#DHEBREW@ INT 5776 (Because)", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.MARCH, 24);
    }

    /**
     * Test Hebrew calendar, parsing single dates (including approximate ones), with no preference to handling imprecise dates
     */
    @Test
    public void testParseHebrewSingleDatesNoPref() {
        assertDate(dp.parse("@#DHEBREW@ 12 TMZ 5776"), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ TMZ 5776"), 2016, Calendar.JULY, 7);
        assertDate(dp.parse("@#DHEBREW@ 5776"), 2015, Calendar.SEPTEMBER, 14);

        assertDate(dp.parse("@#DHEBREW@ ABT 12 TMZ 5776"), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ EST 12 TMZ 5776"), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ CAL 12 TMZ 5776"), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ BEF 12 TMZ 5776"), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ AFT 12 TMZ 5776"), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ FROM 12 TMZ 5776"), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ TO 12 TMZ 5776"), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DHEBREW@ INT 12 TMZ 5776 (Because)"), 2016, Calendar.JULY, 18);

        assertDate(dp.parse("@#DHEBREW@ ABT TMZ 5776"), 2016, Calendar.JULY, 7);
        assertDate(dp.parse("@#DHEBREW@ EST TMZ 5776"), 2016, Calendar.JULY, 7);
        assertDate(dp.parse("@#DHEBREW@ CAL TMZ 5776"), 2016, Calendar.JULY, 7);
        assertDate(dp.parse("@#DHEBREW@ BEF TMZ 5776"), 2016, Calendar.JULY, 7);
        assertDate(dp.parse("@#DHEBREW@ AFT TMZ 5776"), 2016, Calendar.JULY, 7);
        assertDate(dp.parse("@#DHEBREW@ FROM TMZ 5776"), 2016, Calendar.JULY, 7);
        assertDate(dp.parse("@#DHEBREW@ TO TMZ 5776"), 2016, Calendar.JULY, 7);
        assertDate(dp.parse("@#DHEBREW@ INT TMZ 5776 (Because)"), 2016, Calendar.JULY, 7);

        assertDate(dp.parse("@#DHEBREW@ ABT 5776"), 2015, Calendar.SEPTEMBER, 14);
        assertDate(dp.parse("@#DHEBREW@ EST 5776"), 2015, Calendar.SEPTEMBER, 14);
        assertDate(dp.parse("@#DHEBREW@ CAL 5776"), 2015, Calendar.SEPTEMBER, 14);
        assertDate(dp.parse("@#DHEBREW@ BEF 5776"), 2015, Calendar.SEPTEMBER, 14);
        assertDate(dp.parse("@#DHEBREW@ AFT 5776"), 2015, Calendar.SEPTEMBER, 14);
        assertDate(dp.parse("@#DHEBREW@ FROM 5776"), 2015, Calendar.SEPTEMBER, 14);
        assertDate(dp.parse("@#DHEBREW@ TO 5776"), 2015, Calendar.SEPTEMBER, 14);
        assertDate(dp.parse("@#DHEBREW@ INT 5776 (Because)"), 2015, Calendar.SEPTEMBER, 14);
    }

}
