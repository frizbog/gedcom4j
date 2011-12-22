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

import java.util.ArrayList;
import java.util.List;

public class Note {
	public String xref;
	public List<String> lines = new ArrayList<String>();
	public List<Citation> citations = new ArrayList<Citation>();
	public List<UserReference> userReferences = new ArrayList<UserReference>();
	public ChangeDate changeDate;
	public String recIdNumber;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Note other = (Note) obj;
		if (changeDate == null) {
			if (other.changeDate != null) {
				return false;
			}
		} else if (!changeDate.equals(other.changeDate)) {
			return false;
		}
		if (citations == null) {
			if (other.citations != null) {
				return false;
			}
		} else if (!citations.equals(other.citations)) {
			return false;
		}
		if (lines == null) {
			if (other.lines != null) {
				return false;
			}
		} else if (!lines.equals(other.lines)) {
			return false;
		}
		if (recIdNumber == null) {
			if (other.recIdNumber != null) {
				return false;
			}
		} else if (!recIdNumber.equals(other.recIdNumber)) {
			return false;
		}
		if (userReferences == null) {
			if (other.userReferences != null) {
				return false;
			}
		} else if (!userReferences.equals(other.userReferences)) {
			return false;
		}
		if (xref == null) {
			if (other.xref != null) {
				return false;
			}
		} else if (!xref.equals(other.xref)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
		        + ((changeDate == null) ? 0 : changeDate.hashCode());
		result = prime * result
		        + ((citations == null) ? 0 : citations.hashCode());
		result = prime * result + ((lines == null) ? 0 : lines.hashCode());
		result = prime * result
		        + ((recIdNumber == null) ? 0 : recIdNumber.hashCode());
		result = prime * result
		        + ((userReferences == null) ? 0 : userReferences.hashCode());
		result = prime * result + ((xref == null) ? 0 : xref.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Note [xref=" + xref + ", citations=" + citations
		        + ", userReferences=" + userReferences + ", changeDate="
		        + changeDate + ", recIdNumber=" + recIdNumber + ", lines="
		        + lines + "]";
	}
}
