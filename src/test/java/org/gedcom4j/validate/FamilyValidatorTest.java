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
import org.gedcom4j.model.IndividualReference;
import org.gedcom4j.model.StringWithCustomFacts;
import org.gedcom4j.model.TestHelper;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link FamilyValidator}
 * 
 * @author frizbog1
 */
public class FamilyValidatorTest extends AbstractValidatorTestCase {

    /**
     * The child in the family
     */
    private Individual jr;

    /**
     * The family being validated
     */
    private Family f;

    /**
     * Set up the test
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

        jr = new Individual();
        jr.setXref("@I00003@");
        gedcom.getIndividuals().put(jr.getXref(), jr);

        f = new Family();
        f.setXref("@F0001@");
        f.setHusband(new IndividualReference(dad));
        f.setWife(new IndividualReference(mom));
        f.getChildren(true).add(new IndividualReference(jr));
        gedcom.getFamilies().put(f.getXref(), f);

        validator.validate();
        assertNoIssues();
    }

    /**
     * Test the automated record id
     */
    @Test
    public void testAutomatedRecordId() {
        f.setAutomatedRecordId(new StringWithCustomFacts((String) null));
        validator.validate();
        assertFindingsContain(Severity.ERROR, f, ProblemCode.MISSING_REQUIRED_VALUE, "automatedRecordId");

        f.setAutomatedRecordId("");
        validator.validate();
        assertFindingsContain(Severity.ERROR, f, ProblemCode.MISSING_REQUIRED_VALUE, "automatedRecordId");

        f.setAutomatedRecordId("     ");
        validator.validate();
        assertFindingsContain(Severity.ERROR, f, ProblemCode.MISSING_REQUIRED_VALUE, "automatedRecordId");

        f.setAutomatedRecordId("Frying Pan");
        validator.validate();
        assertNoIssues();
    }

    /**
     * Test when there are no citations
     */
    @Test
    public void testNoCitations() {
        f.getCitations(true).clear();
        validator.validate();
        assertNoIssues();
    }

    /**
     * Test when there are no custom facts
     */
    @Test
    public void testNoCustomFacts() {
        validator.validate();
        assertNoIssues();
        f.getCustomFacts(true).clear();
        validator.validate();
        assertNoIssues();
    }

    /**
     * Test when there is no husband in the family
     */
    @Test
    public void testNoDadInFamily() {
        f.setHusband(null);

        validator.validate();
        assertNoIssues();
    }

    /**
     * Test when there are no children in the family
     */
    @Test
    public void testNoKidsInFamily() {
        f.getChildren(true).clear();
        validator.validate();
        assertNoIssues();

        f.getChildren(true).add(new IndividualReference(jr));
        validator.validate();
        assertNoIssues();
    }

    /**
     * Test when there is no wife in the family
     */
    @Test
    public void testNoMomInFamily() {
        f.setWife(null);

        validator.validate();
        assertNoIssues();
    }

    /**
     * Test when there is no multimedia collection
     */
    @Test
    public void testNoMultimedia() {
        validator.validate();
        assertNoIssues();
        f.getMultimedia(true).clear();
        validator.validate();
        assertNoIssues();
    }

    /**
     * Test when there are no people in the family at all
     */
    @Test
    public void testNoPeopleInFamily() {
        f.setHusband(null);
        f.setWife(null);
        f.getChildren(true).clear();

        validator.validate();
        assertNoIssues();
    }

}
