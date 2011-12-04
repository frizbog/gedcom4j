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

import java.util.HashMap;
import java.util.Map;

/**
 * Main (root) class for an entire GEDCOM file.
 * 
 * @author frizbog1
 */
public class Gedcom {
	/**
	 * A map of all the families in the GEDCOM file. The map is keyed on family
	 * cross-reference numbers, and the families themselves are in the value
	 * set.
	 */
	public Map<String, Family> families = new HashMap<String, Family>();
	/**
	 * Header information about the GEDCOM
	 */
	public Header header;
	/**
	 * A map of all the individuals in the GEDCOM file. The map is keyed on the
	 * individual cross-reference numbers and the individuals themselves are in
	 * the value set.
	 */
	public Map<String, Individual> individuals = new HashMap<String, Individual>();
	/**
	 * A map of all the multimedia items in the GEDCOM file. The map is keyed by
	 * the multimedia cross-reference numbers, and the multimedia items
	 * themselves (well, the metadata about them) are in the value set.
	 * Remember, multimedia is not embedded in the GEDCOM, but the GEDCOM
	 * contains metadata about the multimedia.
	 */
	public Map<String, Multimedia> multimedia = new HashMap<String, Multimedia>();
	/**
	 * A map of notes
	 */
	public Map<String, Note> notes = new HashMap<String, Note>();
	/**
	 * A map of all the source repositories in the GEDCOM file. The map is keyed
	 * on the repository cross-reference numbers, and the repositories
	 * themsevles are in the value set.
	 */
	public Map<String, Repository> repositories = new HashMap<String, Repository>();
	/**
	 * A map of all the sources in the GEDCOM file. The map is keyed on source
	 * cross-reference numbers, and the sources themsevles are in the value set.
	 */
	public Map<String, Source> sources = new HashMap<String, Source>();
	/**
	 * Information about the GEDCOM submission.
	 */
	public Submission submission;
	/**
	 * A map of the submitters in the GEDCOM file. The map is keyed on submitter
	 * cross-reference numbers, and the submitters themselves are in the value
	 * set
	 */
	public Map<String, Submitter> submitters = new HashMap<String, Submitter>();
	/**
	 * The trailer of the file
	 */
	public Trailer trailer;
}
