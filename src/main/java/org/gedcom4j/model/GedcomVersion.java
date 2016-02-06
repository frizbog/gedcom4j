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

/**
 * Information about the version of the GEDCOM spec used
 * 
 * @author frizbog1
 * 
 */
public class GedcomVersion extends AbstractElement {

    /**
     * The version number for this GEDCOM
     */
    public SupportedVersion versionNumber = SupportedVersion.V5_5_1;

    /**
     * The form
     */
    public StringWithCustomTags gedcomForm = new StringWithCustomTags("LINEAGE-LINKED");

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        GedcomVersion other = (GedcomVersion) obj;
        if (gedcomForm == null) {
            if (other.gedcomForm != null) {
                return false;
            }
        } else if (!gedcomForm.equals(other.gedcomForm)) {
            return false;
        }
        if (versionNumber == null) {
            if (other.versionNumber != null) {
                return false;
            }
        } else if (!versionNumber.equals(other.versionNumber)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (gedcomForm == null ? 0 : gedcomForm.hashCode());
        result = prime * result + (versionNumber == null ? 0 : versionNumber.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "GedcomVersion [" + (versionNumber != null ? "versionNumber=" + versionNumber + ", " : "")
                + (gedcomForm != null ? "gedcomForm=" + gedcomForm + ", " : "") + (customTags != null ? "customTags=" + customTags : "") + "]";
    }
}
