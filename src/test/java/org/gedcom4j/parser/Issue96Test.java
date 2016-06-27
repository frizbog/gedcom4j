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
package org.gedcom4j.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Note;
import org.junit.Test;

/**
 * Test situation where a NOTE structure has both an ID definition and cross-reference, which is technically not
 * allowed.
 * 
 * @author frizbog
 */
public class Issue96Test {

    /**
     * Test issue 96
     * 
     * @throws IOException
     *             if the data cannot be read
     * @throws GedcomParserException
     *             if the data cannot be parsed
     */
    @Test
    public void testIssue96() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/issue96.ged");
        assertTrue(gp.errors.isEmpty());
        assertEquals(1, gp.warnings.size());
        String w = gp.warnings.get(0);

        // There should be one warning
        assertTrue(w.matches("NOTE line has both an XREF_ID \\(.*\\) and SUBMITTER_TEXT \\(.*\\) value between @ signs - "
                + "treating SUBMITTER_TEXT as string, not a cross-reference"));

        Gedcom g = gp.gedcom;
        assertNotNull(g);
        assertNotNull(g.notes);
        assertEquals(1, g.notes.size());
        Note note = g.notes.values().iterator().next();

        // Cross-reference (or what looks like one) treated like text?
        assertEquals("@N0@", note.lines.get(0));
        // ID treated like ID
        assertEquals("@N00000000000000005678@", note.xref);
    }

}
