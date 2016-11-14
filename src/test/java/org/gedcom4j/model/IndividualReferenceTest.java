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
 * @author frizbog
 *
 */
public class IndividualReferenceTest {

    /**
     * Test method for {@link org.gedcom4j.model.IndividualReference#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject() {
        IndividualReference fr1 = new IndividualReference();
        assertEquals(fr1, fr1);
        assertFalse(fr1.equals(new Gedcom()));

        IndividualReference fr2 = new IndividualReference();
        assertEquals(fr1, fr2);

        fr1.setIndividual(new Individual());
        assertFalse(fr1.equals(new Gedcom()));
        fr2.setIndividual(new Individual());
        assertEquals(fr1, fr2);
    }

    /**
     * Test method for {@link org.gedcom4j.model.IndividualReference#hashCode()}.
     */
    @Test
    public void testHashCode() {
        IndividualReference fr1 = new IndividualReference();
        IndividualReference fr2 = new IndividualReference();
        assertEquals(fr1.hashCode(), fr2.hashCode());

        fr1.setIndividual(new Individual());
        assertFalse(fr1.hashCode() == fr2.hashCode());
        fr2.setIndividual(new Individual());
        assertEquals(fr1.hashCode(), fr2.hashCode());

    }

    /**
     * Test method for {@link org.gedcom4j.model.IndividualReference#toString()}.
     */
    @Test
    public void testToString() {
        IndividualReference fr1 = new IndividualReference();
        assertEquals("IndividualReference []", fr1.toString());

        fr1.setIndividual(new Individual());
        fr1.getCustomFacts(true).add(new CustomFact("_FOO"));
        assertEquals("IndividualReference [individual=Unknown name, customFacts=[CustomFact [tag=_FOO, ]]]", fr1.toString());
    }

}
