package org.gedcom4j.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.gedcom4j.model.*;
import org.junit.Test;

/**
 * Test for Issue 80 - the parser should look for LATI for latitudes
 * 
 * @author frizbog
 */
public class Issue80ParserTest {

    /**
     * Test for Issue 64. Load a file with blank lines that were deliberately introduced.
     * 
     * @throws IOException
     *             if there is a problem read/writing data
     * @throws GedcomParserException
     *             if there is a problem parsing the input file
     */
    @Test
    public void testIssue80() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/Issue 80.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue(gp.warnings.isEmpty());
        assertNotNull(gp.gedcom);
        assertEquals(1, gp.gedcom.individuals.size());

        Individual i = gp.gedcom.individuals.values().iterator().next();
        assertNotNull(i);
        assertNotNull(i.events);
        assertEquals(1, i.events.size());

        Event e = i.events.get(0);
        assertNotNull(e);
        assertTrue(e instanceof IndividualEvent);

        IndividualEvent ie = (IndividualEvent) e;
        assertEquals(IndividualEventType.BIRTH, ie.type);
        assertNotNull(ie.place);

        Place p = ie.place;
        assertEquals("36.72", p.latitude.value);
        assertEquals("-76.58", p.longitude.value);
    }
}
