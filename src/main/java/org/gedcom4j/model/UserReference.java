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
 * A User reference.
 * 
 * @author frizbog1
 */
public class UserReference extends AbstractElement {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -7283713193577447000L;

    /**
     * The reference number
     */
    private StringWithCustomTags referenceNum;

    /**
     * The type of reference
     */
    private StringWithCustomTags type;

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
        UserReference other = (UserReference) obj;
        if (referenceNum == null) {
            if (other.referenceNum != null) {
                return false;
            }
        } else if (!referenceNum.equals(other.referenceNum)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }

    /**
     * Gets the reference num.
     *
     * @return the reference num
     */
    public StringWithCustomTags getReferenceNum() {
        return referenceNum;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public StringWithCustomTags getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (referenceNum == null ? 0 : referenceNum.hashCode());
        result = prime * result + (type == null ? 0 : type.hashCode());
        return result;
    }

    /**
     * Sets the reference num.
     *
     * @param referenceNum
     *            the new reference num
     */
    public void setReferenceNum(StringWithCustomTags referenceNum) {
        this.referenceNum = referenceNum;
    }

    /**
     * Sets the type.
     *
     * @param type
     *            the new type
     */
    public void setType(StringWithCustomTags type) {
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(32);
        builder.append("UserReference [");
        if (referenceNum != null) {
            builder.append("referenceNum=");
            builder.append(referenceNum);
            builder.append(", ");
        }
        if (type != null) {
            builder.append("type=");
            builder.append(type);
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
