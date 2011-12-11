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
		g.submitters.clear();

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
		assertEquals(h1, h2);

		// Read back the empty file and check its contents
		String string = loadFileIntoString(tempFile);

		assertEquals(
		        "0 HEAD\n"
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
		                + "2 CONT \"Read-Me\" file.\n"
		                + "2 CONT \n"
		                + "2 CONT --------------------------\n"
		                + "2 CONT The HEADER Record:\n"
		                + "2 CONT      This record has all possible tags for a HEADER record. In uses one custom tag (\"_HME\") to see what the software\n"
		                + "2 CONT will say about custom tags.\n"
		                + "2 CONT \n"
		                + "2 CONT --------------------------\n"
		                + "2 CONT INDIVIDUAL Records:\n"
		                + "2 CONT      This file has a small number of INDIVIDUAL records. The record named \"Joseph Tag Torture\" has all possible\n"
		                + "2 CONT tags for an INDIVIDUAL record. All remaining  individuals have less tags. Some test specific features; for example:\n"
		                + "2 CONT \n"
		                + "2 CONT      Name: Standard GEDCOM Filelinks\n"
		                + "2 CONT      Name: Nonstandard Multimedia Filelinks\n"
		                + "2 CONT      Name: General Custom Filelinks\n"
		                + "2 CONT      Name: Extra URL Filelinks\n"
		                + "2 CONT           These records link to multimedia files mentioned by the GEDCOM standard and to a variety of other types of\n"
		                + "2 CONT multimedia files, general files, or URL names.\n"
		                + "2 CONT \n"
		                + "2 CONT      Name: Chris Locked Torture\n"
		                + "2 CONT           Has a \"locked\" restriction (RESN) tag - should not be able to edit this record it. This record has one set of notes\n"
		                + "2 CONT that is used to test line breaking in notes and a few other text-parsing features of the GEDCOM software. Read those\n"
		                + "2 CONT notes to see what they are testing.\n"
		                + "2 CONT \n"
		                + "2 CONT      Name: Sandy Privacy Torture\n"
		                + "2 CONT           Has a \"privacy\" restriction (RESN) tag. Is the tag recognized and how is the record displayed and/or printed?\n"
		                + "2 CONT \n"
		                + "2 CONT      Name: Chris Locked Torture\n"
		                + "2 CONT      Name: Sandy Privacy Torture\n"
		                + "2 CONT      Name: Pat Smith Torture\n"
		                + "2 CONT           The three children in this file have unknown sex (no SEX tag). An ancestor tree from each should give five\n"
		                + "2 CONT generations of ancestors.\n"
		                + "2 CONT \n"
		                + "2 CONT      Name: Charlie Accented ANSEL\n"
		                + "2 CONT      Name: Lucy Special ANSEL\n"
		                + "2 CONT           The notes in these records use all possible special characters in the ANSEL character set. The header of this file\n"
		                + "2 CONT denotes this file as using the ANSEL character set. The importing software should handle these special characters in a\n"
		                + "2 CONT reasonable way.\n"
		                + "2 CONT \n"
		                + "2 CONT      Name: Torture GEDCOM Matriarch\n"
		                + "2 CONT            All individuals in this file are related and all are descendants (or spouses of descendants) of Torture GEDCOM\n"
		                + "2 CONT Matriarch. A descendant tree or report from this individual should show five generations of descendants.\n"
		                + "2 CONT \n"
		                + "2 CONT --------------------------\n"
		                + "2 CONT FAMILY Records:\n"
		                + "2 CONT      The FAMILY record for \"Joseph Tag Torture\" (husband) and \"Mary First Jones\" (wife) has all tags allowed in\n"
		                + "2 CONT family records. All other family records use only a few tags and are used to provide records for extra family links in\n"
		                + "2 CONT other records.\n"
		                + "2 CONT \n"
		                + "2 CONT --------------------------\n"
		                + "2 CONT SOURCE Records:\n"
		                + "2 CONT      There are two SOURCE records in this file. The \"Everything You Every Wanted to Know about GEDCOM Tags\"\n"
		                + "2 CONT source has all possible GEDCOM tags for a SOURCE record. The other source only has only a few tags.\n"
		                + "2 CONT \n"
		                + "2 CONT --------------------------\n"
		                + "2 CONT REPOSITORY Record:\n"
		                + "2 CONT      There is just one REPOSITORY record and it uses all possible tags for such a record.\n"
		                + "2 CONT \n"
		                + "2 CONT --------------------------\n"
		                + "2 CONT SUBMITTER Records:\n"
		                + "2 CONT      This file has three SUBMITTER records. The \"John A. Nairn\" record has all tags allowed in such records. The\n"
		                + "2 CONT second and third submitter are to test how programs input files with multiple submitters. The GEDCOM standard does\n"
		                + "2 CONT not allow for notes in SUBMITTER records. Look in the \"Main Submitter\" to verify all address data comes through,\n"
		                + "2 CONT that all three phone numbers appear, and that the multimedia file link is preserved.\n"
		                + "2 CONT \n"
		                + "2 CONT --------------------------\n"
		                + "2 CONT MULTIMEDIA OBJECT Record:\n"
		                + "2 CONT      The one MULTIMEDIA record has all possible tags and even has encoded data for a small image of a flower. There\n"
		                + "2 CONT are no known GEDCOM programs that can read or write such records. The record is included here to test how\n"
		                + "2 CONT programs might respond to finding multimedia records present. There are possible plans to eliminate encoded\n"
		                + "2 CONT multimedia objects in the next version of GEDCOM. In the future all multimedia will be included by links to other files.\n"
		                + "2 CONT To test current file links and extended file links, see the \"Filelinks\" family records described above.\n"
		                + "2 CONT \n"
		                + "2 CONT --------------------------\n"
		                + "2 CONT SUBMISSION Record:\n"
		                + "2 CONT      The one (maximum allowed) SUBMISSION record in this file has all possible tags for such a record.\n"
		                + "2 CONT \n"
		                + "2 CONT --------------------------\n"
		                + "2 CONT NOTE Records:\n"
		                + "2 CONT      This file has many NOTE records. These are all linked to other records.\n"
		                + "2 CONT \n"
		                + "2 CONT --------------------------\n"
		                + "2 CONT TRLR Records:\n"
		                + "2 CONT      This file ends in the standard TRLR record.\n"
		                + "2 CONT \n"
		                + "2 CONT --------------------------\n"
		                + "2 CONT ADDITIONAL NOTES\n"
		                + "2 CONT      This file was originally created by H. Eichmann at <h.eichmann@@mbox.iqo.uni-hannover.de> and posted on the\n"
		                + "2 CONT Internet.\n"
		                + "2 CONT \n"
		                + "2 CONT (NOTE: email addresses are listed here with double \"at\" signs. A rule of GEDCOM parsing is that these should be\n"
		                + "2 CONT converted to single \"at\" at signs, but not many programs follow that rule. In addition, that rule is not needed and may be\n"
		                + "2 CONT abandoned in a future version of GEDCOM).\n"
		                + "2 CONT \n"
		                + "2 CONT This original file was extensively modified by J. A. Nairn using GEDitCOM 2.9.4 (1999-2001) at\n"
		                + "2 CONT <support@@geditcom.com> and posted on the Internet at <http://www.geditcom.com>. Some changes included many\n"
		                + "2 CONT more notes, the use or more tags, extensive testing of multimedia file links, and some notes to test all special ANSEL\n"
		                + "2 CONT characters.\n"
		                + "2 CONT \n"
		                + "2 CONT Feel free to copy and use this GEDCOM file for any  non-commercial purpose.\n"
		                + "2 CONT \n"
		                + "2 CONT For selecting the allowed tags, the GEDCOM standard Release 5.5 (2 JAN 1996) was used. Copyright: The Church of\n"
		                + "2 CONT Jesus Christ of Latter-Day Saints, <gedcom@@gedcom.org>.\n"
		                + "2 CONT \n"
		                + "2 CONT You can download the GEDCOM 5.5 specs from: <ftp.gedcom.com/pub/genealogy/gedcom>. You can read the\n"
		                + "2 CONT GEDCOM 5.5 specs on the Internet at <http://homepages.rootsweb.com/~pmcbride/gedcom/55gctoc.htm>.\n"
		                + "0 TRLR\n", string);

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
