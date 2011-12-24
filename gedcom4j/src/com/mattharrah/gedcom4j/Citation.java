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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An abstract base class for other source citations (both with and without
 * source)
 * 
 * @author frizbog1
 * 
 */
public abstract class Citation {

	/**
	 * Notes on this source citation
	 */
	public List<Note> notes = new ArrayList<Note>();

	/**
	 * This method gets a set of all the XREFs of all the notes in this
	 * Citation. This can be used in equals() implementations to avoid circular
	 * references (e.g., Note -> CitationWithSource -> Source -> Repository ->
	 * Note)
	 * 
	 * @return a set of xrefs of all the notes on this citation.
	 */
	public Set<String> getNoteXrefs() {
		Set<String> result = new HashSet<String>();
		for (Note n : notes) {
			result.add(n.xref);
		}
		return result;
	}

}
