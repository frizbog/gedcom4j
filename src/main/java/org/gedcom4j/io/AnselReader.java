package org.gedcom4j.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A reader that loads from an input stream and gives back a collection of strings representing the data therein. This
 * implementation handles ANSEL encoding (1 byte per character, some extended character support).
 * 
 * @author frizbog
 */
class AnselReader extends AbstractEncodingSpecificReader {

    /**
     * Helper class
     */
    AnselDiacriticalHandler anselDiacriticalHandler = new AnselDiacriticalHandler();

    /**
     * Constructor
     * 
     * @param byteStream
     *            the stream of data being read
     */
    protected AnselReader(InputStream byteStream) {
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
                addNonBlankLine(result, lineBuffer);
                eof = true;
                break;
            }

            // Check for carriage returns - signify EOL
            if (b == 0x0D) {
                addNonBlankLine(result, lineBuffer);
                lineBuffer.setLength(0);
                continue;
            }

            // Check for line feeds - signify EOL (unless prev char was a
            // CR)
            if (b == 0x0A) {
                if (lastChar != 0x0D) {
                    addNonBlankLine(result, lineBuffer);
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

            lineBuffer.append(AnselMapping.decode(b));
        }
        return result;
    }

    /**
     * Add line to result if it is not blank
     * 
     * @param result
     *            the resulting list of lines
     * @param lineBuffer
     *            the line buffer
     */
    private void addNonBlankLine(List<String> result, StringBuilder lineBuffer) {
        if (lineBuffer.length() > 0) {
            result.add(lineBuffer.toString());
        }
    }

}
