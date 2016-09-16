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

import org.gedcom4j.model.StringWithCustomTags;
import org.gedcom4j.model.Submitter;
import org.junit.Test;

/**
 * Test case for {@link SubmitterValidator}
 * 
 * @author frizbog1
 * 
 */
public class SubmitterValidatorTest extends AbstractValidatorTestCase {

    /**
     * Test method for {@link org.gedcom4j.validate.SubmitterValidator#validate()}.
     */
    @Test(expected = NullPointerException.class)
    public void testValidateNullSubmitter() {
        AbstractValidator sv = new SubmitterValidator(validator, null);
        sv.validate();
    }

    /**
     * Test method for {@link org.gedcom4j.validate.SubmitterValidator#validate()}.
     */
    @Test
    public void testValidateSubmitterHappyPath() {
        Submitter submitter = new Submitter();
        submitter.setName(new StringWithCustomTags("somebody"));
        submitter.setXref("@nobody@");
        AbstractValidator sv = new SubmitterValidator(validator, submitter);
        sv.validate();
        assertNoIssues();
    }

    /**
     * Test method for {@link org.gedcom4j.validate.SubmitterValidator#validate()}.
     */
    @Test
    public void testValidateSubmitterHasBlankName() {
        Submitter submitter = new Submitter();
        submitter.setXref("@SOMEVALUE@");
        submitter.setName(new StringWithCustomTags(""));
        AbstractValidator sv = new SubmitterValidator(validator, submitter);
        sv.validate();
        assertFindingsContain(Severity.ERROR, submitter, ProblemCode.MISSING_REQUIRED_VALUE, "name");
    }

    /**
     * Test method for {@link org.gedcom4j.validate.SubmitterValidator#validate()}.
     */
    @Test
    public void testValidateSubmitterHasBlankXref() {
        Submitter submitter = new Submitter();
        submitter.setName(new StringWithCustomTags("somebody"));
        submitter.setXref("");
        AbstractValidator sv = new SubmitterValidator(validator, submitter);
        sv.validate();
        assertFindingsContain(Severity.ERROR, submitter, ProblemCode.MISSING_REQUIRED_VALUE, "xref");
    }

    /**
     * Test method for {@link org.gedcom4j.validate.SubmitterValidator#validate()}.
     */
    @Test
    public void testValidateSubmitterHasNoName() {
        Submitter submitter = new Submitter();
        submitter.setXref("@SOMEVALUE@");
        AbstractValidator sv = new SubmitterValidator(validator, submitter);
        sv.validate();
        assertFindingsContain(Severity.ERROR, submitter, ProblemCode.MISSING_REQUIRED_VALUE, "name");
    }

    /**
     * Test method for {@link org.gedcom4j.validate.SubmitterValidator#validate()}.
     */
    @Test
    public void testValidateSubmitterHasNoXref() {
        Submitter submitter = new Submitter();
        submitter.setName(new StringWithCustomTags("somebody"));
        AbstractValidator sv = new SubmitterValidator(validator, submitter);
        sv.validate();
        assertFindingsContain(Severity.ERROR, submitter, ProblemCode.MISSING_REQUIRED_VALUE, "xref");
    }

}
