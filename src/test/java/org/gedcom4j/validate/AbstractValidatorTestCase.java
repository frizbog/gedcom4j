/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.gedcom4j.validate;

import junit.framework.TestCase;

import org.gedcom4j.model.Gedcom;

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
     * Determines whether to write noise out to System.out. Should ALWAYS default to false, and be turned to true for
     * specific tests, if needed.
     */
    protected boolean verbose = false;

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
     *            the test name
     */
    public AbstractValidatorTestCase(String name) {
        super(name);
    }

    /**
     * Assert that the findings collection on the root validator contains at least one finding of the specified severity
     * with a given substring
     * 
     * @param severity
     *            the expected severity
     * @param substringOfDescription
     *            substring to look for in the finding's description
     */
    protected void assertFindingsContain(Severity severity, String... substringOfDescription) {
        for (GedcomValidationFinding f : rootValidator.findings) {
            if (f.severity == severity) {
                boolean matchAllSoFar = true;
                for (String substring : substringOfDescription) {
                    if (!f.problemDescription.toLowerCase().contains(substring.toLowerCase())) {
                        matchAllSoFar = false;
                    }
                }
                if (matchAllSoFar) {
                    // All the substrings were found, and the severity is right
                    return;
                }
            }
        }
        StringBuilder sb = new StringBuilder("Expected to find at least one finding at severity " + severity);
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
        dumpFindings();
        fail(sb.toString());
    }

    /**
     * Assert that there are no errors or warnings in the findings
     * 
     */
    protected void assertNoIssues() {
        if (rootValidator.hasErrors() || rootValidator.hasWarnings()) {
            boolean saveVerbose = verbose;
            verbose = true;
            dumpFindings();
            verbose = saveVerbose;
            fail("There should not be any warnings or errors");
        }
    }

    /**
     * Write all the findings out to stdout
     */
    protected void dumpFindings() {
        if (rootValidator.findings.isEmpty()) {
            return;
        }
        if (rootValidator.findings.size() > 0) {
            System.out.println(rootValidator.findings.size() + " finding(s) from validation");
        }
        for (GedcomValidationFinding f : rootValidator.findings) {
            System.out.println("  " + f);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        gedcom = new Gedcom();
        rootValidator = new GedcomValidator(gedcom);
    }
}
