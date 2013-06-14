/*
 * Copyright (c) 2009-2013 Matthew R. Harrah
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
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

/**
 * <p>
 * A class for writing staged gedcom file lines out to a file. The {@link org.gedcom4j.writer.GedcomWriter} class prepares a buffer of
 * gedcom lines (a {@link List} of String) that this class writes out to a file, a stream, etc., after encoding the data into ANSEL, ASCII, or
 * UNICODE, as needed. A separate class is needed because GEDCOM requires ANSEL support which Java doesn't have, and also limits the encodings to the
 * three choices mentioned.
 * </p>
 * <p>
 * Note that GEDCOM standard does not allow for BOM's or other preambles for encodings, so none is created by this class.
 * </p>
 * 
 * @author frizbog1
 * 
 */
public class GedcomFileWriter {
    /**
     * The types of line terminators that are supported by the GedcomFileWriter
     * 
     * @author frizbog1
     */
    public enum LineTerminator {
        /**
         * CR + LF - used mostly for Windows
         */
        CRLF,
        /**
         * LF+CR - not commonly used, but GEDCOM supports it
         */
        LFCR,
        /**
         * LF only - used mostly by Linux and newer Macs
         */
        LF_ONLY,
        /**
         * CR only - used mostly by old Macs
         */
        CR_ONLY
    }

    /**
     * The encoding to use when writing the data. Defaults to ANSEL, per GEDCOM spec, unless the gedcom content overrides in the header. Deliberately
     * package private so unit tests can examine it, but so client code can't manipulate it.
     */
    Encoding encoding;

    /**
     * Should this writer use little-endian ordering when writing unicode data? Defaults to true.
     */
    private boolean useLittleEndianForUnicode = true;

    /**
     * The line terminator character to use - defaults to JVM settings but can be overridden
     */
    public LineTerminator terminator;

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
        setEncodingFromContent();
    }

    /**
     * Change whether this GedcomFileWriter will use little-endian byte-ordering for Unicode.
     * 
     * @param useLittleEndian
     *            pass in true if you want little-endian byte-ordering
     */
    public void setLittleEndianForUnicode(boolean useLittleEndian) {
        useLittleEndianForUnicode = useLittleEndian;
        // Swap encoding as needed
        if (useLittleEndian && encoding == Encoding.UNICODE_BIG_ENDIAN) {
            encoding = Encoding.UNICODE_LITTLE_ENDIAN;
        }
        if (!useLittleEndian && encoding == Encoding.UNICODE_LITTLE_ENDIAN) {
            encoding = Encoding.UNICODE_BIG_ENDIAN;
        }
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
    	switch (encoding) {
    	case ASCII:
    		for (String line : gedcomLines) {
    			writeAsciiLine(out, line);
    		}
    		break;
    	case ANSEL:
    		for (String line : gedcomLines) {
    			writeAnselLine(out, line);
    		}
    		break;
    	case UNICODE_BIG_ENDIAN:
    		for (String line : gedcomLines) {
    			writeUnicodeBigEndianLine(out, line);
    		}
    		break;
    	case UNICODE_LITTLE_ENDIAN:
    		for (String line : gedcomLines) {
    			writeUnicodeLittleEndianLine(out, line);
    		}
    		break;
    	case UTF_8:
    		String lineTerminator = null;
    		switch (terminator) {
    		case CR_ONLY:
    			lineTerminator = "\r";
    			break;
    		case LF_ONLY:
    			lineTerminator = "\n";
    			break;
    		case LFCR:
    			lineTerminator = "\n\r";
    			break;
    		case CRLF:
    			lineTerminator = "\r\n";
    			break;
    		default:
    			throw new IllegalStateException("Terminator selection of " + terminator + " is an unrecognized value");
    		}

    		OutputStreamWriter writer = new OutputStreamWriter(out, Charset.forName("UTF-8"));
    		try {
	    		for (String line : gedcomLines) {
	    			writer.write(line);
	    			writer.write(lineTerminator);
	    		}
    		} finally {
    			writer.flush();
    			writer.close();
    		}
    		break;
    	default:
    		throw new IllegalStateException("Encoding " + encoding + " is an unrecognized value");
    	}
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

    /**
     * Set the encoding for the writer from the content. Defaults to ANSEL (per GEDCOM spec), but if it sees that ASCII, UTF-8, or UNICODE are to be
     * used (according to the header) it will use that instead.
     */
    private void setEncodingFromContent() {
        encoding = Encoding.ANSEL;

        for (String line : gedcomLines) {
            if ("1 CHAR ASCII".equals(line)) {
                encoding = Encoding.ASCII;
                return;
            }
            if ("1 CHAR UTF-8".equals(line)) {
                encoding = Encoding.UTF_8;
                return;
            }
            if ("1 CHAR UNICODE".equals(line)) {
                if (useLittleEndianForUnicode) {
                    encoding = Encoding.UNICODE_LITTLE_ENDIAN;
                } else {
                    encoding = Encoding.UNICODE_BIG_ENDIAN;
                }
                return;
            }
        }
    }

    /**
     * Write data out as ANSEL lines.
     * 
     * @param out
     *            the output stream we're writing to
     * @param line
     *            the line of text we're writing
     * @throws IOException
     *             if the data can't be written to the stream
     */
    private void writeAnselLine(OutputStream out, String line) throws IOException {
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            out.write(AnselMapping.encode(c));
        }
        writeLineTerminator(out);
    }

    /**
     * Write data out as ASCII lines. ANy characters in the line that are outside the 0x00-0x7F range allowed by ASCII are written out as question
     * marks.
     * 
     * @param out
     *            the output stream we're writing to
     * @param line
     *            the line of text we're writing
     * @throws IOException
     *             if the data can't be written to the stream
     */
    private void writeAsciiLine(OutputStream out, String line) throws IOException {
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c < 0 || c > 0x7f) {
                c = "?".charAt(0);
            }
            out.write(c);
        }
        writeLineTerminator(out);
    }

    /**
     * Write out the appropriate line terminator based on the encoding and terminator selection for this instance
     * 
     * @param out
     *            the output stream we're writing to
     * @throws IOException
     *             if the line terminator can't be written to the stream
     */
    private void writeLineTerminator(OutputStream out) throws IOException {
        switch (encoding) {
        case ASCII: // Use ANSEL's, it's the same
        case UTF_8: // Use ANSEL's, it's the same
        case ANSEL:
            switch (terminator) {
            case CR_ONLY:
                out.write((byte) 0x0D);
                break;
            case LF_ONLY:
                out.write((byte) 0x0A);
                break;
            case LFCR:
                out.write((byte) 0x0A);
                out.write((byte) 0x0D);
                break;
            case CRLF:
                out.write((byte) 0x0D);
                out.write((byte) 0x0A);
                break;
            default:
                throw new IllegalStateException("Terminator selection of " + terminator + " is an unrecognized value");
            }
            break;
        case UNICODE_BIG_ENDIAN:
            switch (terminator) {
            case CR_ONLY:
                out.write((byte) 0x00);
                out.write((byte) 0x0D);
                break;
            case LF_ONLY:
                out.write((byte) 0x00);
                out.write((byte) 0x0A);
                break;
            case LFCR:
                out.write((byte) 0x00);
                out.write((byte) 0x0A);
                out.write((byte) 0x00);
                out.write((byte) 0x0D);
                break;
            case CRLF:
                out.write((byte) 0x00);
                out.write((byte) 0x0D);
                out.write((byte) 0x00);
                out.write((byte) 0x0A);
                break;
            default:
                throw new IllegalStateException("Terminator selection of " + terminator + " is an unrecognized value");
            }
            break;
        case UNICODE_LITTLE_ENDIAN:
            switch (terminator) {
            case CR_ONLY:
                out.write((byte) 0x0D);
                out.write((byte) 0x00);
                break;
            case LF_ONLY:
                out.write((byte) 0x0A);
                out.write((byte) 0x00);
                break;
            case LFCR:
                out.write((byte) 0x0A);
                out.write((byte) 0x00);
                out.write((byte) 0x0D);
                out.write((byte) 0x00);
                break;
            case CRLF:
                out.write((byte) 0x0D);
                out.write((byte) 0x00);
                out.write((byte) 0x0A);
                out.write((byte) 0x00);
                break;
            default:
                throw new IllegalStateException("Terminator selection of " + terminator + " is an unrecognized value");
            }
            break;
        default:
            throw new IllegalStateException("Encoding " + encoding + " is an unrecognized value");
        }
    }

    /**
     * Writes the data out as big-endian unicode.
     * 
     * @param out
     *            the output stream we're writing to
     * @param line
     *            the line of text we're writing
     * @throws IOException
     *             if the data can't be written to the stream
     */
    private void writeUnicodeBigEndianLine(OutputStream out, String line) throws IOException {
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            out.write(c >> 8);
            out.write(c & 0x00FF);
        }
        writeLineTerminator(out);

    }

    /**
     * Writes the data out as litte-endian unicode.
     * 
     * @param out
     *            the output stream we're writing to
     * @param line
     *            the line of text we're writing
     * @throws IOException
     *             if the data can't be written to the stream
     */
    private void writeUnicodeLittleEndianLine(OutputStream out, String line) throws IOException {
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            out.write(c & 0x00FF);
            out.write(c >> 8);
        }
        writeLineTerminator(out);
    }
}
