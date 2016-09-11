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
package org.gedcom4j.validate;

import org.gedcom4j.model.ModelElement;

/**
 * Represents something that was auto-repaired. As the validation framework does its work, and makes corrections, one of these
 * should be created for each correction, and saved in a list for the caller to review. It should also be added to the
 * {@link ValidationFinding} class.
 * 
 * @author frizbog
 */
public class AutoRepair {
    /**
     * the corrected object
     */
    private final ModelElement after;

    /**
     * the original object in its state immediately before correction
     */
    private final ModelElement before;

    /**
     * Constructor
     * 
     * @param before
     *            the original object in its state immediately before correction
     * @param after
     *            the corrected object
     * @throws IllegalArgumentException
     *             if the before and after objects are different classes
     */
    public AutoRepair(ModelElement before, ModelElement after) {
        if (before != null && after != null && !before.getClass().equals(after.getClass())) {
            throw new IllegalArgumentException("The before object (" + before.getClass().getName()
                    + ") is not the same type as the after object (" + after.getClass().getName() + ").");
        }
        this.before = before;
        this.after = after;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AutoRepair)) {
            return false;
        }
        AutoRepair other = (AutoRepair) obj;
        if (after == null) {
            if (other.after != null) {
                return false;
            }
        } else if (!after.equals(other.after)) {
            return false;
        }
        if (before == null) {
            if (other.before != null) {
                return false;
            }
        } else if (!before.equals(other.before)) {
            return false;
        }
        return true;
    }

    /**
     * Get the corrected object
     * 
     * @return the corrected object
     */
    public ModelElement getAfter() {
        return after;
    }

    /**
     * Get the original object in its state immediately before correction
     * 
     * @return the original object in its state immediately before correction
     */
    public ModelElement getBefore() {
        return before;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((after == null) ? 0 : after.hashCode());
        result = prime * result + ((before == null) ? 0 : before.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AutoRepair [");
        if (before != null) {
            builder.append("before=");
            builder.append(before);
            builder.append(", ");
        }
        if (after != null) {
            builder.append("after=");
            builder.append(after);
        }
        builder.append("]");
        return builder.toString();
    }

}
