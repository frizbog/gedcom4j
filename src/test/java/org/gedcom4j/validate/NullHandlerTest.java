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

import java.util.ArrayList;

import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * Test for {@link NullHandler}
 * 
 * @author frizbog
 *
 */
public class NullHandlerTest {

    /**
     * Test only counting the nulls
     */
    @Test
    public void testCountOnly() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(new String[] { "A", "B", "B", "C", null, null, "D", "D", "D", "D",
                "E", null, "F", "F" }));
        NullHandler<String> classUnderTest = new NullHandler<>(list);
        assertEquals(3, classUnderTest.count());
        assertEquals(Arrays.asList(new String[] { "A", "B", "B", "C", null, null, "D", "D", "D", "D", "E", null, "F", "F" }), list);
        assertEquals("When only counting, repeated calls should five the same result", 3, classUnderTest.count());
    }

    /**
     * Test only counting the nulls
     */
    @Test
    public void testCountOnlyNoNulls() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(new String[] { "A", "B", "B", "C", "D", "D", "D", "D", "E", "F",
                "F" }));
        NullHandler<String> classUnderTest = new NullHandler<>(list);
        assertEquals(0, classUnderTest.count());
        assertEquals(Arrays.asList(new String[] { "A", "B", "B", "C", "D", "D", "D", "D", "E", "F", "F" }), list);
        assertEquals("When only counting, repeated calls should five the same result", 0, classUnderTest.count());
    }

    /**
     * Test removing the nulls
     */
    @Test
    public void testRemoving() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(new String[] { "A", "B", "B", "C", null, null, "D", "D", "D", "D",
                "E", null, "F", "F" }));
        NullHandler<String> classUnderTest = new NullHandler<>(list);
        assertEquals(3, classUnderTest.remove());
        assertEquals(Arrays.asList(new String[] { "A", "B", "B", "C", "D", "D", "D", "D", "E", "F", "F" }), list);
        assertEquals("After removing, repeated calls should return 0 nulls counted/removed", 0, classUnderTest.remove());
    }

    /**
     * Test removing the nulls
     */
    @Test
    public void testRemovingNoNulls() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(new String[] { "A", "B", "B", "C", "D", "D", "D", "D", "E", "F",
                "F" }));
        NullHandler<String> classUnderTest = new NullHandler<>(list);
        assertEquals(0, classUnderTest.remove());
        assertEquals(Arrays.asList(new String[] { "A", "B", "B", "C", "D", "D", "D", "D", "E", "F", "F" }), list);
        assertEquals("After removing, repeated calls should return 0 nulls counted/removed", 0, classUnderTest.remove());
    }

}
