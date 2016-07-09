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
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

/**
 * Test cases for NameVariations
 * 
 * @author frizbog1
 * 
 */
public class NameVariationTest {

    /**
     * Test for .equals()
     */
    @Test
    public void testEquals() {
        NameVariation nv1 = new NameVariation();
        NameVariation nv2 = new NameVariation();

        assertNotSame(nv1, nv2);
        assertEquals(nv1, nv2);

        nv1.getCustomTags(true).add(new StringTree());
        assertFalse(nv1.equals(nv2));
        nv1.getCustomTags().clear();
        assertFalse(nv1.equals(nv2));
        nv2.getCustomTags(true).clear();
        assertEquals(nv1, nv2);

        nv1.variation = "Yo";
        assertFalse(nv1.equals(nv2));
        nv1.variation = null;
        assertEquals(nv1, nv2);

        nv1.variationType = new StringWithCustomTags("Yo");
        assertFalse(nv1.equals(nv2));
        nv1.variationType = null;
        assertEquals(nv1, nv2);

    }

    /**
     * Test for hashCode()
     */
    @Test
    public void testHashCode() {
        NameVariation nv1 = new NameVariation();
        NameVariation nv2 = new NameVariation();

        assertNotSame(nv1, nv2);
        assertEquals(nv1.hashCode(), nv2.hashCode());

        nv1.getCustomTags(true).add(new StringTree());
        assertFalse(nv1.hashCode() == nv2.hashCode());
        nv1.getCustomTags().clear();
        assertFalse(nv1.hashCode() == nv2.hashCode());
        nv2.getCustomTags(true).clear();
        assertEquals(nv1.hashCode(), nv2.hashCode());

        nv1.variation = "Yo";
        assertFalse(nv1.hashCode() == nv2.hashCode());
        nv1.variation = null;
        assertEquals(nv1.hashCode(), nv2.hashCode());

        nv1.variationType = new StringWithCustomTags("Yo");
        assertFalse(nv1.hashCode() == nv2.hashCode());
        nv1.variationType = null;
        assertEquals(nv1.hashCode(), nv2.hashCode());

    }

    /**
     * Test for .toString()
     */
    @Test
    public void testToString() {
        NameVariation nv = new NameVariation();
        assertEquals("NameVariation []", nv.toString());
        nv.variation = "Frying Pan";
        nv.getCustomTags(true).add(new StringTree());
        nv.variationType = new StringWithCustomTags("All");
        assertEquals("NameVariation [variation=Frying Pan, variationType=All, customTags=[Line 0: 0 (null tag) (null value)]]", nv.toString());

    }
}
