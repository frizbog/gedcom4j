package org.gedcom4j.validator;

import org.gedcom4j.model.Individual;

/**
 * Validator for {@link Individual} objects
 * 
 * @author frizbog
 */
class IndividualValidator extends AbstractValidator<Individual> {

    /**
     * Constructor
     * 
     * @param validationVisitor
     *            The validation visitor that will visit and validate child elements
     * @param objectToValidate
     *            the object being validated
     */
    protected IndividualValidator(ValidationVisitor validationVisitor, Individual objectToValidate) {
        super(validationVisitor, objectToValidate);
    }

    @Override
    protected void validate() {
        System.out.println("Validate " + object);
    }

}
