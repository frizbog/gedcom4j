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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.Options;
import org.gedcom4j.exception.GedcomValidationException;
import org.gedcom4j.model.Note;

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
    private final Object parentObject;

    /**
     * Constructor
     * 
     * @param validator
     *            the root validator
     * @param parentObject
     *            the object containing the notes
     * @param notes
     *            the list of notes to be validated
     */
    NotesValidator(Validator validator, Object parentObject, List<Note> notes) {
        this.validator = validator;
        this.parentObject = parentObject;
        this.notes = notes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        if (notes == null && Options.isCollectionInitializationEnabled()) {
            if (validator.isAutorepairEnabled()) {
                try {
                    Field f = parentObject.getClass().getField("notes");
                    f.set(parentObject, new ArrayList<Note>(0));
                    addInfo("Notes collection on " + parentObject.getClass().getSimpleName() + " was null - autorepaired");
                } catch (SecurityException e) {
                    throw new GedcomValidationException("Could not autorepair null notes collection on " + parentObject.getClass()
                            .getSimpleName(), e);
                } catch (NoSuchFieldException e) {
                    throw new GedcomValidationException("Could not autorepair null notes collection on " + parentObject.getClass()
                            .getSimpleName(), e);
                } catch (IllegalArgumentException e) {
                    throw new GedcomValidationException("Could not autorepair null notes collection on " + parentObject.getClass()
                            .getSimpleName(), e);
                } catch (IllegalAccessException e) {
                    throw new GedcomValidationException("Could not autorepair null notes collection on " + parentObject.getClass()
                            .getSimpleName(), e);
                }
            } else {
                addError("Notes collection on " + parentObject.getClass().getSimpleName() + " is null");
            }
        } else {
            int i = 0;
            if (notes != null) {
                if (validator.isAutorepairEnabled()) {
                    int dups = new DuplicateEliminator<>(notes).process();
                    if (dups > 0) {
                        validator.addInfo(dups + " duplicate notes found and removed", notes);
                    }
                }
                for (Note n : notes) {
                    i++;
                    new NoteValidator(validator, i, n).validate();
                }
            }
        }

    }

}
