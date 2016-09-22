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
 * Represents a string value from a tag, and allows for user-defined child facts(tags)
 * 
 * @author frizbog
 */
public class StringWithCustomFacts extends AbstractElement {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -2479578654715820890L;

    /**
     * The string value itself.
     */
    private String value;

    /**
     * Default constructor
     */
    public StringWithCustomFacts() {
        // Default constructor does nothing
    }

    /**
     * Constructor that takes a String as a parameter
     * 
     * @param string
     *            the string
     */
    public StringWithCustomFacts(String string) {
        value = string;
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            the other object to copy
     */
    public StringWithCustomFacts(StringWithCustomFacts other) {
        super(other);
        value = other.value;
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
        StringWithCustomFacts other = (StringWithCustomFacts) obj;
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (value == null ? 0 : value.hashCode());
        return result;
    }

    /**
     * Sets the value.
     *
     * @param value
     *            the new value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (customFacts == null || customFacts.isEmpty()) {
            return value;
        }

        StringBuilder builder = new StringBuilder(30);
        builder.append("StringWithCustomFacts [");
        if (value != null) {
            builder.append("value=");
            builder.append(value);
            builder.append(", ");
        }
        if (getCustomFacts() != null) {
            builder.append("getCustomFacts()=");
            builder.append(getCustomFacts());
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Return the trimmed string value
     * 
     * @return the trimmed string value
     */
    public String trim() {
        if (value == null) {
            return null;
        }
        return value.trim();
    }

}
