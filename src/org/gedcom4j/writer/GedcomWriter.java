/*
 * Copyright (c) 2009-2013 Matthew R. Harrah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.gedcom4j.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.io.GedcomFileWriter;
import org.gedcom4j.model.*;
import org.gedcom4j.validate.GedcomValidationFinding;
import org.gedcom4j.validate.GedcomValidator;
import org.gedcom4j.validate.Severity;

/**
 * <p>
 * A class for writing the gedcom structure out as a GEDCOM 5.5 compliant file.
 * </p>
 * <p>
 * General usage is as follows:
 * <ul>
 * Instantiate a <code>GedcomWriter</code>, passing the {@link Gedcom} to be written as a parameter to the constructor.
 * </ul>
 * <ul>
 * Call one of the variants of the <code>write</code> method to write the data
 * </ul>
 * <ul>
 * Optionally check the contents of the {@link #validationFindings} collection to see if there was anything problematic
 * found in your data.
 * </ul>
 * </p>
 * 
 * <h3>Validation</h3>
 * <p>
 * By default, this class automatically validates your data prior to writing it by using a
 * {@link org.gedcom4j.validate.GedcomValidator}. This is to prevent writing data that does not conform to the spec.
 * </p>
 * 
 * <p>
 * If validation finds any errors that are of severity ERROR, the writer will throw an Exception (usually
 * {@link GedcomWriterVersionDataMismatchException} or {@link GedcomWriterException}). If this occurs, check the
 * {@link #validationFindings} collection to determine what the problem was.
 * </p>
 * 
 * <p>
 * Although validation is automatically performed, autorepair is turned off by default (see
 * {@link org.gedcom4j.validate.GedcomValidator#autorepair})...this way your data is not altered. Validation can be
 * suppressed if you want by setting {@link #validationSuppressed} to true, but this is not recommended. You can also
 * force autorepair on if you want.
 * </p>
 * 
 * @author frizbog1
 */
public class GedcomWriter {

    /**
     * The maximum length of lines to be written out before splitting
     */
    private static final int MAX_LINE_LENGTH = 128;

    /**
     * The Gedcom data to write
     */
    private final Gedcom gedcom;

    /**
     * Are we suppressing the call to the validator? Deliberately package-private so unit tests can fiddle with it to
     * make testing easy.
     */
    boolean validationSuppressed = false;

    /**
     * The lines of the GEDCOM transmission, which will be written using a {@link GedcomFileWriter}. Deliberately
     * package-private so tests can access it but others can't alter it.
     */
    List<String> lines = new ArrayList<String>();

    /**
     * A list of things found during validation of the gedcom data prior to writing it. If the data cannot be written
     * due to an exception caused by failure to validate, this collection will describe the issues encountered.
     */
    public List<GedcomValidationFinding> validationFindings;

    /**
     * Whether or not to use autorepair in the validation step
     */
    public boolean autorepair = false;

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
     * Write the {@link Gedcom} data as a GEDCOM 5.5 file. Automatically fills in the value for the FILE tag in the HEAD
     * structure.
     * 
     * @param file
     *            the {@link File} to write to
     * @throws IOException
     *             if there's a problem writing the data
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    public void write(File file) throws IOException, GedcomWriterException {
        // Automatically replace the contents of the filename in the header
        gedcom.header.fileName = new StringWithCustomTags(file.getName());

        OutputStream o = new FileOutputStream(file);
        try {
            write(o);
            o.flush();
        } finally {
            o.close();
        }
    }

    /**
     * Write the {@link Gedcom} data in GEDCOM 5.5 format to an output stream. If the data is unicode, the data will be
     * written in little-endian format.
     * 
     * @param out
     *            the output stream we're writing to
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    public void write(OutputStream out) throws GedcomWriterException {
        write(out, true);
    }

    /**
     * Write the {@link Gedcom} data in GEDCOM 5.5 format to an output stream
     * 
     * @param out
     *            the output stream we're writing to
     * @param littleEndianForUnicode
     *            if writing unicode, should the byte-order be little-endian?
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written; or if the data fails validation with one or more
     *             finding of severity ERROR (and validation is not suppressed - see
     *             {@link GedcomWriter#validationSuppressed})
     */
    public void write(OutputStream out, boolean littleEndianForUnicode) throws GedcomWriterException {
        if (!validationSuppressed) {
            GedcomValidator gv = new GedcomValidator(gedcom);
            gv.autorepair = autorepair;
            gv.validate();
            validationFindings = gv.findings;
            int numErrorFindings = 0;
            for (GedcomValidationFinding f : validationFindings) {
                if (f.severity == Severity.ERROR) {
                    numErrorFindings++;
                }
            }
            if (numErrorFindings > 0) {
                throw new GedcomWriterException("Cannot write file - " + numErrorFindings
                        + " error(s) found during validation.  Review the validation findings to determine root cause.");
            }
        }
        checkVersionCompatibility();
        emitHeader();
        emitSubmissionRecord();
        emitRecords();
        emitTrailer();
        emitCustomTags(gedcom.customTags);
        try {
            GedcomFileWriter gfw = new GedcomFileWriter(lines);
            gfw.setLittleEndianForUnicode(littleEndianForUnicode);
            gfw.write(out);
        } catch (IOException e) {
            throw new GedcomWriterException("Unable to write file", e);
        }
    }

    /**
     * Write the {@link Gedcom} data as a GEDCOM 5.5 file, with the supplied file name
     * 
     * @param filename
     *            the name of the file to write
     * @throws IOException
     *             if there's a problem writing the data
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    public void write(String filename) throws IOException, GedcomWriterException {
        File f = new File(filename);
        write(f);
    }

    /**
     * Checks that the gedcom version specified is compatible with the data in the model. Not a perfect exhaustive
     * check.
     * 
     * @throws GedcomWriterException
     *             if data is detected that is incompatible with the selected version
     */
    private void checkVersionCompatibility() throws GedcomWriterException {

        if (gedcom.header.gedcomVersion == null) {
            // If there's not one specified, set up a default one that specifies
            // 5.5.1
            gedcom.header.gedcomVersion = new GedcomVersion();
        }
        if (SupportedVersion.V5_5.equals(gedcom.header.gedcomVersion.versionNumber)) {
            checkVersionCompatibility55();
        } else {
            checkVersionCompatibility551();
        }

    }

    /**
     * Check that the data is compatible with 5.5 style Gedcom files
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if a data point is detected that is incompatible with the 5.5 standard
     */
    private void checkVersionCompatibility55() throws GedcomWriterVersionDataMismatchException {
        // Now that we know if we're working with a 5.5.1 file or not, let's
        // check some data points
        if (gedcom.header.copyrightData.size() > 1) {
            throw new GedcomWriterVersionDataMismatchException(
                    "Gedcom version is 5.5, but has multi-line copyright data in header");
        }
        if (gedcom.header.characterSet != null && gedcom.header.characterSet.characterSetName != null
                && "UTF-8".equals(gedcom.header.characterSet.characterSetName.value)) {
            throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but data is encoded using UTF-8");
        }
        if (gedcom.header.sourceSystem != null && gedcom.header.sourceSystem.corporation != null) {
            Corporation c = gedcom.header.sourceSystem.corporation;
            if (!c.wwwUrls.isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException(
                        "Gedcom version is 5.5, but source system corporation has www urls");
            }
            if (!c.faxNumbers.isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException(
                        "Gedcom version is 5.5, but source system corporation has fax numbers");
            }
            if (!c.emails.isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException(
                        "Gedcom version is 5.5, but source system corporation has emails");
            }
        }
        for (Individual i : gedcom.individuals.values()) {
            if (!i.wwwUrls.isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual " + i.xref
                        + " has www urls");
            }
            if (!i.faxNumbers.isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual " + i.xref
                        + " has fax numbers");
            }
            if (!i.emails.isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual " + i.xref
                        + " has emails");
            }
            for (Event e : i.events) {
                if (!e.wwwUrls.isEmpty()) {
                    throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual "
                            + i.xref + " has www urls on an event");
                }
                if (!e.faxNumbers.isEmpty()) {
                    throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual "
                            + i.xref + " has fax numbers on an event");
                }
                if (!e.emails.isEmpty()) {
                    throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual "
                            + i.xref + " has emails on an event");
                }
            }
            for (IndividualAttribute a : i.attributes) {
                if (IndividualAttributeType.FACT.equals(a.type)) {
                    throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual "
                            + i.xref + " has a FACT attribute");
                }
            }
            for (FamilyChild fc : i.familiesWhereChild) {
                if (fc.status != null) {
                    throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual "
                            + i.xref + " is in a family with a status specified (a Gedcom 5.5.1 only feature)");
                }
            }
        }
        for (Submitter s : gedcom.submitters.values()) {
            if (!s.wwwUrls.isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Submitter " + s.xref
                        + " has www urls");
            }
            if (!s.faxNumbers.isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Submitter " + s.xref
                        + " has fax numbers");
            }
            if (!s.emails.isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Submitter " + s.xref
                        + " has emails");
            }
        }
        for (Repository r : gedcom.repositories.values()) {
            if (!r.wwwUrls.isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Repository " + r.xref
                        + " has www urls");
            }
            if (!r.faxNumbers.isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Repository " + r.xref
                        + " has fax numbers");
            }
            if (!r.emails.isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Repository " + r.xref
                        + " has emails");
            }
        }
    }

    /**
     * Check that the data is compatible with 5.5.1 style Gedcom files
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if a data point is detected that is incompatible with the 5.5.1 standard
     * 
     */
    private void checkVersionCompatibility551() throws GedcomWriterVersionDataMismatchException {
        for (Multimedia m : gedcom.multimedia.values()) {
            if (!m.blob.isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5.1, but multimedia item "
                        + m.xref + " contains BLOB data which is unsupported in 5.5.1");
            }
        }
    }

    /**
     * Write an address
     * 
     * @param level
     *            the level at which we are writing
     * @param address
     *            the address
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
        emitCustomTags(address.customTags);
    }

    /**
     * Write a line out to the print writer, splitting if needed with CONC lines
     * 
     * @param level
     *            the level at which we are recording
     * @param line
     *            the line to be written
     */
    private void emitAndSplit(int level, String line) {
        if (line.length() <= MAX_LINE_LENGTH) {
            lines.add(line);
        } else {
            // First part
            lines.add(line.substring(0, MAX_LINE_LENGTH));
            // Now a series of as many CONC lines as needed
            String remainder = line.substring(MAX_LINE_LENGTH);
            while (remainder.length() > 0) {
                if (remainder.length() > MAX_LINE_LENGTH) {
                    lines.add(level + 1 + " CONC " + remainder.substring(0, MAX_LINE_LENGTH));
                    remainder = remainder.substring(MAX_LINE_LENGTH);
                } else {
                    lines.add(level + 1 + " CONC " + remainder);
                    remainder = "";
                }
            }
        }
    }

    /**
     * Emit the person-to-person associations an individual was in - see ASSOCIATION_STRUCTURE in the GEDCOM spec.
     * 
     * @param level
     *            the level at which to start recording
     * @param associations
     *            the list of associations
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    private void emitAssociationStructures(int level, List<Association> associations) throws GedcomWriterException {
        for (Association a : associations) {
            emitTagWithRequiredValue(level, "ASSO", a.associatedEntityXref);
            emitTagWithRequiredValue(level + 1, "TYPE", a.associatedEntityType);
            emitTagWithRequiredValue(level + 1, "RELA", a.relationship);
            emitNotes(level + 1, a.notes);
            emitSourceCitations(level + 1, a.citations);
            emitCustomTags(a.customTags);
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
    private void emitChangeDate(int level, ChangeDate cd) throws GedcomWriterException {
        if (cd != null) {
            emitTag(level, "CHAN");
            emitTagWithRequiredValue(level + 1, "DATE", cd.date);
            emitTagIfValueNotNull(level + 2, "TIME", cd.time);
            emitNotes(level + 1, cd.notes);
            emitCustomTags(cd.customTags);
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
    private void emitChildToFamilyLinks(int level, Individual i) throws GedcomWriterException {
        for (FamilyChild familyChild : i.familiesWhereChild) {
            if (familyChild == null) {
                throw new GedcomWriterException("Family to which " + i + " was a child was null");
            }
            if (familyChild.family == null) {
                throw new GedcomWriterException("Family to which " + i + " was a child had a null family reference");
            }
            emitTagWithRequiredValue(level, "FAMC", familyChild.family.xref);
            emitTagIfValueNotNull(level + 1, "PEDI", familyChild.pedigree);
            emitTagIfValueNotNull(level + 1, "STAT", familyChild.status);
            emitNotes(level + 1, familyChild.notes);
            emitCustomTags(i.customTags);
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
     *             when the data is malformed and cannot be written
     */
    private void emitCitationWithoutSource(int level, AbstractCitation c) throws GedcomWriterException {
        CitationWithoutSource cws = (CitationWithoutSource) c;
        emitLinesOfText(level, "SOUR", cws.description);
        for (List<String> linesOfText : cws.textFromSource) {
            emitLinesOfText(level + 1, "TEXT", linesOfText);
        }
        emitNotes(level + 1, cws.notes);
        emitCustomTags(cws.customTags);
    }

    /**
     * Emit a citation with source
     * 
     * @param level
     *            the level in the hierarchy at which we are emitting data
     * @param cws
     *            the citation with source
     * @throws GedcomWriterException
     *             when the data is malformed and cannot be written
     */
    private void emitCitationWithSource(int level, CitationWithSource cws) throws GedcomWriterException {
        Source source = cws.source;
        if (source == null || source.xref == null || source.xref.length() == 0) {
            throw new GedcomWriterException("Citation with source must have a source record with an xref/id");
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
        emitCustomTags(cws.customTags);
    }

    /**
     * Emit the custom tags
     * 
     * @param customTags
     *            the custom tags
     */
    private void emitCustomTags(List<StringTree> customTags) {
        for (StringTree st : customTags) {
            StringBuilder line = new StringBuilder(Integer.toString(st.level));
            line.append(" ");
            if (st.id != null && st.id.trim().length() > 0) {
                line.append(st.id).append(" ");
            }
            line.append(st.tag);
            if (st.value != null && st.value.trim().length() > 0) {
                line.append(" ").append(st.value);
            }
            emitCustomTags(st.children);
        }
    }

    /**
     * Emit a list of email addresses. New for GEDCOM 5.5.1
     * 
     * @param l
     *            the level we are writing at
     * @param emails
     *            the list of email addresses
     * @throws GedcomWriterException
     *             if the data cannot be written
     */
    private void emitEmails(int l, List<StringWithCustomTags> emails) throws GedcomWriterException {
        for (StringWithCustomTags e : emails) {
            emitTagWithRequiredValue(l, "EMAIL", e);
        }
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
    private void emitEventDetail(int level, Event e) throws GedcomWriterException {
        emitTagIfValueNotNull(level, "TYPE", e.subType);
        emitTagIfValueNotNull(level, "DATE", e.date);
        if (e.place != null) {
            Place p = e.place;
            emitPlace(level, p);
        }
        emitAddress(level, e.address);
        emitTagIfValueNotNull(level, "AGE", e.age);
        emitTagIfValueNotNull(level, "AGNC", e.respAgency);
        emitTagIfValueNotNull(level, "CAUS", e.cause);
        emitTagIfValueNotNull(level, "RELI", e.religiousAffiliation);
        emitTagIfValueNotNull(level, "RESN", e.restrictionNotice);
        emitSourceCitations(level, e.citations);
        emitMultimediaLinks(level, e.multimedia);
        emitNotes(level, e.notes);
        emitCustomTags(e.customTags);
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
            emitTagIfValueNotNull(1, "RESN", f.restrictionNotice);
            emitSourceCitations(1, f.citations);
            emitMultimediaLinks(1, f.multimedia);
            emitNotes(1, f.notes);
            for (UserReference u : f.userReferences) {
                emitTagWithRequiredValue(1, "REFN", u.referenceNum);
                emitTagIfValueNotNull(2, "TYPE", u.type);
            }
            emitTagIfValueNotNull(1, "RIN", f.automatedRecordId);
            emitChangeDate(1, f.changeDate);
            emitCustomTags(f.customTags);
        }
    }

    /**
     * Emit a family event structure (see FAMILY_EVENT_STRUCTURE in the GEDCOM spec)
     * 
     * @param level
     *            the level we're writing at
     * @param e
     *            the event
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    private void emitFamilyEventStructure(int level, FamilyEvent e) throws GedcomWriterException {
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
        emitCustomTags(e.customTags);
    }

    /**
     * Emit a list of Fax numbers. New for GEDCOM 5.5.1
     * 
     * @param l
     *            the level to write at
     * @param faxNumbers
     *            the list of fax numbers to write
     * @throws GedcomWriterException
     *             if the data cannot be written
     */
    private void emitFaxNumbers(int l, List<StringWithCustomTags> faxNumbers) throws GedcomWriterException {
        for (StringWithCustomTags f : faxNumbers) {
            emitTagWithRequiredValue(l, "FAX", f);
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
        lines.add("0 HEAD");
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
        emitLinesOfText(1, "COPR", header.copyrightData);
        emitTag(1, "GEDC");
        emitTagWithRequiredValue(2, "VERS", header.gedcomVersion.versionNumber.toString());
        emitTagWithRequiredValue(2, "FORM", header.gedcomVersion.gedcomForm);
        emitTagWithRequiredValue(1, "CHAR", header.characterSet.characterSetName);
        emitTagIfValueNotNull(2, "VERS", header.characterSet.versionNum);
        emitTagIfValueNotNull(1, "LANG", header.language);
        if (header.placeHierarchy != null && header.placeHierarchy.value != null
                && header.placeHierarchy.value.length() > 0) {
            emitTag(1, "PLAC");
            emitTagWithRequiredValue(2, "FORM", header.placeHierarchy);
        }
        emitNoteLines(1, null, header.notes);
        emitCustomTags(header.customTags);
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
    private void emitIndividualAttributes(int level, List<IndividualAttribute> attributes) throws GedcomWriterException {
        for (IndividualAttribute a : attributes) {
            emitTagWithOptionalValueAndCustomSubtags(level, a.type.tag, a.description);
            emitEventDetail(level + 1, a);
            emitAddress(level + 1, a.address);
            emitPhoneNumbers(level + 1, a.phoneNumbers);
            emitWwwUrls(level + 1, a.wwwUrls);
            emitFaxNumbers(level + 1, a.faxNumbers);
            emitEmails(level + 1, a.emails);
            emitCustomTags(a.customTags);
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
    private void emitIndividualEvents(int level, List<IndividualEvent> events) throws GedcomWriterException {
        for (IndividualEvent e : events) {
            emitTagWithOptionalValue(level, e.type.tag, e.yNull);
            emitEventDetail(level + 1, e);
            if (e.type == IndividualEventType.BIRTH || e.type == IndividualEventType.CHRISTENING) {
                if (e.family != null && e.family.family != null && e.family.family.xref != null) {
                    emitTagWithRequiredValue(level + 1, "FAMC", e.family.family.xref);
                }
            } else if (e.type == IndividualEventType.ADOPTION) {
                if (e.family != null && e.family.family != null && e.family.family.xref != null) {
                    emitTagWithRequiredValue(level + 1, "FAMC", e.family.family.xref);
                    emitTagIfValueNotNull(level + 2, "ADOP", e.family.adoptedBy);
                }
            }
            emitCustomTags(e.customTags);
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
            for (StringWithCustomTags s : i.aliases) {
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
            emitCustomTags(i.customTags);
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
    private void emitLdsFamilyOrdinance(int level, LdsSpouseSealing sealings) throws GedcomWriterException {
        emitTag(level, "SLGS");
        emitTagIfValueNotNull(level + 1, "STAT", sealings.status);
        emitTagIfValueNotNull(level + 1, "DATE", sealings.date);
        emitTagIfValueNotNull(level + 1, "TEMP", sealings.temple);
        emitTagIfValueNotNull(level + 1, "PLAC", sealings.place);
        emitSourceCitations(level + 1, sealings.citations);
        emitNotes(level + 1, sealings.notes);
        emitCustomTags(sealings.customTags);
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
    private void emitLdsIndividualOrdinances(int level, List<LdsIndividualOrdinance> ldsIndividualOrdinances)
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
                emitTagWithRequiredValue(level + 1, "FAMC", o.familyWhereChild.family.xref);
            }
            emitSourceCitations(level + 1, o.citations);
            emitNotes(level + 1, o.notes);
            emitCustomTags(o.customTags);
        }
    }

    /**
     * Convenience method for emitting lines of text when there is no xref, so you don't have to pass null all the time
     * 
     * @param level
     *            the level we are starting at. Continuation lines will be one level deeper than this value
     * @param startingTag
     *            the tag to use for the first line of the text. All subsequent lines will be "CONT" lines.
     * @param linesOfText
     *            the lines of text
     */
    private void emitLinesOfText(int level, String startingTag, List<String> linesOfText) {
        emitLinesOfText(level, null, startingTag, linesOfText);
    }

    /**
     * Emit a multi-line text value.
     * 
     * @param level
     *            the level we are starting at. Continuation lines will be one level deeper than this value
     * @param startingTag
     *            the tag to use for the first line of the text. All subsequent lines will be "CONT" lines.
     * @param xref
     *            the xref of the item with lines of text
     * @param linesOfText
     *            the lines of text
     */
    private void emitLinesOfText(int level, String xref, String startingTag, List<String> linesOfText) {
        int lineNum = 0;
        for (String l : linesOfText) {
            StringBuilder line = new StringBuilder();
            if (lineNum == 0) {
                line.append(level).append(" ");
                if (xref != null && xref.length() > 0) {
                    line.append(xref).append(" ");
                }
                line.append(startingTag).append(" ").append(l);
            } else {
                line.append(level + 1).append(" ");
                if (xref != null && xref.length() > 0) {
                    line.append(xref).append(" ");
                }
                line.append("CONT ").append(l);
            }
            lineNum++;
            emitAndSplit(level, line.toString());
        }
    }

    /**
     * Write out all the embedded multimedia objects in GEDCOM 5.5 format
     * 
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    private void emitMultimedia55() throws GedcomWriterException {
        for (Multimedia m : gedcom.multimedia.values()) {
            emitTag(0, m.xref, "OBJE");
            emitTagWithRequiredValue(1, "FORM", m.embeddedMediaFormat);
            emitTagIfValueNotNull(1, "TITL", m.embeddedTitle);
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
            if (!m.fileReferences.isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException(
                        "GEDCOM version is 5.5, but found file references in multimedia object " + m.xref
                                + " which are not allowed until GEDCOM 5.5.1");
            }
            emitCustomTags(m.customTags);
        }
    }

    /**
     * Write out all the embedded multimedia objects in GEDCOM 5.5.1 format
     * 
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    private void emitMultimedia551() throws GedcomWriterException {
        for (Multimedia m : gedcom.multimedia.values()) {
            emitTag(0, m.xref, "OBJE");
            for (FileReference fr : m.fileReferences) {
                emitTagWithRequiredValue(1, "FILE", fr.referenceToFile);
                emitTagWithRequiredValue(2, "FORM", fr.format);
                emitTagIfValueNotNull(3, "TYPE", fr.mediaType);
                emitTagIfValueNotNull(2, "TITL", fr.title);
            }
            for (UserReference u : m.userReferences) {
                emitTagWithRequiredValue(1, "REFN", u.referenceNum);
                emitTagIfValueNotNull(2, "TYPE", u.type);
            }
            emitTagIfValueNotNull(1, "RIN", m.recIdNumber);
            emitNotes(1, m.notes);
            emitChangeDate(1, m.changeDate);
            emitCustomTags(m.customTags);
            if (!m.blob.isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException(
                        "GEDCOM version is 5.5.1, but BLOB data on multimedia item " + m.xref
                                + " was found.  This is only allowed in GEDCOM 5.5");
            }
            if (m.continuedObject != null) {
                throw new GedcomWriterVersionDataMismatchException(
                        "GEDCOM version is 5.5.1, but BLOB continuation data on multimedia item " + m.xref
                                + " was found.  This is only allowed in GEDCOM 5.5");
            }
            if (m.embeddedMediaFormat != null) {
                throw new GedcomWriterVersionDataMismatchException(
                        "GEDCOM version is 5.5.1, but format on multimedia item " + m.xref
                                + " was found.  This is only allowed in GEDCOM 5.5");
            }
            if (m.embeddedTitle != null) {
                throw new GedcomWriterVersionDataMismatchException(
                        "GEDCOM version is 5.5.1, but title on multimedia item " + m.xref
                                + " was found.  This is only allowed in GEDCOM 5.5");
            }
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
    private void emitMultimediaLinks(int level, List<Multimedia> multimedia) throws GedcomWriterException {
        if (multimedia == null) {
            return;
        }
        for (Multimedia m : multimedia) {
            if (m.xref == null) {
                // Link to referenced form
                if (g55()) {
                    // GEDCOM 5.5 format
                    emitTag(level, "OBJE");
                    if (m.fileReferences.size() > 1) {
                        throw new GedcomWriterVersionDataMismatchException(
                                "GEDCOM version is 5.5, but multimedia link references "
                                        + "multiple files, which is only allowed in GEDCOM 5.5.1");
                    }
                    if (m.fileReferences.size() == 1) {
                        FileReference fr = m.fileReferences.get(0);
                        if (fr.format != null) {
                            emitTagWithRequiredValue(level + 1, "FORM", fr.format);
                        } else {
                            emitTagWithRequiredValue(level + 1, "FORM", m.embeddedMediaFormat);
                        }
                        emitTagIfValueNotNull(level + 1, "TITL", m.embeddedTitle);
                        emitTagWithRequiredValue(level + 1, "FILE", fr.referenceToFile);
                    } else {
                        emitTagWithRequiredValue(level + 1, "FORM", m.embeddedMediaFormat);
                        emitTagIfValueNotNull(level + 1, "TITL", m.embeddedTitle);
                    }
                    emitNotes(level + 1, m.notes);
                } else {
                    // GEDCOM 5.5.1 format
                    for (FileReference fr : m.fileReferences) {
                        emitTagWithRequiredValue(level + 1, "FILE", fr.referenceToFile);
                        emitTagIfValueNotNull(level + 2, "FORM", fr.format);
                        emitTagIfValueNotNull(level + 3, "MEDI", fr.mediaType);
                        emitTagIfValueNotNull(level + 1, "TITL", fr.title);
                    }
                    if (!m.notes.isEmpty()) {
                        throw new GedcomWriterVersionDataMismatchException(
                                "GEDCOM version is 5.5.1, but multimedia link has notes which are no longer allowed in 5.5");
                    }
                }
            } else {
                // Link to the embedded form
                emitTagWithRequiredValue(level, "OBJE", m.xref);
            }
            emitCustomTags(m.customTags);
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
        emitCustomTags(note.customTags);
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
        emitLinesOfText(level, xref, "NOTE", noteLines);
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
    private void emitNotes(int level, List<Note> notes) throws GedcomWriterException {
        for (Note n : notes) {
            emitNote(level, n);
            emitCustomTags(n.customTags);
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
    private void emitPersonalNames(int level, List<PersonalName> names) throws GedcomWriterException {
        for (PersonalName n : names) {
            emitTagWithOptionalValue(level, "NAME", n.basic);
            emitTagIfValueNotNull(level + 1, "NPFX", n.prefix);
            emitTagIfValueNotNull(level + 1, "GIVN", n.givenName);
            emitTagIfValueNotNull(level + 1, "NICK", n.nickname);
            emitTagIfValueNotNull(level + 1, "SPFX", n.surnamePrefix);
            emitTagIfValueNotNull(level + 1, "SURN", n.surname);
            emitTagIfValueNotNull(level + 1, "NSFX", n.suffix);
            for (PersonalNameVariation pnv : n.romanized) {
                emitPersonalNameVariation(level + 1, "ROMN", pnv);
            }
            for (PersonalNameVariation pnv : n.phonetic) {
                emitPersonalNameVariation(level + 1, "FONE", pnv);
            }
            emitSourceCitations(level + 1, n.citations);
            emitNotes(level + 1, n.notes);
            emitCustomTags(n.customTags);
        }
    }

    /**
     * Emit a personal name variation - either romanized or phonetic
     * 
     * @param level
     *            - the level we are writing at
     * @param variationTag
     *            the tag for the type of variation - should be "ROMN" for romanized, or "FONE" for phonetic
     * @param pnv
     *            - the personal name variation we are writing
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    private void emitPersonalNameVariation(int level, String variationTag, PersonalNameVariation pnv)
            throws GedcomWriterException {
        emitTagWithRequiredValue(level, variationTag, pnv.variation);
        emitTagIfValueNotNull(level + 1, "NPFX", pnv.prefix);
        emitTagIfValueNotNull(level + 1, "GIVN", pnv.givenName);
        emitTagIfValueNotNull(level + 1, "NICK", pnv.nickname);
        emitTagIfValueNotNull(level + 1, "SPFX", pnv.surnamePrefix);
        emitTagIfValueNotNull(level + 1, "SURN", pnv.surname);
        emitTagIfValueNotNull(level + 1, "NSFX", pnv.suffix);
        emitSourceCitations(level + 1, pnv.citations);
        emitNotes(level + 1, pnv.notes);
        emitCustomTags(pnv.customTags);
    }

    /**
     * Write out a list of phone numbers
     * 
     * @param level
     *            the level in the hierarchy we are writing at
     * @param phoneNumbers
     *            a list of phone numbers
     */
    private void emitPhoneNumbers(int level, List<StringWithCustomTags> phoneNumbers) {
        for (StringWithCustomTags ph : phoneNumbers) {
            emitTagIfValueNotNull(level, "PHON", ph);
        }
    }

    /**
     * Emit a place.
     * 
     * @param level
     *            the level at which we are emitting data
     * @param p
     *            the place being emitted
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    private void emitPlace(int level, Place p) throws GedcomWriterException {
        emitTagWithOptionalValue(level, "PLAC", p.placeName);
        emitTagIfValueNotNull(level + 1, "FORM", p.placeFormat);
        emitSourceCitations(level + 1, p.citations);
        emitNotes(level + 1, p.notes);
        for (NameVariation nv : p.romanized) {
            if (g55()) {
                throw new GedcomWriterVersionDataMismatchException(
                        "GEDCOM version is 5.5, but romanized variation was specified on place " + p.placeName
                                + ", which is only allowed in GEDCOM 5.5.1");
            }
            emitTagWithRequiredValue(level + 1, "ROMN", nv.variation);
            emitTagIfValueNotNull(level + 2, "TYPE", nv.variationType);
        }
        for (NameVariation nv : p.phonetic) {
            if (g55()) {
                throw new GedcomWriterVersionDataMismatchException(
                        "GEDCOM version is 5.5, but phonetic variation was specified on place " + p.placeName
                                + ", which is only allowed in GEDCOM 5.5.1");
            }
            emitTagWithRequiredValue(level + 1, "FONE", nv.variation);
            emitTagIfValueNotNull(level + 2, "TYPE", nv.variationType);
        }
        if (p.latitude != null || p.longitude != null) {
            emitTag(level + 1, "MAP");
            emitTagWithRequiredValue(level + 2, "LAT", p.latitude);
            emitTagWithRequiredValue(level + 2, "LONG", p.longitude);
            if (g55()) {
                throw new GedcomWriterVersionDataMismatchException(
                        "GEDCOM version is 5.5, but map coordinates were specified on place " + p.placeName
                                + ", which is only allowed in GEDCOM 5.5.1");
            }
        }
        emitCustomTags(p.customTags);
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
        if (g55()) {
            emitMultimedia55();
        } else {
            emitMultimedia551();
        }
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
            emitPhoneNumbers(1, r.phoneNumbers);
            emitWwwUrls(1, r.wwwUrls);
            emitFaxNumbers(1, r.faxNumbers);
            emitEmails(1, r.emails);
            emitChangeDate(1, r.changeDate);
            emitCustomTags(r.customTags);
        }
    }

    /**
     * Write out a repository citation (see SOURCE_REPOSITORY_CITATION in the gedcom spec)
     * 
     * @param level
     *            the level we're writing at
     * @param repositoryCitation
     *            the repository citation to write out
     * @throws GedcomWriterException
     *             if the repository citation passed in has a null repository reference
     */
    private void emitRepositoryCitation(int level, RepositoryCitation repositoryCitation) throws GedcomWriterException {
        if (repositoryCitation != null) {
            if (repositoryCitation.repositoryXref == null) {
                throw new GedcomWriterException("Repository Citation has null repository reference");
            }
            emitTagWithRequiredValue(level, "REPO", repositoryCitation.repositoryXref);
            emitNotes(level + 1, repositoryCitation.notes);
            for (SourceCallNumber scn : repositoryCitation.callNumbers) {
                emitTagWithRequiredValue(level + 1, "CALN", scn.callNumber);
                emitTagIfValueNotNull(level + 2, "MEDI", scn.mediaType);
            }
            emitCustomTags(repositoryCitation.customTags);
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
    private void emitSourceCitations(int level, List<AbstractCitation> citations) throws GedcomWriterException {
        if (citations == null) {
            return;
        }
        for (AbstractCitation c : citations) {
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
            emitChangeDate(1, s.changeDate);
            emitCustomTags(s.customTags);
        }
    }

    /**
     * Write a source system structure (see APPROVED_SYSTEM_ID in the GEDCOM spec)
     * 
     * @param sourceSystem
     *            the source system
     * @throws GedcomWriterException
     *             if data is malformed and cannot be written
     */
    private void emitSourceSystem(SourceSystem sourceSystem) throws GedcomWriterException {
        if (sourceSystem == null) {
            return;
        }
        emitTagWithRequiredValue(1, "SOUR", sourceSystem.systemId);
        emitTagIfValueNotNull(2, "VERS", sourceSystem.versionNum);
        emitTagIfValueNotNull(2, "NAME", sourceSystem.productName);
        Corporation corporation = sourceSystem.corporation;
        if (corporation != null) {
            emitTagWithOptionalValue(2, "CORP", corporation.businessName);
            emitAddress(3, corporation.address);
            emitPhoneNumbers(3, corporation.phoneNumbers);
            emitFaxNumbers(3, corporation.faxNumbers);
            emitWwwUrls(3, corporation.wwwUrls);
            emitEmails(3, corporation.emails);
        }
        HeaderSourceData sourceData = sourceSystem.sourceData;
        if (sourceData != null) {
            emitTagIfValueNotNull(2, "DATA", sourceData.name);
            emitTagIfValueNotNull(3, "DATE", sourceData.publishDate);
            emitTagIfValueNotNull(3, "COPR", sourceData.copyright);
        }
        emitCustomTags(sourceSystem.customTags);
    }

    /**
     * Emit links to all the families to which the supplied individual was a spouse
     * 
     * @param level
     *            the level in the hierarchy at which we are emitting
     * @param i
     *            the individual we are dealing with
     * @throws GedcomWriterException
     *             if data is malformed and cannot be written
     */
    private void emitSpouseInFamilyLinks(int level, Individual i) throws GedcomWriterException {
        for (FamilySpouse familySpouse : i.familiesWhereSpouse) {
            if (familySpouse == null) {
                throw new GedcomWriterException("Family in which " + i + " was a spouse was null");
            }
            if (familySpouse.family == null) {
                throw new GedcomWriterException("Family in which " + i + " was a spouse had a null family reference");
            }
            emitTagWithRequiredValue(level, "FAMS", familySpouse.family.xref);
            emitNotes(level + 1, familySpouse.notes);
            emitCustomTags(familySpouse.customTags);
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
        emitCustomTags(s.customTags);
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
            emitTagWithOptionalValueAndCustomSubtags(1, "NAME", s.name);
            emitAddress(1, s.address);
            emitMultimediaLinks(1, s.multimedia);
            for (StringWithCustomTags l : s.languagePref) {
                emitTagWithRequiredValue(1, "LANG", l);
            }
            emitPhoneNumbers(1, s.phoneNumbers);
            emitWwwUrls(1, s.wwwUrls);
            emitFaxNumbers(1, s.faxNumbers);
            emitEmails(1, s.emails);
            emitTagIfValueNotNull(1, "RFN", s.regFileNumber);
            emitTagIfValueNotNull(1, "RIN", s.recIdNumber);
            emitChangeDate(1, s.changeDate);
            emitCustomTags(s.customTags);
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
        lines.add(level + " " + tag);
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
        StringBuilder line = new StringBuilder(Integer.toString(level));
        if (xref != null && xref.length() > 0) {
            line.append(" ").append(xref);
        }
        line.append(" ").append(tag);
        lines.add(line.toString());
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
    private void emitTagIfValueNotNull(int level, String xref, String tag, Object value) {
        if (value != null) {
            StringBuilder line = new StringBuilder();
            line.append(level);
            if (xref != null && xref.length() > 0) {
                line.append(" ").append(xref);
            }
            line.append(" ").append(tag).append(" ").append(value);
            emitAndSplit(level, line.toString());
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
     *             if the value is null or blank (which never happens, because we check for it)
     */
    private void emitTagWithOptionalValue(int level, String tag, String value) throws GedcomWriterException {
        StringBuilder line = new StringBuilder(level + " " + tag);
        if (value != null) {
            line.append(" ").append(value);
        }
        lines.add(line.toString());
    }

    /**
     * Write a line and tag, with an optional value for the tag
     * 
     * @param level
     *            the level within the file hierarchy
     * @param tag
     *            the tag for the line of the file
     * @param valueToRightOfTag
     *            the value to write to the right of the tag
     * @throws GedcomWriterException
     *             if the value is null or blank (which never happens, because we check for it)
     */
    private void emitTagWithOptionalValueAndCustomSubtags(int level, String tag, StringWithCustomTags valueToRightOfTag)
            throws GedcomWriterException {
        StringBuilder line = new StringBuilder(level + " " + tag);
        if (valueToRightOfTag != null && valueToRightOfTag.value != null) {
            line.append(" ").append(valueToRightOfTag);
        }
        lines.add(line.toString());
        if (valueToRightOfTag != null) {
            emitCustomTags(valueToRightOfTag.customTags);
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
     *             if the value is null or blank
     */
    private void emitTagWithRequiredValue(int level, String tag, String value) throws GedcomWriterException {
        emitTagWithRequiredValue(level, null, tag, new StringWithCustomTags(value));
    }

    /**
     * Write a line and tag, with an optional value for the tag
     * 
     * @param level
     *            the level within the file hierarchy
     * @param tag
     *            the tag for the line of the file
     * @param e
     *            the value to write to the right of the tag
     * @param xref
     *            the xref of the item being emitted
     * @throws GedcomWriterException
     *             if the value is null or blank
     */
    private void emitTagWithRequiredValue(int level, String xref, String tag, StringWithCustomTags e)
            throws GedcomWriterException {
        if (e == null || e.value == null || e.value.trim().length() == 0) {
            throw new GedcomWriterException("Required value for tag " + tag + " at level " + level
                    + " was null or blank");
        }
        StringBuilder line = new StringBuilder(Integer.toString(level));
        if (xref != null && xref.length() > 0) {
            line.append(" ").append(xref);
        }
        line.append(" ").append(tag).append(" ").append(e);
        lines.add(line.toString());
        emitCustomTags(e.customTags);
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
    private void emitTagWithRequiredValue(int level, String tag, StringWithCustomTags value)
            throws GedcomWriterException {
        emitTagWithRequiredValue(level, null, tag, value);
    }

    /**
     * Write out the trailer record
     */
    private void emitTrailer() {
        lines.add("0 TRLR");
    }

    /**
     * Emit a list of WWW URLs. New for GEDCOM 5.5.1
     * 
     * @param l
     *            the level to write at
     * @param wwwUrls
     *            the list of URLs
     * @throws GedcomWriterException
     *             if the data cannot be written
     */
    private void emitWwwUrls(int l, List<StringWithCustomTags> wwwUrls) throws GedcomWriterException {
        for (StringWithCustomTags w : wwwUrls) {
            emitTagWithRequiredValue(l, "WWW", w);
        }
    }

    /**
     * Returns true if and only if the Gedcom data says it is for the 5.5 standard.
     * 
     * @return true if and only if the Gedcom data says it is for the 5.5 standard.
     */
    private boolean g55() {
        return gedcom != null && gedcom.header != null && gedcom.header.gedcomVersion != null
                && SupportedVersion.V5_5.equals(gedcom.header.gedcomVersion.versionNumber);
    }
}
