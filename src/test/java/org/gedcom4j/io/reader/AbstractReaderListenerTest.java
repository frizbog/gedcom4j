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
package org.gedcom4j.io.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.io.event.FileProgressEvent;
import org.gedcom4j.io.event.FileProgressListener;
import org.gedcom4j.model.InMemoryGedcom;
import org.gedcom4j.parser.GedcomParser;
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
     * The expected number of bytes to be read
     */
    private final int expectedBytes;

    /**
     * Constructor
     * 
     * @param testFileName
     *            name of file to load for test
     * @param expectedLines
     *            the expected number of lines in the file
     * @param expectedBytes
     *            the exepected number of bytes to be read
     * @param expectedNotifications
     *            the expected number of notification events
     */
    public AbstractReaderListenerTest(String testFileName, int expectedLines, int expectedBytes, int expectedNotifications) {
        fileName = testFileName;
        this.expectedLines = expectedLines;
        this.expectedBytes = expectedBytes;
        this.expectedNotifications = expectedNotifications;
    }

    /**
     * {@inheritDoc}
     */
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
        GedcomParser gp = new GedcomParser(new InMemoryGedcom());
        gp.load(fileName);
        assertNotNull(gp.getGedcom());
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
        GedcomParser gp = new GedcomParser(new InMemoryGedcom());
        gp.registerFileObserver(this);
        gp.load(fileName);
        assertNotNull(gp.getGedcom());
        assertEquals(expectedNotifications, eventCount);
        assertNotNull(lastEvent);
        assertTrue(lastEvent.isComplete());
        assertEquals(expectedLines, lastEvent.getLinesProcessed());
        assertEquals(expectedBytes, lastEvent.getBytesProcessed());
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
        GedcomParser gp = new GedcomParser(new InMemoryGedcom());
        gp.load(fileName);
        assertNotNull(gp.getGedcom());
        assertEquals(0, eventCount);
        assertNull(lastEvent);
    }

}