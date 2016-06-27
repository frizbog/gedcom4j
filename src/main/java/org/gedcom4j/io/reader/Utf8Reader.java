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
import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.io.event.FileProgressEvent;
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
     * Constructor
     * 
     * @param parser
     *            the {@link GedcomParser} which is using this object to read files
     * 
     * @param byteStream
     *            the stream of data to read
     */
    protected Utf8Reader(GedcomParser parser, InputStream byteStream) {
        super(parser, byteStream);
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
    protected List<String> load() throws IOException {
        List<String> result = new ArrayList<String>();
        InputStreamReader r = null;
        BufferedReader br = null;
        try {
            r = new InputStreamReader(byteStream, "UTF8");

            if (byteOrderMarkerRead) {
                // discard the byte order marker if one was detected
                r.read();
            }

            br = new BufferedReader(r);
            String s = br.readLine();
            while (s != null) {
                if (s.length() != 0) {
                    result.add(s);
                }
                linesRead++;
                if (linesRead % 100 == 0) {
                    parser.notifyObservers(new FileProgressEvent(this, linesRead, false));
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

        parser.notifyObservers(new FileProgressEvent(this, linesRead, true));
        return result;
    }

}
