/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
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

import org.gedcom4j.model.AbstractCitation;
import org.gedcom4j.model.NameVariation;
import org.gedcom4j.model.Place;

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
     * @param rootValidator
     *            the gedcom validator that holds all the findings
     * @param place
     *            the {@link Place} begin validated
     */
    public PlaceValidator(GedcomValidator rootValidator, Place place) {
        this.rootValidator = rootValidator;
        this.place = place;
    }

    @Override
    protected void validate() {
        if (place == null) {
            addError("Place is null and cannot be validated or repaired");
            return;
        }
        if (place.getCitations() == null) {
            if (rootValidator.isAutorepairEnabled()) {
                place.getCitations(true).clear();
                rootValidator.addInfo("Event had null list of citations - repaired", place);
            } else {
                rootValidator.addError("Event has null list of citations", place);
            }
        }
        for (AbstractCitation c : place.getCitations()) {
            new CitationValidator(rootValidator, c).validate();
        }
        checkCustomTags(place);

        checkOptionalString(place.getLatitude(), "latitude", place);
        checkOptionalString(place.getLongitude(), "longitude", place);
        checkNotes(place.getNotes(), place);
        checkOptionalString(place.getPlaceFormat(), "place format", place);
        if (place.getPlaceName() == null) {
            if (rootValidator.isAutorepairEnabled()) {
                addError("Place name was unspecified and cannot be repaired");
            } else {
                addError("Place name was unspecified");
            }
        }

        if (place.getPhonetic() == null) {
            if (rootValidator.isAutorepairEnabled()) {
                place.getPhonetic(true).clear();
                rootValidator.addInfo("Event had null list of phonetic name variations - repaired", place);
            } else {
                rootValidator.addError("Event has null list of phonetic name variations", place);
            }
        }
        for (NameVariation nv : place.getPhonetic()) {
            new NameVariationValidator(rootValidator, nv).validate();
        }

        if (place.getRomanized() == null) {
            if (rootValidator.isAutorepairEnabled()) {
                place.getRomanized(true).clear();
                rootValidator.addInfo("Event had null list of romanized name variations - repaired", place);
            } else {
                rootValidator.addError("Event has null list of romanized name variations", place);
            }
        }
        for (NameVariation nv : place.getRomanized()) {
            new NameVariationValidator(rootValidator, nv).validate();
        }

    }

}
