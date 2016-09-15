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
import org.gedcom4j.model.StringWithCustomTags;
import org.gedcom4j.model.Submitter;
import org.gedcom4j.validate.Validator.Finding;

/**
 * Validate a {@link Submitter} object. See {@link Validator} for usage information.
 * 
 * @author frizbog1
 * 
 */
class SubmitterValidator extends AbstractValidator {

    /**
     * The submitter being validated
     */
    private final Submitter submitter;

    /**
     * Constructor
     * 
     * @param validator
     *            the root validator containing among other things the findings collection
     * @param submitter
     *            the submitter being validated
     */
    SubmitterValidator(Validator validator, Submitter submitter) {
        this.validator = validator;
        this.submitter = submitter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        xrefMustBePresentAndWellFormed(submitter);
        mustHaveValue(submitter, "name");
        checkLanguagePreferences();
        mustHaveValueOrBeOmitted(submitter, "recIdNumber");
        mustHaveValueOrBeOmitted(submitter, "regFileNumber");
        if (submitter.getAddress() != null) {
            new AddressValidator(validator, submitter.getAddress()).validate();
        }
        new NotesListValidator(validator, submitter).validate();
    }

    /**
     * Check the language preferences
     */
    private void checkLanguagePreferences() {
        if (submitter.getLanguagePref() == null && Options.isCollectionInitializationEnabled()) {
            Finding finding = validator.newFinding(submitter, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, "languagePref");
            initializeCollectionIfAllowed(finding);
        }
        List<StringWithCustomTags> languagePref = submitter.getLanguagePref();
        if (languagePref == null) {
            return;
        }
        DuplicateHandler<StringWithCustomTags> dh = new DuplicateHandler<>(languagePref);
        int dups = dh.count();
        if (dups > 0) {
            Finding finding = validator.newFinding(submitter, Severity.ERROR, ProblemCode.DUPLICATE_VALUE, "languagePref");
            if (validator.mayRepair(finding)) {
                Submitter before = new Submitter(submitter);
                dh.remove();
                finding.addRepair(new AutoRepair(before, new Submitter(submitter)));
            }
        }
        if (languagePref.size() > 3) {
            validator.newFinding(submitter, Severity.ERROR, ProblemCode.TOO_MANY_VALUES, "languagePref");
        }
        for (StringWithCustomTags s : languagePref) {
            mustHaveValue(s, "value");
        }
    }
}
