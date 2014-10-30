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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A reader that loads from an input stream and gives back a collection of strings representing the data therein. This
 * implementation handles big-endian Unicode data.
 * 
 * @author frizbog
 */
class UnicodeBigEndianReader extends AbstractEncodingSpecificReader {

    /**
     * Constructor
     * 
     * @param byteStream
     *            the stream of data to read from
     */
    protected UnicodeBigEndianReader(InputStream byteStream) {
        super(byteStream);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<? extends String> load() throws IOException {
        List<String> result = new ArrayList<String>();

        StringBuilder lineBuffer = new StringBuilder();

        boolean eof = false;
        int b1 = -1;
        int b2 = -1;

        int lastB1;
        int lastB2;

        while (!eof) {
            lastB1 = b1;
            lastB2 = b2;
            b1 = byteStream.read();
            b2 = byteStream.read();

            // Check for EOF
            if (b1 < 0 || b2 < 0) {
                // hit EOF - add final line buffer (last line) and get out
                if (lineBuffer.length() > 0) {
                    result.add(lineBuffer.toString());
                }
                eof = true;
                break;
            }

            // Check for carriage returns - signify EOL
            if (b1 == 0x00 && b2 == 0x0D) {
                if (lineBuffer.length() > 0) {
                    result.add(lineBuffer.toString());
                }
                lineBuffer.setLength(0);
                continue;
            }

            // Check for line feeds - signify EOL (unless prev char was a
            // CR)
            if (b1 == 0x00 && b2 == 0x0A) {
                if (lastB1 != 0x00 || lastB2 != 0x0D) {
                    if (lineBuffer.length() > 0) {
                        result.add(lineBuffer.toString());
                    }
                    lineBuffer.setLength(0);
                }
                continue;
            }

            int unicodeChar = b1 << 8 | b2;
            lineBuffer.append(Character.valueOf((char) unicodeChar));
        }
        return result;
    }

}
