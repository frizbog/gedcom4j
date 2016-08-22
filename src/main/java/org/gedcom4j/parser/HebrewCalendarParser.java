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
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * A class for converting Hebrew dates into Gregorian dates. No ability is provided for the reverse. All dates processed as if
 * before sunset.
 * </p>
 * 
 * @author frizbog
 */
class HebrewCalendarParser {

    /**
     * This function converts a Hebrew date into the Gregorian date
     * 
     * @param hebrewYear
     *            the hebrew year
     * @param hebrewMonthAbbrev
     *            the hebrew month abbreviation in GEDCOM format
     * @param dayOfMonth
     *            the day within the month
     * @return the date in Gregorian form
     */
    Date convertHebrewDateToGregorian(int hebrewYear, String hebrewMonthAbbrev, int dayOfMonth) {

        HebrewMonth hebrewMonth = HebrewMonth.getFromAbbreviation(hebrewMonthAbbrev);
        int hebrewMonthNum = hebrewMonth.ordinal() + 1; // one-based

        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.setTime(getFirstDayOfHebrewYear(hebrewYear));

        // Now count up days within the year
        for (int m = 1; m <= hebrewMonthNum - 1; m++) {
            int monthLength = getMonthLength(hebrewYear, HebrewMonth.getFrom1BasedNumber(m));
            c.add(Calendar.DAY_OF_YEAR, +monthLength);
        }
        c.add(Calendar.DAY_OF_YEAR, dayOfMonth - 1);
        return c.getTime();
    }

    /**
     * Get the number of days in the month and year requeted
     * 
     * @param hebrewYear
     *            the hebrew year
     * @param hebrewMonth
     *            the Hebrew month
     * @return the number of days in the month on the specified year
     */
    int getMonthLength(int hebrewYear, HebrewMonth hebrewMonth) {

        int hebrewMonthNum = hebrewMonth.ordinal() + 1;
        boolean leapYear = isLeapYear(hebrewYear);
        int lenHebrewYear = getLengthOfYear(hebrewYear);
        /*
         * The regular length of a non-leap Hebrew year is 354 days. The regular length of a Hebrew leap year is 384 days.
         * 
         * If the year is shorter by one less day, it is called a haser year. Kislev on a haser year has 29 days. If the year is
         * longer by one day, it is called a shalem year. Cheshvan on a shalem year is 30 days.
         */
        boolean haserYear = (lenHebrewYear == 353 || lenHebrewYear == 383);
        boolean shalemYear = (lenHebrewYear == 355 || lenHebrewYear == 385);
        int monthLength = 0;
        if (hebrewMonthNum == 1 || hebrewMonthNum == 5 || hebrewMonthNum == 8 || hebrewMonthNum == 10 || hebrewMonthNum == 12) {
            monthLength = 30;
        } else if (hebrewMonthNum == 4 || hebrewMonthNum == 7 || hebrewMonthNum == 9 || hebrewMonthNum == 11
                || hebrewMonthNum == 13) {
            monthLength = 29;
        } else if (hebrewMonthNum == 6) {
            monthLength = (leapYear ? 30 : 0);
        } else if (hebrewMonthNum == 2) {
            monthLength = (shalemYear ? 30 : 29);
        } else if (hebrewMonthNum == 3) {
            monthLength = (haserYear ? 29 : 30);
        }
        return monthLength;
    }

    /**
     * Get the Gregorian Date corresponding to the first day of a given Hebrew year (1 Tishrei)
     * 
     * @param hebrewYear
     *            the hebrew year (e.g., 5776)
     * @return the gregorian date of the first day of the hebrew year supplied
     */
    private Date getFirstDayOfHebrewYear(int hebrewYear) {
        /*
         * Calculate how many days, hours and chalakim (1/1080th of an hour, about 3.333 secs) it has been from the molad (start of
         * new moon) at the beginning of the year.
         * 
         * The period between one new moon to the next is 29 days, 12 hours and 793 chalakim. We must multiply that by the amount of
         * months that transpired since the first molad. Then we add the time of the first molad (Monday, 5 hours and 204 chalakim).
         */
        int monthsSinceFirstMolad = getMonthsSinceFirstMolad(hebrewYear);
        int chalakim = 793 * monthsSinceFirstMolad;
        chalakim += 204;
        // carry the excess Chalakim over to the hours
        int hours = (int) Math.floor(chalakim / 1080);
        chalakim = chalakim % 1080;

        hours += monthsSinceFirstMolad * 12;
        hours += 5;

        // carry the excess hours over to the days
        int days = (int) Math.floor(hours / 24);
        hours = hours % 24;

        days += 29 * monthsSinceFirstMolad;
        days += 2;

        // Figure out which day of the week the molad occurs. Shabbos is 0, other days of week are 1-based.
        int dayOfWeek = days % 7;

        /*
         * In a perfect world, Rosh Hashanah would be on the day of the molad. The Hebrew calendar makes four exceptions where we
         * push off Rosh Hashanah one or two days. This is done to prevent three situations. Without explaining why, the three
         * situations are:
         * 
         * 1) We don't want Rosh Hashanah to come out on Sunday, Wednesday or Friday
         * 
         * 2) We don't want Rosh Hashanah to be on the day of the molad if the molad occurs after the beginning of 18th hour.
         * 
         * 3) We want to limit years to specific lengths. For non-leap years, we limit it to either 353, 354 or 355 days. For leap
         * years, we limit it to either 383, 384 or 385 days. If setting Rosh Hashanah to the day of the molad will cause this year,
         * or the previous year to fall outside these lengths, we push off Rosh Hashanah to get the year back to a valid length.
         * 
         * This code handles these exceptions.
         */
        if (!isLeapYear(hebrewYear) && dayOfWeek == 3 && (hours * 1080) + chalakim >= (9 * 1080) + 204) {
            /*
             * This prevents the year from being 356 days. We have to push Rosh Hashanah off two days because if we pushed it off
             * only one day, Rosh Hashanah would comes out on a Wednesday. Check the Hebrew year 5745 for an example.
             */
            dayOfWeek = 5;
            days += 2;
        } else if (isLeapYear(hebrewYear - 1) && dayOfWeek == 2 && (hours * 1080) + chalakim >= (15 * 1080) + 589) {
            /*
             * This prevents the previous year from being 382 days. Check the Hebrew Year 5766 for an example. If Rosh Hashanah was
             * not pushed off a day then 5765 would be 382 days
             */
            dayOfWeek = 3;
            days += 1;
        } else {
            // see rule 2 above. Check the Hebrew year 5765 for an example
            if (hours >= 18) {
                dayOfWeek += 1;
                dayOfWeek = dayOfWeek % 7;
                days += 1;
            }
            // see rule 1 above. Check the Hebrew year 5765 for an example
            if (dayOfWeek == 1 || dayOfWeek == 4 || dayOfWeek == 6) {
                dayOfWeek += 1;
                dayOfWeek = dayOfWeek % 7;
                days += 1;
            }
        }

        // Adjust by the number of days since creation for 1 Jan 1900 - starting point for making date adjustments since Java dates
        // are around the epoch
        days -= 2067025;

        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.clear();
        c.set(1900, 0, 1, 0, 0);
        c.add(Calendar.DATE, days);

        // Sep 14, 1752, when Gregorian was adopted by England and its colonies
        Calendar gregorianReformation = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        gregorianReformation.clear();
        gregorianReformation.set(1752, Calendar.SEPTEMBER, 14, 0, 0);

        // Adjust for the Gregorian Reformation if needed
        if (c.before(gregorianReformation)) {
            c.add(Calendar.DATE, -10);
        }

        return c.getTime();
    }

    /**
     * This function gets the number of days of a Hebrew year.
     * 
     * @param hebrewYear
     *            the Hebrew year
     * @return the number of days in the year
     */
    private int getLengthOfYear(int hebrewYear) {
        // subtract the date of this year from the date of next year
        Date thisNewYear = getFirstDayOfHebrewYear(hebrewYear);
        Date nextNewYear = getFirstDayOfHebrewYear(hebrewYear + 1);
        return (int) TimeUnit.DAYS.convert(nextNewYear.getTime() - thisNewYear.getTime(), TimeUnit.MILLISECONDS);
    }

    /**
     * This function returns how many months there has been from the first Molad of the year supplied.
     * 
     * @param hebrewYear
     *            the Hebrew year
     * @return the number of months since the first Molad
     */
    private int getMonthsSinceFirstMolad(int hebrewYear) {
        // The months of this year haven't happened yet, so go back a year
        int y = hebrewYear - 1;

        // Get how many 19 year cycles there has been and multiply it by 235 (which is the number of months in a 19-year cycle)
        int result = (int) (Math.floor(y / 19) * 235);

        // Get the remaining years after the last complete 19 year cycle.
        y = yearInLeapCycle(y);

        // Add 12 months for each of those years...
        result += 12 * y;

        // and then add the extra months to account for the leap years
        if (y >= 17) {
            result += 6;
        } else if (y >= 14) {
            result += 5;
        } else if (y >= 11) {
            result += 4;
        } else if (y >= 8) {
            result += 3;
        } else if (y >= 6) {
            result += 2;
        } else if (y >= 3) {
            result += 1;
        }
        return result;
    }

    /**
     * This function returns if a given year is a leap year.
     * 
     * @param hebrewYear
     *            the Hebrew year
     * @return true if and only if the hebrew year supplied is a leap year
     */
    private boolean isLeapYear(int hebrewYear) {
        int yearInCycle = yearInLeapCycle(hebrewYear);

        return (yearInCycle == 3 || yearInCycle == 6 || yearInCycle == 8 || yearInCycle == 11 || yearInCycle == 14
                || yearInCycle == 17 || yearInCycle == 0);
    }

    /**
     * Find out which year we are within the leap-year cycle. Since the cycle lasts 19 years, the 19th year of the cycle will return
     * 0.
     * 
     * @param hebrewYear
     *            the Hebrew year
     * @return which year within the cycle we're in. The 19th year of the cycle is zero.
     */
    private int yearInLeapCycle(int hebrewYear) {
        return hebrewYear % 19;
    }

}
