package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

public class Family {
	public String regFileNumber;
	public String recIdNumber;
	public Individual wife;
	public Individual husband;
	public List<Individual> children = new ArrayList<Individual>();
	public Integer numChildren;
	public List<Individual> submitters = new ArrayList<Individual>();
	public List<LdsFamilyOrdinance> ldsSpouseSealings = new ArrayList<LdsFamilyOrdinance>();
	public List<Citation> citations = new ArrayList<Citation>();
	public List<Multimedia> multimedia = new ArrayList<Multimedia>();
	public ChangeDate changeDate;
	public List<FamilyEvent> events = new ArrayList<FamilyEvent>();
	public List<Note> notes = new ArrayList<Note>();
	public String xref;
	public List<UserReference> userReferences = new ArrayList<UserReference>();
}
