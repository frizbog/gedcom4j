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

import org.gedcom4j.model.Family;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.StringWithCustomTags;
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
     * The father in the family
     */
    private Individual dad;

    /**
     * The mother in the family
     */
    private Individual mom;

    /**
     * The child in the family
     */
    private Individual jr;

    /**
     * The family being validated
     */
    private Family f;

    /**
     * {@inheritDoc}
     */
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        gedcom = TestHelper.getMinimalGedcom();
        rootValidator.gedcom = gedcom;
        rootValidator.setAutorepairEnabled(false);

        dad = new Individual();
        dad.setXref("@I00001@");
        gedcom.getIndividuals().put(dad.getXref(), dad);

        mom = new Individual();
        mom.setXref("@I00002@");
        gedcom.getIndividuals().put(mom.getXref(), mom);

        jr = new Individual();
        jr.setXref("@I00003@");
        gedcom.getIndividuals().put(jr.getXref(), jr);

        f = new Family();
        f.setXref("@F0001@");
        f.setHusband(dad);
        f.setWife(mom);
        f.getChildren().add(jr);
        gedcom.getFamilies().put(f.getXref(), f);

        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test the automated record id
     */
    @Test
    public void testAutomatedRecordId() {
        f.setAutomatedRecordId(new StringWithCustomTags((String) null));
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "automated", "record", "id", "no value");

        f.setAutomatedRecordId(new StringWithCustomTags(""));
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "automated", "record", "id", "blank");
        assertFindingsContain(Severity.ERROR, "automated", "record", "id", "custom tags", "no value");

        f.setAutomatedRecordId(new StringWithCustomTags("     "));
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "automated", "record", "id", "blank");
        assertFindingsContain(Severity.ERROR, "automated", "record", "id", "custom tags", "no value");

        f.setAutomatedRecordId(new StringWithCustomTags("Frying Pan"));
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test when there are no citations
     */
    @Test
    public void testNoCitations() {
        f.getCitations(true).clear();
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test when there are no custom tags
     */
    @Test
    public void testNoCustomTags() {
        f.getCustomTags().clear();
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test when there is no husband in the family
     */
    @Test
    public void testNoDadInFamily() {
        f.setHusband(null);

        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test when there are no children in the family
     */
    @Test
    public void testNoKidsInFamily() {
        f.getChildren(true).clear();
        rootValidator.validate();
        assertNoIssues();

        f.getChildren().add(jr);
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test when there is no wife in the family
     */
    @Test
    public void testNoMomInFamily() {
        f.setWife(null);

        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test when there is no multimedia collection
     */
    @Test
    public void testNoMultimedia() {
        f.getMultimedia().clear();
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test when there are no people in the family at all
     */
    @Test
    public void testNoPeopleInFamily() {
        f.setHusband(null);
        f.setWife(null);
        f.getChildren().clear();

        rootValidator.validate();
        assertNoIssues();
    }

}
