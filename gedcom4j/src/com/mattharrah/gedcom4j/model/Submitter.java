/*
 * Copyright (c) 2009-2011 Matthew R. Harrah
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
package com.mattharrah.gedcom4j.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A submitter. Corresponds to the SUBMITTER_RECORD structure in the GEDCOM
 * standard.
 * 
 * @author frizbog1
 */
public class Submitter {
    /**
     * The xref for this submitter
     */
    public String xref;
    /**
     * The registration file number for this submitter
     */
    public String regFileNumber;
    /**
     * The name of this submitter
     */
    public String name;
    /**
     * The record ID number
     */
    public String recIdNumber;
    /**
     * The language preferences
     */
    public List<String> languagePref = new ArrayList<String>();
    /**
     * The address of this submitter
     */
    public Address address;
    /**
     * The phone numbers for this submitter
     */
    public List<String> phoneNumbers = new ArrayList<String>();
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
     * The emails for this submitter
     */
    public List<String> emails = new ArrayList<String>();
    /**
     * The notes for this submitter
     */
    public List<Note> notes = new ArrayList<Note>();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
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
        if (emails == null) {
            if (other.emails != null) {
                return false;
            }
        } else if (!emails.equals(other.emails)) {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result
                + ((changeDate == null) ? 0 : changeDate.hashCode());
        result = prime * result + ((emails == null) ? 0 : emails.hashCode());
        result = prime * result
                + ((languagePref == null) ? 0 : languagePref.hashCode());
        result = prime * result
                + ((multimedia == null) ? 0 : multimedia.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((notes == null) ? 0 : notes.hashCode());
        result = prime * result
                + ((phoneNumbers == null) ? 0 : phoneNumbers.hashCode());
        result = prime * result
                + ((recIdNumber == null) ? 0 : recIdNumber.hashCode());
        result = prime * result
                + ((regFileNumber == null) ? 0 : regFileNumber.hashCode());
        result = prime * result
                + ((userReferences == null) ? 0 : userReferences.hashCode());
        result = prime * result + ((xref == null) ? 0 : xref.hashCode());
        return result;
    }
}
