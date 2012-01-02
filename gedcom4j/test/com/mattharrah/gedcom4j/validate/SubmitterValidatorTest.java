/*
 * Copyright (c) 2009-2011 Matthew R. Harrah
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
package com.mattharrah.gedcom4j.validate;

import com.mattharrah.gedcom4j.Submitter;

/**
 * Test cas for {@link SubmitterValidator}
 * 
 * @author frizbog1
 * 
 */
public class SubmitterValidatorTest extends AbstractValidatorTestCase {

    /**
     * Test method for
     * {@link com.mattharrah.gedcom4j.validate.SubmitterValidator#validate()}.
     */
    public void testValidateNullSubmitter() {
        AbstractValidator sv = new SubmitterValidator(rootValidator, null);
        sv.validate();
        assertFindingsContain(Severity.ERROR, "submitter", "null");
    }

    /**
     * Test method for
     * {@link com.mattharrah.gedcom4j.validate.SubmitterValidator#validate()}.
     */
    public void testValidateSubmitterHappyPath() {
        Submitter submitter = new Submitter();
        submitter.name = "somebody";
        submitter.xref = "@nobody@";
        AbstractValidator sv = new SubmitterValidator(rootValidator, submitter);
        sv.validate();
        dumpFindings();
        assertTrue(rootValidator.findings.isEmpty());
    }

    /**
     * Test method for
     * {@link com.mattharrah.gedcom4j.validate.SubmitterValidator#validate()}.
     */
    public void testValidateSubmitterHasBlankName() {
        Submitter submitter = new Submitter();
        submitter.xref = "@SOMEVALUE@";
        submitter.name = "";
        AbstractValidator sv = new SubmitterValidator(rootValidator, submitter);
        sv.validate();
        assertFindingsContain(Severity.ERROR, "name", "blank", "null");
    }

    /**
     * Test method for
     * {@link com.mattharrah.gedcom4j.validate.SubmitterValidator#validate()}.
     */
    public void testValidateSubmitterHasBlankXref() {
        Submitter submitter = new Submitter();
        submitter.name = "somebody";
        submitter.xref = "";
        AbstractValidator sv = new SubmitterValidator(rootValidator, submitter);
        sv.validate();
        assertFindingsContain(Severity.ERROR, "xref", "too short");
        assertFindingsContain(Severity.ERROR, "xref", "null");
        assertFindingsContain(Severity.ERROR, "xref", "@");
    }

    /**
     * Test method for
     * {@link com.mattharrah.gedcom4j.validate.SubmitterValidator#validate()}.
     */
    public void testValidateSubmitterHasNoName() {
        Submitter submitter = new Submitter();
        submitter.xref = "@SOMEVALUE@";
        AbstractValidator sv = new SubmitterValidator(rootValidator, submitter);
        sv.validate();
        assertFindingsContain(Severity.ERROR, "name", "blank", "null");
    }

    /**
     * Test method for
     * {@link com.mattharrah.gedcom4j.validate.SubmitterValidator#validate()}.
     */
    public void testValidateSubmitterHasNoXref() {
        Submitter submitter = new Submitter();
        submitter.name = "somebody";
        AbstractValidator sv = new SubmitterValidator(rootValidator, submitter);
        sv.validate();
        assertFindingsContain(Severity.ERROR, "xref", "blank", "null");
    }

}
