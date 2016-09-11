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
import org.gedcom4j.model.ModelElement;
import org.junit.Test;

/**
 * Test for {@link ValidationFinding}
 * 
 * @author frizbog
 */
public class ValidationFindingTest {

    /**
     * Test method for {@link ValidationFinding#getFieldNameOfConcern()} and {@link ValidationFinding#setFieldNameOfConcern(String)}
     */
    @Test
    public void testGetSetFieldNameOfConcern() {
        ValidationFinding vf = new ValidationFinding();
        assertNull(vf.getFieldNameOfConcern());
        vf.setFieldNameOfConcern("Foo");
        assertEquals("Foo", vf.getFieldNameOfConcern());
    }

    /**
     * Test method for {@link ValidationFinding#getItemOfConcern()} and
     * {@link ValidationFinding#setItemOfConcern(org.gedcom4j.model.ModelElement)}.
     */
    @Test
    public void testGetSetItemOfConcern() {
        ValidationFinding vf = new ValidationFinding();
        assertNull(vf.getItemOfConcern());
        vf.setItemOfConcern(new Gedcom());
        assertEquals(Gedcom.class, vf.getItemOfConcern().getClass());
    }

    /**
     * Test method for {@link ValidationFinding#getProblemCode()} and {@link ValidationFinding#setProblemCode(int)}.
     */
    @Test
    public void testGetSetProblemCode() {
        ValidationFinding vf = new ValidationFinding();
        assertEquals(0, vf.getProblemCode());
        vf.setProblemCode(1000);
        assertEquals(1000, vf.getProblemCode());
    }

    /**
     * Test method for {@link ValidationFinding#getProblemDescription()} and
     * {@link ValidationFinding#setProblemDescription(java.lang.String)}.
     */
    @Test
    public void testGetSetProblemDescription() {
        ValidationFinding vf = new ValidationFinding();
        vf.setProblemCode(1000); // Have to set at 1000 or higher to be able to set the description
        assertNull(vf.getProblemDescription());
        vf.setProblemDescription("Frying Pan");
        assertEquals("Frying Pan", vf.getProblemDescription());
    }

    /**
     * Test method for {@link ValidationFinding#getRelatedItems()} and {@link ValidationFinding#setRelatedItems(java.util.List)}.
     */
    @Test
    public void testGetSetRelatedItems() {
        assertFalse(Options.isCollectionInitializationEnabled());
        ValidationFinding vf = new ValidationFinding();
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
     * Test method for {@link ValidationFinding#getRepairs()} and {@link ValidationFinding#setRepairs(java.util.List)}
     */
    @Test
    public void testGetSetRepairs() {
        assertFalse(Options.isCollectionInitializationEnabled());
        ValidationFinding vf = new ValidationFinding();
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
     * Test method for {@link ValidationFinding#setProblem(ProblemCode)}.
     */
    @Test
    public void testSetProblem() {
        ValidationFinding vf = new ValidationFinding();
        vf.setProblem(ProblemCode.ILLEGAL_VALUE);
        assertEquals(ProblemCode.ILLEGAL_VALUE.ordinal(), vf.getProblemCode());
        assertEquals(ProblemCode.ILLEGAL_VALUE.getDescription(), vf.getProblemDescription());
    }

    /**
     * Test method for {@link ValidationFinding#getProblemDescription()} and
     * {@link ValidationFinding#setProblemDescription(java.lang.String)} when the problem code is less than 1000, which is reserved
     * for gedcom4j use
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetProblemDescriptionCodeBelow1000() {
        ValidationFinding vf = new ValidationFinding();
        assertNull(vf.getProblemDescription());
        vf.setProblemDescription("Frying Pan"); // Boom, because code is less than 1000
    }

}
