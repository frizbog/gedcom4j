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
     * Constructor that makes a {@link LinePieces} object from a line of text input from a GEDCOM file
     * 
     * @param line
     *            a single line of text from the GEDCOM file
     */
    LinePieces(String line) {

        // Level is always 1st character
        level = Integer.parseInt(line.substring(0, 1));

        int c = 2; // 3rd character in line

        // Take care of the id, if any
        if ('@' == (line.charAt(c))) {
            while (c < line.length() && line.charAt(c) != ' ') {
                if (id == null) {
                    id = String.valueOf(line.charAt(c++));
                } else {
                    id += String.valueOf(line.charAt(c++));
                }
            }
            c++;
        }

        // Parse the tag
        while (c < line.length() && line.charAt(c) != ' ') {
            if (tag == null) {
                tag = String.valueOf(line.charAt(c++));
            } else {
                tag += String.valueOf(line.charAt(c++));
            }
        }

        if (c < line.length()) {
            remainder = line.substring(c + 1);
        }
    }
}
