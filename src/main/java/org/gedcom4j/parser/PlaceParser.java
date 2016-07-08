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
 * Parser for {@link Place} objects
 * 
 * @author frizbog
 */
class PlaceParser extends AbstractParser<Place> {

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
    PlaceParser(GedcomParser gedcomParser, StringTree stringTree, Place loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    @Override
    void parse() {
        loadInto.setPlaceName(stringTree.getValue());
        if (stringTree.getChildren() != null) {
            for (StringTree ch : stringTree.getChildren()) {
                if (Tag.FORM.equalsText(ch.getTag())) {
                    loadInto.setPlaceFormat(new StringWithCustomTags(ch));
                } else if (Tag.SOURCE.equalsText(ch.getTag())) {
                    List<AbstractCitation> citations = loadInto.getCitations(true);
                    new CitationListParser(gedcomParser, ch, citations).parse();
                } else if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = loadInto.getNotes(true);
                    new NoteListParser(gedcomParser, ch, notes).parse();
                } else if (Tag.CONCATENATION.equalsText(ch.getTag())) {
                    loadInto.setPlaceName(loadInto.getPlaceName() + (ch.getValue() == null ? "" : ch.getValue()));
                } else if (Tag.CONTINUATION.equalsText(ch.getTag())) {
                    loadInto.setPlaceName(loadInto.getPlaceName() + "\n" + (ch.getValue() == null ? "" : ch.getValue()));
                } else if (Tag.ROMANIZED.equalsText(ch.getTag())) {
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but a romanized variation was specified on a place on line " + ch.getLineNum()
                                + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                    NameVariation nv = new NameVariation();
                    loadInto.getRomanized(true).add(nv);
                    nv.setVariation(ch.getValue());
                    if (ch.getChildren() != null) {
                        for (StringTree gch : ch.getChildren()) {
                            if (Tag.TYPE.equalsText(gch.getTag())) {
                                nv.setVariationType(new StringWithCustomTags(gch));
                            } else {
                                unknownTag(gch, nv);
                            }
                        }
                    }
                } else if (Tag.PHONETIC.equalsText(ch.getTag())) {
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but a phonetic variation was specified on a place on line " + ch.getLineNum()
                                + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                    NameVariation nv = new NameVariation();
                    loadInto.getPhonetic(true).add(nv);
                    nv.setVariation(ch.getValue());
                    if (ch.getChildren() != null) {
                        for (StringTree gch : ch.getChildren()) {
                            if (Tag.TYPE.equalsText(gch.getTag())) {
                                nv.setVariationType(new StringWithCustomTags(gch));
                            } else {
                                unknownTag(gch, nv);
                            }
                        }
                    }
                } else if (Tag.MAP.equalsText(ch.getTag())) {
                    if (g55()) {
                        addWarning("GEDCOM version is 5.5 but a map coordinate was specified on a place on line " + ch.getLineNum()
                                + ", which is a GEDCOM 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                    if (ch.getChildren() != null) {
                        for (StringTree gch : ch.getChildren()) {
                            if (Tag.LATITUDE.equalsText(gch.getTag())) {
                                loadInto.setLatitude(new StringWithCustomTags(gch));
                            } else if (Tag.LONGITUDE.equalsText(gch.getTag())) {
                                loadInto.setLongitude(new StringWithCustomTags(gch));
                            } else {
                                unknownTag(gch, loadInto);
                            }
                        }
                    }
                } else {
                    unknownTag(ch, loadInto);
                }
            }
        }

    }

}
