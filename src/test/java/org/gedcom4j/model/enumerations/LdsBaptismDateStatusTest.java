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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Tests for {@link LdsBaptismDateStatus}
 * 
 * @author reckenrod
 */
public class LdsBaptismDateStatusTest {

    /**
     * Test for {@link LdsBaptismDateStatus#getCode()}
     */
    @Test
    public void testGetCode() {
        assertSame(LdsBaptismDateStatus.CHILD.getCode(), "CHILD");
        assertSame(LdsBaptismDateStatus.COMPLETED.getCode(), "COMPLETED");
        assertSame(LdsBaptismDateStatus.EXCLUDED.getCode(), "EXCLUDED");
        assertSame(LdsBaptismDateStatus.PRE_1970.getCode(), "PRE-1970");
        assertSame(LdsBaptismDateStatus.STILLBORN.getCode(), "STILLBORN");
        assertSame(LdsBaptismDateStatus.SUBMITTED.getCode(), "SUBMITTED");
        assertSame(LdsBaptismDateStatus.UNCLEARED.getCode(), "UNCLEARED");
    }

    /**
     * Test for {@link LdsBaptismDateStatus#getDescription()}
     */
    @Test
    public void testGetDescription() {
        assertSame(LdsBaptismDateStatus.CHILD.getDescription(), "Died before becoming eight years old, baptism not required.");
        assertSame(LdsBaptismDateStatus.COMPLETED.getDescription(), "Completed but the date is not known.");
        assertSame(LdsBaptismDateStatus.EXCLUDED.getDescription(),
                "Patron excluded this ordinance from being cleared in this submission.");
        assertSame(LdsBaptismDateStatus.PRE_1970.getDescription(),
                "Ordinance from temple records of work completed before 1970, assumed complete.");
        assertSame(LdsBaptismDateStatus.STILLBORN.getDescription(), "Stillborn, baptism not required.");
        assertSame(LdsBaptismDateStatus.SUBMITTED.getDescription(), "Ordinance was previously submitted.");
        assertSame(LdsBaptismDateStatus.UNCLEARED.getDescription(), "Data for clearing ordinance request was insufficient.");
    }

    /**
     * Test for {@link LdsBaptismDateStatus#getForCode(String)}
     */
    @Test
    public void testGetForCode() {
        assertSame(LdsBaptismDateStatus.CHILD, LdsBaptismDateStatus.getForCode("CHILD"));
        assertSame(LdsBaptismDateStatus.COMPLETED, LdsBaptismDateStatus.getForCode("COMPLETED"));
        assertSame(LdsBaptismDateStatus.EXCLUDED, LdsBaptismDateStatus.getForCode("EXCLUDED"));
        assertSame(LdsBaptismDateStatus.PRE_1970, LdsBaptismDateStatus.getForCode("PRE-1970"));
        assertSame(LdsBaptismDateStatus.STILLBORN, LdsBaptismDateStatus.getForCode("STILLBORN"));
        assertSame(LdsBaptismDateStatus.SUBMITTED, LdsBaptismDateStatus.getForCode("SUBMITTED"));
        assertSame(LdsBaptismDateStatus.UNCLEARED, LdsBaptismDateStatus.getForCode("UNCLEARED"));
        assertNull(LdsBaptismDateStatus.getForCode("BAD"));
        assertNull(LdsBaptismDateStatus.getForCode(null));
        assertNull(LdsBaptismDateStatus.getForCode(""));
    }

    /**
     * Test for {@link LdsBaptismDateStatus#toString()}
     */
    @Test
    public void testToString() {
        assertSame(LdsBaptismDateStatus.CHILD.getCode(), LdsBaptismDateStatus.CHILD.toString());
        assertSame(LdsBaptismDateStatus.COMPLETED.getCode(), LdsBaptismDateStatus.COMPLETED.toString());
        assertSame(LdsBaptismDateStatus.EXCLUDED.getCode(), LdsBaptismDateStatus.EXCLUDED.toString());
        assertSame(LdsBaptismDateStatus.PRE_1970.getCode(), LdsBaptismDateStatus.PRE_1970.toString());
        assertSame(LdsBaptismDateStatus.STILLBORN.getCode(), LdsBaptismDateStatus.STILLBORN.toString());
        assertSame(LdsBaptismDateStatus.SUBMITTED.getCode(), LdsBaptismDateStatus.SUBMITTED.toString());
        assertSame(LdsBaptismDateStatus.UNCLEARED.getCode(), LdsBaptismDateStatus.UNCLEARED.toString());
    }
}
