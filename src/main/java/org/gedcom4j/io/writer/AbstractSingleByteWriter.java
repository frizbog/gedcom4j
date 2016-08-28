package org.gedcom4j.io.writer;

import java.io.IOException;
import java.io.OutputStream;

import org.gedcom4j.exception.WriterCancelledException;
import org.gedcom4j.writer.GedcomWriter;

/**
 * A writer that uses a single-byte character set (i.e., ANSEL and ASCII)
 * 
 * @author frizbog
 */
public abstract class AbstractSingleByteWriter extends AbstractEncodingSpecificWriter {

    /**
     * Constructor
     * 
     * @param writer
     *            The {@link GedcomWriter} this object is assisting
     */
    public AbstractSingleByteWriter(GedcomWriter writer) {
        super(writer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeLineTerminator(OutputStream out) throws IOException, WriterCancelledException {
        switch (terminator) {
            case CR_ONLY:
                out.write((byte) 0x0D);
                bytesWritten++;
                break;
            case LF_ONLY:
                out.write((byte) 0x0A);
                bytesWritten++;
                break;
            case LFCR:
                out.write((byte) 0x0A);
                out.write((byte) 0x0D);
                bytesWritten += 2;
                break;
            case CRLF:
                out.write((byte) 0x0D);
                out.write((byte) 0x0A);
                bytesWritten += 2;
                break;
            default:
                throw new IllegalStateException("Terminator selection of " + terminator + " is an unrecognized value");
        }
        if (writer.isCancelled()) {
            throw new WriterCancelledException("Construction and writing of GEDCOM cancelled");
        }
    }

}