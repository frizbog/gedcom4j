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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.gedcom4j.Options;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Header;
import org.gedcom4j.model.ModelElement;
import org.gedcom4j.validate.Validator.Finding;
import org.junit.Test;

/**
 * Test for {@link Finding}
 * 
 * @author frizbog
 */
@SuppressWarnings("PMD.TooManyMethods")
public class ValidationFindingTest {

    /**
     * Test method for {@link Finding#getFieldNameOfConcern()} and {@link Finding#setFieldNameOfConcern(String)}
     */
    @Test
    public void testGetSetFieldNameOfConcern() {
        Validator v = new Validator(new Gedcom());
        Finding vf = v.newFinding(new Header(), Severity.ERROR, ProblemCode.ILLEGAL_VALUE, null);
        assertNull(vf.getFieldNameOfConcern());
        vf.setFieldNameOfConcern("Foo");
        assertEquals("Foo", vf.getFieldNameOfConcern());
    }

    /**
     * Test method for {@link Finding#getItemOfConcern()} and {@link Finding#setItemOfConcern(org.gedcom4j.model.ModelElement)}.
     */
    @Test
    public void testGetSetItemOfConcern() {
        Validator v = new Validator(new Gedcom());
        Finding vf = v.newFinding(new Header(), Severity.ERROR, ProblemCode.ILLEGAL_VALUE, null);
        assertNotNull(vf.getItemOfConcern());
        vf.setItemOfConcern(new Gedcom());
        assertEquals(Gedcom.class, vf.getItemOfConcern().getClass());
    }

    /**
     * Test method for {@link Finding#getProblemCode()} and {@link Finding#setProblemCode(int)}.
     */
    @Test
    public void testGetSetProblemCode() {
        Validator v = new Validator(new Gedcom());
        Finding vf = v.newFinding(new Header(), Severity.ERROR, ProblemCode.ILLEGAL_VALUE, null);
        assertEquals(ProblemCode.ILLEGAL_VALUE.ordinal(), vf.getProblemCode());
        vf.setProblemCode(1000);
        assertEquals(1000, vf.getProblemCode());
    }

    /**
     * Test method for {@link Finding#getProblemDescription()} and {@link Finding#setProblemDescription(java.lang.String)}.
     */
    @Test
    public void testGetSetProblemDescription() {
        Validator v = new Validator(new Gedcom());
        Finding vf = v.newFinding(new Header(), Severity.ERROR, ProblemCode.ILLEGAL_VALUE, null);
        vf.setProblemCode(1000); // Have to set at 1000 or higher to be able to set the description
        assertNotNull(vf.getProblemDescription());
        vf.setProblemDescription("Frying Pan");
        assertEquals("Frying Pan", vf.getProblemDescription());
    }

    /**
     * Test method for {@link Finding#getRelatedItems()} and {@link Finding#setRelatedItems(java.util.List)}.
     */
    @Test
    public void testGetSetRelatedItems() {
        Validator v = new Validator(new Gedcom());
        Finding vf = v.newFinding(new Header(), Severity.ERROR, ProblemCode.ILLEGAL_VALUE, null);
        assertNull(vf.getRelatedItems());
        assertNull(vf.getRelatedItems(false));
        assertNotNull(vf.getRelatedItems(true));
        assertEquals(0, vf.getRelatedItems().size());
        vf.getRelatedItems().add(new Family());
        assertEquals(1, vf.getRelatedItems().size());
        vf.setRelatedItems(new ArrayList<ModelElement>());
        assertEquals(0, vf.getRelatedItems().size());
    }

    /**
     * Test method for {@link Finding#getRepairs()} and {@link Finding#setRepairs(java.util.List)}
     */
    @Test
    public void testGetSetRepairs() {
        assertFalse(Options.isCollectionInitializationEnabled());
        Validator v = new Validator(new Gedcom());
        Finding vf = v.newFinding(new Header(), Severity.ERROR, ProblemCode.ILLEGAL_VALUE, null);
        assertNull(vf.getRepairs());
        assertNull(vf.getRepairs(false));
        assertNotNull(vf.getRepairs(true));
        assertEquals(0, vf.getRepairs().size());
        vf.getRepairs().add(new AutoRepair(null, null));
        assertEquals(1, vf.getRepairs().size());
        vf.setRepairs(new ArrayList<AutoRepair>());
        assertEquals(0, vf.getRepairs().size());
    }

    /**
     * Test getting/setting severity.
     */
    @Test
    public void testGetSetSeverity() {
        Validator v = new Validator(new Gedcom());
        Finding vf = v.newFinding(new Header(), Severity.ERROR, ProblemCode.ILLEGAL_VALUE, null);
        assertNotNull(vf.getSeverity());
        vf.setSeverity(Severity.INFO);
        assertEquals(Severity.INFO, vf.getSeverity());
    }

    /**
     * Positive test
     */
    @Test
    public void testMultiArgConstructor() {
        Validator v = new Validator(new Gedcom());
        Finding vf = v.newFinding(new Gedcom(), Severity.INFO, ProblemCode.DUPLICATE_VALUE, null);
        assertNotNull(vf);
        assertEquals(Gedcom.class, vf.getItemOfConcern().getClass());
        assertEquals(Severity.INFO, vf.getSeverity());
        assertEquals(ProblemCode.DUPLICATE_VALUE.ordinal(), vf.getProblemCode());
        assertEquals(ProblemCode.DUPLICATE_VALUE.getDescription(), vf.getProblemDescription());
    }

    /**
     * Negative test 1
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiArgConstructorNegativeNullItemOfConcern() {
        Validator v = new Validator(new Gedcom());
        v.newFinding(null, Severity.INFO, ProblemCode.DUPLICATE_VALUE, null); // Item of concern is null
    }

    /**
     * Negative test 2
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiArgConstructorNegativeNullProblemCode() {
        Validator v = new Validator(new Gedcom());
        v.newFinding(new Gedcom(), Severity.INFO, null, null); // Problem code is null
    }

    /**
     * Negative test 3
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiArgConstructorNegativeNullSeverity() {
        Validator v = new Validator(new Gedcom());
        v.newFinding(new Gedcom(), null, ProblemCode.DUPLICATE_VALUE, null); // Severity is null
    }

    /**
     * Test method for {@link Finding#setProblem(ProblemCode)}.
     */
    @Test
    public void testSetProblem() {
        Validator v = new Validator(new Gedcom());
        Finding vf = v.newFinding(new Gedcom(), Severity.INFO, ProblemCode.DUPLICATE_VALUE, null);
        vf.setProblem(ProblemCode.ILLEGAL_VALUE);
        assertEquals(ProblemCode.ILLEGAL_VALUE.ordinal(), vf.getProblemCode());
        assertEquals(ProblemCode.ILLEGAL_VALUE.getDescription(), vf.getProblemDescription());
    }

    /**
     * Test method for {@link Finding#getProblemDescription()} and {@link Finding#setProblemDescription(java.lang.String)} when the
     * problem code is less than 1000, which is reserved for gedcom4j use
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetProblemDescriptionCodeBelow1000() {
        Validator v = new Validator(new Gedcom());
        Finding vf = v.newFinding(new Gedcom(), Severity.INFO, ProblemCode.DUPLICATE_VALUE, null);
        assertNotNull(vf.getProblemDescription());
        vf.setProblemDescription("Frying Pan"); // Boom, because code is less than 1000
    }

    /**
     * Test setting a severity to null, which is not allowed
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetSeverityNull() {
        Validator v = new Validator(new Gedcom());
        Finding vf = v.newFinding(new Gedcom(), Severity.INFO, ProblemCode.DUPLICATE_VALUE, null);
        vf.setSeverity(null); // Boom
    }

}
