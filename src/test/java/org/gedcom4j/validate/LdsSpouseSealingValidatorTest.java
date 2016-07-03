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
 * Test for {@link LdsSpouseSealingValidator}
 * 
 * @author frizbog1
 */
public class LdsSpouseSealingValidatorTest extends AbstractValidatorTestCase {
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
        dad.xref = "@I00001@";
        gedcom.individuals.put(dad.xref, dad);

        mom = new Individual();
        mom.xref = "@I00002@";
        gedcom.individuals.put(mom.xref, mom);

        jr = new Individual();
        jr.xref = "@I00003@";
        gedcom.individuals.put(jr.xref, jr);

        f = new Family();
        f.xref = "@F0001@";
        f.husband = dad;
        f.wife = mom;
        f.children.add(jr);
        gedcom.families.put(f.xref, f);

        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test when citations are messed up
     */
    @Test
    public void testCitations() {
        LdsSpouseSealing s = new LdsSpouseSealing();
        f.ldsSpouseSealings.add(s);
        s.setCitations(null);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "citations", "null");

        s.setCitations(new ArrayList<AbstractCitation>());
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test when custom tags are messed up
     */
    @Test
    public void testCustomTags() {
        LdsSpouseSealing s = new LdsSpouseSealing();
        f.ldsSpouseSealings.add(s);
        s.setCustomTags(null);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "custom", "tag", "null");

        s.setCustomTags(new ArrayList<StringTree>());
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test when citations are messed up
     */
    @Test
    public void testDate() {
        LdsSpouseSealing s = new LdsSpouseSealing();
        f.ldsSpouseSealings.add(s);
        s.date = new StringWithCustomTags((String) null);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "date", "no value");

        s.date = new StringWithCustomTags("");
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "date", "no value");

        s.date = new StringWithCustomTags("              ");
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "date", "no value");

        s.date = new StringWithCustomTags("Frying Pan");
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test when notes are messed up
     */
    @Test
    public void testNotes() {
        LdsSpouseSealing s = new LdsSpouseSealing();
        f.ldsSpouseSealings.add(s);
        s.notes = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "notes", "null");

        s.notes = new ArrayList<Note>();
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test when list of sealings is null
     */
    @Test
    public void testNullList() {
        f.ldsSpouseSealings = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "lds", "spouse", "sealings", "null");
        f.ldsSpouseSealings = new ArrayList<LdsSpouseSealing>();
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test when citations are messed up
     */
    @Test
    public void testPlace() {
        LdsSpouseSealing s = new LdsSpouseSealing();
        f.ldsSpouseSealings.add(s);
        s.place = new StringWithCustomTags((String) null);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "place", "no value");

        s.place = new StringWithCustomTags("");
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "place", "no value");

        s.place = new StringWithCustomTags("              ");
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "place", "no value");

        s.place = new StringWithCustomTags("Frying Pan");
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test when citations are messed up
     */
    @Test
    public void testStatus() {
        LdsSpouseSealing s = new LdsSpouseSealing();
        f.ldsSpouseSealings.add(s);
        s.status = new StringWithCustomTags((String) null);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "status", "no value");

        s.status = new StringWithCustomTags("");
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "status", "no value");

        s.status = new StringWithCustomTags("              ");
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "status", "no value");

        s.status = new StringWithCustomTags("Frying Pan");
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test when citations are messed up
     */
    @Test
    public void testTemple() {
        LdsSpouseSealing s = new LdsSpouseSealing();
        f.ldsSpouseSealings.add(s);
        s.temple = new StringWithCustomTags((String) null);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "temple", "no value");

        s.temple = new StringWithCustomTags("");
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "temple", "no value");

        s.temple = new StringWithCustomTags("              ");
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "temple", "no value");

        s.temple = new StringWithCustomTags("Frying Pan");
        rootValidator.validate();
        assertNoIssues();
    }
}
