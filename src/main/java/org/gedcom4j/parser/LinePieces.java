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
     * Constructor that makes a {@link LinePieces} object from a line of text input from a GEDCOM file
     * 
     * @param line
     *            a single line of text from the GEDCOM file
     * @param lineNum
     *            which line in the file this is
     * @throws GedcomParserException
     *             if the line of text cannot be split into pieces
     */
    LinePieces(String line, int lineNum) throws GedcomParserException {

        currCharIdx = getLevelAndReturnIndex(line, lineNum);

        processXrefId(line);
        parseTag(line);

        if (currCharIdx < line.length()) {
            remainder = line.substring(currCharIdx + 1);
        }
    }

    /**
     * Parse the level number from the current line, then find the character after the first space (i.e., the part
     * following the level number). Assumes that the line is well-formed insofar as it begins with a 1-2 digit level
     * number followed by at least one space character, otherwise a {@link GedcomParserException} is thrown. The basis
     * for this assumption is the fact that the {@link GedcomParserHelper} class already does this checking prior to
     * breaking up the line into pieces
     * 
     * @param line
     *            the line from the file/stream
     * @param lineNum
     *            the line number
     * @return the index in the line to continue parsing after
     * @throws GedcomParserException
     *             if the line does not begin with a 1-2 digit number followed by a space
     */
    private int getLevelAndReturnIndex(String line, int lineNum) throws GedcomParserException {
        try {
            char c2 = line.charAt(1); // 2nd character in line

            int result = -1;

            if (' ' == c2) {
                // Second character in line is a space, so assume a 1-digit level
                level = Integer.parseInt(line.substring(0, 1));
                result = 2; // Continue parsing at 3rd character in line
            } else {
                // Second character in line is not a space, so assume a 2-digit level
                level = Integer.parseInt(line.substring(0, 2));
                result = 3; // Continue parsting at 4th character in line
            }
            return result;
        } catch (NumberFormatException e) {
            throw new GedcomParserException("Line " + lineNum + " does not begin with a 1 or 2 digit number for the level followed by a space: " + line);
        } catch (IndexOutOfBoundsException e) {
            throw new GedcomParserException("Line " + lineNum + " does not begin with a 1 or 2 digit number for the level followed by a space: " + line);
        }
    }

    /**
     * Parse the tag part out of the line
     * 
     * @param line
     *            the line
     */
    private void parseTag(String line) {
        // Parse the tag
        StringBuilder t = null;
        while (currCharIdx < line.length() && line.charAt(currCharIdx) != ' ') {
            if (t == null) {
                t = new StringBuilder(String.valueOf(line.charAt(currCharIdx++)));
            } else {
                t.append(String.valueOf(line.charAt(currCharIdx++)));
            }
        }
        if (t != null) {
            tag = t.toString().intern();
        }
    }

    /**
     * Process the XREF ID portion of the line
     * 
     * @param line
     *            the line
     */
    private void processXrefId(String line) {
        // Take care of the id, if any
        StringBuilder i = null;
        if ('@' == (line.charAt(currCharIdx))) {
            while (currCharIdx < line.length() && line.charAt(currCharIdx) != ' ') {
                if (i == null) {
                    i = new StringBuilder(String.valueOf(line.charAt(currCharIdx++)));
                } else {
                    i.append(String.valueOf(line.charAt(currCharIdx++)));
                }
            }
            currCharIdx++;
        }
        if (i != null) {
            id = i.toString().intern();
        }
    }
}
