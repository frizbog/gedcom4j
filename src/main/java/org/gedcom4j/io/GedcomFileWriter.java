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

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * <p>
 * A class for writing staged gedcom file lines out to a file. The {@link org.gedcom4j.writer.GedcomWriter} class
 * prepares a buffer of gedcom lines (a {@link List} of String) that this class writes out to a file, a stream, etc.,
 * after encoding the data into ANSEL, ASCII, or UNICODE, as needed. A separate class is needed because GEDCOM requires
 * ANSEL support which Java doesn't have, and also limits the encodings to the three choices mentioned.
 * </p>
 * <p>
 * Note that GEDCOM standard does not allow for BOM's or other preambles for encodings, so none is created by this
 * class.
 * </p>
 * 
 * @author frizbog1
 * 
 */
public class GedcomFileWriter {
    /**
     * The line terminator character to use - defaults to JVM settings but can be overridden
     */
    public LineTerminator terminator;

    /**
     * Should this writer use little-endian ordering when writing unicode data? Defaults to true.
     */
    public boolean useLittleEndianForUnicode = true;

    /**
     * The encoding-specific writer that was or will be used by this writer.
     */
    AbstractEncodingSpecificWriter encodingSpecificWriter;

    /**
     * The lines of the gedcom file (in internal java string format - that is, UTF-16)
     */
    private final List<String> gedcomLines;

    /**
     * Constructor
     * 
     * @param gedcomLines
     *            the lines of text to write
     */
    public GedcomFileWriter(List<String> gedcomLines) {
        this.gedcomLines = gedcomLines;
        setDefaultLineTerminator();
    }

    /**
     * Write the gedcom lines to an output stream, encoding as needed
     * 
     * @param out
     *            the output stream
     * @throws IOException
     *             if the data can't be written to the stream
     */
    public void write(OutputStream out) throws IOException {

        encodingSpecificWriter = new AnselWriter();

        for (String line : gedcomLines) {
            if ("1 CHAR ASCII".equals(line)) {
                encodingSpecificWriter = new AsciiWriter();
                break;
            }
            if ("1 CHAR UTF-8".equals(line)) {
                encodingSpecificWriter = new Utf8Writer();
                break;
            }
            if ("1 CHAR UNICODE".equals(line)) {
                if (useLittleEndianForUnicode) {
                    encodingSpecificWriter = new UnicodeLittleEndianWriter();
                } else {
                    encodingSpecificWriter = new UnicodeBigEndianWriter();
                }
                break;
            }
        }

        encodingSpecificWriter.gedcomLines = gedcomLines;
        encodingSpecificWriter.terminator = terminator;
        encodingSpecificWriter.write(out);
    }

    /**
     * Set default line terminator based on JVM settings
     */
    private void setDefaultLineTerminator() {
        // Default setting is CRLF - a good, safe choice
        terminator = LineTerminator.CRLF;
        String jvmLineTerm = System.getProperty("line.separator");

        if (Character.toString((char) 0x0D).equals(jvmLineTerm)) {
            terminator = LineTerminator.CR_ONLY;
        } else if (Character.toString((char) 0x0A).equals(jvmLineTerm)) {
            terminator = LineTerminator.LF_ONLY;
        } else if ((Character.toString((char) 0x0D) + Character.toString((char) 0x0A)).equals(jvmLineTerm)) {
            terminator = LineTerminator.CRLF;
        } else if ((Character.toString((char) 0x0A) + Character.toString((char) 0x0D)).equals(jvmLineTerm)) {
            // Who does this?!
            terminator = LineTerminator.LFCR;
        }
    }
}
