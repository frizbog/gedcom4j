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
package org.gedcom4j.query;

import java.util.Locale;

/**
 * <p>
 * Implementation of the Soundex algorithm as described by Donald Knuth in Volume 3 of <I>The Art of Computer Programming</I>. Only
 * supports English names so use with care.
 * </p>
 * 
 * @author frizbog
 */
public final class Soundex {

    /**
     * A map of characters and their soundex digit equivalents
     */
    private static final char[] CHARACTER_MAP = { '0', // A
            '1', // B
            '2', // C
            '3', // D
            '0', // E
            '1', // F
            '2', // G
            '0', // H
            '0', // I
            '2', // J
            '2', // K
            '4', // L
            '5', // M
            '5', // N
            '0', // O
            '1', // P
            '2', // Q
            '6', // R
            '2', // S
            '3', // T
            '0', // U
            '1', // V
            '0', // W
            '2', // X
            '0', // Y
            '2' // Z
    };

    /**
     * Calculate a Soundex code from a string
     * 
     * @param s
     *            the string to compute a Soundex code for
     * 
     * @return null the soundex code, or null if the string cannot be mapped to a soundex code
     */
    public static String soundex(String s) {

        if (s == null) {
            return null;
        }

        // Soundex is case-insensitive
        String t = s.toLowerCase(Locale.US);

        StringBuilder result = new StringBuilder();
        char prevChar = '?';
        char thisChar = '?';
        // Main loop: find up to 4 chars that map.
        for (int i = 0; i < t.length(); i++) {
            if (result.length() >= 4) {
                break;
            }

            thisChar = t.charAt(i);

            if (thisChar < 'a' || thisChar > 'z' || thisChar == prevChar) {
                continue;
            }

            prevChar = thisChar;
            // First char isn't mapped to a digit
            if (result.length() == 0) {
                result.append(Character.toUpperCase(thisChar));
            } else {
                char mappedValue = CHARACTER_MAP[thisChar - 'a'];
                if (mappedValue != '0') { // Don't append vowels & vowel-like sounds
                    result.append(mappedValue);
                }
            }
        }
        if (result.length() == 0) {
            return null;
        }
        for (int i = result.length(); i < 4; i++) {
            result.append('0');
        }
        return result.toString();
    }

    /** Private constructor to prevent subclassing and instantiation */
    private Soundex() {
        // Nothing to do
    }
}
