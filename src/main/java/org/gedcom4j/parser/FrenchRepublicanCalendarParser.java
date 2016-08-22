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
import java.util.Date;
import java.util.TimeZone;

/**
 * Class for parsing French Republican Calendar dates. Calculations based on Romme method. Only supports dates on or after September
 * 22, 1792 (Gregorian).
 * 
 * @author frizbog
 */
class FrenchRepublicanCalendarParser {

    /**
     * This function converts a French Republican date into the Gregorian date.
     * 
     * @param frenchRepublicanYear
     *            the French Republican year. French year 1 corresponds to Gregorian year 1792
     * @param frenchRepublicanMonthAbbrev
     *            the French Republican month abbreviation in GEDCOM format
     * @param dayOfMonth
     *            the day within the month
     * @return the date in Gregorian form
     */
    Date convertFrenchRepublicanDateToGregorian(int frenchRepublicanYear, String frenchRepublicanMonthAbbrev, int dayOfMonth) {
        // Validate year
        if (frenchRepublicanYear < 1) {
            return null;
        }

        // Start just before beginning of French Republican time - 21 SEP 1792
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.clear();
        c.set(1792, Calendar.SEPTEMBER, 21, 0, 0, 0);

        // Add years alrady passed
        for (int i = 1; i <= frenchRepublicanYear - 1; i++) {
            c.add(Calendar.DATE, 365); // 365 days per year
            if (isFrenchLeapYearRomme(i)) {
                c.add(Calendar.DATE, 1); // add a leap day
            }

        }

        // Figure out the month
        FrenchRepublicanMonth frm = FrenchRepublicanMonth.getFromGedcomAbbrev(frenchRepublicanMonthAbbrev);
        if (frm == null) {
            return null;
        }
        // Add 30 days per month
        c.add(Calendar.DATE, 30 * frm.ordinal());

        // Validate number of days in month
        if (dayOfMonth < 1) {
            return null;
        }
        if (frm == FrenchRepublicanMonth.JOUR_COMPLEMENTAIRS && dayOfMonth > 6) {
            // There were never more than 6 days in Jour Complementairs, and that was only on leap years
            return null;
        }
        if (frm != FrenchRepublicanMonth.JOUR_COMPLEMENTAIRS && dayOfMonth > 30) {
            // All the other months had 30 days
            return null;
        }

        // Add days for days in the month
        c.add(Calendar.DATE, dayOfMonth);

        return c.getTime();
    }

    /**
     * Is the French Republican year supplied a French Leap Year? Uses the Romme rule, which says:
     * <ul>
     * <li>Years III, VII, XI, XV, and XX are to be leap years</li>
     * <li>After that, every four years; <strong>but</strong> if the French year is divisible by 100 it must also be divisible by
     * 400 to be a leap year (much like Gregorian).
     * </ul>
     * 
     * @param frenchRepublicanYear
     *            the French Republican Year
     * @return true if it's a French Leap Year.
     */
    boolean isFrenchLeapYearRomme(int frenchRepublicanYear) {
        if (frenchRepublicanYear == 3 || frenchRepublicanYear == 7 || frenchRepublicanYear == 11 || frenchRepublicanYear == 15) {
            return true;
        }
        if (frenchRepublicanYear >= 20 && frenchRepublicanYear % 4 == 0) {
            // Probably a leap year
            if (frenchRepublicanYear % 100 == 0) {
                // Must be divisible by 400 if it's also divisible by 100 to be a leap year
                return (frenchRepublicanYear % 400 == 0);
            }
            return true;
        }
        return false;
    }

}
