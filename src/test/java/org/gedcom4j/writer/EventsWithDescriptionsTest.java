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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.model.IGedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.validate.Validator.Finding;
import org.junit.Test;

/**
 * Test how things are written when there are individual events with descriptions, which are allowed by the parser (despite being
 * non-standard), but which are not written by the writer. See Issue 62 at github.
 * 
 * @author frizbog
 */
public class EventsWithDescriptionsTest {

    /**
     * Test that if we load a file with non-standard event tags, and write it back out, the non-standard descriptions after the
     * event tags should not be there any more.
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
        IGedcom gBefore = gp.getGedcom();
        assertNotNull(gBefore);

        assertEquals(1, gBefore.getIndividuals().size());
        Individual iBefore = gBefore.getIndividuals().get("@I1@");
        assertNotNull(iBefore);

        assertNotNull(iBefore.getEvents());
        assertEquals(4, iBefore.getEvents().size());
        IndividualEvent eBefore = iBefore.getEvents().get(0); // The birth event
        assertNotNull(eBefore);
        assertEquals(IndividualEventType.BIRTH, eBefore.getType());
        assertNull(eBefore.getYNull());
        assertNotNull(eBefore.getDescription());

        // Write the file back out in standard format
        String fn = "tmp/" + this.getClass().getName() + ".ged";
        GedcomWriter gw = new GedcomWriter(gBefore);
        gw.setValidationSuppressed(true);
        gw.write(fn);

        // Read the file we just wrote back in. The non-standard part should be removed.
        gp = new GedcomParser();
        gp.load(fn);
        IGedcom gAfter = gp.getGedcom();
        assertNotNull(gBefore);

        assertEquals(1, gAfter.getIndividuals().size());
        Individual iAfter = gAfter.getIndividuals().get("@I1@");
        assertNotNull(iAfter);

        assertNotNull(iAfter.getEvents());
        assertEquals(4, iAfter.getEvents().size());
        IndividualEvent eAfter = iAfter.getEvents().get(0); // The birth event
        assertNotNull(eAfter);
        assertEquals(IndividualEventType.BIRTH, eAfter.getType());
        assertNull(eAfter.getYNull());

        // And the big payoff...
        assertNull(eAfter.getDescription());

    }

    /**
     * Test that if we load a file with non-standard event tags, and write it back out with validation enabled, the writer will barf
     * on the non-standard descriptions on the events.
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
        IGedcom gBefore = gp.getGedcom();
        assertNotNull(gBefore);

        assertEquals(1, gBefore.getIndividuals().size());
        Individual iBefore = gBefore.getIndividuals().get("@I1@");
        assertNotNull(iBefore);

        assertNotNull(iBefore.getEvents());
        assertEquals(4, iBefore.getEvents().size());
        IndividualEvent eBefore = iBefore.getEvents().get(0); // The birth event
        assertNotNull(eBefore);
        assertEquals(IndividualEventType.BIRTH, eBefore.getType());
        assertNull(eBefore.getYNull());
        assertNotNull(eBefore.getDescription());

        // Attempt to write the file back out in standard format
        String fn = "tmp/" + this.getClass().getName() + ".ged";
        GedcomWriter gw = new GedcomWriter(gBefore);
        try {
            gw.write(fn);
            fail("Expected a writer exception due to validation failures");
        } catch (@SuppressWarnings("unused") GedcomWriterException expected) {
            List<Finding> allFindings = gw.getValidator().getResults().getAllFindings();
            assertEquals(3, allFindings.size());

            // Finding 0 is because there's a description on a Birth event
            Finding f0 = allFindings.get(0);
            assertEquals("description", f0.getFieldNameOfConcern());
            IndividualEvent ie = (IndividualEvent) f0.getItemOfConcern();
            assertEquals(IndividualEventType.BIRTH, ie.getType());
            assertNotNull(ie.getDescription());

            // Finding 1 is because there's a Y|Null value of Y on a Cremation event
            Finding f1 = allFindings.get(1);
            assertEquals("yNull", f1.getFieldNameOfConcern());
            ie = (IndividualEvent) f1.getItemOfConcern();
            assertEquals(IndividualEventType.CREMATION, ie.getType());
            assertNotNull(ie.getYNull());

            // Finding 2 is because there's a description on a Burial event
            Finding f2 = allFindings.get(2);
            assertEquals("description", f2.getFieldNameOfConcern());
            ie = (IndividualEvent) f2.getItemOfConcern();
            assertEquals(IndividualEventType.BURIAL, ie.getType());
            assertNotNull(ie.getDescription());

        }
    }
}
