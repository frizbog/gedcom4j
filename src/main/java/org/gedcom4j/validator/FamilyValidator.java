package org.gedcom4j.validator;

import org.gedcom4j.model.Family;

/**
 * Validator for {@link Family} objects
 * 
 * @author frizbog
 */
class FamilyValidator extends AbstractValidator<Family> {

    /**
     * Constructor
     * 
     * @param validationVisitor
     *            The validation visitor that will visit and validate child elements
     * @param objectToValidate
     *            the object being validated
     */
    protected FamilyValidator(ValidationVisitor validationVisitor, Family objectToValidate) {
        super(validationVisitor, objectToValidate);
    }

    @Override
    protected void validate() {
        System.out.println("Validate " + object);

    }

}
