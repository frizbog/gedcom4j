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
package org.gedcom4j.io.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.gedcom4j.exception.WriterCancelledException;
import org.gedcom4j.io.event.FileProgressEvent;
import org.gedcom4j.writer.GedcomWriter;

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
    protected LineTerminator terminator = LineTerminator.getDefaultLineTerminator();

    /**
     * The {@link GedcomWriter} this object is assisting
     */
    protected GedcomWriter writer;

    /**
     * The number of bytes written
     */
    protected int bytesWritten;

    /**
     * When we've exceeded this many line written, notify the listeners and update this value based on the rate
     */
    protected int notifyAfterThisManyLines = 0;

    /**
     * Constructor
     * 
     * @param writer
     *            The {@link GedcomWriter} this object is assisting
     */
    public AbstractEncodingSpecificWriter(GedcomWriter writer) {
        this.writer = writer;
    }

    /**
     * Write the gedcom lines to an output stream, encoding as needed
     * 
     * @param out
     *            the output stream
     * @throws IOException
     *             if the data can't be written to the stream
     * @throws WriterCancelledException
     *             if the write operation was cancelled
     */
    public void write(OutputStream out) throws IOException, WriterCancelledException {
        int lineCount = 0;
        for (String line : gedcomLines) {
            if (lineCount >= notifyAfterThisManyLines) {
                writer.notifyFileObservers(new FileProgressEvent(this, lineCount, bytesWritten, false));
                notifyAfterThisManyLines += writer.getFileNotificationRate();
            }
            writeLine(out, line);
            lineCount++;
        }
        writer.notifyFileObservers(new FileProgressEvent(this, lineCount, bytesWritten, true));
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
     * @throws WriterCancelledException
     *             if the write operation was cancelled
     */
    protected abstract void writeLine(OutputStream out, String line) throws IOException, WriterCancelledException;

    /**
     * Write out the appropriate line terminator based on the encoding and terminator selection for this instance
     * 
     * @param out
     *            the output stream we're writing to
     * @throws IOException
     *             if the line terminator can't be written to the stream
     * @throws WriterCancelledException
     *             if the write operation was cancelled
     */
    protected abstract void writeLineTerminator(OutputStream out) throws IOException, WriterCancelledException;
}
