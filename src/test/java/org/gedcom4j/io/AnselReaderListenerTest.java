package org.gedcom4j.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.parser.GedcomParserException;
import org.junit.Test;

/**
 * Test getting listener notifications from {@link AnselReader}
 * 
 * @author frizbog
 */
public class AnselReaderListenerTest implements FileProgressListener {

    /**
     * The number of event notifications received
     */
    private int eventCount;

    /**
     * The last event received
     */
    private FileProgressEvent lastEvent;

    @Override
    public void progressNotification(FileProgressEvent e) {
        System.out.println(e);

    }

    /**
     * Test when you've never registered as a listener
     * 
     * @throws IOException
     *             if the file can't be loaded
     * @throws GedcomParserException
     *             if the file can't be parsed
     */
    @Test
    public void testNotRegistered() throws IOException, GedcomParserException {
        eventCount = 0;
        GedcomParser gp = new GedcomParser();
        gp.load("sample/willis-ansel.ged");
        assertNotNull(gp.gedcom);
        assertEquals(0, eventCount);
        assertNull(lastEvent);
    }

    @Test
    /**
     * Test when you've never registered as a listener
     * 
     * @throws IOException
     *             if the file can't be loaded
     * @throws GedcomParserException
     *             if the file can't be parsed
     */
    public void testRegistered() throws IOException, GedcomParserException {
        eventCount = 0;
        GedcomParser gp = new GedcomParser();
        gp.load("sample/willis-ansel.ged");
        assertNotNull(gp.gedcom);
        assertEquals(7, eventCount);
        assertNotNull(lastEvent);
        assertTrue(lastEvent.isComplete());
    }

}
