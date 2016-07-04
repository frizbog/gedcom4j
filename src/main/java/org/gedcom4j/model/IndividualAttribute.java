/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.gedcom4j.model;

/**
 * A class for an individual attribute. Corresponds to INDIVIDUAL_ATTRIBUTE_STRUCTURE in the GEDCOM standard
 * 
 * @author frizbog1
 * 
 */
public class IndividualAttribute extends AbstractEvent {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 5029917676949449650L;

    /**
     * The type of attribute
     */
    private IndividualAttributeType type;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof IndividualAttribute)) {
            return false;
        }
        IndividualAttribute other = (IndividualAttribute) obj;
        if (type != other.type) {
            return false;
        }
        return true;
    }

    /**
     * Get the type
     * 
     * @return the type
     */
    public IndividualAttributeType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (type == null ? 0 : type.hashCode());
        return result;
    }

    /**
     * Set the type
     * 
     * @param type
     *            the type to set
     */
    public void setType(IndividualAttributeType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("IndividualAttribute [type=");
        sb.append(type);
        if (address != null) {
            sb.append(", address=").append(address.toString());
        }
        if (age != null) {
            sb.append(", age=").append(age.toString());
        }
        if (cause != null) {
            sb.append(", cause=").append(cause.toString());
        }
        if (date != null) {
            sb.append(", date=").append(date.toString());
        }
        if (description != null) {
            sb.append(", description=").append(description.toString());
        }
        if (place != null) {
            sb.append(", place=").append(place.toString());
        }
        if (subType != null) {
            sb.append(", subType=").append(subType.toString());
        }

        sb.append("]");
        return sb.toString();
    }

}
