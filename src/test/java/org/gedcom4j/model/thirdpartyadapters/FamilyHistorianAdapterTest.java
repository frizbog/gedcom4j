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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.parser.GedcomParser;
import org.junit.Before;
import org.junit.Test;

/**
 * @author frizbog
 *
 */
public class FamilyHistorianAdapterTest {

    /**
     * The test gedcom we read from the sample file that HAS custom tags
     */
    private Gedcom gedcomWithCustomTags;

    /**
     * The main person in the sample file with most of the custom facts
     */
    private Individual tom;

    /**
     * The class under test
     */
    private final FamilyHistorianAdapter fha = new FamilyHistorianAdapter();

    /**
     * The test gedcom we read from the sample file that DOES NOT have custom tags, for negative testing
     */
    private Gedcom gedcomWithoutCustomTags;

    /**
     * Sets up each test
     * 
     * @throws GedcomParserException
     *             if the sample file cannot be parsed
     * @throws IOException
     *             if the sample file cannot be read
     */
    @Before
    public void setUp() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/famhistcustomtags.ged");
        gedcomWithCustomTags = gp.getGedcom();
        assertNotNull(gedcomWithCustomTags);

        gp = new GedcomParser();
        gp.load("sample/famhistnocustomtags.ged");
        gedcomWithoutCustomTags = gp.getGedcom();
        assertNotNull(gedcomWithoutCustomTags);

        assertNotSame(gedcomWithCustomTags, gedcomWithoutCustomTags);

        tom = gedcomWithCustomTags.getIndividuals().get("@I1@");
        assertNotNull(tom);
    }

    /**
     * Negative test {@link FamilyHistorianAdapter#getRootIndividual(Gedcom)}
     */
    @Test
    public void testRootIndividualNegative() {
        Individual individual = fha.getRootIndividual(gedcomWithoutCustomTags);
        assertNull(individual);
    }

    /**
     * Positive test {@link FamilyHistorianAdapter#getRootIndividual(Gedcom)}
     */
    @Test
    public void testRootIndividualPositive() {
        Individual individual = fha.getRootIndividual(gedcomWithCustomTags);
        assertNotNull(individual);
        assertEquals(tom, individual);
        assertSame(tom, individual);
    }
}
