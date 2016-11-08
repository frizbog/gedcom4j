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

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link ValidationException}
 * 
 * @author frizbog1
 */
@SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
public class ValidationExceptionTest {

    /**
     * Value for exception message
     */
    private static final String TEST_STRING = "Foo Bar Baz Bat";

    /**
     * Test method for {@link ValidationException#ValidationException()} .
     */
    @Test
    public void testValidationException() {
        try {
            throw new ValidationException();
        } catch (ValidationException e) {
            Assert.assertNotNull(e);
            Assert.assertNull(e.getMessage());
            Assert.assertNull(e.getCause());
        }
    }

    /**
     * Test method for {@link ValidationException#ValidationException(java.lang.String)} .
     */
    @Test
    public void testValidationExceptionString() {
        try {
            throw new ValidationException(TEST_STRING);
        } catch (ValidationException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(TEST_STRING, e.getMessage());
            Assert.assertNull(e.getCause());
        }
    }

    /**
     * Test method for {@link ValidationException#ValidationException(java.lang.String, java.lang.Throwable)} .
     */
    @Test
    public void testValidationExceptionStringThrowable() {
        try {
            throw new ValidationException(new RuntimeException());
        } catch (ValidationException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals("java.lang.RuntimeException", e.getMessage());
            Assert.assertNotNull(e.getCause());
        }
    }

    /**
     * Test method for {@link ValidationException#ValidationException(java.lang.Throwable)} .
     */
    @Test
    public void testValidationExceptionThrowable() {
        try {
            throw new ValidationException(TEST_STRING, new RuntimeException());
        } catch (ValidationException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(TEST_STRING, e.getMessage());
            Assert.assertNotNull(e.getCause());
        }
    }

}
