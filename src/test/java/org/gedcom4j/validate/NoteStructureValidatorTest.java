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

import org.gedcom4j.factory.IndividualFactory;
import org.gedcom4j.factory.Sex;
import org.gedcom4j.model.InMemoryGedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.NoteRecord;
import org.gedcom4j.model.NoteStructure;
import org.gedcom4j.model.TestHelper;
import org.junit.Test;

/**
 * Test for {@link NoteStructureValidator}
 * 
 * @author frizbog
 * 
 */
public class NoteStructureValidatorTest extends AbstractValidatorTestCase {

    /**
     * Test method for {@link org.gedcom4j.validate.NoteStructureValidator#validate()}.
     */
    @Test
    public void testNoteStructures() {
        InMemoryGedcom g = TestHelper.getMinimalGedcom();
        validator = new Validator(g);
        validator.setAutoRepairResponder(Validator.AUTO_REPAIR_NONE);

        Individual i = new IndividualFactory().create(g, "Bob", "Boberts", Sex.MALE, (String) null, null, null, null);
        NoteStructure n = new NoteStructure();
        i.getNoteStructures(true).add(n);

        validator.validate();
        assertFindingsContain(Severity.ERROR, n, ProblemCode.MISSING_REQUIRED_VALUE, "lines");
        n.getLines(true).add("Frying Pan");
        validator.validate();
        assertNoIssues();
    }

    /**
     * Test that you can't set a reference to a {@link NoteRecord} in a {@link NoteStructure} that has lines already
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetLinesWithReferenceFails() {
        InMemoryGedcom g = TestHelper.getMinimalGedcom();
        validator = new Validator(g);
        validator.setAutoRepairResponder(Validator.AUTO_REPAIR_NONE);

        NoteStructure n = new NoteStructure();
        NoteRecord nr = new NoteRecord("@N1@");
        g.getNotes().put("@N1@", nr);
        n.setNoteReference(nr);

        validator.validate();
        assertNoIssues();
        n.getLines(true).add("Frying Pan");
    }

    /**
     * Test that you can't set a reference to a {@link NoteRecord} in a {@link NoteStructure} that has lines already
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetReferenceWithLinesFails() {
        InMemoryGedcom g = TestHelper.getMinimalGedcom();
        validator = new Validator(g);
        validator.setAutoRepairResponder(Validator.AUTO_REPAIR_NONE);

        Individual i = new IndividualFactory().create(g, "Bob", "Boberts", Sex.MALE, (String) null, null, null, null);
        NoteStructure n = new NoteStructure();
        i.getNoteStructures(true).add(n);

        validator.validate();
        assertFindingsContain(Severity.ERROR, n, ProblemCode.MISSING_REQUIRED_VALUE, "lines");
        n.getLines(true).add("Frying Pan");
        validator.validate();
        assertNoIssues();

        n.setNoteReference(null);
        n.getLines(true).add("Foo");

        n.setNoteReference(new NoteRecord("@N1@")); // Boom

    }
}
