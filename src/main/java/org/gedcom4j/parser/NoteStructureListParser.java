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

import org.gedcom4j.model.NoteRecord;
import org.gedcom4j.model.NoteStructure;
import org.gedcom4j.model.StringTree;

/**
 * Parser for a list of {@link NoteStructure} objects
 * 
 * @author frizbog
 *
 */
class NoteStructureListParser extends AbstractParser<List<NoteStructure>> {

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
    NoteStructureListParser(GedcomParser gedcomParser, StringTree stringTree, List<NoteStructure> loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void parse() {
        NoteStructure noteStructure = new NoteStructure();
        if (stringTree.getXref() == null && referencesAnotherNode(stringTree)) {
            noteStructure.setNoteReference(getNote(stringTree.getValue()));
            loadInto.add(noteStructure);
            remainingChildrenAreCustomTags(stringTree, noteStructure);
            return;
        } else if (stringTree.getXref() == null) {
            noteStructure = new NoteStructure();
            loadInto.add(noteStructure);
            remainingChildrenAreCustomTags(stringTree, noteStructure);
            return;
        }
        noteStructure.getLines(true).add(stringTree.getValue());
        if (stringTree.getChildren() != null) {
            for (StringTree ch : stringTree.getChildren()) {
                if (Tag.CONCATENATION.equalsText(ch.getTag())) {
                    if (noteStructure.getLines().isEmpty()) {
                        noteStructure.getLines(true).add(ch.getValue());
                    } else {
                        String lastNote = noteStructure.getLines().get(noteStructure.getLines().size() - 1);
                        if (lastNote == null || lastNote.length() == 0) {
                            noteStructure.getLines().set(noteStructure.getLines().size() - 1, ch.getValue());
                        } else {
                            noteStructure.getLines().set(noteStructure.getLines().size() - 1, lastNote + ch.getValue());
                        }
                    }
                } else if (Tag.CONTINUATION.equalsText(ch.getTag())) {
                    noteStructure.getLines(true).add(ch.getValue() == null ? "" : ch.getValue());
                } else {
                    unknownTag(ch, noteStructure);
                }
            }
        }
    }

    /**
     * Get a {@link NoteRecord} by its xref, adding it to the gedcom collection of notes if needed.
     * 
     * @param xref
     *            the xref of the note
     * @return the note with the specified xref
     */
    private NoteRecord getNote(String xref) {
        NoteRecord note;
        note = gedcomParser.getGedcom().getNotes().get(xref);
        if (note == null) {
            note = new NoteRecord(xref);
            gedcomParser.getGedcom().getNotes().put(xref, note);
        }
        return note;
    }

}
