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
package org.gedcom4j.io.writer;

/**
 * The types of line terminators that are supported by the GedcomFileWriter
 * 
 * @author frizbog1
 */
public enum LineTerminator {
    /**
     * CR only - used mostly by old Macs
     */
    CR_ONLY,
    /**
     * CR + LF - used mostly for Windows
     */
    CRLF,
    /**
     * LF only - used mostly by Linux and newer Macs
     */
    LF_ONLY,
    /**
     * LF+CR - not commonly used, but GEDCOM supports it
     */
    LFCR;

    /**
     * Set default line terminator based on JVM settings
     * 
     * @return the line terminator that is the default for the platform currently being used
     */
    public static LineTerminator getDefaultLineTerminator() {
        String jvmLineTerm = System.getProperty("line.separator");

        if (Character.toString((char) 0x0D).equals(jvmLineTerm)) {
            return LineTerminator.CR_ONLY;
        } else if (Character.toString((char) 0x0A).equals(jvmLineTerm)) {
            return LineTerminator.LF_ONLY;
        } else if ((Character.toString((char) 0x0D) + Character.toString((char) 0x0A)).equals(jvmLineTerm)) {
            return LineTerminator.CRLF;
        } else if ((Character.toString((char) 0x0A) + Character.toString((char) 0x0D)).equals(jvmLineTerm)) {
            // Who does this?!
            return LineTerminator.LFCR;
        }
        return LineTerminator.CRLF;
    }

}
