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
import org.gedcom4j.model.Family;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.LdsSpouseSealing;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.Submitter;
import org.gedcom4j.validate.Validator.Finding;

/**
 * Validator for {@link Family} objects
 * 
 * @author frizbog1
 */
class FamilyValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -2719392556810437421L;

    /**
     * The family being validated
     */
    private final Family f;

    /**
     * Validator for {@link Family}
     * 
     * @param validator
     *            the main {@link Validator} that holds all the findings and settings
     * @param f
     *            the family being validated
     */
    FamilyValidator(Validator validator, Family f) {
        this.validator = validator;
        this.f = f;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        mustHaveValueOrBeOmitted(f, "automatedRecordId");
        checkChangeDate(f.getChangeDate(), f);
        checkChildren();
        checkCitations(f);
        checkCustomTags(f);
        if (f.getEvents() != null) {
            for (AbstractEvent ev : f.getEvents()) {
                new EventValidator(validator, ev).validate();
            }
        }
        if (f.getHusband() != null) {
            new IndividualValidator(validator, f.getHusband()).validate();
        }
        if (f.getWife() != null) {
            new IndividualValidator(validator, f.getWife()).validate();
        }
        checkLdsSpouseSealings();
        checkMultimedia();
        new NotesListValidator(validator, f).validate();
        mustHaveValueOrBeOmitted(f, "numChildren");
        mustHaveValueOrBeOmitted(f, "recFileNumber");
        mustHaveValueOrBeOmitted(f, "restrictionNotice");
        checkSubmitters();
        checkUserReferences(f.getUserReferences(), f);
    }

    /**
     * Check children.
     */
    private void checkChildren() {
        List<Individual> children = f.getChildren();
        if (children == null && Options.isCollectionInitializationEnabled()) {

            Finding vf = validator.newFinding(f, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, "children");
            initializeCollectionIfAllowed(vf);
        }
        if (children != null) {
            checkListOfModelElementsForDups(f, "children");
            checkListOfModelElementsForNulls(f, "children");
            /*
             * Do not iterate through the children checking the individuals or you will loop infinitely (or until you run out of
             * stack)
             */
        }
    }

    /**
     * Check lds spouse sealings.
     */
    private void checkLdsSpouseSealings() {
        List<LdsSpouseSealing> ldsSpouseSealings = f.getLdsSpouseSealings();
        if (ldsSpouseSealings == null && Options.isCollectionInitializationEnabled()) {

            Finding vf = validator.newFinding(f, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, "ldsSpouseSealings");
            initializeCollectionIfAllowed(vf);
        }
        if (ldsSpouseSealings != null) {
            checkListOfModelElementsForDups(f, "ldsSpouseSealings");
            checkListOfModelElementsForNulls(f, "ldsSpouseSealings");
            for (LdsSpouseSealing s : ldsSpouseSealings) {
                new LdsSpouseSealingValidator(validator, s).validate();
            }
        }
    }

    /**
     * Check multimedia.
     */
    private void checkMultimedia() {
        List<Multimedia> multimedia = f.getMultimedia();
        if (multimedia == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(f, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, "multimedia");
            initializeCollectionIfAllowed(vf);
        }
        if (multimedia != null) {
            checkListOfModelElementsForDups(f, "multimedia");
            checkListOfModelElementsForNulls(f, "multimedia");
            for (Multimedia m : multimedia) {
                new MultimediaValidator(validator, m).validate();
            }
        }
    }

    /**
     * Check submitters.
     */
    private void checkSubmitters() {
        List<Submitter> submitters = f.getSubmitters();
        if (submitters == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(f, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, "submitters");
            initializeCollectionIfAllowed(vf);
        }
        if (submitters != null) {
            checkListOfModelElementsForDups(f, "submitters");
            checkListOfModelElementsForNulls(f, "submitters");
            for (Submitter s : submitters) {
                new SubmitterValidator(validator, s).validate();
            }
        }
    }

}
