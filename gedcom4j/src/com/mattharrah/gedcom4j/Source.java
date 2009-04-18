package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

public class Source {
	public String recFileNumber;
	public String recIdNumber;
	public String sourceFilledBy;
	public List<String> titles = new ArrayList<String>();
	public List<Note> notes = new ArrayList<Note>();
	public List<String> publicationFacts = new ArrayList<String>();
	public List<String> originatorsAuthors = new ArrayList<String>();
	public List<MultimediaLink> multimediaLinks = new ArrayList<MultimediaLink>();
	public ChangeDate changeDate;
	public List<UserReference> userReferences = new ArrayList<UserReference>();
	public List<Data> data = new ArrayList<Data>();
	public List<String> sourceText = new ArrayList<String>();
	public RepositoryCitation repositoryCitation; 

}
