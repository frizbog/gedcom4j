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
 * Tests for {@link LdsChildSealingDateStatus}
 * 
 * @author reckenrod
 */
public class LdsChildSealingDateStatusTest {

    /**
     * Test for {@link LdsChildSealingDateStatus#getCode()}
     */
    @Test
    public void testGetCode() {
        assertSame(LdsChildSealingDateStatus.BIC.getCode(), "BIC");
        assertSame(LdsChildSealingDateStatus.COMPLETED.getCode(), "COMPLETED");
        assertSame(LdsChildSealingDateStatus.EXCLUDED.getCode(), "EXCLUDED");
        assertSame(LdsChildSealingDateStatus.DNS.getCode(), "DNS");
        assertSame(LdsChildSealingDateStatus.PRE_1970.getCode(), "PRE-1970");
        assertSame(LdsChildSealingDateStatus.STILLBORN.getCode(), "STILLBORN");
        assertSame(LdsChildSealingDateStatus.SUBMITTED.getCode(), "SUBMITTED");
        assertSame(LdsChildSealingDateStatus.UNCLEARED.getCode(), "UNCLEARED");
    }

    /**
     * Test for {@link LdsChildSealingDateStatus#getDescription()}
     */
    @Test
    public void testGetDescription() {
        assertSame(LdsChildSealingDateStatus.BIC.getDescription(),
                "Born in the covenant receiving blessing of child to parent sealing.");
        assertSame(LdsChildSealingDateStatus.COMPLETED.getDescription(), "Completed but the date is not known.");
        assertSame(LdsChildSealingDateStatus.EXCLUDED.getDescription(),
                "Patron excluded this ordinance from being cleared in this submission.");
        assertSame(LdsChildSealingDateStatus.DNS.getDescription(), "This ordinance is not authorized.");
        assertSame(LdsChildSealingDateStatus.PRE_1970.getDescription(),
                "Ordinance from temple records of work completed before 1970, assumed complete.");
        assertSame(LdsChildSealingDateStatus.STILLBORN.getDescription(), "Stillborn, baptism not required.");
        assertSame(LdsChildSealingDateStatus.SUBMITTED.getDescription(), "Ordinance was previously submitted.");
        assertSame(LdsChildSealingDateStatus.UNCLEARED.getDescription(), "Data for clearing ordinance request was insufficient.");
    }

    /**
     * Test for {@link LdsChildSealingDateStatus#getForCode(String)}
     */
    @Test
    public void testGetForCode() {
        assertSame(LdsChildSealingDateStatus.BIC, LdsChildSealingDateStatus.getForCode("BIC"));
        assertSame(LdsChildSealingDateStatus.COMPLETED, LdsChildSealingDateStatus.getForCode("COMPLETED"));
        assertSame(LdsChildSealingDateStatus.DNS, LdsChildSealingDateStatus.getForCode("DNS"));
        assertSame(LdsChildSealingDateStatus.EXCLUDED, LdsChildSealingDateStatus.getForCode("EXCLUDED"));
        assertSame(LdsChildSealingDateStatus.PRE_1970, LdsChildSealingDateStatus.getForCode("PRE-1970"));
        assertSame(LdsChildSealingDateStatus.STILLBORN, LdsChildSealingDateStatus.getForCode("STILLBORN"));
        assertSame(LdsChildSealingDateStatus.SUBMITTED, LdsChildSealingDateStatus.getForCode("SUBMITTED"));
        assertSame(LdsChildSealingDateStatus.UNCLEARED, LdsChildSealingDateStatus.getForCode("UNCLEARED"));
        assertNull(LdsChildSealingDateStatus.getForCode("BAD"));
        assertNull(LdsChildSealingDateStatus.getForCode(null));
        assertNull(LdsChildSealingDateStatus.getForCode(""));
    }

    /**
     * Test for {@link LdsChildSealingDateStatus#toString()}
     */
    @Test
    public void testToString() {
        assertSame(LdsChildSealingDateStatus.BIC.getCode(), LdsChildSealingDateStatus.BIC.toString());
        assertSame(LdsChildSealingDateStatus.COMPLETED.getCode(), LdsChildSealingDateStatus.COMPLETED.toString());
        assertSame(LdsChildSealingDateStatus.EXCLUDED.getCode(), LdsChildSealingDateStatus.EXCLUDED.toString());
        assertSame(LdsChildSealingDateStatus.DNS.getCode(), LdsChildSealingDateStatus.DNS.toString());
        assertSame(LdsChildSealingDateStatus.PRE_1970.getCode(), LdsChildSealingDateStatus.PRE_1970.toString());
        assertSame(LdsChildSealingDateStatus.STILLBORN.getCode(), LdsChildSealingDateStatus.STILLBORN.toString());
        assertSame(LdsChildSealingDateStatus.SUBMITTED.getCode(), LdsChildSealingDateStatus.SUBMITTED.toString());
        assertSame(LdsChildSealingDateStatus.UNCLEARED.getCode(), LdsChildSealingDateStatus.UNCLEARED.toString());
    }
}
