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
package org.gedcom4j.validate;

import junit.framework.Assert;

import org.gedcom4j.validate.GedcomValidationException;
import org.junit.Test;

/**
 * @author frizbog1
 * 
 */
public class GedcomValidationExceptionTest {

    /**
     * Test method for
     * {@link org.gedcom4j.validate.GedcomValidationException#GedcomValidationException()}
     * .
     */
    @Test
    public void testGedcomValidationException() {
        try {
            throw new GedcomValidationException();
        } catch (GedcomValidationException e) {
            Assert.assertNotNull(e);
            Assert.assertNull(e.getMessage());
            Assert.assertNull(e.getCause());
        }
    }

    /**
     * Test method for
     * {@link org.gedcom4j.validate.GedcomValidationException#GedcomValidationException(java.lang.String)}
     * .
     */
    @Test
    public void testGedcomValidationExceptionString() {
        try {
            throw new GedcomValidationException("Yo");
        } catch (GedcomValidationException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals("Yo", e.getMessage());
            Assert.assertNull(e.getCause());
        }
    }

    /**
     * Test method for
     * {@link org.gedcom4j.validate.GedcomValidationException#GedcomValidationException(java.lang.String, java.lang.Throwable)}
     * .
     */
    @Test
    public void testGedcomValidationExceptionStringThrowable() {
        try {
            throw new GedcomValidationException(new RuntimeException());
        } catch (GedcomValidationException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals("java.lang.RuntimeException", e.getMessage());
            Assert.assertNotNull(e.getCause());
        }
    }

    /**
     * Test method for
     * {@link org.gedcom4j.validate.GedcomValidationException#GedcomValidationException(java.lang.Throwable)}
     * .
     */
    @Test
    public void testGedcomValidationExceptionThrowable() {
        try {
            throw new GedcomValidationException("Yo", new RuntimeException());
        } catch (GedcomValidationException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals("Yo", e.getMessage());
            Assert.assertNotNull(e.getCause());
        }
    }

}
