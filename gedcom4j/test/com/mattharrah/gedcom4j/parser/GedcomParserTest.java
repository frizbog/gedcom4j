package com.mattharrah.gedcom4j.parser;

import java.io.IOException;

import com.mattharrah.gedcom4j.Gedcom;

import junit.framework.TestCase;

public class GedcomParserTest extends TestCase {

	public void testLoad() throws IOException, GedcomParserException {
		GedcomParser gp = new GedcomParser();
		gp.load("C:\\Documents and Settings\\Matt\\My Documents\\eclipse-workspace\\Gedcom4J\\sample\\Harrah.GED");
		for (String s: gp.warnings) {
			System.out.println(s);
		}
	}

}
