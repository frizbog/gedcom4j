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

import java.util.ArrayList;

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
        checkOptionalString(f.automatedRecordId, "Automated record id", f);
        checkChangeDate(f.changeDate, f);
        if (f.children == null) {
            if (rootValidator.autorepair) {
                f.children = new ArrayList<Individual>();
                rootValidator.addInfo("Family's collection of children was null - repaired", f);
            } else {
                rootValidator.addError("Family's collection of children is null", f);
            }
        } else {
            for (Individual i : f.children) {
                if (i == null) {
                    rootValidator.addError("Family with xref '" + f.xref + "' has a null entry in children collection", f);
                }
            }
        }
        if (f.citations == null) {
            if (rootValidator.autorepair) {
                f.citations = new ArrayList<AbstractCitation>();
                addInfo("citations collection for family was null - rootValidator.autorepaired", f);
            } else {
                addError("citations collection for family is null", f);
            }
        } else {
            for (AbstractCitation c : f.citations) {
                new CitationValidator(rootValidator, c).validate();
            }
        }
        checkCustomTags(f);
        for (Event ev : f.events) {
            new EventValidator(rootValidator, ev).validate();
        }
        if (f.husband != null) {
            new IndividualValidator(rootValidator, f.husband).validate();
        }
        if (f.wife != null) {
            new IndividualValidator(rootValidator, f.wife).validate();
        }
        if (f.ldsSpouseSealings == null) {
            if (rootValidator.autorepair) {
                f.ldsSpouseSealings = new ArrayList<LdsSpouseSealing>();
                addInfo("LDS spouse sealings collection for family was null - rootValidator.autorepaired", f);
            } else {
                addError("LDS spouse sealings collection for family is null", f);
            }
        } else {
            for (LdsSpouseSealing s : f.ldsSpouseSealings) {
                new LdsSpouseSealingValidator(rootValidator, s).validate();
            }
        }
        if (f.multimedia == null) {
            if (rootValidator.autorepair) {
                f.multimedia = new ArrayList<Multimedia>();
                addInfo("Multimedia collection for family was null - rootValidator.autorepaired", f);
            } else {
                addError("Multimedia collection for family is null", f);
            }
        } else {
            for (Multimedia m : f.multimedia) {
                new MultimediaValidator(rootValidator, m).validate();
            }
        }
        checkNotes(f.notes, f);
        checkOptionalString(f.numChildren, "number of children", f);
        checkOptionalString(f.recFileNumber, "record file number", f);
        checkOptionalString(f.restrictionNotice, "restriction notice", f);
        if (f.submitters == null) {
            if (rootValidator.autorepair) {
                f.submitters = new ArrayList<Submitter>();
                addInfo("Submitters collection was missing on family - repaired", f);
            } else {
                addInfo("Submitters collection is missing on family", f);
                return;
            }
        } else {
            for (Submitter s : f.submitters) {
                new SubmitterValidator(rootValidator, s).validate();
            }
        }
        checkUserReferences(f.userReferences, f);
    }

}
