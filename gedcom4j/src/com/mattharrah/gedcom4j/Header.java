package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

public class Header {
	public CharacterSet characterSet;
	public String copyrightData;
	public String date;
	public String destinationSystem;
	public String fileName;
	public GedcomVersion gedcomVersion;
	public Place place;
	public SourceSystem sourceSystem;
	public Submission submission;
	public Submitter submitter;
	public String time;
	public Transmission transmission;
	public String language;
	public List<String> notes = new ArrayList<String>();
}
