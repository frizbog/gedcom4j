package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

/**
 * An LDS Ordinance
 * 
 * @author Matt
 */
public class LdsIndividualOrdinance {
	public LdsIndividualOrdinanceType type;
	public String status;
	public String date;
	public String temple;
	public String place;
	public FamilyChild familyWhereChild;
	public List<Citation> citations = new ArrayList<Citation>();
	public List<Note> notes = new ArrayList<Note>();
}
