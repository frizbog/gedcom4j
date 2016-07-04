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
 * Represents an Association between two individuals. Corresponds to ASSOCIATION_STRUCTURE in the GEDCOM spec.
 * 
 * @author frizbog1
 */
public class Association extends AbstractElement {
    /**
     * Relationship description
     */
    private StringWithCustomTags relationship;

    /**
     * The XREF to the associated entity
     */
    private String associatedEntityXref;

    /**
     * The type of the associated entity
     */
    private StringWithCustomTags associatedEntityType;

    /**
     * The citations for this association
     */
    private List<AbstractCitation> citations = Options.isCollectionInitializationEnabled() ? new ArrayList<AbstractCitation>(0) : null;

    /**
     * Notes about this object
     */
    private List<Note> notes = Options.isCollectionInitializationEnabled() ? new ArrayList<Note>(0) : null;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof Association)) {
            return false;
        }
        Association other = (Association) obj;
        if (associatedEntityType == null) {
            if (other.associatedEntityType != null) {
                return false;
            }
        } else if (!associatedEntityType.equals(other.associatedEntityType)) {
            return false;
        }
        if (associatedEntityXref == null) {
            if (other.associatedEntityXref != null) {
                return false;
            }
        } else if (!associatedEntityXref.equals(other.associatedEntityXref)) {
            return false;
        }
        if (citations == null) {
            if (other.citations != null) {
                return false;
            }
        } else if (!citations.equals(other.citations)) {
            return false;
        }
        if (notes == null) {
            if (other.notes != null) {
                return false;
            }
        } else if (!notes.equals(other.notes)) {
            return false;
        }
        if (relationship == null) {
            if (other.relationship != null) {
                return false;
            }
        } else if (!relationship.equals(other.relationship)) {
            return false;
        }
        return true;
    }

    /**
     * Get the associatedEntityType
     * 
     * @return the associatedEntityType
     */
    public StringWithCustomTags getAssociatedEntityType() {
        return associatedEntityType;
    }

    /**
     * Get the associatedEntityXref
     * 
     * @return the associatedEntityXref
     */
    public String getAssociatedEntityXref() {
        return associatedEntityXref;
    }

    /**
     * Get the citations
     * 
     * @return the citations
     */
    public List<AbstractCitation> getCitations() {
        return citations;
    }

    /**
     * Get the citations
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed?
     * 
     * @return the citations
     */
    public List<AbstractCitation> getCitations(boolean initializeIfNeeded) {
        if (initializeIfNeeded && citations == null) {
            citations = new ArrayList<AbstractCitation>(0);
        }
        return citations;
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

    /**
     * Get the relationship
     * 
     * @return the relationship
     */
    public StringWithCustomTags getRelationship() {
        return relationship;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (associatedEntityType == null ? 0 : associatedEntityType.hashCode());
        result = prime * result + (associatedEntityXref == null ? 0 : associatedEntityXref.hashCode());
        result = prime * result + (citations == null ? 0 : citations.hashCode());
        result = prime * result + (notes == null ? 0 : notes.hashCode());
        result = prime * result + (relationship == null ? 0 : relationship.hashCode());
        return result;
    }

    /**
     * Set the associatedEntityType
     * 
     * @param associatedEntityType
     *            the associatedEntityType to set
     */
    public void setAssociatedEntityType(StringWithCustomTags associatedEntityType) {
        this.associatedEntityType = associatedEntityType;
    }

    /**
     * Set the associatedEntityXref
     * 
     * @param associatedEntityXref
     *            the associatedEntityXref to set
     */
    public void setAssociatedEntityXref(String associatedEntityXref) {
        this.associatedEntityXref = associatedEntityXref;
    }

    /**
     * Set the relationship
     * 
     * @param relationship
     *            the relationship to set
     */
    public void setRelationship(StringWithCustomTags relationship) {
        this.relationship = relationship;
    }

    @Override
    public String toString() {
        return "Association [" + (relationship != null ? "relationship=" + relationship + ", " : "") + (associatedEntityXref != null ? "associatedEntityXref="
                + associatedEntityXref + ", " : "") + (associatedEntityType != null ? "associatedEntityType=" + associatedEntityType + ", " : "")
                + (citations != null ? "citations=" + citations + ", " : "") + (notes != null ? "notes=" + notes + ", " : "") + (getCustomTags() != null
                        ? "customTags=" + getCustomTags() : "") + "]";
    }
}
