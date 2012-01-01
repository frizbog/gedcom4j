package com.mattharrah.gedcom4j.validate;

import junit.framework.TestCase;

import com.mattharrah.gedcom4j.Gedcom;

/**
 * A base class for validator tests with handy helper methods
 * 
 * @author frizbog1
 */
public abstract class AbstractValidatorTestCase extends TestCase {

    /**
     * Root validator - test fixture
     */
    protected GedcomValidator rootValidator;
    /**
     * The test fixture gedcom structure
     */
    protected Gedcom gedcom;

    /**
     * Default constructor
     */
    public AbstractValidatorTestCase() {
        super();
    }

    /**
     * Constructor that takes a test name
     * 
     * @param name
     */
    public AbstractValidatorTestCase(String name) {
        super(name);
    }

    /**
     * Assert that there are no errors or warnings in the findings
     */
    protected void assertNoIssues() {
        if (rootValidator.hasErrors() || rootValidator.hasWarnings()) {
            dumpFindings();
            fail("There should not be any warnings or errors");
        }
    }

    /**
     * Write all the findings out to stdout
     */
    protected void dumpFindings() {
        for (GedcomValidationFinding f : rootValidator.findings) {
            System.out.println(f);
        }
    }
}