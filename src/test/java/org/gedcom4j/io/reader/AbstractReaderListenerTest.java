package org.gedcom4j.io.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.gedcom4j.io.event.FileProgressEvent;
import org.gedcom4j.io.event.FileProgressListener;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.parser.GedcomParserException;
import org.junit.Test;

/**
 * A base class for tests for the listeners
 * 
 * @author frizbog
 */
public abstract class AbstractReaderListenerTest implements FileProgressListener {

    /**
     * Name of file to read. Should be ANSEL encoded for the test to have value.
     */
    protected final String fileName;

    /**
     * The number of event notifications received
     */
    private int eventCount;

    /**
     * The last event received
     */
    private FileProgressEvent lastEvent;

    /**
     * The number of lines expected to be read from the file
     */
    private final int expectedLines;

    /**
     * The expected number of progress notifications to be received
     */
    private final int expectedNotifications;

    /**
     * Constructor
     * 
     * @param testFileName
     *            name of file to load for test
     * @param expectedLines
     *            the expected number of lines in the file
     * @param expectedNotifications
     *            the expected number of notification events
     */
    public AbstractReaderListenerTest(String testFileName, int expectedLines, int expectedNotifications) {
        fileName = testFileName;
        this.expectedLines = expectedLines;
        this.expectedNotifications = expectedNotifications;
    }

    @Override
    public void progressNotification(FileProgressEvent e) {
        eventCount++;
        lastEvent = e;
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
        gp.load(fileName);
        assertNotNull(gp.gedcom);
        assertEquals(0, eventCount);
        assertNull(lastEvent);
    }

    /**
     * Test when you've registered as a listener
     * 
     * @throws IOException
     *             if the file can't be loaded
     * @throws GedcomParserException
     *             if the file can't be parsed
     */
    @Test
    public void testRegistered() throws IOException, GedcomParserException {
        eventCount = 0;
        GedcomParser gp = new GedcomParser();
        gp.registerObserver(this);
        gp.load(fileName);
        assertNotNull(gp.gedcom);
        assertEquals(expectedNotifications, eventCount);
        assertNotNull(lastEvent);
        assertTrue(lastEvent.isComplete());
        assertEquals(expectedLines, lastEvent.getLinesProcessed());
    }

    /**
     * Test when you've registered then unregistered as a listener
     * 
     * @throws IOException
     *             if the file can't be loaded
     * @throws GedcomParserException
     *             if the file can't be parsed
     */
    @Test
    public void testUnregistered() throws IOException, GedcomParserException {
        eventCount = 0;
        GedcomParser gp = new GedcomParser();
        gp.load(fileName);
        assertNotNull(gp.gedcom);
        assertEquals(0, eventCount);
        assertNull(lastEvent);
    }

}