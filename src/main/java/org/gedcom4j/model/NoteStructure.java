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
package org.gedcom4j.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gedcom4j.Options;

/**
 * Corresponds to NOTE_STRUCTURE in the GEDCOM standard. Either references a root-level {@link NoteRecord}, or is a simple in-line
 * note (with just lines of texts) that does not refer to a root-level note. Does not have an xref, citations, etc...those are on
 * {@link NoteRecord} objects.
 * 
 * @author frizbog1
 */
public class NoteStructure extends AbstractElement {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 8355989906882622025L;

    /**
     * The lines of text of this note
     */
    private List<String> lines = getLines(Options.isCollectionInitializationEnabled());

    /**
     * Reference to a root-level {@link NoteRecord}
     */
    private NoteRecord noteReference;

    /** Default constructor */
    public NoteStructure() {
        // Default constructor does nothing
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            the other object to copy
     */
    public NoteStructure(NoteStructure other) {
        super(other);
        if (other.lines != null) {
            lines = new ArrayList<>(other.lines);
        }
        if (other.noteReference != null) {
            noteReference = new NoteRecord(other.noteReference);
        }
    }

    /**
     * Gets the lines.
     *
     * @return the lines
     */
    public List<String> getLines() {
        return lines;
    }

    /**
     * Get the lines
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the lines. If the note structure has a non-null reference to a root-level {@link NoteRecord}, this method will always
     *         return an immutable empty list. To get a mutable list of lines, you must remove the note reference first.
     */
    public List<String> getLines(boolean initializeIfNeeded) {
        if (noteReference != null) {
            return Collections.unmodifiableList((List<String>) Collections.EMPTY_LIST);
        }
        if (initializeIfNeeded && lines == null) {
            lines = new ArrayList<>(0);
        }
        return lines;
    }

    /**
     * Get the noteReference
     * 
     * @return the noteReference
     */
    public NoteRecord getNoteReference() {
        return noteReference;
    }

    /**
     * Set the noteReference
     * 
     * @param noteReference
     *            the noteReference to set
     * @throws IllegalArgumentException
     *             when setting a non-null reference to a {@link NoteRecord} if there are already lines of text in this
     *             {@link NoteStructure} -- these are mutually exclusive.
     */
    public void setNoteReference(NoteRecord noteReference) {
        if (noteReference != null && (lines != null && !lines.isEmpty())) {
            throw new IllegalArgumentException(
                    "Cannot set a note reference when there are lines of text. Clear the lines of text or set the lines property to null first.");
        }
        this.noteReference = noteReference;
    }
}
