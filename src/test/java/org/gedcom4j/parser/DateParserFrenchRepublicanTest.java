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
 * Test for {@link DateParser} methods dealing with French Republican calendar.
 * 
 * @author frizbog
 */
public class DateParserFrenchRepublicanTest extends AbstractDateParserTest {

    /**
     * Test French Republican Date Period
     */
    @Test
    public void testParseFrenchRepublicanDatePeriod() {
        assertDate(dp.parse("@#DFRENCH R@ FROM 5 THER 224 TO 11 THER 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2016,
                Calendar.JULY, 22);
        assertDate(dp.parse("@#DFRENCH R@ FROM 5 THER 224 TO 11 THER 224", ImpreciseDatePreference.FAVOR_LATEST), 2016,
                Calendar.JULY, 28);
        assertDate(dp.parse("@#DFRENCH R@ FROM 5 THER 224 TO 11 THER 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016,
                Calendar.JULY, 25);
    
        assertDate(dp.parse("@#DFRENCH R@ FROM VEND 4 TO THER 4", ImpreciseDatePreference.FAVOR_EARLIEST), 1795, Calendar.SEPTEMBER,
                23);
        assertDate(dp.parse("@#DFRENCH R@ FROM VEND 4 TO THER 4", ImpreciseDatePreference.FAVOR_LATEST), 1796, Calendar.AUGUST, 17);
        assertDate(dp.parse("@#DFRENCH R@ FROM VEND 4 TO THER 4", ImpreciseDatePreference.FAVOR_MIDPOINT), 1796, Calendar.MARCH, 5);
    
        assertDate(dp.parse("@#DFRENCH R@ FROM 1 TO 5", ImpreciseDatePreference.FAVOR_EARLIEST), 1792, Calendar.SEPTEMBER, 22);
        assertDate(dp.parse("@#DFRENCH R@ FROM 1 TO 5", ImpreciseDatePreference.FAVOR_LATEST), 1797, Calendar.SEPTEMBER, 21);
        assertDate(dp.parse("@#DFRENCH R@ FROM 1 TO 5", ImpreciseDatePreference.FAVOR_MIDPOINT), 1795, Calendar.MARCH, 23);
    }

    /**
     * Test French Republican Date Range
     */
    @Test
    public void testParseFrenchRepublicanDateRange() {
        assertDate(dp.parse("@#DFRENCH R@ BET 5 THER 224 AND 11 THER 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2016,
                Calendar.JULY, 22);
        assertDate(dp.parse("@#DFRENCH R@ BET 5 THER 224 AND 11 THER 224", ImpreciseDatePreference.FAVOR_LATEST), 2016,
                Calendar.JULY, 28);
        assertDate(dp.parse("@#DFRENCH R@ BET 5 THER 224 AND 11 THER 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016,
                Calendar.JULY, 25);
    
        assertDate(dp.parse("@#DFRENCH R@ BET VEND 4 AND THER 4", ImpreciseDatePreference.FAVOR_EARLIEST), 1795, Calendar.SEPTEMBER,
                23);
        assertDate(dp.parse("@#DFRENCH R@ BET VEND 4 AND THER 4", ImpreciseDatePreference.FAVOR_LATEST), 1796, Calendar.AUGUST, 17);
        assertDate(dp.parse("@#DFRENCH R@ BET VEND 4 AND THER 4", ImpreciseDatePreference.FAVOR_MIDPOINT), 1796, Calendar.MARCH, 5);
    
        assertDate(dp.parse("@#DFRENCH R@ BET 1 AND 5", ImpreciseDatePreference.FAVOR_EARLIEST), 1792, Calendar.SEPTEMBER, 22);
        assertDate(dp.parse("@#DFRENCH R@ BET 1 AND 5", ImpreciseDatePreference.FAVOR_LATEST), 1797, Calendar.SEPTEMBER, 21);
        assertDate(dp.parse("@#DFRENCH R@ BET 1 AND 5", ImpreciseDatePreference.FAVOR_MIDPOINT), 1795, Calendar.MARCH, 23);
    }

    /**
     * Test French Republican calendar, with preference to earliest possible date intepretation
     */
    @Test
    public void testParseFrenchRepublicanSingleDateFavorEarliest() {
        assertDate(dp.parse("@#DFRENCH R@ 3 THER 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ THER 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DFRENCH R@ 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2015, Calendar.SEPTEMBER, 22);
    
        assertDate(dp.parse("@#DFRENCH R@ ABT 3 THER 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ EST 3 THER 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ CAL 3 THER 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ BEF 3 THER 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ AFT 3 THER 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ FROM 3 THER 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ TO 3 THER 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ INT 3 THER 224 (Because)", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY,
                20);
    
        assertDate(dp.parse("@#DFRENCH R@ ABT THER 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DFRENCH R@ EST THER 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DFRENCH R@ CAL THER 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DFRENCH R@ BEF THER 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DFRENCH R@ AFT THER 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DFRENCH R@ FROM THER 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DFRENCH R@ TO THER 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DFRENCH R@ INT THER 224 (Because)", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY,
                18);
    
        assertDate(dp.parse("@#DFRENCH R@ ABT 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2015, Calendar.SEPTEMBER, 22);
        assertDate(dp.parse("@#DFRENCH R@ EST 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2015, Calendar.SEPTEMBER, 22);
        assertDate(dp.parse("@#DFRENCH R@ CAL 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2015, Calendar.SEPTEMBER, 22);
        assertDate(dp.parse("@#DFRENCH R@ BEF 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2015, Calendar.SEPTEMBER, 22);
        assertDate(dp.parse("@#DFRENCH R@ AFT 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2015, Calendar.SEPTEMBER, 22);
        assertDate(dp.parse("@#DFRENCH R@ FROM 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2015, Calendar.SEPTEMBER, 22);
        assertDate(dp.parse("@#DFRENCH R@ TO 224", ImpreciseDatePreference.FAVOR_EARLIEST), 2015, Calendar.SEPTEMBER, 22);
        assertDate(dp.parse("@#DFRENCH R@ INT 224 (Because)", ImpreciseDatePreference.FAVOR_EARLIEST), 2015, Calendar.SEPTEMBER,
                22);
    }

    /**
     * Test French Republican calendar, with preference to latest possible date intepreteation
     */
    @Test
    public void testParseFrenchRepublicanSingleDateFavorLatest() {
        assertDate(dp.parse("@#DFRENCH R@ 3 THER 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ THER 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.AUGUST, 16);
        assertDate(dp.parse("@#DFRENCH R@ 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.SEPTEMBER, 21);
    
        assertDate(dp.parse("@#DFRENCH R@ ABT 3 THER 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ EST 3 THER 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ CAL 3 THER 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ BEF 3 THER 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ AFT 3 THER 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ FROM 3 THER 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ TO 3 THER 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ INT 3 THER 224 (Because)", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY,
                20);
    
        assertDate(dp.parse("@#DFRENCH R@ ABT THER 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.AUGUST, 16);
        assertDate(dp.parse("@#DFRENCH R@ EST THER 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.AUGUST, 16);
        assertDate(dp.parse("@#DFRENCH R@ CAL THER 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.AUGUST, 16);
        assertDate(dp.parse("@#DFRENCH R@ BEF THER 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.AUGUST, 16);
        assertDate(dp.parse("@#DFRENCH R@ AFT THER 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.AUGUST, 16);
        assertDate(dp.parse("@#DFRENCH R@ FROM THER 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.AUGUST, 16);
        assertDate(dp.parse("@#DFRENCH R@ TO THER 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.AUGUST, 16);
        assertDate(dp.parse("@#DFRENCH R@ INT THER 224 (Because)", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.AUGUST,
                16);
    
        assertDate(dp.parse("@#DFRENCH R@ ABT 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.SEPTEMBER, 21);
        assertDate(dp.parse("@#DFRENCH R@ EST 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.SEPTEMBER, 21);
        assertDate(dp.parse("@#DFRENCH R@ CAL 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.SEPTEMBER, 21);
        assertDate(dp.parse("@#DFRENCH R@ BEF 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.SEPTEMBER, 21);
        assertDate(dp.parse("@#DFRENCH R@ AFT 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.SEPTEMBER, 21);
        assertDate(dp.parse("@#DFRENCH R@ FROM 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.SEPTEMBER, 21);
        assertDate(dp.parse("@#DFRENCH R@ TO 224", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.SEPTEMBER, 21);
        assertDate(dp.parse("@#DFRENCH R@ INT 224 (Because)", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.SEPTEMBER, 21);
    }

    /**
     * Test French Republican calendar, with preference to latest possible date intepreteation
     */
    @Test
    public void testParseFrenchRepublicanSingleDateFavorMidpoint() {
        assertDate(dp.parse("@#DFRENCH R@ 3 THER 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ THER 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.AUGUST, 1);
        assertDate(dp.parse("@#DFRENCH R@ 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.MARCH, 20);
    
        assertDate(dp.parse("@#DFRENCH R@ ABT 3 THER 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ EST 3 THER 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ CAL 3 THER 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ BEF 3 THER 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ AFT 3 THER 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ FROM 3 THER 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ TO 3 THER 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ INT 3 THER 224 (Because)", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY,
                20);
    
        assertDate(dp.parse("@#DFRENCH R@ ABT THER 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.AUGUST, 1);
        assertDate(dp.parse("@#DFRENCH R@ EST THER 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.AUGUST, 1);
        assertDate(dp.parse("@#DFRENCH R@ CAL THER 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.AUGUST, 1);
        assertDate(dp.parse("@#DFRENCH R@ BEF THER 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.AUGUST, 1);
        assertDate(dp.parse("@#DFRENCH R@ AFT THER 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.AUGUST, 1);
        assertDate(dp.parse("@#DFRENCH R@ FROM THER 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.AUGUST, 1);
        assertDate(dp.parse("@#DFRENCH R@ TO THER 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.AUGUST, 1);
        assertDate(dp.parse("@#DFRENCH R@ INT THER 224 (Because)", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.AUGUST,
                1);
    
        assertDate(dp.parse("@#DFRENCH R@ ABT 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.MARCH, 20);
        assertDate(dp.parse("@#DFRENCH R@ EST 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.MARCH, 20);
        assertDate(dp.parse("@#DFRENCH R@ CAL 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.MARCH, 20);
        assertDate(dp.parse("@#DFRENCH R@ BEF 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.MARCH, 20);
        assertDate(dp.parse("@#DFRENCH R@ AFT 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.MARCH, 20);
        assertDate(dp.parse("@#DFRENCH R@ FROM 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.MARCH, 20);
        assertDate(dp.parse("@#DFRENCH R@ TO 224", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.MARCH, 20);
        assertDate(dp.parse("@#DFRENCH R@ INT 224 (Because)", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.MARCH, 20);
    }

    /**
     * Test French Republican calendar
     */
    @Test
    public void testParseFrenchRepublicanSingleDateNoPref() {
        assertDate(dp.parse("@#DFRENCH R@ 3 THER 224"), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ THER 224"), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DFRENCH R@ 224"), 2015, Calendar.SEPTEMBER, 22);
    
        assertDate(dp.parse("@#DFRENCH R@ ABT 3 THER 224"), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ EST 3 THER 224"), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ CAL 3 THER 224"), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ BEF 3 THER 224"), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ AFT 3 THER 224"), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ FROM 3 THER 224"), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ TO 3 THER 224"), 2016, Calendar.JULY, 20);
        assertDate(dp.parse("@#DFRENCH R@ INT 3 THER 224 (Because)"), 2016, Calendar.JULY, 20);
    
        assertDate(dp.parse("@#DFRENCH R@ ABT THER 224"), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DFRENCH R@ EST THER 224"), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DFRENCH R@ CAL THER 224"), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DFRENCH R@ BEF THER 224"), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DFRENCH R@ AFT THER 224"), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DFRENCH R@ FROM THER 224"), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DFRENCH R@ TO THER 224"), 2016, Calendar.JULY, 18);
        assertDate(dp.parse("@#DFRENCH R@ INT THER 224 (Because)"), 2016, Calendar.JULY, 18);
    
        assertDate(dp.parse("@#DFRENCH R@ ABT 224"), 2015, Calendar.SEPTEMBER, 22);
        assertDate(dp.parse("@#DFRENCH R@ EST 224"), 2015, Calendar.SEPTEMBER, 22);
        assertDate(dp.parse("@#DFRENCH R@ CAL 224"), 2015, Calendar.SEPTEMBER, 22);
        assertDate(dp.parse("@#DFRENCH R@ BEF 224"), 2015, Calendar.SEPTEMBER, 22);
        assertDate(dp.parse("@#DFRENCH R@ AFT 224"), 2015, Calendar.SEPTEMBER, 22);
        assertDate(dp.parse("@#DFRENCH R@ FROM 224"), 2015, Calendar.SEPTEMBER, 22);
        assertDate(dp.parse("@#DFRENCH R@ TO 224"), 2015, Calendar.SEPTEMBER, 22);
        assertDate(dp.parse("@#DFRENCH R@ INT 224 (Because)"), 2015, Calendar.SEPTEMBER, 22);
    }

}
