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

import org.junit.Test;

/**
 * Test for {@link LdsSpouseSealing}
 * 
 * @author frizbog
 * 
 */
public class LdsSpouseSealingTest {

    /**
     * Test for {@link LdsSpouseSealing#equals(Object)}
     */
    @Test
    public void testEqualsObject() {
        LdsSpouseSealing l1 = new LdsSpouseSealing();
        LdsSpouseSealing l2 = new LdsSpouseSealing();
        assertNotSame(l1, l2);
        assertEquals(l1, l2);

        l1.getCitations(true).add(new CitationWithoutSource());
        assertFalse(l1.equals(l2));
        l2.getCitations(true).add(new CitationWithoutSource());
        assertEquals(l1, l2);

        l1.getCustomTags(true).add(new StringTree());
        assertFalse(l1.equals(l2));
        l2.getCustomTags(true).add(new StringTree());
        assertEquals(l1, l2);

        l1.date = new StringWithCustomTags("Frying Pan");
        assertFalse(l1.equals(l2));
        l2.date = new StringWithCustomTags("Frying Pan");
        assertEquals(l1, l2);

        l1.place = new StringWithCustomTags("Howdy");
        assertFalse(l1.equals(l2));
        l2.place = new StringWithCustomTags("Howdy");
        assertEquals(l1, l2);

        l1.status = new StringWithCustomTags("Test");
        assertFalse(l1.equals(l2));
        l2.status = new StringWithCustomTags("Test");
        assertEquals(l1, l2);

    }

    /**
     * Test for {@link LdsSpouseSealing#hashCode()}
     */
    @Test
    public void testHashCode() {
        LdsSpouseSealing l1 = new LdsSpouseSealing();
        LdsSpouseSealing l2 = new LdsSpouseSealing();
        assertNotSame(l1, l2);
        assertEquals(l1.hashCode(), l2.hashCode());

        l1.getCitations(true).add(new CitationWithoutSource());
        assertFalse(l1.hashCode() == l2.hashCode());
        l2.getCitations(true).add(new CitationWithoutSource());
        assertEquals(l1.hashCode(), l2.hashCode());

        l1.getCustomTags(true).add(new StringTree());
        assertFalse(l1.hashCode() == l2.hashCode());
        l2.getCustomTags(true).add(new StringTree());
        assertEquals(l1.hashCode(), l2.hashCode());

        l1.date = new StringWithCustomTags("Frying Pan");
        assertFalse(l1.hashCode() == l2.hashCode());
        l2.date = new StringWithCustomTags("Frying Pan");
        assertEquals(l1.hashCode(), l2.hashCode());

        l1.place = new StringWithCustomTags("Howdy");
        assertFalse(l1.hashCode() == l2.hashCode());
        l2.place = new StringWithCustomTags("Howdy");
        assertEquals(l1.hashCode(), l2.hashCode());

        l1.status = new StringWithCustomTags("Test");
        assertFalse(l1.hashCode() == l2.hashCode());
        l2.status = new StringWithCustomTags("Test");
        assertEquals(l1.hashCode(), l2.hashCode());
    }

    /**
     * Test for {@link LdsSpouseSealing#toString()}
     */
    @Test
    public void testToString() {
        LdsSpouseSealing l = new LdsSpouseSealing();
        assertEquals("LdsSpouseSealing []", l.toString());

        l.getCitations(true).add(new CitationWithoutSource());
        l.getCustomTags(true).add(new StringTree());
        l.date = new StringWithCustomTags("Frying Pan");
        l.place = new StringWithCustomTags("Howdy");
        l.status = new StringWithCustomTags("Test");

        assertEquals("LdsSpouseSealing [citations=[CitationWithoutSource []], date=Frying Pan, place=Howdy, "
                + "status=Test, customTags=[Line 0: 0 (null tag) (null value)]]", l.toString());
    }

}
