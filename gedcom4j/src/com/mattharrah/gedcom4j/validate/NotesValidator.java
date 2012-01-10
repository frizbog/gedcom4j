/*
 * Copyright (c) 2009-2011 Matthew R. Harrah
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
package com.mattharrah.gedcom4j.validate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.mattharrah.gedcom4j.AbstractCitation;
import com.mattharrah.gedcom4j.Note;

/**
 * A validator for lists of {@link Note}s
 * 
 * @author frizbog1
 * 
 */
public class NotesValidator extends AbstractValidator {

    /**
     * The notes being validated
     */
    private List<Note> notes;
    /**
     * The object that contains the notes
     */
    private Object parentObject;

    /**
     * Constructor
     * 
     * @param rootValidator
     *            the root validator
     * @param parentObject
     *            the object containing the notes
     * @param notes
     *            the list of notes to be validated
     */
    public NotesValidator(GedcomValidator rootValidator, Object parentObject,
            List<Note> notes) {
        this.rootValidator = rootValidator;
        this.parentObject = parentObject;
        this.notes = notes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mattharrah.gedcom4j.validate.AbstractValidator#validate()
     */
    @Override
    protected void validate() {
        if (notes == null) {
            if (rootValidator.autorepair) {
                try {
                    Field f = parentObject.getClass().getField("notes");
                    f.set(parentObject, new ArrayList<Note>());
                    addInfo("Notes collection on "
                            + parentObject.getClass().getSimpleName()
                            + " was null - autorepaired");
                } catch (SecurityException e) {
                    throw new GedcomValidationException(
                            "Could not autorepair null notes collection on "
                                    + parentObject.getClass().getSimpleName(),
                            e);
                } catch (NoSuchFieldException e) {
                    throw new GedcomValidationException(
                            "Could not autorepair null notes collection on "
                                    + parentObject.getClass().getSimpleName(),
                            e);
                } catch (IllegalArgumentException e) {
                    throw new GedcomValidationException(
                            "Could not autorepair null notes collection on "
                                    + parentObject.getClass().getSimpleName(),
                            e);
                } catch (IllegalAccessException e) {
                    throw new GedcomValidationException(
                            "Could not autorepair null notes collection on "
                                    + parentObject.getClass().getSimpleName(),
                            e);
                }
            } else {
                addError("Notes collection on "
                        + parentObject.getClass().getSimpleName() + " is null");
            }
        } else {
            int i = 0;
            for (Note n : notes) {
                i++;
                if (n.xref == null) {
                    // Kind without an xref must have lines
                    if (n.lines.isEmpty()) {
                        addError("Note " + i + " without xref has no lines", n);
                    }
                }
                checkOptionalString(n.recIdNumber, "automated record id", n);
                if (n.citations == null) {
                    if (rootValidator.autorepair) {
                        addInfo("Source citations collection on note was null - autorepaired");
                    } else {
                        addError("Source citations collection on note is null",
                                n);
                    }
                } else {
                    for (AbstractCitation c : n.citations) {
                        new CitationValidator(rootValidator, c).validate();
                    }
                }
                if (n.userReferences == null) {
                    if (rootValidator.autorepair) {
                        addInfo("User references collection on note was null - autorepaired");
                    } else {
                        addError("User references collection on note is null",
                                n);
                    }
                } else {
                    checkUserReferences(n.userReferences, n);
                }
                checkChangeDate(n.changeDate, n);
            }
        }

    }

}
