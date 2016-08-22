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

import org.junit.Test;

import junit.framework.TestCase;

/**
 * Tests for {@link FamilyEventType}
 * 
 * @author frizbog1
 */
public class FamilyEventTypeTest extends TestCase {

    /**
     * Test for {@link FamilyEventType#getFromTag(String)}
     */
	@Test
    public void testGetFromTag() {
        assertSame(FamilyEventType.ANNULMENT, FamilyEventType.getFromTag("ANUL"));
        assertSame(FamilyEventType.CENSUS, FamilyEventType.getFromTag("CENS"));
        assertSame(FamilyEventType.DIVORCE, FamilyEventType.getFromTag("DIV"));
        assertSame(FamilyEventType.DIVORCE_FILED, FamilyEventType.getFromTag("DIVF"));
        assertSame(FamilyEventType.ENGAGEMENT, FamilyEventType.getFromTag("ENGA"));
        assertSame(FamilyEventType.MARRIAGE, FamilyEventType.getFromTag("MARR"));
        assertSame(FamilyEventType.MARRIAGE_BANNER, FamilyEventType.getFromTag("MARB"));
        assertSame(FamilyEventType.MARRIAGE_CONTRACT, FamilyEventType.getFromTag("MARC"));
        assertSame(FamilyEventType.MARRIAGE_LICENSE, FamilyEventType.getFromTag("MARL"));
        assertSame(FamilyEventType.MARRIAGE_SETTLEMENT, FamilyEventType.getFromTag("MARS"));
        assertSame(FamilyEventType.EVENT, FamilyEventType.getFromTag("EVEN"));
        assertNull(FamilyEventType.getFromTag("BAD"));
        assertNull(FamilyEventType.getFromTag(null));
        assertNull(FamilyEventType.getFromTag(""));
    }

    /**
     * Test for {@link FamilyEventType#isValidTag(String)}
     */
	@Test
    public void testIsValidTag() {
        assertTrue(FamilyEventType.isValidTag("ANUL"));
        assertTrue(FamilyEventType.isValidTag("CENS"));
        assertTrue(FamilyEventType.isValidTag("DIV"));
        assertTrue(FamilyEventType.isValidTag("ENGA"));
        assertTrue(FamilyEventType.isValidTag("MARR"));
        assertTrue(FamilyEventType.isValidTag("MARB"));
        assertTrue(FamilyEventType.isValidTag("MARC"));
        assertTrue(FamilyEventType.isValidTag("MARL"));
        assertTrue(FamilyEventType.isValidTag("MARS"));
        assertTrue(FamilyEventType.isValidTag("EVEN"));
        assertFalse(FamilyEventType.isValidTag("BAD"));
        assertFalse(FamilyEventType.isValidTag(null));
        assertFalse(FamilyEventType.isValidTag(""));
    }
}
