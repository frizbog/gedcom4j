/*
 * Copyright (c) 2009-2011 Matthew R. Harrah
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
package com.mattharrah.gedcom4j;

/**
 * <p>
 * The types of attributes that can be recorded for an individual. This enum
 * covers the valid tags for an individual attribute. Corresponds to
 * ATTRIBUTE_TYPE in the GEDCOM spec.
 * </p>
 * 
 * @author frizbog1
 * 
 */
public enum IndividualAttributeType {
	CASTE_NAME("CAST", "Caste Type"), PHYSICAL_DESCRIPTION("DSCR",
	        "Physical Description"), SCHOLASTIC_ACHIEVEMENT("EDUC",
	        "Scholastic Achievement"), NATIONAL_ID_NUMBER("IDNO",
	        "National ID Number"), NATIONAL_OR_TRIBAL_ORIGIN("NATI",
	        "National or Tribal Origin"), COUNT_OF_CHILDREN("NCHI",
	        "Number of Children"), COUNT_OF_MARRIAGES("NMR",
	        "Number of Marriages"), OCCUPATION("OCCU", "Occupation"), POSSESSIONS(
	        "PROP", "Possessions"), RELIGIOUS_AFFILIATION("RELI",
	        "Religious Affiliation"), RESIDENCE("RESI", "Residence"), SOCIAL_SECURITY_NUMBER(
	        "SSN", "Social Security Number"), NOBILITY_TYPE_TITLE("TITL",
	        "Nobility Type Title");

	public static IndividualAttributeType getFromTag(String tag) {
		for (IndividualAttributeType t : values()) {
			if (t.tag.equals(tag)) {
				return t;
			}
		}
		return null;
	}

	public static boolean isValidTag(String tag) {
		return (getFromTag(tag) != null);
	}

	public final String tag;

	public final String display;

	private IndividualAttributeType(String tag, String display) {
		this.tag = tag;
		this.display = display;
	}
}
