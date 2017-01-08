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
import static org.junit.Assert.assertNotSame;

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.enumerations.FamilyEventType;
import org.junit.Test;

/**
 * Test copy constructor for {@link Family}
 * 
 * @author frizbog
 */
public class FamilyCopyTest extends AbstractCopyTest {

    /**
     * Test copying a null {@link Family}, which should never work
     */
    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testCopyNull() {
        new Family(null);
    }

    /**
     * Test the simplest possible scenario - copy a new default {@link Family}
     */
    @Test
    public void testSimplestPossible() {
        Family orig = new Family();
        Family copy = new Family(orig);
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

        for (Family original : loadedGedcom.getFamilies().values()) {
            Family copy = new Family(original);
            assertNotSame(original, copy);
            assertEquals(original, copy);
        }
    }

    /**
     * Test with values
     */
    @Test
    public void testWithValues() {
        Family orig = new Family();
        orig.setAutomatedRecordId("123");
        ChangeDate cd = new ChangeDate();
        cd.setTime("12:21a");
        orig.setChangeDate(cd);
        orig.setNumChildren("1");
        Individual h = new Individual();
        h.setXref("@I1@");
        orig.setHusband(new IndividualReference(h));
        Individual w = new Individual();
        w.setXref("@I2@");
        orig.setWife(new IndividualReference(w));
        Individual k = new Individual();
        k.setXref("@I3@");
        orig.getChildren(true).add(new IndividualReference(k));
        orig.setRecFileNumber("123");
        orig.setRestrictionNotice("UNRESTRICTED");
        orig.getCitations(true).add(getTestCitation());
        orig.getCustomFacts(true).add(getTestCustomFact());
        FamilyEvent e = new FamilyEvent();
        e.setType(FamilyEventType.ANNULMENT);
        e.setDate("10 OCT 1910");
        e.setDescription("Never happened");
        orig.getEvents(true).add(e);
        LdsSpouseSealing lss = new LdsSpouseSealing();
        lss.setStatus("Complete");
        orig.getLdsSpouseSealings(true).add(lss);
        Multimedia mm = new Multimedia();
        mm.getBlob(true).add("qwpeoklskfsekrpweoalksfnsleorpwqoewklsfjlskjfpoeriwpeori");
        MultimediaReference mr = new MultimediaReference(mm);
        orig.getMultimedia(true).add(mr);
        orig.getNoteStructures(true).add(getTestNoteStructure());
        Submitter s = new Submitter();
        s.setName("Eloise /King/");
        orig.getSubmitters(true).add(new SubmitterReference(s));
        UserReference u = new UserReference();
        u.setType("Any type you like");
        orig.getUserReferences(true).add(u);

        Family copy = new Family(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
        assertEquals(orig.toString(), copy.toString());
    }

}
