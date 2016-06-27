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
package org.gedcom4j.io.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.*;
import java.util.List;

import org.gedcom4j.exception.ParserCancelledException;
import org.gedcom4j.parser.GedcomParser;
import org.junit.Test;

/**
 * @author frizbog1
 * 
 */
public class GedcomFileReaderTest {

    /**
     * A test for whether the GedcomReader properly handles multi line files with CRLF line terminators
     * 
     * @throws IOException
     *             if the data can't be read
     * @throws ParserCancelledException
     *             if the file load was cancelled
     */
    @Test
    public void testAnselCrlf() throws IOException, ParserCancelledException {
        /*
         * Some encoded ANSEL data for a file with two lines. Line one consists of a zero, a space, and an uppercase H.
         * Line two consists of a lowercase o. The lines are separated by a CRLF.
         */
        byte[] anselData = { 0x30, 0x20, 0x48, 0x0D, 0x0A, 0x6F };

        BufferedInputStream s = null;
        try {
            s = new BufferedInputStream(new ByteArrayInputStream(anselData));
            GedcomFileReader gr = new GedcomFileReader(new GedcomParser(), s);
            List<String> lines = gr.getLines();
            assertNotNull(lines);
            assertFalse(lines.isEmpty());
            assertEquals(2, lines.size());
            assertEquals("0 H", lines.get(0));
            assertEquals("o", lines.get(1));
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    /**
     * A test for whether the GedcomReader properly handles multi line files with CR-only line terminators
     * 
     * @throws IOException
     *             if the data can't be read
     * @throws ParserCancelledException
     *             if the file load was cancelled
     */
    @Test
    public void testAnselCrOnly() throws IOException, ParserCancelledException {
        /*
         * Some encoded ANSEL data for a file with two lines. Line one consists of a zero, a space, and an uppercase H.
         * Line two consists of a lowercase o. The lines are separated by a CR only.
         */
        byte[] anselData = { 0x30, 0x20, 0x48, 0x0D, 0x6F };

        BufferedInputStream s = null;
        try {
            s = new BufferedInputStream(new ByteArrayInputStream(anselData));
            GedcomFileReader gr = new GedcomFileReader(new GedcomParser(), s);
            ;
            List<String> lines = gr.getLines();
            assertNotNull(lines);
            assertFalse(lines.isEmpty());
            assertEquals(2, lines.size());
            assertEquals("0 H", lines.get(0));
            assertEquals("o", lines.get(1));
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    /**
     * A test for whether the GedcomReader properly decodes Ansel data
     * 
     * @throws IOException
     *             if the data can't be read
     * @throws ParserCancelledException
     *             if the file load was cancelled
     */
    @Test
    public void testAnselDecodingSingleLine() throws IOException, ParserCancelledException {
        /*
         * Some encoded ANSEL data for the phrase "0 Hello", with the l's replaced by those uppercase L's with slashes
         * through them (U+0141)
         */
        byte[] anselData = { 0x30, 0x20, 0x48, 0x65, (byte) 0xA1, (byte) 0xA1, 0x6F };

        BufferedInputStream s = null;
        try {
            s = new BufferedInputStream(new ByteArrayInputStream(anselData));

            GedcomFileReader gr = new GedcomFileReader(new GedcomParser(), s);
            ;
            List<String> lines = gr.getLines();
            assertNotNull(lines);
            assertFalse(lines.isEmpty());
            assertEquals("0 He\u0141\u0141o", lines.get(0));
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    /**
     * A test for whether the GedcomReader properly handles multi line files with LF-only line terminators
     * 
     * @throws IOException
     *             if the data can't be read
     * @throws ParserCancelledException
     *             if the file load was cancelled
     */
    @Test
    public void testAnselLfOnly() throws IOException, ParserCancelledException {
        /*
         * Some encoded ANSEL data for a file with two lines. Line one consists of a zero, a space, and an uppercase H.
         * Line two consists of a lowercase o. The lines are separated by a LF only.
         */
        byte[] anselData = { 0x30, 0x20, 0x48, 0x0A, 0x6F };

        BufferedInputStream s = null;
        try {
            s = new BufferedInputStream(new ByteArrayInputStream(anselData));
            GedcomFileReader gr = new GedcomFileReader(new GedcomParser(), s);
            ;
            List<String> lines = gr.getLines();
            assertNotNull(lines);
            assertFalse(lines.isEmpty());
            assertEquals(2, lines.size());
            assertEquals("0 H", lines.get(0));
            assertEquals("o", lines.get(1));
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    /**
     * Test for {@link GedcomFileReader#firstNBytes(int)}
     * 
     * @throws IOException
     *             if the first few bytes of the "file" cannot be read
     */
    @Test
    public void testFirstNBytes() throws IOException {
        GedcomFileReader gfr = new GedcomFileReader(new GedcomParser(),
                new BufferedInputStream(new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x56, 0x78 })));
        // Haven't save the first chunk yet
        assertNotNull(gfr.firstChunk);
        assertEquals(0x0, gfr.firstNBytes(1));
        assertEquals(0x0, gfr.firstNBytes(2));
        assertEquals(0x0, gfr.firstNBytes(3));

        gfr.saveFirstChunk();
        assertEquals(0x12, gfr.firstNBytes(1));
        assertEquals(0x1234, gfr.firstNBytes(2));
        assertEquals(0x123456, gfr.firstNBytes(3));
        assertEquals(0x12345678, gfr.firstNBytes(4));
    }

    /**
     * Test reading unicode, big-endian, with CRLF as the line delimiter
     * 
     * @throws IOException
     *             if the data can't be read
     * @throws ParserCancelledException
     *             if the file load was cancelled
     */
    @Test
    public void testUnicodeBigEndianWithCrlf() throws IOException, ParserCancelledException {
        /*
         * Unicode, big-endian data with CRLF line terminator. Says "0 HEAD" on line 1 and "1 CHAR" on line 2.
         */
        byte[] unicodeData = { 0x00, 0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41, 0x00, 0x44, 0x00, 0x0d, 0x00, 0x0a, 0x00, 0x31, 0x00, 0x20, 0x00,
                0x43, 0x00, 0x48, 0x00, 0x41, 0x00, 0x52 };
        BufferedInputStream s = null;
        try {
            s = new BufferedInputStream(new ByteArrayInputStream(unicodeData));
            GedcomFileReader gr = new GedcomFileReader(new GedcomParser(), s);
            ;
            List<String> lines = gr.getLines();
            assertNotNull(lines);
            assertFalse(lines.isEmpty());
            assertEquals(2, lines.size());
            assertEquals("0 HEAD", lines.get(0));
            assertEquals("1 CHAR", lines.get(1));
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    /**
     * Test reading unicode, big-endian, with CR only as the line delimiter
     * 
     * @throws IOException
     *             if the data can't be read
     * @throws ParserCancelledException
     *             if the file load was cancelled
     */
    @Test
    public void testUnicodeBigEndianWithCrOnly() throws IOException, ParserCancelledException {
        /*
         * Unicode, big-endian data with CRLF line terminator. Says "0 HEAD" on line 1 and "1 CHAR" on line 2.
         */
        byte[] unicodeData = { 0x00, 0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41, 0x00, 0x44, 0x00, 0x0d, 0x00, 0x31, 0x00, 0x20, 0x00, 0x43, 0x00,
                0x48, 0x00, 0x41, 0x00, 0x52 };
        BufferedInputStream s = null;
        try {
            s = new BufferedInputStream(new ByteArrayInputStream(unicodeData));
            GedcomFileReader gr = new GedcomFileReader(new GedcomParser(), s);
            ;
            List<String> lines = gr.getLines();
            assertNotNull(lines);
            assertFalse(lines.isEmpty());
            assertEquals(2, lines.size());
            assertEquals("0 HEAD", lines.get(0));
            assertEquals("1 CHAR", lines.get(1));
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    /**
     * Test reading unicode data, big-endian, with only an LF as the line delimiter
     * 
     * @throws IOException
     *             if the data can't be read
     * @throws ParserCancelledException
     *             if the file load was cancelled
     */
    @Test
    public void testUnicodeBigEndianWithLfOnly() throws IOException, ParserCancelledException {
        /*
         * Unicode, big-endian data with CRLF line terminator. Says "0 HEAD" on line 1 and "1 CHAR" on line 2.
         */
        byte[] unicodeData = { 0x00, 0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41, 0x00, 0x44, 0x00, 0x0a, 0x00, 0x31, 0x00, 0x20, 0x00, 0x43, 0x00,
                0x48, 0x00, 0x41, 0x00, 0x52 };
        BufferedInputStream s = null;
        try {
            s = new BufferedInputStream(new ByteArrayInputStream(unicodeData));
            GedcomFileReader gr = new GedcomFileReader(new GedcomParser(), s);
            ;
            List<String> lines = gr.getLines();
            assertNotNull(lines);
            assertFalse(lines.isEmpty());
            assertEquals(2, lines.size());
            assertEquals("0 HEAD", lines.get(0));
            assertEquals("1 CHAR", lines.get(1));
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    /**
     * Test reading unicode data, little-endian byte order, with CRLF's as the line delimiter
     * 
     * @throws IOException
     *             if the data can't be read
     * @throws ParserCancelledException
     *             if the file load was cancelled
     */
    @Test
    public void testUnicodeLittleEndianWithCrlf() throws IOException, ParserCancelledException {
        /*
         * Unicode, little-endian data with CRLF line terminator. Says "0 HEAD" on line 1 and "1 CHAR" on line 2.
         */
        byte[] unicodeData = { 0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41, 0x00, 0x44, 0x00, 0x0d, 0x00, 0x0a, 0x00, 0x31, 0x00, 0x20, 0x00, 0x43,
                0x00, 0x48, 0x00, 0x41, 0x00, 0x52, 0x00 };
        BufferedInputStream s = null;
        try {
            s = new BufferedInputStream(new ByteArrayInputStream(unicodeData));
            GedcomFileReader gr = new GedcomFileReader(new GedcomParser(), s);
            ;
            List<String> lines = gr.getLines();
            assertNotNull(lines);
            assertFalse(lines.isEmpty());
            assertEquals(2, lines.size());
            assertEquals("0 HEAD", lines.get(0));
            assertEquals("1 CHAR", lines.get(1));
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    /**
     * Test reading unicode data, little-endian byte order, CR as line delimiter
     * 
     * @throws IOException
     *             if the data can't be read
     * @throws ParserCancelledException
     *             if the file load was cancelled
     */
    @Test
    public void testUnicodeLittleEndianWithCrOnly() throws IOException, ParserCancelledException {
        /*
         * Unicode, little-endian data with CRLF line terminator. Says "0 HEAD" on line 1 and "1 CHAR" on line 2.
         */
        byte[] unicodeData = { 0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41, 0x00, 0x44, 0x00, 0x0d, 0x00, 0x31, 0x00, 0x20, 0x00, 0x43, 0x00, 0x48,
                0x00, 0x41, 0x00, 0x52, 0x00 };
        BufferedInputStream s = null;
        try {
            s = new BufferedInputStream(new ByteArrayInputStream(unicodeData));
            GedcomFileReader gr = new GedcomFileReader(new GedcomParser(), s);
            ;
            List<String> lines = gr.getLines();
            assertNotNull(lines);
            assertFalse(lines.isEmpty());
            assertEquals(2, lines.size());
            assertEquals("0 HEAD", lines.get(0));
            assertEquals("1 CHAR", lines.get(1));
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    /**
     * Test reading unicode data, little-endian byte order, with LF's as line delimiter
     * 
     * @throws IOException
     *             if the data can't be read
     * @throws ParserCancelledException
     *             if the file load was cancelled
     */
    @Test
    public void testUnicodeLittleEndianWithLfOnly() throws IOException, ParserCancelledException {
        /*
         * Unicode, little-endian data with CRLF line terminator. Says "0 HEAD" on line 1 and "1 CHAR" on line 2.
         */
        byte[] unicodeData = { 0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41, 0x00, 0x44, 0x00, 0x0a, 0x00, 0x31, 0x00, 0x20, 0x00, 0x43, 0x00, 0x48,
                0x00, 0x41, 0x00, 0x52, 0x00 };
        BufferedInputStream s = null;
        try {
            s = new BufferedInputStream(new ByteArrayInputStream(unicodeData));
            GedcomFileReader gr = new GedcomFileReader(new GedcomParser(), s);
            ;
            List<String> lines = gr.getLines();
            assertNotNull(lines);
            assertFalse(lines.isEmpty());
            assertEquals(2, lines.size());
            assertEquals("0 HEAD", lines.get(0));
            assertEquals("1 CHAR", lines.get(1));
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    /**
     * Test reading a UTF8 file using CRLF delimiters and Byte order markers
     * 
     * @throws IOException
     *             if the data can't be read
     * @throws ParserCancelledException
     *             if the file load was cancelled
     */
    @Test
    public void testUtf8CrLfBOM() throws IOException, ParserCancelledException {
        testUtf8File("sample/utf8_crlf_bom.ged");
    }

    /**
     * Test reading a UTF8 file using CRLF delimiters and no Byte order markers
     * 
     * @throws IOException
     *             if the data can't be read
     * @throws ParserCancelledException
     *             if the file load was cancelled
     */
    @Test
    public void testUtf8CrLfNoBOM() throws IOException, ParserCancelledException {
        testUtf8File("sample/utf8_crlf_nobom.ged");
    }

    /**
     * Test reading a UTF8 file using CR delimiters and no Byte order markers
     * 
     * @throws IOException
     *             if the data can't be read
     * @throws ParserCancelledException
     *             if the file load was cancelled
     */
    @Test
    public void testUtf8CrNoBOM() throws IOException, ParserCancelledException {
        testUtf8File("sample/utf8_cr_nobom.ged");
    }

    /**
     * Test reading a UTF8 file using LF delimiters and no Byte order markers
     * 
     * @throws IOException
     *             if the data can't be read
     * @throws ParserCancelledException
     *             if the file load was cancelled
     */
    @Test
    public void testUtf8LfNoBOM() throws IOException, ParserCancelledException {
        testUtf8File("sample/utf8_lf_nobom.ged");
    }

    /**
     * Test that a UTF file was read and character decoded correctly
     * 
     * @param fileName
     *            the name of the file to load and check
     * @throws IOException
     *             if the file can't be loaded
     * @throws FileNotFoundException
     *             if the file can't be found
     * @throws ParserCancelledException
     *             if the file load was cancelled
     */
    void testUtf8File(String fileName) throws IOException, FileNotFoundException, ParserCancelledException {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(fileName);
            GedcomFileReader gr = new GedcomFileReader(new GedcomParser(), new BufferedInputStream(fileInputStream));
            List<String> lines = gr.getLines();
            assertNotNull(lines);
            assertEquals(77, lines.size());
            assertEquals("2 VERS 5.5.1", lines.get(6));
            assertEquals("1 CHAR UTF-8", lines.get(8));

            // Check all the non-ascii characters in the names - just a sample but
            // should give pretty good confidence
            assertEquals("1 NAME John /Gr\u00FCber/", lines.get(10));
            assertEquals("1 NAME Mary /H\u00E6germann/", lines.get(16));
            assertEquals("1 NAME Abraham /Sm\u00EEth/", lines.get(22));
            assertEquals("1 NAME Betsy /Gro\u00DFmann/", lines.get(29));
            assertEquals("1 NAME Cleo /N\u00F4rden/", lines.get(36));
            assertEquals("1 NAME Elizabeth /J\u00e5ckson/", lines.get(39));
            assertEquals("1 NAME Daniel /\u0106uar\u00f3n/", lines.get(42));
            assertEquals("1 NAME Michael /Gar\u00E7on/", lines.get(46));
            assertEquals("1 NAME Ellen /\u0141owenst\u0117in/", lines.get(49));
            assertEquals("1 NAME Fred /\u00DBlrich/", lines.get(53));
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }

    }
}
