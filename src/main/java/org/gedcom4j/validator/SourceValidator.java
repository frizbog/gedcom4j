package org.gedcom4j.validator;

import org.gedcom4j.model.Source;

/**
 * Validator for {@link Source} objects
 * 
 * @author frizbog
 */
class SourceValidator extends AbstractValidator<Source> {

    /**
     * Constructor
     * 
     * @param validationVisitor
     *            The validation visitor that will visit and validate child elements
     * @param objectToValidate
     *            the object being validated
     */
    protected SourceValidator(ValidationVisitor validationVisitor, Source objectToValidate) {
        super(validationVisitor, objectToValidate);
    }

    @Override
    protected void validate() {
        System.out.println("Validate " + object);

    }

}
