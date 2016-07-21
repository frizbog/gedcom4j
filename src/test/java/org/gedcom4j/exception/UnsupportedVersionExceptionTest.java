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
package org.gedcom4j.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Test for {@link UnsupportedOperationException}
 * 
 * @author frizbog
 */
public class UnsupportedVersionExceptionTest {

    /**
     * Test for constructor that takes no args
     */
    @Test
    public void testUnsupportedVersionException() {
        try {
            throw new UnsupportedVersionException();
        } catch (UnsupportedVersionException e) {
            assertNotNull(e);
            assertNull(e.getCause());
            assertNull(e.getMessage());
        }
    }

    /**
     * Test for constructor that takes just a message
     */
    @Test
    public void testUnsupportedVersionExceptionString() {
        try {
            throw new UnsupportedVersionException("Frying Pan");
        } catch (UnsupportedVersionException e) {
            assertNotNull(e);
            assertNull(e.getCause());
            assertEquals("Frying Pan", e.getMessage());
        }
    }

    /**
     * Test for constructor that takes a message and a cause
     */
    @Test
    public void testUnsupportedVersionExceptionStringThrowable() {
        try {
            throw new UnsupportedVersionException("Frying Pan", new RuntimeException());
        } catch (UnsupportedVersionException e) {
            assertNotNull(e);
            assertNotNull(e.getCause());
            assertEquals("Frying Pan", e.getMessage());
        }
    }

    /**
     * Test for constructor that takes just a cause
     */
    @Test
    public void testUnsupportedVersionExceptionThrowable() {
        try {
            throw new UnsupportedVersionException(new RuntimeException());
        } catch (UnsupportedVersionException e) {
            assertNotNull(e);
            assertNotNull(e.getCause());
            assertNotNull(e.getMessage());
        }
    }

}
