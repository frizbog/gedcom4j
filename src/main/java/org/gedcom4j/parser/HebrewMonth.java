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

/**
 * An enumeration of the Hebrew Months
 * 
 * @author frizbog
 */
enum HebrewMonth {

    /** Tishrei */
    TISHREI("TSH"),
    /** Cheshvan */
    CHESHVAN("CSH"),
    /** Kislev */
    KISLEV("KSL"),
    /** Teves */
    TEVES("TVT"),
    /** Shevat */
    SHEVAT("SHV"),
    /** Adar_A (only valid on leap years) */
    ADAR_A("ADR"),
    /** Adar (called Adar B for leap years) */
    ADAR("ADS"),
    /** Nisan */
    NISAN("NSN"),
    /** Iyar */
    IYAR("IYR"),
    /** Sivan */
    SIVAN("SVN"),
    /** Tamuz */
    TAMUZ("TMZ"),
    /** Av */
    AV("AAV"),
    /** Elul */
    ELUL("ELL");

    /**
     * Get the Hebrew month by number. The number is 1-based (i.e., 1 is Tishrei).
     * 
     * @param hebrewMonth
     *            the 1-based number of the month (i.e., 1 is Tishrei)
     * @return the Hebrew month
     */
    public static HebrewMonth getFrom1BasedNumber(int hebrewMonth) {
        return HebrewMonth.values()[hebrewMonth - 1];
    }

    /**
     * Get an enum value from the gedcom abbreviation
     * 
     * @param abbrev
     *            The GEDCOM spec abbreviation for this month
     * @return the enum constant that matches the abbreviation
     */
    public static HebrewMonth getFromAbbreviation(String abbrev) {
        for (HebrewMonth hm : values()) {
            if (hm.gedcomAbbrev.equals(abbrev)) {
                return hm;
            }
        }
        return null;
    }

    /**
     * The GEDCOM spec abbreviation for this month
     */
    private final String gedcomAbbrev;

    /**
     * Constructor
     * 
     * @param gedcomAbbrev
     *            The GEDCOM spec abbreviation for this month
     */
    HebrewMonth(String gedcomAbbrev) {
        this.gedcomAbbrev = gedcomAbbrev;
    }

    /**
     * Get the gedcomAbbrev
     * 
     * @return the gedcomAbbrev
     */
    public String getGedcomAbbrev() {
        return gedcomAbbrev;
    }

}