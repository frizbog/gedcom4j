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

import org.gedcom4j.model.AbstractCitation;
import org.gedcom4j.model.CitationData;
import org.gedcom4j.model.CitationWithSource;
import org.gedcom4j.model.CitationWithoutSource;
import org.gedcom4j.validate.Validator.Finding;

/**
 * A validator for source citations - both {@link CitationWithoutSource} and {@link CitationWithSource}. See {@link Validator} for
 * usage information.
 * 
 * @author frizbog1
 * 
 */
class CitationValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -5330593557253049349L;

    /**
     * The citation being validated
     */
    private final AbstractCitation citation;

    /**
     * Constructor
     * 
     * @param validator
     *            the root validator with the collection of findings
     * @param citation
     *            the citation being validated
     */
    CitationValidator(Validator validator, AbstractCitation citation) {
        super(validator);
        this.citation = citation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        if (citation instanceof CitationWithSource) {
            validateCitationWithSource();
        } else if (citation instanceof CitationWithoutSource) {
            validateCitationWithoutSource();
        } else {
            throw new IllegalStateException("AbstractCitation references must be either CitationWithSource"
                    + " instances or CitationWithoutSource instances");
        }
        checkNotes(citation);
        checkCustomTags(citation);
    }

    /**
     * Validate a citation without source
     */
    private void validateCitationWithoutSource() {
        CitationWithoutSource c = (CitationWithoutSource) citation;
        checkNotes(c);
        checkStringList(c, "description", true);
        checkUninitializedCollection(c, "textFromSource");
        List<List<String>> textFromSource = c.getTextFromSource();
        if (textFromSource == null || textFromSource.isEmpty()) {
            return;
        }
        DuplicateHandler<List<String>> dh = new DuplicateHandler<>(textFromSource);
        if (dh.count() > 0) {
            Finding vf = newFinding(c, Severity.ERROR, ProblemCode.DUPLICATE_VALUE, "textFromSource");
            if (mayRepair(vf)) {
                CitationWithoutSource before = new CitationWithoutSource(c);
                dh.remove();
                vf.addRepair(new AutoRepair(before, new CitationWithoutSource(c)));
            }
        }
        checkForNullEntries(c, "textFromSource");
    }

    /**
     * Validate a citation with source
     */
    private void validateCitationWithSource() {
        CitationWithSource c = (CitationWithSource) citation;
        if (c.getSource() == null) {
            newFinding(c, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, "source");
        }
        mustHaveValueOrBeOmitted(c, "whereInSource");
        mustHaveValueOrBeOmitted(c, "eventCited");
        if (c.getEventCited() == null) {
            mustNotHaveValue(c, "roleInEvent");
        } else {
            mustHaveValueOrBeOmitted(c, "roleInEvent");
        }
        mustHaveValueOrBeOmitted(c, "certainty");
        if (c.getCertainty() != null && c.getCertainty().getValue() != null && !"0".equals(c.getCertainty().getValue()) && !"1"
                .equals(c.getCertainty().getValue()) && !"2".equals(c.getCertainty().getValue()) && !"3".equals(c.getCertainty()
                        .getValue())) {
            newFinding(c, Severity.ERROR, ProblemCode.ILLEGAL_VALUE, "certainty");
        }
        checkUninitializedCollection(c, "data");
        checkListOfModelElementsForDups(c, "data");
        checkListOfModelElementsForNulls(c, "data");
        if (c.getData() != null) {
            for (CitationData cd : c.getData()) {
                checkCustomTags(cd);
                mustBeDateIfSpecified(cd, "entryDate");
                checkUninitializedCollection(cd, "sourceText");
            }
        }
    }
}
