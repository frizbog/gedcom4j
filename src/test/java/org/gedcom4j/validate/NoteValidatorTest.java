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

package org.gedcom4j.validate;

import java.util.ArrayList;

import org.gedcom4j.model.*;
import org.junit.Test;

/**
 * Test for {@link NoteValidator}
 * 
 * @author frizbog
 * 
 */
public class NoteValidatorTest extends AbstractValidatorTestCase {

    /**
     * Test method for {@link org.gedcom4j.validate.NoteValidator#validate()}.
     */
    @Test
    public void testNotesAtRootLevel() {
        Gedcom g = TestHelper.getMinimalGedcom();
        rootValidator.gedcom = g;
        rootValidator.autorepair = false;

        Note n = new Note();
        n.setXref("@N0001@");
        g.getNotes().put(n.getXref(), n);

        rootValidator.validate();
        assertNoIssues();

        n.getCitations(true).clear();
        rootValidator.validate();
        assertNoIssues();
        n.getCitations().add(new CitationWithSource());
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "CitationWithSource", "non-null", "reference");

        n.getUserReferences().clear();
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "CitationWithSource", "non-null", "reference");
        n.getUserReferences().add(new UserReference());
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "reference number", "null");

        n.lines = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "lines", "text", "null");
        n.lines = new ArrayList<String>();
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "reference number", "null");
    }

    /**
     * Test method for {@link org.gedcom4j.validate.NoteValidator#validate()}.
     */
    @Test
    public void testNotesWithoutXref() {
        Gedcom g = TestHelper.getMinimalGedcom();
        rootValidator.gedcom = g;
        rootValidator.autorepair = false;

        Note n = new Note();
        n.setXref(null);
        g.getHeader().getSubmitter().getNotes().add(n);

        // Notes without xrefs must have lines of text
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "note", "without xref", "lines");
        n.lines.add("Frying Pan");
        rootValidator.validate();
        assertNoIssues();

        n.getCitations(true).clear();
        rootValidator.validate();
        assertNoIssues();
        n.getCitations().add(new CitationWithSource());
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "CitationWithSource", "non-null", "reference");

        n.getUserReferences().clear();
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "CitationWithSource", "non-null", "reference");
        n.getUserReferences().add(new UserReference());
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "reference number", "UserReference", "null");

        n.lines = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "lines", "text", "null");
        n.lines = new ArrayList<String>();
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "note", "without xref", "lines");
        n.lines.add("Frying Pan");
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "reference number", "UserReference", "null");
    }

}
