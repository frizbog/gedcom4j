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

/**
 * Represent's an individuals membership, as a child, in a family
 * 
 * @author frizbog1
 */
public class FamilyChild extends AbstractElement {
    /**
     * The family to which the child belonged
     */
    public Family family;

    /**
     * Notes on the membership
     */
    public List<Note> notes = new ArrayList<Note>(0);

    /**
     * Pedigree information
     */
    public StringWithCustomTags pedigree;

    /**
     * Who did the adopting.
     */
    public AdoptedByWhichParent adoptedBy;

    /**
     * The status of this Family/Child relationship. New for GEDCOM 5.5.1. Supposed to be "challenged", "disproven", or
     * "proven", but this implementation allows any value.
     */
    public StringWithCustomTags status;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof FamilyChild)) {
            return false;
        }
        FamilyChild other = (FamilyChild) obj;
        if (adoptedBy == null) {
            if (other.adoptedBy != null) {
                return false;
            }
        } else if (!adoptedBy.equals(other.adoptedBy)) {
            return false;
        }
        if (family == null) {
            if (other.family != null) {
                return false;
            }
        } else {
            if (other.family == null) {
                return false;
            }
            if (family.xref == null) {
                if (other.family.xref != null) {
                    return false;
                }
            } else {
                if (!family.xref.equals(other.family.xref)) {
                    return false;
                }
            }
        }
        if (notes == null) {
            if (other.notes != null) {
                return false;
            }
        } else if (!notes.equals(other.notes)) {
            return false;
        }
        if (pedigree == null) {
            if (other.pedigree != null) {
                return false;
            }
        } else if (!pedigree.equals(other.pedigree)) {
            return false;
        }
        if (status == null) {
            if (other.status != null) {
                return false;
            }
        } else if (!status.equals(other.status)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (adoptedBy == null ? 0 : adoptedBy.hashCode());
        result = prime * result + (family == null || family.xref == null ? 0 : family.xref.hashCode());
        result = prime * result + (notes == null ? 0 : notes.hashCode());
        result = prime * result + (pedigree == null ? 0 : pedigree.hashCode());
        result = prime * result + (status == null ? 0 : status.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "FamilyChild [" + (family != null ? "family=" + family + ", " : "") + (notes != null ? "notes=" + notes + ", " : "")
                + (pedigree != null ? "pedigree=" + pedigree + ", " : "") + (adoptedBy != null ? "adoptedBy=" + adoptedBy + ", " : "")
                + (status != null ? "status=" + status + ", " : "") + (customTags != null ? "customTags=" + customTags : "") + "]";
    }
}
