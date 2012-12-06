/*
 * Copyright (c) 2009-2012 Matthew R. Harrah
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
 * A source call number, used in {@link RepositoryCitation} classes.
 * 
 * @author frizbog1
 * 
 */
public class SourceCallNumber extends AbstractElement {
    /**
     * The call number. Corresponds to SOURCE_CALL_NUMBER in the Gedcom spec.
     */
    public StringWithCustomTags callNumber;

    /**
     * The media type, corresponds to SOURCE_MEDIA_TYPE in the Gedcom spec
     */
    public StringWithCustomTags mediaType;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof SourceCallNumber)) {
            return false;
        }
        SourceCallNumber other = (SourceCallNumber) obj;
        if (callNumber == null) {
            if (other.callNumber != null) {
                return false;
            }
        } else if (!callNumber.equals(other.callNumber)) {
            return false;
        }
        if (mediaType == null) {
            if (other.mediaType != null) {
                return false;
            }
        } else if (!mediaType.equals(other.mediaType)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (callNumber == null ? 0 : callNumber.hashCode());
        result = prime * result + (mediaType == null ? 0 : mediaType.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "SourceCallNumber [callNumber=" + callNumber + ", mediaType=" + mediaType + "]";
    }
}
