/*
 * Copyright (c) 2009-2011 Matthew R. Harrah
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
package com.mattharrah.gedcom4j.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * @author frizbog1
 * 
 */
public class GedcomVersionTest extends GedcomVersion {

    /**
     * Test method for
     * {@link com.mattharrah.gedcom4j.model.GedcomVersion#equals(java.lang.Object)}
     * .
     */
    @Test
    public void testEqualsObject() {
        GedcomVersion gv1 = new GedcomVersion();
        GedcomVersion gv2 = new GedcomVersion();
        assertEquals("objects are equal, so equals() should return true", gv1,
                gv2);
        gv1.gedcomForm = "Frying Pan";
        assertFalse(
                "objects are not equal, so equals() should not return true",
                gv1.equals(gv2));
        gv2.gedcomForm = "Frying Pan";
        assertEquals("objects are equal again, so equals() should return true",
                gv1, gv2);
    }

    /**
     * Test method for
     * {@link com.mattharrah.gedcom4j.model.GedcomVersion#hashCode()}.
     */
    @Test
    public void testHashCode() {
        GedcomVersion gv1 = new GedcomVersion();
        GedcomVersion gv2 = new GedcomVersion();
        assertEquals("objects are equal, so hashcodes should equal",
                gv1.hashCode(), gv2.hashCode());
        gv1.gedcomForm = "Frying Pan";
        assertFalse("objects are not equal, so hashcodes should not equal",
                gv1.hashCode() == gv2.hashCode());
        gv2.gedcomForm = "Frying Pan";
        assertEquals("objects are equal again, so hashcodes should equal",
                gv1.hashCode(), gv2.hashCode());
    }
}
