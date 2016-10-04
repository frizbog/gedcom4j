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

import org.gedcom4j.model.AbstractEvent;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.IndividualReference;
import org.gedcom4j.model.LdsSpouseSealing;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.Submitter;
import org.gedcom4j.model.SubmitterReference;
import org.gedcom4j.model.enumerations.RestrictionNoticeType;

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
        super(validator);
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
        checkCustomFacts(f);
        if (f.getEvents() != null) {
            for (AbstractEvent ev : f.getEvents()) {
                new EventValidator(getValidator(), ev).validate();
            }
        }
        if (f.getHusband() != null) {
            new IndividualValidator(getValidator(), (f.getHusband() == null ? null : f.getHusband().getIndividual())).validate();
        }
        if (f.getWife() != null) {
            new IndividualValidator(getValidator(), (f.getWife() == null ? null : f.getWife().getIndividual())).validate();
        }
        checkLdsSpouseSealings();
        checkMultimedia();
        new NoteStructureListValidator(getValidator(), f).validate();
        mustHaveValueOrBeOmitted(f, "numChildren");
        mustHaveValueOrBeOmitted(f, "recFileNumber");
        mustHaveValueOrBeOmitted(f, "restrictionNotice");
        if (f.getRestrictionNotice() != null) {
            mustBeInEnumIfSpecified(RestrictionNoticeType.class, f, "restrictionNotice");
        }

        checkSubmitters();
        checkUserReferences(f.getUserReferences(), f);
    }

    /**
     * Check children.
     */
    private void checkChildren() {
        checkUninitializedCollection(f, "children");
        List<IndividualReference> children = f.getChildren();
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
        checkUninitializedCollection(f, "ldsSpouseSealings");
        List<LdsSpouseSealing> ldsSpouseSealings = f.getLdsSpouseSealings();
        if (ldsSpouseSealings != null) {
            checkListOfModelElementsForDups(f, "ldsSpouseSealings");
            checkListOfModelElementsForNulls(f, "ldsSpouseSealings");
            for (LdsSpouseSealing s : ldsSpouseSealings) {
                new LdsSpouseSealingValidator(getValidator(), s).validate();
            }
        }
    }

    /**
     * Check multimedia.
     */
    private void checkMultimedia() {
        checkUninitializedCollection(f, "multimedia");
        List<Multimedia> multimedia = f.getMultimedia();
        if (multimedia != null) {
            checkListOfModelElementsForDups(f, "multimedia");
            checkListOfModelElementsForNulls(f, "multimedia");
            for (Multimedia m : multimedia) {
                new MultimediaValidator(getValidator(), m).validate();
            }
        }
    }

    /**
     * Check submitters.
     */
    private void checkSubmitters() {
        checkUninitializedCollection(f, "submitters");
        List<SubmitterReference> submitters = f.getSubmitters();
        if (submitters != null) {
            checkListOfModelElementsForDups(f, "submitters");
            checkListOfModelElementsForNulls(f, "submitters");
            for (SubmitterReference sRef : submitters) {
                Submitter s = sRef.getSubmitter();
                new SubmitterValidator(getValidator(), s).validate();
            }
        }
    }

}
