package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

public class ReusableNote extends Note {
	public Note note;
	public List<Citation> citations = new ArrayList<Citation>();
}
