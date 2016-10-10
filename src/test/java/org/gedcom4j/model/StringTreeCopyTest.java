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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Test copy constructor for {@link StringTree}
 * 
 * @author frizbog
 */
public class StringTreeCopyTest extends AbstractCopyTest {

    /**
     * Test copying a null {@link StringTree}, which should never work
     */
    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testCopyNull() {
        new StringTree(null);
    }

    /**
     * Test with multiple levels of data
     */
    @Test
    public void testFilledInMultiLevels() {
        StringTree orig = new StringTree();
        orig.setLevel(0);
        orig.setXref("A");
        orig.setLineNum(1);
        orig.setTag("B");
        orig.setValue("C");

        StringTree l1 = new StringTree();
        orig.getChildren(true).add(l1);
        l1.setLevel(1);
        l1.setLineNum(2);
        l1.setTag("D");
        l1.setParent(orig);
        l1.setValue("E");

        StringTree l2 = new StringTree();
        l1.getChildren(true).add(l2);
        l2.setLevel(2);
        l2.setLevel(3);
        l2.setTag("F");
        l2.setValue("G");
        l2.setParent(l1);

        StringTree copy = new StringTree(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
        assertEquals(orig.getChildren().get(0), copy.getChildren().get(0));
        assertNotSame(orig.getChildren().get(0).getChildren().get(0), copy.getChildren().get(0).getChildren().get(0));
        assertEquals(orig.getChildren().get(0), copy.getChildren().get(0));
        assertNotSame(orig.getChildren().get(0).getChildren().get(0), copy.getChildren().get(0).getChildren().get(0));

        assertEquals(orig.toString(), copy.toString());

        // Parent of root of copy is null, but children of copy should have parent references to copies
        assertNull(copy.getParent());
        assertEquals(copy, copy.getChildren().get(0).getParent());
        assertEquals(copy.getChildren().get(0), copy.getChildren().get(0).getChildren().get(0).getParent());
    }

    /**
     * Test with one level of data filled in
     */
    @Test
    public void testFilledInOneLevel() {
        StringTree orig = new StringTree();
        orig.setXref("Foo");
        orig.setLevel(3);
        orig.setLineNum(-50);
        orig.setParent(orig);
        orig.setTag("FRyingPan");
        orig.setValue("Cheese");

        StringTree copy = new StringTree(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);

        assertEquals(orig.toString(), copy.toString());

        /* Note that parents aren't copied, and don't factor into equals() */
        assertNull(copy.getParent());
        assertNotNull(orig.getParent());

    }

    /**
     * Test the simplest possible scenario - copy a new default {@link StringTree}
     */
    @Test
    public void testSimplestPossible() {
        StringTree orig = new StringTree();
        StringTree copy = new StringTree(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
    }

}
