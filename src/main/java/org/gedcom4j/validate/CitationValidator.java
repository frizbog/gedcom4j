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
package org.gedcom4j.validate;

import java.util.List;

import org.gedcom4j.Options;
import org.gedcom4j.model.AbstractCitation;
import org.gedcom4j.model.CitationWithSource;
import org.gedcom4j.model.CitationWithoutSource;

/**
 * A validator for source citations - both {@link CitationWithoutSource} and {@link CitationWithSource}. See {@link GedcomValidator}
 * for usage information.
 * 
 * @author frizbog1
 * 
 */
class CitationValidator extends AbstractValidator {

    /**
     * The citation being validated
     */
    private final AbstractCitation citation;

    /**
     * Constructor
     * 
     * @param rootValidator
     *            the root validator with the collection of findings
     * @param citation
     *            the citation being validated
     */
    CitationValidator(GedcomValidator rootValidator, AbstractCitation citation) {
        this.rootValidator = rootValidator;
        this.citation = citation;
    }

    @Override
    protected void validate() {
        if (citation == null) {
            addError("Citation is null");
            return;
        }
        if (citation instanceof CitationWithSource) {
            CitationWithSource c = (CitationWithSource) citation;
            if (c.getSource() == null) {
                addError("CitationWithSource requires a non-null source reference", c);
            }
            checkOptionalString(c.getWhereInSource(), "where within source", c);
            checkOptionalString(c.getEventCited(), "event type cited from", c);
            if (c.getEventCited() == null) {
                if (c.getRoleInEvent() != null) {
                    addError("CitationWithSource has role in event but a null event");
                }
            } else {
                checkOptionalString(c.getRoleInEvent(), "role in event", c);
            }
            checkOptionalString(c.getCertainty(), "certainty/quality", c);
        } else if (citation instanceof CitationWithoutSource) {
            CitationWithoutSource c = (CitationWithoutSource) citation;
            checkStringList(c.getDescription(), "description on a citation without a source", true);
            List<List<String>> textFromSource = c.getTextFromSource();
            if (textFromSource == null && Options.isCollectionInitializationEnabled()) {
                if (rootValidator.isAutorepairEnabled()) {
                    c.getTextFromSource(true).clear();
                    addInfo("Text from source collection (the list of lists) was null in CitationWithoutSource - autorepaired",
                            citation);
                } else {
                    addError("Text from source collection (the list of lists) is null in CitationWithoutSource", citation);
                }
            } else {
                if (rootValidator.isAutorepairEnabled()) {
                    int dups = new DuplicateEliminator<>(textFromSource).process();
                    if (dups > 0) {
                        rootValidator.addInfo(dups + " duplicate texts from source found and removed", citation);
                    }
                }
                if (textFromSource != null) {
                    for (List<String> sl : textFromSource) {
                        if (sl == null) {
                            addError("Text from source collection (the list of lists) in CitationWithoutSource contains a null",
                                    citation);
                        } else {
                            checkStringList(sl, "one of the sublists in the textFromSource collection on a source citation", true);
                        }
                    }
                }
            }
        } else {
            throw new IllegalStateException("AbstractCitation references must be either CitationWithSource"
                    + " instances or CitationWithoutSource instances");
        }
        if (citation.getNotes() == null && Options.isCollectionInitializationEnabled()) {
            if (rootValidator.isAutorepairEnabled()) {
                citation.getNotes(true).clear();
                addInfo("Notes collection was null on " + citation.getClass().getSimpleName() + " - autorepaired");
            } else {
                addError("Notes collection is null on " + citation.getClass().getSimpleName());
            }
        } else {
            new NotesValidator(rootValidator, citation, citation.getNotes()).validate();
        }

    }
}
