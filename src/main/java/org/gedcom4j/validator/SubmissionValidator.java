package org.gedcom4j.validator;

import org.gedcom4j.model.Submission;

/**
 * Validator for {@link Submission} objects
 * 
 * @author frizbog
 */
class SubmissionValidator extends AbstractValidator<Submission> {

    /**
     * Constructor
     * 
     * @param validationVisitor
     *            The validation visitor that will visit and validate child elements
     * @param objectToValidate
     *            the object being validated
     */
    protected SubmissionValidator(ValidationVisitor validationVisitor, Submission objectToValidate) {
        super(validationVisitor, objectToValidate);
    }

    @Override
    protected void validate() {
        System.out.println("Validate " + object);

    }

}
