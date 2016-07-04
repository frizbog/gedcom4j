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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gedcom4j.Options;

/**
 * An individual person. Corresponds to the INDIVIDUAL_RECORD structure in the GEDCOM specification.
 * 
 * @author frizbog1
 */
public class Individual extends AbstractElement {
    /**
     * The address of this individual
     */
    private Address address;

    /**
     * Aliases for the current individual.
     */
    private List<StringWithCustomTags> aliases = new ArrayList<StringWithCustomTags>(0);

    /**
     * A list of submitter(s) who are interested in the ancestry of this individual.
     */
    private List<Submitter> ancestorInterest = new ArrayList<Submitter>(0);

    /**
     * The Ancestral File Number of this individual.
     */
    private StringWithCustomTags ancestralFileNumber;

    /**
     * A list of associations to which this individual belongs/belonged.
     */
    private List<Association> associations = new ArrayList<Association>(0);

    /**
     * A list of individual attributes about this individual.
     */
    private List<IndividualAttribute> attributes = new ArrayList<IndividualAttribute>(0);

    /**
     * The change date for this individual
     */
    private ChangeDate changeDate;

    /**
     * The citations for this object
     */
    private List<AbstractCitation> citations = Options.isCollectionInitializationEnabled() ? new ArrayList<AbstractCitation>(0) : null;

    /**
     * A list of submitters who are interested in the descendants of this individual.
     */
    private List<Submitter> descendantInterest = new ArrayList<Submitter>(0);

    /**
     * The emails for this submitter. New for GEDCOM 5.5.1
     */
    private List<StringWithCustomTags> emails = new ArrayList<StringWithCustomTags>(0);

    /**
     * A list of events for this individual.
     */
    private List<IndividualEvent> events = new ArrayList<IndividualEvent>(0);

    /**
     * A list of families to which this individual was a child
     */
    private List<FamilyChild> familiesWhereChild = new ArrayList<FamilyChild>(0);

    /**
     * A list of families to which this individual was either the husband or wife
     */
    private List<FamilySpouse> familiesWhereSpouse = new ArrayList<FamilySpouse>(0);

    /**
     * Fax numbers. New for GEDCOM 5.5.1.
     */
    private List<StringWithCustomTags> faxNumbers = new ArrayList<StringWithCustomTags>(0);

    /**
     * A list of LDS individual ordinances for this individual
     */
    private List<LdsIndividualOrdinance> ldsIndividualOrdinances = new ArrayList<LdsIndividualOrdinance>(0);

    /**
     * Multimedia links for this source citation
     */
    private List<Multimedia> multimedia = new ArrayList<Multimedia>(0);

    /**
     * A list of names for this individual
     */
    private List<PersonalName> names = new ArrayList<PersonalName>(0);

    /**
     * Notes about this object
     */
    private List<Note> notes = Options.isCollectionInitializationEnabled() ? new ArrayList<Note>(0) : null;

    /**
     * The permanent record file number for this individual
     */
    private StringWithCustomTags permanentRecFileNumber;

    /**
     * The phone numbers for this submitter
     */
    private List<StringWithCustomTags> phoneNumbers = new ArrayList<StringWithCustomTags>(0);

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
    private List<Submitter> submitters = new ArrayList<Submitter>(0);

    /**
     * The user references for this submitter
     */
    private List<UserReference> userReferences = new ArrayList<UserReference>(0);

    /**
     * Web URL's. New for GEDCOM 5.5.1.
     */
    private List<StringWithCustomTags> wwwUrls = new ArrayList<StringWithCustomTags>(0);

    /**
     * The xref for this submitter
     */
    private String xref;

    // CHECKSTYLE:OFF for method length
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
        Individual other = (Individual) obj;
        if (address == null) {
            if (other.address != null) {
                return false;
            }
        } else if (!address.equals(other.address)) {
            return false;
        }
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
        if (notes == null) {
            if (other.notes != null) {
                return false;
            }
        } else if (!notes.equals(other.notes)) {
            return false;
        }
        if (permanentRecFileNumber == null) {
            if (other.permanentRecFileNumber != null) {
                return false;
            }
        } else if (!permanentRecFileNumber.equals(other.permanentRecFileNumber)) {
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

    // CHECKSTYLE:ON

    /**
     * Get the address
     * 
     * @return the address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Get the aliases
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
            aliases = new ArrayList<StringWithCustomTags>(0);
        }
        return aliases;
    }

    /**
     * Get the ancestorInterest
     * 
     * @return the ancestorInterest
     */
    public List<Submitter> getAncestorInterest() {
        return ancestorInterest;
    }

    /**
     * Get the ancestorInterest
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed?
     * @return the ancestorInterest
     */
    public List<Submitter> getAncestorInterest(boolean initializeIfNeeded) {
        if (initializeIfNeeded && ancestorInterest == null) {
            ancestorInterest = new ArrayList<Submitter>(0);
        }
        return ancestorInterest;
    }

    /**
     * Get a set of ALL the direct ancestors of the current individual, in all generations. Includes parents, and all
     * their parents, and all <i>their</i> parents, and so on. Siblings, cousins, aunts/uncles, etc. are not included in
     * the results, nor are alternate spouses for parents (unless this individual was also a child of that family).
     * 
     * @return a set of ancestors for the current individual.
     */
    public Set<Individual> getAncestors() {
        Set<Individual> result = new HashSet<Individual>();
        for (FamilyChild f : familiesWhereChild) {
            if (f.getFamily().husband != null && !result.contains(f.getFamily().husband)) {
                result.add(f.getFamily().husband);
                result.addAll(f.getFamily().husband.getAncestors());
            }
            if (f.getFamily().wife != null && !result.contains(f.getFamily().wife)) {
                result.add(f.getFamily().wife);
                result.addAll(f.getFamily().wife.getAncestors());
            }
        }
        return result;
    }

    /**
     * Get the ancestralFileNumber
     * 
     * @return the ancestralFileNumber
     */
    public StringWithCustomTags getAncestralFileNumber() {
        return ancestralFileNumber;
    }

    /**
     * Get the associations
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
            associations = new ArrayList<Association>(0);
        }
        return associations;
    }

    /**
     * Get the attributes
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
            attributes = new ArrayList<IndividualAttribute>(0);
        }
        return attributes;
    }

    /**
     * Get a list of attributes of the supplied type for this individual. For example, calling this method passing
     * <code>IndividualAttributeType.OCCUPATION</code> will return a list of all the occupations recorded for this
     * individual.
     * 
     * @param type
     *            the type of attribute to get
     * @return a list of attributes of the specified type
     */
    public List<IndividualAttribute> getAttributesOfType(IndividualAttributeType type) {
        List<IndividualAttribute> result = new ArrayList<IndividualAttribute>(0);
        for (IndividualAttribute ir : attributes) {
            if (ir.type == type) {
                result.add(ir);
            }
        }
        return result;
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
     * Get the descendantInterest
     * 
     * @return the descendantInterest
     */
    public List<Submitter> getDescendantInterest() {
        return descendantInterest;
    }

    /**
     * Get the descendantInterest
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed?
     * @return the descendantInterest
     */
    public List<Submitter> getDescendantInterest(boolean initializeIfNeeded) {
        if (initializeIfNeeded && descendantInterest == null) {
            descendantInterest = new ArrayList<Submitter>(0);
        }
        return descendantInterest;
    }

    /**
     * Get all the direct blood-line descendants of the current individual, in all generations. Includes children, their
     * children, <i>their</i> children, and so on. Spouses of descendants are not included in the results.
     * 
     * @return a set of descendants for the current individual
     */
    public Set<Individual> getDescendants() {
        Set<Individual> result = new HashSet<Individual>();
        for (FamilySpouse f : familiesWhereSpouse) {
            for (Individual i : f.getFamily().children) {
                // Recurse if we have not seen this person before in the results already
                if (i != this && !result.contains(i) && !i.familiesWhereSpouse.isEmpty()) {
                    Set<Individual> d = i.getDescendants();
                    result.addAll(d);
                }
                result.add(i);
            }
        }
        return result;
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
     * Get the events
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
            events = new ArrayList<IndividualEvent>(0);
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
        List<IndividualEvent> result = new ArrayList<IndividualEvent>(0);
        for (IndividualEvent ie : events) {
            if (ie.type == type) {
                result.add(ie);
            }
        }
        return result;
    }

    /**
     * Get the familiesWhereChild
     * 
     * @return the familiesWhereChild
     */
    public List<FamilyChild> getFamiliesWhereChild() {
        return familiesWhereChild;
    }

    /**
     * Get the familiesWhereChild
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed?
     * @return the familiesWhereChild
     */
    public List<FamilyChild> getFamiliesWhereChild(boolean initializeIfNeeded) {
        if (initializeIfNeeded && familiesWhereChild == null) {
            familiesWhereChild = new ArrayList<FamilyChild>(0);
        }
        return familiesWhereChild;
    }

    /**
     * Get the familiesWhereSpouse
     * 
     * @return the familiesWhereSpouse
     */
    public List<FamilySpouse> getFamiliesWhereSpouse() {
        return familiesWhereSpouse;
    }

    /**
     * Get the familiesWhereSpouse
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed?
     * @return the familiesWhereSpouse
     */
    public List<FamilySpouse> getFamiliesWhereSpouse(boolean initializeIfNeeded) {
        if (initializeIfNeeded && familiesWhereSpouse == null) {
            familiesWhereSpouse = new ArrayList<FamilySpouse>(0);
        }
        return familiesWhereSpouse;
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
     * Get all the individual's names formatted as a single string. Names after the first one found are shown with "aka"
     * in between each.
     * 
     * @return a string with all the various names for the current individual
     */
    public String getFormattedName() {
        StringBuilder sb = new StringBuilder();
        for (PersonalName n : names) {
            if (sb.length() > 0) {
                sb.append(" aka ");
            }
            sb.append(n);
        }
        return sb.toString();
    }

    /**
     * Get the ldsIndividualOrdinances
     * 
     * @return the ldsIndividualOrdinances
     */
    public List<LdsIndividualOrdinance> getLdsIndividualOrdinances() {
        return ldsIndividualOrdinances;
    }

    /**
     * Get the ldsIndividualOrdinances
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed?
     * @return the ldsIndividualOrdinances
     */
    public List<LdsIndividualOrdinance> getLdsIndividualOrdinances(boolean initializeIfNeeded) {
        if (initializeIfNeeded && ldsIndividualOrdinances == null) {
            ldsIndividualOrdinances = new ArrayList<LdsIndividualOrdinance>(0);
        }
        return ldsIndividualOrdinances;
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
     * Get the names
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
            names = new ArrayList<PersonalName>(0);
        }
        return names;
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
     * Get the permanentRecFileNumber
     * 
     * @return the permanentRecFileNumber
     */
    public StringWithCustomTags getPermanentRecFileNumber() {
        return permanentRecFileNumber;
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
     * Get the recIdNumber
     * 
     * @return the recIdNumber
     */
    public StringWithCustomTags getRecIdNumber() {
        return recIdNumber;
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
     * Get the sex
     * 
     * @return the sex
     */
    public StringWithCustomTags getSex() {
        return sex;
    }

    /**
     * Get a set of spouses for the current individual. Always returns a set, although it may be empty. The returned set
     * is in no particular order.
     * 
     * @return a set of spouses for the current individual.
     */
    public Set<Individual> getSpouses() {
        Set<Individual> result = new HashSet<Individual>();
        for (FamilySpouse f : familiesWhereSpouse) {
            Family fam = f.getFamily();
            if (this != fam.husband && fam.husband != null) {
                result.add(fam.husband);
            }
            if (this != fam.wife && fam.wife != null) {
                result.add(fam.wife);
            }
        }
        return result;
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
     *            initialize the collection if needed?
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
        if (xref != null) {
            return prime * result + xref.hashCode();
        }
        result = prime * result;
        result = prime * result + (address == null ? 0 : address.hashCode());
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
        result = prime * result + (notes == null ? 0 : notes.hashCode());
        result = prime * result + (permanentRecFileNumber == null ? 0 : permanentRecFileNumber.hashCode());
        result = prime * result + (phoneNumbers == null ? 0 : phoneNumbers.hashCode());
        result = prime * result + (faxNumbers == null ? 0 : faxNumbers.hashCode());
        result = prime * result + (wwwUrls == null ? 0 : wwwUrls.hashCode());
        result = prime * result + (emails == null ? 0 : emails.hashCode());
        result = prime * result + (recIdNumber == null ? 0 : recIdNumber.hashCode());
        result = prime * result + (restrictionNotice == null ? 0 : restrictionNotice.hashCode());
        result = prime * result + (sex == null ? 0 : sex.hashCode());
        result = prime * result + (submitters == null ? 0 : submitters.hashCode());
        result = prime * result + (userReferences == null ? 0 : userReferences.hashCode());
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
     * Set the ancestralFileNumber
     * 
     * @param ancestralFileNumber
     *            the ancestralFileNumber to set
     */
    public void setAncestralFileNumber(StringWithCustomTags ancestralFileNumber) {
        this.ancestralFileNumber = ancestralFileNumber;
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
     * Set the permanentRecFileNumber
     * 
     * @param permanentRecFileNumber
     *            the permanentRecFileNumber to set
     */
    public void setPermanentRecFileNumber(StringWithCustomTags permanentRecFileNumber) {
        this.permanentRecFileNumber = permanentRecFileNumber;
    }

    /**
     * Set the recIdNumber
     * 
     * @param recIdNumber
     *            the recIdNumber to set
     */
    public void setRecIdNumber(StringWithCustomTags recIdNumber) {
        this.recIdNumber = recIdNumber;
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
     * Set the sex
     * 
     * @param sex
     *            the sex to set
     */
    public void setSex(StringWithCustomTags sex) {
        this.sex = sex;
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
        StringBuilder sb = new StringBuilder(64); // Initial size - we know we're going to be appending more than 16
        // chars in most cases
        sb.append(getFormattedName());
        for (StringWithCustomTags n : aliases) {
            if (sb.length() > 0) {
                sb.append(" aka ");
            }
            sb.append(n);
        }
        if (sb.length() == 0) {
            sb.append("Unknown name");
        }
        for (FamilySpouse f : familiesWhereSpouse) {
            sb.append(", spouse of ");
            Family fam = f.getFamily();
            if (fam.husband == this) {
                if (fam.wife == null) {
                    sb.append("unknown");
                } else {
                    sb.append(fam.wife.getFormattedName());
                }
            } else {
                if (fam.husband == null) {
                    sb.append("unknown");
                } else {
                    sb.append(fam.husband.getFormattedName());
                }
            }
        }
        for (FamilyChild f : familiesWhereChild) {
            sb.append(", child of ");
            if (f.getFamily().wife != null) {
                sb.append(f.getFamily().wife.getFormattedName());
                sb.append(" and ");
            }
            if (f.getFamily().husband == null) {
                sb.append("unknown");
            } else {
                sb.append(f.getFamily().husband.getFormattedName());
            }
        }
        boolean found = false;
        for (IndividualEvent b : getEventsOfType(IndividualEventType.BIRTH)) {
            if (found) {
                sb.append(" / ");
            } else {
                sb.append(", b.");
            }
            sb.append(b.date);
            found = true;
        }
        found = false;
        for (IndividualEvent d : getEventsOfType(IndividualEventType.DEATH)) {
            if (found) {
                sb.append(" / ");
            } else {
                sb.append(", d.");
            }
            sb.append(d.date);
            found = true;
        }
        return sb.toString();
    }

}
