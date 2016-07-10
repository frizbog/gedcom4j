package org.gedcom4j.validator;

import org.gedcom4j.model.Address;

/**
 * Validator for {@link Address} objects
 * 
 * @author frizbog
 */
class AddressValidator extends AbstractValidator<Address> {

    /**
     * Constructor
     * 
     * @param validationVisitor
     *            The validation visitor that will visit and validate child elements
     * @param objectToValidate
     *            the object being validated
     */
    protected AddressValidator(ValidationVisitor validationVisitor, Address objectToValidate) {
        super(validationVisitor, objectToValidate);
    }

    @Override
    protected void validate() {
        System.out.println("Validate " + object);

    }

}
