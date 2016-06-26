/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
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
package org.gedcom4j.parser;

import junit.framework.TestCase;

/**
 * Test for {@link StringTreeBuilder}
 * 
 * @author frizbog
 */
public class StringTreeBuilderTest extends TestCase {

    /**
     * Test case for {@link StringTreeBuilder#leftTrim(String)}
     */
    public void testLeftTrim() {
        assertNull(StringTreeBuilder.leftTrim(null));
        assertEquals("This is a test", StringTreeBuilder.leftTrim("            This is a test"));
        assertEquals("This is a test", StringTreeBuilder.leftTrim(" This is a test"));
        assertEquals("This is a test", StringTreeBuilder.leftTrim("This is a test"));
        assertEquals("This is a test", StringTreeBuilder.leftTrim("\t   This is a test"));
        assertEquals("This is a test", StringTreeBuilder.leftTrim(" \t  This is a test"));
        assertEquals("This is a test", StringTreeBuilder.leftTrim(" \n  This is a test"));
        assertEquals("This is a test", StringTreeBuilder.leftTrim(" \t\n  \n\r  This is a test"));
        assertEquals("This is a test", StringTreeBuilder.leftTrim(" \t\n  \u000B\f\n\r  This is a test"));
    }

}
