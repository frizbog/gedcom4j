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
import com.mattharrah.gedcom4j.model.Gedcom;
import com.mattharrah.gedcom4j.writer.GedcomWriter;
import com.mattharrah.gedcom4j.writer.GedcomWriterException;

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

        byte[] expected = new byte[] {
                0x30, 0x20, 0x48, 0x45, 0x41, 0x44, 0x0D, 0x0A, 0x31, 0x20,
                0x43, 0x48, 0x41, 0x52, 0x20, 0x41, 0x4E, 0x53, 0x45, 0x4C,
                0x0D, 0x0A, 0x40, 0x20, 0x75, 0x6E, 0x6D, 0x61, 0x70, 0x70,
                0x61, 0x62, 0x6C, 0x65, 0x20, 0x69, 0x6E, 0x20, 0x61, 0x6E,
                0x73, 0x65, 0x6C, 0x0D, 0x0A, (byte) 0xA1, 0x20, 0x6D, 0x61,
                0x70, 0x70, 0x61, 0x62, 0x6C, 0x65, 0x20, 0x69, 0x6E, 0x20,
                0x61, 0x6E, 0x73, 0x65, 0x6C, 0x0D, 0x0A, 0x30, 0x20, 0x54,
                0x52, 0x4C, 0x52, 0x0D, 0x0A,
        };
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
                Arrays.equals(new byte[] {
                        0x30, 0x20, 0x48, 0x45, 0x41, 0x44, 0x0D, 0x31, 0x20,
                        0x43, 0x48, 0x41, 0x52, 0x20, 0x41, 0x4E, 0x53, 0x45,
                        0x4C, 0x0D, 0x40, 0x20, 0x75, 0x6E, 0x6D, 0x61, 0x70,
                        0x70, 0x61, 0x62, 0x6C, 0x65, 0x20, 0x69, 0x6E, 0x20,
                        0x61, 0x6E, 0x73, 0x65, 0x6C, 0x0D, (byte) 0xA1, 0x20,
                        0x6D, 0x61, 0x70, 0x70, 0x61, 0x62, 0x6C, 0x65, 0x20,
                        0x69, 0x6E, 0x20, 0x61, 0x6E, 0x73, 0x65, 0x6C, 0x0D,
                        0x30, 0x20, 0x54, 0x52, 0x4C, 0x52, 0x0D,
                }, out.toByteArray()));
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

        byte[] expected = new byte[] {
                0x30, 0x20, 0x48, 0x45, 0x41, 0x44, 0x0A, 0x0D, 0x31, 0x20,
                0x43, 0x48, 0x41, 0x52, 0x20, 0x41, 0x4E, 0x53, 0x45, 0x4C,
                0x0A, 0x0D, 0x40, 0x20, 0x75, 0x6E, 0x6D, 0x61, 0x70, 0x70,
                0x61, 0x62, 0x6C, 0x65, 0x20, 0x69, 0x6E, 0x20, 0x61, 0x6E,
                0x73, 0x65, 0x6C, 0x0A, 0x0D, (byte) 0xA1, 0x20, 0x6D, 0x61,
                0x70, 0x70, 0x61, 0x62, 0x6C, 0x65, 0x20, 0x69, 0x6E, 0x20,
                0x61, 0x6E, 0x73, 0x65, 0x6C, 0x0A, 0x0D, 0x30, 0x20, 0x54,
                0x52, 0x4C, 0x52, 0x0A, 0x0D,
        };
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
                Arrays.equals(new byte[] {
                        0x30, 0x20, 0x48, 0x45, 0x41, 0x44, 0x0A, 0x31, 0x20,
                        0x43, 0x48, 0x41, 0x52, 0x20, 0x41, 0x4E, 0x53, 0x45,
                        0x4C, 0x0A, 0x40, 0x20, 0x75, 0x6E, 0x6D, 0x61, 0x70,
                        0x70, 0x61, 0x62, 0x6C, 0x65, 0x20, 0x69, 0x6E, 0x20,
                        0x61, 0x6E, 0x73, 0x65, 0x6C, 0x0A, (byte) 0xA1, 0x20,
                        0x6D, 0x61, 0x70, 0x70, 0x61, 0x62, 0x6C, 0x65, 0x20,
                        0x69, 0x6E, 0x20, 0x61, 0x6E, 0x73, 0x65, 0x6C, 0x0A,
                        0x30, 0x20, 0x54, 0x52, 0x4C, 0x52, 0x0A,
                }, out.toByteArray()));
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

        byte[] expected = new byte[] {
                0x30, 0x20, 0x48, 0x45, 0x41, 0x44, 0x0D, 0x0A, 0x31, 0x20,
                0x43, 0x48, 0x41, 0x52, 0x20, 0x41, 0x53, 0x43, 0x49, 0x49,
                0x0D, 0x0A,
                /*
                 * The unmappable character , shown here as a question mark
                 */0x3F, 0x20, 0x69, 0x73, 0x20, 0x75, 0x6E, 0x6D, 0x61, 0x70,
                0x70, 0x61, 0x62, 0x6C, 0x65, 0x20, 0x69, 0x6E, 0x20, 0x61,
                0x73, 0x63, 0x69, 0x69, 0x0D, 0x0A, 0x30, 0x20, 0x54, 0x52,
                0x4C, 0x52, 0x0D, 0x0A,
        };

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
                Arrays.equals(new byte[] {
                        0x30, 0x20, 0x48, 0x45, 0x41, 0x44, 0x0D, 0x31, 0x20,
                        0x43, 0x48, 0x41, 0x52, 0x20, 0x41, 0x53, 0x43, 0x49,
                        0x49, 0x0D, /*
                                     * The unmappable character, shown here as a
                                     * question mark
                                     */0x3F, 0x20, 0x69, 0x73, 0x20, 0x75,
                        0x6E, 0x6D, 0x61, 0x70, 0x70, 0x61, 0x62, 0x6C, 0x65,
                        0x20, 0x69, 0x6E, 0x20, 0x61, 0x73, 0x63, 0x69, 0x69,
                        0x0D, 0x30, 0x20, 0x54, 0x52, 0x4C, 0x52, 0x0D,
                }, out.toByteArray()));
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

        byte[] expected = new byte[] {
                0x30, 0x20, 0x48, 0x45, 0x41, 0x44, 0x0A, 0x0D, 0x31, 0x20,
                0x43, 0x48, 0x41, 0x52, 0x20, 0x41, 0x53, 0x43, 0x49, 0x49,
                0x0A, 0x0D,
                /*
                 * The unmappable character , shown here as a question mark
                 */0x3F, 0x20, 0x69, 0x73, 0x20, 0x75, 0x6E, 0x6D, 0x61, 0x70,
                0x70, 0x61, 0x62, 0x6C, 0x65, 0x20, 0x69, 0x6E, 0x20, 0x61,
                0x73, 0x63, 0x69, 0x69, 0x0A, 0x0D, 0x30, 0x20, 0x54, 0x52,
                0x4C, 0x52, 0x0A, 0x0D,
        };

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
                Arrays.equals(new byte[] {
                        0x30, 0x20, 0x48, 0x45, 0x41, 0x44, 0x0A, 0x31, 0x20,
                        0x43, 0x48, 0x41, 0x52, 0x20, 0x41, 0x53, 0x43, 0x49,
                        0x49, 0x0A, /*
                                     * The unmappable character, shown here as a
                                     * question mark
                                     */0x3F, 0x20, 0x69, 0x73, 0x20, 0x75,
                        0x6E, 0x6D, 0x61, 0x70, 0x70, 0x61, 0x62, 0x6C, 0x65,
                        0x20, 0x69, 0x6E, 0x20, 0x61, 0x73, 0x63, 0x69, 0x69,
                        0x0A, 0x30, 0x20, 0x54, 0x52, 0x4C, 0x52, 0x0A,
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
                Arrays.equals(new byte[] {
                        0x00, 0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00,
                        0x41, 0x00, 0x44, 0x00, 0x0D, 0x00, 0x0A, 0x00, 0x31,
                        0x00, 0x20, 0x00, 0x43, 0x00, 0x48, 0x00, 0x41, 0x00,
                        0x52, 0x00, 0x20, 0x00, 0x55, 0x00, 0x4E, 0x00, 0x49,
                        0x00, 0x43, 0x00, 0x4F, 0x00, 0x44, 0x00, 0x45, 0x00,
                        0x0D, 0x00, 0x0A, 0x00, 0x30, 0x00, 0x20, 0x00, 0x54,
                        0x00, 0x52, 0x00, 0x4C, 0x00, 0x52, 0x00, 0x0D, 0x00,
                        0x0A,
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

        assertTrue(
                "Output bytes are not the expected value",
                Arrays.equals(new byte[] {
                        0x00, 0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00,
                        0x41, 0x00, 0x44, 0x00, 0x0D, 0x00, 0x31, 0x00, 0x20,
                        0x00, 0x43, 0x00, 0x48, 0x00, 0x41, 0x00, 0x52, 0x00,
                        0x20, 0x00, 0x55, 0x00, 0x4E, 0x00, 0x49, 0x00, 0x43,
                        0x00, 0x4F, 0x00, 0x44, 0x00, 0x45, 0x00, 0x0D, 0x00,
                        0x30, 0x00, 0x20, 0x00, 0x54, 0x00, 0x52, 0x00, 0x4C,
                        0x00, 0x52, 0x00, 0x0D,
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

        byte[] expected = new byte[] {
                0x00, 0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41,
                0x00, 0x44, 0x00, 0x0A, 0x00, 0x0D, 0x00, 0x31, 0x00, 0x20,
                0x00, 0x43, 0x00, 0x48, 0x00, 0x41, 0x00, 0x52, 0x00, 0x20,
                0x00, 0x55, 0x00, 0x4E, 0x00, 0x49, 0x00, 0x43, 0x00, 0x4F,
                0x00, 0x44, 0x00, 0x45, 0x00, 0x0A, 0x00, 0x0D, 0x00, 0x30,
                0x00, 0x20, 0x00, 0x54, 0x00, 0x52, 0x00, 0x4C, 0x00, 0x52,
                0x00, 0x0A, 0x00, 0x0D,
        };
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

        assertTrue(
                "Output bytes are not the expected value",
                Arrays.equals(new byte[] {
                        0x00, 0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00,
                        0x41, 0x00, 0x44, 0x00, 0x0A, 0x00, 0x31, 0x00, 0x20,
                        0x00, 0x43, 0x00, 0x48, 0x00, 0x41, 0x00, 0x52, 0x00,
                        0x20, 0x00, 0x55, 0x00, 0x4E, 0x00, 0x49, 0x00, 0x43,
                        0x00, 0x4F, 0x00, 0x44, 0x00, 0x45, 0x00, 0x0A, 0x00,
                        0x30, 0x00, 0x20, 0x00, 0x54, 0x00, 0x52, 0x00, 0x4C,
                        0x00, 0x52, 0x00, 0x0A,
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

        byte[] expected = new byte[] {
                0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41, 0x00,
                0x44, 0x00, 0x0D, 0x00, 0x0A, 0x00, 0x31, 0x00, 0x20, 0x00,
                0x43, 0x00, 0x48, 0x00, 0x41, 0x00, 0x52, 0x00, 0x20, 0x00,
                0x55, 0x00, 0x4E, 0x00, 0x49, 0x00, 0x43, 0x00, 0x4F, 0x00,
                0x44, 0x00, 0x45, 0x00, 0x0D, 0x00, 0x0A, 0x00, 0x30, 0x00,
                0x20, 0x00, 0x54, 0x00, 0x52, 0x00, 0x4C, 0x00, 0x52, 0x00,
                0x0D, 0x00, 0x0A, 0x00
        };
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

        byte[] expected = new byte[] {
                0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41, 0x00,
                0x44, 0x00, 0x0D, 0x00, 0x31, 0x00, 0x20, 0x00, 0x43, 0x00,
                0x48, 0x00, 0x41, 0x00, 0x52, 0x00, 0x20, 0x00, 0x55, 0x00,
                0x4E, 0x00, 0x49, 0x00, 0x43, 0x00, 0x4F, 0x00, 0x44, 0x00,
                0x45, 0x00, 0x0D, 0x00, 0x30, 0x00, 0x20, 0x00, 0x54, 0x00,
                0x52, 0x00, 0x4C, 0x00, 0x52, 0x00, 0x0D, 0x00
        };
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

        byte[] expected = new byte[] {
                0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41, 0x00,
                0x44, 0x00, 0x0A, 0x00, 0x0D, 0x00, 0x31, 0x00, 0x20, 0x00,
                0x43, 0x00, 0x48, 0x00, 0x41, 0x00, 0x52, 0x00, 0x20, 0x00,
                0x55, 0x00, 0x4E, 0x00, 0x49, 0x00, 0x43, 0x00, 0x4F, 0x00,
                0x44, 0x00, 0x45, 0x00, 0x0A, 0x00, 0x0D, 0x00, 0x30, 0x00,
                0x20, 0x00, 0x54, 0x00, 0x52, 0x00, 0x4C, 0x00, 0x52, 0x00,
                0x0A, 0x00, 0x0D, 0x00
        };
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

        byte[] expected = new byte[] {
                0x30, 0x00, 0x20, 0x00, 0x48, 0x00, 0x45, 0x00, 0x41, 0x00,
                0x44, 0x00, 0x0A, 0x00, 0x31, 0x00, 0x20, 0x00, 0x43, 0x00,
                0x48, 0x00, 0x41, 0x00, 0x52, 0x00, 0x20, 0x00, 0x55, 0x00,
                0x4E, 0x00, 0x49, 0x00, 0x43, 0x00, 0x4F, 0x00, 0x44, 0x00,
                0x45, 0x00, 0x0A, 0x00, 0x30, 0x00, 0x20, 0x00, 0x54, 0x00,
                0x52, 0x00, 0x4C, 0x00, 0x52, 0x00, 0x0A, 0x00
        };
        assertTrue("Output bytes are not the expected value",
                Arrays.equals(expected, out.toByteArray()));
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
    public void testWriteFileWithName() throws IOException,
            GedcomWriterException {
        String fn = System.getProperty("java.io.tmpdir")
                + System.getProperty("file.separator")
                + "gedcomfilewritertest.ged";
        Gedcom g = new Gedcom();
        GedcomWriter gw = new GedcomWriter(g);
        gw.write(fn);
    }

}
