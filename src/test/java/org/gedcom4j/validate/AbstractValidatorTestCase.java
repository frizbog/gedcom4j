/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package org.gedcom4j.validate;

import org.gedcom4j.model.Gedcom;

import junit.framework.TestCase;

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
     * Determines whether to write noise out to System.out. Should ALWAYS default to false, and be turned to true for specific
     * tests, if needed.
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
     * {@inheritDoc}
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        gedcom = new Gedcom();
        rootValidator = new GedcomValidator(gedcom);
    }

    /**
     * Assert that the findings collection on the root validator contains at least one finding of the specified severity with a
     * given substring
     * 
     * @param severity
     *            the expected severity
     * @param substringOfDescription
     *            substring to look for in the finding's description
     */
    protected void assertFindingsContain(Severity severity, String... substringOfDescription) {
        for (GedcomValidationFinding f : rootValidator.getFindings()) {
            if (f.getSeverity() == severity) {
                boolean matchAllSoFar = true;
                for (String substring : substringOfDescription) {
                    if (!f.getProblemDescription().toLowerCase().contains(substring.toLowerCase())) {
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
            verbose = saveVerbose;
            fail("There should not be any warnings or errors");
        }
    }
}
