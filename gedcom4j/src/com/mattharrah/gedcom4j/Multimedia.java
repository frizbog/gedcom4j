/*
 * Copyright (c) 2009-2011 Matthew R. Harrah
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
package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for representing multimedia items. Corrsponds to MULTIMEDIA_RECORD in
 * the GEDCOM standard.
 * 
 * @author frizbog1
 * 
 */
public class Multimedia {
	/**
	 * The xref for this multimedia item
	 */
	public String xref;
	/**
	 * The format of this multimedia item
	 */
	public String format;
	/**
	 * The title of this multimedia item
	 */
	public String title;
	/**
	 * The file reference for this multimedia item
	 */
	public String fileReference;
	/**
	 * Notes for this multimedia item
	 */
	public List<Note> notes = new ArrayList<Note>();
	/**
	 * Source citations for this multimedia item
	 */
	public List<Citation> citations = new ArrayList<Citation>();
	/**
	 * The binary (blob) for this multimedia item. Encoded as string data.
	 */
	public List<String> blob = new ArrayList<String>();
	/**
	 * The next object in the chain holding binary data if it needs to be
	 * continued due to size
	 */
	public Multimedia continuedObject;
	/**
	 * User references
	 */
	public List<UserReference> userReferences = new ArrayList<UserReference>();
	/**
	 * The change date for this multimedia item
	 */
	public ChangeDate changeDate;
	/**
	 * The record id number for this multimedia item
	 */
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
		Multimedia other = (Multimedia) obj;
		if (blob == null) {
			if (other.blob != null) {
				return false;
			}
		} else if (!blob.equals(other.blob)) {
			return false;
		}
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
		if (continuedObject == null) {
			if (other.continuedObject != null) {
				return false;
			}
		} else if (!continuedObject.equals(other.continuedObject)) {
			return false;
		}
		if (fileReference == null) {
			if (other.fileReference != null) {
				return false;
			}
		} else if (!fileReference.equals(other.fileReference)) {
			return false;
		}
		if (format == null) {
			if (other.format != null) {
				return false;
			}
		} else if (!format.equals(other.format)) {
			return false;
		}
		if (notes == null) {
			if (other.notes != null) {
				return false;
			}
		} else if (!notes.equals(other.notes)) {
			return false;
		}
		if (recIdNumber == null) {
			if (other.recIdNumber != null) {
				return false;
			}
		} else if (!recIdNumber.equals(other.recIdNumber)) {
			return false;
		}
		if (title == null) {
			if (other.title != null) {
				return false;
			}
		} else if (!title.equals(other.title)) {
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
		result = prime * result + ((blob == null) ? 0 : blob.hashCode());
		result = prime * result
		        + ((changeDate == null) ? 0 : changeDate.hashCode());
		result = prime * result
		        + ((citations == null) ? 0 : citations.hashCode());
		result = prime * result
		        + ((continuedObject == null) ? 0 : continuedObject.hashCode());
		result = prime * result
		        + ((fileReference == null) ? 0 : fileReference.hashCode());
		result = prime * result + ((format == null) ? 0 : format.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result
		        + ((recIdNumber == null) ? 0 : recIdNumber.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result
		        + ((userReferences == null) ? 0 : userReferences.hashCode());
		result = prime * result + ((xref == null) ? 0 : xref.hashCode());
		return result;
	}

}
