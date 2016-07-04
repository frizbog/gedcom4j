/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.gedcom4j.writer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.exception.WriterCancelledException;
import org.gedcom4j.io.writer.NullOutputStream;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.writer.event.ConstructProgressEvent;
import org.gedcom4j.writer.event.ConstructProgressListener;
import org.junit.Test;

/**
 * Test the ability to receive notifications about GEDCOM string construction, and to cancel the operation
 * 
 * @author frizbog
 */
public class GedcomWriterFileProgressAndCancellationTest implements ConstructProgressListener {

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
    @SuppressWarnings("resource")
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
    @SuppressWarnings("resource")
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
