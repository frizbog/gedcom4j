package org.gedcom4j.writer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.OutputStream;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.exception.WriterCancelledException;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.writer.event.ConstructProgressEvent;
import org.gedcom4j.writer.event.ConstructProgressListener;
import org.junit.Test;

/**
 * Test the ability to receive notifications about GEDCOM string construction, and to cancel the operation
 * 
 * @author frizbog
 */
public class GedcomWriterConstructionProgressAndCancellationTest implements ConstructProgressListener {

    /**
     * Inner class that ignores what's written to it
     */
    class NullOutputStream extends OutputStream {

        /**
         * {@inheritDoc}
         */
        @Override
        public void write(int b) throws IOException {
            ; // Do nothing
        }

    }

    /**
     * Number of notifications received
     */
    private int notificationCount = 0;

    /**
     * How many notifications to cancel after
     */
    private int cancelAfter = Integer.MAX_VALUE;

    /**
     * The GedcomWriter we're testing with
     */
    private GedcomWriter gw;

    @Override
    public void progressNotification(ConstructProgressEvent e) {
        notificationCount++;
        if (notificationCount >= cancelAfter) {
            gw.cancel();
        }
    }

    /**
     * Test with cancelling after getting a couple notifications
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if the file can't be parsed
     * @throws GedcomWriterException
     *             if the file can't be written (or is cancelled)
     */
    @Test(expected = WriterCancelledException.class)
    public void testCancellation() throws IOException, GedcomParserException, GedcomWriterException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/willis-ascii.ged");
        gw = new GedcomWriter(gp.gedcom);
        gw.registerConstructObserver(this);
        cancelAfter = 5;
        gw.write(new NullOutputStream());
    }

    /**
     * Test without cancelling
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if the file can't be parsed
     * @throws GedcomWriterException
     *             if the file can't be written (or is cancelled)
     */
    @Test
    public void testNoCancellation() throws IOException, GedcomParserException, GedcomWriterException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/willis-ascii.ged");
        gw = new GedcomWriter(gp.gedcom);
        gw.registerConstructObserver(this);
        gw.write(new NullOutputStream());
        assertEquals(38, notificationCount);
    }

}
