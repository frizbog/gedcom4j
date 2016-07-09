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

import org.gedcom4j.model.*;

/**
 * Parser for {@link IndividualEvent} object nodes
 * 
 * @author frizbog
 */
class IndividualEventParser extends AbstractParser<IndividualEvent> {

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
    IndividualEventParser(GedcomParser gedcomParser, StringTree stringTree, IndividualEvent loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    @Override
    void parse() {
        loadInto.setType(IndividualEventType.getFromTag(stringTree.getTag()));
        if ("Y".equals(stringTree.getValue())) {
            loadInto.setyNull(stringTree.getValue());
            loadInto.setDescription(null);
        } else if (stringTree.getValue() == null || stringTree.getValue().trim().length() == 0) {
            loadInto.setyNull(null);
            loadInto.setDescription(null);
        } else {
            loadInto.setyNull(null);
            loadInto.setDescription(new StringWithCustomTags(stringTree.getValue()));
            addWarning(stringTree.getTag() + " tag had description rather than [Y|<NULL>] - violates standard");
        }
        if (stringTree.getChildren() != null) {
            for (StringTree ch : stringTree.getChildren()) {
                if (Tag.TYPE.equalsText(ch.getTag())) {
                    loadInto.setSubType(new StringWithCustomTags(ch));
                } else if (Tag.DATE.equalsText(ch.getTag())) {
                    loadInto.setDate(new StringWithCustomTags(ch));
                } else if (Tag.PLACE.equalsText(ch.getTag())) {
                    Place place = new Place();
                    loadInto.setPlace(place);
                    new PlaceParser(gedcomParser, ch, place).parse();
                } else if (Tag.OBJECT_MULTIMEDIA.equalsText(ch.getTag())) {
                    List<Multimedia> multimedia = loadInto.getMultimedia(true);
                    new MultimediaLinkParser(gedcomParser, ch, multimedia).parse();
                } else if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = loadInto.getNotes(true);
                    new NoteListParser(gedcomParser, ch, notes).parse();
                } else if (Tag.SOURCE.equalsText(ch.getTag())) {
                    List<AbstractCitation> citations = loadInto.getCitations(true);
                    new CitationListParser(gedcomParser, ch, citations).parse();
                } else if (Tag.AGE.equalsText(ch.getTag())) {
                    loadInto.setAge(new StringWithCustomTags(ch));
                } else if (Tag.CAUSE.equalsText(ch.getTag())) {
                    loadInto.setCause(new StringWithCustomTags(ch));
                } else if (Tag.ADDRESS.equalsText(ch.getTag())) {
                    Address address = new Address();
                    loadInto.setAddress(address);
                    new AddressParser(gedcomParser, ch, address).parse();
                } else if (Tag.AGENCY.equalsText(ch.getTag())) {
                    loadInto.setRespAgency(new StringWithCustomTags(ch));
                } else if (Tag.RESTRICTION.equalsText(ch.getTag())) {
                    loadInto.setRestrictionNotice(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but restriction notice was specified for individual event on line " + ch.getLineNum()
                                + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.RELIGION.equalsText(ch.getTag())) {
                    loadInto.setReligiousAffiliation(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but religious affiliation was specified for individual event on line " + ch.getLineNum()
                                + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.PHONE.equalsText(ch.getTag())) {
                    loadInto.getPhoneNumbers(true).add(new StringWithCustomTags(ch));
                } else if (Tag.WEB_ADDRESS.equalsText(ch.getTag())) {
                    loadInto.getWwwUrls(true).add(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but WWW URL was specified on " + loadInto.getType() + " event on line " + ch.getLineNum()
                                + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.FAX.equalsText(ch.getTag())) {
                    loadInto.getFaxNumbers(true).add(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but fax was specified on " + loadInto.getType() + " event on line " + ch.getLineNum()
                                + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.EMAIL.equalsText(ch.getTag())) {
                    loadInto.getEmails(true).add(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but email was specified on " + loadInto.getType() + " event on line " + ch.getLineNum()
                                + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.CONCATENATION.equalsText(ch.getTag())) {
                    if (loadInto.getDescription() == null) {
                        loadInto.setDescription(new StringWithCustomTags(ch));
                    } else {
                        loadInto.getDescription().setValue(loadInto.getDescription().getValue() + ch.getValue());
                    }
                } else if (Tag.CONTINUATION.equalsText(ch.getTag())) {
                    if (loadInto.getDescription() == null) {
                        loadInto.setDescription(new StringWithCustomTags(ch.getValue() == null ? "" : ch.getValue()));
                    } else {
                        loadInto.getDescription().setValue(loadInto.getDescription().getValue() + "\n" + ch.getValue());
                    }
                } else if (Tag.FAMILY_WHERE_CHILD.equalsText(ch.getTag())) {
                    FamilyChild fc = new FamilyChild();
                    loadInto.setFamily(fc);
                    new FamilyChildParser(gedcomParser, ch, fc).parse();
                } else {
                    unknownTag(ch, loadInto);
                }
            }
        }

    }

}
