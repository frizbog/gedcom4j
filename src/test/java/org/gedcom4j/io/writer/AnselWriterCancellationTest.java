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
package org.gedcom4j.io.writer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.exception.WriterCancelledException;
import org.gedcom4j.io.event.FileProgressEvent;
import org.gedcom4j.io.event.FileProgressListener;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.IGedcom;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.validate.Validator;
import org.gedcom4j.writer.GedcomWriter;
import org.junit.Test;

/**
 * Test cancelling file writes using {@link AnselWriter}
 * 
 * @author frizbog
 */
public class AnselWriterCancellationTest implements FileProgressListener {

    /**
     * The number of event notifications received
     */
    private int eventCount;

    /**
     * GedcomWriter test fixture
     */
    private GedcomWriter gw;

    /**
     * {@inheritDoc}
     */
    @Override
    public void progressNotification(FileProgressEvent e) {
        eventCount++;
        if (eventCount >= 5) {
            gw.cancel();
        }
    }

    /**
     * Test when you've registered as a listener - cancel after 5 notifications
     * 
     * @throws IOException
     *             if the file can't be loaded
     * @throws GedcomParserException
     *             if the file can't be parsed
     * @throws GedcomWriterException
     *             if there's a problem writing the file
     */
    @Test(expected = WriterCancelledException.class)
    @SuppressWarnings({ "resource", "PMD.SystemPrintln" })
    public void testRegistered() throws IOException, GedcomParserException, GedcomWriterException {
        IGedcom g = new Gedcom();
        GedcomParser gp = new GedcomParser(g);
        gp.load("sample/willis-ansel.ged");
        eventCount = 0;
        Validator gv = new Validator(g);
        gv.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);
        gv.validate(); // Cleanup whatever can be cleaned up
        gw = new GedcomWriter(g);
        gw.setValidationSuppressed(true);
        gw.registerFileObserver(this);
        gw.write(new NullOutputStream());

        assertEquals(42, eventCount);
    }

}
