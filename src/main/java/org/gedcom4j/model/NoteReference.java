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

/**
 * A reference to an note, which may have custom facts on the reference that are not custom facts about the note themselves.
 * 
 * @author frizbog
 */
public class NoteReference extends AbstractElement {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -1855269504871281907L;

    /**
     * The note referred to
     */
    Note note;

    /**
     * Default constructor
     */
    public NoteReference() {
        // Default constructor does nothing
    }

    /**
     * Create a new NoteReference object
     * 
     * @param note
     *            the note being referred to
     */
    public NoteReference(Note note) {
        this.note = note;
    }

    /**
     * Copy constructor.
     * 
     * @param other
     *            the other NoteReference being copied
     */
    public NoteReference(NoteReference other) {
        super(other);
        note = new Note(other.note);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof NoteReference)) {
            return false;
        }
        NoteReference other = (NoteReference) obj;
        if (note == null) {
            if (other.note != null) {
                return false;
            }
        } else if (!note.equals(other.note)) {
            return false;
        }
        return true;
    }

    /**
     * Get the note
     * 
     * @return the note
     */
    public Note getNote() {
        return note;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((note == null) ? 0 : note.hashCode());
        return result;
    }

    /**
     * Set the note
     * 
     * @param note
     *            the note to set
     */
    public void setNote(Note note) {
        this.note = note;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(25);
        builder.append("NoteReference [");
        if (note != null) {
            builder.append("note=");
            builder.append(note);
            builder.append(", ");
        }
        if (customFacts != null) {
            builder.append("customFacts=");
            builder.append(customFacts);
        }
        builder.append("]");
        return builder.toString();
    }

}
