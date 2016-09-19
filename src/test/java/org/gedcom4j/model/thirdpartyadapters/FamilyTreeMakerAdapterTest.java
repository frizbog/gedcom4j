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
package org.gedcom4j.model.thirdpartyadapters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.Source;
import org.gedcom4j.parser.GedcomParser;
import org.junit.Before;
import org.junit.Test;

/**
 * @author frizbog
 *
 */
public class FamilyTreeMakerAdapterTest {

    /**
     * The gedcom test fixture we're going to work with
     */
    private Gedcom g;

    /** John, one of the people in the GEDCOM test file. */
    private Individual john;

    /** Jesse, one of the people in the GEDCOM test file. */
    private Individual jesse;

    /** June, one of the people in the GEDCOM test file. */
    private Individual june;

    /** The family in the GEDCOM test file. */
    private Family family;

    /** The photo in the GEDCOM test file. */
    private Multimedia photo;

    /** The source in the GEDCOM test file. */
    private Source source;

    /**
     * Setup test
     * 
     * @throws GedcomParserException
     *             if the gedcom cannot be parsed
     * @throws IOException
     *             if the file cannot be read
     */
    @Before
    public void setUp() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/ftmcustomtags.ged");
        g = gp.getGedcom();
        john = g.getIndividuals().get("@I1@");
        jesse = g.getIndividuals().get("@I2@");
        june = g.getIndividuals().get("@I3@");
        family = g.getFamilies().get("@F1@");
        photo = g.getMultimedia().get("@M1@");
        source = g.getSources().get("@S1@");
    }

    /**
     * Test {@link FamilyTreeMakerAdapter#getCauseOfDeath(Individual)}
     */
    @Test
    public void testCauseOfDeathNegative() {
        FamilyTreeMakerAdapter a = new FamilyTreeMakerAdapter();
        List<String> cod = a.getCauseOfDeath(jesse);
        assertNotNull(cod);
        assertEquals(0, cod.size());
    }

    /**
     * Test {@link FamilyTreeMakerAdapter#getCauseOfDeath(Individual)}.
     */
    @Test
    public void testCauseOfDeathPositive() {
        FamilyTreeMakerAdapter a = new FamilyTreeMakerAdapter();
        List<String> cod = a.getCauseOfDeath(john);
        assertNotNull(cod);
        assertEquals(1, cod.size());
        assertEquals("Alien abduction", cod.get(0));
    }

}
