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
 * Represents an individual event. Corresponds to the INDIVIDUAL_EVENT_STRUCTURE item in the GEDCOM specification.
 * 
 * @author frizbog1
 */
public class IndividualEvent extends AbstractEvent {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 6290302687051916574L;

    /**
     * The family to which this individual adopted was adopted
     */
    private FamilyChild family;

    /**
     * The type of event this represents
     */
    private IndividualEventType type;

    /** Default constructor */
    public IndividualEvent() {
        // Default constructor does nothing
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            object being copied
     */
    public IndividualEvent(IndividualEvent other) {
        super(other);
        if (other.family != null) {
            family = new FamilyChild(other.family, false);
        }
        type = other.type;
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
        if (!(obj instanceof IndividualEvent)) {
            return false;
        }
        IndividualEvent other = (IndividualEvent) obj;
        if (family == null) {
            if (other.family != null) {
                return false;
            }
        } else if (!family.equals(other.family)) {
            return false;
        }
        return type == other.type;
    }

    /**
     * Gets the family.
     *
     * @return the family
     */
    public FamilyChild getFamily() {
        return family;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public IndividualEventType getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (family == null ? 0 : family.hashCode());
        result = prime * result + (type == null ? 0 : type.hashCode());
        return result;
    }

    /**
     * Sets the family.
     *
     * @param family
     *            the new family
     */
    public void setFamily(FamilyChild family) {
        this.family = family;
    }

    /**
     * Sets the type.
     *
     * @param type
     *            the new type
     */
    public void setType(IndividualEventType type) {
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(128);
        builder.append("IndividualEvent [");
        if (family != null) {
            builder.append("family=");
            builder.append(family);
            builder.append(", ");
        }
        if (type != null) {
            builder.append("type=");
            builder.append(type);
            builder.append(", ");
        }
        buildAbstractEventToString(builder);
        builder.append("]");
        return builder.toString();
    }

}
