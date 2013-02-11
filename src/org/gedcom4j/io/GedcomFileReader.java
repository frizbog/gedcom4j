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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A class for reading the gedcom files and handling ASCII, ANSEL, and UNICODE
 * coding as needed. It's basic job is to turn the bytes from the file into a
 * buffer (a {@link List} of Strings) that the
 * {@link org.gedcom4j.parser.GedcomParser} can work with. This class is needed
 * because the built-in character encodings in Java don't support ANSEL
 * encoding, which is the default encoding for gedcom files in v5.5 standard.
 * 
 * @author frizbog1
 */
public class GedcomFileReader {
    /**
     * The size of the first chunk of the GEDCOM to just load into memory for
     * easy review. 16K.
     */
    private static final int FIRST_CHUNK_SIZE = 16384;

    /**
     * The encoding that this reader is using
     */
    private Encoding encoding;

    /**
     * Was a byte order marker used to indicate UTF-8? This is important because
     * the three bytes that make up the byte order marker need to be discarded
     */
    private boolean byteOrderMarker = false;

    /**
     * The first chunk of the file
     */
    private final byte[] firstChunk = new byte[FIRST_CHUNK_SIZE];

    /**
     * Get the gedcom file as a list of string
     * 
     * @param byteStream
     *            an <code>BufferedInputStream</code> from which this class gets
     *            the bytes of the file
     * @return a <code>List</code> of <code>String</code> objects, each of which
     *         represents a line of the input file represented by the byte
     *         stream
     * @throws IOException
     *             if there is a problem reading the data
     */
    public List<String> getLines(BufferedInputStream byteStream) throws IOException {

        saveFirstChunk(byteStream);

        List<String> result = new ArrayList<String>();

        try {
            detectEncoding();
        } catch (UnsupportedGedcomCharsetException e) {
            throw new IOException("Unable to parse GEDCOM data - " + e.getMessage());
        }

        switch (encoding) {
        case ANSEL:
        case ASCII:
            result.addAll(loadAnselAscii(byteStream));
            break;
        case UNICODE_BIG_ENDIAN:
            result.addAll(loadUnicodeBigEndian(byteStream));
            break;
        case UNICODE_LITTLE_ENDIAN:
            result.addAll(loadUnicodeLittleEndian(byteStream));
            break;
        case UTF_8:
            result.addAll(loadUtf8(byteStream));
            break;
        default:
            throw new IllegalStateException("Unknown encoding " + encoding);
        }
        return result;
    }

    /**
     * Tries to determined from examining the first 1000 lines/2k of the file if
     * the file is ASCII, ANSEL, or UTF-8 encoded using a variety of means.
     * 
     * @return which encoding we think it is
     * @throws IOException
     *             if we have trouble previewing the first 2k of the file
     * @throws UnsupportedGedcomCharsetException
     *             if the encoding cannot be narrowed down to Ansel, ASCII, or
     *             UTF-8. This could be caused by a number of things:
     *             <ul>
     *             <li>Illegal value for the CHAR tag</li>
     *             <li>No CHAR tag was found within the first 2k or so of the
     *             file</li>
     *             </ul>
     */
    private Encoding anselAsciiOrUtf8() throws IOException, UnsupportedGedcomCharsetException {
        // Default
        Encoding result = Encoding.ANSEL;

        /*
         * Try reading as UTF-8. Most likely to successfully read and be useful
         * for figuring out what the encoding really is
         */
        BufferedReader r = null;
        try {
            r = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(firstChunk), "UTF8"));
            String s = r.readLine();
            int lineCount = 1;
            while (lineCount < 1000 && s != null) {
                lineCount++;
                if (s.startsWith("1 CHAR ")) {
                    String e = s.substring("1 CHAR ".length());
                    if ("ANSEL".equalsIgnoreCase(e)) {
                        result = Encoding.ANSEL;
                    } else if ("UTF-8".equalsIgnoreCase(e)) {
                        result = Encoding.UTF_8;
                    } else if ("ASCII".equalsIgnoreCase(e)) {
                        result = Encoding.ASCII;
                    } else if ("ANSI".equalsIgnoreCase(e)) {
                        /*
                         * Technically, this is illegal, but UTF_8 is the
                         * most-likely-to-work scenario, so let's try it and be
                         * a bit forgiving
                         */
                        result = Encoding.UTF_8;
                    } else {
                        throw new UnsupportedGedcomCharsetException("Specified charset " + e
                                + " is not a supported charset encoding for GEDCOMs");
                    }
                }
                s = r.readLine();
            }

        } finally {
            if (r != null) {
                r.close();
            }
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
     * @throws IOException
     *             if there is a problem reading the byte stream
     * @throws UnsupportedGedcomCharsetException
     *             if a suitable charset encoding is not found.
     */
    private void detectEncoding() throws IOException, UnsupportedGedcomCharsetException {

        if (firstChunk[0] == (byte) 0xEF && firstChunk[1] == (byte) 0xBB && firstChunk[2] == (byte) 0xBF) {
            /*
             * Special byte order markers to indicate UTF-8 encoding. Not every
             * program does this, but if it does, we KNOW it's UTF-8 and should
             * discard the BOM
             */
            encoding = Encoding.UTF_8;
            byteOrderMarker = true;
            return;
        }

        if (firstChunk[0] == 0x30 && firstChunk[1] == 0x00) {
            // If the first two firstChunk make up a single zero character, it's
            // unicode
            encoding = Encoding.UNICODE_LITTLE_ENDIAN;
        } else if (firstChunk[0] == 0x00 && firstChunk[1] == 0x30) {
            // If the first two firstChunk make up a single zero character, it's
            // unicode
            encoding = Encoding.UNICODE_BIG_ENDIAN;
        } else if (firstChunk[0] == 0x30 && firstChunk[1] == 0x20) {
            /*
             * Could be ANSEL, ASCII, or UTF-8.
             */
            encoding = anselAsciiOrUtf8();
        } else {
            throw new IOException("Does not appear to be a valid gedcom file - "
                    + "doesn't begin with a zero in any supported encoding, "
                    + "and does not begin with a BOM marker for UTF-8 encoding");
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
    private Collection<? extends String> loadAnselAscii(InputStream byteStream) throws IOException {
        List<String> result = new ArrayList<String>();
        StringBuilder lineBuffer = new StringBuilder();

        int lastChar = -1;
        int b = -1;
        boolean eof = false;

        InputStream fis = byteStream;
        try {
            while (!eof) {
                lastChar = b;
                b = fis.read();

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

                // Check for line feeds - signify EOL (unless prev char was a
                // CR)
                if (b == 0x0A) {
                    if (lastChar != 0x0D) {
                        result.add(lineBuffer.toString());
                        lineBuffer = new StringBuilder();
                    }
                    continue;
                }

                // All other characters in 0x00 to 0x7F range are treated the
                // same,
                // regardless of encoding, and added as is
                if (b < 0x80) {
                    lineBuffer.append(Character.valueOf((char) b));
                    continue;
                }

                // Hit an extended character. ASCII doesn't support above 0x7F
                if (encoding == Encoding.ASCII) {
                    throw new IOException("Extended characters not supported in ASCII: 0x" + Integer.toHexString(b));
                }

                lineBuffer.append(AnselMapping.decode(b));
                continue;

            }
        } finally {
            if (fis != null) {
                fis.close();
            }
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
     *             if the data can't be loaded from the stream
     */
    private Collection<? extends String> loadUnicodeBigEndian(InputStream byteStream) throws IOException {
        List<String> result = new ArrayList<String>();

        StringBuilder lineBuffer = new StringBuilder();

        boolean eof = false;
        int b1 = -1;
        int b2 = -1;

        int lastB1;
        int lastB2;

        InputStream fis = null;
        try {
            fis = byteStream;
            while (!eof) {
                lastB1 = b1;
                lastB2 = b2;
                b1 = fis.read();
                b2 = fis.read();

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

                // Check for line feeds - signify EOL (unless prev char was a
                // CR)
                if (b1 == 0x00 && b2 == 0x0A) {
                    if (lastB1 != 0x00 || lastB2 != 0x0D) {
                        result.add(lineBuffer.toString());
                        lineBuffer = new StringBuilder();
                    }
                    continue;
                }

                int unicodeChar = b1 << 8 | b2;
                lineBuffer.append(Character.valueOf((char) unicodeChar));
            }
        } finally {
            if (fis != null) {
                fis.close();
            }
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
    private Collection<? extends String> loadUnicodeLittleEndian(InputStream byteStream) throws IOException {
        List<String> result = new ArrayList<String>();

        StringBuilder lineBuffer = new StringBuilder();

        boolean eof = false;
        int b1 = -1;
        int b2 = -1;

        int lastB1;
        int lastB2;

        InputStream fis = null;
        try {
            fis = byteStream;
            while (!eof) {
                lastB1 = b1;
                lastB2 = b2;
                b1 = fis.read();
                b2 = fis.read();

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

                // Check for line feeds - signify EOL (unless prev char was a
                // CR)
                if (b1 == 0x0A && b2 == 0x00) {
                    if (lastB1 != 0x0D || lastB2 != 0x00) {
                        result.add(lineBuffer.toString());
                        lineBuffer = new StringBuilder();
                    }
                    continue;
                }

                int unicodeChar = b2 << 8 | b1;
                lineBuffer.append(Character.valueOf((char) unicodeChar));
            }
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

        return result;
    }

    /**
     * Read all the lines using UTF-8 character encoding.
     * 
     * @param byteStream
     *            the input stream of bytes
     * @return all the lines of the input stream
     * @throws IOException
     *             if there is a problem reading the bytes
     */
    private Collection<? extends String> loadUtf8(InputStream byteStream) throws IOException {
        List<String> result = new ArrayList<String>();
        InputStreamReader r = null;
        BufferedReader br = null;
        try {
            r = new InputStreamReader(byteStream, "UTF8");

            if (byteOrderMarker) {
                // discard the byte order marker if one was detected
                r.read();
            }

            br = new BufferedReader(r);
            String s = br.readLine();
            while (s != null) {
                if (s.length() != 0) {
                    result.add(s);
                }
                s = br.readLine();
            }
        } finally {
            if (br != null) {
                br.close();
            }
            if (r != null) {
                r.close();
            }
            if (byteStream != null) {
                byteStream.close();
            }
        }

        return result;
    }

    /**
     * Save off a chunk of the beginning of the input stream to memory for easy
     * inspection. The data is loaded into the field
     * 
     * @param byteStream
     *            the stream of bytes
     * @throws IOException
     *             if the stream of bytes cannot be read.
     */
    private void saveFirstChunk(InputStream byteStream) throws IOException {
        byteStream.mark(FIRST_CHUNK_SIZE);
        int read = byteStream.read(firstChunk);
        if (read < 0) {
            throw new IOException("Unable to read bytes off stream");
        }
        byteStream.reset();
    }

}
