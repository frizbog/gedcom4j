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

import org.gedcom4j.model.Family;
import org.gedcom4j.model.FamilyChild;
import org.gedcom4j.model.Note;
import org.gedcom4j.model.StringTree;
import org.gedcom4j.model.StringWithCustomTags;
import org.gedcom4j.model.enumerations.AdoptedByWhichParent;

/**
 * Parser for {@link FamilyChild} objects
 * 
 * @author frizbog
 */
class FamilyChildParser extends AbstractParser<FamilyChild> {

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
    FamilyChildParser(GedcomParser gedcomParser, StringTree stringTree, FamilyChild loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    @Override
    void parse() {
        Family f = getFamily(stringTree.getValue());
        loadInto.setFamily(f);
        if (stringTree.getChildren() != null) {
            for (StringTree ch : stringTree.getChildren()) {
                if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = loadInto.getNotes(true);
                    new NoteListParser(gedcomParser, ch, notes).parse();
                } else if (Tag.PEDIGREE.equalsText(ch.getTag())) {
                    loadInto.setPedigree(new StringWithCustomTags(ch));
                } else if (Tag.ADOPTION.equalsText(ch.getTag())) {
                    loadInto.setAdoptedBy(AdoptedByWhichParent.valueOf(ch.getValue()));
                } else if (Tag.STATUS.equalsText(ch.getTag())) {
                    loadInto.setStatus(new StringWithCustomTags(ch));
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but status was specified for child-to-family link on line " + ch
                                .getLineNum() + ", which is a GEDCOM 5.5.1 feature."
                                + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else {
                    unknownTag(ch, loadInto);
                }
            }
        }
    }

}
