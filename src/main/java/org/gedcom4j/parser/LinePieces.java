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

import org.gedcom4j.exception.GedcomParserException;

/**
 * A class that breaks up a line in a GEDCOM file into its component parts.
 * 
 * @author frizbog1
 */
class LinePieces {

    /**
     * The level of the line
     */
    int level;

    /**
     * The ID number of the item (optional)
     */
    String id;

    /**
     * The tag for the line
     */
    String tag;

    /**
     * The remainder of the line after the tag
     */
    String remainder;

    /**
     * The current character index into the line
     */
    private int currCharIdx;

    /**
     * The line
     */
    private final String line;

    /** The characters in the line */
    private final char[] chars;

    /**
     * Constructor that makes a {@link LinePieces} object from a line of text input from a GEDCOM file
     * 
     * @param lineToParse
     *            a single line of text from the GEDCOM file
     * @param lineNum
     *            which line in the file this is
     * @throws GedcomParserException
     *             if the line of text cannot be split into pieces
     */
    LinePieces(String lineToParse, int lineNum) throws GedcomParserException {

        line = lineToParse;

        chars = line.toCharArray();

        processLevel(lineNum);

        processXrefId();

        processTag();

        processRemainder();
    }

    /**
     * Process the level portion of the line
     * 
     * @param lineNum
     *            the line number we're currently reading
     * @throws GedcomParserException
     *             if the line does not begin with a 1 or 2 digit number for level, followed by a space
     */
    private void processLevel(int lineNum) throws GedcomParserException {
        try {
            char c2 = line.charAt(1); // 2nd character in line

            currCharIdx = -1;

            if (' ' == c2) {
                // Second character in line is a space, so assume a 1-digit level
                level = Character.getNumericValue(chars[0]);
                currCharIdx = 2; // Continue parsing at 3rd character in line
            } else {
                // Second character in line is not a space, so assume a 2-digit level
                level = Character.getNumericValue(chars[0]) * 10 + Character.getNumericValue(chars[1]);
                currCharIdx = 3; // Continue parsing at 4th character in line
            }
        } catch (NumberFormatException e) {
            throw new GedcomParserException("Line " + lineNum + " does not begin with a 1 or 2 digit number for the level followed by a space: " + line, e);
        } catch (IndexOutOfBoundsException e) {
            throw new GedcomParserException("Line " + lineNum + " does not begin with a 1 or 2 digit number for the level followed by a space: " + line, e);
        }
    }

    /**
     * Process the remainder of the line
     */
    private void processRemainder() {
        if (currCharIdx < chars.length) {
            remainder = line.substring(currCharIdx + 1);
        }
    }

    /**
     * Process the tag portion of the line
     */
    private void processTag() {
        // Parse the tag
        StringBuilder t = new StringBuilder();
        while (currCharIdx < chars.length && chars[currCharIdx] != ' ') {
            t.append(chars[currCharIdx++]);
        }
        if (t.length() > 0) {
            tag = t.toString().intern();
        }
    }

    /**
     * Process the XREF ID portion of the line, if there is one
     */
    private void processXrefId() {
        // Take care of the id, if any
        StringBuilder i = null;
        if ('@' == (chars[currCharIdx])) {
            while (currCharIdx < chars.length && chars[currCharIdx] != ' ') {
                if (i == null) {
                    i = new StringBuilder();
                }
                i.append(chars[currCharIdx++]);
            }
            currCharIdx++;
        }
        if (i != null) {
            id = i.toString().intern();
        }
    }
}
