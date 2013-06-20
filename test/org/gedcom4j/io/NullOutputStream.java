package org.gedcom4j.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An output stream, for testing purposes only, that ignores all data sent to it
 * 
 * @author frizbog
 */
public class NullOutputStream extends OutputStream {

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(int b) throws IOException {
        ; // Do nothing
    }

}
