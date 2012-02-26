/*
 * Copyright (c) 2009-2012 Matthew R. Harrah
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
public class GedcomTest {

    /**
     * Test method for
     * {@link com.mattharrah.gedcom4j.model.Gedcom#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject() {
        Gedcom g1 = new Gedcom();
        assertEquals(g1, g1);
        Gedcom g2 = new Gedcom();
        assertEquals("objects are equal, so equals() should return true", g1,
                g2);
        g1.trailer = null;
        assertFalse(
                "objects are no longer equal, so equals() should return false",
                g1.equals(g2));
        g2.trailer = null;
        assertEquals(
                "objects are equal again, so equals() should return true again",
                g1, g2);
        g1.individuals.put("FryingPan", new Individual());
        assertFalse(
                "objects are no longer equal, so equals() should return false",
                g1.equals(g2));
        g2.individuals.put("FryingPan", new Individual());
        assertEquals(
                "objects are equal again, so equals() should return true again",
                g1, g2);
        assertFalse(g1.equals(null));
        assertFalse(g1.equals(this));
    }

    /**
     * Test method for {@link com.mattharrah.gedcom4j.model.Gedcom#hashCode()}.
     */
    @Test
    public void testHashCode() {
        Gedcom g1 = new Gedcom();
        Gedcom g2 = new Gedcom();
        assertEquals("objects are equal, so hashcodes should equal",
                g1.hashCode(), g2.hashCode());
        g1.trailer = null;
        assertFalse(
                "objects are no longer equal, so hashcodes should no longer equal",
                g1.hashCode() == g2.hashCode());
        g2.trailer = null;
        assertEquals(
                "objects are equal again, so hashcodes should equal again",
                g1.hashCode(), g2.hashCode());
        g1.individuals.put("FryingPan", new Individual());
        assertFalse(
                "objects are no longer equal, so hashcodes should no longer equal",
                g1.hashCode() == g2.hashCode());
        g2.individuals.put("FryingPan", new Individual());
        assertEquals(
                "objects are equal again, so hashcodes should equal again",
                g1.hashCode(), g2.hashCode());
    }

}
