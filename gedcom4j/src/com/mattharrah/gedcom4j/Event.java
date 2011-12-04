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
}
