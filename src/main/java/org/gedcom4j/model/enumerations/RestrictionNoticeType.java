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
 * Allowable values for Restriction Notices.
 * 
 * @author frizbog
 */
public enum RestrictionNoticeType {
    /** Confidential */
    CONFIDENTIAL("confidential", "Marked as confidential by user"),
    /** Locked by custodian */
    LOCKED("locked", "Marked as unchangable by Ancestral File custodian"),
    /** Redacted for privacy */
    PRIVACY("privacy", "Information not disclosed to protect privacy");

    /**
     * Gets the value by its code
     *
     * @param code
     *            the code
     * @return the code, or null if no matching value can be found
     */
    public static RestrictionNoticeType getForCode(String code) {
        for (RestrictionNoticeType e : values()) {
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
    RestrictionNoticeType(String code, String description) {
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
