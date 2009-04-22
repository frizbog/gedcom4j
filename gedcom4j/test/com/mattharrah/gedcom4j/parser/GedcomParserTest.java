/*
 * Copyright (c) 2009 Matthew R. Harrah
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package com.mattharrah.gedcom4j.parser;

import java.io.IOException;

import junit.framework.TestCase;

import com.mattharrah.gedcom4j.Family;
import com.mattharrah.gedcom4j.Gedcom;
import com.mattharrah.gedcom4j.Source;
import com.mattharrah.gedcom4j.Submitter;

public class GedcomParserTest extends TestCase {

	public void testLoad1() throws IOException, GedcomParserException {
		GedcomParser gp = new GedcomParser();
		gp.load("sample\\TGC551.ged");
		assertTrue(gp.errors.isEmpty());
		assertFalse(gp.warnings.isEmpty());
		for (String s : gp.warnings) {
			System.out.println(s);
			System.out.flush();
		}
		for (String s : gp.errors) {
			System.err.println(s);
			System.err.flush();
		}
		Gedcom g = gp.gedcom;
		assertNotNull(g.header);
		assertEquals(3, g.submitters.size());
		Submitter submitter = g.submitters.values().iterator().next();
		assertNotNull(submitter);
		assertEquals("H. Eichmann", submitter.name);

		assertEquals(7, g.families.size());
		assertEquals(2, g.sources.size());
		assertEquals(1, g.multimedia.size());
		assertEquals(15, g.individuals.size());

		for (Family f : g.families.values()) {
			if (f.husband != null && f.wife != null) {
				System.out.println("" + f.husband.names.get(0).basic
						+ " married " + f.wife.names.get(0).basic);
			}
		}
	}

	public void testLoad2() throws IOException, GedcomParserException {
		GedcomParser gp = new GedcomParser();
		gp.load("sample\\allged.ged");
		assertTrue(gp.errors.isEmpty());
		assertFalse(gp.warnings.isEmpty());
		for (String s : gp.warnings) {
			System.out.println(s);
			System.out.flush();
		}
		for (String s : gp.errors) {
			System.err.println(s);
			System.err.flush();
		}
		Gedcom g = gp.gedcom;
		assertFalse(g.submitters.isEmpty());
		Submitter submitter = g.submitters.values().iterator().next();
		assertNotNull(submitter);
		assertEquals("/Submitter-Name/", submitter.name);
	}

	public void testLoad3() throws IOException, GedcomParserException {
		GedcomParser gp = new GedcomParser();
		gp.load("sample\\a31486.ged");
		assertTrue(gp.errors.isEmpty());
		assertTrue(gp.warnings.isEmpty());
		for (String s : gp.warnings) {
			System.out.println(s);
			System.out.flush();
		}
		for (String s : gp.errors) {
			System.err.println(s);
			System.err.flush();
		}
		Gedcom g = gp.gedcom;

		// Check submitter
		assertFalse(g.submitters.isEmpty());
		Submitter submitter = g.submitters.values().iterator().next();
		assertNotNull(submitter);
		assertNull(submitter.name);

		// Check header
		assertEquals("6.00", g.header.sourceSystem.versionNum);
		assertEquals("(510) 794-6850",
				g.header.sourceSystem.corporation.phoneNumbers.get(0));

		// There are two sources in this file, and their names should be as
		// shown
		assertEquals(2, g.sources.size());
		for (Source s : g.sources.values()) {
			assertTrue(s.title.get(0).equals("William Barnett Family.FTW")
					|| s.title.get(0).equals("Warrick County, IN WPA Indexes"));
		}

		assertEquals(17, g.families.size());
		assertEquals(64, g.individuals.size());

		// Check a specific family
		Family family = g.families.get("@F1428@");
		assertNotNull(family);
		assertEquals(3, family.children.size());
		assertEquals("Lawrence Henry /Barnett/",
				family.husband.names.get(0).basic);
		assertEquals("Velma //", family.wife.names.get(0).basic);

	}

	public void testLoad4() throws IOException, GedcomParserException {
		GedcomParser gp = new GedcomParser();
		// Different line end char seq than the other file
		gp.load("sample\\TGC551LF.ged");
		assertTrue(gp.errors.isEmpty());
		assertFalse(gp.warnings.isEmpty());
		for (String s : gp.warnings) {
			System.out.println(s);
			System.out.flush();
		}
		for (String s : gp.errors) {
			System.err.println(s);
			System.err.flush();
		}
		Gedcom g = gp.gedcom;
		assertNotNull(g.header);
		assertEquals(3, g.submitters.size());
		Submitter submitter = g.submitters.values().iterator().next();
		assertNotNull(submitter);
		assertEquals("H. Eichmann", submitter.name);

		assertEquals(7, g.families.size());
		assertEquals(2, g.sources.size());
		assertEquals(1, g.multimedia.size());
		assertEquals(15, g.individuals.size());
	}
}
