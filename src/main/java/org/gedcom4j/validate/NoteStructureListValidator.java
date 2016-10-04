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

import org.gedcom4j.exception.ValidationException;
import org.gedcom4j.model.ModelElement;
import org.gedcom4j.model.NoteStructure;
import org.gedcom4j.validate.Validator.Finding;

/**
 * A validator for lists of {@link NoteStructure} object. See {@link Validator} for usage information.
 * 
 * @author frizbog1
 * 
 */
class NoteStructureListValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -7836612973656530761L;

    /**
     * The notes being validated
     */
    private List<NoteStructure> notes;

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
    NoteStructureListValidator(Validator validator, ModelElement parentObject) {
        super(validator);
        this.parentObject = parentObject;
        try {
            notes = (List<NoteStructure>) get(parentObject, "noteStructures");
        } catch (ClassCastException e) {
            throw new ValidationException("Field notestructures on object of type " + parentObject.getClass().getName()
                    + "did not return a List<Note>", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void validate() {
        checkUninitializedCollection(parentObject, "noteStructures");
        try {
            notes = (List<NoteStructure>) get(parentObject, "noteStructures");
        } catch (ClassCastException e) {
            throw new ValidationException("Field noteStructures on object of type " + parentObject.getClass().getName()
                    + "did not return a List<NoteStructure", e);
        }
        if (notes == null) {
            return;
        }
        DuplicateHandler<NoteStructure> dhn = new DuplicateHandler<>(notes);
        if (dhn.count() > 0) {
            Finding vf = newFinding(parentObject, Severity.WARNING, ProblemCode.DUPLICATE_VALUE, "noteStructures");
            if (mayRepair(vf)) {
                ModelElement before = makeCopy(parentObject);
                dhn.remove();
                vf.addRepair(new AutoRepair(before, makeCopy(parentObject)));
            }
        }
        checkForNullEntries(parentObject, "noteStructures");
        for (NoteStructure n : notes) {
            new NoteStructureValidator(getValidator(), n).validate();
        }

    }

}
