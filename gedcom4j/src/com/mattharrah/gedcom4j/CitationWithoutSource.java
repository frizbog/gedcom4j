package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

public class CitationWithoutSource extends Citation {
	public List<String> description = new ArrayList<String>();
	public List<List<String>> textFromSource = new ArrayList<List<String>>();
	public List<Note> notes = new ArrayList<Note>();
}
