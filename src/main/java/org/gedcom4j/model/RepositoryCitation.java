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
package org.gedcom4j.model;

import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.Options;

/**
 * A citation to a repository
 * 
 * @author frizbog1
 * 
 */
public class RepositoryCitation extends AbstractElement {

    /**
     * The xref of the repository. Kept as a string copy of the xref deliberately to avoid circular references in the
     * object graph (particularly, Note -&gt; Citation -&gt; Source -&gt; Repository -&gt; Note -&gt; Citation...)
     */
    public String repositoryXref;

    /**
     * Call numbers
     */
    public List<SourceCallNumber> callNumbers = new ArrayList<SourceCallNumber>(0);

    /**
     * Notes about this object
     */
    private List<Note> notes = Options.isCollectionInitializationEnabled() ? getNotes(true) : null;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof RepositoryCitation)) {
            return false;
        }
        RepositoryCitation other = (RepositoryCitation) obj;
        if (callNumbers == null) {
            if (other.callNumbers != null) {
                return false;
            }
        } else if (!callNumbers.equals(other.callNumbers)) {
            return false;
        }
        if (notes == null) {
            if (other.notes != null) {
                return false;
            }
        } else if (!notes.equals(other.notes)) {
            return false;
        }
        if (repositoryXref == null) {
            if (other.repositoryXref != null) {
                return false;
            }
        } else if (!repositoryXref.equals(other.repositoryXref)) {
            return false;
        }
        return true;
    }

    /**
     * Get the notes
     * 
     * @return the notes
     */
    public List<Note> getNotes() {
        return notes;
    }

    /**
     * Get the notes
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed?
     * 
     * @return the notes
     */
    public List<Note> getNotes(boolean initializeIfNeeded) {
        if (initializeIfNeeded && notes == null) {
            notes = new ArrayList<Note>(0);
        }
        return notes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (callNumbers == null ? 0 : callNumbers.hashCode());
        result = prime * result + (notes == null ? 0 : notes.hashCode());
        result = prime * result + (repositoryXref == null ? 0 : repositoryXref.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "RepositoryCitation [" + (repositoryXref != null ? "repositoryXref=" + repositoryXref + ", " : "") + (notes != null ? "notes=" + notes + ", "
                : "") + (callNumbers != null ? "callNumbers=" + callNumbers + ", " : "") + (getCustomTags() != null ? "customTags=" + getCustomTags() : "")
                + "]";
    }

}
