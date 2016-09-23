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

import org.gedcom4j.model.AbstractNameVariation;
import org.gedcom4j.model.PersonalName;

/**
 * Validator for {@link PersonalName} objects
 * 
 * @author frizbog1
 */
class PersonalNameValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -2718066344479251436L;

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
        super(validator);
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
        checkCustomFacts(pn);
        mustHaveValueOrBeOmitted(pn, "givenName");
        mustHaveValueOrBeOmitted(pn, "nickname");
        mustHaveValueOrBeOmitted(pn, "prefix");
        mustHaveValueOrBeOmitted(pn, "suffix");
        mustHaveValueOrBeOmitted(pn, "surname");
        mustHaveValueOrBeOmitted(pn, "surnamePrefix");

        new NotesListValidator(getValidator(), pn).validate();

        checkPhoneticVariations();
        checkRomanizedVariations();
    }

    /**
     * Check the phonetic variations on the place name
     */
    private void checkPhoneticVariations() {
        checkUninitializedCollection(pn, "phonetic");
        if (pn.getPhonetic() == null) {
            return;
        }
        checkListOfModelElementsForDups(pn, "phonetic");
        checkListOfModelElementsForNulls(pn, "phonetic");
        for (AbstractNameVariation nv : pn.getPhonetic()) {
            new NameVariationValidator(getValidator(), nv).validate();
        }
    }

    /**
     * Check the romanized variations on the place name
     */
    private void checkRomanizedVariations() {
        checkUninitializedCollection(pn, "romanized");
        if (pn.getRomanized() == null) {
            return;
        }
        checkListOfModelElementsForDups(pn, "romanized");
        checkListOfModelElementsForNulls(pn, "romanized");
        for (AbstractNameVariation nv : pn.getRomanized()) {
            new NameVariationValidator(getValidator(), nv).validate();
        }
    }
}
