package org.gedcom4j.validate;

import java.util.ArrayList;

import org.gedcom4j.model.AbstractCitation;
import org.gedcom4j.model.NameVariation;
import org.gedcom4j.model.Place;

/**
 * Validator for {@link Place} objects
 * 
 * @author frizbog1
 * 
 */
public class PlaceValidator extends AbstractValidator {

    /**
     * The place being validated
     */
    private Place place;

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
        if (place.citations == null) {
            if (rootValidator.autorepair) {
                place.citations = new ArrayList<AbstractCitation>();
                rootValidator.addInfo("Event had null list of citations - repaired", place);
            } else {
                rootValidator.addError("Event has null list of citations", place);
            }
        }
        for (AbstractCitation c : place.citations) {
            new CitationValidator(rootValidator, c).validate();
        }
        checkCustomTags(place);

        checkOptionalString(place.latitude, "latitude", place);
        checkOptionalString(place.longitude, "longitude", place);
        checkNotes(place.notes, place);
        checkOptionalString(place.placeFormat, "place format", place);
        if (place.placeName == null) {
            if (rootValidator.autorepair) {
                addError("Place name was unspecified and cannot be repaired");
            } else {
                addError("Place name was unspecified");
            }
        }

        if (place.phonetic == null) {
            if (rootValidator.autorepair) {
                place.phonetic = new ArrayList<NameVariation>();
                rootValidator.addInfo("Event had null list of phonetic name variations - repaired", place);
            } else {
                rootValidator.addError("Event has null list of phonetic name variations", place);
            }
        }
        for (NameVariation nv : place.phonetic) {
            new NameVariationValidator(rootValidator, nv).validate();
        }

        if (place.romanized == null) {
            if (rootValidator.autorepair) {
                place.romanized = new ArrayList<NameVariation>();
                rootValidator.addInfo("Event had null list of romanized name variations - repaired", place);
            } else {
                rootValidator.addError("Event has null list of romanized name variations", place);
            }
        }
        for (NameVariation nv : place.romanized) {
            new NameVariationValidator(rootValidator, nv).validate();
        }

    }

}
