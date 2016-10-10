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
 * A family record. Corresponds to FAM_RECORD in the GEDCOM spec.
 * 
 * @author frizbog1
 */
@SuppressWarnings("PMD.GodClass")
public class Family extends AbstractNotesElement implements HasCitations, HasXref {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -2614258744184662622L;

    /**
     * The automated record ID number
     */
    private StringWithCustomFacts automatedRecordId;

    /**
     * The change date information for this family record
     */
    private ChangeDate changeDate;

    /**
     * A list of the children in the family
     */
    private List<IndividualReference> children = getChildren(Options.isCollectionInitializationEnabled());

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
    private IndividualReference husband;

    /**
     * The LDS Spouse Sealings for this family
     */
    private List<LdsSpouseSealing> ldsSpouseSealings = getLdsSpouseSealings(Options.isCollectionInitializationEnabled());

    /**
     * Multimedia links for this source citation
     */
    private List<MultimediaReference> multimedia = getMultimedia(Options.isCollectionInitializationEnabled());

    /**
     * The number of children
     */
    private StringWithCustomFacts numChildren;

    /**
     * The permanent record file number
     */
    private StringWithCustomFacts recFileNumber;

    /**
     * A notification that this record is in some way restricted. New for GEDCOM 5.5.1. Values are supposed to be "confidential",
     * "locked", or "privacy" but this implementation allows any value.
     */
    private StringWithCustomFacts restrictionNotice;

    /**
     * A list of the submitters for this family
     */
    private List<SubmitterReference> submitters = getSubmitters(Options.isCollectionInitializationEnabled());

    /**
     * The user references for this submitter
     */
    private List<UserReference> userReferences = getUserReferences(Options.isCollectionInitializationEnabled());

    /**
     * The wife in the family
     */
    private IndividualReference wife;

    /**
     * The xref for this submitter
     */
    private String xref;

    /** Default Constructor */
    public Family() {
        // Default constructor does nothing
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            the other object to copy
     */
    public Family(Family other) {
        this(other, true);
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            the other object to copy
     * @param deep
     *            pass in true if a full, deep copy of the family should be created. If false, the family is copied without
     *            references to the individuals in it (the husband, wife, and children).
     */
    public Family(Family other, boolean deep) {
        super(other);
        if (other.automatedRecordId != null) {
            automatedRecordId = new StringWithCustomFacts(other.automatedRecordId);
        }
        if (other.changeDate != null) {
            changeDate = new ChangeDate(other.changeDate);
        }
        if (deep && other.children != null) {
            children = new ArrayList<>();
            for (IndividualReference c : other.children) {
                children.add(new IndividualReference(c, false));
            }
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
        if (other.events != null) {
            events = new ArrayList<>();
            for (FamilyEvent e : other.events) {
                events.add(new FamilyEvent(e));
            }
        }
        if (deep && other.husband != null) {
            husband = new IndividualReference(other.husband, false);
        }
        if (other.ldsSpouseSealings != null) {
            ldsSpouseSealings = new ArrayList<>();
            for (LdsSpouseSealing lss : other.ldsSpouseSealings) {
                ldsSpouseSealings.add(new LdsSpouseSealing(lss));
            }
        }
        if (other.multimedia != null) {
            multimedia = new ArrayList<>();
            for (MultimediaReference m : other.multimedia) {
                multimedia.add(new MultimediaReference(m));
            }
        }
        if (other.numChildren != null) {
            numChildren = new StringWithCustomFacts(other.numChildren);
        }
        if (other.recFileNumber != null) {
            recFileNumber = new StringWithCustomFacts(other.recFileNumber);
        }
        if (other.restrictionNotice != null) {
            restrictionNotice = new StringWithCustomFacts(other.restrictionNotice);
        }
        if (other.submitters != null) {
            submitters = new ArrayList<>();
            for (SubmitterReference s : other.submitters) {
                submitters.add(new SubmitterReference(s));
            }
        }
        if (other.userReferences != null) {
            userReferences = new ArrayList<>();
            for (UserReference ur : other.userReferences) {
                userReferences.add(new UserReference(ur));
            }
        }
        if (deep && other.wife != null) {
            wife = new IndividualReference(other.wife, false);
        }
        xref = other.xref;
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
     * Gets the automated record id.
     *
     * @return the automated record id
     */
    public StringWithCustomFacts getAutomatedRecordId() {
        return automatedRecordId;
    }

    /**
     * Gets the change date.
     *
     * @return the change date
     */
    public ChangeDate getChangeDate() {
        return changeDate;
    }

    /**
     * Gets the children.
     *
     * @return the children
     */
    public List<IndividualReference> getChildren() {
        return children;
    }

    /**
     * Get the children
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the children
     */
    public List<IndividualReference> getChildren(boolean initializeIfNeeded) {
        if (initializeIfNeeded && children == null) {
            children = new ArrayList<>(0);
        }
        return children;
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
     * Gets the events.
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
            events = new ArrayList<>(0);
        }
        return events;
    }

    /**
     * Gets the husband.
     *
     * @return the husband
     */
    public IndividualReference getHusband() {
        return husband;
    }

    /**
     * Gets the LDS spouse sealings.
     *
     * @return the LDS spouse sealings
     */
    public List<LdsSpouseSealing> getLdsSpouseSealings() {
        return ldsSpouseSealings;
    }

    /**
     * Gets the LDS spouse sealings.
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the ldsSpouseSealings
     */
    public List<LdsSpouseSealing> getLdsSpouseSealings(boolean initializeIfNeeded) {
        if (initializeIfNeeded && ldsSpouseSealings == null) {
            ldsSpouseSealings = new ArrayList<>(0);
        }
        return ldsSpouseSealings;
    }

    /**
     * Gets the multimedia.
     *
     * @return the multimedia
     */
    public List<MultimediaReference> getMultimedia() {
        return multimedia;
    }

    /**
     * Get the multimedia
     * 
     * @param initializeIfNeeded
     *            true if this collection should be created on-the-fly if it is currently null
     * @return the multimedia
     */
    public List<MultimediaReference> getMultimedia(boolean initializeIfNeeded) {
        if (initializeIfNeeded && multimedia == null) {
            multimedia = new ArrayList<>(0);
        }
        return multimedia;
    }

    /**
     * Gets the number of children.
     *
     * @return the number of children
     */
    public StringWithCustomFacts getNumChildren() {
        return numChildren;
    }

    /**
     * Gets the rec file number.
     *
     * @return the rec file number
     */
    public StringWithCustomFacts getRecFileNumber() {
        return recFileNumber;
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
     * Gets the submitters.
     *
     * @return the submitters
     */
    public List<SubmitterReference> getSubmitters() {
        return submitters;
    }

    /**
     * Get the submitters
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the submitters
     */
    public List<SubmitterReference> getSubmitters(boolean initializeIfNeeded) {
        if (initializeIfNeeded && submitters == null) {
            submitters = new ArrayList<>(0);
        }
        return submitters;
    }

    /**
     * Gets the user references.
     *
     * @return the user references
     */
    public List<UserReference> getUserReferences() {
        return userReferences;
    }

    /**
     * Get the user references
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the userReferences
     */
    public List<UserReference> getUserReferences(boolean initializeIfNeeded) {
        if (initializeIfNeeded && userReferences == null) {
            userReferences = new ArrayList<>(0);
        }
        return userReferences;
    }

    /**
     * Gets the wife.
     *
     * @return the wife
     */
    public IndividualReference getWife() {
        return wife;
    }

    /**
     * Gets the xref.
     *
     * @return the xref
     */
    @Override
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
     * Sets the automated record id.
     *
     * @param automatedRecordId
     *            the new automated record id
     */
    public void setAutomatedRecordId(String automatedRecordId) {
        this.automatedRecordId = automatedRecordId == null ? null : new StringWithCustomFacts(automatedRecordId);
    }

    /**
     * Sets the automated record id.
     *
     * @param automatedRecordId
     *            the new automated record id
     */
    public void setAutomatedRecordId(StringWithCustomFacts automatedRecordId) {
        this.automatedRecordId = automatedRecordId;
    }

    /**
     * Sets the change date.
     *
     * @param changeDate
     *            the new change date
     */
    public void setChangeDate(ChangeDate changeDate) {
        this.changeDate = changeDate;
    }

    /**
     * Sets the husband.
     *
     * @param husband
     *            the new husband
     */
    public void setHusband(IndividualReference husband) {
        this.husband = husband;
    }

    /**
     * Sets the number of children.
     *
     * @param numChildren
     *            the new number of children
     */
    public void setNumChildren(String numChildren) {
        this.numChildren = numChildren == null ? null : new StringWithCustomFacts(numChildren);
    }

    /**
     * Sets the number of children.
     *
     * @param numChildren
     *            the new number of children
     */
    public void setNumChildren(StringWithCustomFacts numChildren) {
        this.numChildren = numChildren;
    }

    /**
     * Sets the rec file number.
     *
     * @param recFileNumber
     *            the new rec file number
     */
    public void setRecFileNumber(String recFileNumber) {
        this.recFileNumber = recFileNumber == null ? null : new StringWithCustomFacts(recFileNumber);
    }

    /**
     * Sets the rec file number.
     *
     * @param recFileNumber
     *            the new rec file number
     */
    public void setRecFileNumber(StringWithCustomFacts recFileNumber) {
        this.recFileNumber = recFileNumber;
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
     * Sets the wife.
     *
     * @param wife
     *            the new wife
     */
    public void setWife(IndividualReference wife) {
        this.wife = wife;
    }

    /**
     * Sets the xref.
     *
     * @param xref
     *            the new xref
     */
    public void setXref(String xref) {
        this.xref = xref;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(64);
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
        if (getNoteStructures() != null) {
            builder.append("noteStructures=");
            builder.append(getNoteStructures());
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
        if (getCustomFacts() != null) {
            builder.append("customFacts=");
            builder.append(getCustomFacts());
        }
        builder.append("]");
        return builder.toString();
    }

}
