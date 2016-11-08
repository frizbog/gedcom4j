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

import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.exception.GedcomWriterVersionDataMismatchException;
import org.gedcom4j.model.AbstractNameVariation;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Place;
import org.gedcom4j.model.PlaceNameVariation;
import org.gedcom4j.model.enumerations.SupportedVersion;
import org.junit.Test;

/**
 * Test for {@link PlaceEmitter}
 * 
 * @author frizbog
 */
public class PlaceEmitterTest {

    /**
     * Test emitting a {@link Place}
     * 
     * @throws GedcomWriterException
     *             if the Place cannot be written out
     */
    @Test
    public void test551() throws GedcomWriterException {
        Place place = new Place();

        place.setLatitude("X");
        place.setLongitude("Y");

        AbstractNameVariation r = new PlaceNameVariation();
        r.setVariation("Foo");
        place.getRomanized(true).add(r);

        AbstractNameVariation p = new PlaceNameVariation();
        p.setVariation("Phoo");
        place.getPhonetic(true).add(p);

        Gedcom gedcom = new Gedcom();
        gedcom.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
        GedcomWriter baseWriter = new GedcomWriter(gedcom);
        new PlaceEmitter(baseWriter, 1, place).emit();

        assertEquals(6, baseWriter.lines.size());
        assertEquals("1 PLAC", baseWriter.lines.get(0));
        assertEquals("2 ROMN Foo", baseWriter.lines.get(1));
        assertEquals("2 FONE Phoo", baseWriter.lines.get(2));
        assertEquals("2 MAP", baseWriter.lines.get(3));
        assertEquals("3 LATI X", baseWriter.lines.get(4));
        assertEquals("3 LONG Y", baseWriter.lines.get(5));

    }

    /**
     * Test emitting a {@link Place}
     * 
     * @throws GedcomWriterException
     *             if the Place cannot be written out
     */
    @Test(expected = GedcomWriterVersionDataMismatchException.class)
    public void test55Fail1() throws GedcomWriterException {
        Place place = new Place();

        place.setLatitude("X");
        place.setLongitude("Y");

        Gedcom gedcom = new Gedcom();
        gedcom.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        GedcomWriter baseWriter = new GedcomWriter(gedcom);
        new PlaceEmitter(baseWriter, 1, place).emit();

    }

    /**
     * Test emitting a {@link Place}
     * 
     * @throws GedcomWriterException
     *             if the Place cannot be written out
     */
    @Test(expected = GedcomWriterVersionDataMismatchException.class)
    public void test55Fail2() throws GedcomWriterException {
        Place place = new Place();

        AbstractNameVariation r = new PlaceNameVariation();
        r.setVariation("Foo");
        place.getRomanized(true).add(r);

        Gedcom gedcom = new Gedcom();
        gedcom.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        GedcomWriter baseWriter = new GedcomWriter(gedcom);
        new PlaceEmitter(baseWriter, 1, place).emit();

    }

    /**
     * Test emitting a {@link Place}
     * 
     * @throws GedcomWriterException
     *             if the Place cannot be written out
     */
    @Test(expected = GedcomWriterVersionDataMismatchException.class)
    public void test55Fail3() throws GedcomWriterException {
        Place place = new Place();

        AbstractNameVariation p = new PlaceNameVariation();
        p.setVariation("Phoo");
        place.getPhonetic(true).add(p);

        Gedcom gedcom = new Gedcom();
        gedcom.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        GedcomWriter baseWriter = new GedcomWriter(gedcom);
        new PlaceEmitter(baseWriter, 1, place).emit();

    }

}
