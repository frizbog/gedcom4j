/*
 * Copyright (c) 2009-2013 Matthew R. Harrah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.gedcom4j.validate;

import java.util.ArrayList;

import org.gedcom4j.model.AbstractCitation;
import org.gedcom4j.model.NameVariation;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.model.PersonalNameVariation;

/**
 * @author frizbog1
 * 
 */
public class PersonalNameValidator extends AbstractValidator {

    /**
     * The personal name being validated
     */
    private PersonalName pn;

    /**
     * Constructor
     * 
     * @param rootValidator
     *            the root {@link GedcomValidator} that contains all the
     *            findings and options
     * @param pn
     *            the personal name being validated
     */
    public PersonalNameValidator(GedcomValidator rootValidator, PersonalName pn) {
        this.rootValidator = rootValidator;
        this.pn = pn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gedcom4j.validate.AbstractValidator#validate()
     */
    @Override
    protected void validate() {
        if (pn == null) {
            addError("Personal name was null - cannot validate");
            return;
        }
        checkRequiredString(pn.basic, "basic name", pn);
        if (pn.citations == null) {
            if (rootValidator.autorepair) {
                pn.citations = new ArrayList<AbstractCitation>();
                addInfo("citations collection for personal name was null - autorepaired", pn);
            } else {
                addError("citations collection for personal name is null", pn);
            }
        }
        if (pn.citations != null) {
            for (AbstractCitation c : pn.citations) {
                new CitationValidator(rootValidator, c).validate();
            }
        }
        checkCustomTags(pn);
        checkOptionalString(pn.givenName, "given name", pn);
        checkOptionalString(pn.nickname, "nickname", pn);
        checkOptionalString(pn.prefix, "prefix", pn);
        checkOptionalString(pn.suffix, "suffix", pn);
        checkOptionalString(pn.surname, "surname", pn);
        checkOptionalString(pn.surnamePrefix, "surname prefix", pn);

        checkNotes(pn.notes, pn);
        if (pn.phonetic == null) {
            if (rootValidator.autorepair) {
                pn.phonetic = new ArrayList<PersonalNameVariation>();
                rootValidator.addInfo("Event had null list of phonetic name variations - repaired", pn);
            } else {
                rootValidator.addError("Event has null list of phonetic name variations", pn);
            }
        }
        if (pn.phonetic != null) {
            for (NameVariation nv : pn.phonetic) {
                PersonalNameVariation pnv = (PersonalNameVariation) nv;
                new PersonalNameVariationValidator(rootValidator, pnv).validate();
            }
        }

        if (pn.romanized == null) {
            if (rootValidator.autorepair) {
                pn.romanized = new ArrayList<PersonalNameVariation>();
                rootValidator.addInfo("Event had null list of romanized name variations - repaired", pn);
            } else {
                rootValidator.addError("Event has null list of romanized name variations", pn);
            }
        }
        if (pn.romanized != null) {
            for (NameVariation nv : pn.romanized) {
                PersonalNameVariation pnv = (PersonalNameVariation) nv;
                new PersonalNameVariationValidator(rootValidator, pnv).validate();
            }
        }
    }

}
