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
        AbstractNameVariation nv1 = new PersonalNameVariation();
        AbstractNameVariation nv2 = new PersonalNameVariation();

        assertNotSame(nv1, nv2);
        assertEquals(nv1, nv2);

        nv1.getCustomFacts(true).add(new CustomFact("_FOO"));
        assertFalse(nv1.equals(nv2));
        nv1.getCustomFacts().clear();
        assertFalse(nv1.equals(nv2));
        nv2.getCustomFacts(true).clear();
        assertEquals(nv1, nv2);

        nv1.variation = "Yo";
        assertFalse(nv1.equals(nv2));
        nv1.variation = null;
        assertEquals(nv1, nv2);

        nv1.variationType = new StringWithCustomFacts("Yo");
        assertFalse(nv1.equals(nv2));
        nv1.variationType = null;
        assertEquals(nv1, nv2);

    }

    /**
     * Test for hashCode()
     */
    @Test
    public void testHashCode() {
        AbstractNameVariation nv1 = new PersonalNameVariation();
        AbstractNameVariation nv2 = new PersonalNameVariation();

        assertNotSame(nv1, nv2);
        assertEquals(nv1.hashCode(), nv2.hashCode());

        nv1.getCustomFacts(true).add(new CustomFact("_FOO"));
        assertFalse(nv1.hashCode() == nv2.hashCode());
        nv1.getCustomFacts().clear();
        assertFalse(nv1.hashCode() == nv2.hashCode());
        nv2.getCustomFacts(true).clear();
        assertEquals(nv1.hashCode(), nv2.hashCode());

        nv1.variation = "Yo";
        assertFalse(nv1.hashCode() == nv2.hashCode());
        nv1.variation = null;
        assertEquals(nv1.hashCode(), nv2.hashCode());

        nv1.variationType = new StringWithCustomFacts("Yo");
        assertFalse(nv1.hashCode() == nv2.hashCode());
        nv1.variationType = null;
        assertEquals(nv1.hashCode(), nv2.hashCode());

    }

    /**
     * Test for .toString()
     */
    @Test
    public void testToString() {
        AbstractNameVariation nv = new PersonalNameVariation();
        assertEquals("PersonalNameVariation []", nv.toString());
        nv.variation = "Frying Pan";
        nv.getCustomFacts(true).add(new CustomFact("_FOO"));
        nv.variationType = new StringWithCustomFacts("All");
        assertEquals("PersonalNameVariation [variation=Frying Pan, variationType=All, customTags=[CustomFact [tag=_FOO, ]]]", nv
                .toString());

    }
}
