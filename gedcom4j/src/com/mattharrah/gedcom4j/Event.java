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
 * Represents an event. Corresponds to EVENT_DETAIL in the GEDCOM spec.
 * 
 * @author frizbog1
 * 
 */
public class Event {
	/**
	 * The address where this event took place
	 */
	public Address address;
	/**
	 * The phone numbers involved with this event
	 */
	public List<String> phoneNumbers = new ArrayList<String>();
	/**
	 * The age of the person to whom this event is attached at the time it
	 * occurred
	 */
	public String age;
	/**
	 * The cause of the event
	 */
	public String cause;
	/**
	 * List of source citations for this event
	 */
	public List<Citation> citations = new ArrayList<Citation>();
	/**
	 * The date of this event
	 */
	public String date;
	/**
	 * A description of this event
	 */
	public String description;
	/**
	 * Multimeda for this event
	 */
	public List<Multimedia> multimedia = new ArrayList<Multimedia>();
	/**
	 * Notes for this event
	 */
	public List<Note> notes = new ArrayList<Note>();
	/**
	 * The place where this event occurred
	 */
	public Place place;
	/**
	 * The responsible agency for this event
	 */
	public String respAgency;
	/**
	 * Either a Y or a null after the event type;
	 */
	public String yNull;
	/**
	 * A subtype that further qualifies the type
	 */
	public String subType;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Event)) {
			return false;
		}
		Event other = (Event) obj;
		if (address == null) {
			if (other.address != null) {
				return false;
			}
		} else if (!address.equals(other.address)) {
			return false;
		}
		if (age == null) {
			if (other.age != null) {
				return false;
			}
		} else if (!age.equals(other.age)) {
			return false;
		}
		if (cause == null) {
			if (other.cause != null) {
				return false;
			}
		} else if (!cause.equals(other.cause)) {
			return false;
		}
		if (citations == null) {
			if (other.citations != null) {
				return false;
			}
		} else if (!citations.equals(other.citations)) {
			return false;
		}
		if (date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!date.equals(other.date)) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (multimedia == null) {
			if (other.multimedia != null) {
				return false;
			}
		} else if (!multimedia.equals(other.multimedia)) {
			return false;
		}
		if (notes == null) {
			if (other.notes != null) {
				return false;
			}
		} else if (!notes.equals(other.notes)) {
			return false;
		}
		if (phoneNumbers == null) {
			if (other.phoneNumbers != null) {
				return false;
			}
		} else if (!phoneNumbers.equals(other.phoneNumbers)) {
			return false;
		}
		if (place == null) {
			if (other.place != null) {
				return false;
			}
		} else if (!place.equals(other.place)) {
			return false;
		}
		if (respAgency == null) {
			if (other.respAgency != null) {
				return false;
			}
		} else if (!respAgency.equals(other.respAgency)) {
			return false;
		}
		if (subType == null) {
			if (other.subType != null) {
				return false;
			}
		} else if (!subType.equals(other.subType)) {
			return false;
		}
		if (yNull == null) {
			if (other.yNull != null) {
				return false;
			}
		} else if (!yNull.equals(other.yNull)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime * result + ((cause == null) ? 0 : cause.hashCode());
		result = prime * result
		        + ((citations == null) ? 0 : citations.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
		        + ((description == null) ? 0 : description.hashCode());
		result = prime * result
		        + ((multimedia == null) ? 0 : multimedia.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result
		        + ((phoneNumbers == null) ? 0 : phoneNumbers.hashCode());
		result = prime * result + ((place == null) ? 0 : place.hashCode());
		result = prime * result
		        + ((respAgency == null) ? 0 : respAgency.hashCode());
		result = prime * result + ((subType == null) ? 0 : subType.hashCode());
		result = prime * result + ((yNull == null) ? 0 : yNull.hashCode());
		return result;
	}
}
