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
package org.gedcom4j.io.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.parser.GedcomParser;

/**
 * A reader that loads from an input stream and gives back a collection of strings representing the data therein. This
 * implementation handles UTF-8 data
 * 
 * @author frizbog
 */
class Utf8Reader extends AbstractEncodingSpecificReader {

    /**
     * Was a byte order marker read when inspecting the file to detect encoding?
     */
    private boolean byteOrderMarkerRead;

    /**
     * Input stream reader for internal use over the byte stream
     */
    private InputStreamReader inputStreamReader;

    /**
     * Buffered reader over the input stream reader, for interanl use
     */
    private BufferedReader bufferedReader;

    /**
     * Constructor
     * 
     * @param parser
     *            the {@link GedcomParser} which is using this object to read files
     * 
     * @param byteStream
     *            the stream of data to read
     * @throws IOException
     *             if there's a problem reading the data
     */
    Utf8Reader(GedcomParser parser, InputStream byteStream) throws IOException {
        super(parser, byteStream);
        inputStreamReader = null;
        bufferedReader = null;
        try {
            inputStreamReader = new InputStreamReader(byteStream, "UTF8");

            if (byteOrderMarkerRead) {
                // discard the byte order marker if one was detected
                inputStreamReader.read();
            }

            bufferedReader = new BufferedReader(inputStreamReader);
        } catch (IOException e) {
            cleanUp();
            throw e;
        }
    }

    @Override
    public String nextLine() throws IOException, GedcomParserException {
        String result = null;
        String s = bufferedReader.readLine();
        while (s != null) {
            if (s.length() != 0) {
                result = s;
                break;
            }
            s = bufferedReader.readLine();
        }
        return result;
    }

    /**
     * Set whether or not a byte order marker was read when inspecting the file to detect its encoding
     * 
     * @param wasByteOrderMarkerRead
     *            true if a byte order marker was read
     */
    public void setByteOrderMarkerRead(boolean wasByteOrderMarkerRead) {
        byteOrderMarkerRead = wasByteOrderMarkerRead;
    }

    @Override
    void cleanUp() throws IOException {
        if (bufferedReader != null) {
            bufferedReader.close();
        }
        if (inputStreamReader != null) {
            inputStreamReader.close();
        }
    }

}
