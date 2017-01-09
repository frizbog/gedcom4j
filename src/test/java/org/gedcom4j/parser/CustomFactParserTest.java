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
package org.gedcom4j.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.CustomFact;
import org.gedcom4j.model.InMemoryGedcom;
import org.gedcom4j.model.IGedcom;
import org.gedcom4j.model.Individual;
import org.junit.Test;

/**
 * Test parsing a file loaded with custom tags
 * 
 * @author frizbog
 */
public class CustomFactParserTest {

    /**
     * Test loading a file full of custom tags
     * 
     * @throws IOException
     *             if the file cannot be read
     * @throws GedcomParserException
     *             if the file cannot be parsed
     */
    @Test
    @SuppressWarnings("PMD.SystemPrintln")
    public void test() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser(new InMemoryGedcom());
        gp.setStrictCustomTags(false);
        gp.load("sample/ftmcustomtags.ged");
        IGedcom g = gp.getGedcom();
        assertNotNull(g);

        for (String e : gp.getErrors()) {
            System.out.println(e);
        }
        assertEquals(0, gp.getErrors().size());
        for (String w : gp.getWarnings()) {
            System.out.println(w);
        }
        assertEquals(0, gp.getWarnings().size());

        Individual john = g.getIndividuals().get("@I1@");
        assertNotNull(john);
        assertEquals(19, john.getCustomFacts().size());

        CustomFact customFact = john.getCustomFactsWithTag("_MDCL").get(0);
        assertNotNull(customFact);
        assertEquals("Decapitated, but recovered", customFact.getDescription().getValue());
        assertNotNull(customFact.getDate());
        assertEquals("08 AUG 1978", customFact.getDate().getValue());
        assertEquals("San Antonio, Bexar, Texas, USA", customFact.getPlace().getPlaceName());

    }

}
