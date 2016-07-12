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
package org.gedcom4j.io.reader;

import java.io.IOException;
import java.io.InputStream;

/**
 * An {@link InputStream} Decorator that tracks and exposes the number of bytes read
 * 
 * @author frizbog
 */
class ProgressTrackingInputStream extends InputStream {

    /**
     * The input stream being decorated
     */
    private final InputStream in;

    /**
     * The number of bytes read on this input stream
     */
    private int bytesRead = 0;

    /**
     * Constructor
     * 
     * @param byteStream
     *            the input stream to be decorated
     */
    ProgressTrackingInputStream(InputStream byteStream) {
        in = byteStream;
    }

    @Override
    public int read() throws IOException {
        int r = in.read();
        if (r >= 0) {
            bytesRead++;
        }
        return r;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int r = in.read(b);
        if (r > 0) {
            bytesRead += r;
        }
        return r;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int r = in.read(b, off, len);
        if (r > 0) {
            bytesRead += r;
        }
        return r;
    }

    /**
     * Get the bytesRead
     * 
     * @return the bytesRead
     */
    int getBytesRead() {
        return bytesRead;
    }

}
