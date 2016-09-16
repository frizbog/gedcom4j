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
package org.gedcom4j.writer;

import static org.junit.Assert.assertEquals;

import java.io.FileOutputStream;
import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.exception.WriterCancelledException;
import org.gedcom4j.io.encoding.Encoding;
import org.gedcom4j.io.event.FileProgressEvent;
import org.gedcom4j.io.event.FileProgressListener;
import org.gedcom4j.io.writer.LineTerminator;
import org.gedcom4j.io.writer.NullOutputStream;
import org.gedcom4j.model.AbstractEvent;
import org.gedcom4j.model.CharacterSet;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.GedcomVersion;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.StringWithCustomTags;
import org.gedcom4j.model.SupportedVersion;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.validate.Validator;
import org.gedcom4j.validate.Validator.Finding;
import org.gedcom4j.writer.event.ConstructProgressEvent;
import org.gedcom4j.writer.event.ConstructProgressListener;
import org.junit.Test;

/**
 * Test the ability to receive notifications about GEDCOM string construction, and to cancel the operation
 * 
 * @author frizbog
 */
@SuppressWarnings("PMD.TooManyMethods")
public class GedcomWriterFileProgressAndCancellationTest implements ConstructProgressListener, FileProgressListener {

    /**
     * Number of construction notifications received
     */
    private int constructNotificationCount = 0;

    /**
     * How many construction notifications to cancel after
     */
    private int constructionCancelAfter = Integer.MAX_VALUE;

    /**
     * How many lines were constructed
     */
    private int linesConstructed = 0;

    /**
     * How many bytes were written
     */
    private int bytesWritten = 0;

    /**
     * The GedcomWriter we're testing with
     */
    private GedcomWriter gw;

    /**
     * The number of file notifications received
     */
    private int fileNotificationCount = 0;

    /**
     * The number of file notifications to cancel after
     */
    private int fileCancelAfter = Integer.MAX_VALUE;

    /**
     * {@inheritDoc}
     */
    @Override
    public void progressNotification(ConstructProgressEvent e) {
        constructNotificationCount++;
        linesConstructed = e.getLinesProcessed();
        if (constructNotificationCount >= constructionCancelAfter) {
            gw.cancel();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void progressNotification(FileProgressEvent e) {
        fileNotificationCount++;
        bytesWritten = e.getBytesProcessed();
        if (fileNotificationCount > fileCancelAfter) {
            gw.cancel();
        }
    }

    /**
     * Test with cancelling gedcom construction after getting a couple notifications
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
    public void testConstructionCancellation() throws IOException, GedcomParserException, GedcomWriterException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/willis-ascii.ged");
        gw = new GedcomWriter(gp.getGedcom());
        gw.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);
        gw.registerConstructObserver(this);
        gw.registerFileObserver(this);
        constructionCancelAfter = 5;
        gw.write(new NullOutputStream());
    }

    /**
     * Test with cancelling file write after getting a couple notifications
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
    public void testFileCancellation() throws IOException, GedcomParserException, GedcomWriterException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/willis-ascii.ged");
        gw = new GedcomWriter(gp.getGedcom());
        gw.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);
        gw.registerConstructObserver(this);
        gw.registerFileObserver(this);
        fileCancelAfter = 5;
        gw.write(new NullOutputStream());
    }

    /**
     * Test without cancelling, using ANSEL file
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if the file can't be parsed
     * @throws GedcomWriterException
     *             if the file can't be written (or is cancelled)
     */
    @SuppressWarnings({ "resource", "PMD.SystemPrintln" })
    @Test
    public void testNoCancellationAnselCrlf() throws IOException, GedcomParserException, GedcomWriterException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/willis-ansel.ged");
        cleanUpGedcom(gp, Encoding.ANSEL);
        gw = new GedcomWriter(gp.getGedcom());
        gw.setLineTerminator(LineTerminator.CRLF);
        gw.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);
        gw.registerConstructObserver(this);
        gw.registerFileObserver(this);
        try {
            gw.write(new NullOutputStream());
        } catch (GedcomWriterException e) {
            for (Finding f : gw.getValidator().getResults().getAllFindings()) {
                System.out.println(f);
            }
            throw e;
        }
        assertEquals(40, constructNotificationCount);
        assertEquals(41, fileNotificationCount);
        assertEquals(19998, linesConstructed);
        assertEquals(621291, bytesWritten);
    }

    /**
     * Test without cancelling, using ANSEL file
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if the file can't be parsed
     * @throws GedcomWriterException
     *             if the file can't be written (or is cancelled)
     */
    @SuppressWarnings({ "resource", "PMD.SystemPrintln" })
    @Test
    public void testNoCancellationAnselCrOnly() throws IOException, GedcomParserException, GedcomWriterException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/willis-ansel.ged");
        cleanUpGedcom(gp, Encoding.ANSEL);
        gw = new GedcomWriter(gp.getGedcom());
        gw.setLineTerminator(LineTerminator.CR_ONLY);
        gw.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);
        gw.registerConstructObserver(this);
        gw.registerFileObserver(this);
        try {
            gw.write(new NullOutputStream());
        } catch (GedcomWriterException e) {
            for (Finding f : gw.getValidator().getResults().getAllFindings()) {
                System.out.println(f);
            }
            throw e;
        }
        assertEquals(40, constructNotificationCount);
        assertEquals(41, fileNotificationCount);
        assertEquals(19998, linesConstructed);
        assertEquals(601293, bytesWritten);
    }

    /**
     * Test without cancelling, using Ascii file
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if the file can't be parsed
     * @throws GedcomWriterException
     *             if the file can't be written (or is cancelled)
     */
    @SuppressWarnings({ "resource", "PMD.SystemPrintln" })
    @Test
    public void testNoCancellationAsciiCrlf() throws IOException, GedcomParserException, GedcomWriterException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/willis-ascii.ged");
        cleanUpGedcom(gp, Encoding.ASCII);
        gw = new GedcomWriter(gp.getGedcom());
        gw.setLineTerminator(LineTerminator.CRLF);
        gw.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);
        gw.registerConstructObserver(this);
        gw.registerFileObserver(this);
        try {
            gw.write(new NullOutputStream());
        } catch (GedcomWriterException e) {
            for (Finding f : gw.getValidator().getResults().getAllFindings()) {
                System.out.println(f);
            }
            throw e;
        }
        assertEquals(40, constructNotificationCount);
        assertEquals(41, fileNotificationCount);
        assertEquals(19998, linesConstructed);
        assertEquals(621291, bytesWritten);
    }

    /**
     * Test without cancelling, using Ascii file
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if the file can't be parsed
     * @throws GedcomWriterException
     *             if the file can't be written (or is cancelled)
     */
    @SuppressWarnings({ "resource", "PMD.SystemPrintln" })
    @Test
    public void testNoCancellationAsciiCrOnly() throws IOException, GedcomParserException, GedcomWriterException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/willis-ascii.ged");
        cleanUpGedcom(gp, Encoding.ASCII);
        gw = new GedcomWriter(gp.getGedcom());
        gw.setLineTerminator(LineTerminator.CR_ONLY);
        gw.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);
        gw.registerConstructObserver(this);
        gw.registerFileObserver(this);
        try {
            gw.write(new NullOutputStream());
        } catch (GedcomWriterException e) {
            for (Finding f : gw.getValidator().getResults().getAllFindings()) {
                System.out.println(f);
            }
            throw e;
        }
        assertEquals(40, constructNotificationCount);
        assertEquals(41, fileNotificationCount);
        assertEquals(19998, linesConstructed);
        assertEquals(601293, bytesWritten);
    }

    /**
     * Test without cancelling, using Unicode Big-Endian file
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if the file can't be parsed
     * @throws GedcomWriterException
     *             if the file can't be written (or is cancelled)
     */
    @SuppressWarnings({ "resource", "PMD.SystemPrintln" })
    @Test
    public void testNoCancellationUnicodeBigEndianCrlf() throws IOException, GedcomParserException, GedcomWriterException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/willis-unicode-bigendian.ged");
        cleanUpGedcom(gp, Encoding.UNICODE_BIG_ENDIAN);
        gw = new GedcomWriter(gp.getGedcom());
        gw.setLineTerminator(LineTerminator.CRLF);
        gw.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);
        gw.registerConstructObserver(this);
        gw.registerFileObserver(this);
        try {
            gw.write(new NullOutputStream());
        } catch (GedcomWriterException e) {
            for (Finding f : gw.getValidator().getResults().getAllFindings()) {
                System.out.println(f);
            }
            throw e;
        }
        assertEquals(40, constructNotificationCount);
        assertEquals(41, fileNotificationCount);
        assertEquals(19998, linesConstructed);
        assertEquals(1242610, bytesWritten);
    }

    /**
     * Test without cancelling, using Unicode Big-Endian file
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if the file can't be parsed
     * @throws GedcomWriterException
     *             if the file can't be written (or is cancelled)
     */
    @SuppressWarnings({ "resource", "PMD.SystemPrintln" })
    @Test
    public void testNoCancellationUnicodeBigEndianCrOnly() throws IOException, GedcomParserException, GedcomWriterException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/willis-unicode-bigendian.ged");
        cleanUpGedcom(gp, Encoding.UNICODE_BIG_ENDIAN);
        gw = new GedcomWriter(gp.getGedcom());
        gw.setLineTerminator(LineTerminator.CR_ONLY);
        gw.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);
        gw.registerConstructObserver(this);
        gw.registerFileObserver(this);
        try {
            gw.write(new NullOutputStream());
        } catch (GedcomWriterException e) {
            for (Finding f : gw.getValidator().getResults().getAllFindings()) {
                System.out.println(f);
            }
            throw e;
        }
        assertEquals(40, constructNotificationCount);
        assertEquals(41, fileNotificationCount);
        assertEquals(19998, linesConstructed);
        assertEquals(1202614, bytesWritten);
    }

    /**
     * Test without cancelling, using Unicode Little-Endian file
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if the file can't be parsed
     * @throws GedcomWriterException
     *             if the file can't be written (or is cancelled)
     */
    @SuppressWarnings({ "resource", "PMD.SystemPrintln" })
    @Test
    public void testNoCancellationUnicodeLittleEndianCrlf() throws IOException, GedcomParserException, GedcomWriterException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/willis-unicode-littleendian.ged");
        cleanUpGedcom(gp, Encoding.UNICODE_LITTLE_ENDIAN);
        gw = new GedcomWriter(gp.getGedcom());
        gw.setLineTerminator(LineTerminator.CRLF);
        gw.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);
        gw.registerConstructObserver(this);
        gw.registerFileObserver(this);
        try {
            gw.write(new FileOutputStream("tmp/foo-unicode-little-endian.ged"));
        } catch (GedcomWriterException e) {
            for (Finding f : gw.getValidator().getResults().getAllFindings()) {
                System.out.println(f);
            }
            throw e;
        }
        assertEquals(40, constructNotificationCount);
        assertEquals(41, fileNotificationCount);
        assertEquals(19998, linesConstructed);
        assertEquals(1242616, bytesWritten);
    }

    /**
     * Test without cancelling, using Unicode Little-Endian file
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if the file can't be parsed
     * @throws GedcomWriterException
     *             if the file can't be written (or is cancelled)
     */
    @SuppressWarnings({ "resource", "PMD.SystemPrintln" })
    @Test
    public void testNoCancellationUnicodeLittleEndianCrOnly() throws IOException, GedcomParserException, GedcomWriterException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/willis-unicode-littleendian.ged");
        cleanUpGedcom(gp, Encoding.UNICODE_LITTLE_ENDIAN);
        gw = new GedcomWriter(gp.getGedcom());
        gw.setLineTerminator(LineTerminator.CR_ONLY);
        gw.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);
        gw.registerConstructObserver(this);
        gw.registerFileObserver(this);
        try {
            gw.write(new FileOutputStream("tmp/foo-unicode-little-endian.ged"));
        } catch (GedcomWriterException e) {
            for (Finding f : gw.getValidator().getResults().getAllFindings()) {
                System.out.println(f);
            }
            throw e;
        }
        assertEquals(40, constructNotificationCount);
        assertEquals(41, fileNotificationCount);
        assertEquals(19998, linesConstructed);
        assertEquals(1202620, bytesWritten);
    }

    /**
     * Test without cancelling, using UTF-8 file
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if the file can't be parsed
     * @throws GedcomWriterException
     *             if the file can't be written (or is cancelled)
     */
    @SuppressWarnings({ "resource", "PMD.SystemPrintln" })
    @Test
    public void testNoCancellationUtf8Crlf() throws IOException, GedcomParserException, GedcomWriterException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/willis.ged");
        cleanUpGedcom(gp, Encoding.UTF_8);
        gw = new GedcomWriter(gp.getGedcom());
        gw.setLineTerminator(LineTerminator.CRLF);
        gw.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);
        gw.registerConstructObserver(this);
        gw.registerFileObserver(this);
        try {
            gw.write(new NullOutputStream());
        } catch (GedcomWriterException e) {
            for (Finding f : gw.getValidator().getResults().getAllFindings()) {
                System.out.println(f);
            }
            throw e;
        }
        assertEquals(40, constructNotificationCount);
        assertEquals(40, fileNotificationCount);
        assertEquals(19995, linesConstructed);
        assertEquals(573440, bytesWritten);
    }

    /**
     * Test without cancelling, using UTF-8 file
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if the file can't be parsed
     * @throws GedcomWriterException
     *             if the file can't be written (or is cancelled)
     */
    @SuppressWarnings({ "resource", "PMD.SystemPrintln" })
    @Test
    public void testNoCancellationUtf8CrOnly() throws IOException, GedcomParserException, GedcomWriterException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/willis.ged");
        cleanUpGedcom(gp, Encoding.UTF_8);
        gw = new GedcomWriter(gp.getGedcom());
        gw.setLineTerminator(LineTerminator.CR_ONLY);
        gw.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);
        gw.registerConstructObserver(this);
        gw.registerFileObserver(this);
        try {
            gw.write(new NullOutputStream());
        } catch (GedcomWriterException e) {
            for (Finding f : gw.getValidator().getResults().getAllFindings()) {
                System.out.println(f);
            }
            throw e;
        }
        assertEquals(40, constructNotificationCount);
        assertEquals(40, fileNotificationCount);
        assertEquals(19995, linesConstructed);
        assertEquals(557056, bytesWritten);
    }

    /**
     * Helper method to clean up bad data in the GEDCOM so it writes ok
     * 
     * @param gp
     *            the gedcom parser
     * @param encoding
     *            the encoding to use when writing
     */
    private void cleanUpGedcom(GedcomParser gp, Encoding encoding) {
        CharacterSet characterSet = new CharacterSet();
        characterSet.setCharacterSetName(new StringWithCustomTags(encoding.getCharacterSetName()));
        gp.getGedcom().getHeader().setCharacterSet(characterSet);
        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber(SupportedVersion.V5_5_1);
        gp.getGedcom().getHeader().setGedcomVersion(gv);
        for (Individual i : gp.getGedcom().getIndividuals().values()) {
            if (i.getEvents() != null) {
                for (AbstractEvent e : i.getEvents()) {
                    e.setDescription(null);
                }
            }
        }
        for (Family f : gp.getGedcom().getFamilies().values()) {
            if (f.getEvents() != null) {
                for (AbstractEvent e : f.getEvents()) {
                    e.setDescription(null);
                }
            }
        }
    }

}
