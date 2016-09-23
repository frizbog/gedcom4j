/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 *
 * MIT License
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
import org.gedcom4j.io.writer.LineTerminator;
import org.gedcom4j.model.AbstractAddressableElement;
import org.gedcom4j.model.AbstractEvent;
import org.gedcom4j.model.FamilyChild;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.GedcomVersion;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualAttribute;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.Repository;
import org.gedcom4j.model.StringWithCustomFacts;
import org.gedcom4j.model.Submitter;
import org.gedcom4j.model.enumerations.IndividualAttributeType;
import org.gedcom4j.model.enumerations.SupportedVersion;
import org.gedcom4j.validate.AutoRepairResponder;
import org.gedcom4j.validate.Severity;
import org.gedcom4j.validate.Validator;
import org.gedcom4j.validate.Validator.Finding;
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
 * <li>Instantiate a <code>GedcomWriter</code>, passing the {@link Gedcom} to be written as a parameter to the constructor.</li>
 * <li>Call one of the variants of the <code>write</code> method to write the data</li>
 * <li>Optionally check the contents of the {@link #validationFindings} collection to see if there was anything problematic found in
 * your data.</li>
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
 * {@link org.gedcom4j.validate.GedcomValidator#setAutoRepairEnabled(boolean)})...this way your data is not altered. Validation can
 * be suppressed if you want by setting {@link #validationSuppressed} to true, but this is not recommended. You can also force
 * autorepair on if you want.
 * </p>
 * 
 * @author frizbog1
 */
/**
 * @author Mark A Sikes
 *
 */
@SuppressWarnings({ "PMD.GodClass", "PMD.TooManyMethods", "PMD.ExcessiveImports" })
public class GedcomWriter extends AbstractEmitter<Gedcom> {
    /**
     * The text lines of the GEDCOM file we're writing, which will be written using a {@link GedcomFileWriter}. Deliberately
     * package-private so tests can access it but others can't alter it.
     */
    List<String> lines = new ArrayList<>();

    /**
     * The auto repair responder.
     */
    private AutoRepairResponder autoRepairResponder;

    /**
     * Has this writer been cancelled?
     */
    private boolean cancelled;

    /**
     * Send a notification whenever more than this many lines are constructed
     */
    private int constructionNotificationRate = 500;

    /**
     * The list of observers on string construction
     */
    private final List<WeakReference<ConstructProgressListener>> constructObservers = new CopyOnWriteArrayList<>();

    /**
     * Send a notification whenever more than this many lines are written to a file
     */
    private int fileNotificationRate = 500;

    /**
     * The list of observers on file operations
     */
    private final List<WeakReference<FileProgressListener>> fileObservers = new CopyOnWriteArrayList<>();

    /**
     * The number of lines constructed as last reported to the observers
     */
    private int lastLineCountNotified = 0;

    /**
     * The line terminator to use
     */
    private LineTerminator lineTerminator = LineTerminator.getDefaultLineTerminator();

    /**
     * Whether to use little-endian unicode
     */
    private boolean useLittleEndianForUnicode = true;

    /**
     * Are we suppressing the call to the validator?
     */
    private boolean validationSuppressed = false;

    /**
     * The validator.
     */
    private Validator validator;

    /**
     * Constructor
     * 
     * @param gedcom
     *            the {@link Gedcom} structure to write out
     * @throws WriterCancelledException
     *             if cancellation was requested during the operation
     */
    public GedcomWriter(Gedcom gedcom) throws WriterCancelledException {
        super(null, 0, gedcom);
        baseWriter = this;
    }

    /**
     * Cancel construction of the GEDCOM and writing it to a file
     */
    public void cancel() {
        cancelled = true;
    }

    /**
     * Get the autoRepairResponder
     * 
     * @return the autoRepairResponder
     */
    public AutoRepairResponder getAutoRepairResponder() {
        return autoRepairResponder;
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
     * Get the line terminator
     * 
     * @return the line terminator
     */
    public LineTerminator getLineTerminator() {
        return lineTerminator;
    }

    /**
     * Get the validator
     * 
     * @return the validator
     */
    public Validator getValidator() {
        return validator;
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
     * Get the useLittleEndianForUnicode
     * 
     * @return the useLittleEndianForUnicode
     */
    public boolean isUseLittleEndianForUnicode() {
        return useLittleEndianForUnicode;
    }

    /**
     * Get whether validation is suppressed
     * 
     * @return whether validation is suppressed or not
     */
    public boolean isValidationSuppressed() {
        return validationSuppressed;
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
                fileObservers.remove(i);
            } else {
                FileProgressListener l = observerRef.get();
                if (l != null) {
                    l.progressNotification(e);
                }
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
        constructObservers.add(new WeakReference<>(observer));
    }

    /**
     * Register a observer (listener) to be informed about progress and completion.
     * 
     * @param observer
     *            the observer you want notified
     */
    public void registerFileObserver(FileProgressListener observer) {
        fileObservers.add(new WeakReference<>(observer));
    }

    /**
     * Set the autoRepairResponder
     * 
     * @param autoRepairResponder
     *            the autoRepairResponder to set
     */
    public void setAutoRepairResponder(AutoRepairResponder autoRepairResponder) {
        this.autoRepairResponder = autoRepairResponder;
    }

    /**
     * Set the construction notification rate - how many lines need to be constructed before getting a notification
     * 
     * @param constructionNotificationRate
     *            the construction notification rate - how many lines need to be constructed before getting a notification. Must be
     *            1 or greater.
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
     * Set the line terminator
     * 
     * @param lineTerminator
     *            the lineTerminator to set
     */
    public void setLineTerminator(LineTerminator lineTerminator) {
        this.lineTerminator = lineTerminator;
    }

    /**
     * Set the useLittleEndianForUnicode
     * 
     * @param useLittleEndianForUnicode
     *            the useLittleEndianForUnicode to set
     */
    public void setUseLittleEndianForUnicode(boolean useLittleEndianForUnicode) {
        this.useLittleEndianForUnicode = useLittleEndianForUnicode;
    }

    /**
     * Set whether validation is suppressed or not
     * 
     * @param validationSuppressed
     *            set to true to suppress validation
     */
    public void setValidationSuppressed(boolean validationSuppressed) {
        this.validationSuppressed = validationSuppressed;
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
        constructObservers.add(new WeakReference<>(observer));
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
        fileObservers.add(new WeakReference<>(observer));
    }

    /**
     * Write the {@link Gedcom} data as a GEDCOM 5.5 file. Automatically fills in the value for the FILE tag in the HEAD structure.
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
        writeFrom.getHeader().setFileName(new StringWithCustomFacts(file.getName()));

        // If the file doesn't exist yet, we have to create it, otherwise a FileNotFoundException will be thrown
        if (!file.exists() && !file.getCanonicalFile().getParentFile().exists() && !file.getCanonicalFile().getParentFile().mkdirs()
                && !file.createNewFile()) {
            throw new IOException("Unable to create file " + file.getName());
        }

        try (OutputStream o = new FileOutputStream(file);) {
            write(o);
            o.flush();
        }
    }

    /**
     * Write the {@link Gedcom} data in GEDCOM 5.5 format to an output stream
     * 
     * @param out
     *            the output stream we're writing to
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written; or if the data fails validation with one or more finding of
     *             severity ERROR (and validation is not suppressed - see {@link GedcomWriter#validationSuppressed})
     */
    public void write(OutputStream out) throws GedcomWriterException {
        emit();
        try {
            GedcomFileWriter gfw = new GedcomFileWriter(this, lines);
            gfw.setUseLittleEndianForUnicode(useLittleEndianForUnicode);
            gfw.setTerminator(getLineTerminator());
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
     * {@inheritDoc}
     */
    @Override
    protected void emit() throws GedcomWriterException {
        if (!validationSuppressed) {
            validator = new Validator(writeFrom);
            validator.setAutoRepairResponder(getAutoRepairResponder());
            validator.validate();
            int numUnrepairedErrorFindings = 0;
            for (Finding f : validator.getResults().getAllFindings()) {
                if (f.getSeverity() == Severity.ERROR && (f.getRepairs() == null || f.getRepairs().isEmpty())) {
                    numUnrepairedErrorFindings++;
                }
            }
            if (numUnrepairedErrorFindings > 0) {
                throw new GedcomWriterException("Cannot write file - " + numUnrepairedErrorFindings
                        + " error(s) found during validation requiring repair.  Review the validation findings to determine root cause.");
            }
        }
        checkVersionCompatibility();
        new HeaderEmitter(baseWriter, 0, writeFrom.getHeader()).emit();
        new SubmissionEmitter(baseWriter, 0, writeFrom.getSubmission()).emit();
        new IndividualEmitter(baseWriter, 0, writeFrom.getIndividuals().values()).emit();
        new FamilyEmitter(baseWriter, 0, writeFrom.getFamilies().values()).emit();
        if (g55()) {
            new Multimedia55Emitter(baseWriter, 0, writeFrom.getMultimedia().values()).emit();
        } else {
            new Multimedia551Emitter(baseWriter, 0, writeFrom.getMultimedia().values()).emit();
        }
        new NotesEmitter(baseWriter, 0, writeFrom.getNotes().values()).emit();
        new RepositoryEmitter(baseWriter, 0, writeFrom.getRepositories().values()).emit();
        new SourceEmitter(baseWriter, 0, writeFrom.getSources().values()).emit();
        new SubmittersEmitter(this, 0, writeFrom.getSubmitters().values()).emit();
        emitTrailer();
        emitCustomFacts(1, writeFrom.getCustomFacts());
    }

    /**
     * Notify construct observers if more than 100 lines have been constructed since last time we notified them
     */
    void notifyConstructObserversIfNeeded() {
        if (lines.size() - lastLineCountNotified > constructionNotificationRate) {
            notifyConstructObservers(new ConstructProgressEvent(this, lines.size(), true));
        }
    }

    /**
     * Checks that the gedcom version specified is compatible with the data in the model. Not a perfect exhaustive check.
     * 
     * @throws GedcomWriterException
     *             if data is detected that is incompatible with the selected version
     */
    private void checkVersionCompatibility() throws GedcomWriterException {

        if (writeFrom.getHeader().getGedcomVersion() == null) {
            // If there's not one specified, set up a default one that specifies
            // 5.5.1
            writeFrom.getHeader().setGedcomVersion(new GedcomVersion());
        }
        if (SupportedVersion.V5_5.equals(writeFrom.getHeader().getGedcomVersion().getVersionNumber())) {
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
        if (writeFrom.getHeader().getCopyrightData() != null && writeFrom.getHeader().getCopyrightData().size() > 1) {
            throw new GedcomWriterVersionDataMismatchException(
                    "Gedcom version is 5.5, but has multi-line copyright data in header");
        }
        if (writeFrom.getHeader().getCharacterSet() != null && writeFrom.getHeader().getCharacterSet().getCharacterSetName() != null
                && "UTF-8".equals(writeFrom.getHeader().getCharacterSet().getCharacterSetName().getValue())) {
            throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but data is encoded using UTF-8");
        }
        if (writeFrom.getHeader().getSourceSystem() != null && writeFrom.getHeader().getSourceSystem().getCorporation() != null) {
            AbstractAddressableElement c = writeFrom.getHeader().getSourceSystem().getCorporation();
            if (c.getWwwUrls() != null && !c.getWwwUrls().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException(
                        "Gedcom version is 5.5, but source system corporation has www urls");
            }
            if (c.getFaxNumbers() != null && !c.getFaxNumbers().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException(
                        "Gedcom version is 5.5, but source system corporation has fax numbers");
            }
            if (c.getEmails() != null && !c.getEmails().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException(
                        "Gedcom version is 5.5, but source system corporation has emails");
            }
        }
        checkVersionCompatibility55Individuals();
        for (Submitter s : writeFrom.getSubmitters().values()) {
            if (s.getWwwUrls() != null && !s.getWwwUrls().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Submitter " + s.getXref()
                        + " has www urls");
            }
            if (s.getFaxNumbers() != null && !s.getFaxNumbers().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Submitter " + s.getXref()
                        + " has fax numbers");
            }
            if (s.getEmails() != null && !s.getEmails().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Submitter " + s.getXref()
                        + " has emails");
            }
        }
        for (Repository r : writeFrom.getRepositories().values()) {
            if (r.getWwwUrls() != null && !r.getWwwUrls().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Repository " + r.getXref()
                        + " has www urls");
            }
            if (r.getFaxNumbers() != null && !r.getFaxNumbers().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Repository " + r.getXref()
                        + " has fax numbers");
            }
            if (r.getEmails() != null && !r.getEmails().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Repository " + r.getXref()
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
        for (Multimedia m : writeFrom.getMultimedia().values()) {
            if (m.getBlob() != null && !m.getBlob().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5.1, but multimedia item " + m.getXref()
                        + " contains BLOB data which is unsupported in 5.5.1");
            }
        }
    }

    /**
     * Check that the data on individuals is compatible with 5.5 style Gedcom files
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if a data point is detected that is incompatible with the 5.5 standard
     */
    private void checkVersionCompatibility55Individuals() throws GedcomWriterVersionDataMismatchException {
        for (Individual i : writeFrom.getIndividuals().values()) {
            if (i.getWwwUrls() != null && !i.getWwwUrls().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual " + i.getXref()
                        + " has www urls");
            }
            if (i.getFaxNumbers() != null && !i.getFaxNumbers().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual " + i.getXref()
                        + " has fax numbers");
            }
            if (i.getEmails() != null && !i.getEmails().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual " + i.getXref()
                        + " has emails");
            }
            if (i.getEvents() != null) {
                for (AbstractEvent e : i.getEvents()) {
                    if (e.getWwwUrls() != null && !e.getWwwUrls().isEmpty()) {
                        throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual " + i.getXref()
                                + " has www urls on an event");
                    }
                    if (e.getFaxNumbers() != null && !e.getFaxNumbers().isEmpty()) {
                        throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual " + i.getXref()
                                + " has fax numbers on an event");
                    }
                    if (e.getEmails() != null && !e.getEmails().isEmpty()) {
                        throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual " + i.getXref()
                                + " has emails on an event");
                    }
                }
            }
            if (i.getAttributes() != null) {
                for (IndividualAttribute a : i.getAttributes()) {
                    if (IndividualAttributeType.FACT.equals(a.getType())) {
                        throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual " + i.getXref()
                                + " has a FACT attribute");
                    }
                }
            }
            if (i.getFamiliesWhereChild() != null) {
                for (FamilyChild fc : i.getFamiliesWhereChild()) {
                    if (fc.getStatus() != null) {
                        throw new GedcomWriterVersionDataMismatchException("Gedcom version is 5.5, but Individual " + i.getXref()
                                + " is in a family with a status specified (a Gedcom 5.5.1 only feature)");
                    }
                }
            }
        }
    }

    /**
     * Write out the trailer record
     */
    private void emitTrailer() {
        lines.add("0 TRLR");
        notifyConstructObservers(new ConstructProgressEvent(this, lines.size(), true));
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
                constructObservers.remove(i);
            } else {
                ConstructProgressListener l = observerRef.get();
                if (l != null) {
                    l.progressNotification(e);
                }
                i++;
            }
        }
    }

}
