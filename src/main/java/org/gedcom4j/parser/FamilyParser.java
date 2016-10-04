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
import org.gedcom4j.model.ChangeDate;
import org.gedcom4j.model.CustomFact;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.FamilyEvent;
import org.gedcom4j.model.IndividualReference;
import org.gedcom4j.model.LdsSpouseSealing;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.NoteStructure;
import org.gedcom4j.model.StringTree;
import org.gedcom4j.model.SubmitterReference;
import org.gedcom4j.model.UserReference;
import org.gedcom4j.model.enumerations.FamilyEventType;

/**
 * Parser for {@link Family} objects
 * 
 * @author frizbog
 */
class FamilyParser extends AbstractParser<Family> {

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
    FamilyParser(GedcomParser gedcomParser, StringTree stringTree, Family loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    @Override
    void parse() {
        if (stringTree.getChildren() != null) {
            for (StringTree ch : stringTree.getChildren()) {
                if (Tag.HUSBAND.equalsText(ch.getTag())) {
                    IndividualReference husband = new IndividualReference(getIndividual(ch.getValue()));
                    loadInto.setHusband(husband);
                    if (ch.getChildren() != null) {
                        for (StringTree gch : ch.getChildren()) {
                            CustomFact cf = new CustomFact(gch.getTag());
                            husband.getCustomFacts(true).add(cf);
                            new CustomFactParser(gedcomParser, gch, cf).parse();
                        }
                    }
                } else if (Tag.WIFE.equalsText(ch.getTag())) {
                    IndividualReference wife = new IndividualReference(getIndividual(ch.getValue()));
                    loadInto.setWife(wife);
                    if (ch.getChildren() != null) {
                        for (StringTree gch : ch.getChildren()) {
                            CustomFact cf = new CustomFact(gch.getTag());
                            wife.getCustomFacts(true).add(cf);
                            new CustomFactParser(gedcomParser, gch, cf).parse();
                        }
                    }
                } else if (Tag.CHILD.equalsText(ch.getTag())) {
                    IndividualReference child = new IndividualReference(getIndividual(ch.getValue()));
                    loadInto.getChildren(true).add(child);
                    if (ch.getChildren() != null) {
                        for (StringTree gch : ch.getChildren()) {
                            CustomFact cf = new CustomFact(gch.getTag());
                            child.getCustomFacts(true).add(cf);
                            new CustomFactParser(gedcomParser, gch, cf).parse();
                        }
                    }
                } else if (Tag.NUM_CHILDREN.equalsText(ch.getTag())) {
                    loadInto.setNumChildren(parseStringWithCustomFacts(ch));
                } else if (Tag.SOURCE.equalsText(ch.getTag())) {
                    List<AbstractCitation> citations = loadInto.getCitations(true);
                    new CitationListParser(gedcomParser, ch, citations).parse();
                } else if (Tag.OBJECT_MULTIMEDIA.equalsText(ch.getTag())) {
                    List<Multimedia> multimedia = loadInto.getMultimedia(true);
                    new MultimediaLinkParser(gedcomParser, ch, multimedia).parse();
                } else if (Tag.RECORD_ID_NUMBER.equalsText(ch.getTag())) {
                    loadInto.setAutomatedRecordId(parseStringWithCustomFacts(ch));
                } else if (Tag.CHANGED_DATETIME.equalsText(ch.getTag())) {
                    ChangeDate changeDate = new ChangeDate();
                    loadInto.setChangeDate(changeDate);
                    new ChangeDateParser(gedcomParser, ch, changeDate).parse();
                } else if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<NoteStructure> notes = loadInto.getNoteStructures(true);
                    new NoteStructureListParser(gedcomParser, ch, notes).parse();
                } else if (Tag.RESTRICTION.equalsText(ch.getTag())) {
                    loadInto.setRestrictionNotice(parseStringWithCustomFacts(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but restriction notice was specified for family on line " + ch
                                .getLineNum() + " , which is a GEDCOM 5.5.1 feature."
                                + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.REGISTRATION_FILE_NUMBER.equalsText(ch.getTag())) {
                    loadInto.setRecFileNumber(parseStringWithCustomFacts(ch));
                } else if (FamilyEventType.isValidTag(ch.getTag())) {
                    FamilyEvent event = new FamilyEvent();
                    loadInto.getEvents(true).add(event);
                    new FamilyEventParser(gedcomParser, ch, event).parse();
                } else if (Tag.SEALING_SPOUSE.equalsText(ch.getTag())) {
                    LdsSpouseSealing ldsss = new LdsSpouseSealing();
                    loadInto.getLdsSpouseSealings(true).add(ldsss);
                    new LdsSpouseSealingParser(gedcomParser, ch, ldsss).parse();
                } else if (Tag.SUBMITTER.equalsText(ch.getTag())) {
                    loadInto.getSubmitters(true).add(new SubmitterReference(getSubmitter(ch.getValue())));
                } else if (Tag.REFERENCE.equalsText(ch.getTag())) {
                    UserReference u = new UserReference();
                    loadInto.getUserReferences(true).add(u);
                    new UserReferenceParser(gedcomParser, ch, u).parse();
                } else {
                    unknownTag(ch, loadInto);
                }
            }
        }
    }

}
