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
package org.gedcom4j.io;

import java.util.Set;
import java.util.TreeSet;

/**
 * An encoding for a gedcom file.
 * 
 * @author frizbog1
 */
public enum Encoding {

    /**
     * ASCII. Only characters from 0x00 to 0x7F are expected, one byte per
     * character.
     */
    ASCII("ASCII"),
    /**
     * ANSEL (American National Standard for Extended Latin), aka ANSI
     * Z39.47-1985, aka MARC-8. This is the default for GEDCOM files. One byte
     * per character.
     */
    ANSEL("ANSEL"),
    /**
     * Unicode, big-endian. Two bytes per character.
     */
    UNICODE_BIG_ENDIAN("UNICODE"),
    /**
     * Unicode, little-endian. Two bytes per character.
     */
    UNICODE_LITTLE_ENDIAN("UNICODE"),
    /**
     * UTF-8 encoding.
     */
    UTF_8("UTF-8");


    /**
     * The character set name found in the GEDCOM that represents a character
     * set encoding. Note that multiple instances of this enum can share the
     * same value for this field.
     */
    private String characterSetName;

    /**
     * Constructor
     *
     * @param characterSetName
     *            the character set name in the GEDCOM that corresponds to this
     *            Encoding object. Note that this value will not be unique among
     *            instances in the enum.
     */
    private Encoding(String characterSetName) {
        this.characterSetName = characterSetName;
    }

    /**
     * Get an alphabetically sorted set of supported character set names
     * 
     * @return an alphabetically sorted set of supported character set names
     */
    public static Set<String> getSupportedCharacterSetNames() {
        Set<String> result = new TreeSet<String>();
        for (Encoding e : values()) {
            result.add(e.characterSetName);
        }
        return result;
    }

    /**
     * Check if a given character set name is valid for an encoding
     * 
     * @param characterSetName
     *            the character set name (e.g., "ANSEL", "UTF-8")
     * @return true if and only if there is an {@link Encoding} defined for the
     *         given character set name
     */
    public static boolean isValidCharacterSetName(String characterSetName) {
        for (Encoding e : values()) {
            if (e.characterSetName.equals(characterSetName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the character set name
     * 
     * @return the character set name
     */
    public String getCharacterSetName() {
        return characterSetName;
    }
}
