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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.StringWithCustomTags;
import org.gedcom4j.model.Submission;
import org.gedcom4j.model.Submitter;
import org.gedcom4j.writer.GedcomWriter;
import org.gedcom4j.writer.GedcomWriterException;
import org.junit.Test;

/**
 * Test for {@link GedcomFileWriter}. Doesn't actually test writing GEDCOM data per se, but tests reading and writing
 * various encodings (including ANSEL which has no direct Java support) and ensuring that non ASCII characters are
 * handled appropriately (which for most cases means preserving the characters).
 * 
 * @author frizbog1
 */
public class GedcomFileWriterTest {

    /**
     * Test when there is no data
     */
    @Test
    public void testEmptyLines() {
        List<String> lines = new ArrayList<String>();
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        assertNotNull(gfw.terminator);
    }

    /**
     * Test encoding detection when ANSEL is explicitly asked for
     * 
     * @throws IOException
     *             if anything goes wrong with the writing of the data
     */
    @Test
    public void testEncodingDetectionAnselExplicit() throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR ANSEL");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.write(new NullOutputStream());
        assertTrue(gfw.encodingSpecificWriter instanceof AnselWriter);
    }

    /**
     * Test encoding detection for ASCII
     * 
     * @throws IOException
     *             if anything goes wrong with the writing of the data
     */
    @Test
    public void testEncodingDetectionAscii() throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR ASCII");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.useLittleEndianForUnicode = true;
        gfw.write(new NullOutputStream());
        assertTrue(gfw.encodingSpecificWriter instanceof AsciiWriter);
        // Changing little-endian flag should have no effect since it's not unicode
        gfw.useLittleEndianForUnicode = false;
        gfw.write(new NullOutputStream());
        assertTrue(gfw.encodingSpecificWriter instanceof AsciiWriter);
    }

    /**
     * Test encoding detection when no format is explicitly asked for
     * 
     * @throws IOException
     */
    @Test
    public void testEncodingDetectionDefault() throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.write(new NullOutputStream());
        assertTrue(gfw.encodingSpecificWriter instanceof AnselWriter);
        // Changing little-endian flag should have no effect since it's not unicode
        gfw.useLittleEndianForUnicode = false;
        gfw.write(new NullOutputStream());
        assertTrue(gfw.encodingSpecificWriter instanceof AnselWriter);
    }

    /**
     * Test encoding detection for UNICODE
     * 
     * @throws IOException
     */
    @Test
    public void testEncodingDetectionUnicode() throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR UNICODE");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.write(new NullOutputStream());
        assertTrue(gfw.encodingSpecificWriter instanceof UnicodeLittleEndianWriter);
        gfw.useLittleEndianForUnicode = false;
        gfw.write(new NullOutputStream());
        assertTrue(gfw.encodingSpecificWriter instanceof UnicodeBigEndianWriter);
    }

    /**
     * Test encoding detection for UTF-8
     * 
     * @throws IOException
     */
    @Test
    public void testEncodingDetectionUtf8() throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR UTF-8");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.write(new NullOutputStream());
        assertTrue(gfw.encodingSpecificWriter instanceof Utf8Writer);
        // Changing little-endian flag should have no effect since it's not unicode
        gfw.useLittleEndianForUnicode = false;
        gfw.write(new NullOutputStream());
        assertTrue(gfw.encodingSpecificWriter instanceof Utf8Writer);
    }

    /**
     * Test writing out ANSEL bytes with CRLF line terminators. Includes an unmappable character in line 3, and a
     * mappable extended character in line 4
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputAnselCrLf() throws IOException {
        List<String> lines = getAnselGedcomLines();
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.terminator = LineTerminator.CRLF;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        byte[] expected = new byte[] { 0x30, 0x20, 0x48, 0x45, 0x41, 0x44, 0x0D,
                0x0A,
                // End of line 1
                0x31, 0x20, 0x43, 0x48, 0x41, 0x52, 0x20, 0x41, 0x4E, 0x53, 0x45, 0x4C, 0x0D,
                0x0A,
                // End of line 2
                0x40, 0x20, 0x75, 0x6E, 0x6D, 0x61, 0x70, 0x70, 0x61, 0x62, 0x6C, (byte) 0xCD, 0x20, 0x69, 0x6E, 0x20, 0x61, 0x6E, 0x73, (byte) 0xCD, 0x6C,
                0x0D, 0x0A,
                // End of line 3
                (byte) 0xA1, 0x20, 0x6D, 0x61, 0x70, 0x70, 0x61, 0x62, 0x6C, (byte) 0xCD, 0x20, 0x69, 0x6E, 0x20, 0x61, 0x6E, 0x73, (byte) 0xCD, 0x6C, 0x0D,
                0x0A,
                // End of line 4
                0x30, 0x20, 0x54, 0x52, 0x4C, 0x52, 0x0D, 0x0A,
        // End of line 5
        };

        assertTrue("Output bytes are not the expected value", Arrays.equals(expected, out.toByteArray()));
    }

    /**
     * Test writing out ANSEL bytes with only CR line terminators. Includes an unmappable character in line 3, and a
     * mappable extended character in line 4
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputAnselCrOnly() throws IOException {
        List<String> lines = getAnselGedcomLines();
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.terminator = LineTerminator.CR_ONLY;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        assertTrue(
                "Output bytes are not the expected value",
                Arrays.equals(new byte[] {
                        // Line 1
                        0x30, 0x20, 0x48, 0x45, 0x41, 0x44,
                        0x0D,
                        // Line 2
                        0x31, 0x20, 0x43, 0x48, 0x41, 0x52, 0x20, 0x41, 0x4E, 0x53, 0x45, 0x4C,
                        0x0D,
                        // Line 3
                        0x40, 0x20, 0x75, 0x6E, 0x6D, 0x61, 0x70, 0x70, 0x61, 0x62, 0x6C, (byte) 0xCD, 0x20, 0x69, 0x6E, 0x20, 0x61, 0x6E, 0x73, (byte) 0xCD,
                        0x6C, 0x0D,
                        // Line 4
                        (byte) 0xA1, 0x20, 0x6D, 0x61, 0x70, 0x70, 0x61, 0x62, 0x6C, (byte) 0xCD, 0x20, 0x69, 0x6E, 0x20, 0x61, 0x6E, 0x73, (byte) 0xCD, 0x6C,
                        0x0D,
                        // Line 5
                        0x30, 0x20, 0x54, 0x52, 0x4C, 0x52, 0x0D

                }, out.toByteArray()));
    }

    /**
     * Test writing out ANSEL bytes with LFCR line terminators. Includes an unmappable character in line 3, and a
     * mappable extended character in line 4
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputAnselLfCr() throws IOException {
        List<String> lines = getAnselGedcomLines();
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.terminator = LineTerminator.LFCR;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        byte[] expected = new byte[] {
                // Line 1
                0x30, 0x20, 0x48, 0x45, 0x41, 0x44, 0x0A,
                0x0D,
                // Line 2
                0x31, 0x20, 0x43, 0x48, 0x41, 0x52, 0x20, 0x41, 0x4E, 0x53, 0x45, 0x4C, 0x0A,
                0x0D,
                // Line 3
                0x40, 0x20, 0x75, 0x6E, 0x6D, 0x61, 0x70, 0x70, 0x61, 0x62, 0x6C, (byte) 0xCD, 0x20, 0x69, 0x6E, 0x20, 0x61, 0x6E, 0x73, (byte) 0xCD, 0x6C,
                0x0A, 0x0D,
                // Line 4
                (byte) 0xA1, 0x20, 0x6D, 0x61, 0x70, 0x70, 0x61, 0x62, 0x6C, (byte) 0xCD, 0x20, 0x69, 0x6E, 0x20, 0x61, 0x6E, 0x73, (byte) 0xCD, 0x6C, 0x0A,
                0x0D,
                // Line 5
                0x30, 0x20, 0x54, 0x52, 0x4C, 0x52, 0x0A, 0x0D };
        assertTrue("Output bytes are not the expected value", Arrays.equals(expected, out.toByteArray()));
    }

    /**
     * Test writing out ANSEL bytes with LF line terminators. Includes an unmappable character in line 3, and a mappable
     * extended character in line 4
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputAnselLfOnly() throws IOException {
        List<String> lines = getAnselGedcomLines();
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.terminator = LineTerminator.LF_ONLY;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        assertTrue(
                "Output bytes are not the expected value",
                Arrays.equals(new byte[] {
                        // Line 1
                        0x30, 0x20, 0x48, 0x45, 0x41, 0x44,
                        0x0A,
                        // Line 2
                        0x31, 0x20, 0x43, 0x48, 0x41, 0x52, 0x20, 0x41, 0x4E, 0x53, 0x45, 0x4C,
                        0x0A,
                        // Line 3
                        0x40, 0x20, 0x75, 0x6E, 0x6D, 0x61, 0x70, 0x70, 0x61, 0x62, 0x6C, (byte) 0xCD, 0x20, 0x69, 0x6E, 0x20, 0x61, 0x6E, 0x73, (byte) 0xCD,
                        0x6C, 0x0A,
                        // Line 4
                        (byte) 0xA1, 0x20, 0x6D, 0x61, 0x70, 0x70, 0x61, 0x62, 0x6C, (byte) 0xCD, 0x20, 0x69, 0x6E, 0x20, 0x61, 0x6E, 0x73, (byte) 0xCD, 0x6C,
                        0x0A,
                        // Line 5
                        0x30, 0x20, 0x54, 0x52, 0x4C, 0x52, 0x0A }, out.toByteArray()));
    }

    /**
     * Test writing out ASCII bytes with CRLF line terminators. Includes an unmappable character in line 3.
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputAsciiCrLf() throws IOException {
        List<String> lines = getAsciiGedcomLines();
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.terminator = LineTerminator.CRLF;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        byte[] expected = new byte[] { 0x30, 0x20, 0x48, 0x45, 0x41, 0x44, 0x0D, 0x0A,
                // End of line 1
                0x31, 0x20, 0x43, 0x48, 0x41, 0x52, 0x20, 0x41, 0x53, 0x43, 0x49, 0x49, 0x0D, 0x0A,
                // End of line 2
                /*
                 * The unmappable character , shown here as a question mark
                 */
                0x3F, 0x20, 0x69, 0x73, 0x20, 0x75, 0x6E, 0x6D, 0x61, 0x70, 0x70, 0x61, 0x62, 0x6C, 0x65, 0x20, 0x69, 0x6E, 0x20, 0x61, 0x73, 0x63, 0x69, 0x69,
                0x0D, 0x0A,
                // End of line 3
                0x30, 0x20, 0x54, 0x52, 0x4C, 0x52, 0x0D, 0x0A,
        // End of line 4
        };

        assertTrue("Output bytes are not the expected value", Arrays.equals(expected, out.toByteArray()));
    }

    /**
     * Test writing out ASCII bytes with CR-only line terminators. Includes an unmappable character in line 3.
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputAsciiCrOnly() throws IOException {
        List<String> lines = getAsciiGedcomLines();
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.terminator = LineTerminator.CR_ONLY;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        assertTrue(
                "Output bytes are not the expected value",
                Arrays.equals(new byte[] { 0x30, 0x20, 0x48, 0x45, 0x41, 0x44, 0x0D,
                        // End of line 1
                        0x31, 0x20, 0x43, 0x48, 0x41, 0x52, 0x20, 0x41, 0x53, 0x43, 0x49, 0x49, 0x0D,
                        // End of line 2
                        /*
                         * The unmappable character , shown here as a question mark
                         */
                        0x3F, 0x20, 0x69, 0x73, 0x20, 0x75, 0x6E, 0x6D, 0x61, 0x70, 0x70, 0x61, 0x62, 0x6C, 0x65, 0x20, 0x69, 0x6E, 0x20, 0x61, 0x73, 0x63,
                        0x69, 0x69, 0x0D,
                        // End of line
                        0x30, 0x20, 0x54, 0x52, 0x4C, 0x52, 0x0D,
                // End of line
                        }, out.toByteArray()));
    }

    /**
     * Test writing out ASCII bytes with LFCR line terminators. Includes an unmappable character in line 3.
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputAsciiLfCr() throws IOException {
        List<String> lines = getAsciiGedcomLines();
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.terminator = LineTerminator.LFCR;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        byte[] expected = new byte[] { 0x30, 0x20, 0x48, 0x45, 0x41, 0x44, 0x0A, 0x0D,
                // End of line 1
                0x31, 0x20, 0x43, 0x48, 0x41, 0x52, 0x20, 0x41, 0x53, 0x43, 0x49, 0x49, 0x0A, 0x0D,
                // End of line 2
                /*
                 * The unmappable character , shown here as a question mark
                 */
                0x3F, 0x20, 0x69, 0x73, 0x20, 0x75, 0x6E, 0x6D, 0x61, 0x70, 0x70, 0x61, 0x62, 0x6C, 0x65, 0x20, 0x69, 0x6E, 0x20, 0x61, 0x73, 0x63, 0x69, 0x69,
                0x0A, 0x0D,
                // End of line 3
                0x30, 0x20, 0x54, 0x52, 0x4C, 0x52, 0x0A, 0x0D,
        // End of line 4
        };

        assertTrue("Output bytes are not the expected value", Arrays.equals(expected, out.toByteArray()));
    }

    /**
     * Test writing out ASCII bytes with LF-only line terminators. Includes an unmappable character in line 3.
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputAsciiLfOnly() throws IOException {
        List<String> lines = getAsciiGedcomLines();
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.terminator = LineTerminator.LF_ONLY;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        assertTrue(
                "Output bytes are not the expected value",
                Arrays.equals(new byte[] { 0x30, 0x20, 0x48, 0x45, 0x41, 0x44, 0x0A,
                        // End of line 1
                        0x31, 0x20, 0x43, 0x48, 0x41, 0x52, 0x20, 0x41, 0x53, 0x43, 0x49, 0x49, 0x0A,
                        // End of line 2
                        /*
                         * The unmappable character , shown here as a question mark
                         */
                        0x3F, 0x20, 0x69, 0x73, 0x20, 0x75, 0x6E, 0x6D, 0x61, 0x70, 0x70, 0x61, 0x62, 0x6C, 0x65, 0x20, 0x69, 0x6E, 0x20, 0x61, 0x73, 0x63,
                        0x69, 0x69, 0x0A,
                        // End of line 3
                        0x30, 0x20, 0x54, 0x52, 0x4C, 0x52, 0x0A,
                // End of line 4
                        }, out.toByteArray()));
    }

    /**
     * Test writing out big-endian unicode bytes, using CR-only line terminators
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputUnicodeBigEndianCrLF() throws IOException {
        List<String> lines = getUnicodeGedcomLines();
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.useLittleEndianForUnicode = false;
        gfw.terminator = LineTerminator.CRLF;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        assertTrue(
                "Output bytes are not the expected value",
                Arrays.equals(new byte[] { 0x00, 0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41, 0x00, 0x44, 0x00, 0x0D, 0x00,
                        0x0A, // End of line 1
                        0x00, 0x31, 0x00, 0x20, 0x00, 0x43, 0x00, 0x48, 0x00, 0x41, 0x00, 0x52, 0x00, 0x20, 0x00, 0x55, 0x00, 0x4E, 0x00, 0x49, 0x00, 0x43,
                        0x00, 0x4F, 0x00, 0x44, 0x00, 0x45, 0x00, 0x0D, 0x00, 0x0A,
                        // End of line 2
                        0x00, 0x41, 0x00, (byte) 0xC4, // Capital A, Capital A-umlaut
                        0x00, 0x61, 0x00, (byte) 0xE4, // lowercase a, lowercase a-umlaut
                        0x00, 0x0D, 0x00, 0x0A,
                        // End of line 3
                        0x00, 0x30, 0x00, 0x20, 0x00, 0x54, 0x00, 0x52, 0x00, 0x4C, 0x00, 0x52, 0x00, 0x0D, 0x00, 0x0A,
                // End of line 4
                        }, out.toByteArray()));
    }

    /**
     * Test writing out big-endian unicode bytes, using CR-only line terminators
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputUnicodeBigEndianCrOnly() throws IOException {
        List<String> lines = getUnicodeGedcomLines();
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.useLittleEndianForUnicode = false;
        gfw.terminator = LineTerminator.CR_ONLY;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        assertTrue(
                "Output bytes are not the expected value",
                Arrays.equals(new byte[] { 0x00, 0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41, 0x00, 0x44, 0x00, 0x0D,
                        // End of line 1
                        0x00, 0x31, 0x00, 0x20, 0x00, 0x43, 0x00, 0x48, 0x00, 0x41, 0x00, 0x52, 0x00, 0x20, 0x00, 0x55, 0x00, 0x4E, 0x00, 0x49, 0x00, 0x43,
                        0x00, 0x4F, 0x00, 0x44, 0x00, 0x45, 0x00, 0x0D,
                        // End of line 2
                        0x00, 0x41, 0x00, (byte) 0xC4, // Capital A, Capital A-umlaut
                        0x00, 0x61, 0x00, (byte) 0xE4, // lowercase a, lowercase a-umlaut
                        0x00, 0x0D,
                        // End of line 3
                        0x00, 0x30, 0x00, 0x20, 0x00, 0x54, 0x00, 0x52, 0x00, 0x4C, 0x00, 0x52, 0x00, 0x0D,
                // End of line 4
                        }, out.toByteArray()));
    }

    /**
     * Test writing out big-endian unicode bytes, using LFCR line terminators.
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputUnicodeBigEndianLfCr() throws IOException {
        List<String> lines = getUnicodeGedcomLines();
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.useLittleEndianForUnicode = false;
        gfw.terminator = LineTerminator.LFCR;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        byte[] expected = new byte[] { 0x00, 0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41, 0x00, 0x44, 0x00, 0x0A, 0x00, 0x0D,
                // End of line 1
                0x00, 0x31, 0x00, 0x20, 0x00, 0x43, 0x00, 0x48, 0x00, 0x41, 0x00, 0x52, 0x00, 0x20, 0x00, 0x55, 0x00, 0x4E, 0x00, 0x49, 0x00, 0x43, 0x00, 0x4F,
                0x00, 0x44, 0x00, 0x45, 0x00, 0x0A, 0x00, 0x0D,
                // End of line 2
                0x00, 0x41, 0x00, (byte) 0xC4, // Capital A, Capital A-umlaut
                0x00, 0x61, 0x00, (byte) 0xE4, // lowercase a, lowercase a-umlaut
                0x00, 0x0A, 0x00, 0x0D, // LFCR
                // End of line 3
                0x00, 0x30, 0x00, 0x20, 0x00, 0x54, 0x00, 0x52, 0x00, 0x4C, 0x00, 0x52, 0x00, 0x0A, 0x00, 0x0D,
        // End of line 4
        };
        assertTrue("Output bytes are not the expected value", Arrays.equals(expected, out.toByteArray()));
    }

    /**
     * Test writing out big-endian unicode bytes, using CR-only line terminators
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputUnicodeBigEndianLfOnly() throws IOException {
        List<String> lines = getUnicodeGedcomLines();
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.useLittleEndianForUnicode = false;
        gfw.terminator = LineTerminator.LF_ONLY;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        assertTrue(
                "Output bytes are not the expected value",
                Arrays.equals(new byte[] { 0x00, 0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41, 0x00, 0x44, 0x00, 0x0A,
                        // End of line 1
                        0x00, 0x31, 0x00, 0x20, 0x00, 0x43, 0x00, 0x48, 0x00, 0x41, 0x00, 0x52, 0x00, 0x20, 0x00, 0x55, 0x00, 0x4E, 0x00, 0x49, 0x00, 0x43,
                        0x00, 0x4F, 0x00, 0x44, 0x00, 0x45, 0x00, 0x0A,
                        // End of line 2
                        0x00, 0x41, 0x00, (byte) 0xC4, // Capital A, Capital A-umlaut
                        0x00, 0x61, 0x00, (byte) 0xE4, // lowercase a, lowercase a-umlaut
                        0x00, 0x0A, // LF
                        // End of line 3
                        0x00, 0x30, 0x00, 0x20, 0x00, 0x54, 0x00, 0x52, 0x00, 0x4C, 0x00, 0x52, 0x00, 0x0A,
                // End of line 4
                        }, out.toByteArray()));
    }

    /**
     * Test writing out little-endian unicode bytes, using CRLF line terminators
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputUnicodeLittleEndianCrLf() throws IOException {
        List<String> lines = getUnicodeGedcomLines();
        GedcomFileWriter gfw = new GedcomFileWriter(lines);

        // Not necessary, little endian is default, but good for explicitness
        gfw.useLittleEndianForUnicode = true;
        gfw.terminator = LineTerminator.CRLF;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        byte[] expected = new byte[] { 0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41, 0x00, 0x44, 0x00, 0x0D, 0x00, 0x0A, 0x00,
                // End of line 1
                0x31, 0x00, 0x20, 0x00, 0x43, 0x00, 0x48, 0x00, 0x41, 0x00, 0x52, 0x00, 0x20, 0x00, 0x55, 0x00, 0x4E, 0x00, 0x49, 0x00, 0x43, 0x00, 0x4F, 0x00,
                0x44, 0x00, 0x45, 0x00, 0x0D, 0x00, 0x0A, 0x00,
                // End of line 2
                0x41, 0x00, (byte) 0xC4, 0x00, // Capital A, Capital A-umlaut
                0x61, 0x00, (byte) 0xE4, 0x00, // lowercase a, lowercase a-umlaut
                0x0D, 0x00, 0x0A, 0x00, // CRLF
                // End of line 3
                0x30, 0x00, 0x20, 0x00, 0x54, 0x00, 0x52, 0x00, 0x4C, 0x00, 0x52, 0x00, 0x0D, 0x00, 0x0A, 0x00
        // End of line 4
        };
        assertTrue("Output bytes are not the expected value", Arrays.equals(expected, out.toByteArray()));
    }

    /**
     * Test writing out little-endian unicode bytes, using CR only line terminators
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputUnicodeLittleEndianCrOnly() throws IOException {
        List<String> lines = getUnicodeGedcomLines();
        GedcomFileWriter gfw = new GedcomFileWriter(lines);

        // Not necessary, little endian is default, but good for explicitness
        gfw.useLittleEndianForUnicode = true;
        gfw.terminator = LineTerminator.CR_ONLY;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        byte[] expected = new byte[] { 0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41, 0x00, 0x44, 0x00, 0x0D, 0x00,
                // End of line 1
                0x31, 0x00, 0x20, 0x00, 0x43, 0x00, 0x48, 0x00, 0x41, 0x00, 0x52, 0x00, 0x20, 0x00, 0x55, 0x00, 0x4E, 0x00, 0x49, 0x00, 0x43, 0x00, 0x4F, 0x00,
                0x44, 0x00, 0x45, 0x00, 0x0D, 0x00,
                // End of line 2
                0x41, 0x00, (byte) 0xC4, 0x00, // Capital A, Capital A-umlaut
                0x61, 0x00, (byte) 0xE4, 0x00, // lowercase a, lowercase a-umlaut
                0x0D, 0x00, // CR
                // End of line 3
                0x30, 0x00, 0x20, 0x00, 0x54, 0x00, 0x52, 0x00, 0x4C, 0x00, 0x52, 0x00, 0x0D, 0x00
        // End of line 4
        };
        assertTrue("Output bytes are not the expected value", Arrays.equals(expected, out.toByteArray()));
    }

    /**
     * Test writing out little-endian unicode bytes, using LFCR line terminators
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputUnicodeLittleEndianLfCr() throws IOException {
        List<String> lines = getUnicodeGedcomLines();
        GedcomFileWriter gfw = new GedcomFileWriter(lines);

        // Not necessary, little endian is default, but good for explicitness
        gfw.useLittleEndianForUnicode = true;
        gfw.terminator = LineTerminator.LFCR;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        byte[] expected = new byte[] { 0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41, 0x00, 0x44, 0x00, 0x0A, 0x00, 0x0D, 0x00,
                // End of line 1
                0x31, 0x00, 0x20, 0x00, 0x43, 0x00, 0x48, 0x00, 0x41, 0x00, 0x52, 0x00, 0x20, 0x00, 0x55, 0x00, 0x4E, 0x00, 0x49, 0x00, 0x43, 0x00, 0x4F, 0x00,
                0x44, 0x00, 0x45, 0x00, 0x0A, 0x00, 0x0D, 0x00,
                // End of line 2
                0x41, 0x00, (byte) 0xC4, 0x00, // Capital A, Capital A-umlaut
                0x61, 0x00, (byte) 0xE4, 0x00, // lowercase a, lowercase a-umlaut
                0x0A, 0x00, 0x0D, 0x00, // LFCR
                // End of line 3
                0x30, 0x00, 0x20, 0x00, 0x54, 0x00, 0x52, 0x00, 0x4C, 0x00, 0x52, 0x00, 0x0A, 0x00, 0x0D, 0x00
        // End of line 4
        };
        assertTrue("Output bytes are not the expected value", Arrays.equals(expected, out.toByteArray()));
    }

    /**
     * Test writing out little-endian unicode bytes, using LF only line terminators
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputUnicodeLittleEndianLfOnly() throws IOException {
        List<String> lines = getUnicodeGedcomLines();
        GedcomFileWriter gfw = new GedcomFileWriter(lines);

        // Not necessary, little endian is default, but good for explicitness
        gfw.useLittleEndianForUnicode = true;
        gfw.terminator = LineTerminator.LF_ONLY;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        byte[] expected = new byte[] { 0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41, 0x00, 0x44, 0x00, 0x0A, 0x00,
                // End of line 1
                0x31, 0x00, 0x20, 0x00, 0x43, 0x00, 0x48, 0x00, 0x41, 0x00, 0x52, 0x00, 0x20, 0x00, 0x55, 0x00, 0x4E, 0x00, 0x49, 0x00, 0x43, 0x00, 0x4F, 0x00,
                0x44, 0x00, 0x45, 0x00, 0x0A, 0x00,
                // End of line 2
                0x41, 0x00, (byte) 0xC4, 0x00, // Capital A, Capital A-umlaut
                0x61, 0x00, (byte) 0xE4, 0x00, // lowercase a, lowercase a-umlaut
                0x0A, 0x00,
                // End of line 3
                0x30, 0x00, 0x20, 0x00, 0x54, 0x00, 0x52, 0x00, 0x4C, 0x00, 0x52, 0x00, 0x0A, 0x00
        // End of line 4
        };
        assertTrue("Output bytes are not the expected value", Arrays.equals(expected, out.toByteArray()));
    }

    /**
     * Test writing out UTF_8 bytes with CRLF line terminators.
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputUtf8CrLf() throws IOException {
        List<String> lines = getUtf8GedcomLines();
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.terminator = LineTerminator.CRLF;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        assertTrue("Output bytes are not the expected value", Arrays.equals(new byte[] { 0x30, 0x20, 0x48, 0x45, 0x41, 0x44, 0x0D, 0x0A,
                // End of line 1
                0x31, 0x20, 0x43, 0x48, 0x41, 0x52, 0x20, 0x55, 0x54, 0x46, 0x2D, 0x38, 0x0D, 0x0A,
                // End of line 2
                0x41, (byte) 0xC3, (byte) 0x84, // Capital A, Capital A-umlaut
                0x61, (byte) 0xC3, (byte) 0xA4, // lowercase a, lowercase a-umlaut
                0x0D, 0x0A,
                // End of line 3
                0x30, 0x20, 0x54, 0x52, 0x4C, 0x52, 0x0D, 0x0A
        // End of line 4
                }, out.toByteArray()));
    }

    /**
     * Test writing out UTF_8 bytes with only CR line terminators.
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputUtf8CrOnly() throws IOException {
        List<String> lines = getUtf8GedcomLines();
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.terminator = LineTerminator.CR_ONLY;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        assertTrue("Output bytes are not the expected value", Arrays.equals(new byte[] { 0x30, 0x20, 0x48, 0x45, 0x41, 0x44, 0x0D,
                // End of line 1
                0x31, 0x20, 0x43, 0x48, 0x41, 0x52, 0x20, 0x55, 0x54, 0x46, 0x2D, 0x38, 0x0D,
                // End of line 2
                0x41, (byte) 0xC3, (byte) 0x84, // Capital A, Capital A-umlaut
                0x61, (byte) 0xC3, (byte) 0xA4, // lowercase a, lowercase a-umlaut
                0x0D,
                // End of line 3
                0x30, 0x20, 0x54, 0x52, 0x4C, 0x52, 0x0D
        // End of line 4
                }, out.toByteArray()));
    }

    /**
     * Test writing out UTF_8 bytes with LFCR line terminators. 4
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputUtf8LfCr() throws IOException {
        List<String> lines = getUtf8GedcomLines();
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.terminator = LineTerminator.LFCR;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        assertTrue("Output bytes are not the expected value", Arrays.equals(new byte[] { 0x30, 0x20, 0x48, 0x45, 0x41, 0x44, 0x0A, 0x0D,
                // End of line 1
                0x31, 0x20, 0x43, 0x48, 0x41, 0x52, 0x20, 0x55, 0x54, 0x46, 0x2D, 0x38, 0x0A, 0x0D,
                // End of line 2
                0x41, (byte) 0xC3, (byte) 0x84, // Capital A, Capital A-umlaut
                0x61, (byte) 0xC3, (byte) 0xA4, // lowercase a, lowercase a-umlaut
                0x0A, 0x0D,
                // End of line 3
                0x30, 0x20, 0x54, 0x52, 0x4C, 0x52, 0x0A, 0x0D
        // End of line 4
                }, out.toByteArray()));
    }

    /**
     * Test writing out UTF_8 bytes with LF line terminators.
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputUtf8LfOnly() throws IOException {
        List<String> lines = getUtf8GedcomLines();
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.terminator = LineTerminator.LF_ONLY;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        assertTrue("Output bytes are not the expected value", Arrays.equals(new byte[] { 0x30, 0x20, 0x48, 0x45, 0x41, 0x44, 0x0A,
                // End of line 1
                0x31, 0x20, 0x43, 0x48, 0x41, 0x52, 0x20, 0x55, 0x54, 0x46, 0x2D, 0x38, 0x0A,
                // End of line 2
                0x41, (byte) 0xC3, (byte) 0x84, // Capital A, Capital A-umlaut
                0x61, (byte) 0xC3, (byte) 0xA4, // lowercase a, lowercase a-umlaut
                0x0A,
                // End of line 3
                0x30, 0x20, 0x54, 0x52, 0x4C, 0x52, 0x0A
        // End of line 4
                }, out.toByteArray()));
    }

    /**
     * Test writing a gedcom to a file by its filename
     * 
     * @throws GedcomWriterException
     *             if there is a writing failure
     * @throws IOException
     *             if there is an io failure
     */
    @Test
    public void testWriteFileWithName() throws IOException, GedcomWriterException {
        String fn = "tmp/gedcomfilewritertest.ged";
        Gedcom g = new Gedcom();
        g.submission = new Submission("@SUBN0001@");
        g.header.submission = g.submission;
        Submitter s = new Submitter();
        s.xref = "@SUBM0001@";
        s.name = new StringWithCustomTags("Joe Tester");
        g.submitters.put(s.xref, s);
        g.header.submitter = s;
        GedcomWriter gw = new GedcomWriter(g);
        gw.write(fn);
    }

    /**
     * Get a List of strings representing GEDCOM text for an ANSEL-encoded file. Note that the lines do NOT represent a
     * well-formed GEDCOM, but we're only testing the encoding.
     * 
     * @return a list of strings representing GEDCOM text for a ANSEL-encoded file
     */
    private List<String> getAnselGedcomLines() {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR ANSEL");
        lines.add("\u0140 unmappable in ansel");
        lines.add("\u0141 mappable in ansel");
        lines.add("0 TRLR");
        return lines;
    }

    /**
     * Get a List of strings representing GEDCOM text for an ASCII-encoded file. Note that the lines do NOT represent a
     * well-formed GEDCOM, but we're only testing the encoding.
     * 
     * @return a list of strings representing GEDCOM text for an ASCII-encoded file
     */
    private List<String> getAsciiGedcomLines() {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR ASCII");
        lines.add("\u0141 is unmappable in ascii");
        lines.add("0 TRLR");
        return lines;
    }

    /**
     * Get a List of strings representing GEDCOM text for a Unicode-encoded file. Note that the lines do NOT represent a
     * well-formed GEDCOM, but we're only testing the encoding.
     * 
     * @return a list of strings representing GEDCOM text for a Unicode-encoded file
     */
    private List<String> getUnicodeGedcomLines() {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR UNICODE");
        lines.add("A\u00C4a\u00E4"); // Capital A, capital A-umlaut, lowercase a, lowercase a-umlaut
        lines.add("0 TRLR");
        return lines;
    }

    /**
     * Get a List of strings representing GEDCOM text for a UTF-8-encoded file. Note that the lines do NOT represent a
     * well-formed GEDCOM, but we're only testing the encoding.
     * 
     * @return a list of strings representing GEDCOM text for a UTF-8-encoded file
     */
    private List<String> getUtf8GedcomLines() {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR UTF-8");
        lines.add("A\u00C4a\u00E4"); // Capital A, capital A-umlaut, lowercase a, lowercase a-umlaut
        lines.add("0 TRLR");
        return lines;
    }

}
