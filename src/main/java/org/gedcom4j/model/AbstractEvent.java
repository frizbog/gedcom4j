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
     * The address where this event took place
     */
    public Address address;

    /**
     * The age of the person to whom this event is attached at the time it occurred
     */
    public StringWithCustomTags age;

    /**
     * The cause of the event
     */
    public StringWithCustomTags cause;

    /**
     * List of source citations for this event
     */
    public List<AbstractCitation> citations = new ArrayList<AbstractCitation>(0);

    /**
     * The date of this event
     */
    public StringWithCustomTags date;

    /**
     * A description of this event
     */
    public StringWithCustomTags description;

    /**
     * Multimeda for this event
     */
    public List<Multimedia> multimedia = new ArrayList<Multimedia>(0);

    /**
     * The place where this event occurred
     */
    public Place place;

    /**
     * The responsible agency for this event
     */
    public StringWithCustomTags respAgency;

    /**
     * Either a Y or a null after the event type;
     */
    public String yNull;

    /**
     * A subtype that further qualifies the type
     */
    public StringWithCustomTags subType;

    /**
     * The religious affiliation of this event. New for GEDCOM 5.5.1.
     */
    public StringWithCustomTags religiousAffiliation;

    /**
     * A notification that this record is in some way restricted. New for GEDCOM 5.5.1. Values are supposed to be
     * "confidential", "locked", or "privacy" but this implementation allows any value.
     */
    public StringWithCustomTags restrictionNotice;

    /**
     * Notes about this object
     */
    public List<Note> notes = Options.isCollectionInitializationEnabled() ? new ArrayList<Note>(0) : null;

    /**
     * The phone numbers for this submitter
     */
    protected List<StringWithCustomTags> phoneNumbers = new ArrayList<StringWithCustomTags>(0);

    /**
     * Web URL's. New for GEDCOM 5.5.1.
     */
    protected List<StringWithCustomTags> wwwUrls = new ArrayList<StringWithCustomTags>(0);

    /**
     * Fax numbers. New for GEDCOM 5.5.1.
     */
    protected List<StringWithCustomTags> faxNumbers = new ArrayList<StringWithCustomTags>(0);

    /**
     * The emails for this submitter. New for GEDCOM 5.5.1
     */
    protected List<StringWithCustomTags> emails = new ArrayList<StringWithCustomTags>(0);

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
