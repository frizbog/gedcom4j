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

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This is a helper class that deals with diacritical marks for ANSEL.
 * </p>
 * <p>
 * In ANSEL, diacriticals are separate characters from the ones they modify/decorate. The base character is preceded by
 * zero, one, or two diacritical characters. These diacriticals are in the 0xE0 to 0xFE range, and when rendered do not
 * take up any horizontal space -- that being left to the base character being decorated.
 * </p>
 * <p>
 * In Java strings, which are UTF-16 (which, in the characters we are using, is essentially the same as unicode), these
 * diacritical marks, when used, come <em>after</em> the character being modified. This means that ANSEL order and Java
 * order are reversed, so something needs to be done.
 * </p>
 * <p>
 * The solution selected here, however, is not just to change the order of the bytes, but to combine the bytes, whenever
 * possible, into pre-combined unicode characters when reading, and to reverse the process for writing.
 * </p>
 * <p>
 * For example - consider the lowercase a with an acute accent: <tt>&#x00E1;</tt>.
 * </p>
 * <ul>
 * <li>ANSEL should render this as 0xE2 0x61. 0xE2 is the acute accent, and 0x61 is the lowercase a. The two characters
 * combine when rendered to look like the desired glyph, but is actually two characters that overlap.</li>
 * <li>UTF-16 (i.e., a Java string) could/should render this as 0x0061 0x0301. 0x0061 is the lowercase a, and 0x0301 is
 * the unicode version of the combining acute accent. The two characters combine as in the ANSEL scenario.</li>
 * <li>UTF-16 ideally would render this as the 0x00E1 character, which is a combined lowercase a with the acute accent
 * already part of the glyph. This is now a single character in the Java string to represent the desired glyph.</li>
 * </ul>
 * <p>
 * Special care has to be taken on CONC tag lines because the line split could occur between the diacritical at the end
 * of a line, and the letter being modified at the beginning of the next one
 * </p>
 * 
 * @author frizbog
 */
class AnselHandler {

    /**
     * The byte value at which ANSEL extended characters begin
     */
    private static final int ANSEL_EXTENDED_BEGIN_AT = 0x00A0;

    /**
     * The byte value at which combining diacritics begin in ANSEL encoding
     */
    private static final char ANSEL_DIACRITICS_BEGIN_AT = 0x00E0;

    /**
     * Converts a file (list) of UTF-16 lines into ANSEL lines. Note that some Unicode characters with diacritical marks
     * can be split into multiple ANSEL characters.
     * 
     * @param utf16Lines
     *            a list of java UTF-16 strings, each character of which will be expanded/converted to ANSEL.
     * @return a list of UTF16 strings
     */
    public List<String> toAnsel(List<String> utf16Lines) {
        List<String> result = new ArrayList<String>();
        for (String utf16 : utf16Lines) {
            result.add(toAnsel(utf16));
        }
        return result;
    }

    /**
     * Converts a file (list) of ansel lines into utf16 lines
     * 
     * @param anselLines
     *            a list of strings, each character of which represents an unconverted ANSEL byte
     * @return a list of UTF16 strings
     */
    public List<String> toUtf16(List<String> anselLines) {
        List<String> result = new ArrayList<String>();
        String prevAnsel = null;
        for (String ansel : anselLines) {
            /*
             * If concatenating from the previous line, need to see if the last character on previous line is a
             * diacritical mark modifying the beginning of this line
             */
            if (prevAnsel != null && ansel.length() >= 6 && ansel.substring(2, 6).equals("CONC") && endsWithDiacritical(prevAnsel)) {
                // Remove the last line we just added - need to adjust it and re-add it - not terribly efficient, but
                // simpler to code
                result.remove(result.size() - 1);

                // Strip the leading combining diacritical off previous line
                char d1 = prevAnsel.charAt(prevAnsel.length() - 1);
                prevAnsel = prevAnsel.substring(0, prevAnsel.length() - 1);
                char d2 = 0;
                if (endsWithDiacritical(prevAnsel)) {
                    // There was a second diacritical at the end of the line
                    d2 = prevAnsel.charAt(prevAnsel.length() - 1);
                    prevAnsel = prevAnsel.substring(0, prevAnsel.length() - 1);
                }
                // Re-add the line with the diacriticals removed
                result.add(toUtf16(prevAnsel));
                // Insert the diacriticals on the current line so they stay with the character being modified
                if (d2 == 0) {
                    ansel = ansel.substring(0, 7) + d1 + ansel.substring(7);
                } else {
                    ansel = ansel.substring(0, 7) + d2 + d1 + ansel.substring(7);
                }
                // And translate/add it
                result.add(toUtf16(ansel));
            } else {
                // Simpler case - just translate current line
                result.add(toUtf16(ansel));
            }
            prevAnsel = ansel;
        }
        return result;
    }

    /**
     * Convert a single UTF-16 string into a string of characters, each of which represents an ANSEL character
     * 
     * @param utf16
     *            a run-of-the mill java string in UTF-16 encoding, containing special characters if desired
     * @return a string, each character of which corresponds to a single byte that should be written to ANSEL stream
     */
    String toAnsel(String utf16) {
        StringBuilder ansel = new StringBuilder();
        for (int i = 0; i < utf16.length(); i++) {
            char c = utf16.charAt(i);

            // TODO - lookahead to see if the following character or two are combining diacritics, in case they weren't
            // precomposed for some reason

            if (c < ANSEL_EXTENDED_BEGIN_AT) {
                ansel.append(c);
                continue;
            }
            if (c < ANSEL_DIACRITICS_BEGIN_AT) {
                ansel.append(AnselMapping.encode(c));
                continue;
            }

            char[] breakdown = getBrokenDownGlyph(c);
            if (breakdown != null) {
                if (breakdown.length > 1 && breakdown[1] > (char) 0x0000) {
                    ansel.append(breakdown[1]); // Already encoded to ANSEL
                }
                if (breakdown.length > 2 && breakdown[2] > (char) 0x0000) {
                    ansel.append(breakdown[2]); // Already encoded to ANSEL
                }
                ansel.append(breakdown[0]);
            } else if (c < ANSEL_EXTENDED_BEGIN_AT) {
                ansel.append(c);
            } else {
                ansel.append(AnselMapping.encode(c));
            }

        }
        return ansel.toString();
    }

    /**
     * Convert an string of ANSEL bytes to UTF-16
     * 
     * @param ansel
     *            A string of ANSEL data. Each byte of ANSEL data should be represented as a single character in the
     *            string, unconverted to any unicode and without changing the order of characters.
     * @return the UTF16 string representation of the ANSEL data, after translation
     */
    String toUtf16(String ansel) {
        StringBuilder utf16 = new StringBuilder();
        int anselIndex = 0;
        char c;
        while (anselIndex < ansel.length()) {
            c = ansel.charAt(anselIndex++);
            if (c < ANSEL_DIACRITICS_BEGIN_AT || anselIndex >= ansel.length()) {
                utf16.append(AnselMapping.decode(c));
                continue;
            }
            char diacritic2 = 0; // 0 means no second diacritic
            char diacritic1 = c; // this character is actually a diacritic, so save it, and get another character
            if (anselIndex >= ansel.length()) {
                // wraps in middle of diacritic+character combination - ugh
                utf16.append(AnselMapping.decode(c));
                continue;
            }
            c = ansel.charAt(anselIndex++);
            if (c >= ANSEL_DIACRITICS_BEGIN_AT) {
                // This character is ALSO a diacritic - save it and read another character
                diacritic2 = c;
                if (anselIndex >= ansel.length()) {
                    // wraps in middle of diacritic+character combination - ugh
                    utf16.append(AnselMapping.decode(c));
                    continue;
                }
                c = ansel.charAt(anselIndex++);
            }
            char combined = getCombinedGlyph(c, diacritic1, diacritic2);
            if (combined != 0) {
                // A combined glyph was available!
                utf16.append(combined);
            } else {
                // no combined glyph available - continue to use a composite
                utf16.append(AnselMapping.decode(c));
                utf16.append(AnselMapping.decode(diacritic1));
                if (diacritic2 != 0) {
                    utf16.append(AnselMapping.decode(diacritic2));
                }
            }
        }
        return utf16.toString();
    }

    /**
     * Return true if ANSEL string ends in a combining diacritical
     * 
     * @param s
     *            the ANSEL string
     * @return true if ANSEL string ends in a combining diacritical
     */
    private boolean endsWithDiacritical(String s) {
        return (s.charAt(s.length() - 1) >= ANSEL_DIACRITICS_BEGIN_AT);
    }

    /**
     * Some unicode characters are represented in ANSEL as a combination of characters. This method returns an array of
     * those characters if such a breakdown exists, or null otherwise. All results are already encoded to ANSEL and
     * should not be encoded again.
     * 
     * @param c
     *            the unicode character to be represented
     * @return the array of characters that represent that character, or null if there is no special breakdown. The
     *         first array element is the base character. The remaining two elements are combining diacritics. An
     *         element of 0x0000 means that that character is not part of the mapping.
     */
    private char[] getBrokenDownGlyph(Character c) {
        switch (c) {

            case '\u01E3': {
                /* Latin Small Letter Ae With Macron = Latin Small Letter Ae + Combining Macron */
                return new char[] { (char) 0x00E6, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u01FD': {
                /* Latin Small Letter Ae With Acute = Latin Small Letter Ae + Combining Acute Accent */
                return new char[] { (char) 0x00E6, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u00C0': {
                /* Latin Capital Letter A With Grave = Latin Capital Letter A + Combining Grave Accent */
                return new char[] { (char) 0x0041, (char) 0x00E1, (char) 0x0000 };
            }
            case '\u00C1': {
                /* Latin Capital Letter A With Acute = Latin Capital Letter A + Combining Acute Accent */
                return new char[] { (char) 0x0041, (char) 0x00E2, (char) 0x0000 };
            }
            case '\u00C2': {
                /* Latin Capital Letter A With Circumflex = Latin Capital Letter A + Combining Circumflex Accent */
                return new char[] { (char) 0x0041, (char) 0x00E3, (char) 0x0000 };
            }
            case '\u00C3': {
                /* Latin Capital Letter A With Tilde = Latin Capital Letter A + Combining Tilde */
                return new char[] { (char) 0x0041, (char) 0x00E4, (char) 0x0000 };
            }
            case '\u00C4': {
                /* Latin Capital Letter A With Diaeresis = Latin Capital Letter A + Combining Diaeresis */
                return new char[] { (char) 0x0041, (char) 0x00E8, (char) 0x0000 };
            }
            case '\u00C5': {
                /* Latin Capital Letter A With Ring Above = Latin Capital Letter A + Combining Ring Above */
                return new char[] { (char) 0x0041, (char) 0x00EA, (char) 0x0000 };
            }
            case '\u0100': {
                /* Latin Capital Letter A With Macron = Latin Capital Letter A + Combining Macron */
                return new char[] { (char) 0x0041, (char) 0x00E5, (char) 0x0000 };
            }
            case '\u0102': {
                /* Latin Capital Letter A With Breve = Latin Capital Letter A + Combining Breve */
                return new char[] { (char) 0x0041, (char) 0x00E6, (char) 0x0000 };
            }
            case '\u0104': {
                /* Latin Capital Letter A With Ogonek = Latin Capital Letter A + Combining Ogonek */
                return new char[] { (char) 0x0041, (char) 0x00F1, (char) 0x0000 };
            }
            case '\u01CD': {
                /* Latin Capital Letter A With Caron = Latin Capital Letter A + Combining Caron */
                return new char[] { (char) 0x0041, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1EA6': {
                /*
                 * Latin Capital Letter A With Circumflex And Grave = Latin Capital Letter A + Combining Circumflex
                 * Accent + Combining Grave Accent
                 */
                return new char[] { (char) 0x0041, (char) 0x00E0, (char) 0x00E1 };
            }
            case '\u1EB0': {
                /*
                 * Latin Capital Letter A With Breve And Grave = Latin Capital Letter A + Combining Breve + Combining
                 * Grave Accent
                 */
                return new char[] { (char) 0x0041, (char) 0x00E0, (char) 0x00E1 };
            }
            case '\u0200': {
                /* Latin Capital Letter A With Double Grave = Latin Capital Letter A + Combining Double Grave Accent */
                return new char[] { (char) 0x0041, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0202': {
                /* Latin Capital Letter A With Inverted Breve = Latin Capital Letter A + Combining Inverted Breve */
                return new char[] { (char) 0x0041, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E00': {
                /* Latin Capital Letter A With Ring Below = Latin Capital Letter A + Combining Ring Below */
                return new char[] { (char) 0x0041, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u01FA': {
                /*
                 * Latin Capital Letter A With Ring Above And Acute = Latin Capital Letter A + Combining Ring Above +
                 * Combining Acute Accent
                 */
                return new char[] { (char) 0x0041, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1EA4': {
                /*
                 * Latin Capital Letter A With Circumflex And Acute = Latin Capital Letter A + Combining Circumflex
                 * Accent + Combining Acute Accent
                 */
                return new char[] { (char) 0x0041, (char) 0x00E0, (char) 0x00E2 };
            }
            case '\u1EAE': {
                /*
                 * Latin Capital Letter A With Breve And Acute = Latin Capital Letter A + Combining Breve + Combining
                 * Acute Accent
                 */
                return new char[] { (char) 0x0041, (char) 0x00E0, (char) 0x00E2 };
            }
            case '\u1EAA': {
                /*
                 * Latin Capital Letter A With Circumflex And Tilde = Latin Capital Letter A + Combining Circumflex
                 * Accent + Combining Tilde
                 */
                return new char[] { (char) 0x0041, (char) 0x00E0, (char) 0x00E4 };
            }
            case '\u1EA0': {
                /* Latin Capital Letter A With Dot Below = Latin Capital Letter A + Combining Dot Below */
                return new char[] { (char) 0x0041, (char) 0x00F2, (char) 0x00E4 };
            }
            case '\u1EA2': {
                /* Latin Capital Letter A With Hook Above = Latin Capital Letter A + Combining Hook Above */
                return new char[] { (char) 0x0041, (char) 0x00E0, (char) 0x00E4 };
            }
            case '\u1EB4': {
                /*
                 * Latin Capital Letter A With Breve And Tilde = Latin Capital Letter A + Combining Breve + Combining
                 * Tilde
                 */
                return new char[] { (char) 0x0041, (char) 0x00E0, (char) 0x00E4 };
            }
            case '\u01DE': {
                /*
                 * Latin Capital Letter A With Diaeresis And Macron = Latin Capital Letter A + Combining Diaeresis +
                 * Combining Macron
                 */
                return new char[] { (char) 0x0041, (char) 0x00E7, (char) 0x00E5 };
            }
            case '\u01E0': {
                /*
                 * Latin Capital Letter A With Dot Above And Macron = Latin Capital Letter A + Combining Dot Above +
                 * Combining Macron
                 */
                return new char[] { (char) 0x0041, (char) 0x00E7, (char) 0x00E5 };
            }
            case '\u1EA8': {
                /*
                 * Latin Capital Letter A With Circumflex And Hook Above = Latin Capital Letter A + Combining Circumflex
                 * Accent + Combining Hook Above
                 */
                return new char[] { (char) 0x0041, (char) 0x00E0, (char) 0x00E0 };
            }
            case '\u1EB2': {
                /*
                 * Latin Capital Letter A With Breve And Hook Above = Latin Capital Letter A + Combining Breve +
                 * Combining Hook Above
                 */
                return new char[] { (char) 0x0041, (char) 0x00E0, (char) 0x00E0 };
            }
            case '\u1EAC': {
                /*
                 * Latin Capital Letter A With Circumflex And Dot Below = Latin Capital Letter A + Combining Circumflex
                 * Accent + Combining Dot Below
                 */
                return new char[] { (char) 0x0041, (char) 0x00E0, (char) 0x00F2 };
            }
            case '\u1EB6': {
                /*
                 * Latin Capital Letter A With Breve And Dot Below = Latin Capital Letter A + Combining Breve +
                 * Combining Dot Below
                 */
                return new char[] { (char) 0x0041, (char) 0x00E0, (char) 0x00F2 };
            }
            case '\u1E02': {
                /* Latin Capital Letter B With Dot Above = Latin Capital Letter B + Combining Dot Above */
                return new char[] { (char) 0x0042, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E04': {
                /* Latin Capital Letter B With Dot Below = Latin Capital Letter B + Combining Dot Below */
                return new char[] { (char) 0x0042, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E06': {
                /* Latin Capital Letter B With Line Below = Latin Capital Letter B + Combining Low Line */
                return new char[] { (char) 0x0042, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u00C7': {
                /* Latin Capital Letter C With Cedilla = Latin Capital Letter C + Combining Cedilla */
                return new char[] { (char) 0x0043, (char) 0x00F0, (char) 0x0000 };
            }
            case '\u0106': {
                /* Latin Capital Letter C With Acute = Latin Capital Letter C + Combining Acute Accent */
                return new char[] { (char) 0x0043, (char) 0x00E2, (char) 0x0000 };
            }
            case '\u0108': {
                /* Latin Capital Letter C With Circumflex = Latin Capital Letter C + Combining Circumflex Accent */
                return new char[] { (char) 0x0043, (char) 0x00E3, (char) 0x0000 };
            }
            case '\u010A': {
                /* Latin Capital Letter C With Dot Above = Latin Capital Letter C + Combining Dot Above */
                return new char[] { (char) 0x0043, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u010C': {
                /* Latin Capital Letter C With Caron = Latin Capital Letter C + Combining Caron */
                return new char[] { (char) 0x0043, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E08': {
                /*
                 * Latin Capital Letter C With Cedilla And Acute = Latin Capital Letter C + Combining Cedilla +
                 * Combining Acute Accent
                 */
                return new char[] { (char) 0x0043, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u010E': {
                /* Latin Capital Letter D With Caron = Latin Capital Letter D + Combining Caron */
                return new char[] { (char) 0x0044, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E0A': {
                /* Latin Capital Letter D With Dot Above = Latin Capital Letter D + Combining Dot Above */
                return new char[] { (char) 0x0044, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E0C': {
                /* Latin Capital Letter D With Dot Below = Latin Capital Letter D + Combining Dot Below */
                return new char[] { (char) 0x0044, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E0E': {
                /* Latin Capital Letter D With Line Below = Latin Capital Letter D + Combining Low Line */
                return new char[] { (char) 0x0044, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E10': {
                /* Latin Capital Letter D With Cedilla = Latin Capital Letter D + Combining Cedilla */
                return new char[] { (char) 0x0044, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E12': {
                /*
                 * Latin Capital Letter D With Circumflex Below = Latin Capital Letter D + Combining Circumflex Accent
                 * Below
                 */
                return new char[] { (char) 0x0044, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u00C8': {
                /* Latin Capital Letter E With Grave = Latin Capital Letter E + Combining Grave Accent */
                return new char[] { (char) 0x0045, (char) 0x00E1, (char) 0x0000 };
            }
            case '\u00C9': {
                /* Latin Capital Letter E With Acute = Latin Capital Letter E + Combining Acute Accent */
                return new char[] { (char) 0x0045, (char) 0x00E2, (char) 0x0000 };
            }
            case '\u00CA': {
                /* Latin Capital Letter E With Circumflex = Latin Capital Letter E + Combining Circumflex Accent */
                return new char[] { (char) 0x0045, (char) 0x00E3, (char) 0x0000 };
            }
            case '\u00CB': {
                /* Latin Capital Letter E With Diaeresis = Latin Capital Letter E + Combining Diaeresis */
                return new char[] { (char) 0x0045, (char) 0x00E8, (char) 0x0000 };
            }
            case '\u0112': {
                /* Latin Capital Letter E With Macron = Latin Capital Letter E + Combining Macron */
                return new char[] { (char) 0x0045, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0114': {
                /* Latin Capital Letter E With Breve = Latin Capital Letter E + Combining Breve */
                return new char[] { (char) 0x0045, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0116': {
                /* Latin Capital Letter E With Dot Above = Latin Capital Letter E + Combining Dot Above */
                return new char[] { (char) 0x0045, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0118': {
                /* Latin Capital Letter E With Ogonek = Latin Capital Letter E + Combining Ogonek */
                return new char[] { (char) 0x0045, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u011A': {
                /* Latin Capital Letter E With Caron = Latin Capital Letter E + Combining Caron */
                return new char[] { (char) 0x0045, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E14': {
                /*
                 * Latin Capital Letter E With Macron And Grave = Latin Capital Letter E + Combining Macron + Combining
                 * Grave Accent
                 */
                return new char[] { (char) 0x0045, (char) 0x00E7, (char) 0x00E1 };
            }
            case '\u1EC0': {
                /*
                 * Latin Capital Letter E With Circumflex And Grave = Latin Capital Letter E + Combining Circumflex
                 * Accent + Combining Grave Accent
                 */
                return new char[] { (char) 0x0045, (char) 0x00E0, (char) 0x00E1 };
            }
            case '\u0204': {
                /* Latin Capital Letter E With Double Grave = Latin Capital Letter E + Combining Double Grave Accent */
                return new char[] { (char) 0x0045, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0206': {
                /* Latin Capital Letter E With Inverted Breve = Latin Capital Letter E + Combining Inverted Breve */
                return new char[] { (char) 0x0045, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E18': {
                /*
                 * Latin Capital Letter E With Circumflex Below = Latin Capital Letter E + Combining Circumflex Accent
                 * Below
                 */
                return new char[] { (char) 0x0045, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E1A': {
                /* Latin Capital Letter E With Tilde Below = Latin Capital Letter E + Combining Tilde Below */
                return new char[] { (char) 0x0045, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E16': {
                /*
                 * Latin Capital Letter E With Macron And Acute = Latin Capital Letter E + Combining Macron + Combining
                 * Acute Accent
                 */
                return new char[] { (char) 0x0045, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1EBE': {
                /*
                 * Latin Capital Letter E With Circumflex And Acute = Latin Capital Letter E + Combining Circumflex
                 * Accent + Combining Acute Accent
                 */
                return new char[] { (char) 0x0045, (char) 0x00E0, (char) 0x00E2 };
            }
            case '\u1EC4': {
                /*
                 * Latin Capital Letter E With Circumflex And Tilde = Latin Capital Letter E + Combining Circumflex
                 * Accent + Combining Tilde
                 */
                return new char[] { (char) 0x0045, (char) 0x00E0, (char) 0x00E4 };
            }
            case '\u1EB8': {
                /* Latin Capital Letter E With Dot Below = Latin Capital Letter E + Combining Dot Below */
                return new char[] { (char) 0x0045, (char) 0x00E0, (char) 0x00E5 };
            }
            case '\u1EBA': {
                /* Latin Capital Letter E With Hook Above = Latin Capital Letter E + Combining Hook Above */
                return new char[] { (char) 0x0045, (char) 0x00E0, (char) 0x00E5 };
            }
            case '\u1EBC': {
                /* Latin Capital Letter E With Tilde = Latin Capital Letter E + Combining Tilde */
                return new char[] { (char) 0x0045, (char) 0x00E0, (char) 0x00E5 };
            }
            case '\u1E1C': {
                /*
                 * Latin Capital Letter E With Cedilla And Breve = Latin Capital Letter E + Combining Cedilla +
                 * Combining Breve
                 */
                return new char[] { (char) 0x0045, (char) 0x00E7, (char) 0x00E6 };
            }
            case '\u1EC2': {
                /*
                 * Latin Capital Letter E With Circumflex And Hook Above = Latin Capital Letter E + Combining Circumflex
                 * Accent + Combining Hook Above
                 */
                return new char[] { (char) 0x0045, (char) 0x00E0, (char) 0x00E0 };
            }
            case '\u1EC6': {
                /*
                 * Latin Capital Letter E With Circumflex And Dot Below = Latin Capital Letter E + Combining Circumflex
                 * Accent + Combining Dot Below
                 */
                return new char[] { (char) 0x0045, (char) 0x00E0, (char) 0x00F2 };
            }
            case '\u1E1E': {
                /* Latin Capital Letter F With Dot Above = Latin Capital Letter F + Combining Dot Above */
                return new char[] { (char) 0x0046, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u011C': {
                /* Latin Capital Letter G With Circumflex = Latin Capital Letter G + Combining Circumflex Accent */
                return new char[] { (char) 0x0047, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u011E': {
                /* Latin Capital Letter G With Breve = Latin Capital Letter G + Combining Breve */
                return new char[] { (char) 0x0047, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0120': {
                /* Latin Capital Letter G With Dot Above = Latin Capital Letter G + Combining Dot Above */
                return new char[] { (char) 0x0047, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0122': {
                /* Latin Capital Letter G With Cedilla = Latin Capital Letter G + Combining Cedilla */
                return new char[] { (char) 0x0047, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u01E6': {
                /* Latin Capital Letter G With Caron = Latin Capital Letter G + Combining Caron */
                return new char[] { (char) 0x0047, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u01F4': {
                /* Latin Capital Letter G With Acute = Latin Capital Letter G + Combining Acute Accent */
                return new char[] { (char) 0x0047, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E20': {
                /* Latin Capital Letter G With Macron = Latin Capital Letter G + Combining Macron */
                return new char[] { (char) 0x0047, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u0124': {
                /* Latin Capital Letter H With Circumflex = Latin Capital Letter H + Combining Circumflex Accent */
                return new char[] { (char) 0x0048, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E22': {
                /* Latin Capital Letter H With Dot Above = Latin Capital Letter H + Combining Dot Above */
                return new char[] { (char) 0x0048, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E24': {
                /* Latin Capital Letter H With Dot Below = Latin Capital Letter H + Combining Dot Below */
                return new char[] { (char) 0x0048, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E26': {
                /* Latin Capital Letter H With Diaeresis = Latin Capital Letter H + Combining Diaeresis */
                return new char[] { (char) 0x0048, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E28': {
                /* Latin Capital Letter H With Cedilla = Latin Capital Letter H + Combining Cedilla */
                return new char[] { (char) 0x0048, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E2A': {
                /* Latin Capital Letter H With Breve Below = Latin Capital Letter H + Combining Breve Below */
                return new char[] { (char) 0x0048, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u00CC': {
                /* Latin Capital Letter I With Grave = Latin Capital Letter I + Combining Grave Accent */
                return new char[] { (char) 0x0049, (char) 0x00E1, (char) 0x0000 };
            }
            case '\u00CD': {
                /* Latin Capital Letter I With Acute = Latin Capital Letter I + Combining Acute Accent */
                return new char[] { (char) 0x0049, (char) 0x00E2, (char) 0x0000 };
            }
            case '\u00CE': {
                /* Latin Capital Letter I With Circumflex = Latin Capital Letter I + Combining Circumflex Accent */
                return new char[] { (char) 0x0049, (char) 0x00E3, (char) 0x0000 };
            }
            case '\u00CF': {
                /* Latin Capital Letter I With Diaeresis = Latin Capital Letter I + Combining Diaeresis */
                return new char[] { (char) 0x0049, (char) 0x00E8, (char) 0x0000 };
            }
            case '\u0128': {
                /* Latin Capital Letter I With Tilde = Latin Capital Letter I + Combining Tilde */
                return new char[] { (char) 0x0049, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u012A': {
                /* Latin Capital Letter I With Macron = Latin Capital Letter I + Combining Macron */
                return new char[] { (char) 0x0049, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u012C': {
                /* Latin Capital Letter I With Breve = Latin Capital Letter I + Combining Breve */
                return new char[] { (char) 0x0049, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u012E': {
                /* Latin Capital Letter I With Ogonek = Latin Capital Letter I + Combining Ogonek */
                return new char[] { (char) 0x0049, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0130': {
                /* Latin Capital Letter I With Dot Above = Latin Capital Letter I + Combining Dot Above */
                return new char[] { (char) 0x0049, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u01CF': {
                /* Latin Capital Letter I With Caron = Latin Capital Letter I + Combining Caron */
                return new char[] { (char) 0x0049, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0208': {
                /* Latin Capital Letter I With Double Grave = Latin Capital Letter I + Combining Double Grave Accent */
                return new char[] { (char) 0x0049, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u020A': {
                /* Latin Capital Letter I With Inverted Breve = Latin Capital Letter I + Combining Inverted Breve */
                return new char[] { (char) 0x0049, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E2C': {
                /* Latin Capital Letter I With Tilde Below = Latin Capital Letter I + Combining Tilde Below */
                return new char[] { (char) 0x0049, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E2E': {
                /*
                 * Latin Capital Letter I With Diaeresis And Acute = Latin Capital Letter I + Combining Diaeresis +
                 * Combining Acute Accent
                 */
                return new char[] { (char) 0x0049, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1EC8': {
                /* Latin Capital Letter I With Hook Above = Latin Capital Letter I + Combining Hook Above */
                return new char[] { (char) 0x0049, (char) 0x00E0, (char) 0x0000 };
            }
            case '\u1ECA': {
                /* Latin Capital Letter I With Dot Below = Latin Capital Letter I + Combining Dot Below */
                return new char[] { (char) 0x0049, (char) 0x00E0, (char) 0x0000 };
            }
            case '\u1E54': {
                /* Latin Capital Letter P With Acute = Latin Capital Letter P + Combining Acute Accent */
                return new char[] { (char) 0x0050, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E56': {
                /* Latin Capital Letter P With Dot Above = Latin Capital Letter P + Combining Dot Above */
                return new char[] { (char) 0x0050, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u0154': {
                /* Latin Capital Letter R With Acute = Latin Capital Letter R + Combining Acute Accent */
                return new char[] { (char) 0x0052, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0156': {
                /* Latin Capital Letter R With Cedilla = Latin Capital Letter R + Combining Cedilla */
                return new char[] { (char) 0x0052, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0158': {
                /* Latin Capital Letter R With Caron = Latin Capital Letter R + Combining Caron */
                return new char[] { (char) 0x0052, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0210': {
                /* Latin Capital Letter R With Double Grave = Latin Capital Letter R + Combining Double Grave Accent */
                return new char[] { (char) 0x0052, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0212': {
                /* Latin Capital Letter R With Inverted Breve = Latin Capital Letter R + Combining Inverted Breve */
                return new char[] { (char) 0x0052, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E58': {
                /* Latin Capital Letter R With Dot Above = Latin Capital Letter R + Combining Dot Above */
                return new char[] { (char) 0x0052, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E5A': {
                /* Latin Capital Letter R With Dot Below = Latin Capital Letter R + Combining Dot Below */
                return new char[] { (char) 0x0052, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E5E': {
                /* Latin Capital Letter R With Line Below = Latin Capital Letter R + Combining Low Line */
                return new char[] { (char) 0x0052, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E5C': {
                /*
                 * Latin Capital Letter R With Dot Below And Macron = Latin Capital Letter R + Combining Dot Below +
                 * Combining Macron
                 */
                return new char[] { (char) 0x0052, (char) 0x00E7, (char) 0x00E5 };
            }
            case '\u015A': {
                /* Latin Capital Letter S With Acute = Latin Capital Letter S + Combining Acute Accent */
                return new char[] { (char) 0x0053, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u015C': {
                /* Latin Capital Letter S With Circumflex = Latin Capital Letter S + Combining Circumflex Accent */
                return new char[] { (char) 0x0053, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u015E': {
                /* Latin Capital Letter S With Cedilla = Latin Capital Letter S + Combining Cedilla */
                return new char[] { (char) 0x0053, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0160': {
                /* Latin Capital Letter S With Caron = Latin Capital Letter S + Combining Caron */
                return new char[] { (char) 0x0053, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E60': {
                /* Latin Capital Letter S With Dot Above = Latin Capital Letter S + Combining Dot Above */
                return new char[] { (char) 0x0053, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E62': {
                /* Latin Capital Letter S With Dot Below = Latin Capital Letter S + Combining Dot Below */
                return new char[] { (char) 0x0053, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E64': {
                /*
                 * Latin Capital Letter S With Acute And Dot Above = Latin Capital Letter S + Combining Acute Accent +
                 * Combining Dot Above
                 */
                return new char[] { (char) 0x0053, (char) 0x00E7, (char) 0x00E7 };
            }
            case '\u1E66': {
                /*
                 * Latin Capital Letter S With Caron And Dot Above = Latin Capital Letter S + Combining Caron +
                 * Combining Dot Above
                 */
                return new char[] { (char) 0x0053, (char) 0x00E7, (char) 0x00E7 };
            }
            case '\u1E68': {
                /*
                 * Latin Capital Letter S With Dot Below And Dot Above = Latin Capital Letter S + Combining Dot Below +
                 * Combining Dot Above
                 */
                return new char[] { (char) 0x0053, (char) 0x00E7, (char) 0x00E7 };
            }
            case '\u0162': {
                /* Latin Capital Letter T With Cedilla = Latin Capital Letter T + Combining Cedilla */
                return new char[] { (char) 0x0054, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0164': {
                /* Latin Capital Letter T With Caron = Latin Capital Letter T + Combining Caron */
                return new char[] { (char) 0x0054, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E6A': {
                /* Latin Capital Letter T With Dot Above = Latin Capital Letter T + Combining Dot Above */
                return new char[] { (char) 0x0054, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E6C': {
                /* Latin Capital Letter T With Dot Below = Latin Capital Letter T + Combining Dot Below */
                return new char[] { (char) 0x0054, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E6E': {
                /* Latin Capital Letter T With Line Below = Latin Capital Letter T + Combining Low Line */
                return new char[] { (char) 0x0054, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E70': {
                /*
                 * Latin Capital Letter T With Circumflex Below = Latin Capital Letter T + Combining Circumflex Accent
                 * Below
                 */
                return new char[] { (char) 0x0054, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u00D9': {
                /* Latin Capital Letter U With Grave = Latin Capital Letter U + Combining Grave Accent */
                return new char[] { (char) 0x0055, (char) 0x00E1, (char) 0x0000 };
            }
            case '\u00DA': {
                /* Latin Capital Letter U With Acute = Latin Capital Letter U + Combining Acute Accent */
                return new char[] { (char) 0x0055, (char) 0x00E2, (char) 0x0000 };
            }
            case '\u00DB': {
                /* Latin Capital Letter U With Circumflex = Latin Capital Letter U + Combining Circumflex Accent */
                return new char[] { (char) 0x0055, (char) 0x00E3, (char) 0x0000 };
            }
            case '\u00DC': {
                /* Latin Capital Letter U With Diaeresis = Latin Capital Letter U + Combining Diaeresis */
                return new char[] { (char) 0x0055, (char) 0x00E8, (char) 0x0000 };
            }
            case '\u0168': {
                /* Latin Capital Letter U With Tilde = Latin Capital Letter U + Combining Tilde */
                return new char[] { (char) 0x0055, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u016A': {
                /* Latin Capital Letter U With Macron = Latin Capital Letter U + Combining Macron */
                return new char[] { (char) 0x0055, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u016C': {
                /* Latin Capital Letter U With Breve = Latin Capital Letter U + Combining Breve */
                return new char[] { (char) 0x0055, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u016E': {
                /* Latin Capital Letter U With Ring Above = Latin Capital Letter U + Combining Ring Above */
                return new char[] { (char) 0x0055, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0170': {
                /* Latin Capital Letter U With Double Acute = Latin Capital Letter U + Combining Double Acute Accent */
                return new char[] { (char) 0x0055, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0172': {
                /* Latin Capital Letter U With Ogonek = Latin Capital Letter U + Combining Ogonek */
                return new char[] { (char) 0x0055, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u01AF': {
                /* Latin Capital Letter U With Horn = Latin Capital Letter U + Combining Horn */
                return new char[] { (char) 0x00AD, (char) 0x0000, (char) 0x0000 };
            }
            case '\u01D3': {
                /* Latin Capital Letter U With Caron = Latin Capital Letter U + Combining Caron */
                return new char[] { (char) 0x0055, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u01DB': {
                /*
                 * Latin Capital Letter U With Diaeresis And Grave = Latin Capital Letter U + Combining Diaeresis +
                 * Combining Grave Accent
                 */
                return new char[] { (char) 0x0055, (char) 0x00E7, (char) 0x00E1 };
            }
            case '\u0214': {
                /* Latin Capital Letter U With Double Grave = Latin Capital Letter U + Combining Double Grave Accent */
                return new char[] { (char) 0x0055, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0216': {
                /* Latin Capital Letter U With Inverted Breve = Latin Capital Letter U + Combining Inverted Breve */
                return new char[] { (char) 0x0055, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1EEA': {
                /*
                 * Latin Capital Letter U With Horn And Grave = Latin Capital Letter U + Combining Horn + Combining
                 * Grave Accent
                 */
                return new char[] { (char) 0x0055, (char) 0x00E0, (char) 0x00E1 };
            }
            case '\u01D7': {
                /*
                 * Latin Capital Letter U With Diaeresis And Acute = Latin Capital Letter U + Combining Diaeresis +
                 * Combining Acute Accent
                 */
                return new char[] { (char) 0x0055, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E78': {
                /*
                 * Latin Capital Letter U With Tilde And Acute = Latin Capital Letter U + Combining Tilde + Combining
                 * Acute Accent
                 */
                return new char[] { (char) 0x0055, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E72': {
                /* Latin Capital Letter U With Diaeresis Below = Latin Capital Letter U + Combining Diaeresis Below */
                return new char[] { (char) 0x0055, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E74': {
                /* Latin Capital Letter U With Tilde Below = Latin Capital Letter U + Combining Tilde Below */
                return new char[] { (char) 0x0055, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E76': {
                /*
                 * Latin Capital Letter U With Circumflex Below = Latin Capital Letter U + Combining Circumflex Accent
                 * Below
                 */
                return new char[] { (char) 0x0055, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1EE8': {
                /*
                 * Latin Capital Letter U With Horn And Acute = Latin Capital Letter U + Combining Horn + Combining
                 * Acute Accent
                 */
                return new char[] { (char) 0x0055, (char) 0x00E0, (char) 0x00E2 };
            }
            case '\u1EEE': {
                /*
                 * Latin Capital Letter U With Horn And Tilde = Latin Capital Letter U + Combining Horn + Combining
                 * Tilde
                 */
                return new char[] { (char) 0x0055, (char) 0x00E0, (char) 0x00E4 };
            }
            case '\u01D5': {
                /*
                 * Latin Capital Letter U With Diaeresis And Macron = Latin Capital Letter U + Combining Diaeresis +
                 * Combining Macron
                 */
                return new char[] { (char) 0x0055, (char) 0x00E7, (char) 0x00E5 };
            }
            case '\u1E7A': {
                /*
                 * Latin Capital Letter U With Macron And Diaeresis = Latin Capital Letter U + Combining Macron +
                 * Combining Diaeresis
                 */
                return new char[] { (char) 0x0055, (char) 0x00E7, (char) 0x00E8 };
            }
            case '\u1EEC': {
                /*
                 * Latin Capital Letter U With Horn And Hook Above = Latin Capital Letter U + Combining Horn + Combining
                 * Hook Above
                 */
                return new char[] { (char) 0x0055, (char) 0x00E0, (char) 0x00E0 };
            }
            case '\u1EE4': {
                /* Latin Capital Letter U With Dot Below = Latin Capital Letter U + Combining Dot Below */
                return new char[] { (char) 0x0055, (char) 0x00E0, (char) 0x00F2 };
            }
            case '\u1EE6': {
                /* Latin Capital Letter U With Hook Above = Latin Capital Letter U + Combining Hook Above */
                return new char[] { (char) 0x0055, (char) 0x00E0, (char) 0x00F2 };
            }
            case '\u1EF0': {
                /*
                 * Latin Capital Letter U With Horn And Dot Below = Latin Capital Letter U + Combining Horn + Combining
                 * Dot Below
                 */
                return new char[] { (char) 0x0055, (char) 0x00E0, (char) 0x00F2 };
            }
            case '\u01D9': {
                /*
                 * Latin Capital Letter U With Diaeresis And Caron = Latin Capital Letter U + Combining Diaeresis +
                 * Combining Caron
                 */
                return new char[] { (char) 0x0055, (char) 0x00E7, (char) 0x00E9 };
            }
            case '\u1E7C': {
                /* Latin Capital Letter V With Tilde = Latin Capital Letter V + Combining Tilde */
                return new char[] { (char) 0x0056, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u1E7E': {
                /* Latin Capital Letter V With Dot Below = Latin Capital Letter V + Combining Dot Below */
                return new char[] { (char) 0x0056, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u0174': {
                /* Latin Capital Letter W With Circumflex = Latin Capital Letter W + Combining Circumflex Accent */
                return new char[] { (char) 0x0057, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E80': {
                /* Latin Capital Letter W With Grave = Latin Capital Letter W + Combining Grave Accent */
                return new char[] { (char) 0x0057, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u1E82': {
                /* Latin Capital Letter W With Acute = Latin Capital Letter W + Combining Acute Accent */
                return new char[] { (char) 0x0057, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u1E84': {
                /* Latin Capital Letter W With Diaeresis = Latin Capital Letter W + Combining Diaeresis */
                return new char[] { (char) 0x0057, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u1E86': {
                /* Latin Capital Letter W With Dot Above = Latin Capital Letter W + Combining Dot Above */
                return new char[] { (char) 0x0057, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u1E88': {
                /* Latin Capital Letter W With Dot Below = Latin Capital Letter W + Combining Dot Below */
                return new char[] { (char) 0x0057, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u1E8A': {
                /* Latin Capital Letter X With Dot Above = Latin Capital Letter X + Combining Dot Above */
                return new char[] { (char) 0x0058, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u1E8C': {
                /* Latin Capital Letter X With Diaeresis = Latin Capital Letter X + Combining Diaeresis */
                return new char[] { (char) 0x0058, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u00DD': {
                /* Latin Capital Letter Y With Acute = Latin Capital Letter Y + Combining Acute Accent */
                return new char[] { (char) 0x0059, (char) 0x00E2, (char) 0x0000 };
            }
            case '\u0176': {
                /* Latin Capital Letter Y With Circumflex = Latin Capital Letter Y + Combining Circumflex Accent */
                return new char[] { (char) 0x0059, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0178': {
                /* Latin Capital Letter Y With Diaeresis = Latin Capital Letter Y + Combining Diaeresis */
                return new char[] { (char) 0x0059, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E8E': {
                /* Latin Capital Letter Y With Dot Above = Latin Capital Letter Y + Combining Dot Above */
                return new char[] { (char) 0x0059, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u1EF2': {
                /* Latin Capital Letter Y With Grave = Latin Capital Letter Y + Combining Grave Accent */
                return new char[] { (char) 0x0059, (char) 0x00E0, (char) 0x0000 };
            }
            case '\u1EF4': {
                /* Latin Capital Letter Y With Dot Below = Latin Capital Letter Y + Combining Dot Below */
                return new char[] { (char) 0x0059, (char) 0x00E0, (char) 0x0000 };
            }
            case '\u1EF6': {
                /* Latin Capital Letter Y With Hook Above = Latin Capital Letter Y + Combining Hook Above */
                return new char[] { (char) 0x0059, (char) 0x00E0, (char) 0x0000 };
            }
            case '\u1EF8': {
                /* Latin Capital Letter Y With Tilde = Latin Capital Letter Y + Combining Tilde */
                return new char[] { (char) 0x0059, (char) 0x00E4, (char) 0x0000 };
            }
            case '\u00E0': {
                /* Latin Small Letter A With Grave = Latin Small Letter A + Combining Grave Accent */
                return new char[] { (char) 0x0061, (char) 0x00E1, (char) 0x0000 };
            }
            case '\u00E1': {
                /* Latin Small Letter A With Acute = Latin Small Letter A + Combining Acute Accent */
                return new char[] { (char) 0x0061, (char) 0x00E2, (char) 0x0000 };
            }
            case '\u00E2': {
                /* Latin Small Letter A With Circumflex = Latin Small Letter A + Combining Circumflex Accent */
                return new char[] { (char) 0x0061, (char) 0x00E3, (char) 0x0000 };
            }
            case '\u00E3': {
                /* Latin Small Letter A With Tilde = Latin Small Letter A + Combining Tilde */
                return new char[] { (char) 0x0061, (char) 0x00E4, (char) 0x0000 };
            }
            case '\u00E4': {
                /* Latin Small Letter A With Diaeresis = Latin Small Letter A + Combining Diaeresis */
                return new char[] { (char) 0x0061, (char) 0x00E8, (char) 0x0000 };
            }
            case '\u00E5': {
                /* Latin Small Letter A With Ring Above = Latin Small Letter A + Combining Ring Above */
                return new char[] { (char) 0x0061, (char) 0x00EA, (char) 0x0000 };
            }
            case '\u0101': {
                /* Latin Small Letter A With Macron = Latin Small Letter A + Combining Macron */
                return new char[] { (char) 0x0061, (char) 0x00E5, (char) 0x0000 };
            }
            case '\u0103': {
                /* Latin Small Letter A With Breve = Latin Small Letter A + Combining Breve */
                return new char[] { (char) 0x0061, (char) 0x00E6, (char) 0x0000 };
            }
            case '\u0105': {
                /* Latin Small Letter A With Ogonek = Latin Small Letter A + Combining Ogonek */
                return new char[] { (char) 0x0061, (char) 0x00F1, (char) 0x0000 };
            }
            case '\u01CE': {
                /* Latin Small Letter A With Caron = Latin Small Letter A + Combining Caron */
                return new char[] { (char) 0x0061, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1EA7': {
                /*
                 * Latin Small Letter A With Circumflex And Grave = Latin Small Letter A + Combining Circumflex Accent +
                 * Combining Grave Accent
                 */
                return new char[] { (char) 0x0061, (char) 0x00E0, (char) 0x00E1 };
            }
            case '\u1EB1': {
                /*
                 * Latin Small Letter A With Breve And Grave = Latin Small Letter A + Combining Breve + Combining Grave
                 * Accent
                 */
                return new char[] { (char) 0x0061, (char) 0x00E0, (char) 0x00E1 };
            }
            case '\u0201': {
                /* Latin Small Letter A With Double Grave = Latin Small Letter A + Combining Double Grave Accent */
                return new char[] { (char) 0x0061, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0203': {
                /* Latin Small Letter A With Inverted Breve = Latin Small Letter A + Combining Inverted Breve */
                return new char[] { (char) 0x0061, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E01': {
                /* Latin Small Letter A With Ring Below = Latin Small Letter A + Combining Ring Below */
                return new char[] { (char) 0x0061, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u01FB': {
                /*
                 * Latin Small Letter A With Ring Above And Acute = Latin Small Letter A + Combining Ring Above +
                 * Combining Acute Accent
                 */
                return new char[] { (char) 0x0061, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1EA5': {
                /*
                 * Latin Small Letter A With Circumflex And Acute = Latin Small Letter A + Combining Circumflex Accent +
                 * Combining Acute Accent
                 */
                return new char[] { (char) 0x0061, (char) 0x00E0, (char) 0x00E2 };
            }
            case '\u1EAF': {
                /*
                 * Latin Small Letter A With Breve And Acute = Latin Small Letter A + Combining Breve + Combining Acute
                 * Accent
                 */
                return new char[] { (char) 0x0061, (char) 0x00E0, (char) 0x00E2 };
            }
            case '\u1EAB': {
                /*
                 * Latin Small Letter A With Circumflex And Tilde = Latin Small Letter A + Combining Circumflex Accent +
                 * Combining Tilde
                 */
                return new char[] { (char) 0x0061, (char) 0x00E0, (char) 0x00E4 };
            }
            case '\u1EA1': {
                /* Latin Small Letter A With Dot Below = Latin Small Letter A + Combining Dot Below */
                return new char[] { (char) 0x0061, (char) 0x00F2, (char) 0x00E4 };
            }
            case '\u1EA3': {
                /* Latin Small Letter A With Hook Above = Latin Small Letter A + Combining Hook Above */
                return new char[] { (char) 0x0061, (char) 0x00E0, (char) 0x00E4 };
            }
            case '\u1EB5': {
                /* Latin Small Letter A With Breve And Tilde = Latin Small Letter A + Combining Breve + Combining Tilde */
                return new char[] { (char) 0x0061, (char) 0x00E0, (char) 0x00E4 };
            }
            case '\u01DF': {
                /*
                 * Latin Small Letter A With Diaeresis And Macron = Latin Small Letter A + Combining Diaeresis +
                 * Combining Macron
                 */
                return new char[] { (char) 0x0061, (char) 0x00E7, (char) 0x00E5 };
            }
            case '\u01E1': {
                /*
                 * Latin Small Letter A With Dot Above And Macron = Latin Small Letter A + Combining Dot Above +
                 * Combining Macron
                 */
                return new char[] { (char) 0x0061, (char) 0x00E7, (char) 0x00E5 };
            }
            case '\u1EA9': {
                /*
                 * Latin Small Letter A With Circumflex And Hook Above = Latin Small Letter A + Combining Circumflex
                 * Accent + Combining Hook Above
                 */
                return new char[] { (char) 0x0061, (char) 0x00E0, (char) 0x00E0 };
            }
            case '\u1EB3': {
                /*
                 * Latin Small Letter A With Breve And Hook Above = Latin Small Letter A + Combining Breve + Combining
                 * Hook Above
                 */
                return new char[] { (char) 0x0061, (char) 0x00E0, (char) 0x00E0 };
            }
            case '\u1EAD': {
                /*
                 * Latin Small Letter A With Circumflex And Dot Below = Latin Small Letter A + Combining Circumflex
                 * Accent + Combining Dot Below
                 */
                return new char[] { (char) 0x0061, (char) 0x00E0, (char) 0x00F2 };
            }
            case '\u1EB7': {
                /*
                 * Latin Small Letter A With Breve And Dot Below = Latin Small Letter A + Combining Breve + Combining
                 * Dot Below
                 */
                return new char[] { (char) 0x0061, (char) 0x00E0, (char) 0x00F2 };
            }
            case '\u1E03': {
                /* Latin Small Letter B With Dot Above = Latin Small Letter B + Combining Dot Above */
                return new char[] { (char) 0x0062, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E05': {
                /* Latin Small Letter B With Dot Below = Latin Small Letter B + Combining Dot Below */
                return new char[] { (char) 0x0062, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E07': {
                /* Latin Small Letter B With Line Below = Latin Small Letter B + Combining Low Line */
                return new char[] { (char) 0x0062, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u00E7': {
                /* Latin Small Letter C With Cedilla = Latin Small Letter C + Combining Cedilla */
                return new char[] { (char) 0x0063, (char) 0x00F0, (char) 0x0000 };
            }
            case '\u0107': {
                /* Latin Small Letter C With Acute = Latin Small Letter C + Combining Acute Accent */
                return new char[] { (char) 0x0063, (char) 0x00E2, (char) 0x0000 };
            }
            case '\u0109': {
                /* Latin Small Letter C With Circumflex = Latin Small Letter C + Combining Circumflex Accent */
                return new char[] { (char) 0x0063, (char) 0x00E3, (char) 0x0000 };
            }
            case '\u010B': {
                /* Latin Small Letter C With Dot Above = Latin Small Letter C + Combining Dot Above */
                return new char[] { (char) 0x0063, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u010D': {
                /* Latin Small Letter C With Caron = Latin Small Letter C + Combining Caron */
                return new char[] { (char) 0x0063, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E09': {
                /*
                 * Latin Small Letter C With Cedilla And Acute = Latin Small Letter C + Combining Cedilla + Combining
                 * Acute Accent
                 */
                return new char[] { (char) 0x0063, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u010F': {
                /* Latin Small Letter D With Caron = Latin Small Letter D + Combining Caron */
                return new char[] { (char) 0x0064, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E0B': {
                /* Latin Small Letter D With Dot Above = Latin Small Letter D + Combining Dot Above */
                return new char[] { (char) 0x0064, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E0D': {
                /* Latin Small Letter D With Dot Below = Latin Small Letter D + Combining Dot Below */
                return new char[] { (char) 0x0064, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E0F': {
                /* Latin Small Letter D With Line Below = Latin Small Letter D + Combining Low Line */
                return new char[] { (char) 0x0064, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E11': {
                /* Latin Small Letter D With Cedilla = Latin Small Letter D + Combining Cedilla */
                return new char[] { (char) 0x0064, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E13': {
                /* Latin Small Letter D With Circumflex Below = Latin Small Letter D + Combining Circumflex Accent Below */
                return new char[] { (char) 0x0064, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u00E8': {
                /* Latin Small Letter E With Grave = Latin Small Letter E + Combining Grave Accent */
                return new char[] { (char) 0x0065, (char) 0x00E1, (char) 0x0000 };
            }
            case '\u00E9': {
                /* Latin Small Letter E With Acute = Latin Small Letter E + Combining Acute Accent */
                return new char[] { (char) 0x0065, (char) 0x00E2, (char) 0x0000 };
            }
            case '\u00EA': {
                /* Latin Small Letter E With Circumflex = Latin Small Letter E + Combining Circumflex Accent */
                return new char[] { (char) 0x0065, (char) 0x00E3, (char) 0x0000 };
            }
            case '\u00EB': {
                /* Latin Small Letter E With Diaeresis = Latin Small Letter E + Combining Diaeresis */
                return new char[] { (char) 0x0065, (char) 0x00E8, (char) 0x0000 };
            }
            case '\u0113': {
                /* Latin Small Letter E With Macron = Latin Small Letter E + Combining Macron */
                return new char[] { (char) 0x0065, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0115': {
                /* Latin Small Letter E With Breve = Latin Small Letter E + Combining Breve */
                return new char[] { (char) 0x0065, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0117': {
                /* Latin Small Letter E With Dot Above = Latin Small Letter E + Combining Dot Above */
                return new char[] { (char) 0x0065, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0119': {
                /* Latin Small Letter E With Ogonek = Latin Small Letter E + Combining Ogonek */
                return new char[] { (char) 0x0065, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u011B': {
                /* Latin Small Letter E With Caron = Latin Small Letter E + Combining Caron */
                return new char[] { (char) 0x0065, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E15': {
                /*
                 * Latin Small Letter E With Macron And Grave = Latin Small Letter E + Combining Macron + Combining
                 * Grave Accent
                 */
                return new char[] { (char) 0x0065, (char) 0x00E7, (char) 0x00E1 };
            }
            case '\u1EC1': {
                /*
                 * Latin Small Letter E With Circumflex And Grave = Latin Small Letter E + Combining Circumflex Accent +
                 * Combining Grave Accent
                 */
                return new char[] { (char) 0x0065, (char) 0x00E0, (char) 0x00E1 };
            }
            case '\u0205': {
                /* Latin Small Letter E With Double Grave = Latin Small Letter E + Combining Double Grave Accent */
                return new char[] { (char) 0x0065, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0207': {
                /* Latin Small Letter E With Inverted Breve = Latin Small Letter E + Combining Inverted Breve */
                return new char[] { (char) 0x0065, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E19': {
                /* Latin Small Letter E With Circumflex Below = Latin Small Letter E + Combining Circumflex Accent Below */
                return new char[] { (char) 0x0065, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E1B': {
                /* Latin Small Letter E With Tilde Below = Latin Small Letter E + Combining Tilde Below */
                return new char[] { (char) 0x0065, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E17': {
                /*
                 * Latin Small Letter E With Macron And Acute = Latin Small Letter E + Combining Macron + Combining
                 * Acute Accent
                 */
                return new char[] { (char) 0x0065, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1EBF': {
                /*
                 * Latin Small Letter E With Circumflex And Acute = Latin Small Letter E + Combining Circumflex Accent +
                 * Combining Acute Accent
                 */
                return new char[] { (char) 0x0065, (char) 0x00E0, (char) 0x00E2 };
            }
            case '\u1EC5': {
                /*
                 * Latin Small Letter E With Circumflex And Tilde = Latin Small Letter E + Combining Circumflex Accent +
                 * Combining Tilde
                 */
                return new char[] { (char) 0x0065, (char) 0x00E0, (char) 0x00E4 };
            }
            case '\u1EB9': {
                /* Latin Small Letter E With Dot Below = Latin Small Letter E + Combining Dot Below */
                return new char[] { (char) 0x0065, (char) 0x00E0, (char) 0x00E5 };
            }
            case '\u1EBB': {
                /* Latin Small Letter E With Hook Above = Latin Small Letter E + Combining Hook Above */
                return new char[] { (char) 0x0065, (char) 0x00E0, (char) 0x00E5 };
            }
            case '\u1EBD': {
                /* Latin Small Letter E With Tilde = Latin Small Letter E + Combining Tilde */
                return new char[] { (char) 0x0065, (char) 0x00E0, (char) 0x00E5 };
            }
            case '\u1E1D': {
                /*
                 * Latin Small Letter E With Cedilla And Breve = Latin Small Letter E + Combining Cedilla + Combining
                 * Breve
                 */
                return new char[] { (char) 0x0065, (char) 0x00E7, (char) 0x00E6 };
            }
            case '\u1EC3': {
                /*
                 * Latin Small Letter E With Circumflex And Hook Above = Latin Small Letter E + Combining Circumflex
                 * Accent + Combining Hook Above
                 */
                return new char[] { (char) 0x0065, (char) 0x00E0, (char) 0x00E0 };
            }
            case '\u1EC7': {
                /*
                 * Latin Small Letter E With Circumflex And Dot Below = Latin Small Letter E + Combining Circumflex
                 * Accent + Combining Dot Below
                 */
                return new char[] { (char) 0x0065, (char) 0x00E0, (char) 0x00F2 };
            }
            case '\u1E1F': {
                /* Latin Small Letter F With Dot Above = Latin Small Letter F + Combining Dot Above */
                return new char[] { (char) 0x0066, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u011D': {
                /* Latin Small Letter G With Circumflex = Latin Small Letter G + Combining Circumflex Accent */
                return new char[] { (char) 0x0067, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u011F': {
                /* Latin Small Letter G With Breve = Latin Small Letter G + Combining Breve */
                return new char[] { (char) 0x0067, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0121': {
                /* Latin Small Letter G With Dot Above = Latin Small Letter G + Combining Dot Above */
                return new char[] { (char) 0x0067, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0123': {
                /* Latin Small Letter G With Cedilla = Latin Small Letter G + Combining Cedilla */
                return new char[] { (char) 0x0067, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u01E7': {
                /* Latin Small Letter G With Caron = Latin Small Letter G + Combining Caron */
                return new char[] { (char) 0x0067, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u01F5': {
                /* Latin Small Letter G With Acute = Latin Small Letter G + Combining Acute Accent */
                return new char[] { (char) 0x0067, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E21': {
                /* Latin Small Letter G With Macron = Latin Small Letter G + Combining Macron */
                return new char[] { (char) 0x0067, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u0125': {
                /* Latin Small Letter H With Circumflex = Latin Small Letter H + Combining Circumflex Accent */
                return new char[] { (char) 0x0068, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E23': {
                /* Latin Small Letter H With Dot Above = Latin Small Letter H + Combining Dot Above */
                return new char[] { (char) 0x0068, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E25': {
                /* Latin Small Letter H With Dot Below = Latin Small Letter H + Combining Dot Below */
                return new char[] { (char) 0x0068, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E27': {
                /* Latin Small Letter H With Diaeresis = Latin Small Letter H + Combining Diaeresis */
                return new char[] { (char) 0x0068, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E29': {
                /* Latin Small Letter H With Cedilla = Latin Small Letter H + Combining Cedilla */
                return new char[] { (char) 0x0068, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E2B': {
                /* Latin Small Letter H With Breve Below = Latin Small Letter H + Combining Breve Below */
                return new char[] { (char) 0x0068, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E96': {
                /* Latin Small Letter H With Line Below = Latin Small Letter H + Combining Low Line */
                return new char[] { (char) 0x0068, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u00EC': {
                /* Latin Small Letter I With Grave = Latin Small Letter I + Combining Grave Accent */
                return new char[] { (char) 0x0069, (char) 0x00E1, (char) 0x0000 };
            }
            case '\u00ED': {
                /* Latin Small Letter I With Acute = Latin Small Letter I + Combining Acute Accent */
                return new char[] { (char) 0x0069, (char) 0x00E2, (char) 0x0000 };
            }
            case '\u00EE': {
                /* Latin Small Letter I With Circumflex = Latin Small Letter I + Combining Circumflex Accent */
                return new char[] { (char) 0x0069, (char) 0x00E3, (char) 0x0000 };
            }
            case '\u00EF': {
                /* Latin Small Letter I With Diaeresis = Latin Small Letter I + Combining Diaeresis */
                return new char[] { (char) 0x0069, (char) 0x00E8, (char) 0x0000 };
            }
            case '\u0129': {
                /* Latin Small Letter I With Tilde = Latin Small Letter I + Combining Tilde */
                return new char[] { (char) 0x0069, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u012B': {
                /* Latin Small Letter I With Macron = Latin Small Letter I + Combining Macron */
                return new char[] { (char) 0x0069, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u012D': {
                /* Latin Small Letter I With Breve = Latin Small Letter I + Combining Breve */
                return new char[] { (char) 0x0069, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u012F': {
                /* Latin Small Letter I With Ogonek = Latin Small Letter I + Combining Ogonek */
                return new char[] { (char) 0x0069, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u01D0': {
                /* Latin Small Letter I With Caron = Latin Small Letter I + Combining Caron */
                return new char[] { (char) 0x0069, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0209': {
                /* Latin Small Letter I With Double Grave = Latin Small Letter I + Combining Double Grave Accent */
                return new char[] { (char) 0x0069, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u020B': {
                /* Latin Small Letter I With Inverted Breve = Latin Small Letter I + Combining Inverted Breve */
                return new char[] { (char) 0x0069, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E2D': {
                /* Latin Small Letter I With Tilde Below = Latin Small Letter I + Combining Tilde Below */
                return new char[] { (char) 0x0069, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E2F': {
                /*
                 * Latin Small Letter I With Diaeresis And Acute = Latin Small Letter I + Combining Diaeresis +
                 * Combining Acute Accent
                 */
                return new char[] { (char) 0x0069, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1EC9': {
                /* Latin Small Letter I With Hook Above = Latin Small Letter I + Combining Hook Above */
                return new char[] { (char) 0x0069, (char) 0x00E0, (char) 0x0000 };
            }
            case '\u1ECB': {
                /* Latin Small Letter I With Dot Below = Latin Small Letter I + Combining Dot Below */
                return new char[] { (char) 0x0069, (char) 0x00E0, (char) 0x0000 };
            }
            case '\u1E55': {
                /* Latin Small Letter P With Acute = Latin Small Letter P + Combining Acute Accent */
                return new char[] { (char) 0x0070, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E57': {
                /* Latin Small Letter P With Dot Above = Latin Small Letter P + Combining Dot Above */
                return new char[] { (char) 0x0070, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u0155': {
                /* Latin Small Letter R With Acute = Latin Small Letter R + Combining Acute Accent */
                return new char[] { (char) 0x0072, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0157': {
                /* Latin Small Letter R With Cedilla = Latin Small Letter R + Combining Cedilla */
                return new char[] { (char) 0x0072, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0159': {
                /* Latin Small Letter R With Caron = Latin Small Letter R + Combining Caron */
                return new char[] { (char) 0x0072, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0211': {
                /* Latin Small Letter R With Double Grave = Latin Small Letter R + Combining Double Grave Accent */
                return new char[] { (char) 0x0072, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0213': {
                /* Latin Small Letter R With Inverted Breve = Latin Small Letter R + Combining Inverted Breve */
                return new char[] { (char) 0x0072, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E59': {
                /* Latin Small Letter R With Dot Above = Latin Small Letter R + Combining Dot Above */
                return new char[] { (char) 0x0072, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E5B': {
                /* Latin Small Letter R With Dot Below = Latin Small Letter R + Combining Dot Below */
                return new char[] { (char) 0x0072, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E5F': {
                /* Latin Small Letter R With Line Below = Latin Small Letter R + Combining Low Line */
                return new char[] { (char) 0x0072, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E5D': {
                /*
                 * Latin Small Letter R With Dot Below And Macron = Latin Small Letter R + Combining Dot Below +
                 * Combining Macron
                 */
                return new char[] { (char) 0x0072, (char) 0x00E7, (char) 0x00E5 };
            }
            case '\u015B': {
                /* Latin Small Letter S With Acute = Latin Small Letter S + Combining Acute Accent */
                return new char[] { (char) 0x0073, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u015D': {
                /* Latin Small Letter S With Circumflex = Latin Small Letter S + Combining Circumflex Accent */
                return new char[] { (char) 0x0073, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u015F': {
                /* Latin Small Letter S With Cedilla = Latin Small Letter S + Combining Cedilla */
                return new char[] { (char) 0x0073, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0161': {
                /* Latin Small Letter S With Caron = Latin Small Letter S + Combining Caron */
                return new char[] { (char) 0x0073, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E61': {
                /* Latin Small Letter S With Dot Above = Latin Small Letter S + Combining Dot Above */
                return new char[] { (char) 0x0073, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E63': {
                /* Latin Small Letter S With Dot Below = Latin Small Letter S + Combining Dot Below */
                return new char[] { (char) 0x0073, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E65': {
                /*
                 * Latin Small Letter S With Acute And Dot Above = Latin Small Letter S + Combining Acute Accent +
                 * Combining Dot Above
                 */
                return new char[] { (char) 0x0073, (char) 0x00E7, (char) 0x00E7 };
            }
            case '\u1E67': {
                /*
                 * Latin Small Letter S With Caron And Dot Above = Latin Small Letter S + Combining Caron + Combining
                 * Dot Above
                 */
                return new char[] { (char) 0x0073, (char) 0x00E7, (char) 0x00E7 };
            }
            case '\u1E69': {
                /*
                 * Latin Small Letter S With Dot Below And Dot Above = Latin Small Letter S + Combining Dot Below +
                 * Combining Dot Above
                 */
                return new char[] { (char) 0x0073, (char) 0x00E7, (char) 0x00E7 };
            }
            case '\u0163': {
                /* Latin Small Letter T With Cedilla = Latin Small Letter T + Combining Cedilla */
                return new char[] { (char) 0x0074, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0165': {
                /* Latin Small Letter T With Caron = Latin Small Letter T + Combining Caron */
                return new char[] { (char) 0x0074, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E6B': {
                /* Latin Small Letter T With Dot Above = Latin Small Letter T + Combining Dot Above */
                return new char[] { (char) 0x0074, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E6D': {
                /* Latin Small Letter T With Dot Below = Latin Small Letter T + Combining Dot Below */
                return new char[] { (char) 0x0074, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E6F': {
                /* Latin Small Letter T With Line Below = Latin Small Letter T + Combining Low Line */
                return new char[] { (char) 0x0074, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E71': {
                /* Latin Small Letter T With Circumflex Below = Latin Small Letter T + Combining Circumflex Accent Below */
                return new char[] { (char) 0x0074, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E97': {
                /* Latin Small Letter T With Diaeresis = Latin Small Letter T + Combining Diaeresis */
                return new char[] { (char) 0x0074, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u00F9': {
                /* Latin Small Letter U With Grave = Latin Small Letter U + Combining Grave Accent */
                return new char[] { (char) 0x0075, (char) 0x00E1, (char) 0x0000 };
            }
            case '\u00FA': {
                /* Latin Small Letter U With Acute = Latin Small Letter U + Combining Acute Accent */
                return new char[] { (char) 0x0075, (char) 0x00E2, (char) 0x0000 };
            }
            case '\u00FB': {
                /* Latin Small Letter U With Circumflex = Latin Small Letter U + Combining Circumflex Accent */
                return new char[] { (char) 0x0075, (char) 0x00E3, (char) 0x0000 };
            }
            case '\u00FC': {
                /* Latin Small Letter U With Diaeresis = Latin Small Letter U + Combining Diaeresis */
                return new char[] { (char) 0x0075, (char) 0x00E8, (char) 0x0000 };
            }
            case '\u0169': {
                /* Latin Small Letter U With Tilde = Latin Small Letter U + Combining Tilde */
                return new char[] { (char) 0x0075, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u016B': {
                /* Latin Small Letter U With Macron = Latin Small Letter U + Combining Macron */
                return new char[] { (char) 0x0075, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u016D': {
                /* Latin Small Letter U With Breve = Latin Small Letter U + Combining Breve */
                return new char[] { (char) 0x0075, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u016F': {
                /* Latin Small Letter U With Ring Above = Latin Small Letter U + Combining Ring Above */
                return new char[] { (char) 0x0075, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0171': {
                /* Latin Small Letter U With Double Acute = Latin Small Letter U + Combining Double Acute Accent */
                return new char[] { (char) 0x0075, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0173': {
                /* Latin Small Letter U With Ogonek = Latin Small Letter U + Combining Ogonek */
                return new char[] { (char) 0x0075, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u01B0': {
                /* Latin Small Letter U With Horn = Latin Small Letter U + Combining Horn */
                return new char[] { (char) 0x00BD, (char) 0x0000, (char) 0x0000 };
            }
            case '\u01D4': {
                /* Latin Small Letter U With Caron = Latin Small Letter U + Combining Caron */
                return new char[] { (char) 0x0075, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u01DC': {
                /*
                 * Latin Small Letter U With Diaeresis And Grave = Latin Small Letter U + Combining Diaeresis +
                 * Combining Grave Accent
                 */
                return new char[] { (char) 0x0075, (char) 0x00E7, (char) 0x00E1 };
            }
            case '\u0215': {
                /* Latin Small Letter U With Double Grave = Latin Small Letter U + Combining Double Grave Accent */
                return new char[] { (char) 0x0075, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0217': {
                /* Latin Small Letter U With Inverted Breve = Latin Small Letter U + Combining Inverted Breve */
                return new char[] { (char) 0x0075, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1EEB': {
                /*
                 * Latin Small Letter U With Horn And Grave = Latin Small Letter U + Combining Horn + Combining Grave
                 * Accent
                 */
                return new char[] { (char) 0x0075, (char) 0x00E0, (char) 0x00E1 };
            }
            case '\u01D8': {
                /*
                 * Latin Small Letter U With Diaeresis And Acute = Latin Small Letter U + Combining Diaeresis +
                 * Combining Acute Accent
                 */
                return new char[] { (char) 0x0075, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E79': {
                /*
                 * Latin Small Letter U With Tilde And Acute = Latin Small Letter U + Combining Tilde + Combining Acute
                 * Accent
                 */
                return new char[] { (char) 0x0075, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E73': {
                /* Latin Small Letter U With Diaeresis Below = Latin Small Letter U + Combining Diaeresis Below */
                return new char[] { (char) 0x0075, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E75': {
                /* Latin Small Letter U With Tilde Below = Latin Small Letter U + Combining Tilde Below */
                return new char[] { (char) 0x0075, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E77': {
                /* Latin Small Letter U With Circumflex Below = Latin Small Letter U + Combining Circumflex Accent Below */
                return new char[] { (char) 0x0075, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1EE9': {
                /*
                 * Latin Small Letter U With Horn And Acute = Latin Small Letter U + Combining Horn + Combining Acute
                 * Accent
                 */
                return new char[] { (char) 0x0075, (char) 0x00E0, (char) 0x00E2 };
            }
            case '\u1EEF': {
                /* Latin Small Letter U With Horn And Tilde = Latin Small Letter U + Combining Horn + Combining Tilde */
                return new char[] { (char) 0x0075, (char) 0x00E0, (char) 0x00E4 };
            }
            case '\u01D6': {
                /*
                 * Latin Small Letter U With Diaeresis And Macron = Latin Small Letter U + Combining Diaeresis +
                 * Combining Macron
                 */
                return new char[] { (char) 0x0075, (char) 0x00E7, (char) 0x00E5 };
            }
            case '\u1E7B': {
                /*
                 * Latin Small Letter U With Macron And Diaeresis = Latin Small Letter U + Combining Macron + Combining
                 * Diaeresis
                 */
                return new char[] { (char) 0x0075, (char) 0x00E7, (char) 0x00E8 };
            }
            case '\u1EED': {
                /*
                 * Latin Small Letter U With Horn And Hook Above = Latin Small Letter U + Combining Horn + Combining
                 * Hook Above
                 */
                return new char[] { (char) 0x0075, (char) 0x00E0, (char) 0x00E0 };
            }
            case '\u1EE5': {
                /* Latin Small Letter U With Dot Below = Latin Small Letter U + Combining Dot Below */
                return new char[] { (char) 0x0075, (char) 0x00E0, (char) 0x00F2 };
            }
            case '\u1EE7': {
                /* Latin Small Letter U With Hook Above = Latin Small Letter U + Combining Hook Above */
                return new char[] { (char) 0x0075, (char) 0x00E0, (char) 0x00F2 };
            }
            case '\u1EF1': {
                /*
                 * Latin Small Letter U With Horn And Dot Below = Latin Small Letter U + Combining Horn + Combining Dot
                 * Below
                 */
                return new char[] { (char) 0x0075, (char) 0x00E0, (char) 0x00F2 };
            }
            case '\u01DA': {
                /*
                 * Latin Small Letter U With Diaeresis And Caron = Latin Small Letter U + Combining Diaeresis +
                 * Combining Caron
                 */
                return new char[] { (char) 0x0075, (char) 0x00E7, (char) 0x00E9 };
            }
            case '\u1E7D': {
                /* Latin Small Letter V With Tilde = Latin Small Letter V + Combining Tilde */
                return new char[] { (char) 0x0076, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u1E7F': {
                /* Latin Small Letter V With Dot Below = Latin Small Letter V + Combining Dot Below */
                return new char[] { (char) 0x0076, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u0175': {
                /* Latin Small Letter W With Circumflex = Latin Small Letter W + Combining Circumflex Accent */
                return new char[] { (char) 0x0077, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E81': {
                /* Latin Small Letter W With Grave = Latin Small Letter W + Combining Grave Accent */
                return new char[] { (char) 0x0077, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u1E83': {
                /* Latin Small Letter W With Acute = Latin Small Letter W + Combining Acute Accent */
                return new char[] { (char) 0x0077, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u1E85': {
                /* Latin Small Letter W With Diaeresis = Latin Small Letter W + Combining Diaeresis */
                return new char[] { (char) 0x0077, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u1E87': {
                /* Latin Small Letter W With Dot Above = Latin Small Letter W + Combining Dot Above */
                return new char[] { (char) 0x0077, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u1E89': {
                /* Latin Small Letter W With Dot Below = Latin Small Letter W + Combining Dot Below */
                return new char[] { (char) 0x0077, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u1E98': {
                /* Latin Small Letter W With Ring Above = Latin Small Letter W + Combining Ring Above */
                return new char[] { (char) 0x0077, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u1E8B': {
                /* Latin Small Letter X With Dot Above = Latin Small Letter X + Combining Dot Above */
                return new char[] { (char) 0x0078, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u1E8D': {
                /* Latin Small Letter X With Diaeresis = Latin Small Letter X + Combining Diaeresis */
                return new char[] { (char) 0x0078, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u00FD': {
                /* Latin Small Letter Y With Acute = Latin Small Letter Y + Combining Acute Accent */
                return new char[] { (char) 0x0079, (char) 0x00E2, (char) 0x0000 };
            }
            case '\u00FF': {
                /* Latin Small Letter Y With Diaeresis = Latin Small Letter Y + Combining Diaeresis */
                return new char[] { (char) 0x0079, (char) 0x00E8, (char) 0x0000 };
            }
            case '\u0177': {
                /* Latin Small Letter Y With Circumflex = Latin Small Letter Y + Combining Circumflex Accent */
                return new char[] { (char) 0x0079, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E8F': {
                /* Latin Small Letter Y With Dot Above = Latin Small Letter Y + Combining Dot Above */
                return new char[] { (char) 0x0079, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u1E99': {
                /* Latin Small Letter Y With Ring Above = Latin Small Letter Y + Combining Ring Above */
                return new char[] { (char) 0x0079, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u1EF3': {
                /* Latin Small Letter Y With Grave = Latin Small Letter Y + Combining Grave Accent */
                return new char[] { (char) 0x0079, (char) 0x00E0, (char) 0x0000 };
            }
            case '\u1EF5': {
                /* Latin Small Letter Y With Dot Below = Latin Small Letter Y + Combining Dot Below */
                return new char[] { (char) 0x0079, (char) 0x00E0, (char) 0x0000 };
            }
            case '\u1EF7': {
                /* Latin Small Letter Y With Hook Above = Latin Small Letter Y + Combining Hook Above */
                return new char[] { (char) 0x0079, (char) 0x00E0, (char) 0x0000 };
            }
            case '\u1EF9': {
                /* Latin Small Letter Y With Tilde = Latin Small Letter Y + Combining Tilde */
                return new char[] { (char) 0x0079, (char) 0x00E4, (char) 0x0000 };
            }
            case '\u01EF': {
                /* Latin Small Letter Ezh With Caron = Latin Small Letter Ezh + Combining Caron */
                return new char[] { (char) 0x0292, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0134': {
                /* Latin Capital Letter J With Circumflex = Latin Capital Letter J + Combining Circumflex Accent */
                return new char[] { (char) 0x004A, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0136': {
                /* Latin Capital Letter K With Cedilla = Latin Capital Letter K + Combining Cedilla */
                return new char[] { (char) 0x004B, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u01E8': {
                /* Latin Capital Letter K With Caron = Latin Capital Letter K + Combining Caron */
                return new char[] { (char) 0x004B, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E30': {
                /* Latin Capital Letter K With Acute = Latin Capital Letter K + Combining Acute Accent */
                return new char[] { (char) 0x004B, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E32': {
                /* Latin Capital Letter K With Dot Below = Latin Capital Letter K + Combining Dot Below */
                return new char[] { (char) 0x004B, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E34': {
                /* Latin Capital Letter K With Line Below = Latin Capital Letter K + Combining Low Line */
                return new char[] { (char) 0x004B, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u0139': {
                /* Latin Capital Letter L With Acute = Latin Capital Letter L + Combining Acute Accent */
                return new char[] { (char) 0x004C, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u013B': {
                /* Latin Capital Letter L With Cedilla = Latin Capital Letter L + Combining Cedilla */
                return new char[] { (char) 0x004C, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u013D': {
                /* Latin Capital Letter L With Caron = Latin Capital Letter L + Combining Caron */
                return new char[] { (char) 0x004C, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E36': {
                /* Latin Capital Letter L With Dot Below = Latin Capital Letter L + Combining Dot Below */
                return new char[] { (char) 0x004C, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E3A': {
                /* Latin Capital Letter L With Line Below = Latin Capital Letter L + Combining Low Line */
                return new char[] { (char) 0x004C, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E3C': {
                /*
                 * Latin Capital Letter L With Circumflex Below = Latin Capital Letter L + Combining Circumflex Accent
                 * Below
                 */
                return new char[] { (char) 0x004C, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E38': {
                /*
                 * Latin Capital Letter L With Dot Below And Macron = Latin Capital Letter L + Combining Dot Below +
                 * Combining Macron
                 */
                return new char[] { (char) 0x004C, (char) 0x00E7, (char) 0x00E5 };
            }
            case '\u1E3E': {
                /* Latin Capital Letter M With Acute = Latin Capital Letter M + Combining Acute Accent */
                return new char[] { (char) 0x004D, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E40': {
                /* Latin Capital Letter M With Dot Above = Latin Capital Letter M + Combining Dot Above */
                return new char[] { (char) 0x004D, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E42': {
                /* Latin Capital Letter M With Dot Below = Latin Capital Letter M + Combining Dot Below */
                return new char[] { (char) 0x004D, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u00D1': {
                /* Latin Capital Letter N With Tilde = Latin Capital Letter N + Combining Tilde */
                return new char[] { (char) 0x004E, (char) 0x00E4, (char) 0x0000 };
            }
            case '\u0143': {
                /* Latin Capital Letter N With Acute = Latin Capital Letter N + Combining Acute Accent */
                return new char[] { (char) 0x004E, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0145': {
                /* Latin Capital Letter N With Cedilla = Latin Capital Letter N + Combining Cedilla */
                return new char[] { (char) 0x004E, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0147': {
                /* Latin Capital Letter N With Caron = Latin Capital Letter N + Combining Caron */
                return new char[] { (char) 0x004E, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E44': {
                /* Latin Capital Letter N With Dot Above = Latin Capital Letter N + Combining Dot Above */
                return new char[] { (char) 0x004E, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E46': {
                /* Latin Capital Letter N With Dot Below = Latin Capital Letter N + Combining Dot Below */
                return new char[] { (char) 0x004E, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E48': {
                /* Latin Capital Letter N With Line Below = Latin Capital Letter N + Combining Low Line */
                return new char[] { (char) 0x004E, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E4A': {
                /*
                 * Latin Capital Letter N With Circumflex Below = Latin Capital Letter N + Combining Circumflex Accent
                 * Below
                 */
                return new char[] { (char) 0x004E, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u00D2': {
                /* Latin Capital Letter O With Grave = Latin Capital Letter O + Combining Grave Accent */
                return new char[] { (char) 0x004F, (char) 0x00E1, (char) 0x0000 };
            }
            case '\u00D3': {
                /* Latin Capital Letter O With Acute = Latin Capital Letter O + Combining Acute Accent */
                return new char[] { (char) 0x004F, (char) 0x00E2, (char) 0x0000 };
            }
            case '\u00D4': {
                /* Latin Capital Letter O With Circumflex = Latin Capital Letter O + Combining Circumflex Accent */
                return new char[] { (char) 0x004F, (char) 0x00E3, (char) 0x0000 };
            }
            case '\u00D5': {
                /* Latin Capital Letter O With Tilde = Latin Capital Letter O + Combining Tilde */
                return new char[] { (char) 0x004F, (char) 0x00E4, (char) 0x0000 };
            }
            case '\u00D6': {
                /* Latin Capital Letter O With Diaeresis = Latin Capital Letter O + Combining Diaeresis */
                return new char[] { (char) 0x004F, (char) 0x00E8, (char) 0x0000 };
            }
            case '\u014C': {
                /* Latin Capital Letter O With Macron = Latin Capital Letter O + Combining Macron */
                return new char[] { (char) 0x004F, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u014E': {
                /* Latin Capital Letter O With Breve = Latin Capital Letter O + Combining Breve */
                return new char[] { (char) 0x004F, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0150': {
                /* Latin Capital Letter O With Double Acute = Latin Capital Letter O + Combining Double Acute Accent */
                return new char[] { (char) 0x004F, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u01A0': {
                /* Latin Capital Letter O With Horn = Latin Capital Letter O + Combining Horn */
                return new char[] { (char) 0x00AC, (char) 0x0000, (char) 0x0000 };
            }
            case '\u01D1': {
                /* Latin Capital Letter O With Caron = Latin Capital Letter O + Combining Caron */
                return new char[] { (char) 0x004F, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E50': {
                /*
                 * Latin Capital Letter O With Macron And Grave = Latin Capital Letter O + Combining Macron + Combining
                 * Grave Accent
                 */
                return new char[] { (char) 0x004F, (char) 0x00E7, (char) 0x00E1 };
            }
            case '\u01EA': {
                /* Latin Capital Letter O With Ogonek = Latin Capital Letter O + Combining Ogonek */
                return new char[] { (char) 0x004F, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1ED2': {
                /*
                 * Latin Capital Letter O With Circumflex And Grave = Latin Capital Letter O + Combining Circumflex
                 * Accent + Combining Grave Accent
                 */
                return new char[] { (char) 0x004F, (char) 0x00E0, (char) 0x00E1 };
            }
            case '\u1EDC': {
                /*
                 * Latin Capital Letter O With Horn And Grave = Latin Capital Letter O + Combining Horn + Combining
                 * Grave Accent
                 */
                return new char[] { (char) 0x004F, (char) 0x00E0, (char) 0x00E1 };
            }
            case '\u020C': {
                /* Latin Capital Letter O With Double Grave = Latin Capital Letter O + Combining Double Grave Accent */
                return new char[] { (char) 0x004F, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u020E': {
                /* Latin Capital Letter O With Inverted Breve = Latin Capital Letter O + Combining Inverted Breve */
                return new char[] { (char) 0x004F, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E4C': {
                /*
                 * Latin Capital Letter O With Tilde And Acute = Latin Capital Letter O + Combining Tilde + Combining
                 * Acute Accent
                 */
                return new char[] { (char) 0x004F, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E52': {
                /*
                 * Latin Capital Letter O With Macron And Acute = Latin Capital Letter O + Combining Macron + Combining
                 * Acute Accent
                 */
                return new char[] { (char) 0x004F, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1ED0': {
                /*
                 * Latin Capital Letter O With Circumflex And Acute = Latin Capital Letter O + Combining Circumflex
                 * Accent + Combining Acute Accent
                 */
                return new char[] { (char) 0x004F, (char) 0x00E0, (char) 0x00E2 };
            }
            case '\u1EDA': {
                /*
                 * Latin Capital Letter O With Horn And Acute = Latin Capital Letter O + Combining Horn + Combining
                 * Acute Accent
                 */
                return new char[] { (char) 0x004F, (char) 0x00E0, (char) 0x00E2 };
            }
            case '\u1ED6': {
                /*
                 * Latin Capital Letter O With Circumflex And Tilde = Latin Capital Letter O + Combining Circumflex
                 * Accent + Combining Tilde
                 */
                return new char[] { (char) 0x004F, (char) 0x00E0, (char) 0x00E4 };
            }
            case '\u1EE0': {
                /*
                 * Latin Capital Letter O With Horn And Tilde = Latin Capital Letter O + Combining Horn + Combining
                 * Tilde
                 */
                return new char[] { (char) 0x004F, (char) 0x00E0, (char) 0x00E4 };
            }
            case '\u01EC': {
                /*
                 * Latin Capital Letter O With Ogonek And Macron = Latin Capital Letter O + Combining Ogonek + Combining
                 * Macron
                 */
                return new char[] { (char) 0x004F, (char) 0x00E7, (char) 0x00E5 };
            }
            case '\u1ECC': {
                /* Latin Capital Letter O With Dot Below = Latin Capital Letter O + Combining Dot Below */
                return new char[] { (char) 0x004F, (char) 0x00E0, (char) 0x0000 };
            }
            case '\u1ECE': {
                /* Latin Capital Letter O With Hook Above = Latin Capital Letter O + Combining Hook Above */
                return new char[] { (char) 0x004F, (char) 0x00E0, (char) 0x0000 };
            }
            case '\u1E4E': {
                /*
                 * Latin Capital Letter O With Tilde And Diaeresis = Latin Capital Letter O + Combining Tilde +
                 * Combining Diaeresis
                 */
                return new char[] { (char) 0x004F, (char) 0x00E7, (char) 0x00E8 };
            }
            case '\u1ED4': {
                /*
                 * Latin Capital Letter O With Circumflex And Hook Above = Latin Capital Letter O + Combining Circumflex
                 * Accent + Combining Hook Above
                 */
                return new char[] { (char) 0x004F, (char) 0x00E0, (char) 0x00E0 };
            }
            case '\u1EDE': {
                /*
                 * Latin Capital Letter O With Horn And Hook Above = Latin Capital Letter O + Combining Horn + Combining
                 * Hook Above
                 */
                return new char[] { (char) 0x004F, (char) 0x00E0, (char) 0x00E0 };
            }
            case '\u1ED8': {
                /*
                 * Latin Capital Letter O With Circumflex And Dot Below = Latin Capital Letter O + Combining Circumflex
                 * Accent + Combining Dot Below
                 */
                return new char[] { (char) 0x004F, (char) 0x00E0, (char) 0x00F2 };
            }
            case '\u1EE2': {
                /*
                 * Latin Capital Letter O With Horn And Dot Below = Latin Capital Letter O + Combining Horn + Combining
                 * Dot Below
                 */
                return new char[] { (char) 0x004F, (char) 0x00E0, (char) 0x00F2 };
            }
            case '\u0179': {
                /* Latin Capital Letter Z With Acute = Latin Capital Letter Z + Combining Acute Accent */
                return new char[] { (char) 0x005A, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u017B': {
                /* Latin Capital Letter Z With Dot Above = Latin Capital Letter Z + Combining Dot Above */
                return new char[] { (char) 0x005A, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u017D': {
                /* Latin Capital Letter Z With Caron = Latin Capital Letter Z + Combining Caron */
                return new char[] { (char) 0x005A, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E90': {
                /* Latin Capital Letter Z With Circumflex = Latin Capital Letter Z + Combining Circumflex Accent */
                return new char[] { (char) 0x005A, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u1E92': {
                /* Latin Capital Letter Z With Dot Below = Latin Capital Letter Z + Combining Dot Below */
                return new char[] { (char) 0x005A, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u1E94': {
                /* Latin Capital Letter Z With Line Below = Latin Capital Letter Z + Combining Low Line */
                return new char[] { (char) 0x005A, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u0135': {
                /* Latin Small Letter J With Circumflex = Latin Small Letter J + Combining Circumflex Accent */
                return new char[] { (char) 0x006A, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u01F0': {
                /* Latin Small Letter J With Caron = Latin Small Letter J + Combining Caron */
                return new char[] { (char) 0x006A, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0137': {
                /* Latin Small Letter K With Cedilla = Latin Small Letter K + Combining Cedilla */
                return new char[] { (char) 0x006B, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u01E9': {
                /* Latin Small Letter K With Caron = Latin Small Letter K + Combining Caron */
                return new char[] { (char) 0x006B, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E31': {
                /* Latin Small Letter K With Acute = Latin Small Letter K + Combining Acute Accent */
                return new char[] { (char) 0x006B, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E33': {
                /* Latin Small Letter K With Dot Below = Latin Small Letter K + Combining Dot Below */
                return new char[] { (char) 0x006B, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E35': {
                /* Latin Small Letter K With Line Below = Latin Small Letter K + Combining Low Line */
                return new char[] { (char) 0x006B, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u013A': {
                /* Latin Small Letter L With Acute = Latin Small Letter L + Combining Acute Accent */
                return new char[] { (char) 0x006C, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u013C': {
                /* Latin Small Letter L With Cedilla = Latin Small Letter L + Combining Cedilla */
                return new char[] { (char) 0x006C, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u013E': {
                /* Latin Small Letter L With Caron = Latin Small Letter L + Combining Caron */
                return new char[] { (char) 0x006C, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E37': {
                /* Latin Small Letter L With Dot Below = Latin Small Letter L + Combining Dot Below */
                return new char[] { (char) 0x006C, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E3B': {
                /* Latin Small Letter L With Line Below = Latin Small Letter L + Combining Low Line */
                return new char[] { (char) 0x006C, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E3D': {
                /* Latin Small Letter L With Circumflex Below = Latin Small Letter L + Combining Circumflex Accent Below */
                return new char[] { (char) 0x006C, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E39': {
                /*
                 * Latin Small Letter L With Dot Below And Macron = Latin Small Letter L + Combining Dot Below +
                 * Combining Macron
                 */
                return new char[] { (char) 0x006C, (char) 0x00E7, (char) 0x00E5 };
            }
            case '\u1E3F': {
                /* Latin Small Letter M With Acute = Latin Small Letter M + Combining Acute Accent */
                return new char[] { (char) 0x006D, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E41': {
                /* Latin Small Letter M With Dot Above = Latin Small Letter M + Combining Dot Above */
                return new char[] { (char) 0x006D, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E43': {
                /* Latin Small Letter M With Dot Below = Latin Small Letter M + Combining Dot Below */
                return new char[] { (char) 0x006D, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u00F1': {
                /* Latin Small Letter N With Tilde = Latin Small Letter N + Combining Tilde */
                return new char[] { (char) 0x006E, (char) 0x00E4, (char) 0x0000 };
            }
            case '\u0144': {
                /* Latin Small Letter N With Acute = Latin Small Letter N + Combining Acute Accent */
                return new char[] { (char) 0x006E, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0146': {
                /* Latin Small Letter N With Cedilla = Latin Small Letter N + Combining Cedilla */
                return new char[] { (char) 0x006E, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0148': {
                /* Latin Small Letter N With Caron = Latin Small Letter N + Combining Caron */
                return new char[] { (char) 0x006E, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E45': {
                /* Latin Small Letter N With Dot Above = Latin Small Letter N + Combining Dot Above */
                return new char[] { (char) 0x006E, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E47': {
                /* Latin Small Letter N With Dot Below = Latin Small Letter N + Combining Dot Below */
                return new char[] { (char) 0x006E, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E49': {
                /* Latin Small Letter N With Line Below = Latin Small Letter N + Combining Low Line */
                return new char[] { (char) 0x006E, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E4B': {
                /* Latin Small Letter N With Circumflex Below = Latin Small Letter N + Combining Circumflex Accent Below */
                return new char[] { (char) 0x006E, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u00F2': {
                /* Latin Small Letter O With Grave = Latin Small Letter O + Combining Grave Accent */
                return new char[] { (char) 0x006F, (char) 0x00E1, (char) 0x0000 };
            }
            case '\u00F3': {
                /* Latin Small Letter O With Acute = Latin Small Letter O + Combining Acute Accent */
                return new char[] { (char) 0x006F, (char) 0x00E2, (char) 0x0000 };
            }
            case '\u00F4': {
                /* Latin Small Letter O With Circumflex = Latin Small Letter O + Combining Circumflex Accent */
                return new char[] { (char) 0x006F, (char) 0x00E3, (char) 0x0000 };
            }
            case '\u00F5': {
                /* Latin Small Letter O With Tilde = Latin Small Letter O + Combining Tilde */
                return new char[] { (char) 0x006F, (char) 0x00E4, (char) 0x0000 };
            }
            case '\u00F6': {
                /* Latin Small Letter O With Diaeresis = Latin Small Letter O + Combining Diaeresis */
                return new char[] { (char) 0x006F, (char) 0x00E8, (char) 0x0000 };
            }
            case '\u014D': {
                /* Latin Small Letter O With Macron = Latin Small Letter O + Combining Macron */
                return new char[] { (char) 0x006F, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u014F': {
                /* Latin Small Letter O With Breve = Latin Small Letter O + Combining Breve */
                return new char[] { (char) 0x006F, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u0151': {
                /* Latin Small Letter O With Double Acute = Latin Small Letter O + Combining Double Acute Accent */
                return new char[] { (char) 0x006F, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u01A1': {
                /* Latin Small Letter O With Horn = Latin Small Letter O + Combining Horn */
                return new char[] { (char) 0x00BC, (char) 0x0000, (char) 0x0000 };
            }
            case '\u01D2': {
                /* Latin Small Letter O With Caron = Latin Small Letter O + Combining Caron */
                return new char[] { (char) 0x006F, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E51': {
                /*
                 * Latin Small Letter O With Macron And Grave = Latin Small Letter O + Combining Macron + Combining
                 * Grave Accent
                 */
                return new char[] { (char) 0x006F, (char) 0x00E7, (char) 0x00E1 };
            }
            case '\u01EB': {
                /* Latin Small Letter O With Ogonek = Latin Small Letter O + Combining Ogonek */
                return new char[] { (char) 0x006F, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1ED3': {
                /*
                 * Latin Small Letter O With Circumflex And Grave = Latin Small Letter O + Combining Circumflex Accent +
                 * Combining Grave Accent
                 */
                return new char[] { (char) 0x006F, (char) 0x00E0, (char) 0x00E1 };
            }
            case '\u1EDD': {
                /*
                 * Latin Small Letter O With Horn And Grave = Latin Small Letter O + Combining Horn + Combining Grave
                 * Accent
                 */
                return new char[] { (char) 0x006F, (char) 0x00E0, (char) 0x00E1 };
            }
            case '\u020D': {
                /* Latin Small Letter O With Double Grave = Latin Small Letter O + Combining Double Grave Accent */
                return new char[] { (char) 0x006F, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u020F': {
                /* Latin Small Letter O With Inverted Breve = Latin Small Letter O + Combining Inverted Breve */
                return new char[] { (char) 0x006F, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E4D': {
                /*
                 * Latin Small Letter O With Tilde And Acute = Latin Small Letter O + Combining Tilde + Combining Acute
                 * Accent
                 */
                return new char[] { (char) 0x006F, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1E53': {
                /*
                 * Latin Small Letter O With Macron And Acute = Latin Small Letter O + Combining Macron + Combining
                 * Acute Accent
                 */
                return new char[] { (char) 0x006F, (char) 0x00E7, (char) 0x00E2 };
            }
            case '\u1ED1': {
                /*
                 * Latin Small Letter O With Circumflex And Acute = Latin Small Letter O + Combining Circumflex Accent +
                 * Combining Acute Accent
                 */
                return new char[] { (char) 0x006F, (char) 0x00E0, (char) 0x00E2 };
            }
            case '\u1EDB': {
                /*
                 * Latin Small Letter O With Horn And Acute = Latin Small Letter O + Combining Horn + Combining Acute
                 * Accent
                 */
                return new char[] { (char) 0x006F, (char) 0x00E0, (char) 0x00E2 };
            }
            case '\u1ED7': {
                /*
                 * Latin Small Letter O With Circumflex And Tilde = Latin Small Letter O + Combining Circumflex Accent +
                 * Combining Tilde
                 */
                return new char[] { (char) 0x006F, (char) 0x00E0, (char) 0x00E4 };
            }
            case '\u1EE1': {
                /* Latin Small Letter O With Horn And Tilde = Latin Small Letter O + Combining Horn + Combining Tilde */
                return new char[] { (char) 0x006F, (char) 0x00E0, (char) 0x00E4 };
            }
            case '\u01ED': {
                /*
                 * Latin Small Letter O With Ogonek And Macron = Latin Small Letter O + Combining Ogonek + Combining
                 * Macron
                 */
                return new char[] { (char) 0x006F, (char) 0x00E7, (char) 0x00E5 };
            }
            case '\u1ECD': {
                /* Latin Small Letter O With Dot Below = Latin Small Letter O + Combining Dot Below */
                return new char[] { (char) 0x006F, (char) 0x00E0, (char) 0x0000 };
            }
            case '\u1ECF': {
                /* Latin Small Letter O With Hook Above = Latin Small Letter O + Combining Hook Above */
                return new char[] { (char) 0x006F, (char) 0x00E0, (char) 0x0000 };
            }
            case '\u1E4F': {
                /*
                 * Latin Small Letter O With Tilde And Diaeresis = Latin Small Letter O + Combining Tilde + Combining
                 * Diaeresis
                 */
                return new char[] { (char) 0x006F, (char) 0x00E7, (char) 0x00E8 };
            }
            case '\u1ED5': {
                /*
                 * Latin Small Letter O With Circumflex And Hook Above = Latin Small Letter O + Combining Circumflex
                 * Accent + Combining Hook Above
                 */
                return new char[] { (char) 0x006F, (char) 0x00E0, (char) 0x00E0 };
            }
            case '\u1EDF': {
                /*
                 * Latin Small Letter O With Horn And Hook Above = Latin Small Letter O + Combining Horn + Combining
                 * Hook Above
                 */
                return new char[] { (char) 0x006F, (char) 0x00E0, (char) 0x00E0 };
            }
            case '\u1ED9': {
                /*
                 * Latin Small Letter O With Circumflex And Dot Below = Latin Small Letter O + Combining Circumflex
                 * Accent + Combining Dot Below
                 */
                return new char[] { (char) 0x006F, (char) 0x00E0, (char) 0x00F2 };
            }
            case '\u1EE3': {
                /*
                 * Latin Small Letter O With Horn And Dot Below = Latin Small Letter O + Combining Horn + Combining Dot
                 * Below
                 */
                return new char[] { (char) 0x006F, (char) 0x00E0, (char) 0x00F2 };
            }
            case '\u017A': {
                /* Latin Small Letter Z With Acute = Latin Small Letter Z + Combining Acute Accent */
                return new char[] { (char) 0x007A, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u017C': {
                /* Latin Small Letter Z With Dot Above = Latin Small Letter Z + Combining Dot Above */
                return new char[] { (char) 0x007A, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u017E': {
                /* Latin Small Letter Z With Caron = Latin Small Letter Z + Combining Caron */
                return new char[] { (char) 0x007A, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E91': {
                /* Latin Small Letter Z With Circumflex = Latin Small Letter Z + Combining Circumflex Accent */
                return new char[] { (char) 0x007A, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u1E93': {
                /* Latin Small Letter Z With Dot Below = Latin Small Letter Z + Combining Dot Below */
                return new char[] { (char) 0x007A, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u1E95': {
                /* Latin Small Letter Z With Line Below = Latin Small Letter Z + Combining Low Line */
                return new char[] { (char) 0x007A, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u01E2': {
                /* Latin Capital Letter Ae With Macron = Latin Capital Letter Ae + Combining Macron */
                return new char[] { (char) 0x00C6, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u01FC': {
                /* Latin Capital Letter Ae With Acute = Latin Capital Letter Ae + Combining Acute Accent */
                return new char[] { (char) 0x00C6, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u1E9B': {
                /* Latin Small Letter Long S With Dot Above = Latin Small Letter Long S + Combining Dot Above */
                return new char[] { (char) 0x017F, (char) 0x00E7, (char) 0x00E4 };
            }
            case '\u01EE': {
                /* Latin Capital Letter Ezh With Caron = Latin Capital Letter Ezh + Combining Caron */
                return new char[] { (char) 0x01B7, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u01C4': {
                /* Latin Capital Letter Dz With Caron = Latin Capital Letter Dz + Combining Caron */
                return new char[] { (char) 0x01F1, (char) 0x00E7, (char) 0x0000 };
            }
            case '\u01C6': {
                /* Latin Small Letter Dz With Caron = Latin Small Letter Dz + Combining Caron */
                return new char[] { (char) 0x01F3, (char) 0x00E7, (char) 0x0000 };
            }

            default:
                return null;
        }
    }

    /**
     * Get a character that represents the precombined base character plus up to two diacritics. Results are already
     * decoded from ANSEL and should not be decoded again.
     * 
     * @param base
     *            the base character
     * @param diacritic1
     *            diacritic 1
     * @param diacritic2
     *            diacritic 2 - pass zero if there is no second diacritic
     * @return a single character that combines the base and the diacritic(s), or a zero if no such character exists
     */
    private char getCombinedGlyph(char base, char diacritic1, char diacritic2) {
        if (base == 0x00E6 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01E3'; // Latin Small Letter Ae With Macron = Latin Small Letter Ae + Combining Macron
        }
        if (base == 0x00E6 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01FD'; // Latin Small Letter Ae With Acute = Latin Small Letter Ae + Combining Acute Accent
        }
        if (base == 0x0041 && (diacritic1 == 0x00E1 && diacritic2 == 0x0000)) {
            return '\u00C0'; // Latin Capital Letter A With Grave = Latin Capital Letter A + Combining Grave Accent
        }
        if (base == 0x0041 && (diacritic1 == 0x00E2 && diacritic2 == 0x0000)) {
            return '\u00C1'; // Latin Capital Letter A With Acute = Latin Capital Letter A + Combining Acute Accent
        }
        if (base == 0x0041 && (diacritic1 == 0x00E3 && diacritic2 == 0x0000)) {
            return '\u00C2'; // Latin Capital Letter A With Circumflex = Latin Capital Letter A + Combining Circumflex
            // Accent
        }
        if (base == 0x0041 && (diacritic1 == 0x00E4 && diacritic2 == 0x0000)) {
            return '\u00C3'; // Latin Capital Letter A With Tilde = Latin Capital Letter A + Combining Tilde
        }
        if (base == 0x0041 && (diacritic1 == 0x00E8 && diacritic2 == 0x0000)) {
            return '\u00C4'; // Latin Capital Letter A With Diaeresis = Latin Capital Letter A + Combining Diaeresis
        }
        if (base == 0x0041 && (diacritic1 == 0x00EA && diacritic2 == 0x0000)) {
            return '\u00C5'; // Latin Capital Letter A With Ring Above = Latin Capital Letter A + Combining Ring Above
        }
        if (base == 0x0041 && (diacritic1 == 0x00E5 && diacritic2 == 0x0000)) {
            return '\u0100'; // Latin Capital Letter A With Macron = Latin Capital Letter A + Combining Macron
        }
        if (base == 0x0041 && (diacritic1 == 0x00E6 && diacritic2 == 0x0000)) {
            return '\u0102'; // Latin Capital Letter A With Breve = Latin Capital Letter A + Combining Breve
        }
        if (base == 0x0041 && (diacritic1 == 0x00F1 && diacritic2 == 0x0000)) {
            return '\u0104'; // Latin Capital Letter A With Ogonek = Latin Capital Letter A + Combining Ogonek
        }
        if (base == 0x0041 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01CD'; // Latin Capital Letter A With Caron = Latin Capital Letter A + Combining Caron
        }
        if (base == 0x0041 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E1)) {
            return '\u1EA6'; // Latin Capital Letter A With Circumflex And Grave = Latin Capital Letter A + Combining
            // Circumflex Accent + Combining Grave Accent
        }
        if (base == 0x0041 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E1)) {
            return '\u1EB0'; // Latin Capital Letter A With Breve And Grave = Latin Capital Letter A + Combining Breve +
            // Combining Grave Accent
        }
        if (base == 0x0041 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0200'; // Latin Capital Letter A With Double Grave = Latin Capital Letter A + Combining Double
            // Grave Accent
        }
        if (base == 0x0041 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0202'; // Latin Capital Letter A With Inverted Breve = Latin Capital Letter A + Combining Inverted
            // Breve
        }
        if (base == 0x0041 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u1E00'; // Latin Capital Letter A With Ring Below = Latin Capital Letter A + Combining Ring Below
        }
        if (base == 0x0041 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u01FA'; // Latin Capital Letter A With Ring Above And Acute = Latin Capital Letter A + Combining
            // Ring Above + Combining Acute Accent
        }
        if (base == 0x0041 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E2)) {
            return '\u1EA4'; // Latin Capital Letter A With Circumflex And Acute = Latin Capital Letter A + Combining
            // Circumflex Accent + Combining Acute Accent
        }
        if (base == 0x0041 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E2)) {
            return '\u1EAE'; // Latin Capital Letter A With Breve And Acute = Latin Capital Letter A + Combining Breve +
            // Combining Acute Accent
        }
        if (base == 0x0041 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E4)) {
            return '\u1EAA'; // Latin Capital Letter A With Circumflex And Tilde = Latin Capital Letter A + Combining
            // Circumflex Accent + Combining Tilde
        }
        if (base == 0x0041 && (diacritic1 == 0x00F2 && diacritic2 == 0x00E4)) {
            return '\u1EA0'; // Latin Capital Letter A With Dot Below = Latin Capital Letter A + Combining Dot Below
        }
        if (base == 0x0041 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E4)) {
            return '\u1EA2'; // Latin Capital Letter A With Hook Above = Latin Capital Letter A + Combining Hook Above
        }
        if (base == 0x0041 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E4)) {
            return '\u1EB4'; // Latin Capital Letter A With Breve And Tilde = Latin Capital Letter A + Combining Breve +
            // Combining Tilde
        }
        if (base == 0x0041 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E5)) {
            return '\u01DE'; // Latin Capital Letter A With Diaeresis And Macron = Latin Capital Letter A + Combining
            // Diaeresis + Combining Macron
        }
        if (base == 0x0041 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E5)) {
            return '\u01E0'; // Latin Capital Letter A With Dot Above And Macron = Latin Capital Letter A + Combining
            // Dot Above + Combining Macron
        }
        if (base == 0x0041 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E0)) {
            return '\u1EA8'; // Latin Capital Letter A With Circumflex And Hook Above = Latin Capital Letter A +
            // Combining Circumflex Accent + Combining Hook Above
        }
        if (base == 0x0041 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E0)) {
            return '\u1EB2'; // Latin Capital Letter A With Breve And Hook Above = Latin Capital Letter A + Combining
            // Breve + Combining Hook Above
        }
        if (base == 0x0041 && (diacritic1 == 0x00E0 && diacritic2 == 0x00F2)) {
            return '\u1EAC'; // Latin Capital Letter A With Circumflex And Dot Below = Latin Capital Letter A +
            // Combining Circumflex Accent + Combining Dot Below
        }
        if (base == 0x0041 && (diacritic1 == 0x00E0 && diacritic2 == 0x00F2)) {
            return '\u1EB6'; // Latin Capital Letter A With Breve And Dot Below = Latin Capital Letter A + Combining
            // Breve + Combining Dot Below
        }
        if (base == 0x0042 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u1E02'; // Latin Capital Letter B With Dot Above = Latin Capital Letter B + Combining Dot Above
        }
        if (base == 0x0042 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u1E04'; // Latin Capital Letter B With Dot Below = Latin Capital Letter B + Combining Dot Below
        }
        if (base == 0x0042 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u1E06'; // Latin Capital Letter B With Line Below = Latin Capital Letter B + Combining Low Line
        }
        if (base == 0x0043 && (diacritic1 == 0x00F0 && diacritic2 == 0x0000)) {
            return '\u00C7'; // Latin Capital Letter C With Cedilla = Latin Capital Letter C + Combining Cedilla
        }
        if (base == 0x0043 && (diacritic1 == 0x00E2 && diacritic2 == 0x0000)) {
            return '\u0106'; // Latin Capital Letter C With Acute = Latin Capital Letter C + Combining Acute Accent
        }
        if (base == 0x0043 && (diacritic1 == 0x00E3 && diacritic2 == 0x0000)) {
            return '\u0108'; // Latin Capital Letter C With Circumflex = Latin Capital Letter C + Combining Circumflex
            // Accent
        }
        if (base == 0x0043 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u010A'; // Latin Capital Letter C With Dot Above = Latin Capital Letter C + Combining Dot Above
        }
        if (base == 0x0043 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u010C'; // Latin Capital Letter C With Caron = Latin Capital Letter C + Combining Caron
        }
        if (base == 0x0043 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E08'; // Latin Capital Letter C With Cedilla And Acute = Latin Capital Letter C + Combining
            // Cedilla + Combining Acute Accent
        }
        if (base == 0x0044 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u010E'; // Latin Capital Letter D With Caron = Latin Capital Letter D + Combining Caron
        }
        if (base == 0x0044 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u1E0A'; // Latin Capital Letter D With Dot Above = Latin Capital Letter D + Combining Dot Above
        }
        if (base == 0x0044 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u1E0C'; // Latin Capital Letter D With Dot Below = Latin Capital Letter D + Combining Dot Below
        }
        if (base == 0x0044 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u1E0E'; // Latin Capital Letter D With Line Below = Latin Capital Letter D + Combining Low Line
        }
        if (base == 0x0044 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u1E10'; // Latin Capital Letter D With Cedilla = Latin Capital Letter D + Combining Cedilla
        }
        if (base == 0x0044 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u1E12'; // Latin Capital Letter D With Circumflex Below = Latin Capital Letter D + Combining
            // Circumflex Accent Below
        }
        if (base == 0x0045 && (diacritic1 == 0x00E1 && diacritic2 == 0x0000)) {
            return '\u00C8'; // Latin Capital Letter E With Grave = Latin Capital Letter E + Combining Grave Accent
        }
        if (base == 0x0045 && (diacritic1 == 0x00E2 && diacritic2 == 0x0000)) {
            return '\u00C9'; // Latin Capital Letter E With Acute = Latin Capital Letter E + Combining Acute Accent
        }
        if (base == 0x0045 && (diacritic1 == 0x00E3 && diacritic2 == 0x0000)) {
            return '\u00CA'; // Latin Capital Letter E With Circumflex = Latin Capital Letter E + Combining Circumflex
            // Accent
        }
        if (base == 0x0045 && (diacritic1 == 0x00E8 && diacritic2 == 0x0000)) {
            return '\u00CB'; // Latin Capital Letter E With Diaeresis = Latin Capital Letter E + Combining Diaeresis
        }
        if (base == 0x0045 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0112'; // Latin Capital Letter E With Macron = Latin Capital Letter E + Combining Macron
        }
        if (base == 0x0045 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0114'; // Latin Capital Letter E With Breve = Latin Capital Letter E + Combining Breve
        }
        if (base == 0x0045 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0116'; // Latin Capital Letter E With Dot Above = Latin Capital Letter E + Combining Dot Above
        }
        if (base == 0x0045 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0118'; // Latin Capital Letter E With Ogonek = Latin Capital Letter E + Combining Ogonek
        }
        if (base == 0x0045 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u011A'; // Latin Capital Letter E With Caron = Latin Capital Letter E + Combining Caron
        }
        if (base == 0x0045 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E1)) {
            return '\u1E14'; // Latin Capital Letter E With Macron And Grave = Latin Capital Letter E + Combining Macron
            // + Combining Grave Accent
        }
        if (base == 0x0045 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E1)) {
            return '\u1EC0'; // Latin Capital Letter E With Circumflex And Grave = Latin Capital Letter E + Combining
            // Circumflex Accent + Combining Grave Accent
        }
        if (base == 0x0045 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0204'; // Latin Capital Letter E With Double Grave = Latin Capital Letter E + Combining Double
            // Grave Accent
        }
        if (base == 0x0045 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0206'; // Latin Capital Letter E With Inverted Breve = Latin Capital Letter E + Combining Inverted
            // Breve
        }
        if (base == 0x0045 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E18'; // Latin Capital Letter E With Circumflex Below = Latin Capital Letter E + Combining
            // Circumflex Accent Below
        }
        if (base == 0x0045 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E1A'; // Latin Capital Letter E With Tilde Below = Latin Capital Letter E + Combining Tilde Below
        }
        if (base == 0x0045 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E16'; // Latin Capital Letter E With Macron And Acute = Latin Capital Letter E + Combining Macron
            // + Combining Acute Accent
        }
        if (base == 0x0045 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E2)) {
            return '\u1EBE'; // Latin Capital Letter E With Circumflex And Acute = Latin Capital Letter E + Combining
            // Circumflex Accent + Combining Acute Accent
        }
        if (base == 0x0045 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E4)) {
            return '\u1EC4'; // Latin Capital Letter E With Circumflex And Tilde = Latin Capital Letter E + Combining
            // Circumflex Accent + Combining Tilde
        }
        if (base == 0x0045 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E5)) {
            return '\u1EB8'; // Latin Capital Letter E With Dot Below = Latin Capital Letter E + Combining Dot Below
        }
        if (base == 0x0045 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E5)) {
            return '\u1EBA'; // Latin Capital Letter E With Hook Above = Latin Capital Letter E + Combining Hook Above
        }
        if (base == 0x0045 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E5)) {
            return '\u1EBC'; // Latin Capital Letter E With Tilde = Latin Capital Letter E + Combining Tilde
        }
        if (base == 0x0045 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E6)) {
            return '\u1E1C'; // Latin Capital Letter E With Cedilla And Breve = Latin Capital Letter E + Combining
            // Cedilla + Combining Breve
        }
        if (base == 0x0045 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E0)) {
            return '\u1EC2'; // Latin Capital Letter E With Circumflex And Hook Above = Latin Capital Letter E +
            // Combining Circumflex Accent + Combining Hook Above
        }
        if (base == 0x0045 && (diacritic1 == 0x00E0 && diacritic2 == 0x00F2)) {
            return '\u1EC6'; // Latin Capital Letter E With Circumflex And Dot Below = Latin Capital Letter E +
            // Combining Circumflex Accent + Combining Dot Below
        }
        if (base == 0x0046 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E1E'; // Latin Capital Letter F With Dot Above = Latin Capital Letter F + Combining Dot Above
        }
        if (base == 0x0047 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u011C'; // Latin Capital Letter G With Circumflex = Latin Capital Letter G + Combining Circumflex
            // Accent
        }
        if (base == 0x0047 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u011E'; // Latin Capital Letter G With Breve = Latin Capital Letter G + Combining Breve
        }
        if (base == 0x0047 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0120'; // Latin Capital Letter G With Dot Above = Latin Capital Letter G + Combining Dot Above
        }
        if (base == 0x0047 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0122'; // Latin Capital Letter G With Cedilla = Latin Capital Letter G + Combining Cedilla
        }
        if (base == 0x0047 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01E6'; // Latin Capital Letter G With Caron = Latin Capital Letter G + Combining Caron
        }
        if (base == 0x0047 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01F4'; // Latin Capital Letter G With Acute = Latin Capital Letter G + Combining Acute Accent
        }
        if (base == 0x0047 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E20'; // Latin Capital Letter G With Macron = Latin Capital Letter G + Combining Macron
        }
        if (base == 0x0048 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0124'; // Latin Capital Letter H With Circumflex = Latin Capital Letter H + Combining Circumflex
            // Accent
        }
        if (base == 0x0048 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E22'; // Latin Capital Letter H With Dot Above = Latin Capital Letter H + Combining Dot Above
        }
        if (base == 0x0048 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E24'; // Latin Capital Letter H With Dot Below = Latin Capital Letter H + Combining Dot Below
        }
        if (base == 0x0048 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E26'; // Latin Capital Letter H With Diaeresis = Latin Capital Letter H + Combining Diaeresis
        }
        if (base == 0x0048 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E28'; // Latin Capital Letter H With Cedilla = Latin Capital Letter H + Combining Cedilla
        }
        if (base == 0x0048 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E2A'; // Latin Capital Letter H With Breve Below = Latin Capital Letter H + Combining Breve Below
        }
        if (base == 0x0049 && (diacritic1 == 0x00E1 && diacritic2 == 0x0000)) {
            return '\u00CC'; // Latin Capital Letter I With Grave = Latin Capital Letter I + Combining Grave Accent
        }
        if (base == 0x0049 && (diacritic1 == 0x00E2 && diacritic2 == 0x0000)) {
            return '\u00CD'; // Latin Capital Letter I With Acute = Latin Capital Letter I + Combining Acute Accent
        }
        if (base == 0x0049 && (diacritic1 == 0x00E3 && diacritic2 == 0x0000)) {
            return '\u00CE'; // Latin Capital Letter I With Circumflex = Latin Capital Letter I + Combining Circumflex
            // Accent
        }
        if (base == 0x0049 && (diacritic1 == 0x00E8 && diacritic2 == 0x0000)) {
            return '\u00CF'; // Latin Capital Letter I With Diaeresis = Latin Capital Letter I + Combining Diaeresis
        }
        if (base == 0x0049 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0128'; // Latin Capital Letter I With Tilde = Latin Capital Letter I + Combining Tilde
        }
        if (base == 0x0049 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u012A'; // Latin Capital Letter I With Macron = Latin Capital Letter I + Combining Macron
        }
        if (base == 0x0049 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u012C'; // Latin Capital Letter I With Breve = Latin Capital Letter I + Combining Breve
        }
        if (base == 0x0049 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u012E'; // Latin Capital Letter I With Ogonek = Latin Capital Letter I + Combining Ogonek
        }
        if (base == 0x0049 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0130'; // Latin Capital Letter I With Dot Above = Latin Capital Letter I + Combining Dot Above
        }
        if (base == 0x0049 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01CF'; // Latin Capital Letter I With Caron = Latin Capital Letter I + Combining Caron
        }
        if (base == 0x0049 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0208'; // Latin Capital Letter I With Double Grave = Latin Capital Letter I + Combining Double
            // Grave Accent
        }
        if (base == 0x0049 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u020A'; // Latin Capital Letter I With Inverted Breve = Latin Capital Letter I + Combining Inverted
            // Breve
        }
        if (base == 0x0049 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E2C'; // Latin Capital Letter I With Tilde Below = Latin Capital Letter I + Combining Tilde Below
        }
        if (base == 0x0049 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E2E'; // Latin Capital Letter I With Diaeresis And Acute = Latin Capital Letter I + Combining
            // Diaeresis + Combining Acute Accent
        }
        if (base == 0x0049 && (diacritic1 == 0x00E0 && diacritic2 == 0x0000)) {
            return '\u1EC8'; // Latin Capital Letter I With Hook Above = Latin Capital Letter I + Combining Hook Above
        }
        if (base == 0x0049 && (diacritic1 == 0x00E0 && diacritic2 == 0x0000)) {
            return '\u1ECA'; // Latin Capital Letter I With Dot Below = Latin Capital Letter I + Combining Dot Below
        }
        if (base == 0x0050 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E54'; // Latin Capital Letter P With Acute = Latin Capital Letter P + Combining Acute Accent
        }
        if (base == 0x0050 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E56'; // Latin Capital Letter P With Dot Above = Latin Capital Letter P + Combining Dot Above
        }
        if (base == 0x0052 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0154'; // Latin Capital Letter R With Acute = Latin Capital Letter R + Combining Acute Accent
        }
        if (base == 0x0052 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0156'; // Latin Capital Letter R With Cedilla = Latin Capital Letter R + Combining Cedilla
        }
        if (base == 0x0052 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0158'; // Latin Capital Letter R With Caron = Latin Capital Letter R + Combining Caron
        }
        if (base == 0x0052 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0210'; // Latin Capital Letter R With Double Grave = Latin Capital Letter R + Combining Double
            // Grave Accent
        }
        if (base == 0x0052 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0212'; // Latin Capital Letter R With Inverted Breve = Latin Capital Letter R + Combining Inverted
            // Breve
        }
        if (base == 0x0052 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E58'; // Latin Capital Letter R With Dot Above = Latin Capital Letter R + Combining Dot Above
        }
        if (base == 0x0052 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E5A'; // Latin Capital Letter R With Dot Below = Latin Capital Letter R + Combining Dot Below
        }
        if (base == 0x0052 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E5E'; // Latin Capital Letter R With Line Below = Latin Capital Letter R + Combining Low Line
        }
        if (base == 0x0052 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E5)) {
            return '\u1E5C'; // Latin Capital Letter R With Dot Below And Macron = Latin Capital Letter R + Combining
            // Dot Below + Combining Macron
        }
        if (base == 0x0053 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u015A'; // Latin Capital Letter S With Acute = Latin Capital Letter S + Combining Acute Accent
        }
        if (base == 0x0053 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u015C'; // Latin Capital Letter S With Circumflex = Latin Capital Letter S + Combining Circumflex
            // Accent
        }
        if (base == 0x0053 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u015E'; // Latin Capital Letter S With Cedilla = Latin Capital Letter S + Combining Cedilla
        }
        if (base == 0x0053 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0160'; // Latin Capital Letter S With Caron = Latin Capital Letter S + Combining Caron
        }
        if (base == 0x0053 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E60'; // Latin Capital Letter S With Dot Above = Latin Capital Letter S + Combining Dot Above
        }
        if (base == 0x0053 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E62'; // Latin Capital Letter S With Dot Below = Latin Capital Letter S + Combining Dot Below
        }
        if (base == 0x0053 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E7)) {
            return '\u1E64'; // Latin Capital Letter S With Acute And Dot Above = Latin Capital Letter S + Combining
            // Acute Accent + Combining Dot Above
        }
        if (base == 0x0053 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E7)) {
            return '\u1E66'; // Latin Capital Letter S With Caron And Dot Above = Latin Capital Letter S + Combining
            // Caron + Combining Dot Above
        }
        if (base == 0x0053 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E7)) {
            return '\u1E68'; // Latin Capital Letter S With Dot Below And Dot Above = Latin Capital Letter S + Combining
            // Dot Below + Combining Dot Above
        }
        if (base == 0x0054 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0162'; // Latin Capital Letter T With Cedilla = Latin Capital Letter T + Combining Cedilla
        }
        if (base == 0x0054 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0164'; // Latin Capital Letter T With Caron = Latin Capital Letter T + Combining Caron
        }
        if (base == 0x0054 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E6A'; // Latin Capital Letter T With Dot Above = Latin Capital Letter T + Combining Dot Above
        }
        if (base == 0x0054 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E6C'; // Latin Capital Letter T With Dot Below = Latin Capital Letter T + Combining Dot Below
        }
        if (base == 0x0054 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E6E'; // Latin Capital Letter T With Line Below = Latin Capital Letter T + Combining Low Line
        }
        if (base == 0x0054 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E70'; // Latin Capital Letter T With Circumflex Below = Latin Capital Letter T + Combining
            // Circumflex Accent Below
        }
        if (base == 0x0055 && (diacritic1 == 0x00E1 && diacritic2 == 0x0000)) {
            return '\u00D9'; // Latin Capital Letter U With Grave = Latin Capital Letter U + Combining Grave Accent
        }
        if (base == 0x0055 && (diacritic1 == 0x00E2 && diacritic2 == 0x0000)) {
            return '\u00DA'; // Latin Capital Letter U With Acute = Latin Capital Letter U + Combining Acute Accent
        }
        if (base == 0x0055 && (diacritic1 == 0x00E3 && diacritic2 == 0x0000)) {
            return '\u00DB'; // Latin Capital Letter U With Circumflex = Latin Capital Letter U + Combining Circumflex
            // Accent
        }
        if (base == 0x0055 && (diacritic1 == 0x00E8 && diacritic2 == 0x0000)) {
            return '\u00DC'; // Latin Capital Letter U With Diaeresis = Latin Capital Letter U + Combining Diaeresis
        }
        if (base == 0x0055 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0168'; // Latin Capital Letter U With Tilde = Latin Capital Letter U + Combining Tilde
        }
        if (base == 0x0055 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u016A'; // Latin Capital Letter U With Macron = Latin Capital Letter U + Combining Macron
        }
        if (base == 0x0055 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u016C'; // Latin Capital Letter U With Breve = Latin Capital Letter U + Combining Breve
        }
        if (base == 0x0055 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u016E'; // Latin Capital Letter U With Ring Above = Latin Capital Letter U + Combining Ring Above
        }
        if (base == 0x0055 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0170'; // Latin Capital Letter U With Double Acute = Latin Capital Letter U + Combining Double
            // Acute Accent
        }
        if (base == 0x0055 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0172'; // Latin Capital Letter U With Ogonek = Latin Capital Letter U + Combining Ogonek
        }
        if (base == 0x0055 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01AF'; // Latin Capital Letter U With Horn = Latin Capital Letter U + Combining Horn
        }
        if (base == 0x0055 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01D3'; // Latin Capital Letter U With Caron = Latin Capital Letter U + Combining Caron
        }
        if (base == 0x0055 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E1)) {
            return '\u01DB'; // Latin Capital Letter U With Diaeresis And Grave = Latin Capital Letter U + Combining
            // Diaeresis + Combining Grave Accent
        }
        if (base == 0x0055 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0214'; // Latin Capital Letter U With Double Grave = Latin Capital Letter U + Combining Double
            // Grave Accent
        }
        if (base == 0x0055 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0216'; // Latin Capital Letter U With Inverted Breve = Latin Capital Letter U + Combining Inverted
            // Breve
        }
        if (base == 0x0055 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E1)) {
            return '\u1EEA'; // Latin Capital Letter U With Horn And Grave = Latin Capital Letter U + Combining Horn +
            // Combining Grave Accent
        }
        if (base == 0x0055 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u01D7'; // Latin Capital Letter U With Diaeresis And Acute = Latin Capital Letter U + Combining
            // Diaeresis + Combining Acute Accent
        }
        if (base == 0x0055 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E78'; // Latin Capital Letter U With Tilde And Acute = Latin Capital Letter U + Combining Tilde +
            // Combining Acute Accent
        }
        if (base == 0x0055 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E72'; // Latin Capital Letter U With Diaeresis Below = Latin Capital Letter U + Combining
            // Diaeresis Below
        }
        if (base == 0x0055 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E74'; // Latin Capital Letter U With Tilde Below = Latin Capital Letter U + Combining Tilde Below
        }
        if (base == 0x0055 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E76'; // Latin Capital Letter U With Circumflex Below = Latin Capital Letter U + Combining
            // Circumflex Accent Below
        }
        if (base == 0x0055 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E2)) {
            return '\u1EE8'; // Latin Capital Letter U With Horn And Acute = Latin Capital Letter U + Combining Horn +
            // Combining Acute Accent
        }
        if (base == 0x0055 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E4)) {
            return '\u1EEE'; // Latin Capital Letter U With Horn And Tilde = Latin Capital Letter U + Combining Horn +
            // Combining Tilde
        }
        if (base == 0x0055 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E5)) {
            return '\u01D5'; // Latin Capital Letter U With Diaeresis And Macron = Latin Capital Letter U + Combining
            // Diaeresis + Combining Macron
        }
        if (base == 0x0055 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E8)) {
            return '\u1E7A'; // Latin Capital Letter U With Macron And Diaeresis = Latin Capital Letter U + Combining
            // Macron + Combining Diaeresis
        }
        if (base == 0x0055 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E0)) {
            return '\u1EEC'; // Latin Capital Letter U With Horn And Hook Above = Latin Capital Letter U + Combining
            // Horn + Combining Hook Above
        }
        if (base == 0x0055 && (diacritic1 == 0x00E0 && diacritic2 == 0x00F2)) {
            return '\u1EE4'; // Latin Capital Letter U With Dot Below = Latin Capital Letter U + Combining Dot Below
        }
        if (base == 0x0055 && (diacritic1 == 0x00E0 && diacritic2 == 0x00F2)) {
            return '\u1EE6'; // Latin Capital Letter U With Hook Above = Latin Capital Letter U + Combining Hook Above
        }
        if (base == 0x0055 && (diacritic1 == 0x00E0 && diacritic2 == 0x00F2)) {
            return '\u1EF0'; // Latin Capital Letter U With Horn And Dot Below = Latin Capital Letter U + Combining Horn
            // + Combining Dot Below
        }
        if (base == 0x0055 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E9)) {
            return '\u01D9'; // Latin Capital Letter U With Diaeresis And Caron = Latin Capital Letter U + Combining
            // Diaeresis + Combining Caron
        }
        if (base == 0x0056 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E7C'; // Latin Capital Letter V With Tilde = Latin Capital Letter V + Combining Tilde
        }
        if (base == 0x0056 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E7E'; // Latin Capital Letter V With Dot Below = Latin Capital Letter V + Combining Dot Below
        }
        if (base == 0x0057 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0174'; // Latin Capital Letter W With Circumflex = Latin Capital Letter W + Combining Circumflex
            // Accent
        }
        if (base == 0x0057 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E80'; // Latin Capital Letter W With Grave = Latin Capital Letter W + Combining Grave Accent
        }
        if (base == 0x0057 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E82'; // Latin Capital Letter W With Acute = Latin Capital Letter W + Combining Acute Accent
        }
        if (base == 0x0057 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E84'; // Latin Capital Letter W With Diaeresis = Latin Capital Letter W + Combining Diaeresis
        }
        if (base == 0x0057 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E86'; // Latin Capital Letter W With Dot Above = Latin Capital Letter W + Combining Dot Above
        }
        if (base == 0x0057 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E88'; // Latin Capital Letter W With Dot Below = Latin Capital Letter W + Combining Dot Below
        }
        if (base == 0x0058 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E8A'; // Latin Capital Letter X With Dot Above = Latin Capital Letter X + Combining Dot Above
        }
        if (base == 0x0058 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E8C'; // Latin Capital Letter X With Diaeresis = Latin Capital Letter X + Combining Diaeresis
        }
        if (base == 0x0059 && (diacritic1 == 0x00E2 && diacritic2 == 0x0000)) {
            return '\u00DD'; // Latin Capital Letter Y With Acute = Latin Capital Letter Y + Combining Acute Accent
        }
        if (base == 0x0059 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0176'; // Latin Capital Letter Y With Circumflex = Latin Capital Letter Y + Combining Circumflex
            // Accent
        }
        if (base == 0x0059 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0178'; // Latin Capital Letter Y With Diaeresis = Latin Capital Letter Y + Combining Diaeresis
        }
        if (base == 0x0059 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E8E'; // Latin Capital Letter Y With Dot Above = Latin Capital Letter Y + Combining Dot Above
        }
        if (base == 0x0059 && (diacritic1 == 0x00E0 && diacritic2 == 0x0000)) {
            return '\u1EF2'; // Latin Capital Letter Y With Grave = Latin Capital Letter Y + Combining Grave Accent
        }
        if (base == 0x0059 && (diacritic1 == 0x00E0 && diacritic2 == 0x0000)) {
            return '\u1EF4'; // Latin Capital Letter Y With Dot Below = Latin Capital Letter Y + Combining Dot Below
        }
        if (base == 0x0059 && (diacritic1 == 0x00E0 && diacritic2 == 0x0000)) {
            return '\u1EF6'; // Latin Capital Letter Y With Hook Above = Latin Capital Letter Y + Combining Hook Above
        }
        if (base == 0x0059 && (diacritic1 == 0x00E4 && diacritic2 == 0x0000)) {
            return '\u1EF8'; // Latin Capital Letter Y With Tilde = Latin Capital Letter Y + Combining Tilde
        }
        if (base == 0x0061 && (diacritic1 == 0x00E1 && diacritic2 == 0x0000)) {
            return '\u00E0'; // Latin Small Letter A With Grave = Latin Small Letter A + Combining Grave Accent
        }
        if (base == 0x0061 && (diacritic1 == 0x00E2 && diacritic2 == 0x0000)) {
            return '\u00E1'; // Latin Small Letter A With Acute = Latin Small Letter A + Combining Acute Accent
        }
        if (base == 0x0061 && (diacritic1 == 0x00E3 && diacritic2 == 0x0000)) {
            return '\u00E2'; // Latin Small Letter A With Circumflex = Latin Small Letter A + Combining Circumflex
            // Accent
        }
        if (base == 0x0061 && (diacritic1 == 0x00E4 && diacritic2 == 0x0000)) {
            return '\u00E3'; // Latin Small Letter A With Tilde = Latin Small Letter A + Combining Tilde
        }
        if (base == 0x0061 && (diacritic1 == 0x00E8 && diacritic2 == 0x0000)) {
            return '\u00E4'; // Latin Small Letter A With Diaeresis = Latin Small Letter A + Combining Diaeresis
        }
        if (base == 0x0061 && (diacritic1 == 0x00EA && diacritic2 == 0x0000)) {
            return '\u00E5'; // Latin Small Letter A With Ring Above = Latin Small Letter A + Combining Ring Above
        }
        if (base == 0x0061 && (diacritic1 == 0x00E5 && diacritic2 == 0x0000)) {
            return '\u0101'; // Latin Small Letter A With Macron = Latin Small Letter A + Combining Macron
        }
        if (base == 0x0061 && (diacritic1 == 0x00E6 && diacritic2 == 0x0000)) {
            return '\u0103'; // Latin Small Letter A With Breve = Latin Small Letter A + Combining Breve
        }
        if (base == 0x0061 && (diacritic1 == 0x00F1 && diacritic2 == 0x0000)) {
            return '\u0105'; // Latin Small Letter A With Ogonek = Latin Small Letter A + Combining Ogonek
        }
        if (base == 0x0061 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01CE'; // Latin Small Letter A With Caron = Latin Small Letter A + Combining Caron
        }
        if (base == 0x0061 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E1)) {
            return '\u1EA7'; // Latin Small Letter A With Circumflex And Grave = Latin Small Letter A + Combining
            // Circumflex Accent + Combining Grave Accent
        }
        if (base == 0x0061 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E1)) {
            return '\u1EB1'; // Latin Small Letter A With Breve And Grave = Latin Small Letter A + Combining Breve +
            // Combining Grave Accent
        }
        if (base == 0x0061 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0201'; // Latin Small Letter A With Double Grave = Latin Small Letter A + Combining Double Grave
            // Accent
        }
        if (base == 0x0061 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0203'; // Latin Small Letter A With Inverted Breve = Latin Small Letter A + Combining Inverted
            // Breve
        }
        if (base == 0x0061 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u1E01'; // Latin Small Letter A With Ring Below = Latin Small Letter A + Combining Ring Below
        }
        if (base == 0x0061 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u01FB'; // Latin Small Letter A With Ring Above And Acute = Latin Small Letter A + Combining Ring
            // Above + Combining Acute Accent
        }
        if (base == 0x0061 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E2)) {
            return '\u1EA5'; // Latin Small Letter A With Circumflex And Acute = Latin Small Letter A + Combining
            // Circumflex Accent + Combining Acute Accent
        }
        if (base == 0x0061 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E2)) {
            return '\u1EAF'; // Latin Small Letter A With Breve And Acute = Latin Small Letter A + Combining Breve +
            // Combining Acute Accent
        }
        if (base == 0x0061 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E4)) {
            return '\u1EAB'; // Latin Small Letter A With Circumflex And Tilde = Latin Small Letter A + Combining
            // Circumflex Accent + Combining Tilde
        }
        if (base == 0x0061 && (diacritic1 == 0x00F2 && diacritic2 == 0x00E4)) {
            return '\u1EA1'; // Latin Small Letter A With Dot Below = Latin Small Letter A + Combining Dot Below
        }
        if (base == 0x0061 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E4)) {
            return '\u1EA3'; // Latin Small Letter A With Hook Above = Latin Small Letter A + Combining Hook Above
        }
        if (base == 0x0061 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E4)) {
            return '\u1EB5'; // Latin Small Letter A With Breve And Tilde = Latin Small Letter A + Combining Breve +
            // Combining Tilde
        }
        if (base == 0x0061 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E5)) {
            return '\u01DF'; // Latin Small Letter A With Diaeresis And Macron = Latin Small Letter A + Combining
            // Diaeresis + Combining Macron
        }
        if (base == 0x0061 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E5)) {
            return '\u01E1'; // Latin Small Letter A With Dot Above And Macron = Latin Small Letter A + Combining Dot
            // Above + Combining Macron
        }
        if (base == 0x0061 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E0)) {
            return '\u1EA9'; // Latin Small Letter A With Circumflex And Hook Above = Latin Small Letter A + Combining
            // Circumflex Accent + Combining Hook Above
        }
        if (base == 0x0061 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E0)) {
            return '\u1EB3'; // Latin Small Letter A With Breve And Hook Above = Latin Small Letter A + Combining Breve
            // + Combining Hook Above
        }
        if (base == 0x0061 && (diacritic1 == 0x00E0 && diacritic2 == 0x00F2)) {
            return '\u1EAD'; // Latin Small Letter A With Circumflex And Dot Below = Latin Small Letter A + Combining
            // Circumflex Accent + Combining Dot Below
        }
        if (base == 0x0061 && (diacritic1 == 0x00E0 && diacritic2 == 0x00F2)) {
            return '\u1EB7'; // Latin Small Letter A With Breve And Dot Below = Latin Small Letter A + Combining Breve +
            // Combining Dot Below
        }
        if (base == 0x0062 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u1E03'; // Latin Small Letter B With Dot Above = Latin Small Letter B + Combining Dot Above
        }
        if (base == 0x0062 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u1E05'; // Latin Small Letter B With Dot Below = Latin Small Letter B + Combining Dot Below
        }
        if (base == 0x0062 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u1E07'; // Latin Small Letter B With Line Below = Latin Small Letter B + Combining Low Line
        }
        if (base == 0x0063 && (diacritic1 == 0x00F0 && diacritic2 == 0x0000)) {
            return '\u00E7'; // Latin Small Letter C With Cedilla = Latin Small Letter C + Combining Cedilla
        }
        if (base == 0x0063 && (diacritic1 == 0x00E2 && diacritic2 == 0x0000)) {
            return '\u0107'; // Latin Small Letter C With Acute = Latin Small Letter C + Combining Acute Accent
        }
        if (base == 0x0063 && (diacritic1 == 0x00E3 && diacritic2 == 0x0000)) {
            return '\u0109'; // Latin Small Letter C With Circumflex = Latin Small Letter C + Combining Circumflex
            // Accent
        }
        if (base == 0x0063 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u010B'; // Latin Small Letter C With Dot Above = Latin Small Letter C + Combining Dot Above
        }
        if (base == 0x0063 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u010D'; // Latin Small Letter C With Caron = Latin Small Letter C + Combining Caron
        }
        if (base == 0x0063 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E09'; // Latin Small Letter C With Cedilla And Acute = Latin Small Letter C + Combining Cedilla +
            // Combining Acute Accent
        }
        if (base == 0x0064 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u010F'; // Latin Small Letter D With Caron = Latin Small Letter D + Combining Caron
        }
        if (base == 0x0064 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u1E0B'; // Latin Small Letter D With Dot Above = Latin Small Letter D + Combining Dot Above
        }
        if (base == 0x0064 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u1E0D'; // Latin Small Letter D With Dot Below = Latin Small Letter D + Combining Dot Below
        }
        if (base == 0x0064 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u1E0F'; // Latin Small Letter D With Line Below = Latin Small Letter D + Combining Low Line
        }
        if (base == 0x0064 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u1E11'; // Latin Small Letter D With Cedilla = Latin Small Letter D + Combining Cedilla
        }
        if (base == 0x0064 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u1E13'; // Latin Small Letter D With Circumflex Below = Latin Small Letter D + Combining Circumflex
            // Accent Below
        }
        if (base == 0x0065 && (diacritic1 == 0x00E1 && diacritic2 == 0x0000)) {
            return '\u00E8'; // Latin Small Letter E With Grave = Latin Small Letter E + Combining Grave Accent
        }
        if (base == 0x0065 && (diacritic1 == 0x00E2 && diacritic2 == 0x0000)) {
            return '\u00E9'; // Latin Small Letter E With Acute = Latin Small Letter E + Combining Acute Accent
        }
        if (base == 0x0065 && (diacritic1 == 0x00E3 && diacritic2 == 0x0000)) {
            return '\u00EA'; // Latin Small Letter E With Circumflex = Latin Small Letter E + Combining Circumflex
            // Accent
        }
        if (base == 0x0065 && (diacritic1 == 0x00E8 && diacritic2 == 0x0000)) {
            return '\u00EB'; // Latin Small Letter E With Diaeresis = Latin Small Letter E + Combining Diaeresis
        }
        if (base == 0x0065 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0113'; // Latin Small Letter E With Macron = Latin Small Letter E + Combining Macron
        }
        if (base == 0x0065 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0115'; // Latin Small Letter E With Breve = Latin Small Letter E + Combining Breve
        }
        if (base == 0x0065 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0117'; // Latin Small Letter E With Dot Above = Latin Small Letter E + Combining Dot Above
        }
        if (base == 0x0065 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0119'; // Latin Small Letter E With Ogonek = Latin Small Letter E + Combining Ogonek
        }
        if (base == 0x0065 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u011B'; // Latin Small Letter E With Caron = Latin Small Letter E + Combining Caron
        }
        if (base == 0x0065 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E1)) {
            return '\u1E15'; // Latin Small Letter E With Macron And Grave = Latin Small Letter E + Combining Macron +
            // Combining Grave Accent
        }
        if (base == 0x0065 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E1)) {
            return '\u1EC1'; // Latin Small Letter E With Circumflex And Grave = Latin Small Letter E + Combining
            // Circumflex Accent + Combining Grave Accent
        }
        if (base == 0x0065 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0205'; // Latin Small Letter E With Double Grave = Latin Small Letter E + Combining Double Grave
            // Accent
        }
        if (base == 0x0065 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0207'; // Latin Small Letter E With Inverted Breve = Latin Small Letter E + Combining Inverted
            // Breve
        }
        if (base == 0x0065 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E19'; // Latin Small Letter E With Circumflex Below = Latin Small Letter E + Combining Circumflex
            // Accent Below
        }
        if (base == 0x0065 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E1B'; // Latin Small Letter E With Tilde Below = Latin Small Letter E + Combining Tilde Below
        }
        if (base == 0x0065 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E17'; // Latin Small Letter E With Macron And Acute = Latin Small Letter E + Combining Macron +
            // Combining Acute Accent
        }
        if (base == 0x0065 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E2)) {
            return '\u1EBF'; // Latin Small Letter E With Circumflex And Acute = Latin Small Letter E + Combining
            // Circumflex Accent + Combining Acute Accent
        }
        if (base == 0x0065 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E4)) {
            return '\u1EC5'; // Latin Small Letter E With Circumflex And Tilde = Latin Small Letter E + Combining
            // Circumflex Accent + Combining Tilde
        }
        if (base == 0x0065 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E5)) {
            return '\u1EB9'; // Latin Small Letter E With Dot Below = Latin Small Letter E + Combining Dot Below
        }
        if (base == 0x0065 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E5)) {
            return '\u1EBB'; // Latin Small Letter E With Hook Above = Latin Small Letter E + Combining Hook Above
        }
        if (base == 0x0065 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E5)) {
            return '\u1EBD'; // Latin Small Letter E With Tilde = Latin Small Letter E + Combining Tilde
        }
        if (base == 0x0065 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E6)) {
            return '\u1E1D'; // Latin Small Letter E With Cedilla And Breve = Latin Small Letter E + Combining Cedilla +
            // Combining Breve
        }
        if (base == 0x0065 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E0)) {
            return '\u1EC3'; // Latin Small Letter E With Circumflex And Hook Above = Latin Small Letter E + Combining
            // Circumflex Accent + Combining Hook Above
        }
        if (base == 0x0065 && (diacritic1 == 0x00E0 && diacritic2 == 0x00F2)) {
            return '\u1EC7'; // Latin Small Letter E With Circumflex And Dot Below = Latin Small Letter E + Combining
            // Circumflex Accent + Combining Dot Below
        }
        if (base == 0x0066 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E1F'; // Latin Small Letter F With Dot Above = Latin Small Letter F + Combining Dot Above
        }
        if (base == 0x0067 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u011D'; // Latin Small Letter G With Circumflex = Latin Small Letter G + Combining Circumflex
            // Accent
        }
        if (base == 0x0067 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u011F'; // Latin Small Letter G With Breve = Latin Small Letter G + Combining Breve
        }
        if (base == 0x0067 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0121'; // Latin Small Letter G With Dot Above = Latin Small Letter G + Combining Dot Above
        }
        if (base == 0x0067 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0123'; // Latin Small Letter G With Cedilla = Latin Small Letter G + Combining Cedilla
        }
        if (base == 0x0067 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01E7'; // Latin Small Letter G With Caron = Latin Small Letter G + Combining Caron
        }
        if (base == 0x0067 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01F5'; // Latin Small Letter G With Acute = Latin Small Letter G + Combining Acute Accent
        }
        if (base == 0x0067 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E21'; // Latin Small Letter G With Macron = Latin Small Letter G + Combining Macron
        }
        if (base == 0x0068 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0125'; // Latin Small Letter H With Circumflex = Latin Small Letter H + Combining Circumflex
            // Accent
        }
        if (base == 0x0068 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E23'; // Latin Small Letter H With Dot Above = Latin Small Letter H + Combining Dot Above
        }
        if (base == 0x0068 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E25'; // Latin Small Letter H With Dot Below = Latin Small Letter H + Combining Dot Below
        }
        if (base == 0x0068 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E27'; // Latin Small Letter H With Diaeresis = Latin Small Letter H + Combining Diaeresis
        }
        if (base == 0x0068 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E29'; // Latin Small Letter H With Cedilla = Latin Small Letter H + Combining Cedilla
        }
        if (base == 0x0068 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E2B'; // Latin Small Letter H With Breve Below = Latin Small Letter H + Combining Breve Below
        }
        if (base == 0x0068 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E96'; // Latin Small Letter H With Line Below = Latin Small Letter H + Combining Low Line
        }
        if (base == 0x0069 && (diacritic1 == 0x00E1 && diacritic2 == 0x0000)) {
            return '\u00EC'; // Latin Small Letter I With Grave = Latin Small Letter I + Combining Grave Accent
        }
        if (base == 0x0069 && (diacritic1 == 0x00E2 && diacritic2 == 0x0000)) {
            return '\u00ED'; // Latin Small Letter I With Acute = Latin Small Letter I + Combining Acute Accent
        }
        if (base == 0x0069 && (diacritic1 == 0x00E3 && diacritic2 == 0x0000)) {
            return '\u00EE'; // Latin Small Letter I With Circumflex = Latin Small Letter I + Combining Circumflex
            // Accent
        }
        if (base == 0x0069 && (diacritic1 == 0x00E8 && diacritic2 == 0x0000)) {
            return '\u00EF'; // Latin Small Letter I With Diaeresis = Latin Small Letter I + Combining Diaeresis
        }
        if (base == 0x0069 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0129'; // Latin Small Letter I With Tilde = Latin Small Letter I + Combining Tilde
        }
        if (base == 0x0069 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u012B'; // Latin Small Letter I With Macron = Latin Small Letter I + Combining Macron
        }
        if (base == 0x0069 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u012D'; // Latin Small Letter I With Breve = Latin Small Letter I + Combining Breve
        }
        if (base == 0x0069 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u012F'; // Latin Small Letter I With Ogonek = Latin Small Letter I + Combining Ogonek
        }
        if (base == 0x0069 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01D0'; // Latin Small Letter I With Caron = Latin Small Letter I + Combining Caron
        }
        if (base == 0x0069 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0209'; // Latin Small Letter I With Double Grave = Latin Small Letter I + Combining Double Grave
            // Accent
        }
        if (base == 0x0069 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u020B'; // Latin Small Letter I With Inverted Breve = Latin Small Letter I + Combining Inverted
            // Breve
        }
        if (base == 0x0069 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E2D'; // Latin Small Letter I With Tilde Below = Latin Small Letter I + Combining Tilde Below
        }
        if (base == 0x0069 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E2F'; // Latin Small Letter I With Diaeresis And Acute = Latin Small Letter I + Combining
            // Diaeresis + Combining Acute Accent
        }
        if (base == 0x0069 && (diacritic1 == 0x00E0 && diacritic2 == 0x0000)) {
            return '\u1EC9'; // Latin Small Letter I With Hook Above = Latin Small Letter I + Combining Hook Above
        }
        if (base == 0x0069 && (diacritic1 == 0x00E0 && diacritic2 == 0x0000)) {
            return '\u1ECB'; // Latin Small Letter I With Dot Below = Latin Small Letter I + Combining Dot Below
        }
        if (base == 0x0070 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E55'; // Latin Small Letter P With Acute = Latin Small Letter P + Combining Acute Accent
        }
        if (base == 0x0070 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E57'; // Latin Small Letter P With Dot Above = Latin Small Letter P + Combining Dot Above
        }
        if (base == 0x0072 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0155'; // Latin Small Letter R With Acute = Latin Small Letter R + Combining Acute Accent
        }
        if (base == 0x0072 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0157'; // Latin Small Letter R With Cedilla = Latin Small Letter R + Combining Cedilla
        }
        if (base == 0x0072 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0159'; // Latin Small Letter R With Caron = Latin Small Letter R + Combining Caron
        }
        if (base == 0x0072 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0211'; // Latin Small Letter R With Double Grave = Latin Small Letter R + Combining Double Grave
            // Accent
        }
        if (base == 0x0072 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0213'; // Latin Small Letter R With Inverted Breve = Latin Small Letter R + Combining Inverted
            // Breve
        }
        if (base == 0x0072 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E59'; // Latin Small Letter R With Dot Above = Latin Small Letter R + Combining Dot Above
        }
        if (base == 0x0072 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E5B'; // Latin Small Letter R With Dot Below = Latin Small Letter R + Combining Dot Below
        }
        if (base == 0x0072 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E5F'; // Latin Small Letter R With Line Below = Latin Small Letter R + Combining Low Line
        }
        if (base == 0x0072 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E5)) {
            return '\u1E5D'; // Latin Small Letter R With Dot Below And Macron = Latin Small Letter R + Combining Dot
            // Below + Combining Macron
        }
        if (base == 0x0073 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u015B'; // Latin Small Letter S With Acute = Latin Small Letter S + Combining Acute Accent
        }
        if (base == 0x0073 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u015D'; // Latin Small Letter S With Circumflex = Latin Small Letter S + Combining Circumflex
            // Accent
        }
        if (base == 0x0073 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u015F'; // Latin Small Letter S With Cedilla = Latin Small Letter S + Combining Cedilla
        }
        if (base == 0x0073 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0161'; // Latin Small Letter S With Caron = Latin Small Letter S + Combining Caron
        }
        if (base == 0x0073 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E61'; // Latin Small Letter S With Dot Above = Latin Small Letter S + Combining Dot Above
        }
        if (base == 0x0073 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E63'; // Latin Small Letter S With Dot Below = Latin Small Letter S + Combining Dot Below
        }
        if (base == 0x0073 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E7)) {
            return '\u1E65'; // Latin Small Letter S With Acute And Dot Above = Latin Small Letter S + Combining Acute
            // Accent + Combining Dot Above
        }
        if (base == 0x0073 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E7)) {
            return '\u1E67'; // Latin Small Letter S With Caron And Dot Above = Latin Small Letter S + Combining Caron +
            // Combining Dot Above
        }
        if (base == 0x0073 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E7)) {
            return '\u1E69'; // Latin Small Letter S With Dot Below And Dot Above = Latin Small Letter S + Combining Dot
            // Below + Combining Dot Above
        }
        if (base == 0x0074 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0163'; // Latin Small Letter T With Cedilla = Latin Small Letter T + Combining Cedilla
        }
        if (base == 0x0074 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0165'; // Latin Small Letter T With Caron = Latin Small Letter T + Combining Caron
        }
        if (base == 0x0074 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E6B'; // Latin Small Letter T With Dot Above = Latin Small Letter T + Combining Dot Above
        }
        if (base == 0x0074 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E6D'; // Latin Small Letter T With Dot Below = Latin Small Letter T + Combining Dot Below
        }
        if (base == 0x0074 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E6F'; // Latin Small Letter T With Line Below = Latin Small Letter T + Combining Low Line
        }
        if (base == 0x0074 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E71'; // Latin Small Letter T With Circumflex Below = Latin Small Letter T + Combining Circumflex
            // Accent Below
        }
        if (base == 0x0074 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E97'; // Latin Small Letter T With Diaeresis = Latin Small Letter T + Combining Diaeresis
        }
        if (base == 0x0075 && (diacritic1 == 0x00E1 && diacritic2 == 0x0000)) {
            return '\u00F9'; // Latin Small Letter U With Grave = Latin Small Letter U + Combining Grave Accent
        }
        if (base == 0x0075 && (diacritic1 == 0x00E2 && diacritic2 == 0x0000)) {
            return '\u00FA'; // Latin Small Letter U With Acute = Latin Small Letter U + Combining Acute Accent
        }
        if (base == 0x0075 && (diacritic1 == 0x00E3 && diacritic2 == 0x0000)) {
            return '\u00FB'; // Latin Small Letter U With Circumflex = Latin Small Letter U + Combining Circumflex
            // Accent
        }
        if (base == 0x0075 && (diacritic1 == 0x00E8 && diacritic2 == 0x0000)) {
            return '\u00FC'; // Latin Small Letter U With Diaeresis = Latin Small Letter U + Combining Diaeresis
        }
        if (base == 0x0075 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0169'; // Latin Small Letter U With Tilde = Latin Small Letter U + Combining Tilde
        }
        if (base == 0x0075 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u016B'; // Latin Small Letter U With Macron = Latin Small Letter U + Combining Macron
        }
        if (base == 0x0075 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u016D'; // Latin Small Letter U With Breve = Latin Small Letter U + Combining Breve
        }
        if (base == 0x0075 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u016F'; // Latin Small Letter U With Ring Above = Latin Small Letter U + Combining Ring Above
        }
        if (base == 0x0075 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0171'; // Latin Small Letter U With Double Acute = Latin Small Letter U + Combining Double Acute
            // Accent
        }
        if (base == 0x0075 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0173'; // Latin Small Letter U With Ogonek = Latin Small Letter U + Combining Ogonek
        }
        if (base == 0x0075 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01B0'; // Latin Small Letter U With Horn = Latin Small Letter U + Combining Horn
        }
        if (base == 0x0075 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01D4'; // Latin Small Letter U With Caron = Latin Small Letter U + Combining Caron
        }
        if (base == 0x0075 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E1)) {
            return '\u01DC'; // Latin Small Letter U With Diaeresis And Grave = Latin Small Letter U + Combining
            // Diaeresis + Combining Grave Accent
        }
        if (base == 0x0075 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0215'; // Latin Small Letter U With Double Grave = Latin Small Letter U + Combining Double Grave
            // Accent
        }
        if (base == 0x0075 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0217'; // Latin Small Letter U With Inverted Breve = Latin Small Letter U + Combining Inverted
            // Breve
        }
        if (base == 0x0075 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E1)) {
            return '\u1EEB'; // Latin Small Letter U With Horn And Grave = Latin Small Letter U + Combining Horn +
            // Combining Grave Accent
        }
        if (base == 0x0075 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u01D8'; // Latin Small Letter U With Diaeresis And Acute = Latin Small Letter U + Combining
            // Diaeresis + Combining Acute Accent
        }
        if (base == 0x0075 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E79'; // Latin Small Letter U With Tilde And Acute = Latin Small Letter U + Combining Tilde +
            // Combining Acute Accent
        }
        if (base == 0x0075 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E73'; // Latin Small Letter U With Diaeresis Below = Latin Small Letter U + Combining Diaeresis
            // Below
        }
        if (base == 0x0075 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E75'; // Latin Small Letter U With Tilde Below = Latin Small Letter U + Combining Tilde Below
        }
        if (base == 0x0075 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E77'; // Latin Small Letter U With Circumflex Below = Latin Small Letter U + Combining Circumflex
            // Accent Below
        }
        if (base == 0x0075 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E2)) {
            return '\u1EE9'; // Latin Small Letter U With Horn And Acute = Latin Small Letter U + Combining Horn +
            // Combining Acute Accent
        }
        if (base == 0x0075 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E4)) {
            return '\u1EEF'; // Latin Small Letter U With Horn And Tilde = Latin Small Letter U + Combining Horn +
            // Combining Tilde
        }
        if (base == 0x0075 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E5)) {
            return '\u01D6'; // Latin Small Letter U With Diaeresis And Macron = Latin Small Letter U + Combining
            // Diaeresis + Combining Macron
        }
        if (base == 0x0075 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E8)) {
            return '\u1E7B'; // Latin Small Letter U With Macron And Diaeresis = Latin Small Letter U + Combining Macron
            // + Combining Diaeresis
        }
        if (base == 0x0075 && (diacritic1 == 0x00E0 && diacritic2 == 0x00E0)) {
            return '\u1EED'; // Latin Small Letter U With Horn And Hook Above = Latin Small Letter U + Combining Horn +
            // Combining Hook Above
        }
        if (base == 0x0075 && (diacritic1 == 0x00E0 && diacritic2 == 0x00F2)) {
            return '\u1EE5'; // Latin Small Letter U With Dot Below = Latin Small Letter U + Combining Dot Below
        }
        if (base == 0x0075 && (diacritic1 == 0x00E0 && diacritic2 == 0x00F2)) {
            return '\u1EE7'; // Latin Small Letter U With Hook Above = Latin Small Letter U + Combining Hook Above
        }
        if (base == 0x0075 && (diacritic1 == 0x00E0 && diacritic2 == 0x00F2)) {
            return '\u1EF1'; // Latin Small Letter U With Horn And Dot Below = Latin Small Letter U + Combining Horn +
            // Combining Dot Below
        }
        if (base == 0x0075 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E9)) {
            return '\u01DA'; // Latin Small Letter U With Diaeresis And Caron = Latin Small Letter U + Combining
            // Diaeresis + Combining Caron
        }
        if (base == 0x0076 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E7D'; // Latin Small Letter V With Tilde = Latin Small Letter V + Combining Tilde
        }
        if (base == 0x0076 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E7F'; // Latin Small Letter V With Dot Below = Latin Small Letter V + Combining Dot Below
        }
        if (base == 0x0077 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0175'; // Latin Small Letter W With Circumflex = Latin Small Letter W + Combining Circumflex
            // Accent
        }
        if (base == 0x0077 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E81'; // Latin Small Letter W With Grave = Latin Small Letter W + Combining Grave Accent
        }
        if (base == 0x0077 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E83'; // Latin Small Letter W With Acute = Latin Small Letter W + Combining Acute Accent
        }
        if (base == 0x0077 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E85'; // Latin Small Letter W With Diaeresis = Latin Small Letter W + Combining Diaeresis
        }
        if (base == 0x0077 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E87'; // Latin Small Letter W With Dot Above = Latin Small Letter W + Combining Dot Above
        }
        if (base == 0x0077 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E89'; // Latin Small Letter W With Dot Below = Latin Small Letter W + Combining Dot Below
        }
        if (base == 0x0077 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E98'; // Latin Small Letter W With Ring Above = Latin Small Letter W + Combining Ring Above
        }
        if (base == 0x0078 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E8B'; // Latin Small Letter X With Dot Above = Latin Small Letter X + Combining Dot Above
        }
        if (base == 0x0078 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E8D'; // Latin Small Letter X With Diaeresis = Latin Small Letter X + Combining Diaeresis
        }
        if (base == 0x0079 && (diacritic1 == 0x00E2 && diacritic2 == 0x0000)) {
            return '\u00FD'; // Latin Small Letter Y With Acute = Latin Small Letter Y + Combining Acute Accent
        }
        if (base == 0x0079 && (diacritic1 == 0x00E8 && diacritic2 == 0x0000)) {
            return '\u00FF'; // Latin Small Letter Y With Diaeresis = Latin Small Letter Y + Combining Diaeresis
        }
        if (base == 0x0079 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0177'; // Latin Small Letter Y With Circumflex = Latin Small Letter Y + Combining Circumflex
            // Accent
        }
        if (base == 0x0079 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E8F'; // Latin Small Letter Y With Dot Above = Latin Small Letter Y + Combining Dot Above
        }
        if (base == 0x0079 && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E99'; // Latin Small Letter Y With Ring Above = Latin Small Letter Y + Combining Ring Above
        }
        if (base == 0x0079 && (diacritic1 == 0x00E0 && diacritic2 == 0x0000)) {
            return '\u1EF3'; // Latin Small Letter Y With Grave = Latin Small Letter Y + Combining Grave Accent
        }
        if (base == 0x0079 && (diacritic1 == 0x00E0 && diacritic2 == 0x0000)) {
            return '\u1EF5'; // Latin Small Letter Y With Dot Below = Latin Small Letter Y + Combining Dot Below
        }
        if (base == 0x0079 && (diacritic1 == 0x00E0 && diacritic2 == 0x0000)) {
            return '\u1EF7'; // Latin Small Letter Y With Hook Above = Latin Small Letter Y + Combining Hook Above
        }
        if (base == 0x0079 && (diacritic1 == 0x00E4 && diacritic2 == 0x0000)) {
            return '\u1EF9'; // Latin Small Letter Y With Tilde = Latin Small Letter Y + Combining Tilde
        }
        if (base == 0x0292 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01EF'; // Latin Small Letter Ezh With Caron = Latin Small Letter Ezh + Combining Caron
        }
        if (base == 0x004A && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0134'; // Latin Capital Letter J With Circumflex = Latin Capital Letter J + Combining Circumflex
            // Accent
        }
        if (base == 0x004B && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0136'; // Latin Capital Letter K With Cedilla = Latin Capital Letter K + Combining Cedilla
        }
        if (base == 0x004B && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01E8'; // Latin Capital Letter K With Caron = Latin Capital Letter K + Combining Caron
        }
        if (base == 0x004B && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E30'; // Latin Capital Letter K With Acute = Latin Capital Letter K + Combining Acute Accent
        }
        if (base == 0x004B && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E32'; // Latin Capital Letter K With Dot Below = Latin Capital Letter K + Combining Dot Below
        }
        if (base == 0x004B && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E34'; // Latin Capital Letter K With Line Below = Latin Capital Letter K + Combining Low Line
        }
        if (base == 0x004C && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0139'; // Latin Capital Letter L With Acute = Latin Capital Letter L + Combining Acute Accent
        }
        if (base == 0x004C && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u013B'; // Latin Capital Letter L With Cedilla = Latin Capital Letter L + Combining Cedilla
        }
        if (base == 0x004C && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u013D'; // Latin Capital Letter L With Caron = Latin Capital Letter L + Combining Caron
        }
        if (base == 0x004C && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E36'; // Latin Capital Letter L With Dot Below = Latin Capital Letter L + Combining Dot Below
        }
        if (base == 0x004C && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E3A'; // Latin Capital Letter L With Line Below = Latin Capital Letter L + Combining Low Line
        }
        if (base == 0x004C && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E3C'; // Latin Capital Letter L With Circumflex Below = Latin Capital Letter L + Combining
            // Circumflex Accent Below
        }
        if (base == 0x004C && (diacritic1 == 0x00E7 && diacritic2 == 0x00E5)) {
            return '\u1E38'; // Latin Capital Letter L With Dot Below And Macron = Latin Capital Letter L + Combining
            // Dot Below + Combining Macron
        }
        if (base == 0x004D && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E3E'; // Latin Capital Letter M With Acute = Latin Capital Letter M + Combining Acute Accent
        }
        if (base == 0x004D && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E40'; // Latin Capital Letter M With Dot Above = Latin Capital Letter M + Combining Dot Above
        }
        if (base == 0x004D && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E42'; // Latin Capital Letter M With Dot Below = Latin Capital Letter M + Combining Dot Below
        }
        if (base == 0x004E && (diacritic1 == 0x00E4 && diacritic2 == 0x0000)) {
            return '\u00D1'; // Latin Capital Letter N With Tilde = Latin Capital Letter N + Combining Tilde
        }
        if (base == 0x004E && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0143'; // Latin Capital Letter N With Acute = Latin Capital Letter N + Combining Acute Accent
        }
        if (base == 0x004E && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0145'; // Latin Capital Letter N With Cedilla = Latin Capital Letter N + Combining Cedilla
        }
        if (base == 0x004E && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0147'; // Latin Capital Letter N With Caron = Latin Capital Letter N + Combining Caron
        }
        if (base == 0x004E && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E44'; // Latin Capital Letter N With Dot Above = Latin Capital Letter N + Combining Dot Above
        }
        if (base == 0x004E && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E46'; // Latin Capital Letter N With Dot Below = Latin Capital Letter N + Combining Dot Below
        }
        if (base == 0x004E && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E48'; // Latin Capital Letter N With Line Below = Latin Capital Letter N + Combining Low Line
        }
        if (base == 0x004E && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E4A'; // Latin Capital Letter N With Circumflex Below = Latin Capital Letter N + Combining
            // Circumflex Accent Below
        }
        if (base == 0x004F && (diacritic1 == 0x00E1 && diacritic2 == 0x0000)) {
            return '\u00D2'; // Latin Capital Letter O With Grave = Latin Capital Letter O + Combining Grave Accent
        }
        if (base == 0x004F && (diacritic1 == 0x00E2 && diacritic2 == 0x0000)) {
            return '\u00D3'; // Latin Capital Letter O With Acute = Latin Capital Letter O + Combining Acute Accent
        }
        if (base == 0x004F && (diacritic1 == 0x00E3 && diacritic2 == 0x0000)) {
            return '\u00D4'; // Latin Capital Letter O With Circumflex = Latin Capital Letter O + Combining Circumflex
            // Accent
        }
        if (base == 0x004F && (diacritic1 == 0x00E4 && diacritic2 == 0x0000)) {
            return '\u00D5'; // Latin Capital Letter O With Tilde = Latin Capital Letter O + Combining Tilde
        }
        if (base == 0x004F && (diacritic1 == 0x00E8 && diacritic2 == 0x0000)) {
            return '\u00D6'; // Latin Capital Letter O With Diaeresis = Latin Capital Letter O + Combining Diaeresis
        }
        if (base == 0x004F && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u014C'; // Latin Capital Letter O With Macron = Latin Capital Letter O + Combining Macron
        }
        if (base == 0x004F && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u014E'; // Latin Capital Letter O With Breve = Latin Capital Letter O + Combining Breve
        }
        if (base == 0x004F && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0150'; // Latin Capital Letter O With Double Acute = Latin Capital Letter O + Combining Double
            // Acute Accent
        }
        if (base == 0x004F && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01A0'; // Latin Capital Letter O With Horn = Latin Capital Letter O + Combining Horn
        }
        if (base == 0x004F && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01D1'; // Latin Capital Letter O With Caron = Latin Capital Letter O + Combining Caron
        }
        if (base == 0x004F && (diacritic1 == 0x00E7 && diacritic2 == 0x00E1)) {
            return '\u1E50'; // Latin Capital Letter O With Macron And Grave = Latin Capital Letter O + Combining Macron
            // + Combining Grave Accent
        }
        if (base == 0x004F && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01EA'; // Latin Capital Letter O With Ogonek = Latin Capital Letter O + Combining Ogonek
        }
        if (base == 0x004F && (diacritic1 == 0x00E0 && diacritic2 == 0x00E1)) {
            return '\u1ED2'; // Latin Capital Letter O With Circumflex And Grave = Latin Capital Letter O + Combining
            // Circumflex Accent + Combining Grave Accent
        }
        if (base == 0x004F && (diacritic1 == 0x00E0 && diacritic2 == 0x00E1)) {
            return '\u1EDC'; // Latin Capital Letter O With Horn And Grave = Latin Capital Letter O + Combining Horn +
            // Combining Grave Accent
        }
        if (base == 0x004F && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u020C'; // Latin Capital Letter O With Double Grave = Latin Capital Letter O + Combining Double
            // Grave Accent
        }
        if (base == 0x004F && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u020E'; // Latin Capital Letter O With Inverted Breve = Latin Capital Letter O + Combining Inverted
            // Breve
        }
        if (base == 0x004F && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E4C'; // Latin Capital Letter O With Tilde And Acute = Latin Capital Letter O + Combining Tilde +
            // Combining Acute Accent
        }
        if (base == 0x004F && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E52'; // Latin Capital Letter O With Macron And Acute = Latin Capital Letter O + Combining Macron
            // + Combining Acute Accent
        }
        if (base == 0x004F && (diacritic1 == 0x00E0 && diacritic2 == 0x00E2)) {
            return '\u1ED0'; // Latin Capital Letter O With Circumflex And Acute = Latin Capital Letter O + Combining
            // Circumflex Accent + Combining Acute Accent
        }
        if (base == 0x004F && (diacritic1 == 0x00E0 && diacritic2 == 0x00E2)) {
            return '\u1EDA'; // Latin Capital Letter O With Horn And Acute = Latin Capital Letter O + Combining Horn +
            // Combining Acute Accent
        }
        if (base == 0x004F && (diacritic1 == 0x00E0 && diacritic2 == 0x00E4)) {
            return '\u1ED6'; // Latin Capital Letter O With Circumflex And Tilde = Latin Capital Letter O + Combining
            // Circumflex Accent + Combining Tilde
        }
        if (base == 0x004F && (diacritic1 == 0x00E0 && diacritic2 == 0x00E4)) {
            return '\u1EE0'; // Latin Capital Letter O With Horn And Tilde = Latin Capital Letter O + Combining Horn +
            // Combining Tilde
        }
        if (base == 0x004F && (diacritic1 == 0x00E7 && diacritic2 == 0x00E5)) {
            return '\u01EC'; // Latin Capital Letter O With Ogonek And Macron = Latin Capital Letter O + Combining
            // Ogonek + Combining Macron
        }
        if (base == 0x004F && (diacritic1 == 0x00E0 && diacritic2 == 0x0000)) {
            return '\u1ECC'; // Latin Capital Letter O With Dot Below = Latin Capital Letter O + Combining Dot Below
        }
        if (base == 0x004F && (diacritic1 == 0x00E0 && diacritic2 == 0x0000)) {
            return '\u1ECE'; // Latin Capital Letter O With Hook Above = Latin Capital Letter O + Combining Hook Above
        }
        if (base == 0x004F && (diacritic1 == 0x00E7 && diacritic2 == 0x00E8)) {
            return '\u1E4E'; // Latin Capital Letter O With Tilde And Diaeresis = Latin Capital Letter O + Combining
            // Tilde + Combining Diaeresis
        }
        if (base == 0x004F && (diacritic1 == 0x00E0 && diacritic2 == 0x00E0)) {
            return '\u1ED4'; // Latin Capital Letter O With Circumflex And Hook Above = Latin Capital Letter O +
            // Combining Circumflex Accent + Combining Hook Above
        }
        if (base == 0x004F && (diacritic1 == 0x00E0 && diacritic2 == 0x00E0)) {
            return '\u1EDE'; // Latin Capital Letter O With Horn And Hook Above = Latin Capital Letter O + Combining
            // Horn + Combining Hook Above
        }
        if (base == 0x004F && (diacritic1 == 0x00E0 && diacritic2 == 0x00F2)) {
            return '\u1ED8'; // Latin Capital Letter O With Circumflex And Dot Below = Latin Capital Letter O +
            // Combining Circumflex Accent + Combining Dot Below
        }
        if (base == 0x004F && (diacritic1 == 0x00E0 && diacritic2 == 0x00F2)) {
            return '\u1EE2'; // Latin Capital Letter O With Horn And Dot Below = Latin Capital Letter O + Combining Horn
            // + Combining Dot Below
        }
        if (base == 0x005A && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0179'; // Latin Capital Letter Z With Acute = Latin Capital Letter Z + Combining Acute Accent
        }
        if (base == 0x005A && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u017B'; // Latin Capital Letter Z With Dot Above = Latin Capital Letter Z + Combining Dot Above
        }
        if (base == 0x005A && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u017D'; // Latin Capital Letter Z With Caron = Latin Capital Letter Z + Combining Caron
        }
        if (base == 0x005A && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E90'; // Latin Capital Letter Z With Circumflex = Latin Capital Letter Z + Combining Circumflex
            // Accent
        }
        if (base == 0x005A && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E92'; // Latin Capital Letter Z With Dot Below = Latin Capital Letter Z + Combining Dot Below
        }
        if (base == 0x005A && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E94'; // Latin Capital Letter Z With Line Below = Latin Capital Letter Z + Combining Low Line
        }
        if (base == 0x006A && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0135'; // Latin Small Letter J With Circumflex = Latin Small Letter J + Combining Circumflex
            // Accent
        }
        if (base == 0x006A && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01F0'; // Latin Small Letter J With Caron = Latin Small Letter J + Combining Caron
        }
        if (base == 0x006B && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0137'; // Latin Small Letter K With Cedilla = Latin Small Letter K + Combining Cedilla
        }
        if (base == 0x006B && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01E9'; // Latin Small Letter K With Caron = Latin Small Letter K + Combining Caron
        }
        if (base == 0x006B && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E31'; // Latin Small Letter K With Acute = Latin Small Letter K + Combining Acute Accent
        }
        if (base == 0x006B && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E33'; // Latin Small Letter K With Dot Below = Latin Small Letter K + Combining Dot Below
        }
        if (base == 0x006B && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E35'; // Latin Small Letter K With Line Below = Latin Small Letter K + Combining Low Line
        }
        if (base == 0x006C && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u013A'; // Latin Small Letter L With Acute = Latin Small Letter L + Combining Acute Accent
        }
        if (base == 0x006C && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u013C'; // Latin Small Letter L With Cedilla = Latin Small Letter L + Combining Cedilla
        }
        if (base == 0x006C && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u013E'; // Latin Small Letter L With Caron = Latin Small Letter L + Combining Caron
        }
        if (base == 0x006C && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E37'; // Latin Small Letter L With Dot Below = Latin Small Letter L + Combining Dot Below
        }
        if (base == 0x006C && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E3B'; // Latin Small Letter L With Line Below = Latin Small Letter L + Combining Low Line
        }
        if (base == 0x006C && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E3D'; // Latin Small Letter L With Circumflex Below = Latin Small Letter L + Combining Circumflex
            // Accent Below
        }
        if (base == 0x006C && (diacritic1 == 0x00E7 && diacritic2 == 0x00E5)) {
            return '\u1E39'; // Latin Small Letter L With Dot Below And Macron = Latin Small Letter L + Combining Dot
            // Below + Combining Macron
        }
        if (base == 0x006D && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E3F'; // Latin Small Letter M With Acute = Latin Small Letter M + Combining Acute Accent
        }
        if (base == 0x006D && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E41'; // Latin Small Letter M With Dot Above = Latin Small Letter M + Combining Dot Above
        }
        if (base == 0x006D && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E43'; // Latin Small Letter M With Dot Below = Latin Small Letter M + Combining Dot Below
        }
        if (base == 0x006E && (diacritic1 == 0x00E4 && diacritic2 == 0x0000)) {
            return '\u00F1'; // Latin Small Letter N With Tilde = Latin Small Letter N + Combining Tilde
        }
        if (base == 0x006E && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0144'; // Latin Small Letter N With Acute = Latin Small Letter N + Combining Acute Accent
        }
        if (base == 0x006E && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0146'; // Latin Small Letter N With Cedilla = Latin Small Letter N + Combining Cedilla
        }
        if (base == 0x006E && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0148'; // Latin Small Letter N With Caron = Latin Small Letter N + Combining Caron
        }
        if (base == 0x006E && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E45'; // Latin Small Letter N With Dot Above = Latin Small Letter N + Combining Dot Above
        }
        if (base == 0x006E && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E47'; // Latin Small Letter N With Dot Below = Latin Small Letter N + Combining Dot Below
        }
        if (base == 0x006E && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E49'; // Latin Small Letter N With Line Below = Latin Small Letter N + Combining Low Line
        }
        if (base == 0x006E && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E4B'; // Latin Small Letter N With Circumflex Below = Latin Small Letter N + Combining Circumflex
            // Accent Below
        }
        if (base == 0x006F && (diacritic1 == 0x00E1 && diacritic2 == 0x0000)) {
            return '\u00F2'; // Latin Small Letter O With Grave = Latin Small Letter O + Combining Grave Accent
        }
        if (base == 0x006F && (diacritic1 == 0x00E2 && diacritic2 == 0x0000)) {
            return '\u00F3'; // Latin Small Letter O With Acute = Latin Small Letter O + Combining Acute Accent
        }
        if (base == 0x006F && (diacritic1 == 0x00E3 && diacritic2 == 0x0000)) {
            return '\u00F4'; // Latin Small Letter O With Circumflex = Latin Small Letter O + Combining Circumflex
            // Accent
        }
        if (base == 0x006F && (diacritic1 == 0x00E4 && diacritic2 == 0x0000)) {
            return '\u00F5'; // Latin Small Letter O With Tilde = Latin Small Letter O + Combining Tilde
        }
        if (base == 0x006F && (diacritic1 == 0x00E8 && diacritic2 == 0x0000)) {
            return '\u00F6'; // Latin Small Letter O With Diaeresis = Latin Small Letter O + Combining Diaeresis
        }
        if (base == 0x006F && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u014D'; // Latin Small Letter O With Macron = Latin Small Letter O + Combining Macron
        }
        if (base == 0x006F && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u014F'; // Latin Small Letter O With Breve = Latin Small Letter O + Combining Breve
        }
        if (base == 0x006F && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u0151'; // Latin Small Letter O With Double Acute = Latin Small Letter O + Combining Double Acute
            // Accent
        }
        if (base == 0x006F && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01A1'; // Latin Small Letter O With Horn = Latin Small Letter O + Combining Horn
        }
        if (base == 0x006F && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01D2'; // Latin Small Letter O With Caron = Latin Small Letter O + Combining Caron
        }
        if (base == 0x006F && (diacritic1 == 0x00E7 && diacritic2 == 0x00E1)) {
            return '\u1E51'; // Latin Small Letter O With Macron And Grave = Latin Small Letter O + Combining Macron +
            // Combining Grave Accent
        }
        if (base == 0x006F && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01EB'; // Latin Small Letter O With Ogonek = Latin Small Letter O + Combining Ogonek
        }
        if (base == 0x006F && (diacritic1 == 0x00E0 && diacritic2 == 0x00E1)) {
            return '\u1ED3'; // Latin Small Letter O With Circumflex And Grave = Latin Small Letter O + Combining
            // Circumflex Accent + Combining Grave Accent
        }
        if (base == 0x006F && (diacritic1 == 0x00E0 && diacritic2 == 0x00E1)) {
            return '\u1EDD'; // Latin Small Letter O With Horn And Grave = Latin Small Letter O + Combining Horn +
            // Combining Grave Accent
        }
        if (base == 0x006F && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u020D'; // Latin Small Letter O With Double Grave = Latin Small Letter O + Combining Double Grave
            // Accent
        }
        if (base == 0x006F && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u020F'; // Latin Small Letter O With Inverted Breve = Latin Small Letter O + Combining Inverted
            // Breve
        }
        if (base == 0x006F && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E4D'; // Latin Small Letter O With Tilde And Acute = Latin Small Letter O + Combining Tilde +
            // Combining Acute Accent
        }
        if (base == 0x006F && (diacritic1 == 0x00E7 && diacritic2 == 0x00E2)) {
            return '\u1E53'; // Latin Small Letter O With Macron And Acute = Latin Small Letter O + Combining Macron +
            // Combining Acute Accent
        }
        if (base == 0x006F && (diacritic1 == 0x00E0 && diacritic2 == 0x00E2)) {
            return '\u1ED1'; // Latin Small Letter O With Circumflex And Acute = Latin Small Letter O + Combining
            // Circumflex Accent + Combining Acute Accent
        }
        if (base == 0x006F && (diacritic1 == 0x00E0 && diacritic2 == 0x00E2)) {
            return '\u1EDB'; // Latin Small Letter O With Horn And Acute = Latin Small Letter O + Combining Horn +
            // Combining Acute Accent
        }
        if (base == 0x006F && (diacritic1 == 0x00E0 && diacritic2 == 0x00E4)) {
            return '\u1ED7'; // Latin Small Letter O With Circumflex And Tilde = Latin Small Letter O + Combining
            // Circumflex Accent + Combining Tilde
        }
        if (base == 0x006F && (diacritic1 == 0x00E0 && diacritic2 == 0x00E4)) {
            return '\u1EE1'; // Latin Small Letter O With Horn And Tilde = Latin Small Letter O + Combining Horn +
            // Combining Tilde
        }
        if (base == 0x006F && (diacritic1 == 0x00E7 && diacritic2 == 0x00E5)) {
            return '\u01ED'; // Latin Small Letter O With Ogonek And Macron = Latin Small Letter O + Combining Ogonek +
            // Combining Macron
        }
        if (base == 0x006F && (diacritic1 == 0x00E0 && diacritic2 == 0x0000)) {
            return '\u1ECD'; // Latin Small Letter O With Dot Below = Latin Small Letter O + Combining Dot Below
        }
        if (base == 0x006F && (diacritic1 == 0x00E0 && diacritic2 == 0x0000)) {
            return '\u1ECF'; // Latin Small Letter O With Hook Above = Latin Small Letter O + Combining Hook Above
        }
        if (base == 0x006F && (diacritic1 == 0x00E7 && diacritic2 == 0x00E8)) {
            return '\u1E4F'; // Latin Small Letter O With Tilde And Diaeresis = Latin Small Letter O + Combining Tilde +
            // Combining Diaeresis
        }
        if (base == 0x006F && (diacritic1 == 0x00E0 && diacritic2 == 0x00E0)) {
            return '\u1ED5'; // Latin Small Letter O With Circumflex And Hook Above = Latin Small Letter O + Combining
            // Circumflex Accent + Combining Hook Above
        }
        if (base == 0x006F && (diacritic1 == 0x00E0 && diacritic2 == 0x00E0)) {
            return '\u1EDF'; // Latin Small Letter O With Horn And Hook Above = Latin Small Letter O + Combining Horn +
            // Combining Hook Above
        }
        if (base == 0x006F && (diacritic1 == 0x00E0 && diacritic2 == 0x00F2)) {
            return '\u1ED9'; // Latin Small Letter O With Circumflex And Dot Below = Latin Small Letter O + Combining
            // Circumflex Accent + Combining Dot Below
        }
        if (base == 0x006F && (diacritic1 == 0x00E0 && diacritic2 == 0x00F2)) {
            return '\u1EE3'; // Latin Small Letter O With Horn And Dot Below = Latin Small Letter O + Combining Horn +
            // Combining Dot Below
        }
        if (base == 0x007A && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u017A'; // Latin Small Letter Z With Acute = Latin Small Letter Z + Combining Acute Accent
        }
        if (base == 0x007A && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u017C'; // Latin Small Letter Z With Dot Above = Latin Small Letter Z + Combining Dot Above
        }
        if (base == 0x007A && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u017E'; // Latin Small Letter Z With Caron = Latin Small Letter Z + Combining Caron
        }
        if (base == 0x007A && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E91'; // Latin Small Letter Z With Circumflex = Latin Small Letter Z + Combining Circumflex
            // Accent
        }
        if (base == 0x007A && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E93'; // Latin Small Letter Z With Dot Below = Latin Small Letter Z + Combining Dot Below
        }
        if (base == 0x007A && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E95'; // Latin Small Letter Z With Line Below = Latin Small Letter Z + Combining Low Line
        }
        if (base == 0x00C6 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01E2'; // Latin Capital Letter Ae With Macron = Latin Capital Letter Ae + Combining Macron
        }
        if (base == 0x00C6 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01FC'; // Latin Capital Letter Ae With Acute = Latin Capital Letter Ae + Combining Acute Accent
        }
        if (base == 0x017F && (diacritic1 == 0x00E7 && diacritic2 == 0x00E4)) {
            return '\u1E9B'; // Latin Small Letter Long S With Dot Above = Latin Small Letter Long S + Combining Dot
            // Above
        }
        if (base == 0x01B7 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01EE'; // Latin Capital Letter Ezh With Caron = Latin Capital Letter Ezh + Combining Caron
        }
        if (base == 0x01F1 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01C4'; // Latin Capital Letter Dz With Caron = Latin Capital Letter Dz + Combining Caron
        }
        if (base == 0x01F3 && (diacritic1 == 0x00E7 && diacritic2 == 0x0000)) {
            return '\u01C6'; // Latin Small Letter Dz With Caron = Latin Small Letter Dz + Combining Caron
        }
        return 0;
    }

}
