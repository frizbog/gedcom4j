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

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * A class for parsing dates from strings. Slightly more relaxed than the GEDCOM spec allows.
 * 
 * @author frizbog
 * @since v3.0.1
 */
public class DateParser implements Serializable {

    /**
     * When a range or imprecise date value is found, what is the preference for handling it?
     */
    public enum ImpreciseDatePreference {
        /**
         * Return as precise a date as possible. For ranges and periods where more than one date is supplied (e.g., FROM 17 JUL 2016
         * TO 31 JUL 2016), use the first of the two dates.
         */
        PRECISE,

        /**
         * Return the earliest reasonable value for the interpreted date or range.
         */
        FAVOR_EARLIEST,

        /**
         * Return the latest reasonable value for the interpreted date or range.
         */
        FAVOR_LATEST,

        /**
         * Return the midpoint between the earliest and latest possible values for the interpreted date or range. For example, if a
         * value of "1900" is supplied, the value returned is 1900-07-01 (July 1, 1900). "JUL 1900" is supplied, the value returned
         * is 1900-07-15 (July 15). If the supplied value is not a range (i.e., there is only one date), return as precise a value
         * as possible.
         */
        FAVOR_MIDPOINT
    }

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 8700681252079486414L;

    /**
     * Range and period prefixes
     */
    private static final String FORMAT_RANGE_PERIOD_PREFIX = "(FROM|BEF|BEF\\.|BET|BET\\.|BTW|BTW\\.|AFT|AFT\\.|TO|BETWEEN) ";

    /**
     * Miscellaneous date characters, for ignoring sections of dates - alphanumeric, spaces, and periods
     */
    private static final String FORMAT_DATE_MISC = "[A-Za-z0-9. ]*";

    /**
     * The regex string for a year
     */
    private static final String FORMAT_YEAR = "\\d{1,4}(\\/\\d{2})? ?(BC|B.C.|BCE)?";

    /**
     * Regex string for a day value
     */
    private static final String FORMAT_DAY = "(0?[1-9]|[12]\\d|3[01])";

    /**
     * Regex string for a month value
     */
    private static final String FORMAT_MONTH_GREGORIAN_JULIAN = "(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)";

    /**
     * Regex string for case insensitivity
     */
    private static final String FORMAT_CASE_INSENSITIVE = "(?i)";

    /**
     * The regex pattern that identifies a single, full Gregorian/Julian date, with year, month, and day
     */
    private static final Pattern PATTERN_SINGLE_DATE_FULL_GREGORIAN_JULIAN = Pattern.compile(FORMAT_CASE_INSENSITIVE + FORMAT_DAY
            + " " + FORMAT_MONTH_GREGORIAN_JULIAN + " " + FORMAT_YEAR);

    /**
     * The regex pattern that identifies a single date, with year, month, but no day
     */
    private static final Pattern PATTERN_SINGLE_DATE_MONTH_YEAR_GREGORIAN_JULIAN = Pattern.compile(FORMAT_CASE_INSENSITIVE
            + FORMAT_MONTH_GREGORIAN_JULIAN + " " + FORMAT_YEAR);

    /**
     * The regex pattern that identifies a single date, year only (no month or day)
     */
    private static final Pattern PATTERN_SINGLE_DATE_YEAR_ONLY = Pattern.compile(FORMAT_CASE_INSENSITIVE + FORMAT_YEAR);

    /**
     * The regex pattern that matches a string ending in a double-entry year
     */
    private static final Pattern PATTERN_ENDS_IN_DOUBLE_ENTRY_YEAR = Pattern.compile(FORMAT_CASE_INSENSITIVE + FORMAT_DATE_MISC
            + "\\d{4}\\/\\d{2}$");

    /**
     * The regex pattern that identifies two-date range or period. Works for Gregorian, Julian, and Hebrew years.
     */
    static final Pattern PATTERN_TWO_DATES = Pattern.compile(FORMAT_CASE_INSENSITIVE + FORMAT_RANGE_PERIOD_PREFIX + FORMAT_DATE_MISC
            + FORMAT_YEAR + " (AND|TO) " + FORMAT_DATE_MISC + FORMAT_YEAR);

    /**
     * The regex format for matching a Hebrew month (per GEDCOM spec)
     */
    private static final String FORMAT_MONTH_HEBREW = "(TSH|CSH|KSL|TVT|SHV|ADR|ADS|NSN|IYR|SVN|TMZ|AAV|ELL)";

    /**
     * The regex format for matching a French Republican month (per GEDCOM spec)
     */
    private static final String FORMAT_MONTH_FRENCH_REPUBLICAN = "(VEND|BRUM|FRIM|NIVO|PLUV|VENT|GERM|FLOR|PRAI|MESS|THER|FRUC|COMP)";

    /**
     * Pattern for matching a single Hebrew date in GEDCOM format
     */
    private static final Pattern PATTERN_SINGLE_HEBREW_DATE = Pattern.compile(FORMAT_CASE_INSENSITIVE + FORMAT_DAY + "? ?"
            + FORMAT_MONTH_HEBREW + "? ?\\d{4}");

    /**
     * Pattern for matching a single French Republican date in GEDCOM format
     */
    private static final Pattern PATTERN_SINGLE_FRENCH_REPUBLICAN_DATE = Pattern.compile(FORMAT_CASE_INSENSITIVE + FORMAT_DAY
            + "? ?" + FORMAT_MONTH_FRENCH_REPUBLICAN + "? ?\\d{1,4}");

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
     * Parse the string as date.
     * 
     * @param dateString
     *            the date string
     * @param pref
     *            the preference for handling an imprecise date.
     * @return the date, if one can be derived from the string
     */
    public Date parse(String dateString, ImpreciseDatePreference pref) {
        String ds = dateString.toUpperCase(Locale.US);
        if (ds.startsWith("@#DHEBREW@ ")) {
            return parseHebrew(ds.substring("@#DHEBREW@ ".length()), pref);
        }
        if (ds.startsWith("@#DFRENCH R@ ")) {
            return parseFrenchRepublican(ds.substring("@#DFRENCH R@ ".length()), pref);
        }
        if (ds.startsWith("@#DGREGORIAN@ ")) {
            return parseGregorianJulian(ds.substring("@#DGREGORIAN@ ".length()), pref);
        }
        if (ds.startsWith("@#DJULIAN@ ")) {
            return parseGregorianJulian(ds.substring("@#DJULIAN@ ".length()), pref);
        }
        return parseGregorianJulian(ds, pref);
    }

    /**
     * Format a string so BC dates are turned into negative years, for parsing by {@link SimpleDateFormat}
     * 
     * @param dateString
     *            the date string, which may or may not have a BC suffix
     * @return the date formatted so BC dates have negative years
     */
    String formatBC(String dateString) {
        String d = dateString;
        if (d.endsWith("BC") || d.endsWith("BCE") || d.endsWith("B.C.") || d.endsWith("B.C.E.")) {
            String ds = d.substring(0, d.lastIndexOf('B')).trim();
            String yyyy = null;
            if (ds.lastIndexOf(' ') > -1) {
                yyyy = ds.substring(ds.lastIndexOf(' ')).trim();
                int i = Integer.parseInt(yyyy);
                int bc = 1 - i;
                String ddMMM = ds.substring(0, ds.lastIndexOf(' '));
                d = ddMMM + " " + bc;
            } else {
                yyyy = ds.trim();
                int i = Integer.parseInt(yyyy);
                d = Integer.toString(1 - i);
            }
        }
        return d;
    }

    /**
     * Convert a French Republican date string (in proper GEDCOM format) to a (Gregorian) java.util.Date.
     * 
     * @param frenchRepublicanDateString
     *            the French Republican date in GEDCOM spec format - see DATE_HEBR and MONTH_HEBR in the spec.
     * @param pref
     *            preference on how to handle imprecise dates - return the earliest day of the month, the latest, the midpoint?
     * @return the Gregorian date that represents the French Republican date supplied
     */
    Date parseFrenchRepublicanSingleDate(String frenchRepublicanDateString, ImpreciseDatePreference pref) {
        String frds = removeApproximations(frenchRepublicanDateString.toUpperCase(Locale.US));
        frds = removeOpenEndedRangesAndPeriods(frds);

        if (!PATTERN_SINGLE_FRENCH_REPUBLICAN_DATE.matcher(frds).matches()) {
            return null;
        }

        String[] datePieces = frds.split(" ");
        if (datePieces == null) {
            return null;
        }

        if (datePieces.length == 3) {
            return parseFrenchRepublicanDayMonthYear(datePieces);
        } else if (datePieces.length == 2) {
            return parseFrenchRepublicanMonthYear(datePieces, pref);
        } else if (datePieces.length == 1) {
            return parseFrenchRepublicanYearOnly(datePieces, pref);
        } else {
            return null;
        }

    }

    /**
     * Convert a Hebrew date string (in proper GEDCOM format) to a (Gregorian) java.util.Date.
     * 
     * @param hebrewDateString
     *            the Hebrew date in GEDCOM spec format - see DATE_HEBR and MONTH_HEBR in the spec.
     * @param pref
     *            preference on how to handle imprecise dates - return the earliest day of the month, the latest, the midpoint?
     * @return the Gregorian date that represents the Hebrew date supplied
     */
    Date parseHebrewSingleDate(String hebrewDateString, ImpreciseDatePreference pref) {
        String hds = removeApproximations(hebrewDateString.toUpperCase(Locale.US));
        hds = removeOpenEndedRangesAndPeriods(hds);

        if (!PATTERN_SINGLE_HEBREW_DATE.matcher(hds).matches()) {
            return null;
        }

        String[] datePieces = hds.split(" ");
        if (datePieces == null) {
            return null;
        }

        if (datePieces.length == 3) {
            return parseHebrewDayMonthYear(datePieces);
        } else if (datePieces.length == 2) {
            return parseHebrewMonthYear(pref, datePieces);
        } else if (datePieces.length == 1) {
            return parseHebrewYearOnly(pref, datePieces);
        } else {
            return null;
        }

    }

    /**
     * Return a version of the string with approximation prefixes removed, including handling for interpreted dates
     * 
     * @param dateString
     *            the date string
     * @return a version of the string with approximation prefixes removed
     */
    String removeApproximations(String dateString) {
        String ds = removePrefixes(dateString, new String[] { "ABT", "ABOUT", "APPX", "APPROX", "CAL", "CALC", "EST" });

        // Interpreted dates - require terms in parentheses after the date
        if (ds.startsWith("INT ") && ds.indexOf('(') > 6) {
            return ds.substring(4, ds.indexOf('(')).trim();
        }
        if (ds.startsWith("INT. ") && ds.indexOf('(') > 7) {
            return ds.substring(4, ds.indexOf('(')).trim();
        }

        return ds;
    }

    /**
     * Remove any of a set of prefixes from a date string. The prefixes will be removed if they begin the string, followed by an
     * optional period, then a space. For example, if "BEF" is one of the prefixes passed in, and the date string passed in is
     * either "BEF 1900" or "BEF. 1900", the result will be "1900".
     * 
     * @param dateString
     *            the date string
     * @param prefixes
     *            the prefixes
     * @return the string with the prefixes removed
     */
    String removePrefixes(String dateString, String... prefixes) {

        for (String pfx : prefixes) {
            if (dateString.startsWith(pfx + " ") && dateString.length() > pfx.length() + 1) {
                return dateString.substring(pfx.length() + 1).trim();
            }
            if (dateString.startsWith(pfx + ". ") && dateString.length() > pfx.length() + 2) {
                return dateString.substring(pfx.length() + 2).trim();
            }
        }
        return dateString;
    }

    /**
     * <p>
     * Resolve a date in double-dated format, for the old/new dates preceding the English calendar switch of 1752.
     * </p>
     * <p>
     * Because of the switch to the Gregorian Calendar in 1752 in England and its colonies, and the corresponding change of the
     * first day of the year, it's not uncommon for dates in the range between 1582 and 1752 to be written using a double-dated
     * format, showing the old and new dates simultaneously. For example, today we would render George Washington's birthday in
     * GEDCOM format as <code>22 FEB 1732</code>. However, in 1760 or so, one might have written it as Feb 22 1731/32, thus be
     * entered into a GEDCOM field as <code>22 FEB 1731/32</code>.
     * </p>
     * 
     * @param dateString
     *            the date string. Assumed to have had any BC (or similar) era suffix removed already, so the string is assumed to
     *            end in a year.
     * @return the date, resolved to a Gregorian date
     */
    String resolveEnglishCalendarSwitch(String dateString) {
        if (!PATTERN_ENDS_IN_DOUBLE_ENTRY_YEAR.matcher(dateString).matches()) {
            return dateString;
        }

        int l = dateString.length();
        String oldYYYY = dateString.substring(l - 7, l - 3);
        int yyyy = Integer.parseInt(oldYYYY);
        if (yyyy > 1752 || yyyy < 1582) {
            return dateString;
        }

        String newYY = dateString.substring(l - 2);
        int yy = Integer.parseInt(newYY);

        // Handle century boundary
        if (yy == 0 && yyyy % 100 == 99) {
            yyyy++;
        }

        String upToYYYY = dateString.substring(0, l - 7);
        StringBuilder ds = new StringBuilder(upToYYYY);
        ds.append(yyyy / 100);
        ds.append(newYY);
        return ds.toString();
    }

    /**
     * Split a two-date string, removing prefixes, and return an array of two date strings
     * 
     * @param dateString
     *            the date string containing two dates
     * @param splitOn
     *            the delimiting phrase or character between the two dates
     * @return an array of two strings, or an empty array if the supplied <code>dateString</code> value does not contain the
     *         <code>splitOn</code> delimiter value. Never returns null.
     */
    String[] splitTwoDateString(String dateString, String splitOn) {
        if (dateString.contains(splitOn)) {
            String[] dateStrings = new String[2];
            dateStrings[0] = removePrefixes(dateString.substring(0, dateString.indexOf(splitOn)).trim(), new String[] { "BETWEEN",
                    "BET", "BTW", "FROM" });
            dateStrings[1] = dateString.substring(dateString.indexOf(splitOn) + splitOn.length()).trim();
            return dateStrings;
        }
        return new String[] {};
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
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdf.parse(dateString);
        } catch (@SuppressWarnings("unused") ParseException ignored) {
            return null;
        }
    }

    /**
     * Get a Gregorian date from a French Republican date string consisting of two dates separated by either "AND" or "TO", and with
     * a prefix like "FROM" or "BET", using the supplied method of resolving a single date from the range
     * 
     * @param frenchRepublicanDateString
     *            a French Republican date string
     * @param pref
     *            preference on how to handle imprecise dates - return the earliest day of the month, the latest, the midpoint?
     * @return the preferred date based on the string supplied, or null if no date could be determined
     */
    private Date getPreferredDateFromFrenchRepublicanRangeOrPeriod(String frenchRepublicanDateString,
            ImpreciseDatePreference pref) {
        // Split the string into two dates
        String[] dateStrings = splitTwoDateString(frenchRepublicanDateString, " AND ");
        if (dateStrings.length == 0) {
            dateStrings = splitTwoDateString(frenchRepublicanDateString, " TO ");
        }
        if (dateStrings.length == 0) {
            return null;
        }

        // Calculate the dates from the two strings, based on what's preferred
        switch (pref) {
            case FAVOR_EARLIEST:
                return parseFrenchRepublicanSingleDate(dateStrings[0], pref);
            case FAVOR_LATEST:
                return parseFrenchRepublicanSingleDate(dateStrings[1], pref);
            case FAVOR_MIDPOINT:
                Date d1 = parseFrenchRepublicanSingleDate(dateStrings[0], ImpreciseDatePreference.FAVOR_EARLIEST);
                Date d2 = parseFrenchRepublicanSingleDate(dateStrings[1], ImpreciseDatePreference.FAVOR_LATEST);
                if (d1 == null || d2 == null) {
                    return null;
                }
                long daysBetween = TimeUnit.DAYS.convert(d2.getTime() - d1.getTime(), TimeUnit.MILLISECONDS);
                Calendar c = Calendar.getInstance(Locale.US);
                c.setTimeZone(TimeZone.getTimeZone("UTC"));
                c.setTime(d1);
                c.add(Calendar.DAY_OF_YEAR, (int) daysBetween / 2);
                return c.getTime();
            case PRECISE:
                return parseFrenchRepublicanSingleDate(dateStrings[0], pref);
            default:
                throw new IllegalArgumentException("Unexpected value for imprecise date preference: " + pref);
        }
    }

    /**
     * Get a Gregorian date from a Hebrew date string consisting of two dates separated by either "AND" or "TO", and with a prefix
     * like "FROM" or "BET", using the supplied method of resolving a single date from the range
     * 
     * 
     * @param hebrewDateString
     *            the Hebrew date string
     * @param pref
     *            preference on how to handle imprecise dates - return the earliest day of the month, the latest, the midpoint?
     * @return the date, or null if no date could be parsed from the data
     */
    private Date getPreferredDateFromHebrewRangeOrPeriod(String hebrewDateString, ImpreciseDatePreference pref) {
        // Split the string into two dates
        String[] dateStrings = splitTwoDateString(hebrewDateString, " AND ");
        if (dateStrings.length == 0) {
            dateStrings = splitTwoDateString(hebrewDateString, " TO ");
        }
        if (dateStrings.length == 0) {
            return null;
        }

        // Calculate the dates from the two strings, based on what's preferred
        switch (pref) {
            case FAVOR_EARLIEST:
                return parseHebrewSingleDate(dateStrings[0], pref);
            case FAVOR_LATEST:
                return parseHebrewSingleDate(dateStrings[1], pref);
            case FAVOR_MIDPOINT:
                Date d1 = parseHebrewSingleDate(dateStrings[0], ImpreciseDatePreference.FAVOR_EARLIEST);
                Date d2 = parseHebrewSingleDate(dateStrings[1], ImpreciseDatePreference.FAVOR_LATEST);
                if (d1 == null || d2 == null) {
                    return null;
                }
                long daysBetween = TimeUnit.DAYS.convert(d2.getTime() - d1.getTime(), TimeUnit.MILLISECONDS);
                Calendar c = Calendar.getInstance(Locale.US);
                c.setTimeZone(TimeZone.getTimeZone("UTC"));
                c.setTime(d1);
                c.add(Calendar.DAY_OF_YEAR, (int) daysBetween / 2);
                return c.getTime();
            case PRECISE:
                return parseHebrewSingleDate(dateStrings[0], pref);
            default:
                throw new IllegalArgumentException("Unexpected value for imprecise date preference: " + pref);
        }
    }

    /**
     * Get the preferred date from a range or period, for Gregorian/Julian dates
     * 
     * @param dateString
     *            the date string
     * @param pref
     *            the preferred method of handling the range
     * @return the date, or null if no date could be parsed from the data
     */
    private Date getPreferredDateFromRangeOrPeriod(String dateString, ImpreciseDatePreference pref) {
        // Split the string into two dates
        String[] dateStrings = splitTwoDateString(dateString, " AND ");
        if (dateStrings.length == 0) {
            dateStrings = splitTwoDateString(dateString, " TO ");
        }
        if (dateStrings.length == 0) {
            return null;
        }

        // Calculate the dates from the two strings, based on what's preferred
        switch (pref) {
            case FAVOR_EARLIEST:
                return parse(dateStrings[0], pref);
            case FAVOR_LATEST:
                return parse(dateStrings[1], pref);
            case FAVOR_MIDPOINT:
                Date d1 = parse(dateStrings[0], ImpreciseDatePreference.FAVOR_EARLIEST);
                Date d2 = parse(dateStrings[1], ImpreciseDatePreference.FAVOR_LATEST);
                if (d1 == null || d2 == null) {
                    return null;
                }
                long daysBetween = TimeUnit.DAYS.convert(d2.getTime() - d1.getTime(), TimeUnit.MILLISECONDS);
                Calendar c = Calendar.getInstance(Locale.US);
                c.setTimeZone(TimeZone.getTimeZone("UTC"));
                c.setTime(d1);
                c.add(Calendar.DAY_OF_YEAR, (int) daysBetween / 2);
                return c.getTime();
            case PRECISE:
                return parse(dateStrings[0], pref);
            default:
                throw new IllegalArgumentException("Unexpected value for imprecise date preference: " + pref);
        }
    }

    /**
     * Get the date from a date string when the string is formatted with a month and year but no day
     * 
     * @param dateString
     *            the date string
     * @return the date found, if any, or null if no date could be extracted
     */
    private Date getYearMonthDay(String dateString) {
        String bc = formatBC(dateString);
        String e = resolveEnglishCalendarSwitch(bc);
        return getDateWithFormatString(e, "dd MMM yyyy");
    }

    /**
     * Get the date from a date string when the string is formatted with a month and year but no day
     * 
     * @param dateString
     *            the date string
     * @param pref
     *            preference on how to handle imprecise dates, like this one - return the earliest day of the month, the latest, the
     *            midpoint?
     * @return the date found, if any, or null if no date could be extracted
     */
    private Date getYearMonthNoDay(String dateString, ImpreciseDatePreference pref) {
        String bc = formatBC(dateString);
        String e = resolveEnglishCalendarSwitch(bc);
        Date d = getDateWithFormatString(e, "MMM yyyy");
        Calendar c = Calendar.getInstance(Locale.US);
        c.setTimeZone(TimeZone.getTimeZone("UTC"));
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

    /**
     * Get the date from a date string when the string is formatted with a year but no month or day
     * 
     * @param dateString
     *            the date string
     * @param pref
     *            preference on how to handle imprecise dates, like this one - return the earliest day of the month, the latest, the
     *            midpoint?
     * @return the date found, if any, or null if no date could be extracted
     */
    private Date getYearOnly(String dateString, ImpreciseDatePreference pref) {
        String bc = formatBC(dateString);
        String e = resolveEnglishCalendarSwitch(bc);
        Date d = getDateWithFormatString(e, "yyyy");
        Calendar c = Calendar.getInstance(Locale.US);
        c.setTimeZone(TimeZone.getTimeZone("UTC"));
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

    /**
     * Convert a French Republican date string (in proper GEDCOM format) to a (Gregorian) java.util.Date.
     * 
     * @param frenchRepublicanDateString
     *            the French Republican date in GEDCOM spec format - see DATE_FREN and MONTH_FREN in the spec. Could be a single
     *            date, an approximate date, a date range, or a date period.
     * @param pref
     *            preference on how to handle imprecise dates - return the earliest day of the month, the latest, the midpoint?
     * @return the Gregorian date that represents the French Republican date supplied
     */
    private Date parseFrenchRepublican(String frenchRepublicanDateString, ImpreciseDatePreference pref) {
        if (PATTERN_TWO_DATES.matcher(frenchRepublicanDateString).matches()) {
            return getPreferredDateFromFrenchRepublicanRangeOrPeriod(frenchRepublicanDateString, pref);
        }
        return parseFrenchRepublicanSingleDate(frenchRepublicanDateString, pref);
    }

    /**
     * Get the date from a French Republican date string when the string is formatted with a year, month, and day
     * 
     * @param datePieces
     *            3-element array with the day, month, and year (in that order).
     * 
     * @return the date found, if any, or null if no date could be extracted
     */
    private Date parseFrenchRepublicanDayMonthYear(String... datePieces) {
        FrenchRepublicanMonth frMonth = FrenchRepublicanMonth.getFromGedcomAbbrev(datePieces[1]);
        if (frMonth == null) {
            return null;
        }
        FrenchRepublicanCalendarParser frc = new FrenchRepublicanCalendarParser();
        int frYear = Integer.parseInt(datePieces[2]);
        int frDay = Integer.parseInt(datePieces[0]);

        return frc.convertFrenchRepublicanDateToGregorian(frYear, frMonth.getGedcomAbbrev(), frDay);
    }

    /**
     * Get the date from a French Republican date string when the string is formatted with a year and month but no day
     * 
     * @param datePieces
     *            2-element array with month and year (in that order)
     * @param pref
     *            preference on how to handle imprecise dates, like this one - return the earliest day of the month, the latest, the
     *            midpoint?
     * @return the date found, if any, or null if no date could be extracted
     */
    private Date parseFrenchRepublicanMonthYear(String[] datePieces, ImpreciseDatePreference pref) {
        FrenchRepublicanMonth frMonth = FrenchRepublicanMonth.getFromGedcomAbbrev(datePieces[0]);
        if (frMonth == null) {
            return null;
        }
        FrenchRepublicanCalendarParser frc = new FrenchRepublicanCalendarParser();
        int frYear = Integer.parseInt(datePieces[1]);
        int frDay;
        switch (pref) {
            case FAVOR_EARLIEST:
                frDay = 1;
                break;
            case FAVOR_LATEST:
                if (frMonth == FrenchRepublicanMonth.JOUR_COMPLEMENTAIRS) {
                    if (frc.isFrenchLeapYearRomme(frYear)) {
                        frDay = 6;
                    } else {
                        frDay = 5;
                    }
                } else {
                    frDay = 30;
                }
                break;
            case FAVOR_MIDPOINT:
                if (frMonth == FrenchRepublicanMonth.JOUR_COMPLEMENTAIRS) {
                    if (frc.isFrenchLeapYearRomme(frYear)) {
                        frDay = 3;
                    } else {
                        frDay = 2;
                    }
                } else {
                    frDay = 15;
                }
                break;
            case PRECISE:
                frDay = 1;
                break;
            default:
                throw new IllegalArgumentException("Unexpected value for imprecise date preference: " + pref);
        }

        return frc.convertFrenchRepublicanDateToGregorian(frYear, frMonth.getGedcomAbbrev(), frDay);
    }

    /**
     * Get the date from a French Republican date string when the string is formatted with a year but no month or day
     * 
     * @param datePieces
     *            1-element array containing the year
     * @param pref
     *            preference on how to handle imprecise dates, like this one - return the earliest day of the month, the latest, the
     *            midpoint?
     * @return the date found, if any, or null if no date could be extracted
     */
    private Date parseFrenchRepublicanYearOnly(String[] datePieces, ImpreciseDatePreference pref) {
        FrenchRepublicanCalendarParser frc = new FrenchRepublicanCalendarParser();
        int frYear = Integer.parseInt(datePieces[0]);
        int frDay;
        FrenchRepublicanMonth frMonth = null;
        switch (pref) {
            case FAVOR_EARLIEST:
                frMonth = FrenchRepublicanMonth.VENDEMIAIRE;
                frDay = 1;
                break;
            case FAVOR_LATEST:
                frMonth = FrenchRepublicanMonth.JOUR_COMPLEMENTAIRS;
                if (frc.isFrenchLeapYearRomme(frYear)) {
                    frDay = 6;
                } else {
                    frDay = 5;
                }
                break;
            case FAVOR_MIDPOINT:
                frMonth = FrenchRepublicanMonth.GERMINAL;
                frDay = 1;
                break;
            case PRECISE:
                frMonth = FrenchRepublicanMonth.VENDEMIAIRE;
                frDay = 1;
                break;
            default:
                throw new IllegalArgumentException("Unexpected value for imprecise date preference: " + pref);
        }
        return frc.convertFrenchRepublicanDateToGregorian(frYear, frMonth.getGedcomAbbrev(), frDay);
    }

    /**
     * Parse a Gregorian or Julian date string
     * 
     * @param dateString
     *            the date string to parse
     * @param pref
     *            the preference for handling an imprecise date.
     * @return the date, if one can be derived from the string
     */
    private Date parseGregorianJulian(String dateString, ImpreciseDatePreference pref) {
        String ds;
        ds = removeApproximations(dateString.toUpperCase(Locale.US));
        ds = removeOpenEndedRangesAndPeriods(ds);
        if (PATTERN_SINGLE_DATE_FULL_GREGORIAN_JULIAN.matcher(ds).matches()) {
            return getYearMonthDay(ds);
        }
        if (PATTERN_SINGLE_DATE_MONTH_YEAR_GREGORIAN_JULIAN.matcher(ds).matches()) {
            return getYearMonthNoDay(ds, pref);
        }
        if (PATTERN_SINGLE_DATE_YEAR_ONLY.matcher(ds).matches()) {
            return getYearOnly(ds, pref);
        }
        if (PATTERN_TWO_DATES.matcher(ds).matches()) {
            return getPreferredDateFromRangeOrPeriod(ds, pref);
        }
        return null;
    }

    /**
     * Convert a Hebrew date string (in proper GEDCOM format) to a (Gregorian) java.util.Date.
     * 
     * @param hebrewDateString
     *            the Hebrew date in GEDCOM spec format - see DATE_HEBR and MONTH_HEBR in the spec. Could be a single date, an
     *            approximate date, a date range, or a date period.
     * @param pref
     *            preference on how to handle imprecise dates - return the earliest day of the month, the latest, the midpoint?
     * 
     * @return the Gregorian date that represents the Hebrew date supplied
     */
    private Date parseHebrew(String hebrewDateString, ImpreciseDatePreference pref) {
        if (PATTERN_TWO_DATES.matcher(hebrewDateString).matches()) {
            return getPreferredDateFromHebrewRangeOrPeriod(hebrewDateString, pref);
        }
        return parseHebrewSingleDate(hebrewDateString, pref);
    }

    /**
     * Get the date from a Hebrew date string when the string is formatted with a year, month, and day
     * 
     * @param datePieces
     *            3-element array with the day, month, and year (in that order).
     * 
     * @return the date found, if any, or null if no date could be extracted
     */
    private Date parseHebrewDayMonthYear(String... datePieces) {
        {
            HebrewMonth hebrewMonth = HebrewMonth.getFromAbbreviation(datePieces[1]);
            if (hebrewMonth == null) {
                // Didn't find a matching month abbreviation
                return null;
            }
            HebrewCalendarParser hc = new HebrewCalendarParser();
            int hebrewDay = Integer.parseInt(datePieces[0]);
            int hebrewYear = Integer.parseInt(datePieces[2]);
            return hc.convertHebrewDateToGregorian(hebrewYear, hebrewMonth.getGedcomAbbrev(), hebrewDay);
        }
    }

    /**
     * Get the date from a Hebrew date string when the string is formatted with a year and month but no day
     * 
     * @param datePieces
     *            2-element array with month and year (in that order)
     * @param pref
     *            preference on how to handle imprecise dates, like this one - return the earliest day of the month, the latest, the
     *            midpoint?
     * @return the date found, if any, or null if no date could be extracted
     */
    private Date parseHebrewMonthYear(ImpreciseDatePreference pref, String... datePieces) {
        {
            HebrewMonth hebrewMonth = HebrewMonth.getFromAbbreviation(datePieces[0]);
            if (hebrewMonth == null) {
                return null;
            }
            HebrewCalendarParser hc = new HebrewCalendarParser();
            int hebrewYear = Integer.parseInt(datePieces[1]);
            int hebrewDay;
            switch (pref) {
                case FAVOR_EARLIEST:
                    hebrewDay = 1;
                    break;
                case FAVOR_LATEST:
                    hebrewDay = hc.getMonthLength(hebrewYear, hebrewMonth);
                    break;
                case FAVOR_MIDPOINT:
                    hebrewDay = hc.getMonthLength(hebrewYear, hebrewMonth) / 2;
                    break;
                case PRECISE:
                    hebrewDay = 1;
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected value for imprecise date preference: " + pref);
            }
            return hc.convertHebrewDateToGregorian(hebrewYear, hebrewMonth.getGedcomAbbrev(), hebrewDay);
        }
    }

    /**
     * Get the date from a Hebrew date string when the string is formatted with a year but no month or day
     * 
     * @param datePieces
     *            1-element array containing the year
     * @param pref
     *            preference on how to handle imprecise dates, like this one - return the earliest day of the month, the latest, the
     *            midpoint?
     * @return the date found, if any, or null if no date could be extracted
     */
    private Date parseHebrewYearOnly(ImpreciseDatePreference pref, String... datePieces) {
        {
            HebrewCalendarParser hc = new HebrewCalendarParser();
            int hebrewYear = Integer.parseInt(datePieces[0]);
            HebrewMonth hebrewMonth;
            int hebrewDay;
            switch (pref) {
                case FAVOR_EARLIEST:
                    hebrewMonth = HebrewMonth.TISHREI;
                    hebrewDay = 1;
                    break;
                case FAVOR_LATEST:
                    hebrewMonth = HebrewMonth.ELUL;
                    hebrewDay = hc.getMonthLength(hebrewYear, hebrewMonth);
                    break;
                case FAVOR_MIDPOINT:
                    hebrewMonth = HebrewMonth.ADAR;
                    hebrewDay = hc.getMonthLength(hebrewYear, hebrewMonth) / 2;
                    break;
                case PRECISE:
                    hebrewMonth = HebrewMonth.TISHREI;
                    hebrewDay = 1;
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected value for imprecise date preference: " + pref);
            }
            return hc.convertHebrewDateToGregorian(hebrewYear, hebrewMonth.getGedcomAbbrev(), hebrewDay);
        }
    }

    /**
     * Remove the prefixes for open ended date ranges with only one date (e.g., "BEF 1900", "FROM 1756", "AFT 2000")
     * 
     * @param dateString
     *            the date string
     * @return the same date string with range/period prefixes removed, but only if it's an open-ended period or range
     */
    private String removeOpenEndedRangesAndPeriods(String dateString) {
        if (!PATTERN_TWO_DATES.matcher(dateString).matches()) {
            return removePrefixes(dateString, new String[] { "FROM", "BEF", "BEFORE", "AFT", "AFTER", "TO" });
        }

        return dateString;
    }

}
