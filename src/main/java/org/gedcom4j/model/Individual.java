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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gedcom4j.Options;

/**
 * An individual person. Corresponds to the INDIVIDUAL_RECORD structure in the GEDCOM specification.
 * 
 * @author frizbog1
 */
@SuppressWarnings({ "PMD.ExcessiveClassLength", "PMD.ExcessivePublicCount", "PMD.GodClass" })
public class Individual extends AbstractAddressableElement implements HasCitations, HasXref {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -3542679192972351102L;

    /**
     * Aliases for the current individual.
     */
    private List<StringWithCustomTags> aliases = getAliases(Options.isCollectionInitializationEnabled());

    /**
     * A list of submitter(s) who are interested in the ancestry of this individual.
     */
    private List<Submitter> ancestorInterest = getAncestorInterest(Options.isCollectionInitializationEnabled());

    /**
     * The Ancestral File Number of this individual.
     */
    private StringWithCustomTags ancestralFileNumber;

    /**
     * A list of associations to which this individual belongs/belonged.
     */
    private List<Association> associations = getAssociations(Options.isCollectionInitializationEnabled());

    /**
     * A list of individual attributes about this individual.
     */
    private List<IndividualAttribute> attributes = getAttributes(Options.isCollectionInitializationEnabled());

    /**
     * The change date for this individual
     */
    private ChangeDate changeDate;

    /**
     * The citations for this object
     */
    private List<AbstractCitation> citations = getCitations(Options.isCollectionInitializationEnabled());

    /**
     * A list of submitters who are interested in the descendants of this individual.
     */
    private List<Submitter> descendantInterest = getDescendantInterest(Options.isCollectionInitializationEnabled());

    /**
     * A list of events for this individual.
     */
    private List<IndividualEvent> events = getEvents(Options.isCollectionInitializationEnabled());

    /**
     * A list of families to which this individual was a child
     */
    private List<FamilyChild> familiesWhereChild = getFamiliesWhereChild(Options.isCollectionInitializationEnabled());

    /**
     * A list of families to which this individual was either the husband or wife
     */
    private List<FamilySpouse> familiesWhereSpouse = getFamiliesWhereSpouse(Options.isCollectionInitializationEnabled());

    /**
     * A list of LDS individual ordinances for this individual
     */
    private List<LdsIndividualOrdinance> ldsIndividualOrdinances = getLdsIndividualOrdinances(Options
            .isCollectionInitializationEnabled());

    /**
     * Multimedia links for this source citation
     */
    private List<Multimedia> multimedia = getMultimedia(Options.isCollectionInitializationEnabled());

    /**
     * A list of names for this individual
     */
    private List<PersonalName> names = getNames(Options.isCollectionInitializationEnabled());

    /**
     * The permanent record file number for this individual
     */
    private StringWithCustomTags permanentRecFileNumber;

    /**
     * The record ID number
     */
    private StringWithCustomTags recIdNumber;

    /**
     * The restriction notice (if any) for this individual
     */
    private StringWithCustomTags restrictionNotice;

    /**
     * The sex of this individual
     */
    private StringWithCustomTags sex;

    /**
     * A list of submitter(s) of this individual
     */
    private List<Submitter> submitters = getSubmitters(Options.isCollectionInitializationEnabled());

    /**
     * The user references for this submitter
     */
    private List<UserReference> userReferences = getUserReferences(Options.isCollectionInitializationEnabled());

    /**
     * The xref for this submitter
     */
    private String xref;

    /** Default constructor */
    public Individual() {
        // Default constructor does nothing
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            object being copied
     */
    public Individual(Individual other) {
        this(other, true);
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            object being copied
     * @param deep
     *            pass in true if a full, deep copy should be made for any families the individual is in. If false, the families
     *            will be copied but will not contain back-references to the individuals in it (husband, wife, children). This
     *            allows preventing infinite recursion.
     */
    public Individual(Individual other, boolean deep) {
        super(other);
        if (other.aliases != null) {
            aliases = new ArrayList<>();
            for (StringWithCustomTags swct : other.aliases) {
                aliases.add(new StringWithCustomTags(swct));
            }
        }
        if (other.ancestorInterest != null) {
            ancestorInterest = new ArrayList<>();
            for (Submitter ai : other.ancestorInterest) {
                ancestorInterest.add(new Submitter(ai));
            }
        }
        if (other.ancestralFileNumber != null) {
            ancestralFileNumber = new StringWithCustomTags(other.ancestralFileNumber);
        }
        if (other.associations != null) {
            associations = new ArrayList<>();
            for (Association a : other.associations) {
                associations.add(new Association(a));
            }
        }
        if (other.attributes != null) {
            attributes = new ArrayList<>();
            for (IndividualAttribute ia : other.attributes) {
                attributes.add(new IndividualAttribute(ia));
            }
        }
        if (other.changeDate != null) {
            changeDate = new ChangeDate(other.changeDate);
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
        if (other.descendantInterest != null) {
            descendantInterest = new ArrayList<>();
            for (Submitter ai : other.descendantInterest) {
                descendantInterest.add(new Submitter(ai));
            }
        }
        if (other.events != null) {
            events = new ArrayList<>();
            for (IndividualEvent e : other.events) {
                events.add(new IndividualEvent(e));
            }
        }
        if (other.familiesWhereChild != null) {
            familiesWhereChild = new ArrayList<>();
            for (FamilyChild fc : other.familiesWhereChild) {
                familiesWhereChild.add(new FamilyChild(fc, deep));
            }
        }
        if (other.familiesWhereSpouse != null) {
            familiesWhereSpouse = new ArrayList<>();
            for (FamilySpouse fs : other.familiesWhereSpouse) {
                familiesWhereSpouse.add(new FamilySpouse(fs, deep));
            }
        }
        if (other.ldsIndividualOrdinances != null) {
            ldsIndividualOrdinances = new ArrayList<>();
            for (LdsIndividualOrdinance lio : other.ldsIndividualOrdinances) {
                ldsIndividualOrdinances.add(new LdsIndividualOrdinance(lio));
            }
        }
        if (other.multimedia != null) {
            multimedia = new ArrayList<>();
            for (Multimedia m : other.multimedia) {
                multimedia.add(new Multimedia(m));
            }
        }
        if (other.names != null) {
            names = new ArrayList<>();
            for (PersonalName pn : other.names) {
                names.add(new PersonalName(pn));
            }
        }
        if (other.permanentRecFileNumber != null) {
            permanentRecFileNumber = new StringWithCustomTags(other.permanentRecFileNumber);
        }
        if (other.recIdNumber != null) {
            recIdNumber = new StringWithCustomTags(other.recIdNumber);
        }
        if (other.restrictionNotice != null) {
            restrictionNotice = new StringWithCustomTags(other.restrictionNotice);
        }
        if (other.sex != null) {
            sex = new StringWithCustomTags(other.sex);
        }
        if (other.submitters != null) {
            submitters = new ArrayList<>();
            for (Submitter s : other.submitters) {
                submitters.add(new Submitter(s));
            }
        }
        if (other.userReferences != null) {
            userReferences = new ArrayList<>();
            for (UserReference ur : other.userReferences) {
                userReferences.add(new UserReference(ur));
            }
        }
        xref = other.xref;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({ "PMD.NcssMethodCount", "PMD.ExcessiveMethodLength", "checkstyle:methodlength" })
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
        Individual other = (Individual) obj;
        if (aliases == null) {
            if (other.aliases != null) {
                return false;
            }
        } else if (!aliases.equals(other.aliases)) {
            return false;
        }
        if (ancestorInterest == null) {
            if (other.ancestorInterest != null) {
                return false;
            }
        } else if (!ancestorInterest.equals(other.ancestorInterest)) {
            return false;
        }
        if (ancestralFileNumber == null) {
            if (other.ancestralFileNumber != null) {
                return false;
            }
        } else if (!ancestralFileNumber.equals(other.ancestralFileNumber)) {
            return false;
        }
        if (associations == null) {
            if (other.associations != null) {
                return false;
            }
        } else if (!associations.equals(other.associations)) {
            return false;
        }
        if (attributes == null) {
            if (other.attributes != null) {
                return false;
            }
        } else if (!attributes.equals(other.attributes)) {
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
        if (descendantInterest == null) {
            if (other.descendantInterest != null) {
                return false;
            }
        } else if (!descendantInterest.equals(other.descendantInterest)) {
            return false;
        }
        if (events == null) {
            if (other.events != null) {
                return false;
            }
        } else if (!events.equals(other.events)) {
            return false;
        }
        if (familiesWhereChild == null) {
            if (other.familiesWhereChild != null) {
                return false;
            }
        } else if (!familiesWhereChild.equals(other.familiesWhereChild)) {
            return false;
        }
        if (familiesWhereSpouse == null) {
            if (other.familiesWhereSpouse != null) {
                return false;
            }
        } else if (!familiesWhereSpouse.equals(other.familiesWhereSpouse)) {
            return false;
        }
        if (ldsIndividualOrdinances == null) {
            if (other.ldsIndividualOrdinances != null) {
                return false;
            }
        } else if (!ldsIndividualOrdinances.equals(other.ldsIndividualOrdinances)) {
            return false;
        }
        if (multimedia == null) {
            if (other.multimedia != null) {
                return false;
            }
        } else if (!multimedia.equals(other.multimedia)) {
            return false;
        }
        if (names == null) {
            if (other.names != null) {
                return false;
            }
        } else if (!names.equals(other.names)) {
            return false;
        }
        if (permanentRecFileNumber == null) {
            if (other.permanentRecFileNumber != null) {
                return false;
            }
        } else if (!permanentRecFileNumber.equals(other.permanentRecFileNumber)) {
            return false;
        }
        if (recIdNumber == null) {
            if (other.recIdNumber != null) {
                return false;
            }
        } else if (!recIdNumber.equals(other.recIdNumber)) {
            return false;
        }

        if (restrictionNotice == null) {
            if (other.restrictionNotice != null) {
                return false;
            }
        } else if (!restrictionNotice.equals(other.restrictionNotice)) {
            return false;
        }
        if (sex == null) {
            if (other.sex != null) {
                return false;
            }
        } else if (!sex.equals(other.sex)) {
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
     * Gets the aliases.
     *
     * @return the aliases
     */
    public List<StringWithCustomTags> getAliases() {
        return aliases;
    }

    /**
     * Get the aliases
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed?
     * @return the aliases
     */
    public List<StringWithCustomTags> getAliases(boolean initializeIfNeeded) {
        if (initializeIfNeeded && aliases == null) {
            aliases = new ArrayList<>(0);
        }
        return aliases;
    }

    /**
     * Gets the ancestor interest.
     *
     * @return the ancestor interest
     */
    public List<Submitter> getAncestorInterest() {
        return ancestorInterest;
    }

    /**
     * Get the ancestor interest
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed?
     * @return the ancestor interest
     */
    public List<Submitter> getAncestorInterest(boolean initializeIfNeeded) {
        if (initializeIfNeeded && ancestorInterest == null) {
            ancestorInterest = new ArrayList<>(0);
        }
        return ancestorInterest;
    }

    /**
     * Gets the ancestors.
     *
     * @return the ancestors
     */
    public Set<Individual> getAncestors() {
        Set<Individual> result = new HashSet<>();
        addGenerationOfAncestors(result);
        return result;
    }

    /**
     * Gets the ancestral file number.
     *
     * @return the ancestral file number
     */
    public StringWithCustomTags getAncestralFileNumber() {
        return ancestralFileNumber;
    }

    /**
     * Gets the associations.
     *
     * @return the associations
     */
    public List<Association> getAssociations() {
        return associations;
    }

    /**
     * Get the associations
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed?
     * @return the associations
     */
    public List<Association> getAssociations(boolean initializeIfNeeded) {
        if (initializeIfNeeded && associations == null) {
            associations = new ArrayList<>(0);
        }
        return associations;
    }

    /**
     * Gets the attributes.
     *
     * @return the attributes
     */
    public List<IndividualAttribute> getAttributes() {
        return attributes;
    }

    /**
     * Get the attributes
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed?
     * @return the attributes
     */
    public List<IndividualAttribute> getAttributes(boolean initializeIfNeeded) {
        if (initializeIfNeeded && attributes == null) {
            attributes = new ArrayList<>(0);
        }
        return attributes;
    }

    /**
     * Get a list of attributes of the supplied type for this individual. For example, calling this method passing
     * <code>IndividualAttributeType.OCCUPATION</code> will return a list of all the occupations recorded for this individual.
     * 
     * @param type
     *            the type of attribute to get
     * @return a list of attributes of the specified type
     */
    public List<IndividualAttribute> getAttributesOfType(IndividualAttributeType type) {
        List<IndividualAttribute> result = new ArrayList<>(0);
        for (IndividualAttribute ir : attributes) {
            if (ir.getType() == type) {
                result.add(ir);
            }
        }
        return result;
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
     * Gets the citations.
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
            citations = new ArrayList<>(0);
        }
        return citations;
    }

    /**
     * Gets the descendant interest.
     *
     * @return the descendant interest
     */
    public List<Submitter> getDescendantInterest() {
        return descendantInterest;
    }

    /**
     * Get the descendant interest
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed?
     * @return the descendant interest
     */
    public List<Submitter> getDescendantInterest(boolean initializeIfNeeded) {
        if (initializeIfNeeded && descendantInterest == null) {
            descendantInterest = new ArrayList<>(0);
        }
        return descendantInterest;
    }

    /**
     * Gets the descendants.
     *
     * @return the descendants
     */
    public Set<Individual> getDescendants() {
        Set<Individual> result = new HashSet<>();
        addGenerationOfDescendants(result);
        return result;
    }

    /**
     * Gets the events.
     *
     * @return the events
     */
    public List<IndividualEvent> getEvents() {
        return events;
    }

    /**
     * Get the events
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed?
     * @return the events
     */
    public List<IndividualEvent> getEvents(boolean initializeIfNeeded) {
        if (initializeIfNeeded && events == null) {
            events = new ArrayList<>(0);
        }
        return events;
    }

    /**
     * Get a list of events of the supplied type for this individual
     * 
     * @param type
     *            the type of event to get
     * @return a list of events of the specified type
     */
    public List<IndividualEvent> getEventsOfType(IndividualEventType type) {
        List<IndividualEvent> result = new ArrayList<>(0);
        if (events != null) {
            for (IndividualEvent ie : events) {
                if (ie.getType() == type) {
                    result.add(ie);
                }
            }
        }
        return result;
    }

    /**
     * Gets the families where child.
     *
     * @return the families where child
     */
    public List<FamilyChild> getFamiliesWhereChild() {
        return familiesWhereChild;
    }

    /**
     * Gets the families where child.
     *
     * @param initializeIfNeeded
     *            initialize the collection if needed?
     * @return the families where child
     */
    public List<FamilyChild> getFamiliesWhereChild(boolean initializeIfNeeded) {
        if (initializeIfNeeded && familiesWhereChild == null) {
            familiesWhereChild = new ArrayList<>(0);
        }
        return familiesWhereChild;
    }

    /**
     * Gets the families where spouse.
     *
     * @return the families where spouse
     */
    public List<FamilySpouse> getFamiliesWhereSpouse() {
        return familiesWhereSpouse;
    }

    /**
     * Gets the families where spouse.
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed?
     * @return the families where spouse
     */
    public List<FamilySpouse> getFamiliesWhereSpouse(boolean initializeIfNeeded) {
        if (initializeIfNeeded && familiesWhereSpouse == null) {
            familiesWhereSpouse = new ArrayList<>(0);
        }
        return familiesWhereSpouse;
    }

    /**
     * Gets the formatted name.
     *
     * @return the formatted name
     */
    public String getFormattedName() {
        StringBuilder sb = new StringBuilder();
        if (names != null) {
            for (PersonalName n : names) {
                if (sb.length() > 0) {
                    sb.append(" aka ");
                }
                sb.append(n);
            }
        }
        return sb.toString();
    }

    /**
     * Gets the LDS individual ordinances.
     *
     * @return the LDS individual ordinances
     */
    public List<LdsIndividualOrdinance> getLdsIndividualOrdinances() {
        return ldsIndividualOrdinances;
    }

    /**
     * Gets the LDS individual ordinances.
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed?
     * @return the LDS individual ordinances
     */
    public List<LdsIndividualOrdinance> getLdsIndividualOrdinances(boolean initializeIfNeeded) {
        if (initializeIfNeeded && ldsIndividualOrdinances == null) {
            ldsIndividualOrdinances = new ArrayList<>(0);
        }
        return ldsIndividualOrdinances;
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
     * Gets the names.
     *
     * @return the names
     */
    public List<PersonalName> getNames() {
        return names;
    }

    /**
     * Get the names
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed?
     * @return the names
     */
    public List<PersonalName> getNames(boolean initializeIfNeeded) {
        if (initializeIfNeeded && names == null) {
            names = new ArrayList<>(0);
        }
        return names;
    }

    /**
     * Gets the permanent rec file number.
     *
     * @return the permanent rec file number
     */
    public StringWithCustomTags getPermanentRecFileNumber() {
        return permanentRecFileNumber;
    }

    /**
     * Gets the rec id number.
     *
     * @return the rec id number
     */
    public StringWithCustomTags getRecIdNumber() {
        return recIdNumber;
    }

    /**
     * Gets the restriction notice.
     *
     * @return the restriction notice
     */
    public StringWithCustomTags getRestrictionNotice() {
        return restrictionNotice;
    }

    /**
     * Gets the sex.
     *
     * @return the sex
     */
    public StringWithCustomTags getSex() {
        return sex;
    }

    /**
     * Gets the spouses.
     *
     * @return the spouses
     */
    public Set<Individual> getSpouses() {
        Set<Individual> result = new HashSet<>();
        if (familiesWhereSpouse != null) {
            for (FamilySpouse f : familiesWhereSpouse) {
                Family fam = f.getFamily();
                if (this != fam.getHusband() && fam.getHusband() != null) {
                    result.add(fam.getHusband());
                }
                if (this != fam.getWife() && fam.getWife() != null) {
                    result.add(fam.getWife());
                }
            }
        }
        return result;
    }

    /**
     * Gets the submitters.
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
     *            initialize the collection if needed?
     * @return the submitters
     */
    public List<Submitter> getSubmitters(boolean initializeIfNeeded) {
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
     * @return the user references
     */
    public List<UserReference> getUserReferences(boolean initializeIfNeeded) {
        if (initializeIfNeeded && userReferences == null) {
            userReferences = new ArrayList<>(0);
        }
        return userReferences;
    }

    /**
     * Gets the xref.
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
        if (xref != null) {
            return prime * result + xref.hashCode();
        }
        result = prime * result;
        result = prime * result + (aliases == null ? 0 : aliases.hashCode());
        result = prime * result + (ancestorInterest == null ? 0 : ancestorInterest.hashCode());
        result = prime * result + (ancestralFileNumber == null ? 0 : ancestralFileNumber.hashCode());
        result = prime * result + (associations == null ? 0 : associations.hashCode());
        result = prime * result + (attributes == null ? 0 : attributes.hashCode());
        result = prime * result + (changeDate == null ? 0 : changeDate.hashCode());
        result = prime * result + (citations == null ? 0 : citations.hashCode());
        result = prime * result + (descendantInterest == null ? 0 : descendantInterest.hashCode());
        result = prime * result + (events == null ? 0 : events.hashCode());
        result = prime * result + (familiesWhereChild == null ? 0 : familiesWhereChild.hashCode());
        result = prime * result + (familiesWhereSpouse == null ? 0 : familiesWhereSpouse.hashCode());
        result = prime * result + (ldsIndividualOrdinances == null ? 0 : ldsIndividualOrdinances.hashCode());
        result = prime * result + (multimedia == null ? 0 : multimedia.hashCode());
        result = prime * result + (names == null ? 0 : names.hashCode());
        result = prime * result + (permanentRecFileNumber == null ? 0 : permanentRecFileNumber.hashCode());
        result = prime * result + (recIdNumber == null ? 0 : recIdNumber.hashCode());
        result = prime * result + (restrictionNotice == null ? 0 : restrictionNotice.hashCode());
        result = prime * result + (sex == null ? 0 : sex.hashCode());
        result = prime * result + (submitters == null ? 0 : submitters.hashCode());
        result = prime * result + (userReferences == null ? 0 : userReferences.hashCode());
        return result;
    }

    /**
     * Sets the ancestral file number.
     *
     * @param ancestralFileNumber
     *            the new ancestral file number
     */
    public void setAncestralFileNumber(StringWithCustomTags ancestralFileNumber) {
        this.ancestralFileNumber = ancestralFileNumber;
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
     * Sets the permanent rec file number.
     *
     * @param permanentRecFileNumber
     *            the new permanent rec file number
     */
    public void setPermanentRecFileNumber(StringWithCustomTags permanentRecFileNumber) {
        this.permanentRecFileNumber = permanentRecFileNumber;
    }

    /**
     * Sets the rec id number.
     *
     * @param recIdNumber
     *            the new rec id number
     */
    public void setRecIdNumber(StringWithCustomTags recIdNumber) {
        this.recIdNumber = recIdNumber;
    }

    /**
     * Sets the restriction notice.
     *
     * @param restrictionNotice
     *            the new restriction notice
     */
    public void setRestrictionNotice(StringWithCustomTags restrictionNotice) {
        this.restrictionNotice = restrictionNotice;
    }

    /**
     * Sets the sex.
     *
     * @param sex
     *            the new sex
     */
    public void setSex(StringWithCustomTags sex) {
        this.sex = sex;
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
        StringBuilder sb = new StringBuilder(64);
        sb.append(getFormattedName());
        if (aliases != null) {
            for (StringWithCustomTags n : aliases) {
                if (sb.length() > 0) {
                    sb.append(" aka ");
                }
                sb.append(n);
            }
        }
        if (sb.length() == 0) {
            sb.append("Unknown name");
        }
        if (familiesWhereSpouse != null) {
            for (FamilySpouse f : familiesWhereSpouse) {
                Family fam = f.getFamily();
                if (fam.getHusband() == this) {
                    if (fam.getWife() != null) {
                        sb.append(", spouse of ");
                        sb.append(fam.getWife().getFormattedName());
                    }
                } else {
                    if (fam.getHusband() != null) {
                        sb.append(", spouse of ");
                        sb.append(fam.getHusband().getFormattedName());
                    }
                }
            }
        }
        if (familiesWhereChild != null) {
            for (FamilyChild f : familiesWhereChild) {
                sb.append(", child of ");
                if (f.getFamily().getWife() != null) {
                    sb.append(f.getFamily().getWife().getFormattedName());
                    sb.append(" and ");
                }
                if (f.getFamily().getHusband() == null) {
                    sb.append("unknown");
                } else {
                    sb.append(f.getFamily().getHusband().getFormattedName());
                }
            }
        }
        boolean found = false;
        List<IndividualEvent> births = getEventsOfType(IndividualEventType.BIRTH);
        if (births != null) {
            for (IndividualEvent b : births) {
                if (found) {
                    sb.append(" / ");
                } else {
                    sb.append(", b.");
                }
                sb.append(b.getDate());
                found = true;
            }
        }
        found = false;
        List<IndividualEvent> deaths = getEventsOfType(IndividualEventType.DEATH);
        if (deaths != null) {
            for (IndividualEvent d : deaths) {
                if (found) {
                    sb.append(" / ");
                } else {
                    sb.append(", d.");
                }
                sb.append(d.getDate());
                found = true;
            }
        }
        return sb.toString();
    }

    /**
     * Recursive method for adding the ancestors of this individual to a running set of individuals. This running set is used to
     * track who we've seen so far, so we don't infinitely recurse.
     * 
     * @param seenSoFar
     *            the running set of people we've seen so far in adding ancestors
     * @return the people in another generation of ancestors, to be added to the running list
     */
    private Set<Individual> addGenerationOfAncestors(Set<Individual> seenSoFar) {
        Set<Individual> result = new HashSet<>();
        if (familiesWhereChild != null) {
            for (FamilyChild f : familiesWhereChild) {
                if (f == null) {
                    continue;
                }
                Individual husband = f.getFamily().getHusband();
                if (husband != null && !seenSoFar.contains(husband)) {
                    result.add(husband);
                    seenSoFar.add(husband);
                    Set<Individual> husbandsAncestors = husband.addGenerationOfAncestors(seenSoFar);
                    result.addAll(husbandsAncestors);
                    seenSoFar.addAll(husbandsAncestors);
                }
                Individual wife = f.getFamily().getWife();
                if (wife != null && !seenSoFar.contains(wife)) {
                    result.add(wife);
                    seenSoFar.add(wife);
                    Set<Individual> wifesAncestors = wife.addGenerationOfAncestors(seenSoFar);
                    result.addAll(wifesAncestors);
                    seenSoFar.addAll(wifesAncestors);
                }
            }
        }
        return result;
    }

    /**
     * Recursive method for adding the descendants of this individual to a running set of individuals. This running set is used to
     * track who we've seen so far, so we don't infinitely recurse.
     * 
     * @param seenSoFar
     *            the running set of people we've seen so far in adding descendants
     * @return the people in another generation of ancestors, to be added to the running list
     */
    private Set<Individual> addGenerationOfDescendants(Set<Individual> seenSoFar) {
        Set<Individual> result = new HashSet<>();
        if (familiesWhereSpouse != null) {
            for (FamilySpouse f : familiesWhereSpouse) {
                if (f.getFamily().getChildren() != null) {
                    for (Individual i : f.getFamily().getChildren()) {
                        if (i == null) {
                            continue;
                        }
                        // Recurse if we have not seen this person before in the results already, and they have spouses
                        boolean notSeenAlready = i != this && !seenSoFar.contains(i);
                        boolean hasSpouses = i.familiesWhereSpouse != null && !i.familiesWhereSpouse.isEmpty();
                        if (notSeenAlready && hasSpouses) {
                            seenSoFar.add(i);
                            Set<Individual> d = i.addGenerationOfDescendants(seenSoFar);
                            result.addAll(d);
                            seenSoFar.addAll(d);
                        }
                        result.add(i);
                    }
                }
            }
        }
        return result;
    }

}
