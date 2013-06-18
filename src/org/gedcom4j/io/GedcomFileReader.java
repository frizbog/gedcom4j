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

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for reading the gedcom files and handling ASCII, ANSEL, and UNICODE coding as needed. It's basic job is to
 * turn the bytes from the file into a buffer (a {@link List} of Strings) that the
 * {@link org.gedcom4j.parser.GedcomParser} can work with. This class is needed because the built-in character encodings
 * in Java don't support ANSEL encoding, which is the default encoding for gedcom files in v5.5 standard.
 * 
 * @author frizbog1
 */
public class GedcomFileReader {

    /**
     * The size of the first chunk of the GEDCOM to just load into memory for easy review. 16K.
     */
    private static final int FIRST_CHUNK_SIZE = 16384;

    /**
     * The encoding that this reader is using
     */
    private Encoding encoding;

    /**
     * Was a byte order marker used to indicate UTF-8? This is important because the three bytes that make up the byte
     * order marker need to be discarded
     */
    private boolean byteOrderMarker = false;

    /**
     * The first chunk of the file
     */
    private final byte[] firstChunk = new byte[FIRST_CHUNK_SIZE];

    /**
     * The buffered input stream of bytes to read
     */
    private final BufferedInputStream byteStream;

    /**
     * Constructor
     * 
     * @param bufferedInputStream
     *            the buffered input stream of bytes
     */
    public GedcomFileReader(BufferedInputStream bufferedInputStream) {
        byteStream = bufferedInputStream;
    }

    /**
     * Get the gedcom file as a list of string
     * 
     * @return a <code>List</code> of <code>String</code> objects, each of which represents a line of the input file
     *         represented by the byte stream
     * @throws IOException
     *             if there is a problem reading the data
     */
    public List<String> getLines() throws IOException {

        saveFirstChunk(byteStream);

        try {
            getEncodingSpecificReader();
        } catch (UnsupportedGedcomCharsetException e) {
            throw new IOException("Unable to parse GEDCOM data - " + e.getMessage(), e);
        }

        List<String> result = new ArrayList<String>();
        switch (encoding) {
            case ANSEL:
                result.addAll(new AnselReader(byteStream).load());
                break;
            case ASCII:
                result.addAll(new AsciiReader(byteStream).load());
                break;
            case UNICODE_BIG_ENDIAN:
                result.addAll(new UnicodeBigEndianReader(byteStream).load());
                break;
            case UNICODE_LITTLE_ENDIAN:
                result.addAll(new UnicodeLittleEndianReader(byteStream).load());
                break;
            case UTF_8:
                Utf8Reader utf8Reader = new Utf8Reader(byteStream);
                utf8Reader.setByteOrderMarkerFlag(byteOrderMarker);
                result.addAll(utf8Reader.load());
                break;
            default:
                throw new IllegalStateException("Unknown encoding " + encoding);
        }

        return result;
    }

    /**
     * Tries to determined from examining the first 1000 lines/2k of the file if the file is ASCII, ANSEL, or UTF-8
     * encoded using a variety of means.
     * 
     * @return which encoding we think it is
     * @throws IOException
     *             if we have trouble previewing the first 2k of the file
     * @throws UnsupportedGedcomCharsetException
     *             if the encoding cannot be narrowed down to Ansel, ASCII, or UTF-8. This could be caused by a number
     *             of things:
     *             <ul>
     *             <li>Illegal value for the CHAR tag</li>
     *             <li>No CHAR tag was found within the first 2k or so of the file</li>
     *             </ul>
     */
    private Encoding anselAsciiOrUtf8() throws IOException, UnsupportedGedcomCharsetException {
        // Default
        Encoding result = Encoding.ANSEL;

        /*
         * Try reading as UTF-8. Most likely to successfully read and be useful for figuring out what the encoding
         * really is
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
                         * Technically, this is illegal, but UTF_8 is the most-likely-to-work scenario, so let's try it
                         * and be a bit forgiving
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
     * Determine the encoding of the byte stream by examining its first two characters, in order to determine how many
     * bytes are to be read for each character.
     * </p>
     * 
     * @throws IOException
     *             if there is a problem reading the byte stream
     * @throws UnsupportedGedcomCharsetException
     *             if a suitable charset encoding is not found.
     */
    private void getEncodingSpecificReader() throws IOException, UnsupportedGedcomCharsetException {

        if (firstChunk[0] == (byte) 0xEF && firstChunk[1] == (byte) 0xBB && firstChunk[2] == (byte) 0xBF) {
            /*
             * Special byte order markers to indicate UTF-8 encoding. Not every program does this, but if it does, we
             * KNOW it's UTF-8 and should discard the BOM
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
     * Save off a chunk of the beginning of the input stream to memory for easy inspection. The data is loaded into the
     * field
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
