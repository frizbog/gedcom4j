package com.mattharrah.gedcom4j.validate;

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
        if (submitter.xref == null || submitter.xref.trim().length() == 0) {
            addError("Submitter has no xref", submitter);
        }
    }
}
