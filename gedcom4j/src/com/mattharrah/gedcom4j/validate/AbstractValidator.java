package com.mattharrah.gedcom4j.validate;

/**
 * A base class for all validators
 * 
 * @author frizbog1
 * 
 */
public abstract class AbstractValidator {

    /**
     * The root validator - the one that holds the collection of findings among
     * other things
     */
    protected GedcomValidator rootValidator;

    /**
     * Add a new finding of severity ERROR
     * 
     * @param description
     *            the description of the error
     */
    protected void addError(String description) {
        rootValidator.findings.add(new GedcomValidationFinding(description,
                Severity.ERROR, null));
    }

    /**
     * Add a new finding of severity ERROR
     * 
     * @param description
     *            the description of the error
     * @param o
     *            the object in error
     */
    protected void addError(String description, Object o) {
        rootValidator.findings.add(new GedcomValidationFinding(description,
                Severity.ERROR, o));
    }

    /**
     * Add a new finding of severity INFO
     * 
     * @param description
     *            the description of the finding
     */
    protected void addInfo(String description) {
        rootValidator.findings.add(new GedcomValidationFinding(description,
                Severity.INFO, null));
    }

    /**
     * Add a new finding of severity INFO
     * 
     * @param description
     *            the description of the finding
     * @param o
     *            the object in error
     */
    protected void addInfo(String description, Object o) {
        rootValidator.findings.add(new GedcomValidationFinding(description,
                Severity.INFO, o));
    }

    /**
     * Add a new finding of severity WARNING
     * 
     * @param description
     *            the description of the warning
     */
    protected void addWarning(String description) {
        rootValidator.findings.add(new GedcomValidationFinding(description,
                Severity.WARNING, null));
    }

    /**
     * Add a new finding of severity WARNING
     * 
     * @param description
     *            the description of the warning
     * @param o
     *            the object in error
     */
    protected void addWarning(String description, Object o) {
        rootValidator.findings.add(new GedcomValidationFinding(description,
                Severity.WARNING, o));
    }

    /**
     * Are there any errors in the findings (so far)?
     * 
     * @return true if there exists at least one finding with severity ERROR
     */
    protected boolean hasErrors() {
        for (GedcomValidationFinding finding : rootValidator.findings) {
            if (finding.severity == Severity.ERROR) {
                return true;
            }
        }
        return false;
    }

    /**
     * Are there any warnings in the findings (so far)?
     * 
     * @return true if there exists at least one finding with severity WARNING
     */
    protected boolean hasWarnings() {
        for (GedcomValidationFinding finding : rootValidator.findings) {
            if (finding.severity == Severity.WARNING) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validate the gedcom file
     */
    protected abstract void validate();
}
