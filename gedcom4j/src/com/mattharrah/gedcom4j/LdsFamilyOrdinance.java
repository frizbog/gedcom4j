package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

public class LdsFamilyOrdinance {
	public String status;
	public String date;
	public String temple;
	public String place;
	public List<Citation> citations = new ArrayList<Citation>();
	public List<Note> notes = new ArrayList<Note>();

}
