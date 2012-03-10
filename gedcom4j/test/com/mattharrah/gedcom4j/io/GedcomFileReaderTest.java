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
package com.mattharrah.gedcom4j.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

/**
 * @author frizbog1
 * 
 */
public class GedcomFileReaderTest {

    /**
     * A test for whether the GedcomReader properly handles multi line files
     * with CRLF line terminators
     * 
     * @throws IOException
     *             if the data can't be read
     */
    @Test
    public void testAnselCrlf() throws IOException {
        /*
         * Some encoded ANSEL data for a file with two lines. Line one consists
         * of a zero, a space, and an uppercase H. Line two consists of a
         * lowercase o. The lines are separated by a CRLF.
         */
        byte[] anselData = {
                0x30, 0x20, 0x48, 0x0D, 0x0A, 0x6F
        };

        ByteArrayInputStream s = new ByteArrayInputStream(anselData);
        GedcomFileReader gr = new GedcomFileReader();
        List<String> lines = gr.getLines(s);
        assertNotNull(lines);
        assertFalse(lines.isEmpty());
        assertEquals(2, lines.size());
        assertEquals("0 H", lines.get(0));
        assertEquals("o", lines.get(1));
    }

    /**
     * A test for whether the GedcomReader properly handles multi line files
     * with CR-only line terminators
     * 
     * @throws IOException
     *             if the data can't be read
     */
    @Test
    public void testAnselCrOnly() throws IOException {
        /*
         * Some encoded ANSEL data for a file with two lines. Line one consists
         * of a zero, a space, and an uppercase H. Line two consists of a
         * lowercase o. The lines are separated by a CR only.
         */
        byte[] anselData = {
                0x30, 0x20, 0x48, 0x0D, 0x6F
        };

        ByteArrayInputStream s = new ByteArrayInputStream(anselData);
        GedcomFileReader gr = new GedcomFileReader();
        List<String> lines = gr.getLines(s);
        assertNotNull(lines);
        assertFalse(lines.isEmpty());
        assertEquals(2, lines.size());
        assertEquals("0 H", lines.get(0));
        assertEquals("o", lines.get(1));
    }

    /**
     * A test for whether the GedcomReader properly decodes Ansel data
     * 
     * @throws IOException
     *             if the data can't be read
     */
    @Test
    public void testAnselDecodingSingleLine() throws IOException {
        /*
         * Some encoded ANSEL data for the phrase "0 Hello", with the l's
         * replaced by those uppercase L's with slashes through them (U+0141)
         */
        byte[] anselData = {
                0x30, 0x20, 0x48, 0x65, (byte) 0xA1, (byte) 0xA1, 0x6F
        };

        ByteArrayInputStream s = new ByteArrayInputStream(anselData);

        GedcomFileReader gr = new GedcomFileReader();
        List<String> lines = gr.getLines(s);
        assertNotNull(lines);
        assertFalse(lines.isEmpty());
        assertEquals("0 He\u0141\u0141o", lines.get(0));
    }

    /**
     * A test for whether the GedcomReader properly handles multi line files
     * with LF-only line terminators
     * 
     * @throws IOException
     *             if the data can't be read
     */
    @Test
    public void testAnselLfOnly() throws IOException {
        /*
         * Some encoded ANSEL data for a file with two lines. Line one consists
         * of a zero, a space, and an uppercase H. Line two consists of a
         * lowercase o. The lines are separated by a LF only.
         */
        byte[] anselData = {
                0x30, 0x20, 0x48, 0x0A, 0x6F
        };

        ByteArrayInputStream s = new ByteArrayInputStream(anselData);
        GedcomFileReader gr = new GedcomFileReader();
        List<String> lines = gr.getLines(s);
        assertNotNull(lines);
        assertFalse(lines.isEmpty());
        assertEquals(2, lines.size());
        assertEquals("0 H", lines.get(0));
        assertEquals("o", lines.get(1));
    }

    /**
     * Test reading unicode, big-endian, with CRLF as the line delimiter
     * 
     * @throws IOException
     *             if the data can't be read
     */
    @Test
    public void testUnicodeBigEndianWithCrlf() throws IOException {
        /*
         * Unicode, big-endian data with CRLF line terminator. Says "0 HEAD" on
         * line 1 and "1 CHAR" on line 2.
         */
        byte[] unicodeData = {
                0x00, 0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41,
                0x00, 0x44, 0x00, 0x0d, 0x00, 0x0a, 0x00, 0x31, 0x00, 0x20,
                0x00, 0x43, 0x00, 0x48, 0x00, 0x41, 0x00, 0x52
        };
        ByteArrayInputStream s = new ByteArrayInputStream(unicodeData);
        GedcomFileReader gr = new GedcomFileReader();
        List<String> lines = gr.getLines(s);
        assertNotNull(lines);
        assertFalse(lines.isEmpty());
        assertEquals(2, lines.size());
        assertEquals("0 HEAD", lines.get(0));
        assertEquals("1 CHAR", lines.get(1));
    }

    /**
     * Test reading unicode, big-endian, with CR only as the line delimiter
     * 
     * @throws IOException
     *             if the data can't be read
     */
    @Test
    public void testUnicodeBigEndianWithCrOnly() throws IOException {
        /*
         * Unicode, big-endian data with CRLF line terminator. Says "0 HEAD" on
         * line 1 and "1 CHAR" on line 2.
         */
        byte[] unicodeData = {
                0x00, 0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41,
                0x00, 0x44, 0x00, 0x0d, 0x00, 0x31, 0x00, 0x20, 0x00, 0x43,
                0x00, 0x48, 0x00, 0x41, 0x00, 0x52
        };
        ByteArrayInputStream s = new ByteArrayInputStream(unicodeData);
        GedcomFileReader gr = new GedcomFileReader();
        List<String> lines = gr.getLines(s);
        assertNotNull(lines);
        assertFalse(lines.isEmpty());
        assertEquals(2, lines.size());
        assertEquals("0 HEAD", lines.get(0));
        assertEquals("1 CHAR", lines.get(1));
    }

    /**
     * Test reading unicode data, big-endian, with only an LF as the line
     * delimiter
     * 
     * @throws IOException
     *             if the data can't be read
     */
    @Test
    public void testUnicodeBigEndianWithLfOnly() throws IOException {
        /*
         * Unicode, big-endian data with CRLF line terminator. Says "0 HEAD" on
         * line 1 and "1 CHAR" on line 2.
         */
        byte[] unicodeData = {
                0x00, 0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41,
                0x00, 0x44, 0x00, 0x0a, 0x00, 0x31, 0x00, 0x20, 0x00, 0x43,
                0x00, 0x48, 0x00, 0x41, 0x00, 0x52
        };
        ByteArrayInputStream s = new ByteArrayInputStream(unicodeData);
        GedcomFileReader gr = new GedcomFileReader();
        List<String> lines = gr.getLines(s);
        assertNotNull(lines);
        assertFalse(lines.isEmpty());
        assertEquals(2, lines.size());
        assertEquals("0 HEAD", lines.get(0));
        assertEquals("1 CHAR", lines.get(1));
    }

    /**
     * Test reading unicode data, little-endian byte order, with CRLF's as the
     * line delimiter
     * 
     * @throws IOException
     *             if the data can't be read
     */
    @Test
    public void testUnicodeLittleEndianWithCrlf() throws IOException {
        /*
         * Unicode, little-endian data with CRLF line terminator. Says "0 HEAD"
         * on line 1 and "1 CHAR" on line 2.
         */
        byte[] unicodeData = {
                0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41, 0x00,
                0x44, 0x00, 0x0d, 0x00, 0x0a, 0x00, 0x31, 0x00, 0x20, 0x00,
                0x43, 0x00, 0x48, 0x00, 0x41, 0x00, 0x52, 0x00
        };
        ByteArrayInputStream s = new ByteArrayInputStream(unicodeData);
        GedcomFileReader gr = new GedcomFileReader();
        List<String> lines = gr.getLines(s);
        assertNotNull(lines);
        assertFalse(lines.isEmpty());
        assertEquals(2, lines.size());
        assertEquals("0 HEAD", lines.get(0));
        assertEquals("1 CHAR", lines.get(1));
    }

    /**
     * Test reading unicode data, little-endian byte order, CR as line delimiter
     * 
     * @throws IOException
     *             if the data can't be read
     */
    @Test
    public void testUnicodeLittleEndianWithCrOnly() throws IOException {
        /*
         * Unicode, little-endian data with CRLF line terminator. Says "0 HEAD"
         * on line 1 and "1 CHAR" on line 2.
         */
        byte[] unicodeData = {
                0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41, 0x00,
                0x44, 0x00, 0x0d, 0x00, 0x31, 0x00, 0x20, 0x00, 0x43, 0x00,
                0x48, 0x00, 0x41, 0x00, 0x52, 0x00
        };
        ByteArrayInputStream s = new ByteArrayInputStream(unicodeData);
        GedcomFileReader gr = new GedcomFileReader();
        List<String> lines = gr.getLines(s);
        assertNotNull(lines);
        assertFalse(lines.isEmpty());
        assertEquals(2, lines.size());
        assertEquals("0 HEAD", lines.get(0));
        assertEquals("1 CHAR", lines.get(1));
    }

    /**
     * Test reading unicode data, little-endian byte order, with LF's as line
     * delimiter
     * 
     * @throws IOException
     *             if the data can't be read
     */
    @Test
    public void testUnicodeLittleEndianWithLfOnly() throws IOException {
        /*
         * Unicode, little-endian data with CRLF line terminator. Says "0 HEAD"
         * on line 1 and "1 CHAR" on line 2.
         */
        byte[] unicodeData = {
                0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41, 0x00,
                0x44, 0x00, 0x0a, 0x00, 0x31, 0x00, 0x20, 0x00, 0x43, 0x00,
                0x48, 0x00, 0x41, 0x00, 0x52, 0x00
        };
        ByteArrayInputStream s = new ByteArrayInputStream(unicodeData);
        GedcomFileReader gr = new GedcomFileReader();
        List<String> lines = gr.getLines(s);
        assertNotNull(lines);
        assertFalse(lines.isEmpty());
        assertEquals(2, lines.size());
        assertEquals("0 HEAD", lines.get(0));
        assertEquals("1 CHAR", lines.get(1));
    }

}
