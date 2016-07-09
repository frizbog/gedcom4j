package org.gedcom4j.validator;

import org.gedcom4j.model.Multimedia;

/**
 * Validator for {@link Multimedia} objects
 * 
 * @author frizbog
 */
class MultimediaValidator extends AbstractValidator<Multimedia> {

    /**
     * Constructor
     * 
     * @param validationVisitor
     *            The validation visitor that will visit and validate child elements
     * @param objectToValidate
     *            the object being validated
     */
    protected MultimediaValidator(ValidationVisitor validationVisitor, Multimedia objectToValidate) {
        super(validationVisitor, objectToValidate);
    }

    @Override
    protected void validate() {
        System.out.println("Validate " + object);
    }

}
