package com.mattharrah.gedcom4j.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import com.mattharrah.gedcom4j.Address;
import com.mattharrah.gedcom4j.Citation;
import com.mattharrah.gedcom4j.Corporation;
import com.mattharrah.gedcom4j.Gedcom;
import com.mattharrah.gedcom4j.Header;
import com.mattharrah.gedcom4j.HeaderSourceData;
import com.mattharrah.gedcom4j.Multimedia;
import com.mattharrah.gedcom4j.Note;
import com.mattharrah.gedcom4j.SourceSystem;
import com.mattharrah.gedcom4j.Submission;
import com.mattharrah.gedcom4j.Submitter;
import com.mattharrah.gedcom4j.validate.GedcomValidationException;
import com.mattharrah.gedcom4j.validate.GedcomValidator;

public class GedcomWriter {

	/**
	 * The Gedcom data to write
	 */
	private Gedcom gedcom;

	/**
	 * Are we suppressing the call to the validator? Deliberately
	 * package-private so unit tests can fiddle with it to make testing easy.
	 */
	boolean validationSuppressed = false;

	/**
	 * Constructor
	 * 
	 * @param gedcom
	 *            the {@link Gedcom} structure to write out
	 */
	public GedcomWriter(Gedcom gedcom) {
		this.gedcom = gedcom;
	}

	/**
	 * Write the {@link Gedcom} data as a GEDCOM 5.5 file. Automatically fills
	 * in the value for the FILE tag in the HEAD structure.
	 * 
	 * @param gedcom
	 *            the {@link Gedcom} structure to write
	 * @param file
	 *            the {@link File} to write to
	 * @throws IOException
	 *             if there's a problem writing the data
	 * @throws GedcomValidationException
	 *             if the gedcom data has validation errors that won't allow it
	 *             to be written
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	public void write(File file) throws IOException, GedcomValidationException,
	        GedcomWriterException {
		// Automatically replace the contents of the filename in the header
		gedcom.header.fileName = file.getName();

		OutputStream o = new FileOutputStream(file);
		write(o);
		o.flush();
		o.close();
	}

	/**
	 * Write the {@link Gedcom} data in GEDCOM 5.5 format to an output stream
	 * 
	 * @param gedcom
	 *            the {@link Gedcom} structure to write
	 * @param out
	 *            the output stream we're writing to
	 * @throws GedcomValidationException
	 *             if the gedcom data has validation errors that won't allow it
	 *             to be written
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 * @throws FileNotFoundException
	 *             if the output file cannot be written (perhaps the directory
	 *             we're writing into doesn't exist?)
	 */
	public void write(OutputStream out) throws GedcomValidationException,
	        GedcomWriterException {
		if (!validationSuppressed) {
			GedcomValidator gv = new GedcomValidator(gedcom);
			gv.validate();
		}
		PrintWriter pw = new PrintWriter(out);
		emitHeader(pw);
		emitSubmissionRecord(pw);
		emitRecords(pw);
		emitTrailer(pw);
		pw.flush();
		pw.close();
	}

	/**
	 * Write the {@link Gedcom} data as a GEDCOM 5.5 file, with the supplied
	 * file name
	 * 
	 * @param gedcom
	 *            the {@link Gedcom} structure to write
	 * @param filename
	 *            the name of the file to write
	 * @throws IOException
	 *             if there's a problem writing the data
	 * @throws GedcomValidationException
	 *             if the gedcom data has validation errors that won't allow it
	 *             to be written
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	public void write(String filename) throws IOException,
	        GedcomValidationException, GedcomWriterException {
		File f = new File(filename);
		write(f);
	}

	/**
	 * Write an address
	 * 
	 * @param pw
	 * @param level
	 * @param address
	 */
	private void emitAddress(PrintWriter pw, int level, Address address) {
		if (address == null) {
			return;
		}
		int addrLineNum = 0;
		for (String l : address.lines) {
			if (addrLineNum++ == 0) {
				emitTagIfValueNotNull(pw, level, null, "ADDR", l);
			} else {
				emitTagIfValueNotNull(pw, level + 1, null, "CONT", l);
			}
		}
		emitTagIfValueNotNull(pw, level + 1, null, "ADR1", address.addr1);
		emitTagIfValueNotNull(pw, level + 1, null, "ADR2", address.addr2);
		emitTagIfValueNotNull(pw, level + 1, null, "CITY", address.city);
		emitTagIfValueNotNull(pw, level + 1, null, "STAE",
		        address.stateProvince);
		emitTagIfValueNotNull(pw, level + 1, null, "POST", address.postalCode);
		emitTagIfValueNotNull(pw, level + 1, null, "CTRY", address.country);
	}

	/**
	 * Write out all the Families
	 * 
	 * @param pw
	 *            the {@link PrintWriter} we're writing to
	 */
	private void emitFamilies(PrintWriter pw) {
		// TODO Auto-generated method stub

	}

	/**
	 * Write the header record (see the HEADER structure in the GEDCOM standard)
	 * 
	 * @param gedcom
	 *            the {@link Gedcom} structure containing the header
	 * @param pw
	 *            the {@link PrintWriter} we're writing to
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitHeader(PrintWriter pw) throws GedcomWriterException {
		Header header = gedcom.header;
		if (header == null) {
			header = new Header();
		}
		pw.println("0 HEAD");
		emitSourceSystem(pw, header.sourceSystem);
		emitTagIfValueNotNull(pw, 1, null, "DEST", header.destinationSystem);
		if (header.date != null) {
			emitTagIfValueNotNull(pw, 1, null, "DATE", header.date);
			emitTagIfValueNotNull(pw, 2, null, "TIME", header.time);
		}
		if (header.submitter != null) {
			emitTagWithRequiredValue(pw, 1, null, "SUBM", header.submitter.xref);
		}
		if (header.submission != null) {
			emitTagWithRequiredValue(pw, 1, null, "SUBN",
			        header.submission.xref);
		}
		emitTagIfValueNotNull(pw, 1, null, "FILE", header.fileName);
		emitTagIfValueNotNull(pw, 1, null, "COPR", header.copyrightData);
		emitTag(pw, 1, null, "GEDC");
		emitTagWithRequiredValue(pw, 2, null, "VERS",
		        header.gedcomVersion.versionNumber);
		emitTagWithRequiredValue(pw, 2, null, "FORM",
		        header.gedcomVersion.gedcomForm);
		emitTagWithRequiredValue(pw, 1, null, "CHAR",
		        header.characterSet.characterSetName);
		emitTagIfValueNotNull(pw, 2, null, "VERS",
		        header.characterSet.versionNum);
		emitTagIfValueNotNull(pw, 1, null, "LANG", header.language);
		if (header.placeStructure != null && !header.placeStructure.isEmpty()) {
			// TODO - need better handling for PLAC structures in the header
			emitTag(pw, 1, null, "PLAC");
			emitTagWithRequiredValue(pw, 2, null, "FORM", header.placeStructure);
		}
		emitNote(pw, 1, header.notes);
	}

	/**
	 * Write out all the individuals
	 * 
	 * @param pw
	 *            the {@link PrintWriter} we're writing to
	 */
	private void emitIndividuals(PrintWriter pw) {
		// TODO Auto-generated method stub

	}

	/**
	 * Write out all the multimedia references
	 * 
	 * @param pw
	 *            the {@link PrintWriter} we're writing to
	 */
	private void emitMultimedia(PrintWriter pw) {
		// TODO Auto-generated method stub

	}

	/**
	 * Emit a list of multimedia links
	 * 
	 * @param pw
	 *            the {@link PrintWriter} we are writing to
	 * @param level
	 *            the level in the hierarchy we are writing at
	 * @param multimedia
	 *            the {@link List} of {@link Multimedia} objects
	 */
	private void emitMultimediaLinks(PrintWriter pw, int level,
	        List<Multimedia> multimedia) {
		if (multimedia == null) {
			return;
		}
		// TODO - complete writing out the multimedia links
	}

	/**
	 * Emit a note (possibly multi-line)
	 * 
	 * @param pw
	 *            the {@link PrintWriter} we are writing to
	 * @param level
	 *            the level in the hierarchy we are writing at
	 * @param notes
	 *            the Notes text
	 */
	private void emitNote(PrintWriter pw, int level, List<String> notes) {
		int noteLineNum = 0;
		for (String n : notes) {
			if (noteLineNum++ == 0) {
				emitTagIfValueNotNull(pw, level, null, "NOTE", n);
			} else {
				pw.println(level + 1 + " " + "CONT" + " "
				        + (n == null ? "" : n));
			}
		}
	}

	/**
	 * Emit a note structure (possibly multi-line)
	 * 
	 * @param pw
	 *            the {@link PrintWriter} we are writing to
	 * @param level
	 *            the level in the hierarchy we are writing at
	 * @param notes
	 *            the Notes text
	 */
	private void emitNote(PrintWriter pw, int level, Note note) {
		int noteLineNum = 0;
		for (String n : note.lines) {
			if (noteLineNum++ == 0) {
				emitTagIfValueNotNull(pw, level, null, "NOTE", n);
			} else {
				pw.println(level + 1 + " " + "CONT" + " "
				        + (n == null ? "" : n));
			}
		}
		emitSourceCitations(pw, level, note.citations);
	}

	/**
	 * Emit a list of note structures
	 * 
	 * @param pw
	 *            the {@link PrintWriter} we are writing to
	 * @param level
	 *            the level in the hierarchy we are writing at
	 * @param notes
	 *            a list of {@link Note} structures
	 */
	private void emitNotes(PrintWriter pw, int level, List<Note> notes) {
		for (Note n : notes) {
			emitNote(pw, level, n);
		}
	}

	/**
	 * Write out all the notes
	 * 
	 * @param pw
	 *            the {@link PrintWriter} we're writing to
	 */

	private void emitNoteStructures(PrintWriter pw) {
		// TODO Auto-generated method stub

	}

	/**
	 * Write out the phone numbers
	 * 
	 * @param pw
	 *            the {@link PrintWriter} we are writing to
	 * @param level
	 *            the level in the hierarchy we are writing at
	 * @param phoneNumbers
	 *            a list of phone numbers
	 */
	private void emitPhoneNumbers(PrintWriter pw, int level,
	        List<String> phoneNumbers) {
		for (String ph : phoneNumbers) {
			emitTagIfValueNotNull(pw, level, null, "PHON", ph);
		}

	}

	/**
	 * Write the records (see the RECORD structure in the GEDCOM standard)
	 * 
	 * @param pw
	 *            the {@link PrintWriter} we're writing to
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitRecords(PrintWriter pw) throws GedcomWriterException {
		emitSubmitter(pw);
		emitIndividuals(pw);
		emitFamilies(pw);
		emitMultimedia(pw);
		emitNoteStructures(pw);
		emitRepositories(pw);
		emitSources(pw);
	}

	/**
	 * Write out all the repositories
	 * 
	 * @param pw
	 *            the {@link PrintWriter} we're writing to
	 */
	private void emitRepositories(PrintWriter pw) {
		// TODO Auto-generated method stub

	}

	/**
	 * Write out a list of source citations
	 * 
	 * @param pw
	 *            the {@link PrintWriter} we are writing to
	 * @param level
	 *            the level in the hierarchy we are writing at
	 * @param citations
	 *            the source citations
	 */
	private void emitSourceCitations(PrintWriter pw, int level,
	        List<Citation> citations) {
		// TODO Auto-generated method stub

	}

	/**
	 * Write out all the sources
	 * 
	 * @param pw
	 *            the {@link PrintWriter} we're writing to
	 */
	private void emitSources(PrintWriter pw) {
		// TODO Auto-generated method stub

	}

	/**
	 * Write a source system structure (see APPROVED_SYSTEM_ID) in the GEDCOM
	 * spec
	 * 
	 * @param pw
	 *            the {@link PrintWriter} we're writing to
	 * @param sourceSystem
	 *            the source system
	 * @throws GedcomWriterException
	 *             if data is malformed and cannot be written
	 */
	private void emitSourceSystem(PrintWriter pw, SourceSystem sourceSystem)
	        throws GedcomWriterException {
		if (sourceSystem == null) {
			return;
		}
		emitTagIfValueNotNull(pw, 1, null, "SOUR", sourceSystem.systemId);
		emitTagWithOptionalValue(pw, 2, null, "VERS", sourceSystem.versionNum);
		emitTagWithOptionalValue(pw, 2, null, "NAME", sourceSystem.productName);
		Corporation corporation = sourceSystem.corporation;
		if (corporation != null) {
			emitTagWithOptionalValue(pw, 2, null, "CORP",
			        corporation.businessName);
			emitAddress(pw, 3, corporation.address);
			emitPhoneNumbers(pw, 3, corporation.phoneNumbers);
		}
		HeaderSourceData sourceData = sourceSystem.sourceData;
		if (sourceData != null) {
			emitTagIfValueNotNull(pw, 2, null, "DATA", sourceData.name);
			emitTagIfValueNotNull(pw, 3, null, "DATE", sourceData.publishDate);
			emitTagIfValueNotNull(pw, 3, null, "COPR", sourceData.copyright);
		}
	}

	/**
	 * Write the SUBMISSION_RECORD
	 * 
	 * @param gedcom
	 *            the {@link Gedcom} structure containing the header
	 * @param pw
	 *            the {@link PrintWriter} we're writing to
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitSubmissionRecord(PrintWriter pw)
	        throws GedcomWriterException {
		Submission s = gedcom.submission;
		if (s == null) {
			return;
		}
		emitTag(pw, 0, s.xref, "SUBN");
		if (s.submitter != null) {
			emitTagWithOptionalValue(pw, 1, null, "SUBM", s.submitter.xref);
		}
		emitTagIfValueNotNull(pw, 1, null, "FAMF", s.nameOfFamilyFile);
		emitTagIfValueNotNull(pw, 1, null, "TEMP", s.templeCode);
		emitTagIfValueNotNull(pw, 1, null, "ANCE", s.ancestorsCount);
		emitTagIfValueNotNull(pw, 1, null, "DESC", s.descendantsCount);
		emitTagIfValueNotNull(pw, 1, null, "ORDI", s.ordinanceProcessFlag);
		emitTagIfValueNotNull(pw, 1, null, "RIN", s.recIdNumber);
	}

	/**
	 * Write out the submitter record
	 * 
	 * @param pw
	 *            the {@link PrintWriter} we're writing to
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitSubmitter(PrintWriter pw) throws GedcomWriterException {
		for (Submitter s : gedcom.submitters.values()) {
			emitTag(pw, 0, s.xref, "SUBM");
			emitTagWithOptionalValue(pw, 1, null, "NAME", s.name);
			emitAddress(pw, 1, s.address);
			emitMultimediaLinks(pw, 1, s.multimedia);
			for (String l : s.languagePref) {
				emitTagWithRequiredValue(pw, 1, null, "LANG", l);
			}
			/*
			 * Unclear if really part of the GEDCOM or not - a stress test file
			 * includes them, and the tool can parse them, so if we have them,
			 * write them. Won't hurt anything if the collection is empty.
			 */
			for (String l : s.phoneNumbers) {
				emitTagWithRequiredValue(pw, 1, null, "PHON", l);
			}

			emitTagIfValueNotNull(pw, 1, null, "RFN", s.regFileNumber);
			emitTagIfValueNotNull(pw, 1, null, "RIN", s.recIdNumber);
			if (s.changeDate != null) {
				emitTag(pw, 1, null, "CHAN");
				emitTagWithRequiredValue(pw, 2, null, "DATE", s.changeDate.date);
				emitTagIfValueNotNull(pw, 3, null, "TIME", s.changeDate.time);
				emitNotes(pw, 2, s.changeDate.notes);
			}
		}
	}

	/**
	 * Write a line with a tag, with no value following the tag
	 * 
	 * @param pw
	 *            the {@link PrintWriter} we're writing to
	 * @param level
	 *            the level within the file hierarchy
	 * @param tag
	 *            the tag for the line of the file
	 */
	private void emitTag(PrintWriter pw, int level, String xref, String tag) {
		pw.print(level);
		if (xref != null && !xref.isEmpty()) {
			pw.print(" " + xref);
		}
		pw.println(" " + tag);
	}

	/**
	 * Write a line if the value is non-null
	 * 
	 * @param pw
	 *            the {@link PrintWriter} we're writing to
	 * @param level
	 *            the level within the file hierarchy
	 * @param tag
	 *            the tag for the line of the file
	 * @param value
	 *            the value to write to the right of the tag
	 */
	private void emitTagIfValueNotNull(PrintWriter pw, int level, String xref,
	        String tag, String value) {
		if (value != null) {
			pw.print(level);
			if (xref != null && !xref.isEmpty()) {
				pw.print(" " + xref);
			}
			pw.println(" " + tag + " " + value);
		}
	}

	/**
	 * Write a line and tag, with an optional value for the tag
	 * 
	 * @param pw
	 *            the {@link PrintWriter} we're writing to
	 * @param level
	 *            the level within the file hierarchy
	 * @param xref
	 *            the xref ID identifying this entry
	 * @param tag
	 *            the tag for the line of the file
	 * @param value
	 *            the value to write to the right of the tag
	 * @throws GedcomWriterException
	 *             if the value is null or blank (which never happens, because
	 *             we check for it)
	 */
	private void emitTagWithOptionalValue(PrintWriter pw, int level,
	        String xref, String tag, String value) throws GedcomWriterException {
		if (value == null) {
			emitTag(pw, level, xref, tag);
		} else {
			pw.print(level);
			if (xref != null && !xref.isEmpty()) {
				pw.print(" " + xref);
			}
			pw.println(" " + tag + " " + value);
		}
	}

	/**
	 * Write a line and tag, with an optional value for the tag
	 * 
	 * @param pw
	 *            the {@link PrintWriter} we're writing to
	 * @param level
	 *            the level within the file hierarchy
	 * @param tag
	 *            the tag for the line of the file
	 * @param value
	 *            the value to write to the right of the tag
	 * @param xref
	 * @throws GedcomWriterException
	 *             if the value is null or blank
	 */
	private void emitTagWithRequiredValue(PrintWriter pw, int level,
	        String xref, String tag, String value) throws GedcomWriterException {
		if (value == null || "".equals(value)) {
			throw new GedcomWriterException("Required value for tag " + tag
			        + " at level " + level + " was null or blank");
		}
		pw.print(level);
		if (xref != null && !xref.isEmpty()) {
			pw.print(" " + xref);
		}
		pw.println(" " + tag + " " + value);
	}

	/**
	 * Write out the trailer record
	 * 
	 * @param gedcom
	 *            the {@link Gedcom} structure containing the header
	 */
	private void emitTrailer(PrintWriter pw) {
		pw.println("0 TRLR");
	}
}
