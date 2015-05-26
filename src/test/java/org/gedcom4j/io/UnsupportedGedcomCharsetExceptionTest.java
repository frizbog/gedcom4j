/*
 * Copyright (c) 2009-2014 Matthew R. Harrah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.gedcom4j.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.gedcom4j.io.UnsupportedGedcomCharsetException;
import org.junit.Test;

/**
 * @author frizbog1
 * 
 */
public class UnsupportedGedcomCharsetExceptionTest {

    /**
     * Test throwing the exception with cause and message
     */
    @Test
    public void testCauseAndMessage() {
        try {
            throw new UnsupportedGedcomCharsetException("Foo", new Exception(
                    "Foo"));
        } catch (UnsupportedGedcomCharsetException e) {
            assertNotNull(e);
            assertEquals("Foo", e.getMessage());
            assertNotNull(e.getCause());
        }
    }

    /**
     * Test throwing the exception with just a cause
     */
    @Test
    public void testJustCause() {
        try {
            throw new UnsupportedGedcomCharsetException(new Exception("Foo"));
        } catch (UnsupportedGedcomCharsetException e) {
            assertNotNull(e);
            assertEquals("java.lang.Exception: Foo", e.getMessage());
            assertNotNull(e.getCause());
        }
    }

    /**
     * Test throwing the exception with just message
     */
    @Test
    public void testJustMessage() {
        try {
            throw new UnsupportedGedcomCharsetException("Foo");
        } catch (UnsupportedGedcomCharsetException e) {
            assertNotNull(e);
            assertEquals("Foo", e.getMessage());
            assertNull(e.getCause());
        }
    }

    /**
     * Test throwing the exception with no parms
     */
    @Test
    public void testNoParms() {
        try {
            throw new UnsupportedGedcomCharsetException();
        } catch (UnsupportedGedcomCharsetException e) {
            assertNotNull(e);
            assertNull(e.getMessage());
            assertNull(e.getCause());
        }
    }

}
