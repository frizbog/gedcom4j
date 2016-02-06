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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
     */
    @Test
    public void testWriteLine() throws IOException {
        AnselWriter anselWriter = new AnselWriter();
        anselWriter.terminator = LineTerminator.CRLF;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Mix of precomposed and combined diacritics
        String utf16 = "ẢB\u0309C\u0309D\u0309ẺF\u0309G\u0309H\u0309ỈJ\u0309K\u0309L\u0309M\u0309";
        anselWriter.writeLine(baos, utf16);
        String ansel = baos.toString();
        for (int i = 0; i < ansel.length(); i++) {
            System.out.println(String.format("%s (%02X)", ansel.charAt(i), (byte) ansel.charAt(i)));
        }
        System.out.println(ansel);
    }

}
