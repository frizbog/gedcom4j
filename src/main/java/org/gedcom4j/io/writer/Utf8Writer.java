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
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.gedcom4j.exception.WriterCancelledException;
import org.gedcom4j.io.event.FileProgressEvent;
import org.gedcom4j.writer.GedcomWriter;

/**
 * A class that writes GEDCOM data in UTF-8 format.
 * 
 * @author frizbog
 */
class Utf8Writer extends AbstractEncodingSpecificWriter {

    /**
     * The number of lines written
     */
    private int lineCount;

    /**
     * Constructor
     * 
     * @param writer
     *            The {@link GedcomWriter} this object is assisting
     */
    public Utf8Writer(GedcomWriter writer) {
        super(writer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(OutputStream out) throws IOException, WriterCancelledException {
        String lineTerminatorString = null;
        switch (terminator) {
            case CR_ONLY:
                lineTerminatorString = "\r";
                break;
            case LF_ONLY:
                lineTerminatorString = "\n";
                break;
            case LFCR:
                lineTerminatorString = "\n\r";
                break;
            case CRLF:
                lineTerminatorString = "\r\n";
                break;
            default:
                throw new IllegalStateException("Terminator selection of " + terminator + " is an unrecognized value");
        }

        // Go ahead and use Java's built in UTF-8 encoder here
        ProgressTrackingOutputStream outputStream = new ProgressTrackingOutputStream(out);
        try (OutputStreamWriter osw = new OutputStreamWriter(outputStream, Charset.forName("UTF-8"))) {
            for (String line : gedcomLines) {
                osw.write(line);
                bytesWritten = outputStream.bytesWritten;
                osw.write(lineTerminatorString);
                bytesWritten = outputStream.bytesWritten;
                lineCount++;
                if (lineCount >= notifyAfterThisManyLines) {
                    writer.notifyFileObservers(new FileProgressEvent(this, lineCount, bytesWritten, false));
                    notifyAfterThisManyLines += writer.getFileNotificationRate();
                }

                if (writer.isCancelled()) {
                    throw new WriterCancelledException("Construction and writing of GEDCOM cancelled");
                }
            }
            osw.flush();
            bytesWritten = outputStream.bytesWritten;
        }
    }

    /**
     * <p>
     * Note: This writer implementation does not use the writeLine or writeLineTerminator methods.
     * </p>
     * {@inheritDoc}
     */
    @Override
    protected void writeLine(OutputStream out, String line) throws IOException {
        // Not used
        throw new UnsupportedOperationException(this.getClass().getName() + " does not use the abstract writeLine method");
    }

    /**
     * <p>
     * Note: This writer implementation does not use the writeLine or writeLineTerminator methods.
     * </p>
     * {@inheritDoc}
     */
    @Override
    protected void writeLineTerminator(OutputStream out) throws IOException {
        // Not used
        throw new UnsupportedOperationException(this.getClass().getName()
                + " does not use the abstract writeLineTerminator method");
    }

}
