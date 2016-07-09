package org.gedcom4j.validator;

import org.gedcom4j.model.Header;

/**
 * Validator for {@link Header} objects
 * 
 * @author frizbog
 */
class HeaderValidator extends AbstractValidator<Header> {

    /**
     * Constructor
     * 
     * @param validationVisitor
     *            The validation visitor that will visit and validate child elements
     * @param objectToValidate
     *            the object being validated
     */
    protected HeaderValidator(ValidationVisitor validationVisitor, Header objectToValidate) {
        super(validationVisitor, objectToValidate);
    }

    @Override
    protected void validate() {
        System.out.println("Validate " + object);

    }

}
