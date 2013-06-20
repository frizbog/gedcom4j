package org.gedcom4j.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


/**
 * A base class for encoding-specific writer classes.
 * 
 * @author frizbog
 */
abstract class AbstractEncodingSpecificWriter {

    /**
     * The lines of GEDCOM data to write
     */
    protected List<String> gedcomLines;

    /**
     * The line terminator character to use - defaults to JVM settings but can be overridden
     */
    protected LineTerminator terminator;

    /**
     * Write the gedcom lines to an output stream, encoding as needed
     * 
     * @param out
     *            the output stream
     * @throws IOException
     *             if the data can't be written to the stream
     */
    public void write(OutputStream out) throws IOException {
        for (String line : gedcomLines) {
            writeLine(out, line);
        }
    }

    /**
     * Write data out as lines of text using the appropriate encoding.
     * 
     * @param out
     *            the output stream we're writing to
     * @param line
     *            the line of text we're writing
     * @throws IOException
     *             if the data can't be written to the stream
     */
    protected abstract void writeLine(OutputStream out, String line) throws IOException;

    /**
     * Write out the appropriate line terminator based on the encoding and terminator selection for this instance
     * 
     * @param out
     *            the output stream we're writing to
     * @throws IOException
     *             if the line terminator can't be written to the stream
     */
    protected abstract void writeLineTerminator(OutputStream out) throws IOException;
}
