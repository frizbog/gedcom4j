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
     * @param rootValidator
     *            the root {@link GedcomValidator} that contains the findings and the settings
     * @param pnv
     *            the personal name variation to be validated
     */
    public PersonalNameVariationValidator(GedcomValidator rootValidator, PersonalNameVariation pnv) {
        super(rootValidator, pnv);
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
        if (pnv.citations == null) {
            if (rootValidator.autorepair) {
                pnv.citations = new ArrayList<AbstractCitation>();
                addInfo("citations collection for personal name was null - autorepaired", pnv);
            } else {
                addError("citations collection for personal name is null", pnv);
            }
        } else {
            for (AbstractCitation c : pnv.citations) {
                new CitationValidator(rootValidator, c).validate();
            }
        }
        checkOptionalString(pnv.givenName, "given name", pnv);
        checkOptionalString(pnv.nickname, "nickname", pnv);
        checkOptionalString(pnv.prefix, "prefix", pnv);
        checkOptionalString(pnv.suffix, "suffix", pnv);
        checkOptionalString(pnv.surname, "surname", pnv);
        checkOptionalString(pnv.surnamePrefix, "surname prefix", pnv);
        checkNotes(pnv.notes, pnv);
    }
}
