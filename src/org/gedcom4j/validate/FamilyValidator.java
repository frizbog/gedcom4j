package org.gedcom4j.validate;

import java.util.ArrayList;

import org.gedcom4j.model.AbstractCitation;
import org.gedcom4j.model.Event;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.LdsSpouseSealing;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.Submitter;

/**
 * Validator for {@link Family} objects
 * 
 * @author frizbog1
 */
public class FamilyValidator extends AbstractValidator {

    /**
     * The family being validated
     */
    private Family f;

    /**
     * Validator for {@link Family}
     * 
     * @param gedcomValidator
     *            the {@link GedcomValidator} that holds all the findings and
     *            settings
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
                    rootValidator.addError("Family with xref '" + f.xref + "' has a null entry in children collection",
                            f);
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
        } // TODO - validate the LDS Spouse Sealing if there are any
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
