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
package org.gedcom4j.model.enumerations;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test for {@link RestrictionNoticeType}
 * 
 * @author frizbog
 */
public class RestrictionNoticeTypeTest {

    /**
     * Test {@link RestrictionNoticeType#getDescription()}
     */
    @Test
    public void testDescription() {
        assertEquals("Marked as confidential by user", RestrictionNoticeType.CONFIDENTIAL.getDescription());
    }

    /**
     * Test {@link RestrictionNoticeType#getForCode(String)}
     */
    @Test
    public void testGetForCode() {
        assertEquals(RestrictionNoticeType.CONFIDENTIAL, RestrictionNoticeType.getForCode("confidential"));
        assertEquals(RestrictionNoticeType.LOCKED, RestrictionNoticeType.getForCode("locked"));
        assertEquals(RestrictionNoticeType.PRIVACY, RestrictionNoticeType.getForCode("privacy"));
        assertEquals(null, RestrictionNoticeType.getForCode("undefined"));
    }

    /**
     * Test {@link RestrictionNoticeType#toString()}
     */
    @Test
    public void testToString() {
        assertEquals("confidential", RestrictionNoticeType.CONFIDENTIAL.toString());
    }

}
