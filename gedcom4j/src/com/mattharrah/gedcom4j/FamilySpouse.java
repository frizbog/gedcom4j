package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Indicates an individual's membership, as a spouse, in a family
 * 
 * @author Matt
 * 
 */
public class FamilySpouse {
	public Family family;
	public List<Note> notes = new ArrayList<Note>();
}
