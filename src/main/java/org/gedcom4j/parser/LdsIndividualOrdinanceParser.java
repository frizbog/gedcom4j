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

import java.util.List;

import org.gedcom4j.model.*;

/**
 * Parser for {@link LdsIndividualOrdinance} records
 * 
 * @author frizbog
 */
class LdsIndividualOrdinanceParser extends AbstractParser<LdsIndividualOrdinance> {

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
    LdsIndividualOrdinanceParser(GedcomParser gedcomParser, StringTree stringTree, LdsIndividualOrdinance loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    @Override
    void parse() {
        loadInto.setType(LdsIndividualOrdinanceType.getFromTag(stringTree.getTag()));
        loadInto.setyNull(stringTree.getValue());
        if (stringTree.getChildren() != null) {
            for (StringTree ch : stringTree.getChildren()) {
                if (Tag.DATE.equalsText(ch.getTag())) {
                    loadInto.setDate(new StringWithCustomTags(ch));
                } else if (Tag.PLACE.equalsText(ch.getTag())) {
                    loadInto.setPlace(new StringWithCustomTags(ch));
                } else if (Tag.STATUS.equalsText(ch.getTag())) {
                    loadInto.setStatus(new StringWithCustomTags(ch));
                } else if (Tag.TEMPLE.equalsText(ch.getTag())) {
                    loadInto.setTemple(new StringWithCustomTags(ch));
                } else if (Tag.SOURCE.equalsText(ch.getTag())) {
                    List<AbstractCitation> citations = loadInto.getCitations(true);
                    new CitationListParser(gedcomParser, ch, citations).parse();
                } else if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = loadInto.getNotes(true);
                    new NoteListParser(gedcomParser, ch, notes).parse();
                } else if (Tag.FAMILY_WHERE_CHILD.equalsText(ch.getTag())) {
                    FamilyChild fc = new FamilyChild();
                    loadInto.setFamilyWhereChild(fc);
                    new FamilyChildParser(gedcomParser, ch, fc).parse();
                } else {
                    unknownTag(ch, loadInto);
                }
            }
        }
    }

}
