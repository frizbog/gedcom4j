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

import org.gedcom4j.model.Family;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.LdsSpouseSealing;
import org.gedcom4j.model.StringWithCustomTags;
import org.gedcom4j.model.TestHelper;
import org.gedcom4j.model.enumerations.LdsSpouseSealingDateStatus;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link LdsSpouseSealingValidator}
 * 
 * @author frizbog1
 */
public class LdsSpouseSealingValidatorTest extends AbstractValidatorTestCase {
    /**
     * The family being validated
     */
    private Family f;

    /**
     * set up before the test
     */
    @Before
    public void setUp() {
        gedcom = TestHelper.getMinimalGedcom();
        validator = new Validator(gedcom);
        validator.setAutoRepairResponder(Validator.AUTO_REPAIR_NONE);

        final Individual dad = new Individual();
        dad.setXref("@I00001@");
        gedcom.getIndividuals().put(dad.getXref(), dad);

        final Individual mom = new Individual();
        mom.setXref("@I00002@");
        gedcom.getIndividuals().put(mom.getXref(), mom);

        final Individual jr = new Individual();
        jr.setXref("@I00003@");
        gedcom.getIndividuals().put(jr.getXref(), jr);

        f = new Family();
        f.setXref("@F0001@");
        f.setHusband(dad);
        f.setWife(mom);
        f.getChildren(true).add(jr);
        gedcom.getFamilies().put(f.getXref(), f);

        validator.validate();
        assertNoIssues();
    }

    /**
     * Test when citations are messed up
     */
    @Test
    public void testCitations() {
        LdsSpouseSealing s = new LdsSpouseSealing();
        f.getLdsSpouseSealings(true).add(s);

        s.getCitations(true).clear();
        validator.validate();
        assertNoIssues();
    }

    /**
     * Test when custom tags are messed up
     */
    @Test
    public void testCustomTags() {
        LdsSpouseSealing s = new LdsSpouseSealing();
        f.getLdsSpouseSealings(true).add(s);

        s.getCustomTags(true).clear();
        validator.validate();
        assertNoIssues();
    }

    /**
     * Test when citations are messed up
     */
    @Test
    public void testDate() {
        LdsSpouseSealing s = new LdsSpouseSealing();
        f.getLdsSpouseSealings(true).add(s);
        s.setDate((String) null);
        validator.validate();
        assertFindingsContain(Severity.ERROR, s, ProblemCode.ILLEGAL_VALUE, "date");

        s.setDate("");
        validator.validate();
        assertFindingsContain(Severity.ERROR, s, ProblemCode.ILLEGAL_VALUE, "date");

        s.setDate("              ");
        validator.validate();
        assertFindingsContain(Severity.ERROR, s, ProblemCode.ILLEGAL_VALUE, "date");

        s.setDate("Frying Pan");
        validator.validate();
        assertFindingsContain(Severity.ERROR, s, ProblemCode.ILLEGAL_VALUE, "date");

        s.setDate("01 JAN 1901");
        validator.validate();
        assertFindingsContain(Severity.ERROR, s, ProblemCode.ILLEGAL_VALUE, "date");

        s.setStatus(LdsSpouseSealingDateStatus.COMPLETED.getCode());
        validator.validate();
        assertNoIssues();
    }

    /**
     * Test when notes are messed up
     */
    @Test
    public void testNotes() {
        LdsSpouseSealing s = new LdsSpouseSealing();
        f.getLdsSpouseSealings(true).add(s);

        s.getNotes(true).clear();
        validator.validate();
        assertNoIssues();
    }

    /**
     * Test when list of sealings is null
     */
    @Test
    public void testNullList() {
        f.getLdsSpouseSealings(true).clear();
        validator.validate();
        assertNoIssues();
    }

    /**
     * Test when citations are messed up
     */
    @Test
    public void testPlace() {
        LdsSpouseSealing s = new LdsSpouseSealing();
        f.getLdsSpouseSealings(true).add(s);
        s.setPlace(new StringWithCustomTags((String) null));
        validator.validate();
        assertFindingsContain(Severity.ERROR, s, ProblemCode.MISSING_REQUIRED_VALUE, "place");

        s.setPlace("");
        validator.validate();
        assertFindingsContain(Severity.ERROR, s, ProblemCode.MISSING_REQUIRED_VALUE, "place");

        s.setPlace("              ");
        validator.validate();
        assertFindingsContain(Severity.ERROR, s, ProblemCode.MISSING_REQUIRED_VALUE, "place");

        s.setPlace("Frying Pan");
        validator.validate();
        assertNoIssues();
    }

    /**
     * Test when citations are messed up
     */
    @Test
    public void testStatus() {
        LdsSpouseSealing s = new LdsSpouseSealing();
        f.getLdsSpouseSealings(true).add(s);
        s.setStatus(new StringWithCustomTags((String) null));
        validator.validate();
        assertNoIssues();

        s.setStatus("");
        validator.validate();
        assertFindingsContain(Severity.ERROR, s, ProblemCode.ILLEGAL_VALUE, "status");

        s.setStatus("              ");
        validator.validate();
        assertFindingsContain(Severity.ERROR, s, ProblemCode.ILLEGAL_VALUE, "status");

        s.setStatus("Frying Pan");
        validator.validate();
        assertFindingsContain(Severity.ERROR, s, ProblemCode.ILLEGAL_VALUE, "status");

        s.setStatus(LdsSpouseSealingDateStatus.DNS_CAN.getCode());
        validator.validate();
        assertFindingsContain(Severity.ERROR, s, ProblemCode.MISSING_REQUIRED_VALUE, "date");

        s.setDate("1 JAN 1990");
        validator.validate();
        assertNoIssues();
    }

    /**
     * Test when citations are messed up
     */
    @Test
    public void testTemple() {
        LdsSpouseSealing s = new LdsSpouseSealing();
        f.getLdsSpouseSealings(true).add(s);
        s.setTemple(new StringWithCustomTags((String) null));
        validator.validate();
        assertFindingsContain(Severity.ERROR, s, ProblemCode.MISSING_REQUIRED_VALUE, "temple");

        s.setTemple("");
        validator.validate();
        assertFindingsContain(Severity.ERROR, s, ProblemCode.MISSING_REQUIRED_VALUE, "temple");

        s.setTemple("              ");
        validator.validate();
        assertFindingsContain(Severity.ERROR, s, ProblemCode.MISSING_REQUIRED_VALUE, "temple");

        s.setTemple("Frying Pan");
        validator.validate();
        assertNoIssues();
    }
}
