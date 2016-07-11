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
package org.gedcom4j.io.event;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test for {@link FileProgressEvent}
 * 
 * @author frizbog
 *
 */
public class FileProgressEventTest {

    /**
     * Test #1
     */
    @Test
    public void testOne() {
        FileProgressEvent e = new FileProgressEvent(this, -1, 5, true);
        assertNotNull(e);
        assertEquals(-1, e.getLinesProcessed());
        assertEquals(5, e.getBytesProcessed());
        assertTrue(e.isComplete());
        assertSame(this, e.getSource());
    }

    /**
     * Test #2
     */
    @Test
    public void testTwo() {
        FileProgressEvent e = new FileProgressEvent(this, 50, Integer.MIN_VALUE, false);
        assertNotNull(e);
        assertEquals(50, e.getLinesProcessed());
        assertEquals(Integer.MIN_VALUE, e.getBytesProcessed());
        assertFalse(e.isComplete());
        assertSame(this, e.getSource());
    }

}
