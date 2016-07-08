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
public class GedcomParser extends AbstractParser<Gedcom> {

    /**
     * The things that went wrong while parsing the gedcom file
     */
    private final List<String> errors = new ArrayList<String>();

    /**
     * The content of the gedcom file
     */
    private Gedcom gedcom = new Gedcom();

    /**
     * Indicates whether handling of custom tags should be strict - that is, must an unrecognized tag begin with an
     * underscore to be loaded into the custom tags collection? If false, unrecognized tags will be treated as custom
     * tags even if they don't begin with underscores, and no errors will be issued. If true, unrecognized tags that do
     * not begin with underscores will be discarded, with errors added to the errors collection.
     */
    private boolean strictCustomTags = true;

    /**
     * Indicates whether non-compliant GEDCOM files with actual line breaks in text values (rather than CONT tags)
     * should be parsed (with some loss of data) rather than fail with an exception.
     */
    private boolean strictLineBreaks = true;

    /**
     * The warnings issued during the parsing of the gedcom file
     */
    private final List<String> warnings = new ArrayList<String>();

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
     * The {@link StringTreeBuilder} that is assisting this class
     */
    private StringTreeBuilder stringTreeBuilder;

    /**
     * The 1-based line number that we've most recently read, so starts at zero (when we haven't read any lines yet)
     */
    private int lineNum;

    /**
     * 
     */
    public GedcomParser() {
        super(null, null, null);
        gedcomParser = this;
    }

    /**
     * @param gedcomParser
     * @param stringTree
     * @param loadInto
     */
    public GedcomParser(AbstractParser<Gedcom> gedcomParser, StringTree stringTree, Gedcom loadInto) {
        super(null, null, null);
        this.gedcomParser = this;
    }

    /**
     * Indicate that file loading should be cancelled
     */
    public void cancel() {
        cancelled = true;
    }

    /**
     * Get the errors
     * 
     * @return the errors
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Get the fileObservers
     * 
     * @return the fileObservers
     */
    public List<WeakReference<FileProgressListener>> getFileObservers() {
        return fileObservers;
    }

    /**
     * Get the gedcom
     * 
     * @return the gedcom
     */
    public Gedcom getGedcom() {
        return gedcom;
    }

    /**
     * Get the line number we're reading
     * 
     * @return the line number we're reading
     */
    public int getLineNum() {
        return lineNum;
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
     * Get the parseObservers
     * 
     * @return the parseObservers
     */
    public List<WeakReference<ParseProgressListener>> getParseObservers() {
        return parseObservers;
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
     * Get the warnings
     * 
     * @return the warnings
     */
    public List<String> getWarnings() {
        return warnings;
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
     * Get the strictCustomTags
     * 
     * @return the strictCustomTags
     */
    public boolean isStrictCustomTags() {
        return strictCustomTags;
    }

    /**
     * Get the strictLineBreaks
     * 
     * @return the strictLineBreaks
     */
    public boolean isStrictLineBreaks() {
        return strictLineBreaks;
    }

    /**
     * Read data from an {@link java.io.InputStream} and construct a {@link StringTree} object from its contents
     * 
     * @param bytes
     *            the input stream over the bytes of the file
     * @throws IOException
     *             if there is a problem reading the data from the reader
     * @throws GedcomParserException
     *             if there is an error with parsing the data from the stream
     */
    public void load(BufferedInputStream bytes) throws IOException, GedcomParserException {
        // Reset counters and stuff
        lineNum = 0;
        errors.clear();
        warnings.clear();
        cancelled = false;

        if (cancelled) {
            throw new ParserCancelledException("File load/parse cancelled");
        }
        GedcomFileReader gfr = new GedcomFileReader(this, bytes);
        stringTreeBuilder = new StringTreeBuilder(this);
        String line = gfr.nextLine();
        while (line != null) {

            if (line.charAt(0) == '0') {
                // We've hit the start of the next root node
                parseAndLoadPreviousStringTree();
            }

            lineNum++;
            stringTreeBuilder.appendLine(line);
            line = gfr.nextLine();
            if (cancelled) {
                throw new ParserCancelledException("File load/parse is cancelled");
            }
            if (lineNum % parseNotificationRate == 0) {
                notifyParseObservers(new ParseProgressEvent(this, gedcom, false, lineNum));
            }

        }
        parseAndLoadPreviousStringTree();
    }

    /**
     * Load a gedcom file with the supplied name
     * 
     * @param filename
     *            the name of the file to load
     * @throws IOException
     *             if the file cannot be read
     * @throws GedcomParserException
     *             if the file cannot be parsed
     */
    public void load(String filename) throws IOException, GedcomParserException {
        FileInputStream fis = new FileInputStream(filename);
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(fis);
            load(bis);
        } finally {
            if (bis != null) {
                bis.close();
            }
            fis.close();
        }
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
     * Set the gedcom
     * 
     * @param gedcom
     *            the gedcom to set
     */
    public void setGedcom(Gedcom gedcom) {
        this.gedcom = gedcom;
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
     * Set the strictCustomTags
     * 
     * @param strictCustomTags
     *            the strictCustomTags to set
     */
    public void setStrictCustomTags(boolean strictCustomTags) {
        this.strictCustomTags = strictCustomTags;
    }

    /**
     * Set the strictLineBreaks
     * 
     * @param strictLineBreaks
     *            the strictLineBreaks to set
     */
    public void setStrictLineBreaks(boolean strictLineBreaks) {
        this.strictLineBreaks = strictLineBreaks;
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

    @Override
    void parse() {
    }

    /**
     * Load the flat file into a tree structure that reflects the heirarchy of its contents, using the default encoding
     * for your JVM
     * 
     * @param filename
     *            the file to load
     * @throws IOException
     *             if there is a problem reading the file
     * @throws GedcomParserException
     *             if there is a problem parsing the data in the file
     */
    void readFile(String filename) throws IOException, GedcomParserException {

    }

    /**
     * Load a family structure from a stringtree node, and load it into the gedcom family collection
     * 
     * @param st
     *            the node
     */
    private void loadFamily(StringTree st) {
        Family f = getFamily(st.getId());
        if (st.getChildren() != null) {
            for (StringTree ch : st.getChildren()) {
                if (Tag.HUSBAND.equalsText(ch.getTag())) {
                    f.setHusband(getIndividual(ch.getValue()));
                } else if (Tag.WIFE.equalsText(ch.getTag())) {
                    f.setWife(getIndividual(ch.getValue()));
                } else if (Tag.CHILD.equalsText(ch.getTag())) {
                    f.getChildren(true).add(getIndividual(ch.getValue()));
                } else if (Tag.NUM_CHILDREN.equalsText(ch.getTag())) {
                    f.setNumChildren(new StringWithCustomTags(ch));
                } else if (Tag.SOURCE.equalsText(ch.getTag())) {
                    List<AbstractCitation> citations = f.getCitations(true);
                    new CitationListParser(gedcomParser, ch, citations).parse();
                } else if (Tag.OBJECT_MULTIMEDIA.equalsText(ch.getTag())) {
                    List<Multimedia> multimedia = f.getMultimedia(true);
                    new MultimediaLinkParser(gedcomParser, ch, multimedia).parse();
                } else if (Tag.RECORD_ID_NUMBER.equalsText(ch.getTag())) {
                    f.setAutomatedRecordId(new StringWithCustomTags(ch));
                } else if (Tag.CHANGED_DATETIME.equalsText(ch.getTag())) {
                    ChangeDate changeDate = new ChangeDate();
                    f.setChangeDate(changeDate);
                    new ChangeDateParser(gedcomParser, ch, changeDate).parse();
                } else if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = f.getNotes(true);
                    new NoteListParser(gedcomParser, ch, notes).parse();
                } else if (Tag.RESTRICTION.equalsText(ch.getTag())) {
                    f.setRestrictionNotice(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but restriction notice was specified for family on line " + ch.getLineNum()
                                + " , which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.REGISTRATION_FILE_NUMBER.equalsText(ch.getTag())) {
                    f.setRecFileNumber(new StringWithCustomTags(ch));
                } else if (FamilyEventType.isValidTag(ch.getTag())) {
                    loadFamilyEvent(ch, f.getEvents(true));
                } else if (Tag.SEALING_SPOUSE.equalsText(ch.getTag())) {
                    loadLdsSpouseSealing(ch, f.getLdsSpouseSealings(true));
                } else if (Tag.SUBMITTER.equalsText(ch.getTag())) {
                    f.getSubmitters(true).add(getSubmitter(ch.getValue()));
                } else if (Tag.REFERENCE.equalsText(ch.getTag())) {
                    UserReference u = new UserReference();
                    f.getUserReferences(true).add(u);
                    new UserReferenceParser(gedcomParser, ch, u).parse();
                } else {
                    unknownTag(ch, f);
                }
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
        e.setType(FamilyEventType.getFromTag(st.getTag()));
        if ("Y".equals(st.getValue())) {
            e.setyNull(st.getValue());
            e.setDescription(null);
        } else if (st.getValue() == null || st.getValue().trim().length() == 0) {
            e.setyNull(null);
            e.setDescription(null);
        } else {
            e.setyNull(null);
            e.setDescription(new StringWithCustomTags(st.getValue()));
            addWarning(st.getTag() + " tag had description rather than [Y|<NULL>] - violates standard");
        }
        if (st.getChildren() != null) {
            for (StringTree ch : st.getChildren()) {
                if (Tag.TYPE.equalsText(ch.getTag())) {
                    e.setSubType(new StringWithCustomTags(ch));
                } else if (Tag.DATE.equalsText(ch.getTag())) {
                    e.setDate(new StringWithCustomTags(ch));
                } else if (Tag.PLACE.equalsText(ch.getTag())) {
                    Place place = new Place();
                    e.setPlace(place);
                    new PlaceParser(gedcomParser, ch, place).parse();
                } else if (Tag.OBJECT_MULTIMEDIA.equalsText(ch.getTag())) {
                    List<Multimedia> multimedia = e.getMultimedia(true);
                    new MultimediaLinkParser(gedcomParser, ch, multimedia).parse();
                } else if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = e.getNotes(true);
                    new NoteListParser(gedcomParser, ch, notes).parse();
                } else if (Tag.SOURCE.equalsText(ch.getTag())) {
                    List<AbstractCitation> citations = e.getCitations(true);
                    new CitationListParser(gedcomParser, ch, citations).parse();
                } else if (Tag.RESTRICTION.equalsText(ch.getTag())) {
                    e.setRestrictionNotice(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but restriction notice was specified for family event on line " + ch.getLineNum()
                                + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.RELIGION.equalsText(ch.getTag())) {
                    e.setReligiousAffiliation(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but religious affiliation was specified for family event on line " + ch.getLineNum()
                                + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.AGE.equalsText(ch.getTag())) {
                    e.setAge(new StringWithCustomTags(ch));
                } else if (Tag.CAUSE.equalsText(ch.getTag())) {
                    e.setCause(new StringWithCustomTags(ch));
                } else if (Tag.ADDRESS.equalsText(ch.getTag())) {
                    Address address = new Address();
                    e.setAddress(address);
                    new AddressParser(gedcomParser, ch, address).parse();
                } else if (Tag.AGENCY.equalsText(ch.getTag())) {
                    e.setRespAgency(new StringWithCustomTags(ch));
                } else if (Tag.PHONE.equalsText(ch.getTag())) {
                    e.getPhoneNumbers(true).add(new StringWithCustomTags(ch));
                } else if (Tag.WEB_ADDRESS.equalsText(ch.getTag())) {
                    e.getWwwUrls(true).add(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but WWW URL was specified for " + e.getType() + " family event on line " + ch.getLineNum()
                                + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.FAX.equalsText(ch.getTag())) {
                    e.getFaxNumbers(true).add(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but fax number was specified for " + e.getType() + " family event on line " + ch.getLineNum()
                                + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.EMAIL.equalsText(ch.getTag())) {
                    e.getEmails(true).add(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but email was specified for " + e.getType() + " family event on line " + ch.getLineNum()
                                + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.HUSBAND.equalsText(ch.getTag())) {
                    e.setHusbandAge(new StringWithCustomTags(ch.getChildren().get(0)));
                } else if (Tag.WIFE.equalsText(ch.getTag())) {
                    e.setWifeAge(new StringWithCustomTags(ch.getChildren().get(0)));
                } else if (Tag.CONCATENATION.equalsText(ch.getTag())) {
                    if (e.getDescription() == null) {
                        e.setDescription(new StringWithCustomTags(ch));
                    } else {
                        e.getDescription().setValue(e.getDescription().getValue() + ch.getValue());
                    }
                } else if (Tag.CONTINUATION.equalsText(ch.getTag())) {
                    if (e.getDescription() == null) {
                        e.setDescription(new StringWithCustomTags(ch.getValue() == null ? "" : ch.getValue()));
                    } else {
                        e.getDescription().setValue(e.getDescription().getValue() + "\n" + ch.getValue());
                    }
                } else {
                    unknownTag(ch, e);
                }
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
        Family f = getFamily(st.getValue());
        FamilySpouse fs = new FamilySpouse();
        fs.setFamily(f);
        familiesWhereSpouse.add(fs);
        if (st.getChildren() != null) {
            if (st.getChildren() != null) {
                for (StringTree ch : st.getChildren()) {
                    if (Tag.NOTE.equalsText(ch.getTag())) {
                        List<Note> notes = fs.getNotes(true);
                        new NoteListParser(gedcomParser, ch, notes).parse();
                    } else {
                        unknownTag(ch, fs);
                    }
                }
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
        Individual i = getIndividual(st.getId());
        if (st.getChildren() != null) {
            for (StringTree ch : st.getChildren()) {
                if (Tag.NAME.equalsText(ch.getTag())) {
                    PersonalName pn = new PersonalName();
                    i.getNames(true).add(pn);
                    new PersonalNameParser(gedcomParser, ch, pn).parse();
                } else if (Tag.SEX.equalsText(ch.getTag())) {
                    i.setSex(new StringWithCustomTags(ch));
                } else if (Tag.ADDRESS.equalsText(ch.getTag())) {
                    Address address = new Address();
                    i.setAddress(address);
                    new AddressParser(gedcomParser, ch, address).parse();
                } else if (Tag.PHONE.equalsText(ch.getTag())) {
                    i.getPhoneNumbers(true).add(new StringWithCustomTags(ch));
                } else if (Tag.WEB_ADDRESS.equalsText(ch.getTag())) {
                    i.getWwwUrls(true).add(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but WWW URL was specified for individual " + i.getXref() + " on line " + ch.getLineNum()
                                + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.FAX.equalsText(ch.getTag())) {
                    i.getFaxNumbers(true).add(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but fax was specified for individual " + i.getXref() + "on line " + ch.getLineNum()
                                + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.EMAIL.equalsText(ch.getTag())) {
                    i.getEmails(true).add(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but email was specified for individual " + i.getXref() + " on line " + ch.getLineNum()
                                + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (IndividualEventType.isValidTag(ch.getTag())) {
                    IndividualEvent event = new IndividualEvent();
                    i.getEvents(true).add(event);
                    new IndividualEventParser(gedcomParser, ch, event).parse();
                } else if (IndividualAttributeType.isValidTag(ch.getTag())) {
                    IndividualAttribute a = new IndividualAttribute();
                    i.getAttributes(true).add(a);
                    new IndividualAttributeParser(gedcomParser, ch, a).parse();
                } else if (LdsIndividualOrdinanceType.isValidTag(ch.getTag())) {
                    loadLdsIndividualOrdinance(ch, i.getLdsIndividualOrdinances(true));
                } else if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = i.getNotes(true);
                    new NoteListParser(gedcomParser, ch, notes).parse();
                } else if (Tag.CHANGED_DATETIME.equalsText(ch.getTag())) {
                    ChangeDate changeDate = new ChangeDate();
                    i.setChangeDate(changeDate);
                    new ChangeDateParser(gedcomParser, ch, changeDate).parse();
                } else if (Tag.RECORD_ID_NUMBER.equalsText(ch.getTag())) {
                    i.setRecIdNumber(new StringWithCustomTags(ch));
                } else if (Tag.REGISTRATION_FILE_NUMBER.equalsText(ch.getTag())) {
                    i.setPermanentRecFileNumber(new StringWithCustomTags(ch));
                } else if (Tag.OBJECT_MULTIMEDIA.equalsText(ch.getTag())) {
                    List<Multimedia> multimedia = i.getMultimedia(true);
                    new MultimediaLinkParser(gedcomParser, ch, multimedia).parse();
                } else if (Tag.RESTRICTION.equalsText(ch.getTag())) {
                    i.setRestrictionNotice(new StringWithCustomTags(ch));
                } else if (Tag.SOURCE.equalsText(ch.getTag())) {
                    List<AbstractCitation> citations = i.getCitations(true);
                    new CitationListParser(gedcomParser, ch, citations).parse();
                } else if (Tag.ALIAS.equalsText(ch.getTag())) {
                    i.getAliases(true).add(new StringWithCustomTags(ch));
                } else if (Tag.FAMILY_WHERE_SPOUSE.equalsText(ch.getTag())) {
                    loadFamilyWhereSpouse(ch, i.getFamiliesWhereSpouse(true));
                } else if (Tag.FAMILY_WHERE_CHILD.equalsText(ch.getTag())) {
                    FamilyChild fc = new FamilyChild();
                    i.getFamiliesWhereChild(true).add(fc);
                    new FamilyChildParser(gedcomParser, ch, fc).parse();
                } else if (Tag.ASSOCIATION.equalsText(ch.getTag())) {
                    Association a = new Association();
                    i.getAssociations(true).add(a);
                    new AssociationParser(gedcomParser, ch, a).parse();
                } else if (Tag.ANCESTOR_INTEREST.equalsText(ch.getTag())) {
                    i.getAncestorInterest(true).add(getSubmitter(ch.getValue()));
                } else if (Tag.DESCENDANT_INTEREST.equalsText(ch.getTag())) {
                    i.getDescendantInterest(true).add(getSubmitter(ch.getValue()));
                } else if (Tag.ANCESTRAL_FILE_NUMBER.equalsText(ch.getTag())) {
                    i.setAncestralFileNumber(new StringWithCustomTags(ch));
                } else if (Tag.REFERENCE.equalsText(ch.getTag())) {
                    UserReference u = new UserReference();
                    i.getUserReferences(true).add(u);
                    new UserReferenceParser(gedcomParser, ch, u).parse();
                } else if (Tag.SUBMITTER.equalsText(ch.getTag())) {
                    i.getSubmitters(true).add(getSubmitter(ch.getValue()));
                } else {
                    unknownTag(ch, i);
                }
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
        o.setType(LdsIndividualOrdinanceType.getFromTag(st.getTag()));
        o.setyNull(st.getValue());
        if (st.getChildren() != null) {
            for (StringTree ch : st.getChildren()) {
                if (Tag.DATE.equalsText(ch.getTag())) {
                    o.setDate(new StringWithCustomTags(ch));
                } else if (Tag.PLACE.equalsText(ch.getTag())) {
                    o.setPlace(new StringWithCustomTags(ch));
                } else if (Tag.STATUS.equalsText(ch.getTag())) {
                    o.setStatus(new StringWithCustomTags(ch));
                } else if (Tag.TEMPLE.equalsText(ch.getTag())) {
                    o.setTemple(new StringWithCustomTags(ch));
                } else if (Tag.SOURCE.equalsText(ch.getTag())) {
                    List<AbstractCitation> citations = o.getCitations(true);
                    new CitationListParser(gedcomParser, ch, citations).parse();
                } else if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = o.getNotes(true);
                    new NoteListParser(gedcomParser, ch, notes).parse();
                } else if (Tag.FAMILY_WHERE_CHILD.equalsText(ch.getTag())) {
                    FamilyChild fc = new FamilyChild();
                    o.setFamilyWhereChild(fc);
                    new FamilyChildParser(gedcomParser, ch, fc).parse();
                } else {
                    unknownTag(ch, o);
                }
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
        if (st.getChildren() != null) {
            for (StringTree ch : st.getChildren()) {
                if (Tag.DATE.equalsText(ch.getTag())) {
                    o.setDate(new StringWithCustomTags(ch));
                } else if (Tag.PLACE.equalsText(ch.getTag())) {
                    o.setPlace(new StringWithCustomTags(ch));
                } else if (Tag.STATUS.equalsText(ch.getTag())) {
                    o.setStatus(new StringWithCustomTags(ch));
                } else if (Tag.TEMPLE.equalsText(ch.getTag())) {
                    o.setTemple(new StringWithCustomTags(ch));
                } else if (Tag.SOURCE.equalsText(ch.getTag())) {
                    List<AbstractCitation> citations = o.getCitations(true);
                    new CitationListParser(gedcomParser, ch, citations).parse();
                } else if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = o.getNotes(true);
                    new NoteListParser(gedcomParser, ch, notes).parse();
                } else {
                    unknownTag(ch, o);
                }

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
        Repository r = getRepository(st.getId());
        if (st.getChildren() != null) {
            for (StringTree ch : st.getChildren()) {
                if (Tag.NAME.equalsText(ch.getTag())) {
                    r.setName(new StringWithCustomTags(ch));
                } else if (Tag.ADDRESS.equalsText(ch.getTag())) {
                    Address address = new Address();
                    r.setAddress(address);
                    new AddressParser(gedcomParser, ch, address).parse();
                } else if (Tag.PHONE.equalsText(ch.getTag())) {
                    r.getPhoneNumbers(true).add(new StringWithCustomTags(ch));
                } else if (Tag.WEB_ADDRESS.equalsText(ch.getTag())) {
                    r.getWwwUrls(true).add(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but WWW URL was specified on repository " + r.getXref() + " on line " + ch.getLineNum()
                                + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.FAX.equalsText(ch.getTag())) {
                    r.getFaxNumbers(true).add(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but fax was specified on repository " + r.getXref() + " on line " + ch.getLineNum()
                                + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.EMAIL.equalsText(ch.getTag())) {
                    r.getEmails(true).add(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but email was specified on repository " + r.getXref() + " on line " + ch.getLineNum()
                                + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = r.getNotes(true);
                    new NoteListParser(gedcomParser, ch, notes).parse();
                } else if (Tag.REFERENCE.equalsText(ch.getTag())) {
                    UserReference u = new UserReference();
                    r.getUserReferences(true).add(u);
                    new UserReferenceParser(gedcomParser, ch, u).parse();
                } else if (Tag.RECORD_ID_NUMBER.equalsText(ch.getTag())) {
                    r.setRecIdNumber(new StringWithCustomTags(ch));
                } else if (Tag.CHANGED_DATETIME.equalsText(ch.getTag())) {
                    ChangeDate changeDate = new ChangeDate();
                    r.setChangeDate(changeDate);
                    new ChangeDateParser(gedcomParser, ch, changeDate).parse();
                } else {
                    unknownTag(ch, r);
                }
            }
        }

    }

    /**
     * Load a single root-level item
     * 
     * @param rootLevelItem
     *            the string tree for the root level item
     * @throws GedcomParserException
     *             if the data cannot be parsed because it's not in the format expected
     */
    private void loadRootItem(StringTree rootLevelItem) throws GedcomParserException {
        if (Tag.HEADER.equalsText(rootLevelItem.getTag())) {
            Header header = new Header();
            gedcom.setHeader(header);
            new HeaderParser(this, rootLevelItem, header).parse();
        } else if (Tag.SUBMITTER.equalsText(rootLevelItem.getTag())) {

            loadSubmitter(rootLevelItem);
        } else if (Tag.INDIVIDUAL.equalsText(rootLevelItem.getTag())) {
            loadIndividual(rootLevelItem);
        } else if (Tag.SUBMISSION.equalsText(rootLevelItem.getTag())) {
            loadSubmission(rootLevelItem);
        } else if (Tag.NOTE.equalsText(rootLevelItem.getTag())) {
            List<Note> dummyList = new ArrayList<Note>();
            new NoteListParser(this, rootLevelItem, dummyList).parse();
            if (!dummyList.isEmpty()) {
                throw new GedcomParserException("At root level NOTE structures should have @ID@'s");
            }
        } else if (Tag.FAMILY.equalsText(rootLevelItem.getTag())) {
            loadFamily(rootLevelItem);
        } else if (Tag.TRAILER.equalsText(rootLevelItem.getTag())) {
            gedcom.setTrailer(new Trailer());
        } else if (Tag.SOURCE.equalsText(rootLevelItem.getTag())) {
            Source s = getSource(rootLevelItem.getId());
            new SourceParser(this, rootLevelItem, s).parse();
        } else if (Tag.REPOSITORY.equalsText(rootLevelItem.getTag())) {
            loadRepository(rootLevelItem);
        } else if (Tag.OBJECT_MULTIMEDIA.equalsText(rootLevelItem.getTag())) {
            Multimedia multimedia = getMultimedia(rootLevelItem.getId());
            new MultimediaRecordParser(this, rootLevelItem, multimedia).parse();
        } else {
            unknownTag(rootLevelItem, gedcom);
        }
    }

    /**
     * Load the submission structure from a string tree node
     * 
     * @param st
     *            the node
     */
    private void loadSubmission(StringTree st) {
        Submission s = new Submission(st.getId());
        gedcom.setSubmission(s);
        if (gedcom.getHeader() == null) {
            gedcom.setHeader(new Header());
        }
        if (gedcom.getHeader().getSubmission() == null) {
            /*
             * The GEDCOM spec puts a cross reference to the root-level SUBN element in the HEAD structure. Now that we
             * have a submission object, represent that cross reference in the header object
             */
            gedcom.getHeader().setSubmission(s);
        }
        if (st.getChildren() != null) {
            for (StringTree ch : st.getChildren()) {
                Submission submission = gedcom.getSubmission();
                if (Tag.SUBMITTER.equalsText(ch.getTag())) {
                    submission.setSubmitter(getSubmitter(ch.getValue()));
                } else if (Tag.FAMILY_FILE.equalsText(ch.getTag())) {
                    submission.setNameOfFamilyFile(new StringWithCustomTags(ch));
                } else if (Tag.TEMPLE.equalsText(ch.getTag())) {
                    submission.setTempleCode(new StringWithCustomTags(ch));
                } else if (Tag.ANCESTORS.equalsText(ch.getTag())) {
                    submission.setAncestorsCount(new StringWithCustomTags(ch));
                } else if (Tag.DESCENDANTS.equalsText(ch.getTag())) {
                    submission.setDescendantsCount(new StringWithCustomTags(ch));
                } else if (Tag.ORDINANCE_PROCESS_FLAG.equalsText(ch.getTag())) {
                    submission.setOrdinanceProcessFlag(new StringWithCustomTags(ch));
                } else if (Tag.RECORD_ID_NUMBER.equalsText(ch.getTag())) {
                    submission.setRecIdNumber(new StringWithCustomTags(ch));
                } else {
                    unknownTag(ch, submission);
                }
            }
        }

    }

    /**
     * Load a submitter from a string tree node into the gedcom global collection of submitters
     * 
     * @param subm
     *            the stringtree for the submitter node
     */
    private void loadSubmitter(StringTree subm) {
        Submitter submitter = getSubmitter(subm.getId());
        if (subm.getChildren() != null) {
            for (StringTree ch : subm.getChildren()) {
                if (Tag.NAME.equalsText(ch.getTag())) {
                    submitter.setName(new StringWithCustomTags(ch));
                } else if (Tag.ADDRESS.equalsText(ch.getTag())) {
                    Address address = new Address();
                    submitter.setAddress(address);
                    new AddressParser(gedcomParser, ch, address).parse();
                } else if (Tag.PHONE.equalsText(ch.getTag())) {
                    submitter.getPhoneNumbers(true).add(new StringWithCustomTags(ch));
                } else if (Tag.WEB_ADDRESS.equalsText(ch.getTag())) {
                    submitter.getWwwUrls(true).add(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but WWW URL number was specified on submitter on line " + ch.getLineNum()
                                + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.FAX.equalsText(ch.getTag())) {
                    submitter.getFaxNumbers(true).add(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but fax number was specified on submitter on line " + ch.getLineNum()
                                + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.EMAIL.equalsText(ch.getTag())) {
                    submitter.getEmails(true).add(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but email was specified on submitter on line " + ch.getLineNum()
                                + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.LANGUAGE.equalsText(ch.getTag())) {
                    submitter.getLanguagePref(true).add(new StringWithCustomTags(ch));
                } else if (Tag.CHANGED_DATETIME.equalsText(ch.getTag())) {
                    ChangeDate changeDate = new ChangeDate();
                    submitter.setChangeDate(changeDate);
                    new ChangeDateParser(gedcomParser, ch, changeDate).parse();
                } else if (Tag.OBJECT_MULTIMEDIA.equalsText(ch.getTag())) {
                    List<Multimedia> multimedia = submitter.getMultimedia(true);
                    new MultimediaLinkParser(gedcomParser, ch, multimedia).parse();
                } else if (Tag.RECORD_ID_NUMBER.equalsText(ch.getTag())) {
                    submitter.setRecIdNumber(new StringWithCustomTags(ch));
                } else if (Tag.REGISTRATION_FILE_NUMBER.equalsText(ch.getTag())) {
                    submitter.setRegFileNumber(new StringWithCustomTags(ch));
                } else if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = submitter.getNotes(true);
                    new NoteListParser(gedcomParser, ch, notes).parse();
                } else {
                    unknownTag(ch, submitter);
                }
            }
        }
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
                ParseProgressListener l = observerRef.get();
                if (l != null) {
                    l.progressNotification(e);
                }
                i++;
            }
        }
    }

    /**
     * Parse the {@link StringTreeBuilder}'s string tree in memory, load it into the object model, then discard that
     * string tree buffer
     * 
     * @throws GedcomParserException
     *             if the string tree contents cannot be parsed, or parsing was cancelled
     */
    private void parseAndLoadPreviousStringTree() throws GedcomParserException {
        StringTree tree = stringTreeBuilder.getTree();
        if (tree != null && tree.getLevel() == -1 && tree.getChildren() != null && tree.getChildren().size() == 1) {
            // We've still got the prior root node in memory - parse it and add to object model
            StringTree rootLevelItem = stringTreeBuilder.getTree().getChildren().get(0);
            if (rootLevelItem.getLevel() != 0) {
                throw new GedcomParserException("Expected a root level item in the buffer, but found " + rootLevelItem.getLevel() + " " + rootLevelItem.getTag()
                        + " from line " + lineNum);
            }
            loadRootItem(rootLevelItem);
            // And discard it, now that it's loaded
            stringTreeBuilder = new StringTreeBuilder(this);
        }
    }

}
