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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.mattharrah.gedcom4j.io.GedcomFileWriter.LineTerminator;

/**
 * @author frizbog1
 * 
 */
public class GedcomFileWriterTest {

    /**
     * Test when there is no data
     */
    @Test
    public void testEmptyLines() {
        List<String> lines = new ArrayList<String>();
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        assertNotNull(gfw.encoding);
        assertNotNull(gfw.terminator);
    }

    /**
     * Test encoding detection when ANSEL is explicitly asked for
     */
    @Test
    public void testEncodingDetectionAnselExplicit() {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR ANSEL");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        assertEquals(Encoding.ANSEL, gfw.encoding);
    }

    /**
     * Test encoding detection for ASCII
     */
    @Test
    public void testEncodingDetectionAscii() {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR ASCII");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        assertEquals(Encoding.ASCII, gfw.encoding);
        gfw.setLittleEndianForUnicode(false);
        assertEquals(
                "Changing little-endian flag should have no effect since it's not unicode",
                Encoding.ASCII, gfw.encoding);
    }

    /**
     * Test encoding detection when no format is explicitly asked for
     */
    @Test
    public void testEncodingDetectionDefault() {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        assertEquals(Encoding.ANSEL, gfw.encoding);
        gfw.setLittleEndianForUnicode(false);
        assertEquals(
                "Changing little-endian flag should have no effect since it's not unicode",
                Encoding.ANSEL, gfw.encoding);
    }

    /**
     * Test encoding detection for UNICODE
     */
    @Test
    public void testEncodingDetectionUnicode() {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR UNICODE");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        assertEquals("Always defaults to little endian",
                Encoding.UNICODE_LITTLE_ENDIAN, gfw.encoding);
        gfw.setLittleEndianForUnicode(false);
        assertEquals("Changing flag should change encoding",
                Encoding.UNICODE_BIG_ENDIAN, gfw.encoding);
    }

    /**
     * Test writing out ANSEL bytes with CRLF line terminators. Includes an
     * unmappable character in line 3, and a mappable extended character in line
     * 4
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputAnselCrLf() throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR ANSEL");
        lines.add("\u0140 unmappable in ansel");
        lines.add("\u0141 mappable in ansel");
        lines.add("0 TRLR");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.terminator = LineTerminator.CRLF;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        byte[] expected = new byte[] { (byte) 0x30, (byte) 0x20, (byte) 0x48,
                (byte) 0x45, (byte) 0x41, (byte) 0x44, (byte) 0x0D,
                (byte) 0x0A, (byte) 0x31, (byte) 0x20, (byte) 0x43,
                (byte) 0x48, (byte) 0x41, (byte) 0x52, (byte) 0x20,
                (byte) 0x41, (byte) 0x4E, (byte) 0x53, (byte) 0x45,
                (byte) 0x4C, (byte) 0x0D, (byte) 0x0A, (byte) 0x40,
                (byte) 0x20, (byte) 0x75, (byte) 0x6E, (byte) 0x6D,
                (byte) 0x61, (byte) 0x70, (byte) 0x70, (byte) 0x61,
                (byte) 0x62, (byte) 0x6C, (byte) 0x65, (byte) 0x20,
                (byte) 0x69, (byte) 0x6E, (byte) 0x20, (byte) 0x61,
                (byte) 0x6E, (byte) 0x73, (byte) 0x65, (byte) 0x6C,
                (byte) 0x0D, (byte) 0x0A, (byte) 0xA1, (byte) 0x20,
                (byte) 0x6D, (byte) 0x61, (byte) 0x70, (byte) 0x70,
                (byte) 0x61, (byte) 0x62, (byte) 0x6C, (byte) 0x65,
                (byte) 0x20, (byte) 0x69, (byte) 0x6E, (byte) 0x20,
                (byte) 0x61, (byte) 0x6E, (byte) 0x73, (byte) 0x65,
                (byte) 0x6C, (byte) 0x0D, (byte) 0x0A, (byte) 0x30,
                (byte) 0x20, (byte) 0x54, (byte) 0x52, (byte) 0x4C,
                (byte) 0x52, (byte) 0x0D, (byte) 0x0A, };
        assertTrue("Output bytes are not the expected value",
                Arrays.equals(expected, out.toByteArray()));
    }

    /**
     * Test writing out ANSEL bytes with only CR line terminators. Includes an
     * unmappable character in line 3, and a mappable extended character in line
     * 4
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputAnselCrOnly() throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR ANSEL");
        lines.add("\u0140 unmappable in ansel");
        lines.add("\u0141 mappable in ansel");
        lines.add("0 TRLR");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.terminator = LineTerminator.CR_ONLY;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        assertTrue(
                "Output bytes are not the expected value",
                Arrays.equals(new byte[] { (byte) 0x30, (byte) 0x20,
                        (byte) 0x48, (byte) 0x45, (byte) 0x41, (byte) 0x44,
                        (byte) 0x0D, (byte) 0x31, (byte) 0x20, (byte) 0x43,
                        (byte) 0x48, (byte) 0x41, (byte) 0x52, (byte) 0x20,
                        (byte) 0x41, (byte) 0x4E, (byte) 0x53, (byte) 0x45,
                        (byte) 0x4C, (byte) 0x0D, (byte) 0x40, (byte) 0x20,
                        (byte) 0x75, (byte) 0x6E, (byte) 0x6D, (byte) 0x61,
                        (byte) 0x70, (byte) 0x70, (byte) 0x61, (byte) 0x62,
                        (byte) 0x6C, (byte) 0x65, (byte) 0x20, (byte) 0x69,
                        (byte) 0x6E, (byte) 0x20, (byte) 0x61, (byte) 0x6E,
                        (byte) 0x73, (byte) 0x65, (byte) 0x6C, (byte) 0x0D,
                        (byte) 0xA1, (byte) 0x20, (byte) 0x6D, (byte) 0x61,
                        (byte) 0x70, (byte) 0x70, (byte) 0x61, (byte) 0x62,
                        (byte) 0x6C, (byte) 0x65, (byte) 0x20, (byte) 0x69,
                        (byte) 0x6E, (byte) 0x20, (byte) 0x61, (byte) 0x6E,
                        (byte) 0x73, (byte) 0x65, (byte) 0x6C, (byte) 0x0D,
                        (byte) 0x30, (byte) 0x20, (byte) 0x54, (byte) 0x52,
                        (byte) 0x4C, (byte) 0x52, (byte) 0x0D, },
                        out.toByteArray()));
    }

    /**
     * Test writing out ANSEL bytes with LFCR line terminators. Includes an
     * unmappable character in line 3, and a mappable extended character in line
     * 4
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputAnselLfCr() throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR ANSEL");
        lines.add("\u0140 unmappable in ansel");
        lines.add("\u0141 mappable in ansel");
        lines.add("0 TRLR");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.terminator = LineTerminator.LFCR;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        byte[] expected = new byte[] { (byte) 0x30, (byte) 0x20, (byte) 0x48,
                (byte) 0x45, (byte) 0x41, (byte) 0x44, (byte) 0x0A,
                (byte) 0x0D, (byte) 0x31, (byte) 0x20, (byte) 0x43,
                (byte) 0x48, (byte) 0x41, (byte) 0x52, (byte) 0x20,
                (byte) 0x41, (byte) 0x4E, (byte) 0x53, (byte) 0x45,
                (byte) 0x4C, (byte) 0x0A, (byte) 0x0D, (byte) 0x40,
                (byte) 0x20, (byte) 0x75, (byte) 0x6E, (byte) 0x6D,
                (byte) 0x61, (byte) 0x70, (byte) 0x70, (byte) 0x61,
                (byte) 0x62, (byte) 0x6C, (byte) 0x65, (byte) 0x20,
                (byte) 0x69, (byte) 0x6E, (byte) 0x20, (byte) 0x61,
                (byte) 0x6E, (byte) 0x73, (byte) 0x65, (byte) 0x6C,
                (byte) 0x0A, (byte) 0x0D, (byte) 0xA1, (byte) 0x20,
                (byte) 0x6D, (byte) 0x61, (byte) 0x70, (byte) 0x70,
                (byte) 0x61, (byte) 0x62, (byte) 0x6C, (byte) 0x65,
                (byte) 0x20, (byte) 0x69, (byte) 0x6E, (byte) 0x20,
                (byte) 0x61, (byte) 0x6E, (byte) 0x73, (byte) 0x65,
                (byte) 0x6C, (byte) 0x0A, (byte) 0x0D, (byte) 0x30,
                (byte) 0x20, (byte) 0x54, (byte) 0x52, (byte) 0x4C,
                (byte) 0x52, (byte) 0x0A, (byte) 0x0D, };
        assertTrue("Output bytes are not the expected value",
                Arrays.equals(expected, out.toByteArray()));
    }

    /**
     * Test writing out ANSEL bytes with LF line terminators. Includes an
     * unmappable character in line 3, and a mappable extended character in line
     * 4
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputAnselLfOnly() throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR ANSEL");
        lines.add("\u0140 unmappable in ansel");
        lines.add("\u0141 mappable in ansel");
        lines.add("0 TRLR");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.terminator = LineTerminator.LF_ONLY;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        assertTrue(
                "Output bytes are not the expected value",
                Arrays.equals(new byte[] { (byte) 0x30, (byte) 0x20,
                        (byte) 0x48, (byte) 0x45, (byte) 0x41, (byte) 0x44,
                        (byte) 0x0A, (byte) 0x31, (byte) 0x20, (byte) 0x43,
                        (byte) 0x48, (byte) 0x41, (byte) 0x52, (byte) 0x20,
                        (byte) 0x41, (byte) 0x4E, (byte) 0x53, (byte) 0x45,
                        (byte) 0x4C, (byte) 0x0A, (byte) 0x40, (byte) 0x20,
                        (byte) 0x75, (byte) 0x6E, (byte) 0x6D, (byte) 0x61,
                        (byte) 0x70, (byte) 0x70, (byte) 0x61, (byte) 0x62,
                        (byte) 0x6C, (byte) 0x65, (byte) 0x20, (byte) 0x69,
                        (byte) 0x6E, (byte) 0x20, (byte) 0x61, (byte) 0x6E,
                        (byte) 0x73, (byte) 0x65, (byte) 0x6C, (byte) 0x0A,
                        (byte) 0xA1, (byte) 0x20, (byte) 0x6D, (byte) 0x61,
                        (byte) 0x70, (byte) 0x70, (byte) 0x61, (byte) 0x62,
                        (byte) 0x6C, (byte) 0x65, (byte) 0x20, (byte) 0x69,
                        (byte) 0x6E, (byte) 0x20, (byte) 0x61, (byte) 0x6E,
                        (byte) 0x73, (byte) 0x65, (byte) 0x6C, (byte) 0x0A,
                        (byte) 0x30, (byte) 0x20, (byte) 0x54, (byte) 0x52,
                        (byte) 0x4C, (byte) 0x52, (byte) 0x0A, },
                        out.toByteArray()));
    }

    /**
     * Test writing out ASCII bytes with CRLF line terminators. Includes an
     * unmappable character in line 3.
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputAsciiCrLf() throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR ASCII");
        lines.add("\u0141 is unmappable in ascii");
        lines.add("0 TRLR");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.terminator = LineTerminator.CRLF;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        byte[] expected = new byte[] { (byte) 0x30, (byte) 0x20, (byte) 0x48,
                (byte) 0x45, (byte) 0x41, (byte) 0x44, (byte) 0x0D,
                (byte) 0x0A, (byte) 0x31, (byte) 0x20, (byte) 0x43,
                (byte) 0x48, (byte) 0x41, (byte) 0x52, (byte) 0x20,
                (byte) 0x41, (byte) 0x53, (byte) 0x43, (byte) 0x49,
                (byte) 0x49, (byte) 0x0D, (byte) 0x0A,
                /*
                 * The unmappable character , shown here as a question mark
                 */(byte) 0x3F, (byte) 0x20, (byte) 0x69, (byte) 0x73,
                (byte) 0x20, (byte) 0x75, (byte) 0x6E, (byte) 0x6D,
                (byte) 0x61, (byte) 0x70, (byte) 0x70, (byte) 0x61,
                (byte) 0x62, (byte) 0x6C, (byte) 0x65, (byte) 0x20,
                (byte) 0x69, (byte) 0x6E, (byte) 0x20, (byte) 0x61,
                (byte) 0x73, (byte) 0x63, (byte) 0x69, (byte) 0x69,
                (byte) 0x0D, (byte) 0x0A, (byte) 0x30, (byte) 0x20,
                (byte) 0x54, (byte) 0x52, (byte) 0x4C, (byte) 0x52,
                (byte) 0x0D, (byte) 0x0A, };

        assertTrue("Output bytes are not the expected value",
                Arrays.equals(expected, out.toByteArray()));
    }

    /**
     * Test writing out ASCII bytes with CR-only line terminators. Includes an
     * unmappable character in line 3.
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputAsciiCrOnly() throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR ASCII");
        lines.add("\u0141 is unmappable in ascii");
        lines.add("0 TRLR");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.terminator = LineTerminator.CR_ONLY;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        assertTrue(
                "Output bytes are not the expected value",
                Arrays.equals(new byte[] { (byte) 0x30, (byte) 0x20,
                        (byte) 0x48, (byte) 0x45, (byte) 0x41, (byte) 0x44,
                        (byte) 0x0D, (byte) 0x31, (byte) 0x20, (byte) 0x43,
                        (byte) 0x48, (byte) 0x41, (byte) 0x52, (byte) 0x20,
                        (byte) 0x41, (byte) 0x53, (byte) 0x43, (byte) 0x49,
                        (byte) 0x49, (byte) 0x0D, /*
                                                   * The unmappable character,
                                                   * shown here as a question
                                                   * mark
                                                   */(byte) 0x3F, (byte) 0x20,
                        (byte) 0x69, (byte) 0x73, (byte) 0x20, (byte) 0x75,
                        (byte) 0x6E, (byte) 0x6D, (byte) 0x61, (byte) 0x70,
                        (byte) 0x70, (byte) 0x61, (byte) 0x62, (byte) 0x6C,
                        (byte) 0x65, (byte) 0x20, (byte) 0x69, (byte) 0x6E,
                        (byte) 0x20, (byte) 0x61, (byte) 0x73, (byte) 0x63,
                        (byte) 0x69, (byte) 0x69, (byte) 0x0D, (byte) 0x30,
                        (byte) 0x20, (byte) 0x54, (byte) 0x52, (byte) 0x4C,
                        (byte) 0x52, (byte) 0x0D, }, out.toByteArray()));
    }

    /**
     * Test writing out ASCII bytes with LFCR line terminators. Includes an
     * unmappable character in line 3.
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputAsciiLfCr() throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR ASCII");
        lines.add("\u0141 is unmappable in ascii");
        lines.add("0 TRLR");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.terminator = LineTerminator.LFCR;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        byte[] expected = new byte[] { (byte) 0x30, (byte) 0x20, (byte) 0x48,
                (byte) 0x45, (byte) 0x41, (byte) 0x44, (byte) 0x0A,
                (byte) 0x0D, (byte) 0x31, (byte) 0x20, (byte) 0x43,
                (byte) 0x48, (byte) 0x41, (byte) 0x52, (byte) 0x20,
                (byte) 0x41, (byte) 0x53, (byte) 0x43, (byte) 0x49,
                (byte) 0x49, (byte) 0x0A, (byte) 0x0D,
                /*
                 * The unmappable character , shown here as a question mark
                 */(byte) 0x3F, (byte) 0x20, (byte) 0x69, (byte) 0x73,
                (byte) 0x20, (byte) 0x75, (byte) 0x6E, (byte) 0x6D,
                (byte) 0x61, (byte) 0x70, (byte) 0x70, (byte) 0x61,
                (byte) 0x62, (byte) 0x6C, (byte) 0x65, (byte) 0x20,
                (byte) 0x69, (byte) 0x6E, (byte) 0x20, (byte) 0x61,
                (byte) 0x73, (byte) 0x63, (byte) 0x69, (byte) 0x69,
                (byte) 0x0A, (byte) 0x0D, (byte) 0x30, (byte) 0x20,
                (byte) 0x54, (byte) 0x52, (byte) 0x4C, (byte) 0x52,
                (byte) 0x0A, (byte) 0x0D, };

        assertTrue("Output bytes are not the expected value",
                Arrays.equals(expected, out.toByteArray()));
    }

    /**
     * Test writing out ASCII bytes with LF-only line terminators. Includes an
     * unmappable character in line 3.
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputAsciiLfOnly() throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR ASCII");
        lines.add("\u0141 is unmappable in ascii");
        lines.add("0 TRLR");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.terminator = LineTerminator.LF_ONLY;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        assertTrue(
                "Output bytes are not the expected value",
                Arrays.equals(new byte[] { (byte) 0x30, (byte) 0x20,
                        (byte) 0x48, (byte) 0x45, (byte) 0x41, (byte) 0x44,
                        (byte) 0x0A, (byte) 0x31, (byte) 0x20, (byte) 0x43,
                        (byte) 0x48, (byte) 0x41, (byte) 0x52, (byte) 0x20,
                        (byte) 0x41, (byte) 0x53, (byte) 0x43, (byte) 0x49,
                        (byte) 0x49, (byte) 0x0A, /*
                                                   * The unmappable character,
                                                   * shown here as a question
                                                   * mark
                                                   */(byte) 0x3F, (byte) 0x20,
                        (byte) 0x69, (byte) 0x73, (byte) 0x20, (byte) 0x75,
                        (byte) 0x6E, (byte) 0x6D, (byte) 0x61, (byte) 0x70,
                        (byte) 0x70, (byte) 0x61, (byte) 0x62, (byte) 0x6C,
                        (byte) 0x65, (byte) 0x20, (byte) 0x69, (byte) 0x6E,
                        (byte) 0x20, (byte) 0x61, (byte) 0x73, (byte) 0x63,
                        (byte) 0x69, (byte) 0x69, (byte) 0x0A, (byte) 0x30,
                        (byte) 0x20, (byte) 0x54, (byte) 0x52, (byte) 0x4C,
                        (byte) 0x52, (byte) 0x0A, }, out.toByteArray()));
    }

    /**
     * Test writing out big-endian unicode bytes, using CR-only line terminators
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputUnicodeBigEndianCrLF() throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR UNICODE");
        lines.add("0 TRLR");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.setLittleEndianForUnicode(false);
        gfw.terminator = LineTerminator.CRLF;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        assertTrue(
                "Output bytes are not the expected value",
                Arrays.equals(new byte[] { (byte) 0x00, (byte) 0x30,
                        (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x48,
                        (byte) 0x00, (byte) 0x45, (byte) 0x00, (byte) 0x41,
                        (byte) 0x00, (byte) 0x44, (byte) 0x00, (byte) 0x0D,
                        (byte) 0x00, (byte) 0x0A, (byte) 0x00, (byte) 0x31,
                        (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x43,
                        (byte) 0x00, (byte) 0x48, (byte) 0x00, (byte) 0x41,
                        (byte) 0x00, (byte) 0x52, (byte) 0x00, (byte) 0x20,
                        (byte) 0x00, (byte) 0x55, (byte) 0x00, (byte) 0x4E,
                        (byte) 0x00, (byte) 0x49, (byte) 0x00, (byte) 0x43,
                        (byte) 0x00, (byte) 0x4F, (byte) 0x00, (byte) 0x44,
                        (byte) 0x00, (byte) 0x45, (byte) 0x00, (byte) 0x0D,
                        (byte) 0x00, (byte) 0x0A, (byte) 0x00, (byte) 0x30,
                        (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x54,
                        (byte) 0x00, (byte) 0x52, (byte) 0x00, (byte) 0x4C,
                        (byte) 0x00, (byte) 0x52, (byte) 0x00, (byte) 0x0D,
                        (byte) 0x00, (byte) 0x0A, }, out.toByteArray()));
    }

    /**
     * Test writing out big-endian unicode bytes, using CR-only line terminators
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputUnicodeBigEndianCrOnly() throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR UNICODE");
        lines.add("0 TRLR");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.setLittleEndianForUnicode(false);
        gfw.terminator = LineTerminator.CR_ONLY;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        assertTrue("Output bytes are not the expected value", Arrays.equals(
                new byte[] { (byte) 0x00, (byte) 0x30, (byte) 0x00,
                        (byte) 0x20, (byte) 0x00, (byte) 0x48, (byte) 0x00,
                        (byte) 0x45, (byte) 0x00, (byte) 0x41, (byte) 0x00,
                        (byte) 0x44, (byte) 0x00, (byte) 0x0D, (byte) 0x00,
                        (byte) 0x31, (byte) 0x00, (byte) 0x20, (byte) 0x00,
                        (byte) 0x43, (byte) 0x00, (byte) 0x48, (byte) 0x00,
                        (byte) 0x41, (byte) 0x00, (byte) 0x52, (byte) 0x00,
                        (byte) 0x20, (byte) 0x00, (byte) 0x55, (byte) 0x00,
                        (byte) 0x4E, (byte) 0x00, (byte) 0x49, (byte) 0x00,
                        (byte) 0x43, (byte) 0x00, (byte) 0x4F, (byte) 0x00,
                        (byte) 0x44, (byte) 0x00, (byte) 0x45, (byte) 0x00,
                        (byte) 0x0D, (byte) 0x00, (byte) 0x30, (byte) 0x00,
                        (byte) 0x20, (byte) 0x00, (byte) 0x54, (byte) 0x00,
                        (byte) 0x52, (byte) 0x00, (byte) 0x4C, (byte) 0x00,
                        (byte) 0x52, (byte) 0x00, (byte) 0x0D, },
                out.toByteArray()));
    }

    /**
     * Test writing out big-endian unicode bytes, using LFCR line terminators.
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputUnicodeBigEndianLfCr() throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR UNICODE");
        lines.add("0 TRLR");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.setLittleEndianForUnicode(false);
        gfw.terminator = LineTerminator.LFCR;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        byte[] expected = new byte[] { (byte) 0x00, (byte) 0x30, (byte) 0x00,
                (byte) 0x20, (byte) 0x00, (byte) 0x48, (byte) 0x00,
                (byte) 0x45, (byte) 0x00, (byte) 0x41, (byte) 0x00,
                (byte) 0x44, (byte) 0x00, (byte) 0x0A, (byte) 0x00,
                (byte) 0x0D, (byte) 0x00, (byte) 0x31, (byte) 0x00,
                (byte) 0x20, (byte) 0x00, (byte) 0x43, (byte) 0x00,
                (byte) 0x48, (byte) 0x00, (byte) 0x41, (byte) 0x00,
                (byte) 0x52, (byte) 0x00, (byte) 0x20, (byte) 0x00,
                (byte) 0x55, (byte) 0x00, (byte) 0x4E, (byte) 0x00,
                (byte) 0x49, (byte) 0x00, (byte) 0x43, (byte) 0x00,
                (byte) 0x4F, (byte) 0x00, (byte) 0x44, (byte) 0x00,
                (byte) 0x45, (byte) 0x00, (byte) 0x0A, (byte) 0x00,
                (byte) 0x0D, (byte) 0x00, (byte) 0x30, (byte) 0x00,
                (byte) 0x20, (byte) 0x00, (byte) 0x54, (byte) 0x00,
                (byte) 0x52, (byte) 0x00, (byte) 0x4C, (byte) 0x00,
                (byte) 0x52, (byte) 0x00, (byte) 0x0A, (byte) 0x00,
                (byte) 0x0D, };
        assertTrue("Output bytes are not the expected value",
                Arrays.equals(expected, out.toByteArray()));
    }

    /**
     * Test writing out big-endian unicode bytes, using CR-only line terminators
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputUnicodeBigEndianLfOnly() throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR UNICODE");
        lines.add("0 TRLR");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);
        gfw.setLittleEndianForUnicode(false);
        gfw.terminator = LineTerminator.LF_ONLY;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        assertTrue("Output bytes are not the expected value", Arrays.equals(
                new byte[] { (byte) 0x00, (byte) 0x30, (byte) 0x00,
                        (byte) 0x20, (byte) 0x00, (byte) 0x48, (byte) 0x00,
                        (byte) 0x45, (byte) 0x00, (byte) 0x41, (byte) 0x00,
                        (byte) 0x44, (byte) 0x00, (byte) 0x0A, (byte) 0x00,
                        (byte) 0x31, (byte) 0x00, (byte) 0x20, (byte) 0x00,
                        (byte) 0x43, (byte) 0x00, (byte) 0x48, (byte) 0x00,
                        (byte) 0x41, (byte) 0x00, (byte) 0x52, (byte) 0x00,
                        (byte) 0x20, (byte) 0x00, (byte) 0x55, (byte) 0x00,
                        (byte) 0x4E, (byte) 0x00, (byte) 0x49, (byte) 0x00,
                        (byte) 0x43, (byte) 0x00, (byte) 0x4F, (byte) 0x00,
                        (byte) 0x44, (byte) 0x00, (byte) 0x45, (byte) 0x00,
                        (byte) 0x0A, (byte) 0x00, (byte) 0x30, (byte) 0x00,
                        (byte) 0x20, (byte) 0x00, (byte) 0x54, (byte) 0x00,
                        (byte) 0x52, (byte) 0x00, (byte) 0x4C, (byte) 0x00,
                        (byte) 0x52, (byte) 0x00, (byte) 0x0A, },
                out.toByteArray()));
    }

    /**
     * Test writing out little-endian unicode bytes, using CRLF line terminators
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputUnicodeLittleEndianCrLf() throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR UNICODE");
        lines.add("0 TRLR");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);

        // Not necessary, little endian is default, but good for explicitness
        gfw.setLittleEndianForUnicode(true);

        gfw.terminator = LineTerminator.CRLF;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        byte[] expected = new byte[] { (byte) 0x30, (byte) 0x00, (byte) 0x20,
                (byte) 0x00, (byte) 0x48, (byte) 0x00, (byte) 0x45,
                (byte) 0x00, (byte) 0x41, (byte) 0x00, (byte) 0x44,
                (byte) 0x00, (byte) 0x0D, (byte) 0x00, (byte) 0x0A,
                (byte) 0x00, (byte) 0x31, (byte) 0x00, (byte) 0x20,
                (byte) 0x00, (byte) 0x43, (byte) 0x00, (byte) 0x48,
                (byte) 0x00, (byte) 0x41, (byte) 0x00, (byte) 0x52,
                (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x55,
                (byte) 0x00, (byte) 0x4E, (byte) 0x00, (byte) 0x49,
                (byte) 0x00, (byte) 0x43, (byte) 0x00, (byte) 0x4F,
                (byte) 0x00, (byte) 0x44, (byte) 0x00, (byte) 0x45,
                (byte) 0x00, (byte) 0x0D, (byte) 0x00, (byte) 0x0A,
                (byte) 0x00, (byte) 0x30, (byte) 0x00, (byte) 0x20,
                (byte) 0x00, (byte) 0x54, (byte) 0x00, (byte) 0x52,
                (byte) 0x00, (byte) 0x4C, (byte) 0x00, (byte) 0x52,
                (byte) 0x00, (byte) 0x0D, (byte) 0x00, (byte) 0x0A, (byte) 0x00 };
        assertTrue("Output bytes are not the expected value",
                Arrays.equals(expected, out.toByteArray()));
    }

    /**
     * Test writing out little-endian unicode bytes, using CR only line
     * terminators
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputUnicodeLittleEndianCrOnly() throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR UNICODE");
        lines.add("0 TRLR");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);

        // Not necessary, little endian is default, but good for explicitness
        gfw.setLittleEndianForUnicode(true);

        gfw.terminator = LineTerminator.CR_ONLY;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        byte[] expected = new byte[] { (byte) 0x30, (byte) 0x00, (byte) 0x20,
                (byte) 0x00, (byte) 0x48, (byte) 0x00, (byte) 0x45,
                (byte) 0x00, (byte) 0x41, (byte) 0x00, (byte) 0x44,
                (byte) 0x00, (byte) 0x0D, (byte) 0x00, (byte) 0x31,
                (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x43,
                (byte) 0x00, (byte) 0x48, (byte) 0x00, (byte) 0x41,
                (byte) 0x00, (byte) 0x52, (byte) 0x00, (byte) 0x20,
                (byte) 0x00, (byte) 0x55, (byte) 0x00, (byte) 0x4E,
                (byte) 0x00, (byte) 0x49, (byte) 0x00, (byte) 0x43,
                (byte) 0x00, (byte) 0x4F, (byte) 0x00, (byte) 0x44,
                (byte) 0x00, (byte) 0x45, (byte) 0x00, (byte) 0x0D,
                (byte) 0x00, (byte) 0x30, (byte) 0x00, (byte) 0x20,
                (byte) 0x00, (byte) 0x54, (byte) 0x00, (byte) 0x52,
                (byte) 0x00, (byte) 0x4C, (byte) 0x00, (byte) 0x52,
                (byte) 0x00, (byte) 0x0D, (byte) 0x00 };
        assertTrue("Output bytes are not the expected value",
                Arrays.equals(expected, out.toByteArray()));
    }

    /**
     * Test writing out little-endian unicode bytes, using LFCR line terminators
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputUnicodeLittleEndianLfCr() throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR UNICODE");
        lines.add("0 TRLR");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);

        // Not necessary, little endian is default, but good for explicitness
        gfw.setLittleEndianForUnicode(true);

        gfw.terminator = LineTerminator.LFCR;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        byte[] expected = new byte[] { (byte) 0x30, (byte) 0x00, (byte) 0x20,
                (byte) 0x00, (byte) 0x48, (byte) 0x00, (byte) 0x45,
                (byte) 0x00, (byte) 0x41, (byte) 0x00, (byte) 0x44,
                (byte) 0x00, (byte) 0x0A, (byte) 0x00, (byte) 0x0D,
                (byte) 0x00, (byte) 0x31, (byte) 0x00, (byte) 0x20,
                (byte) 0x00, (byte) 0x43, (byte) 0x00, (byte) 0x48,
                (byte) 0x00, (byte) 0x41, (byte) 0x00, (byte) 0x52,
                (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x55,
                (byte) 0x00, (byte) 0x4E, (byte) 0x00, (byte) 0x49,
                (byte) 0x00, (byte) 0x43, (byte) 0x00, (byte) 0x4F,
                (byte) 0x00, (byte) 0x44, (byte) 0x00, (byte) 0x45,
                (byte) 0x00, (byte) 0x0A, (byte) 0x00, (byte) 0x0D,
                (byte) 0x00, (byte) 0x30, (byte) 0x00, (byte) 0x20,
                (byte) 0x00, (byte) 0x54, (byte) 0x00, (byte) 0x52,
                (byte) 0x00, (byte) 0x4C, (byte) 0x00, (byte) 0x52,
                (byte) 0x00, (byte) 0x0A, (byte) 0x00, (byte) 0x0D, (byte) 0x00 };
        assertTrue("Output bytes are not the expected value",
                Arrays.equals(expected, out.toByteArray()));
    }

    /**
     * Test writing out little-endian unicode bytes, using LF only line
     * terminators
     * 
     * @throws IOException
     *             if the data can't be written
     */
    @Test
    public void testOutputUnicodeLittleEndianLfOnly() throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("0 HEAD");
        lines.add("1 CHAR UNICODE");
        lines.add("0 TRLR");
        GedcomFileWriter gfw = new GedcomFileWriter(lines);

        // Not necessary, little endian is default, but good for explicitness
        gfw.setLittleEndianForUnicode(true);

        gfw.terminator = LineTerminator.LF_ONLY;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gfw.write(out);
        out.close();

        byte[] expected = new byte[] { (byte) 0x30, (byte) 0x00, (byte) 0x20,
                (byte) 0x00, (byte) 0x48, (byte) 0x00, (byte) 0x45,
                (byte) 0x00, (byte) 0x41, (byte) 0x00, (byte) 0x44,
                (byte) 0x00, (byte) 0x0A, (byte) 0x00, (byte) 0x31,
                (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x43,
                (byte) 0x00, (byte) 0x48, (byte) 0x00, (byte) 0x41,
                (byte) 0x00, (byte) 0x52, (byte) 0x00, (byte) 0x20,
                (byte) 0x00, (byte) 0x55, (byte) 0x00, (byte) 0x4E,
                (byte) 0x00, (byte) 0x49, (byte) 0x00, (byte) 0x43,
                (byte) 0x00, (byte) 0x4F, (byte) 0x00, (byte) 0x44,
                (byte) 0x00, (byte) 0x45, (byte) 0x00, (byte) 0x0A,
                (byte) 0x00, (byte) 0x30, (byte) 0x00, (byte) 0x20,
                (byte) 0x00, (byte) 0x54, (byte) 0x00, (byte) 0x52,
                (byte) 0x00, (byte) 0x4C, (byte) 0x00, (byte) 0x52,
                (byte) 0x00, (byte) 0x0A, (byte) 0x00 };
        assertTrue("Output bytes are not the expected value",
                Arrays.equals(expected, out.toByteArray()));
    }

    private void dump(byte[] actual) {
        System.out
                .println("        assertTrue(\"Output bytes are not the expected value\", Arrays.equals(\n"
                        + "                new byte[] { ");
        for (byte element : actual) {
            System.out.print("(byte) ");
            System.out.print(String.format("%02X", Byte.valueOf(element))
                    + ", ");
        }
        System.out.println("},\n" + "                out.toByteArray()));");
    }

    private void showDiffs(byte[] expected, byte[] actual) {
        for (int i = 0; i < expected.length && i < actual.length; i++) {
            System.out.print(String.format("%02X", Byte.valueOf(expected[i]))
                    + " ");
            System.out.print(String.format("%02X", Byte.valueOf(actual[i]))
                    + " ");
            if (expected[i] != actual[i]) {
                System.out.print(" <--");
            }
            System.out.println();
        }
    }
}
