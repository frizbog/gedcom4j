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
package org.gedcom4j.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.query.Finder;
import org.junit.Test;

/**
 * Test for Issue 99, where {@link Individual#getDescendants()} was returning only one generation of descendants and was
 * not recursing properly.
 * 
 * @author frizbog
 *
 */
public class Issue99Test {

    /**
     * Test that {@link Individual#getDescendants()} works properly
     * 
     * @throws GedcomParserException
     *             if the GEDCOM file cannot be parsed
     * @throws IOException
     *             if the GEDCOM file cannot be read.
     */
    @Test
    public void testGetDescendants() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/willis.ged");
        assertEquals(761, gp.getGedcom().getIndividuals().size());
        Finder f = new Finder(gp.getGedcom());
        List<Individual> matches = f.findByName("Willis", "Edmund Henry");
        assertNotNull(matches);
        assertEquals(1, matches.size());
        Individual i = matches.get(0);
        Set<Individual> d = i.getDescendants();
        assertNotNull(d);
        assertEquals(54, d.size());
    }

}
