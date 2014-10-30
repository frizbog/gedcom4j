package org.gedcom4j.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A reader that loads from an input stream and gives back a collection of strings representing the data therein. This
 * implementation handles little-endian Unicode data.
 * 
 * @author frizbog
 */
class UnicodeLittleEndianReader extends AbstractEncodingSpecificReader {

    /**
     * Constructor
     * 
     * @param byteStream
     *            the stream of data to be read
     */
    public UnicodeLittleEndianReader(InputStream byteStream) {
        super(byteStream);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<? extends String> load() throws IOException {
        List<String> result = new ArrayList<String>();

        StringBuilder lineBuffer = new StringBuilder();

        boolean eof = false;
        int b1 = -1;
        int b2 = -1;

        int lastB1;
        int lastB2;

        while (!eof) {
            lastB1 = b1;
            lastB2 = b2;
            b1 = byteStream.read();
            b2 = byteStream.read();

            // Check for EOF
            if (b1 < 0 || b2 < 0) {
                // hit EOF - add final line buffer (last line) and get out
                if (lineBuffer.length() > 0) {
                    result.add(lineBuffer.toString());
                }
                eof = true;
                break;
            }

            // Check for carriage returns - signify EOL
            if (b1 == 0x0D && b2 == 0x00) {
                if (lineBuffer.length() > 0) {
                    result.add(lineBuffer.toString());
                }
                lineBuffer.setLength(0);
                continue;
            }

            // Check for line feeds - signify EOL (unless prev char was a
            // CR)
            if (b1 == 0x0A && b2 == 0x00) {
                if (lastB1 != 0x0D || lastB2 != 0x00) {
                    if (lineBuffer.length() > 0) {
                        result.add(lineBuffer.toString());
                    }
                    lineBuffer.setLength(0);
                }
                continue;
            }

            int unicodeChar = b2 << 8 | b1;
            lineBuffer.append(Character.valueOf((char) unicodeChar));
        }

        return result;
    }

}
