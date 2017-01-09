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
package org.gedcom4j.io.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.InMemoryGedcom;
import org.gedcom4j.parser.GedcomParser;
import org.junit.Test;

/**
 * Test for {@link Utf8Reader}
 * 
 * @author frizbog
 */
public class Utf8ReaderTest {
    /**
     * Test an empty byte array, no byte order mark
     * 
     * @throws IOException
     *             if the data can't be read
     * @throws GedcomParserException
     *             if the data cannot be parsed
     */
    @Test
    public void testEmptyNoByteOrderMark() throws IOException, GedcomParserException {
        GedcomParser parser = new GedcomParser(new InMemoryGedcom());

        byte[] buf = new byte[] {};
        InputStream fis = new ByteArrayInputStream(buf);
        Utf8Reader r = new Utf8Reader(parser, fis);
        assertNotNull(r);
        assertNull(r.nextLine());
    }

    /**
     * Test an array of empty strings
     * 
     * @throws IOException
     *             if the data can't be read
     * @throws GedcomParserException
     *             if the data cannot be parsed
     */
    @Test
    public void testEmptyStrings() throws IOException, GedcomParserException {
        GedcomParser parser = new GedcomParser(new InMemoryGedcom());

        byte[] buf = "\n\n\n\n\n\n\n\n\n".getBytes();
        InputStream fis = new ByteArrayInputStream(buf);
        Utf8Reader r = new Utf8Reader(parser, fis);
        assertNotNull(r);
        assertNull(r.nextLine());
    }

    /**
     * Test an empty byte array with byte order mark
     * 
     * @throws IOException
     *             if the data can't be read
     * @throws GedcomParserException
     *             if the data cannot be parsed
     */
    @Test
    public void testEmptyWithByteOrderMark() throws IOException, GedcomParserException {
        GedcomParser parser = new GedcomParser(new InMemoryGedcom());

        byte[] buf = new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
        InputStream fis = new ByteArrayInputStream(buf);
        Utf8Reader r = new Utf8Reader(parser, fis);
        assertNotNull(r);
        assertNull(r.nextLine());
    }

    /**
     * Test an file with lines containing leading whitespace
     * 
     * @throws IOException
     *             if the data can't be read
     * @throws GedcomParserException
     *             if the data cannot be parsed
     */
    @Test
    public void testLeadingWhitespaceStrings() throws IOException, GedcomParserException {
        GedcomParser parser = new GedcomParser(new InMemoryGedcom());

        byte[] buf = "\t Foo\n Bar\n     Baz\n       \t     \t Bat\n".getBytes();
        InputStream fis = new ByteArrayInputStream(buf);
        Utf8Reader r = new Utf8Reader(parser, fis);
        assertNotNull(r);
        assertEquals("Foo", r.nextLine());
        assertEquals("Bar", r.nextLine());
        assertEquals("Baz", r.nextLine());
        assertEquals("Bat", r.nextLine());
    }

    /**
     * Test a non-existent file
     * 
     * @throws IOException
     *             if the data can't be read
     * @throws GedcomParserException
     *             if the data cannot be parsed
     */
    @Test(expected = FileNotFoundException.class)
    public void testNonExistentFile() throws IOException, GedcomParserException {
        GedcomParser parser = new GedcomParser(new InMemoryGedcom());

        try (InputStream fis = new FileInputStream("I DON'T EXIST!!!")) {
            Utf8Reader r = new Utf8Reader(parser, fis);
            assertNotNull(r);
            assertNull(r.nextLine());
        }
    }

    /**
     * Test an file with lines containing trailing whitespace
     * 
     * @throws IOException
     *             if the data can't be read
     * @throws GedcomParserException
     *             if the data cannot be parsed
     */
    @Test
    public void testTrailingWhitespaceStrings() throws IOException, GedcomParserException {
        GedcomParser parser = new GedcomParser(new InMemoryGedcom());

        byte[] buf = "\tFoo \nBar  \nBaz   \nBat \t \t \n".getBytes();
        InputStream fis = new ByteArrayInputStream(buf);
        Utf8Reader r = new Utf8Reader(parser, fis);
        assertNotNull(r);
        assertEquals("Foo ", r.nextLine());
        assertEquals("Bar  ", r.nextLine());
        assertEquals("Baz   ", r.nextLine());
        assertEquals("Bat \t \t ", r.nextLine());
    }

    /**
     * Test an array of strings full of whitespace
     * 
     * @throws IOException
     *             if the data can't be read
     * @throws GedcomParserException
     *             if the data cannot be parsed
     */
    @Test
    public void testWhitespaceStrings() throws IOException, GedcomParserException {
        GedcomParser parser = new GedcomParser(new InMemoryGedcom());

        byte[] buf = "\t \n\t \n\t \n\t \n\t \n\t \n  \t \n \t \n\n".getBytes();
        InputStream fis = new ByteArrayInputStream(buf);
        Utf8Reader r = new Utf8Reader(parser, fis);
        assertNotNull(r);
        assertNull(r.nextLine());
    }

}
