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
import org.gedcom4j.model.Place;

/**
 * Validator for {@link Place} objects
 * 
 * @author frizbog1
 * 
 */
class PlaceValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 3340140076536125787L;

    /**
     * The place being validated
     */
    private final Place place;

    /**
     * Constructor
     * 
     * @param validator
     *            the gedcom validator that holds all the findings
     * @param place
     *            the {@link Place} begin validated
     */
    PlaceValidator(Validator validator, Place place) {
        super(validator);
        this.place = place;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        checkCitations(place);
        checkCustomFacts(place);

        mustHaveValueOrBeOmitted(place, "latitude");
        mustHaveValueOrBeOmitted(place, "longitude");
        new NotesListValidator(getValidator(), place).validate();
        mustHaveValueOrBeOmitted(place, "placeFormat");
        if (place.getPlaceName() == null) {
            newFinding(place, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, "placeName");
        }

        checkPhoneticVariations();
        checkRomanizedVariations();
    }

    /**
     * Check the phonetic variations on the place name
     */
    private void checkPhoneticVariations() {
        checkUninitializedCollection(place, "phonetic");
        if (place.getPhonetic() == null) {
            return;
        }
        checkListOfModelElementsForDups(place, "phonetic");
        checkListOfModelElementsForNulls(place, "phonetic");
        for (AbstractNameVariation nv : place.getPhonetic()) {
            new NameVariationValidator(getValidator(), nv).validate();
        }
    }

    /**
     * Check the romanized variations on the place name
     */
    private void checkRomanizedVariations() {
        checkUninitializedCollection(place, "romanized");
        if (place.getRomanized() == null) {
            return;
        }
        checkListOfModelElementsForDups(place, "romanized");
        checkListOfModelElementsForNulls(place, "romanized");
        for (AbstractNameVariation nv : place.getRomanized()) {
            new NameVariationValidator(getValidator(), nv).validate();
        }
    }

}
