package com.mattharrah.gedcom4j;

/**
 * A source call number, used in {@link RepositoryCitation} classes.
 * 
 * @author frizbog1
 * 
 */
public class SourceCallNumber {
    /**
     * The call number. Corresponds to SOURCE_CALL_NUMBER in the Gedcom spec.
     */
    public String callNumber;
    /**
     * The media type, corresponds to SOURCE_MEDIA_TYPE in the Gedcom spec
     */
    public String mediaType;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
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
        int result = 1;
        result = prime * result
                + ((callNumber == null) ? 0 : callNumber.hashCode());
        result = prime * result
                + ((mediaType == null) ? 0 : mediaType.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "SourceCallNumber [callNumber=" + callNumber + ", mediaType="
                + mediaType + "]";
    }
}
