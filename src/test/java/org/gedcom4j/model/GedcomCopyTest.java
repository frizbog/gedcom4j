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
package org.gedcom4j.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

import java.io.IOException;
import java.util.Date;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.factory.FamilyFactory;
import org.gedcom4j.factory.IndividualFactory;
import org.gedcom4j.factory.Sex;
import org.gedcom4j.parser.DateParser;
import org.junit.Test;

/**
 * Test copy constructor for {@link InMemoryGedcom}
 * 
 * @author frizbog
 */
public class GedcomCopyTest extends AbstractCopyTest {

    /**
     * Test copying a null {@link InMemoryGedcom}, which should never work
     */
    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testCopyNull() {
        new InMemoryGedcom(null);
    }

    /**
     * Test with nulled-out fields
     */
    @Test
    public void testNulledFields() {
        InMemoryGedcom orig = new InMemoryGedcom();
        IGedcom copy = new InMemoryGedcom(orig);
        orig.setHeader(null);
        orig.setSubmission(null);
        assertFalse(orig.equals(copy));
        assertNotSame(orig, copy);
    }

    /**
     * Test the simplest possible scenario - copy a new default {@link InMemoryGedcom}
     */
    @Test
    public void testSimplestPossible() {
        InMemoryGedcom orig = new InMemoryGedcom();
        IGedcom copy = new InMemoryGedcom(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
    }

    /**
     * Test with a loaded file
     * 
     * @throws IOException
     *             if the file cannot be read
     * @throws GedcomParserException
     *             if the file cannot be parsed
     */
    @Test
    public void testWithLoadedFile() throws IOException, GedcomParserException {
        IGedcom loadedGedcom = getLoadedGedcom();
        IGedcom copy = new InMemoryGedcom((InMemoryGedcom) loadedGedcom);
        assertEquals(loadedGedcom, copy);
        assertNotSame(loadedGedcom, copy);
    }

    /**
     * Test with values
     */
    @Test
    public void testWithValues() {
        InMemoryGedcom orig = new InMemoryGedcom();
        Header hdr = new Header();
        hdr.setLanguage("Pig Latin");
        orig.setHeader(hdr);
        Submission s = new Submission();
        s.setXref("@SBM1@");
        s.setRecIdNumber("40404040");
        orig.setSubmission(s);
        orig.setTrailer(new Trailer());
        orig.getCustomFacts(true).add(getTestCustomFact());

        DateParser dp = new DateParser();
        IndividualFactory ifact = new IndividualFactory();
        FamilyFactory ffact = new FamilyFactory();

        Individual h = ifact.create(orig, "Fred", "Frederickson", Sex.MALE, dp.parse("01 JAN 1980"), "New Mexico", new Date(),
                "Antarctica");
        Individual w = ifact.create(orig, "Gladys", "Gladstone", Sex.FEMALE, dp.parse("11 NOV 1979"), "Arizona", null, null);
        Individual k = ifact.create(orig, "Frank", "Frederickson", Sex.MALE, dp.parse("6 JUN 2010"), "Wyoming", null, null);
        ffact.create(orig, h, w, k);

        Multimedia m = new Multimedia();
        m.getBlob(true).add("wpeoirpweoirpwoirpwoeirpwoeirpwoeirwe");
        orig.getMultimedia().put(m.getXref(), m);

        Source src = new Source();
        src.setXref("@SRC1@");
        src.setRecIdNumber("987");
        orig.getSources().put(src.getXref(), src);

        NoteRecord n = getTestNoteRecord();
        n.setXref("@N1@");
        orig.getNotes().put(n.getXref(), n);

        Repository r = new Repository();
        r.setXref("@R1@");
        r.setName("Repository of all truth and wisdom");
        orig.getRepositories().put(r.getXref(), r);

        Submitter sbr = new Submitter();
        sbr.setXref("@SBR1@");
        sbr.setName("Steve /Submitter/");
        orig.getSubmitters().put(sbr.getXref(), sbr);

        IGedcom copy = new InMemoryGedcom(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
    }
}
