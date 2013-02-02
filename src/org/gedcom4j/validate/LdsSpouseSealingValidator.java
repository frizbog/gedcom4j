package org.gedcom4j.validate;

import java.util.ArrayList;

import org.gedcom4j.model.AbstractCitation;
import org.gedcom4j.model.LdsSpouseSealing;

/**
 * Validator for {@link LdsSpouseSealing} objects
 * 
 * @author frizbog1
 * 
 */
public class LdsSpouseSealingValidator extends AbstractValidator {

    /**
     * The sealing being validated
     */
    private LdsSpouseSealing s;

    /**
     * Constructor
     * 
     * @param rootValidator
     *            root {@link GedcomValidator} that contains findings and
     *            settings
     * @param s
     *            the sealing being validated
     */
    public LdsSpouseSealingValidator(GedcomValidator rootValidator, LdsSpouseSealing s) {
        this.rootValidator = rootValidator;
        this.s = s;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gedcom4j.validate.AbstractValidator#validate()
     */
    @Override
    protected void validate() {
        if (s == null) {
            addError("LDS Spouse Sealing is null and cannot be validated");
            return;
        }
        if (s.citations == null) {
            if (rootValidator.autorepair) {
                s.citations = new ArrayList<AbstractCitation>();
                addInfo("citations collection for lds spouse sealing was null - rootValidator.autorepaired", s);
            } else {
                addError("citations collection for lds spouse sealing is null", s);
            }
        } else {
            for (AbstractCitation c : s.citations) {
                new CitationValidator(rootValidator, c).validate();
            }
        }
        checkCustomTags(s);
        checkOptionalString(s.date, "date", s);
        checkNotes(s.notes, s);
        checkOptionalString(s.place, "place", s);
        checkOptionalString(s.status, "status", s);
        checkOptionalString(s.temple, "temple", s);
    }

}
