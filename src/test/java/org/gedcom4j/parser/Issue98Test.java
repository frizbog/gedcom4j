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
package org.gedcom4j.parser;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.junit.Test;

/**
 * Test for issue 98 - making sure byte order markers in unicode files parse correctly
 * 
 * @author frizbog
 */
public class Issue98Test {

    /**
     * Test issue 98 - load a UTF-16 file with big-endian byte order marker
     * 
     * @throws GedcomParserException
     *             if the data cannot be parsed
     * @throws IOException
     *             if the data cannot be read
     */
    @Test
    public void testUtf16BigEndianWithByteOrderMarker() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/utf16be.ged");
        assertTrue(gp.getErrors().isEmpty());
        assertTrue(gp.getWarnings().isEmpty());
    }

    /**
     * Test issue 98 - load a UTF-16 file with big-endian byte order marker
     * 
     * @throws GedcomParserException
     *             if the data cannot be parsed
     * @throws IOException
     *             if the data cannot be read
     */
    @Test
    public void testUtf16LittleEndianWithByteOrderMarker() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/utf16le.ged");
        assertTrue(gp.getErrors().isEmpty());
        assertTrue(gp.getWarnings().isEmpty());
    }

    /**
     * Test issue 98 - load a UTF16 file with big-endian byte order marker
     * 
     * @throws GedcomParserException
     *             if the data cannot be parsed
     * @throws IOException
     *             if the data cannot be read
     */
    @Test
    public void testUtf8WithByteOrderMarker() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/utf8.ged");
        assertTrue(gp.getErrors().isEmpty());
        assertTrue(gp.getWarnings().isEmpty());
    }

}
