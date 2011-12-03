/*
 * Copyright (c) 2009 Matthew R. Harrah
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

public enum LdsIndividualOrdinanceType {
	BAPTISM("BAPL", "LDS Baptism"), CONFIRMATION("CONL", "LDS Confirmation"), ENDOWMENT(
	        "ENDL", "LDS Endowment"), CHILD_SEALING("SLGC", "LDS Child Sealing");

	public static LdsIndividualOrdinanceType getFromTag(String tag) {
		for (LdsIndividualOrdinanceType t : values()) {
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

	private LdsIndividualOrdinanceType(String tag, String display) {
		this.tag = tag;
		this.display = display;
	}
}
