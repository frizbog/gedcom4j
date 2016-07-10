package org.gedcom4j.validator;

/**
 * Base class for validators of various types of objects
 * 
 * @author frizbog
 * @param <T>
 *            the type of object being validated
 */
public abstract class AbstractValidator<T> {
    /**
     * The validation visitor that will visit and validate child elements
     */
    protected ValidationVisitor validationVisitor;

    /**
     * Object being validated
     */
    protected final T object;

    /**
     * Constructor
     * 
     * @param validationVisitor
     *            The validation visitor that will visit and validate child elements
     * @param objectToValidate
     *            the object being validated
     */
    protected AbstractValidator(ValidationVisitor validationVisitor, T objectToValidate) {
        this.validationVisitor = validationVisitor;
        object = objectToValidate;
    }

    /**
     * Do the validation
     */
    protected abstract void validate();
}
