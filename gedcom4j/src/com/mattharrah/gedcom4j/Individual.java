package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

/**
 * An individual person.
 * 
 * @author Matt
 */
public class Individual {

	public String recFileNumber;
	public String recIdNumber;
	public String ancestralFileNumber;
	public String sex;
	public String permanentRecFileNumber;
	public List<PersonalName> names = new ArrayList<PersonalName>();
	public List<Association> associations = new ArrayList<Association>();
	public List<Submitter> ancestorInterest = new ArrayList<Submitter>();
	public List<Submitter> descendantInterest = new ArrayList<Submitter>();
	public List<UserReference> userReferences = new ArrayList<UserReference>();
	public List<IndividualEvent> events = new ArrayList<IndividualEvent>();
	public List<IndividualAttribute> attributes = new ArrayList<IndividualAttribute>();
	public List<Citation> citations = new ArrayList<Citation>();
	public List<MultimediaLink> multimediaLinks = new ArrayList<MultimediaLink>();
	public List<Note> notes = new ArrayList<Note>();
	public ChangeDate changeDate;
	public List<FamilySpouse> familiesWhereSpouse = new ArrayList<FamilySpouse>();
	public List<FamilyChild> familiesWhereChild = new ArrayList<FamilyChild>();
	public LdsOrdinance ldsIndividualOrdinance;
	public List<String> aliases = new ArrayList<String>();
}
