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
import java.util.Arrays;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.io.encoding.AnselHandler;
import org.gedcom4j.parser.GedcomParser;

/**
 * A reader that reads a single line from an ANSEL-encoded file. This implementation handles ANSEL encoding (1 byte per
 * character, some extended character support).
 * 
 * @author frizbog
 */
final class AnselReader extends AbstractEncodingSpecificReader {

    /**
     * The byte value at which combining diacritics begin in ANSEL encoding
     */
    private static final char ANSEL_DIACRITICS_BEGIN_AT = 0x00E0;

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
     * Are we at the end of file yet?
     */
    private boolean eof = false;

    /**
     * The line buffer
     */
    private final char[] lineBuffer = new char[256];

    /**
     * Prior character read
     */
    private int oneCharBack = -1;

    /**
     * Index into {@link #holdingBin} array
     */
    private int holdingBinIdx = 0;

    /**
     * A holding bin for combining diacritics that are separated from the base character by a line break. This makes it
     * possible for us to keep the diacritics and the base character together.
     */
    private final char[] holdingBin = new char[2];

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
            int twoCharsBack = oneCharBack;
            oneCharBack = currChar;
            currChar = byteStream.read();

            // Check for EOF
            if (currChar < 0) {
                result = getThisLine();
                eof = true;
                break;
            }

            // Ignore leading spaces
            if (currChar == ' ' && lineBufferIdx == 0) {
                continue;
            }

            // Check for carriage returns or line feeds - signify EOL
            if ((currChar == 0x0D || currChar == 0x0A)) {

                // Check for line breaks between combining diacritics and the base characters

                if (oneCharBack >= ANSEL_DIACRITICS_BEGIN_AT) {
                    if (twoCharsBack >= ANSEL_DIACRITICS_BEGIN_AT) {
                        /*
                         * Two diacritics at end of line, already in the lineBuffer, and presumably the base character
                         * is at the beginning of the next line (after a CONC tag) - store in holding bin
                         */
                        holdingBin[holdingBinIdx++] = (char) twoCharsBack;
                        twoCharsBack = -1; // Keeps from holding characters in reserve repeatedly
                    }
                    /*
                     * One diacritic at end of line, already in the lineBuffer, and presumably the base character is at
                     * the beginning of the next line (after a CONC tag) - store in holding bin
                     */
                    holdingBin[holdingBinIdx++] = (char) oneCharBack;
                    oneCharBack = -1; // Keeps from holding characters in reserve repeatedly
                }

                // If we have a line break and contents in the buffer, return the string
                if (lineBufferIdx > 0) {
                    result = getThisLine();
                    break;
                }

                // Otherwise, ignore the extra line break characters
                continue;
            }

            // If this is a CONC line, AND if we have held-over diacritics from the previous line, pretend they're here
            // on the byte strem now
            if (holdingBinIdx > 0 && isStartOfConcLine()) {
                lineBuffer[lineBufferIdx++] = holdingBin[0];
                if (holdingBinIdx > 1) {
                    lineBuffer[lineBufferIdx++] = holdingBin[1];
                }
                holdingBinIdx = 0;
                holdingBin[0] = ' ';
                holdingBin[1] = ' ';

            }

            // Split line if it's too long, but don't split diactrics apart from their base characters
            if (lineBufferIdx >= 250 && currChar < ANSEL_DIACRITICS_BEGIN_AT) {
                result = getThisLine();
                insertSyntheticConcTag(result);
                break;
            }

            // All other characters are treated the same at this point,
            // regardless of encoding, and added as is
            lineBuffer[lineBufferIdx++] = (char) currChar;

        }
        return result;

    }

    @Override
    void cleanUp() throws IOException {
        // do nothing
    }

    /**
     * Determine what level was in use on the provided line
     * 
     * @param line
     *            the line to determine the level of
     * 
     * @return what level the supplied line was
     * @throws GedcomParserException
     *             if the line level can't be determined, because the file doesn't begin with a 1 or 2 digit number
     *             followed by a space.
     */
    private int getLevelFromLine(String line) throws GedcomParserException {
        int level = -1;
        char[] lineChars = line.toCharArray();
        if (Character.isDigit(lineChars[0])) {
            if (Character.isDigit(lineChars[1])) {
                if (lineChars[2] == ' ') {
                    level = Character.getNumericValue(lineChars[0]) * 10 + Character.getNumericValue(lineChars[1]);

                } else {
                    /*
                     * Line is too long and doesn't begin with a 1 or 2 digit number followed by a space, so we can't
                     * put in CONC's on the fly (because we don't know what level we're at)
                     */
                    throw new GedcomParserException("Line " + linesRead + " does not begin with a 1 or 2 digit number. " + "Can't split automatically.");
                }
            } else {
                if (lineChars[1] == ' ') {
                    level = Character.getNumericValue(lineChars[0]);
                } else {
                    /*
                     * Line is too long and doesn't begin with a 1 or 2 digit number followed by a space, so we can't
                     * put in CONC's on the fly (because we don't know what level we're at)
                     */
                    throw new GedcomParserException("Line " + linesRead + " does not begin with a 1 or 2 digit number. " + "Can't split automatically.");
                }
            }
        } else {
            /*
             * Line is too long and doesn't begin with a 1 or 2 digit number followed by a space, so we can't put in
             * CONC's on the fly (because we don't know what level we're at)
             */
            throw new GedcomParserException("Line " + linesRead + " does not begin with a 1 or 2 digit number. Can't split automatically.");
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
            String s = new String(lineBuffer).substring(0, lineBufferIdx - holdingBinIdx);
            result = anselHandler.toUtf16(s);
        }
        linesRead++;
        Arrays.fill(lineBuffer, ' ');
        lineBufferIdx = 0;
        return result;
    }

    /**
     * Insert synthetic CONC tags into the character buffer as if they had been there the whole time
     * 
     * @param previousLine
     *            the previous line
     * 
     * @throws GedcomParserException
     */
    private void insertSyntheticConcTag(String previousLine) throws GedcomParserException {
        int level = getLevelFromLine(previousLine);

        parser.getWarnings().add("Line " + linesRead + " exceeds max length - introducing synthetic CONC tag to split line");
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
        lineBuffer[lineBufferIdx++] = (char) currChar;
    }

    /**
     * Are we at the beginning of the text portion of a CONC line? If so, now would be the time to insert any held-over
     * characters from the previous line
     * 
     * @return true iff we at the beginning of the text portion of a CONC line
     */
    private boolean isStartOfConcLine() {
        return (lineBufferIdx >= 7 && Character.isDigit(lineBuffer[lineBufferIdx - 7]) && lineBuffer[lineBufferIdx - 6] == ' ' && lineBuffer[lineBufferIdx
                - 5] == 'C' && lineBuffer[lineBufferIdx - 4] == 'O' && lineBuffer[lineBufferIdx - 3] == 'N' && lineBuffer[lineBufferIdx - 2] == 'C'
                && lineBuffer[lineBufferIdx - 1] == ' ');
    }

}
