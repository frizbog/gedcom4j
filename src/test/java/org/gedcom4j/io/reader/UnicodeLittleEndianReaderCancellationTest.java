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
package org.gedcom4j.io.reader;

import java.io.IOException;

import org.gedcom4j.io.event.FileProgressEvent;
import org.gedcom4j.io.event.FileProgressListener;
import org.gedcom4j.io.exception.LoadCancelledException;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.parser.GedcomParserException;
import org.junit.Test;

/**
 * Test cancelling loads using {@link UnicodeLittleEndianReader}
 * 
 * @author frizbog
 */
public class UnicodeLittleEndianReaderCancellationTest implements FileProgressListener {

    /**
     * The number of event notifications received
     */
    private int eventCount;

    /**
     * GedcomParser test fixture
     */
    private final GedcomParser gp = new GedcomParser();

    @Override
    public void progressNotification(FileProgressEvent e) {
        eventCount++;
        if (eventCount >= 100) {
            gp.cancel();
        }
    }

    /**
     * t Test when you've registered as a listener - cancel after 100 notifications
     * 
     * @throws IOException
     *             if the file can't be loaded
     * @throws GedcomParserException
     *             if the file can't be parsed
     */
    @Test(expected = LoadCancelledException.class)
    public void testRegistered() throws IOException, GedcomParserException {
        eventCount = 0;
        gp.registerFileObserver(this);
        gp.load("sample/willis-unicode-littleendian.ged");
    }

}
