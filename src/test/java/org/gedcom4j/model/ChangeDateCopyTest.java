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
 * Test copy constructor for {@link ChangeDate}
 * 
 * @author frizbog
 */
public class ChangeDateCopyTest extends AbstractCopyTest {

    /**
     * Test copying a null {@link ChangeDate}, which should never work
     */
    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testCopyNull() {
        new ChangeDate(null);
    }

    /**
     * Test the simplest possible scenario - copy a new default {@link ChangeDate}
     */
    @Test
    public void testSimplestPossible() {
        ChangeDate orig = new ChangeDate();
        ChangeDate copy = new ChangeDate(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
    }

    /**
     * Test with some values
     */
    @Test
    public void testValues() {
        ChangeDate orig = new ChangeDate();
        orig.setDate("5 MAY 1905");
        orig.setTime("12:00:00 Eastern");
        NoteStructure n = new NoteStructure();
        n.getLines(true).add("Frying Pan");
        n.getLines().add("Tomato sauce");
        orig.getNoteStructures(true).add(n);

        ChangeDate copy = new ChangeDate(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);

        assertEquals(1, copy.getNoteStructures().size());
        assertEquals(2, copy.getNoteStructures().get(0).getLines().size());

        assertEquals(orig.toString(), copy.toString());

        orig.getDate().setValue("31 DEC 2001");
        assertEquals("Copy should not change when original does", "5 MAY 1905", copy.getDate().getValue());

        orig.getNoteStructures().get(0).getLines().add(0, "Inserted line in original");
        assertEquals("Copy should not change when original does", "Frying Pan", copy.getNoteStructures().get(0).getLines().get(0));
    }

}
