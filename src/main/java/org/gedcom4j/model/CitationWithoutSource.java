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
import java.util.List;

import org.gedcom4j.Options;

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
     * Serial Version UID
     */
    private static final long serialVersionUID = -3562494505830574223L;

    /**
     * Lines of text describing this citation
     */
    private List<String> description = getDescription(Options.isCollectionInitializationEnabled());

    /**
     * Lines of Lines of text from the source (yeah, really)
     */
    private List<List<String>> textFromSource = getTextFromSource(Options.isCollectionInitializationEnabled());

    @Override
    public void accept(IVisitor v) {
        v.visit(this);
    }

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
        if (getNotes() == null) {
            if (other.getNotes() != null) {
                return false;
            }
        } else if (!getNotes().equals(other.getNotes())) {
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

    /**
     * Get the description
     * 
     * @return the description
     */
    public List<String> getDescription() {
        return description;
    }

    /**
     * Get the description
     * 
     * @param initializeIfNeeded
     *            true if this collection should be created on-the-fly if it is currently null
     * @return the description
     */
    public List<String> getDescription(boolean initializeIfNeeded) {
        if (initializeIfNeeded && description == null) {
            description = new ArrayList<String>(0);
        }
        return description;
    }

    /**
     * Get the textFromSource
     * 
     * @return the textFromSource
     */
    public List<List<String>> getTextFromSource() {
        return textFromSource;
    }

    /**
     * Get the textFromSource
     * 
     * @param initializeIfNeeded
     *            true if this collection should be created on-the-fly if it is currently null
     * @return the textFromSource
     */
    public List<List<String>> getTextFromSource(boolean initializeIfNeeded) {
        if (initializeIfNeeded && textFromSource == null) {
            textFromSource = new ArrayList<List<String>>(0);
        }
        return textFromSource;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (description == null ? 0 : description.hashCode());
        result = prime * result + (getNotes() == null ? 0 : getNotes().hashCode());
        result = prime * result + (textFromSource == null ? 0 : textFromSource.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CitationWithoutSource [");
        if (description != null) {
            builder.append("description=");
            builder.append(description);
            builder.append(", ");
        }
        if (textFromSource != null) {
            builder.append("textFromSource=");
            builder.append(textFromSource);
        }
        builder.append("]");
        return builder.toString();
    }
}
