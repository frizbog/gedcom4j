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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.gedcom4j.parser.DateParser.ImpreciseDatePreference;
import org.junit.Test;

/**
 * Test case for {@link DateParser}.
 * 
 * @author frizbog
 */
public class DateParserTest extends AbstractDateParserTest {

    /**
     * Test BC Dates
     */
    @Test
    public void testBC() {
        SimpleDateFormat sdf = new SimpleDateFormat("y G", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date d;

        d = dp.parse("1905 BC");
        assertNotNull(d);
        assertEquals("1905 BC", sdf.format(d));

        d = dp.parse("1905");
        assertNotNull(d);
        assertEquals("1905 AD", sdf.format(d));

        d = dp.parse("500 BC");
        assertNotNull(d);
        assertEquals("500 BC", sdf.format(d));

        d = dp.parse("500");
        assertNotNull(d);
        assertEquals("500 AD", sdf.format(d));

        d = dp.parse("50 BC");
        assertNotNull(d);
        assertEquals("50 BC", sdf.format(d));

        d = dp.parse("50");
        assertNotNull(d);
        assertEquals("50 AD", sdf.format(d));

        d = dp.parse("5 BC");
        assertNotNull(d);
        assertEquals("5 BC", sdf.format(d));

        d = dp.parse("5");
        assertNotNull(d);
        assertEquals("5 AD", sdf.format(d));

        d = dp.parse("1");
        assertNotNull(d);
        assertEquals("1 AD", sdf.format(d));

        d = dp.parse("1 BC");
        assertNotNull(d);
        assertEquals("1 BC", sdf.format(d));

    }

    /**
     * Test for {@link DateParser#formatBC(String)}
     */
    @Test
    public void testFormatBC() {
        assertEquals("14 DEC -2049", dp.formatBC("14 DEC 2050BC"));
        assertEquals("DEC -2049", dp.formatBC("DEC 2050BC"));
        assertEquals("-2049", dp.formatBC("2050BC"));
        assertEquals("14 DEC -649", dp.formatBC("14 DEC 650BC"));
        assertEquals("DEC -649", dp.formatBC("DEC 650BC"));
        assertEquals("-649", dp.formatBC("650BC"));
        assertEquals("14 DEC -49", dp.formatBC("14 DEC 50BC"));
        assertEquals("DEC -49", dp.formatBC("DEC 50BC"));
        assertEquals("-49", dp.formatBC("50BC"));

        assertEquals("14 DEC -2049", dp.formatBC("14 DEC 2050 BC"));
        assertEquals("DEC -2049", dp.formatBC("DEC 2050 BC"));
        assertEquals("-2049", dp.formatBC("2050 BC"));
        assertEquals("14 DEC -649", dp.formatBC("14 DEC 650 BC"));
        assertEquals("DEC -649", dp.formatBC("DEC 650 BC"));
        assertEquals("-649", dp.formatBC("650 BC"));
        assertEquals("14 DEC -49", dp.formatBC("14 DEC 50 BC"));
        assertEquals("DEC -49", dp.formatBC("DEC 50 BC"));
        assertEquals("-49", dp.formatBC("50 BC"));

        assertEquals("14 DEC -2049", dp.formatBC("14 DEC 2050 B.C."));
        assertEquals("DEC -2049", dp.formatBC("DEC 2050 B.C."));
        assertEquals("-2049", dp.formatBC("2050 B.C."));
        assertEquals("14 DEC -649", dp.formatBC("14 DEC 650 B.C."));
        assertEquals("DEC -649", dp.formatBC("DEC 650 B.C."));
        assertEquals("-649", dp.formatBC("650 B.C."));
        assertEquals("14 DEC -49", dp.formatBC("14 DEC 50 B.C."));
        assertEquals("DEC -49", dp.formatBC("DEC 50 B.C."));
        assertEquals("-49", dp.formatBC("50 B.C."));

        assertEquals("14 DEC -2049", dp.formatBC("14 DEC 2050 BCE"));
        assertEquals("DEC -2049", dp.formatBC("DEC 2050 BCE"));
        assertEquals("-2049", dp.formatBC("2050 BCE"));
        assertEquals("14 DEC -649", dp.formatBC("14 DEC 650 BCE"));
        assertEquals("DEC -649", dp.formatBC("DEC 650 BCE"));
        assertEquals("-649", dp.formatBC("650 BCE"));
        assertEquals("14 DEC -49", dp.formatBC("14 DEC 50 BCE"));
        assertEquals("DEC -49", dp.formatBC("DEC 50 BCE"));
        assertEquals("-49", dp.formatBC("50 BCE"));

        assertEquals("14 DEC -2049", dp.formatBC("14 DEC 2050 B.C.E."));
        assertEquals("DEC -2049", dp.formatBC("DEC 2050 B.C.E."));
        assertEquals("-2049", dp.formatBC("2050 B.C.E."));
        assertEquals("14 DEC -649", dp.formatBC("14 DEC 650 B.C.E."));
        assertEquals("DEC -649", dp.formatBC("DEC 650 B.C.E."));
        assertEquals("-649", dp.formatBC("650 B.C.E."));
        assertEquals("14 DEC -49", dp.formatBC("14 DEC 50 B.C.E."));
        assertEquals("DEC -49", dp.formatBC("DEC 50 B.C.E."));
        assertEquals("-49", dp.formatBC("50 B.C.E."));

        assertEquals("50", dp.formatBC("50"));
        assertEquals("650", dp.formatBC("650"));
        assertEquals("1950", dp.formatBC("1950"));
        assertEquals("JUN 1950", dp.formatBC("JUN 1950"));
        assertEquals("14 JUN 1950", dp.formatBC("14 JUN 1950"));
    }

    /**
     * Test parsing a single date with month and year but no day, using precise preference
     */
    @Test
    public void testParseInterpretedDatesGregorian() {
        assertDate(dp.parse("INT 17 JUL 2016 (\"today\")"), 2016, Calendar.JULY, 17);
        assertDate(dp.parse("INT 31 JUL 2016 (\"today\")"), 2016, Calendar.JULY, 31);
        assertDate(dp.parse("INT 17 JUL 932 (\"today\")"), 932, Calendar.JULY, 17);
        assertDate(dp.parse("INT FEB 2016 (\"today\")"), 2016, Calendar.FEBRUARY, 1);
        assertDate(dp.parse("INT 1900 (\"today\")"), 1900, Calendar.JANUARY, 1);

        assertDate(dp.parse("Int. JUL 2016 (\"today\")"), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("Int. JUL 932 (\"today\")"), 932, Calendar.JULY, 1);
        assertDate(dp.parse("Int. FEB 2016 (\"today\")"), 2016, Calendar.FEBRUARY, 1);
        assertDate(dp.parse("Int. 1900 (\"today\")"), 1900, Calendar.JANUARY, 1);

        assertDate(dp.parse("Int. JUL 2016 (\"today\")", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.JULY, 1);
        assertDate(dp.parse("Int. JUL 932 (\"today\")", ImpreciseDatePreference.FAVOR_EARLIEST), 932, Calendar.JULY, 1);
        assertDate(dp.parse("Int. FEB 2016 (\"today\")", ImpreciseDatePreference.FAVOR_EARLIEST), 2016, Calendar.FEBRUARY, 1);
        assertDate(dp.parse("Int. 1900 (\"today\")", ImpreciseDatePreference.FAVOR_EARLIEST), 1900, Calendar.JANUARY, 1);

        assertDate(dp.parse("Int. JUL 2016 (\"today\")", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.JULY, 31);
        assertDate(dp.parse("Int. JUL 932 (\"today\")", ImpreciseDatePreference.FAVOR_LATEST), 932, Calendar.JULY, 31);
        // 2016 was a leap year
        assertDate(dp.parse("Int. FEB 2016 (\"today\")", ImpreciseDatePreference.FAVOR_LATEST), 2016, Calendar.FEBRUARY, 29);
        assertDate(dp.parse("Int. 1900 (\"today\")", ImpreciseDatePreference.FAVOR_LATEST), 1900, Calendar.DECEMBER, 31);

        assertDate(dp.parse("Int. JUL 2016 (\"today\")", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.JULY, 15);
        assertDate(dp.parse("Int. JUL 932 (\"today\")", ImpreciseDatePreference.FAVOR_MIDPOINT), 932, Calendar.JULY, 15);
        assertDate(dp.parse("Int. FEB 2016 (\"today\")", ImpreciseDatePreference.FAVOR_MIDPOINT), 2016, Calendar.FEBRUARY, 14);
        assertDate(dp.parse("Int. 1900 (\"today\")", ImpreciseDatePreference.FAVOR_MIDPOINT), 1900, Calendar.JULY, 1);
    }

    /**
     * Test handling for dates that are not compliant with the GEDCOM spec in their format
     */
    @Test
    public void testParseUnsupportedFormats() {
        assertNull("Slashes not supported", dp.parse("07/17/2016"));
        assertNull("Hyphens not supported", dp.parse("2016-07-17"));
        assertNull("Field order not correct", dp.parse("2016 JUL 17"));
        assertNull("No month has 41 days", dp.parse("41 JUL 2016"));
    }

    /**
     * Test pattern for two dates
     */
    @Test
    public void testPatternTwoDates() {
        assertTrue(DateParser.PATTERN_TWO_DATES.matcher("FROM 17 JUL 1900 TO 31 DEC 2000").matches());
        assertTrue(DateParser.PATTERN_TWO_DATES.matcher("FROM JUL 1900 TO DEC 2000").matches());
        assertTrue(DateParser.PATTERN_TWO_DATES.matcher("FROM 1900 TO 2000").matches());

        assertTrue(DateParser.PATTERN_TWO_DATES.matcher("BET 17 JUL 1900 AND 31 DEC 2000").matches());
        assertTrue(DateParser.PATTERN_TWO_DATES.matcher("BET JUL 1900 AND DEC 2000").matches());
        assertTrue(DateParser.PATTERN_TWO_DATES.matcher("BET 1900 AND 2000").matches());

        assertTrue(DateParser.PATTERN_TWO_DATES.matcher("BTW 17 JUL 1900 AND 31 DEC 2000").matches());
        assertTrue(DateParser.PATTERN_TWO_DATES.matcher("BTW JUL 1900 AND DEC 2000").matches());
        assertTrue(DateParser.PATTERN_TWO_DATES.matcher("BTW 1900 AND 2000").matches());

        assertTrue(DateParser.PATTERN_TWO_DATES.matcher("BETWEEN 17 JUL 1900 AND 31 DEC 2000").matches());
        assertTrue(DateParser.PATTERN_TWO_DATES.matcher("BETWEEN JUL 1900 AND DEC 2000").matches());
        assertTrue(DateParser.PATTERN_TWO_DATES.matcher("BETWEEN 1900 AND 2000").matches());

        /*
         * The next three are strictly speaking not ok - BET should not have a period, and should not be paired with AND - but we'll
         * allow it.
         */
        assertTrue(DateParser.PATTERN_TWO_DATES.matcher("BET. 17 JUL 1900 TO 31 DEC 2000").matches());
        assertTrue(DateParser.PATTERN_TWO_DATES.matcher("BET. JUL 1900 TO DEC 2000").matches());
        assertTrue(DateParser.PATTERN_TWO_DATES.matcher("BET. 1900 TO 2000").matches());

    }

    /**
     * Test {@link DateParser#removeApproximations(String)}
     */
    @Test
    public void testRemoveAppoximations() {
        assertEquals("31 DEC 1900", dp.removeApproximations("31 DEC 1900"));
        assertEquals("31 DEC 1900", dp.removeApproximations("APPX 31 DEC 1900"));
        assertEquals("31 DEC 1900", dp.removeApproximations("APPROX 31 DEC 1900"));
        assertEquals("31 DEC 1900", dp.removeApproximations("APPX. 31 DEC 1900"));
        assertEquals("31 DEC 1900", dp.removeApproximations("APPROX. 31 DEC 1900"));
        assertEquals("31 DEC 1900", dp.removeApproximations("ABT 31 DEC 1900"));
        assertEquals("31 DEC 1900", dp.removeApproximations("ABT. 31 DEC 1900"));
        assertEquals("31 DEC 1900", dp.removeApproximations("ABOUT 31 DEC 1900"));
        assertEquals("31 DEC 1900", dp.removeApproximations("EST 31 DEC 1900"));
        assertEquals("31 DEC 1900", dp.removeApproximations("EST. 31 DEC 1900"));
    }

    /**
     * Test {@link DateParser#removePrefixes(String, String[])}
     */
    @Test
    public void testRemovePrefixes() {
        assertEquals("FOO", dp.removePrefixes("FOO", new String[] {}));
        assertEquals("FOO BAR BAZ BAT", dp.removePrefixes("FOO BAR BAZ BAT", new String[] {}));
        assertEquals("BAR BAZ BAT", dp.removePrefixes("FOO BAR BAZ BAT", new String[] { "FOO" }));
        assertEquals("BAR BAZ BAT", dp.removePrefixes("FOO. BAR BAZ BAT", new String[] { "FOO" }));
        assertEquals("BAR BAZ BAT", dp.removePrefixes("FOO BAR BAZ BAT", new String[] { "BAR", "BAZ", "BAT", "FOO" }));
        assertEquals("BAR BAZ BAT", dp.removePrefixes("FOO. BAR BAZ BAT", new String[] { "BAR", "BAZ", "BAT", "FOO" }));
    }

    /**
     * Test for {@link DateParser#resolveEnglishCalendarSwitch(String)}
     */
    @Test
    public void testResolveEnglishCalendarSwitch() {
        assertEquals("22 FEB 1732", dp.resolveEnglishCalendarSwitch("22 FEB 1732"));
        assertEquals("22 FEB 1732", dp.resolveEnglishCalendarSwitch("22 FEB 1731/32"));
        assertEquals("FEB 1732", dp.resolveEnglishCalendarSwitch("FEB 1731/32"));
        assertEquals("1732", dp.resolveEnglishCalendarSwitch("1731/32"));

        // Boundary condition for centuries
        assertEquals("22 FEB 1700", dp.resolveEnglishCalendarSwitch("22 FEB 1700"));
        assertEquals("22 FEB 1700", dp.resolveEnglishCalendarSwitch("22 FEB 1699/00"));
        assertEquals("FEB 1700", dp.resolveEnglishCalendarSwitch("FEB 1699/00"));
        assertEquals("1700", dp.resolveEnglishCalendarSwitch("1699/00"));

        assertEquals("22 FEB 1532", dp.resolveEnglishCalendarSwitch("22 FEB 1532"));
        assertEquals("22 FEB 1531/32", dp.resolveEnglishCalendarSwitch("22 FEB 1531/32"));
        assertEquals("FEB 1531/32", dp.resolveEnglishCalendarSwitch("FEB 1531/32"));
        assertEquals("1531/32", dp.resolveEnglishCalendarSwitch("1531/32"));

        assertEquals("22 FEB 1762", dp.resolveEnglishCalendarSwitch("22 FEB 1762"));
        assertEquals("22 FEB 1761/62", dp.resolveEnglishCalendarSwitch("22 FEB 1761/62"));
        assertEquals("FEB 1761/62", dp.resolveEnglishCalendarSwitch("FEB 1761/62"));
        assertEquals("1761/62", dp.resolveEnglishCalendarSwitch("1761/62"));
    }

    /**
     * Test for {@link DateParser#splitTwoDateString(String, String)}
     */
    @Test
    public void testSplitTwoDateString() {
        assertArrayEquals(new String[] { "FOO", "BAR" }, dp.splitTwoDateString("FROM FOO TO BAR", " TO "));
        assertArrayEquals(new String[] { "1 JAN 2016", "31 DEC 2016" }, dp.splitTwoDateString("FROM 1 JAN 2016 TO 31 DEC 2016",
                " TO "));
        assertArrayEquals(new String[] { "1 JAN 2016", "31 DEC 2016" }, dp.splitTwoDateString("BTW 1 JAN 2016 AND 31 DEC 2016",
                " AND "));
        assertArrayEquals(new String[] { "JAN 2016", "31 DEC 2016" }, dp.splitTwoDateString("BTW JAN 2016 AND 31 DEC 2016",
                " AND "));
        assertArrayEquals(new String[] { "2016", "DEC 2016" }, dp.splitTwoDateString("BTW 2016 AND DEC 2016", " AND "));
        assertArrayEquals(new String[] { "1 JAN 2016", "31 DEC 2016" }, dp.splitTwoDateString("BTW. 1 JAN 2016 AND 31 DEC 2016",
                " AND "));
        assertArrayEquals(new String[] { "JAN 2016", "31 DEC 2016" }, dp.splitTwoDateString("BTW. JAN 2016 AND 31 DEC 2016",
                " AND "));
        assertArrayEquals(new String[] { "2016", "DEC 2016" }, dp.splitTwoDateString("BTW. 2016 AND DEC 2016", " AND "));
        assertArrayEquals(new String[] { "1 JAN 2016", "31 DEC 2016" }, dp.splitTwoDateString("BET 1 JAN 2016 AND 31 DEC 2016",
                " AND "));
        assertArrayEquals(new String[] { "JAN 2016", "31 DEC 2016" }, dp.splitTwoDateString("BET JAN 2016 AND 31 DEC 2016",
                " AND "));
        assertArrayEquals(new String[] { "2016", "DEC 2016" }, dp.splitTwoDateString("BET 2016 AND DEC 2016", " AND "));
        assertArrayEquals(new String[] { "1 JAN 2016", "31 DEC 2016" }, dp.splitTwoDateString("BET. 1 JAN 2016 AND 31 DEC 2016",
                " AND "));
        assertArrayEquals(new String[] { "JAN 2016", "31 DEC 2016" }, dp.splitTwoDateString("BET. JAN 2016 AND 31 DEC 2016",
                " AND "));
        assertArrayEquals(new String[] { "2016", "DEC 2016" }, dp.splitTwoDateString("BET. 2016 AND DEC 2016", " AND "));
    }

}
