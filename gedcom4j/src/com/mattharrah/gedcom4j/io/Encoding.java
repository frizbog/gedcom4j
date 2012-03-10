/*
 * Copyright (c) 2009-2012 Matthew R. Harrah
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
package com.mattharrah.gedcom4j.io;

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
    ASCII,
    /**
     * ANSEL (American National Standard for Extended Latin), aka ANSI
     * Z39.47-1985, aka MARC-8. This is the default for GEDCOM files. One byte
     * per character.
     */
    ANSEL,
    /**
     * Unicode, big-endian. Two bytes per character.
     */
    UNICODE_BIG_ENDIAN,
    /**
     * Unicode, little-endian. Two bytes per character.
     */
    UNICODE_LITTLE_ENDIAN;
}
