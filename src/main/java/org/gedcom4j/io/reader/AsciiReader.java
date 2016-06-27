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
import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.io.event.FileProgressEvent;
import org.gedcom4j.parser.GedcomParser;

/**
 * A reader that loads from an input stream and gives back a collection of strings representing the data therein. This
 * implementation handles ASCII encoding (1 byte per character, no extended character support).
 * 
 * @author frizbog
 */
class AsciiReader extends AbstractEncodingSpecificReader {

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

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<String> load() throws IOException {
        List<String> result = new ArrayList<String>();
        StringBuilder lineBuffer = new StringBuilder();

        int lastChar;
        int b = -1;
        boolean eof = false;

        while (!eof) {
            lastChar = b;
            b = byteStream.read();

            // Check for EOF
            if (b < 0) {
                // hit EOF - add final line buffer (last line) and get out
                addNonBlankLine(result, lineBuffer);
                break;
            }

            // Check for carriage returns - signify EOL
            if (b == 0x0D) {
                addNonBlankLine(result, lineBuffer);
                lineBuffer.setLength(0);
                continue;
            }

            // Check for line feeds - signify EOL (unless prev char was a
            // CR)
            if (b == 0x0A) {
                if (lastChar != 0x0D) {
                    addNonBlankLine(result, lineBuffer);
                    lineBuffer.setLength(0);
                }
                continue;
            }

            // All other characters in 0x00 to 0x7F range are treated the
            // same,
            // regardless of encoding, and added as is
            if (b < 0x80) {
                lineBuffer.append(Character.valueOf((char) b));
                continue;
            }

            // If we fell through to here, we have an extended character
            throw new IOException("Extended characters not supported in ASCII: 0x" + Integer.toHexString(b));
        }
        parser.notifyObservers(new FileProgressEvent(this, linesRead, true));
        return result;
    }

    /**
     * Add line to result if it is not blank
     * 
     * @param result
     *            the resulting list of lines
     * @param lineBuffer
     *            the line buffer
     */
    private void addNonBlankLine(List<String> result, StringBuilder lineBuffer) {
        if (lineBuffer.length() > 0) {
            result.add(lineBuffer.toString());
        }
        linesRead++;
        if (linesRead % 100 == 0) {
            parser.notifyObservers(new FileProgressEvent(this, linesRead, false));
        }
    }

}
