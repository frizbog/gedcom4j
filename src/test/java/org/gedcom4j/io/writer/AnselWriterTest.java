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
package org.gedcom4j.io.writer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.gedcom4j.exception.WriterCancelledException;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.writer.GedcomWriter;
import org.junit.Test;

/**
 * Test for {@link AnselWriter}
 * 
 * @author frizbog
 */
public class AnselWriterTest {

    /**
     * Try writing a UTF-16 string with diacritics out as an ANSEL string
     * 
     * @throws IOException
     *             if the line cannot be written to the memory output stream
     * @throws WriterCancelledException
     *             if the write operation was cancelled
     */
    @Test
    public void testWriteLine() throws IOException, WriterCancelledException {
        AnselWriter anselWriter = new AnselWriter(new GedcomWriter(new Gedcom()));
        anselWriter.terminator = LineTerminator.CRLF;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Mix of precomposed and combined diacritics
        String utf16 = "\u1EA2B\u0309C\u0309D\u0309\u1EBAF\u0309G\u0309H\u0309\u1EC8J\u0309K\u0309L\u0309M\u0309";

        anselWriter.writeLine(baos, utf16);
        String ansel = baos.toString("UTF-8");
        String expected = "\uFFFD\u0041\uFFFD\u0042\uFFFD\u0043\uFFFD\u0044\uFFFD\u0045\uFFFD\u0046"
                + "\uFFFD\u0047\uFFFD\u0048\uFFFD\u0049\uFFFD\u004A\uFFFD\u004B\uFFFD\u004C\uFFFD\u004D\r\n";
        for (int i = 0; i < ansel.length(); i++) {
            char a = ansel.charAt(i);
            char e = expected.charAt(i);
            assertEquals("Character " + i + " is not equal", e, a);
        }
    }
}
