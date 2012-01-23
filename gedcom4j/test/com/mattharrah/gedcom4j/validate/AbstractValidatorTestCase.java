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
     * Assert that the findings collection on the root validator contains at
     * least one finding of the specified severity with a given substring
     * 
     * @param severity
     *            the expected severity
     * @param substringOfDescription
     *            substring to look for in the finding's description
     */
    protected void assertFindingsContain(Severity severity,
            String... substringOfDescription) {
        for (GedcomValidationFinding f : rootValidator.findings) {
            if (f.severity == severity) {
                boolean matchAllSoFar = true;
                for (String substring : substringOfDescription) {
                    if (!f.problemDescription.toLowerCase().contains(substring)) {
                        matchAllSoFar = false;
                    }
                }
                if (matchAllSoFar) {
                    // All the substrings were found, and the severity is right
                    return;
                }
            }
        }
        StringBuilder sb = new StringBuilder(
                "Expected to find at least one finding at severity " + severity);
        if (substringOfDescription != null && substringOfDescription.length > 0) {
            for (int i = 0; i < substringOfDescription.length; i++) {
                if (i == 0) {
                    sb.append(" mentioning '");
                } else {
                    sb.append(" and '");
                }
                sb.append(substringOfDescription[i]).append("'");
            }
        }
        dumpFindings(rootValidator);
        fail(sb.toString());
    }

    /**
     * Assert that there are no errors or warnings in the findings
     */
    protected void assertNoIssues() {
        if (rootValidator.hasErrors() || rootValidator.hasWarnings()) {
            dumpFindings(rootValidator);
            fail("There should not be any warnings or errors");
        }
    }

    /**
     * Write all the findings out to stdout
     * 
     * @param v
     */
    protected void dumpFindings(AbstractValidator v) {
        if (v.rootValidator.findings.size() > 0) {
            System.out.println(v.rootValidator.findings.size()
                    + " finding(s) from validation");
        }
        for (GedcomValidationFinding f : v.rootValidator.findings) {
            System.out.println(f);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        gedcom = new Gedcom();
        rootValidator = new GedcomValidator(gedcom);
    }
}
