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

import org.gedcom4j.model.enumerations.LdsIndividualOrdinanceType;

/**
 * An LDS Ordinance for an individual
 * 
 * @author frizbog1
 */
public class LdsIndividualOrdinance extends AbstractLdsOrdinance {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -6448687737594960394L;

    /**
     * The family in which the individual was a child - used for SLGC types
     */
    private FamilyChild familyWhereChild;

    /**
     * The type
     */
    private LdsIndividualOrdinanceType type;

    /**
     * Allows for a Y or null to be processed after the type. Not strictly part of the GEDCOM, but allows for flexibility
     */
    private String yNull;

    /** Default constructor */
    public LdsIndividualOrdinance() {
        // Default constructor does nothing
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            object being copied
     */
    public LdsIndividualOrdinance(LdsIndividualOrdinance other) {
        super(other);
        if (other.familyWhereChild != null) {
            familyWhereChild = new FamilyChild(other.familyWhereChild);
        }
        type = other.type;
        yNull = other.yNull;
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
        if (!(obj instanceof LdsIndividualOrdinance)) {
            return false;
        }
        LdsIndividualOrdinance other = (LdsIndividualOrdinance) obj;
        if (familyWhereChild == null) {
            if (other.familyWhereChild != null) {
                return false;
            }
        } else if (!familyWhereChild.equals(other.familyWhereChild)) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        if (yNull == null) {
            if (other.yNull != null) {
                return false;
            }
        } else if (!yNull.equals(other.yNull)) {
            return false;
        }
        return true;
    }

    /**
     * Gets the family where child.
     *
     * @return the family where child
     */
    public FamilyChild getFamilyWhereChild() {
        return familyWhereChild;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public LdsIndividualOrdinanceType getType() {
        return type;
    }

    /**
     * Get the yNull
     * 
     * @return the yNull
     */
    public String getYNull() {
        return yNull;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (familyWhereChild == null ? 0 : familyWhereChild.hashCode());
        result = prime * result + (type == null ? 0 : type.hashCode());
        result = prime * result + (yNull == null ? 0 : yNull.hashCode());
        return result;
    }

    /**
     * Sets the family where child.
     *
     * @param familyWhereChild
     *            the new family where child
     */
    public void setFamilyWhereChild(FamilyChild familyWhereChild) {
        this.familyWhereChild = familyWhereChild;
    }

    /**
     * Sets the type.
     *
     * @param type
     *            the new type
     */
    public void setType(LdsIndividualOrdinanceType type) {
        this.type = type;
    }

    /**
     * Set the yNull
     * 
     * @param yNull
     *            the yNull to set
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public void setYNull(String yNull) {
        this.yNull = yNull;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(64);
        builder.append("LdsIndividualOrdinance [");
        if (familyWhereChild != null) {
            builder.append("familyWhereChild=");
            builder.append(familyWhereChild);
            builder.append(", ");
        }
        if (type != null) {
            builder.append("type=");
            builder.append(type);
            builder.append(", ");
        }
        if (yNull != null) {
            builder.append("yNull=");
            builder.append(yNull);
            builder.append(", ");
        }
        if (citations != null) {
            builder.append("citations=");
            builder.append(citations);
            builder.append(", ");
        }
        if (date != null) {
            builder.append("date=");
            builder.append(date);
            builder.append(", ");
        }
        if (getNoteStructures() != null) {
            builder.append("notes=");
            builder.append(getNoteStructures());
            builder.append(", ");
        }
        if (place != null) {
            builder.append("place=");
            builder.append(place);
            builder.append(", ");
        }
        if (status != null) {
            builder.append("status=");
            builder.append(status);
            builder.append(", ");
        }
        if (temple != null) {
            builder.append("temple=");
            builder.append(temple);
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
