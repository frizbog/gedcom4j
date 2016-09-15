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
import org.gedcom4j.model.AbstractNameVariation;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.model.PersonalNameVariation;
import org.gedcom4j.validate.Validator.Finding;

/**
 * Validator for {@link PersonalName} objects
 * 
 * @author frizbog1
 */
class PersonalNameValidator extends AbstractValidator {

    /**
     * The personal name being validated
     */
    private final PersonalName pn;

    /**
     * Constructor
     * 
     * @param validator
     *            the {@link Validator} that contains all the findings and options
     * @param pn
     *            the personal name being validated
     */
    PersonalNameValidator(Validator validator, PersonalName pn) {
        this.validator = validator;
        this.pn = pn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        mustHaveValue(pn, "basic");
        checkCitations(pn);
        checkCitations(pn);
        checkCustomTags(pn);
        mustHaveValueOrBeOmitted(pn, "given");
        mustHaveValueOrBeOmitted(pn, "nickname");
        mustHaveValueOrBeOmitted(pn, "prefix");
        mustHaveValueOrBeOmitted(pn, "suffix");
        mustHaveValueOrBeOmitted(pn, "surname");
        mustHaveValueOrBeOmitted(pn, "surnamePrefix");

        new NotesListValidator(validator, pn).validate();

        checkPhoneticVariations();
        checkRomanizedVariations();
    }

    /**
     * Check the phonetic variations on the place name
     */
    private void checkPhoneticVariations() {
        List<PersonalNameVariation> phonetic = pn.getPhonetic();
        if (phonetic == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(pn, Severity.ERROR, ProblemCode.UNINITIALIZED_COLLECTION, "phonetic");
            initializeCollectionIfAllowed(vf);
        }
        if (phonetic == null) {
            return;
        }
        checkListOfModelElementsForDups(pn, "phonetic");
        checkListOfModelElementsForNulls(pn, "phonetic");
        for (AbstractNameVariation nv : phonetic) {
            new NameVariationValidator(validator, nv).validate();
        }
    }

    /**
     * Check the romanized variations on the place name
     */
    private void checkRomanizedVariations() {
        List<PersonalNameVariation> romanized = pn.getRomanized();
        if (romanized == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(pn, Severity.ERROR, ProblemCode.UNINITIALIZED_COLLECTION, "romanized");
            initializeCollectionIfAllowed(vf);
        }
        if (romanized == null) {
            return;
        }
        checkListOfModelElementsForDups(pn, "romanized");
        checkListOfModelElementsForNulls(pn, "romanized");
        for (AbstractNameVariation nv : romanized) {
            new NameVariationValidator(validator, nv).validate();
        }
    }
}
