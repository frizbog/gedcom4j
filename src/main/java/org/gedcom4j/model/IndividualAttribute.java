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
        return (type == other.type);
    }

    /**
     * Gets the type.
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
     * Sets the type.
     *
     * @param type
     *            the new type
     */
    public void setType(IndividualAttributeType type) {
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IndividualAttribute [");
        if (type != null) {
            builder.append("type=");
            builder.append(type);
            builder.append(", ");
        }
        if (address != null) {
            builder.append("address=");
            builder.append(address);
            builder.append(", ");
        }
        if (age != null) {
            builder.append("age=");
            builder.append(age);
            builder.append(", ");
        }
        if (cause != null) {
            builder.append("cause=");
            builder.append(cause);
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
        if (description != null) {
            builder.append("description=");
            builder.append(description);
            builder.append(", ");
        }
        if (emails != null) {
            builder.append("emails=");
            builder.append(emails);
            builder.append(", ");
        }
        if (faxNumbers != null) {
            builder.append("faxNumbers=");
            builder.append(faxNumbers);
            builder.append(", ");
        }
        if (multimedia != null) {
            builder.append("multimedia=");
            builder.append(multimedia);
            builder.append(", ");
        }
        if (notes != null) {
            builder.append("notes=");
            builder.append(notes);
            builder.append(", ");
        }
        if (phoneNumbers != null) {
            builder.append("phoneNumbers=");
            builder.append(phoneNumbers);
            builder.append(", ");
        }
        if (place != null) {
            builder.append("place=");
            builder.append(place);
            builder.append(", ");
        }
        if (religiousAffiliation != null) {
            builder.append("religiousAffiliation=");
            builder.append(religiousAffiliation);
            builder.append(", ");
        }
        if (respAgency != null) {
            builder.append("respAgency=");
            builder.append(respAgency);
            builder.append(", ");
        }
        if (restrictionNotice != null) {
            builder.append("restrictionNotice=");
            builder.append(restrictionNotice);
            builder.append(", ");
        }
        if (subType != null) {
            builder.append("subType=");
            builder.append(subType);
            builder.append(", ");
        }
        if (wwwUrls != null) {
            builder.append("wwwUrls=");
            builder.append(wwwUrls);
            builder.append(", ");
        }
        if (yNull != null) {
            builder.append("yNull=");
            builder.append(yNull);
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
