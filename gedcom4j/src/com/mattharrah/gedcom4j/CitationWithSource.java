package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

public class CitationWithSource extends Citation {
	public String whereInSource;
	public String certainty;
	public String eventCited;
	public List<CitationData> data = new ArrayList<CitationData>();
	public Source source;
	public List<Note> notes = new ArrayList<Note>();
	public List<MultimediaLink> multimediaLinks = new ArrayList<MultimediaLink>();
}
