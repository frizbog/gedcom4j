package org.gedcom4j.validator;

import org.gedcom4j.model.Trailer;

/**
 * Validator for {@link Trailer} objects
 * 
 * @author frizbog
 */
class TrailerValidator extends AbstractValidator<Trailer> {

    /**
     * Constructor
     * 
     * @param validationVisitor
     *            The validation visitor that will visit and validate child elements
     * @param objectToValidate
     *            the object being validated
     */
    protected TrailerValidator(ValidationVisitor validationVisitor, Trailer objectToValidate) {
        super(validationVisitor, objectToValidate);
    }

    @Override
    protected void validate() {
        System.out.println("Validate " + object);

    }

}
