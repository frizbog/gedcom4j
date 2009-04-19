package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

public class Source {
	public String regFileNumber;
	public String recIdNumber;
	public String sourceFiledBy;
	public List<String> title = new ArrayList<String>();
	public List<Note> notes = new ArrayList<Note>();
	public List<String> publicationFacts = new ArrayList<String>();
	public List<String> originatorsAuthors = new ArrayList<String>();
	public List<Multimedia> multimedia = new ArrayList<Multimedia>();
	public ChangeDate changeDate;
	public List<UserReference> userReferences = new ArrayList<UserReference>();
	public SourceData data;
	public List<String> sourceText = new ArrayList<String>();
	public RepositoryCitation repositoryCitation;
	public String xref;

}
