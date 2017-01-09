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
 * Tests for {@link LdsEndowmentDateStatus}
 * 
 * @author reckenrod
 */
public class LdsEndowmentDateStatusTest {

    /**
     * Test for {@link LdsEndowmentDateStatus#getCode()}
     */
    @Test
    public void testGetCode() {
        assertSame(LdsEndowmentDateStatus.CHILD.getCode(), "CHILD");
        assertSame(LdsEndowmentDateStatus.COMPLETED.getCode(), "COMPLETED");
        assertSame(LdsEndowmentDateStatus.EXCLUDED.getCode(), "EXCLUDED");
        assertSame(LdsEndowmentDateStatus.PRE_1970.getCode(), "PRE-1970");
        assertSame(LdsEndowmentDateStatus.STILLBORN.getCode(), "STILLBORN");
        assertSame(LdsEndowmentDateStatus.SUBMITTED.getCode(), "SUBMITTED");
        assertSame(LdsEndowmentDateStatus.UNCLEARED.getCode(), "UNCLEARED");
    }

    /**
     * Test for {@link LdsEndowmentDateStatus#getDescription()}
     */
    @Test
    public void testGetDescription() {
        assertSame(LdsEndowmentDateStatus.CHILD.getDescription(), "Died before becoming eight years old.");
        assertSame(LdsEndowmentDateStatus.COMPLETED.getDescription(), "Completed but the date is not known.");
        assertSame(LdsEndowmentDateStatus.EXCLUDED.getDescription(),
                "Patron excluded this ordinance from being cleared in this submission.");
        assertSame(LdsEndowmentDateStatus.PRE_1970.getDescription(),
                "Ordinance from temple records of work completed before 1970, assumed complete.");
        assertSame(LdsEndowmentDateStatus.STILLBORN.getDescription(), "Stillborn, baptism not required.");
        assertSame(LdsEndowmentDateStatus.SUBMITTED.getDescription(), "Ordinance was previously submitted.");
        assertSame(LdsEndowmentDateStatus.UNCLEARED.getDescription(), "Data for clearing ordinance request was insufficient.");
    }

    /**
     * Test for {@link LdsEndowmentDateStatus#getForCode(String)}
     */
    @Test
    public void testGetForCode() {
        assertSame(LdsEndowmentDateStatus.CHILD, LdsEndowmentDateStatus.getForCode("CHILD"));
        assertSame(LdsEndowmentDateStatus.COMPLETED, LdsEndowmentDateStatus.getForCode("COMPLETED"));
        assertSame(LdsEndowmentDateStatus.EXCLUDED, LdsEndowmentDateStatus.getForCode("EXCLUDED"));
        assertSame(LdsEndowmentDateStatus.PRE_1970, LdsEndowmentDateStatus.getForCode("PRE-1970"));
        assertSame(LdsEndowmentDateStatus.STILLBORN, LdsEndowmentDateStatus.getForCode("STILLBORN"));
        assertSame(LdsEndowmentDateStatus.SUBMITTED, LdsEndowmentDateStatus.getForCode("SUBMITTED"));
        assertSame(LdsEndowmentDateStatus.UNCLEARED, LdsEndowmentDateStatus.getForCode("UNCLEARED"));
        assertNull(LdsEndowmentDateStatus.getForCode("BAD"));
        assertNull(LdsEndowmentDateStatus.getForCode(null));
        assertNull(LdsEndowmentDateStatus.getForCode(""));
    }

    /**
     * Test for {@link LdsEndowmentDateStatus#toString()}
     */
    @Test
    public void testToString() {
        assertSame(LdsEndowmentDateStatus.CHILD.getCode(), LdsEndowmentDateStatus.CHILD.toString());
        assertSame(LdsEndowmentDateStatus.COMPLETED.getCode(), LdsEndowmentDateStatus.COMPLETED.toString());
        assertSame(LdsEndowmentDateStatus.EXCLUDED.getCode(), LdsEndowmentDateStatus.EXCLUDED.toString());
        assertSame(LdsEndowmentDateStatus.PRE_1970.getCode(), LdsEndowmentDateStatus.PRE_1970.toString());
        assertSame(LdsEndowmentDateStatus.STILLBORN.getCode(), LdsEndowmentDateStatus.STILLBORN.toString());
        assertSame(LdsEndowmentDateStatus.SUBMITTED.getCode(), LdsEndowmentDateStatus.SUBMITTED.toString());
        assertSame(LdsEndowmentDateStatus.UNCLEARED.getCode(), LdsEndowmentDateStatus.UNCLEARED.toString());
    }
}
