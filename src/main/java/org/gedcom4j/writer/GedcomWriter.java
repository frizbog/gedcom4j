/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
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
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.exception.GedcomWriterVersionDataMismatchException;
import org.gedcom4j.exception.WriterCancelledException;
import org.gedcom4j.io.event.FileProgressEvent;
import org.gedcom4j.io.event.FileProgressListener;
import org.gedcom4j.io.writer.GedcomFileWriter;
import org.gedcom4j.model.*;
import org.gedcom4j.validate.GedcomValidationFinding;
import org.gedcom4j.validate.GedcomValidator;
import org.gedcom4j.validate.Severity;
import org.gedcom4j.writer.event.ConstructProgressEvent;
import org.gedcom4j.writer.event.ConstructProgressListener;

/**
 * <p>
 * A class for writing the gedcom structure out as a GEDCOM 5.5 compliant file.
 * </p>
 * <p>
 * General usage is as follows:
 * </p>
 * <ul>
 * <li>Instantiate a <code>GedcomWriter</code>, passing the {@link Gedcom} to be written as a parameter to the
 * constructor.</li>
 * <li>Call one of the variants of the <code>write</code> method to write the data</li>
 * <li>Optionally check the contents of the {@link #validationFindings} collection to see if there was anything
 * problematic found in your data.</li>
 * </ul>
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
     * Whether or not to use autorepair in the validation step
     */
    public boolean autorepair = false;

    /**
     * Whether to use little-endian unicode
     */
    public boolean useLittleEndianForUnicode = true;

    /**
     * A list of things found during validation of the gedcom data prior to writing it. If the data cannot be written
     * due to an exception caused by failure to validate, this collection will describe the issues encountered.
     */
    public List<GedcomValidationFinding> validationFindings;

    /**
     * The lines of the GEDCOM transmission, which will be written using a {@link GedcomFileWriter}. Deliberately
     * package-private so tests can access it but others can't alter it.
     */
    List<String> lines = new ArrayList<String>();

    /**
     * Are we suppressing the call to the validator? Deliberately package-private so unit tests can fiddle with it to
     * make testing easy.
     */
    boolean validationSuppressed = false;

    /**
     * Has this writer been cancelled?
     */
    private boolean cancelled;

    /**
     * Send a notification whenever more than this many lines are constructed
     */
    private int constructionNotificationRate = 500;

    /**
     * Send a notification whenever more than this many lines are written to a file
     */
    private int fileNotificationRate = 500;

    /**
     * The list of observers on string construction
     */
    private final List<WeakReference<ConstructProgressListener>> constructObservers = new CopyOnWriteArrayList<WeakReference<ConstructProgressListener>>();

    /**
     * The list of observers on file operations
     */
    private final List<WeakReference<FileProgressListener>> fileObservers = new CopyOnWriteArrayList<WeakReference<FileProgressListener>>();

    /**
     * The Gedcom data to write
     */
    private final Gedcom gedcom;

    /**
     * The number of lines constructed as last reported to the observers
     */
    private int lastLineCountNotified = 0;

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
     * Cancel construction of the GEDCOM and writing it to a file
     */
    public void cancel() {
        cancelled = true;
    }

    /**
     * Get the construction notification rate - how many lines need to be constructed before getting a notification
     * 
     * @return the construction notification rate - how many lines need to be constructed before getting a notification
     */
    public int getConstructionNotificationRate() {
        return constructionNotificationRate;
    }

    /**
     * Get the number of lines to be written between each file notification
     * 
     * @return the number of lines to be written between each file notification
     */
    public int getFileNotificationRate() {
        return fileNotificationRate;
    }

    /**
     * Has this writer been cancelled?
     * 
     * @return true if this writer has been cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Notify all listeners about the file progress
     * 
     * @param e
     *            the change event to tell the observers
     */
    public void notifyFileObservers(FileProgressEvent e) {
        int i = 0;
        while (i < fileObservers.size()) {
            WeakReference<FileProgressListener> observerRef = fileObservers.get(i);
            if (observerRef == null) {
                fileObservers.remove(observerRef);
            } else {
                observerRef.get().progressNotification(e);
                i++;
            }
        }
    }

    /**
     * Register a observer (listener) to be informed about progress and completion.
     * 
     * @param observer
     *            the observer you want notified
     */
    public void registerConstructObserver(ConstructProgressListener observer) {
        constructObservers.add(new WeakReference<ConstructProgressListener>(observer));
    }

    /**
     * Register a observer (listener) to be informed about progress and completion.
     * 
     * @param observer
     *            the observer you want notified
     */
    public void registerFileObserver(FileProgressListener observer) {
        fileObservers.add(new WeakReference<FileProgressListener>(observer));
    }

    /**
     * Set the construction notification rate - how many lines need to be constructed before getting a notification
     * 
     * @param constructionNotificationRate
     *            the construction notification rate - how many lines need to be constructed before getting a
     *            notification. Must be 1 or greater.
     */
    public void setConstructionNotificationRate(int constructionNotificationRate) {
        if (constructionNotificationRate < 1) {
            throw new IllegalArgumentException("Construction Notification Rate must be at least 1");
        }
        this.constructionNotificationRate = constructionNotificationRate;
    }

    /**
     * Set the number of lines to be written between each file notification
     * 
     * @param fileNotificationRate
     *            the number of lines to be written between each file notification. Must be 1 or greater.
     */
    public void setFileNotificationRate(int fileNotificationRate) {
        if (fileNotificationRate < 1) {
            throw new IllegalArgumentException("File Notification Rate must be at least 1");
        }
        this.fileNotificationRate = fileNotificationRate;
    }

    /**
     * Unregister a observer (listener) to be informed about progress and completion.
     * 
     * @param observer
     *            the observer you want notified
     */
    public void unregisterConstructObserver(ConstructProgressListener observer) {
        int i = 0;
        while (i < constructObservers.size()) {
            WeakReference<ConstructProgressListener> observerRef = constructObservers.get(i);
            if (observerRef == null || observerRef.get() == observer) {
                constructObservers.remove(observerRef);
            } else {
                i++;
            }
        }
        constructObservers.add(new WeakReference<ConstructProgressListener>(observer));
    }

    /**
     * Unregister a observer (listener) to be informed about progress and completion.
     * 
     * @param observer
     *            the observer you want notified
     */
    public void unregisterFileObserver(FileProgressListener observer) {
        int i = 0;
        while (i < fileObservers.size()) {
            WeakReference<FileProgressListener> observerRef = fileObservers.get(i);
            if (observerRef == null || observerRef.get() == observer) {
                fileObservers.remove(observerRef);
            } else {
                i++;
            }
        }
        fileObservers.add(new WeakReference<FileProgressListener>(observer));
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
        gedcom.getHeader().setFileName(new StringWithCustomTags(file.getName()));

        // If the file doesn't exist yet, we have to create it, otherwise a FileNotFoundException will be thrown
        if (!file.exists() && !file.getCanonicalFile().getParentFile().exists() && !file.getCanonicalFile().getParentFile().mkdirs() && !file.createNewFile()) {
            throw new IOException("Unable to create file " + file.getName());
        }
        OutputStream o = new FileOutputStream(file);
        try {
            write(o);
            o.flush();
        } finally {
            o.close();
        }
    }

    /**
     * Write the {@link Gedcom} data in GEDCOM 5.5 format to an output stream
     * 
     * @param out
     *            the output stream we're writing to
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written; or if the data fails validation with one or more
     *             finding of severity ERROR (and validation is not suppressed - see
     *             {@link GedcomWriter#validationSuppressed})
     */
    public void write(OutputStream out) throws GedcomWriterException {
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
        emitCustomTags(1, gedcom.getCustomTags());
        try {
            GedcomFileWriter gfw = new GedcomFileWriter(this, lines);
            gfw.useLittleEndianForUnicode = useLittleEndianForUnicode;
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
     * Split up an array of text lines to when line break characters appear. If any of the original line contains line
     * split characters (newlines, line feeds, carriage returns), split the line up into multiple lines.
     * 
     * @param linesOfText
     *            a single string that may or may not contain line breaks
     * @return a list of Strings that reflect the line breaks in the original string
     */
    List<String> splitLinesOnBreakingCharacters(List<String> linesOfText) {
        List<String> result = new ArrayList<String>();

        for (String s : linesOfText) {
            String[] pieces = s.split("(\r\n|\n\r|\r|\n)");
            for (String piece : pieces) {
                result.add(piece);
            }
        }
        return result;
    }

    /**
     * Checks that the gedcom version specified is compatible with the data in the model. Not a perfect exhaustive
     * check.
     * 
     * @throws GedcomWriterException
     *             if data is detected that is incompatible with the selected version
     */
    private void checkVersionCompatibility() throws GedcomWriterException {

        if (gedcom.getHeader().getGedcomVersion() == null) {
            // If there's not one specified, set up a default one that specifies
            // 5.5.1
            gedcom.getHeader().setGedcomVersion(new GedcomVersion());
        }
        if (SupportedVersion.V5_5.equals(gedcom.getHeader().getGedcomVersion().getVersionNumber())) {
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
        if (gedcom.getHeader().getCopyrightData().size() > 1) {
            throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but has multi-line copyright data in header");
        }
        if (gedcom.getHeader().getCharacterSet() != null && gedcom.getHeader().getCharacterSet().getCharacterSetName() != null && "UTF-8".equals(gedcom
                .getHeader().getCharacterSet().getCharacterSetName().getValue())) {
            throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but data is encoded using UTF-8");
        }
        if (gedcom.getHeader().getSourceSystem() != null && gedcom.getHeader().getSourceSystem().corporation != null) {
            Corporation c = gedcom.getHeader().getSourceSystem().corporation;
            if (!c.getWwwUrls().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but source system corporation has www urls");
            }
            if (!c.getFaxNumbers().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but source system corporation has fax numbers");
            }
            if (!c.getEmails().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but source system corporation has emails");
            }
        }
        for (Individual i : gedcom.getIndividuals().values()) {
            if (!i.getWwwUrls().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual " + i.getXref() + " has www urls");
            }
            if (!i.getFaxNumbers().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual " + i.getXref() + " has fax numbers");
            }
            if (!i.getEmails().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual " + i.getXref() + " has emails");
            }
            for (AbstractEvent e : i.events) {
                if (!e.getWwwUrls().isEmpty()) {
                    throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual " + i.getXref() + " has www urls on an event");
                }
                if (!e.getFaxNumbers().isEmpty()) {
                    throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual " + i.getXref() + " has fax numbers on an event");
                }
                if (!e.getEmails().isEmpty()) {
                    throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual " + i.getXref() + " has emails on an event");
                }
            }
            for (IndividualAttribute a : i.attributes) {
                if (IndividualAttributeType.FACT.equals(a.type)) {
                    throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual " + i.getXref() + " has a FACT attribute");
                }
            }
            for (FamilyChild fc : i.familiesWhereChild) {
                if (fc.getStatus() != null) {
                    throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual " + i.getXref()
                            + " is in a family with a status specified (a Gedcom 5.5.1 only feature)");
                }
            }
        }
        for (Submitter s : gedcom.getSubmitters().values()) {
            if (!s.getWwwUrls().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Submitter " + s.getXref() + " has www urls");
            }
            if (!s.getFaxNumbers().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Submitter " + s.getXref() + " has fax numbers");
            }
            if (!s.getEmails().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Submitter " + s.getXref() + " has emails");
            }
        }
        for (Repository r : gedcom.getRepositories().values()) {
            if (!r.getWwwUrls().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Repository " + r.getXref() + " has www urls");
            }
            if (!r.getFaxNumbers().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Repository " + r.getXref() + " has fax numbers");
            }
            if (!r.getEmails().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Repository " + r.getXref() + " has emails");
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
        for (Multimedia m : gedcom.getMultimedia().values()) {
            if (!m.blob.isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5.1, but multimedia item " + m.getXref()
                        + " contains BLOB data which is unsupported in 5.5.1");
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
        emitLinesOfText(level, "ADDR", address.getLines());
        emitTagIfValueNotNull(level + 1, "ADR1", address.getAddr1());
        emitTagIfValueNotNull(level + 1, "ADR2", address.getAddr2());
        emitTagIfValueNotNull(level + 1, "CITY", address.getCity());
        emitTagIfValueNotNull(level + 1, "STAE", address.getStateProvince());
        emitTagIfValueNotNull(level + 1, "POST", address.getPostalCode());
        emitTagIfValueNotNull(level + 1, "CTRY", address.getCountry());
        emitCustomTags(level + 1, address.getCustomTags());
    }

    /**
     * Write a line out to the print writer, splitting due to length if needed with CONC lines
     * 
     * @param level
     *            the level at which we are recording
     * @param line
     *            the line to be written, which may have line breaking characters (which will result in CONT lines)
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
            emitTagWithRequiredValue(level, "ASSO", a.getAssociatedEntityXref());
            emitTagWithRequiredValue(level + 1, "TYPE", a.getAssociatedEntityType());
            emitTagWithRequiredValue(level + 1, "RELA", a.getRelationship());
            emitNotes(level + 1, a.getNotes());
            emitSourceCitations(level + 1, a.getCitations());
            emitCustomTags(level + 1, a.getCustomTags());
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
            emitTagWithRequiredValue(level + 1, "DATE", cd.getDate());
            emitTagIfValueNotNull(level + 2, "TIME", cd.getTime());
            emitNotes(level + 1, cd.getNotes());
            emitCustomTags(level + 1, cd.getCustomTags());
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
            if (familyChild.getFamily() == null) {
                throw new GedcomWriterException("Family to which " + i + " was a child had a null family reference");
            }
            emitTagWithRequiredValue(level, "FAMC", familyChild.getFamily().getXref());
            emitTagIfValueNotNull(level + 1, "PEDI", familyChild.getPedigree());
            emitTagIfValueNotNull(level + 1, "STAT", familyChild.getStatus());
            emitNotes(level + 1, familyChild.getNotes());
            emitCustomTags(level + 1, i.getCustomTags());
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
        emitLinesOfText(level, "SOUR", cws.getDescription());
        for (List<String> linesOfText : cws.getTextFromSource()) {
            emitLinesOfText(level + 1, "TEXT", linesOfText);
        }
        emitNotes(level + 1, cws.getNotes());
        emitCustomTags(level + 1, cws.getCustomTags());
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
        Source source = cws.getSource();
        if (source == null || source.getXref() == null || source.getXref().length() == 0) {
            throw new GedcomWriterException("Citation with source must have a source record with an xref/id");
        }
        emitTagWithRequiredValue(level, "SOUR", source.getXref());
        emitTagIfValueNotNull(level + 1, "PAGE", cws.getWhereInSource());
        emitTagIfValueNotNull(level + 1, "EVEN", cws.getEventCited());
        emitTagIfValueNotNull(level + 2, "ROLE", cws.getRoleInEvent());
        if (cws.getData() != null && !cws.getData().isEmpty()) {
            emitTag(level + 1, "DATA");
            for (CitationData cd : cws.getData()) {
                emitTagIfValueNotNull(level + 2, "DATE", cd.getEntryDate());
                for (List<String> linesOfText : cd.getSourceText()) {
                    emitLinesOfText(level + 2, "TEXT", linesOfText);
                }
            }
        }
        emitTagIfValueNotNull(level + 1, "QUAY", cws.getCertainty());
        emitMultimediaLinks(level + 1, cws.getMultimedia());
        emitNotes(level + 1, cws.getNotes());
        emitCustomTags(level + 1, cws.getCustomTags());
    }

    /**
     * Emit the custom tags
     * 
     * @param customTags
     *            the custom tags
     * @param level
     *            the level at which the custom tags are to be written
     */
    private void emitCustomTags(int level, List<StringTree> customTags) {
        for (StringTree st : customTags) {
            StringBuilder line = new StringBuilder(Integer.toString(level));
            line.append(" ");
            if (st.id != null && st.id.trim().length() > 0) {
                line.append(st.id).append(" ");
            }
            line.append(st.tag);
            if (st.value != null && st.value.trim().length() > 0) {
                line.append(" ").append(st.value);
            }
            lines.add(line.toString());
            emitCustomTags(level + 1, st.children);
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
    private void emitEventDetail(int level, AbstractEvent e) throws GedcomWriterException {
        emitTagIfValueNotNull(level, "TYPE", e.getSubType());
        emitTagIfValueNotNull(level, "DATE", e.getDate());
        if (e.getPlace() != null) {
            Place p = e.getPlace();
            emitPlace(level, p);
        }
        emitAddress(level, e.getAddress());
        emitTagIfValueNotNull(level, "AGE", e.getAge());
        emitTagIfValueNotNull(level, "AGNC", e.getRespAgency());
        emitTagIfValueNotNull(level, "CAUS", e.getCause());
        emitTagIfValueNotNull(level, "RELI", e.getReligiousAffiliation());
        emitTagIfValueNotNull(level, "RESN", e.getRestrictionNotice());
        emitSourceCitations(level, e.getCitations());
        emitMultimediaLinks(level, e.getMultimedia());
        emitNotes(level, e.getNotes());
        emitCustomTags(level, e.getCustomTags());
    }

    /**
     * Write out all the Families
     * 
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    private void emitFamilies() throws GedcomWriterException {
        for (Family f : gedcom.getFamilies().values()) {
            emitTag(0, f.getXref(), "FAM");
            for (FamilyEvent e : f.events) {
                emitFamilyEventStructure(1, e);
            }
            if (f.husband != null) {
                emitTagWithRequiredValue(1, "HUSB", f.husband.getXref());
            }
            if (f.wife != null) {
                emitTagWithRequiredValue(1, "WIFE", f.wife.getXref());
            }
            for (Individual i : f.children) {
                emitTagWithRequiredValue(1, "CHIL", i.getXref());
            }
            emitTagIfValueNotNull(1, "NCHI", f.numChildren);
            for (Submitter s : f.submitters) {
                emitTagWithRequiredValue(1, "SUBM", s.getXref());
            }
            for (LdsSpouseSealing s : f.ldsSpouseSealings) {
                emitLdsFamilyOrdinance(1, s);
            }
            emitTagIfValueNotNull(1, "RESN", f.restrictionNotice);
            emitSourceCitations(1, f.citations);
            emitMultimediaLinks(1, f.getMultimedia());
            emitNotes(1, f.notes);
            for (UserReference u : f.getUserReferences()) {
                emitTagWithRequiredValue(1, "REFN", u.referenceNum);
                emitTagIfValueNotNull(2, "TYPE", u.type);
            }
            emitTagIfValueNotNull(1, "RIN", f.automatedRecordId);
            emitChangeDate(1, f.changeDate);
            emitCustomTags(1, f.getCustomTags());
            notifyConstructObserversIfNeeded();
            if (cancelled) {
                throw new WriterCancelledException("Construction and writing of GEDCOM cancelled");
            }
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
        emitTagWithOptionalValue(level, e.getType().getTag(), e.getyNull());
        emitEventDetail(level + 1, e);
        if (e.getHusbandAge() != null) {
            emitTag(level + 1, "HUSB");
            emitTagWithRequiredValue(level + 2, "AGE", e.getHusbandAge());
        }
        if (e.getWifeAge() != null) {
            emitTag(level + 1, "WIFE");
            emitTagWithRequiredValue(level + 2, "AGE", e.getWifeAge());
        }
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
        Header header = gedcom.getHeader();
        if (header == null) {
            header = new Header();
        }
        lines.add("0 HEAD");
        emitSourceSystem(header.getSourceSystem());
        emitTagIfValueNotNull(1, "DEST", header.getDestinationSystem());
        if (header.getDate() != null) {
            emitTagIfValueNotNull(1, "DATE", header.getDate());
            emitTagIfValueNotNull(2, "TIME", header.getTime());
        }
        if (header.getSubmitter() != null) {
            emitTagWithRequiredValue(1, "SUBM", header.getSubmitter().getXref());
        }
        if (header.getSubmission() != null) {
            emitTagWithRequiredValue(1, "SUBN", header.getSubmission().getXref());
        }
        emitTagIfValueNotNull(1, "FILE", header.getFileName());
        emitLinesOfText(1, "COPR", header.getCopyrightData());
        emitTag(1, "GEDC");
        emitTagWithRequiredValue(2, "VERS", header.getGedcomVersion().getVersionNumber().toString());
        emitTagWithRequiredValue(2, "FORM", header.getGedcomVersion().getGedcomForm());
        emitTagWithRequiredValue(1, "CHAR", header.getCharacterSet().getCharacterSetName());
        emitTagIfValueNotNull(2, "VERS", header.getCharacterSet().getVersionNum());
        emitTagIfValueNotNull(1, "LANG", header.getLanguage());
        if (header.getPlaceHierarchy() != null && header.getPlaceHierarchy().getValue() != null && header.getPlaceHierarchy().getValue().length() > 0) {
            emitTag(1, "PLAC");
            emitTagWithRequiredValue(2, "FORM", header.getPlaceHierarchy());
        }
        emitNotes(1, header.getNotes());
        emitCustomTags(1, header.getCustomTags());
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
            emitTagWithOptionalValueAndCustomSubtags(level, a.type.tag, a.getDescription());
            emitEventDetail(level + 1, a);
            emitAddress(level + 1, a.getAddress());
            emitPhoneNumbers(level + 1, a.getPhoneNumbers());
            emitWwwUrls(level + 1, a.getWwwUrls());
            emitFaxNumbers(level + 1, a.getFaxNumbers());
            emitEmails(level + 1, a.getEmails());
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
            emitTagWithOptionalValue(level, e.type.tag, e.getyNull());
            emitEventDetail(level + 1, e);
            if (e.type == IndividualEventType.BIRTH || e.type == IndividualEventType.CHRISTENING) {
                if (e.family != null && e.family.getFamily() != null && e.family.getFamily().getXref() != null) {
                    emitTagWithRequiredValue(level + 1, "FAMC", e.family.getFamily().getXref());
                }
            } else if (e.type == IndividualEventType.ADOPTION && e.family != null && e.family.getFamily() != null && e.family.getFamily().getXref() != null) {
                emitTagWithRequiredValue(level + 1, "FAMC", e.family.getFamily().getXref());
                emitTagIfValueNotNull(level + 2, "ADOP", e.family.getAdoptedBy());
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
        for (Individual i : gedcom.getIndividuals().values()) {
            emitTag(0, i.getXref(), "INDI");
            emitTagIfValueNotNull(1, "RESN", i.restrictionNotice);
            emitPersonalNames(1, i.names);
            emitTagIfValueNotNull(1, "SEX", i.sex);
            emitIndividualEvents(1, i.events);
            emitIndividualAttributes(1, i.attributes);
            emitLdsIndividualOrdinances(1, i.ldsIndividualOrdinances);
            emitChildToFamilyLinks(1, i);
            emitSpouseInFamilyLinks(1, i);
            for (Submitter s : i.submitters) {
                emitTagWithRequiredValue(1, "SUBM", s.getXref());
            }
            emitAssociationStructures(1, i.associations);
            for (StringWithCustomTags s : i.aliases) {
                emitTagWithRequiredValue(1, "ALIA", s);
            }
            for (Submitter s : i.ancestorInterest) {
                emitTagWithRequiredValue(1, "ANCI", s.getXref());
            }
            for (Submitter s : i.descendantInterest) {
                emitTagWithRequiredValue(1, "DESI", s.getXref());
            }
            emitSourceCitations(1, i.citations);
            emitMultimediaLinks(1, i.getMultimedia());
            emitNotes(1, i.notes);
            emitTagIfValueNotNull(1, "RFN", i.permanentRecFileNumber);
            emitTagIfValueNotNull(1, "AFN", i.ancestralFileNumber);
            for (UserReference u : i.getUserReferences()) {
                emitTagWithRequiredValue(1, "REFN", u.referenceNum);
                emitTagIfValueNotNull(2, "TYPE", u.type);
            }
            emitTagIfValueNotNull(1, "RIN", i.getRecIdNumber());
            emitChangeDate(1, i.changeDate);
            emitCustomTags(1, i.getCustomTags());
            notifyConstructObserversIfNeeded();
            if (cancelled) {
                throw new WriterCancelledException("Construction and writing of GEDCOM cancelled");
            }
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
        emitTagIfValueNotNull(level + 1, "STAT", sealings.getStatus());
        emitTagIfValueNotNull(level + 1, "DATE", sealings.getDate());
        emitTagIfValueNotNull(level + 1, "TEMP", sealings.getTemple());
        emitTagIfValueNotNull(level + 1, "PLAC", sealings.getPlace());
        emitSourceCitations(level + 1, sealings.getCitations());
        emitNotes(level + 1, sealings.getNotes());
        emitCustomTags(level + 1, sealings.getCustomTags());
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
    private void emitLdsIndividualOrdinances(int level, List<LdsIndividualOrdinance> ldsIndividualOrdinances) throws GedcomWriterException {
        for (LdsIndividualOrdinance o : ldsIndividualOrdinances) {
            emitTagWithOptionalValue(level, o.type.tag, o.yNull);
            emitTagIfValueNotNull(level + 1, "STAT", o.getStatus());
            emitTagIfValueNotNull(level + 1, "DATE", o.getDate());
            emitTagIfValueNotNull(level + 1, "TEMP", o.getTemple());
            emitTagIfValueNotNull(level + 1, "PLAC", o.getPlace());
            if (o.type == LdsIndividualOrdinanceType.CHILD_SEALING) {
                if (o.familyWhereChild == null) {
                    throw new GedcomWriterException("LDS Ordinance info for a child sealing had no reference to a family");
                }
                if (o.familyWhereChild.getFamily() == null) {
                    throw new GedcomWriterException("LDS Ordinance info for a child sealing had familyChild object with a null reference to a family");
                }
                emitTagWithRequiredValue(level + 1, "FAMC", o.familyWhereChild.getFamily().getXref());
            }
            emitSourceCitations(level + 1, o.getCitations());
            emitNotes(level + 1, o.getNotes());
            emitCustomTags(level + 1, o.getCustomTags());
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
     * Emit a multi-line text value. If a line of text contains line breaks (newlines, line feeds, carriage returns),
     * the parts on either side of the line break will be treated as separate lines.
     * 
     * @param level
     *            the level we are starting at. Continuation lines will be one level deeper than this value
     * @param startingTag
     *            the tag to use for the first line of the text. All subsequent lines will be "CONT" lines.
     * @param xref
     *            the xref of the item with lines of text
     * @param linesOfText
     *            the lines of text to write
     */
    private void emitLinesOfText(int level, String xref, String startingTag, List<String> linesOfText) {
        List<String> splitLinesOfText = splitLinesOnBreakingCharacters(linesOfText);
        int lineNum = 0;
        for (String l : splitLinesOfText) {
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
        for (Multimedia m : gedcom.getMultimedia().values()) {
            emitTag(0, m.getXref(), "OBJE");
            emitTagWithRequiredValue(1, "FORM", m.embeddedMediaFormat);
            emitTagIfValueNotNull(1, "TITL", m.embeddedTitle);
            emitNotes(1, m.getNotes());
            emitTag(1, "BLOB");
            for (String b : m.blob) {
                emitTagWithRequiredValue(2, "CONT", b);
            }
            if (m.continuedObject != null && m.continuedObject.getXref() != null) {
                emitTagWithRequiredValue(1, "OBJE", m.continuedObject.getXref());
            }
            for (UserReference u : m.getUserReferences()) {
                emitTagWithRequiredValue(1, "REFN", u.referenceNum);
                emitTagIfValueNotNull(2, "TYPE", u.type);
            }
            emitTagIfValueNotNull(1, "RIN", m.getRecIdNumber());
            emitChangeDate(1, m.changeDate);
            if (!m.fileReferences.isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("GEDCOM version is 5.5, but found file references in multimedia object " + m.getXref()
                        + " which are not allowed until GEDCOM 5.5.1");
            }
            emitCustomTags(1, m.getCustomTags());
            notifyConstructObserversIfNeeded();
            if (cancelled) {
                throw new WriterCancelledException("Construction and writing of GEDCOM cancelled");
            }
        }
    }

    /**
     * Write out all the embedded multimedia objects in GEDCOM 5.5.1 format
     * 
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    private void emitMultimedia551() throws GedcomWriterException {
        for (Multimedia m : gedcom.getMultimedia().values()) {
            emitTag(0, m.getXref(), "OBJE");
            for (FileReference fr : m.fileReferences) {
                emitTagWithRequiredValue(1, "FILE", fr.getReferenceToFile());
                emitTagWithRequiredValue(2, "FORM", fr.getFormat());
                emitTagIfValueNotNull(3, "TYPE", fr.getMediaType());
                emitTagIfValueNotNull(2, "TITL", fr.getTitle());
            }
            for (UserReference u : m.getUserReferences()) {
                emitTagWithRequiredValue(1, "REFN", u.referenceNum);
                emitTagIfValueNotNull(2, "TYPE", u.type);
            }
            emitTagIfValueNotNull(1, "RIN", m.getRecIdNumber());
            emitNotes(1, m.getNotes());
            emitChangeDate(1, m.changeDate);
            emitCustomTags(1, m.getCustomTags());
            if (!m.blob.isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("GEDCOM version is 5.5.1, but BLOB data on multimedia item " + m.getXref()
                        + " was found.  This is only allowed in GEDCOM 5.5");
            }
            if (m.continuedObject != null) {
                throw new GedcomWriterVersionDataMismatchException("GEDCOM version is 5.5.1, but BLOB continuation data on multimedia item " + m.getXref()
                        + " was found.  This is only allowed in GEDCOM 5.5");
            }
            if (m.embeddedMediaFormat != null) {
                throw new GedcomWriterVersionDataMismatchException("GEDCOM version is 5.5.1, but format on multimedia item " + m.getXref()
                        + " was found.  This is only allowed in GEDCOM 5.5");
            }
            if (m.embeddedTitle != null) {
                throw new GedcomWriterVersionDataMismatchException("GEDCOM version is 5.5.1, but title on multimedia item " + m.getXref()
                        + " was found.  This is only allowed in GEDCOM 5.5");
            }
            notifyConstructObserversIfNeeded();
            if (cancelled) {
                throw new WriterCancelledException("Construction and writing of GEDCOM cancelled");
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
            if (m.getXref() == null) {
                // Link to referenced form
                if (g55()) {
                    // GEDCOM 5.5 format
                    emitTag(level, "OBJE");
                    if (m.fileReferences.size() > 1) {
                        throw new GedcomWriterVersionDataMismatchException("GEDCOM version is 5.5, but multimedia link references "
                                + "multiple files, which is only allowed in GEDCOM 5.5.1");
                    }
                    if (m.fileReferences.size() == 1) {
                        FileReference fr = m.fileReferences.get(0);
                        if (fr.getFormat() == null) {
                            emitTagWithRequiredValue(level + 1, "FORM", m.embeddedMediaFormat);
                        } else {
                            emitTagWithRequiredValue(level + 1, "FORM", fr.getFormat());
                        }
                        emitTagIfValueNotNull(level + 1, "TITL", m.embeddedTitle);
                        emitTagWithRequiredValue(level + 1, "FILE", fr.getReferenceToFile());
                    } else {
                        emitTagWithRequiredValue(level + 1, "FORM", m.embeddedMediaFormat);
                        emitTagIfValueNotNull(level + 1, "TITL", m.embeddedTitle);
                    }
                    emitNotes(level + 1, m.getNotes());
                } else {
                    // GEDCOM 5.5.1 format
                    for (FileReference fr : m.fileReferences) {
                        emitTagWithRequiredValue(level + 1, "FILE", fr.getReferenceToFile());
                        emitTagIfValueNotNull(level + 2, "FORM", fr.getFormat());
                        emitTagIfValueNotNull(level + 3, "MEDI", fr.getMediaType());
                        emitTagIfValueNotNull(level + 1, "TITL", fr.getTitle());
                    }
                    if (!m.getNotes().isEmpty()) {
                        throw new GedcomWriterVersionDataMismatchException(
                                "GEDCOM version is 5.5.1, but multimedia link has notes which are no longer allowed in 5.5");
                    }
                }
            } else {
                // Link to the embedded form
                emitTagWithRequiredValue(level, "OBJE", m.getXref());
            }
            emitCustomTags(level + 1, m.getCustomTags());
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
        if (level > 0 && note.getXref() != null) {
            emitTagWithRequiredValue(level, "NOTE", note.getXref());
            return;
        }
        emitNoteLines(level, note.getXref(), note.lines);
        emitSourceCitations(level + 1, note.citations);
        for (UserReference u : note.getUserReferences()) {
            emitTagWithRequiredValue(level + 1, "REFN", u.referenceNum);
            emitTagIfValueNotNull(level + 2, "TYPE", u.type);
        }
        emitTagIfValueNotNull(level + 1, "RIN", note.getRecIdNumber());
        emitChangeDate(level + 1, note.changeDate);
        emitCustomTags(level + 1, note.getCustomTags());
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
            emitCustomTags(level + 1, n.getCustomTags());
            notifyConstructObserversIfNeeded();
            if (cancelled) {
                throw new WriterCancelledException("Construction and writing of GEDCOM cancelled");
            }
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
            emitCustomTags(level + 1, n.getCustomTags());
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
    private void emitPersonalNameVariation(int level, String variationTag, PersonalNameVariation pnv) throws GedcomWriterException {
        emitTagWithRequiredValue(level, variationTag, pnv.variation);
        emitTagIfValueNotNull(level + 1, "NPFX", pnv.prefix);
        emitTagIfValueNotNull(level + 1, "GIVN", pnv.givenName);
        emitTagIfValueNotNull(level + 1, "NICK", pnv.nickname);
        emitTagIfValueNotNull(level + 1, "SPFX", pnv.surnamePrefix);
        emitTagIfValueNotNull(level + 1, "SURN", pnv.surname);
        emitTagIfValueNotNull(level + 1, "NSFX", pnv.suffix);
        emitSourceCitations(level + 1, pnv.citations);
        emitNotes(level + 1, pnv.notes);
        emitCustomTags(level + 1, pnv.getCustomTags());
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
                throw new GedcomWriterVersionDataMismatchException("GEDCOM version is 5.5, but romanized variation was specified on place " + p.placeName
                        + ", which is only allowed in GEDCOM 5.5.1");
            }
            emitTagWithRequiredValue(level + 1, "ROMN", nv.variation);
            emitTagIfValueNotNull(level + 2, "TYPE", nv.variationType);
        }
        for (NameVariation nv : p.phonetic) {
            if (g55()) {
                throw new GedcomWriterVersionDataMismatchException("GEDCOM version is 5.5, but phonetic variation was specified on place " + p.placeName
                        + ", which is only allowed in GEDCOM 5.5.1");
            }
            emitTagWithRequiredValue(level + 1, "FONE", nv.variation);
            emitTagIfValueNotNull(level + 2, "TYPE", nv.variationType);
        }
        if (p.latitude != null || p.longitude != null) {
            emitTag(level + 1, "MAP");
            emitTagWithRequiredValue(level + 2, "LATI", p.latitude);
            emitTagWithRequiredValue(level + 2, "LONG", p.longitude);
            if (g55()) {
                throw new GedcomWriterVersionDataMismatchException("GEDCOM version is 5.5, but map coordinates were specified on place " + p.placeName
                        + ", which is only allowed in GEDCOM 5.5.1");
            }
        }
        emitCustomTags(level + 1, p.getCustomTags());
    }

    /**
     * Write the records (see the RECORD structure in the GEDCOM standard)
     * 
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    private void emitRecords() throws GedcomWriterException {
        notifyConstructObserversIfNeeded();
        emitIndividuals();
        emitFamilies();
        if (g55()) {
            emitMultimedia55();
        } else {
            emitMultimedia551();
        }
        emitNotes(0, new ArrayList<Note>(gedcom.getNotes().values()));
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
        for (Repository r : gedcom.getRepositories().values()) {
            emitTag(0, r.getXref(), "REPO");
            emitTagIfValueNotNull(1, "NAME", r.name);
            emitAddress(1, r.address);
            emitNotes(1, r.notes);
            for (UserReference u : r.getUserReferences()) {
                emitTagWithRequiredValue(1, "REFN", u.referenceNum);
                emitTagIfValueNotNull(2, "TYPE", u.type);
            }
            emitTagIfValueNotNull(1, "RIN", r.getRecIdNumber());
            emitPhoneNumbers(1, r.getPhoneNumbers());
            emitWwwUrls(1, r.getWwwUrls());
            emitFaxNumbers(1, r.getFaxNumbers());
            emitEmails(1, r.getEmails());
            emitChangeDate(1, r.changeDate);
            emitCustomTags(1, r.getCustomTags());
            notifyConstructObserversIfNeeded();
            if (cancelled) {
                throw new WriterCancelledException("Construction and writing of GEDCOM cancelled");
            }
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
            emitCustomTags(level + 1, repositoryCitation.getCustomTags());
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
        for (Source s : gedcom.getSources().values()) {
            emitTag(0, s.getXref(), "SOUR");
            SourceData d = s.data;
            if (d != null) {
                emitTag(1, "DATA");
                for (EventRecorded e : d.eventsRecorded) {
                    emitTagWithOptionalValue(2, "EVEN", e.getEventType());
                    emitTagIfValueNotNull(3, "DATE", e.getDatePeriod());
                    emitTagIfValueNotNull(3, "PLAC", e.getJurisdiction());
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
            emitMultimediaLinks(1, s.getMultimedia());
            emitNotes(1, s.notes);
            for (UserReference u : s.getUserReferences()) {
                emitTagWithRequiredValue(1, "REFN", u.referenceNum);
                emitTagIfValueNotNull(2, "TYPE", u.type);
            }
            emitTagIfValueNotNull(1, "RIN", s.getRecIdNumber());
            emitChangeDate(1, s.changeDate);
            emitCustomTags(1, s.getCustomTags());
            notifyConstructObserversIfNeeded();
            if (cancelled) {
                throw new WriterCancelledException("Construction and writing of GEDCOM cancelled");
            }
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
            emitTagWithOptionalValue(2, "CORP", corporation.getBusinessName());
            emitAddress(3, corporation.getAddress());
            emitPhoneNumbers(3, corporation.getPhoneNumbers());
            emitFaxNumbers(3, corporation.getFaxNumbers());
            emitWwwUrls(3, corporation.getWwwUrls());
            emitEmails(3, corporation.getEmails());
        }
        HeaderSourceData sourceData = sourceSystem.sourceData;
        if (sourceData != null) {
            emitTagIfValueNotNull(2, "DATA", sourceData.getName());
            emitTagIfValueNotNull(3, "DATE", sourceData.getPublishDate());
            emitTagIfValueNotNull(3, "COPR", sourceData.getCopyright());
        }
        emitCustomTags(1, sourceSystem.getCustomTags());
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
            if (familySpouse.getFamily() == null) {
                throw new GedcomWriterException("Family in which " + i + " was a spouse had a null family reference");
            }
            emitTagWithRequiredValue(level, "FAMS", familySpouse.getFamily().getXref());
            emitNotes(level + 1, familySpouse.getNotes());
            emitCustomTags(level + 1, familySpouse.getCustomTags());
        }
    }

    /**
     * Write the SUBMISSION_RECORD
     * 
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    private void emitSubmissionRecord() throws GedcomWriterException {
        Submission s = gedcom.getSubmission();
        if (s == null) {
            return;
        }
        emitTag(0, s.getXref(), "SUBN");
        if (s.submitter != null) {
            emitTagWithOptionalValue(1, "SUBM", s.submitter.getXref());
        }
        emitTagIfValueNotNull(1, "FAMF", s.nameOfFamilyFile);
        emitTagIfValueNotNull(1, "TEMP", s.templeCode);
        emitTagIfValueNotNull(1, "ANCE", s.ancestorsCount);
        emitTagIfValueNotNull(1, "DESC", s.descendantsCount);
        emitTagIfValueNotNull(1, "ORDI", s.ordinanceProcessFlag);
        emitTagIfValueNotNull(1, "RIN", s.getRecIdNumber());
        emitCustomTags(1, s.getCustomTags());
    }

    /**
     * Write out the submitter record
     * 
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    private void emitSubmitter() throws GedcomWriterException {
        for (Submitter s : gedcom.getSubmitters().values()) {
            emitTag(0, s.getXref(), "SUBM");
            emitTagWithOptionalValueAndCustomSubtags(1, "NAME", s.getName());
            emitAddress(1, s.getAddress());
            emitMultimediaLinks(1, s.getMultimedia());
            for (StringWithCustomTags l : s.getLanguagePref()) {
                emitTagWithRequiredValue(1, "LANG", l);
            }
            emitPhoneNumbers(1, s.getPhoneNumbers());
            emitWwwUrls(1, s.getWwwUrls());
            emitFaxNumbers(1, s.getFaxNumbers());
            emitEmails(1, s.getEmails());
            emitTagIfValueNotNull(1, "RFN", s.getRegFileNumber());
            emitTagIfValueNotNull(1, "RIN", s.getRecIdNumber());
            emitChangeDate(1, s.getChangeDate());
            emitCustomTags(1, s.getCustomTags());
            notifyConstructObserversIfNeeded();
            if (cancelled) {
                throw new WriterCancelledException("Construction and writing of GEDCOM cancelled");
            }
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

            List<String> temp = new ArrayList<String>();
            temp.add(value.toString());
            List<String> valueLines = splitLinesOnBreakingCharacters(temp);

            boolean first = true;
            for (String v : valueLines) {

                StringBuilder line = new StringBuilder();
                if (first) {
                    line.append(level);
                    if (xref != null && xref.length() > 0) {
                        line.append(" ").append(xref);
                    }
                    line.append(" ").append(tag).append(" ").append(v);
                    emitAndSplit(level, line.toString());
                } else {
                    line.append(level + 1);
                    line.append(" CONT ").append(v);
                    emitAndSplit(level + 1, line.toString());
                }

                first = false;
            }
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
        if (value != null) {
            List<String> temp = new ArrayList<String>();
            temp.add(value.toString());
            List<String> valueLines = splitLinesOnBreakingCharacters(temp);

            boolean first = true;
            for (String v : valueLines) {

                StringBuilder line = new StringBuilder();
                if (first) {
                    line.append(level);
                    line.append(" ").append(tag).append(" ").append(v);
                    emitAndSplit(level, line.toString());
                } else {
                    line.append(level + 1);
                    line.append(" CONT ").append(v);
                    emitAndSplit(level + 1, line.toString());
                }

                first = false;
            }
        } else {
            StringBuilder line = new StringBuilder(Integer.toString(level));
            line.append(" ").append(tag);
            lines.add(line.toString());
        }
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
    private void emitTagWithOptionalValueAndCustomSubtags(int level, String tag, StringWithCustomTags valueToRightOfTag) throws GedcomWriterException {
        if (valueToRightOfTag == null || valueToRightOfTag.getValue() == null) {
            StringBuilder line = new StringBuilder(Integer.toString(level));
            line.append(" ").append(tag);
            lines.add(line.toString());
            if (valueToRightOfTag != null) {
                emitCustomTags(level + 1, valueToRightOfTag.getCustomTags());
            }
            return;
        }

        List<String> temp = new ArrayList<String>();
        temp.add(valueToRightOfTag.getValue());
        List<String> valueLines = splitLinesOnBreakingCharacters(temp);

        boolean first = true;
        for (String v : valueLines) {
            StringBuilder line = new StringBuilder();
            if (first) {
                line.append(level);
                line.append(" ").append(tag).append(" ").append(v);
                emitAndSplit(level, line.toString());
            } else {
                line.append(level + 1);
                line.append(" CONT ").append(v);
                emitAndSplit(level + 1, line.toString());
            }

            first = false;
        }
        emitCustomTags(level + 1, valueToRightOfTag.getCustomTags());
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
    private void emitTagWithRequiredValue(int level, String xref, String tag, StringWithCustomTags e) throws GedcomWriterException {
        if (e == null || e.getValue() == null || e.getValue().trim().length() == 0) {
            throw new GedcomWriterException("Required value for tag " + tag + " at level " + level + " was null or blank");
        }
        List<String> temp = new ArrayList<String>();
        temp.add(e.getValue());
        List<String> valueLines = splitLinesOnBreakingCharacters(temp);

        boolean first = true;
        for (String v : valueLines) {
            StringBuilder line = new StringBuilder();
            if (first) {
                line.append(level);
                if (xref != null && xref.length() > 0) {
                    line.append(" ").append(xref);
                }
                line.append(" ").append(tag).append(" ").append(v);
                emitAndSplit(level, line.toString());
            } else {
                line.append(level + 1);
                line.append(" CONT ").append(v);
                emitAndSplit(level + 1, line.toString());
            }

            first = false;
        }

        emitCustomTags(level + 1, e.getCustomTags());
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
    private void emitTagWithRequiredValue(int level, String tag, StringWithCustomTags value) throws GedcomWriterException {
        emitTagWithRequiredValue(level, null, tag, value);
    }

    /**
     * Write out the trailer record
     */
    private void emitTrailer() {
        lines.add("0 TRLR");
        notifyConstructObservers(new ConstructProgressEvent(this, lines.size(), true));
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
        return gedcom != null && gedcom.getHeader() != null && gedcom.getHeader().getGedcomVersion() != null && SupportedVersion.V5_5.equals(gedcom.getHeader()
                .getGedcomVersion().getVersionNumber());
    }

    /**
     * Notify all listeners about the line being
     * 
     * @param e
     *            the change event to tell the observers
     */
    private void notifyConstructObservers(ConstructProgressEvent e) {
        int i = 0;
        lastLineCountNotified = e.getLinesProcessed();
        while (i < constructObservers.size()) {
            WeakReference<ConstructProgressListener> observerRef = constructObservers.get(i);
            if (observerRef == null) {
                constructObservers.remove(observerRef);
            } else {
                observerRef.get().progressNotification(e);
                i++;
            }
        }
    }

    /**
     * Notify construct observers if more than 100 lines have been constructed since last time we notified them
     */
    private void notifyConstructObserversIfNeeded() {
        if ((lines.size() - lastLineCountNotified) > constructionNotificationRate) {
            notifyConstructObservers(new ConstructProgressEvent(this, lines.size(), true));
        }
    }
}
