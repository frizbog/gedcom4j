package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

public class Repository {
	public String xref;
	public String name;
	public String regFileNumber;
	public String recIdNumber;
	public Address address;
	public List<Note> notes = new ArrayList<Note>();
	public ChangeDate changeDate;
	public List<UserReference> userReferences = new ArrayList<UserReference>();
	public List<String> phoneNumbers = new ArrayList<String>();

}
