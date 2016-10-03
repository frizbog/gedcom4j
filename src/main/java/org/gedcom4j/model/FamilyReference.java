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
 * A reference to an family, which may have custom facts on the reference that are not custom facts about the family themselves.
 * 
 * @author frizbog
 */
public class FamilyReference extends AbstractElement {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -1855269504871281907L;

    /**
     * The family referred to
     */
    Family family;

    /**
     * Default constructor
     */
    public FamilyReference() {
        // Default constructor does nothing
    }

    /**
     * Create a new FamilyReference object
     * 
     * @param family
     *            the family being referred to
     */
    public FamilyReference(Family family) {
        this.family = family;
    }

    /**
     * Copy constructor.
     * 
     * @param other
     *            the other FamilyReference being copied
     * @param deep
     *            pass in true if a full, deep copy of the {@link FamilyReference} should be created, including copies of the
     *            families the referenced family is in. If false, the object is copied without references to the families the family
     *            is in.
     */
    public FamilyReference(FamilyReference other, boolean deep) {
        super(other);
        family = new Family(other.family, deep);
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
        if (!(obj instanceof FamilyReference)) {
            return false;
        }
        FamilyReference other = (FamilyReference) obj;
        if (family == null) {
            if (other.family != null) {
                return false;
            }
        } else if (!family.equals(other.family)) {
            return false;
        }
        return true;
    }

    /**
     * Get the family
     * 
     * @return the family
     */
    public Family getFamily() {
        return family;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((family == null) ? 0 : family.hashCode());
        return result;
    }

    /**
     * Set the family
     * 
     * @param family
     *            the family to set
     */
    public void setFamily(Family family) {
        this.family = family;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(25);
        builder.append("FamilyReference [");
        if (family != null) {
            builder.append("family=");
            builder.append(family);
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
