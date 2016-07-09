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
import org.gedcom4j.model.AbstractCitation;
import org.gedcom4j.model.AbstractNameVariation;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.model.PersonalNameVariation;

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
     * @param rootValidator
     *            the root {@link GedcomValidator} that contains all the findings and options
     * @param pn
     *            the personal name being validated
     */
    public PersonalNameValidator(GedcomValidator rootValidator, PersonalName pn) {
        this.rootValidator = rootValidator;
        this.pn = pn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        if (pn == null) {
            addError("Personal name was null - cannot validate");
            return;
        }
        checkRequiredString(pn.getBasic(), "basic name", pn);
        if (pn.getCitations() == null && Options.isCollectionInitializationEnabled()) {
            if (rootValidator.isAutorepairEnabled()) {
                pn.getCitations(true).clear();
                addInfo("citations collection for personal name was null - autorepaired", pn);
            } else {
                addError("citations collection for personal name is null", pn);
            }
        }
        if (pn.getCitations() != null) {
            for (AbstractCitation c : pn.getCitations()) {
                new CitationValidator(rootValidator, c).validate();
            }
        }
        checkCustomTags(pn);
        checkOptionalString(pn.getGivenName(), "given name", pn);
        checkOptionalString(pn.getNickname(), "nickname", pn);
        checkOptionalString(pn.getPrefix(), "prefix", pn);
        checkOptionalString(pn.getSuffix(), "suffix", pn);
        checkOptionalString(pn.getSurname(), "surname", pn);
        checkOptionalString(pn.getSurnamePrefix(), "surname prefix", pn);

        new NotesValidator(rootValidator, pn, pn.getNotes()).validate();
        if (pn.getPhonetic() == null && Options.isCollectionInitializationEnabled()) {
            if (rootValidator.isAutorepairEnabled()) {
                pn.getPhonetic(true).clear();
                rootValidator.addInfo("PersonalNameValidator had null list of phonetic name variations - repaired", pn);
            } else {
                rootValidator.addError("PersonalNamevalidator has null list of phonetic name variations", pn);
            }
        } else {
            if (pn.getPhonetic() != null) {
                for (AbstractNameVariation nv : pn.getPhonetic()) {
                    PersonalNameVariation pnv = (PersonalNameVariation) nv;
                    new PersonalNameVariationValidator(rootValidator, pnv).validate();
                }
            }
        }

        if (pn.getRomanized() == null && Options.isCollectionInitializationEnabled()) {
            if (rootValidator.isAutorepairEnabled()) {
                pn.getRomanized(true).clear();
                rootValidator.addInfo("Event had null list of romanized name variations - repaired", pn);
            } else {
                rootValidator.addError("Event has null list of romanized name variations", pn);
            }
        } else {
            if (pn.getRomanized() != null) {
                for (AbstractNameVariation nv : pn.getRomanized()) {
                    PersonalNameVariation pnv = (PersonalNameVariation) nv;
                    new PersonalNameVariationValidator(rootValidator, pnv).validate();
                }
            }
        }
    }

}
