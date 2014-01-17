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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A class that supports the encoding of extended ANSEL characters to/from arrays of bytes.
 * 
 * @author frizbog1
 */
final class AnselMapping {

    /**
     * The encoding mapping from characters to arrays of byte
     */
    private static Map<Character, Character> charToByte = new HashMap<Character, Character>();

    /**
     * The encoding mapping from characters to arrays of byte
     */
    private static Map<Character, Character> byteToChar = new HashMap<Character, Character>();

    static {
        charToByte.put(Character.valueOf('\u0141'), Character.valueOf((char) 0xA1));
        charToByte.put(Character.valueOf('\u00D8'), Character.valueOf((char) 0xA2));
        charToByte.put(Character.valueOf('\u0110'), Character.valueOf((char) 0xA3));
        charToByte.put(Character.valueOf('\u00DE'), Character.valueOf((char) 0xA4));
        charToByte.put(Character.valueOf('\u00C6'), Character.valueOf((char) 0xA5));
        charToByte.put(Character.valueOf('\u0152'), Character.valueOf((char) 0xA6));
        charToByte.put(Character.valueOf('\u02B9'), Character.valueOf((char) 0xA7));
        charToByte.put(Character.valueOf('\u00B7'), Character.valueOf((char) 0xA8));
        charToByte.put(Character.valueOf('\u266D'), Character.valueOf((char) 0xA9));
        charToByte.put(Character.valueOf('\u00AE'), Character.valueOf((char) 0xAA));
        charToByte.put(Character.valueOf('\u00B1'), Character.valueOf((char) 0xAB));
        charToByte.put(Character.valueOf('\u01A0'), Character.valueOf((char) 0xAC));
        charToByte.put(Character.valueOf('\u01AF'), Character.valueOf((char) 0xAD));
        charToByte.put(Character.valueOf('\u02BC'), Character.valueOf((char) 0xAE));
        charToByte.put(Character.valueOf('\u02BB'), Character.valueOf((char) 0xB0));
        charToByte.put(Character.valueOf('\u0142'), Character.valueOf((char) 0xB1));
        charToByte.put(Character.valueOf('\u00F8'), Character.valueOf((char) 0xB2));
        charToByte.put(Character.valueOf('\u0111'), Character.valueOf((char) 0xB3));
        charToByte.put(Character.valueOf('\u00FE'), Character.valueOf((char) 0xB4));
        charToByte.put(Character.valueOf('\u00E6'), Character.valueOf((char) 0xB5));
        charToByte.put(Character.valueOf('\u0153'), Character.valueOf((char) 0xB6));
        charToByte.put(Character.valueOf('\u02BA'), Character.valueOf((char) 0xB7));
        charToByte.put(Character.valueOf('\u0131'), Character.valueOf((char) 0xB8));
        charToByte.put(Character.valueOf('\u00A3'), Character.valueOf((char) 0xB9));
        charToByte.put(Character.valueOf('\u00F0'), Character.valueOf((char) 0xBA));
        charToByte.put(Character.valueOf('\u01A1'), Character.valueOf((char) 0xBC));
        charToByte.put(Character.valueOf('\u01B0'), Character.valueOf((char) 0xBD));
        charToByte.put(Character.valueOf('\u00B0'), Character.valueOf((char) 0xC0));
        charToByte.put(Character.valueOf('\u2113'), Character.valueOf((char) 0xC1));
        charToByte.put(Character.valueOf('\u2117'), Character.valueOf((char) 0xC2));
        charToByte.put(Character.valueOf('\u00A9'), Character.valueOf((char) 0xC3));
        charToByte.put(Character.valueOf('\u266F'), Character.valueOf((char) 0xC4));
        charToByte.put(Character.valueOf('\u00BF'), Character.valueOf((char) 0xC5));
        charToByte.put(Character.valueOf('\u00A1'), Character.valueOf((char) 0xC6));
        charToByte.put(Character.valueOf('\u00DF'), Character.valueOf((char) 0xCF));
        charToByte.put(Character.valueOf('\u0309'), Character.valueOf((char) 0xE0));
        charToByte.put(Character.valueOf('\u0300'), Character.valueOf((char) 0xE1));
        charToByte.put(Character.valueOf('\u0301'), Character.valueOf((char) 0xE2));
        charToByte.put(Character.valueOf('\u0302'), Character.valueOf((char) 0xE3));
        charToByte.put(Character.valueOf('\u0303'), Character.valueOf((char) 0xE4));
        charToByte.put(Character.valueOf('\u0304'), Character.valueOf((char) 0xE5));
        charToByte.put(Character.valueOf('\u0306'), Character.valueOf((char) 0xE6));
        charToByte.put(Character.valueOf('\u0307'), Character.valueOf((char) 0xE7));
        charToByte.put(Character.valueOf('\u0308'), Character.valueOf((char) 0xE8));
        charToByte.put(Character.valueOf('\u030C'), Character.valueOf((char) 0xE9));
        charToByte.put(Character.valueOf('\u030A'), Character.valueOf((char) 0xEA));
        charToByte.put(Character.valueOf('\uFE20'), Character.valueOf((char) 0xEB));
        charToByte.put(Character.valueOf('\uFE21'), Character.valueOf((char) 0xEC));
        charToByte.put(Character.valueOf('\u0315'), Character.valueOf((char) 0xED));
        charToByte.put(Character.valueOf('\u030B'), Character.valueOf((char) 0xEE));
        charToByte.put(Character.valueOf('\u0310'), Character.valueOf((char) 0xEF));
        charToByte.put(Character.valueOf('\u0327'), Character.valueOf((char) 0xF0));
        charToByte.put(Character.valueOf('\u0328'), Character.valueOf((char) 0xF1));
        charToByte.put(Character.valueOf('\u0323'), Character.valueOf((char) 0xF2));
        charToByte.put(Character.valueOf('\u0324'), Character.valueOf((char) 0xF3));
        charToByte.put(Character.valueOf('\u0325'), Character.valueOf((char) 0xF4));
        charToByte.put(Character.valueOf('\u0333'), Character.valueOf((char) 0xF5));
        charToByte.put(Character.valueOf('\u0332'), Character.valueOf((char) 0xF6));
        charToByte.put(Character.valueOf('\u0326'), Character.valueOf((char) 0xF7));
        charToByte.put(Character.valueOf('\u031C'), Character.valueOf((char) 0xF8));
        charToByte.put(Character.valueOf('\u032E'), Character.valueOf((char) 0xF9));
        charToByte.put(Character.valueOf('\uFE22'), Character.valueOf((char) 0xFA));
        charToByte.put(Character.valueOf('\uFE23'), Character.valueOf((char) 0xFB));
        charToByte.put(Character.valueOf('\u0313'), Character.valueOf((char) 0xFE));

        // Derive the reverse mapping from the original
        for (Entry<Character, Character> e : charToByte.entrySet()) {
            byteToChar.put(e.getValue(), e.getKey());
        }
    }

    /**
     * Decode an ANSEL byte into a UTF-16 Character
     * 
     * @param b
     *            the ANSEL byte (in int form)
     * @return the character (in UTF-16) represented by the byte
     */
    public static char decode(int b) {
        if (b < 0x80) {
            return (char) b;
        }
        Character result = byteToChar.get(Character.valueOf((char) b));
        if (result == null) {
            // Map unmappable characters to a question mark
            return '?';
        }
        return result.charValue();
    }

    /**
     * Encode a UTF-16 character into an ANSEL byte
     * 
     * @param c
     *            the ANSEL byte (as a char)
     * @return the character (in UTF-16) represented by the byte
     */
    public static char encode(char c) {
        Character b = charToByte.get(Character.valueOf(c));
        if (b != null) {
            return b.charValue();
        }
        return c;
    }

    /**
     * Private constructor prevents instantiation and subclassing
     */
    private AnselMapping() {
        super();
    }

}
