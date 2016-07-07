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
package org.gedcom4j.parser;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.exception.ParserCancelledException;
import org.gedcom4j.exception.UnsupportedVersionException;
import org.gedcom4j.io.event.FileProgressEvent;
import org.gedcom4j.io.event.FileProgressListener;
import org.gedcom4j.io.reader.GedcomFileReader;
import org.gedcom4j.model.*;
import org.gedcom4j.parser.event.ParseProgressEvent;
import org.gedcom4j.parser.event.ParseProgressListener;

/**
 * <p>
 * Class for parsing GEDCOM 5.5 files and creating a {@link Gedcom} structure from them.
 * </p>
 * <p>
 * General usage is as follows:
 * </p>
 * <ol>
 * <li>Instantiate a <code>GedcomParser</code> object</li>
 * <li>Call the <code>GedcomParser.load()</code> method (in one of its various forms) to parse a file/stream</li>
 * <li>Access the parser's <code>gedcom</code> property to access the parsed data</li>
 * </ol>
 * <p>
 * It is <b>highly recommended</b> that after calling the <code>GedcomParser.load()</code> method, the user check the
 * {@link GedcomParser#errors} and {@link GedcomParser#warnings} collections to see if anything problematic was
 * encountered in the data while parsing. Most commonly, the <code>warnings</code> collection will have information
 * about tags from GEDCOM 5.5.1 that were specified in a file that was designated as a GEDCOM 5.5 file. When this
 * occurs, the data is loaded, but will not be able to be written by {@link org.gedcom4j.writer.GedcomWriter} until the
 * version number in the <code>gedcomVersion</code> field of {@link Gedcom#header} is updated to
 * {@link SupportedVersion#V5_5_1}, or the 5.5.1-specific data is cleared from the data.
 * </p>
 * <p>
 * The parser takes a "forgiving" approach where it tries to load as much data as possible, including 5.5.1 data in a
 * file that says it's in 5.5 format, and vice-versa. However, when it finds inconsistencies, it will add messages to
 * the warnings and errors collections. Most of these messages indicate that the data was loaded, even though it was
 * incorrect, and the data will need to be corrected before it can be written.
 * </p>
 * 
 * <p>
 * The parser makes the assumption that if the version of GEDCOM used is explicitly specified in the file header, that
 * the rest of the data in the file should conform to that spec. For example, if the file header says the file is in 5.5
 * format (i.e., has a VERS 5.5 tag), then it will generate warnings if the new 5.5.1 tags (e.g., EMAIL) are encountered
 * elsewhere, but will load the data anyway. If no version is specified, the 5.5.1 format is assumed as a default.
 * </p>
 * 
 * <p>
 * This approach was selected based on the presumption that most of the uses of GEDCOM4J will be to read GEDCOM files
 * rather than to write them, so this provides that use case with the lowest friction.
 * </p>
 * 
 * @author frizbog1
 * 
 */
public class GedcomParser {

    /**
     * The things that went wrong while parsing the gedcom file
     */
    public List<String> errors = new ArrayList<String>();

    /**
     * The content of the gedcom file
     */
    public Gedcom gedcom = new Gedcom();

    /**
     * Indicates whether handling of custom tags should be strict - that is, must an unrecognized tag begin with an
     * underscore to be loaded into the custom tags collection? If false, unrecognized tags will be treated as custom
     * tags even if they don't begin with underscores, and no errors will be issued. If true, unrecognized tags that do
     * not begin with underscores will be discarded, with errors added to the errors collection.
     */
    public boolean strictCustomTags = true;

    /**
     * Indicates whether non-compliant GEDCOM files with actual line breaks in text values (rather than CONT tags)
     * should be parsed (with some loss of data) rather than fail with an exception.
     */
    public boolean strictLineBreaks = true;

    /**
     * The warnings issued during the parsing of the gedcom file
     */
    public List<String> warnings = new ArrayList<String>();

    /**
     * Is the load/parse process being cancelled
     */
    private boolean cancelled;

    /**
     * Send a notification to listeners every time this many lines (or more) are read
     */
    private int readNotificationRate = 500;

    /**
     * The list of observers on file operations
     */
    private final List<WeakReference<FileProgressListener>> fileObservers = new CopyOnWriteArrayList<WeakReference<FileProgressListener>>();

    /**
     * The list of observers on parsing
     */
    private final List<WeakReference<ParseProgressListener>> parseObservers = new CopyOnWriteArrayList<WeakReference<ParseProgressListener>>();

    /**
     * Get a notification whenever this many items (or more) have been parsed
     */
    private int parseNotificationRate = 500;

    /**
     * Indicate that file loading should be cancelled
     */
    public void cancel() {
        cancelled = true;
    }

    /**
     * Get the parse notification rate (the number of items that get parsed between each notification, if listening)
     * 
     * @return the parse notification rate (the number of items that get parsed between each notification, if listening)
     */
    public int getParseNotificationRate() {
        return parseNotificationRate;
    }

    /**
     * Get the read notification rate
     * 
     * @return the read notification rate
     */
    public int getReadNotificationRate() {
        return readNotificationRate;
    }

    /**
     * Is the load and parse operation cancelled?
     * 
     * @return whether the load and parse operation is cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Load a gedcom file from an input stream and create an object hierarchy from the data therein.
     * 
     * @param stream
     *            the stream to load from
     * @throws IOException
     *             if the file cannot be read
     * @throws GedcomParserException
     *             if the file cannot be parsed
     */
    public void load(BufferedInputStream stream) throws IOException, GedcomParserException {
        StringTree stringTree = readStream(stream);
        if (cancelled) {
            throw new ParserCancelledException("File load/parse cancelled");
        }
        loadRootItems(stringTree);
    }

    /**
     * Load a gedcom file by filename and create an object heirarchy from the data therein.
     * 
     * @param filename
     *            the name of the file to load
     * @throws IOException
     *             if the file cannot be read
     * @throws GedcomParserException
     *             if the file cannot be parsed
     */
    public void load(String filename) throws IOException, GedcomParserException {
        StringTree stringTree = readFile(filename);
        if (cancelled) {
            throw new ParserCancelledException("File load/parse cancelled");
        }
        loadRootItems(stringTree);
    }

    /**
     * Notify all listeners about the change
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
    public void registerFileObserver(FileProgressListener observer) {
        fileObservers.add(new WeakReference<FileProgressListener>(observer));
    }

    /**
     * Register a observer (listener) to be informed about progress and completion.
     * 
     * @param observer
     *            the observer you want notified
     */
    public void registerParseObserver(ParseProgressListener observer) {
        parseObservers.add(new WeakReference<ParseProgressListener>(observer));
    }

    /**
     * Set the parse notification rate (the number of items that get parsed between each notification, if listening)
     * 
     * @param parseNotificationRate
     *            the parse notification rate (the number of items that get parsed between each notification, if
     *            listening). Must be at least 1.
     */
    public void setParseNotificationRate(int parseNotificationRate) {
        if (parseNotificationRate < 1) {
            throw new IllegalArgumentException("Parse Notification Rate must be at least 1");
        }
        this.parseNotificationRate = parseNotificationRate;
    }

    /**
     * Set the read notification rate.
     * 
     * @param readNotificationRate
     *            the read notification rate. Must be a positive integer.
     */
    public void setReadNotificationRate(int readNotificationRate) {
        if (readNotificationRate < 1) {
            throw new IllegalArgumentException("Read Notification Rate must be at least 1");
        }
        this.readNotificationRate = readNotificationRate;
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
     * Unregister a observer (listener) to be informed about progress and completion.
     * 
     * @param observer
     *            the observer you want notified
     */
    public void unregisterParseObserver(ParseProgressListener observer) {
        int i = 0;
        while (i < parseObservers.size()) {
            WeakReference<ParseProgressListener> observerRef = parseObservers.get(i);
            if (observerRef == null || observerRef.get() == observer) {
                parseObservers.remove(observerRef);
            } else {
                i++;
            }
        }
        parseObservers.add(new WeakReference<ParseProgressListener>(observer));
    }

    /**
     * Load the flat file into a tree structure that reflects the heirarchy of its contents, using the default encoding
     * for your JVM
     * 
     * @param filename
     *            the file to load
     * @return the string tree representation of the data from the file
     * @throws IOException
     *             if there is a problem reading the file
     * @throws GedcomParserException
     *             if there is a problem parsing the data in the file
     */
    StringTree readFile(String filename) throws IOException, GedcomParserException {
        FileInputStream fis = new FileInputStream(filename);
        try {
            return makeStringTreeFromStream(new BufferedInputStream(fis));
        } finally {
            fis.close();
        }
    }

    /**
     * Returns true if and only if the Gedcom data says it is for the 5.5 standard.
     * 
     * @return true if and only if the Gedcom data says it is for the 5.5 standard.
     */
    private boolean g55() {
        return gedcom != null && gedcom.header != null && gedcom.header.gedcomVersion != null && SupportedVersion.V5_5.equals(
                gedcom.header.gedcomVersion.versionNumber);
    }

    /**
     * Get a family by their xref, adding them to the gedcom collection of families if needed.
     * 
     * @param xref
     *            the xref of the family
     * @return the family with the specified xref
     */
    private Family getFamily(String xref) {
        Family f = gedcom.families.get(xref);
        if (f == null) {
            f = new Family();
            f.xref = xref;
            gedcom.families.put(xref, f);
        }
        return f;
    }

    /**
     * Get an individual by their xref, adding them to the gedcom collection of individuals if needed.
     * 
     * @param xref
     *            the xref of the individual
     * @return the individual with the specified xref
     */
    private Individual getIndividual(String xref) {
        Individual i;
        i = gedcom.individuals.get(xref);
        if (i == null) {
            i = new Individual();
            i.xref = xref;
            gedcom.individuals.put(xref, i);
        }
        return i;
    }

    /**
     * Get a multimedia item by its xref, adding it to the gedcom collection of multimedia items if needed.
     * 
     * @param xref
     *            the xref of the multimedia item
     * @return the multimedia item with the specified xref
     */
    private Multimedia getMultimedia(String xref) {
        Multimedia m;
        m = gedcom.multimedia.get(xref);
        if (m == null) {
            m = new Multimedia();
            m.xref = xref;
            gedcom.multimedia.put(xref, m);
        }
        return m;
    }

    /**
     * Get a note by its xref, adding it to the gedcom collection of notes if needed.
     * 
     * @param xref
     *            the xref of the note
     * @return the note with the specified xref
     */
    private Note getNote(String xref) {
        Note note;
        note = gedcom.notes.get(xref);
        if (note == null) {
            note = new Note();
            note.xref = xref;
            gedcom.notes.put(xref, note);
        }
        return note;
    }

    /**
     * Get a repository by its xref, adding it to the gedcom collection of repositories if needed.
     * 
     * @param xref
     *            the xref of the repository
     * @return the repository with the specified xref
     */
    private Repository getRepository(String xref) {
        Repository r = gedcom.repositories.get(xref);
        if (r == null) {
            r = new Repository();
            r.xref = xref;
            gedcom.repositories.put(xref, r);
        }
        return r;

    }

    /**
     * Get a source by its xref, adding it to the gedcom collection of sources if needed.
     * 
     * @param xref
     *            the xref of the source
     * @return the source with the specified xref
     */
    private Source getSource(String xref) {
        Source src = gedcom.sources.get(xref);
        if (src == null) {
            src = new Source(xref);
            gedcom.sources.put(src.xref, src);
        }
        return src;
    }

    /**
     * Get a submitter by their xref, adding them to the gedcom collection of submitters if needed.
     * 
     * @param xref
     *            the xref of the submitter
     * @return the submitter with the specified xref
     */
    private Submitter getSubmitter(String xref) {
        Submitter s;
        s = gedcom.submitters.get(xref);
        if (s == null) {
            s = new Submitter();
            s.name = new StringWithCustomTags("UNSPECIFIED");
            s.xref = xref;
            gedcom.submitters.put(xref, s);
        }
        return s;
    }

    /**
     * Load an address node into an address structure
     * 
     * @param st
     *            the string tree node
     * @param address
     *            the address to load into
     */
    private void loadAddress(StringTree st, Address address) {
        if (st.value != null) {
            address.lines.add(st.value);
        }
        for (StringTree ch : st.children) {
            if (Tag.ADDRESS_1.equals(ch.tag)) {
                address.addr1 = new StringWithCustomTags(ch);
            } else if (Tag.ADDRESS_2.equals(ch.tag)) {
                address.addr2 = new StringWithCustomTags(ch);
            } else if (Tag.CITY.equals(ch.tag)) {
                address.city = new StringWithCustomTags(ch);
            } else if (Tag.STATE.equals(ch.tag)) {
                address.stateProvince = new StringWithCustomTags(ch);
            } else if (Tag.POSTAL_CODE.equals(ch.tag)) {
                address.postalCode = new StringWithCustomTags(ch);
            } else if (Tag.COUNTRY.equals(ch.tag)) {
                address.country = new StringWithCustomTags(ch);
            } else if (Tag.CONCATENATION.equals(ch.tag)) {
                if (address.lines.isEmpty()) {
                    address.lines.add(ch.value);
                } else {
                    address.lines.set(address.lines.size() - 1, address.lines.get(address.lines.size() - 1) + ch.value);
                }
            } else if (Tag.CONTINUATION.equals(ch.tag)) {
                address.lines.add(ch.value == null ? "" : ch.value);
            } else {
                unknownTag(ch, address);
            }
        }
    }

    /**
     * Load an association between two individuals from a string tree node
     * 
     * @param st
     *            the node
     * @param associations
     *            the list of associations to load into
     */
    private void loadAssociation(StringTree st, List<Association> associations) {
        Association a = new Association();
        associations.add(a);
        a.associatedEntityXref = st.value;
        for (StringTree ch : st.children) {
            if (Tag.RELATIONSHIP.equals(ch.tag)) {
                a.relationship = new StringWithCustomTags(ch);
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, a.notes);
            } else if (Tag.SOURCE.equals(ch.tag)) {
                loadCitation(ch, a.citations);
            } else if (Tag.TYPE.equals(ch.tag)) {
                a.associatedEntityType = new StringWithCustomTags(ch);
            } else {
                unknownTag(ch, a);
            }
        }

    }

    /**
     * Load a change date structure from a string tree node
     * 
     * @param st
     *            the node
     * @param changeDate
     *            the change date to load into
     */
    private void loadChangeDate(StringTree st, ChangeDate changeDate) {
        for (StringTree ch : st.children) {
            if (Tag.DATE.equals(ch.tag)) {
                changeDate.date = new StringWithCustomTags(ch.value);
                if (!ch.children.isEmpty()) {
                    changeDate.time = new StringWithCustomTags(ch.children.get(0));
                }
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, changeDate.notes);
            } else {
                unknownTag(ch, changeDate);
            }
        }

    }

    /**
     * Load a source citation from a string tree node into a list of citations
     * 
     * @param st
     *            the string tree node
     * @param list
     *            the list of citations to load into
     */
    private void loadCitation(StringTree st, List<AbstractCitation> list) {
        AbstractCitation citation;
        if (referencesAnotherNode(st)) {
            citation = new CitationWithSource();
            loadCitationWithSource(st, citation);
        } else {
            citation = new CitationWithoutSource();
            loadCitationWithoutSource(st, citation);
        }
        list.add(citation);
    }

    /**
     * Load a DATA structure in a source citation from a string tree node
     * 
     * @param st
     *            the node
     * @param d
     *            the CitationData structure
     */
    private void loadCitationData(StringTree st, CitationData d) {
        for (StringTree ch : st.children) {
            if (Tag.DATE.equals(ch.tag)) {
                d.entryDate = new StringWithCustomTags(ch);
            } else if (Tag.TEXT.equals(ch.tag)) {
                List<String> ls = new ArrayList<String>();
                d.sourceText.add(ls);
                loadMultiLinesOfText(ch, ls, d);
            } else {
                unknownTag(ch, d);
            }
        }

    }

    /**
     * Load the non-cross-referenced source citation from a string tree node
     * 
     * @param st
     *            the node
     * @param citation
     *            the citation to load into
     */
    private void loadCitationWithoutSource(StringTree st, AbstractCitation citation) {
        CitationWithoutSource cws = (CitationWithoutSource) citation;
        cws.description.add(st.value);
        for (StringTree ch : st.children) {
            if (Tag.CONTINUATION.equals(ch.tag)) {
                cws.description.add(ch.value == null ? "" : ch.value);
            } else if (Tag.CONCATENATION.equals(ch.tag)) {
                if (cws.description.isEmpty()) {
                    cws.description.add(ch.value);
                } else {
                    // Append to last value in string list
                    cws.description.set(cws.description.size() - 1, cws.description.get(cws.description.size() - 1) + ch.value);
                }
            } else if (Tag.TEXT.equals(ch.tag)) {
                List<String> ls = new ArrayList<String>();
                cws.textFromSource.add(ls);
                loadMultiLinesOfText(ch, ls, cws);
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, cws.notes);
            } else {
                unknownTag(ch, citation);
            }
        }
    }

    /**
     * Load a cross-referenced source citation from a string tree node
     * 
     * @param st
     *            the node
     * @param citation
     *            the citation to load into
     */
    private void loadCitationWithSource(StringTree st, AbstractCitation citation) {
        CitationWithSource cws = (CitationWithSource) citation;
        Source src = null;
        if (referencesAnotherNode(st)) {
            src = getSource(st.value);
        }
        cws.source = src;
        for (StringTree ch : st.children) {
            if (Tag.PAGE.equals(ch.tag)) {
                cws.whereInSource = new StringWithCustomTags(ch);
            } else if (Tag.EVENT.equals(ch.tag)) {
                cws.eventCited = new StringWithCustomTags(ch.value);
                if (ch.children != null) {
                    for (StringTree gc : ch.children) {
                        if (Tag.ROLE.equals(gc.tag)) {
                            cws.roleInEvent = new StringWithCustomTags(gc);
                        } else {
                            unknownTag(gc, cws.eventCited);
                        }
                    }
                }
            } else if (Tag.DATA_FOR_CITATION.equals(ch.tag)) {
                CitationData d = new CitationData();
                cws.data.add(d);
                loadCitationData(ch, d);
            } else if (Tag.QUALITY.equals(ch.tag)) {
                cws.certainty = new StringWithCustomTags(ch);
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, cws.notes);
            } else if (Tag.OBJECT_MULTIMEDIA.equals(ch.tag)) {
                loadMultimediaLink(ch, cws.multimedia);
            } else {
                unknownTag(ch, citation);
            }
        }
    }

    /**
     * Load a corporation structure from a string tree node
     * 
     * @param st
     *            the node
     * @param corporation
     *            the corporation structure to fill
     */
    private void loadCorporation(StringTree st, Corporation corporation) {
        corporation.businessName = st.value;
        for (StringTree ch : st.children) {
            if (Tag.ADDRESS.equals(ch.tag)) {
                corporation.address = new Address();
                loadAddress(ch, corporation.address);
            } else if (Tag.PHONE.equals(ch.tag)) {
                corporation.phoneNumbers.add(new StringWithCustomTags(ch));
            } else if (Tag.WEB_ADDRESS.equals(ch.tag)) {
                corporation.wwwUrls.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but WWW URL was specified for the corporation in the source system on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.FAX.equals(ch.tag)) {
                corporation.faxNumbers.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but fax number was specified for the corporation in the source system on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.EMAIL.equals(ch.tag)) {
                corporation.emails.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but emails was specified for the corporation in the source system on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else {
                unknownTag(ch, corporation);
            }
        }
    }

    /**
     * Load a family structure from a stringtree node, and load it into the gedcom family collection
     * 
     * @param st
     *            the node
     */
    private void loadFamily(StringTree st) {
        Family f = getFamily(st.id);
        for (StringTree ch : st.children) {
            if (Tag.HUSBAND.equals(ch.tag)) {
                f.husband = getIndividual(ch.value);
            } else if (Tag.WIFE.equals(ch.tag)) {
                f.wife = getIndividual(ch.value);
            } else if (Tag.CHILD.equals(ch.tag)) {
                f.children.add(getIndividual(ch.value));
            } else if (Tag.NUM_CHILDREN.equals(ch.tag)) {
                f.numChildren = new StringWithCustomTags(ch);
            } else if (Tag.SOURCE.equals(ch.tag)) {
                loadCitation(ch, f.citations);
            } else if (Tag.OBJECT_MULTIMEDIA.equals(ch.tag)) {
                loadMultimediaLink(ch, f.multimedia);
            } else if (Tag.RECORD_ID_NUMBER.equals(ch.tag)) {
                f.automatedRecordId = new StringWithCustomTags(ch);
            } else if (Tag.CHANGED_DATETIME.equals(ch.tag)) {
                f.changeDate = new ChangeDate();
                loadChangeDate(ch, f.changeDate);
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, f.notes);
            } else if (Tag.RESTRICTION.equals(ch.tag)) {
                f.restrictionNotice = new StringWithCustomTags(ch);
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but restriction notice was specified for family on line " + ch.lineNum
                            + " , which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.REGISTRATION_FILE_NUMBER.equals(ch.tag)) {
                f.recFileNumber = new StringWithCustomTags(ch);
            } else if (FamilyEventType.isValidTag(ch.tag)) {
                loadFamilyEvent(ch, f.events);
            } else if (Tag.SEALING_SPOUSE.equals(ch.tag)) {
                loadLdsSpouseSealing(ch, f.ldsSpouseSealings);
            } else if (Tag.SUBMITTER.equals(ch.tag)) {
                f.submitters.add(getSubmitter(ch.value));
            } else if (Tag.REFERENCE.equals(ch.tag)) {
                UserReference u = new UserReference();
                f.userReferences.add(u);
                loadUserReference(ch, u);
            } else {
                unknownTag(ch, f);
            }
        }

    }

    /**
     * Load a family event from a string tree node into a list of family events
     * 
     * @param st
     *            the node
     * @param events
     *            the list of family events
     */
    private void loadFamilyEvent(StringTree st, List<FamilyEvent> events) {
        FamilyEvent e = new FamilyEvent();
        events.add(e);
        e.type = FamilyEventType.getFromTag(st.tag);
        if ("Y".equals(st.value)) {
            e.yNull = st.value;
            e.description = null;
        } else if (st.value == null || st.value.trim().length() == 0) {
            e.yNull = null;
            e.description = null;
        } else {
            e.yNull = null;
            e.description = new StringWithCustomTags(st.value);
            warnings.add(st.tag + " tag had description rather than [Y|<NULL>] - violates standard");
        }
        for (StringTree ch : st.children) {
            if (Tag.TYPE.equals(ch.tag)) {
                e.subType = new StringWithCustomTags(ch);
            } else if (Tag.DATE.equals(ch.tag)) {
                e.date = new StringWithCustomTags(ch);
            } else if (Tag.PLACE.equals(ch.tag)) {
                e.place = new Place();
                loadPlace(ch, e.place);
            } else if (Tag.OBJECT_MULTIMEDIA.equals(ch.tag)) {
                loadMultimediaLink(ch, e.multimedia);
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, e.notes);
            } else if (Tag.SOURCE.equals(ch.tag)) {
                loadCitation(ch, e.citations);
            } else if (Tag.RESTRICTION.equals(ch.tag)) {
                e.restrictionNotice = new StringWithCustomTags(ch);
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but restriction notice was specified for family event on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.RELIGION.equals(ch.tag)) {
                e.religiousAffiliation = new StringWithCustomTags(ch);
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but religious affiliation was specified for family event on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.AGE.equals(ch.tag)) {
                e.age = new StringWithCustomTags(ch);
            } else if (Tag.CAUSE.equals(ch.tag)) {
                e.cause = new StringWithCustomTags(ch);
            } else if (Tag.ADDRESS.equals(ch.tag)) {
                e.address = new Address();
                loadAddress(ch, e.address);
            } else if (Tag.AGENCY.equals(ch.tag)) {
                e.respAgency = new StringWithCustomTags(ch);
            } else if (Tag.PHONE.equals(ch.tag)) {
                e.phoneNumbers.add(new StringWithCustomTags(ch));
            } else if (Tag.WEB_ADDRESS.equals(ch.tag)) {
                e.wwwUrls.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but WWW URL was specified for " + e.type + " family event on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.FAX.equals(ch.tag)) {
                e.faxNumbers.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but fax number was specified for " + e.type + " family event on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.EMAIL.equals(ch.tag)) {
                e.emails.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but email was specified for " + e.type + " family event on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.HUSBAND.equals(ch.tag)) {
                e.husbandAge = new StringWithCustomTags(ch.children.get(0));
            } else if (Tag.WIFE.equals(ch.tag)) {
                e.wifeAge = new StringWithCustomTags(ch.children.get(0));
            } else if (Tag.CONCATENATION.equals(ch.tag)) {
                if (e.description == null) {
                    e.description = new StringWithCustomTags(ch);
                } else {
                    e.description.value += ch.value;
                }
            } else if (Tag.CONTINUATION.equals(ch.tag)) {
                if (e.description == null) {
                    e.description = new StringWithCustomTags(ch.value == null ? "" : ch.value);
                } else {
                    e.description.value += "\n" + ch.value;
                }
            } else {
                unknownTag(ch, e);
            }
        }

    }

    /**
     * Load a reference to a family where this individual was a child, from a string tree node
     * 
     * @param st
     *            the string tree node
     * @param familiesWhereChild
     *            the list of families where the individual was a child
     */
    private void loadFamilyWhereChild(StringTree st, List<FamilyChild> familiesWhereChild) {
        Family f = getFamily(st.value);
        FamilyChild fc = new FamilyChild();
        familiesWhereChild.add(fc);
        fc.family = f;
        for (StringTree ch : st.children) {
            if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, fc.notes);
            } else if (Tag.PEDIGREE.equals(ch.tag)) {
                fc.pedigree = new StringWithCustomTags(ch);
            } else if (Tag.ADOPTION.equals(ch.tag)) {
                fc.adoptedBy = AdoptedByWhichParent.valueOf(ch.value);
            } else if (Tag.STATUS.equals(ch.tag)) {
                fc.status = new StringWithCustomTags(ch);
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but status was specified for child-to-family link on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else {
                unknownTag(ch, fc);
            }
        }

    }

    /**
     * Load a reference to a family where this individual was a spouse, from a string tree node
     * 
     * @param st
     *            the string tree node
     * @param familiesWhereSpouse
     *            the list of families where the individual was a child
     */
    private void loadFamilyWhereSpouse(StringTree st, List<FamilySpouse> familiesWhereSpouse) {
        Family f = getFamily(st.value);
        FamilySpouse fs = new FamilySpouse();
        fs.family = f;
        familiesWhereSpouse.add(fs);
        for (StringTree ch : st.children) {
            if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, fs.notes);
            } else {
                unknownTag(ch, fs);
            }
        }
    }

    /**
     * Load a single 5.5-style file reference
     * 
     * @param m
     *            The multimedia object to contain the new file reference
     * @param children
     *            the sub-tags of the OBJE tag
     */
    private void loadFileReference55(Multimedia m, List<StringTree> children) {
        FileReference currentFileRef = new FileReference();
        m.fileReferences.add(currentFileRef);
        for (StringTree ch : children) {
            if (Tag.FORM.equals(ch.tag)) {
                currentFileRef.format = new StringWithCustomTags(ch);
            } else if (Tag.TITLE.equals(ch.tag)) {
                m.embeddedTitle = new StringWithCustomTags(ch);
            } else if (Tag.FILE.equals(ch.tag)) {
                currentFileRef.referenceToFile = new StringWithCustomTags(ch);
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, m.notes);
            } else {
                unknownTag(ch, m);
            }
        }

    }

    /**
     * Load all the file references in the current OBJE tag
     * 
     * @param m
     *            the multimedia object being with the reference
     * @param st
     *            the OBJE node being parsed
     */
    private void loadFileReferences(Multimedia m, StringTree st) {
        int fileTagCount = 0;
        int formTagCount = 0;

        for (StringTree ch : st.children) {
            /*
             * Count up the number of files referenced for this object - GEDCOM 5.5.1 allows multiple, 5.5 only allows 1
             */
            if (Tag.FILE.equals(ch.tag)) {
                fileTagCount++;
            }
            /*
             * Count the number of formats referenced per file - GEDCOM 5.5.1 has them as children of FILEs (so should
             * be zero), 5.5 pairs them with the single FILE tag (so should be one)
             */
            if (Tag.FORM.equals(ch.tag)) {
                formTagCount++;
            }
        }
        if (g55()) {
            if (fileTagCount > 1) {
                warnings.add("GEDCOM version is 5.5, but multiple files referenced in multimedia reference on line " + st.lineNum
                        + ", which is only allowed in 5.5.1. "
                        + "Data will be loaded, but cannot be written back out unless the GEDCOM version is changed to 5.5.1");
            }
            if (formTagCount == 0) {
                warnings.add("GEDCOM version is 5.5, but there is not a FORM tag in the multimedia link on line " + st.lineNum
                        + ", a scenario which is only allowed in 5.5.1. "
                        + "Data will be loaded, but cannot be written back out unless the GEDCOM version is changed to 5.5.1");
            }
        }
        if (formTagCount > 1) {
            errors.add("Multiple FORM tags were found for a multimedia file reference at line " + st.lineNum
                    + " - this is not compliant with any GEDCOM standard - data not loaded");
            return;
        }

        if (fileTagCount > 1 || formTagCount < fileTagCount) {
            loadFileReferences551(m, st.children);
        } else {
            loadFileReference55(m, st.children);
        }
    }

    /**
     * Load one or more 5.5.1-style references
     * 
     * @param m
     *            the multimedia object to which we are adding the file references
     * 
     * @param children
     *            the sub-tags of the OBJE tag
     */
    private void loadFileReferences551(Multimedia m, List<StringTree> children) {
        for (StringTree ch : children) {
            if (Tag.FILE.equals(ch.tag)) {
                FileReference fr = new FileReference();
                m.fileReferences.add(fr);
                fr.referenceToFile = new StringWithCustomTags(ch);
                if (ch.children.size() != 1) {
                    errors.add("Missing or multiple children nodes found under FILE node - GEDCOM 5.5.1 standard requires exactly 1 FORM node");
                }
                for (StringTree gch : ch.children) {
                    if (Tag.FORM.equals(gch.tag)) {
                        fr.format = new StringWithCustomTags(gch.value);
                        for (StringTree ggch : ch.children) {
                            if (Tag.MEDIA.equals(ggch.tag)) {
                                fr.mediaType = new StringWithCustomTags(ggch);
                            } else {
                                unknownTag(ggch, fr);
                            }
                        }
                    } else {
                        unknownTag(gch, fr);
                    }
                }
            } else if (Tag.TITLE.equals(ch.tag)) {
                for (FileReference fr : m.fileReferences) {
                    fr.title = new StringWithCustomTags(ch.tag.intern());
                }
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, m.notes);
                if (!g55()) {
                    warnings.add("Gedcom version was 5.5.1, but a NOTE was found on a multimedia link on line " + ch.lineNum
                            + ", which is no longer supported. "
                            + "Data will be loaded, but cannot be written back out unless the GEDCOM version is changed to 5.5");
                }
            } else {
                unknownTag(ch, m);
            }
        }
    }

    /**
     * Load a gedcom version from the string tree node
     * 
     * @param st
     *            the node
     * @param gedcomVersion
     *            the gedcom version structure to load
     */
    private void loadGedcomVersion(StringTree st, GedcomVersion gedcomVersion) {
        for (StringTree ch : st.children) {
            if (Tag.VERSION.equals(ch.tag)) {
                SupportedVersion vn = null;
                try {
                    vn = SupportedVersion.forString(ch.value);
                } catch (UnsupportedVersionException e) {
                    errors.add(e.getMessage());
                }
                gedcomVersion.versionNumber = vn;
            } else if (Tag.FORM.equals(ch.tag)) {
                gedcomVersion.gedcomForm = new StringWithCustomTags(ch);
            } else {
                unknownTag(ch, gedcomVersion);
            }
        }
    }

    /**
     * Load a gedcom header from a string tree node
     * 
     * @param st
     *            the node
     */
    private void loadHeader(StringTree st) {
        Header header = new Header();
        gedcom.header = header;
        for (StringTree ch : st.children) {
            if (Tag.SOURCE.equals(ch.tag)) {
                header.sourceSystem = new SourceSystem();
                loadSourceSystem(ch, header.sourceSystem);
            } else if (Tag.DESTINATION.equals(ch.tag)) {
                header.destinationSystem = new StringWithCustomTags(ch);
            } else if (Tag.DATE.equals(ch.tag)) {
                header.date = new StringWithCustomTags(ch);
                // one optional time subitem is the only possibility here
                if (!ch.children.isEmpty()) {
                    header.time = new StringWithCustomTags(ch.children.get(0));
                }
            } else if (Tag.CHARACTER_SET.equals(ch.tag)) {
                header.characterSet = new CharacterSet();
                header.characterSet.characterSetName = new StringWithCustomTags(ch);
                // one optional version subitem is the only possibility here
                if (!ch.children.isEmpty()) {
                    header.characterSet.versionNum = new StringWithCustomTags(ch.children.get(0));
                }
            } else if (Tag.SUBMITTER.equals(ch.tag)) {
                header.submitter = getSubmitter(ch.value);
            } else if (Tag.FILE.equals(ch.tag)) {
                header.fileName = new StringWithCustomTags(ch);
            } else if (Tag.GEDCOM_VERSION.equals(ch.tag)) {
                header.gedcomVersion = new GedcomVersion();
                loadGedcomVersion(ch, header.gedcomVersion);
            } else if (Tag.COPYRIGHT.equals(ch.tag)) {
                loadMultiLinesOfText(ch, header.copyrightData, header);
                if (g55() && header.copyrightData.size() > 1) {
                    warnings.add("GEDCOM version is 5.5, but multiple lines of copyright data were specified, which is only allowed in GEDCOM 5.5.1. "
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.SUBMISSION.equals(ch.tag)) {
                if (header.submission == null) {
                    /*
                     * There can only be one SUBMISSION record per GEDCOM, and it's found at the root level, but the
                     * HEAD structure has a cross-reference to that root-level structure, so we're setting it here (if
                     * it hasn't already been loaded, which it probably isn't yet)
                     */
                    header.submission = gedcom.submission;
                }
            } else if (Tag.LANGUAGE.equals(ch.tag)) {
                header.language = new StringWithCustomTags(ch);
            } else if (Tag.PLACE.equals(ch.tag)) {
                header.placeHierarchy = new StringWithCustomTags(ch.children.get(0));
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadMultiLinesOfText(ch, header.notes, header);
            } else {
                unknownTag(ch, header);
            }
        }
    }

    /**
     * Load the header source data structure from a string tree node
     * 
     * @param st
     *            the node
     * @param sourceData
     *            the header source data
     */
    private void loadHeaderSourceData(StringTree st, HeaderSourceData sourceData) {
        sourceData.name = st.value;
        for (StringTree ch : st.children) {
            if (Tag.DATE.equals(ch.tag)) {
                sourceData.publishDate = new StringWithCustomTags(ch);
            } else if (Tag.COPYRIGHT.equals(ch.tag)) {
                sourceData.copyright = new StringWithCustomTags(ch);
            } else {
                unknownTag(ch, sourceData);
            }
        }

    }

    /**
     * Load an individual from a string tree node
     * 
     * @param st
     *            the node
     */
    private void loadIndividual(StringTree st) {
        Individual i = getIndividual(st.id);
        for (StringTree ch : st.children) {
            if (Tag.NAME.equals(ch.tag)) {
                PersonalName pn = new PersonalName();
                i.names.add(pn);
                loadPersonalName(ch, pn);
            } else if (Tag.SEX.equals(ch.tag)) {
                i.sex = new StringWithCustomTags(ch);
            } else if (Tag.ADDRESS.equals(ch.tag)) {
                i.address = new Address();
                loadAddress(ch, i.address);
            } else if (Tag.PHONE.equals(ch.tag)) {
                i.phoneNumbers.add(new StringWithCustomTags(ch));
            } else if (Tag.WEB_ADDRESS.equals(ch.tag)) {
                i.wwwUrls.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but WWW URL was specified for individual " + i.xref + " on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.FAX.equals(ch.tag)) {
                i.faxNumbers.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but fax was specified for individual " + i.xref + "on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.EMAIL.equals(ch.tag)) {
                i.emails.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but email was specified for individual " + i.xref + " on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (IndividualEventType.isValidTag(ch.tag)) {
                loadIndividualEvent(ch, i.events);
            } else if (IndividualAttributeType.isValidTag(ch.tag)) {
                loadIndividualAttribute(ch, i.attributes);
            } else if (LdsIndividualOrdinanceType.isValidTag(ch.tag)) {
                loadLdsIndividualOrdinance(ch, i.ldsIndividualOrdinances);
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, i.notes);
            } else if (Tag.CHANGED_DATETIME.equals(ch.tag)) {
                i.changeDate = new ChangeDate();
                loadChangeDate(ch, i.changeDate);
            } else if (Tag.RECORD_ID_NUMBER.equals(ch.tag)) {
                i.recIdNumber = new StringWithCustomTags(ch);
            } else if (Tag.REGISTRATION_FILE_NUMBER.equals(ch.tag)) {
                i.permanentRecFileNumber = new StringWithCustomTags(ch);
            } else if (Tag.OBJECT_MULTIMEDIA.equals(ch.tag)) {
                loadMultimediaLink(ch, i.multimedia);
            } else if (Tag.RESTRICTION.equals(ch.tag)) {
                i.restrictionNotice = new StringWithCustomTags(ch);
            } else if (Tag.SOURCE.equals(ch.tag)) {
                loadCitation(ch, i.citations);
            } else if (Tag.ALIAS.equals(ch.tag)) {
                i.aliases.add(new StringWithCustomTags(ch));
            } else if (Tag.FAMILY_WHERE_SPOUSE.equals(ch.tag)) {
                loadFamilyWhereSpouse(ch, i.familiesWhereSpouse);
            } else if (Tag.FAMILY_WHERE_CHILD.equals(ch.tag)) {
                loadFamilyWhereChild(ch, i.familiesWhereChild);
            } else if (Tag.ASSOCIATION.equals(ch.tag)) {
                loadAssociation(ch, i.associations);
            } else if (Tag.ANCESTOR_INTEREST.equals(ch.tag)) {
                i.ancestorInterest.add(getSubmitter(ch.value));
            } else if (Tag.DESCENDANT_INTEREST.equals(ch.tag)) {
                i.descendantInterest.add(getSubmitter(ch.value));
            } else if (Tag.ANCESTRAL_FILE_NUMBER.equals(ch.tag)) {
                i.ancestralFileNumber = new StringWithCustomTags(ch);
            } else if (Tag.REFERENCE.equals(ch.tag)) {
                UserReference u = new UserReference();
                i.userReferences.add(u);
                loadUserReference(ch, u);
            } else if (Tag.SUBMITTER.equals(ch.tag)) {
                i.submitters.add(getSubmitter(ch.value));
            } else {
                unknownTag(ch, i);
            }
        }

    }

    /**
     * Load an attribute about an individual from a string tree node
     * 
     * @param st
     *            the node
     * @param attributes
     *            the list of individual attributes
     */
    private void loadIndividualAttribute(StringTree st, List<IndividualAttribute> attributes) {
        IndividualAttribute a = new IndividualAttribute();
        attributes.add(a);
        a.type = IndividualAttributeType.getFromTag(st.tag);
        if (IndividualAttributeType.FACT.equals(a.type) && g55()) {
            warnings.add("FACT tag specified on a GEDCOM 5.5 file at line " + st.lineNum + ", but FACT was not added until 5.5.1."
                    + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
        }
        a.description = new StringWithCustomTags(st.value);
        for (StringTree ch : st.children) {
            if (Tag.TYPE.equals(ch.tag)) {
                a.subType = new StringWithCustomTags(ch);
            } else if (Tag.DATE.equals(ch.tag)) {
                a.date = new StringWithCustomTags(ch);
            } else if (Tag.PLACE.equals(ch.tag)) {
                a.place = new Place();
                loadPlace(ch, a.place);
            } else if (Tag.AGE.equals(ch.tag)) {
                a.age = new StringWithCustomTags(ch);
            } else if (Tag.CAUSE.equals(ch.tag)) {
                a.cause = new StringWithCustomTags(ch);
            } else if (Tag.SOURCE.equals(ch.tag)) {
                loadCitation(ch, a.citations);
            } else if (Tag.AGENCY.equals(ch.tag)) {
                a.respAgency = new StringWithCustomTags(ch);
            } else if (Tag.PHONE.equals(ch.tag)) {
                a.phoneNumbers.add(new StringWithCustomTags(ch));
            } else if (Tag.WEB_ADDRESS.equals(ch.tag)) {
                a.wwwUrls.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but WWW URL was specified for " + a.type + " attribute on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.FAX.equals(ch.tag)) {
                a.faxNumbers.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but fax was specified for " + a.type + " attribute on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.EMAIL.equals(ch.tag)) {
                a.emails.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but email was specified for " + a.type + " attribute on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.ADDRESS.equals(ch.tag)) {
                a.address = new Address();
                loadAddress(ch, a.address);
            } else if (Tag.OBJECT_MULTIMEDIA.equals(ch.tag)) {
                loadMultimediaLink(ch, a.multimedia);
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, a.notes);
            } else if (Tag.CONCATENATION.equals(ch.tag)) {
                if (a.description == null) {
                    a.description = new StringWithCustomTags(ch);
                } else {
                    a.description.value += ch.value;
                }
            } else {
                unknownTag(ch, a);
            }
        }
    }

    /**
     * Load an event about an individual from a string tree node
     * 
     * @param st
     *            the node
     * @param events
     *            the list of events about an individual
     */
    private void loadIndividualEvent(StringTree st, List<IndividualEvent> events) {
        IndividualEvent e = new IndividualEvent();
        events.add(e);
        e.type = IndividualEventType.getFromTag(st.tag);
        if ("Y".equals(st.value)) {
            e.yNull = st.value;
            e.description = null;
        } else if (st.value == null || st.value.trim().length() == 0) {
            e.yNull = null;
            e.description = null;
        } else {
            e.yNull = null;
            e.description = new StringWithCustomTags(st.value);
            warnings.add(st.tag + " tag had description rather than [Y|<NULL>] - violates standard");
        }
        for (StringTree ch : st.children) {
            if (Tag.TYPE.equals(ch.tag)) {
                e.subType = new StringWithCustomTags(ch);
            } else if (Tag.DATE.equals(ch.tag)) {
                e.date = new StringWithCustomTags(ch);
            } else if (Tag.PLACE.equals(ch.tag)) {
                e.place = new Place();
                loadPlace(ch, e.place);
            } else if (Tag.OBJECT_MULTIMEDIA.equals(ch.tag)) {
                loadMultimediaLink(ch, e.multimedia);
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, e.notes);
            } else if (Tag.SOURCE.equals(ch.tag)) {
                loadCitation(ch, e.citations);
            } else if (Tag.AGE.equals(ch.tag)) {
                e.age = new StringWithCustomTags(ch);
            } else if (Tag.CAUSE.equals(ch.tag)) {
                e.cause = new StringWithCustomTags(ch);
            } else if (Tag.ADDRESS.equals(ch.tag)) {
                e.address = new Address();
                loadAddress(ch, e.address);
            } else if (Tag.AGENCY.equals(ch.tag)) {
                e.respAgency = new StringWithCustomTags(ch);
            } else if (Tag.RESTRICTION.equals(ch.tag)) {
                e.restrictionNotice = new StringWithCustomTags(ch);
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but restriction notice was specified for individual event on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.RELIGION.equals(ch.tag)) {
                e.religiousAffiliation = new StringWithCustomTags(ch);
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but religious affiliation was specified for individual event on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.PHONE.equals(ch.tag)) {
                e.phoneNumbers.add(new StringWithCustomTags(ch));
            } else if (Tag.WEB_ADDRESS.equals(ch.tag)) {
                e.wwwUrls.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but WWW URL was specified on " + e.type + " event on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.FAX.equals(ch.tag)) {
                e.faxNumbers.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but fax was specified on " + e.type + " event on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.EMAIL.equals(ch.tag)) {
                e.emails.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but email was specified on " + e.type + " event on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.CONCATENATION.equals(ch.tag)) {
                if (e.description == null) {
                    e.description = new StringWithCustomTags(ch);
                } else {
                    e.description.value += ch.value;
                }
            } else if (Tag.CONTINUATION.equals(ch.tag)) {
                if (e.description == null) {
                    e.description = new StringWithCustomTags(ch.value == null ? "" : ch.value);
                } else {
                    e.description.value += "\n" + ch.value;
                }
            } else if (Tag.FAMILY_WHERE_CHILD.equals(ch.tag)) {
                List<FamilyChild> families = new ArrayList<FamilyChild>();
                loadFamilyWhereChild(ch, families);
                if (!families.isEmpty()) {
                    e.family = families.get(0);
                }
            } else {
                unknownTag(ch, e);
            }
        }

    }

    /**
     * Load an LDS individual ordinance from a string tree node
     * 
     * @param st
     *            the node
     * @param ldsIndividualOrdinances
     *            the list of LDS ordinances
     */
    private void loadLdsIndividualOrdinance(StringTree st, List<LdsIndividualOrdinance> ldsIndividualOrdinances) {
        LdsIndividualOrdinance o = new LdsIndividualOrdinance();
        ldsIndividualOrdinances.add(o);
        o.type = LdsIndividualOrdinanceType.getFromTag(st.tag);
        o.yNull = st.value;
        for (StringTree ch : st.children) {
            if (Tag.DATE.equals(ch.tag)) {
                o.date = new StringWithCustomTags(ch);
            } else if (Tag.PLACE.equals(ch.tag)) {
                o.place = new StringWithCustomTags(ch);
            } else if (Tag.STATUS.equals(ch.tag)) {
                o.status = new StringWithCustomTags(ch);
            } else if (Tag.TEMPLE.equals(ch.tag)) {
                o.temple = new StringWithCustomTags(ch);
            } else if (Tag.SOURCE.equals(ch.tag)) {
                loadCitation(ch, o.citations);
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, o.notes);
            } else if (Tag.FAMILY_WHERE_CHILD.equals(ch.tag)) {
                List<FamilyChild> families = new ArrayList<FamilyChild>();
                loadFamilyWhereChild(ch, families);
                if (!families.isEmpty()) {
                    o.familyWhereChild = families.get(0);
                }
            } else {
                unknownTag(ch, o);
            }
        }
    }

    /**
     * Load an LDS Spouse Sealing from a string tree node
     * 
     * @param st
     *            the string tree node
     * @param ldsSpouseSealings
     *            the list of LDS spouse sealings on the family
     */
    private void loadLdsSpouseSealing(StringTree st, List<LdsSpouseSealing> ldsSpouseSealings) {
        LdsSpouseSealing o = new LdsSpouseSealing();
        ldsSpouseSealings.add(o);
        for (StringTree ch : st.children) {
            if (Tag.DATE.equals(ch.tag)) {
                o.date = new StringWithCustomTags(ch);
            } else if (Tag.PLACE.equals(ch.tag)) {
                o.place = new StringWithCustomTags(ch);
            } else if (Tag.STATUS.equals(ch.tag)) {
                o.status = new StringWithCustomTags(ch);
            } else if (Tag.TEMPLE.equals(ch.tag)) {
                o.temple = new StringWithCustomTags(ch);
            } else if (Tag.SOURCE.equals(ch.tag)) {
                loadCitation(ch, o.citations);
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, o.notes);
            } else {
                unknownTag(ch, o);
            }

        }

    }

    /**
     * Load multiple (continued) lines of text from a string tree node
     * 
     * @param st
     *            the node
     * @param listOfString
     *            the list of string to load into
     * @param element
     *            the parent element to which the <code>listOfString</code> belongs
     */
    private void loadMultiLinesOfText(StringTree st, List<String> listOfString, AbstractElement element) {
        if (st.value != null) {
            listOfString.add(st.value);
        }
        for (StringTree ch : st.children) {
            if (Tag.CONTINUATION.equals(ch.tag)) {
                if (ch.value == null) {
                    listOfString.add("");
                } else {
                    listOfString.add(ch.value);
                }
            } else if (Tag.CONCATENATION.equals(ch.tag)) {
                // If there's no value to concatenate, ignore it
                if (ch.value != null) {
                    if (listOfString.isEmpty()) {
                        listOfString.add(ch.value);
                    } else {
                        listOfString.set(listOfString.size() - 1, listOfString.get(listOfString.size() - 1) + ch.value);
                    }
                }
            } else {
                unknownTag(ch, element);
            }
        }
    }

    /**
     * Load a multimedia reference from a string tree node. This corresponds to the MULTIMEDIA_LINK structure in the
     * GEDCOM specs.
     * 
     * @param st
     *            the string tree node
     * @param multimedia
     *            the list of multimedia on the current object
     */
    private void loadMultimediaLink(StringTree st, List<Multimedia> multimedia) {
        Multimedia m;
        if (referencesAnotherNode(st)) {
            m = getMultimedia(st.value);
        } else {
            m = new Multimedia();
            loadFileReferences(m, st);
        }
        multimedia.add(m);
    }

    /**
     * Determine which style is being used here - GEDCOM 5.5 or 5.5.1 - and load appropriately. Warn if the structure is
     * inconsistent with the specified format.
     * 
     * @param st
     *            the OBJE node being loaded
     */
    private void loadMultimediaRecord(StringTree st) {
        int fileTagCount = 0;
        for (StringTree ch : st.children) {
            if (Tag.FILE.equals(ch.tag)) {
                fileTagCount++;
            }
        }
        if (fileTagCount > 0) {
            if (g55()) {
                warnings.add("GEDCOM version was 5.5, but a 5.5.1-style multimedia record was found at line " + st.lineNum + ". "
                        + "Data will be loaded, but might have problems being written until the version is for the data is changed to 5.5.1");
            }
            loadMultimediaRecord551(st);
        } else {
            if (!g55()) {
                warnings.add("GEDCOM version is 5.5.1, but a 5.5-style multimedia record was found at line " + st.lineNum + ". "
                        + "Data will be loaded, but might have problems being written until the version is for the data is changed to 5.5.1");
            }
            loadMultimediaRecord55(st);
        }
    }

    /**
     * Load a GEDCOM 5.5-style multimedia record (that could be referenced from another object) from a string tree node.
     * This corresponds to the MULTIMEDIA_RECORD structure in the GEDCOM 5.5 spec.
     * 
     * @param st
     *            the OBJE node being loaded
     */
    private void loadMultimediaRecord55(StringTree st) {
        Multimedia m = getMultimedia(st.id);
        for (StringTree ch : st.children) {
            if (Tag.FORM.equals(ch.tag)) {
                m.embeddedMediaFormat = new StringWithCustomTags(ch);
            } else if (Tag.TITLE.equals(ch.tag)) {
                m.embeddedTitle = new StringWithCustomTags(ch);
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, m.notes);
            } else if (Tag.SOURCE.equals(ch.tag)) {
                loadCitation(ch, m.citations);
            } else if (Tag.BLOB.equals(ch.tag)) {
                loadMultiLinesOfText(ch, m.blob, m);
                if (!g55()) {
                    warnings.add("GEDCOM version is 5.5.1, but a BLOB tag was found at line " + ch.lineNum + ". "
                            + "Data will be loaded but will not be writeable unless GEDCOM version is changed to 5.5.1");
                }
            } else if (Tag.OBJECT_MULTIMEDIA.equals(ch.tag)) {
                List<Multimedia> continuedObjects = new ArrayList<Multimedia>();
                loadMultimediaLink(ch, continuedObjects);
                m.continuedObject = continuedObjects.get(0);
                if (!g55()) {
                    warnings.add("GEDCOM version is 5.5.1, but a chained OBJE tag was found at line " + ch.lineNum + ". "
                            + "Data will be loaded but will not be writeable unless GEDCOM version is changed to 5.5.1");
                }
            } else if (Tag.REFERENCE.equals(ch.tag)) {
                UserReference u = new UserReference();
                m.userReferences.add(u);
                loadUserReference(ch, u);
            } else if (Tag.RECORD_ID_NUMBER.equals(ch.tag)) {
                m.recIdNumber = new StringWithCustomTags(ch);
            } else if (Tag.CHANGED_DATETIME.equals(ch.tag)) {
                m.changeDate = new ChangeDate();
                loadChangeDate(ch, m.changeDate);
            } else {
                unknownTag(ch, m);
            }
        }

    }

    /**
     * Load a GEDCOM 5.5.1-style multimedia record (that could be referenced from another object) from a string tree
     * node. This corresponds to the MULTIMEDIA_RECORD structure in the GEDCOM 5.5.1 spec.
     * 
     * @param st
     *            the OBJE node being loaded
     */
    private void loadMultimediaRecord551(StringTree st) {
        Multimedia m = getMultimedia(st.id);
        for (StringTree ch : st.children) {
            if (Tag.FILE.equals(ch.tag)) {
                FileReference fr = new FileReference();
                m.fileReferences.add(fr);
                fr.referenceToFile = new StringWithCustomTags(ch);
                for (StringTree gch : ch.children) {
                    if (Tag.FORM.equals(gch.tag)) {
                        fr.format = new StringWithCustomTags(gch.value);
                        if (gch.children.size() == 1) {
                            StringTree ggch = gch.children.get(0);
                            if (Tag.TYPE.equals(ggch.tag)) {
                                fr.mediaType = new StringWithCustomTags(ggch);
                            } else {
                                unknownTag(ggch, fr);
                            }
                        }
                    } else if (Tag.TITLE.equals(gch.tag)) {
                        fr.title = new StringWithCustomTags(gch);
                    } else {
                        unknownTag(gch, fr);
                    }
                }
                if (fr.format == null) {
                    errors.add("FORM tag not found under FILE reference on line " + st.lineNum);
                }
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, m.notes);
            } else if (Tag.SOURCE.equals(ch.tag)) {
                loadCitation(ch, m.citations);
            } else if (Tag.REFERENCE.equals(ch.tag)) {
                UserReference u = new UserReference();
                m.userReferences.add(u);
                loadUserReference(ch, u);
            } else if (Tag.RECORD_ID_NUMBER.equals(ch.tag)) {
                m.recIdNumber = new StringWithCustomTags(ch);
            } else if (Tag.CHANGED_DATETIME.equals(ch.tag)) {
                m.changeDate = new ChangeDate();
                loadChangeDate(ch, m.changeDate);
            } else {
                unknownTag(ch, m);
            }

        }

    }

    /**
     * Load a note from a string tree node into a list of notes
     * 
     * @param st
     *            the node
     * @param notes
     *            the list of notes to add the note to as it is parsed.
     */
    private void loadNote(StringTree st, List<Note> notes) {
        Note note;
        if (st.id == null && referencesAnotherNode(st)) {
            note = getNote(st.value);
            notes.add(note);
            return;
        } else if (st.id == null) {
            note = new Note();
            notes.add(note);
        } else {
            if (referencesAnotherNode(st)) {
                warnings.add("NOTE line has both an XREF_ID (" + st.id + ") and SUBMITTER_TEXT (" + st.value
                        + ") value between @ signs - treating SUBMITTER_TEXT as string, not a cross-reference");
            }
            note = getNote(st.id);
        }
        note.lines.add(st.value);
        for (StringTree ch : st.children) {
            if (Tag.CONCATENATION.equals(ch.tag)) {
                if (note.lines.isEmpty()) {
                    note.lines.add(ch.value);
                } else {
                    String lastNote = note.lines.get(note.lines.size() - 1);
                    if (lastNote == null || lastNote.length() == 0) {
                        note.lines.set(note.lines.size() - 1, ch.value);
                    } else {
                        note.lines.set(note.lines.size() - 1, lastNote + ch.value);
                    }
                }
            } else if (Tag.CONTINUATION.equals(ch.tag)) {
                note.lines.add(ch.value == null ? "" : ch.value);
            } else if (Tag.SOURCE.equals(ch.tag)) {
                loadCitation(ch, note.citations);
            } else if (Tag.REFERENCE.equals(ch.tag)) {
                UserReference u = new UserReference();
                note.userReferences.add(u);
                loadUserReference(ch, u);
            } else if (Tag.RECORD_ID_NUMBER.equals(ch.tag)) {
                note.recIdNumber = new StringWithCustomTags(ch);
            } else if (Tag.CHANGED_DATETIME.equals(ch.tag)) {
                note.changeDate = new ChangeDate();
                loadChangeDate(ch, note.changeDate);
            } else {
                unknownTag(ch, note);
            }
        }
    }

    /**
     * Load a personal name structure from a string tree node
     * 
     * @param st
     *            the node
     * @param pn
     *            the personal name structure to fill in
     */
    private void loadPersonalName(StringTree st, PersonalName pn) {
        pn.basic = st.value;
        for (StringTree ch : st.children) {
            if (Tag.NAME_PREFIX.equals(ch.tag)) {
                pn.prefix = new StringWithCustomTags(ch);
            } else if (Tag.GIVEN_NAME.equals(ch.tag)) {
                pn.givenName = new StringWithCustomTags(ch);
            } else if (Tag.NICKNAME.equals(ch.tag)) {
                pn.nickname = new StringWithCustomTags(ch);
            } else if (Tag.SURNAME_PREFIX.equals(ch.tag)) {
                pn.surnamePrefix = new StringWithCustomTags(ch);
            } else if (Tag.SURNAME.equals(ch.tag)) {
                pn.surname = new StringWithCustomTags(ch);
            } else if (Tag.NAME_SUFFIX.equals(ch.tag)) {
                pn.suffix = new StringWithCustomTags(ch);
            } else if (Tag.SOURCE.equals(ch.tag)) {
                loadCitation(ch, pn.citations);
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, pn.notes);
            } else if (Tag.ROMANIZED.equals(ch.tag)) {
                PersonalNameVariation pnv = new PersonalNameVariation();
                pn.romanized.add(pnv);
                loadPersonalNameVariation(ch, pnv);
            } else if (Tag.PHONETIC.equals(ch.tag)) {
                PersonalNameVariation pnv = new PersonalNameVariation();
                pn.phonetic.add(pnv);
                loadPersonalNameVariation(ch, pnv);
            } else {
                unknownTag(ch, pn);
            }
        }

    }

    /**
     * Load a personal name variation (romanization or phonetic version) from a string tree node
     * 
     * @param st
     *            the string tree node to load from
     * @param pnv
     *            the personal name variation to fill in
     */
    private void loadPersonalNameVariation(StringTree st, PersonalNameVariation pnv) {
        pnv.variation = st.value;
        for (StringTree ch : st.children) {
            if (Tag.NAME_PREFIX.equals(ch.tag)) {
                pnv.prefix = new StringWithCustomTags(ch);
            } else if (Tag.GIVEN_NAME.equals(ch.tag)) {
                pnv.givenName = new StringWithCustomTags(ch);
            } else if (Tag.NICKNAME.equals(ch.tag)) {
                pnv.nickname = new StringWithCustomTags(ch);
            } else if (Tag.SURNAME_PREFIX.equals(ch.tag)) {
                pnv.surnamePrefix = new StringWithCustomTags(ch);
            } else if (Tag.SURNAME.equals(ch.tag)) {
                pnv.surname = new StringWithCustomTags(ch);
            } else if (Tag.NAME_SUFFIX.equals(ch.tag)) {
                pnv.suffix = new StringWithCustomTags(ch);
            } else if (Tag.SOURCE.equals(ch.tag)) {
                loadCitation(ch, pnv.citations);
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, pnv.notes);
            } else if (Tag.TYPE.equals(ch.tag)) {
                pnv.variationType = new StringWithCustomTags(ch);
            } else {
                unknownTag(ch, pnv);
            }
        }
    }

    /**
     * Load a place structure from a string tree node
     * 
     * @param st
     *            the node
     * @param place
     *            the place structure to fill in
     */
    private void loadPlace(StringTree st, Place place) {
        place.placeName = st.value;
        for (StringTree ch : st.children) {
            if (Tag.FORM.equals(ch.tag)) {
                place.placeFormat = new StringWithCustomTags(ch);
            } else if (Tag.SOURCE.equals(ch.tag)) {
                loadCitation(ch, place.citations);
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, place.notes);
            } else if (Tag.CONCATENATION.equals(ch.tag)) {
                place.placeName += (ch.value == null ? "" : ch.value);
            } else if (Tag.CONTINUATION.equals(ch.tag)) {
                place.placeName += "\n" + (ch.value == null ? "" : ch.value);
            } else if (Tag.ROMANIZED.equals(ch.tag)) {
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but a romanized variation was specified on a place on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
                NameVariation nv = new NameVariation();
                place.romanized.add(nv);
                nv.variation = ch.value;
                for (StringTree gch : ch.children) {
                    if (Tag.TYPE.equals(gch.tag)) {
                        nv.variationType = new StringWithCustomTags(gch);
                    } else {
                        unknownTag(gch, nv);
                    }
                }
            } else if (Tag.PHONETIC.equals(ch.tag)) {
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but a phonetic variation was specified on a place on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
                NameVariation nv = new NameVariation();
                place.phonetic.add(nv);
                nv.variation = ch.value;
                for (StringTree gch : ch.children) {
                    if (Tag.TYPE.equals(gch.tag)) {
                        nv.variationType = new StringWithCustomTags(gch);
                    } else {
                        unknownTag(gch, nv);
                    }
                }
            } else if (Tag.MAP.equals(ch.tag)) {
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but a map coordinate was specified on a place on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
                for (StringTree gch : ch.children) {
                    if (Tag.LATITUDE.equals(gch.tag)) {
                        place.latitude = new StringWithCustomTags(gch);
                    } else if (Tag.LONGITUDE.equals(gch.tag)) {
                        place.longitude = new StringWithCustomTags(gch);
                    } else {
                        unknownTag(gch, place);
                    }
                }
            } else {
                unknownTag(ch, place);
            }
        }

    }

    /**
     * Load a repository for sources from a string tree node, and put it in the gedcom collection of repositories
     * 
     * @param st
     *            the node
     */
    private void loadRepository(StringTree st) {
        Repository r = getRepository(st.id);
        for (StringTree ch : st.children) {
            if (Tag.NAME.equals(ch.tag)) {
                r.name = new StringWithCustomTags(ch);
            } else if (Tag.ADDRESS.equals(ch.tag)) {
                r.address = new Address();
                loadAddress(ch, r.address);
            } else if (Tag.PHONE.equals(ch.tag)) {
                r.phoneNumbers.add(new StringWithCustomTags(ch));
            } else if (Tag.WEB_ADDRESS.equals(ch.tag)) {
                r.wwwUrls.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but WWW URL was specified on repository " + r.xref + " on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.FAX.equals(ch.tag)) {
                r.faxNumbers.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but fax was specified on repository " + r.xref + " on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.EMAIL.equals(ch.tag)) {
                r.emails.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but email was specified on repository " + r.xref + " on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, r.notes);
            } else if (Tag.REFERENCE.equals(ch.tag)) {
                UserReference u = new UserReference();
                r.userReferences.add(u);
                loadUserReference(ch, u);
            } else if (Tag.RECORD_ID_NUMBER.equals(ch.tag)) {
                r.recIdNumber = new StringWithCustomTags(ch);
            } else if (Tag.CHANGED_DATETIME.equals(ch.tag)) {
                r.changeDate = new ChangeDate();
                loadChangeDate(ch, r.changeDate);
            } else {
                unknownTag(ch, r);
            }
        }

    }

    /**
     * Load a reference to a repository in a source, from a string tree node
     * 
     * @param st
     *            the node
     * @return the RepositoryCitation loaded
     */
    private RepositoryCitation loadRepositoryCitation(StringTree st) {
        RepositoryCitation r = new RepositoryCitation();
        r.repositoryXref = st.value;
        for (StringTree ch : st.children) {
            if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, r.notes);
            } else if (Tag.CALL_NUMBER.equals(ch.tag)) {
                SourceCallNumber scn = new SourceCallNumber();
                r.callNumbers.add(scn);
                scn.callNumber = new StringWithCustomTags(ch.value);
                for (StringTree gch : ch.children) {
                    if (Tag.MEDIA.equals(gch.tag)) {
                        scn.mediaType = new StringWithCustomTags(gch);
                    } else {
                        unknownTag(gch, scn.callNumber);
                    }
                }
            } else {
                unknownTag(ch, r);
            }
        }
        return r;
    }

    /**
     * Load the root level items for the gedcom
     * 
     * @param st
     *            the root of the string tree
     * @throws GedcomParserException
     *             if the data cannot be parsed because it's not in the format expected
     */
    private void loadRootItems(StringTree st) throws GedcomParserException {
        if (cancelled) {
            throw new ParserCancelledException("File load/parse cancelled");
        }
        int i = 0;
        for (StringTree ch : st.children) {
            if (Tag.HEADER.equals(ch.tag)) {
                loadHeader(ch);
            } else if (Tag.SUBMITTER.equals(ch.tag)) {
                loadSubmitter(ch);
            } else if (Tag.INDIVIDUAL.equals(ch.tag)) {
                loadIndividual(ch);
            } else if (Tag.SUBMISSION.equals(ch.tag)) {
                loadSubmission(ch);
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadRootNote(ch);
            } else if (Tag.FAMILY.equals(ch.tag)) {
                loadFamily(ch);
            } else if (Tag.TRAILER.equals(ch.tag)) {
                gedcom.trailer = new Trailer();
            } else if (Tag.SOURCE.equals(ch.tag)) {
                loadSource(ch);
            } else if (Tag.REPOSITORY.equals(ch.tag)) {
                loadRepository(ch);
            } else if (Tag.OBJECT_MULTIMEDIA.equals(ch.tag)) {
                loadMultimediaRecord(ch);
            } else {
                unknownTag(ch, gedcom);
            }
            if (i % parseNotificationRate == 0) {
                notifyParseObservers(new ParseProgressEvent(this, gedcom, false));
            }
            if (cancelled) {
                throw new ParserCancelledException("File load/parse cancelled");
            }
            i++;
        }
        notifyParseObservers(new ParseProgressEvent(this, gedcom, true));
    }

    /**
     * Load a note at the root level of the GEDCOM. All these should have &#64;ID&#64;'s and thus should get added to
     * the GEDCOM's collection of notes rather than the one passed to <code>loadNote()</code>
     * 
     * @param ch
     *            the child nodes to be loaded as a note
     * @throws GedcomParserException
     *             if the data cannot be parsed because it's not in the format expected
     */
    private void loadRootNote(StringTree ch) throws GedcomParserException {
        List<Note> dummyList = new ArrayList<Note>();
        loadNote(ch, dummyList);
        if (!dummyList.isEmpty()) {
            throw new GedcomParserException("At root level NOTE structures should have @ID@'s");
        }
    }

    /**
     * Load a source (which may be referenced later) from a source tree node, and put it in the gedcom collection of
     * sources.
     * 
     * @param st
     *            the node
     */
    private void loadSource(StringTree st) {
        Source s = getSource(st.id);
        for (StringTree ch : st.children) {
            if (Tag.DATA_FOR_SOURCE.equals(ch.tag)) {
                s.data = new SourceData();
                loadSourceData(ch, s.data);
            } else if (Tag.TITLE.equals(ch.tag)) {
                loadMultiLinesOfText(ch, s.title, s);
            } else if (Tag.PUBLICATION_FACTS.equals(ch.tag)) {
                loadMultiLinesOfText(ch, s.publicationFacts, s);
            } else if (Tag.TEXT.equals(ch.tag)) {
                loadMultiLinesOfText(ch, s.sourceText, s);
            } else if (Tag.ABBREVIATION.equals(ch.tag)) {
                s.sourceFiledBy = new StringWithCustomTags(ch);
            } else if (Tag.AUTHORS.equals(ch.tag)) {
                loadMultiLinesOfText(ch, s.originatorsAuthors, s);
            } else if (Tag.REPOSITORY.equals(ch.tag)) {
                s.repositoryCitation = loadRepositoryCitation(ch);
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, s.notes);
            } else if (Tag.OBJECT_MULTIMEDIA.equals(ch.tag)) {
                loadMultimediaLink(ch, s.multimedia);
            } else if (Tag.REFERENCE.equals(ch.tag)) {
                UserReference u = new UserReference();
                s.userReferences.add(u);
                loadUserReference(ch, u);
            } else if (Tag.RECORD_ID_NUMBER.equals(ch.tag)) {
                s.recIdNumber = new StringWithCustomTags(ch);
            } else if (Tag.CHANGED_DATETIME.equals(ch.tag)) {
                s.changeDate = new ChangeDate();
                loadChangeDate(ch, s.changeDate);
            } else {
                unknownTag(ch, s);
            }
        }
    }

    /**
     * Load data for a source from a string tree node into a source data structure
     * 
     * @param st
     *            the node
     * @param data
     *            the source data structure
     */
    private void loadSourceData(StringTree st, SourceData data) {
        for (StringTree ch : st.children) {
            if (Tag.EVENT.equals(ch.tag)) {
                loadSourceDataEventRecorded(ch, data);
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, data.notes);
            } else if (Tag.AGENCY.equals(ch.tag)) {
                data.respAgency = new StringWithCustomTags(ch);
            } else {
                unknownTag(ch, data);
            }
        }
    }

    /**
     * Load the data for a recorded event from a string tree node
     * 
     * @param st
     *            the node
     * @param data
     *            the source data
     */
    private void loadSourceDataEventRecorded(StringTree st, SourceData data) {
        EventRecorded e = new EventRecorded();
        data.eventsRecorded.add(e);
        e.eventType = st.value;
        for (StringTree ch : st.children) {
            if (Tag.DATE.equals(ch.tag)) {
                e.datePeriod = new StringWithCustomTags(ch);
            } else if (Tag.PLACE.equals(ch.tag)) {
                e.jurisdiction = new StringWithCustomTags(ch);
            } else {
                unknownTag(ch, data);
            }
        }

    }

    /**
     * Load a source system structure from a string tree node
     * 
     * @param st
     *            the node
     * @param sourceSystem
     *            the source system structure
     */
    private void loadSourceSystem(StringTree st, SourceSystem sourceSystem) {
        sourceSystem.systemId = st.value;
        for (StringTree ch : st.children) {
            if (Tag.VERSION.equals(ch.tag)) {
                sourceSystem.versionNum = new StringWithCustomTags(ch);
            } else if (Tag.NAME.equals(ch.tag)) {
                sourceSystem.productName = new StringWithCustomTags(ch);
            } else if (Tag.CORPORATION.equals(ch.tag)) {
                sourceSystem.corporation = new Corporation();
                loadCorporation(ch, sourceSystem.corporation);
            } else if (Tag.DATA_FOR_CITATION.equals(ch.tag)) {
                sourceSystem.sourceData = new HeaderSourceData();
                loadHeaderSourceData(ch, sourceSystem.sourceData);
            } else {
                unknownTag(ch, sourceSystem);
            }
        }
    }

    /**
     * Load the submission structure from a string tree node
     * 
     * @param st
     *            the node
     */
    private void loadSubmission(StringTree st) {
        Submission s = new Submission(st.id);
        gedcom.submission = s;
        if (gedcom.header == null) {
            gedcom.header = new Header();
        }
        if (gedcom.header.submission == null) {
            /*
             * The GEDCOM spec puts a cross reference to the root-level SUBN element in the HEAD structure. Now that we
             * have a submission object, represent that cross reference in the header object
             */
            gedcom.header.submission = s;
        }
        for (StringTree ch : st.children) {
            if (Tag.SUBMITTER.equals(ch.tag)) {
                gedcom.submission.submitter = getSubmitter(ch.value);
            } else if (Tag.FAMILY_FILE.equals(ch.tag)) {
                gedcom.submission.nameOfFamilyFile = new StringWithCustomTags(ch);
            } else if (Tag.TEMPLE.equals(ch.tag)) {
                gedcom.submission.templeCode = new StringWithCustomTags(ch);
            } else if (Tag.ANCESTORS.equals(ch.tag)) {
                gedcom.submission.ancestorsCount = new StringWithCustomTags(ch);
            } else if (Tag.DESCENDANTS.equals(ch.tag)) {
                gedcom.submission.descendantsCount = new StringWithCustomTags(ch);
            } else if (Tag.ORDINANCE_PROCESS_FLAG.equals(ch.tag)) {
                gedcom.submission.ordinanceProcessFlag = new StringWithCustomTags(ch);
            } else if (Tag.RECORD_ID_NUMBER.equals(ch.tag)) {
                gedcom.submission.recIdNumber = new StringWithCustomTags(ch);
            } else {
                unknownTag(ch, gedcom.submission);
            }
        }

    }

    /**
     * Load a submitter from a string tree node into the gedcom global collection of submitters
     * 
     * @param st
     *            the node
     */
    private void loadSubmitter(StringTree st) {
        Submitter submitter = getSubmitter(st.id);
        for (StringTree ch : st.children) {
            if (Tag.NAME.equals(ch.tag)) {
                submitter.name = new StringWithCustomTags(ch);
            } else if (Tag.ADDRESS.equals(ch.tag)) {
                submitter.address = new Address();
                loadAddress(ch, submitter.address);
            } else if (Tag.PHONE.equals(ch.tag)) {
                submitter.phoneNumbers.add(new StringWithCustomTags(ch));
            } else if (Tag.WEB_ADDRESS.equals(ch.tag)) {
                submitter.wwwUrls.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but WWW URL number was specified on submitter on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.FAX.equals(ch.tag)) {
                submitter.faxNumbers.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but fax number was specified on submitter on line " + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.EMAIL.equals(ch.tag)) {
                submitter.emails.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but email was specified on submitter on line " + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (Tag.LANGUAGE.equals(ch.tag)) {
                submitter.languagePref.add(new StringWithCustomTags(ch));
            } else if (Tag.CHANGED_DATETIME.equals(ch.tag)) {
                submitter.changeDate = new ChangeDate();
                loadChangeDate(ch, submitter.changeDate);
            } else if (Tag.OBJECT_MULTIMEDIA.equals(ch.tag)) {
                loadMultimediaLink(ch, submitter.multimedia);
            } else if (Tag.RECORD_ID_NUMBER.equals(ch.tag)) {
                submitter.recIdNumber = new StringWithCustomTags(ch);
            } else if (Tag.REGISTRATION_FILE_NUMBER.equals(ch.tag)) {
                submitter.regFileNumber = new StringWithCustomTags(ch);
            } else if (Tag.NOTE.equals(ch.tag)) {
                loadNote(ch, submitter.notes);
            } else {
                unknownTag(ch, submitter);
            }
        }
    }

    /**
     * Load a user reference to from a string tree node
     * 
     * @param st
     *            the string tree node
     * @param u
     *            the user reference object
     */
    private void loadUserReference(StringTree st, UserReference u) {
        u.referenceNum = st.value;
        if (!st.children.isEmpty()) {
            u.type = st.children.get(0).value;
        }

    }

    /**
     * Read data from an {@link java.io.InputStream} and construct a {@link StringTree} object from its contents
     * 
     * @param bytes
     *            the input stream over the bytes of the file
     * @return the {@link StringTree} created from the contents of the input stream
     * @throws IOException
     *             if there is a problem reading the data from the reader
     * @throws GedcomParserException
     *             if there is an error with parsing the data from the stream
     */
    private StringTree makeStringTreeFromStream(BufferedInputStream bytes) throws IOException, GedcomParserException {
        /* This was the old way - loaded entire file into arraylist of strings */
        /*
         * List<String> lines = new GedcomFileReader(this, bytes).getLines(); return new StringTreeBuilder(this,
         * bytes).makeStringTreeFromFlatLines(lines);
         */
        /*
         * This is the new way - reads line at a time and adds each one to StringTree, avoiding temp arraylist of
         * strings
         */
        GedcomFileReader gfr = new GedcomFileReader(this, bytes);
        StringTreeBuilder stb = new StringTreeBuilder(this);
        String line = gfr.nextLine();
        while (line != null) {
            stb.appendLine(line);
            line = gfr.nextLine();
            if (cancelled) {
                throw new ParserCancelledException("File load/parse is cancelled");
            }
        }
        return stb.getTree();
    }

    /**
     * Notify all listeners about the change
     * 
     * @param e
     *            the change event to tell the observers
     */
    private void notifyParseObservers(ParseProgressEvent e) {
        int i = 0;
        while (i < parseObservers.size()) {
            WeakReference<ParseProgressListener> observerRef = parseObservers.get(i);
            if (observerRef == null) {
                parseObservers.remove(observerRef);
            } else {
                observerRef.get().progressNotification(e);
                i++;
            }
        }
    }

    /**
     * Read all the data from a stream and return the StringTree representation of that data
     * 
     * @param stream
     *            the stream to read
     * @return the data from the stream as a StringTree
     * @throws IOException
     *             if there's a problem reading the data off the stream
     * @throws GedcomParserException
     *             if there is an error parsing the gedcom data
     */
    private StringTree readStream(BufferedInputStream stream) throws IOException, GedcomParserException {
        return makeStringTreeFromStream(stream);
    }

    /**
     * Returns true if the node passed in uses a cross-reference to another node
     * 
     * @param st
     *            the node
     * @return true if and only if the node passed in uses a cross-reference to another node
     */
    private boolean referencesAnotherNode(StringTree st) {
        if (st.value == null) {
            return false;
        }
        int r1 = st.value.indexOf('@');
        if (r1 == -1) {
            return false;
        }
        int r2 = st.value.indexOf('@', r1);
        if (r2 == -1) {
            return false;
        }
        return true;
    }

    /**
     * <p>
     * Default handler for a tag that the parser was not expecting to see.
     * </p>
     * <ul>
     * <li>If the tag begins with an underscore, it is a user-defined tag, which is stored in the customTags collection
     * of the passed in element, and returns.</li>
     * <li>If {@link #strictCustomTags} parsing is turned off (i.e., == false), it is treated as a user-defined tag
     * (despite the lack of beginning underscore) and treated like any other user-defined tag.</li>
     * <li>If {@link #strictCustomTags} parsing is turned on (i.e., == true), it is treated as bad tag and an error is
     * logged in the {@link #errors} collection.</li>
     * </ul>
     * 
     * @param node
     *            the node containing the unknown tag.
     * @param element
     *            the element that the node is part of, so if it's a custom tag, this unknown tag can be added to this
     *            node's collection of custom tags
     */
    private void unknownTag(StringTree node, AbstractElement element) {
        if (node.tag.length() > 0 && (node.tag.charAt(0) == '_') || !strictCustomTags) {
            element.customTags.add(node);
            return;
        }

        StringBuilder sb = new StringBuilder(64); // Min size = 64
        sb.append("Line ").append(node.lineNum).append(": Cannot handle tag ");
        sb.append(node.tag);
        StringTree st = node;
        while (st.parent != null) {
            st = st.parent;
            sb.append(", child of ").append(st.tag == null ? null : st.tag);
            if (st.id != null) {
                sb.append(" ").append(st.id);
            }
            sb.append(" on line ").append(st.lineNum);
        }
        errors.add(sb.toString());
    }

}
