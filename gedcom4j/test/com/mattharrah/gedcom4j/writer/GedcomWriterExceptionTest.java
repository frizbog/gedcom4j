/*
 * Copyright (c) 2009-2012 Matthew R. Harrah
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
package com.mattharrah.gedcom4j.writer;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author frizbog1
 * 
 */
public class GedcomWriterExceptionTest {

    /**
     * Test method for
     * {@link com.mattharrah.gedcom4j.writer.GedcomWriterException#GedcomWriterException()}
     * .
     */
    @Test
    public void testGedcomWriterException() {
        try {
            throw new GedcomWriterException();
        } catch (GedcomWriterException e) {
            Assert.assertNotNull(e);
            Assert.assertNull(e.getMessage());
            Assert.assertNull(e.getCause());
        }
    }

    /**
     * Test method for
     * {@link com.mattharrah.gedcom4j.writer.GedcomWriterException#GedcomWriterException(java.lang.String)}
     * .
     */
    @Test
    public void testGedcomWriterExceptionString() {
        try {
            throw new GedcomWriterException("Yo");
        } catch (GedcomWriterException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals("Yo", e.getMessage());
            Assert.assertNull(e.getCause());
        }
    }

    /**
     * Test method for
     * {@link com.mattharrah.gedcom4j.writer.GedcomWriterException#GedcomWriterException(java.lang.String, java.lang.Throwable)}
     * .
     */
    @Test
    public void testGedcomWriterExceptionStringThrowable() {
        try {
            throw new GedcomWriterException(new RuntimeException());
        } catch (GedcomWriterException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals("java.lang.RuntimeException", e.getMessage());
            Assert.assertNotNull(e.getCause());
        }
    }

    /**
     * Test method for
     * {@link com.mattharrah.gedcom4j.writer.GedcomWriterException#GedcomWriterException(java.lang.Throwable)}
     * .
     */
    @Test
    public void testGedcomWriterExceptionThrowable() {
        try {
            throw new GedcomWriterException("Yo", new RuntimeException());
        } catch (GedcomWriterException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals("Yo", e.getMessage());
            Assert.assertNotNull(e.getCause());
        }
    }

}
