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

import org.gedcom4j.model.Family;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Source;
import org.junit.Test;

/**
 * Test for the {@link AutoRepair} class
 * 
 * @author frizbog
 *
 */
public class AutoRepairTest {

    /**
     * Test constructor with mixed types
     */
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void mixedTypes() {
        new AutoRepair(new Gedcom(), new Family());
    }

    /**
     * Test for the {@link AutoRepair} class
     */
    @Test
    public void testBasic() {
        Source b = new Source();
        b.setXref("@S123@");
        b.setRecIdNumber("1");

        Source a = new Source(b);
        a.setRecIdNumber("2");

        AutoRepair ar = new AutoRepair(b, a);
        assertNotNull(ar);
        assertFalse(ar.getBefore().equals(ar.getAfter()));
    }

    /**
     * Test {@link AutoRepair#equals(Object)}, {@link AutoRepair#hashCode()}, and {@link AutoRepair#toString()}
     */
    @Test
    public void testEqualsHashcodeToString() {
        AutoRepair ar1 = new AutoRepair(new Gedcom(), new Gedcom());
        AutoRepair ar2 = new AutoRepair(new Gedcom(), new Gedcom());

        assertEquals(ar1, ar2);
        assertEquals(ar1.hashCode(), ar2.hashCode());
        assertEquals(ar1.toString(), ar2.toString());

        ar2 = new AutoRepair(new Family(), new Family());
        assertFalse(ar1.equals(ar2));
        assertFalse(ar1.hashCode() == ar2.hashCode());
        assertFalse(ar1.toString().equals(ar2.toString()));
    }
}
