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
 * A family record. Corresponds to FAM_RECORD in the GEDCOM spec.
 * 
 * @author frizbog1
 */
public class Family extends AbstractElement {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -2614258744184662622L;

    /**
     * The automated record ID number
     */
    private StringWithCustomTags automatedRecordId;

    /**
     * The change date information for this family record
     */
    private ChangeDate changeDate;

    /**
     * A list of the children in the family
     */
    private List<Individual> children = getChildren(Options.isCollectionInitializationEnabled());

    /**
     * The citations for this object
     */
    private List<AbstractCitation> citations = getCitations(Options.isCollectionInitializationEnabled());

    /**
     * All the family events
     */
    private List<FamilyEvent> events = getEvents(Options.isCollectionInitializationEnabled());

    /**
     * The husband in the family
     */
    private Individual husband;

    /**
     * The LDS Spouse Sealings for this family
     */
    private List<LdsSpouseSealing> ldsSpouseSealings = getLdsSpouseSealings(Options.isCollectionInitializationEnabled());

    /**
     * Multimedia links for this source citation
     */
    private List<Multimedia> multimedia = getMultimedia(Options.isCollectionInitializationEnabled());

    /**
     * Notes about this object
     */
    private List<Note> notes = getNotes(Options.isCollectionInitializationEnabled());

    /**
     * The number of children
     */
    private StringWithCustomTags numChildren;

    /**
     * The permanent record file number
     */
    private StringWithCustomTags recFileNumber;

    /**
     * A notification that this record is in some way restricted. New for GEDCOM 5.5.1. Values are supposed to be
     * "confidential", "locked", or "privacy" but this implementation allows any value.
     */
    private StringWithCustomTags restrictionNotice;

    /**
     * A list of the submitters for this family
     */
    private List<Submitter> submitters = getSubmitters(Options.isCollectionInitializationEnabled());

    /**
     * The user references for this submitter
     */
    private List<UserReference> userReferences = getUserReferences(Options.isCollectionInitializationEnabled());

    /**
     * The wife in the family
     */
    private Individual wife;

    /**
     * The xref for this submitter
     */
    private String xref;

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
     * Get the automatedRecordId
     * 
     * @return the automatedRecordId
     */
    public StringWithCustomTags getAutomatedRecordId() {
        return automatedRecordId;
    }

    /**
     * Get the changeDate
     * 
     * @return the changeDate
     */
    public ChangeDate getChangeDate() {
        return changeDate;
    }

    /**
     * Get the children
     * 
     * @return the children
     */
    public List<Individual> getChildren() {
        return children;
    }

    /**
     * Get the children
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the children
     */
    public List<Individual> getChildren(boolean initializeIfNeeded) {
        if (initializeIfNeeded && children == null) {
            children = new ArrayList<Individual>(0);
        }
        return children;
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
     * Get the events
     * 
     * @return the events
     */
    public List<FamilyEvent> getEvents() {
        return events;
    }

    /**
     * Get the events
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the events
     */
    public List<FamilyEvent> getEvents(boolean initializeIfNeeded) {
        if (initializeIfNeeded && events == null) {
            events = new ArrayList<FamilyEvent>(0);
        }
        return events;
    }

    /**
     * Get the husband
     * 
     * @return the husband
     */
    public Individual getHusband() {
        return husband;
    }

    /**
     * Get the ldsSpouseSealings
     * 
     * @return the ldsSpouseSealings
     */
    public List<LdsSpouseSealing> getLdsSpouseSealings() {
        return ldsSpouseSealings;
    }

    /**
     * Get the ldsSpouseSealings
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the ldsSpouseSealings
     */
    public List<LdsSpouseSealing> getLdsSpouseSealings(boolean initializeIfNeeded) {
        if (initializeIfNeeded && ldsSpouseSealings == null) {
            ldsSpouseSealings = new ArrayList<LdsSpouseSealing>(0);
        }
        return ldsSpouseSealings;
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
     * Get the numChildren
     * 
     * @return the numChildren
     */
    public StringWithCustomTags getNumChildren() {
        return numChildren;
    }

    /**
     * Get the recFileNumber
     * 
     * @return the recFileNumber
     */
    public StringWithCustomTags getRecFileNumber() {
        return recFileNumber;
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
     * Get the submitters
     * 
     * @return the submitters
     */
    public List<Submitter> getSubmitters() {
        return submitters;
    }

    /**
     * Get the submitters
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the submitters
     */
    public List<Submitter> getSubmitters(boolean initializeIfNeeded) {
        if (initializeIfNeeded && submitters == null) {
            submitters = new ArrayList<Submitter>(0);
        }
        return submitters;
    }

    /**
     * Get the userReferences
     * 
     * @return the userReferences
     */
    public List<UserReference> getUserReferences() {
        return userReferences;
    }

    /**
     * Get the userReferences
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the userReferences
     */
    public List<UserReference> getUserReferences(boolean initializeIfNeeded) {
        if (initializeIfNeeded && userReferences == null) {
            userReferences = new ArrayList<UserReference>(0);
        }
        return userReferences;
    }

    /**
     * Get the wife
     * 
     * @return the wife
     */
    public Individual getWife() {
        return wife;
    }

    /**
     * Get the xref
     * 
     * @return the xref
     */
    public String getXref() {
        return xref;
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

    /**
     * Set the automatedRecordId
     * 
     * @param automatedRecordId
     *            the automatedRecordId to set
     */
    public void setAutomatedRecordId(StringWithCustomTags automatedRecordId) {
        this.automatedRecordId = automatedRecordId;
    }

    /**
     * Set the changeDate
     * 
     * @param changeDate
     *            the changeDate to set
     */
    public void setChangeDate(ChangeDate changeDate) {
        this.changeDate = changeDate;
    }

    /**
     * Set the husband
     * 
     * @param husband
     *            the husband to set
     */
    public void setHusband(Individual husband) {
        this.husband = husband;
    }

    /**
     * Set the numChildren
     * 
     * @param numChildren
     *            the numChildren to set
     */
    public void setNumChildren(StringWithCustomTags numChildren) {
        this.numChildren = numChildren;
    }

    /**
     * Set the recFileNumber
     * 
     * @param recFileNumber
     *            the recFileNumber to set
     */
    public void setRecFileNumber(StringWithCustomTags recFileNumber) {
        this.recFileNumber = recFileNumber;
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
     * Set the wife
     * 
     * @param wife
     *            the wife to set
     */
    public void setWife(Individual wife) {
        this.wife = wife;
    }

    /**
     * Set the xref
     * 
     * @param xref
     *            the xref to set
     */
    public void setXref(String xref) {
        this.xref = xref;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Family [");
        if (automatedRecordId != null) {
            builder.append("automatedRecordId=");
            builder.append(automatedRecordId);
            builder.append(", ");
        }
        if (changeDate != null) {
            builder.append("changeDate=");
            builder.append(changeDate);
            builder.append(", ");
        }
        if (children != null) {
            builder.append("children=");
            builder.append(children);
            builder.append(", ");
        }
        if (citations != null) {
            builder.append("citations=");
            builder.append(citations);
            builder.append(", ");
        }
        if (events != null) {
            builder.append("events=");
            builder.append(events);
            builder.append(", ");
        }
        if (husband != null) {
            builder.append("husband=");
            builder.append(husband);
            builder.append(", ");
        }
        if (ldsSpouseSealings != null) {
            builder.append("ldsSpouseSealings=");
            builder.append(ldsSpouseSealings);
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
        if (numChildren != null) {
            builder.append("numChildren=");
            builder.append(numChildren);
            builder.append(", ");
        }
        if (recFileNumber != null) {
            builder.append("recFileNumber=");
            builder.append(recFileNumber);
            builder.append(", ");
        }
        if (restrictionNotice != null) {
            builder.append("restrictionNotice=");
            builder.append(restrictionNotice);
            builder.append(", ");
        }
        if (submitters != null) {
            builder.append("submitters=");
            builder.append(submitters);
            builder.append(", ");
        }
        if (userReferences != null) {
            builder.append("userReferences=");
            builder.append(userReferences);
            builder.append(", ");
        }
        if (wife != null) {
            builder.append("wife=");
            builder.append(wife);
            builder.append(", ");
        }
        if (xref != null) {
            builder.append("xref=");
            builder.append(xref);
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
