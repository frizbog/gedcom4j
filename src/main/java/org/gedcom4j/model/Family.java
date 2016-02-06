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
 * A family record. Corresponds to FAM_RECORD in the GEDCOM spec.
 * 
 * @author frizbog1
 */
public class Family extends AbstractElement {
    /**
     * The permanent record file number
     */
    public StringWithCustomTags recFileNumber;

    /**
     * The automated record ID number
     */
    public StringWithCustomTags automatedRecordId;

    /**
     * The wife in the family
     */
    public Individual wife;

    /**
     * The husband in the family
     */
    public Individual husband;

    /**
     * A list of the children in the family
     */
    public List<Individual> children = new ArrayList<Individual>();

    /**
     * The number of children
     */
    public StringWithCustomTags numChildren;

    /**
     * A list of the submitters for this family
     */
    public List<Submitter> submitters = new ArrayList<Submitter>();

    /**
     * The LDS Spouse Sealings for this family
     */
    public List<LdsSpouseSealing> ldsSpouseSealings = new ArrayList<LdsSpouseSealing>();

    /**
     * The source citations for this family
     */
    public List<AbstractCitation> citations = new ArrayList<AbstractCitation>();

    /**
     * The multimedia for this family
     */
    public List<Multimedia> multimedia = new ArrayList<Multimedia>();

    /**
     * The change date information for this family record
     */
    public ChangeDate changeDate;

    /**
     * All the family events
     */
    public List<FamilyEvent> events = new ArrayList<FamilyEvent>();

    /**
     * Notes on this family
     */
    public List<Note> notes = new ArrayList<Note>();

    /**
     * A cross reference id that things can use to identify this family
     */
    public String xref;

    /**
     * The user references
     */
    public List<UserReference> userReferences = new ArrayList<UserReference>();

    /**
     * A notification that this record is in some way restricted. New for GEDCOM 5.5.1. Values are supposed to be
     * "confidential", "locked", or "privacy" but this implementation allows any value.
     */
    public StringWithCustomTags restrictionNotice;

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
        if (getClass() != obj.getClass()) {
            return false;
        }
        Family other = (Family) obj;
        if (automatedRecordId == null) {
            if (other.automatedRecordId != null) {
                return false;
            }
        } else if (!automatedRecordId.equals(other.automatedRecordId)) {
            return false;
        }
        if (changeDate == null) {
            if (other.changeDate != null) {
                return false;
            }
        } else if (!changeDate.equals(other.changeDate)) {
            return false;
        }
        if (children == null) {
            if (other.children != null) {
                return false;
            }
        } else if (!children.equals(other.children)) {
            return false;
        }
        if (citations == null) {
            if (other.citations != null) {
                return false;
            }
        } else if (!citations.equals(other.citations)) {
            return false;
        }
        if (events == null) {
            if (other.events != null) {
                return false;
            }
        } else if (!events.equals(other.events)) {
            return false;
        }
        if (husband == null) {
            if (other.husband != null) {
                return false;
            }
        } else if (!husband.equals(other.husband)) {
            return false;
        }
        if (ldsSpouseSealings == null) {
            if (other.ldsSpouseSealings != null) {
                return false;
            }
        } else if (!ldsSpouseSealings.equals(other.ldsSpouseSealings)) {
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
        if (numChildren == null) {
            if (other.numChildren != null) {
                return false;
            }
        } else if (!numChildren.equals(other.numChildren)) {
            return false;
        }
        if (recFileNumber == null) {
            if (other.recFileNumber != null) {
                return false;
            }
        } else if (!recFileNumber.equals(other.recFileNumber)) {
            return false;
        }
        if (submitters == null) {
            if (other.submitters != null) {
                return false;
            }
        } else if (!submitters.equals(other.submitters)) {
            return false;
        }
        if (userReferences == null) {
            if (other.userReferences != null) {
                return false;
            }
        } else if (!userReferences.equals(other.userReferences)) {
            return false;
        }
        if (wife == null) {
            if (other.wife != null) {
                return false;
            }
        } else if (!wife.equals(other.wife)) {
            return false;
        }
        if (xref == null) {
            if (other.xref != null) {
                return false;
            }
        } else if (!xref.equals(other.xref)) {
            return false;
        }
        if (restrictionNotice == null) {
            if (other.restrictionNotice != null) {
                return false;
            }
        } else if (!restrictionNotice.equals(other.restrictionNotice)) {
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
        result = prime * result + (automatedRecordId == null ? 0 : automatedRecordId.hashCode());
        result = prime * result + (changeDate == null ? 0 : changeDate.hashCode());
        result = prime * result + (children == null ? 0 : children.hashCode());
        result = prime * result + (citations == null ? 0 : citations.hashCode());
        result = prime * result + (events == null ? 0 : events.hashCode());
        result = prime * result + (husband == null ? 0 : husband.hashCode());
        result = prime * result + (ldsSpouseSealings == null ? 0 : ldsSpouseSealings.hashCode());
        result = prime * result + (multimedia == null ? 0 : multimedia.hashCode());
        result = prime * result + (notes == null ? 0 : notes.hashCode());
        result = prime * result + (numChildren == null ? 0 : numChildren.hashCode());
        result = prime * result + (recFileNumber == null ? 0 : recFileNumber.hashCode());
        result = prime * result + (submitters == null ? 0 : submitters.hashCode());
        result = prime * result + (userReferences == null ? 0 : userReferences.hashCode());
        result = prime * result + (wife == null ? 0 : wife.hashCode());
        result = prime * result + (xref == null ? 0 : xref.hashCode());
        result = prime * result + (restrictionNotice == null ? 0 : restrictionNotice.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Family [" + (recFileNumber != null ? "recFileNumber=" + recFileNumber + ", " : "")
                + (automatedRecordId != null ? "automatedRecordId=" + automatedRecordId + ", " : "") + (wife != null ? "wife=" + wife + ", " : "")
                + (husband != null ? "husband=" + husband + ", " : "") + (children != null ? "children=" + children + ", " : "")
                + (numChildren != null ? "numChildren=" + numChildren + ", " : "") + (submitters != null ? "submitters=" + submitters + ", " : "")
                + (ldsSpouseSealings != null ? "ldsSpouseSealings=" + ldsSpouseSealings + ", " : "")
                + (citations != null ? "citations=" + citations + ", " : "") + (multimedia != null ? "multimedia=" + multimedia + ", " : "")
                + (changeDate != null ? "changeDate=" + changeDate + ", " : "") + (events != null ? "events=" + events + ", " : "")
                + (notes != null ? "notes=" + notes + ", " : "") + (xref != null ? "xref=" + xref + ", " : "")
                + (userReferences != null ? "userReferences=" + userReferences + ", " : "")
                + (restrictionNotice != null ? "restrictionNotice=" + restrictionNotice + ", " : "") + (customTags != null ? "customTags=" + customTags : "")
                + "]";
    }

}
