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
 * A source call number, used in {@link RepositoryCitation} classes.
 * 
 * @author frizbog1
 * 
 */
public class SourceCallNumber extends AbstractElement {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 508420414070288759L;

    /**
     * The call number. Corresponds to SOURCE_CALL_NUMBER in the Gedcom spec.
     */
    private StringWithCustomTags callNumber;

    /**
     * The media type, corresponds to SOURCE_MEDIA_TYPE in the Gedcom spec
     */
    private StringWithCustomTags mediaType;

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

    /**
     * Get the callNumber
     * 
     * @return the callNumber
     */
    public StringWithCustomTags getCallNumber() {
        return callNumber;
    }

    /**
     * Get the mediaType
     * 
     * @return the mediaType
     */
    public StringWithCustomTags getMediaType() {
        return mediaType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (callNumber == null ? 0 : callNumber.hashCode());
        result = prime * result + (mediaType == null ? 0 : mediaType.hashCode());
        return result;
    }

    /**
     * Set the callNumber
     * 
     * @param callNumber
     *            the callNumber to set
     */
    public void setCallNumber(StringWithCustomTags callNumber) {
        this.callNumber = callNumber;
    }

    /**
     * Set the mediaType
     * 
     * @param mediaType
     *            the mediaType to set
     */
    public void setMediaType(StringWithCustomTags mediaType) {
        this.mediaType = mediaType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SourceCallNumber [");
        if (callNumber != null) {
            builder.append("callNumber=");
            builder.append(callNumber);
            builder.append(", ");
        }
        if (mediaType != null) {
            builder.append("mediaType=");
            builder.append(mediaType);
            builder.append(", ");
        }
        if (customTags != null) {
            builder.append("customTags=");
            builder.append(customTags);
        }
        builder.append("]");
        return builder.toString();
    }
}
