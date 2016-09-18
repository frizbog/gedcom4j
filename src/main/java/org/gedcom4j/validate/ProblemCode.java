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
    XREF_INVALID(9, "Xref is malformed - should begin and end with @-sign, and have at least one character in between"),
    /**
     * Unable to determine GEDCOM version - assuming v5.5.1
     */
    UNABLE_TO_DETERMINE_GEDCOM_VERSION(10, "Unable to determine GEDCOM version - assuming v5.5.1"),
    /**
     * Not a valid email address (format)
     */
    NOT_VALID_EMAIL_ADDRESS(11, "Invalid email address"),
    /**
     * Not a valid URL (format)
     */
    NOT_VALID_WWW_URL(12, "Invalid WWW URL"),
    /**
     * Not a valid date string (format)
     */
    INVALID_DATE(13, "Invalid date"),
    /**
     * The child has surname(s) that do not match those of either parent
     */
    SURNAMES_DONT_MATCH_PARENTS(14, "Child has surname(s) that do not match those of either parent"),
    /**
     * The child's mother may not have been born at the time
     */
    MOTHER_MAY_NOT_HAVE_BEEN_BORN_YET(15, "Mother may not have been born yet"),
    /**
     * The child's mother was not sixteen yet at the time
     */
    MOTHER_WAS_LESS_THAN_SIXTEEN(16, "Mother may not have been 16 yet at the time"),
    /**
     * The child's father may not have been born at the time
     */
    FATHER_MAY_NOT_HAVE_BEEN_BORN_YET(17, "Father may not have been born yet"),
    /**
     * The child's father was not sixteen yet at the time
     */
    FATHER_WAS_LESS_THAN_SIXTEEN(18, "Mother may not have been 16 yet at the time"),
    /**
     * Mother may have been deceased at the time
     */
    MOTHER_MAY_HAVE_BEEN_DECEASED(19, "Mother may have been deceased at the time"),
    /**
     * Father may have been deceased at the time
     */
    FATHER_MAY_HAVE_BEEN_DECEASED(20, "Father may have been deceased at the time"),
    /**
     * Mother was sixty or older at the time
     */
    MOTHER_WAS_SIXTY_OR_OLDER(21, "Mother was sixty or older at the time"),
    /**
     * Father was sixty or older at the time
     */
    FATHER_WAS_SIXTY_OR_OLDER(22, "Father was sixty or older at the time"),
    /**
     * Husband less than sixteen years old at the time
     */
    HUSBAND_WAS_LESS_THAN_SIXTEEN(23, "Husband less than sixteen years old at the time"),
    /**
     * Wife less than sixteen years old at the time
     */
    WIFE_WAS_LESS_THAN_SIXTEEN(24, "Wife less than sixteen years old at the time"),
    /**
     * Date is in the future
     */
    DATE_IN_FUTURE(25, "Date is in the future");

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
