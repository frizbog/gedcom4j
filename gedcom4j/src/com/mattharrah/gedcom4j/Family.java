package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

public class Family {
	public String recFileNumber;
	public String recIdNumber;
	public Individual mother;
	public Individual father;
	public List<Individual> children = new ArrayList<Individual>();
	public Integer numChildren;
	public List<Submitter> submitters = new ArrayList<Submitter>();
	public List<LdsOrdinance> ldsSpouseSealings = new ArrayList<LdsOrdinance>();
	public List<Citation> citations = new ArrayList<Citation>();
	public List<MultimediaLink> multimediaLinks = new ArrayList<MultimediaLink>();
	public ChangeDate changeDate;
	public List<FamilyEvent> events = new ArrayList<FamilyEvent>();
}
