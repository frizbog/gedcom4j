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
 * Represents a string value from a tag, and allows for user-defined child tags
 * 
 * @author frizbog
 */
public class StringWithCustomTags extends AbstractElement {

    /**
     * The string value itself.
     */
    public String value;

    /**
     * Default constructor
     */
    public StringWithCustomTags() {
        super();
    }

    /**
     * Constructor that takes a String as a parameter
     * 
     * @param string
     *            the string
     */
    public StringWithCustomTags(String string) {
        value = string;
    }

    /**
     * Constructor that takes a StringTree as a parameter
     * 
     * @param s
     *            the stringtree with the value of the string, plus optional custom tags
     */
    public StringWithCustomTags(StringTree s) {
        value = s.value;
        customTags = s.children;
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
        StringWithCustomTags other = (StringWithCustomTags) obj;
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
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(value == null ? "null" : value);
        for (StringTree ct : customTags) {
            sb.append("\n");
            sb.append(ct.level + (ct.id == null ? "" : " " + ct.id) + " " + ct.tag + " " + ct.value);
        }

        return sb.toString();
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
