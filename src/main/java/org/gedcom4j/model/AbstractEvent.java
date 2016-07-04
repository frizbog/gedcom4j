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

import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.Options;

/**
 * Represents an event. Corresponds to EVENT_DETAIL in the GEDCOM spec.
 * 
 * @author frizbog1
 * 
 */
public abstract class AbstractEvent extends AbstractElement {
    /**
     * The place where this event occurred
     */
    protected Place place;

    /**
     * The address where this event took place
     */
    protected Address address;

    /**
     * The age of the person to whom this event is attached at the time it occurred
     */
    protected StringWithCustomTags age;

    /**
     * The cause of the event
     */
    protected StringWithCustomTags cause;

    /**
     * The citations for this object
     */
    protected List<AbstractCitation> citations = getCitations(Options.isCollectionInitializationEnabled());

    /**
     * The date of this event
     */
    protected StringWithCustomTags date;

    /**
     * A description of this event
     */
    protected StringWithCustomTags description;

    /**
     * The emails for this submitter. New for GEDCOM 5.5.1
     */
    protected List<StringWithCustomTags> emails = getEmails(Options.isCollectionInitializationEnabled());

    /**
     * Fax numbers. New for GEDCOM 5.5.1.
     */
    protected List<StringWithCustomTags> faxNumbers = getFaxNumbers(Options.isCollectionInitializationEnabled());

    /**
     * Multimedia links for this source citation
     */
    protected List<Multimedia> multimedia = getMultimedia(Options.isCollectionInitializationEnabled());

    /**
     * Notes about this object
     */
    protected List<Note> notes = getNotes(Options.isCollectionInitializationEnabled());

    /**
     * The phone numbers for this submitter
     */
    protected List<StringWithCustomTags> phoneNumbers = getPhoneNumbers(Options.isCollectionInitializationEnabled());

    /**
     * The religious affiliation of this event. New for GEDCOM 5.5.1.
     */
    protected StringWithCustomTags religiousAffiliation;

    /**
     * The responsible agency for this event
     */
    protected StringWithCustomTags respAgency;

    /**
     * A notification that this record is in some way restricted. New for GEDCOM 5.5.1. Values are supposed to be
     * "confidential", "locked", or "privacy" but this implementation allows any value.
     */
    protected StringWithCustomTags restrictionNotice;

    /**
     * A subtype that further qualifies the type
     */
    protected StringWithCustomTags subType;

    /**
     * Web URL's. New for GEDCOM 5.5.1.
     */
    protected List<StringWithCustomTags> wwwUrls = getWwwUrls(Options.isCollectionInitializationEnabled());

    /**
     * Either a Y or a null after the event type;
     */
    protected String yNull;

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
        if (notes == null) {
            if (other.notes != null) {
                return false;
            }
        } else if (!notes.equals(other.notes)) {
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
     * Get the address
     * 
     * @return the address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Get the age
     * 
     * @return the age
     */
    public StringWithCustomTags getAge() {
        return age;
    }

    /**
     * Get the cause
     * 
     * @return the cause
     */
    public StringWithCustomTags getCause() {
        return cause;
    }

    /**
     * Get the citations
     * 
     * @return the citations
     */
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
    public List<AbstractCitation> getCitations(boolean initializeIfNeeded) {
        if (initializeIfNeeded && citations == null) {
            citations = new ArrayList<AbstractCitation>(0);
        }
        return citations;
    }

    /**
     * Get the date
     * 
     * @return the date
     */
    public StringWithCustomTags getDate() {
        return date;
    }

    /**
     * Get the description
     * 
     * @return the description
     */
    public StringWithCustomTags getDescription() {
        return description;
    }

    /**
     * Get the emails
     * 
     * @return the emails
     */
    public List<StringWithCustomTags> getEmails() {
        return emails;
    }

    /**
     * Get the emails
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the emails
     */
    public List<StringWithCustomTags> getEmails(boolean initializeIfNeeded) {
        if (initializeIfNeeded && emails == null) {
            emails = new ArrayList<StringWithCustomTags>(0);
        }

        return emails;
    }

    /**
     * Get the faxNumbers
     * 
     * @return the faxNumbers
     */
    public List<StringWithCustomTags> getFaxNumbers() {
        return faxNumbers;
    }

    /**
     * Get the faxNumbers
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the faxNumbers
     */
    public List<StringWithCustomTags> getFaxNumbers(boolean initializeIfNeeded) {
        if (initializeIfNeeded && faxNumbers == null) {
            faxNumbers = new ArrayList<StringWithCustomTags>(0);
        }
        return faxNumbers;
    }

    /**
     * Get the multimedia
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
            multimedia = new ArrayList<Multimedia>(0);
        }
        return multimedia;
    }

    /**
     * Get the notes
     * 
     * @return the notes
     */
    public List<Note> getNotes() {
        return notes;
    }

    /**
     * Get the notes
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed?
     * 
     * @return the notes
     */
    public List<Note> getNotes(boolean initializeIfNeeded) {
        if (initializeIfNeeded && notes == null) {
            notes = new ArrayList<Note>(0);
        }
        return notes;
    }

    /**
     * Get the phoneNumbers
     * 
     * @return the phoneNumbers
     */
    public List<StringWithCustomTags> getPhoneNumbers() {
        return phoneNumbers;
    }

    /**
     * Get the phoneNumbers
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the phoneNumbers
     */
    public List<StringWithCustomTags> getPhoneNumbers(boolean initializeIfNeeded) {
        if (initializeIfNeeded && phoneNumbers == null) {
            phoneNumbers = new ArrayList<StringWithCustomTags>(0);
        }
        return phoneNumbers;
    }

    /**
     * Get the place
     * 
     * @return the place
     */
    public Place getPlace() {
        return place;
    }

    /**
     * Get the religiousAffiliation
     * 
     * @return the religiousAffiliation
     */
    public StringWithCustomTags getReligiousAffiliation() {
        return religiousAffiliation;
    }

    /**
     * Get the respAgency
     * 
     * @return the respAgency
     */
    public StringWithCustomTags getRespAgency() {
        return respAgency;
    }

    /**
     * Get the restrictionNotice
     * 
     * @return the restrictionNotice
     */
    public StringWithCustomTags getRestrictionNotice() {
        return restrictionNotice;
    }

    /**
     * Get the subType
     * 
     * @return the subType
     */
    public StringWithCustomTags getSubType() {
        return subType;
    }

    /**
     * Get the wwwUrls
     * 
     * @return the wwwUrls
     */
    public List<StringWithCustomTags> getWwwUrls() {
        return wwwUrls;
    }

    /**
     * Get the wwwUrls
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the wwwUrls
     */
    public List<StringWithCustomTags> getWwwUrls(boolean initializeIfNeeded) {
        if (initializeIfNeeded && wwwUrls == null) {
            wwwUrls = new ArrayList<StringWithCustomTags>(0);
        }
        return wwwUrls;
    }

    /**
     * Get the yNull
     * 
     * @return the yNull
     */
    public String getyNull() {
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
        result = prime * result + (notes == null ? 0 : notes.hashCode());
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
     * Set the address
     * 
     * @param address
     *            the address to set
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Set the age
     * 
     * @param age
     *            the age to set
     */
    public void setAge(StringWithCustomTags age) {
        this.age = age;
    }

    /**
     * Set the cause
     * 
     * @param cause
     *            the cause to set
     */
    public void setCause(StringWithCustomTags cause) {
        this.cause = cause;
    }

    /**
     * Set the date
     * 
     * @param date
     *            the date to set
     */
    public void setDate(StringWithCustomTags date) {
        this.date = date;
    }

    /**
     * Set the description
     * 
     * @param description
     *            the description to set
     */
    public void setDescription(StringWithCustomTags description) {
        this.description = description;
    }

    /**
     * Set the place
     * 
     * @param place
     *            the place to set
     */
    public void setPlace(Place place) {
        this.place = place;
    }

    /**
     * Set the religiousAffiliation
     * 
     * @param religiousAffiliation
     *            the religiousAffiliation to set
     */
    public void setReligiousAffiliation(StringWithCustomTags religiousAffiliation) {
        this.religiousAffiliation = religiousAffiliation;
    }

    /**
     * Set the respAgency
     * 
     * @param respAgency
     *            the respAgency to set
     */
    public void setRespAgency(StringWithCustomTags respAgency) {
        this.respAgency = respAgency;
    }

    /**
     * Set the restrictionNotice
     * 
     * @param restrictionNotice
     *            the restrictionNotice to set
     */
    public void setRestrictionNotice(StringWithCustomTags restrictionNotice) {
        this.restrictionNotice = restrictionNotice;
    }

    /**
     * Set the subType
     * 
     * @param subType
     *            the subType to set
     */
    public void setSubType(StringWithCustomTags subType) {
        this.subType = subType;
    }

    /**
     * Set the yNull
     * 
     * @param yNull
     *            the yNull to set
     */
    public void setyNull(String yNull) {
        this.yNull = yNull;
    }

    @Override
    public String toString() {
        return "Event [" + (address != null ? "address=" + address + ", " : "") + (phoneNumbers != null ? "phoneNumbers=" + phoneNumbers + ", " : "")
                + (wwwUrls != null ? "wwwUrls=" + wwwUrls + ", " : "") + (faxNumbers != null ? "faxNumbers=" + faxNumbers + ", " : "") + (emails != null
                        ? "emails=" + emails + ", " : "") + (age != null ? "age=" + age + ", " : "") + (cause != null ? "cause=" + cause + ", " : "")
                + (citations != null ? "citations=" + citations + ", " : "") + (date != null ? "date=" + date + ", " : "") + (description != null
                        ? "description=" + description + ", " : "") + (multimedia != null ? "multimedia=" + multimedia + ", " : "") + (notes != null ? "notes="
                                + notes + ", " : "") + (place != null ? "place=" + place + ", " : "") + (respAgency != null ? "respAgency=" + respAgency + ", "
                                        : "") + (yNull != null ? "yNull=" + yNull + ", " : "") + (subType != null ? "subType=" + subType + ", " : "")
                + (religiousAffiliation != null ? "religiousAffiliation=" + religiousAffiliation + ", " : "") + (restrictionNotice != null
                        ? "restrictionNotice=" + restrictionNotice + ", " : "") + (getCustomTags() != null ? "customTags=" + getCustomTags() : "") + "]";
    }

}
