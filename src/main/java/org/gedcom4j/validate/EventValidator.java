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
import org.gedcom4j.model.AbstractEvent;
import org.gedcom4j.model.FamilyEvent;
import org.gedcom4j.model.FamilyEventType;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.IndividualEventType;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.validate.Validator.Finding;

/**
 * Validator for events
 * 
 * @author frizbog1
 */
class EventValidator extends AbstractValidator {

    /**
     * The event being validated
     */
    private final AbstractEvent e;

    /**
     * Constructor
     * 
     * @param validator
     *            the {@link Validator} that contains the findings and the settings
     * @param e
     *            the event beign validated
     */
    EventValidator(Validator validator, AbstractEvent e) {
        this.validator = validator;
        this.e = e;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("PMD.ExcessiveMethodLength")
    protected void validate() {
        if (e instanceof IndividualEvent) {
            IndividualEvent ie = (IndividualEvent) e;
            mustHaveValue(ie, "type");
            if (ie.getType() == IndividualEventType.BIRTH || ie.getType() == IndividualEventType.CHRISTENING || ie
                    .getType() == IndividualEventType.DEATH) {
                mustHaveValueOrBeOmitted(ie, "yNull");
            } else {
                mustNotHaveValue(ie, "yNull");
            }
            if (ie.getType() == IndividualEventType.BIRTH || ie.getType() == IndividualEventType.CHRISTENING || ie
                    .getType() == IndividualEventType.ADOPTION) {
                mustHaveValueOrBeOmitted(ie, "family");
            } else {
                mustNotHaveValue(ie, "family");
            }
        } else if (e instanceof FamilyEvent) {
            FamilyEvent fe = (FamilyEvent) e;
            mustHaveValue(fe, "type");
            if (fe.getType() == FamilyEventType.MARRIAGE) {
                mustHaveValueOrBeOmitted(fe, "yNull");
            } else {
                mustNotHaveValue(fe, "yNull");
            }
            if (fe.getType() == FamilyEventType.EVENT) {
                mustHaveValueOrBeOmitted(fe, "description");
            } else {
                mustNotHaveValue(fe, "description");
            }
        }
        if (e.getAddress() != null) {
            new AddressValidator(validator, e.getAddress()).validate();
        }
        mustHaveValueOrBeOmitted(e, "age");
        mustHaveValueOrBeOmitted(e, "cause");
        checkCitations(e);
        checkCustomTags(e);
        mustHaveValueOrBeOmitted(e, "date");
        if (e.getDescription() != null && e.getDescription().trim().length() != 0) {
            validator.newFinding(e, Severity.ERROR, ProblemCode.ILLEGAL_VALUE, "description");
        }
        checkEmails(e);
        checkFaxNumbers(e);
        checkMultimedia();
        new NotesListValidator(validator, e).validate();
        checkPhoneNumbers(e);
        mustHaveValueOrBeOmitted(e, "religiousAffiliation");
        mustHaveValueOrBeOmitted(e, "respAgency");
        mustHaveValueOrBeOmitted(e, "restrictionNotice");
        checkWwwUrls(e);

    }

    /**
     * Check the multimedia
     */
    private void checkMultimedia() {
        List<Multimedia> multimedia = e.getMultimedia();
        if (multimedia == null && Options.isCollectionInitializationEnabled()) {

            Finding vf = validator.newFinding(e, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, "multimedia");
            initializeCollectionIfAllowed(vf);
        }
        if (multimedia != null) {
            checkListOfModelElementsForDups(e, "multimedia");
            checkListOfModelElementsForNulls(e, "multimedia");
            for (Multimedia m : multimedia) {
                new MultimediaValidator(validator, m).validate();
            }
        }
    }
}
