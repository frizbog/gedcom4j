package org.gedcom4j.validator;

import org.gedcom4j.model.Submitter;

/**
 * Validator for {@link Submitter} objects
 * 
 * @author frizbog
 */
class SubmitterValidator extends AbstractValidator<Submitter> {

    /**
     * Constructor
     * 
     * @param validationVisitor
     *            The validation visitor that will visit and validate child elements
     * @param objectToValidate
     *            the object being validated
     */
    protected SubmitterValidator(ValidationVisitor validationVisitor, Submitter objectToValidate) {
        super(validationVisitor, objectToValidate);
    }

    @Override
    protected void validate() {
        System.out.println("Validate " + object);

    }

}
