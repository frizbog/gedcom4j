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
 * Indicates an individual's membership, as a spouse, in a family
 * 
 * @author frizbog1
 * 
 */
public class FamilySpouse extends AbstractElement {
    /**
     * The family in which the person was one of the spouses
     */
    public Family family;

    /**
     * Notes on the membership
     */
    public List<Note> notes = new ArrayList<Note>(0);

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof FamilySpouse)) {
            return false;
        }
        FamilySpouse other = (FamilySpouse) obj;
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
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (family == null || family.xref == null ? 0 : family.xref.hashCode());
        result = prime * result + (notes == null ? 0 : notes.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "FamilySpouse [" + (family != null ? "family=" + family + ", " : "") + (notes != null ? "notes=" + notes + ", " : "")
                + (customTags != null ? "customTags=" + customTags : "") + "]";
    }
}
