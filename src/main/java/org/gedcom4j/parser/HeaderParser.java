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

import org.gedcom4j.model.CharacterSet;
import org.gedcom4j.model.GedcomVersion;
import org.gedcom4j.model.Header;
import org.gedcom4j.model.SourceSystem;
import org.gedcom4j.model.StringTree;
import org.gedcom4j.model.SubmissionReference;
import org.gedcom4j.model.SubmitterReference;

/**
 * A parser for {@link Header} objects
 * 
 * @author frizbog
 */
class HeaderParser extends AbstractParser<Header> {

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
    HeaderParser(GedcomParser gedcomParser, StringTree stringTree, Header loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void parse() {
        if (stringTree.getChildren() != null) {
            for (StringTree ch : stringTree.getChildren()) {
                if (Tag.SOURCE.equalsText(ch.getTag())) {
                    SourceSystem sourceSystem = new SourceSystem();
                    loadInto.setSourceSystem(sourceSystem);
                    new SourceSystemParser(gedcomParser, ch, sourceSystem).parse();
                } else if (Tag.DESTINATION.equalsText(ch.getTag())) {
                    loadInto.setDestinationSystem(parseStringWithCustomFacts(ch));
                    // remainingChildrenAreCustomTags(ch, loadInto.getDestinationSystem());
                } else if (Tag.DATE.equalsText(ch.getTag())) {
                    loadInto.setDate(parseStringWithCustomFacts(ch));
                    // one optional time subitem is the only possibility here
                    if (ch.getChildren() != null) {
                        for (StringTree gch : ch.getChildren()) {
                            if ("TIME".equals(gch.getTag())) {
                                loadInto.setTime(parseStringWithCustomFacts(gch));
                            } else {
                                unknownTag(gch, loadInto.getDate());
                            }
                        }
                    }
                } else if (Tag.CHARACTER_SET.equalsText(ch.getTag())) {
                    loadInto.setCharacterSet(new CharacterSet());
                    loadInto.getCharacterSet().setCharacterSetName(parseStringWithCustomFacts(ch));
                    // one optional version subitem is the only standard possibility here, but there can be custom tags
                    if (ch.getChildren() != null && !ch.getChildren().isEmpty()) {
                        for (StringTree gch : ch.getChildren()) {
                            if ("VERS".equals(gch.getTag())) {
                                loadInto.getCharacterSet().setVersionNum(parseStringWithCustomFacts(gch));
                            } else {
                                unknownTag(gch, loadInto.getCharacterSet());
                            }
                        }
                    }
                } else if (Tag.SUBMITTER.equalsText(ch.getTag())) {
                    loadInto.setSubmitterReference(new SubmitterReference(getSubmitter(ch.getValue())));
                    remainingChildrenAreCustomTags(ch, loadInto.getSubmitterReference());
                } else if (Tag.FILE.equalsText(ch.getTag())) {
                    loadInto.setFileName(parseStringWithCustomFacts(ch));
                } else if (Tag.GEDCOM_VERSION.equalsText(ch.getTag())) {
                    GedcomVersion gedcomVersion = new GedcomVersion();
                    loadInto.setGedcomVersion(gedcomVersion);
                    new GedcomVersionParser(gedcomParser, ch, gedcomVersion).parse();
                } else if (Tag.COPYRIGHT.equalsText(ch.getTag())) {
                    loadMultiLinesOfText(ch, loadInto.getCopyrightData(true), loadInto);
                    if (g55() && loadInto.getCopyrightData().size() > 1) {
                        gedcomParser.getWarnings().add(
                                "GEDCOM version is 5.5, but multiple lines of copyright data were specified, which is only allowed in GEDCOM 5.5.1. "
                                        + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                } else if (Tag.SUBMISSION.equalsText(ch.getTag())) {
                    if (loadInto.getSubmissionReference() == null) {
                        /*
                         * There can only be one SUBMISSION record per GEDCOM, and it's found at the root level, but the HEAD
                         * structure has a cross-reference to that root-level structure, so we're setting it here (if it hasn't
                         * already been loaded, which it probably isn't yet)
                         */
                        loadInto.setSubmissionReference(new SubmissionReference(gedcomParser.getGedcom().getSubmission()));
                        remainingChildrenAreCustomTags(ch, loadInto.getSubmissionReference());
                    }
                } else if (Tag.LANGUAGE.equalsText(ch.getTag())) {
                    loadInto.setLanguage(parseStringWithCustomFacts(ch));
                } else if (Tag.PLACE.equalsText(ch.getTag())) {
                    loadInto.setPlaceHierarchy(parseStringWithCustomFacts(ch.getChildren().get(0)));
                } else if (Tag.NOTE.equalsText(ch.getTag())) {
                    new NoteListParser(gedcomParser, ch, loadInto.getNotes(true)).parse();
                } else {
                    unknownTag(ch, loadInto);
                }
            }
        }
    }
}
