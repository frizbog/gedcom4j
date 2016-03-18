/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
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
 * An event type that occurs for a family. This enum lists the valid tags for family events.
 * 
 * @author frizbog1
 * 
 */
public enum FamilyEventType {
    /**
     * Annullment
     */
    ANNULMENT("ANUL", "Annulment"),
    /**
     * Census
     */
    CENSUS("CENS", "Census"),
    /**
     * Divorce
     */
    DIVORCE("DIV", "Divorce"),
    /**
     * Divorce filed
     */
    DIVORCE_FILED("DIVF", "Divorce filed"),
    /**
     * Engagement
     */
    ENGAGEMENT("ENGA", "Engagement"),
    /**
     * Marriage
     */
    MARRIAGE("MARR", "Marriage"),
    /**
     * Marriage Banner
     */
    MARRIAGE_BANNER("MARB", "Marriage banner"),
    /**
     * Marriage Contract
     */
    MARRIAGE_CONTRACT("MARC", "Marriage contract"),
    /**
     * Marriage license
     */
    MARRIAGE_LICENSE("MARL", "Marriage license"),
    /**
     * Marriage settlement
     */
    MARRIAGE_SETTLEMENT("MARS", "Marriage settlement"),
    /**
     * General event
     */
    EVENT("EVEN", "Event");

    /**
     * The tag
     */
    public final String tag;

    /**
     * The display value for the tag
     */
    public final String display;

    /**
     * Constructor
     *
     * @param tag
     *            the tag for the enum constant
     * @param display
     *            the display value for the enum constant
     */
    private FamilyEventType(String tag, String display) {
        this.tag = tag;
        this.display = display;
    }

    /**
     * Get an enum type from its tag string
     * 
     * @param tag
     *            the tag as a string
     * @return the enum type that corresponds to the tag, or null if it's not a known tag
     */
    public static FamilyEventType getFromTag(String tag) {
        for (FamilyEventType t : values()) {
            if (t.tag.equals(tag)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Is the tag string a valid tag for a Family Event Type?
     * 
     * @param tag
     *            the tag as a string
     * @return true if and only if the tag is one of the known tags for Family Event types
     */
    public static boolean isValidTag(String tag) {
        return getFromTag(tag) != null;
    }

}
