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
import java.util.Collections;
import java.util.List;

import org.gedcom4j.Options;

/**
 * An abstract class from which most other items in the data model extend. Exists to hold custom tag information, which by
 * definition cannot be known in advance.
 * 
 * @author frizbog
 */
public abstract class AbstractElement implements HasCustomFacts {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -983667065483378388L;

    /**
     * A list of custom facts on this item.
     */
    protected List<CustomFact> customFacts = getCustomFacts(Options.isCollectionInitializationEnabled());

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
        if (other.customFacts != null) {
            customFacts = new ArrayList<>();
            for (CustomFact cf : other.customFacts) {
                if (cf != null) {
                    customFacts.add(new CustomFact(cf));
                }
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
        if (getCustomFacts() == null) {
            if (other.getCustomFacts() != null) {
                return false;
            }
        } else if (!getCustomFacts().equals(other.getCustomFacts())) {
            return false;
        }
        return true;
    }

    /**
     * Gets the custom facts.
     *
     * @return the custom facts
     */
    @Override
    public List<CustomFact> getCustomFacts() {
        return customFacts;
    }

    /**
     * Get the custom facts
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed
     * @return the customFacts
     */
    @Override
    public List<CustomFact> getCustomFacts(boolean initializeIfNeeded) {
        if (initializeIfNeeded && customFacts == null) {
            customFacts = new ArrayList<>(0);
        }
        return customFacts;
    }

    /**
     * Gets the custom facts that have a tag that matches the one supplied
     * 
     * @param tag
     *            the tag we are looking for
     * @return a list of custom facts that have the desired tag. Always returns a list but it might be empty. Although the entries
     *         in the result list are modifiable, the list itself is not.
     */
    @Override
    public List<CustomFact> getCustomFactsWithTag(String tag) {
        List<CustomFact> result = new ArrayList<>();
        if (customFacts != null) {
            for (CustomFact cf : customFacts) {
                if (cf.getTag() != null && cf.getTag().equals(tag)) {
                    result.add(cf);
                }
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (getCustomFacts() == null ? 0 : getCustomFacts().hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(32);
        builder.append("AbstractElement [");
        if (customFacts != null) {
            builder.append("customFacts=");
            builder.append(customFacts);
        }
        builder.append("]");
        return builder.toString();
    }

}
