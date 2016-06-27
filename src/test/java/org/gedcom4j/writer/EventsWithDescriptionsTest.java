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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.IndividualEventType;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.validate.GedcomValidationFinding;
import org.junit.Test;

/**
 * Test how things are written when there are individual events with descriptions, which are allowed by the parser
 * (despite being non-standard), but which are not written by the writer. See Issue 62 at github.
 * 
 * @author frizbog
 */
public class EventsWithDescriptionsTest {

    /**
     * Test that if we load a file with non-standard event tags, and write it back out, the non-standard descriptions
     * after the event tags should not be there any more.
     * 
     * @throws IOException
     *             if there's a problem reading/writing files
     * @throws GedcomParserException
     *             if there's a problem parsing the file as a GEDCOM
     * @throws GedcomWriterException
     *             if there's a problem writing the gedcom
     */
    @Test
    public void testNonStdTagsNotWritten() throws IOException, GedcomParserException, GedcomWriterException {
        // Read original non-standard file, find non-standard birth event, assert some pre-conditions
        GedcomParser gp = new GedcomParser();
        gp.load("sample/Event Tag Test.ged");
        Gedcom gBefore = gp.gedcom;
        assertNotNull(gBefore);

        assertEquals(1, gBefore.individuals.size());
        Individual iBefore = gBefore.individuals.get("@I1@");
        assertNotNull(iBefore);

        assertNotNull(iBefore.events);
        assertEquals(4, iBefore.events.size());
        IndividualEvent eBefore = iBefore.events.get(0); // The birth event
        assertNotNull(eBefore);
        assertEquals(IndividualEventType.BIRTH, eBefore.type);
        assertNull(eBefore.yNull);
        assertNotNull(eBefore.description);

        // Write the file back out in standard format
        String fn = "tmp/" + this.getClass().getName() + ".ged";
        GedcomWriter gw = new GedcomWriter(gBefore);
        gw.validationSuppressed = true;
        gw.write(fn);

        // Read the file we just wrote back in. The non-standard part should be removed.
        gp = new GedcomParser();
        gp.load(fn);
        Gedcom gAfter = gp.gedcom;
        assertNotNull(gBefore);

        assertEquals(1, gAfter.individuals.size());
        Individual iAfter = gAfter.individuals.get("@I1@");
        assertNotNull(iAfter);

        assertNotNull(iAfter.events);
        assertEquals(4, iAfter.events.size());
        IndividualEvent eAfter = iAfter.events.get(0); // The birth event
        assertNotNull(eAfter);
        assertEquals(IndividualEventType.BIRTH, eAfter.type);
        assertNull(eAfter.yNull);

        // And the big payoff...
        assertNull(eAfter.description);

    }

    /**
     * Test that if we load a file with non-standard event tags, and write it back out with validation enabled, the
     * writer will barf on the non-standard descriptions on the events.
     * 
     * @throws IOException
     *             if there's a problem reading/writing files
     * @throws GedcomParserException
     *             if there's a problem parsing the file as a GEDCOM
     * @throws GedcomWriterException
     *             if there's a problem writing the gedcom
     */
    @Test
    public void testValidation() throws IOException, GedcomParserException, GedcomWriterException {
        // Read original non-standard file, find non-standard birth event, assert some pre-conditions
        GedcomParser gp = new GedcomParser();
        gp.load("sample/Event Tag Test.ged");
        Gedcom gBefore = gp.gedcom;
        assertNotNull(gBefore);

        assertEquals(1, gBefore.individuals.size());
        Individual iBefore = gBefore.individuals.get("@I1@");
        assertNotNull(iBefore);

        assertNotNull(iBefore.events);
        assertEquals(4, iBefore.events.size());
        IndividualEvent eBefore = iBefore.events.get(0); // The birth event
        assertNotNull(eBefore);
        assertEquals(IndividualEventType.BIRTH, eBefore.type);
        assertNull(eBefore.yNull);
        assertNotNull(eBefore.description);

        // Attempt to write the file back out in standard format
        String fn = "tmp/" + this.getClass().getName() + ".ged";
        GedcomWriter gw = new GedcomWriter(gBefore);
        try {
            gw.write(fn);
            fail("Expected a writer exception due to validation failures");
        } catch (GedcomWriterException expected) {
            assertEquals(2, gw.validationFindings.size());
            for (GedcomValidationFinding vf : gw.validationFindings) {
                System.out.println(vf);
            }
        }
    }
}
