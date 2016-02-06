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
 * A citation without a source. Corresponds to the second form of the SOURCE_CITATION structure (which you'd do in
 * Pascal with a variant record, but here we use subclasses of a parent abstract class).
 * 
 * 
 * @author frizbog1
 * 
 */
public class CitationWithoutSource extends AbstractCitation {
    /**
     * Lines of text describing this citation
     */
    public List<String> description = new ArrayList<String>();

    /**
     * Lines of Lines of text from the source (yeah, really)
     */
    public List<List<String>> textFromSource = new ArrayList<List<String>>();

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
        CitationWithoutSource other = (CitationWithoutSource) obj;
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (notes == null) {
            if (other.notes != null) {
                return false;
            }
        } else if (!notes.equals(other.notes)) {
            return false;
        }
        if (textFromSource == null) {
            if (other.textFromSource != null) {
                return false;
            }
        } else if (!textFromSource.equals(other.textFromSource)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (description == null ? 0 : description.hashCode());
        result = prime * result + (notes == null ? 0 : notes.hashCode());
        result = prime * result + (textFromSource == null ? 0 : textFromSource.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "CitationWithoutSource [" + (description != null ? "description=" + description + ", " : "")
                + (textFromSource != null ? "textFromSource=" + textFromSource + ", " : "") + (notes != null ? "notes=" + notes + ", " : "")
                + (customTags != null ? "customTags=" + customTags : "") + "]";
    }
}
