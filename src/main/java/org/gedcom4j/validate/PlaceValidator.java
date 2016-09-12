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
import org.gedcom4j.model.AbstractCitation;
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
    PlaceValidator(GedcomValidator validator, Place place) {
        this.validator = validator;
        this.place = place;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        if (place == null) {
            addError("Place is null and cannot be validated or repaired");
            return;
        }
        if (place.getCitations() == null && Options.isCollectionInitializationEnabled()) {
            if (validator.isAutorepairEnabled()) {
                place.getCitations(true).clear();
                validator.addInfo("Event had null list of citations - repaired", place);
            } else {
                validator.addError("Event has null list of citations", place);
            }
        }
        if (place.getCitations() != null) {
            for (AbstractCitation c : place.getCitations()) {
                new CitationValidator(validator, c).validate();
            }
        }
        checkCustomTags(place);

        mustHaveValueOrBeOmitted(place.getLatitude(), "latitude", place);
        mustHaveValueOrBeOmitted(place.getLongitude(), "longitude", place);
        new NotesValidator(validator, place, place.getNotes()).validate();
        mustHaveValueOrBeOmitted(place.getPlaceFormat(), "place format", place);
        if (place.getPlaceName() == null) {
            if (validator.isAutorepairEnabled()) {
                addError("Place name was unspecified and cannot be repaired");
            } else {
                addError("Place name was unspecified");
            }
        }

        List<AbstractNameVariation> phonetic = place.getPhonetic();
        if (phonetic == null && Options.isCollectionInitializationEnabled()) {
            if (validator.isAutorepairEnabled()) {
                place.getPhonetic(true).clear();
                validator.addInfo("Event had null list of phonetic name variations - repaired", place);
            } else {
                validator.addError("Event has null list of phonetic name variations", place);
            }
        }
        if (phonetic != null) {
            if (validator.isAutorepairEnabled()) {
                int dups = new DuplicateHandler<>(phonetic).process();
                if (dups > 0) {
                    validator.addInfo(dups + " duplicate phonetic variations found and removed", place);
                }
            }

            for (AbstractNameVariation nv : phonetic) {
                new NameVariationValidator(validator, nv).validate();
            }
        }

        List<AbstractNameVariation> romanized = place.getRomanized();
        if (romanized == null && Options.isCollectionInitializationEnabled()) {
            if (validator.isAutorepairEnabled()) {
                place.getRomanized(true).clear();
                validator.addInfo("Event had null list of romanized name variations - repaired", place);
            } else {
                validator.addError("Event has null list of romanized name variations", place);
            }
        }
        if (validator.isAutorepairEnabled()) {
            int dups = new DuplicateHandler<>(romanized).process();
            if (dups > 0) {
                validator.addInfo(dups + " duplicate romanized variations found and removed", place);
            }
        }

        if (romanized != null) {
            for (AbstractNameVariation nv : romanized) {
                new NameVariationValidator(validator, nv).validate();
            }
        }

    }

}
