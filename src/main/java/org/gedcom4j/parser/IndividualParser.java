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
package org.gedcom4j.parser;

import java.util.List;

import org.gedcom4j.model.AbstractCitation;
import org.gedcom4j.model.Address;
import org.gedcom4j.model.Association;
import org.gedcom4j.model.ChangeDate;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.FamilyChild;
import org.gedcom4j.model.FamilySpouse;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualAttribute;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.LdsIndividualOrdinance;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.Note;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.model.StringTree;
import org.gedcom4j.model.StringWithCustomTags;
import org.gedcom4j.model.UserReference;
import org.gedcom4j.model.enumerations.IndividualAttributeType;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.model.enumerations.LdsIndividualOrdinanceType;

/**
 * Parser for {@link Individual} records
 * 
 * @author frizbog
 */
class IndividualParser extends AbstractParser<Individual> {

    /**
     * Constructor
     * 
     * @param gedcomParser
     *            a reference to the root {@link GedcomParser}
     * @param stringTree
     *            {@link StringTree} to be parsed
     * @param loadInto
     *            the object we are loading data into
     */
    IndividualParser(GedcomParser gedcomParser, StringTree stringTree, Individual loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("PMD.NcssMethodCount")
    void parse() {
        if (stringTree.getChildren() != null) {
            for (StringTree ch : stringTree.getChildren()) {
                if (Tag.NAME.equalsText(ch.getTag())) {
                    PersonalName pn = new PersonalName();
                    loadInto.getNames(true).add(pn);
                    new PersonalNameParser(gedcomParser, ch, pn).parse();
                } else if (Tag.SEX.equalsText(ch.getTag())) {
                    loadInto.setSex(new StringWithCustomTags(ch));
                } else if (Tag.ADDRESS.equalsText(ch.getTag())) {
                    Address address = new Address();
                    loadInto.setAddress(address);
                    new AddressParser(gedcomParser, ch, address).parse();
                } else if (Tag.PHONE.equalsText(ch.getTag())) {
                    loadInto.getPhoneNumbers(true).add(new StringWithCustomTags(ch));
                } else if (Tag.WEB_ADDRESS.equalsText(ch.getTag())) {
                    loadInto.getWwwUrls(true).add(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but WWW URL was specified for individual " + loadInto.getXref()
                                + " on line " + ch.getLineNum() + ", which is a GEDCOM 5.5.1 feature."
                                + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.FAX.equalsText(ch.getTag())) {
                    loadInto.getFaxNumbers(true).add(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but fax was specified for individual " + loadInto.getXref() + "on line "
                                + ch.getLineNum() + ", which is a GEDCOM 5.5.1 feature."
                                + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.EMAIL.equalsText(ch.getTag())) {
                    loadInto.getEmails(true).add(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but email was specified for individual " + loadInto.getXref()
                                + " on line " + ch.getLineNum() + ", which is a GEDCOM 5.5.1 feature."
                                + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (IndividualEventType.isValidTag(ch.getTag())) {
                    IndividualEvent event = new IndividualEvent();
                    loadInto.getEvents(true).add(event);
                    new IndividualEventParser(gedcomParser, ch, event).parse();
                } else if (IndividualAttributeType.isValidTag(ch.getTag())) {
                    IndividualAttribute a = new IndividualAttribute();
                    loadInto.getAttributes(true).add(a);
                    new IndividualAttributeParser(gedcomParser, ch, a).parse();
                } else if (LdsIndividualOrdinanceType.isValidTag(ch.getTag())) {
                    LdsIndividualOrdinance ord = new LdsIndividualOrdinance();
                    loadInto.getLdsIndividualOrdinances(true).add(ord);
                    new LdsIndividualOrdinanceParser(gedcomParser, ch, ord).parse();
                } else if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = loadInto.getNotes(true);
                    new NoteListParser(gedcomParser, ch, notes).parse();
                } else if (Tag.CHANGED_DATETIME.equalsText(ch.getTag())) {
                    ChangeDate changeDate = new ChangeDate();
                    loadInto.setChangeDate(changeDate);
                    new ChangeDateParser(gedcomParser, ch, changeDate).parse();
                } else if (Tag.RECORD_ID_NUMBER.equalsText(ch.getTag())) {
                    loadInto.setRecIdNumber(new StringWithCustomTags(ch));
                } else if (Tag.REGISTRATION_FILE_NUMBER.equalsText(ch.getTag())) {
                    loadInto.setPermanentRecFileNumber(new StringWithCustomTags(ch));
                } else if (Tag.OBJECT_MULTIMEDIA.equalsText(ch.getTag())) {
                    List<Multimedia> multimedia = loadInto.getMultimedia(true);
                    new MultimediaLinkParser(gedcomParser, ch, multimedia).parse();
                } else if (Tag.RESTRICTION.equalsText(ch.getTag())) {
                    loadInto.setRestrictionNotice(new StringWithCustomTags(ch));
                } else if (Tag.SOURCE.equalsText(ch.getTag())) {
                    List<AbstractCitation> citations = loadInto.getCitations(true);
                    new CitationListParser(gedcomParser, ch, citations).parse();
                } else if (Tag.ALIAS.equalsText(ch.getTag())) {
                    loadInto.getAliases(true).add(new StringWithCustomTags(ch));
                } else if (Tag.FAMILY_WHERE_SPOUSE.equalsText(ch.getTag())) {
                    loadFamilyWhereSpouse(ch, loadInto.getFamiliesWhereSpouse(true));
                } else if (Tag.FAMILY_WHERE_CHILD.equalsText(ch.getTag())) {
                    FamilyChild fc = new FamilyChild();
                    loadInto.getFamiliesWhereChild(true).add(fc);
                    new FamilyChildParser(gedcomParser, ch, fc).parse();
                } else if (Tag.ASSOCIATION.equalsText(ch.getTag())) {
                    Association a = new Association();
                    loadInto.getAssociations(true).add(a);
                    new AssociationParser(gedcomParser, ch, a).parse();
                } else if (Tag.ANCESTOR_INTEREST.equalsText(ch.getTag())) {
                    loadInto.getAncestorInterest(true).add(getSubmitter(ch.getValue()));
                } else if (Tag.DESCENDANT_INTEREST.equalsText(ch.getTag())) {
                    loadInto.getDescendantInterest(true).add(getSubmitter(ch.getValue()));
                } else if (Tag.ANCESTRAL_FILE_NUMBER.equalsText(ch.getTag())) {
                    loadInto.setAncestralFileNumber(new StringWithCustomTags(ch));
                } else if (Tag.REFERENCE.equalsText(ch.getTag())) {
                    UserReference u = new UserReference();
                    loadInto.getUserReferences(true).add(u);
                    new UserReferenceParser(gedcomParser, ch, u).parse();
                } else if (Tag.SUBMITTER.equalsText(ch.getTag())) {
                    loadInto.getSubmitters(true).add(getSubmitter(ch.getValue()));
                } else {
                    unknownTag(ch, loadInto);
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
