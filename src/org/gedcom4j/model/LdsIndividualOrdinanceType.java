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
 * An enumeration of the known LDS Individual Ordinance types.
 * 
 * @author frizbog1
 * 
 */
public enum LdsIndividualOrdinanceType {
    /**
     * Baptism
     */
    BAPTISM("BAPL", "LDS Baptism"),
    /**
     * Confirmation
     */
    CONFIRMATION("CONL", "LDS Confirmation"),
    /**
     * Endowment
     */
    ENDOWMENT("ENDL", "LDS Endowment"),
    /**
     * Child sealing
     */
    CHILD_SEALING("SLGC", "LDS Child Sealing");

    /**
     * Get an enum constant from the tag it corresponds to
     * 
     * @param tag
     *            the tag
     * @return the corresponding enum constant for the supplied tag (if any)
     */
    public static LdsIndividualOrdinanceType getFromTag(String tag) {
        for (LdsIndividualOrdinanceType t : values()) {
            if (t.tag.equals(tag)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Is the supplied tag value one of the known tags for an LDS individual
     * ordinance?
     * 
     * @param tag
     *            the tag
     * @return true if and only if the supplied tag corresponds to a known LDS
     *         individual ordinance type
     */
    public static boolean isValidTag(String tag) {
        return (getFromTag(tag) != null);
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
     * Contructor
     * 
     * @param tag
     *            the tag
     * @param display
     *            the display value
     */
    private LdsIndividualOrdinanceType(String tag, String display) {
        this.tag = tag;
        this.display = display;
    }
}
