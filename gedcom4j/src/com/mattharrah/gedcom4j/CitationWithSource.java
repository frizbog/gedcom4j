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
 * A citation with a source. Corresponds to the first (preferred) form of the
 * SOURCE_CITATION structure (which you'd do in Pascal with a variant record,
 * but here we use subclasses of a parent abstract class).
 * 
 * @author frizbog1
 * 
 */
public class CitationWithSource extends Citation {
	/**
	 * Where within the source is the information being cited
	 */
	public String whereInSource;
	/**
	 * The quality of this citation. Supposed to be 0, 1, 2, or 3, but stored as
	 * a string since we're not doing math on it.
	 */
	public String certainty;
	/**
	 * The type of event or attribute cited from. Will be the tag from one of
	 * the the following three enum types: {@link FamilyEventType},
	 * {@link IndividualEventType}, {@link IndividualAttributeType}.
	 */
	public String eventCited;
	/**
	 * A list of citation data entries
	 */
	public List<CitationData> data = new ArrayList<CitationData>();
	/**
	 * A reference to the cited source
	 */
	public Source source;
	/**
	 * Notes on this source citation
	 */
	public List<Note> notes = new ArrayList<Note>();
	/**
	 * Multimedia links for this source citation
	 */
	public List<Multimedia> multimedia = new ArrayList<Multimedia>();
}
