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
package org.gedcom4j.model.enumerations;

/**
 * Allowed statuses for LDS Endowments
 * 
 * @author frizbog
 */
public enum LdsEndowmentDateStatus {

    /** child */
    CHILD("CHILD", "Died before becoming eight years old."),

    /** completed */
    COMPLETED("COMPLETED", "Completed but the date is not known."),

    /** excluded */
    EXCLUDED("EXCLUDED", "Patron excluded this ordinance from being cleared in this submission."),

    /** pre 1970 */
    PRE_1970("PRE-1970", "Ordinance from temple records of work completed before 1970, assumed complete."),

    /** stillborn */
    STILLBORN("STILLBORN", "Stillborn, baptism not required."),

    /** submitted */
    SUBMITTED("SUBMITTED", "Ordinance was previously submitted."),

    /** uncleared */
    UNCLEARED("UNCLEARED", "Data for clearing ordinance request was insufficient.");

    /**
     * Gets the value by its code
     *
     * @param code
     *            the code
     * @return the code, or null if no matching value can be found
     */
    public static LdsEndowmentDateStatus getForCode(String code) {
        for (LdsEndowmentDateStatus e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }

    /**
     * The code value
     */
    private final String code;

    /**
     * Description of the problem
     */
    private final String description;

    /**
     * Constructor
     * 
     * @param code
     *            the code value
     * @param description
     *            the description of the problem
     */
    LdsEndowmentDateStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Get the code
     * 
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Get the description
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return code;
    }

}
