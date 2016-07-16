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

import java.io.IOException;
import java.io.InputStream;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.parser.GedcomParser;

/**
 * A reader that reads a single line from a Big-Endian Unicode-encoded file. Two bytes per character.
 * 
 * @author frizbog
 */
final class UnicodeBigEndianReader extends AbstractEncodingSpecificReader {

    /**
     * Are we at the end of file yet?
     */
    private boolean eof = false;

    /**
     * The line buffer for the current line
     */
    private final StringBuilder lineBuffer = new StringBuilder();

    /**
     * Constructor
     * 
     * @param parser
     *            the {@link GedcomParser} which is using this object to read files
     * @param byteStream
     *            the stream of data to read from
     */
    protected UnicodeBigEndianReader(GedcomParser parser, InputStream byteStream) {
        super(parser, byteStream);
    }

    @Override
    public String nextLine() throws IOException, GedcomParserException {
        String result = null;

        boolean beginningOfFile = true;

        while (!eof) {
            int currChar1 = byteStream.read();
            if (currChar1 >= 0) {
                bytesRead++;
            }
            int currChar2 = byteStream.read();
            if (currChar2 >= 0) {
                bytesRead++;
            }

            // Check for EOF
            if (currChar1 < 0 || currChar2 < 0) {
                // hit EOF - add final line buffer (last line) and get out
                if (lineBuffer.length() > 0) {
                    result = lineBuffer.toString();
                }
                eof = true;
                break;
            }

            // If it's a byte order marker at the beginning of the file, discard it
            if (beginningOfFile && (currChar1 == 0xFE && currChar2 == 0xFF)) {
                beginningOfFile = false;
                lineBuffer.setLength(0);
                continue;
            }

            beginningOfFile = false;

            // Check for carriage returns or line feeds - signify EOL
            if ((currChar1 == 0x00 && currChar2 == 0x0D) || (currChar1 == 0x00 && currChar2 == 0x0A)) {
                if (lineBuffer.length() > 0) {
                    result = lineBuffer.toString();
                    lineBuffer.setLength(0);
                    break;
                }
                continue;
            }

            // Do bit shifting stuff to make the character from the bytes
            int unicodeChar = currChar1 << 8 | currChar2;
            lineBuffer.append(Character.valueOf((char) unicodeChar));
        }
        return result;
    }

    @Override
    void cleanUp() throws IOException {
        // do nothing
    }

}
