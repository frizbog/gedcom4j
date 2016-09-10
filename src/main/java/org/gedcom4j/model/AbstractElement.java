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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.Options;

/**
 * An abstract class from which most other items in the data model extend. Exists to hold custom tag information, which by
 * definition cannot be known in advance.
 * 
 * @author frizbog
 */
public abstract class AbstractElement implements Serializable, HasCustomTags {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -983667065483378388L;

    /**
     * A list of custom tags on this item.
     */
    private List<StringTree> customTags = getCustomTags(Options.isCollectionInitializationEnabled());

    /**
     * Default constructor
     */
    public AbstractElement() {
        super();
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            the other object to copy
     */
    public AbstractElement(AbstractElement other) {
        super();
        if (other.customTags != null) {
            customTags = new ArrayList<>();
            for (StringTree st : other.customTags) {
                StringTree newSt = new StringTree(st);
                customTags.add(newSt);
            }
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
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractElement other = (AbstractElement) obj;
        if (getCustomTags() == null) {
            if (other.getCustomTags() != null) {
                return false;
            }
        } else if (!getCustomTags().equals(other.getCustomTags())) {
            return false;
        }
        return true;
    }

    /**
     * Gets the custom tags.
     *
     * @return the custom tags
     */
    public List<StringTree> getCustomTags() {
        return customTags;
    }

    /**
     * Get the custom tags
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed
     * @return the customTags
     */
    public List<StringTree> getCustomTags(boolean initializeIfNeeded) {
        if (initializeIfNeeded && customTags == null) {
            customTags = new ArrayList<>(0);
        }
        return customTags;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (getCustomTags() == null ? 0 : getCustomTags().hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(32);
        builder.append("AbstractElement [");
        if (customTags != null) {
            builder.append("customTags=");
            builder.append(customTags);
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Set the custom tags
     * 
     * @param theCustomTags
     *            the custom tags
     */
    protected void setCustomTags(List<StringTree> theCustomTags) {
        customTags = theCustomTags;
    }

}
