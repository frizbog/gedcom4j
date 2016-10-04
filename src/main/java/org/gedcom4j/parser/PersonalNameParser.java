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
import org.gedcom4j.model.NoteStructure;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.model.PersonalNameVariation;
import org.gedcom4j.model.StringTree;

/**
 * @author frizbog
 *
 */
class PersonalNameParser extends AbstractParser<PersonalName> {

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
    PersonalNameParser(GedcomParser gedcomParser, StringTree stringTree, PersonalName loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    @Override
    void parse() {
        loadInto.setBasic(stringTree.getValue());
        if (stringTree.getChildren() != null) {
            for (StringTree ch : stringTree.getChildren()) {
                if (Tag.NAME_PREFIX.equalsText(ch.getTag())) {
                    loadInto.setPrefix(parseStringWithCustomFacts(ch));
                } else if (Tag.GIVEN_NAME.equalsText(ch.getTag())) {
                    loadInto.setGivenName(parseStringWithCustomFacts(ch));
                } else if (Tag.NICKNAME.equalsText(ch.getTag())) {
                    loadInto.setNickname(parseStringWithCustomFacts(ch));
                } else if (Tag.SURNAME_PREFIX.equalsText(ch.getTag())) {
                    loadInto.setSurnamePrefix(parseStringWithCustomFacts(ch));
                } else if (Tag.SURNAME.equalsText(ch.getTag())) {
                    loadInto.setSurname(parseStringWithCustomFacts(ch));
                } else if (Tag.NAME_SUFFIX.equalsText(ch.getTag())) {
                    loadInto.setSuffix(parseStringWithCustomFacts(ch));
                } else if (Tag.SOURCE.equalsText(ch.getTag())) {
                    List<AbstractCitation> citations = loadInto.getCitations(true);
                    new CitationListParser(gedcomParser, ch, citations).parse();
                } else if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<NoteStructure> notes = loadInto.getNoteStructures(true);
                    new NoteStructureListParser(gedcomParser, ch, notes).parse();
                } else if (Tag.ROMANIZED.equalsText(ch.getTag())) {
                    PersonalNameVariation pnv = new PersonalNameVariation();
                    loadInto.getRomanized(true).add(pnv);
                    loadPersonalNameVariation(ch, pnv);
                } else if (Tag.PHONETIC.equalsText(ch.getTag())) {
                    PersonalNameVariation pnv = new PersonalNameVariation();
                    loadInto.getPhonetic(true).add(pnv);
                    loadPersonalNameVariation(ch, pnv);
                } else {
                    unknownTag(ch, loadInto);
                }
            }
        }

    }

    /**
     * Load a personal name variation (romanization or phonetic version) from a string tree node
     * 
     * @param romnOrPhon
     *            the ROMN or PHON string tree node to load from
     * @param pnv
     *            the personal name variation to fill in
     */
    private void loadPersonalNameVariation(StringTree romnOrPhon, PersonalNameVariation pnv) {
        pnv.setVariation(romnOrPhon.getValue());
        if (romnOrPhon.getChildren() != null) {
            for (StringTree ch : romnOrPhon.getChildren()) {
                if (Tag.NAME_PREFIX.equalsText(ch.getTag())) {
                    pnv.setPrefix(parseStringWithCustomFacts(ch));
                } else if (Tag.GIVEN_NAME.equalsText(ch.getTag())) {
                    pnv.setGivenName(parseStringWithCustomFacts(ch));
                } else if (Tag.NICKNAME.equalsText(ch.getTag())) {
                    pnv.setNickname(parseStringWithCustomFacts(ch));
                } else if (Tag.SURNAME_PREFIX.equalsText(ch.getTag())) {
                    pnv.setSurnamePrefix(parseStringWithCustomFacts(ch));
                } else if (Tag.SURNAME.equalsText(ch.getTag())) {
                    pnv.setSurname(parseStringWithCustomFacts(ch));
                } else if (Tag.NAME_SUFFIX.equalsText(ch.getTag())) {
                    pnv.setSuffix(parseStringWithCustomFacts(ch));
                } else if (Tag.SOURCE.equalsText(ch.getTag())) {
                    List<AbstractCitation> citations = pnv.getCitations(true);
                    new CitationListParser(gedcomParser, ch, citations).parse();
                } else if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<NoteStructure> notes = pnv.getNoteStructures(true);
                    new NoteStructureListParser(gedcomParser, ch, notes).parse();
                } else if (Tag.TYPE.equalsText(ch.getTag())) {
                    pnv.setVariationType(parseStringWithCustomFacts(ch));
                } else {
                    unknownTag(ch, pnv);
                }
            }
        }
    }

}
