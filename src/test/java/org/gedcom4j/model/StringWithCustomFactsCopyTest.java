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
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

/**
 * Test copy constructor for {@link StringWithCustomFacts}
 * 
 * @author frizbog
 */
public class StringWithCustomFactsCopyTest extends AbstractCopyTest {

    /**
     * Test copying a null {@link StringWithCustomFacts}, which should never work
     */
    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testCopyNull() {
        new StringWithCustomFacts((StringWithCustomFacts) null);
    }

    /**
     * Test the simplest possible scenario - copy a new default {@link StringWithCustomFacts}
     */
    @Test
    public void testSimplestPossible() {
        StringWithCustomFacts orig = new StringWithCustomFacts();
        StringWithCustomFacts copy = new StringWithCustomFacts(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
    }

    /**
     * Test with values and custom facts in play
     */
    @Test
    public void testWithCustomFacts() {
        StringWithCustomFacts orig = new StringWithCustomFacts("FryingPan");
        CustomFact cf = getTestCustomFact();
        cf.setDescription("UNCHANGED");
        orig.getCustomFacts(true).add(cf);

        StringWithCustomFacts copy = new StringWithCustomFacts(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);

        assertEquals("FryingPan", copy.getValue());
        orig.setValue("Not a Frying Pan anymore");
        assertEquals("FryingPan", copy.getValue());

        assertEquals(orig.getCustomFacts().get(0), copy.getCustomFacts().get(0));
        assertEquals("_HOWDY", copy.getCustomFacts().get(0).getTag());
        cf.setDescription("CHANGED");
        assertEquals("Copy should not change when original does", "UNCHANGED", copy.getCustomFacts().get(0).getDescription()
                .getValue());

    }

    /**
     * Test with values in play
     */
    @Test
    public void testWithValues() {
        StringWithCustomFacts orig = new StringWithCustomFacts("FryingPan");
        StringWithCustomFacts copy = new StringWithCustomFacts(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
        assertEquals("FryingPan", copy.getValue());
        orig.setValue("Not a Frying Pan anymore");
        assertEquals("FryingPan", copy.getValue());
    }

}
