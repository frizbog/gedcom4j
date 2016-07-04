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
 * A class for source citation data.
 * 
 * @author frizbog1
 */
public class CitationData extends AbstractElement {
    /**
     * The date of the entry
     */
    private StringWithCustomTags entryDate;

    /**
     * The source text - one or more lines of it
     */
    private List<List<String>> sourceText = getSourceText(Options.isCollectionInitializationEnabled());

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
        CitationData other = (CitationData) obj;
        if (entryDate == null) {
            if (other.entryDate != null) {
                return false;
            }
        } else if (!entryDate.equals(other.entryDate)) {
            return false;
        }
        if (sourceText == null) {
            if (other.sourceText != null) {
                return false;
            }
        } else if (!sourceText.equals(other.sourceText)) {
            return false;
        }
        return true;
    }

    /**
     * Get the entryDate
     * 
     * @return the entryDate
     */
    public StringWithCustomTags getEntryDate() {
        return entryDate;
    }

    /**
     * Get the sourceText
     * 
     * @return the sourceText
     */
    public List<List<String>> getSourceText() {
        return sourceText;
    }

    /**
     * Get the sourceText
     * 
     * @param initializeIfNeeded
     *            true if this collection should be created on-the-fly if it is currently null
     * @return the sourceText
     */
    public List<List<String>> getSourceText(boolean initializeIfNeeded) {
        if (initializeIfNeeded && sourceText == null) {
            sourceText = new ArrayList<List<String>>(0);
        }
        return sourceText;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (entryDate == null ? 0 : entryDate.hashCode());
        result = prime * result + (sourceText == null ? 0 : sourceText.hashCode());
        return result;
    }

    /**
     * Set the entryDate
     * 
     * @param entryDate
     *            the entryDate to set
     */
    public void setEntryDate(StringWithCustomTags entryDate) {
        this.entryDate = entryDate;
    }

    @Override
    public String toString() {
        return "CitationData [" + (entryDate != null ? "entryDate=" + entryDate + ", " : "") + (sourceText != null ? "sourceText=" + sourceText + ", " : "")
                + (getCustomTags() != null ? "customTags=" + getCustomTags() : "") + "]";
    }
}
