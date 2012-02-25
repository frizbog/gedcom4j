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
package com.mattharrah.gedcom4j.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An individual person. Corresponds to the INDIVIDUAL_RECORD structure in the
 * GEDCOM specification.
 * 
 * @author frizbog1
 */
public class Individual {
    /**
     * Aliases for the current individual.
     */
    public List<String> aliases = new ArrayList<String>();

    /**
     * A list of submitter(s) who are interested in the ancestry of this
     * individual.
     */
    public List<Submitter> ancestorInterest = new ArrayList<Submitter>();
    /**
     * The Ancestral File Number of this individual.
     */
    public String ancestralFileNumber;
    /**
     * A list of associations to which this individual belongs/belonged.
     */
    public List<Association> associations = new ArrayList<Association>();
    /**
     * A list of individual attributes about this individual.
     */
    public List<IndividualAttribute> attributes = new ArrayList<IndividualAttribute>();
    /**
     * The change date for this individual
     */
    public ChangeDate changeDate;
    /**
     * A list of citations of sources about this individual
     */
    public List<AbstractCitation> citations = new ArrayList<AbstractCitation>();
    /**
     * A list of submitters who are interested in the descendants of this
     * individual.
     */
    public List<Submitter> descendantInterest = new ArrayList<Submitter>();
    /**
     * A list of events for this individual.
     */
    public List<IndividualEvent> events = new ArrayList<IndividualEvent>();
    /**
     * A list of families to which this individual was a child
     */
    public List<FamilyChild> familiesWhereChild = new ArrayList<FamilyChild>();
    /**
     * A list of families to which this individual was either the husband or
     * wife
     */
    public List<FamilySpouse> familiesWhereSpouse = new ArrayList<FamilySpouse>();
    /**
     * A list of LDS individual ordinances for this individual
     */
    public List<LdsIndividualOrdinance> ldsIndividualOrdinances = new ArrayList<LdsIndividualOrdinance>();
    /**
     * A list of multimedia items for this individual
     */
    public List<Multimedia> multimedia = new ArrayList<Multimedia>();
    /**
     * A list of names for this individual
     */
    public List<PersonalName> names = new ArrayList<PersonalName>();
    /**
     * A list of notes for this individual
     */
    public List<Note> notes = new ArrayList<Note>();
    /**
     * The permanent record file number for this individual
     */
    public String permanentRecFileNumber;
    /**
     * The record ID number for this individual
     */
    public String recIdNumber;
    /**
     * The restriction notice (if any) for this individual
     */
    public String restrictionNotice;
    /**
     * The sex of this individual
     */
    public String sex;
    /**
     * A list of submitter(s) of this individual
     */
    public List<Submitter> submitters = new ArrayList<Submitter>();
    /**
     * A list of user references for this individual
     */
    public List<UserReference> userReferences = new ArrayList<UserReference>();
    /**
     * The cross-reference ID for this individual
     */
    public String xref;
    /**
     * The address of this individual
     */
    public Address address;
    /**
     * The phone numbers for the individual
     */
    public List<String> phoneNumbers = new ArrayList<String>();

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
        } else if (!ldsIndividualOrdinances
                .equals(other.ldsIndividualOrdinances)) {
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
     * Get all the individual's names formatted as a single string. Names after
     * the first one found are shown with "aka" in between each.
     * 
     * @return a string with all the various names for the current individual
     */
    public String formattedName() {
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
     * Get a set of ALL the direct ancestors of the current individual, in all
     * generations. Includes parents, and all their parents, and all
     * <i>their</i> parents, and so on. Siblings, cousins, aunts/uncles, etc.
     * are not included in the results.
     * 
     * @return a set of ancestors for the current individual.
     */
    public Set<Individual> getAncestors() {
        Set<Individual> result = new HashSet<Individual>();
        for (FamilyChild f : this.familiesWhereChild) {
            if (f.family.husband != null) {
                result.add(f.family.husband);
                result.addAll(f.family.husband.getAncestors());
            }
            if (f.family.wife != null) {
                result.add(f.family.wife);
                result.addAll(f.family.wife.getAncestors());
            }
        }
        return result;
    }

    /**
     * Get all the direct blood-line descendants of the current individual, in
     * all generations. Includes children, their children, <i>their</i>
     * children, and so on. Spouses of descendants are not included in the
     * results.
     * 
     * @return a set of descendants for the current individual
     */
    public Set<Individual> getDescendants() {
        Set<Individual> result = new HashSet<Individual>();
        for (FamilySpouse f : this.familiesWhereSpouse) {
            result.addAll(f.family.children);
            for (Individual i : f.family.children) {
                if (i != this && !result.contains(i)) {
                    result.addAll(i.getDescendants());
                }
            }
        }
        return result;
    }

    /**
     * Get a list of events of the supplied type for this individual
     * 
     * @param type
     *            the type of event to get
     * @return a list of events of the specified type
     */
    public List<IndividualEvent> getEventsOfType(IndividualEventType type) {
        List<IndividualEvent> result = new ArrayList<IndividualEvent>();
        for (IndividualEvent ie : events) {
            if (ie.type == type) {
                result.add(ie);
            }
        }
        return result;
    }

    /**
     * Get a set of spouses for the current individual. Always returns a set,
     * although it may be empty. The returned set is in no particular order.
     * 
     * @return a set of spouses for the current individual.
     */
    public Set<Individual> getSpouses() {
        Set<Individual> result = new HashSet<Individual>();
        for (FamilySpouse f : this.familiesWhereSpouse) {
            if (this != f.family.husband && f.family.husband != null) {
                result.add(f.family.husband);
            }
            if (this != f.family.wife && f.family.wife != null) {
                result.add(f.family.wife);
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((aliases == null) ? 0 : aliases.hashCode());
        result = prime
                * result
                + ((ancestorInterest == null) ? 0 : ancestorInterest.hashCode());
        result = prime
                * result
                + ((ancestralFileNumber == null) ? 0 : ancestralFileNumber
                        .hashCode());
        result = prime * result
                + ((associations == null) ? 0 : associations.hashCode());
        result = prime * result
                + ((attributes == null) ? 0 : attributes.hashCode());
        result = prime * result
                + ((changeDate == null) ? 0 : changeDate.hashCode());
        result = prime * result
                + ((citations == null) ? 0 : citations.hashCode());
        result = prime
                * result
                + ((descendantInterest == null) ? 0 : descendantInterest
                        .hashCode());
        result = prime * result + ((events == null) ? 0 : events.hashCode());
        result = prime
                * result
                + ((familiesWhereChild == null) ? 0 : familiesWhereChild
                        .hashCode());
        result = prime
                * result
                + ((familiesWhereSpouse == null) ? 0 : familiesWhereSpouse
                        .hashCode());
        result = prime
                * result
                + ((ldsIndividualOrdinances == null) ? 0
                        : ldsIndividualOrdinances.hashCode());
        result = prime * result
                + ((multimedia == null) ? 0 : multimedia.hashCode());
        result = prime * result + ((names == null) ? 0 : names.hashCode());
        result = prime * result + ((notes == null) ? 0 : notes.hashCode());
        result = prime
                * result
                + ((permanentRecFileNumber == null) ? 0
                        : permanentRecFileNumber.hashCode());
        result = prime * result
                + ((phoneNumbers == null) ? 0 : phoneNumbers.hashCode());
        result = prime * result
                + ((recIdNumber == null) ? 0 : recIdNumber.hashCode());
        result = prime
                * result
                + ((restrictionNotice == null) ? 0 : restrictionNotice
                        .hashCode());
        result = prime * result + ((sex == null) ? 0 : sex.hashCode());
        result = prime * result
                + ((submitters == null) ? 0 : submitters.hashCode());
        result = prime * result
                + ((userReferences == null) ? 0 : userReferences.hashCode());
        result = prime * result + ((xref == null) ? 0 : xref.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(formattedName());
        for (String n : aliases) {
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
            if (f.family.husband == this) {
                if (f.family.wife == null) {
                    sb.append("unknown");
                } else {
                    sb.append(f.family.wife.formattedName());
                }
            } else {
                if (f.family.husband == null) {
                    sb.append("unknown");
                } else {
                    sb.append(f.family.husband.formattedName());
                }
            }
        }
        for (FamilyChild f : familiesWhereChild) {
            sb.append(", child of ");
            if (f.family.wife != null) {
                sb.append(f.family.wife.formattedName());
                sb.append(" and ");
            }
            if (f.family.husband == null) {
                sb.append("unknown");
            } else {
                sb.append(f.family.husband.formattedName());
            }
        }
        boolean found = false;
        for (IndividualEvent b : getEventsOfType(IndividualEventType.BIRTH)) {
            if (!found) {
                sb.append(", b.");
            }
            sb.append(b.date);
            found = true;
        }
        for (IndividualEvent b : getEventsOfType(IndividualEventType.DEATH)) {
            sb.append(", d." + b.date);
            break;
        }
        return sb.toString();
    }
}
