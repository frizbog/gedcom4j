package org.gedcom4j.validator;

import org.gedcom4j.model.Note;

/**
 * Validator for {@link Note} objects
 * 
 * @author frizbog
 */
class NoteValidator extends AbstractValidator<Note> {

    /**
     * Constructor
     * 
     * @param validationVisitor
     *            The validation visitor that will visit and validate child elements
     * @param objectToValidate
     *            the object being validated
     */
    protected NoteValidator(ValidationVisitor validationVisitor, Note objectToValidate) {
        super(validationVisitor, objectToValidate);
    }

    @Override
    protected void validate() {
        System.out.println("Validate " + object);

    }

}
