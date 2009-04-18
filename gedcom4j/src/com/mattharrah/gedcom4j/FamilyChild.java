package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent's an individuals membership, as a child, in a family
 * @author Matt
 */
public class FamilyChild {
	public Family family;
	public List<Note> notes = new ArrayList<Note>();
	public List<String> pedigreeTypes = new ArrayList<String>();
}
