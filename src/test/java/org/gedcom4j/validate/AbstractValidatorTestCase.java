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

import static org.junit.Assert.fail;

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.ModelElement;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.validate.Validator.Finding;

/**
 * A base class for validator tests with handy helper methods
 * 
 * @author frizbog1
 */
public abstract class AbstractValidatorTestCase {

    /**
     * Root validator - test fixture
     */
    protected Validator validator;

    /**
     * The test fixture gedcom structure
     */
    protected Gedcom gedcom = new Gedcom();

    /**
     * Default constructor
     */
    public AbstractValidatorTestCase() {
        gedcom = new Gedcom();
        validator = new Validator(gedcom);
    }

    /**
     * Assert that the findings collection on the root validator contains at least one finding of the specified severity with a
     * given substring
     * 
     * @param severity
     *            the expected severity. Required and must match exactly.
     * @param c
     *            the type (class) of object the finding is expected to be on.
     * @param code
     *            code of the expected finding. Required and must match exactly.
     * @param fieldName
     *            the name of the file with the problem value. Optional, but if supplied, must match exactly.
     */
    @SuppressWarnings("checkstyle:WhitespaceAround")
    protected void assertFindingsContain(Severity severity, Class<? extends ModelElement> c, int code, String fieldName) {
        for (Finding f : validator.getResults().getAllFindings()) {
            if (f.getSeverity() == severity && f.getItemOfConcern().getClass().equals(c) && f.getProblemCode() == code
                    && (fieldName == null || fieldName.equals(f.getFieldNameOfConcern()))) {
                return;
            }
        }
        fail("No finding of severity " + severity + " found on object of type " + c.getName() + " with code " + code + " on field "
                + fieldName + " as expected.\nFindings contain: " + validator.getResults().getAllFindings());
    }

    /**
     * Assert that the findings collection on the root validator contains at least one finding of the specified severity with a
     * given substring
     * 
     * @param severity
     *            the expected severity. Required and must match exactly.
     * @param objectWithFinding
     *            the object the finding is expected to be on. Required and must be the same object, not just one equivalent.
     * @param code
     *            code of the expected finding. Required and must match exactly.
     * @param fieldName
     *            the name of the file with the problem value. Optional, but if supplied, must match exactly.
     */
    protected void assertFindingsContain(Severity severity, ModelElement objectWithFinding, int code, String fieldName) {
        for (Finding f : validator.getResults().getAllFindings()) {
            if (f.getSeverity() == severity && f.getItemOfConcern() == objectWithFinding && f.getProblemCode() == code
                    && (fieldName == null || fieldName.equals(f.getFieldNameOfConcern()))) {
                return;
            }
        }
        fail("No finding of severity " + severity + " found on object of type " + objectWithFinding.getClass().getName()
                + " with code " + code + " on field " + fieldName + " as expected.\nFindings contain: " + validator.getResults()
                        .getAllFindings());
    }

    /**
     * Assert that the findings collection on the root validator contains at least one finding of the specified severity with a
     * given substring
     * 
     * @param severity
     *            the expected severity. Required and must match exactly.
     * @param objectWithFinding
     *            the object the finding is expected to be on. Required and must be the same object, not just one equivalent.
     * @param code
     *            enumerated built-in code of the expected finding. Required and must match exactly (on the code value - description
     *            matching not checked).
     * @param fieldName
     *            the name of the file with the problem value. Optional, but if supplied, must match exactly.
     */
    protected void assertFindingsContain(Severity severity, ModelElement objectWithFinding, ProblemCode code, String fieldName) {
        assertFindingsContain(severity, objectWithFinding, code.getCode(), fieldName);
    }

    /**
     * Assert that there are no errors or warnings in the findings
     * 
     */
    protected void assertNoIssues() {
        if (!validator.getResults().getAllFindings().isEmpty()) {
            StringBuilder sb = new StringBuilder(100);
            boolean first = true;
            for (Finding f : validator.getResults().getAllFindings()) {
                if (!first) {
                    sb.append("\n");
                }
                sb.append("\t").append(f);
            }
            fail("There should not be any warnings or errors, but there were:" + sb.toString());
        }
    }

    /**
     * Load a file for the tes
     *
     * @param fileName
     *            the file name
     * @throws IOException
     *             if the file cannot be read
     * @throws GedcomParserException
     *             if the file cannot be parsed
     */
    protected void loadFile(String fileName) throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load(fileName);
        gedcom = gp.getGedcom();
        validator = new Validator(gedcom);
    }
}
