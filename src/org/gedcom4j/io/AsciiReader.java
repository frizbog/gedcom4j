package org.gedcom4j.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A reader that loads from an input stream and gives back a collection of strings representing the data therein. This
 * implementation handles ASCII encoding (1 byte per character, no extended character support).
 * 
 * @author frizbog
 */
class AsciiReader extends AbstractEncodingSpecificReader {

    /**
     * Constructor
     * 
     * @param byteStream
     *            stream of data to read
     */
    protected AsciiReader(InputStream byteStream) {
        super(byteStream);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<? extends String> load() throws IOException {
        List<String> result = new ArrayList<String>();
        StringBuilder lineBuffer = new StringBuilder();

        int lastChar = -1;
        int b = -1;
        boolean eof = false;

        while (!eof) {
            lastChar = b;
            b = byteStream.read();

            // Check for EOF
            if (b < 0) {
                // hit EOF - add final line buffer (last line) and get out
                if (lineBuffer.length() > 0) {
                    result.add(lineBuffer.toString());
                }
                eof = true;
                break;
            }

            // Check for carriage returns - signify EOL
            if (b == 0x0D) {
                if (lineBuffer.length() > 0) {
                    result.add(lineBuffer.toString());
                }
                lineBuffer.setLength(0);
                continue;
            }

            // Check for line feeds - signify EOL (unless prev char was a
            // CR)
            if (b == 0x0A) {
                if (lastChar != 0x0D) {
                    if (lineBuffer.length() > 0) {
                        result.add(lineBuffer.toString());
                    }
                    lineBuffer.setLength(0);
                }
                continue;
            }

            // All other characters in 0x00 to 0x7F range are treated the
            // same,
            // regardless of encoding, and added as is
            if (b < 0x80) {
                lineBuffer.append(Character.valueOf((char) b));
                continue;
            }

            // If we fell through to here, we have an extended character
            throw new IOException("Extended characters not supported in ASCII: 0x" + Integer.toHexString(b));
        }
        return result;
    }

}
