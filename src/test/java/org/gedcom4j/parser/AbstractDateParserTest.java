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
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Base class for {@link DateParser} tests
 * 
 * @author frizbog
 */
public abstract class AbstractDateParserTest {

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
    @SuppressWarnings("PMD.FieldDeclarationsShouldBeAtStartOfClass")
    protected final DateParser dp = new DateParser();

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
    protected void assertDate(Date d, int year, int month, int day) {
        assertNotNull("Expected date of " + (month + 1) + "/" + day + "/" + year + " but got null", d);
        Calendar c = Calendar.getInstance(Locale.US);
        c.setTimeZone(TimeZone.getTimeZone("UTC"));
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
