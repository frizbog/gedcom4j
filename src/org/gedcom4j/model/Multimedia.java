/*
 * Copyright (c) 2009-2012 Matthew R. Harrah
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
 * A class for representing multimedia items. Corresponds to MULTIMEDIA_RECORD in the GEDCOM standard.
 * </p>
 * <p>
 * Please note that when gedcom4j v1.1.0 was released to include GEDCOM 5.5.1 support, the data model got a bit more complicated. The changes to the
 * multimedia specs were among the most drastic and most difficult to deal with. Not only did version 5.5 do away with embedded multimedia support
 * (i.e., the `BLOB` tag), it also changed cardinalities (multiple file references per MULTIMEDIA_RECORD in 5.5.1, where 5.5 only allowed one), and
 * moved tags to become children of other tags (i.e., the `FORM` tag is now a child of the new `FILE` tag in 5.5.1).
 * </p>
 * <p>
 * Users who plan to read files produced by other systems and rewrite them with gedcom4j should pay special attention to the multimedia section and
 * ensure that the data in the model is compliant with the version of GEDCOM being used, and making adjustments as needed.
 * </p>
 * 
 * @author frizbog1
 * 
 */
public class Multimedia {
    /**
     * The xref for this multimedia item
     */
    public String xref;

    /**
     * The title of this multimedia item. This field should ONLY be used when the spec is 5.5 and should be null for 5.5.1 files.
     */
    public String embeddedTitle;

    /**
     * The file reference for this multimedia item
     */
    public List<FileReference> fileReferences = new ArrayList<FileReference>();

    /**
     * Notes for this multimedia item
     */
    public List<Note> notes = new ArrayList<Note>();

    /**
     * Source citations for this multimedia item
     */
    public List<AbstractCitation> citations = new ArrayList<AbstractCitation>();

    /**
     * The binary (blob) for this multimedia item. Encoded as string data. This field should always be an empty list for 5.5.1 files.
     */
    public List<String> blob = new ArrayList<String>();

    /**
     * The next object in the chain holding binary data if it needs to be continued due to size. This field should always be null for 5.5.1 files.
     */
    public Multimedia continuedObject;

    /**
     * User references
     */
    public List<UserReference> userReferences = new ArrayList<UserReference>();

    /**
     * The change date for this multimedia item
     */
    public ChangeDate changeDate;

    /**
     * The record id number for this multimedia item
     */
    public String recIdNumber;

    /**
     * The format of the multimedia object - only for 5.5 style multimedia files, and should be null for 5.5.1 files.
     */
    public String embeddedMediaFormat;

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
        Multimedia other = (Multimedia) obj;
        if (blob == null) {
            if (other.blob != null) {
                return false;
            }
        } else if (!blob.equals(other.blob)) {
            return false;
        }
        if (embeddedMediaFormat == null) {
            if (other.embeddedMediaFormat != null) {
                return false;
            }
        } else if (!embeddedMediaFormat.equals(other.embeddedMediaFormat)) {
            return false;
        }
        if (changeDate == null) {
            if (other.changeDate != null) {
                return false;
            }
        } else if (!changeDate.equals(other.changeDate)) {
            return false;
        }
        if (citations == null) {
            if (other.citations != null) {
                return false;
            }
        } else if (!citations.equals(other.citations)) {
            return false;
        }
        if (continuedObject == null) {
            if (other.continuedObject != null) {
                return false;
            }
        } else if (!continuedObject.equals(other.continuedObject)) {
            return false;
        }
        if (fileReferences == null) {
            if (other.fileReferences != null) {
                return false;
            }
        } else if (!fileReferences.equals(other.fileReferences)) {
            return false;
        }
        if (notes == null) {
            if (other.notes != null) {
                return false;
            }
        } else if (!notes.equals(other.notes)) {
            return false;
        }
        if (recIdNumber == null) {
            if (other.recIdNumber != null) {
                return false;
            }
        } else if (!recIdNumber.equals(other.recIdNumber)) {
            return false;
        }
        if (embeddedTitle == null) {
            if (other.embeddedTitle != null) {
                return false;
            }
        } else if (!embeddedTitle.equals(other.embeddedTitle)) {
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
        result = prime * result + (blob == null ? 0 : blob.hashCode());
        result = prime * result + (embeddedMediaFormat == null ? 0 : embeddedMediaFormat.hashCode());
        result = prime * result + (changeDate == null ? 0 : changeDate.hashCode());
        result = prime * result + (citations == null ? 0 : citations.hashCode());
        result = prime * result + (continuedObject == null ? 0 : continuedObject.hashCode());
        result = prime * result + (fileReferences == null ? 0 : fileReferences.hashCode());
        result = prime * result + (notes == null ? 0 : notes.hashCode());
        result = prime * result + (recIdNumber == null ? 0 : recIdNumber.hashCode());
        result = prime * result + (embeddedTitle == null ? 0 : embeddedTitle.hashCode());
        result = prime * result + (userReferences == null ? 0 : userReferences.hashCode());
        result = prime * result + (xref == null ? 0 : xref.hashCode());
        return result;
    }

}
