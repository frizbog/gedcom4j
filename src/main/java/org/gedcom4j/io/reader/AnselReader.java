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

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.exception.ParserCancelledException;
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
     * The resulting list of strings containing the file data
     */
    List<String> result = new ArrayList<String>();

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
    protected List<String> load() throws IOException, GedcomParserException {

        boolean eof = false;
        while (!eof) {
            lastChar = currChar;
            currChar = byteStream.read();

            // Check for EOF
            if (currChar < 0) {
                addNonBlankLine();
                break;
            }

            // Check for carriage returns - signify EOL
            if (currChar == 0x0D) {
                addNonBlankLine();
                lineBufferIdx = 0;
                continue;
            }

            // Check for line feeds - signify EOL (unless prev char was a
            // CR)
            if (currChar == 0x0A) {
                if (lastChar != 0x0D) {
                    addNonBlankLine();
                    lineBufferIdx = 0;
                }
                continue;
            }

            // All other characters are treated the same at this point,
            // regardless of encoding, and added as is
            lineBuffer[lineBufferIdx++] = (char) currChar;

            forceLineSplitIfNeeded();

            continue;
        }
        result = anselHandler.toUtf16Lines(result);
        parser.notifyFileObservers(new FileProgressEvent(this, linesRead, true));
        return result;
    }

    /**
     * Add line to result if it is not blank. Notify listeners of progress every 100 lines.
     * 
     * @throws ParserCancelledException
     *             if the file load is cancelled
     */
    private void addNonBlankLine() throws ParserCancelledException {
        if (parser.isCancelled()) {
            throw new ParserCancelledException("File load is cancelled");
        }
        if (lineBufferIdx > 0) {
            String s = new String(lineBuffer).substring(0, lineBufferIdx);
            result.add(s);
        }
        linesRead++;
        if (linesRead % parser.getReadNotificationRate() == 0) {
            parser.notifyFileObservers(new FileProgressEvent(this, linesRead, false));
        }
    }

    /**
     * Force synthetic CONC tags for any line longer than 255 characters
     * 
     * @throws ParserCancelledException
     * @throws GedcomParserException
     */
    private void forceLineSplitIfNeeded() throws ParserCancelledException, GedcomParserException {
        if (lineBufferIdx >= 255) {
            int level = -1;
            addNonBlankLine();
            if (Character.isDigit(lineBuffer[0])) {
                if (Character.isDigit(lineBuffer[1])) {
                    if (lineBuffer[2] == ' ') {
                        level = Character.getNumericValue(lineBuffer[0]) * 10 + Character.getNumericValue(lineBuffer[1]);

                    } else {
                        /*
                         * Line is too long and doesn't begin with a 1 or 2 digit number followed by a space, so we
                         * can't put in CONC's on the fly (because we don't know what level we're at)
                         */
                        throw new GedcomParserException(
                                "Line " + linesRead + " exceeds 255 characters and does not begin with a 1 or 2 digit number. " + "Can't split automatically.");
                    }
                } else {
                    if (lineBuffer[1] == ' ') {
                        level = Character.getNumericValue(lineBuffer[0]);
                    } else {
                        /*
                         * Line is too long and doesn't begin with a 1 or 2 digit number followed by a space, so we
                         * can't put in CONC's on the fly (because we don't know what level we're at)
                         */
                        throw new GedcomParserException(
                                "Line " + linesRead + " exceeds 255 characters and does not begin with a 1 or 2 digit number. " + "Can't split automatically.");
                    }
                }
            } else {
                /*
                 * Line is too long and doesn't begin with a 1 or 2 digit number followed by a space, so we can't put in
                 * CONC's on the fly (because we don't know what level we're at)
                 */
                throw new GedcomParserException(
                        "Line " + linesRead + " exceeds 255 characters and does not begin with a 1 or 2 digit number. Can't split automatically.");
            }

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

}
