package com.mattharrah.gedcom4j.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.mattharrah.gedcom4j.Address;
import com.mattharrah.gedcom4j.ChangeDate;
import com.mattharrah.gedcom4j.Citation;
import com.mattharrah.gedcom4j.CitationData;
import com.mattharrah.gedcom4j.CitationWithSource;
import com.mattharrah.gedcom4j.CitationWithoutSource;
import com.mattharrah.gedcom4j.Corporation;
import com.mattharrah.gedcom4j.EventRecorded;
import com.mattharrah.gedcom4j.Family;
import com.mattharrah.gedcom4j.FamilyEvent;
import com.mattharrah.gedcom4j.Gedcom;
import com.mattharrah.gedcom4j.Header;
import com.mattharrah.gedcom4j.HeaderSourceData;
import com.mattharrah.gedcom4j.Individual;
import com.mattharrah.gedcom4j.LdsFamilyOrdinance;
import com.mattharrah.gedcom4j.Multimedia;
import com.mattharrah.gedcom4j.Note;
import com.mattharrah.gedcom4j.Place;
import com.mattharrah.gedcom4j.Repository;
import com.mattharrah.gedcom4j.RepositoryCitation;
import com.mattharrah.gedcom4j.Source;
import com.mattharrah.gedcom4j.SourceCallNumber;
import com.mattharrah.gedcom4j.SourceData;
import com.mattharrah.gedcom4j.SourceSystem;
import com.mattharrah.gedcom4j.Submission;
import com.mattharrah.gedcom4j.Submitter;
import com.mattharrah.gedcom4j.UserReference;
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
	 * The {@link PrintWriter} we are writing to
	 */
	private PrintWriter pw;

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
		pw = new PrintWriter(out);
		emitHeader();
		emitSubmissionRecord();
		emitRecords();
		emitTrailer();
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
	 * @param level
	 * @param address
	 */
	private void emitAddress(int level, Address address) {
		if (address == null) {
			return;
		}
		emitLinesOfText(level, "ADDR", address.lines);
		emitTagIfValueNotNull(level + 1, null, "ADR1", address.addr1);
		emitTagIfValueNotNull(level + 1, null, "ADR2", address.addr2);
		emitTagIfValueNotNull(level + 1, null, "CITY", address.city);
		emitTagIfValueNotNull(level + 1, null, "STAE", address.stateProvince);
		emitTagIfValueNotNull(level + 1, null, "POST", address.postalCode);
		emitTagIfValueNotNull(level + 1, null, "CTRY", address.country);
	}

	private void emitChangeDate(int level, ChangeDate cd)
	        throws GedcomWriterException {
		if (cd != null) {
			emitTag(level, null, "CHAN");
			emitTagWithRequiredValue(level + 1, null, "DATE", cd.date);
			emitTagIfValueNotNull(level + 2, null, "TIME", cd.time);
			emitNotes(level + 1, cd.notes);
		}
	}

	private void emitCitationWithoutSource(int level, Citation c)
	        throws GedcomWriterException {
		CitationWithoutSource cws = (CitationWithoutSource) c;
		emitLinesOfText(level, "SOUR", cws.description);
		for (List<String> linesOfText : cws.textFromSource) {
			emitLinesOfText(level, "TEXT", linesOfText);
		}
		emitNotes(level + 1, cws.notes);
	}

	/**
	 * Emit a citation with source
	 * 
	 * @param level
	 * @param cws
	 * @throws GedcomWriterException
	 */
	private void emitCitationWithSource(int level, CitationWithSource cws)
	        throws GedcomWriterException {
		Source source = cws.source;
		if (source == null || source.xref == null || source.xref.isEmpty()) {
			throw new GedcomWriterException(
			        "Citation with source must have a source record with an xref/id");
		}
		emitTagWithRequiredValue(level, null, "SOUR", source.xref);
		emitTagIfValueNotNull(level + 1, null, "PAGE", cws.whereInSource);
		emitTagIfValueNotNull(level + 1, null, "EVEN", cws.eventCited);
		emitTagIfValueNotNull(level + 2, null, "ROLE", cws.roleInEvent);
		if (cws.data != null && !cws.data.isEmpty()) {
			emitTag(level + 1, null, "DATA");
			for (CitationData cd : cws.data) {
				emitTagIfValueNotNull(level + 2, null, "DATE", cd.entryDate);
				for (List<String> linesOfText : cd.sourceText) {
					emitLinesOfText(level + 2, "TEXT", linesOfText);
				}
			}
		}
		emitTagIfValueNotNull(level + 1, null, "QUAY", cws.certainty);
		emitMultimediaLinks(level + 1, cws.multimedia);
		emitNotes(level + 1, cws.notes);
	}

	/**
	 * Write out all the Families
	 * 
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitFamilies() throws GedcomWriterException {
		for (Family f : gedcom.families.values()) {
			emitTag(0, f.xref, "FAM");
			for (FamilyEvent e : f.events) {
				emitFamilyEventStructure(1, e);
			}
			if (f.husband != null) {
				emitTagWithRequiredValue(1, null, "HUSB", f.husband.xref);
			}
			if (f.wife != null) {
				emitTagWithRequiredValue(1, null, "WIFE", f.wife.xref);
			}
			for (Individual i : f.children) {
				emitTagWithRequiredValue(1, null, "CHIL", i.xref);
			}
			emitTagIfValueNotNull(1, null, "NCHI", f.numChildren);
			for (Submitter s : f.submitters) {
				emitTagWithRequiredValue(1, null, "SUBM", s.xref);
			}
			for (LdsFamilyOrdinance s : f.ldsSpouseSealings) {
				emitLdsFamilyOrdinance(1, s);
			}
			emitSourceCitations(1, f.citations);
			emitMultimediaLinks(1, f.multimedia);
			emitNotes(1, f.notes);
			for (UserReference u : f.userReferences) {
				emitTagWithRequiredValue(1, null, "REFN", u.referenceNum);
				emitTagIfValueNotNull(2, null, "TYPE", u.type);
			}
			emitTagIfValueNotNull(1, null, "RIN", f.automatedRecordId);
			emitChangeDate(1, f.changeDate);
		}
	}

	/**
	 * Emit a family event structure (see FAMILY_EVENT_STRUCTURE in the GEDCOM
	 * spec)
	 * 
	 * @param level
	 *            the level we're writing at
	 * @param e
	 *            the event
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitFamilyEventStructure(int level, FamilyEvent e)
	        throws GedcomWriterException {
		emitTagWithOptionalValue(level, null, e.type.tag, e.yNull);
		emitTagIfValueNotNull(level + 1, null, "TYPE", e.subType);
		emitTagIfValueNotNull(level + 1, null, "DATE", e.date);
		if (e.place != null) {
			Place p = e.place;
			emitTagWithOptionalValue(level + 1, null, "PLAC", p.placeName);
			emitTagIfValueNotNull(level + 2, null, "FORM", p.placeFormat);
			emitSourceCitations(level + 2, p.citations);
			emitNotes(level + 2, p.notes);
		}
		emitAddress(level + 1, e.address);
		emitTagIfValueNotNull(level + 1, null, "AGE", e.age);
		emitTagIfValueNotNull(level + 1, null, "AGNC", e.respAgency);
		emitTagIfValueNotNull(level + 1, null, "CAUS", e.cause);
		emitSourceCitations(level + 1, e.citations);
		emitMultimediaLinks(level + 1, e.multimedia);
		emitNotes(level + 1, e.notes);
		if (e.husbandAge != null) {
			emitTag(level + 1, null, "HUSB");
			emitTagWithRequiredValue(level + 2, null, "AGE", e.husbandAge);
		}
		if (e.wifeAge != null) {
			emitTag(level + 1, null, "WIFE");
			emitTagWithRequiredValue(level + 2, null, "AGE", e.wifeAge);
		}
	}

	/**
	 * Write the header record (see the HEADER structure in the GEDCOM standard)
	 * 
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitHeader() throws GedcomWriterException {
		Header header = gedcom.header;
		if (header == null) {
			header = new Header();
		}
		pw.println("0 HEAD");
		emitSourceSystem(header.sourceSystem);
		emitTagIfValueNotNull(1, null, "DEST", header.destinationSystem);
		if (header.date != null) {
			emitTagIfValueNotNull(1, null, "DATE", header.date);
			emitTagIfValueNotNull(2, null, "TIME", header.time);
		}
		if (header.submitter != null) {
			emitTagWithRequiredValue(1, null, "SUBM", header.submitter.xref);
		}
		if (header.submission != null) {
			emitTagWithRequiredValue(1, null, "SUBN", header.submission.xref);
		}
		emitTagIfValueNotNull(1, null, "FILE", header.fileName);
		emitTagIfValueNotNull(1, null, "COPR", header.copyrightData);
		emitTag(1, null, "GEDC");
		emitTagWithRequiredValue(2, null, "VERS",
		        header.gedcomVersion.versionNumber);
		emitTagWithRequiredValue(2, null, "FORM",
		        header.gedcomVersion.gedcomForm);
		emitTagWithRequiredValue(1, null, "CHAR",
		        header.characterSet.characterSetName);
		emitTagIfValueNotNull(2, null, "VERS", header.characterSet.versionNum);
		emitTagIfValueNotNull(1, null, "LANG", header.language);
		if (header.placeStructure != null && !header.placeStructure.isEmpty()) {
			// TODO - need better handling for PLAC structures in the header
			emitTag(1, null, "PLAC");
			emitTagWithRequiredValue(2, null, "FORM", header.placeStructure);
		}
		emitNoteLines(1, null, header.notes);
	}

	/**
	 * Write out all the individuals
	 * 
	 */
	private void emitIndividuals() {
		// TODO write out individuals

	}

	/**
	 * @param level
	 *            the level we're writing at
	 * @param sealings
	 *            the {@link LdsFamilyOrdinance} structure
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitLdsFamilyOrdinance(int level, LdsFamilyOrdinance sealings)
	        throws GedcomWriterException {
		emitTag(level, null, "SLGS");
		emitTagIfValueNotNull(level + 1, null, "STAT", sealings.status);
		emitTagIfValueNotNull(level + 1, null, "DATE", sealings.date);
		emitTagIfValueNotNull(level + 1, null, "TEMP", sealings.temple);
		emitTagIfValueNotNull(level + 1, null, "PLAC", sealings.place);
		emitSourceCitations(level + 1, sealings.citations);
		emitNotes(level + 1, sealings.notes);

	}

	/**
	 * Emit a multi-line text value.
	 * 
	 * @param level
	 *            the level we are starting at. Continuation lines will be one
	 *            level deeper than this value
	 * @param startingTag
	 *            the tag to use for the first line of the text. All subsequent
	 *            lines will be "CONT" lines.
	 * @param linesOfText
	 *            the lines of text
	 */
	private void emitLinesOfText(int level, String startingTag,
	        List<String> linesOfText) {
		int lineNum = 0;
		for (String l : linesOfText) {
			if (lineNum++ == 0) {
				emitTagIfValueNotNull(level, null, startingTag, l);
			} else {
				emitTagIfValueNotNull(level + 1, null, "CONT", l);
			}
		}
	}

	/**
	 * Write out all the embedded multimedia objects
	 * 
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitMultimedia() throws GedcomWriterException {
		for (Multimedia m : gedcom.multimedia.values()) {
			emitTag(0, m.xref, "OBJE");
			emitTagWithRequiredValue(1, null, "FORM", m.format);
			emitTagIfValueNotNull(1, null, "TITL", m.title);
			emitNotes(1, m.notes);
			emitTag(1, null, "BLOB");
			for (String b : m.blob) {
				emitTagWithRequiredValue(2, null, "CONT", b);
			}
			if (m.continuedObject != null && m.continuedObject.xref != null) {
				emitTagWithRequiredValue(1, null, "OBJE",
				        m.continuedObject.xref);
			}
			for (UserReference u : m.userReferences) {
				emitTagWithRequiredValue(1, null, "REFN", u.referenceNum);
				emitTagIfValueNotNull(2, null, "TYPE", u.type);
			}
			emitTagIfValueNotNull(1, null, "RIN", m.recIdNumber);
			emitChangeDate(1, m.changeDate);
		}
	}

	/**
	 * Emit a list of multimedia links
	 * 
	 * @param level
	 *            the level in the hierarchy we are writing at
	 * @param multimedia
	 *            the {@link List} of {@link Multimedia} objects
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitMultimediaLinks(int level, List<Multimedia> multimedia)
	        throws GedcomWriterException {
		if (multimedia == null) {
			return;
		}
		for (Multimedia m : multimedia) {
			if (m.xref != null) {
				// Link to the embedded form
				emitTagWithRequiredValue(level, null, "OBJE", m.xref);
			} else {
				// Link to external file
				emitTag(level, null, "OBJE");
				emitTagWithRequiredValue(level + 1, null, "FORM", m.format);
				emitTagIfValueNotNull(level + 1, null, "TITL", m.title);
				emitTagWithRequiredValue(level + 1, null, "FILE",
				        m.fileReference);
				emitNotes(level + 1, m.notes);
			}
		}
	}

	/**
	 * Emit a note structure (possibly multi-line)
	 * 
	 * @param level
	 *            the level in the hierarchy we are writing at
	 * @param notes
	 *            the Notes text
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitNote(int level, Note note) throws GedcomWriterException {
		if (level > 0 && note.xref != null) {
			emitTagWithRequiredValue(level, null, "NOTE", note.xref);
			return;
		}
		emitNoteLines(level, note.xref, note.lines);
		emitSourceCitations(level + 1, note.citations);
		for (UserReference u : note.userReferences) {
			emitTagWithRequiredValue(level + 1, null, "REFN", u.referenceNum);
			emitTagIfValueNotNull(level + 2, null, "TYPE", u.type);
		}
		emitTagIfValueNotNull(level + 1, null, "RIN", note.recIdNumber);
		emitChangeDate(level + 1, note.changeDate);
	}

	/**
	 * Emit line(s) of a note
	 * 
	 * @param level
	 *            the level in the hierarchy we are writing at
	 * @param notes
	 *            the Notes text
	 */
	private void emitNoteLines(int level, String xref, List<String> notes) {
		int noteLineNum = 0;
		for (String n : notes) {
			if (noteLineNum++ == 0) {
				emitTagIfValueNotNull(level, xref, "NOTE", n);
			} else {
				pw.println(level + 1 + " " + "CONT"
				        + (n == null ? "" : " " + n));
			}
		}
	}

	/**
	 * Emit a list of note structures
	 * 
	 * @param level
	 *            the level in the hierarchy we are writing at
	 * @param notes
	 *            a list of {@link Note} structures
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitNotes(int level, List<Note> notes)
	        throws GedcomWriterException {
		for (Note n : notes) {
			emitNote(level, n);
		}
	}

	/**
	 * Write out a list of phone numbers
	 * 
	 * @param level
	 *            the level in the hierarchy we are writing at
	 * @param phoneNumbers
	 *            a list of phone numbers
	 */
	private void emitPhoneNumbers(int level, List<String> phoneNumbers) {
		for (String ph : phoneNumbers) {
			emitTagIfValueNotNull(level, null, "PHON", ph);
		}
	}

	/**
	 * Write the records (see the RECORD structure in the GEDCOM standard)
	 * 
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitRecords() throws GedcomWriterException {
		emitIndividuals();
		emitFamilies();
		emitMultimedia();
		emitNotes(0, new ArrayList<Note>(gedcom.notes.values()));
		emitRepositories();
		emitSources();
		emitSubmitter();
	}

	/**
	 * Write out all the repositories (see REPOSITORY_RECORD in the Gedcom spec)
	 * 
	 * @throws GedcomWriterException
	 *             if the data being written is malformed
	 */
	private void emitRepositories() throws GedcomWriterException {
		for (Repository r : gedcom.repositories.values()) {
			emitTag(0, r.xref, "REPO");
			emitTagIfValueNotNull(1, null, "NAME", r.name);
			emitAddress(1, r.address);
			emitNotes(1, r.notes);
			for (UserReference u : r.userReferences) {
				emitTagWithRequiredValue(1, null, "REFN", u.referenceNum);
				emitTagIfValueNotNull(2, null, "TYPE", u.type);
			}
			emitTagIfValueNotNull(1, null, "RIN", r.recIdNumber);
			emitTagIfValueNotNull(1, null, "RFN", r.regFileNumber);
			emitPhoneNumbers(1, r.phoneNumbers);
			emitChangeDate(1, r.changeDate);
		}
	}

	/**
	 * Write out a repository citation (see SOURCE_REPOSITORY_CITATION in the
	 * gedcom spec)
	 * 
	 * @param level
	 *            the level we're writing at
	 * @param repositoryCitation
	 *            the repository citation to write out
	 * @throws GedcomWriterException
	 *             if the repository citation passed in has a null repository
	 *             reference
	 */
	private void emitRepositoryCitation(int level,
	        RepositoryCitation repositoryCitation) throws GedcomWriterException {
		if (repositoryCitation != null) {
			if (repositoryCitation.repository == null) {
				throw new GedcomWriterException(
				        "Repository Citation has null repository reference");
			}
			emitTagWithRequiredValue(level, null, "REPO",
			        repositoryCitation.repository.xref);
			emitNotes(level + 1, repositoryCitation.notes);
			for (SourceCallNumber scn : repositoryCitation.callNumbers) {
				emitTagWithRequiredValue(level + 1, null, "CALN",
				        scn.callNumber);
				emitTagIfValueNotNull(level + 2, null, "MEDI", scn.mediaType);
			}
		}

	}

	/**
	 * Write out a list of source citations
	 * 
	 * @param level
	 *            the level in the hierarchy we are writing at
	 * @param citations
	 *            the source citations
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitSourceCitations(int level, List<Citation> citations)
	        throws GedcomWriterException {
		if (citations == null) {
			return;
		}
		for (Citation c : citations) {
			if (c instanceof CitationWithoutSource) {
				emitCitationWithoutSource(level, c);
			} else if (c instanceof CitationWithSource) {
				emitCitationWithSource(level, (CitationWithSource) c);
			}
		}

	}

	/**
	 * Write out all the sources (see SOURCE_RECORD in the Gedcom spec)
	 * 
	 * @throws GedcomWriterException
	 *             if the data being written is malformed
	 */
	private void emitSources() throws GedcomWriterException {
		for (Source s : gedcom.sources.values()) {
			emitTag(0, s.xref, "SOUR");
			SourceData d = s.data;
			if (d != null) {
				emitTag(1, null, "DATA");
				for (EventRecorded e : d.eventsRecorded) {
					emitTagWithOptionalValue(2, null, "EVEN", e.eventType);
					emitTagIfValueNotNull(2, null, "DATE", e.datePeriod);
					emitTagIfValueNotNull(2, null, "PLAC", e.jurisdiction);
				}
				emitTagIfValueNotNull(2, null, "AGNC", d.respAgency);
				emitNotes(2, d.notes);
			}
			emitLinesOfText(1, "AUTH", s.originatorsAuthors);
			emitLinesOfText(1, "TITL", s.title);
			emitTagIfValueNotNull(1, null, "ABBR", s.sourceFiledBy);
			emitLinesOfText(1, "PUBL", s.publicationFacts);
			emitLinesOfText(1, "TEXT", s.sourceText);
			emitRepositoryCitation(1, s.repositoryCitation);
			emitMultimediaLinks(1, s.multimedia);
			emitNotes(1, s.notes);
			for (UserReference u : s.userReferences) {
				emitTagWithRequiredValue(1, null, "REFN", u.referenceNum);
				emitTagIfValueNotNull(2, null, "TYPE", u.type);
			}
			emitTagIfValueNotNull(1, null, "RIN", s.recIdNumber);
			emitTagIfValueNotNull(1, null, "RFN", s.regFileNumber);
			emitChangeDate(1, s.changeDate);
		}
	}

	/**
	 * Write a source system structure (see APPROVED_SYSTEM_ID in the GEDCOM
	 * spec)
	 * 
	 * @param sourceSystem
	 *            the source system
	 * @throws GedcomWriterException
	 *             if data is malformed and cannot be written
	 */
	private void emitSourceSystem(SourceSystem sourceSystem)
	        throws GedcomWriterException {
		if (sourceSystem == null) {
			return;
		}
		emitTagIfValueNotNull(1, null, "SOUR", sourceSystem.systemId);
		emitTagWithOptionalValue(2, null, "VERS", sourceSystem.versionNum);
		emitTagWithOptionalValue(2, null, "NAME", sourceSystem.productName);
		Corporation corporation = sourceSystem.corporation;
		if (corporation != null) {
			emitTagWithOptionalValue(2, null, "CORP", corporation.businessName);
			emitAddress(3, corporation.address);
			emitPhoneNumbers(3, corporation.phoneNumbers);
		}
		HeaderSourceData sourceData = sourceSystem.sourceData;
		if (sourceData != null) {
			emitTagIfValueNotNull(2, null, "DATA", sourceData.name);
			emitTagIfValueNotNull(3, null, "DATE", sourceData.publishDate);
			emitTagIfValueNotNull(3, null, "COPR", sourceData.copyright);
		}
	}

	/**
	 * Write the SUBMISSION_RECORD
	 * 
	 * @param gedcom
	 *            the {@link Gedcom} structure containing the header
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitSubmissionRecord() throws GedcomWriterException {
		Submission s = gedcom.submission;
		if (s == null) {
			return;
		}
		emitTag(0, s.xref, "SUBN");
		if (s.submitter != null) {
			emitTagWithOptionalValue(1, null, "SUBM", s.submitter.xref);
		}
		emitTagIfValueNotNull(1, null, "FAMF", s.nameOfFamilyFile);
		emitTagIfValueNotNull(1, null, "TEMP", s.templeCode);
		emitTagIfValueNotNull(1, null, "ANCE", s.ancestorsCount);
		emitTagIfValueNotNull(1, null, "DESC", s.descendantsCount);
		emitTagIfValueNotNull(1, null, "ORDI", s.ordinanceProcessFlag);
		emitTagIfValueNotNull(1, null, "RIN", s.recIdNumber);
	}

	/**
	 * Write out the submitter record
	 * 
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitSubmitter() throws GedcomWriterException {
		for (Submitter s : gedcom.submitters.values()) {
			emitTag(0, s.xref, "SUBM");
			emitTagWithOptionalValue(1, null, "NAME", s.name);
			emitAddress(1, s.address);
			emitMultimediaLinks(1, s.multimedia);
			for (String l : s.languagePref) {
				emitTagWithRequiredValue(1, null, "LANG", l);
			}
			/*
			 * Unclear if really part of the GEDCOM or not - a stress test file
			 * includes them, and the tool can parse them, so if we have them,
			 * write them. Won't hurt anything if the collection is empty.
			 */
			for (String l : s.phoneNumbers) {
				emitTagWithRequiredValue(1, null, "PHON", l);
			}

			emitTagIfValueNotNull(1, null, "RFN", s.regFileNumber);
			emitTagIfValueNotNull(1, null, "RIN", s.recIdNumber);
			emitChangeDate(1, s.changeDate);
		}
	}

	/**
	 * Write a line with a tag, with no value following the tag
	 * 
	 * @param level
	 *            the level within the file hierarchy
	 * @param tag
	 *            the tag for the line of the file
	 */
	private void emitTag(int level, String xref, String tag) {
		pw.print(level);
		if (xref != null && !xref.isEmpty()) {
			pw.print(" " + xref);
		}
		pw.println(" " + tag);
	}

	/**
	 * Write a line if the value is non-null
	 * 
	 * @param level
	 *            the level within the file hierarchy
	 * @param tag
	 *            the tag for the line of the file
	 * @param value
	 *            the value to write to the right of the tag
	 */
	private void emitTagIfValueNotNull(int level, String xref, String tag,
	        Object value) {
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
	private void emitTagWithOptionalValue(int level, String xref, String tag,
	        String value) throws GedcomWriterException {
		if (value == null) {
			emitTag(level, xref, tag);
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
	private void emitTagWithRequiredValue(int level, String xref, String tag,
	        String value) throws GedcomWriterException {
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
	private void emitTrailer() {
		pw.println("0 TRLR");
	}
}
