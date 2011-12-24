package com.mattharrah.gedcom4j.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.mattharrah.gedcom4j.Address;
import com.mattharrah.gedcom4j.Association;
import com.mattharrah.gedcom4j.ChangeDate;
import com.mattharrah.gedcom4j.Citation;
import com.mattharrah.gedcom4j.CitationData;
import com.mattharrah.gedcom4j.CitationWithSource;
import com.mattharrah.gedcom4j.CitationWithoutSource;
import com.mattharrah.gedcom4j.Corporation;
import com.mattharrah.gedcom4j.Event;
import com.mattharrah.gedcom4j.EventRecorded;
import com.mattharrah.gedcom4j.Family;
import com.mattharrah.gedcom4j.FamilyChild;
import com.mattharrah.gedcom4j.FamilyEvent;
import com.mattharrah.gedcom4j.FamilySpouse;
import com.mattharrah.gedcom4j.Gedcom;
import com.mattharrah.gedcom4j.Header;
import com.mattharrah.gedcom4j.HeaderSourceData;
import com.mattharrah.gedcom4j.Individual;
import com.mattharrah.gedcom4j.IndividualAttribute;
import com.mattharrah.gedcom4j.IndividualEvent;
import com.mattharrah.gedcom4j.IndividualEventType;
import com.mattharrah.gedcom4j.LdsIndividualOrdinance;
import com.mattharrah.gedcom4j.LdsIndividualOrdinanceType;
import com.mattharrah.gedcom4j.LdsSpouseSealing;
import com.mattharrah.gedcom4j.Multimedia;
import com.mattharrah.gedcom4j.Note;
import com.mattharrah.gedcom4j.PersonalName;
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

/**
 * A class for writing the gedcom structure out as a GEDCOM 5.5 compliant file.
 * 
 * @author frizbog1
 */
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
	 * @param out
	 *            the output stream we're writing to
	 * @throws GedcomValidationException
	 *             if the gedcom data has validation errors that won't allow it
	 *             to be written
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
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
		emitTagIfValueNotNull(level + 1, "ADR1", address.addr1);
		emitTagIfValueNotNull(level + 1, "ADR2", address.addr2);
		emitTagIfValueNotNull(level + 1, "CITY", address.city);
		emitTagIfValueNotNull(level + 1, "STAE", address.stateProvince);
		emitTagIfValueNotNull(level + 1, "POST", address.postalCode);
		emitTagIfValueNotNull(level + 1, "CTRY", address.country);
	}

	/**
	 * Emit the person-to-person associations an individual was in - see
	 * ASSOCIATION_STRUCTURE in the GEDCOM spec.
	 * 
	 * @param level
	 *            the level at which to start recording
	 * @param associations
	 *            the list of associations
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitAssociationStructures(int level,
	        List<Association> associations) throws GedcomWriterException {
		for (Association a : associations) {
			emitTagWithRequiredValue(level, "ASSO", a.associatedEntityXref);
			emitTagIfValueNotNull(level + 1, "TYPE", a.associatedEntityType);
			emitTagWithRequiredValue(level + 1, "RELA", a.relationship);
			emitNotes(level + 1, a.notes);
			emitSourceCitations(level + 1, a.citations);
		}
	}

	/**
	 * Emit a change date
	 * 
	 * @param level
	 *            the level at which we are emitting data
	 * @param cd
	 *            the change date
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitChangeDate(int level, ChangeDate cd)
	        throws GedcomWriterException {
		if (cd != null) {
			emitTag(level, "CHAN");
			emitTagWithRequiredValue(level + 1, "DATE", cd.date);
			emitTagIfValueNotNull(level + 2, "TIME", cd.time);
			emitNotes(level + 1, cd.notes);
		}
	}

	/**
	 * Emit links to all the families to which this individual was a child
	 * 
	 * @param level
	 *            the level in the hierarchy at which we are emitting
	 * @param i
	 *            the individual we're dealing with
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitChildToFamilyLinks(int level, Individual i)
	        throws GedcomWriterException {
		for (FamilyChild familyChild : i.familiesWhereChild) {
			if (familyChild == null) {
				throw new GedcomWriterException("Family to which " + i
				        + " was a child was null");
			}
			if (familyChild.family == null) {
				throw new GedcomWriterException("Family to which " + i
				        + " was a child had a null family reference");
			}
			emitTagWithRequiredValue(level, "FAMC", familyChild.family.xref);
			emitTagIfValueNotNull(level + 1, "PEDI", familyChild.pedigree);
			emitNotes(level + 1, familyChild.notes);
		}
	}

	/**
	 * Emit a citation without a source
	 * 
	 * @param level
	 *            the level in the hierarchy at which we are emitting data
	 * @param c
	 *            the citation
	 * @throws GedcomWriterException
	 */
	private void emitCitationWithoutSource(int level, Citation c)
	        throws GedcomWriterException {
		CitationWithoutSource cws = (CitationWithoutSource) c;
		emitLinesOfText(level, "SOUR", cws.description);
		for (List<String> linesOfText : cws.textFromSource) {
			emitLinesOfText(level + 1, "TEXT", linesOfText);
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
		emitTagWithRequiredValue(level, "SOUR", source.xref);
		emitTagIfValueNotNull(level + 1, "PAGE", cws.whereInSource);
		emitTagIfValueNotNull(level + 1, "EVEN", cws.eventCited);
		emitTagIfValueNotNull(level + 2, "ROLE", cws.roleInEvent);
		if (cws.data != null && !cws.data.isEmpty()) {
			emitTag(level + 1, "DATA");
			for (CitationData cd : cws.data) {
				emitTagIfValueNotNull(level + 2, "DATE", cd.entryDate);
				for (List<String> linesOfText : cd.sourceText) {
					emitLinesOfText(level + 2, "TEXT", linesOfText);
				}
			}
		}
		emitTagIfValueNotNull(level + 1, "QUAY", cws.certainty);
		emitMultimediaLinks(level + 1, cws.multimedia);
		emitNotes(level + 1, cws.notes);
	}

	/**
	 * Emit an event detail structure - see EVENT_DETAIL in the GEDCOM spec
	 * 
	 * @param level
	 *            the level at which we are emitting data
	 * @param e
	 *            the event being emitted
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitEventDetail(int level, Event e)
	        throws GedcomWriterException {
		emitTagIfValueNotNull(level, "TYPE", e.subType);
		emitTagIfValueNotNull(level, "DATE", e.date);
		if (e.place != null) {
			Place p = e.place;
			emitTagWithOptionalValue(level, "PLAC", p.placeName);
			emitTagIfValueNotNull(level + 1, "FORM", p.placeFormat);
			emitSourceCitations(level + 1, p.citations);
			emitNotes(level + 1, p.notes);
		}
		emitAddress(level, e.address);
		emitTagIfValueNotNull(level, "AGE", e.age);
		emitTagIfValueNotNull(level, "AGNC", e.respAgency);
		emitTagIfValueNotNull(level, "CAUS", e.cause);
		emitSourceCitations(level, e.citations);
		emitMultimediaLinks(level, e.multimedia);
		emitNotes(level, e.notes);
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
				emitTagWithRequiredValue(1, "HUSB", f.husband.xref);
			}
			if (f.wife != null) {
				emitTagWithRequiredValue(1, "WIFE", f.wife.xref);
			}
			for (Individual i : f.children) {
				emitTagWithRequiredValue(1, "CHIL", i.xref);
			}
			emitTagIfValueNotNull(1, "NCHI", f.numChildren);
			for (Submitter s : f.submitters) {
				emitTagWithRequiredValue(1, "SUBM", s.xref);
			}
			for (LdsSpouseSealing s : f.ldsSpouseSealings) {
				emitLdsFamilyOrdinance(1, s);
			}
			emitSourceCitations(1, f.citations);
			emitMultimediaLinks(1, f.multimedia);
			emitNotes(1, f.notes);
			for (UserReference u : f.userReferences) {
				emitTagWithRequiredValue(1, "REFN", u.referenceNum);
				emitTagIfValueNotNull(2, "TYPE", u.type);
			}
			emitTagIfValueNotNull(1, "RIN", f.automatedRecordId);
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
		emitTagWithOptionalValue(level, e.type.tag, e.yNull);
		emitEventDetail(level + 1, e);
		if (e.husbandAge != null) {
			emitTag(level + 1, "HUSB");
			emitTagWithRequiredValue(level + 2, "AGE", e.husbandAge);
		}
		if (e.wifeAge != null) {
			emitTag(level + 1, "WIFE");
			emitTagWithRequiredValue(level + 2, "AGE", e.wifeAge);
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
		emitTagIfValueNotNull(1, "DEST", header.destinationSystem);
		if (header.date != null) {
			emitTagIfValueNotNull(1, "DATE", header.date);
			emitTagIfValueNotNull(2, "TIME", header.time);
		}
		if (header.submitter != null) {
			emitTagWithRequiredValue(1, "SUBM", header.submitter.xref);
		}
		if (header.submission != null) {
			emitTagWithRequiredValue(1, "SUBN", header.submission.xref);
		}
		emitTagIfValueNotNull(1, "FILE", header.fileName);
		emitTagIfValueNotNull(1, "COPR", header.copyrightData);
		emitTag(1, "GEDC");
		emitTagWithRequiredValue(2, "VERS", header.gedcomVersion.versionNumber);
		emitTagWithRequiredValue(2, "FORM", header.gedcomVersion.gedcomForm);
		emitTagWithRequiredValue(1, "CHAR",
		        header.characterSet.characterSetName);
		emitTagIfValueNotNull(2, "VERS", header.characterSet.versionNum);
		emitTagIfValueNotNull(1, "LANG", header.language);
		if (header.placeStructure != null && !header.placeStructure.isEmpty()) {
			// TODO - need better handling for PLAC structures in the header
			emitTag(1, "PLAC");
			emitTagWithRequiredValue(2, "FORM", header.placeStructure);
		}
		emitNoteLines(1, null, header.notes);
	}

	/**
	 * Emit a collection of individual attributes
	 * 
	 * @param level
	 *            the level to start emitting at
	 * @param attributes
	 *            the attributes
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitIndividualAttributes(int level,
	        List<IndividualAttribute> attributes) throws GedcomWriterException {
		for (IndividualAttribute a : attributes) {
			emitTagWithOptionalValue(level, a.type.tag, a.description);
			emitEventDetail(level + 1, a);
			emitPhoneNumbers(level + 1, a.phoneNumbers);
		}
	}

	/**
	 * Emit a collection of individual events
	 * 
	 * @param level
	 *            the level to start emitting at
	 * @param events
	 *            the events
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitIndividualEvents(int level, List<IndividualEvent> events)
	        throws GedcomWriterException {
		for (IndividualEvent e : events) {
			emitTagWithOptionalValue(level, e.type.tag, e.yNull);
			emitEventDetail(level + 1, e);
			if (e.type == IndividualEventType.BIRTH
			        || e.type == IndividualEventType.CHRISTENING) {
				if (e.family != null && e.family.family != null
				        && e.family.family.xref != null) {
					emitTagWithRequiredValue(level + 1, "FAMC",
					        e.family.family.xref);
				}
			} else if (e.type == IndividualEventType.ADOPTION) {
				if (e.family != null && e.family.family != null
				        && e.family.family.xref != null) {
					emitTagWithRequiredValue(level + 1, "FAMC",
					        e.family.family.xref);
					emitTagIfValueNotNull(level + 2, "ADOP", e.family.adoptedBy);
				}
			}
		}
	}

	/**
	 * Write out all the individuals at the root level
	 * 
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitIndividuals() throws GedcomWriterException {
		for (Individual i : gedcom.individuals.values()) {
			emitTag(0, i.xref, "INDI");
			emitTagIfValueNotNull(1, "RESN", i.restrictionNotice);
			emitPersonalNames(1, i.names);
			emitTagIfValueNotNull(1, "SEX", i.sex);
			emitIndividualEvents(1, i.events);
			emitIndividualAttributes(1, i.attributes);
			emitLdsIndividualOrdinances(1, i.ldsIndividualOrdinances);
			emitChildToFamilyLinks(1, i);
			emitSpouseInFamilyLinks(1, i);
			for (Submitter s : i.submitters) {
				emitTagWithRequiredValue(1, "SUBM", s.xref);
			}
			emitAssociationStructures(1, i.associations);
			for (String s : i.aliases) {
				emitTagWithRequiredValue(1, "ALIA", s);
			}
			for (Submitter s : i.ancestorInterest) {
				emitTagWithRequiredValue(1, "ANCI", s.xref);
			}
			for (Submitter s : i.descendantInterest) {
				emitTagWithRequiredValue(1, "DESI", s.xref);
			}
			emitSourceCitations(1, i.citations);
			emitMultimediaLinks(1, i.multimedia);
			emitNotes(1, i.notes);
			emitTagIfValueNotNull(1, "RFN", i.permanentRecFileNumber);
			emitTagIfValueNotNull(1, "AFN", i.ancestralFileNumber);
			for (UserReference u : i.userReferences) {
				emitTagWithRequiredValue(1, "REFN", u.referenceNum);
				emitTagIfValueNotNull(2, "TYPE", u.type);
			}
			emitTagIfValueNotNull(1, "RIN", i.recIdNumber);
			emitChangeDate(1, i.changeDate);

		}

	}

	/**
	 * Emit the LDS spouse sealing information
	 * 
	 * @param level
	 *            the level we're writing at
	 * @param sealings
	 *            the {@link LdsSpouseSealing} structure
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitLdsFamilyOrdinance(int level, LdsSpouseSealing sealings)
	        throws GedcomWriterException {
		emitTag(level, "SLGS");
		emitTagIfValueNotNull(level + 1, "STAT", sealings.status);
		emitTagIfValueNotNull(level + 1, "DATE", sealings.date);
		emitTagIfValueNotNull(level + 1, "TEMP", sealings.temple);
		emitTagIfValueNotNull(level + 1, "PLAC", sealings.place);
		emitSourceCitations(level + 1, sealings.citations);
		emitNotes(level + 1, sealings.notes);

	}

	/**
	 * Emit the LDS individual ordinances
	 * 
	 * @param level
	 *            the level in the hierarchy we are processing
	 * @param ldsIndividualOrdinances
	 *            the list of LDS individual ordinances to emit
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitLdsIndividualOrdinances(int level,
	        List<LdsIndividualOrdinance> ldsIndividualOrdinances)
	        throws GedcomWriterException {
		for (LdsIndividualOrdinance o : ldsIndividualOrdinances) {
			emitTagWithOptionalValue(level, o.type.tag, o.yNull);
			emitTagIfValueNotNull(level + 1, "STAT", o.status);
			emitTagIfValueNotNull(level + 1, "DATE", o.date);
			emitTagIfValueNotNull(level + 1, "TEMP", o.temple);
			emitTagIfValueNotNull(level + 1, "PLAC", o.place);
			if (o.type == LdsIndividualOrdinanceType.CHILD_SEALING) {
				if (o.familyWhereChild == null) {
					throw new GedcomWriterException(
					        "LDS Ordinance info for a child sealing had no reference to a family");
				}
				if (o.familyWhereChild.family == null) {
					throw new GedcomWriterException(
					        "LDS Ordinance info for a child sealing had familyChild object with a null reference to a family");
				}
				emitTagWithRequiredValue(level + 1, "FAMC",
				        o.familyWhereChild.family.xref);
			}
			emitSourceCitations(level + 1, o.citations);
			emitNotes(level + 1, o.notes);
		}
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
				emitTagIfValueNotNull(level, startingTag, l);
			} else {
				emitTagIfValueNotNull(level + 1, "CONT", l);
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
			emitTagWithRequiredValue(1, "FORM", m.format);
			emitTagIfValueNotNull(1, "TITL", m.title);
			emitNotes(1, m.notes);
			emitTag(1, "BLOB");
			for (String b : m.blob) {
				emitTagWithRequiredValue(2, "CONT", b);
			}
			if (m.continuedObject != null && m.continuedObject.xref != null) {
				emitTagWithRequiredValue(1, "OBJE", m.continuedObject.xref);
			}
			for (UserReference u : m.userReferences) {
				emitTagWithRequiredValue(1, "REFN", u.referenceNum);
				emitTagIfValueNotNull(2, "TYPE", u.type);
			}
			emitTagIfValueNotNull(1, "RIN", m.recIdNumber);
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
				emitTagWithRequiredValue(level, "OBJE", m.xref);
			} else {
				// Link to external file
				emitTag(level, "OBJE");
				emitTagWithRequiredValue(level + 1, "FORM", m.format);
				emitTagIfValueNotNull(level + 1, "TITL", m.title);
				emitTagWithRequiredValue(level + 1, "FILE", m.fileReference);
				emitNotes(level + 1, m.notes);
			}
		}
	}

	/**
	 * Emit a note structure (possibly multi-line)
	 * 
	 * @param level
	 *            the level in the hierarchy we are writing at
	 * @param note
	 *            the note structure
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitNote(int level, Note note) throws GedcomWriterException {
		if (level > 0 && note.xref != null) {
			emitTagWithRequiredValue(level, "NOTE", note.xref);
			return;
		}
		emitNoteLines(level, note.xref, note.lines);
		emitSourceCitations(level + 1, note.citations);
		for (UserReference u : note.userReferences) {
			emitTagWithRequiredValue(level + 1, "REFN", u.referenceNum);
			emitTagIfValueNotNull(level + 2, "TYPE", u.type);
		}
		emitTagIfValueNotNull(level + 1, "RIN", note.recIdNumber);
		emitChangeDate(level + 1, note.changeDate);
	}

	/**
	 * Emit line(s) of a note
	 * 
	 * @param level
	 *            the level in the hierarchy we are writing at
	 * @param xref
	 *            the xref of the note being written
	 * @param noteLines
	 *            the Notes text
	 */
	private void emitNoteLines(int level, String xref, List<String> noteLines) {
		int noteLineNum = 0;
		for (String n : noteLines) {
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
	 * Emit a list of personal names for an individual
	 * 
	 * @param level
	 *            the level to start emitting at
	 * @param names
	 *            the names
	 * @throws GedcomWriterException
	 *             if the data is malformed and cannot be written
	 */
	private void emitPersonalNames(int level, List<PersonalName> names)
	        throws GedcomWriterException {
		for (PersonalName n : names) {
			emitTagWithOptionalValue(level, "NAME", n.basic);
			emitTagIfValueNotNull(level + 1, "NPFX", n.prefix);
			emitTagIfValueNotNull(level + 1, "GIVN", n.givenName);
			emitTagIfValueNotNull(level + 1, "NICK", n.nickname);
			emitTagIfValueNotNull(level + 1, "SPFX", n.surnamePrefix);
			emitTagIfValueNotNull(level + 1, "SURN", n.surname);
			emitTagIfValueNotNull(level + 1, "NSFX", n.suffix);
			emitSourceCitations(level + 1, n.citations);
			emitNotes(level + 1, n.notes);
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
			emitTagIfValueNotNull(level, "PHON", ph);
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
			emitTagIfValueNotNull(1, "NAME", r.name);
			emitAddress(1, r.address);
			emitNotes(1, r.notes);
			for (UserReference u : r.userReferences) {
				emitTagWithRequiredValue(1, "REFN", u.referenceNum);
				emitTagIfValueNotNull(2, "TYPE", u.type);
			}
			emitTagIfValueNotNull(1, "RIN", r.recIdNumber);
			emitTagIfValueNotNull(1, "RFN", r.regFileNumber);
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
			if (repositoryCitation.repositoryXref == null) {
				throw new GedcomWriterException(
				        "Repository Citation has null repository reference");
			}
			emitTagWithRequiredValue(level, "REPO",
			        repositoryCitation.repositoryXref);
			emitNotes(level + 1, repositoryCitation.notes);
			for (SourceCallNumber scn : repositoryCitation.callNumbers) {
				emitTagWithRequiredValue(level + 1, "CALN", scn.callNumber);
				emitTagIfValueNotNull(level + 2, "MEDI", scn.mediaType);
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
				emitTag(1, "DATA");
				for (EventRecorded e : d.eventsRecorded) {
					emitTagWithOptionalValue(2, "EVEN", e.eventType);
					emitTagIfValueNotNull(3, "DATE", e.datePeriod);
					emitTagIfValueNotNull(3, "PLAC", e.jurisdiction);
				}
				emitTagIfValueNotNull(2, "AGNC", d.respAgency);
				emitNotes(2, d.notes);
			}
			emitLinesOfText(1, "AUTH", s.originatorsAuthors);
			emitLinesOfText(1, "TITL", s.title);
			emitTagIfValueNotNull(1, "ABBR", s.sourceFiledBy);
			emitLinesOfText(1, "PUBL", s.publicationFacts);
			emitLinesOfText(1, "TEXT", s.sourceText);
			emitRepositoryCitation(1, s.repositoryCitation);
			emitMultimediaLinks(1, s.multimedia);
			emitNotes(1, s.notes);
			for (UserReference u : s.userReferences) {
				emitTagWithRequiredValue(1, "REFN", u.referenceNum);
				emitTagIfValueNotNull(2, "TYPE", u.type);
			}
			emitTagIfValueNotNull(1, "RIN", s.recIdNumber);
			emitTagIfValueNotNull(1, "RFN", s.regFileNumber);
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
		emitTagIfValueNotNull(1, "SOUR", sourceSystem.systemId);
		emitTagWithOptionalValue(2, "VERS", sourceSystem.versionNum);
		emitTagWithOptionalValue(2, "NAME", sourceSystem.productName);
		Corporation corporation = sourceSystem.corporation;
		if (corporation != null) {
			emitTagWithOptionalValue(2, "CORP", corporation.businessName);
			emitAddress(3, corporation.address);
			emitPhoneNumbers(3, corporation.phoneNumbers);
		}
		HeaderSourceData sourceData = sourceSystem.sourceData;
		if (sourceData != null) {
			emitTagIfValueNotNull(2, "DATA", sourceData.name);
			emitTagIfValueNotNull(3, "DATE", sourceData.publishDate);
			emitTagIfValueNotNull(3, "COPR", sourceData.copyright);
		}
	}

	/**
	 * Emit links to all the families to which the supplied individual was a
	 * spouse
	 * 
	 * @param level
	 *            the level in the hierarchy at which we are emitting
	 * @param i
	 *            the individual we are dealing with
	 * @throws GedcomWriterException
	 *             if data is malformed and cannot be written
	 */
	private void emitSpouseInFamilyLinks(int level, Individual i)
	        throws GedcomWriterException {
		for (FamilySpouse familySpouse : i.familiesWhereSpouse) {
			if (familySpouse == null) {
				throw new GedcomWriterException("Family in which " + i
				        + " was a spouse was null");
			}
			if (familySpouse.family == null) {
				throw new GedcomWriterException("Family in which " + i
				        + " was a spouse had a null family reference");
			}
			emitTagWithRequiredValue(level, "FAMS", familySpouse.family.xref);
			emitNotes(level + 1, familySpouse.notes);
		}
	}

	/**
	 * Write the SUBMISSION_RECORD
	 * 
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
			emitTagWithOptionalValue(1, "SUBM", s.submitter.xref);
		}
		emitTagIfValueNotNull(1, "FAMF", s.nameOfFamilyFile);
		emitTagIfValueNotNull(1, "TEMP", s.templeCode);
		emitTagIfValueNotNull(1, "ANCE", s.ancestorsCount);
		emitTagIfValueNotNull(1, "DESC", s.descendantsCount);
		emitTagIfValueNotNull(1, "ORDI", s.ordinanceProcessFlag);
		emitTagIfValueNotNull(1, "RIN", s.recIdNumber);
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
			emitTagWithOptionalValue(1, "NAME", s.name);
			emitAddress(1, s.address);
			emitMultimediaLinks(1, s.multimedia);
			for (String l : s.languagePref) {
				emitTagWithRequiredValue(1, "LANG", l);
			}
			/*
			 * Unclear if really part of the GEDCOM or not - a stress test file
			 * includes them, and the tool can parse them, so if we have them,
			 * write them. Won't hurt anything if the collection is empty.
			 */
			for (String l : s.phoneNumbers) {
				emitTagWithRequiredValue(1, "PHON", l);
			}

			emitTagIfValueNotNull(1, "RFN", s.regFileNumber);
			emitTagIfValueNotNull(1, "RIN", s.recIdNumber);
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
	private void emitTag(int level, String tag) {
		pw.println(level + " " + tag);
	}

	/**
	 * Write a line with a tag, with no value following the tag
	 * 
	 * @param level
	 *            the level within the file hierarchy
	 * @param xref
	 *            the xref of the item being written, if any
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
	private void emitTagIfValueNotNull(int level, String tag, Object value) {
		emitTagIfValueNotNull(level, null, tag, value);
	}

	/**
	 * Write a line if the value is non-null
	 * 
	 * @param level
	 *            the level within the file hierarchy
	 * @param xref
	 *            the xref for the item, if any
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
	 * @param tag
	 *            the tag for the line of the file
	 * @param value
	 *            the value to write to the right of the tag
	 * @throws GedcomWriterException
	 *             if the value is null or blank (which never happens, because
	 *             we check for it)
	 */
	private void emitTagWithOptionalValue(int level, String tag, String value)
	        throws GedcomWriterException {

		pw.print(level + " " + tag);
		if (value != null) {
			pw.print(" " + value);
		}
		pw.println();
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
	 * @throws GedcomWriterException
	 *             if the value is null or blank
	 */
	private void emitTagWithRequiredValue(int level, String tag, String value)
	        throws GedcomWriterException {
		emitTagWithRequiredValue(level, null, tag, value);
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
	 */
	private void emitTrailer() {
		pw.println("0 TRLR");
	}
}
