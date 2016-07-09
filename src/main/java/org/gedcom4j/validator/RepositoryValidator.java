package org.gedcom4j.validator;

import org.gedcom4j.model.Repository;

/**
 * Validator for {@link Repository} objects
 * 
 * @author frizbog
 */
class RepositoryValidator extends AbstractValidator<Repository> {

    /**
     * Constructor
     * 
     * @param validationVisitor
     *            The validation visitor that will visit and validate child elements
     * @param objectToValidate
     *            the object being validated
     */
    protected RepositoryValidator(ValidationVisitor validationVisitor, Repository objectToValidate) {
        super(validationVisitor, objectToValidate);
    }

    @Override
    protected void validate() {
        System.out.println("Validate " + object);

    }

}
