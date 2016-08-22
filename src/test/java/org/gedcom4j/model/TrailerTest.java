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
 * @author frizbog1
 * 
 */
public class TrailerTest {

    /**
     * Test method for {@link org.gedcom4j.model.Trailer#equals(java.lang.Object)}.
     */
    @Test
    @SuppressWarnings("PMD.EqualsNull")
	public void testEqualsObject() {
        Trailer t1 = new Trailer();
        assertEquals(t1, t1);

        Trailer t2 = new Trailer();
        assertEquals("Hashcodes for trailers are always equal - they have no properties", t1, t2);

        assertFalse(t1.equals(null));
        assertFalse(t1.equals(this));
    }

    /**
     * Test method for {@link org.gedcom4j.model.Trailer#hashCode()}.
     */
    @Test
    public void testHashCode() {
        Trailer t1 = new Trailer();
        Trailer t2 = new Trailer();
        assertEquals("Hashcodes for trailers are always equal - they have no properties", t1.hashCode(), t2.hashCode());
    }

}
