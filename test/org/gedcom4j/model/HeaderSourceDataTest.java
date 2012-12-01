/*
 * Copyright (c) 2009-2012 Matthew R. Harrah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.gedcom4j.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.gedcom4j.model.HeaderSourceData;
import org.junit.Test;

/**
 * @author frizbog1
 * 
 */
public class HeaderSourceDataTest {

    /**
     * Test method for
     * {@link org.gedcom4j.model.HeaderSourceData#equals(java.lang.Object)}
     * .
     */
    @Test
    public void testEqualsObject() {
        HeaderSourceData h1 = new HeaderSourceData();
        assertEquals(h1, h1);

        HeaderSourceData h2 = new HeaderSourceData();
        assertEquals(h1, h2);

        h1.copyright = "Frying Pan";
        assertFalse(h1.equals(h2));
        h2.copyright = "Frying Pan";
        assertEquals(h1, h2);
        h1.copyright = null;
        assertFalse(h1.equals(h2));
        h2.copyright = null;
        assertEquals(h1, h2);

        h1.name = "Frying Pan";
        assertFalse(h1.equals(h2));
        h2.name = "Frying Pan";
        assertEquals(h1, h2);
        h1.name = null;
        assertFalse(h1.equals(h2));
        h2.name = null;
        assertEquals(h1, h2);

        h1.publishDate = "Frying Pan";
        assertFalse(h1.equals(h2));
        h2.publishDate = "Frying Pan";
        assertEquals(h1, h2);
        h1.publishDate = null;
        assertFalse(h1.equals(h2));
        h2.publishDate = null;
        assertEquals(h1, h2);

        assertFalse(h1.equals(null));
        assertFalse(h1.equals(this));
    }

    /**
     * Test method for
     * {@link org.gedcom4j.model.HeaderSourceData#hashCode()}.
     */
    @Test
    public void testHashCode() {
        HeaderSourceData h1 = new HeaderSourceData();
        HeaderSourceData h2 = new HeaderSourceData();
        assertEquals(h1.hashCode(), h2.hashCode());

        h1.copyright = "Frying Pan";
        assertFalse(h1.hashCode() == h2.hashCode());
        h2.copyright = "Frying Pan";
        assertEquals(h1.hashCode(), h2.hashCode());
        h1.copyright = null;
        assertFalse(h1.hashCode() == h2.hashCode());
        h2.copyright = null;
        assertEquals(h1.hashCode(), h2.hashCode());

        h1.name = "Frying Pan";
        assertFalse(h1.hashCode() == h2.hashCode());
        h2.name = "Frying Pan";
        assertEquals(h1.hashCode(), h2.hashCode());
        h1.name = null;
        assertFalse(h1.hashCode() == h2.hashCode());
        h2.name = null;
        assertEquals(h1.hashCode(), h2.hashCode());

        h1.publishDate = "Frying Pan";
        assertFalse(h1.hashCode() == h2.hashCode());
        h2.publishDate = "Frying Pan";
        assertEquals(h1.hashCode(), h2.hashCode());
        h1.publishDate = null;
        assertFalse(h1.hashCode() == h2.hashCode());
        h2.publishDate = null;
        assertEquals(h1.hashCode(), h2.hashCode());
    }

}
