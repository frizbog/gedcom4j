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

import org.gedcom4j.model.LdsBaptismDateStatus;
import org.gedcom4j.model.LdsIndividualOrdinance;
import org.gedcom4j.model.LdsIndividualOrdinanceType;
import org.gedcom4j.model.Note;
import org.gedcom4j.model.StringWithCustomTags;
import org.junit.Test;

/**
 * Test for {@link LdsIndividualOrdinanceValidator}
 * 
 * @author frizbog
 */
public class LdsIndividualOrdinanceValidatorTest extends AbstractValidatorTestCase {

    /**
     * Negative test case - bad enum string
     */
    @Test
    public void testBadEnumString1() {
        LdsIndividualOrdinance l = new LdsIndividualOrdinance();
        l.setDate("5 MAY 1905");
        l.setTemple("Temple 1");
        l.setType(LdsIndividualOrdinanceType.BAPTISM);
        l.setStatus("I'm a bad baptism status");

        new LdsIndividualOrdinanceValidator(validator, l).validate();
        assertFindingsContain(Severity.ERROR, l, ProblemCode.ILLEGAL_VALUE, "status");
    }

    /**
     * Negative test case - no type
     */
    @Test
    public void testNoType() {
        LdsIndividualOrdinance l = new LdsIndividualOrdinance();
        l.setDate("5 MAY 1905");
        l.setTemple("Temple 1");
        Note n = new Note();
        n.getLines(true).add("Testing 1 2 3");
        l.getNotes(true).add(n);

        new LdsIndividualOrdinanceValidator(validator, l).validate();
        assertFindingsContain(Severity.ERROR, l, ProblemCode.MISSING_REQUIRED_VALUE, "type");
    }

    /**
     * Positive test case
     */
    @Test
    public void testPositive() {
        LdsIndividualOrdinance l = new LdsIndividualOrdinance();
        l.setDate("5 MAY 1905");
        l.setTemple("Temple 1");
        Note n = new Note();
        n.getLines(true).add("Testing 1 2 3");
        l.getNotes(true).add(n);
        l.setType(LdsIndividualOrdinanceType.BAPTISM);

        new LdsIndividualOrdinanceValidator(validator, l).validate();
        assertNoIssues();
    }

    /**
     * Positive test case - valid status given ordinance type
     */
    @Test
    public void testPositiveEnumString() {
        LdsIndividualOrdinance l = new LdsIndividualOrdinance();
        l.setDate("5 MAY 1905");
        l.setTemple("Temple 1");
        l.setType(LdsIndividualOrdinanceType.BAPTISM);
        l.setStatus(new StringWithCustomTags(LdsBaptismDateStatus.CHILD.getCode()));

        new LdsIndividualOrdinanceValidator(validator, l).validate();
        assertNoIssues();
    }

}
