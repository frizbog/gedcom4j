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
package org.gedcom4j.validate;

import org.gedcom4j.model.CitationWithSource;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Note;
import org.gedcom4j.model.TestHelper;
import org.gedcom4j.model.UserReference;
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
        validator = new Validator(g);
        validator.setAutoRepairResponder(Validator.AUTO_REPAIR_NONE);

        Note n = new Note();
        n.setXref("@N0001@");
        g.getNotes().put(n.getXref(), n);

        validator.validate();
        assertNoIssues();

        n.getCitations(true).clear();
        validator.validate();
        assertNoIssues();
        CitationWithSource cws = new CitationWithSource();
        n.getCitations(true).add(cws);
        validator.validate();
        assertFindingsContain(Severity.ERROR, cws, ProblemCode.MISSING_REQUIRED_VALUE, "reference");

        n.getUserReferences(true).clear();
        validator.validate();
        assertFindingsContain(Severity.ERROR, n, ProblemCode.MISSING_REQUIRED_VALUE, "userReference");
        UserReference u = new UserReference();
        n.getUserReferences(true).add(u);
        validator.validate();
        assertFindingsContain(Severity.ERROR, u, ProblemCode.MISSING_REQUIRED_VALUE, "referenceNum");

    }

    /**
     * Test method for {@link org.gedcom4j.validate.NoteValidator#validate()}.
     */
    @Test
    public void testNotesWithoutXref() {
        Gedcom g = TestHelper.getMinimalGedcom();
        validator = new Validator(g);
        validator.setAutoRepairResponder(Validator.AUTO_REPAIR_NONE);

        Note n = new Note();
        n.setXref(null);
        g.getHeader().getSubmitter().getNotes(true).add(n);

        // Notes without xrefs must have lines of text
        validator.validate();
        assertFindingsContain(Severity.ERROR, n, ProblemCode.XREF_INVALID, "xref");
        n.getLines(true).add("Frying Pan");
        validator.validate();
        assertNoIssues();

        n.getCitations(true).clear();
        validator.validate();
        assertNoIssues();
        CitationWithSource cws = new CitationWithSource();
        n.getCitations(true).add(cws);
        validator.validate();
        assertFindingsContain(Severity.ERROR, cws, ProblemCode.ILLEGAL_VALUE, "reference");

        n.getUserReferences(true).clear();
        validator.validate();
        assertFindingsContain(Severity.ERROR, cws, ProblemCode.MISSING_REQUIRED_VALUE, "reference");
        UserReference u = new UserReference();
        n.getUserReferences(true).add(u);
        validator.validate();
        assertFindingsContain(Severity.ERROR, u, ProblemCode.LIST_WITH_NULL_VALUE, "userReference");

    }

}
