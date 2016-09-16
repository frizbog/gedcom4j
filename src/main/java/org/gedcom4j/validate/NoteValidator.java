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

import org.gedcom4j.Options;
import org.gedcom4j.model.Note;
import org.gedcom4j.validate.Validator.Finding;

/**
 * Validator for a single {@link Note}
 * 
 * @author frizbog
 */
class NoteValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 7426278912021230776L;

    /**
     * The note being validated
     */
    private final Note note;

    /**
     * Constructor
     * 
     * @param validator
     *            the main gedcom validator that holds all the findings
     * @param note
     *            the note being validated
     */
    NoteValidator(Validator validator, Note note) {
        this.validator = validator;
        this.note = note;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {

        if (Options.isCollectionInitializationEnabled() && note.getLines() == null) {
            Finding finding = validator.newFinding(note, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, "lines");
            initializeCollectionIfAllowed(finding);
        }

        if (note.getXref() == null && (note.getLines() == null || note.getLines().isEmpty())) {
            validator.newFinding(note, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, "lines");
        }

        mustHaveValueOrBeOmitted(note, "recIdNumber");
        checkCitations(note);
        checkUserReferences(note.getUserReferences(), note);
        checkChangeDate(note.getChangeDate(), note);
    }

}
