package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

public class Multimedia {
	public String xref;
	public String format;
	public String title;
	public String fileReference;
	public List<Note> notes = new ArrayList<Note>();
	public List<Citation> citations = new ArrayList<Citation>();
	public List<String> blob = new ArrayList<String>();
	public Multimedia continuedObject;
	public List<UserReference> userReferences = new ArrayList<UserReference>();
	public ChangeDate changeDate;
	public String recIdNumber;

}
