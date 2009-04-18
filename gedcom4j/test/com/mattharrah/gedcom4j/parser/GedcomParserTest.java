package com.mattharrah.gedcom4j.parser;

import java.io.IOException;

import com.mattharrah.gedcom4j.Gedcom;

import junit.framework.TestCase;

public class GedcomParserTest extends TestCase {

	public void testLoad() throws IOException, GedcomParserException {
		GedcomParser gp = new GedcomParser();
		gp.load("sample\\TGC551.ged");
		for (String s: gp.warnings) {
			System.out.println(s);
			System.out.flush();
		}
		for (String s: gp.errors) {
			System.err.println(s);
			System.err.flush();
		}
	}

}
