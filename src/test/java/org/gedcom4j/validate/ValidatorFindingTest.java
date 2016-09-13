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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Header;
import org.gedcom4j.model.ModelElement;
import org.gedcom4j.validate.Validator.Finding;
import org.junit.Test;

/**
 * Test for {@link Validator.Finding}
 * 
 * @author frizbog
 */
@SuppressWarnings("PMD.TooManyMethods")
public class ValidatorFindingTest {

    /**
     * Test method for {@link Validator.Finding#addRepair(AutoRepair)}.
     */
    @Test
    public void testAddRepair() {
        Validator v = new Validator(new Gedcom());
        Finding f = v.new Finding();
        assertNotNull(f);
        assertNotNull(f.getRepairs(true));
        f.addRepair(new AutoRepair(new Header(), new Header()));
        assertEquals(1, f.getRepairs().size());
    }

    /**
     * Test method for {@link Validator.Finding#Finding()}.
     */
    @Test
    public void testFinding() {
        Validator v = new Validator(new Gedcom());
        Finding f = v.new Finding();
        assertNotNull(f);
    }

    /**
     * Test method for {@link Validator.Finding#getRepairs()}.
     */
    @Test
    public void testGetRepairs() {
        Validator v = new Validator(new Gedcom());
        Finding f = v.new Finding();
        assertNotNull(f);
        assertNull(f.getRepairs());
    }

    /**
     * Test method for {@link Validator.Finding#getRepairs(boolean)}.
     */
    @Test
    public void testGetRepairsBoolean() {
        Validator v = new Validator(new Gedcom());
        Finding f = v.new Finding();
        assertNotNull(f);
        assertNotNull(f.getRepairs(true));
    }

    /**
     * Test method for {@link Validator.Finding#setFieldNameOfConcern(String)}.
     */
    @Test
    public void testGetSetFieldNameOfConcern() {
        Validator v = new Validator(new Gedcom());
        Finding f = v.new Finding();
        assertNull(f.getFieldNameOfConcern());
        f.setFieldNameOfConcern("Foo");
        assertEquals("Foo", f.getFieldNameOfConcern());
    }

    /**
     * Test method for {@link Validator.Finding#setItemOfConcern(org.gedcom4j.model.ModelElement)}.
     */
    @Test
    public void testGetSetItemOfConcern() {
        Validator v = new Validator(new Gedcom());
        Finding f = v.new Finding();
        assertNull(f.getItemOfConcern());
        f.setItemOfConcern(new Header());
        assertEquals(new Header(), f.getItemOfConcern());
    }

    /**
     * Test method for {@link Validator.Finding#setProblemCode(int)}.
     */
    @Test
    public void testGetSetProblemCode() {
        Validator v = new Validator(new Gedcom());
        Finding f = v.new Finding();
        assertEquals(0, f.getProblemCode());
        f.setProblemCode(1000);
        assertEquals(1000, f.getProblemCode());
    }

    /**
     * Test method for {@link Validator.Finding#setProblemDescription(String)}.
     */
    @Test
    public void testGetSetProblemDescription() {
        Validator v = new Validator(new Gedcom());
        Finding f = v.new Finding();
        assertEquals(0, f.getProblemCode());
        f.setProblemCode(1000);
        assertEquals(1000, f.getProblemCode());
        assertNull(f.getProblemDescription());
        f.setProblemDescription("FRYING PAN");
        assertEquals("FRYING PAN", f.getProblemDescription());
    }

    /**
     * Test method for {@link Validator.Finding#setRelatedItems(java.util.List)}.
     */
    @Test
    public void testGetSetRelatedItems() {
        Validator v = new Validator(new Gedcom());
        Finding f = v.new Finding();
        assertNull(f.getRelatedItems());
        assertNotNull(f.getRelatedItems(true));
        f.setRelatedItems(null);
        assertNull(f.getRelatedItems());
        f.setRelatedItems(new ArrayList<ModelElement>());
        assertNotNull(f.getRelatedItems(true));
    }

    /**
     * Test method for {@link Validator.Finding#getSeverity()}.
     */
    @Test
    public void testGetSetSeverity() {
        Validator v = new Validator(new Gedcom());
        Finding f = v.new Finding();
        assertNull(f.getSeverity());
        f.setSeverity(Severity.INFO);
        assertEquals(Severity.INFO, f.getSeverity());
    }

    /**
     * Test method for {@link Validator.Finding#setProblem(ProblemCode)}.
     */
    @Test
    public void testSetProblem() {
        Validator v = new Validator(new Gedcom());
        Finding f = v.new Finding();
        assertEquals(0, f.getProblemCode());
        f.setProblem(ProblemCode.CROSS_REFERENCE_NOT_FOUND);
        assertEquals(ProblemCode.CROSS_REFERENCE_NOT_FOUND.getCode(), f.getProblemCode());
        assertEquals(ProblemCode.CROSS_REFERENCE_NOT_FOUND.getDescription(), f.getProblemDescription());
    }

    /**
     * Test method for {@link Validator.Finding#setRepairs(java.util.List)}.
     */
    @Test
    public void testSetRepairs() {
        Validator v = new Validator(new Gedcom());
        Finding f = v.new Finding();
        assertNull(f.getRepairs());
        f.setRepairs(new ArrayList<AutoRepair>());
        assertNotNull(f.getRepairs());
    }

    /**
     * Test method for {@link Validator.Finding#toString()}.
     */
    @Test
    public void testToString() {
        Validator v = new Validator(new Gedcom());
        Finding f = v.new Finding();
        f.setProblem(ProblemCode.CROSS_REFERENCE_NOT_FOUND);
        assertEquals("Finding [problemCode=0, problemDescription=Cross-referenced item could not be found in the GEDCOM, ]", f
                .toString());
    }

}
