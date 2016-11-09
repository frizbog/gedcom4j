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

import org.gedcom4j.model.Submission;
import org.gedcom4j.model.Submitter;
import org.junit.Test;

/**
 * Test for {@link SubmissionValidator}
 * 
 * @author frizbog
 *
 */
public class SubmissionValidatorTest extends AbstractValidatorTestCase {

    /**
     * Test an empty submission record
     */
    @Test
    public void testEmptySubmission() {
        Submission s = new Submission();
        new SubmissionValidator(validator, s).validate();
        assertFindingsContain(Severity.ERROR, Submission.class, ProblemCode.MISSING_REQUIRED_VALUE.getCode(), "xref");
    }

    /**
     * Test an minimal submission record
     */
    @Test
    public void testMinimalSubmission() {
        Submission s = new Submission();
        s.setXref("@SUBM1@");
        new SubmissionValidator(validator, s).validate();
        assertNoIssues();
    }

    /**
     * Test a reasonably well-populated submission record
     */
    @Test
    public void testPopulatedSubmission() {
        Submission s = new Submission();
        s.setXref("@SUBM1@");
        s.setAncestorsCount("1");
        s.setDescendantsCount("2");
        s.setNameOfFamilyFile("Foo");
        s.setOrdinanceProcessFlag("yes");
        s.setRecIdNumber("1");
        s.setSubmitter(new Submitter());
        s.setTempleCode("ABC");
        new SubmissionValidator(validator, s).validate();
        assertNoIssues();

        s.setOrdinanceProcessFlag("Y");
        new SubmissionValidator(validator, s).validate();
        assertFindingsContain(Severity.ERROR, Submission.class, ProblemCode.ILLEGAL_VALUE.getCode(), "ordinanceProcessFlag");
    }
}
