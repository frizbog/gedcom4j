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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mattharrah.gedcom4j.parser.GedcomParser;

/**
 * A class for reading the gedcom files and handling ASCII, ANSEL, and UNICODE
 * coding as needed. It's basic job is to turn the bytes from the file into a
 * buffer (a {@link List} of Strings) that the {@link GedcomParser} can work
 * with. This class is needed because the built-in character encodings in Java
 * don't support ANSEL encoding, which is the default encoding for gedcom files.
 * 
 * @author frizbog1
 */
public class GedcomFileReader {

    /**
     * The encoding that this reader is using
     */
    private Encoding encoding;

    /**
     * Get the gedcom file as a list of string
     * 
     * @param byteStream
     *            an <code>InputStream</code> from which this class gets the
     *            bytes of the file
     * @return a <code>List</code> of <code>String</code> objects, each of which
     *         represents a line of the input file represented by the byte
     *         stream
     * @throws IOException
     *             if there is a problem reading the data
     */
    public List<String> getLines(InputStream byteStream) throws IOException {
        BufferedInputStream bytes = null;
        if (byteStream instanceof BufferedInputStream) {
            bytes = (BufferedInputStream) byteStream;
        } else {
            bytes = new BufferedInputStream(byteStream);
        }
        List<String> result = new ArrayList<String>();

        // Detect the encoding to use (for now, can switch during course of
        // loading)
        detectEncoding(bytes);

        switch (encoding) {
        case ANSEL:
        case ASCII:
            result.addAll(loadUnicodeAnselAscii(bytes));
            break;
        case UNICODE_BIG_ENDIAN:
            result.addAll(loadUnicodeBigEndian(bytes));
            break;
        case UNICODE_LITTLE_ENDIAN:
            result.addAll(loadUnicodeLittleEndian(bytes));
            break;
        default:
            throw new IllegalStateException("Unknown encoding " + encoding);
        }
        return result;
    }

    /**
     * <p>
     * Determine the encoding of the byte stream by examining its first two
     * characters, in order to determine how many bytes are to be read for each
     * character.
     * </p>
     * 
     * @param bytes
     *            an {@link InputStream} over the file bytes
     * @throws IOException
     *             if there is a problem reading the byte stream
     */
    private void detectEncoding(BufferedInputStream bytes) throws IOException {
        // Mark the beginning of the input stream so we can read ahead two
        // characters
        bytes.mark(2);

        // Determine encoding by testing first two bytes
        byte b1 = (byte) bytes.read();
        if (b1 < 0) {
            throw new IOException("Invalid gedcom - no bytes to read");
        }
        byte b2 = (byte) bytes.read();
        if (b1 < 0) {
            throw new IOException(
                    "Invalid gedcom - need at least two bytes of length");
        }
        bytes.reset();

        // If the first two bytes make up a single zero character, it's unicode
        if (b1 == 0x30 && b2 == 0x00) {
            encoding = Encoding.UNICODE_LITTLE_ENDIAN;
        } else if (b1 == 0x00 && b2 == 0x30) {
            encoding = Encoding.UNICODE_BIG_ENDIAN;
        } else if (b1 == 0x30 && b2 == 0x20) {
            encoding = Encoding.ANSEL;
        } else {
            throw new IOException(
                    "Not a gedcom file - doesn't begin with a zero in any supported encoding");
        }

    }

    /**
     * Read all the lines using ANSEL or ASCII encoding (1 byte per character)
     * 
     * @param byteStream
     *            the input stream of bytes
     * @return all the lines of the input stream
     * @throws IOException
     *             if there is a problem reading the bytes
     */
    private Collection<? extends String> loadUnicodeAnselAscii(
            InputStream byteStream) throws IOException {
        List<String> result = new ArrayList<String>();

        StringBuilder lineBuffer = new StringBuilder();

        int lastChar = -1;
        int b = -1;
        boolean eof = false;
        while (!eof) {
            lastChar = b;
            b = byteStream.read();

            // Check for EOF
            if (b < 0) {
                // hit EOF - add final line buffer (last line) and get out
                if (lineBuffer.length() > 0) {
                    result.add(lineBuffer.toString());
                }
                eof = true;
                break;
            }

            // Check for carriage returns - signify EOL
            if (b == 0x0D) {
                result.add(lineBuffer.toString());
                lineBuffer = new StringBuilder();
                continue;
            }

            // Check for line feeds - signify EOL (unless prev char was a CR)
            if (b == 0x0A) {
                if (lastChar != 0x0D) {
                    result.add(lineBuffer.toString());
                    lineBuffer = new StringBuilder();
                }
                continue;
            }

            // All other characters in 0x00 to 0x7F range are treated the same,
            // regardless of encoding, and added as is
            if (b < 0x80) {
                lineBuffer.append(Character.valueOf((char) b));
                continue;
            }

            // Hit an extended character. Better be in ANSEL mode -- ASCII
            // doesn't support above 0x7F
            if (encoding != Encoding.ANSEL) {
                throw new IOException(
                        "Extended characters not supported in ASCII: 0x"
                                + Integer.toHexString(b));
            }

            lineBuffer.append(AnselMapping.decode(b));
        }

        return result;
    }

    /**
     * Read all the lines using Unicode big-endian encoding (2 bytes per
     * character, most significant values in second byte)
     * 
     * @param byteStream
     *            the input stream of bytes
     * @return all the lines of the input stream
     * @throws IOException
     */
    private Collection<? extends String> loadUnicodeBigEndian(
            InputStream byteStream) throws IOException {
        List<String> result = new ArrayList<String>();

        StringBuilder lineBuffer = new StringBuilder();

        boolean eof = false;
        int b1 = -1;
        int b2 = -1;

        int lastB1;
        int lastB2;
        while (!eof) {
            lastB1 = b1;
            lastB2 = b2;
            b1 = byteStream.read();
            b2 = byteStream.read();

            // Check for EOF
            if (b1 < 0 || b2 < 0) {
                // hit EOF - add final line buffer (last line) and get out
                if (lineBuffer.length() > 0) {
                    result.add(lineBuffer.toString());
                }
                eof = true;
                break;
            }

            // Check for carriage returns - signify EOL
            if (b1 == 0x00 && b2 == 0x0D) {
                result.add(lineBuffer.toString());
                lineBuffer = new StringBuilder();
                continue;
            }

            // Check for line feeds - signify EOL (unless prev char was a CR)
            if (b1 == 0x00 && b2 == 0x0A) {
                if (lastB1 != 0x00 || lastB2 != 0x0D) {
                    result.add(lineBuffer.toString());
                    lineBuffer = new StringBuilder();
                }
                continue;
            }

            int unicodeChar = (b1 << 8) | b2;
            lineBuffer.append(Character.valueOf((char) unicodeChar));
        }

        return result;
    }

    /**
     * Read all the lines using Unicode little-endian encoding (2 bytes per
     * character, most significant values in first byte)
     * 
     * @param byteStream
     *            the input stream of bytes
     * @return all the lines of the input stream
     * @throws IOException
     *             if the data can't be read
     */
    private Collection<? extends String> loadUnicodeLittleEndian(
            InputStream byteStream) throws IOException {
        List<String> result = new ArrayList<String>();

        StringBuilder lineBuffer = new StringBuilder();

        boolean eof = false;
        int b1 = -1;
        int b2 = -1;

        int lastB1;
        int lastB2;
        while (!eof) {
            lastB1 = b1;
            lastB2 = b2;
            b1 = byteStream.read();
            b2 = byteStream.read();

            // Check for EOF
            if (b1 < 0 || b2 < 0) {
                // hit EOF - add final line buffer (last line) and get out
                if (lineBuffer.length() > 0) {
                    result.add(lineBuffer.toString());
                }
                eof = true;
                break;
            }

            // Check for carriage returns - signify EOL
            if (b1 == 0x0D && b2 == 0x00) {
                result.add(lineBuffer.toString());
                lineBuffer = new StringBuilder();
                continue;
            }

            // Check for line feeds - signify EOL (unless prev char was a CR)
            if (b1 == 0x0A && b2 == 0x00) {
                if (lastB1 != 0x0D || lastB2 != 0x00) {
                    result.add(lineBuffer.toString());
                    lineBuffer = new StringBuilder();
                }
                continue;
            }

            int unicodeChar = (b2 << 8) | b1;
            lineBuffer.append(Character.valueOf((char) unicodeChar));
        }

        return result;
    }
}
