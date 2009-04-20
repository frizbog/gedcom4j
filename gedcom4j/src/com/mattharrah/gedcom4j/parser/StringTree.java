package com.mattharrah.gedcom4j.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a tree structure containing a single line of text from the gedcom
 * file, and a list of child structures, used by the parser for temporary
 * storage of the text of the gedcom. the class and its members are deliberately
 * package private so only the parser will reference it.
 * 
 * @author Matt
 */
class StringTree {
	int level;
	String id;
	String tag;
	String value;
	List<StringTree> children = new ArrayList<StringTree>();
	StringTree parent = null;
	int lineNum;

	@Override
	public String toString() {
		return "Line " + lineNum + ": " + level + " " + tag + " " + value;
	}
}