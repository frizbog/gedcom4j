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

import java.util.ArrayList;
import java.util.Collection;

import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.exception.GedcomWriterVersionDataMismatchException;
import org.gedcom4j.model.FileReference;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.IGedcom;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.MultimediaReference;
import org.gedcom4j.model.enumerations.SupportedVersion;
import org.junit.Test;

/**
 * Test for {@link Multimedia551Emitter}
 * 
 * @author frizbog
 */
public class Multimedia55EmitterTest {

    /**
     * Test emitting a multimedia item with a continued object
     * 
     * @throws GedcomWriterException
     *             if anything goes wrong
     */
    @Test
    public void testContinuedObject() throws GedcomWriterException {
        Collection<Multimedia> mm = new ArrayList<>();
        Multimedia m2 = new Multimedia();
        m2.setEmbeddedMediaFormat("Foo");
        m2.getBlob(true).add("xyz");
        Multimedia m1 = new Multimedia();
        m1.setEmbeddedMediaFormat("Foo");
        m1.getBlob(true).add("xyz");
        m1.setContinuedObject(new MultimediaReference(m2));
        mm.add(m1);
        GedcomWriter baseWriter = new GedcomWriter(new Gedcom());
        new Multimedia55Emitter(baseWriter, 1, mm).emit();
        assertEquals(4, baseWriter.lines.size());
        assertEquals("0 OBJE", baseWriter.lines.get(0));
        assertEquals("1 FORM Foo", baseWriter.lines.get(1));
        assertEquals("1 BLOB", baseWriter.lines.get(2));
        assertEquals("2 CONT xyz", baseWriter.lines.get(3));
    }

    /**
     * Test emitting when there are file references but for a gedcom 5.5 file
     * 
     * @throws GedcomWriterException
     *             if anything goes wrong
     */
    @Test(expected = GedcomWriterVersionDataMismatchException.class)
    public void testFileReferences() throws GedcomWriterException {
        IGedcom gedcom = new Gedcom();
        gedcom.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        GedcomWriter baseWriter = new GedcomWriter(gedcom);

        Collection<Multimedia> mms = new ArrayList<>();
        Multimedia mm = new Multimedia();
        mm.setEmbeddedMediaFormat("Foo");
        mm.getBlob(true).add("xyz");
        FileReference fr = new FileReference();
        fr.setTitle("Yo");
        mm.getFileReferences(true).add(fr);
        mms.add(mm);
        new Multimedia55Emitter(baseWriter, 1, mms).emit();
    }

    /**
     * Test emitting a null list of multimedia
     * 
     * @throws GedcomWriterException
     *             if anything goes wrong
     */
    @Test
    public void testNull() throws GedcomWriterException {
        GedcomWriter baseWriter = new GedcomWriter(new Gedcom());
        new Multimedia55Emitter(baseWriter, 1, null).emit();
        assertEquals(0, baseWriter.lines.size());
    }

}
