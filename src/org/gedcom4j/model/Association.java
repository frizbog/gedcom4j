/*
 * Copyright (c) 2009-2012 Matthew R. Harrah
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

/**
 * Represents an Association between two individuals. Corresponds to ASSOCIATION_STRUCTURE in the GEDCOM spec.
 * 
 * @author frizbog1
 */
public class Association extends AbstractElement {
    /**
     * Relationship description
     */
    public StringWithCustomTags relationship;

    /**
     * The XREF to the associated entity
     */
    public String associatedEntityXref;

    /**
     * The type of the associated entity
     */
    public StringWithCustomTags associatedEntityType;

    /**
     * The citations for this association
     */
    public List<AbstractCitation> citations = new ArrayList<AbstractCitation>();

    /**
     * Notes about this association
     */
    public List<Note> notes = new ArrayList<Note>();

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
}
