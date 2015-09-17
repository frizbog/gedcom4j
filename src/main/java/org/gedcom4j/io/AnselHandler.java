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
            /*
             * Look ahead for combining diacritics after this character - if we find some, they need to be appended
             * prior to the base character
             */
            char oneCharAhead = 0;
            char twoCharAhead = 0;
            if (i + 1 < utf16.length()) {
                oneCharAhead = utf16.charAt(i + 1);
                if (AnselMapping.isUnicodeCombiningDiacritic(oneCharAhead)) {
                    // It's a diacritic - check one more character ahead, combining diacritics can come in pairs
                    if (i + 2 < utf16.length()) {
                        twoCharAhead = utf16.charAt(i + 2);
                        if (AnselMapping.isUnicodeCombiningDiacritic(twoCharAhead)) {
                            // It's another diacritic
                        } else {
                            twoCharAhead = 0; // Wipe out to zero - indicates only one combining diacritic
                        }
                    }
                } else {
                    oneCharAhead = 0; // Wipe out to zero - indicates no combining diacritics at all
                }
            }
            if (twoCharAhead != 0) /* there were two combining diacritics following the base character */{
                ansel.append(AnselMapping.encode(oneCharAhead));
                ansel.append(AnselMapping.encode(twoCharAhead));
                i += 2;
                ansel.append(c);
                continue;
            }
            if (oneCharAhead != 0) /* there was one combining diacritic following the base character */{
                ansel.append(AnselMapping.encode(oneCharAhead));
                i++;
                ansel.append(c);
                continue;
            }

            // No combining diacritics following the base character
            // Characters below a certain point don't need mapping
            if (c < ANSEL_EXTENDED_BEGIN_AT) {
                ansel.append(c);
                continue;
            }

            // Map the character and see if it's a simple extended character and make sure it's not a combining
            // diacritic
            char ec = AnselMapping.encode(c);
            if (ec < ANSEL_DIACRITICS_BEGIN_AT && ec != c) {
                ansel.append(AnselMapping.encode(c));
                continue;
            }

            /*
             * Not a simple basic character, not a simple extended character, could be a pre-combined diacritic or a
             * combining diacritic
             */
            char[] breakdown = getBrokenDownGlyph(c);
            if (breakdown != null) {
                // Precomposed diacritic - write down decomposed character - base character last for ANSEL
                if (breakdown.length > 1 && breakdown[1] > (char) 0x0000) {
                    ansel.append(breakdown[1]); // Already encoded to ANSEL
                }
                if (breakdown.length > 2 && breakdown[2] > (char) 0x0000) {
                    ansel.append(breakdown[2]); // Already encoded to ANSEL
                }
                ansel.append(breakdown[0]);
            } else {
                // Some leftover combining diacritic?
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
            case '\u1EA2': {
                /* LATIN CAPITAL LETTER A WITH HOOK ABOVE */
                return new char[] { (char) 0x41, (char) 0xE0, (char) 0x00 };
            }
            case '\u00C0': {
                /* LATIN CAPITAL LETTER A WITH GRAVE */
                return new char[] { (char) 0x41, (char) 0xE1, (char) 0x00 };
            }
            case '\u00C1': {
                /* LATIN CAPITAL LETTER A WITH ACUTE */
                return new char[] { (char) 0x41, (char) 0xE2, (char) 0x00 };
            }
            case '\u00C2': {
                /* LATIN CAPITAL LETTER A WITH CIRCUMFLEX */
                return new char[] { (char) 0x41, (char) 0xE3, (char) 0x00 };
            }
            case '\u1EA8': {
                /* LATIN CAPITAL LETTER A WITH CIRCUMFLEX AND HOOK ABOVE */
                return new char[] { (char) 0x41, (char) 0xE3, (char) 0xE0 };
            }
            case '\u1EA6': {
                /* LATIN CAPITAL LETTER A WITH CIRCUMFLEX AND GRAVE */
                return new char[] { (char) 0x41, (char) 0xE3, (char) 0xE1 };
            }
            case '\u1EA4': {
                /* LATIN CAPITAL LETTER A WITH CIRCUMFLEX AND ACUTE */
                return new char[] { (char) 0x41, (char) 0xE3, (char) 0xE2 };
            }
            case '\u1EAA': {
                /* LATIN CAPITAL LETTER A WITH CIRCUMFLEX AND TILDE */
                return new char[] { (char) 0x41, (char) 0xE3, (char) 0xE4 };
            }
            case '\u1EAC': {
                /* LATIN CAPITAL LETTER A WITH CIRCUMFLEX AND DOT BELOW */
                return new char[] { (char) 0x41, (char) 0xE3, (char) 0xF2 };
            }
            case '\u00C3': {
                /* LATIN CAPITAL LETTER A WITH TILDE */
                return new char[] { (char) 0x41, (char) 0xE4, (char) 0x00 };
            }
            case '\u0100': {
                /* LATIN CAPITAL LETTER A WITH MACRON */
                return new char[] { (char) 0x41, (char) 0xE5, (char) 0x00 };
            }
            case '\u0102': {
                /* LATIN CAPITAL LETTER A WITH BREVE */
                return new char[] { (char) 0x41, (char) 0xE6, (char) 0x00 };
            }
            case '\u1EB2': {
                /* LATIN CAPITAL LETTER A WITH BREVE AND HOOK ABOVE */
                return new char[] { (char) 0x41, (char) 0xE6, (char) 0xE0 };
            }
            case '\u1EB0': {
                /* LATIN CAPITAL LETTER A WITH BREVE AND GRAVE */
                return new char[] { (char) 0x41, (char) 0xE6, (char) 0xE1 };
            }
            case '\u1EAE': {
                /* LATIN CAPITAL LETTER A WITH BREVE AND ACUTE */
                return new char[] { (char) 0x41, (char) 0xE6, (char) 0xE2 };
            }
            case '\u1EB4': {
                /* LATIN CAPITAL LETTER A WITH BREVE AND TILDE */
                return new char[] { (char) 0x41, (char) 0xE6, (char) 0xE4 };
            }
            case '\u1EB6': {
                /* LATIN CAPITAL LETTER A WITH BREVE AND DOT BELOW */
                return new char[] { (char) 0x41, (char) 0xE6, (char) 0xF2 };
            }
            case '\u0226': {
                /* LATIN CAPITAL LETTER A WITH DOT ABOVE */
                return new char[] { (char) 0x41, (char) 0xE7, (char) 0x00 };
            }
            case '\u01E0': {
                /* LATIN CAPITAL LETTER A WITH DOT ABOVE AND MACRON */
                return new char[] { (char) 0x41, (char) 0xE7, (char) 0xE5 };
            }
            case '\u00C4': {
                /* LATIN CAPITAL LETTER A WITH DIAERESIS */
                return new char[] { (char) 0x41, (char) 0xE8, (char) 0x00 };
            }
            case '\u01DE': {
                /* LATIN CAPITAL LETTER A WITH DIAERESIS AND MACRON */
                return new char[] { (char) 0x41, (char) 0xE8, (char) 0xE5 };
            }
            case '\u01CD': {
                /* LATIN CAPITAL LETTER A WITH CARON */
                return new char[] { (char) 0x41, (char) 0xE9, (char) 0x00 };
            }
            case '\u00C5': {
                /* LATIN CAPITAL LETTER A WITH RING ABOVE */
                return new char[] { (char) 0x41, (char) 0xEA, (char) 0x00 };
            }
            case '\u01FA': {
                /* LATIN CAPITAL LETTER A WITH RING ABOVE AND ACUTE */
                return new char[] { (char) 0x41, (char) 0xEA, (char) 0xE2 };
            }
            case '\u0104': {
                /* LATIN CAPITAL LETTER A WITH OGONEK */
                return new char[] { (char) 0x41, (char) 0xF1, (char) 0x00 };
            }
            case '\u1EA0': {
                /* LATIN CAPITAL LETTER A WITH DOT BELOW */
                return new char[] { (char) 0x41, (char) 0xF2, (char) 0x00 };
            }
            case '\u1E00': {
                /* LATIN CAPITAL LETTER A WITH RING BELOW */
                return new char[] { (char) 0x41, (char) 0xF4, (char) 0x00 };
            }
            case '\u1E02': {
                /* LATIN CAPITAL LETTER B WITH DOT ABOVE */
                return new char[] { (char) 0x42, (char) 0xE7, (char) 0x00 };
            }
            case '\u1E04': {
                /* LATIN CAPITAL LETTER B WITH DOT BELOW */
                return new char[] { (char) 0x42, (char) 0xF2, (char) 0x00 };
            }
            case '\u0106': {
                /* LATIN CAPITAL LETTER C WITH ACUTE */
                return new char[] { (char) 0x43, (char) 0xE2, (char) 0x00 };
            }
            case '\u0108': {
                /* LATIN CAPITAL LETTER C WITH CIRCUMFLEX */
                return new char[] { (char) 0x43, (char) 0xE3, (char) 0x00 };
            }
            case '\u010A': {
                /* LATIN CAPITAL LETTER C WITH DOT ABOVE */
                return new char[] { (char) 0x43, (char) 0xE7, (char) 0x00 };
            }
            case '\u010C': {
                /* LATIN CAPITAL LETTER C WITH CARON */
                return new char[] { (char) 0x43, (char) 0xE9, (char) 0x00 };
            }
            case '\u00C7': {
                /* LATIN CAPITAL LETTER C WITH CEDILLA */
                return new char[] { (char) 0x43, (char) 0xF0, (char) 0x00 };
            }
            case '\u1E08': {
                /* LATIN CAPITAL LETTER C WITH CEDILLA AND ACUTE */
                return new char[] { (char) 0x43, (char) 0xF0, (char) 0xE2 };
            }
            case '\u1E0A': {
                /* LATIN CAPITAL LETTER D WITH DOT ABOVE */
                return new char[] { (char) 0x44, (char) 0xE7, (char) 0x00 };
            }
            case '\u010E': {
                /* LATIN CAPITAL LETTER D WITH CARON */
                return new char[] { (char) 0x44, (char) 0xE9, (char) 0x00 };
            }
            case '\u1E10': {
                /* LATIN CAPITAL LETTER D WITH CEDILLA */
                return new char[] { (char) 0x44, (char) 0xF0, (char) 0x00 };
            }
            case '\u1E0C': {
                /* LATIN CAPITAL LETTER D WITH DOT BELOW */
                return new char[] { (char) 0x44, (char) 0xF2, (char) 0x00 };
            }
            case '\u1EBA': {
                /* LATIN CAPITAL LETTER E WITH HOOK ABOVE */
                return new char[] { (char) 0x45, (char) 0xE0, (char) 0x00 };
            }
            case '\u00C8': {
                /* LATIN CAPITAL LETTER E WITH GRAVE */
                return new char[] { (char) 0x45, (char) 0xE1, (char) 0x00 };
            }
            case '\u00C9': {
                /* LATIN CAPITAL LETTER E WITH ACUTE */
                return new char[] { (char) 0x45, (char) 0xE2, (char) 0x00 };
            }
            case '\u00CA': {
                /* LATIN CAPITAL LETTER E WITH CIRCUMFLEX */
                return new char[] { (char) 0x45, (char) 0xE3, (char) 0x00 };
            }
            case '\u1EC2': {
                /* LATIN CAPITAL LETTER E WITH CIRCUMFLEX AND HOOK ABOVE */
                return new char[] { (char) 0x45, (char) 0xE3, (char) 0xE0 };
            }
            case '\u1EC0': {
                /* LATIN CAPITAL LETTER E WITH CIRCUMFLEX AND GRAVE */
                return new char[] { (char) 0x45, (char) 0xE3, (char) 0xE1 };
            }
            case '\u1EBE': {
                /* LATIN CAPITAL LETTER E WITH CIRCUMFLEX AND ACUTE */
                return new char[] { (char) 0x45, (char) 0xE3, (char) 0xE2 };
            }
            case '\u1EC4': {
                /* LATIN CAPITAL LETTER E WITH CIRCUMFLEX AND TILDE */
                return new char[] { (char) 0x45, (char) 0xE3, (char) 0xE4 };
            }
            case '\u1EC6': {
                /* LATIN CAPITAL LETTER E WITH CIRCUMFLEX AND DOT BELOW */
                return new char[] { (char) 0x45, (char) 0xE3, (char) 0xF2 };
            }
            case '\u1EBC': {
                /* LATIN CAPITAL LETTER E WITH TILDE */
                return new char[] { (char) 0x45, (char) 0xE4, (char) 0x00 };
            }
            case '\u0112': {
                /* LATIN CAPITAL LETTER E WITH MACRON */
                return new char[] { (char) 0x45, (char) 0xE5, (char) 0x00 };
            }
            case '\u1E14': {
                /* LATIN CAPITAL LETTER E WITH MACRON AND GRAVE */
                return new char[] { (char) 0x45, (char) 0xE5, (char) 0xE1 };
            }
            case '\u1E16': {
                /* LATIN CAPITAL LETTER E WITH MACRON AND ACUTE */
                return new char[] { (char) 0x45, (char) 0xE5, (char) 0xE2 };
            }
            case '\u0114': {
                /* LATIN CAPITAL LETTER E WITH BREVE */
                return new char[] { (char) 0x45, (char) 0xE6, (char) 0x00 };
            }
            case '\u0116': {
                /* LATIN CAPITAL LETTER E WITH DOT ABOVE */
                return new char[] { (char) 0x45, (char) 0xE7, (char) 0x00 };
            }
            case '\u00CB': {
                /* LATIN CAPITAL LETTER E WITH DIAERESIS */
                return new char[] { (char) 0x45, (char) 0xE8, (char) 0x00 };
            }
            case '\u011A': {
                /* LATIN CAPITAL LETTER E WITH CARON */
                return new char[] { (char) 0x45, (char) 0xE9, (char) 0x00 };
            }
            case '\u0228': {
                /* LATIN CAPITAL LETTER E WITH CEDILLA */
                return new char[] { (char) 0x45, (char) 0xF0, (char) 0x00 };
            }
            case '\u1E1C': {
                /* LATIN CAPITAL LETTER E WITH CEDILLA AND BREVE */
                return new char[] { (char) 0x45, (char) 0xF0, (char) 0xE6 };
            }
            case '\u0118': {
                /* LATIN CAPITAL LETTER E WITH OGONEK */
                return new char[] { (char) 0x45, (char) 0xF1, (char) 0x00 };
            }
            case '\u1EB8': {
                /* LATIN CAPITAL LETTER E WITH DOT BELOW */
                return new char[] { (char) 0x45, (char) 0xF2, (char) 0x00 };
            }
            case '\u1E1E': {
                /* LATIN CAPITAL LETTER F WITH DOT ABOVE */
                return new char[] { (char) 0x46, (char) 0xE7, (char) 0x00 };
            }
            case '\u01F4': {
                /* LATIN CAPITAL LETTER G WITH ACUTE */
                return new char[] { (char) 0x47, (char) 0xE2, (char) 0x00 };
            }
            case '\u011C': {
                /* LATIN CAPITAL LETTER G WITH CIRCUMFLEX */
                return new char[] { (char) 0x47, (char) 0xE3, (char) 0x00 };
            }
            case '\u1E20': {
                /* LATIN CAPITAL LETTER G WITH MACRON */
                return new char[] { (char) 0x47, (char) 0xE5, (char) 0x00 };
            }
            case '\u011E': {
                /* LATIN CAPITAL LETTER G WITH BREVE */
                return new char[] { (char) 0x47, (char) 0xE6, (char) 0x00 };
            }
            case '\u0120': {
                /* LATIN CAPITAL LETTER G WITH DOT ABOVE */
                return new char[] { (char) 0x47, (char) 0xE7, (char) 0x00 };
            }
            case '\u01E6': {
                /* LATIN CAPITAL LETTER G WITH CARON */
                return new char[] { (char) 0x47, (char) 0xE9, (char) 0x00 };
            }
            case '\u0122': {
                /* LATIN CAPITAL LETTER G WITH CEDILLA */
                return new char[] { (char) 0x47, (char) 0xF0, (char) 0x00 };
            }
            case '\u0124': {
                /* LATIN CAPITAL LETTER H WITH CIRCUMFLEX */
                return new char[] { (char) 0x48, (char) 0xE3, (char) 0x00 };
            }
            case '\u1E22': {
                /* LATIN CAPITAL LETTER H WITH DOT ABOVE */
                return new char[] { (char) 0x48, (char) 0xE7, (char) 0x00 };
            }
            case '\u1E26': {
                /* LATIN CAPITAL LETTER H WITH DIAERESIS */
                return new char[] { (char) 0x48, (char) 0xE8, (char) 0x00 };
            }
            case '\u021E': {
                /* LATIN CAPITAL LETTER H WITH CARON */
                return new char[] { (char) 0x48, (char) 0xE9, (char) 0x00 };
            }
            case '\u1E28': {
                /* LATIN CAPITAL LETTER H WITH CEDILLA */
                return new char[] { (char) 0x48, (char) 0xF0, (char) 0x00 };
            }
            case '\u1E24': {
                /* LATIN CAPITAL LETTER H WITH DOT BELOW */
                return new char[] { (char) 0x48, (char) 0xF2, (char) 0x00 };
            }
            case '\u1E2A': {
                /* LATIN CAPITAL LETTER H WITH BREVE BELOW */
                return new char[] { (char) 0x48, (char) 0xF9, (char) 0x00 };
            }
            case '\u1EC8': {
                /* LATIN CAPITAL LETTER I WITH HOOK ABOVE */
                return new char[] { (char) 0x49, (char) 0xE0, (char) 0x00 };
            }
            case '\u00CC': {
                /* LATIN CAPITAL LETTER I WITH GRAVE */
                return new char[] { (char) 0x49, (char) 0xE1, (char) 0x00 };
            }
            case '\u00CD': {
                /* LATIN CAPITAL LETTER I WITH ACUTE */
                return new char[] { (char) 0x49, (char) 0xE2, (char) 0x00 };
            }
            case '\u00CE': {
                /* LATIN CAPITAL LETTER I WITH CIRCUMFLEX */
                return new char[] { (char) 0x49, (char) 0xE3, (char) 0x00 };
            }
            case '\u0128': {
                /* LATIN CAPITAL LETTER I WITH TILDE */
                return new char[] { (char) 0x49, (char) 0xE4, (char) 0x00 };
            }
            case '\u012A': {
                /* LATIN CAPITAL LETTER I WITH MACRON */
                return new char[] { (char) 0x49, (char) 0xE5, (char) 0x00 };
            }
            case '\u012C': {
                /* LATIN CAPITAL LETTER I WITH BREVE */
                return new char[] { (char) 0x49, (char) 0xE6, (char) 0x00 };
            }
            case '\u0130': {
                /* LATIN CAPITAL LETTER I WITH DOT ABOVE */
                return new char[] { (char) 0x49, (char) 0xE7, (char) 0x00 };
            }
            case '\u00CF': {
                /* LATIN CAPITAL LETTER I WITH DIAERESIS */
                return new char[] { (char) 0x49, (char) 0xE8, (char) 0x00 };
            }
            case '\u1E2E': {
                /* LATIN CAPITAL LETTER I WITH DIAERESIS AND ACUTE */
                return new char[] { (char) 0x49, (char) 0xE8, (char) 0xE2 };
            }
            case '\u01CF': {
                /* LATIN CAPITAL LETTER I WITH CARON */
                return new char[] { (char) 0x49, (char) 0xE9, (char) 0x00 };
            }
            case '\u012E': {
                /* LATIN CAPITAL LETTER I WITH OGONEK */
                return new char[] { (char) 0x49, (char) 0xF1, (char) 0x00 };
            }
            case '\u1ECA': {
                /* LATIN CAPITAL LETTER I WITH DOT BELOW */
                return new char[] { (char) 0x49, (char) 0xF2, (char) 0x00 };
            }
            case '\u0134': {
                /* LATIN CAPITAL LETTER J WITH CIRCUMFLEX */
                return new char[] { (char) 0x4A, (char) 0xE3, (char) 0x00 };
            }
            case '\u1E30': {
                /* LATIN CAPITAL LETTER K WITH ACUTE */
                return new char[] { (char) 0x4B, (char) 0xE2, (char) 0x00 };
            }
            case '\u01E8': {
                /* LATIN CAPITAL LETTER K WITH CARON */
                return new char[] { (char) 0x4B, (char) 0xE9, (char) 0x00 };
            }
            case '\u0136': {
                /* LATIN CAPITAL LETTER K WITH CEDILLA */
                return new char[] { (char) 0x4B, (char) 0xF0, (char) 0x00 };
            }
            case '\u1E32': {
                /* LATIN CAPITAL LETTER K WITH DOT BELOW */
                return new char[] { (char) 0x4B, (char) 0xF2, (char) 0x00 };
            }
            case '\u0139': {
                /* LATIN CAPITAL LETTER L WITH ACUTE */
                return new char[] { (char) 0x4C, (char) 0xE2, (char) 0x00 };
            }
            case '\u013D': {
                /* LATIN CAPITAL LETTER L WITH CARON */
                return new char[] { (char) 0x4C, (char) 0xE9, (char) 0x00 };
            }
            case '\u013B': {
                /* LATIN CAPITAL LETTER L WITH CEDILLA */
                return new char[] { (char) 0x4C, (char) 0xF0, (char) 0x00 };
            }
            case '\u1E36': {
                /* LATIN CAPITAL LETTER L WITH DOT BELOW */
                return new char[] { (char) 0x4C, (char) 0xF2, (char) 0x00 };
            }
            case '\u1E38': {
                /* LATIN CAPITAL LETTER L WITH DOT BELOW AND MACRON */
                return new char[] { (char) 0x4C, (char) 0xF2, (char) 0xE5 };
            }
            case '\u1E3E': {
                /* LATIN CAPITAL LETTER M WITH ACUTE */
                return new char[] { (char) 0x4D, (char) 0xE2, (char) 0x00 };
            }
            case '\u1E40': {
                /* LATIN CAPITAL LETTER M WITH DOT ABOVE */
                return new char[] { (char) 0x4D, (char) 0xE7, (char) 0x00 };
            }
            case '\u1E42': {
                /* LATIN CAPITAL LETTER M WITH DOT BELOW */
                return new char[] { (char) 0x4D, (char) 0xF2, (char) 0x00 };
            }
            case '\u01F8': {
                /* LATIN CAPITAL LETTER N WITH GRAVE */
                return new char[] { (char) 0x4E, (char) 0xE1, (char) 0x00 };
            }
            case '\u0143': {
                /* LATIN CAPITAL LETTER N WITH ACUTE */
                return new char[] { (char) 0x4E, (char) 0xE2, (char) 0x00 };
            }
            case '\u00D1': {
                /* LATIN CAPITAL LETTER N WITH TILDE */
                return new char[] { (char) 0x4E, (char) 0xE4, (char) 0x00 };
            }
            case '\u1E44': {
                /* LATIN CAPITAL LETTER N WITH DOT ABOVE */
                return new char[] { (char) 0x4E, (char) 0xE7, (char) 0x00 };
            }
            case '\u0147': {
                /* LATIN CAPITAL LETTER N WITH CARON */
                return new char[] { (char) 0x4E, (char) 0xE9, (char) 0x00 };
            }
            case '\u0145': {
                /* LATIN CAPITAL LETTER N WITH CEDILLA */
                return new char[] { (char) 0x4E, (char) 0xF0, (char) 0x00 };
            }
            case '\u1E46': {
                /* LATIN CAPITAL LETTER N WITH DOT BELOW */
                return new char[] { (char) 0x4E, (char) 0xF2, (char) 0x00 };
            }
            case '\u1ECE': {
                /* LATIN CAPITAL LETTER O WITH HOOK ABOVE */
                return new char[] { (char) 0x4F, (char) 0xE0, (char) 0x00 };
            }
            case '\u00D2': {
                /* LATIN CAPITAL LETTER O WITH GRAVE */
                return new char[] { (char) 0x4F, (char) 0xE1, (char) 0x00 };
            }
            case '\u00D3': {
                /* LATIN CAPITAL LETTER O WITH ACUTE */
                return new char[] { (char) 0x4F, (char) 0xE2, (char) 0x00 };
            }
            case '\u00D4': {
                /* LATIN CAPITAL LETTER O WITH CIRCUMFLEX */
                return new char[] { (char) 0x4F, (char) 0xE3, (char) 0x00 };
            }
            case '\u1ED4': {
                /* LATIN CAPITAL LETTER O WITH CIRCUMFLEX AND HOOK ABOVE */
                return new char[] { (char) 0x4F, (char) 0xE3, (char) 0xE0 };
            }
            case '\u1ED2': {
                /* LATIN CAPITAL LETTER O WITH CIRCUMFLEX AND GRAVE */
                return new char[] { (char) 0x4F, (char) 0xE3, (char) 0xE1 };
            }
            case '\u1ED0': {
                /* LATIN CAPITAL LETTER O WITH CIRCUMFLEX AND ACUTE */
                return new char[] { (char) 0x4F, (char) 0xE3, (char) 0xE2 };
            }
            case '\u1ED6': {
                /* LATIN CAPITAL LETTER O WITH CIRCUMFLEX AND TILDE */
                return new char[] { (char) 0x4F, (char) 0xE3, (char) 0xE4 };
            }
            case '\u1ED8': {
                /* LATIN CAPITAL LETTER O WITH CIRCUMFLEX AND DOT BELOW */
                return new char[] { (char) 0x4F, (char) 0xE3, (char) 0xF2 };
            }
            case '\u00D5': {
                /* LATIN CAPITAL LETTER O WITH TILDE */
                return new char[] { (char) 0x4F, (char) 0xE4, (char) 0x00 };
            }
            case '\u1E4C': {
                /* LATIN CAPITAL LETTER O WITH TILDE AND ACUTE */
                return new char[] { (char) 0x4F, (char) 0xE4, (char) 0xE2 };
            }
            case '\u022C': {
                /* LATIN CAPITAL LETTER O WITH TILDE AND MACRON */
                return new char[] { (char) 0x4F, (char) 0xE4, (char) 0xE5 };
            }
            case '\u1E4E': {
                /* LATIN CAPITAL LETTER O WITH TILDE AND DIAERESIS */
                return new char[] { (char) 0x4F, (char) 0xE4, (char) 0xE8 };
            }
            case '\u014C': {
                /* LATIN CAPITAL LETTER O WITH MACRON */
                return new char[] { (char) 0x4F, (char) 0xE5, (char) 0x00 };
            }
            case '\u1E50': {
                /* LATIN CAPITAL LETTER O WITH MACRON AND GRAVE */
                return new char[] { (char) 0x4F, (char) 0xE5, (char) 0xE1 };
            }
            case '\u1E52': {
                /* LATIN CAPITAL LETTER O WITH MACRON AND ACUTE */
                return new char[] { (char) 0x4F, (char) 0xE5, (char) 0xE2 };
            }
            case '\u014E': {
                /* LATIN CAPITAL LETTER O WITH BREVE */
                return new char[] { (char) 0x4F, (char) 0xE6, (char) 0x00 };
            }
            case '\u022E': {
                /* LATIN CAPITAL LETTER O WITH DOT ABOVE */
                return new char[] { (char) 0x4F, (char) 0xE7, (char) 0x00 };
            }
            case '\u0230': {
                /* LATIN CAPITAL LETTER O WITH DOT ABOVE AND MACRON */
                return new char[] { (char) 0x4F, (char) 0xE7, (char) 0xE5 };
            }
            case '\u00D6': {
                /* LATIN CAPITAL LETTER O WITH DIAERESIS */
                return new char[] { (char) 0x4F, (char) 0xE8, (char) 0x00 };
            }
            case '\u022A': {
                /* LATIN CAPITAL LETTER O WITH DIAERESIS AND MACRON */
                return new char[] { (char) 0x4F, (char) 0xE8, (char) 0xE5 };
            }
            case '\u01D1': {
                /* LATIN CAPITAL LETTER O WITH CARON */
                return new char[] { (char) 0x4F, (char) 0xE9, (char) 0x00 };
            }
            case '\u0150': {
                /* LATIN CAPITAL LETTER O WITH DOUBLE ACUTE */
                return new char[] { (char) 0x4F, (char) 0xEE, (char) 0x00 };
            }
            case '\u01EA': {
                /* LATIN CAPITAL LETTER O WITH OGONEK */
                return new char[] { (char) 0x4F, (char) 0xF1, (char) 0x00 };
            }
            case '\u01EC': {
                /* LATIN CAPITAL LETTER O WITH OGONEK AND MACRON */
                return new char[] { (char) 0x4F, (char) 0xF1, (char) 0xE5 };
            }
            case '\u1ECC': {
                /* LATIN CAPITAL LETTER O WITH DOT BELOW */
                return new char[] { (char) 0x4F, (char) 0xF2, (char) 0x00 };
            }
            case '\u1E54': {
                /* LATIN CAPITAL LETTER P WITH ACUTE */
                return new char[] { (char) 0x50, (char) 0xE2, (char) 0x00 };
            }
            case '\u1E56': {
                /* LATIN CAPITAL LETTER P WITH DOT ABOVE */
                return new char[] { (char) 0x50, (char) 0xE7, (char) 0x00 };
            }
            case '\u0154': {
                /* LATIN CAPITAL LETTER R WITH ACUTE */
                return new char[] { (char) 0x52, (char) 0xE2, (char) 0x00 };
            }
            case '\u1E58': {
                /* LATIN CAPITAL LETTER R WITH DOT ABOVE */
                return new char[] { (char) 0x52, (char) 0xE7, (char) 0x00 };
            }
            case '\u0158': {
                /* LATIN CAPITAL LETTER R WITH CARON */
                return new char[] { (char) 0x52, (char) 0xE9, (char) 0x00 };
            }
            case '\u0156': {
                /* LATIN CAPITAL LETTER R WITH CEDILLA */
                return new char[] { (char) 0x52, (char) 0xF0, (char) 0x00 };
            }
            case '\u1E5A': {
                /* LATIN CAPITAL LETTER R WITH DOT BELOW */
                return new char[] { (char) 0x52, (char) 0xF2, (char) 0x00 };
            }
            case '\u1E5C': {
                /* LATIN CAPITAL LETTER R WITH DOT BELOW AND MACRON */
                return new char[] { (char) 0x52, (char) 0xF2, (char) 0xE5 };
            }
            case '\u015A': {
                /* LATIN CAPITAL LETTER S WITH ACUTE */
                return new char[] { (char) 0x53, (char) 0xE2, (char) 0x00 };
            }
            case '\u1E64': {
                /* LATIN CAPITAL LETTER S WITH ACUTE AND DOT ABOVE */
                return new char[] { (char) 0x53, (char) 0xE2, (char) 0xE7 };
            }
            case '\u015C': {
                /* LATIN CAPITAL LETTER S WITH CIRCUMFLEX */
                return new char[] { (char) 0x53, (char) 0xE3, (char) 0x00 };
            }
            case '\u1E60': {
                /* LATIN CAPITAL LETTER S WITH DOT ABOVE */
                return new char[] { (char) 0x53, (char) 0xE7, (char) 0x00 };
            }
            case '\u0160': {
                /* LATIN CAPITAL LETTER S WITH CARON */
                return new char[] { (char) 0x53, (char) 0xE9, (char) 0x00 };
            }
            case '\u1E66': {
                /* LATIN CAPITAL LETTER S WITH CARON AND DOT ABOVE */
                return new char[] { (char) 0x53, (char) 0xE9, (char) 0xE7 };
            }
            case '\u015E': {
                /* LATIN CAPITAL LETTER S WITH CEDILLA */
                return new char[] { (char) 0x53, (char) 0xF0, (char) 0x00 };
            }
            case '\u1E62': {
                /* LATIN CAPITAL LETTER S WITH DOT BELOW */
                return new char[] { (char) 0x53, (char) 0xF2, (char) 0x00 };
            }
            case '\u1E68': {
                /* LATIN CAPITAL LETTER S WITH DOT BELOW AND DOT ABOVE */
                return new char[] { (char) 0x53, (char) 0xF2, (char) 0xE7 };
            }
            case '\u0218': {
                /* LATIN CAPITAL LETTER S WITH COMMA BELOW */
                return new char[] { (char) 0x53, (char) 0xF7, (char) 0x00 };
            }
            case '\u1E6A': {
                /* LATIN CAPITAL LETTER T WITH DOT ABOVE */
                return new char[] { (char) 0x54, (char) 0xE7, (char) 0x00 };
            }
            case '\u0164': {
                /* LATIN CAPITAL LETTER T WITH CARON */
                return new char[] { (char) 0x54, (char) 0xE9, (char) 0x00 };
            }
            case '\u0162': {
                /* LATIN CAPITAL LETTER T WITH CEDILLA */
                return new char[] { (char) 0x54, (char) 0xF0, (char) 0x00 };
            }
            case '\u1E6C': {
                /* LATIN CAPITAL LETTER T WITH DOT BELOW */
                return new char[] { (char) 0x54, (char) 0xF2, (char) 0x00 };
            }
            case '\u021A': {
                /* LATIN CAPITAL LETTER T WITH COMMA BELOW */
                return new char[] { (char) 0x54, (char) 0xF7, (char) 0x00 };
            }
            case '\u1EE6': {
                /* LATIN CAPITAL LETTER U WITH HOOK ABOVE */
                return new char[] { (char) 0x55, (char) 0xE0, (char) 0x00 };
            }
            case '\u00D9': {
                /* LATIN CAPITAL LETTER U WITH GRAVE */
                return new char[] { (char) 0x55, (char) 0xE1, (char) 0x00 };
            }
            case '\u00DA': {
                /* LATIN CAPITAL LETTER U WITH ACUTE */
                return new char[] { (char) 0x55, (char) 0xE2, (char) 0x00 };
            }
            case '\u00DB': {
                /* LATIN CAPITAL LETTER U WITH CIRCUMFLEX */
                return new char[] { (char) 0x55, (char) 0xE3, (char) 0x00 };
            }
            case '\u0168': {
                /* LATIN CAPITAL LETTER U WITH TILDE */
                return new char[] { (char) 0x55, (char) 0xE4, (char) 0x00 };
            }
            case '\u1E78': {
                /* LATIN CAPITAL LETTER U WITH TILDE AND ACUTE */
                return new char[] { (char) 0x55, (char) 0xE4, (char) 0xE2 };
            }
            case '\u016A': {
                /* LATIN CAPITAL LETTER U WITH MACRON */
                return new char[] { (char) 0x55, (char) 0xE5, (char) 0x00 };
            }
            case '\u1E7A': {
                /* LATIN CAPITAL LETTER U WITH MACRON AND DIAERESIS */
                return new char[] { (char) 0x55, (char) 0xE5, (char) 0xE8 };
            }
            case '\u016C': {
                /* LATIN CAPITAL LETTER U WITH BREVE */
                return new char[] { (char) 0x55, (char) 0xE6, (char) 0x00 };
            }
            case '\u00DC': {
                /* LATIN CAPITAL LETTER U WITH DIAERESIS */
                return new char[] { (char) 0x55, (char) 0xE8, (char) 0x00 };
            }
            case '\u01DB': {
                /* LATIN CAPITAL LETTER U WITH DIAERESIS AND GRAVE */
                return new char[] { (char) 0x55, (char) 0xE8, (char) 0xE1 };
            }
            case '\u01D7': {
                /* LATIN CAPITAL LETTER U WITH DIAERESIS AND ACUTE */
                return new char[] { (char) 0x55, (char) 0xE8, (char) 0xE2 };
            }
            case '\u01D5': {
                /* LATIN CAPITAL LETTER U WITH DIAERESIS AND MACRON */
                return new char[] { (char) 0x55, (char) 0xE8, (char) 0xE5 };
            }
            case '\u01D9': {
                /* LATIN CAPITAL LETTER U WITH DIAERESIS AND CARON */
                return new char[] { (char) 0x55, (char) 0xE8, (char) 0xE9 };
            }
            case '\u01D3': {
                /* LATIN CAPITAL LETTER U WITH CARON */
                return new char[] { (char) 0x55, (char) 0xE9, (char) 0x00 };
            }
            case '\u016E': {
                /* LATIN CAPITAL LETTER U WITH RING ABOVE */
                return new char[] { (char) 0x55, (char) 0xEA, (char) 0x00 };
            }
            case '\u0170': {
                /* LATIN CAPITAL LETTER U WITH DOUBLE ACUTE */
                return new char[] { (char) 0x55, (char) 0xEE, (char) 0x00 };
            }
            case '\u0172': {
                /* LATIN CAPITAL LETTER U WITH OGONEK */
                return new char[] { (char) 0x55, (char) 0xF1, (char) 0x00 };
            }
            case '\u1EE4': {
                /* LATIN CAPITAL LETTER U WITH DOT BELOW */
                return new char[] { (char) 0x55, (char) 0xF2, (char) 0x00 };
            }
            case '\u1E72': {
                /* LATIN CAPITAL LETTER U WITH DIAERESIS BELOW */
                return new char[] { (char) 0x55, (char) 0xF3, (char) 0x00 };
            }
            case '\u1E7C': {
                /* LATIN CAPITAL LETTER V WITH TILDE */
                return new char[] { (char) 0x56, (char) 0xE4, (char) 0x00 };
            }
            case '\u1E7E': {
                /* LATIN CAPITAL LETTER V WITH DOT BELOW */
                return new char[] { (char) 0x56, (char) 0xF2, (char) 0x00 };
            }
            case '\u1E80': {
                /* LATIN CAPITAL LETTER W WITH GRAVE */
                return new char[] { (char) 0x57, (char) 0xE1, (char) 0x00 };
            }
            case '\u1E82': {
                /* LATIN CAPITAL LETTER W WITH ACUTE */
                return new char[] { (char) 0x57, (char) 0xE2, (char) 0x00 };
            }
            case '\u0174': {
                /* LATIN CAPITAL LETTER W WITH CIRCUMFLEX */
                return new char[] { (char) 0x57, (char) 0xE3, (char) 0x00 };
            }
            case '\u1E86': {
                /* LATIN CAPITAL LETTER W WITH DOT ABOVE */
                return new char[] { (char) 0x57, (char) 0xE7, (char) 0x00 };
            }
            case '\u1E84': {
                /* LATIN CAPITAL LETTER W WITH DIAERESIS */
                return new char[] { (char) 0x57, (char) 0xE8, (char) 0x00 };
            }
            case '\u1E88': {
                /* LATIN CAPITAL LETTER W WITH DOT BELOW */
                return new char[] { (char) 0x57, (char) 0xF2, (char) 0x00 };
            }
            case '\u1E8A': {
                /* LATIN CAPITAL LETTER X WITH DOT ABOVE */
                return new char[] { (char) 0x58, (char) 0xE7, (char) 0x00 };
            }
            case '\u1E8C': {
                /* LATIN CAPITAL LETTER X WITH DIAERESIS */
                return new char[] { (char) 0x58, (char) 0xE8, (char) 0x00 };
            }
            case '\u1EF6': {
                /* LATIN CAPITAL LETTER Y WITH HOOK ABOVE */
                return new char[] { (char) 0x59, (char) 0xE0, (char) 0x00 };
            }
            case '\u1EF2': {
                /* LATIN CAPITAL LETTER Y WITH GRAVE */
                return new char[] { (char) 0x59, (char) 0xE1, (char) 0x00 };
            }
            case '\u00DD': {
                /* LATIN CAPITAL LETTER Y WITH ACUTE */
                return new char[] { (char) 0x59, (char) 0xE2, (char) 0x00 };
            }
            case '\u0176': {
                /* LATIN CAPITAL LETTER Y WITH CIRCUMFLEX */
                return new char[] { (char) 0x59, (char) 0xE3, (char) 0x00 };
            }
            case '\u1EF8': {
                /* LATIN CAPITAL LETTER Y WITH TILDE */
                return new char[] { (char) 0x59, (char) 0xE4, (char) 0x00 };
            }
            case '\u0232': {
                /* LATIN CAPITAL LETTER Y WITH MACRON */
                return new char[] { (char) 0x59, (char) 0xE5, (char) 0x00 };
            }
            case '\u1E8E': {
                /* LATIN CAPITAL LETTER Y WITH DOT ABOVE */
                return new char[] { (char) 0x59, (char) 0xE7, (char) 0x00 };
            }
            case '\u0178': {
                /* LATIN CAPITAL LETTER Y WITH DIAERESIS */
                return new char[] { (char) 0x59, (char) 0xE8, (char) 0x00 };
            }
            case '\u1EF4': {
                /* LATIN CAPITAL LETTER Y WITH DOT BELOW */
                return new char[] { (char) 0x59, (char) 0xF2, (char) 0x00 };
            }
            case '\u0179': {
                /* LATIN CAPITAL LETTER Z WITH ACUTE */
                return new char[] { (char) 0x5A, (char) 0xE2, (char) 0x00 };
            }
            case '\u1E90': {
                /* LATIN CAPITAL LETTER Z WITH CIRCUMFLEX */
                return new char[] { (char) 0x5A, (char) 0xE3, (char) 0x00 };
            }
            case '\u017B': {
                /* LATIN CAPITAL LETTER Z WITH DOT ABOVE */
                return new char[] { (char) 0x5A, (char) 0xE7, (char) 0x00 };
            }
            case '\u017D': {
                /* LATIN CAPITAL LETTER Z WITH CARON */
                return new char[] { (char) 0x5A, (char) 0xE9, (char) 0x00 };
            }
            case '\u1E92': {
                /* LATIN CAPITAL LETTER Z WITH DOT BELOW */
                return new char[] { (char) 0x5A, (char) 0xF2, (char) 0x00 };
            }
            case '\u1EA3': {
                /* LATIN SMALL LETTER A WITH HOOK ABOVE */
                return new char[] { (char) 0x61, (char) 0xE0, (char) 0x00 };
            }
            case '\u00E0': {
                /* LATIN SMALL LETTER A WITH GRAVE */
                return new char[] { (char) 0x61, (char) 0xE1, (char) 0x00 };
            }
            case '\u00E1': {
                /* LATIN SMALL LETTER A WITH ACUTE */
                return new char[] { (char) 0x61, (char) 0xE2, (char) 0x00 };
            }
            case '\u00E2': {
                /* LATIN SMALL LETTER A WITH CIRCUMFLEX */
                return new char[] { (char) 0x61, (char) 0xE3, (char) 0x00 };
            }
            case '\u1EA9': {
                /* LATIN SMALL LETTER A WITH CIRCUMFLEX AND HOOK ABOVE */
                return new char[] { (char) 0x61, (char) 0xE3, (char) 0xE0 };
            }
            case '\u1EA7': {
                /* LATIN SMALL LETTER A WITH CIRCUMFLEX AND GRAVE */
                return new char[] { (char) 0x61, (char) 0xE3, (char) 0xE1 };
            }
            case '\u1EA5': {
                /* LATIN SMALL LETTER A WITH CIRCUMFLEX AND ACUTE */
                return new char[] { (char) 0x61, (char) 0xE3, (char) 0xE2 };
            }
            case '\u1EAB': {
                /* LATIN SMALL LETTER A WITH CIRCUMFLEX AND TILDE */
                return new char[] { (char) 0x61, (char) 0xE3, (char) 0xE4 };
            }
            case '\u1EAD': {
                /* LATIN SMALL LETTER A WITH CIRCUMFLEX AND DOT BELOW */
                return new char[] { (char) 0x61, (char) 0xE3, (char) 0xF2 };
            }
            case '\u00E3': {
                /* LATIN SMALL LETTER A WITH TILDE */
                return new char[] { (char) 0x61, (char) 0xE4, (char) 0x00 };
            }
            case '\u0101': {
                /* LATIN SMALL LETTER A WITH MACRON */
                return new char[] { (char) 0x61, (char) 0xE5, (char) 0x00 };
            }
            case '\u0103': {
                /* LATIN SMALL LETTER A WITH BREVE */
                return new char[] { (char) 0x61, (char) 0xE6, (char) 0x00 };
            }
            case '\u1EB3': {
                /* LATIN SMALL LETTER A WITH BREVE AND HOOK ABOVE */
                return new char[] { (char) 0x61, (char) 0xE6, (char) 0xE0 };
            }
            case '\u1EB1': {
                /* LATIN SMALL LETTER A WITH BREVE AND GRAVE */
                return new char[] { (char) 0x61, (char) 0xE6, (char) 0xE1 };
            }
            case '\u1EAF': {
                /* LATIN SMALL LETTER A WITH BREVE AND ACUTE */
                return new char[] { (char) 0x61, (char) 0xE6, (char) 0xE2 };
            }
            case '\u1EB5': {
                /* LATIN SMALL LETTER A WITH BREVE AND TILDE */
                return new char[] { (char) 0x61, (char) 0xE6, (char) 0xE4 };
            }
            case '\u1EB7': {
                /* LATIN SMALL LETTER A WITH BREVE AND DOT BELOW */
                return new char[] { (char) 0x61, (char) 0xE6, (char) 0xF2 };
            }
            case '\u0227': {
                /* LATIN SMALL LETTER A WITH DOT ABOVE */
                return new char[] { (char) 0x61, (char) 0xE7, (char) 0x00 };
            }
            case '\u01E1': {
                /* LATIN SMALL LETTER A WITH DOT ABOVE AND MACRON */
                return new char[] { (char) 0x61, (char) 0xE7, (char) 0xE5 };
            }
            case '\u00E4': {
                /* LATIN SMALL LETTER A WITH DIAERESIS */
                return new char[] { (char) 0x61, (char) 0xE8, (char) 0x00 };
            }
            case '\u01DF': {
                /* LATIN SMALL LETTER A WITH DIAERESIS AND MACRON */
                return new char[] { (char) 0x61, (char) 0xE8, (char) 0xE5 };
            }
            case '\u01CE': {
                /* LATIN SMALL LETTER A WITH CARON */
                return new char[] { (char) 0x61, (char) 0xE9, (char) 0x00 };
            }
            case '\u00E5': {
                /* LATIN SMALL LETTER A WITH RING ABOVE */
                return new char[] { (char) 0x61, (char) 0xEA, (char) 0x00 };
            }
            case '\u01FB': {
                /* LATIN SMALL LETTER A WITH RING ABOVE AND ACUTE */
                return new char[] { (char) 0x61, (char) 0xEA, (char) 0xE2 };
            }
            case '\u0105': {
                /* LATIN SMALL LETTER A WITH OGONEK */
                return new char[] { (char) 0x61, (char) 0xF1, (char) 0x00 };
            }
            case '\u1EA1': {
                /* LATIN SMALL LETTER A WITH DOT BELOW */
                return new char[] { (char) 0x61, (char) 0xF2, (char) 0x00 };
            }
            case '\u1E01': {
                /* LATIN SMALL LETTER A WITH RING BELOW */
                return new char[] { (char) 0x61, (char) 0xF4, (char) 0x00 };
            }
            case '\u1E03': {
                /* LATIN SMALL LETTER B WITH DOT ABOVE */
                return new char[] { (char) 0x62, (char) 0xE7, (char) 0x00 };
            }
            case '\u1E05': {
                /* LATIN SMALL LETTER B WITH DOT BELOW */
                return new char[] { (char) 0x62, (char) 0xF2, (char) 0x00 };
            }
            case '\u0107': {
                /* LATIN SMALL LETTER C WITH ACUTE */
                return new char[] { (char) 0x63, (char) 0xE2, (char) 0x00 };
            }
            case '\u0109': {
                /* LATIN SMALL LETTER C WITH CIRCUMFLEX */
                return new char[] { (char) 0x63, (char) 0xE3, (char) 0x00 };
            }
            case '\u010B': {
                /* LATIN SMALL LETTER C WITH DOT ABOVE */
                return new char[] { (char) 0x63, (char) 0xE7, (char) 0x00 };
            }
            case '\u010D': {
                /* LATIN SMALL LETTER C WITH CARON */
                return new char[] { (char) 0x63, (char) 0xE9, (char) 0x00 };
            }
            case '\u00E7': {
                /* LATIN SMALL LETTER C WITH CEDILLA */
                return new char[] { (char) 0x63, (char) 0xF0, (char) 0x00 };
            }
            case '\u1E09': {
                /* LATIN SMALL LETTER C WITH CEDILLA AND ACUTE */
                return new char[] { (char) 0x63, (char) 0xF0, (char) 0xE2 };
            }
            case '\u1E0B': {
                /* LATIN SMALL LETTER D WITH DOT ABOVE */
                return new char[] { (char) 0x64, (char) 0xE7, (char) 0x00 };
            }
            case '\u010F': {
                /* LATIN SMALL LETTER D WITH CARON */
                return new char[] { (char) 0x64, (char) 0xE9, (char) 0x00 };
            }
            case '\u1E11': {
                /* LATIN SMALL LETTER D WITH CEDILLA */
                return new char[] { (char) 0x64, (char) 0xF0, (char) 0x00 };
            }
            case '\u1E0D': {
                /* LATIN SMALL LETTER D WITH DOT BELOW */
                return new char[] { (char) 0x64, (char) 0xF2, (char) 0x00 };
            }
            case '\u1EBB': {
                /* LATIN SMALL LETTER E WITH HOOK ABOVE */
                return new char[] { (char) 0x65, (char) 0xE0, (char) 0x00 };
            }
            case '\u00E8': {
                /* LATIN SMALL LETTER E WITH GRAVE */
                return new char[] { (char) 0x65, (char) 0xE1, (char) 0x00 };
            }
            case '\u00E9': {
                /* LATIN SMALL LETTER E WITH ACUTE */
                return new char[] { (char) 0x65, (char) 0xE2, (char) 0x00 };
            }
            case '\u00EA': {
                /* LATIN SMALL LETTER E WITH CIRCUMFLEX */
                return new char[] { (char) 0x65, (char) 0xE3, (char) 0x00 };
            }
            case '\u1EC3': {
                /* LATIN SMALL LETTER E WITH CIRCUMFLEX AND HOOK ABOVE */
                return new char[] { (char) 0x65, (char) 0xE3, (char) 0xE0 };
            }
            case '\u1EC1': {
                /* LATIN SMALL LETTER E WITH CIRCUMFLEX AND GRAVE */
                return new char[] { (char) 0x65, (char) 0xE3, (char) 0xE1 };
            }
            case '\u1EBF': {
                /* LATIN SMALL LETTER E WITH CIRCUMFLEX AND ACUTE */
                return new char[] { (char) 0x65, (char) 0xE3, (char) 0xE2 };
            }
            case '\u1EC5': {
                /* LATIN SMALL LETTER E WITH CIRCUMFLEX AND TILDE */
                return new char[] { (char) 0x65, (char) 0xE3, (char) 0xE4 };
            }
            case '\u1EC7': {
                /* LATIN SMALL LETTER E WITH CIRCUMFLEX AND DOT BELOW */
                return new char[] { (char) 0x65, (char) 0xE3, (char) 0xF2 };
            }
            case '\u1EBD': {
                /* LATIN SMALL LETTER E WITH TILDE */
                return new char[] { (char) 0x65, (char) 0xE4, (char) 0x00 };
            }
            case '\u0113': {
                /* LATIN SMALL LETTER E WITH MACRON */
                return new char[] { (char) 0x65, (char) 0xE5, (char) 0x00 };
            }
            case '\u1E15': {
                /* LATIN SMALL LETTER E WITH MACRON AND GRAVE */
                return new char[] { (char) 0x65, (char) 0xE5, (char) 0xE1 };
            }
            case '\u1E17': {
                /* LATIN SMALL LETTER E WITH MACRON AND ACUTE */
                return new char[] { (char) 0x65, (char) 0xE5, (char) 0xE2 };
            }
            case '\u0115': {
                /* LATIN SMALL LETTER E WITH BREVE */
                return new char[] { (char) 0x65, (char) 0xE6, (char) 0x00 };
            }
            case '\u0117': {
                /* LATIN SMALL LETTER E WITH DOT ABOVE */
                return new char[] { (char) 0x65, (char) 0xE7, (char) 0x00 };
            }
            case '\u00EB': {
                /* LATIN SMALL LETTER E WITH DIAERESIS */
                return new char[] { (char) 0x65, (char) 0xE8, (char) 0x00 };
            }
            case '\u011B': {
                /* LATIN SMALL LETTER E WITH CARON */
                return new char[] { (char) 0x65, (char) 0xE9, (char) 0x00 };
            }
            case '\u0229': {
                /* LATIN SMALL LETTER E WITH CEDILLA */
                return new char[] { (char) 0x65, (char) 0xF0, (char) 0x00 };
            }
            case '\u1E1D': {
                /* LATIN SMALL LETTER E WITH CEDILLA AND BREVE */
                return new char[] { (char) 0x65, (char) 0xF0, (char) 0xE6 };
            }
            case '\u0119': {
                /* LATIN SMALL LETTER E WITH OGONEK */
                return new char[] { (char) 0x65, (char) 0xF1, (char) 0x00 };
            }
            case '\u1EB9': {
                /* LATIN SMALL LETTER E WITH DOT BELOW */
                return new char[] { (char) 0x65, (char) 0xF2, (char) 0x00 };
            }
            case '\u1E1F': {
                /* LATIN SMALL LETTER F WITH DOT ABOVE */
                return new char[] { (char) 0x66, (char) 0xE7, (char) 0x00 };
            }
            case '\u01F5': {
                /* LATIN SMALL LETTER G WITH ACUTE */
                return new char[] { (char) 0x67, (char) 0xE2, (char) 0x00 };
            }
            case '\u011D': {
                /* LATIN SMALL LETTER G WITH CIRCUMFLEX */
                return new char[] { (char) 0x67, (char) 0xE3, (char) 0x00 };
            }
            case '\u1E21': {
                /* LATIN SMALL LETTER G WITH MACRON */
                return new char[] { (char) 0x67, (char) 0xE5, (char) 0x00 };
            }
            case '\u011F': {
                /* LATIN SMALL LETTER G WITH BREVE */
                return new char[] { (char) 0x67, (char) 0xE6, (char) 0x00 };
            }
            case '\u0121': {
                /* LATIN SMALL LETTER G WITH DOT ABOVE */
                return new char[] { (char) 0x67, (char) 0xE7, (char) 0x00 };
            }
            case '\u01E7': {
                /* LATIN SMALL LETTER G WITH CARON */
                return new char[] { (char) 0x67, (char) 0xE9, (char) 0x00 };
            }
            case '\u0123': {
                /* LATIN SMALL LETTER G WITH CEDILLA */
                return new char[] { (char) 0x67, (char) 0xF0, (char) 0x00 };
            }
            case '\u0125': {
                /* LATIN SMALL LETTER H WITH CIRCUMFLEX */
                return new char[] { (char) 0x68, (char) 0xE3, (char) 0x00 };
            }
            case '\u1E23': {
                /* LATIN SMALL LETTER H WITH DOT ABOVE */
                return new char[] { (char) 0x68, (char) 0xE7, (char) 0x00 };
            }
            case '\u1E27': {
                /* LATIN SMALL LETTER H WITH DIAERESIS */
                return new char[] { (char) 0x68, (char) 0xE8, (char) 0x00 };
            }
            case '\u021F': {
                /* LATIN SMALL LETTER H WITH CARON */
                return new char[] { (char) 0x68, (char) 0xE9, (char) 0x00 };
            }
            case '\u1E29': {
                /* LATIN SMALL LETTER H WITH CEDILLA */
                return new char[] { (char) 0x68, (char) 0xF0, (char) 0x00 };
            }
            case '\u1E25': {
                /* LATIN SMALL LETTER H WITH DOT BELOW */
                return new char[] { (char) 0x68, (char) 0xF2, (char) 0x00 };
            }
            case '\u1E2B': {
                /* LATIN SMALL LETTER H WITH BREVE BELOW */
                return new char[] { (char) 0x68, (char) 0xF9, (char) 0x00 };
            }
            case '\u1EC9': {
                /* LATIN SMALL LETTER I WITH HOOK ABOVE */
                return new char[] { (char) 0x69, (char) 0xE0, (char) 0x00 };
            }
            case '\u00EC': {
                /* LATIN SMALL LETTER I WITH GRAVE */
                return new char[] { (char) 0x69, (char) 0xE1, (char) 0x00 };
            }
            case '\u00ED': {
                /* LATIN SMALL LETTER I WITH ACUTE */
                return new char[] { (char) 0x69, (char) 0xE2, (char) 0x00 };
            }
            case '\u00EE': {
                /* LATIN SMALL LETTER I WITH CIRCUMFLEX */
                return new char[] { (char) 0x69, (char) 0xE3, (char) 0x00 };
            }
            case '\u0129': {
                /* LATIN SMALL LETTER I WITH TILDE */
                return new char[] { (char) 0x69, (char) 0xE4, (char) 0x00 };
            }
            case '\u012B': {
                /* LATIN SMALL LETTER I WITH MACRON */
                return new char[] { (char) 0x69, (char) 0xE5, (char) 0x00 };
            }
            case '\u012D': {
                /* LATIN SMALL LETTER I WITH BREVE */
                return new char[] { (char) 0x69, (char) 0xE6, (char) 0x00 };
            }
            case '\u00EF': {
                /* LATIN SMALL LETTER I WITH DIAERESIS */
                return new char[] { (char) 0x69, (char) 0xE8, (char) 0x00 };
            }
            case '\u1E2F': {
                /* LATIN SMALL LETTER I WITH DIAERESIS AND ACUTE */
                return new char[] { (char) 0x69, (char) 0xE8, (char) 0xE2 };
            }
            case '\u01D0': {
                /* LATIN SMALL LETTER I WITH CARON */
                return new char[] { (char) 0x69, (char) 0xE9, (char) 0x00 };
            }
            case '\u012F': {
                /* LATIN SMALL LETTER I WITH OGONEK */
                return new char[] { (char) 0x69, (char) 0xF1, (char) 0x00 };
            }
            case '\u1ECB': {
                /* LATIN SMALL LETTER I WITH DOT BELOW */
                return new char[] { (char) 0x69, (char) 0xF2, (char) 0x00 };
            }
            case '\u0135': {
                /* LATIN SMALL LETTER J WITH CIRCUMFLEX */
                return new char[] { (char) 0x6A, (char) 0xE3, (char) 0x00 };
            }
            case '\u01F0': {
                /* LATIN SMALL LETTER J WITH CARON */
                return new char[] { (char) 0x6A, (char) 0xE9, (char) 0x00 };
            }
            case '\u1E31': {
                /* LATIN SMALL LETTER K WITH ACUTE */
                return new char[] { (char) 0x6B, (char) 0xE2, (char) 0x00 };
            }
            case '\u01E9': {
                /* LATIN SMALL LETTER K WITH CARON */
                return new char[] { (char) 0x6B, (char) 0xE9, (char) 0x00 };
            }
            case '\u0137': {
                /* LATIN SMALL LETTER K WITH CEDILLA */
                return new char[] { (char) 0x6B, (char) 0xF0, (char) 0x00 };
            }
            case '\u1E33': {
                /* LATIN SMALL LETTER K WITH DOT BELOW */
                return new char[] { (char) 0x6B, (char) 0xF2, (char) 0x00 };
            }
            case '\u013A': {
                /* LATIN SMALL LETTER L WITH ACUTE */
                return new char[] { (char) 0x6C, (char) 0xE2, (char) 0x00 };
            }
            case '\u013E': {
                /* LATIN SMALL LETTER L WITH CARON */
                return new char[] { (char) 0x6C, (char) 0xE9, (char) 0x00 };
            }
            case '\u013C': {
                /* LATIN SMALL LETTER L WITH CEDILLA */
                return new char[] { (char) 0x6C, (char) 0xF0, (char) 0x00 };
            }
            case '\u1E37': {
                /* LATIN SMALL LETTER L WITH DOT BELOW */
                return new char[] { (char) 0x6C, (char) 0xF2, (char) 0x00 };
            }
            case '\u1E39': {
                /* LATIN SMALL LETTER L WITH DOT BELOW AND MACRON */
                return new char[] { (char) 0x6C, (char) 0xF2, (char) 0xE5 };
            }
            case '\u1E3F': {
                /* LATIN SMALL LETTER M WITH ACUTE */
                return new char[] { (char) 0x6D, (char) 0xE2, (char) 0x00 };
            }
            case '\u1E41': {
                /* LATIN SMALL LETTER M WITH DOT ABOVE */
                return new char[] { (char) 0x6D, (char) 0xE7, (char) 0x00 };
            }
            case '\u1E43': {
                /* LATIN SMALL LETTER M WITH DOT BELOW */
                return new char[] { (char) 0x6D, (char) 0xF2, (char) 0x00 };
            }
            case '\u01F9': {
                /* LATIN SMALL LETTER N WITH GRAVE */
                return new char[] { (char) 0x6E, (char) 0xE1, (char) 0x00 };
            }
            case '\u0144': {
                /* LATIN SMALL LETTER N WITH ACUTE */
                return new char[] { (char) 0x6E, (char) 0xE2, (char) 0x00 };
            }
            case '\u00F1': {
                /* LATIN SMALL LETTER N WITH TILDE */
                return new char[] { (char) 0x6E, (char) 0xE4, (char) 0x00 };
            }
            case '\u1E45': {
                /* LATIN SMALL LETTER N WITH DOT ABOVE */
                return new char[] { (char) 0x6E, (char) 0xE7, (char) 0x00 };
            }
            case '\u0148': {
                /* LATIN SMALL LETTER N WITH CARON */
                return new char[] { (char) 0x6E, (char) 0xE9, (char) 0x00 };
            }
            case '\u0146': {
                /* LATIN SMALL LETTER N WITH CEDILLA */
                return new char[] { (char) 0x6E, (char) 0xF0, (char) 0x00 };
            }
            case '\u1E47': {
                /* LATIN SMALL LETTER N WITH DOT BELOW */
                return new char[] { (char) 0x6E, (char) 0xF2, (char) 0x00 };
            }
            case '\u1ECF': {
                /* LATIN SMALL LETTER O WITH HOOK ABOVE */
                return new char[] { (char) 0x6F, (char) 0xE0, (char) 0x00 };
            }
            case '\u00F2': {
                /* LATIN SMALL LETTER O WITH GRAVE */
                return new char[] { (char) 0x6F, (char) 0xE1, (char) 0x00 };
            }
            case '\u00F3': {
                /* LATIN SMALL LETTER O WITH ACUTE */
                return new char[] { (char) 0x6F, (char) 0xE2, (char) 0x00 };
            }
            case '\u00F4': {
                /* LATIN SMALL LETTER O WITH CIRCUMFLEX */
                return new char[] { (char) 0x6F, (char) 0xE3, (char) 0x00 };
            }
            case '\u1ED5': {
                /* LATIN SMALL LETTER O WITH CIRCUMFLEX AND HOOK ABOVE */
                return new char[] { (char) 0x6F, (char) 0xE3, (char) 0xE0 };
            }
            case '\u1ED3': {
                /* LATIN SMALL LETTER O WITH CIRCUMFLEX AND GRAVE */
                return new char[] { (char) 0x6F, (char) 0xE3, (char) 0xE1 };
            }
            case '\u1ED1': {
                /* LATIN SMALL LETTER O WITH CIRCUMFLEX AND ACUTE */
                return new char[] { (char) 0x6F, (char) 0xE3, (char) 0xE2 };
            }
            case '\u1ED7': {
                /* LATIN SMALL LETTER O WITH CIRCUMFLEX AND TILDE */
                return new char[] { (char) 0x6F, (char) 0xE3, (char) 0xE4 };
            }
            case '\u1ED9': {
                /* LATIN SMALL LETTER O WITH CIRCUMFLEX AND DOT BELOW */
                return new char[] { (char) 0x6F, (char) 0xE3, (char) 0xF2 };
            }
            case '\u00F5': {
                /* LATIN SMALL LETTER O WITH TILDE */
                return new char[] { (char) 0x6F, (char) 0xE4, (char) 0x00 };
            }
            case '\u1E4D': {
                /* LATIN SMALL LETTER O WITH TILDE AND ACUTE */
                return new char[] { (char) 0x6F, (char) 0xE4, (char) 0xE2 };
            }
            case '\u022D': {
                /* LATIN SMALL LETTER O WITH TILDE AND MACRON */
                return new char[] { (char) 0x6F, (char) 0xE4, (char) 0xE5 };
            }
            case '\u1E4F': {
                /* LATIN SMALL LETTER O WITH TILDE AND DIAERESIS */
                return new char[] { (char) 0x6F, (char) 0xE4, (char) 0xE8 };
            }
            case '\u014D': {
                /* LATIN SMALL LETTER O WITH MACRON */
                return new char[] { (char) 0x6F, (char) 0xE5, (char) 0x00 };
            }
            case '\u1E51': {
                /* LATIN SMALL LETTER O WITH MACRON AND GRAVE */
                return new char[] { (char) 0x6F, (char) 0xE5, (char) 0xE1 };
            }
            case '\u1E53': {
                /* LATIN SMALL LETTER O WITH MACRON AND ACUTE */
                return new char[] { (char) 0x6F, (char) 0xE5, (char) 0xE2 };
            }
            case '\u014F': {
                /* LATIN SMALL LETTER O WITH BREVE */
                return new char[] { (char) 0x6F, (char) 0xE6, (char) 0x00 };
            }
            case '\u022F': {
                /* LATIN SMALL LETTER O WITH DOT ABOVE */
                return new char[] { (char) 0x6F, (char) 0xE7, (char) 0x00 };
            }
            case '\u0231': {
                /* LATIN SMALL LETTER O WITH DOT ABOVE AND MACRON */
                return new char[] { (char) 0x6F, (char) 0xE7, (char) 0xE5 };
            }
            case '\u00F6': {
                /* LATIN SMALL LETTER O WITH DIAERESIS */
                return new char[] { (char) 0x6F, (char) 0xE8, (char) 0x00 };
            }
            case '\u022B': {
                /* LATIN SMALL LETTER O WITH DIAERESIS AND MACRON */
                return new char[] { (char) 0x6F, (char) 0xE8, (char) 0xE5 };
            }
            case '\u01D2': {
                /* LATIN SMALL LETTER O WITH CARON */
                return new char[] { (char) 0x6F, (char) 0xE9, (char) 0x00 };
            }
            case '\u0151': {
                /* LATIN SMALL LETTER O WITH DOUBLE ACUTE */
                return new char[] { (char) 0x6F, (char) 0xEE, (char) 0x00 };
            }
            case '\u01EB': {
                /* LATIN SMALL LETTER O WITH OGONEK */
                return new char[] { (char) 0x6F, (char) 0xF1, (char) 0x00 };
            }
            case '\u01ED': {
                /* LATIN SMALL LETTER O WITH OGONEK AND MACRON */
                return new char[] { (char) 0x6F, (char) 0xF1, (char) 0xE5 };
            }
            case '\u1ECD': {
                /* LATIN SMALL LETTER O WITH DOT BELOW */
                return new char[] { (char) 0x6F, (char) 0xF2, (char) 0x00 };
            }
            case '\u1E55': {
                /* LATIN SMALL LETTER P WITH ACUTE */
                return new char[] { (char) 0x70, (char) 0xE2, (char) 0x00 };
            }
            case '\u1E57': {
                /* LATIN SMALL LETTER P WITH DOT ABOVE */
                return new char[] { (char) 0x70, (char) 0xE7, (char) 0x00 };
            }
            case '\u0155': {
                /* LATIN SMALL LETTER R WITH ACUTE */
                return new char[] { (char) 0x72, (char) 0xE2, (char) 0x00 };
            }
            case '\u1E59': {
                /* LATIN SMALL LETTER R WITH DOT ABOVE */
                return new char[] { (char) 0x72, (char) 0xE7, (char) 0x00 };
            }
            case '\u0159': {
                /* LATIN SMALL LETTER R WITH CARON */
                return new char[] { (char) 0x72, (char) 0xE9, (char) 0x00 };
            }
            case '\u0157': {
                /* LATIN SMALL LETTER R WITH CEDILLA */
                return new char[] { (char) 0x72, (char) 0xF0, (char) 0x00 };
            }
            case '\u1E5B': {
                /* LATIN SMALL LETTER R WITH DOT BELOW */
                return new char[] { (char) 0x72, (char) 0xF2, (char) 0x00 };
            }
            case '\u1E5D': {
                /* LATIN SMALL LETTER R WITH DOT BELOW AND MACRON */
                return new char[] { (char) 0x72, (char) 0xF2, (char) 0xE5 };
            }
            case '\u015B': {
                /* LATIN SMALL LETTER S WITH ACUTE */
                return new char[] { (char) 0x73, (char) 0xE2, (char) 0x00 };
            }
            case '\u1E65': {
                /* LATIN SMALL LETTER S WITH ACUTE AND DOT ABOVE */
                return new char[] { (char) 0x73, (char) 0xE2, (char) 0xE7 };
            }
            case '\u015D': {
                /* LATIN SMALL LETTER S WITH CIRCUMFLEX */
                return new char[] { (char) 0x73, (char) 0xE3, (char) 0x00 };
            }
            case '\u1E61': {
                /* LATIN SMALL LETTER S WITH DOT ABOVE */
                return new char[] { (char) 0x73, (char) 0xE7, (char) 0x00 };
            }
            case '\u0161': {
                /* LATIN SMALL LETTER S WITH CARON */
                return new char[] { (char) 0x73, (char) 0xE9, (char) 0x00 };
            }
            case '\u1E67': {
                /* LATIN SMALL LETTER S WITH CARON AND DOT ABOVE */
                return new char[] { (char) 0x73, (char) 0xE9, (char) 0xE7 };
            }
            case '\u015F': {
                /* LATIN SMALL LETTER S WITH CEDILLA */
                return new char[] { (char) 0x73, (char) 0xF0, (char) 0x00 };
            }
            case '\u1E63': {
                /* LATIN SMALL LETTER S WITH DOT BELOW */
                return new char[] { (char) 0x73, (char) 0xF2, (char) 0x00 };
            }
            case '\u1E69': {
                /* LATIN SMALL LETTER S WITH DOT BELOW AND DOT ABOVE */
                return new char[] { (char) 0x73, (char) 0xF2, (char) 0xE7 };
            }
            case '\u0219': {
                /* LATIN SMALL LETTER S WITH COMMA BELOW */
                return new char[] { (char) 0x73, (char) 0xF7, (char) 0x00 };
            }
            case '\u1E6B': {
                /* LATIN SMALL LETTER T WITH DOT ABOVE */
                return new char[] { (char) 0x74, (char) 0xE7, (char) 0x00 };
            }
            case '\u1E97': {
                /* LATIN SMALL LETTER T WITH DIAERESIS */
                return new char[] { (char) 0x74, (char) 0xE8, (char) 0x00 };
            }
            case '\u0165': {
                /* LATIN SMALL LETTER T WITH CARON */
                return new char[] { (char) 0x74, (char) 0xE9, (char) 0x00 };
            }
            case '\u0163': {
                /* LATIN SMALL LETTER T WITH CEDILLA */
                return new char[] { (char) 0x74, (char) 0xF0, (char) 0x00 };
            }
            case '\u1E6D': {
                /* LATIN SMALL LETTER T WITH DOT BELOW */
                return new char[] { (char) 0x74, (char) 0xF2, (char) 0x00 };
            }
            case '\u021B': {
                /* LATIN SMALL LETTER T WITH COMMA BELOW */
                return new char[] { (char) 0x74, (char) 0xF7, (char) 0x00 };
            }
            case '\u1EE7': {
                /* LATIN SMALL LETTER U WITH HOOK ABOVE */
                return new char[] { (char) 0x75, (char) 0xE0, (char) 0x00 };
            }
            case '\u00F9': {
                /* LATIN SMALL LETTER U WITH GRAVE */
                return new char[] { (char) 0x75, (char) 0xE1, (char) 0x00 };
            }
            case '\u00FA': {
                /* LATIN SMALL LETTER U WITH ACUTE */
                return new char[] { (char) 0x75, (char) 0xE2, (char) 0x00 };
            }
            case '\u00FB': {
                /* LATIN SMALL LETTER U WITH CIRCUMFLEX */
                return new char[] { (char) 0x75, (char) 0xE3, (char) 0x00 };
            }
            case '\u0169': {
                /* LATIN SMALL LETTER U WITH TILDE */
                return new char[] { (char) 0x75, (char) 0xE4, (char) 0x00 };
            }
            case '\u1E79': {
                /* LATIN SMALL LETTER U WITH TILDE AND ACUTE */
                return new char[] { (char) 0x75, (char) 0xE4, (char) 0xE2 };
            }
            case '\u016B': {
                /* LATIN SMALL LETTER U WITH MACRON */
                return new char[] { (char) 0x75, (char) 0xE5, (char) 0x00 };
            }
            case '\u1E7B': {
                /* LATIN SMALL LETTER U WITH MACRON AND DIAERESIS */
                return new char[] { (char) 0x75, (char) 0xE5, (char) 0xE8 };
            }
            case '\u016D': {
                /* LATIN SMALL LETTER U WITH BREVE */
                return new char[] { (char) 0x75, (char) 0xE6, (char) 0x00 };
            }
            case '\u00FC': {
                /* LATIN SMALL LETTER U WITH DIAERESIS */
                return new char[] { (char) 0x75, (char) 0xE8, (char) 0x00 };
            }
            case '\u01DC': {
                /* LATIN SMALL LETTER U WITH DIAERESIS AND GRAVE */
                return new char[] { (char) 0x75, (char) 0xE8, (char) 0xE1 };
            }
            case '\u01D8': {
                /* LATIN SMALL LETTER U WITH DIAERESIS AND ACUTE */
                return new char[] { (char) 0x75, (char) 0xE8, (char) 0xE2 };
            }
            case '\u01D6': {
                /* LATIN SMALL LETTER U WITH DIAERESIS AND MACRON */
                return new char[] { (char) 0x75, (char) 0xE8, (char) 0xE5 };
            }
            case '\u01DA': {
                /* LATIN SMALL LETTER U WITH DIAERESIS AND CARON */
                return new char[] { (char) 0x75, (char) 0xE8, (char) 0xE9 };
            }
            case '\u01D4': {
                /* LATIN SMALL LETTER U WITH CARON */
                return new char[] { (char) 0x75, (char) 0xE9, (char) 0x00 };
            }
            case '\u016F': {
                /* LATIN SMALL LETTER U WITH RING ABOVE */
                return new char[] { (char) 0x75, (char) 0xEA, (char) 0x00 };
            }
            case '\u0171': {
                /* LATIN SMALL LETTER U WITH DOUBLE ACUTE */
                return new char[] { (char) 0x75, (char) 0xEE, (char) 0x00 };
            }
            case '\u0173': {
                /* LATIN SMALL LETTER U WITH OGONEK */
                return new char[] { (char) 0x75, (char) 0xF1, (char) 0x00 };
            }
            case '\u1EE5': {
                /* LATIN SMALL LETTER U WITH DOT BELOW */
                return new char[] { (char) 0x75, (char) 0xF2, (char) 0x00 };
            }
            case '\u1E73': {
                /* LATIN SMALL LETTER U WITH DIAERESIS BELOW */
                return new char[] { (char) 0x75, (char) 0xF3, (char) 0x00 };
            }
            case '\u1E7D': {
                /* LATIN SMALL LETTER V WITH TILDE */
                return new char[] { (char) 0x76, (char) 0xE4, (char) 0x00 };
            }
            case '\u1E7F': {
                /* LATIN SMALL LETTER V WITH DOT BELOW */
                return new char[] { (char) 0x76, (char) 0xF2, (char) 0x00 };
            }
            case '\u1E81': {
                /* LATIN SMALL LETTER W WITH GRAVE */
                return new char[] { (char) 0x77, (char) 0xE1, (char) 0x00 };
            }
            case '\u1E83': {
                /* LATIN SMALL LETTER W WITH ACUTE */
                return new char[] { (char) 0x77, (char) 0xE2, (char) 0x00 };
            }
            case '\u0175': {
                /* LATIN SMALL LETTER W WITH CIRCUMFLEX */
                return new char[] { (char) 0x77, (char) 0xE3, (char) 0x00 };
            }
            case '\u1E87': {
                /* LATIN SMALL LETTER W WITH DOT ABOVE */
                return new char[] { (char) 0x77, (char) 0xE7, (char) 0x00 };
            }
            case '\u1E85': {
                /* LATIN SMALL LETTER W WITH DIAERESIS */
                return new char[] { (char) 0x77, (char) 0xE8, (char) 0x00 };
            }
            case '\u1E98': {
                /* LATIN SMALL LETTER W WITH RING ABOVE */
                return new char[] { (char) 0x77, (char) 0xEA, (char) 0x00 };
            }
            case '\u1E89': {
                /* LATIN SMALL LETTER W WITH DOT BELOW */
                return new char[] { (char) 0x77, (char) 0xF2, (char) 0x00 };
            }
            case '\u1E8B': {
                /* LATIN SMALL LETTER X WITH DOT ABOVE */
                return new char[] { (char) 0x78, (char) 0xE7, (char) 0x00 };
            }
            case '\u1E8D': {
                /* LATIN SMALL LETTER X WITH DIAERESIS */
                return new char[] { (char) 0x78, (char) 0xE8, (char) 0x00 };
            }
            case '\u1EF7': {
                /* LATIN SMALL LETTER Y WITH HOOK ABOVE */
                return new char[] { (char) 0x79, (char) 0xE0, (char) 0x00 };
            }
            case '\u1EF3': {
                /* LATIN SMALL LETTER Y WITH GRAVE */
                return new char[] { (char) 0x79, (char) 0xE1, (char) 0x00 };
            }
            case '\u00FD': {
                /* LATIN SMALL LETTER Y WITH ACUTE */
                return new char[] { (char) 0x79, (char) 0xE2, (char) 0x00 };
            }
            case '\u0177': {
                /* LATIN SMALL LETTER Y WITH CIRCUMFLEX */
                return new char[] { (char) 0x79, (char) 0xE3, (char) 0x00 };
            }
            case '\u1EF9': {
                /* LATIN SMALL LETTER Y WITH TILDE */
                return new char[] { (char) 0x79, (char) 0xE4, (char) 0x00 };
            }
            case '\u0233': {
                /* LATIN SMALL LETTER Y WITH MACRON */
                return new char[] { (char) 0x79, (char) 0xE5, (char) 0x00 };
            }
            case '\u1E8F': {
                /* LATIN SMALL LETTER Y WITH DOT ABOVE */
                return new char[] { (char) 0x79, (char) 0xE7, (char) 0x00 };
            }
            case '\u00FF': {
                /* LATIN SMALL LETTER Y WITH DIAERESIS */
                return new char[] { (char) 0x79, (char) 0xE8, (char) 0x00 };
            }
            case '\u1E99': {
                /* LATIN SMALL LETTER Y WITH RING ABOVE */
                return new char[] { (char) 0x79, (char) 0xEA, (char) 0x00 };
            }
            case '\u1EF5': {
                /* LATIN SMALL LETTER Y WITH DOT BELOW */
                return new char[] { (char) 0x79, (char) 0xF2, (char) 0x00 };
            }
            case '\u017A': {
                /* LATIN SMALL LETTER Z WITH ACUTE */
                return new char[] { (char) 0x7A, (char) 0xE2, (char) 0x00 };
            }
            case '\u1E91': {
                /* LATIN SMALL LETTER Z WITH CIRCUMFLEX */
                return new char[] { (char) 0x7A, (char) 0xE3, (char) 0x00 };
            }
            case '\u017C': {
                /* LATIN SMALL LETTER Z WITH DOT ABOVE */
                return new char[] { (char) 0x7A, (char) 0xE7, (char) 0x00 };
            }
            case '\u017E': {
                /* LATIN SMALL LETTER Z WITH CARON */
                return new char[] { (char) 0x7A, (char) 0xE9, (char) 0x00 };
            }
            case '\u1E93': {
                /* LATIN SMALL LETTER Z WITH DOT BELOW */
                return new char[] { (char) 0x7A, (char) 0xF2, (char) 0x00 };
            }

            default:
                return null;
        }
    }

    /**
     * Get a unicode character that represents the precombined base character plus up to two diacritic modifiers.
     * Results are already decoded from ANSEL and should not be decoded again.
     * 
     * @param baseChar
     *            the base character
     * @param modifier1
     *            diacritic 1
     * @param modifier2
     *            diacritic 2 - pass zero if there is no second diacritic
     * @return a single character that combines the base and the diacritic(s), or a zero if no such character exists
     */
    private char getCombinedGlyph(char baseChar, char modifier1, char modifier2) {

        if (baseChar == 'A') {
            if (modifier1 == '\u00E0' /* HOOK ABOVE */) {
                /* LATIN CAPITAL LETTER A WITH HOOK ABOVE */
                return '\u1EA2';
            }

            if (modifier1 == '\u00E1' /* GRAVE */) {
                /* LATIN CAPITAL LETTER A WITH GRAVE */
                return '\u00C0';
            }

            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN CAPITAL LETTER A WITH ACUTE */
                return '\u00C1';
            }

            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                if (modifier2 == '\u00E0' /* HOOK ABOVE */) {
                    /* LATIN CAPITAL LETTER A WITH CIRCUMFLEX AND HOOK ABOVE */
                    return '\u1EA8';
                }
                if (modifier2 == '\u00E1' /* GRAVE */) {
                    /* LATIN CAPITAL LETTER A WITH CIRCUMFLEX AND GRAVE */
                    return '\u1EA6';
                }
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN CAPITAL LETTER A WITH CIRCUMFLEX AND ACUTE */
                    return '\u1EA4';
                }
                if (modifier2 == '\u00E4' /* TILDE */) {
                    /* LATIN CAPITAL LETTER A WITH CIRCUMFLEX AND TILDE */
                    return '\u1EAA';
                }
                if (modifier2 == '\u00F2' /* DOT BELOW */) {
                    /* LATIN CAPITAL LETTER A WITH CIRCUMFLEX AND DOT BELOW */
                    return '\u1EAC';
                }
                /* LATIN CAPITAL LETTER A WITH CIRCUMFLEX */
                return '\u00C2';
            }

            if (modifier1 == '\u00E4' /* TILDE */) {
                /* LATIN CAPITAL LETTER A WITH TILDE */
                return '\u00C3';
            }

            if (modifier1 == '\u00E5' /* MACRON */) {
                /* LATIN CAPITAL LETTER A WITH MACRON */
                return '\u0100';
            }

            if (modifier1 == '\u00E6' /* BREVE */) {
                if (modifier2 == '\u00E0' /* HOOK ABOVE */) {
                    /* LATIN CAPITAL LETTER A WITH BREVE AND HOOK ABOVE */
                    return '\u1EB2';
                }
                if (modifier2 == '\u00E1' /* GRAVE */) {
                    /* LATIN CAPITAL LETTER A WITH BREVE AND GRAVE */
                    return '\u1EB0';
                }
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN CAPITAL LETTER A WITH BREVE AND ACUTE */
                    return '\u1EAE';
                }
                if (modifier2 == '\u00E4' /* TILDE */) {
                    /* LATIN CAPITAL LETTER A WITH BREVE AND TILDE */
                    return '\u1EB4';
                }
                if (modifier2 == '\u00F2' /* DOT BELOW */) {
                    /* LATIN CAPITAL LETTER A WITH BREVE AND DOT BELOW */
                    return '\u1EB6';
                }
                /* LATIN CAPITAL LETTER A WITH BREVE */
                return '\u0102';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                if (modifier2 == '\u00E5' /* MACRON */) {
                    /* LATIN CAPITAL LETTER A WITH DOT ABOVE AND MACRON */
                    return '\u01E0';
                }
                /* LATIN CAPITAL LETTER A WITH DOT ABOVE */
                return '\u0226';
            }

            if (modifier1 == '\u00E8' /* DIAERESIS */) {
                if (modifier2 == '\u00E5' /* MACRON */) {
                    /* LATIN CAPITAL LETTER A WITH DIAERESIS AND MACRON */
                    return '\u01DE';
                }
                /* LATIN CAPITAL LETTER A WITH DIAERESIS */
                return '\u00C4';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN CAPITAL LETTER A WITH CARON */
                return '\u01CD';
            }

            if (modifier1 == '\u00EA' /* RING ABOVE */) {
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN CAPITAL LETTER A WITH RING ABOVE AND ACUTE */
                    return '\u01FA';
                }
                /* LATIN CAPITAL LETTER A WITH RING ABOVE */
                return '\u00C5';
            }

            if (modifier1 == '\u00F1' /* OGONEK */) {
                /* LATIN CAPITAL LETTER A WITH OGONEK */
                return '\u0104';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN CAPITAL LETTER A WITH DOT BELOW */
                return '\u1EA0';
            }

            if (modifier1 == '\u00F4' /* RING BELOW */) {
                /* LATIN CAPITAL LETTER A WITH RING BELOW */
                return '\u1E00';
            }

        }
        if (baseChar == 'B') {
            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN CAPITAL LETTER B WITH DOT ABOVE */
                return '\u1E02';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN CAPITAL LETTER B WITH DOT BELOW */
                return '\u1E04';
            }

        }
        if (baseChar == 'C') {
            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN CAPITAL LETTER C WITH ACUTE */
                return '\u0106';
            }

            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                /* LATIN CAPITAL LETTER C WITH CIRCUMFLEX */
                return '\u0108';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN CAPITAL LETTER C WITH DOT ABOVE */
                return '\u010A';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN CAPITAL LETTER C WITH CARON */
                return '\u010C';
            }

            if (modifier1 == '\u00F0' /* CEDILLA */) {
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN CAPITAL LETTER C WITH CEDILLA AND ACUTE */
                    return '\u1E08';
                }
                /* LATIN CAPITAL LETTER C WITH CEDILLA */
                return '\u00C7';
            }

        }
        if (baseChar == 'D') {
            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN CAPITAL LETTER D WITH DOT ABOVE */
                return '\u1E0A';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN CAPITAL LETTER D WITH CARON */
                return '\u010E';
            }

            if (modifier1 == '\u00F0' /* CEDILLA */) {
                /* LATIN CAPITAL LETTER D WITH CEDILLA */
                return '\u1E10';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN CAPITAL LETTER D WITH DOT BELOW */
                return '\u1E0C';
            }

        }
        if (baseChar == 'E') {
            if (modifier1 == '\u00E0' /* HOOK ABOVE */) {
                /* LATIN CAPITAL LETTER E WITH HOOK ABOVE */
                return '\u1EBA';
            }

            if (modifier1 == '\u00E1' /* GRAVE */) {
                /* LATIN CAPITAL LETTER E WITH GRAVE */
                return '\u00C8';
            }

            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN CAPITAL LETTER E WITH ACUTE */
                return '\u00C9';
            }

            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                if (modifier2 == '\u00E0' /* HOOK ABOVE */) {
                    /* LATIN CAPITAL LETTER E WITH CIRCUMFLEX AND HOOK ABOVE */
                    return '\u1EC2';
                }
                if (modifier2 == '\u00E1' /* GRAVE */) {
                    /* LATIN CAPITAL LETTER E WITH CIRCUMFLEX AND GRAVE */
                    return '\u1EC0';
                }
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN CAPITAL LETTER E WITH CIRCUMFLEX AND ACUTE */
                    return '\u1EBE';
                }
                if (modifier2 == '\u00E4' /* TILDE */) {
                    /* LATIN CAPITAL LETTER E WITH CIRCUMFLEX AND TILDE */
                    return '\u1EC4';
                }
                if (modifier2 == '\u00F2' /* DOT BELOW */) {
                    /* LATIN CAPITAL LETTER E WITH CIRCUMFLEX AND DOT BELOW */
                    return '\u1EC6';
                }
                /* LATIN CAPITAL LETTER E WITH CIRCUMFLEX */
                return '\u00CA';
            }

            if (modifier1 == '\u00E4' /* TILDE */) {
                /* LATIN CAPITAL LETTER E WITH TILDE */
                return '\u1EBC';
            }

            if (modifier1 == '\u00E5' /* MACRON */) {
                if (modifier2 == '\u00E1' /* GRAVE */) {
                    /* LATIN CAPITAL LETTER E WITH MACRON AND GRAVE */
                    return '\u1E14';
                }
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN CAPITAL LETTER E WITH MACRON AND ACUTE */
                    return '\u1E16';
                }
                /* LATIN CAPITAL LETTER E WITH MACRON */
                return '\u0112';
            }

            if (modifier1 == '\u00E6' /* BREVE */) {
                /* LATIN CAPITAL LETTER E WITH BREVE */
                return '\u0114';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN CAPITAL LETTER E WITH DOT ABOVE */
                return '\u0116';
            }

            if (modifier1 == '\u00E8' /* DIAERESIS */) {
                /* LATIN CAPITAL LETTER E WITH DIAERESIS */
                return '\u00CB';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN CAPITAL LETTER E WITH CARON */
                return '\u011A';
            }

            if (modifier1 == '\u00F0' /* CEDILLA */) {
                if (modifier2 == '\u00E6' /* BREVE */) {
                    /* LATIN CAPITAL LETTER E WITH CEDILLA AND BREVE */
                    return '\u1E1C';
                }
                /* LATIN CAPITAL LETTER E WITH CEDILLA */
                return '\u0228';
            }

            if (modifier1 == '\u00F1' /* OGONEK */) {
                /* LATIN CAPITAL LETTER E WITH OGONEK */
                return '\u0118';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN CAPITAL LETTER E WITH DOT BELOW */
                return '\u1EB8';
            }

        }
        if (baseChar == 'F') {
            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN CAPITAL LETTER F WITH DOT ABOVE */
                return '\u1E1E';
            }

        }
        if (baseChar == 'G') {
            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN CAPITAL LETTER G WITH ACUTE */
                return '\u01F4';
            }

            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                /* LATIN CAPITAL LETTER G WITH CIRCUMFLEX */
                return '\u011C';
            }

            if (modifier1 == '\u00E5' /* MACRON */) {
                /* LATIN CAPITAL LETTER G WITH MACRON */
                return '\u1E20';
            }

            if (modifier1 == '\u00E6' /* BREVE */) {
                /* LATIN CAPITAL LETTER G WITH BREVE */
                return '\u011E';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN CAPITAL LETTER G WITH DOT ABOVE */
                return '\u0120';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN CAPITAL LETTER G WITH CARON */
                return '\u01E6';
            }

            if (modifier1 == '\u00F0' /* CEDILLA */) {
                /* LATIN CAPITAL LETTER G WITH CEDILLA */
                return '\u0122';
            }

        }
        if (baseChar == 'H') {
            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                /* LATIN CAPITAL LETTER H WITH CIRCUMFLEX */
                return '\u0124';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN CAPITAL LETTER H WITH DOT ABOVE */
                return '\u1E22';
            }

            if (modifier1 == '\u00E8' /* DIAERESIS */) {
                /* LATIN CAPITAL LETTER H WITH DIAERESIS */
                return '\u1E26';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN CAPITAL LETTER H WITH CARON */
                return '\u021E';
            }

            if (modifier1 == '\u00F0' /* CEDILLA */) {
                /* LATIN CAPITAL LETTER H WITH CEDILLA */
                return '\u1E28';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN CAPITAL LETTER H WITH DOT BELOW */
                return '\u1E24';
            }

            if (modifier1 == '\u00F9' /* BREVE BELOW */) {
                /* LATIN CAPITAL LETTER H WITH BREVE BELOW */
                return '\u1E2A';
            }

        }
        if (baseChar == 'I') {
            if (modifier1 == '\u00E0' /* HOOK ABOVE */) {
                /* LATIN CAPITAL LETTER I WITH HOOK ABOVE */
                return '\u1EC8';
            }

            if (modifier1 == '\u00E1' /* GRAVE */) {
                /* LATIN CAPITAL LETTER I WITH GRAVE */
                return '\u00CC';
            }

            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN CAPITAL LETTER I WITH ACUTE */
                return '\u00CD';
            }

            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                /* LATIN CAPITAL LETTER I WITH CIRCUMFLEX */
                return '\u00CE';
            }

            if (modifier1 == '\u00E4' /* TILDE */) {
                /* LATIN CAPITAL LETTER I WITH TILDE */
                return '\u0128';
            }

            if (modifier1 == '\u00E5' /* MACRON */) {
                /* LATIN CAPITAL LETTER I WITH MACRON */
                return '\u012A';
            }

            if (modifier1 == '\u00E6' /* BREVE */) {
                /* LATIN CAPITAL LETTER I WITH BREVE */
                return '\u012C';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN CAPITAL LETTER I WITH DOT ABOVE */
                return '\u0130';
            }

            if (modifier1 == '\u00E8' /* DIAERESIS */) {
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN CAPITAL LETTER I WITH DIAERESIS AND ACUTE */
                    return '\u1E2E';
                }
                /* LATIN CAPITAL LETTER I WITH DIAERESIS */
                return '\u00CF';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN CAPITAL LETTER I WITH CARON */
                return '\u01CF';
            }

            if (modifier1 == '\u00F1' /* OGONEK */) {
                /* LATIN CAPITAL LETTER I WITH OGONEK */
                return '\u012E';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN CAPITAL LETTER I WITH DOT BELOW */
                return '\u1ECA';
            }

        }
        if (baseChar == 'J') {
            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                /* LATIN CAPITAL LETTER J WITH CIRCUMFLEX */
                return '\u0134';
            }

        }
        if (baseChar == 'K') {
            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN CAPITAL LETTER K WITH ACUTE */
                return '\u1E30';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN CAPITAL LETTER K WITH CARON */
                return '\u01E8';
            }

            if (modifier1 == '\u00F0' /* CEDILLA */) {
                /* LATIN CAPITAL LETTER K WITH CEDILLA */
                return '\u0136';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN CAPITAL LETTER K WITH DOT BELOW */
                return '\u1E32';
            }

        }
        if (baseChar == 'L') {
            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN CAPITAL LETTER L WITH ACUTE */
                return '\u0139';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN CAPITAL LETTER L WITH CARON */
                return '\u013D';
            }

            if (modifier1 == '\u00F0' /* CEDILLA */) {
                /* LATIN CAPITAL LETTER L WITH CEDILLA */
                return '\u013B';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                if (modifier2 == '\u00E5' /* MACRON */) {
                    /* LATIN CAPITAL LETTER L WITH DOT BELOW AND MACRON */
                    return '\u1E38';
                }
                /* LATIN CAPITAL LETTER L WITH DOT BELOW */
                return '\u1E36';
            }

        }
        if (baseChar == 'M') {
            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN CAPITAL LETTER M WITH ACUTE */
                return '\u1E3E';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN CAPITAL LETTER M WITH DOT ABOVE */
                return '\u1E40';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN CAPITAL LETTER M WITH DOT BELOW */
                return '\u1E42';
            }

        }
        if (baseChar == 'N') {
            if (modifier1 == '\u00E1' /* GRAVE */) {
                /* LATIN CAPITAL LETTER N WITH GRAVE */
                return '\u01F8';
            }

            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN CAPITAL LETTER N WITH ACUTE */
                return '\u0143';
            }

            if (modifier1 == '\u00E4' /* TILDE */) {
                /* LATIN CAPITAL LETTER N WITH TILDE */
                return '\u00D1';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN CAPITAL LETTER N WITH DOT ABOVE */
                return '\u1E44';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN CAPITAL LETTER N WITH CARON */
                return '\u0147';
            }

            if (modifier1 == '\u00F0' /* CEDILLA */) {
                /* LATIN CAPITAL LETTER N WITH CEDILLA */
                return '\u0145';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN CAPITAL LETTER N WITH DOT BELOW */
                return '\u1E46';
            }

        }
        if (baseChar == 'O') {
            if (modifier1 == '\u00E0' /* HOOK ABOVE */) {
                /* LATIN CAPITAL LETTER O WITH HOOK ABOVE */
                return '\u1ECE';
            }

            if (modifier1 == '\u00E1' /* GRAVE */) {
                /* LATIN CAPITAL LETTER O WITH GRAVE */
                return '\u00D2';
            }

            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN CAPITAL LETTER O WITH ACUTE */
                return '\u00D3';
            }

            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                if (modifier2 == '\u00E0' /* HOOK ABOVE */) {
                    /* LATIN CAPITAL LETTER O WITH CIRCUMFLEX AND HOOK ABOVE */
                    return '\u1ED4';
                }
                if (modifier2 == '\u00E1' /* GRAVE */) {
                    /* LATIN CAPITAL LETTER O WITH CIRCUMFLEX AND GRAVE */
                    return '\u1ED2';
                }
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN CAPITAL LETTER O WITH CIRCUMFLEX AND ACUTE */
                    return '\u1ED0';
                }
                if (modifier2 == '\u00E4' /* TILDE */) {
                    /* LATIN CAPITAL LETTER O WITH CIRCUMFLEX AND TILDE */
                    return '\u1ED6';
                }
                if (modifier2 == '\u00F2' /* DOT BELOW */) {
                    /* LATIN CAPITAL LETTER O WITH CIRCUMFLEX AND DOT BELOW */
                    return '\u1ED8';
                }
                /* LATIN CAPITAL LETTER O WITH CIRCUMFLEX */
                return '\u00D4';
            }

            if (modifier1 == '\u00E4' /* TILDE */) {
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN CAPITAL LETTER O WITH TILDE AND ACUTE */
                    return '\u1E4C';
                }
                if (modifier2 == '\u00E5' /* MACRON */) {
                    /* LATIN CAPITAL LETTER O WITH TILDE AND MACRON */
                    return '\u022C';
                }
                if (modifier2 == '\u00E8' /* DIAERESIS */) {
                    /* LATIN CAPITAL LETTER O WITH TILDE AND DIAERESIS */
                    return '\u1E4E';
                }
                /* LATIN CAPITAL LETTER O WITH TILDE */
                return '\u00D5';
            }

            if (modifier1 == '\u00E5' /* MACRON */) {
                if (modifier2 == '\u00E1' /* GRAVE */) {
                    /* LATIN CAPITAL LETTER O WITH MACRON AND GRAVE */
                    return '\u1E50';
                }
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN CAPITAL LETTER O WITH MACRON AND ACUTE */
                    return '\u1E52';
                }
                /* LATIN CAPITAL LETTER O WITH MACRON */
                return '\u014C';
            }

            if (modifier1 == '\u00E6' /* BREVE */) {
                /* LATIN CAPITAL LETTER O WITH BREVE */
                return '\u014E';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                if (modifier2 == '\u00E5' /* MACRON */) {
                    /* LATIN CAPITAL LETTER O WITH DOT ABOVE AND MACRON */
                    return '\u0230';
                }
                /* LATIN CAPITAL LETTER O WITH DOT ABOVE */
                return '\u022E';
            }

            if (modifier1 == '\u00E8' /* DIAERESIS */) {
                if (modifier2 == '\u00E5' /* MACRON */) {
                    /* LATIN CAPITAL LETTER O WITH DIAERESIS AND MACRON */
                    return '\u022A';
                }
                /* LATIN CAPITAL LETTER O WITH DIAERESIS */
                return '\u00D6';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN CAPITAL LETTER O WITH CARON */
                return '\u01D1';
            }

            if (modifier1 == '\u00EE' /* DOUBLE ACUTE */) {
                /* LATIN CAPITAL LETTER O WITH DOUBLE ACUTE */
                return '\u0150';
            }

            if (modifier1 == '\u00F1' /* OGONEK */) {
                if (modifier2 == '\u00E5' /* MACRON */) {
                    /* LATIN CAPITAL LETTER O WITH OGONEK AND MACRON */
                    return '\u01EC';
                }
                /* LATIN CAPITAL LETTER O WITH OGONEK */
                return '\u01EA';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN CAPITAL LETTER O WITH DOT BELOW */
                return '\u1ECC';
            }

        }
        if (baseChar == 'P') {
            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN CAPITAL LETTER P WITH ACUTE */
                return '\u1E54';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN CAPITAL LETTER P WITH DOT ABOVE */
                return '\u1E56';
            }

        }
        if (baseChar == 'Q') {
        }
        if (baseChar == 'R') {
            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN CAPITAL LETTER R WITH ACUTE */
                return '\u0154';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN CAPITAL LETTER R WITH DOT ABOVE */
                return '\u1E58';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN CAPITAL LETTER R WITH CARON */
                return '\u0158';
            }

            if (modifier1 == '\u00F0' /* CEDILLA */) {
                /* LATIN CAPITAL LETTER R WITH CEDILLA */
                return '\u0156';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                if (modifier2 == '\u00E5' /* MACRON */) {
                    /* LATIN CAPITAL LETTER R WITH DOT BELOW AND MACRON */
                    return '\u1E5C';
                }
                /* LATIN CAPITAL LETTER R WITH DOT BELOW */
                return '\u1E5A';
            }

        }
        if (baseChar == 'S') {
            if (modifier1 == '\u00E2' /* ACUTE */) {
                if (modifier2 == '\u00E7' /* DOT ABOVE */) {
                    /* LATIN CAPITAL LETTER S WITH ACUTE AND DOT ABOVE */
                    return '\u1E64';
                }
                /* LATIN CAPITAL LETTER S WITH ACUTE */
                return '\u015A';
            }

            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                /* LATIN CAPITAL LETTER S WITH CIRCUMFLEX */
                return '\u015C';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN CAPITAL LETTER S WITH DOT ABOVE */
                return '\u1E60';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                if (modifier2 == '\u00E7' /* DOT ABOVE */) {
                    /* LATIN CAPITAL LETTER S WITH CARON AND DOT ABOVE */
                    return '\u1E66';
                }
                /* LATIN CAPITAL LETTER S WITH CARON */
                return '\u0160';
            }

            if (modifier1 == '\u00F0' /* CEDILLA */) {
                /* LATIN CAPITAL LETTER S WITH CEDILLA */
                return '\u015E';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                if (modifier2 == '\u00E7' /* DOT ABOVE */) {
                    /* LATIN CAPITAL LETTER S WITH DOT BELOW AND DOT ABOVE */
                    return '\u1E68';
                }
                /* LATIN CAPITAL LETTER S WITH DOT BELOW */
                return '\u1E62';
            }

            if (modifier1 == '\u00F7' /* COMMA BELOW */) {
                /* LATIN CAPITAL LETTER S WITH COMMA BELOW */
                return '\u0218';
            }

        }
        if (baseChar == 'T') {
            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN CAPITAL LETTER T WITH DOT ABOVE */
                return '\u1E6A';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN CAPITAL LETTER T WITH CARON */
                return '\u0164';
            }

            if (modifier1 == '\u00F0' /* CEDILLA */) {
                /* LATIN CAPITAL LETTER T WITH CEDILLA */
                return '\u0162';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN CAPITAL LETTER T WITH DOT BELOW */
                return '\u1E6C';
            }

            if (modifier1 == '\u00F7' /* COMMA BELOW */) {
                /* LATIN CAPITAL LETTER T WITH COMMA BELOW */
                return '\u021A';
            }

        }
        if (baseChar == 'U') {
            if (modifier1 == '\u00E0' /* HOOK ABOVE */) {
                /* LATIN CAPITAL LETTER U WITH HOOK ABOVE */
                return '\u1EE6';
            }

            if (modifier1 == '\u00E1' /* GRAVE */) {
                /* LATIN CAPITAL LETTER U WITH GRAVE */
                return '\u00D9';
            }

            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN CAPITAL LETTER U WITH ACUTE */
                return '\u00DA';
            }

            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                /* LATIN CAPITAL LETTER U WITH CIRCUMFLEX */
                return '\u00DB';
            }

            if (modifier1 == '\u00E4' /* TILDE */) {
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN CAPITAL LETTER U WITH TILDE AND ACUTE */
                    return '\u1E78';
                }
                /* LATIN CAPITAL LETTER U WITH TILDE */
                return '\u0168';
            }

            if (modifier1 == '\u00E5' /* MACRON */) {
                if (modifier2 == '\u00E8' /* DIAERESIS */) {
                    /* LATIN CAPITAL LETTER U WITH MACRON AND DIAERESIS */
                    return '\u1E7A';
                }
                /* LATIN CAPITAL LETTER U WITH MACRON */
                return '\u016A';
            }

            if (modifier1 == '\u00E6' /* BREVE */) {
                /* LATIN CAPITAL LETTER U WITH BREVE */
                return '\u016C';
            }

            if (modifier1 == '\u00E8' /* DIAERESIS */) {
                if (modifier2 == '\u00E1' /* GRAVE */) {
                    /* LATIN CAPITAL LETTER U WITH DIAERESIS AND GRAVE */
                    return '\u01DB';
                }
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN CAPITAL LETTER U WITH DIAERESIS AND ACUTE */
                    return '\u01D7';
                }
                if (modifier2 == '\u00E5' /* MACRON */) {
                    /* LATIN CAPITAL LETTER U WITH DIAERESIS AND MACRON */
                    return '\u01D5';
                }
                if (modifier2 == '\u00E9' /* CARON */) {
                    /* LATIN CAPITAL LETTER U WITH DIAERESIS AND CARON */
                    return '\u01D9';
                }
                /* LATIN CAPITAL LETTER U WITH DIAERESIS */
                return '\u00DC';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN CAPITAL LETTER U WITH CARON */
                return '\u01D3';
            }

            if (modifier1 == '\u00EA' /* RING ABOVE */) {
                /* LATIN CAPITAL LETTER U WITH RING ABOVE */
                return '\u016E';
            }

            if (modifier1 == '\u00EE' /* DOUBLE ACUTE */) {
                /* LATIN CAPITAL LETTER U WITH DOUBLE ACUTE */
                return '\u0170';
            }

            if (modifier1 == '\u00F1' /* OGONEK */) {
                /* LATIN CAPITAL LETTER U WITH OGONEK */
                return '\u0172';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN CAPITAL LETTER U WITH DOT BELOW */
                return '\u1EE4';
            }

            if (modifier1 == '\u00F3' /* DIAERESIS BELOW */) {
                /* LATIN CAPITAL LETTER U WITH DIAERESIS BELOW */
                return '\u1E72';
            }

        }
        if (baseChar == 'V') {
            if (modifier1 == '\u00E4' /* TILDE */) {
                /* LATIN CAPITAL LETTER V WITH TILDE */
                return '\u1E7C';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN CAPITAL LETTER V WITH DOT BELOW */
                return '\u1E7E';
            }

        }
        if (baseChar == 'W') {
            if (modifier1 == '\u00E1' /* GRAVE */) {
                /* LATIN CAPITAL LETTER W WITH GRAVE */
                return '\u1E80';
            }

            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN CAPITAL LETTER W WITH ACUTE */
                return '\u1E82';
            }

            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                /* LATIN CAPITAL LETTER W WITH CIRCUMFLEX */
                return '\u0174';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN CAPITAL LETTER W WITH DOT ABOVE */
                return '\u1E86';
            }

            if (modifier1 == '\u00E8' /* DIAERESIS */) {
                /* LATIN CAPITAL LETTER W WITH DIAERESIS */
                return '\u1E84';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN CAPITAL LETTER W WITH DOT BELOW */
                return '\u1E88';
            }

        }
        if (baseChar == 'X') {
            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN CAPITAL LETTER X WITH DOT ABOVE */
                return '\u1E8A';
            }

            if (modifier1 == '\u00E8' /* DIAERESIS */) {
                /* LATIN CAPITAL LETTER X WITH DIAERESIS */
                return '\u1E8C';
            }

        }
        if (baseChar == 'Y') {
            if (modifier1 == '\u00E0' /* HOOK ABOVE */) {
                /* LATIN CAPITAL LETTER Y WITH HOOK ABOVE */
                return '\u1EF6';
            }

            if (modifier1 == '\u00E1' /* GRAVE */) {
                /* LATIN CAPITAL LETTER Y WITH GRAVE */
                return '\u1EF2';
            }

            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN CAPITAL LETTER Y WITH ACUTE */
                return '\u00DD';
            }

            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                /* LATIN CAPITAL LETTER Y WITH CIRCUMFLEX */
                return '\u0176';
            }

            if (modifier1 == '\u00E4' /* TILDE */) {
                /* LATIN CAPITAL LETTER Y WITH TILDE */
                return '\u1EF8';
            }

            if (modifier1 == '\u00E5' /* MACRON */) {
                /* LATIN CAPITAL LETTER Y WITH MACRON */
                return '\u0232';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN CAPITAL LETTER Y WITH DOT ABOVE */
                return '\u1E8E';
            }

            if (modifier1 == '\u00E8' /* DIAERESIS */) {
                /* LATIN CAPITAL LETTER Y WITH DIAERESIS */
                return '\u0178';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN CAPITAL LETTER Y WITH DOT BELOW */
                return '\u1EF4';
            }

        }
        if (baseChar == 'Z') {
            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN CAPITAL LETTER Z WITH ACUTE */
                return '\u0179';
            }

            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                /* LATIN CAPITAL LETTER Z WITH CIRCUMFLEX */
                return '\u1E90';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN CAPITAL LETTER Z WITH DOT ABOVE */
                return '\u017B';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN CAPITAL LETTER Z WITH CARON */
                return '\u017D';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN CAPITAL LETTER Z WITH DOT BELOW */
                return '\u1E92';
            }

        }
        if (baseChar == 'a') {
            if (modifier1 == '\u00E0' /* HOOK ABOVE */) {
                /* LATIN SMALL LETTER A WITH HOOK ABOVE */
                return '\u1EA3';
            }

            if (modifier1 == '\u00E1' /* GRAVE */) {
                /* LATIN SMALL LETTER A WITH GRAVE */
                return '\u00E0';
            }

            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN SMALL LETTER A WITH ACUTE */
                return '\u00E1';
            }

            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                if (modifier2 == '\u00E0' /* HOOK ABOVE */) {
                    /* LATIN SMALL LETTER A WITH CIRCUMFLEX AND HOOK ABOVE */
                    return '\u1EA9';
                }
                if (modifier2 == '\u00E1' /* GRAVE */) {
                    /* LATIN SMALL LETTER A WITH CIRCUMFLEX AND GRAVE */
                    return '\u1EA7';
                }
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN SMALL LETTER A WITH CIRCUMFLEX AND ACUTE */
                    return '\u1EA5';
                }
                if (modifier2 == '\u00E4' /* TILDE */) {
                    /* LATIN SMALL LETTER A WITH CIRCUMFLEX AND TILDE */
                    return '\u1EAB';
                }
                if (modifier2 == '\u00F2' /* DOT BELOW */) {
                    /* LATIN SMALL LETTER A WITH CIRCUMFLEX AND DOT BELOW */
                    return '\u1EAD';
                }
                /* LATIN SMALL LETTER A WITH CIRCUMFLEX */
                return '\u00E2';
            }

            if (modifier1 == '\u00E4' /* TILDE */) {
                /* LATIN SMALL LETTER A WITH TILDE */
                return '\u00E3';
            }

            if (modifier1 == '\u00E5' /* MACRON */) {
                /* LATIN SMALL LETTER A WITH MACRON */
                return '\u0101';
            }

            if (modifier1 == '\u00E6' /* BREVE */) {
                if (modifier2 == '\u00E0' /* HOOK ABOVE */) {
                    /* LATIN SMALL LETTER A WITH BREVE AND HOOK ABOVE */
                    return '\u1EB3';
                }
                if (modifier2 == '\u00E1' /* GRAVE */) {
                    /* LATIN SMALL LETTER A WITH BREVE AND GRAVE */
                    return '\u1EB1';
                }
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN SMALL LETTER A WITH BREVE AND ACUTE */
                    return '\u1EAF';
                }
                if (modifier2 == '\u00E4' /* TILDE */) {
                    /* LATIN SMALL LETTER A WITH BREVE AND TILDE */
                    return '\u1EB5';
                }
                if (modifier2 == '\u00F2' /* DOT BELOW */) {
                    /* LATIN SMALL LETTER A WITH BREVE AND DOT BELOW */
                    return '\u1EB7';
                }
                /* LATIN SMALL LETTER A WITH BREVE */
                return '\u0103';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                if (modifier2 == '\u00E5' /* MACRON */) {
                    /* LATIN SMALL LETTER A WITH DOT ABOVE AND MACRON */
                    return '\u01E1';
                }
                /* LATIN SMALL LETTER A WITH DOT ABOVE */
                return '\u0227';
            }

            if (modifier1 == '\u00E8' /* DIAERESIS */) {
                if (modifier2 == '\u00E5' /* MACRON */) {
                    /* LATIN SMALL LETTER A WITH DIAERESIS AND MACRON */
                    return '\u01DF';
                }
                /* LATIN SMALL LETTER A WITH DIAERESIS */
                return '\u00E4';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN SMALL LETTER A WITH CARON */
                return '\u01CE';
            }

            if (modifier1 == '\u00EA' /* RING ABOVE */) {
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN SMALL LETTER A WITH RING ABOVE AND ACUTE */
                    return '\u01FB';
                }
                /* LATIN SMALL LETTER A WITH RING ABOVE */
                return '\u00E5';
            }

            if (modifier1 == '\u00F1' /* OGONEK */) {
                /* LATIN SMALL LETTER A WITH OGONEK */
                return '\u0105';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN SMALL LETTER A WITH DOT BELOW */
                return '\u1EA1';
            }

            if (modifier1 == '\u00F4' /* RING BELOW */) {
                /* LATIN SMALL LETTER A WITH RING BELOW */
                return '\u1E01';
            }

        }
        if (baseChar == 'b') {
            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN SMALL LETTER B WITH DOT ABOVE */
                return '\u1E03';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN SMALL LETTER B WITH DOT BELOW */
                return '\u1E05';
            }

        }
        if (baseChar == 'c') {
            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN SMALL LETTER C WITH ACUTE */
                return '\u0107';
            }

            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                /* LATIN SMALL LETTER C WITH CIRCUMFLEX */
                return '\u0109';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN SMALL LETTER C WITH DOT ABOVE */
                return '\u010B';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN SMALL LETTER C WITH CARON */
                return '\u010D';
            }

            if (modifier1 == '\u00F0' /* CEDILLA */) {
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN SMALL LETTER C WITH CEDILLA AND ACUTE */
                    return '\u1E09';
                }
                /* LATIN SMALL LETTER C WITH CEDILLA */
                return '\u00E7';
            }

        }
        if (baseChar == 'd') {
            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN SMALL LETTER D WITH DOT ABOVE */
                return '\u1E0B';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN SMALL LETTER D WITH CARON */
                return '\u010F';
            }

            if (modifier1 == '\u00F0' /* CEDILLA */) {
                /* LATIN SMALL LETTER D WITH CEDILLA */
                return '\u1E11';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN SMALL LETTER D WITH DOT BELOW */
                return '\u1E0D';
            }

        }
        if (baseChar == 'e') {
            if (modifier1 == '\u00E0' /* HOOK ABOVE */) {
                /* LATIN SMALL LETTER E WITH HOOK ABOVE */
                return '\u1EBB';
            }

            if (modifier1 == '\u00E1' /* GRAVE */) {
                /* LATIN SMALL LETTER E WITH GRAVE */
                return '\u00E8';
            }

            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN SMALL LETTER E WITH ACUTE */
                return '\u00E9';
            }

            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                if (modifier2 == '\u00E0' /* HOOK ABOVE */) {
                    /* LATIN SMALL LETTER E WITH CIRCUMFLEX AND HOOK ABOVE */
                    return '\u1EC3';
                }
                if (modifier2 == '\u00E1' /* GRAVE */) {
                    /* LATIN SMALL LETTER E WITH CIRCUMFLEX AND GRAVE */
                    return '\u1EC1';
                }
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN SMALL LETTER E WITH CIRCUMFLEX AND ACUTE */
                    return '\u1EBF';
                }
                if (modifier2 == '\u00E4' /* TILDE */) {
                    /* LATIN SMALL LETTER E WITH CIRCUMFLEX AND TILDE */
                    return '\u1EC5';
                }
                if (modifier2 == '\u00F2' /* DOT BELOW */) {
                    /* LATIN SMALL LETTER E WITH CIRCUMFLEX AND DOT BELOW */
                    return '\u1EC7';
                }
                /* LATIN SMALL LETTER E WITH CIRCUMFLEX */
                return '\u00EA';
            }

            if (modifier1 == '\u00E4' /* TILDE */) {
                /* LATIN SMALL LETTER E WITH TILDE */
                return '\u1EBD';
            }

            if (modifier1 == '\u00E5' /* MACRON */) {
                if (modifier2 == '\u00E1' /* GRAVE */) {
                    /* LATIN SMALL LETTER E WITH MACRON AND GRAVE */
                    return '\u1E15';
                }
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN SMALL LETTER E WITH MACRON AND ACUTE */
                    return '\u1E17';
                }
                /* LATIN SMALL LETTER E WITH MACRON */
                return '\u0113';
            }

            if (modifier1 == '\u00E6' /* BREVE */) {
                /* LATIN SMALL LETTER E WITH BREVE */
                return '\u0115';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN SMALL LETTER E WITH DOT ABOVE */
                return '\u0117';
            }

            if (modifier1 == '\u00E8' /* DIAERESIS */) {
                /* LATIN SMALL LETTER E WITH DIAERESIS */
                return '\u00EB';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN SMALL LETTER E WITH CARON */
                return '\u011B';
            }

            if (modifier1 == '\u00F0' /* CEDILLA */) {
                if (modifier2 == '\u00E6' /* BREVE */) {
                    /* LATIN SMALL LETTER E WITH CEDILLA AND BREVE */
                    return '\u1E1D';
                }
                /* LATIN SMALL LETTER E WITH CEDILLA */
                return '\u0229';
            }

            if (modifier1 == '\u00F1' /* OGONEK */) {
                /* LATIN SMALL LETTER E WITH OGONEK */
                return '\u0119';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN SMALL LETTER E WITH DOT BELOW */
                return '\u1EB9';
            }

        }
        if (baseChar == 'f') {
            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN SMALL LETTER F WITH DOT ABOVE */
                return '\u1E1F';
            }

        }
        if (baseChar == 'g') {
            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN SMALL LETTER G WITH ACUTE */
                return '\u01F5';
            }

            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                /* LATIN SMALL LETTER G WITH CIRCUMFLEX */
                return '\u011D';
            }

            if (modifier1 == '\u00E5' /* MACRON */) {
                /* LATIN SMALL LETTER G WITH MACRON */
                return '\u1E21';
            }

            if (modifier1 == '\u00E6' /* BREVE */) {
                /* LATIN SMALL LETTER G WITH BREVE */
                return '\u011F';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN SMALL LETTER G WITH DOT ABOVE */
                return '\u0121';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN SMALL LETTER G WITH CARON */
                return '\u01E7';
            }

            if (modifier1 == '\u00F0' /* CEDILLA */) {
                /* LATIN SMALL LETTER G WITH CEDILLA */
                return '\u0123';
            }

        }
        if (baseChar == 'h') {
            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                /* LATIN SMALL LETTER H WITH CIRCUMFLEX */
                return '\u0125';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN SMALL LETTER H WITH DOT ABOVE */
                return '\u1E23';
            }

            if (modifier1 == '\u00E8' /* DIAERESIS */) {
                /* LATIN SMALL LETTER H WITH DIAERESIS */
                return '\u1E27';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN SMALL LETTER H WITH CARON */
                return '\u021F';
            }

            if (modifier1 == '\u00F0' /* CEDILLA */) {
                /* LATIN SMALL LETTER H WITH CEDILLA */
                return '\u1E29';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN SMALL LETTER H WITH DOT BELOW */
                return '\u1E25';
            }

            if (modifier1 == '\u00F9' /* BREVE BELOW */) {
                /* LATIN SMALL LETTER H WITH BREVE BELOW */
                return '\u1E2B';
            }

        }
        if (baseChar == 'i') {
            if (modifier1 == '\u00E0' /* HOOK ABOVE */) {
                /* LATIN SMALL LETTER I WITH HOOK ABOVE */
                return '\u1EC9';
            }

            if (modifier1 == '\u00E1' /* GRAVE */) {
                /* LATIN SMALL LETTER I WITH GRAVE */
                return '\u00EC';
            }

            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN SMALL LETTER I WITH ACUTE */
                return '\u00ED';
            }

            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                /* LATIN SMALL LETTER I WITH CIRCUMFLEX */
                return '\u00EE';
            }

            if (modifier1 == '\u00E4' /* TILDE */) {
                /* LATIN SMALL LETTER I WITH TILDE */
                return '\u0129';
            }

            if (modifier1 == '\u00E5' /* MACRON */) {
                /* LATIN SMALL LETTER I WITH MACRON */
                return '\u012B';
            }

            if (modifier1 == '\u00E6' /* BREVE */) {
                /* LATIN SMALL LETTER I WITH BREVE */
                return '\u012D';
            }

            if (modifier1 == '\u00E8' /* DIAERESIS */) {
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN SMALL LETTER I WITH DIAERESIS AND ACUTE */
                    return '\u1E2F';
                }
                /* LATIN SMALL LETTER I WITH DIAERESIS */
                return '\u00EF';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN SMALL LETTER I WITH CARON */
                return '\u01D0';
            }

            if (modifier1 == '\u00F1' /* OGONEK */) {
                /* LATIN SMALL LETTER I WITH OGONEK */
                return '\u012F';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN SMALL LETTER I WITH DOT BELOW */
                return '\u1ECB';
            }

        }
        if (baseChar == 'j') {
            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                /* LATIN SMALL LETTER J WITH CIRCUMFLEX */
                return '\u0135';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN SMALL LETTER J WITH CARON */
                return '\u01F0';
            }

        }
        if (baseChar == 'k') {
            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN SMALL LETTER K WITH ACUTE */
                return '\u1E31';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN SMALL LETTER K WITH CARON */
                return '\u01E9';
            }

            if (modifier1 == '\u00F0' /* CEDILLA */) {
                /* LATIN SMALL LETTER K WITH CEDILLA */
                return '\u0137';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN SMALL LETTER K WITH DOT BELOW */
                return '\u1E33';
            }

        }
        if (baseChar == 'l') {
            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN SMALL LETTER L WITH ACUTE */
                return '\u013A';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN SMALL LETTER L WITH CARON */
                return '\u013E';
            }

            if (modifier1 == '\u00F0' /* CEDILLA */) {
                /* LATIN SMALL LETTER L WITH CEDILLA */
                return '\u013C';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                if (modifier2 == '\u00E5' /* MACRON */) {
                    /* LATIN SMALL LETTER L WITH DOT BELOW AND MACRON */
                    return '\u1E39';
                }
                /* LATIN SMALL LETTER L WITH DOT BELOW */
                return '\u1E37';
            }

        }
        if (baseChar == 'm') {
            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN SMALL LETTER M WITH ACUTE */
                return '\u1E3F';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN SMALL LETTER M WITH DOT ABOVE */
                return '\u1E41';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN SMALL LETTER M WITH DOT BELOW */
                return '\u1E43';
            }

        }
        if (baseChar == 'n') {
            if (modifier1 == '\u00E1' /* GRAVE */) {
                /* LATIN SMALL LETTER N WITH GRAVE */
                return '\u01F9';
            }

            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN SMALL LETTER N WITH ACUTE */
                return '\u0144';
            }

            if (modifier1 == '\u00E4' /* TILDE */) {
                /* LATIN SMALL LETTER N WITH TILDE */
                return '\u00F1';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN SMALL LETTER N WITH DOT ABOVE */
                return '\u1E45';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN SMALL LETTER N WITH CARON */
                return '\u0148';
            }

            if (modifier1 == '\u00F0' /* CEDILLA */) {
                /* LATIN SMALL LETTER N WITH CEDILLA */
                return '\u0146';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN SMALL LETTER N WITH DOT BELOW */
                return '\u1E47';
            }

        }
        if (baseChar == 'o') {
            if (modifier1 == '\u00E0' /* HOOK ABOVE */) {
                /* LATIN SMALL LETTER O WITH HOOK ABOVE */
                return '\u1ECF';
            }

            if (modifier1 == '\u00E1' /* GRAVE */) {
                /* LATIN SMALL LETTER O WITH GRAVE */
                return '\u00F2';
            }

            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN SMALL LETTER O WITH ACUTE */
                return '\u00F3';
            }

            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                if (modifier2 == '\u00E0' /* HOOK ABOVE */) {
                    /* LATIN SMALL LETTER O WITH CIRCUMFLEX AND HOOK ABOVE */
                    return '\u1ED5';
                }
                if (modifier2 == '\u00E1' /* GRAVE */) {
                    /* LATIN SMALL LETTER O WITH CIRCUMFLEX AND GRAVE */
                    return '\u1ED3';
                }
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN SMALL LETTER O WITH CIRCUMFLEX AND ACUTE */
                    return '\u1ED1';
                }
                if (modifier2 == '\u00E4' /* TILDE */) {
                    /* LATIN SMALL LETTER O WITH CIRCUMFLEX AND TILDE */
                    return '\u1ED7';
                }
                if (modifier2 == '\u00F2' /* DOT BELOW */) {
                    /* LATIN SMALL LETTER O WITH CIRCUMFLEX AND DOT BELOW */
                    return '\u1ED9';
                }
                /* LATIN SMALL LETTER O WITH CIRCUMFLEX */
                return '\u00F4';
            }

            if (modifier1 == '\u00E4' /* TILDE */) {
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN SMALL LETTER O WITH TILDE AND ACUTE */
                    return '\u1E4D';
                }
                if (modifier2 == '\u00E5' /* MACRON */) {
                    /* LATIN SMALL LETTER O WITH TILDE AND MACRON */
                    return '\u022D';
                }
                if (modifier2 == '\u00E8' /* DIAERESIS */) {
                    /* LATIN SMALL LETTER O WITH TILDE AND DIAERESIS */
                    return '\u1E4F';
                }
                /* LATIN SMALL LETTER O WITH TILDE */
                return '\u00F5';
            }

            if (modifier1 == '\u00E5' /* MACRON */) {
                if (modifier2 == '\u00E1' /* GRAVE */) {
                    /* LATIN SMALL LETTER O WITH MACRON AND GRAVE */
                    return '\u1E51';
                }
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN SMALL LETTER O WITH MACRON AND ACUTE */
                    return '\u1E53';
                }
                /* LATIN SMALL LETTER O WITH MACRON */
                return '\u014D';
            }

            if (modifier1 == '\u00E6' /* BREVE */) {
                /* LATIN SMALL LETTER O WITH BREVE */
                return '\u014F';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                if (modifier2 == '\u00E5' /* MACRON */) {
                    /* LATIN SMALL LETTER O WITH DOT ABOVE AND MACRON */
                    return '\u0231';
                }
                /* LATIN SMALL LETTER O WITH DOT ABOVE */
                return '\u022F';
            }

            if (modifier1 == '\u00E8' /* DIAERESIS */) {
                if (modifier2 == '\u00E5' /* MACRON */) {
                    /* LATIN SMALL LETTER O WITH DIAERESIS AND MACRON */
                    return '\u022B';
                }
                /* LATIN SMALL LETTER O WITH DIAERESIS */
                return '\u00F6';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN SMALL LETTER O WITH CARON */
                return '\u01D2';
            }

            if (modifier1 == '\u00EE' /* DOUBLE ACUTE */) {
                /* LATIN SMALL LETTER O WITH DOUBLE ACUTE */
                return '\u0151';
            }

            if (modifier1 == '\u00F1' /* OGONEK */) {
                if (modifier2 == '\u00E5' /* MACRON */) {
                    /* LATIN SMALL LETTER O WITH OGONEK AND MACRON */
                    return '\u01ED';
                }
                /* LATIN SMALL LETTER O WITH OGONEK */
                return '\u01EB';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN SMALL LETTER O WITH DOT BELOW */
                return '\u1ECD';
            }

        }
        if (baseChar == 'p') {
            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN SMALL LETTER P WITH ACUTE */
                return '\u1E55';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN SMALL LETTER P WITH DOT ABOVE */
                return '\u1E57';
            }

        }
        if (baseChar == 'q') {
        }
        if (baseChar == 'r') {
            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN SMALL LETTER R WITH ACUTE */
                return '\u0155';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN SMALL LETTER R WITH DOT ABOVE */
                return '\u1E59';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN SMALL LETTER R WITH CARON */
                return '\u0159';
            }

            if (modifier1 == '\u00F0' /* CEDILLA */) {
                /* LATIN SMALL LETTER R WITH CEDILLA */
                return '\u0157';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                if (modifier2 == '\u00E5' /* MACRON */) {
                    /* LATIN SMALL LETTER R WITH DOT BELOW AND MACRON */
                    return '\u1E5D';
                }
                /* LATIN SMALL LETTER R WITH DOT BELOW */
                return '\u1E5B';
            }

        }
        if (baseChar == 's') {
            if (modifier1 == '\u00E2' /* ACUTE */) {
                if (modifier2 == '\u00E7' /* DOT ABOVE */) {
                    /* LATIN SMALL LETTER S WITH ACUTE AND DOT ABOVE */
                    return '\u1E65';
                }
                /* LATIN SMALL LETTER S WITH ACUTE */
                return '\u015B';
            }

            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                /* LATIN SMALL LETTER S WITH CIRCUMFLEX */
                return '\u015D';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN SMALL LETTER S WITH DOT ABOVE */
                return '\u1E61';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                if (modifier2 == '\u00E7' /* DOT ABOVE */) {
                    /* LATIN SMALL LETTER S WITH CARON AND DOT ABOVE */
                    return '\u1E67';
                }
                /* LATIN SMALL LETTER S WITH CARON */
                return '\u0161';
            }

            if (modifier1 == '\u00F0' /* CEDILLA */) {
                /* LATIN SMALL LETTER S WITH CEDILLA */
                return '\u015F';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                if (modifier2 == '\u00E7' /* DOT ABOVE */) {
                    /* LATIN SMALL LETTER S WITH DOT BELOW AND DOT ABOVE */
                    return '\u1E69';
                }
                /* LATIN SMALL LETTER S WITH DOT BELOW */
                return '\u1E63';
            }

            if (modifier1 == '\u00F7' /* COMMA BELOW */) {
                /* LATIN SMALL LETTER S WITH COMMA BELOW */
                return '\u0219';
            }

        }
        if (baseChar == 't') {
            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN SMALL LETTER T WITH DOT ABOVE */
                return '\u1E6B';
            }

            if (modifier1 == '\u00E8' /* DIAERESIS */) {
                /* LATIN SMALL LETTER T WITH DIAERESIS */
                return '\u1E97';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN SMALL LETTER T WITH CARON */
                return '\u0165';
            }

            if (modifier1 == '\u00F0' /* CEDILLA */) {
                /* LATIN SMALL LETTER T WITH CEDILLA */
                return '\u0163';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN SMALL LETTER T WITH DOT BELOW */
                return '\u1E6D';
            }

            if (modifier1 == '\u00F7' /* COMMA BELOW */) {
                /* LATIN SMALL LETTER T WITH COMMA BELOW */
                return '\u021B';
            }

        }
        if (baseChar == 'u') {
            if (modifier1 == '\u00E0' /* HOOK ABOVE */) {
                /* LATIN SMALL LETTER U WITH HOOK ABOVE */
                return '\u1EE7';
            }

            if (modifier1 == '\u00E1' /* GRAVE */) {
                /* LATIN SMALL LETTER U WITH GRAVE */
                return '\u00F9';
            }

            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN SMALL LETTER U WITH ACUTE */
                return '\u00FA';
            }

            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                /* LATIN SMALL LETTER U WITH CIRCUMFLEX */
                return '\u00FB';
            }

            if (modifier1 == '\u00E4' /* TILDE */) {
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN SMALL LETTER U WITH TILDE AND ACUTE */
                    return '\u1E79';
                }
                /* LATIN SMALL LETTER U WITH TILDE */
                return '\u0169';
            }

            if (modifier1 == '\u00E5' /* MACRON */) {
                if (modifier2 == '\u00E8' /* DIAERESIS */) {
                    /* LATIN SMALL LETTER U WITH MACRON AND DIAERESIS */
                    return '\u1E7B';
                }
                /* LATIN SMALL LETTER U WITH MACRON */
                return '\u016B';
            }

            if (modifier1 == '\u00E6' /* BREVE */) {
                /* LATIN SMALL LETTER U WITH BREVE */
                return '\u016D';
            }

            if (modifier1 == '\u00E8' /* DIAERESIS */) {
                if (modifier2 == '\u00E1' /* GRAVE */) {
                    /* LATIN SMALL LETTER U WITH DIAERESIS AND GRAVE */
                    return '\u01DC';
                }
                if (modifier2 == '\u00E2' /* ACUTE */) {
                    /* LATIN SMALL LETTER U WITH DIAERESIS AND ACUTE */
                    return '\u01D8';
                }
                if (modifier2 == '\u00E5' /* MACRON */) {
                    /* LATIN SMALL LETTER U WITH DIAERESIS AND MACRON */
                    return '\u01D6';
                }
                if (modifier2 == '\u00E9' /* CARON */) {
                    /* LATIN SMALL LETTER U WITH DIAERESIS AND CARON */
                    return '\u01DA';
                }
                /* LATIN SMALL LETTER U WITH DIAERESIS */
                return '\u00FC';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN SMALL LETTER U WITH CARON */
                return '\u01D4';
            }

            if (modifier1 == '\u00EA' /* RING ABOVE */) {
                /* LATIN SMALL LETTER U WITH RING ABOVE */
                return '\u016F';
            }

            if (modifier1 == '\u00EE' /* DOUBLE ACUTE */) {
                /* LATIN SMALL LETTER U WITH DOUBLE ACUTE */
                return '\u0171';
            }

            if (modifier1 == '\u00F1' /* OGONEK */) {
                /* LATIN SMALL LETTER U WITH OGONEK */
                return '\u0173';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN SMALL LETTER U WITH DOT BELOW */
                return '\u1EE5';
            }

            if (modifier1 == '\u00F3' /* DIAERESIS BELOW */) {
                /* LATIN SMALL LETTER U WITH DIAERESIS BELOW */
                return '\u1E73';
            }

        }
        if (baseChar == 'v') {
            if (modifier1 == '\u00E4' /* TILDE */) {
                /* LATIN SMALL LETTER V WITH TILDE */
                return '\u1E7D';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN SMALL LETTER V WITH DOT BELOW */
                return '\u1E7F';
            }

        }
        if (baseChar == 'w') {
            if (modifier1 == '\u00E1' /* GRAVE */) {
                /* LATIN SMALL LETTER W WITH GRAVE */
                return '\u1E81';
            }

            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN SMALL LETTER W WITH ACUTE */
                return '\u1E83';
            }

            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                /* LATIN SMALL LETTER W WITH CIRCUMFLEX */
                return '\u0175';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN SMALL LETTER W WITH DOT ABOVE */
                return '\u1E87';
            }

            if (modifier1 == '\u00E8' /* DIAERESIS */) {
                /* LATIN SMALL LETTER W WITH DIAERESIS */
                return '\u1E85';
            }

            if (modifier1 == '\u00EA' /* RING ABOVE */) {
                /* LATIN SMALL LETTER W WITH RING ABOVE */
                return '\u1E98';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN SMALL LETTER W WITH DOT BELOW */
                return '\u1E89';
            }

        }
        if (baseChar == 'x') {
            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN SMALL LETTER X WITH DOT ABOVE */
                return '\u1E8B';
            }

            if (modifier1 == '\u00E8' /* DIAERESIS */) {
                /* LATIN SMALL LETTER X WITH DIAERESIS */
                return '\u1E8D';
            }

        }
        if (baseChar == 'y') {
            if (modifier1 == '\u00E0' /* HOOK ABOVE */) {
                /* LATIN SMALL LETTER Y WITH HOOK ABOVE */
                return '\u1EF7';
            }

            if (modifier1 == '\u00E1' /* GRAVE */) {
                /* LATIN SMALL LETTER Y WITH GRAVE */
                return '\u1EF3';
            }

            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN SMALL LETTER Y WITH ACUTE */
                return '\u00FD';
            }

            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                /* LATIN SMALL LETTER Y WITH CIRCUMFLEX */
                return '\u0177';
            }

            if (modifier1 == '\u00E4' /* TILDE */) {
                /* LATIN SMALL LETTER Y WITH TILDE */
                return '\u1EF9';
            }

            if (modifier1 == '\u00E5' /* MACRON */) {
                /* LATIN SMALL LETTER Y WITH MACRON */
                return '\u0233';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN SMALL LETTER Y WITH DOT ABOVE */
                return '\u1E8F';
            }

            if (modifier1 == '\u00E8' /* DIAERESIS */) {
                /* LATIN SMALL LETTER Y WITH DIAERESIS */
                return '\u00FF';
            }

            if (modifier1 == '\u00EA' /* RING ABOVE */) {
                /* LATIN SMALL LETTER Y WITH RING ABOVE */
                return '\u1E99';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN SMALL LETTER Y WITH DOT BELOW */
                return '\u1EF5';
            }

        }
        if (baseChar == 'z') {
            if (modifier1 == '\u00E2' /* ACUTE */) {
                /* LATIN SMALL LETTER Z WITH ACUTE */
                return '\u017A';
            }

            if (modifier1 == '\u00E3' /* CIRCUMFLEX */) {
                /* LATIN SMALL LETTER Z WITH CIRCUMFLEX */
                return '\u1E91';
            }

            if (modifier1 == '\u00E7' /* DOT ABOVE */) {
                /* LATIN SMALL LETTER Z WITH DOT ABOVE */
                return '\u017C';
            }

            if (modifier1 == '\u00E9' /* CARON */) {
                /* LATIN SMALL LETTER Z WITH CARON */
                return '\u017E';
            }

            if (modifier1 == '\u00F2' /* DOT BELOW */) {
                /* LATIN SMALL LETTER Z WITH DOT BELOW */
                return '\u1E93';
            }

        }

        return 0;
    }

}
