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

import org.gedcom4j.model.NoteRecord;

/**
 * Validator for a single {@link NoteRecord}
 * 
 * @author frizbog
 */
class NoteRecordValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 7426278912021230776L;

    /**
     * The noteRecord being validated
     */
    private final NoteRecord noteRecord;

    /**
     * Constructor
     * 
     * @param validator
     *            the main gedcom validator that holds all the findings
     * @param noteRecord
     *            the noteRecord being validated
     */
    NoteRecordValidator(Validator validator, NoteRecord noteRecord) {
        super(validator);
        this.noteRecord = noteRecord;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        checkUninitializedCollection(noteRecord, "lines");
        if (noteRecord.getXref() == null || !noteRecord.getXref().matches("^\\@\\w+\\@$")) {
            newFinding(noteRecord, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, "xref");
        }
        if (noteRecord.getXref() == null && (noteRecord.getLines() == null || noteRecord.getLines().isEmpty())) {
            newFinding(noteRecord, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, "lines");
        }
        mustHaveValueOrBeOmitted(noteRecord, "recIdNumber");
        checkCitations(noteRecord);
        checkUserReferences(noteRecord.getUserReferences(), noteRecord);
        checkChangeDate(noteRecord.getChangeDate(), noteRecord);
    }

}
