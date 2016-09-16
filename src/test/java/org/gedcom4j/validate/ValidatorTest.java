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
import static org.junit.Assert.assertSame;

import org.gedcom4j.model.Gedcom;
import org.gedcom4j.validate.Validator.Finding;
import org.junit.Test;

/**
 * Test for {@link Validator} class's basic methods, and not the actual validation itself. That's spread among a lot of other
 * classes in the package...but from the top of the object hierarchy, try {@link ValidatorGedcomStructureTest}.
 * 
 * @author frizbog
 */
public class ValidatorTest implements AutoRepairResponder {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 8324174994665255952L;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean mayRepair(Finding repairableValidationFinding) {
        return false;
    }

    /**
     * Test for constructor with a null argument, which is forbidden
     */
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullGedcom() {
        new Validator(null); // boom
    }

    /**
     * Test method for {@link Validator#getAutoRepairResponder()}.
     */
    @Test
    public void testGetAutoRepairResponder() {
        Validator v = new Validator(new Gedcom());
        assertNotNull(v);
        assertSame(Validator.AUTO_REPAIR_NONE, v.getAutoRepairResponder());
    }

    /**
     * Test method for {@link Validator#getGedcom()}.
     */
    @Test
    public void testGetGedcom() {
        Gedcom g = new Gedcom();
        Validator v = new Validator(g);
        assertNotNull(v);
        assertSame(g, v.getGedcom());
    }

    /**
     * Test method for {@link Validator#getResults()}.
     */
    @Test
    public void testGetResults() {
        Gedcom g = new Gedcom();
        Validator v = new Validator(g);
        assertNotNull(v.getResults().getAllFindings());
        assertEquals(0, v.getResults().getAllFindings().size());
        v.newFinding(g, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, null);
        assertEquals(1, v.getResults().getAllFindings().size());
    }

    /**
     * Test method for {@link Validator#setAutoRepairResponder(AutoRepairResponder)}.
     */
    @Test
    public void testSetAutoRepairResponder() {
        Gedcom g = new Gedcom();
        Validator v = new Validator(g);
        assertSame(Validator.AUTO_REPAIR_NONE, v.getAutoRepairResponder());
        v.setAutoRepairResponder(this);
        assertSame(this, v.getAutoRepairResponder());
    }

}
