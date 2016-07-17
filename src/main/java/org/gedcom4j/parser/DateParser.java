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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * A class for parsing dates from strings. Tries a whole lot of formats, growing increasingly imprecise. Also attempts to work with
 * ranges.
 * 
 * @author frizbog
 * @since v3.0.1
 */
public class DateParser {

    /**
     * When a range or imprecise date value is found, what is the preference for handling it?
     */
    public enum ImpreciseDatePreference {
        /**
         * We only want precise dates. Ignore others.
         */
        PRECISE,

        /**
         * Return the earliest possible value for the interpreted date or range
         */
        FAVOR_EARLIEST,

        /**
         * Return the latest possible value for the interpreted date or range
         */
        FAVOR_LATEST,

        /**
         * Return the midpoint between the earliest and latest possible vlues for the interpreted date or range
         */
        FAVOR_MIDPOINT
    };

    /**
     * The regex pattern that identifies a single, full date, with year, month, and day
     */
    private static final Pattern REGEX_SINGLE_DATE_FULL = Pattern.compile("[012]{0,1}[0-9]{1} [A-Za-z]{3} \\d{3,4}");

    /**
     * The regex pattern that identifies a single date, with year, month, but no day
     */
    private static final Pattern REGEX_SINGLE_DATE_MONTH_YEAR = Pattern.compile("[A-Za-z]{3} \\d{3,4}");

    /**
     * The regex pattern that identifies a single date, year only (no month or day)
     */
    private static final Pattern REGEX_SINGLE_DATE_YEAR_ONLY = Pattern.compile("\\d{3,4}");

    /**
     * Parse the string as date, with the default imprecise date handling preference of {@link ImpreciseDatePreference#PRECISE}.
     * 
     * @param dateString
     *            the date string
     * @return the date, if one can be derived from the string
     */
    public Date parse(String dateString) {
        return parse(dateString, ImpreciseDatePreference.PRECISE);
    }

    /**
     * Parse the string as date
     * 
     * @param dateString
     *            the date string
     * @param pref
     *            the preference for handling an imprecise date
     * @return the date, if one can be derived from the string
     */
    public Date parse(String dateString, ImpreciseDatePreference pref) {
        if (REGEX_SINGLE_DATE_FULL.matcher(dateString).matches()) {
            return getDateWithFormatString(dateString, "dd MMM yyyy");
        }
        if (REGEX_SINGLE_DATE_MONTH_YEAR.matcher(dateString).matches()) {
            return getYearMonthNoDay(dateString, pref);
        }
        if (REGEX_SINGLE_DATE_YEAR_ONLY.matcher(dateString).matches()) {
            Date d = getDateWithFormatString(dateString, "yyyy");
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            switch (pref) {
                case FAVOR_EARLIEST:
                    // First day of year
                    c.set(Calendar.DAY_OF_YEAR, 1);
                    return c.getTime();
                case FAVOR_LATEST:
                    // Last day of year
                    c.set(Calendar.MONTH, Calendar.DECEMBER);
                    c.set(Calendar.DAY_OF_MONTH, 31);
                    return c.getTime();
                case FAVOR_MIDPOINT:
                    // Middle day of year - go with July 1. Not precisely midpoint but feels midpointy.
                    c.set(Calendar.MONTH, Calendar.JULY);
                    c.set(Calendar.DAY_OF_MONTH, 1);
                    return c.getTime();
                case PRECISE:
                    return d;
                default:
                    throw new IllegalArgumentException("Unknown value for date handling preference: " + pref);
            }
        }
        return null;
    }

    /**
     * Attempt to parse <code>dateString</code> using date format <code>fmt</code>. If successful, return the date. Otherwise return
     * null.
     * 
     * @param dateString
     *            the date string
     * @param pattern
     *            the date format to try parsing with
     * @return the date if successful, or null if the date cannot be parsed using the format supplied
     */
    private Date getDateWithFormatString(String dateString, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(dateString);
        } catch (@SuppressWarnings("unused") ParseException ignored) {
            return null;
        }
    }

    /**
     * @param dateString
     * @param pref
     * @return
     */
    private Date getYearMonthNoDay(String dateString, ImpreciseDatePreference pref) {
        Date d = getDateWithFormatString(dateString, "MMM yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        switch (pref) {
            case FAVOR_EARLIEST:
                // First day of month
                c.set(Calendar.DAY_OF_MONTH, 1);
                return c.getTime();
            case FAVOR_LATEST:
                // Last day of month
                c.set(Calendar.DAY_OF_MONTH, 1);
                c.add(Calendar.MONTH, 1);
                c.add(Calendar.DAY_OF_YEAR, -1);
                return c.getTime();
            case FAVOR_MIDPOINT:
                // Middle day of month
                c.set(Calendar.DAY_OF_MONTH, 1);
                c.add(Calendar.MONTH, 1);
                c.add(Calendar.DAY_OF_MONTH, -1);
                int dom = c.get(Calendar.DAY_OF_MONTH) / 2;
                c.set(Calendar.DAY_OF_MONTH, dom);
                return c.getTime();
            case PRECISE:
                return d;
            default:
                throw new IllegalArgumentException("Unknown value for date handling preference: " + pref);
        }
    }

}
