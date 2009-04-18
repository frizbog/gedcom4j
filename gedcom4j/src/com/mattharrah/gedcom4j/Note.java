package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

public class Note {
	public String xref;
	public List<String> lines = new ArrayList<String>();
	public List<Citation> citations = new ArrayList<Citation>();
}
