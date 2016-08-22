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
 * Test for {@link DateParser} methods that deal with ranges and periods of Gregorian dates.
 * 
 * @author frizbog
 */
public class DateParserRangePeriodTest extends AbstractDateParserTest {

    /**
     * Test ranges
     */
    @Test
    public void testPeriodFullDates() {
        assertDate(dp.parse("FROM 1 DEC 2017 TO 31 DEC 2017"), 2017, Calendar.DECEMBER, 1);
        assertDate(dp.parse("FROM 1 DEC 2017 TO 31 DEC 2017", ImpreciseDatePreference.FAVOR_EARLIEST), 2017, Calendar.DECEMBER, 1);
        assertDate(dp.parse("FROM 1 DEC 2017 TO 31 DEC 2017", ImpreciseDatePreference.FAVOR_LATEST), 2017, Calendar.DECEMBER, 31);
        assertDate(dp.parse("FROM 1 DEC 2017 TO 31 DEC 2017", ImpreciseDatePreference.FAVOR_MIDPOINT), 2017, Calendar.DECEMBER, 16);
    
        assertDate(dp.parse("FROM 28 DEC 2017 TO 31 DEC 2017"), 2017, Calendar.DECEMBER, 28);
        assertDate(dp.parse("FROM 28 DEC 2017 TO 31 DEC 2017", ImpreciseDatePreference.FAVOR_EARLIEST), 2017, Calendar.DECEMBER,
                28);
        assertDate(dp.parse("FROM 28 DEC 2017 TO 31 DEC 2017", ImpreciseDatePreference.FAVOR_LATEST), 2017, Calendar.DECEMBER, 31);
        assertDate(dp.parse("FROM 28 DEC 2017 TO 31 DEC 2017", ImpreciseDatePreference.FAVOR_MIDPOINT), 2017, Calendar.DECEMBER,
                29);
    
    }

    /**
     * Test ranges of dates with a variety of mixed specificity (some dates with year/month/day, some with just year/month, some
     * with just year)
     */
    @Test
    public void testPeriodMixed() {
        assertDate(dp.parse("FROM 2014 TO 1 DEC 2017"), 2014, Calendar.JANUARY, 1);
        assertDate(dp.parse("FROM 2014 TO 1 DEC 2017", ImpreciseDatePreference.FAVOR_EARLIEST), 2014, Calendar.JANUARY, 1);
        assertDate(dp.parse("FROM 2014 TO 1 DEC 2017", ImpreciseDatePreference.FAVOR_LATEST), 2017, Calendar.DECEMBER, 1);
        assertDate(dp.parse("FROM 2014 TO 1 DEC 2017", ImpreciseDatePreference.FAVOR_MIDPOINT), 2015, Calendar.DECEMBER, 17);
    
        assertDate(dp.parse("FROM 1 FEB 2014 TO 2017"), 2014, Calendar.FEBRUARY, 1);
        assertDate(dp.parse("FROM 1 FEB 2014 TO 2017", ImpreciseDatePreference.FAVOR_EARLIEST), 2014, Calendar.FEBRUARY, 1);
        assertDate(dp.parse("FROM 1 FEB 2014 TO 2017", ImpreciseDatePreference.FAVOR_LATEST), 2017, Calendar.DECEMBER, 31);
        assertDate(dp.parse("FROM 1 FEB 2014 TO 2017", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JANUARY, 16);
    
        assertDate(dp.parse("FROM 1 FEB 2014 TO OCT 2017"), 2014, Calendar.FEBRUARY, 1);
        assertDate(dp.parse("FROM 1 FEB 2014 TO OCT 2017", ImpreciseDatePreference.FAVOR_EARLIEST), 2014, Calendar.FEBRUARY, 1);
        assertDate(dp.parse("FROM 1 FEB 2014 TO OCT 2017", ImpreciseDatePreference.FAVOR_LATEST), 2017, Calendar.OCTOBER, 31);
        assertDate(dp.parse("FROM 1 FEB 2014 TO OCT 2017", ImpreciseDatePreference.FAVOR_MIDPOINT), 2015, Calendar.DECEMBER, 17);
    
        assertDate(dp.parse("FROM 28 FEB 2014 TO OCT 2017"), 2014, Calendar.FEBRUARY, 28);
        assertDate(dp.parse("FROM 28 FEB 2014 TO OCT 2017", ImpreciseDatePreference.FAVOR_EARLIEST), 2014, Calendar.FEBRUARY, 28);
        assertDate(dp.parse("FROM 28 FEB 2014 TO OCT 2017", ImpreciseDatePreference.FAVOR_LATEST), 2017, Calendar.OCTOBER, 31);
        assertDate(dp.parse("FROM 28 FEB 2014 TO OCT 2017", ImpreciseDatePreference.FAVOR_MIDPOINT), 2015, Calendar.DECEMBER, 30);
    }

    /**
     * Test ranges of month and year dates without days
     */
    @Test
    public void testPeriodMonthYear() {
        assertDate(dp.parse("FROM JAN 2017 TO DEC 2017"), 2017, Calendar.JANUARY, 1);
        assertDate(dp.parse("FROM JAN 2017 TO DEC 2017", ImpreciseDatePreference.FAVOR_EARLIEST), 2017, Calendar.JANUARY, 1);
        assertDate(dp.parse("FROM JAN 2017 TO DEC 2017", ImpreciseDatePreference.FAVOR_LATEST), 2017, Calendar.DECEMBER, 31);
        assertDate(dp.parse("FROM JAN 2017 TO DEC 2017", ImpreciseDatePreference.FAVOR_MIDPOINT), 2017, Calendar.JULY, 2);
    
        assertDate(dp.parse("FROM FEB 2014 TO OCT 2017"), 2014, Calendar.FEBRUARY, 1);
        assertDate(dp.parse("FROM FEB 2014 TO OCT 2017", ImpreciseDatePreference.FAVOR_EARLIEST), 2014, Calendar.FEBRUARY, 1);
        assertDate(dp.parse("FROM FEB 2014 TO OCT 2017", ImpreciseDatePreference.FAVOR_LATEST), 2017, Calendar.OCTOBER, 31);
        assertDate(dp.parse("FROM FEB 2014 TO OCT 2017", ImpreciseDatePreference.FAVOR_MIDPOINT), 2015, Calendar.DECEMBER, 17);
    }

    /**
     * Test periods with only one date
     */
    @Test
    public void testPeriodOpenEnded() {
        assertDate(dp.parse("FROM 17 JUL 2016", ImpreciseDatePreference.PRECISE), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("FROM. 17 JUL 2016", ImpreciseDatePreference.PRECISE), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("FROM JUL 2016", ImpreciseDatePreference.PRECISE), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("FROM 2016", ImpreciseDatePreference.PRECISE), 2016, Calendar.JANUARY, 1);
        assertDate(dp.parse("TO 17 JUL 2016", ImpreciseDatePreference.PRECISE), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("TO. 17 JUL 2016", ImpreciseDatePreference.PRECISE), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("TO JUL 2016", ImpreciseDatePreference.PRECISE), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("TO 2016", ImpreciseDatePreference.PRECISE), 2016, Calendar.JANUARY, 1);
    
        assertDate(dp.parse("FROM 17 JUL 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("FROM. 17 JUL 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("FROM JUL 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("FROM 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JANUARY, 1);
        assertDate(dp.parse("TO 17 JUL 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("TO. 17 JUL 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("TO JUL 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("TO 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JANUARY, 1);
    
        assertDate(dp.parse("FROM 17 JUL 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("FROM. 17 JUL 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("FROM JUL 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 31);
        assertDate(dp.parse("FROM 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.DECEMBER, 31);
        assertDate(dp.parse("TO 17 JUL 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("TO. 17 JUL 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("TO JUL 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 31);
        assertDate(dp.parse("TO 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.DECEMBER, 31);
    
        assertDate(dp.parse("FROM 17 JUL 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("FROM. 17 JUL 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("FROM JUL 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 15);
        assertDate(dp.parse("FROM 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("TO 17 JUL 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("TO. 17 JUL 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("TO JUL 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 15);
        assertDate(dp.parse("TO 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 1);
    }

    /**
     * Test ranges of dates with only years, no months or days
     */
    @Test
    public void testPeriodYearOnly() {
        assertDate(dp.parse("FROM 2014 TO 2017"), 2014, Calendar.JANUARY, 1);
        assertDate(dp.parse("FROM 2014 TO 2017", ImpreciseDatePreference.FAVOR_EARLIEST), 2014, Calendar.JANUARY, 1);
        assertDate(dp.parse("FROM 2014 TO 2017", ImpreciseDatePreference.FAVOR_LATEST), 2017, Calendar.DECEMBER, 31);
        assertDate(dp.parse("FROM 2014 TO 2017", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JANUARY, 1);
        assertDate(dp.parse("FROM 2013 TO 2017", ImpreciseDatePreference.FAVOR_MIDPOINT), 2015, Calendar.JULY, 2);
    }

    /**
     * Test ranges
     */
    @Test
    public void testRangeFullDates() {
        assertDate(dp.parse("BET 1 DEC 2017 AND 31 DEC 2017"), 2017, Calendar.DECEMBER, 1);
        assertDate(dp.parse("BET 1 DEC 2017 AND 31 DEC 2017", ImpreciseDatePreference.FAVOR_EARLIEST), 2017, Calendar.DECEMBER, 1);
        assertDate(dp.parse("BET 1 DEC 2017 AND 31 DEC 2017", ImpreciseDatePreference.FAVOR_LATEST), 2017, Calendar.DECEMBER, 31);
        assertDate(dp.parse("BET 1 DEC 2017 AND 31 DEC 2017", ImpreciseDatePreference.FAVOR_MIDPOINT), 2017, Calendar.DECEMBER, 16);
    
        assertDate(dp.parse("BET 28 DEC 2017 AND 31 DEC 2017"), 2017, Calendar.DECEMBER, 28);
        assertDate(dp.parse("BET 28 DEC 2017 AND 31 DEC 2017", ImpreciseDatePreference.FAVOR_EARLIEST), 2017, Calendar.DECEMBER,
                28);
        assertDate(dp.parse("BET 28 DEC 2017 AND 31 DEC 2017", ImpreciseDatePreference.FAVOR_LATEST), 2017, Calendar.DECEMBER, 31);
        assertDate(dp.parse("BET 28 DEC 2017 AND 31 DEC 2017", ImpreciseDatePreference.FAVOR_MIDPOINT), 2017, Calendar.DECEMBER,
                29);
    
    }

    /**
     * Test ranges of dates with a variety of mixed specificity (some dates with year/month/day, some with just year/month, some
     * with just year)
     */
    @Test
    public void testRangeMixed() {
        assertDate(dp.parse("BET 2014 AND 1 DEC 2017"), 2014, Calendar.JANUARY, 1);
        assertDate(dp.parse("BET 2014 AND 1 DEC 2017", ImpreciseDatePreference.FAVOR_EARLIEST), 2014, Calendar.JANUARY, 1);
        assertDate(dp.parse("BET 2014 AND 1 DEC 2017", ImpreciseDatePreference.FAVOR_LATEST), 2017, Calendar.DECEMBER, 1);
        assertDate(dp.parse("BET 2014 AND 1 DEC 2017", ImpreciseDatePreference.FAVOR_MIDPOINT), 2015, Calendar.DECEMBER, 17);
    
        assertDate(dp.parse("BET 1 FEB 2014 AND 2017"), 2014, Calendar.FEBRUARY, 1);
        assertDate(dp.parse("BET 1 FEB 2014 AND 2017", ImpreciseDatePreference.FAVOR_EARLIEST), 2014, Calendar.FEBRUARY, 1);
        assertDate(dp.parse("BET 1 FEB 2014 AND 2017", ImpreciseDatePreference.FAVOR_LATEST), 2017, Calendar.DECEMBER, 31);
        assertDate(dp.parse("BET 1 FEB 2014 AND 2017", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JANUARY, 16);
    
        assertDate(dp.parse("BET 1 FEB 2014 AND OCT 2017"), 2014, Calendar.FEBRUARY, 1);
        assertDate(dp.parse("BET 1 FEB 2014 AND OCT 2017", ImpreciseDatePreference.FAVOR_EARLIEST), 2014, Calendar.FEBRUARY, 1);
        assertDate(dp.parse("BET 1 FEB 2014 AND OCT 2017", ImpreciseDatePreference.FAVOR_LATEST), 2017, Calendar.OCTOBER, 31);
        assertDate(dp.parse("BET 1 FEB 2014 AND OCT 2017", ImpreciseDatePreference.FAVOR_MIDPOINT), 2015, Calendar.DECEMBER, 17);
    
        assertDate(dp.parse("BET 28 FEB 2014 AND OCT 2017"), 2014, Calendar.FEBRUARY, 28);
        assertDate(dp.parse("BET 28 FEB 2014 AND OCT 2017", ImpreciseDatePreference.FAVOR_EARLIEST), 2014, Calendar.FEBRUARY, 28);
        assertDate(dp.parse("BET 28 FEB 2014 AND OCT 2017", ImpreciseDatePreference.FAVOR_LATEST), 2017, Calendar.OCTOBER, 31);
        assertDate(dp.parse("BET 28 FEB 2014 AND OCT 2017", ImpreciseDatePreference.FAVOR_MIDPOINT), 2015, Calendar.DECEMBER, 30);
    }

    /**
     * Test ranges of month and year dates without days
     */
    @Test
    public void testRangeMonthYear() {
        assertDate(dp.parse("BTW JAN 2017 AND DEC 2017"), 2017, Calendar.JANUARY, 1);
        assertDate(dp.parse("BTW JAN 2017 AND DEC 2017", ImpreciseDatePreference.FAVOR_EARLIEST), 2017, Calendar.JANUARY, 1);
        assertDate(dp.parse("BTW JAN 2017 AND DEC 2017", ImpreciseDatePreference.FAVOR_LATEST), 2017, Calendar.DECEMBER, 31);
        assertDate(dp.parse("BTW JAN 2017 AND DEC 2017", ImpreciseDatePreference.FAVOR_MIDPOINT), 2017, Calendar.JULY, 2);
    
        assertDate(dp.parse("BTW FEB 2014 AND OCT 2017"), 2014, Calendar.FEBRUARY, 1);
        assertDate(dp.parse("BTW FEB 2014 AND OCT 2017", ImpreciseDatePreference.FAVOR_EARLIEST), 2014, Calendar.FEBRUARY, 1);
        assertDate(dp.parse("BTW FEB 2014 AND OCT 2017", ImpreciseDatePreference.FAVOR_LATEST), 2017, Calendar.OCTOBER, 31);
        assertDate(dp.parse("BTW FEB 2014 AND OCT 2017", ImpreciseDatePreference.FAVOR_MIDPOINT), 2015, Calendar.DECEMBER, 17);
    }

    /**
     * Test ranges with only one date
     */
    @Test
    public void testRangeOpenEnded() {
        assertDate(dp.parse("BEF 17 JUL 2016", ImpreciseDatePreference.PRECISE), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("BEF. 17 JUL 2016", ImpreciseDatePreference.PRECISE), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("BEFORE JUL 2016", ImpreciseDatePreference.PRECISE), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("BEFORE 2016", ImpreciseDatePreference.PRECISE), 2016, Calendar.JANUARY, 1);
        assertDate(dp.parse("AFT 17 JUL 2016", ImpreciseDatePreference.PRECISE), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("AFT. 17 JUL 2016", ImpreciseDatePreference.PRECISE), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("AFTER JUL 2016", ImpreciseDatePreference.PRECISE), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("AFTER 2016", ImpreciseDatePreference.PRECISE), 2016, Calendar.JANUARY, 1);
    
        assertDate(dp.parse("BEF 17 JUL 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("BEF. 17 JUL 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("BEFORE JUL 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("BEFORE 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JANUARY, 1);
        assertDate(dp.parse("AFT 17 JUL 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("AFT. 17 JUL 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("AFTER JUL 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("AFTER 2016", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JANUARY, 1);
    
        assertDate(dp.parse("BEF 17 JUL 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("BEF. 17 JUL 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("BEFORE JUL 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 31);
        assertDate(dp.parse("BEFORE 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.DECEMBER, 31);
        assertDate(dp.parse("AFT 17 JUL 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("AFT. 17 JUL 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("AFTER JUL 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 31);
        assertDate(dp.parse("AFTER 2016", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.DECEMBER, 31);
    
        assertDate(dp.parse("BEF 17 JUL 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("BEF. 17 JUL 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("BEFORE JUL 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 15);
        assertDate(dp.parse("BEFORE 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("AFT 17 JUL 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("AFT. 17 JUL 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("AFTER JUL 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 15);
        assertDate(dp.parse("AFTER 2016", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 1);
    }

    /**
     * Test ranges of dates with only years, no months or days
     */
    @Test
    public void testRangeYearOnly() {
        assertDate(dp.parse("BET 2014 AND 2017"), 2014, Calendar.JANUARY, 1);
        assertDate(dp.parse("BET 2014 AND 2017", ImpreciseDatePreference.FAVOR_EARLIEST), 2014, Calendar.JANUARY, 1);
        assertDate(dp.parse("BET 2014 AND 2017", ImpreciseDatePreference.FAVOR_LATEST), 2017, Calendar.DECEMBER, 31);
        assertDate(dp.parse("BET 2014 AND 2017", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JANUARY, 1);
        assertDate(dp.parse("BET 2013 AND 2017", ImpreciseDatePreference.FAVOR_MIDPOINT), 2015, Calendar.JULY, 2);
    }

}
