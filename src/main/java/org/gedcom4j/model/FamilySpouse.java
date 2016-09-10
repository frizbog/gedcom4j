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
 * Indicates an individual's membership, as a spouse, in a family
 * 
 * @author frizbog1
 * 
 */
public class FamilySpouse extends AbstractNotesElement {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -2986477725062735490L;

    /**
     * The family in which the person was one of the spouses
     */
    private Family family;

    /** Default constructor */
    public FamilySpouse() {
        // Default constructor does nothing
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            object being copied
     */
    public FamilySpouse(FamilySpouse other) {
        this(other, true);
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            object being copied
     * @param deep
     *            pass in true if a full, deep copy of the family should be created. If false, the family will be copied but will
     *            not contain back-references to the individuals in it (husband, wife, children). This allows preventing infinite
     *            recursion.
     */
    public FamilySpouse(FamilySpouse other, boolean deep) {
        super(other);
        if (other.family != null) {
            family = new Family(other.family, deep);
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
        if (!(obj instanceof FamilySpouse)) {
            return false;
        }
        FamilySpouse other = (FamilySpouse) obj;
        if (family == null) {
            if (other.family != null) {
                return false;
            }
        } else {
            if (other.family == null) {
                return false;
            }
            if (family.getXref() == null) {
                if (other.family.getXref() != null) {
                    return false;
                }
            } else {
                if (!family.getXref().equals(other.family.getXref())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gets the family.
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
        result = prime * result + (family == null || family.getXref() == null ? 0 : family.getXref().hashCode());
        return result;
    }

    /**
     * Sets the family.
     *
     * @param family
     *            the new family
     */
    public void setFamily(Family family) {
        this.family = family;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(24);
        builder.append("FamilySpouse [");
        if (family != null) {
            builder.append("family=");
            builder.append(family);
            builder.append(", ");
        }
        if (getCustomTags() != null) {
            builder.append("customTags=");
            builder.append(getCustomTags());
        }
        builder.append("]");
        return builder.toString();
    }
}
