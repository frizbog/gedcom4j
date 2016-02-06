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

/**
 * <p>
 * A submitter. Corresponds to the SUBMITTER_RECORD structure in the GEDCOM standard.
 * </p>
 * <p>
 * Note that a valid GEDCOM requires at least one Submitter record to be valid.
 * </p>
 * 
 * @author frizbog1
 */
public class Submitter extends AbstractElement {
    /**
     * The xref for this submitter
     */
    public String xref;

    /**
     * The registration file number for this submitter
     */
    public StringWithCustomTags regFileNumber;

    /**
     * The name of this submitter
     */
    public StringWithCustomTags name;

    /**
     * The record ID number
     */
    public StringWithCustomTags recIdNumber;

    /**
     * The language preferences
     */
    public List<StringWithCustomTags> languagePref = new ArrayList<StringWithCustomTags>();

    /**
     * The address of this submitter
     */
    public Address address;

    /**
     * The phone numbers for this submitter
     */
    public List<StringWithCustomTags> phoneNumbers = new ArrayList<StringWithCustomTags>();

    /**
     * Web URL's. New for GEDCOM 5.5.1.
     */
    public List<StringWithCustomTags> wwwUrls = new ArrayList<StringWithCustomTags>();

    /**
     * Fax numbers. New for GEDCOM 5.5.1.
     */
    public List<StringWithCustomTags> faxNumbers = new ArrayList<StringWithCustomTags>();

    /**
     * The emails for this submitter. New for GEDCOM 5.5.1
     */
    public List<StringWithCustomTags> emails = new ArrayList<StringWithCustomTags>();

    /**
     * The multimedia for this submitter
     */
    public List<Multimedia> multimedia = new ArrayList<Multimedia>();

    /**
     * The change date for this submitter
     */
    public ChangeDate changeDate;

    /**
     * The user references for this submitter
     */
    public List<UserReference> userReferences = new ArrayList<UserReference>();

    /**
     * The notes for this submitter
     */
    public List<Note> notes = new ArrayList<Note>();

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("PMD.ExcessiveMethodLength")
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
        Submitter other = (Submitter) obj;
        if (address == null) {
            if (other.address != null) {
                return false;
            }
        } else if (!address.equals(other.address)) {
            return false;
        }
        if (changeDate == null) {
            if (other.changeDate != null) {
                return false;
            }
        } else if (!changeDate.equals(other.changeDate)) {
            return false;
        }
        if (languagePref == null) {
            if (other.languagePref != null) {
                return false;
            }
        } else if (!languagePref.equals(other.languagePref)) {
            return false;
        }
        if (multimedia == null) {
            if (other.multimedia != null) {
                return false;
            }
        } else if (!multimedia.equals(other.multimedia)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
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
        if (recIdNumber == null) {
            if (other.recIdNumber != null) {
                return false;
            }
        } else if (!recIdNumber.equals(other.recIdNumber)) {
            return false;
        }
        if (regFileNumber == null) {
            if (other.regFileNumber != null) {
                return false;
            }
        } else if (!regFileNumber.equals(other.regFileNumber)) {
            return false;
        }
        if (userReferences == null) {
            if (other.userReferences != null) {
                return false;
            }
        } else if (!userReferences.equals(other.userReferences)) {
            return false;
        }
        if (xref == null) {
            if (other.xref != null) {
                return false;
            }
        } else if (!xref.equals(other.xref)) {
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
        result = prime * result + (address == null ? 0 : address.hashCode());
        result = prime * result + (changeDate == null ? 0 : changeDate.hashCode());
        result = prime * result + (languagePref == null ? 0 : languagePref.hashCode());
        result = prime * result + (multimedia == null ? 0 : multimedia.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (notes == null ? 0 : notes.hashCode());
        result = prime * result + (phoneNumbers == null ? 0 : phoneNumbers.hashCode());
        result = prime * result + (faxNumbers == null ? 0 : faxNumbers.hashCode());
        result = prime * result + (wwwUrls == null ? 0 : wwwUrls.hashCode());
        result = prime * result + (emails == null ? 0 : emails.hashCode());
        result = prime * result + (recIdNumber == null ? 0 : recIdNumber.hashCode());
        result = prime * result + (regFileNumber == null ? 0 : regFileNumber.hashCode());
        result = prime * result + (userReferences == null ? 0 : userReferences.hashCode());
        result = prime * result + (xref == null ? 0 : xref.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Submitter [" + (xref != null ? "xref=" + xref + ", " : "") + (regFileNumber != null ? "regFileNumber=" + regFileNumber + ", " : "")
                + (name != null ? "name=" + name + ", " : "") + (recIdNumber != null ? "recIdNumber=" + recIdNumber + ", " : "")
                + (languagePref != null ? "languagePref=" + languagePref + ", " : "") + (address != null ? "address=" + address + ", " : "")
                + (phoneNumbers != null ? "phoneNumbers=" + phoneNumbers + ", " : "") + (wwwUrls != null ? "wwwUrls=" + wwwUrls + ", " : "")
                + (faxNumbers != null ? "faxNumbers=" + faxNumbers + ", " : "") + (emails != null ? "emails=" + emails + ", " : "")
                + (multimedia != null ? "multimedia=" + multimedia + ", " : "") + (changeDate != null ? "changeDate=" + changeDate + ", " : "")
                + (userReferences != null ? "userReferences=" + userReferences + ", " : "") + (notes != null ? "notes=" + notes + ", " : "")
                + (customTags != null ? "customTags=" + customTags : "") + "]";
    }
}
