package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

/**
 * An individual person.
 * 
 * @author Matt
 */
public class Individual {
	public List<Individual> aliases = new ArrayList<Individual>();
	public List<Individual> ancestorInterest = new ArrayList<Individual>();
	public String ancestralFileNumber;
	public List<Association> associations = new ArrayList<Association>();
	public List<IndividualAttribute> attributes = new ArrayList<IndividualAttribute>();
	public ChangeDate changeDate;
	public List<Citation> citations = new ArrayList<Citation>();
	public List<Individual> descendantInterest = new ArrayList<Individual>();
	public List<IndividualEvent> events = new ArrayList<IndividualEvent>();
	public List<FamilyChild> familiesWhereChild = new ArrayList<FamilyChild>();
	public List<FamilySpouse> familiesWhereSpouse = new ArrayList<FamilySpouse>();
	public List<LdsIndividualOrdinance> ldsIndividualOrdinances = new ArrayList<LdsIndividualOrdinance>();
	public List<Multimedia> multimedia = new ArrayList<Multimedia>();
	public List<PersonalName> names = new ArrayList<PersonalName>();
	public List<Note> notes = new ArrayList<Note>();
	public String permanentRecFileNumber;
	public String recIdNumber;
	public String regFileNumber;
	public String restrictionNotice;
	public String sex;
	public List<Individual> submitters = new ArrayList<Individual>();
	public List<UserReference> userReferences = new ArrayList<UserReference>();
	public String xref;
}
