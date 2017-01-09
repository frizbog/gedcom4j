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
 * Tests for {@link LdsSpouseSealingDateStatus}
 * 
 * @author reckenrod
 */
public class LdsSpouseSealingDateStatusTest {

    /**
     * Test for {@link LdsSpouseSealingDateStatus#getCode()}
     */
    @Test
    public void testGetCode() {
        assertSame(LdsSpouseSealingDateStatus.CANCELED.getCode(), "CANCELED");
        assertSame(LdsSpouseSealingDateStatus.COMPLETED.getCode(), "COMPLETED");
        assertSame(LdsSpouseSealingDateStatus.EXCLUDED.getCode(), "EXCLUDED");
        assertSame(LdsSpouseSealingDateStatus.DNS.getCode(), "DNS");
        assertSame(LdsSpouseSealingDateStatus.DNS_CAN.getCode(), "DNS/CAN");
        assertSame(LdsSpouseSealingDateStatus.PRE_1970.getCode(), "PRE-1970");
        assertSame(LdsSpouseSealingDateStatus.SUBMITTED.getCode(), "SUBMITTED");
        assertSame(LdsSpouseSealingDateStatus.UNCLEARED.getCode(), "UNCLEARED");
    }

    /**
     * Test for {@link LdsSpouseSealingDateStatus#getDescription()}
     */
    @Test
    public void testGetDescription() {
        assertSame(LdsSpouseSealingDateStatus.CANCELED.getDescription(), "Canceled and considered invalid.");
        assertSame(LdsSpouseSealingDateStatus.COMPLETED.getDescription(), "Completed but the date is not known.");
        assertSame(LdsSpouseSealingDateStatus.EXCLUDED.getDescription(),
                "Patron excluded this ordinance from being cleared in this submission.");
        assertSame(LdsSpouseSealingDateStatus.DNS.getDescription(), "This ordinance is not authorized.");
        assertSame(LdsSpouseSealingDateStatus.DNS_CAN.getDescription(),
                "This ordinance is not authorized, previous sealing cancelled.");
        assertSame(LdsSpouseSealingDateStatus.PRE_1970.getDescription(), "From before 1970, assumed complete.");
        assertSame(LdsSpouseSealingDateStatus.SUBMITTED.getDescription(), "Ordinance was previously submitted.");
        assertSame(LdsSpouseSealingDateStatus.UNCLEARED.getDescription(), "Data for clearing ordinance request was insufficient.");
    }

    /**
     * Test for {@link LdsSpouseSealingDateStatus#getForCode(String)}
     */
    @Test
    public void testGetForCode() {
        assertSame(LdsSpouseSealingDateStatus.CANCELED, LdsSpouseSealingDateStatus.getForCode("CANCELED"));
        assertSame(LdsSpouseSealingDateStatus.COMPLETED, LdsSpouseSealingDateStatus.getForCode("COMPLETED"));
        assertSame(LdsSpouseSealingDateStatus.DNS, LdsSpouseSealingDateStatus.getForCode("DNS"));
        assertSame(LdsSpouseSealingDateStatus.DNS_CAN, LdsSpouseSealingDateStatus.getForCode("DNS/CAN"));
        assertSame(LdsSpouseSealingDateStatus.EXCLUDED, LdsSpouseSealingDateStatus.getForCode("EXCLUDED"));
        assertSame(LdsSpouseSealingDateStatus.PRE_1970, LdsSpouseSealingDateStatus.getForCode("PRE-1970"));
        assertSame(LdsSpouseSealingDateStatus.SUBMITTED, LdsSpouseSealingDateStatus.getForCode("SUBMITTED"));
        assertSame(LdsSpouseSealingDateStatus.UNCLEARED, LdsSpouseSealingDateStatus.getForCode("UNCLEARED"));
        assertNull(LdsSpouseSealingDateStatus.getForCode("BAD"));
        assertNull(LdsSpouseSealingDateStatus.getForCode(null));
        assertNull(LdsSpouseSealingDateStatus.getForCode(""));
    }

    /**
     * Test for {@link LdsSpouseSealingDateStatus#toString()}
     */
    @Test
    public void testToString() {
        assertSame(LdsSpouseSealingDateStatus.CANCELED.getCode(), LdsSpouseSealingDateStatus.CANCELED.toString());
        assertSame(LdsSpouseSealingDateStatus.COMPLETED.getCode(), LdsSpouseSealingDateStatus.COMPLETED.toString());
        assertSame(LdsSpouseSealingDateStatus.EXCLUDED.getCode(), LdsSpouseSealingDateStatus.EXCLUDED.toString());
        assertSame(LdsSpouseSealingDateStatus.DNS.getCode(), LdsSpouseSealingDateStatus.DNS.toString());
        assertSame(LdsSpouseSealingDateStatus.DNS_CAN.getCode(), LdsSpouseSealingDateStatus.DNS_CAN.toString());
        assertSame(LdsSpouseSealingDateStatus.PRE_1970.getCode(), LdsSpouseSealingDateStatus.PRE_1970.toString());
        assertSame(LdsSpouseSealingDateStatus.SUBMITTED.getCode(), LdsSpouseSealingDateStatus.SUBMITTED.toString());
        assertSame(LdsSpouseSealingDateStatus.UNCLEARED.getCode(), LdsSpouseSealingDateStatus.UNCLEARED.toString());
    }
}
