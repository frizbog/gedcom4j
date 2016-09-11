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
    CROSS_REFERENCE_NOT_FOUND("Cross-referenced item could not be found in the GEDCOM"),
    /**
     * Duplicated item
     */
    DUPLICATE_VALUE("Value is a duplicate"),
    /**
     * Value is there but not allowed
     */
    ILLEGAL_VALUE("Value supplied is not allowed"),
    /**
     * List with null value
     */
    LIST_WITH_NULL_VALUE("List contains null value"),
    /**
     * Missing a required value
     */
    MISSING_REQUIRED_VALUE("Required value is missing"),
    /**
     * Not allowed in GEDCOM 5.5
     */
    NOT_ALLOWED_IN_GEDCOM_55("Not allowed in GEDCOM 5.5, only in GEDCOM 5.5.1"),
    /**
     * Not allowed in GEDCOM 5.5.1
     */
    NOT_ALLOWED_IN_GEDCOM_551("Not allowed in GEDCOM 5.5.1, only in GEDCOM 5.5"),
    /**
     * Uninitialized collection when Options are set to auto-initialize collections
     */
    UNINITIALIZED_COLLECTION("Collection is uninitialized");

    /**
     * Description of the problem
     */
    private final String description;

    /**
     * Constructor
     * 
     * @param description
     *            the description of the problem
     */
    ProblemCode(String description) {
        this.description = description;
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
        return ordinal() + ": " + description;
    }
}
