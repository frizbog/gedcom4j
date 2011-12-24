package com.mattharrah.gedcom4j.parser;

import java.io.IOException;

import junit.framework.TestCase;

import com.mattharrah.gedcom4j.Family;
import com.mattharrah.gedcom4j.FamilyEvent;
import com.mattharrah.gedcom4j.Gedcom;

/**
 * A test specifically for issue 2 (see gedcom4j.googlecode.com)
 * 
 * @author frizbog1
 */
public class FamilyEventTypeParseTest extends TestCase {

	/**
	 * Test fixture - loaded GEDCOM
	 */
	private Gedcom g;

	/**
	 * Positive test case for google code issue 2
	 * 
	 * @throws GedcomParserException
	 *             if the file cannot be parsed
	 * @throws IOException
	 *             if there is an error reading the data
	 */
	public void testIssue2() throws IOException, GedcomParserException {
		int familyCount = 0;
		for (Family fam : g.families.values()) {
			familyCount++;
			assertNotNull(fam.events);
			for (FamilyEvent event : fam.events) {
				assertNotNull(event.type);
			}
		}
		assertEquals("There are 7 families in the stress test file", 7,
		        familyCount);
	}

	/**
	 * Set up test fixture by loading stress test file into a {@link Gedcom}
	 * struture
	 * 
	 * @throws GedcomParserException
	 *             if the file cannot be parsed
	 * @throws IOException
	 *             if there is an error reading the data
	 */
	@Override
	protected void setUp() throws IOException, GedcomParserException {
		GedcomParser gp = new GedcomParser();
		gp.load("sample/TGC551.ged");
		assertTrue(gp.errors.isEmpty());
		g = gp.gedcom;
	}
}
