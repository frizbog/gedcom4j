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

import org.gedcom4j.io.encoding.AnselHandler;
import org.gedcom4j.io.event.FileProgressEvent;
import org.gedcom4j.parser.GedcomParser;

/**
 * A reader that loads from an input stream and gives back a collection of strings representing the data therein. This
 * implementation handles ANSEL encoding (1 byte per character, some extended character support).
 * 
 * @author frizbog
 */
class AnselReader extends AbstractEncodingSpecificReader {

    /**
     * Helper class
     */
    AnselHandler anselHandler = new AnselHandler();

    /**
     * Constructor
     * 
     * @param parser
     *            the {@link GedcomParser} which is using this object to read files
     * 
     * @param byteStream
     *            the stream of data being read
     */
    protected AnselReader(GedcomParser parser, InputStream byteStream) {
        super(parser, byteStream);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<String> load() throws IOException {
        List<String> result = new ArrayList<String>();
        char[] lineBuffer = new char[256];

        int lastChar;
        int currChar = -1;
        boolean eof = false;
        int lineBufferIdx = 0;

        while (!eof) {
            lastChar = currChar;
            currChar = byteStream.read();

            // Check for EOF
            if (currChar < 0) {
                addNonBlankLine(result, lineBuffer, lineBufferIdx);
                break;
            }

            // Check for carriage returns - signify EOL
            if (currChar == 0x0D) {
                addNonBlankLine(result, lineBuffer, lineBufferIdx);
                lineBufferIdx = 0;
                continue;
            }

            // Check for line feeds - signify EOL (unless prev char was a
            // CR)
            if (currChar == 0x0A) {
                if (lastChar != 0x0D) {
                    addNonBlankLine(result, lineBuffer, lineBufferIdx);
                    lineBufferIdx = 0;
                }
                continue;
            }

            // All other characters are treated the same at this point,
            // regardless of encoding, and added as is
            lineBuffer[lineBufferIdx++] = (char) currChar;
            continue;
        }
        result = anselHandler.toUtf16Lines(result);
        parser.notifyFileObservers(new FileProgressEvent(this, linesRead, true));
        return result;
    }

    /**
     * Add line to result if it is not blank. Notify listeners of progress every 100 lines.
     * 
     * @param result
     *            the resulting list of lines
     * @param lineBuffer
     *            the line buffer - this is all the ANSEL bytes in the line, converted to characters
     * @param lineBufferIdx
     *            the position in the line buffer we're up to - that is, the portion of the line buffer that is
     *            populated with data we want to use
     */
    private void addNonBlankLine(List<String> result, char[] lineBuffer, int lineBufferIdx) {
        if (lineBufferIdx > 0) {
            String s = new String(lineBuffer).substring(0, lineBufferIdx);
            result.add(s);
            linesRead++;
            if (linesRead % parser.getReadNotificationRate() == 0) {
                parser.notifyFileObservers(new FileProgressEvent(this, linesRead, false));
            }
        }
    }

}
