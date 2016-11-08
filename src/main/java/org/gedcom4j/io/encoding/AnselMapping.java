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
package org.gedcom4j.io.encoding;

/**
 * A class that supports the encoding of extended ANSEL characters to/from arrays of bytes. Technically, supports ANSEL, GEDCOM's
 * extensions, and the MARC 21 Extended Latin character set.
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
    @SuppressWarnings({ "PMD.ExcessiveMethodLength", "PMD.NcssMethodCount" })
    public static char decode(int b) {
        if (b < 0x80) {
            return (char) b;
        }
        switch (b) {
            case 0x00A1: // latin capital letter L with stroke
                return '\u0141';
            case 0x00A2: // latin capital letter O with stroke
                return '\u00D8';
            case 0x00A3: // latin capital letter D with stroke
                return '\u0110';
            case 0x00A4: // latin capital letter thorn
                return '\u00DE';
            case 0x00A5: // latin capital letter AE
                return '\u00C6';
            case 0x00A6: // latin capital ligature OE
                return '\u0152';
            case 0x00A7: // modified letter prime
                return '\u02B9';
            case 0x00A8: // middle dot
                return '\u00B7';
            case 0x00A9: // music flat sign
                return '\u266D';
            case 0x00AA: // registered sign
                return '\u00AE';
            case 0x00AB: // plus-minus sign
                return '\u00B1';
            case 0x00AC: // latin capital letter O with horn
                return '\u01A0';
            case 0x00AD: // latin capital letter U with horn
                return '\u01AF';
            case 0x00AE: // alif - modifier letter right half ring
                return '\u02BC';
            case 0x00B0: // ayn - modifier letter left half ring
                return '\u02BB';
            case 0x00B1: // latin small letter L with stroke
                return '\u0142';
            case 0x00B2: // latin small letter O with stroke
                return '\u00F8';
            case 0x00B3: // latin small letter D with stroke
                return '\u0111';
            case 0x00B4: // latin small letter thorn
                return '\u00FE';
            case 0x00B5: // latin small letter AE
                return '\u00E6';
            case 0x00B6: // latin small ligature OE
                return '\u0153';
            case 0x00B7: // modified letter double prime
                return '\u02BA';
            case 0x00B8: // latin small letter dotless i
                return '\u0131';
            case 0x00B9: // british pound sign
                return '\u00A3';
            case 0x00BA: // latin small letter eth
                return '\u00F0';
            case 0x00BC: // latin small letter O with horn
                return '\u01A1';
            case 0x00BD: // latin small letter U with horn
                return '\u01B0';
            case 0x00BE: // empty box
                return '\u25A1';
            case 0x00BF: // black box
                return '\u25A0';
            case 0x00C0: // degree sign
                return '\u00B0';
            case 0x00C1: // script small L
                return '\u2113';
            case 0x00C2: // sound recording copyright
                return '\u2117';
            case 0x00C3: // copyright sign
                return '\u00A9';
            case 0x00C4: // music sharp sign
                return '\u266F';
            case 0x00C5: // inverted question mark
                return '\u00BF';
            case 0x00C6: // inverted exclamation mark
                return '\u00A1';
            case 0x00C8:
                return '\u20AC';
            case 0x00CD: // midline e
                return '\u0065';
            case 0x00CE: // midline o
                return '\u006F';
            case 0x00CF: // es zet - latin small letter sharp S
                return '\u00DF';
            case 0x00E0: // combining hook above
                return '\u0309';
            case 0x00E1: // combining grave accent
                return '\u0300';
            case 0x00E2: // combining acute accent
                return '\u0301';
            case 0x00E3: // combining circumflex accent
                return '\u0302';
            case 0x00E4: // combining tilde
                return '\u0303';
            case 0x00E5: // combining macron
                return '\u0304';
            case 0x00E6: // combining breve
                return '\u0306';
            case 0x00E7: // combining dot above
                return '\u0307';
            case 0x00E8: // combining diaeresis
                return '\u0308';
            case 0x00E9: // combining caron
                return '\u030C';
            case 0x00EA: // combining ring above
                return '\u030A';
            case 0x00EB: // combining ligature left half
                return '\uFE20';
            case 0x00EC: // combining ligature right half
                return '\uFE21';
            case 0x00ED: // combining comma above right
                return '\u0315';
            case 0x00EE: // combining double acute accent
                return '\u030B';
            case 0x00EF: // combining candrabindu
                return '\u0310';
            case 0x00F0: // combining cedilla
                return '\u0327';
            case 0x00F1: // combining ogonek
                return '\u0328';
            case 0x00F2: // combining dot below
                return '\u0323';
            case 0x00F3: // combining diaeresis below
                return '\u0324';
            case 0x00F4: // combining ring below
                return '\u0325';
            case 0x00F5: // combining double low line
                return '\u0333';
            case 0x00F6: // combining low line
                return '\u0332';
            case 0x00F7: // combining comma below
                return '\u0326';
            case 0x00F8: // combining left half ring below
                return '\u031C';
            case 0x00F9: // combining breve below
                return '\u032E';
            case 0x00FA: // combining double tilde left half
                return '\uFE22';
            case 0x00FB: // combining double tilde right half
                return '\uFE23';
            case 0x00FC: // diacritic slash through char
                return '\u0338';
            case 0x00FE: // combining comma above
                return '\u0313';
            default:
                return '?';
        }
    }

    /**
     * Encode a UTF-16 character into an ANSEL byte
     * 
     * @param c
     *            the ANSEL byte (as a char)
     * @return the character (in UTF-16) represented by the byte
     */
    @SuppressWarnings({ "PMD.ExcessiveMethodLength", "PMD.NcssMethodCount" })
    public static char encode(char c) {
        switch (c) {
            case '\u0065': // midline e
                return '\u00CD';
            case '\u00A1': // inverted exclamation mark
                return '\u00C6';
            case '\u00A3': // british pound sign
                return '\u00B9';
            case '\u00A9': // copyright sign
                return '\u00C3';
            case '\u00AE': // registered sign
                return '\u00AA';
            case '\u00B0': // degree sign
                return '\u00C0';
            case '\u00B1': // plus-minus sign
                return '\u00AB';
            case '\u00B7': // modified letter double prime
                return '\u00A8';
            case '\u00BF': // inverted question mark
                return '\u00C5';
            case '\u00C6': // inverted exclamation mark
                return '\u00A5';
            case '\u00D8': // latin capital letter O with stroke
                return '\u00A2';
            case '\u00DE': // latin capital letter thorn
                return '\u00A4';
            case '\u00DF': // es zet - latin small letter sharp S
                return '\u00CF';
            case '\u00E6': // combining breve
                return '\u00B5';
            case '\u006F': // midline o
                return '\u00CE';
            case '\u00F0': // latin small letter eth
                return '\u00BA';
            case '\u00F8': // latin small letter O with stroke
                return '\u00B2';
            case '\u00FE': // latin small letter thorn
                return '\u00B4';
            case '\u0110': // latin capital letter D with stroke
                return '\u00A3';
            case '\u01B0': // latin small letter U with horn
                return '\u00BD';
            case '\u0111': // latin small letter D with stroke
                return '\u00B3';
            case '\u0131': // latin small letter dotless i
                return '\u00B8';
            case '\u0141': // latin capital letter L with stroke
                return '\u00A1';
            case '\u0142': // latin small letter L with stroke
                return '\u00B1';
            case '\u0152': // latin capital ligature OE
                return '\u00A6';
            case '\u0153': // latin small ligature OE
                return '\u00B6';
            case '\u01A0': // latin capital letter O with horn
                return '\u00AC';
            case '\u01A1': // latin small letter O with horn
                return '\u00BC';
            case '\u01AF': // latin capital letter U with horn
                return '\u00AD';
            case '\u02B9': // modified letter prime
                return '\u00A7';
            case '\u02BA': // modified letter double prime
                return '\u00B7';
            case '\u02BB': // ayn - modifier letter left half ring
                return '\u00B0';
            case '\u02BC': // alif - modifier letter right half ring
                return '\u00AE';
            case '\u0300': // combining grave accent
                return '\u00E1';
            case '\u0301': // combining acute accent
                return '\u00E2';
            case '\u0302': // combining circumflex accent
                return '\u00E3';
            case '\u0303': // combining tilde
                return '\u00E4';
            case '\u0304': // combining macron
                return '\u00E5';
            case '\u0306': // combining breve
                return '\u00E6';
            case '\u0307': // combining dot above
                return '\u00E7';
            case '\u0308': // combining diaeresis
                return '\u00E8';
            case '\u0309':// combining hook above
                return '\u00E0';
            case '\u030A': // combining ring above
                return '\u00EA';
            case '\u030B': // combining double acute accent
                return '\u00EE';
            case '\u030C': // combining caron
                return '\u00E9';
            case '\u0310': // combining candrabindu
                return '\u00EF';
            case '\u0313': // combining comma above
                return '\u00FE';
            case '\u0315': // combining comma above right
                return '\u00ED';
            case '\u031C': // combining left half ring below
                return '\u00F8';
            case '\u0323': // combining dot below
                return '\u00F2';
            case '\u0324': // combining diaeresis below
                return '\u00F3';
            case '\u0325': // combining ring below
                return '\u00F4';
            case '\u0326': // combining comma below
                return '\u00F7';
            case '\u0327': // combining cedilla
                return '\u00F0';
            case '\u0328': // combining ogonek
                return '\u00F1';
            case '\u032E': // combining breve below
                return '\u00F9';
            case '\u0332': // combining low line
                return '\u00F6';
            case '\u0333': // combining double low line
                return '\u00F5';
            case '\u0338': // diacritic slash through char
                return '\u00FC';
            case '\u20AC':
                return '\u00C8';
            case '\u2113': // script small L
                return '\u00C1';
            case '\u2117': // sound recording copyright
                return '\u00C2';
            case '\u25A0': // black box
                return '\u00BF';
            case '\u25A1': // empty box
                return '\u00BE';
            case '\u266D': // music flat sign
                return '\u00A9';
            case '\u266F': // music sharp sign
                return '\u00C4';
            case '\uFE20': // combining ligature left half
                return '\u00EB';
            case '\uFE21': // combining ligature right half
                return '\u00EC';
            case '\uFE22': // combining double tilde left half
                return '\u00FA';
            case '\uFE23': // combining double tilde right half
                return '\u00FB';

            default:
                return c;
        }
    }

    /**
     * Return true iff the character is one of the supported combining diacritic characters, in UNICODE form.
     * 
     * @param c
     *            the character
     * @return true iff the character is one of the supported combining diacritic characters, in UNICODE form.
     */
    public static boolean isUnicodeCombiningDiacritic(char c) {
        return c >= 0x0300 && c <= 0x0333 || c >= 0xFE20 && c <= 0xFE23;
    }

    /**
     * Private constructor prevents instantiation and subclassing
     */
    private AnselMapping() {
        // Nothing to do
    }

}
