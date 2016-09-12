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
package org.gedcom4j.validate;

/**
 * An enumeration of predefined problem codes.
 * 
 * @author frizbog
 */
public enum ProblemCode {

    /**
     * Cross-reference not found
     */
    CROSS_REFERENCE_NOT_FOUND(0, "Cross-referenced item could not be found in the GEDCOM"),
    /**
     * Duplicated item
     */
    DUPLICATE_VALUE(1, "Value is a duplicate"),
    /**
     * Value is there but not allowed
     */
    ILLEGAL_VALUE(2, "Value supplied is not allowed"),
    /**
     * List with null value
     */
    LIST_WITH_NULL_VALUE(3, "List contains null value"),
    /**
     * Missing a required value
     */
    MISSING_REQUIRED_VALUE(4, "Required value is missing"),
    /**
     * Not allowed in GEDCOM 5.5
     */
    NOT_ALLOWED_IN_GEDCOM_55(5, "Not allowed in GEDCOM 5.5, only in GEDCOM 5.5.1"),
    /**
     * Not allowed in GEDCOM 5.5.1
     */
    NOT_ALLOWED_IN_GEDCOM_551(6, "Not allowed in GEDCOM 5.5.1, only in GEDCOM 5.5"),
    /**
     * Too many values in a list
     */
    TOO_MANY_VALUES(7, "Number of items exceeds maximum"),
    /**
     * Uninitialized collection when Options are set to auto-initialize collections
     */
    UNINITIALIZED_COLLECTION(8, "Collection is uninitialized"),
    /**
     * Bad XRef value
     */
    XREF_INVALID(9, "Xref is malformed - should begin and end with @-sign, and have at least one character in between");

    /*
     * Static initializer to check for skipped code numbers (which would also occur if there are duplicates)
     */
    static {
        for (int i = 0; i < values().length; i++) {
            if (getForCode(i) == null) {
                throw new IllegalStateException("No value found for code number " + i);
            }
        }
    }

    /**
     * Gets the value by its code number
     *
     * @param n
     *            the code number
     * @return the code, or null if no matching value can be found
     */
    public static ProblemCode getForCode(int n) {
        for (ProblemCode pc : values()) {
            if (pc.code == n) {
                return pc;
            }
        }
        return null;
    }

    /**
     * The code value
     */
    private final int code;

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
     * @throws IllegalArgumentException
     *             if the code value is a duplicate of some other
     */
    ProblemCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Get the code
     * 
     * @return the code
     */
    public int getCode() {
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
        return code + ": " + description;
    }
}
