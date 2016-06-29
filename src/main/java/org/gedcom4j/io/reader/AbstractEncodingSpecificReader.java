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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.parser.GedcomParser;

/**
 * A base class for the various kinds of readers needed based on the encoding used for the data
 * 
 * @author frizbog
 */
abstract class AbstractEncodingSpecificReader {
    /**
     * The stream of bytes to read
     */
    protected final InputStream byteStream;

    /**
     * The number of lines read from the input file
     */
    protected int linesRead = 0;

    /**
     * Whether the file has been completely read
     */
    protected boolean complete = false;

    /**
     * The {@link GedcomParser} we're reading files for
     */
    protected final GedcomParser parser;

    /**
     * Constructor.
     * 
     * @param parser
     *            The {@link GedcomParser} we're reading files for
     * 
     * @param byteStream
     *            the stream of bytes to read
     */
    protected AbstractEncodingSpecificReader(GedcomParser parser, InputStream byteStream) {
        this.parser = parser;
        this.byteStream = byteStream;
    }

    /**
     * Indicate that file loading should be cancelled
     */
    public void cancel() {
        parser.cancel();
    }

    /**
     * Read all the lines using the appropriate encoding
     * 
     * @return all the lines of the input stream
     * @throws IOException
     *             if there is a problem reading the bytes
     * @throws GedcomParserException
     *             if the file load is cancelled or fails
     */
    protected abstract List<String> load() throws IOException, GedcomParserException;
}
