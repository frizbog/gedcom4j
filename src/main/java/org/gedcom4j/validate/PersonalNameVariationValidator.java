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
import org.gedcom4j.model.PersonalNameVariation;

/**
 * Validator for {@link PersonalNameVariation} objects
 * 
 * @author frizbog1
 */
class PersonalNameVariationValidator extends NameVariationValidator {

    /**
     * Constructor
     * 
     * @param validator
     *            the root {@link GedcomValidator} that contains the findings and the settings
     * @param pnv
     *            the personal name variation to be validated
     */
    PersonalNameVariationValidator(GedcomValidator validator, PersonalNameVariation pnv) {
        super(validator, pnv);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        super.validate();
        if (nv == null) {
            return;
        }
        if (!(nv instanceof PersonalNameVariation)) {
            addError("Name variation on person is not a PersonalNameVariation");
            return;
        }
        PersonalNameVariation pnv = (PersonalNameVariation) nv;
        List<AbstractCitation> citations = pnv.getCitations();
        if (citations == null) {
            if (validator.isAutorepairEnabled()) {
                pnv.getCitations(true).clear();
                addInfo("citations collection for personal name was null - autorepaired", pnv);
            } else {
                addError("citations collection for personal name is null", pnv);
            }
        } else {
            if (validator.isAutorepairEnabled()) {
                int dups = new DuplicateHandler<>(citations).process();
                if (dups > 0) {
                    validator.addInfo(dups + " duplicate citations found and removed", pnv);
                }
            }

            for (AbstractCitation c : citations) {
                new CitationValidator(validator, c).validate();
            }
        }
        mustHaveValueOrBeOmitted(pnv.getGivenName(), "given name", pnv);
        mustHaveValueOrBeOmitted(pnv.getNickname(), "nickname", pnv);
        mustHaveValueOrBeOmitted(pnv.getPrefix(), "prefix", pnv);
        mustHaveValueOrBeOmitted(pnv.getSuffix(), "suffix", pnv);
        mustHaveValueOrBeOmitted(pnv.getSurname(), "surname", pnv);
        mustHaveValueOrBeOmitted(pnv.getSurnamePrefix(), "surname prefix", pnv);
        new NotesValidator(validator, pnv, pnv.getNotes()).validate();
    }
}
