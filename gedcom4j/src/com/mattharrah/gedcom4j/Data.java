package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

public class Data {
	public String type;
	public String respAgency;
	public List<EventRecorded> eventsRecorded = new ArrayList<EventRecorded>();
	public List<Note> notes = new ArrayList<Note>();
}
