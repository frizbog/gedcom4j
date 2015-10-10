/*
 * Copyright (c) 2009-2015 Matthew R. Harrah
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
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.gedcom4j.model.Family;
import org.gedcom4j.model.FamilyEvent;
import org.gedcom4j.model.FamilyEventType;
import org.junit.Test;

/**
 * Test to check handling of skipped level numbers in malformed GEDCOM files. See Issue 87.
 * 
 * @author frizbog
 */
public class Issue87Test {

    /**
     * Test the scenario for issue 87 where a level number was skipped (a level 3 line immediately following a level 1
     * line, which is technically malformed).
     * 
     * @throws IOException
     * @throws GedcomParserException
     */
    @Test
    public void testIssue87() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/Issue87.ged");
        assertTrue(gp.warnings.isEmpty());

        // We should have an error in the collection about the problem
        assertEquals(1, gp.errors.size());
        String e = gp.errors.get(0);
        assertEquals("NOTE tag at line 46: Unable to find suitable parent node at level 2 under Line 45: 1 MARR null", e);

        // There should also be no NOTE tag under the MARR event
        Family family = gp.gedcom.families.get("@F1031@");
        assertNotNull(family);
        assertEquals(1, family.events.size());
        FamilyEvent marriage = family.events.get(0);
        assertEquals(FamilyEventType.MARRIAGE, marriage.type);
        assertTrue(marriage.notes.isEmpty());
    }

}
