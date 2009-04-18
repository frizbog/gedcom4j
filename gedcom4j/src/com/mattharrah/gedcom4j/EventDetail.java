package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

public class EventDetail {
	public List<String> eventDescriptor = new ArrayList<String>();
	public List<String> dates = new ArrayList<String>();
	public List<String> agesAtEvent = new ArrayList<String>();
	public List<String> respAgency = new ArrayList<String>();
	public List<String> causesOfEvent = new ArrayList<String>();
	public Address address;
	public List<Citation> citations = new ArrayList<Citation>();
	public List<MultimediaLink> multimediaLinks = new ArrayList<MultimediaLink>();
	public List<Note> notes = new ArrayList<Note>();
}
