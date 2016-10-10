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
 * A multi-line list of strings, along with custom facts.
 * 
 * @author frizbog1
 */
public class MultiStringWithCustomFacts extends AbstractElement {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 8355989906882622025L;

    /**
     * The lines of text of this note
     */
    private List<String> lines = getLines(Options.isCollectionInitializationEnabled());

    /** Default constructor */
    public MultiStringWithCustomFacts() {
        // Default constructor does nothing
    }

    /**
     * Constructor that takes the lines as a parameter
     * 
     * @param lines
     *            lines of text
     */
    public MultiStringWithCustomFacts(List<String> lines) {
        this.lines.clear();
        this.lines.addAll(lines);
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            the other object to copy
     */
    public MultiStringWithCustomFacts(MultiStringWithCustomFacts other) {
        super(other);
        if (other.lines != null) {
            lines = new ArrayList<>(other.lines);
        }
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
        if (!(obj instanceof MultiStringWithCustomFacts)) {
            return false;
        }
        MultiStringWithCustomFacts other = (MultiStringWithCustomFacts) obj;
        if (lines == null) {
            if (other.lines != null) {
                return false;
            }
        } else if (!lines.equals(other.lines)) {
            return false;
        }
        return true;
    }

    /**
     * Gets the lines.
     *
     * @return the lines
     */
    public List<String> getLines() {
        return lines;
    }

    /**
     * Get the lines
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the lines. If the note structure has a non-null reference to a root-level {@link NoteRecord}, this method will always
     *         return an immutable empty list. To get a mutable list of lines, you must remove the note reference first.
     */
    public List<String> getLines(boolean initializeIfNeeded) {
        if (initializeIfNeeded && lines == null) {
            lines = new ArrayList<>(0);
        }
        return lines;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((lines == null) ? 0 : lines.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(31);
        builder.append("MultiStringWithCustomFacts [");
        if (lines != null) {
            builder.append("lines=");
            builder.append(lines);
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
