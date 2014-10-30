package org.gedcom4j.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

/**
 * Test for Issue 64 - the parser should be tolerant of blank lines
 * 
 * @author frizbog
 */
public class Issue64Test {

    /**
     * Test for Issue 64. Load a file with blank lines that were deliberately introduced.
     * 
     * @throws IOException
     *             if there is a problem read/writing data
     * @throws GedcomParserException
     *             if there is a problem parsing the input file
     */
    @Test
    public void testIssue64() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/Issue 64.ged");
        assertNotNull(gp.gedcom);
        assertEquals(2, gp.gedcom.individuals.size());
        assertEquals(1, gp.gedcom.families.size());
    }
}
