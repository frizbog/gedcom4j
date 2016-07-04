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

import java.util.ArrayList;

import org.gedcom4j.model.*;
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
        rootValidator.autorepair = false;

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
        f.husband = dad;
        f.wife = mom;
        f.children.add(jr);
        gedcom.getFamilies().put(f.getXref(), f);

        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test the automated record id
     */
    @Test
    public void testAutomatedRecordId() {
        f.automatedRecordId = new StringWithCustomTags((String) null);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "automated", "record", "id", "no value");

        f.automatedRecordId = new StringWithCustomTags("");
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "automated", "record", "id", "blank");
        assertFindingsContain(Severity.ERROR, "automated", "record", "id", "custom tags", "no value");

        f.automatedRecordId = new StringWithCustomTags("     ");
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "automated", "record", "id", "blank");
        assertFindingsContain(Severity.ERROR, "automated", "record", "id", "custom tags", "no value");

        f.automatedRecordId = new StringWithCustomTags("Frying Pan");
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test when there are no citations
     */
    @Test
    public void testNoCitations() {
        f.citations = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "citations", "null");

        f.citations = new ArrayList<AbstractCitation>();
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
        f.husband = null;

        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test when there are no children in the family
     */
    @Test
    public void testNoKidsInFamily() {
        f.children = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "children", "null", "family");

        f.children = new ArrayList<Individual>();
        rootValidator.validate();
        assertNoIssues();

        f.children.add(jr);
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test when there is no wife in the family
     */
    @Test
    public void testNoMomInFamily() {
        f.wife = null;

        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test when there is no multimedia collection
     */
    @Test
    public void testNoMultimedia() {
        f.multimedia = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "multimedia", "null");

        f.multimedia = new ArrayList<Multimedia>();
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test when there are no people in the family at all
     */
    @Test
    public void testNoPeopleInFamily() {
        f.husband = null;
        f.wife = null;
        f.children.clear();

        rootValidator.validate();
        assertNoIssues();
    }

}
