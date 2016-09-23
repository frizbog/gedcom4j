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
 */
public class SourceCallNumber extends AbstractElement {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 508420414070288759L;

    /**
     * The call number. Corresponds to SOURCE_CALL_NUMBER in the Gedcom spec.
     */
    private StringWithCustomFacts callNumber;

    /**
     * The media type, corresponds to SOURCE_MEDIA_TYPE in the Gedcom spec
     */
    private StringWithCustomFacts mediaType;

    /** Default constructor */
    public SourceCallNumber() {
        // Default constructor does nothing
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            object being copied
     */
    public SourceCallNumber(SourceCallNumber other) {
        super(other);
        if (other.callNumber != null) {
            callNumber = new StringWithCustomFacts(other.callNumber);
        }
        if (other.mediaType != null) {
            mediaType = new StringWithCustomFacts(other.mediaType);
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
     * Gets the call number.
     *
     * @return the call number
     */
    public StringWithCustomFacts getCallNumber() {
        return callNumber;
    }

    /**
     * Gets the media type.
     *
     * @return the media type
     */
    public StringWithCustomFacts getMediaType() {
        return mediaType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (callNumber == null ? 0 : callNumber.hashCode());
        result = prime * result + (mediaType == null ? 0 : mediaType.hashCode());
        return result;
    }

    /**
     * Sets the call number.
     *
     * @param callNumber
     *            the new call number
     */
    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber == null ? null : new StringWithCustomFacts(callNumber);
    }

    /**
     * Sets the call number.
     *
     * @param callNumber
     *            the new call number
     */
    public void setCallNumber(StringWithCustomFacts callNumber) {
        this.callNumber = callNumber;
    }

    /**
     * Sets the media type.
     *
     * @param mediaType
     *            the new media type
     */
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType == null ? null : new StringWithCustomFacts(mediaType);
    }

    /**
     * Sets the media type.
     *
     * @param mediaType
     *            the new media type
     */
    public void setMediaType(StringWithCustomFacts mediaType) {
        this.mediaType = mediaType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(32);
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
        if (getCustomFacts() != null) {
            builder.append("customFacts=");
            builder.append(getCustomFacts());
        }
        builder.append("]");
        return builder.toString();
    }
}
