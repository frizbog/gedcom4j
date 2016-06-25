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
 * A repository. Corresponds to REPOSITORY_RECORD in the GEDCOM standard.
 * 
 * @author frizbog1
 * 
 */
public class Repository extends AbstractElement {
    /**
     * The xref for this repository
     */
    public String xref;

    /**
     * The name of this repository
     */
    public StringWithCustomTags name;

    /**
     * The record ID for this repository
     */
    public StringWithCustomTags recIdNumber;

    /**
     * The address for this repository
     */
    public Address address;

    /**
     * The notes for this repository
     */
    public List<Note> notes = new ArrayList<Note>(0);

    /**
     * The change date for this repository
     */
    public ChangeDate changeDate;

    /**
     * The user references to this repository
     */
    public List<UserReference> userReferences = new ArrayList<UserReference>(0);

    /**
     * The phone numbers for this repository
     */
    public List<StringWithCustomTags> phoneNumbers = new ArrayList<StringWithCustomTags>(0);

    /**
     * Web URL's. New for GEDCOM 5.5.1.
     */
    public List<StringWithCustomTags> wwwUrls = new ArrayList<StringWithCustomTags>(0);

    /**
     * Fax numbers. New for GEDCOM 5.5.1.
     */
    public List<StringWithCustomTags> faxNumbers = new ArrayList<StringWithCustomTags>(0);

    /**
     * The emails for this submitter. New for GEDCOM 5.5.1
     */
    public List<StringWithCustomTags> emails = new ArrayList<StringWithCustomTags>(0);

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof Repository)) {
            return false;
        }
        Repository other = (Repository) obj;
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
        if (recIdNumber == null) {
            if (other.recIdNumber != null) {
                return false;
            }
        } else if (!recIdNumber.equals(other.recIdNumber)) {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (address == null ? 0 : address.hashCode());
        result = prime * result + (changeDate == null ? 0 : changeDate.hashCode());
        result = prime * result + (faxNumbers == null ? 0 : faxNumbers.hashCode());
        result = prime * result + (wwwUrls == null ? 0 : wwwUrls.hashCode());
        result = prime * result + (emails == null ? 0 : emails.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (notes == null ? 0 : notes.hashCode());
        result = prime * result + (phoneNumbers == null ? 0 : phoneNumbers.hashCode());
        result = prime * result + (recIdNumber == null ? 0 : recIdNumber.hashCode());
        result = prime * result + (userReferences == null ? 0 : userReferences.hashCode());
        result = prime * result + (xref == null ? 0 : xref.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Repository [" + (xref != null ? "xref=" + xref + ", " : "") + (name != null ? "name=" + name + ", " : "")
                + (recIdNumber != null ? "recIdNumber=" + recIdNumber + ", " : "") + (address != null ? "address=" + address + ", " : "")
                + (notes != null ? "notes=" + notes + ", " : "") + (changeDate != null ? "changeDate=" + changeDate + ", " : "")
                + (userReferences != null ? "userReferences=" + userReferences + ", " : "")
                + (phoneNumbers != null ? "phoneNumbers=" + phoneNumbers + ", " : "") + (wwwUrls != null ? "wwwUrls=" + wwwUrls + ", " : "")
                + (faxNumbers != null ? "faxNumbers=" + faxNumbers + ", " : "") + (emails != null ? "emails=" + emails + ", " : "")
                + (customTags != null ? "customTags=" + customTags : "") + "]";
    }

}
