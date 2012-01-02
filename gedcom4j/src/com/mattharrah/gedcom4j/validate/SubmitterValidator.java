package com.mattharrah.gedcom4j.validate;

import java.util.ArrayList;

import com.mattharrah.gedcom4j.Submitter;

/**
 * Validate a submitter
 * 
 * @author frizbog1
 * 
 */
public class SubmitterValidator extends AbstractValidator {

    /**
     * The submitter being validated
     */
    private Submitter submitter;

    /**
     * Constructor
     * 
     * @param rootValidator
     *            the root validator containing among other things the findings
     *            collection
     * @param submitter
     *            the submitter being validated
     */
    public SubmitterValidator(GedcomValidator rootValidator, Submitter submitter) {
        this.rootValidator = rootValidator;
        this.submitter = submitter;
    }

    @Override
    protected void validate() {
        if (submitter == null) {
            addError("Submitter being validated is null");
            return;
        }
        checkXref(submitter);
        checkRequiredString(submitter.name, "name", submitter);
        checkLanguagePreferences();
        checkOptionalString(submitter.recIdNumber, "record id number",
                submitter);
        checkOptionalString(submitter.regFileNumber,
                "submitter registered rfn", submitter);
    }

    /**
     * Check the language preferences
     */
    private void checkLanguagePreferences() {
        if (submitter.languagePref == null) {
            if (rootValidator.autorepair) {
                submitter.languagePref = new ArrayList<String>();
                addInfo("Submitter language preference collection was null - autorepaired",
                        submitter);
            } else {
                addInfo("Submitter language preference collection is null",
                        submitter);
            }
        } else {
            if (submitter.languagePref.size() > 3) {
                addError("Submitter exceeds limit on language preferences (3)",
                        submitter);
            }
            for (String s : submitter.languagePref) {
                checkRequiredString(s, "language pref", submitter);
            }
        }
    }
}
