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

import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.model.AbstractCitation;
import org.gedcom4j.model.CitationData;
import org.gedcom4j.model.CitationWithSource;
import org.gedcom4j.model.CitationWithoutSource;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.Note;
import org.gedcom4j.model.Source;
import org.gedcom4j.model.StringTree;
import org.gedcom4j.model.StringWithCustomFacts;

/**
 * @author frizbog
 *
 */
class CitationListParser extends AbstractParser<List<AbstractCitation>> {

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
    CitationListParser(GedcomParser gedcomParser, StringTree stringTree, List<AbstractCitation> loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void parse() {
        AbstractCitation citation;
        if (referencesAnotherNode(stringTree)) {
            citation = new CitationWithSource();
            loadCitationWithSource(stringTree, citation);
        } else {
            citation = new CitationWithoutSource();
            loadCitationWithoutSource(stringTree, citation);
        }
        loadInto.add(citation);

    }

    /**
     * Load a DATA structure in a source citation from a string tree node
     * 
     * @param data
     *            the DATA (source-citation data) node
     * @param d
     *            the CitationData structure
     */
    private void loadCitationData(StringTree data, CitationData d) {
        if (data.getChildren() != null) {
            for (StringTree ch : data.getChildren()) {
                if (Tag.DATE.equalsText(ch.getTag())) {
                    d.setEntryDate(parseStringWithCustomFacts(ch));
                } else if (Tag.TEXT.equalsText(ch.getTag())) {
                    List<String> ls = new ArrayList<>();
                    d.getSourceText(true).add(ls);
                    loadMultiLinesOfText(ch, ls, d);
                } else {
                    unknownTag(ch, d);
                }
            }
        }

    }

    /**
     * Load the non-cross-referenced source citation from a string tree node
     * 
     * @param sour
     *            the SOUR (source-citation) node
     * @param citation
     *            the citation to load into
     */
    private void loadCitationWithoutSource(StringTree sour, AbstractCitation citation) {
        CitationWithoutSource cws = (CitationWithoutSource) citation;
        cws.getDescription(true).add(sour.getValue());
        if (sour.getChildren() != null) {
            for (StringTree ch : sour.getChildren()) {
                if (Tag.CONTINUATION.equalsText(ch.getTag())) {
                    cws.getDescription(true).add(ch.getValue() == null ? "" : ch.getValue());
                } else if (Tag.CONCATENATION.equalsText(ch.getTag())) {
                    if (cws.getDescription().isEmpty()) {
                        cws.getDescription(true).add(ch.getValue());
                    } else {
                        // Append to last value in string list
                        cws.getDescription().set(cws.getDescription().size() - 1, cws.getDescription().get(cws.getDescription()
                                .size() - 1) + ch.getValue());
                    }
                } else if (Tag.TEXT.equalsText(ch.getTag())) {
                    List<String> ls = new ArrayList<>();
                    cws.getTextFromSource(true).add(ls);
                    loadMultiLinesOfText(ch, ls, cws);
                } else if (Tag.NOTE.equalsText(ch.getTag())) {
                    new NoteListParser(gedcomParser, ch, cws.getNotes(true)).parse();
                } else {
                    unknownTag(ch, citation);
                }
            }
        }
    }

    /**
     * Load a cross-referenced source citation from a string tree node
     * 
     * @param sour
     *            the SOUR (source-citation) node
     * @param citation
     *            the citation to load into
     */
    private void loadCitationWithSource(StringTree sour, AbstractCitation citation) {
        CitationWithSource cws = (CitationWithSource) citation;
        Source src = null;
        if (referencesAnotherNode(sour)) {
            src = getSource(sour.getValue());
        }
        cws.setSource(src);
        if (sour.getChildren() != null) {
            for (StringTree ch : sour.getChildren()) {
                if (Tag.PAGE.equalsText(ch.getTag())) {
                    cws.setWhereInSource(parseStringWithCustomFacts(ch));
                } else if (Tag.EVENT.equalsText(ch.getTag())) {
                    cws.setEventCited(new StringWithCustomFacts(ch.getValue()));
                    if (ch.getChildren() != null) {
                        for (StringTree gc : ch.getChildren()) {
                            if (Tag.ROLE.equalsText(gc.getTag())) {
                                cws.setRoleInEvent(parseStringWithCustomFacts(gc));
                            } else {
                                unknownTag(gc, cws.getEventCited());
                            }
                        }
                    }
                } else if (Tag.DATA_FOR_CITATION.equalsText(ch.getTag())) {
                    CitationData d = new CitationData();
                    cws.getData(true).add(d);
                    loadCitationData(ch, d);
                } else if (Tag.QUALITY.equalsText(ch.getTag())) {
                    cws.setCertainty(parseStringWithCustomFacts(ch));
                } else if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = cws.getNotes(true);
                    new NoteListParser(gedcomParser, ch, notes).parse();
                } else if (Tag.OBJECT_MULTIMEDIA.equalsText(ch.getTag())) {
                    List<Multimedia> multimedia = cws.getMultimedia(true);
                    new MultimediaLinkParser(gedcomParser, ch, multimedia).parse();
                } else {
                    unknownTag(ch, citation);
                }
            }
        }
    }

}
