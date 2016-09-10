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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Test copy constructor for {@link CitationWithoutSource}
 * 
 * @author frizbog
 */
public class CitationWithoutSourceCopyTest extends AbstractCopyTest {

    /**
     * Test copying a null {@link CitationWithoutSource}, which should never work
     */
    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testCopyNull() {
        new CitationWithoutSource(null);
    }

    /**
     * Test the simplest possible scenario - copy a new default {@link CitationWithoutSource}
     */
    @Test
    public void testSimplestPossible() {
        CitationWithoutSource orig = new CitationWithoutSource();
        CitationWithoutSource copy = new CitationWithoutSource(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
    }

    /**
     * Test with values
     */
    @Test
    public void testWithValues() {
        CitationWithoutSource orig = new CitationWithoutSource();
        orig.getCustomTags(true).add(getTestCustomTags());
        orig.getDescription(true).add("Description Line 1");
        orig.getDescription().add("Description Line 2");
        orig.getNotes(true).add(getTestNote());
        List<String> ls = new ArrayList<>();
        ls.add("Line 1");
        ls.add("Line 2");
        ls.add("Line 3");
        orig.getTextFromSource(true).add(ls);

        CitationWithoutSource copy = new CitationWithoutSource(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
        assertEquals(orig.toString(), copy.toString());

        orig.getTextFromSource().get(0).set(0, "XXXXXXX");
        assertFalse("Copy shouldn't match when original is changed", orig.equals(copy));
    }

}
