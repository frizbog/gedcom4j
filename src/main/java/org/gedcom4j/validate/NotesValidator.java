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

import java.util.List;

import org.gedcom4j.Options;
import org.gedcom4j.model.ModelElement;
import org.gedcom4j.model.Note;
import org.gedcom4j.validate.Validator.Finding;

/**
 * A validator for lists of {@link Note} object. See {@link GedcomValidator} for usage information.
 * 
 * @author frizbog1
 * 
 */
class NotesValidator extends AbstractValidator {

    /**
     * The notes being validated
     */
    private final List<Note> notes;

    /**
     * The object that contains the notes
     */
    private final ModelElement parentObject;

    /**
     * Constructor
     * 
     * @param validator
     *            the root validator
     * @param parentObject
     *            the object containing the notes
     */
    @SuppressWarnings("unchecked")
    NotesValidator(Validator validator, ModelElement parentObject) {
        this.validator = validator;
        this.parentObject = parentObject;
        notes = (List<Note>) get(parentObject, "notes");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        if (notes == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(parentObject, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, "notes");
            initializeCollectionIfAllowed(vf);
        }
        if (notes == null) {
            return;
        }
        DuplicateHandler<Note> dhn = new DuplicateHandler<>(notes);
        if (dhn.count() > 0) {
            Finding vf = validator.newFinding(parentObject, Severity.WARNING, ProblemCode.DUPLICATE_VALUE, "notes");
            if (validator.mayRepair(vf)) {
                ModelElement before = makeCopy(parentObject);
                dhn.remove();
                vf.addRepair(new AutoRepair(before, makeCopy(parentObject)));
            }
        }
        int i = 0;
        for (Note n : notes) {
            i++;
            new NoteValidator(validator, n).validate();
        }

    }

}
