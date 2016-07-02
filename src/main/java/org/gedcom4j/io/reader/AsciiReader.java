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

import java.io.IOException;
import java.io.InputStream;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.parser.GedcomParser;

/**
 * A reader that reads a single line from an Ascii-encoded file.
 * 
 * @author frizbog
 */
class AsciiReader extends AbstractEncodingSpecificReader {

    /**
     * Are we at the end of file yet?
     */
    private boolean eof = false;

    /**
     * The prior character we read
     */
    private int lastChar = -1;

    /**
     * The current character we've just read
     */
    private int currChar = -1;

    /**
     * The line buffer for the current line
     */
    private final StringBuilder lineBuffer = new StringBuilder();

    /**
     * Constructor
     * 
     * @param parser
     *            the {@link GedcomParser} which is using this object to read files
     * 
     * @param byteStream
     *            stream of data to read
     */
    protected AsciiReader(GedcomParser parser, InputStream byteStream) {
        super(parser, byteStream);
    }

    @Override
    public String nextLine() throws IOException, GedcomParserException {
        String result = null;
        while (!eof) {
            lastChar = currChar;
            currChar = byteStream.read();

            // Check for EOF
            if (currChar < 0) {
                // hit EOF - add final line buffer (last line) and get out
                eof = true;
                result = lineBuffer.toString();
                break;
            }

            // Check for carriage returns - signify EOL
            if (currChar == 0x0D) {
                result = lineBuffer.toString();
                lineBuffer.setLength(0);
                break;
            }

            // Check for line feeds - signify EOL (unless prev char was a
            // CR)
            if (currChar == 0x0A) {
                if (lastChar != 0x0D) {
                    result = lineBuffer.toString();
                    lineBuffer.setLength(0);
                }
                break;
            }

            // All other characters in 0x00 to 0x7F range are treated the
            // same,
            // regardless of encoding, and added as is
            if (currChar < 0x80) {
                lineBuffer.append(Character.valueOf((char) currChar));
                continue;
            }

            // If we fell through to here, we have an extended character
            throw new IOException("Extended characters not supported in ASCII: 0x" + Integer.toHexString(currChar));
        }
        return result;
    }

}
