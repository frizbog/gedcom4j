package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

public class Submitter {
	public String xref;
	public String regFileNumber;
	public String name;
	public String regIdNumber;
	public List<String> languagePref = new ArrayList<String>();
	public Address address;
	public List<String> phoneNumbers = new ArrayList<String>();
	public List<MultimediaLink> multimediaLinks = new ArrayList<MultimediaLink>();
	public ChangeDate changeDate;
	public List<UserReference> userReferences = new ArrayList<UserReference>();
}
