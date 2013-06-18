package org.gedcom4j.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

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
     * Constructor.
     * 
     * @param byteStream
     *            the stream of bytes to read
     */
    protected AbstractEncodingSpecificReader(InputStream byteStream) {
        this.byteStream = byteStream;
    }

    /**
     * Read all the lines using the appropriate encoding
     * 
     * @return all the lines of the input stream
     * @throws IOException
     *             if there is a problem reading the bytes
     */
    protected abstract Collection<? extends String> load() throws IOException;
}
