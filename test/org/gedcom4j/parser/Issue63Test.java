/*
 * Copyright (c) 2009-2014 Matthew R. Harrah
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

package org.gedcom4j.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.gedcom4j.model.Family;
import org.gedcom4j.model.Gedcom;
import org.junit.Test;

/**
 * Test for Issue 63, where the parser failed to associate individuals with families if they are not listed in a
 * specific order in the GEDCOM file.
 * 
 * @author frizbog
 */
public class Issue63Test {

    /**
     * Test with sample file that recreates reported condition.
     * 
     * @throws GedcomParserException
     *             if the file can't be parsed
     * @throws IOException
     *             if the file cannot be read
     */
    @Test
    public void test() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/Issue 63.ged");
        Gedcom g = gp.gedcom;
        assertNotNull(g);
        assertEquals(1, g.families.size());
        assertEquals(2, g.individuals.size());

        Family family = g.families.get("@F001@");
        assertNotNull(family);
        assertNotNull(family.husband);
        assertEquals("@I001@", family.husband.xref);
        assertEquals("Husband /Gedcom/", family.husband.names.get(0).basic);

        assertNotNull(family.wife);
        assertEquals("@I002@", family.wife.xref);

        // Things above this line passed (and below this line failed) prior to the fix for Issue 63
        assertEquals("Wife /Gedcom/", family.wife.names.get(0).basic);

    }
}
