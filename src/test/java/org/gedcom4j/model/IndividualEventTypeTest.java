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

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for the individual event type
 * 
 * @author frizbog1
 * 
 */
public class IndividualEventTypeTest {

    /**
     * Test for {@link IndividualEventType#getFromTag(String)}
     */
    @Test
    public void testGetFromTag() {
        Assert.assertSame(IndividualEventType.PROBATE, IndividualEventType.getFromTag("PROB"));
        Assert.assertNull(IndividualEventType.getFromTag(""));
        Assert.assertNull(IndividualEventType.getFromTag(null));
    }

    /**
     * Test for {@link IndividualEventType#isValidTag(String)}
     */
    @Test
    public void testIsValidTag() {
        Assert.assertTrue(IndividualEventType.isValidTag("BAPM"));
        Assert.assertFalse("Baptism is BAPM, not BAPT like you might expect", IndividualEventType.isValidTag("BAPT"));
    }
}
