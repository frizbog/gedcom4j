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
package org.gedcom4j.io;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author frizbog1
 * 
 */
public class AnselMappingTest {

    /**
     * Test decoding bytes into characters
     */
    @Test
    public void testDecode() {
        assertEquals("Capital A is ASCII/ANSEL 0x41", 'A', AnselMapping.decode((char) 0x41));
        assertEquals("Lowercase z is ASCII/ANSEL 0x7A", 'z', AnselMapping.decode((char) 0x7A));
        assertEquals("The unicode character for a capital L with a slash through it (U+0141, or \u0141) is ANSEL A1", '\u0141',
                AnselMapping.decode((char) 0xA1));
        assertEquals("The unicode character for a musical flat (U+266D, or \u266D) is ANSEL A9", '\u266D', AnselMapping.decode((char) 0xA9));
    }

    /**
     * Test encoding characters into bytes
     */
    @Test
    public void testEncode() {
        assertEquals("Capital A is ASCII/ANSEL 0x41", 0x41, AnselMapping.encode('A'));
        assertEquals("Lowercase z is ASCII/ANSEL 0x7A", 0x7A, AnselMapping.encode('z'));
        assertEquals("The unicode character for a capital L with a slash through it (U+0141, or \u0141) is ANSEL A1", 0xA1, AnselMapping.encode('\u0141'));
        assertEquals("The unicode character for a musical flat (U+266D, or \u266D) is ANSEL A9", 0xA9, AnselMapping.encode('\u266D'));
    }
}
