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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Test for {@link DuplicateEliminator}
 * 
 * @author frizbog
 */
public class DuplicateEliminatorTest {

    /**
     * Test when the list is empty
     */
    @Test
    public void testEmptyList() {
        List<String> l = new ArrayList<>();
        int count = new DuplicateEliminator<>(l).process();
        assertEquals(0, count);
        assertNotNull(l);
        assertTrue(l.isEmpty());
    }

    /**
     * Test when the list has dups
     */
    @Test
    public void testListWithDups() {
        List<String> l = new ArrayList<>();
        l.add("Foo");
        l.add("Bar");
        l.add("Dup");
        l.add("Dup");
        l.add("Bat");
        assertEquals(5, l.size());
        int count = new DuplicateEliminator<>(l).process();
        assertEquals(1, count);
        assertEquals(4, l.size());
        assertEquals("Foo", l.get(0));
        assertEquals("Bar", l.get(1));
        assertEquals("Dup", l.get(2));
        assertEquals("Bat", l.get(3));

        // Add a dup to end of list and re-process. The first "Foo" should be retained.
        l.add("Foo");
        assertEquals(5, l.size());
        count = new DuplicateEliminator<>(l).process();
        assertEquals(1, count);
        assertEquals(4, l.size());
        assertEquals("Foo", l.get(0));
        assertEquals("Bar", l.get(1));
        assertEquals("Dup", l.get(2));
        assertEquals("Bat", l.get(3));

        // Add several dups to end of list, change one entry, and re-process. The order should be preserved as expected.
        l.add("Foo");
        l.add("Bar");
        l.add("Baz");
        l.add("Bat");
        l.set(2, "Baz");
        assertEquals(8, l.size());
        count = new DuplicateEliminator<>(l).process();
        assertEquals(4, count);
        assertEquals(4, l.size());
        assertEquals("Foo", l.get(0));
        assertEquals("Bar", l.get(1));
        assertEquals("Baz", l.get(2));
        assertEquals("Bat", l.get(3));

    }

    /**
     * Test when the list contains nulls. Nulls are considered duplicates of each other.
     */
    @Test
    public void testListWithNulls() {
        List<String> l = new ArrayList<>();
        l.add("Foo");
        l.add("Bar");
        l.add(null);
        l.add("Baz");
        l.add("Bat");
        assertEquals(5, l.size());
        int count = new DuplicateEliminator<>(l).process();
        assertEquals(0, count);
        assertEquals(5, l.size());
        assertEquals("Foo", l.get(0));
        assertEquals("Bar", l.get(1));
        assertNull(l.get(2));
        assertEquals("Baz", l.get(3));
        assertEquals("Bat", l.get(4));

        l.set(4, null);
        l.add(null);
        assertEquals(6, l.size());
        count = new DuplicateEliminator<>(l).process();
        assertEquals(2, count);
        assertEquals(4, l.size());
        assertEquals("Foo", l.get(0));
        assertEquals("Bar", l.get(1));
        assertNull(l.get(2));
        assertEquals("Baz", l.get(3));

    }

    /**
     * Test with no dups in the list
     */
    @Test
    public void testNoDupList() {
        List<String> l = new ArrayList<>();
        l.add("Foo");
        l.add("Bar");
        l.add("Baz");
        l.add("Bat");
        assertEquals(4, l.size());
        int count = new DuplicateEliminator<>(l).process();
        assertEquals(0, count);
        assertEquals(4, l.size());
        assertEquals("Foo", l.get(0));
        assertEquals("Bar", l.get(1));
        assertEquals("Baz", l.get(2));
        assertEquals("Bat", l.get(3));
    }

    /**
     * Test when the list is null
     */
    @Test
    public void testNullList() {
        List<String> l = null;
        int count = new DuplicateEliminator<>(l).process();
        assertEquals(0, count);
        assertNull(l);
    }

}
