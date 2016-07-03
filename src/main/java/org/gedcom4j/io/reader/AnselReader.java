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
import org.gedcom4j.io.encoding.AnselHandler;
import org.gedcom4j.parser.GedcomParser;

/**
 * A reader that reads a single line from an ANSEL-encoded file. This implementation handles ANSEL encoding (1 byte per
 * character, some extended character support).
 * 
 * @author frizbog
 */
class AnselReader extends AbstractEncodingSpecificReader {

    /**
     * Helper class
     */
    AnselHandler anselHandler = new AnselHandler();

    /**
     * Index into the line buffer
     */
    int lineBufferIdx = 0;

    /**
     * Current character
     */
    private int currChar = -1;

    /**
     * Last character read
     */
    private int lastChar;

    /**
     * The line buffer
     */
    private final char[] lineBuffer = new char[256];

    /**
     * Are we at the end of file yet?
     */
    private boolean eof = false;

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

    @Override
    public String nextLine() throws IOException, GedcomParserException {
        if (eof) {
            return null;
        }
        String result = null;
        while (!eof) {
            lastChar = currChar;
            currChar = byteStream.read();

            // Check for EOF
            if (currChar < 0) {
                result = getThisLine();
                eof = true;
                break;
            }

            // Check for carriage returns or line feeds - signify EOL
            if (currChar == 0x0D || currChar == 0x0A) {
                if (lineBufferIdx > 0) {
                    result = getThisLine();
                    lineBufferIdx = 0;
                    break;
                }
                continue;
            }

            // All other characters are treated the same at this point,
            // regardless of encoding, and added as is
            lineBuffer[lineBufferIdx++] = (char) currChar;

            if (lineBufferIdx >= 255) {
                result = getThisLine();
                lineBufferIdx = 0;
                insertSyntheticConcTag();
                break;
            }

        }
        return result;
    }

    /**
     * Determine what level the current line in the line buffer is
     * 
     * @return what level the current line in the line buffer is
     * @throws GedcomParserException
     *             if the line level can't be determined, because the file doesn't begin with a 1 or 2 digit number
     *             followed by a space.
     */
    private int getCurrentLevelFromLineBuffer() throws GedcomParserException {
        int level = -1;
        if (Character.isDigit(lineBuffer[0])) {
            if (Character.isDigit(lineBuffer[1])) {
                if (lineBuffer[2] == ' ') {
                    level = Character.getNumericValue(lineBuffer[0]) * 10 + Character.getNumericValue(lineBuffer[1]);

                } else {
                    /*
                     * Line is too long and doesn't begin with a 1 or 2 digit number followed by a space, so we can't
                     * put in CONC's on the fly (because we don't know what level we're at)
                     */
                    throw new GedcomParserException("Line " + linesRead + " exceeds 255 characters and does not begin with a 1 or 2 digit number. "
                            + "Can't split automatically.");
                }
            } else {
                if (lineBuffer[1] == ' ') {
                    level = Character.getNumericValue(lineBuffer[0]);
                } else {
                    /*
                     * Line is too long and doesn't begin with a 1 or 2 digit number followed by a space, so we can't
                     * put in CONC's on the fly (because we don't know what level we're at)
                     */
                    throw new GedcomParserException("Line " + linesRead + " exceeds 255 characters and does not begin with a 1 or 2 digit number. "
                            + "Can't split automatically.");
                }
            }
        } else {
            /*
             * Line is too long and doesn't begin with a 1 or 2 digit number followed by a space, so we can't put in
             * CONC's on the fly (because we don't know what level we're at)
             */
            throw new GedcomParserException("Line " + linesRead
                    + " exceeds 255 characters and does not begin with a 1 or 2 digit number. Can't split automatically.");
        }
        return level;
    }

    /**
     * Get the current line buffer's contents
     * 
     * @return the current line buffer's contents
     */
    private String getThisLine() {
        String result = null;
        if (lineBufferIdx > 0) {
            String s = new String(lineBuffer).substring(0, lineBufferIdx);
            result = anselHandler.toUtf16(s);
            if (STRINGS_TO_INTERN.contains(result)) {
                result = result.intern();
            }
        }
        linesRead++;
        return result;
    }

    /**
     * Insert synthetic CONC tags into the character buffer as if they had been there the whole time
     * 
     * @throws GedcomParserException
     */
    private void insertSyntheticConcTag() throws GedcomParserException {
        int level = getCurrentLevelFromLineBuffer();

        lineBufferIdx = 0;
        parser.warnings.add("Line " + linesRead + " exceeds 255 characters - introducing synthetic CONC tag to split line");
        level++;
        if (level > 9) {
            lineBuffer[lineBufferIdx++] = Character.forDigit(level / 10, 10);
            lineBuffer[lineBufferIdx++] = Character.forDigit(level % 10, 10);
        } else {
            lineBuffer[lineBufferIdx++] = Character.forDigit(level, 10);
        }
        lineBuffer[lineBufferIdx++] = ' ';
        lineBuffer[lineBufferIdx++] = 'C';
        lineBuffer[lineBufferIdx++] = 'O';
        lineBuffer[lineBufferIdx++] = 'N';
        lineBuffer[lineBufferIdx++] = 'C';
        lineBuffer[lineBufferIdx++] = ' ';
    }

}
