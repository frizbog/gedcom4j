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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.gedcom4j.exception.UnsupportedVersionException;
import org.junit.Test;

/**
 * Test for the SupportedVersion class
 * 
 * @author frizbog
 * 
 */
public class SupportedVersionTest {

    /**
     * Tests the SupportedVersion class and the UnsupportedVersionException
     * 
     * @throws UnsupportedVersionException
     *             should never happen unless the {@link SupportedVersion#forString(String)} method is broken
     */
    @Test
    public void test() throws UnsupportedVersionException {
        assertNotNull(SupportedVersion.forString("5.5"));
        assertNotNull(SupportedVersion.forString("5.5.1"));

        try {
            SupportedVersion.forString("frying pan");
            fail("Should have gotten an exception");
        } catch (@SuppressWarnings("unused") UnsupportedVersionException expectedAndIgnored) {
            ; // Good!
        }
    }

}
