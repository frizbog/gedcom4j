/* 
 * Copyright (c) 2009-2012 Matthew R. Harrah
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.model.*;

/**
 * <p>
 * Class for parsing GEDCOM 5.5 files and creating a {@link Gedcom} structure from them.
 * </p>
 * <p>
 * General usage is as follows:
 * <ol>
 * <li>Instantiate a <code>GedcomParser</code> object</li>
 * <li>Call the <code>GedcomParser.load()</code> method (in one of its various forms) to parse a file/stream</li>
 * <li>Access the parser's <code>gedcom</code> property to access the parsed data</li>
 * </ol>
 * </p>
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
     * The content of the gedcom file
     */
    public Gedcom gedcom = new Gedcom();

    /**
     * The things that went wrong while parsing the gedcom file
     */
    public List<String> errors = new ArrayList<String>();

    /**
     * The warnings issued during the parsing of the gedcom file
     */
    public List<String> warnings = new ArrayList<String>();

    /**
     * A flag that indicates whether feedback should be sent to System.out as parsing occurs
     */
    public boolean verbose = false;

    /**
     * Returns true if and only if the Gedcom data says it is for the 5.5 standard.
     * 
     * @return true if and only if the Gedcom data says it is for the 5.5 standard.
     */
    private boolean g55() {
        return gedcom != null && gedcom.header != null && gedcom.header.gedcomVersion != null
                && SupportedVersion.V5_5.equals(gedcom.header.gedcomVersion.versionNumber);
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
        Submitter i;
        i = gedcom.submitters.get(xref);
        if (i == null) {
            i = new Submitter();
            i.xref = xref;
            gedcom.submitters.put(xref, i);
        }
        return i;
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
            if ("ADR1".equals(ch.tag)) {
                address.addr1 = new StringWithCustomTags(ch);
            } else if ("ADR2".equals(ch.tag)) {
                address.addr2 = new StringWithCustomTags(ch);
            } else if ("CITY".equals(ch.tag)) {
                address.city = new StringWithCustomTags(ch);
            } else if ("STAE".equals(ch.tag)) {
                address.stateProvince = new StringWithCustomTags(ch);
            } else if ("POST".equals(ch.tag)) {
                address.postalCode = new StringWithCustomTags(ch);
            } else if ("CTRY".equals(ch.tag)) {
                address.country = new StringWithCustomTags(ch);
            } else if ("CONC".equals(ch.tag)) {
                if (address.lines.size() == 0) {
                    address.lines.add(ch.value);
                } else {
                    address.lines.set(address.lines.size() - 1, address.lines.get(address.lines.size() - 1) + ch.value);
                }
            } else if ("CONT".equals(ch.tag)) {
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
            if ("RELA".equals(ch.tag)) {
                a.relationship = new StringWithCustomTags(ch);
            } else if ("NOTE".equals(ch.tag)) {
                loadNote(ch, a.notes);
            } else if ("SOUR".equals(ch.tag)) {
                loadCitation(ch, a.citations);
            } else if ("TYPE".equals(ch.tag)) {
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
            if ("DATE".equals(ch.tag)) {
                changeDate.date = new StringWithCustomTags(ch.value);
                if (!ch.children.isEmpty()) {
                    changeDate.time = new StringWithCustomTags(ch.children.get(0));
                }
            } else if ("NOTE".equals(ch.tag)) {
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
        if (GedcomParserHelper.referencesAnotherNode(st)) {
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
            if ("DATE".equals(ch.tag)) {
                d.entryDate = new StringWithCustomTags(ch);
            } else if ("TEXT".equals(ch.tag)) {
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
            if ("CONT".equals(ch.tag)) {
                cws.description.add(ch.value == null ? "" : ch.value);
            } else if ("CONC".equals(ch.tag)) {
                if (cws.description.size() == 0) {
                    cws.description.add(ch.value);
                } else {
                    // Append to last value in string list
                    cws.description.set(cws.description.size() - 1, cws.description.get(cws.description.size() - 1)
                            + ch.value);
                }
            } else if ("TEXT".equals(ch.tag)) {
                List<String> ls = new ArrayList<String>();
                cws.textFromSource.add(ls);
                loadMultiLinesOfText(ch, ls, cws);
            } else if ("NOTE".equals(ch.tag)) {
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
        if (GedcomParserHelper.referencesAnotherNode(st)) {
            src = getSource(st.value);
        }
        cws.source = src;
        for (StringTree ch : st.children) {
            if ("PAGE".equals(ch.tag)) {
                cws.whereInSource = new StringWithCustomTags(ch);
            } else if ("EVEN".equals(ch.tag)) {
                cws.eventCited = new StringWithCustomTags(ch.value);
                if (ch.children != null) {
                    for (StringTree gc : ch.children) {
                        if ("ROLE".equals(gc.tag)) {
                            cws.roleInEvent = new StringWithCustomTags(gc);
                        } else {
                            unknownTag(gc, cws.eventCited);
                        }
                    }
                }
            } else if ("DATA".equals(ch.tag)) {
                CitationData d = new CitationData();
                cws.data.add(d);
                loadCitationData(ch, d);
            } else if ("QUAY".equals(ch.tag)) {
                cws.certainty = new StringWithCustomTags(ch);
            } else if ("NOTE".equals(ch.tag)) {
                loadNote(ch, cws.notes);
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
            if ("ADDR".equals(ch.tag)) {
                corporation.address = new Address();
                loadAddress(ch, corporation.address);
            } else if ("PHON".equals(ch.tag)) {
                corporation.phoneNumbers.add(new StringWithCustomTags(ch));
            } else if ("WWW".equals(ch.tag)) {
                corporation.wwwUrls.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but WWW URL was specified for the corporation in the source system on line "
                            + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("FAX".equals(ch.tag)) {
                corporation.faxNumbers.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but fax number was specified for the corporation in the source system on line "
                            + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("EMAIL".equals(ch.tag)) {
                corporation.emails.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but emails was specified for the corporation in the source system on line "
                            + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
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
            if ("HUSB".equals(ch.tag)) {
                f.husband = getIndividual(ch.value);
            } else if ("WIFE".equals(ch.tag)) {
                f.wife = getIndividual(ch.value);
            } else if ("CHIL".equals(ch.tag)) {
                f.children.add(getIndividual(ch.value));
            } else if ("NCHI".equals(ch.tag)) {
                f.numChildren = new StringWithCustomTags(ch);
            } else if ("SOUR".equals(ch.tag)) {
                loadCitation(ch, f.citations);
            } else if ("OBJE".equals(ch.tag)) {
                loadMultimediaLink(ch, f.multimedia);
            } else if ("RIN".equals(ch.tag)) {
                f.automatedRecordId = new StringWithCustomTags(ch);
            } else if ("CHAN".equals(ch.tag)) {
                f.changeDate = new ChangeDate();
                loadChangeDate(ch, f.changeDate);
            } else if ("NOTE".equals(ch.tag)) {
                loadNote(ch, f.notes);
            } else if ("RESN".equals(ch.tag)) {
                f.restrictionNotice = new StringWithCustomTags(ch);
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but restriction notice was specified for family on line "
                            + ch.lineNum + " , which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("RFN".equals(ch.tag)) {
                f.recFileNumber = new StringWithCustomTags(ch);
            } else if (FamilyEventType.isValidTag(ch.tag)) {
                loadFamilyEvent(ch, f.events);
            } else if ("SLGS".equals(ch.tag)) {
                loadLdsSpouseSealing(ch, f.ldsSpouseSealings);
            } else if ("SUBM".equals(ch.tag)) {
                f.submitters.add(getSubmitter(ch.value));
            } else if ("REFN".equals(ch.tag)) {
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
        e.yNull = st.value;
        for (StringTree ch : st.children) {
            if ("TYPE".equals(ch.tag)) {
                e.subType = new StringWithCustomTags(ch);
            } else if ("DATE".equals(ch.tag)) {
                e.date = new StringWithCustomTags(ch);
            } else if ("PLAC".equals(ch.tag)) {
                e.place = new Place();
                loadPlace(ch, e.place);
            } else if ("OBJE".equals(ch.tag)) {
                loadMultimediaLink(ch, e.multimedia);
            } else if ("NOTE".equals(ch.tag)) {
                loadNote(ch, e.notes);
            } else if ("SOUR".equals(ch.tag)) {
                loadCitation(ch, e.citations);
            } else if ("RESN".equals(ch.tag)) {
                e.restrictionNotice = new StringWithCustomTags(ch);
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but restriction notice was specified for family event on line "
                            + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("RELI".equals(ch.tag)) {
                e.religiousAffiliation = new StringWithCustomTags(ch);
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but religious affiliation was specified for family event on line "
                            + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("AGE".equals(ch.tag)) {
                e.age = new StringWithCustomTags(ch);
            } else if ("CAUS".equals(ch.tag)) {
                e.cause = new StringWithCustomTags(ch);
            } else if ("ADDR".equals(ch.tag)) {
                e.address = new Address();
                loadAddress(ch, e.address);
            } else if ("AGNC".equals(ch.tag)) {
                e.respAgency = new StringWithCustomTags(ch);
            } else if ("PHON".equals(ch.tag)) {
                e.phoneNumbers.add(new StringWithCustomTags(ch));
            } else if ("WWW".equals(ch.tag)) {
                e.wwwUrls.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but WWW URL was specified for " + e.type
                            + " family event on line " + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("FAX".equals(ch.tag)) {
                e.faxNumbers.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but fax number was specified for " + e.type
                            + " family event on line " + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("EMAIL".equals(ch.tag)) {
                e.emails.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but email was specified for " + e.type
                            + " family event on line " + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("HUSB".equals(ch.tag)) {
                e.husbandAge = new StringWithCustomTags(ch.children.get(0));
            } else if ("WIFE".equals(ch.tag)) {
                e.wifeAge = new StringWithCustomTags(ch.children.get(0));
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
            if ("NOTE".equals(ch.tag)) {
                loadNote(ch, fc.notes);
            } else if ("PEDI".equals(ch.tag)) {
                fc.pedigree = new StringWithCustomTags(ch);
            } else if ("ADOP".equals(ch.tag)) {
                fc.adoptedBy = AdoptedByWhichParent.valueOf(ch.value);
            } else if ("STAT".equals(ch.tag)) {
                fc.status = new StringWithCustomTags(ch);
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but status was specified for child-to-family link on line "
                            + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
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
            if ("NOTE".equals(ch.tag)) {
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
            if ("FORM".equals(ch.tag)) {
                currentFileRef.format = new StringWithCustomTags(ch);
            } else if ("TITL".equals(ch.tag)) {
                m.embeddedTitle = new StringWithCustomTags(ch);
            } else if ("FILE".equals(ch.tag)) {
                currentFileRef.referenceToFile = new StringWithCustomTags(ch);
            } else if ("NOTE".equals(ch.tag)) {
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
            if ("FILE".equals(ch.tag)) {
                fileTagCount++;
            }
            /*
             * Count the number of formats referenced per file - GEDCOM 5.5.1 has them as children of FILEs (so should
             * be zero), 5.5 pairs them with the single FILE tag (so should be one)
             */
            if ("FORM".equals(ch.tag)) {
                formTagCount++;
            }
        }
        if (fileTagCount > 1) {
            if (g55()) {
                warnings.add("GEDCOM version is 5.5, but multiple files referenced in multimedia reference on line "
                        + st.lineNum
                        + ", which is only allowed in 5.5.1. "
                        + "Data will be loaded, but cannot be written back out unless the GEDCOM version is changed to 5.5.1");
            }
        }
        if (formTagCount == 0) {
            if (g55()) {
                warnings.add("GEDCOM version is 5.5, but there is not a FORM tag in the multimedia link on line "
                        + st.lineNum
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
            if ("FILE".equals(ch.tag)) {
                FileReference fr = new FileReference();
                m.fileReferences.add(fr);
                fr.referenceToFile = new StringWithCustomTags(ch);
                if (ch.children.size() != 1) {
                    errors.add("Missing or multiple children nodes found under FILE node - GEDCOM 5.5.1 standard requires exactly 1 FORM node");
                }
                for (StringTree gch : ch.children) {
                    if ("FORM".equals(gch.tag)) {
                        fr.format = new StringWithCustomTags(gch.value);
                        for (StringTree ggch : ch.children) {
                            if ("MEDI".equals(ggch.tag)) {
                                fr.mediaType = new StringWithCustomTags(ggch);
                            } else {
                                unknownTag(ggch, fr);
                            }
                        }
                    } else {
                        unknownTag(gch, fr);
                    }
                }
            } else if ("TITL".equals(ch.tag)) {
                for (FileReference fr : m.fileReferences) {
                    fr.title = new StringWithCustomTags(ch.tag);
                }
            } else if ("NOTE".equals(ch.tag)) {
                loadNote(ch, m.notes);
                if (!g55()) {
                    warnings.add("Gedcom version was 5.5.1, but a NOTE was found on a multimedia link on line "
                            + ch.lineNum
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
            if ("VERS".equals(ch.tag)) {
                SupportedVersion vn = null;
                try {
                    vn = SupportedVersion.forString(ch.value);
                } catch (UnsupportedVersionException e) {
                    errors.add(e.getMessage());
                }
                gedcomVersion.versionNumber = vn;
            } else if ("FORM".equals(ch.tag)) {
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
            if ("SOUR".equals(ch.tag)) {
                header.sourceSystem = new SourceSystem();
                loadSourceSystem(ch, header.sourceSystem);
            } else if ("DEST".equals(ch.tag)) {
                header.destinationSystem = new StringWithCustomTags(ch);
            } else if ("DATE".equals(ch.tag)) {
                header.date = new StringWithCustomTags(ch);
                // one optional time subitem is the only possibility here
                if (!ch.children.isEmpty()) {
                    header.time = new StringWithCustomTags(ch.children.get(0));
                }
            } else if ("CHAR".equals(ch.tag)) {
                header.characterSet = new CharacterSet();
                header.characterSet.characterSetName = new StringWithCustomTags(ch);
                // one optional version subitem is the only possibility here
                if (!ch.children.isEmpty()) {
                    header.characterSet.versionNum = new StringWithCustomTags(ch.children.get(0));
                }
            } else if ("SUBM".equals(ch.tag)) {
                header.submitter = getSubmitter(ch.value);
            } else if ("FILE".equals(ch.tag)) {
                header.fileName = new StringWithCustomTags(ch);
            } else if ("GEDC".equals(ch.tag)) {
                header.gedcomVersion = new GedcomVersion();
                loadGedcomVersion(ch, header.gedcomVersion);
            } else if ("COPR".equals(ch.tag)) {
                loadMultiLinesOfText(ch, header.copyrightData, header);
                if (g55() && header.copyrightData.size() > 1) {
                    warnings.add("GEDCOM version is 5.5, but multiple lines of copyright data were specified, which is only allowed in GEDCOM 5.5.1. "
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("SUBN".equals(ch.tag)) {
                if (header.submission == null) {
                    /*
                     * There can only be one SUBMISSION record per GEDCOM, and it's found at the root level, but the
                     * HEAD structure has a cross-reference to that root-level structure, so we're setting it here (if
                     * it hasn't already been loaded, which it probably isn't yet)
                     */
                    header.submission = gedcom.submission;
                }
            } else if ("LANG".equals(ch.tag)) {
                header.language = new StringWithCustomTags(ch);
            } else if ("PLAC".equals(ch.tag)) {
                header.placeHierarchy = new StringWithCustomTags(ch.children.get(0));
            } else if ("NOTE".equals(ch.tag)) {
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
            if ("DATE".equals(ch.tag)) {
                sourceData.publishDate = new StringWithCustomTags(ch);
            } else if ("COPR".equals(ch.tag)) {
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
        Individual i = new Individual();
        gedcom.individuals.put(st.id, i);
        i.xref = st.id;
        for (StringTree ch : st.children) {
            if ("NAME".equals(ch.tag)) {
                PersonalName pn = new PersonalName();
                i.names.add(pn);
                loadPersonalName(ch, pn);
            } else if ("SEX".equals(ch.tag)) {
                i.sex = new StringWithCustomTags(ch);
            } else if ("ADDR".equals(ch.tag)) {
                i.address = new Address();
                loadAddress(ch, i.address);
            } else if ("PHON".equals(ch.tag)) {
                i.phoneNumbers.add(new StringWithCustomTags(ch));
            } else if ("WWW".equals(ch.tag)) {
                i.wwwUrls.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but WWW URL was specified for individual " + i.xref
                            + " on line " + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("FAX".equals(ch.tag)) {
                i.faxNumbers.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but fax was specified for individual " + i.xref + "on line "
                            + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("EMAIL".equals(ch.tag)) {
                i.emails.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but email was specified for individual " + i.xref + " on line "
                            + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if (IndividualEventType.isValidTag(ch.tag)) {
                loadIndividualEvent(ch, i.events);
            } else if (IndividualAttributeType.isValidTag(ch.tag)) {
                loadIndividualAttribute(ch, i.attributes);
            } else if (LdsIndividualOrdinanceType.isValidTag(ch.tag)) {
                loadLdsIndividualOrdinance(ch, i.ldsIndividualOrdinances);
            } else if ("NOTE".equals(ch.tag)) {
                loadNote(ch, i.notes);
            } else if ("CHAN".equals(ch.tag)) {
                i.changeDate = new ChangeDate();
                loadChangeDate(ch, i.changeDate);
            } else if ("RIN".equals(ch.tag)) {
                i.recIdNumber = new StringWithCustomTags(ch);
            } else if ("RFN".equals(ch.tag)) {
                i.permanentRecFileNumber = new StringWithCustomTags(ch);
            } else if ("OBJE".equals(ch.tag)) {
                loadMultimediaLink(ch, i.multimedia);
            } else if ("RESN".equals(ch.tag)) {
                i.restrictionNotice = new StringWithCustomTags(ch);
            } else if ("SOUR".equals(ch.tag)) {
                loadCitation(ch, i.citations);
            } else if ("ALIA".equals(ch.tag)) {
                i.aliases.add(new StringWithCustomTags(ch));
            } else if ("FAMS".equals(ch.tag)) {
                loadFamilyWhereSpouse(ch, i.familiesWhereSpouse);
            } else if ("FAMC".equals(ch.tag)) {
                loadFamilyWhereChild(ch, i.familiesWhereChild);
            } else if ("ASSO".equals(ch.tag)) {
                loadAssociation(ch, i.associations);
            } else if ("ANCI".equals(ch.tag)) {
                i.ancestorInterest.add(getSubmitter(ch.value));
            } else if ("DESI".equals(ch.tag)) {
                i.descendantInterest.add(getSubmitter(ch.value));
            } else if ("AFN".equals(ch.tag)) {
                i.ancestralFileNumber = new StringWithCustomTags(ch);
            } else if ("REFN".equals(ch.tag)) {
                UserReference u = new UserReference();
                i.userReferences.add(u);
                loadUserReference(ch, u);
            } else if ("SUBM".equals(ch.tag)) {
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
            warnings.add("FACT tag specified on a GEDCOM 5.5 file at line " + st.lineNum
                    + ", but FACT was not added until 5.5.1."
                    + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
        }
        a.description = new StringWithCustomTags(st.value);
        for (StringTree ch : st.children) {
            if ("TYPE".equals(ch.tag)) {
                a.subType = new StringWithCustomTags(ch);
            } else if ("DATE".equals(ch.tag)) {
                a.date = new StringWithCustomTags(ch);
            } else if ("PLAC".equals(ch.tag)) {
                a.place = new Place();
                loadPlace(ch, a.place);
            } else if ("AGE".equals(ch.tag)) {
                a.age = new StringWithCustomTags(ch);
            } else if ("CAUS".equals(ch.tag)) {
                a.cause = new StringWithCustomTags(ch);
            } else if ("SOUR".equals(ch.tag)) {
                loadCitation(ch, a.citations);
            } else if ("AGNC".equals(ch.tag)) {
                a.respAgency = new StringWithCustomTags(ch);
            } else if ("PHON".equals(ch.tag)) {
                a.phoneNumbers.add(new StringWithCustomTags(ch));
            } else if ("WWW".equals(ch.tag)) {
                a.wwwUrls.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but WWW URL was specified for " + a.type
                            + " attribute on line " + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("FAX".equals(ch.tag)) {
                a.faxNumbers.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but fax was specified for " + a.type + " attribute on line "
                            + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("EMAIL".equals(ch.tag)) {
                a.emails.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but email was specified for " + a.type + " attribute on line "
                            + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("ADDR".equals(ch.tag)) {
                a.address = new Address();
                loadAddress(ch, a.address);
            } else if ("OBJE".equals(ch.tag)) {
                loadMultimediaLink(ch, a.multimedia);
            } else if ("NOTE".equals(ch.tag)) {
                loadNote(ch, a.notes);
            } else if ("SOUR".equals(ch.tag)) {
                loadCitation(ch, a.citations);
            } else if ("CONC".equals(ch.tag)) {
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
        e.yNull = st.value;
        for (StringTree ch : st.children) {
            if ("TYPE".equals(ch.tag)) {
                e.subType = new StringWithCustomTags(ch);
            } else if ("DATE".equals(ch.tag)) {
                e.date = new StringWithCustomTags(ch);
            } else if ("PLAC".equals(ch.tag)) {
                e.place = new Place();
                loadPlace(ch, e.place);
            } else if ("OBJE".equals(ch.tag)) {
                loadMultimediaLink(ch, e.multimedia);
            } else if ("NOTE".equals(ch.tag)) {
                loadNote(ch, e.notes);
            } else if ("SOUR".equals(ch.tag)) {
                loadCitation(ch, e.citations);
            } else if ("AGE".equals(ch.tag)) {
                e.age = new StringWithCustomTags(ch);
            } else if ("CAUS".equals(ch.tag)) {
                e.cause = new StringWithCustomTags(ch);
            } else if ("ADDR".equals(ch.tag)) {
                e.address = new Address();
                loadAddress(ch, e.address);
            } else if ("AGNC".equals(ch.tag)) {
                e.respAgency = new StringWithCustomTags(ch);
            } else if ("RESN".equals(ch.tag)) {
                e.restrictionNotice = new StringWithCustomTags(ch);
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but restriction notice was specified for individual event on line "
                            + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("RELI".equals(ch.tag)) {
                e.religiousAffiliation = new StringWithCustomTags(ch);
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but religious affiliation was specified for individual event on line "
                            + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("PHON".equals(ch.tag)) {
                e.phoneNumbers.add(new StringWithCustomTags(ch));
            } else if ("WWW".equals(ch.tag)) {
                e.wwwUrls.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but WWW URL was specified on " + e.type + " event on line "
                            + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("FAX".equals(ch.tag)) {
                e.faxNumbers.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but fax was specified on " + e.type + " event on line "
                            + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("EMAIL".equals(ch.tag)) {
                e.emails.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but email was specified on " + e.type + " event on line "
                            + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("CONC".equals(ch.tag)) {
                if (e.description == null) {
                    e.description = new StringWithCustomTags(ch);
                } else {
                    e.description.value += ch.value;
                }
            } else if ("CONT".equals(ch.tag)) {
                if (e.description == null) {
                    e.description = new StringWithCustomTags(ch.value == null ? "" : ch.value);
                } else {
                    e.description.value += "\n" + ch.value;
                }
            } else if ("FAMC".equals(ch.tag)) {
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
            if ("DATE".equals(ch.tag)) {
                o.date = new StringWithCustomTags(ch);
            } else if ("PLAC".equals(ch.tag)) {
                o.place = new StringWithCustomTags(ch);
            } else if ("STAT".equals(ch.tag)) {
                o.status = new StringWithCustomTags(ch);
            } else if ("TEMP".equals(ch.tag)) {
                o.temple = new StringWithCustomTags(ch);
            } else if ("SOUR".equals(ch.tag)) {
                loadCitation(ch, o.citations);
            } else if ("NOTE".equals(ch.tag)) {
                loadNote(ch, o.notes);
            } else if ("FAMC".equals(ch.tag)) {
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
            if ("DATE".equals(ch.tag)) {
                o.date = new StringWithCustomTags(ch);
            } else if ("PLAC".equals(ch.tag)) {
                o.place = new StringWithCustomTags(ch);
            } else if ("STAT".equals(ch.tag)) {
                o.status = new StringWithCustomTags(ch);
            } else if ("TEMP".equals(ch.tag)) {
                o.temple = new StringWithCustomTags(ch);
            } else if ("SOUR".equals(ch.tag)) {
                loadCitation(ch, o.citations);
            } else if ("NOTE".equals(ch.tag)) {
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
            if ("CONT".equals(ch.tag)) {
                if (ch.value == null) {
                    listOfString.add("");
                } else {
                    listOfString.add(ch.value);
                }
            } else if ("CONC".equals(ch.tag)) {
                // If there's no value to concatenate, ignore it
                if (ch.value != null) {
                    if (listOfString.size() == 0) {
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
        Multimedia m = null;
        if (GedcomParserHelper.referencesAnotherNode(st)) {
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
            if ("FILE".equals(ch.tag)) {
                fileTagCount++;
            }
        }
        if (fileTagCount > 0) {
            if (g55()) {
                warnings.add("GEDCOM version was 5.5, but a 5.5.1-style multimedia record was found at line "
                        + st.lineNum
                        + ". "
                        + "Data will be loaded, but might have problems being written until the version is for the data is changed to 5.5.1");
            }
            loadMultimediaRecord551(st);
        } else {
            if (!g55()) {
                warnings.add("GEDCOM version is 5.5.1, but a 5.5-style multimedia record was found at line "
                        + st.lineNum
                        + ". "
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
            if ("FORM".equals(ch.tag)) {
                m.embeddedMediaFormat = new StringWithCustomTags(ch);
            } else if ("TITL".equals(ch.tag)) {
                m.embeddedTitle = new StringWithCustomTags(ch);
            } else if ("NOTE".equals(ch.tag)) {
                loadNote(ch, m.notes);
            } else if ("SOUR".equals(ch.tag)) {
                loadCitation(ch, m.citations);
            } else if ("BLOB".equals(ch.tag)) {
                loadMultiLinesOfText(ch, m.blob, m);
                if (!g55()) {
                    warnings.add("GEDCOM version is 5.5.1, but a BLOB tag was found at line " + ch.lineNum + ". "
                            + "Data will be loaded but will not be writeable unless GEDCOM version is changed to 5.5.1");
                }
            } else if ("OBJE".equals(ch.tag)) {
                List<Multimedia> continuedObjects = new ArrayList<Multimedia>();
                loadMultimediaLink(ch, continuedObjects);
                m.continuedObject = continuedObjects.get(0);
                if (!g55()) {
                    warnings.add("GEDCOM version is 5.5.1, but a chained OBJE tag was found at line " + ch.lineNum
                            + ". "
                            + "Data will be loaded but will not be writeable unless GEDCOM version is changed to 5.5.1");
                }
            } else if ("REFN".equals(ch.tag)) {
                UserReference u = new UserReference();
                m.userReferences.add(u);
                loadUserReference(ch, u);
            } else if ("RIN".equals(ch.tag)) {
                m.recIdNumber = new StringWithCustomTags(ch);
            } else if ("CHAN".equals(ch.tag)) {
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
            if ("FILE".equals(ch.tag)) {
                FileReference fr = new FileReference();
                m.fileReferences.add(fr);
                fr.referenceToFile = new StringWithCustomTags(ch);
                for (StringTree gch : ch.children) {
                    if ("FORM".equals(gch.tag)) {
                        fr.format = new StringWithCustomTags(gch.value);
                        if (gch.children.size() == 1) {
                            StringTree ggch = gch.children.get(0);
                            if ("TYPE".equals(ggch.tag)) {
                                fr.mediaType = new StringWithCustomTags(ggch);
                            } else {
                                unknownTag(ggch, fr);
                            }
                        }
                    } else if ("TITL".equals(gch.tag)) {
                        fr.title = new StringWithCustomTags(gch);
                    } else {
                        unknownTag(gch, fr);
                    }
                }
                if (fr.format == null) {
                    errors.add("FORM tag not found under FILE reference on line " + st.lineNum);
                }
            } else if ("NOTE".equals(ch.tag)) {
                loadNote(ch, m.notes);
            } else if ("SOUR".equals(ch.tag)) {
                loadCitation(ch, m.citations);
            } else if ("REFN".equals(ch.tag)) {
                UserReference u = new UserReference();
                m.userReferences.add(u);
                loadUserReference(ch, u);
            } else if ("RIN".equals(ch.tag)) {
                m.recIdNumber = new StringWithCustomTags(ch);
            } else if ("CHAN".equals(ch.tag)) {
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
        Note note = null;
        if (GedcomParserHelper.referencesAnotherNode(st)) {
            note = getNote(st.value);
            notes.add(note);
            return;
        } else if (st.id != null) {
            note = getNote(st.id);
        } else {
            note = new Note();
            notes.add(note);
        }
        note.lines.add(st.value);
        for (StringTree ch : st.children) {
            if ("CONC".equals(ch.tag)) {
                if (note.lines.size() == 0) {
                    note.lines.add(ch.value);
                } else {
                    String lastNote = note.lines.get(note.lines.size() - 1);
                    if (lastNote == null || lastNote.length() == 0) {
                        note.lines.set(note.lines.size() - 1, ch.value);
                    } else {
                        note.lines.set(note.lines.size() - 1, lastNote + ch.value);
                    }
                }
            } else if ("CONT".equals(ch.tag)) {
                note.lines.add(ch.value == null ? "" : ch.value);
            } else if ("SOUR".equals(ch.tag)) {
                loadCitation(ch, note.citations);
            } else if ("REFN".equals(ch.tag)) {
                UserReference u = new UserReference();
                note.userReferences.add(u);
                loadUserReference(ch, u);
            } else if ("RIN".equals(ch.tag)) {
                note.recIdNumber = new StringWithCustomTags(ch);
            } else if ("CHAN".equals(ch.tag)) {
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
            if ("NPFX".equals(ch.tag)) {
                pn.prefix = new StringWithCustomTags(ch);
            } else if ("GIVN".equals(ch.tag)) {
                pn.givenName = new StringWithCustomTags(ch);
            } else if ("NICK".equals(ch.tag)) {
                pn.nickname = new StringWithCustomTags(ch);
            } else if ("SPFX".equals(ch.tag)) {
                pn.surnamePrefix = new StringWithCustomTags(ch);
            } else if ("SURN".equals(ch.tag)) {
                pn.surname = new StringWithCustomTags(ch);
            } else if ("NSFX".equals(ch.tag)) {
                pn.suffix = new StringWithCustomTags(ch);
            } else if ("SOUR".equals(ch.tag)) {
                loadCitation(ch, pn.citations);
            } else if ("NOTE".equals(ch.tag)) {
                loadNote(ch, pn.notes);
            } else if ("ROMN".equals(ch.tag)) {
                PersonalNameVariation pnv = new PersonalNameVariation();
                pn.romanized.add(pnv);
                loadPersonalNameVariation(ch, pnv);
            } else if ("FONE".equals(ch.tag)) {
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
            if ("NPFX".equals(ch.tag)) {
                pnv.prefix = new StringWithCustomTags(ch);
            } else if ("GIVN".equals(ch.tag)) {
                pnv.givenName = new StringWithCustomTags(ch);
            } else if ("NICK".equals(ch.tag)) {
                pnv.nickname = new StringWithCustomTags(ch);
            } else if ("SPFX".equals(ch.tag)) {
                pnv.surnamePrefix = new StringWithCustomTags(ch);
            } else if ("SURN".equals(ch.tag)) {
                pnv.surname = new StringWithCustomTags(ch);
            } else if ("NSFX".equals(ch.tag)) {
                pnv.suffix = new StringWithCustomTags(ch);
            } else if ("SOUR".equals(ch.tag)) {
                loadCitation(ch, pnv.citations);
            } else if ("NOTE".equals(ch.tag)) {
                loadNote(ch, pnv.notes);
            } else if ("TYPE".equals(ch.tag)) {
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
            if ("FORM".equals(ch.tag)) {
                place.placeFormat = new StringWithCustomTags(ch);
            } else if ("SOUR".equals(ch.tag)) {
                loadCitation(ch, place.citations);
            } else if ("NOTE".equals(ch.tag)) {
                loadNote(ch, place.notes);
            } else if ("CONC".equals(ch.tag)) {
                place.placeName += new StringWithCustomTags(ch) == null ? "" : ch.value;
            } else if ("CONT".equals(ch.tag)) {
                place.placeName += "\n" + (ch.value == null ? "" : ch.value);
            } else if ("ROMN".equals(ch.tag)) {
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but a romanized variation was specified on a place on line "
                            + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
                NameVariation nv = new NameVariation();
                place.romanized.add(nv);
                nv.variation = ch.value;
                for (StringTree gch : ch.children) {
                    if ("TYPE".equals(gch.tag)) {
                        nv.variationType = new StringWithCustomTags(gch);
                    } else {
                        unknownTag(gch, nv);
                    }
                }
            } else if ("FONE".equals(ch.tag)) {
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but a phonetic variation was specified on a place on line "
                            + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
                NameVariation nv = new NameVariation();
                place.phonetic.add(nv);
                nv.variation = ch.value;
                for (StringTree gch : ch.children) {
                    if ("TYPE".equals(gch.tag)) {
                        nv.variationType = new StringWithCustomTags(gch);
                    } else {
                        unknownTag(gch, nv);
                    }
                }
            } else if ("MAP".equals(ch.tag)) {
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but a map coordinate was specified on a place on line "
                            + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
                for (StringTree gch : ch.children) {
                    if ("LAT".equals(gch.tag)) {
                        place.latitude = new StringWithCustomTags(gch);
                    } else if ("LONG".equals(gch.tag)) {
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
            if ("NAME".equals(ch.tag)) {
                r.name = new StringWithCustomTags(ch);
            } else if ("ADDR".equals(ch.tag)) {
                r.address = new Address();
                loadAddress(ch, r.address);
            } else if ("PHON".equals(ch.tag)) {
                r.phoneNumbers.add(new StringWithCustomTags(ch));
            } else if ("WWW".equals(ch.tag)) {
                r.wwwUrls.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but WWW URL was specified on repository " + r.xref
                            + " on line " + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("FAX".equals(ch.tag)) {
                r.faxNumbers.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but fax was specified on repository " + r.xref + " on line "
                            + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("EMAIL".equals(ch.tag)) {
                r.emails.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but email was specified on repository " + r.xref + " on line "
                            + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("NOTE".equals(ch.tag)) {
                loadNote(ch, r.notes);
            } else if ("REFN".equals(ch.tag)) {
                UserReference u = new UserReference();
                r.userReferences.add(u);
                loadUserReference(ch, u);
            } else if ("RIN".equals(ch.tag)) {
                r.recIdNumber = new StringWithCustomTags(ch);
            } else if ("CHAN".equals(ch.tag)) {
                r.changeDate = new ChangeDate();
                loadChangeDate(ch, r.changeDate);
            } else if ("CHAN".equals(ch.tag)) {
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
     * @param s
     *            the source which is referencing a repository
     * @return the RepositoryCitation loaded
     */
    private RepositoryCitation loadRepositoryCitation(StringTree st, Source s) {
        RepositoryCitation r = new RepositoryCitation();
        r.repositoryXref = st.value;
        for (StringTree ch : st.children) {
            if ("NOTE".equals(ch.tag)) {
                loadNote(ch, r.notes);
            } else if ("CALN".equals(ch.tag)) {
                SourceCallNumber scn = new SourceCallNumber();
                r.callNumbers.add(scn);
                scn.callNumber = new StringWithCustomTags(ch.value);
                for (StringTree gch : ch.children) {
                    if ("MEDI".equals(gch.tag)) {
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
        for (StringTree ch : st.children) {
            if ("HEAD".equals(ch.tag)) {
                loadHeader(ch);
            } else if ("SUBM".equals(ch.tag)) {
                loadSubmitter(ch);
            } else if ("INDI".equals(ch.tag)) {
                loadIndividual(ch);
            } else if ("SUBN".equals(ch.tag)) {
                loadSubmission(ch);
            } else if ("NOTE".equals(ch.tag)) {
                loadRootNote(ch);
            } else if ("FAM".equals(ch.tag)) {
                loadFamily(ch);
            } else if ("TRLR".equals(ch.tag)) {
                gedcom.trailer = new Trailer();
            } else if ("SOUR".equals(ch.tag)) {
                loadSource(ch);
            } else if ("REPO".equals(ch.tag)) {
                loadRepository(ch);
            } else if ("OBJE".equals(ch.tag)) {
                loadMultimediaRecord(ch);
            } else {
                unknownTag(ch, gedcom);
            }
        }
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
        if (dummyList.size() > 0) {
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
            if ("DATA".equals(ch.tag)) {
                s.data = new SourceData();
                loadSourceData(ch, s.data);
            } else if ("TITL".equals(ch.tag)) {
                loadMultiLinesOfText(ch, s.title, s);
            } else if ("PUBL".equals(ch.tag)) {
                loadMultiLinesOfText(ch, s.publicationFacts, s);
            } else if ("TEXT".equals(ch.tag)) {
                loadMultiLinesOfText(ch, s.sourceText, s);
            } else if ("ABBR".equals(ch.tag)) {
                s.sourceFiledBy = new StringWithCustomTags(ch);
            } else if ("AUTH".equals(ch.tag)) {
                loadMultiLinesOfText(ch, s.originatorsAuthors, s);
            } else if ("REPO".equals(ch.tag)) {
                s.repositoryCitation = loadRepositoryCitation(ch, s);
            } else if ("NOTE".equals(ch.tag)) {
                loadNote(ch, s.notes);
            } else if ("OBJE".equals(ch.tag)) {
                loadMultimediaLink(ch, s.multimedia);
            } else if ("REFN".equals(ch.tag)) {
                UserReference u = new UserReference();
                s.userReferences.add(u);
                loadUserReference(ch, u);
            } else if ("RIN".equals(ch.tag)) {
                s.recIdNumber = new StringWithCustomTags(ch);
            } else if ("CHAN".equals(ch.tag)) {
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
            if ("EVEN".equals(ch.tag)) {
                loadSourceDataEventRecorded(ch, data);
            } else if ("NOTE".equals(ch.tag)) {
                loadNote(ch, data.notes);
            } else if ("AGNC".equals(ch.tag)) {
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
            if ("DATE".equals(ch.tag)) {
                e.datePeriod = new StringWithCustomTags(ch);
            } else if ("PLAC".equals(ch.tag)) {
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
            if ("VERS".equals(ch.tag)) {
                sourceSystem.versionNum = new StringWithCustomTags(ch);
            } else if ("NAME".equals(ch.tag)) {
                sourceSystem.productName = new StringWithCustomTags(ch);
            } else if ("CORP".equals(ch.tag)) {
                sourceSystem.corporation = new Corporation();
                loadCorporation(ch, sourceSystem.corporation);
            } else if ("DATA".equals(ch.tag)) {
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
            if ("SUBM".equals(ch.tag)) {
                gedcom.submission.submitter = getSubmitter(ch.value);
            } else if ("FAMF".equals(ch.tag)) {
                gedcom.submission.nameOfFamilyFile = new StringWithCustomTags(ch);
            } else if ("TEMP".equals(ch.tag)) {
                gedcom.submission.templeCode = new StringWithCustomTags(ch);
            } else if ("ANCE".equals(ch.tag)) {
                gedcom.submission.ancestorsCount = new StringWithCustomTags(ch);
            } else if ("DESC".equals(ch.tag)) {
                gedcom.submission.descendantsCount = new StringWithCustomTags(ch);
            } else if ("ORDI".equals(ch.tag)) {
                gedcom.submission.ordinanceProcessFlag = new StringWithCustomTags(ch);
            } else if ("RIN".equals(ch.tag)) {
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
            if ("NAME".equals(ch.tag)) {
                submitter.name = new StringWithCustomTags(ch);
            } else if ("ADDR".equals(ch.tag)) {
                submitter.address = new Address();
                loadAddress(ch, submitter.address);
            } else if ("PHON".equals(ch.tag)) {
                submitter.phoneNumbers.add(new StringWithCustomTags(ch));
            } else if ("WWW".equals(ch.tag)) {
                submitter.wwwUrls.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but WWW URL number was specified on submitter on line "
                            + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("FAX".equals(ch.tag)) {
                submitter.faxNumbers.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but fax number was specified on submitter on line "
                            + ch.lineNum + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("EMAIL".equals(ch.tag)) {
                submitter.emails.add(new StringWithCustomTags(ch));
                if (g55()) {
                    warnings.add("GEDCOM version is 5.5 but email was specified on submitter on line " + ch.lineNum
                            + ", which is a GEDCOM 5.5.1 feature."
                            + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                }
            } else if ("LANG".equals(ch.tag)) {
                submitter.languagePref.add(new StringWithCustomTags(ch));
            } else if ("CHAN".equals(ch.tag)) {
                submitter.changeDate = new ChangeDate();
                loadChangeDate(ch, submitter.changeDate);
            } else if ("OBJE".equals(ch.tag)) {
                loadMultimediaLink(ch, submitter.multimedia);
            } else if ("RIN".equals(ch.tag)) {
                submitter.recIdNumber = new StringWithCustomTags(ch);
            } else if ("RFN".equals(ch.tag)) {
                submitter.regFileNumber = new StringWithCustomTags(ch);
            } else if ("NOTE".equals(ch.tag)) {
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
     * Default handler for a tag that the parser was not expecting to see. If the tag begins with an underscore, it is a
     * user-defined tag, which is stored in the customTags collection of the passed in element, and returns. If it does
     * not begin with an underscore, it is presumably a real tag from the spec and should be processed, so that would
     * indicate a bug in the parser, or a bad tag that indicates a data error.
     * 
     * @param node
     *            the node containing the unknown tag.
     */
    private void unknownTag(StringTree node, AbstractElement element) {
        if (node.tag.startsWith("_")) {
            element.customTags.add(node);
            return;
        }
        unknownTagNoUserDefinedTagsAllowed(node);
    }

    /**
     * This is the handler for when a node is read that is not a user-defined tag, but that the parser does not
     * recognize as valid or does not have a handler for...either of which is bad, so it gets added to the errors
     * collection.
     * 
     * @param node
     *            the node with the unrecognized tag
     */
    private void unknownTagNoUserDefinedTagsAllowed(StringTree node) {
        StringBuilder sb = new StringBuilder("Line " + node.lineNum + ": Cannot handle tag ");
        sb.append(node.tag);
        StringTree st = node;
        while (st.parent != null) {
            st = st.parent;
            sb.append(", child of ").append(st.tag);
            if (st.id != null) {
                sb.append(" ").append(st.id);
            }
            sb.append(" on line ").append(st.lineNum);
        }
        errors.add(sb.toString());
    }

    /**
     * A convenience method to write all the parsing errors and warnings to System.err.
     */
    public void dumpErrorsAndWarnings() {
        if (errors.isEmpty()) {
            System.out.println("No errors.");
        } else {
            System.out.println("Errors:");
            for (String e : errors) {
                System.out.println("  " + e);
            }
        }
        if (warnings.isEmpty()) {
            System.out.println("No warnings.");
        } else {
            System.out.println("Warnings:");
            for (String w : warnings) {
                System.out.println("  " + w);
            }
        }
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
        if (verbose) {
            System.out.println("Loading and parsing GEDCOM from input stream");
        }
        StringTree stringTree = GedcomParserHelper.readStream(stream);
        loadRootItems(stringTree);
        if (verbose) {
            dumpErrorsAndWarnings();
        }
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
        if (verbose) {
            System.out.println("Loading and parsing GEDCOM from file " + filename);
        }
        StringTree stringTree = GedcomParserHelper.readFile(filename);
        loadRootItems(stringTree);
        if (verbose) {
            dumpErrorsAndWarnings();
        }
    }

}
