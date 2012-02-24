package com.mattharrah.gedcom4j.validate;

import com.mattharrah.gedcom4j.model.Multimedia;

/**
 * A validator for {@link Multimedia} objects. See {@link GedcomValidator} for
 * usage instructions.
 * 
 * @author frizbog1
 * 
 */
public class MultimediaValidator extends AbstractValidator {

    /**
     * The multimedia being validated
     */
    private Multimedia multimedia;

    /**
     * Constructor
     * 
     * @param rootValidator
     *            the root validator
     * @param multimedia
     *            the multimedia object being validated
     */
    public MultimediaValidator(GedcomValidator rootValidator,
            Multimedia multimedia) {
        this.rootValidator = rootValidator;
        this.multimedia = multimedia;
    }

    @Override
    protected void validate() {
        // TODO Actually validate the multimedia object
    }

}
