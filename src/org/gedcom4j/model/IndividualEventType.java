/*
 * Copyright (c) 2009-2012 Matthew R. Harrah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.gedcom4j.model;

/**
 * An event type that occurs for an individual. This enum lists the valid tags for individual events,
 * 
 * @author frizbog1
 * 
 */
public enum IndividualEventType {
    /**
     * Adoption
     */
    ADOPTION("ADOP", "Adoption"),
    /**
     * Arrival
     */
    ARRIVAL("ARVL", "Arrival"),
    /**
     * Birth
     */
    BIRTH("BIRT", "Birth"),
    /**
     * Baptism
     */
    BAPTISM("BAPM", "Baptism"),
    /**
     * Bar Mitzvah
     */
    BAR_MITZVAH("BARM", "Bar Mitzvah"),
    /**
     * Bas Mitzvah
     */
    BAS_MITZVAH("BASM", "Bas Miztvah"),
    /**
     * Blessing
     */
    BLESSING("BLES", "Blessing"),
    /**
     * Burial
     */
    BURIAL("BURI", "Burial"),
    /**
     * Census
     */
    CENSUS("CENS", "Census"),
    /**
     * Christening
     */
    CHRISTENING("CHR", "Christening"),
    /**
     * Christening as an adult
     */
    CHRISTENING_ADULT("CHRA", "Christening (Adult)"),
    /**
     * Confirmation
     */
    CONFIRMATION("CONF", "Confirmation"),
    /**
     * Cremation
     */
    CREMATION("CREM", "Cremation"),
    /**
     * Death
     */
    DEATH("DEAT", "Death"),
    /**
     * Emigration
     */
    EMIGRATION("EMIG", "Emigration"),
    /**
     * First Communion
     */
    FIRST_COMMUNION("FCOM", "First Communion"),
    /**
     * Graduation
     */
    GRADUATION("GRAD", "Graduation"),
    /**
     * Immigration
     */
    IMMIGRATION("IMMI", "Immigration"),
    /**
     * Naturalization
     */
    NATURALIZATION("NATU", "Naturalization"),
    /**
     * Ordination
     */
    ORDINATION("ORDN", "Ordination"),
    /**
     * Retirement
     */
    RETIREMENT("RETI", "Retirement"),
    /**
     * Probate
     */
    PROBATE("PROB", "Probate"),
    /**
     * Will
     */
    WILL("WILL", "Will"),
    /**
     * Event
     */
    EVENT("EVEN", "Event");

    /**
     * Get an individual event type enum constant from its tag
     * 
     * @param tag
     *            the tag (as a string)
     * @return the individual event enum constant that corresponds to the tag
     */
    public static IndividualEventType getFromTag(String tag) {
        for (IndividualEventType t : values()) {
            if (t.tag.equals(tag)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Is the string supplied a tag that corresponds to an enumerated Individual Event Type?
     * 
     * @param tag
     *            the tag being tested
     * @return true if and only if the tag corresponds to an enumerated individual event type
     */
    public static boolean isValidTag(String tag) {
        return getFromTag(tag) != null;
    }

    /**
     * The tag
     */
    public final String tag;

    /**
     * The display value
     */
    public final String display;

    /**
     * Private constructor
     * 
     * @param tag
     *            the tag
     * @param display
     *            the display value
     */
    private IndividualEventType(String tag, String display) {
        this.tag = tag;
        this.display = display;
    }
}
