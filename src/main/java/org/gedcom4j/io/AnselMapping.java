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
 * A class that supports the encoding of extended ANSEL characters to/from arrays of bytes. Technically, supports ANSEL,
 * GEDCOM's extensions, and the MARC 21 Extended Latin character set.
 * 
 * @author frizbog1
 */
final class AnselMapping {

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
     * Return true iff the character is one of the supported combining diacritic characters, in UNICODE form.
     * 
     * @param c
     *            the character
     * @return true iff the character is one of the supported combining diacritic characters, in UNICODE form.
     */
    public static boolean isUnicodeCombiningDiacritic(char c) {
        return (c >= 0x0300 && c <= 0x0333) || (c >= 0xFE20 && c <= 0xFE23);
    }

    /**
     * The encoding mapping from characters to arrays of byte
     */
    static Map<Character, Character> charToByte = new HashMap<Character, Character>();

    /**
     * The encoding mapping from characters to arrays of byte
     */
    static Map<Character, Character> byteToChar = new HashMap<Character, Character>();

    static {
        // latin capital letter L with stroke
        charToByte.put(Character.valueOf('\u0141'), Character.valueOf((char) 0xA1));

        // latin capital letter O with stroke
        charToByte.put(Character.valueOf('\u00D8'), Character.valueOf((char) 0xA2));

        // latin capital letter D with stroke
        charToByte.put(Character.valueOf('\u0110'), Character.valueOf((char) 0xA3));

        // latin capital letter thorn
        charToByte.put(Character.valueOf('\u00DE'), Character.valueOf((char) 0xA4));

        // latin capital letter AE
        charToByte.put(Character.valueOf('\u00C6'), Character.valueOf((char) 0xA5));

        // latin capital ligature OE
        charToByte.put(Character.valueOf('\u0152'), Character.valueOf((char) 0xA6));

        // modified letter prime
        charToByte.put(Character.valueOf('\u02B9'), Character.valueOf((char) 0xA7));

        // middle dot
        charToByte.put(Character.valueOf('\u00B7'), Character.valueOf((char) 0xA8));

        // music flat sign
        charToByte.put(Character.valueOf('\u266D'), Character.valueOf((char) 0xA9));

        // registered sign
        charToByte.put(Character.valueOf('\u00AE'), Character.valueOf((char) 0xAA));

        // plus-minus sign
        charToByte.put(Character.valueOf('\u00B1'), Character.valueOf((char) 0xAB));

        // latin capital letter O with horn
        charToByte.put(Character.valueOf('\u01A0'), Character.valueOf((char) 0xAC));

        // latin capital letter U with horn
        charToByte.put(Character.valueOf('\u01AF'), Character.valueOf((char) 0xAD));

        // alif - modifier letter right half ring
        charToByte.put(Character.valueOf('\u02BC'), Character.valueOf((char) 0xAE));

        // ayn - modifier letter left half ring
        charToByte.put(Character.valueOf('\u02BB'), Character.valueOf((char) 0xB0));

        // latin small letter L with stroke
        charToByte.put(Character.valueOf('\u0142'), Character.valueOf((char) 0xB1));

        // latin small letter O with stroke
        charToByte.put(Character.valueOf('\u00F8'), Character.valueOf((char) 0xB2));

        // latin small letter D with stroke
        charToByte.put(Character.valueOf('\u0111'), Character.valueOf((char) 0xB3));

        // latin small letter thorn
        charToByte.put(Character.valueOf('\u00FE'), Character.valueOf((char) 0xB4));

        // latin small letter AE
        charToByte.put(Character.valueOf('\u00E6'), Character.valueOf((char) 0xB5));

        // latin small ligature OE
        charToByte.put(Character.valueOf('\u0153'), Character.valueOf((char) 0xB6));

        // modified letter double prime
        charToByte.put(Character.valueOf('\u02BA'), Character.valueOf((char) 0xB7));

        // latin small letter dotless i
        charToByte.put(Character.valueOf('\u0131'), Character.valueOf((char) 0xB8));

        // british pound sign
        charToByte.put(Character.valueOf('\u00A3'), Character.valueOf((char) 0xB9));

        // latin small letter eth
        charToByte.put(Character.valueOf('\u00F0'), Character.valueOf((char) 0xBA));

        // latin small letter O with horn
        charToByte.put(Character.valueOf('\u01A1'), Character.valueOf((char) 0xBC));

        // latin small letter U with horn
        charToByte.put(Character.valueOf('\u01B0'), Character.valueOf((char) 0xBD));

        // empty box
        charToByte.put(Character.valueOf('\u25A1'), Character.valueOf((char) 0xBE));

        // black box
        charToByte.put(Character.valueOf('\u25A0'), Character.valueOf((char) 0xBF));

        // degree sign
        charToByte.put(Character.valueOf('\u00B0'), Character.valueOf((char) 0xC0));

        // script small L
        charToByte.put(Character.valueOf('\u2113'), Character.valueOf((char) 0xC1));

        // sound recording copyright
        charToByte.put(Character.valueOf('\u2117'), Character.valueOf((char) 0xC2));

        // copyright sign
        charToByte.put(Character.valueOf('\u00A9'), Character.valueOf((char) 0xC3));

        // music sharp sign
        charToByte.put(Character.valueOf('\u266F'), Character.valueOf((char) 0xC4));

        // inverted question mark
        charToByte.put(Character.valueOf('\u00BF'), Character.valueOf((char) 0xC5));

        // inverted exclamation mark
        charToByte.put(Character.valueOf('\u00A1'), Character.valueOf((char) 0xC6));

        //
        charToByte.put(Character.valueOf('\u20AC'), Character.valueOf((char) 0xC8));

        // midline e
        charToByte.put(Character.valueOf('\u0065'), Character.valueOf((char) 0xCD));

        // midline o
        charToByte.put(Character.valueOf('\u006F'), Character.valueOf((char) 0xCE));

        // es zet - latin small letter sharp S
        charToByte.put(Character.valueOf('\u00DF'), Character.valueOf((char) 0xCF));

        // combining hook above
        charToByte.put(Character.valueOf('\u0309'), Character.valueOf((char) 0xE0));

        // combining grave accent
        charToByte.put(Character.valueOf('\u0300'), Character.valueOf((char) 0xE1));

        // combining acute accent
        charToByte.put(Character.valueOf('\u0301'), Character.valueOf((char) 0xE2));

        // combining circumflex accent
        charToByte.put(Character.valueOf('\u0302'), Character.valueOf((char) 0xE3));

        // combining tilde
        charToByte.put(Character.valueOf('\u0303'), Character.valueOf((char) 0xE4));

        // combining macron
        charToByte.put(Character.valueOf('\u0304'), Character.valueOf((char) 0xE5));

        // combining breve
        charToByte.put(Character.valueOf('\u0306'), Character.valueOf((char) 0xE6));

        // combining dot above
        charToByte.put(Character.valueOf('\u0307'), Character.valueOf((char) 0xE7));

        // combining diaeresis
        charToByte.put(Character.valueOf('\u0308'), Character.valueOf((char) 0xE8));

        // combining caron
        charToByte.put(Character.valueOf('\u030C'), Character.valueOf((char) 0xE9));

        // combining ring above
        charToByte.put(Character.valueOf('\u030A'), Character.valueOf((char) 0xEA));

        // combining ligature left half
        charToByte.put(Character.valueOf('\uFE20'), Character.valueOf((char) 0xEB));

        // combining ligature right half
        charToByte.put(Character.valueOf('\uFE21'), Character.valueOf((char) 0xEC));

        // combining comma above right
        charToByte.put(Character.valueOf('\u0315'), Character.valueOf((char) 0xED));

        // combining double acute accent
        charToByte.put(Character.valueOf('\u030B'), Character.valueOf((char) 0xEE));

        // combining candrabindu
        charToByte.put(Character.valueOf('\u0310'), Character.valueOf((char) 0xEF));

        // combining cedilla
        charToByte.put(Character.valueOf('\u0327'), Character.valueOf((char) 0xF0));

        // combining ogonek
        charToByte.put(Character.valueOf('\u0328'), Character.valueOf((char) 0xF1));

        // combining dot below
        charToByte.put(Character.valueOf('\u0323'), Character.valueOf((char) 0xF2));

        // combining diaeresis below
        charToByte.put(Character.valueOf('\u0324'), Character.valueOf((char) 0xF3));

        // combining ring below
        charToByte.put(Character.valueOf('\u0325'), Character.valueOf((char) 0xF4));

        // combining double low line
        charToByte.put(Character.valueOf('\u0333'), Character.valueOf((char) 0xF5));

        // combining low line
        charToByte.put(Character.valueOf('\u0332'), Character.valueOf((char) 0xF6));

        // combining comma below
        charToByte.put(Character.valueOf('\u0326'), Character.valueOf((char) 0xF7));

        // combining left half ring below
        charToByte.put(Character.valueOf('\u031C'), Character.valueOf((char) 0xF8));

        // combining breve below
        charToByte.put(Character.valueOf('\u032E'), Character.valueOf((char) 0xF9));

        // combining double tilde left half
        charToByte.put(Character.valueOf('\uFE22'), Character.valueOf((char) 0xFA));

        // combining double tilde right half
        charToByte.put(Character.valueOf('\uFE23'), Character.valueOf((char) 0xFB));

        // diacritic slash through char
        charToByte.put(Character.valueOf('\u0338'), Character.valueOf((char) 0xFC));

        // combining comma above
        charToByte.put(Character.valueOf('\u0313'), Character.valueOf((char) 0xFE));

        // Derive the reverse mapping from the original
        for (Entry<Character, Character> e : charToByte.entrySet()) {
            byteToChar.put(e.getValue(), e.getKey());
        }
    }

    /**
     * Private constructor prevents instantiation and subclassing
     */
    private AnselMapping() {
        super();
    }

}
