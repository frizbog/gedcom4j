/*
 * h * Copyright (c) 2009-2012 Matthew R. Harrah
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
 * 
 */
public class LinePieces {
    /**
     * The level of the line
     */
    public int level;
    /**
     * The ID number of the item (optional)
     */
    public String id;
    /**
     * The tag for the line
     */
    public String tag;
    /**
     * The remainder of the line after the tag
     */
    public String remainder;

    /**
     * Constructor that makes a {@link LinePieces} object from a line of text
     * input from a GEDCOM file
     * 
     * @param line
     *            a single line of text from the GEDCOM file
     */
    public LinePieces(String line) {

        // Level is always 1st character
        level = Integer.parseInt(line.substring(0, 1));

        int c = 2; // 3rd character in line

        // Take care of the id, if any
        if ('@' == (line.charAt(c))) {
            while (c < line.length() && line.charAt(c) != ' ') {
                if (id == null) {
                    id = "" + line.charAt(c++);
                } else {
                    id += line.charAt(c++);
                }
            }
            c++;
        }

        // Parse the tag
        while (c < line.length() && line.charAt(c) != ' ') {
            if (tag == null) {
                tag = "" + line.charAt(c++);
            } else {
                tag += line.charAt(c++);
            }
        }

        if (c < line.length()) {
            remainder = line.substring(c + 1);
        }
    }

    /**
     * Takes an array of strings (representing a bunch of words and terms) and
     * joins them all together with spaces between each, starting at a specific
     * element in the array and continuing to the end of the array.
     * 
     * @param parts
     *            the array of strings to be joined
     * @param startFrom
     *            the first element of the array to be joined
     * @return a string containing all the words in the array, starting at the
     *         <tt>startFrom</tt>th element and continuing to the end of the
     *         array, delimited with a single space between each element
     */
    private String joinParts(String[] parts, int startFrom) {
        StringBuilder sb = new StringBuilder();
        for (int i = startFrom; i < parts.length; i++) {
            sb.append(parts[i]);
            if (i < parts.length - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}
