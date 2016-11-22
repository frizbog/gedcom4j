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

import static org.junit.Assert.assertNotNull;

import org.gedcom4j.model.Individual;
import org.gedcom4j.model.NoteStructure;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test for {@link NoteStructureListValidator}
 * 
 * @author frizbog
 */
public class NoteStructureListValidatorTest extends AbstractValidatorTestCase {

    /**
     * JUnit rule
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Test {@link NoteStructureListValidator#NoteStructureListValidator(Validator, org.gedcom4j.model.ModelElement)}
     */
    @Test
    public void testWithDuplicates() {
        Individual i = new Individual();
        NoteStructureListValidator nslv = new NoteStructureListValidator(validator, i);
        assertNotNull(nslv);
        NoteStructure n = new NoteStructure();
        n.getLines(true).add("Foo");
        i.getNoteStructures(true).add(n);

        nslv.validate();
        assertNoIssues();

        i.getNoteStructures(true).add(new NoteStructure());
        i.getNoteStructures(true).add(new NoteStructure());

        nslv.validate();
        assertFindingsContain(Severity.WARNING, Individual.class, ProblemCode.DUPLICATE_VALUE.getCode(), "noteStructures");
    }

    /**
     * Test {@link NoteStructureListValidator#NoteStructureListValidator(Validator, org.gedcom4j.model.ModelElement)}
     */
    @Test
    public void testWithDuplicatesWithAutoRepair() {
        validator.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);

        Individual i = new Individual();
        NoteStructureListValidator nslv = new NoteStructureListValidator(validator, i);
        assertNotNull(nslv);
        NoteStructure n = new NoteStructure();
        n.getLines(true).add("Foo");
        i.getNoteStructures(true).add(n);

        nslv.validate();
        assertNoIssues();

        i.getNoteStructures(true).add(new NoteStructure());
        i.getNoteStructures(true).add(new NoteStructure());

        nslv.validate();
        assertFindingsContain(Severity.WARNING, Individual.class, ProblemCode.DUPLICATE_VALUE.getCode(), "noteStructures");
    }

}
