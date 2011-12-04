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
 * A cited event. Corresponds to the "Event Cited" item in the data model chart
 * at the end of the GEDCOM spec.
 * 
 * @author frizbog1
 * 
 */
public class EventCited {
	/**
	 * The event type. This is a tag that will be in either the
	 * {@link IndividualEventType} enum or the {@link FamilyEventType} enum, and
	 * corresponds to EVENT_TYPE_CITED_FROM in the GEDCOM spec.
	 */
	public String eventType;
	/**
	 * A description of the person's role in the event. Corresponds to
	 * ROLE_IN_EVENT in GEDCOM spec.
	 */
	public String roleInEvent;
}
