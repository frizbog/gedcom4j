package com.mattharrah.gedcom4j.validate;

import com.mattharrah.gedcom4j.AbstractCitation;

public class CitationValidator extends AbstractValidator {

    /**
     * The citation being validated
     */
    AbstractCitation citation;

    /**
     * Constructor
     * 
     * @param rootValidator
     *            the root validator with the collection of findings
     * @param citation
     *            the citation being validated
     */
    public CitationValidator(GedcomValidator rootValidator,
            AbstractCitation citation) {
        this.rootValidator = rootValidator;
        this.citation = citation;
    }

    @Override
    protected void validate() {
        if (citation == null) {
            addError("Citation is null");
        }
    }

}
