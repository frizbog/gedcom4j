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

/**
 * A name variation - phonetic, or romanized. Used for personal names and place names.
 * 
 * @author frizbog
 */
public abstract class AbstractNameVariation extends AbstractElement {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 5302060855856746189L;

    /**
     * The variation text
     */
    protected String variation;

    /**
     * The variation type. For romanized names, the method used in transforming the text to a romanized variation. For phonetic
     * names, the method used in transforming the text to the phonetic variation.
     */
    protected StringWithCustomFacts variationType;

    /** Default constructor */
    public AbstractNameVariation() {
        // Default constructor does nothing
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            object being copied
     */
    public AbstractNameVariation(AbstractNameVariation other) {
        super(other);
        variation = other.variation;
        if (other.variationType != null) {
            variationType = new StringWithCustomFacts(other.variationType);
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractNameVariation other = (AbstractNameVariation) obj;
        if (variation == null) {
            if (other.variation != null) {
                return false;
            }
        } else if (!variation.equals(other.variation)) {
            return false;
        }
        if (variationType == null) {
            if (other.variationType != null) {
                return false;
            }
        } else if (!variationType.equals(other.variationType)) {
            return false;
        }
        return true;
    }

    /**
     * Gets the variation.
     *
     * @return the variation
     */
    public String getVariation() {
        return variation;
    }

    /**
     * Gets the variation type.
     *
     * @return the variation type
     */
    public StringWithCustomFacts getVariationType() {
        return variationType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (variation == null ? 0 : variation.hashCode());
        result = prime * result + (variationType == null ? 0 : variationType.hashCode());
        return result;
    }

    /**
     * Sets the variation.
     *
     * @param variation
     *            the new variation
     */
    public void setVariation(String variation) {
        this.variation = variation;
    }

    /**
     * Sets the variation type.
     *
     * @param variationType
     *            the new variation type
     */
    public void setVariationType(String variationType) {
        this.variationType = new StringWithCustomFacts(variationType);
    }

    /**
     * Sets the variation type.
     *
     * @param variationType
     *            the new variation type
     */
    public void setVariationType(StringWithCustomFacts variationType) {
        this.variationType = variationType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(64);
        builder.append("AbstractNameVariation [");
        if (variation != null) {
            builder.append("variation=");
            builder.append(variation);
            builder.append(", ");
        }
        if (variationType != null) {
            builder.append("variationType=");
            builder.append(variationType);
            builder.append(", ");
        }
        if (getCustomFacts() != null) {
            builder.append("customFacts=");
            builder.append(getCustomFacts());
        }
        builder.append("]");
        return builder.toString();
    }

}
