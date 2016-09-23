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

import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.Options;

/**
 * Represents an event. Corresponds to EVENT_DETAIL in the GEDCOM spec.
 * 
 * @author frizbog1
 * 
 */
@SuppressWarnings("PMD.GodClass")
public abstract class AbstractEvent extends AbstractAddressableElement implements HasCitations {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 2745411202618610785L;

    /**
     * The age of the person to whom this event is attached at the time it occurred
     */
    private StringWithCustomFacts age;

    /**
     * The cause of the event
     */
    private StringWithCustomFacts cause;

    /**
     * The citations for this object
     */
    private List<AbstractCitation> citations = getCitations(Options.isCollectionInitializationEnabled());

    /**
     * The date of this event
     */
    private StringWithCustomFacts date;

    /**
     * A description of this event
     */
    private StringWithCustomFacts description;

    /**
     * Multimedia links for this source citation
     */
    private List<Multimedia> multimedia = getMultimedia(Options.isCollectionInitializationEnabled());

    /**
     * The place where this event occurred
     */
    private Place place;

    /**
     * The religious affiliation of this event. New for GEDCOM 5.5.1.
     */
    private StringWithCustomFacts religiousAffiliation;

    /**
     * The responsible agency for this event
     */
    private StringWithCustomFacts respAgency;

    /**
     * A notification that this record is in some way restricted. New for GEDCOM 5.5.1. Values are supposed to be "confidential",
     * "locked", or "privacy" but this implementation allows any value.
     */
    private StringWithCustomFacts restrictionNotice;

    /**
     * A subtype that further qualifies the type
     */
    private StringWithCustomFacts subType;

    /**
     * Either a Y or a null after the event type;
     */
    private String yNull;

    /** Default constructor */
    public AbstractEvent() {
        // Default constructor does nothing
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            object being copied
     */
    public AbstractEvent(AbstractEvent other) {
        super(other);

        if (other.age != null) {
            age = new StringWithCustomFacts(other.age);
        }
        if (other.cause != null) {
            cause = new StringWithCustomFacts(other.cause);
        }
        if (other.citations != null) {
            citations = new ArrayList<>();
            for (AbstractCitation ac : other.citations) {
                if (ac instanceof CitationWithoutSource) {
                    citations.add(new CitationWithoutSource((CitationWithoutSource) ac));
                } else if (ac instanceof CitationWithSource) {
                    citations.add(new CitationWithSource((CitationWithSource) ac));
                }
            }
        }
        if (other.date != null) {
            date = new StringWithCustomFacts(other.date);
        }
        if (other.description != null) {
            description = new StringWithCustomFacts(other.description);
        }
        if (other.multimedia != null) {
            multimedia = new ArrayList<>();
            for (Multimedia m : other.multimedia) {
                multimedia.add(new Multimedia(m));
            }
        }
        if (other.place != null) {
            place = new Place(other.place);
        }
        if (other.religiousAffiliation != null) {
            religiousAffiliation = new StringWithCustomFacts(other.religiousAffiliation);
        }
        if (other.respAgency != null) {
            respAgency = new StringWithCustomFacts(other.respAgency);
        }
        if (other.restrictionNotice != null) {
            restrictionNotice = new StringWithCustomFacts(other.restrictionNotice);
        }
        if (other.subType != null) {
            subType = new StringWithCustomFacts(other.subType);
        }
        yNull = other.yNull;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "PMD.ExcessiveMethodLength", "PMD.NcssMethodCount" })
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof AbstractEvent)) {
            return false;
        }
        AbstractEvent other = (AbstractEvent) obj;
        if (address == null) {
            if (other.address != null) {
                return false;
            }
        } else if (!address.equals(other.address)) {
            return false;
        }
        if (age == null) {
            if (other.age != null) {
                return false;
            }
        } else if (!age.equals(other.age)) {
            return false;
        }
        if (cause == null) {
            if (other.cause != null) {
                return false;
            }
        } else if (!cause.equals(other.cause)) {
            return false;
        }
        if (citations == null) {
            if (other.citations != null) {
                return false;
            }
        } else if (!citations.equals(other.citations)) {
            return false;
        }
        if (date == null) {
            if (other.date != null) {
                return false;
            }
        } else if (!date.equals(other.date)) {
            return false;
        }
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (multimedia == null) {
            if (other.multimedia != null) {
                return false;
            }
        } else if (!multimedia.equals(other.multimedia)) {
            return false;
        }
        if (phoneNumbers == null) {
            if (other.phoneNumbers != null) {
                return false;
            }
        } else if (!phoneNumbers.equals(other.phoneNumbers)) {
            return false;
        }
        if (place == null) {
            if (other.place != null) {
                return false;
            }
        } else if (!place.equals(other.place)) {
            return false;
        }
        if (respAgency == null) {
            if (other.respAgency != null) {
                return false;
            }
        } else if (!respAgency.equals(other.respAgency)) {
            return false;
        }
        if (subType == null) {
            if (other.subType != null) {
                return false;
            }
        } else if (!subType.equals(other.subType)) {
            return false;
        }
        if (yNull == null) {
            if (other.yNull != null) {
                return false;
            }
        } else if (!yNull.equals(other.yNull)) {
            return false;
        }
        if (religiousAffiliation == null) {
            if (other.religiousAffiliation != null) {
                return false;
            }
        } else if (!religiousAffiliation.equals(other.religiousAffiliation)) {
            return false;
        }
        if (restrictionNotice == null) {
            if (other.restrictionNotice != null) {
                return false;
            }
        } else if (!restrictionNotice.equals(other.restrictionNotice)) {
            return false;
        }
        if (wwwUrls == null) {
            if (other.wwwUrls != null) {
                return false;
            }
        } else if (!wwwUrls.equals(other.wwwUrls)) {
            return false;
        }
        if (faxNumbers == null) {
            if (other.faxNumbers != null) {
                return false;
            }
        } else if (!faxNumbers.equals(other.faxNumbers)) {
            return false;
        }
        if (emails == null) {
            if (other.emails != null) {
                return false;
            }
        } else if (!emails.equals(other.emails)) {
            return false;
        }

        return true;
    }

    /**
     * Gets the age.
     *
     * @return the age
     */
    public StringWithCustomFacts getAge() {
        return age;
    }

    /**
     * Gets the cause.
     *
     * @return the cause
     */
    public StringWithCustomFacts getCause() {
        return cause;
    }

    /**
     * Gets the citations.
     *
     * @return the citations
     */
    @Override
    public List<AbstractCitation> getCitations() {
        return citations;
    }

    /**
     * Get the citations
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed?
     * 
     * @return the citations
     */
    @Override
    public List<AbstractCitation> getCitations(boolean initializeIfNeeded) {
        if (initializeIfNeeded && citations == null) {
            citations = new ArrayList<>(0);
        }
        return citations;
    }

    /**
     * Gets the date.
     *
     * @return the date
     */
    public StringWithCustomFacts getDate() {
        return date;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public StringWithCustomFacts getDescription() {
        return description;
    }

    /**
     * Gets the multimedia.
     *
     * @return the multimedia
     */
    public List<Multimedia> getMultimedia() {
        return multimedia;
    }

    /**
     * Get the multimedia
     * 
     * @param initializeIfNeeded
     *            true if this collection should be created on-the-fly if it is currently null
     * @return the multimedia
     */
    public List<Multimedia> getMultimedia(boolean initializeIfNeeded) {
        if (initializeIfNeeded && multimedia == null) {
            multimedia = new ArrayList<>(0);
        }
        return multimedia;
    }

    /**
     * Gets the place.
     *
     * @return the place
     */
    public Place getPlace() {
        return place;
    }

    /**
     * Gets the religious affiliation.
     *
     * @return the religious affiliation
     */
    public StringWithCustomFacts getReligiousAffiliation() {
        return religiousAffiliation;
    }

    /**
     * Gets the resp agency.
     *
     * @return the resp agency
     */
    public StringWithCustomFacts getRespAgency() {
        return respAgency;
    }

    /**
     * Gets the restriction notice.
     *
     * @return the restriction notice
     */
    public StringWithCustomFacts getRestrictionNotice() {
        return restrictionNotice;
    }

    /**
     * Gets the sub type.
     *
     * @return the sub type
     */
    public StringWithCustomFacts getSubType() {
        return subType;
    }

    /**
     * Get the Y or Null flag
     * 
     * @return the Y or Null flag
     */
    public String getYNull() {
        return yNull;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (address == null ? 0 : address.hashCode());
        result = prime * result + (age == null ? 0 : age.hashCode());
        result = prime * result + (cause == null ? 0 : cause.hashCode());
        result = prime * result + (citations == null ? 0 : citations.hashCode());
        result = prime * result + (date == null ? 0 : date.hashCode());
        result = prime * result + (description == null ? 0 : description.hashCode());
        result = prime * result + (multimedia == null ? 0 : multimedia.hashCode());
        result = prime * result + (phoneNumbers == null ? 0 : phoneNumbers.hashCode());
        result = prime * result + (place == null ? 0 : place.hashCode());
        result = prime * result + (respAgency == null ? 0 : respAgency.hashCode());
        result = prime * result + (subType == null ? 0 : subType.hashCode());
        result = prime * result + (yNull == null ? 0 : yNull.hashCode());
        result = prime * result + (religiousAffiliation == null ? 0 : religiousAffiliation.hashCode());
        result = prime * result + (restrictionNotice == null ? 0 : restrictionNotice.hashCode());
        result = prime * result + (faxNumbers == null ? 0 : faxNumbers.hashCode());
        result = prime * result + (wwwUrls == null ? 0 : wwwUrls.hashCode());
        result = prime * result + (emails == null ? 0 : emails.hashCode());
        return result;
    }

    /**
     * Sets the age.
     *
     * @param age
     *            the new age
     */
    public void setAge(String age) {
        this.age = age == null ? null : new StringWithCustomFacts(age);
    }

    /**
     * Sets the age.
     *
     * @param age
     *            the new age
     */
    public void setAge(StringWithCustomFacts age) {
        this.age = age;
    }

    /**
     * Sets the cause.
     *
     * @param cause
     *            the new cause
     */
    public void setCause(String cause) {
        this.cause = cause == null ? null : new StringWithCustomFacts(cause);
    }

    /**
     * Sets the cause.
     *
     * @param cause
     *            the new cause
     */
    public void setCause(StringWithCustomFacts cause) {
        this.cause = cause;
    }

    /**
     * Sets the date.
     *
     * @param date
     *            the new date
     */
    public void setDate(String date) {
        this.date = date == null ? null : new StringWithCustomFacts(date);
    }

    /**
     * Sets the date.
     *
     * @param date
     *            the new date
     */
    public void setDate(StringWithCustomFacts date) {
        this.date = date;
    }

    /**
     * Sets the description.
     *
     * @param description
     *            the new description
     */
    public void setDescription(String description) {
        this.description = description == null ? null : new StringWithCustomFacts(description);
    }

    /**
     * Sets the description.
     *
     * @param description
     *            the new description
     */
    public void setDescription(StringWithCustomFacts description) {
        this.description = description;
    }

    /**
     * Sets the place.
     *
     * @param place
     *            the new place
     */
    public void setPlace(Place place) {
        this.place = place;
    }

    /**
     * Sets the religious affiliation.
     *
     * @param religiousAffiliation
     *            the new religious affiliation
     */
    public void setReligiousAffiliation(String religiousAffiliation) {
        this.religiousAffiliation = religiousAffiliation == null ? null : new StringWithCustomFacts(religiousAffiliation);
    }

    /**
     * Sets the religious affiliation.
     *
     * @param religiousAffiliation
     *            the new religious affiliation
     */
    public void setReligiousAffiliation(StringWithCustomFacts religiousAffiliation) {
        this.religiousAffiliation = religiousAffiliation;
    }

    /**
     * Sets the resp agency.
     *
     * @param respAgency
     *            the new resp agency
     */
    public void setRespAgency(String respAgency) {
        this.respAgency = respAgency == null ? null : new StringWithCustomFacts(respAgency);
    }

    /**
     * Sets the resp agency.
     *
     * @param respAgency
     *            the new resp agency
     */
    public void setRespAgency(StringWithCustomFacts respAgency) {
        this.respAgency = respAgency;
    }

    /**
     * Sets the restriction notice.
     *
     * @param restrictionNotice
     *            the new restriction notice
     */
    public void setRestrictionNotice(String restrictionNotice) {
        this.restrictionNotice = restrictionNotice == null ? null : new StringWithCustomFacts(restrictionNotice);
    }

    /**
     * Sets the restriction notice.
     *
     * @param restrictionNotice
     *            the new restriction notice
     */
    public void setRestrictionNotice(StringWithCustomFacts restrictionNotice) {
        this.restrictionNotice = restrictionNotice;
    }

    /**
     * Sets the sub type.
     *
     * @param subType
     *            the new sub type
     */
    public void setSubType(String subType) {
        this.subType = subType == null ? null : new StringWithCustomFacts(subType);
    }

    /**
     * Sets the sub type.
     *
     * @param subType
     *            the new sub type
     */
    public void setSubType(StringWithCustomFacts subType) {
        this.subType = subType;
    }

    /**
     * Set the Y or Null flag
     * 
     * @param yNull
     *            the Y or Null flag to set
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public void setYNull(String yNull) {
        this.yNull = yNull;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(128);
        builder.append("AbstractEvent [");
        buildAbstractEventToString(builder);
        builder.append("]");
        return builder.toString();
    }

    /**
     * Converts only the field members into the toString() representation, for easy sub-classing.
     * 
     * @param builder
     *            the StringBuilder to output into.
     * 
     * @return the StringBuilder that was passed in.
     */
    protected StringBuilder buildAbstractEventToString(StringBuilder builder) {
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
        if (multimedia != null) {
            builder.append("multimedia=");
            builder.append(multimedia);
            builder.append(", ");
        }
        if (getNotes() != null) {
            builder.append("notes=");
            builder.append(getNotes());
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
        if (yNull != null) {
            builder.append("yNull=");
            builder.append(yNull);
            builder.append(", ");
        }
        appendAddressFields(builder, true);
        if (getCustomFacts() != null) {
            builder.append("customFacts=");
            builder.append(getCustomFacts());
        }
        return builder;
    }

}
