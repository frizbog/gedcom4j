package com.mattharrah.gedcom4j.writer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import junit.framework.TestCase;

import com.mattharrah.gedcom4j.Gedcom;
import com.mattharrah.gedcom4j.Header;
import com.mattharrah.gedcom4j.parser.GedcomParser;
import com.mattharrah.gedcom4j.parser.GedcomParserException;
import com.mattharrah.gedcom4j.validate.GedcomValidationException;

public class GedcomWriterTest extends TestCase {

	/**
	 * Test writing a gedcom structure for the degenerate case when the data is
	 * empty. Writes the data, reads it back, and compares it to a fixed string
	 * containing the expected contents.
	 * 
	 * @throws IOException
	 * @throws GedcomValidationException
	 * @throws GedcomWriterException
	 */
	public void testWrite1() throws IOException, GedcomValidationException,
	        GedcomWriterException {
		// Write an empty file
		Gedcom g = new Gedcom();
		GedcomWriter gw = new GedcomWriter(g);
		gw.validationSuppressed = true;
		File tempFile = new File(System.getProperty("java.io.tmpdir")
		        + "gedcom4j.writertest.ged");
		System.out.println(tempFile.getAbsolutePath());
		gw.write(tempFile);

		// Read back the empty file and check its contents
		String string = loadFileIntoString(tempFile);

		assertEquals("0 HEAD\n1 FILE gedcom4j.writertest.ged\n"
		        + "1 GEDC\n2 VERS 5.5\n2 FORM LINEAGE-LINKED\n"
		        + "1 CHAR ANSEL\n0 TRLR\n", string);

	}

	/**
	 * Test by reading the torture test file, eliminating everything but the
	 * header, and checking the generated text against a fixed string
	 * 
	 * @throws IOException
	 *             if some file somewhere can't be read or written
	 * @throws GedcomParserException
	 *             if the file can't be parsed
	 * @throws GedcomValidationException
	 *             if the data doesn't pass validation
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	public void testWrite2() throws IOException, GedcomParserException,
	        GedcomValidationException, GedcomWriterException {
		// Load a file
		GedcomParser p = new GedcomParser();
		p.load("sample/TGC551.ged");
		Gedcom g = p.gedcom;
		// Clear out everything except the header
		g.families.clear();
		g.individuals.clear();
		g.multimedia.clear();
		g.notes.clear();
		g.repositories.clear();
		g.sources.clear();

		GedcomWriter gw = new GedcomWriter(g);
		File tempFile = new File(System.getProperty("java.io.tmpdir")
		        + "gedcom4j.writertest.ged");
		System.out.println(tempFile.getAbsolutePath());
		gw.write(tempFile);

		// Reload the file we just wrote
		p = new GedcomParser();
		p.load(tempFile.getAbsolutePath());
		Gedcom g2 = p.gedcom;

		// Compare what was loaded from original file and what was reloaded from
		// the file we just wrote
		// Should be basically the same.
		assertNotNull(g2);
		Header h1 = g.header;
		Header h2 = g2.header;
		assertNotSame(h1, h2);
		assertNotNull(h1);
		// For right now, blank out the multimedia references in the submitters,
		// until we are successfully writing that out
		h1.submitter.multimedia = null;
		h2.submitter.multimedia = null;

		assertEquals(h1, h2);

		// Read back the empty file and check its contents
		String string = loadFileIntoString(tempFile);
		System.out.println(string);

		assertTrue(string
		        .startsWith("0 HEAD\n"
		                + "1 SOUR GEDitCOM\n"
		                + "2 VERS 2.9.4\n"
		                + "2 NAME GEDitCOM\n"
		                + "2 CORP RSAC Software\n"
		                + "3 ADDR 7108 South Pine Cone Street\n"
		                + "4 CONT Salt Lake City, UT 84121\n"
		                + "4 CONT USA\n"
		                + "4 ADR1 RSAC Software\n"
		                + "4 ADR2 7108 South Pine Cone Street\n"
		                + "4 CITY Salt Lake City\n"
		                + "4 STAE UT\n"
		                + "4 POST 84121\n"
		                + "4 CTRY USA\n"
		                + "3 PHON +1-801-942-7768\n"
		                + "3 PHON +1-801-555-1212\n"
		                + "3 PHON +1-801-942-1148 (FAX) (last one!)\n"
		                + "2 DATA Name of source data\n"
		                + "3 DATE 1 JAN 1998\n"
		                + "3 COPR Copyright of source data\n"
		                + "1 DEST ANSTFILE\n"
		                + "1 DATE 1 JAN 1998\n"
		                + "2 TIME 13:57:24.80\n"
		                + "1 SUBM @SUBMITTER@\n"
		                + "1 SUBN @SUBMISSION@\n"
		                + "1 FILE gedcom4j.writertest.ged\n"
		                + "1 COPR Ã 1997 by H. Eichmann, parts Ã 1999-2000 by J. A. Nairn.\n"
		                + "1 GEDC\n"
		                + "2 VERS 5.5\n"
		                + "2 FORM LINEAGE-LINKED\n"
		                + "1 CHAR ANSEL\n"
		                + "2 VERS ANSI Z39.47-1985\n"
		                + "1 LANG English\n"
		                + "1 PLAC\n"
		                + "2 FORM City, County, State, Country\n"
		                + "1 NOTE This file demonstrates all tags that are allowed in GEDCOM 5.5. Here are some comments about the HEADER record\n"
		                + "2 CONT and comments about where to look for information on the other 9 types of GEDCOM records. Most other records will\n"
		                + "2 CONT have their own notes that describe what to look for in that record and what to hope the importing software will find.\n"
		                + "2 CONT \n"
		                + "2 CONT Many applications will fail to import these notes. The notes are therefore also provided with the files as a plain-text\n"
		                + "2 CONT \"Read-Me\" file.\n2 CONT \n"));
		assertTrue(string.contains("0 @SUBMISSION@ SUBN\n"
		        + "1 SUBM @SUBMITTER@\n1 FAMF NameOfFamilyFile\n"
		        + "1 TEMP Abbreviated Temple Code\n1 ANCE 1\n"
		        + "1 DESC 1\n1 ORDI yes\n1 RIN 1\n"));
		assertTrue(string.contains("0 @SUBMITTER@ SUBM\n"
		        + "1 NAME John A. Nairn\n"
		        + "1 ADDR Submitter address line 1\n"
		        + "2 CONT Submitter address line 2\n"
		        + "2 CONT Submitter address line 3\n"
		        + "2 CONT Submitter address line 4\n"
		        + "2 ADR1 Submitter address line 1\n"
		        + "2 ADR2 Submitter address line 2\n"
		        + "2 CITY Submitter address city\n"
		        + "2 STAE Submitter address state\n"
		        + "2 POST Submitter address ZIP code\n"
		        + "2 CTRY Submitter address country\n1 LANG English\n"
		        + "1 PHON Submitter phone number 1\n"
		        + "1 PHON Submitter phone number 2\n"
		        + "1 PHON Submitter phone number 3 (last one!)\n"
		        + "1 RFN Submitter Registered RFN\n1 RIN 1\n1 CHAN\n"
		        + "2 DATE 7 Sep 2000\n3 TIME 8:35:36"));
		assertTrue(string.contains("0 @SM2@ SUBM\n"
		        + "1 NAME Secondary Submitter\n"
		        + "1 ADDR Secondary Submitter Address 1\n"
		        + "2 CONT Secondary Submitter Address 2\n1 LANG English\n"
		        + "1 RIN 2\n1 CHAN\n2 DATE 12 Mar 2000\n3 TIME 10:38:33"));
		assertTrue(string.endsWith("0 TRLR\n"));

	}

	/**
	 * Loads a file into a string for easy test assertions
	 * 
	 * @param file
	 *            the file
	 * @return the file's contents in a string
	 * @throws FileNotFoundException
	 *             if the file can't be found
	 * @throws IOException
	 *             if the file can't be read or written
	 */
	private String loadFileIntoString(File file) throws FileNotFoundException,
	        IOException {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String s = br.readLine();
		while (s != null) {
			pw.println(s);
			s = br.readLine();
		}
		pw.flush();
		pw.close();
		String string = sw.toString();
		return string;
	}
}
