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
package com.mattharrah.gedcom4j.model;

/**
 * <p>
 * The types of attributes that can be recorded for an individual. This enum covers the valid tags for an individual attribute. Corresponds to
 * ATTRIBUTE_TYPE in the GEDCOM spec.
 * </p>
 * 
 * @author frizbog1
 * 
 */
public enum IndividualAttributeType {
    /**
     * Caste name/type
     */
    CASTE_NAME("CAST", "Caste Type"),
    /**
     * Physical description
     */
    PHYSICAL_DESCRIPTION("DSCR", "Physical Description"),
    /**
     * Scholastic achievement
     */
    SCHOLASTIC_ACHIEVEMENT("EDUC", "Scholastic Achievement"),
    /**
     * National ID number
     */
    NATIONAL_ID_NUMBER("IDNO", "National ID Number"),
    /**
     * National or tribal origin
     */
    NATIONAL_OR_TRIBAL_ORIGIN("NATI", "National or Tribal Origin"),
    /**
     * Count of children
     */
    COUNT_OF_CHILDREN("NCHI", "Number of Children"),
    /**
     * Count of marriages
     */
    COUNT_OF_MARRIAGES("NMR", "Number of Marriages"),
    /**
     * Occupation
     */
    OCCUPATION("OCCU", "Occupation"),
    /**
     * Possessions
     */
    POSSESSIONS("PROP", "Possessions"),
    /**
     * Religous affiliation
     */
    RELIGIOUS_AFFILIATION("RELI", "Religious Affiliation"),
    /**
     * Residence
     */
    RESIDENCE("RESI", "Residence"),
    /**
     * Social Security Number
     */
    SOCIAL_SECURITY_NUMBER("SSN", "Social Security Number"),
    /**
     * Nobility type title
     */
    NOBILITY_TYPE_TITLE("TITL", "Title"),
    /**
     * Generic fact. New for GEDCOM 5.5.1.
     */
    FACT("FACT", "Fact");

    /**
     * Get an enum constant from its tag value
     * 
     * @param tag
     *            the tag value for the enum constant you want
     * @return the enum constant that matches the supplied tag, or null if no match was found
     */
    public static IndividualAttributeType getFromTag(String tag) {
        for (IndividualAttributeType t : values()) {
            if (t.tag.equals(tag)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Is the supplied tag a valid value for an Individual Attribute?
     * 
     * @param tag
     *            the tag we are testing
     * @return true if and only if the tag supplied is a valid tag for an Individual Attribute
     */
    public static boolean isValidTag(String tag) {
        return (getFromTag(tag) != null);
    }

    /**
     * The tag
     */
    public final String tag;

    /**
     * The display value for the type
     */
    public final String display;

    /**
     * Constructor
     * 
     * @param tag
     *            the tag
     * @param display
     *            the display value
     */
    private IndividualAttributeType(String tag, String display) {
        this.tag = tag;
        this.display = display;
    }
}
