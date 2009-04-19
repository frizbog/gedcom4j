package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

public class Event {
	public Address address;
	public List<String> phoneNumbers = new ArrayList<String>();
	public String age;
	public String cause;
	public List<Citation> citations = new ArrayList<Citation>();
	public String date;
	public String description;
	public List<Multimedia> multimedia = new ArrayList<Multimedia>();
	public List<Note> notes = new ArrayList<Note>();
	public Place place;
	public String respAgency;
}
