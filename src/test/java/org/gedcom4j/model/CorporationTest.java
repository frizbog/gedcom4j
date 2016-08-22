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
package org.gedcom4j.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * @author frizbog1
 * 
 */
public class CorporationTest {

    /**
     * Test method for {@link org.gedcom4j.model.Corporation#equals(java.lang.Object)} .
     */
    @Test
    @SuppressWarnings("PMD.EqualsNull")
    public void testEqualsObject() {
        Corporation c1 = new Corporation();
        assertEquals(c1, c1);
        Corporation c2 = new Corporation();
        assertEquals("CorporationTests are equal, so equals() should return true", c1, c2);
        c1.setBusinessName("Frying Pan");
        assertFalse("CorporationTests are no longer equal, so hashcodes should no longer equal", c1.equals(c2));
        c2.setBusinessName("Frying Pan");
        assertEquals("CorporationTests are equal again, so equals() should return true again", c1.hashCode(), c2.hashCode());
        assertFalse(c1.equals(null));
        assertFalse(c1.equals(this));
    }

    /**
     * Test method for {@link org.gedcom4j.model.Corporation#hashCode()}.
     */
    @Test
    public void testHashCode() {
        Corporation c1 = new Corporation();
        Corporation c2 = new Corporation();
        assertEquals("Objects are equal, so equals() should return true", c1.hashCode(), c2.hashCode());
        c1.setBusinessName("Frying Pan");
        assertFalse("Objects are no longer equal, so equals() should return false", c1.hashCode() == c2.hashCode());
        c2.setBusinessName("Frying Pan");
        assertEquals("Objects are equal again, so equals() should return true again", c1.hashCode(), c2.hashCode());
    }

}
