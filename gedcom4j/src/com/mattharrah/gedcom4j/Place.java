package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

public class Place {
	public String placeName;
	public String placeFormat;
	public List<Citation> citations = new ArrayList<Citation>();
	public List<Note> notes = new ArrayList<Note>();
}
