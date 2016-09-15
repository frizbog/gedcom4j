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
import org.gedcom4j.model.Place;
import org.gedcom4j.validate.Validator.Finding;

/**
 * Validator for {@link Place} objects
 * 
 * @author frizbog1
 * 
 */
class PlaceValidator extends AbstractValidator {

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
        this.validator = validator;
        this.place = place;
    }

    /**
     * Check the phonetic variations on the place name
     */
    protected void checkPhoneticVariations() {
        List<AbstractNameVariation> phonetic = place.getPhonetic();
        if (phonetic == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(place, Severity.ERROR, ProblemCode.UNINITIALIZED_COLLECTION, "phonetic");
            initializeCollectionIfAllowed(vf);
        }
        if (phonetic == null) {
            return;
        }
        checkListOfModelElementsForDups(place, "phonetic");
        checkListOfModelElementsForNulls(place, "phonetic");
        for (AbstractNameVariation nv : phonetic) {
            new NameVariationValidator(validator, nv).validate();
        }
    }

    /**
     * Check the romanized variations on the place name
     */
    protected void checkRomanizedVariations() {
        List<AbstractNameVariation> romanized = place.getRomanized();
        if (romanized == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(place, Severity.ERROR, ProblemCode.UNINITIALIZED_COLLECTION, "romanized");
            initializeCollectionIfAllowed(vf);
        }
        if (romanized == null) {
            return;
        }
        checkListOfModelElementsForDups(place, "romanized");
        checkListOfModelElementsForNulls(place, "romanized");
        for (AbstractNameVariation nv : romanized) {
            new NameVariationValidator(validator, nv).validate();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        checkCitations(place);
        checkCustomTags(place);

        mustHaveValueOrBeOmitted(place, "latitude");
        mustHaveValueOrBeOmitted(place, "longitude");
        new NotesListValidator(validator, place).validate();
        mustHaveValueOrBeOmitted(place, "placeFormat");
        if (place.getPlaceName() == null) {
            validator.newFinding(place, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, "placeName");
        }

        checkPhoneticVariations();

    }

}
