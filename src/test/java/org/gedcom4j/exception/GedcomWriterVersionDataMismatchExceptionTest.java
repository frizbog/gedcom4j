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

import org.gedcom4j.exception.GedcomWriterVersionDataMismatchException;
import org.junit.Test;

/**
 * Test for {@link GedcomWriterVersionDataMismatchException}
 * 
 * @author frizbog1
 */
public class GedcomWriterVersionDataMismatchExceptionTest {

    /**
     * Test no arg constructor
     */
    @Test
    public void testGedcomWriterVersionDataMismatchException() {
        try {
            throw new GedcomWriterVersionDataMismatchException();
        } catch (GedcomWriterVersionDataMismatchException e) {
            assertNotNull(e);
            assertNull(e.getCause());
            assertNull(e.getMessage());
        }
    }

    /**
     * Test single string constructor
     */
    @Test
    public void testGedcomWriterVersionDataMismatchExceptionString() {
        try {
            throw new GedcomWriterVersionDataMismatchException("Foo");
        } catch (GedcomWriterVersionDataMismatchException e) {
            assertNotNull(e);
            assertNull(e.getCause());
            assertEquals("Foo", e.getMessage());
        }
    }

    /**
     * Test constructor that takes string and throwable
     */
    @Test
    public void testGedcomWriterVersionDataMismatchExceptionStringThrowable() {
        try {
            throw new GedcomWriterVersionDataMismatchException("Foo", new RuntimeException());
        } catch (GedcomWriterVersionDataMismatchException e) {
            assertNotNull(e);
            assertNotNull(e.getCause());
            assertEquals("Foo", e.getMessage());
        }
    }

    /**
     * Test constructor that takes throwable
     */
    @Test
    public void testGedcomWriterVersionDataMismatchExceptionThrowable() {
        try {
            throw new GedcomWriterVersionDataMismatchException(new RuntimeException());
        } catch (GedcomWriterVersionDataMismatchException e) {
            assertNotNull(e);
            assertNotNull(e.getCause());
            assertNotNull(e.getMessage());
        }
    }

}
