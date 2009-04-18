package com.mattharrah.gedcom4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Main (root) class for an entire GEDCOM file.
 * @author Matt
 */
public class Gedcom {
	public Header header;
	public Submitter submitter;
	public Map<String, Individual> individuals = new HashMap<String, Individual>();
	public Map<String, Note> notes = new HashMap<String, Note>();
	public Map<String, Family> families = new HashMap<String, Family>();
	public Map<String, Source> sources = new HashMap<String, Source>();
	public Trailer trailer;
}
