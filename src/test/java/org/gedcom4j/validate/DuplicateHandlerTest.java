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
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * Test for {@link DuplicateHandler}
 * 
 * @author frizbog
 *
 */
public class DuplicateHandlerTest {

    /** Test fixture */
    private List<String> list = null;

    /**
     * Set up the test fixtures
     */
    @Before
    public void setUp() {
        list = new ArrayList<>(Arrays.asList(new String[] { "A", "B", "B", "C", "D", "D", "D", "D", "E", "F", "F" }));
    }

    /**
     * Test only counting the duplicates
     */
    @Test
    public void testCountOnly() {
        DuplicateHandler<String> classUnderTest = new DuplicateHandler<>(list);
        assertEquals(5, classUnderTest.count());
        assertEquals("When only counting, repeated calls should five the same result", 5, classUnderTest.count());
    }

    /**
     * Test removing the duplicates
     */
    @Test
    public void testRemoving() {
        DuplicateHandler<String> classUnderTest = new DuplicateHandler<>(list);
        assertEquals(5, classUnderTest.remove());
        assertEquals(Arrays.asList(new String[] { "A", "B", "C", "D", "E", "F" }), list);
        assertEquals("After removing, repeated calls should return 0 duplicates counted/removed", 0, classUnderTest.remove());
    }

}
