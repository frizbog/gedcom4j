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
package org.gedcom4j.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test for {@link Tag}
 * 
 * @author frizbog
 */
public class TagTest {

    /**
     * The expected number of items in the {@link Tag} enum
     */
    private static final int EXPECTED_TAG_COUNT = 93;

    /**
     * Test for {@link Tag#equals(Object)}
     */
    @Test
    public void testEquals() {
        assertTrue(Tag.ABBREVIATION.equalsText("ABBR"));
        assertTrue(Tag.SUBMISSION.equalsText("SUBN"));
        assertTrue(Tag.TRAILER.equalsText("TRLR"));
        assertFalse(Tag.BLOB.equalsText("FRYINGPAN"));
        assertFalse(Tag.HEADER.equalsText(null));
    }

    /**
     * Test of tag count
     */
    @Test
    public void testTagCount() {
        assertEquals(EXPECTED_TAG_COUNT, Tag.values().length);
    }

    /**
     * Test of inner value and {@link Tag#toString()}
     */
    @Test
    public void testTagStrings() {
        for (Tag t : Tag.values()) {
            assertNotNull(t);
            assertNotNull(t.tagText);
            assertEquals(t.tagText, t.toString());
        }
    }

    /**
     * Test for {@link Tag#toString()}
     */
    @Test
    public void testToString() {
        assertEquals("FAMS", Tag.FAMILY_WHERE_SPOUSE.toString());
        assertEquals("INDI", Tag.INDIVIDUAL.toString());
    }

    /**
     * Negative test for {@link Tag#valueOf(String)}
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValueOfNegative() {
        Tag.valueOf("I_AM_NOT_DEFINED");
    }

    /**
     * Positive test for {@link Tag#valueOf(String)}
     */
    @Test
    public void testValueOfPositive() {
        assertEquals(Tag.ADDRESS, Tag.valueOf("ADDRESS"));
        assertEquals(Tag.OBJECT_MULTIMEDIA, Tag.valueOf("OBJECT_MULTIMEDIA"));
    }
}
