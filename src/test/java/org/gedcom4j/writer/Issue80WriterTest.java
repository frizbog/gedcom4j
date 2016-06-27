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
import static org.junit.Assert.assertTrue;

import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.model.*;
import org.junit.Test;

/**
 * Test for issue 80 - latitudes should be written with the tag "LATI", not "LAT"
 * 
 * @author frizbog
 *
 */
public class Issue80WriterTest {

    /**
     * Test fix for issue 80
     * 
     * @throws GedcomWriterException
     *             if the GEDCOM cannot be written to stdout
     */
    @Test
    public void testIssue80() throws GedcomWriterException {

        // Set up a place with a lat and long
        Place p = new Place();
        p.placeName = "Walla Walla, Washington";
        p.latitude = new StringWithCustomTags("123");
        p.longitude = new StringWithCustomTags("3.14159");

        IndividualEvent b = new IndividualEvent();
        b.type = IndividualEventType.BIRTH;
        b.place = p;

        Individual i = new Individual();
        i.xref = "@I001@";
        i.events.add(b);

        Gedcom g = new Gedcom();
        g.individuals.put("@I001@", i);

        // Gedcoms require a submitter
        Submitter s = new Submitter();
        s.xref = "@SUBM@"; // Some unique xref for a submitter
        s.name = new StringWithCustomTags("Matt /Harrah/");
        g.header.submitter = s;
        g.submitters.put(s.xref, s); // Use the xref as the map key

        // Write to sysout
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = false;
        gw.useLittleEndianForUnicode = false;
        gw.write(System.out);

        // Now that we've written the gedcom, let's examine what we wrote
        assertEquals("Should have written 17 lines", 17, gw.lines.size());

        boolean foundMap = false;
        // Loop through and find the map coordinates - don't assume at any particular location
        for (int n = 0; n < 17; n++) {
            String l = gw.lines.get(n);
            if ("3 MAP".equals(l)) {
                foundMap = true;
                assertEquals("4 LATI 123", gw.lines.get(n + 1));
                assertEquals("4 LONG 3.14159", gw.lines.get(n + 2));
                break;
            }
        }
        assertTrue("Should have found map coordinates", foundMap);
    }

}
