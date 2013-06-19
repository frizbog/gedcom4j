package org.gedcom4j.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A reader that loads from an input stream and gives back a collection of strings representing the data therein. This
 * implementation handles UTF-8 data
 * 
 * @author frizbog
 */

public class Utf8Reader extends AbstractEncodingSpecificReader {

    /**
     * Was a byte order marker read when inspecting the file to detect encoding?
     */
    private boolean byteOrderMarkerRead;

    /**
     * Constructor
     * 
     * @param byteStream
     *            the stream of data to read
     */
    protected Utf8Reader(InputStream byteStream) {
        super(byteStream);
    }

    /**
     * Set whether or not a byte order marker was read when inspecting the file to detect its encoding
     * 
     * @param byteOrderMarkerRead
     *            true if a byte order marker was read
     */
    public void setByteOrderMarkerRead(boolean byteOrderMarkerRead) {
        this.byteOrderMarkerRead = byteOrderMarkerRead;
    }

    @Override
    protected Collection<? extends String> load() throws IOException {
        List<String> result = new ArrayList<String>();
        InputStreamReader r = null;
        BufferedReader br = null;
        try {
            r = new InputStreamReader(byteStream, "UTF8");

            if (byteOrderMarkerRead) {
                // discard the byte order marker if one was detected
                r.read();
            }

            br = new BufferedReader(r);
            String s = br.readLine();
            while (s != null) {
                if (s.length() != 0) {
                    result.add(s);
                }
                s = br.readLine();
            }
        } finally {
            if (br != null) {
                br.close();
            }
            if (r != null) {
                r.close();
            }
            if (byteStream != null) {
                byteStream.close();
            }
        }

        return result;
    }

}
