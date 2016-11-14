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
 * Test for {@link FamilyReference}
 * 
 * @author frizbog
 */
public class FamilyReferenceTest {

    /**
     * Test method for {@link FamilyReference#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject() {
        FamilyReference fr1 = new FamilyReference();
        assertEquals(fr1, fr1);
        assertFalse(fr1.equals(new Gedcom()));

        FamilyReference fr2 = new FamilyReference();
        assertEquals(fr1, fr2);

        fr1.setFamily(new Family());
        assertFalse(fr1.equals(new Gedcom()));
        fr2.setFamily(new Family());
        assertEquals(fr1, fr2);
    }

    /**
     * Test method for {@link FamilyReference#hashCode()}.
     */
    @Test
    public void testHashCode() {
        FamilyReference fr1 = new FamilyReference();
        FamilyReference fr2 = new FamilyReference();
        assertEquals(fr1.hashCode(), fr2.hashCode());

        fr1.setFamily(new Family());
        assertFalse(fr1.hashCode() == fr2.hashCode());
        fr2.setFamily(new Family());
        assertEquals(fr1.hashCode(), fr2.hashCode());

    }

    /**
     * Test method for {@link FamilyReference#toString()}.
     */
    @Test
    public void testToString() {
        FamilyReference fr1 = new FamilyReference();
        assertEquals("FamilyReference []", fr1.toString());

        fr1.setFamily(new Family());
        fr1.getCustomFacts(true).add(new CustomFact("_FOO"));
        assertEquals("FamilyReference [family=Family [], customFacts=[CustomFact [tag=_FOO, ]]]", fr1.toString());
    }

}
