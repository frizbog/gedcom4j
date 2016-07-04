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

import org.gedcom4j.model.*;

/**
 * Validator for {@link Family} objects
 * 
 * @author frizbog1
 */
class FamilyValidator extends AbstractValidator {

    /**
     * The family being validated
     */
    private final Family f;

    /**
     * Validator for {@link Family}
     * 
     * @param gedcomValidator
     *            the {@link GedcomValidator} that holds all the findings and settings
     * @param f
     *            the family being validated
     */
    public FamilyValidator(GedcomValidator gedcomValidator, Family f) {
        rootValidator = gedcomValidator;
        this.f = f;
    }

    @Override
    protected void validate() {
        checkOptionalString(f.getAutomatedRecordId(), "Automated record id", f);
        checkChangeDate(f.getChangeDate(), f);
        if (f.getChildren() == null) {
            if (rootValidator.autorepair) {
                f.getChildren(true).clear();
                rootValidator.addInfo("Family's collection of children was null - repaired", f);
            } else {
                rootValidator.addError("Family's collection of children is null", f);
            }
        } else {
            for (Individual i : f.getChildren()) {
                if (i == null) {
                    rootValidator.addError("Family with xref '" + f.getXref() + "' has a null entry in children collection", f);
                }
            }
        }
        if (f.getCitations() == null) {
            if (rootValidator.autorepair) {
                f.getCitations(true).clear();
                addInfo("citations collection for family was null - rootValidator.autorepaired", f);
            } else {
                addError("citations collection for family is null", f);
            }
        } else {
            for (AbstractCitation c : f.getCitations()) {
                new CitationValidator(rootValidator, c).validate();
            }
        }
        checkCustomTags(f);
        for (AbstractEvent ev : f.getEvents()) {
            new EventValidator(rootValidator, ev).validate();
        }
        if (f.getHusband() != null) {
            new IndividualValidator(rootValidator, f.getHusband()).validate();
        }
        if (f.getWife() != null) {
            new IndividualValidator(rootValidator, f.getWife()).validate();
        }
        if (f.getLdsSpouseSealings() == null) {
            if (rootValidator.autorepair) {
                f.getLdsSpouseSealings(true).clear();
                addInfo("LDS spouse sealings collection for family was null - rootValidator.autorepaired", f);
            } else {
                addError("LDS spouse sealings collection for family is null", f);
            }
        } else {
            for (LdsSpouseSealing s : f.getLdsSpouseSealings()) {
                new LdsSpouseSealingValidator(rootValidator, s).validate();
            }
        }
        if (f.getMultimedia() == null) {
            if (rootValidator.autorepair) {
                f.getMultimedia(true).clear();
                addInfo("Multimedia collection for family was null - rootValidator.autorepaired", f);
            } else {
                addError("Multimedia collection for family is null", f);
            }
        } else {
            for (Multimedia m : f.getMultimedia()) {
                new MultimediaValidator(rootValidator, m).validate();
            }
        }
        checkNotes(f.getNotes(), f);
        checkOptionalString(f.getNumChildren(), "number of children", f);
        checkOptionalString(f.getRecFileNumber(), "record file number", f);
        checkOptionalString(f.getRestrictionNotice(), "restriction notice", f);
        if (f.getSubmitters() == null) {
            if (rootValidator.autorepair) {
                f.getSubmitters(true).clear();
                addInfo("Submitters collection was missing on family - repaired", f);
            } else {
                addInfo("Submitters collection is missing on family", f);
                return;
            }
        } else {
            for (Submitter s : f.getSubmitters()) {
                new SubmitterValidator(rootValidator, s).validate();
            }
        }
        checkUserReferences(f.getUserReferences(), f);
    }

}
