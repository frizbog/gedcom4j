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
package org.gedcom4j.model.thirdpartyadapters;

import java.util.List;

import org.gedcom4j.model.Address;
import org.gedcom4j.model.CustomFact;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.FamilyEvent;
import org.gedcom4j.model.HasCustomFacts;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.IndividualReference;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.Source;

/**
 * <p>
 * A custom tag adapter for Legacy Family Tree 8.
 * </p>
 * <p>
 * The following custom tags are supported:
 * </p>
 * <ul>
 * <li>_CAT = The Category of a To-Do item (under a _TODO record).</li>
 * <li>_CDATE = Closed Date of a To-Do item (under a _TODO record).</li>
 * <li>_TODO = Indicates the start of a To-Do record that describes the item to be done.</li>
 * <li>_NAME = The name of an individual as part of an address (under an ADDR block).</li>
 * <li>_TYPE = The type of a multimedia object: Photo, Sound, or Video (under an OBJE block).</li>
 * <li>_SCBK = Indicates that a Picture is tagged to be included in a scrapbook report (under an OBJE block).</li>
 * <li>_SOUND = A sound file name that is attached to a picture (under an OBJE block).</li>
 * <li>_PRIM = Means a multimedia object, usually a picture, is the Primary object (the one that is shown on a report) (under an
 * OBJE block).</li>
 * <li>_SORT = The spelling of a name to be used when sorting addresses for a report (under an ADDR block).</li>
 * <li>_UID = A Unique Identification Number given to each individual in a family file.</li>
 * <li>_DATE = A date associated with a multimedia object, usually a picture or video (under an OBJE block).</li>
 * <li>_RDATE = Reminder date on to-do items. (Under a _TODO record.)</li>
 * <li>_LOCL = The Locality of a To-Do item (under a _TODO record).</li>
 * <li>_PREF = Indicates a Preferred spouse, child or parents.</li>
 * <li>_PRIV = Indicates that an address or event is marked as Private.</li>
 * <li>_EMAIL = An email address (under an ADDR block).</li>
 * <li>_NONE = Indicates that a couple had no children (under a FAM record).</li>
 * <li>_PAREN = Indicates that the Publication Facts of a source should be printed within parentheses on a report (under a SOUR
 * record).</li>
 * <li>_QUOTED Y = Indicates that a source title should be printed within quotes on a report (under a SOUR record).</li>
 * <li>_ITALIC Y = Indicates that a source title should be printed on a report in italics (under a SOUR record).</li>
 * <li>_LIST1 YES = Indicates that a person's address is part of the Newsletter grouping (under an ADDR block).</li>
 * <li>_LIST2 YES = Indicates that a person's address is part of the Family Association grouping (under an ADDR block).</li>
 * <li>_LIST3 YES = Indicates that a person's address is part of the Birthday grouping (under an ADDR block).</li>
 * <li>_LIST4 YES = Indicates that a person's address is part of the Research grouping (under an ADDR block).</li>
 * <li>_LIST5 YES = Indicates that a person's address is part of the Christmas grouping (under an ADDR block).</li>
 * <li>_LIST6 YES = Indicates that a person's address is part of the Holiday grouping (under an ADDR block).</li>
 * <li>_TAG = Indicates that an address, or place has been tagged. Also used for Tag 1 selection for an individual.</li>
 * </ul>
 * <p>
 * Several tags are not supported, even though they are documented:
 * </p>
 * <ul>
 * <li>_EVENT_DEFN and _PLACE_DEFN structures and all their subtags</li>
 * <li>_FREL = The Relationship of a child to the Father (under a CHIL block under a FAM record).</li>
 * <li>_MREL = The Relationship of a child to the Mother (under a CHIL block under a FAM record).</li>
 * <li>_OVER = An event sentence override (under an EVEN block).</li>
 * <li>_STAT = The Status of a marriage (Married, Unmarried, etc.). Also the Status of a child (Twin, Triplet, etc.). (The marriage
 * status of Divorced is exported using a DIV tag.)</li>
 * <li>_TAG NO - When used under a SOUR record, indicates to exclude the source citation detail on reports.</li>
 * <li>_TAG2 - TAG9 = When under an INDI record, indicates that an individual has been given certain tag marks.</li>
 * <li>_TAG2 NO - When used under a SOUR record, indicates to exclude the source citation on reports.</li>
 * <li>_TAG3 YES - When used under a SOUR record, indicates to include the source citation detail text on reports.</li>
 * <li>_TAG4 YES - When used under a SOUR record, indicates to include the source citation detail notes on reports.</li>
 * <li>_URL = An Internet address (under an INDI record).</li>
 * <li>_VERI = Indicates that a source citation or place name has a checkmark in the Verified column.</li>
 * </ul>
 * 
 * 
 * 
 * <p>
 * As of 1 Oct 2016, Legacy&copy; is a registered trademark of Millennia Corporation, Surprise, AZ 85374. This software is neither
 * written nor endorsed by the authors and copyright holders of Family Historian.
 * </p>
 * 
 * @author frizbog
 */
@SuppressWarnings({ "PMD.GodClass", "PMD.ExcessivePublicCount" })
public class LegacyFamilyTree8Adapter extends AbstractThirdPartyAdapter {

    /**
     * Mailing lists that can be checked on or off for an an address in Legacy Family Tree 8.
     * 
     * @author frizbog
     */
    public enum AddressMailingList {
        /** Newsletter mailing list */
        NEWSLETTER,
        /** Family association mailing list */
        FAMILY_ASSOCIATION,
        /** Birthday mailing list */
        BIRTHDAY,
        /** Research mailing list */
        RESEARCH,
        /** Christmas mailing list */
        CHRISTMAS,
        /** Holiday mailing list */
        HOLIDAY
    }

    /**
     * Add a Todo custom fact to the supplied object
     * 
     * @param hasCustomFacts
     *            the object to add the to-do to.
     * @param todo
     *            the to-do to add. Must be non-null, and must have the _TODO tag set.
     */
    public void addToDo(HasCustomFacts hasCustomFacts, CustomFact todo) {
        if (todo == null) {
            throw new IllegalArgumentException("The todo argument is required.");
        }
        if (!isNonNullAndHasRequiredTag(todo, "_TODO")) {
            throw new IllegalArgumentException("The Todo did not have the expected custom tag. Expected _TODO, found " + todo
                    .getTag());
        }
        hasCustomFacts.getCustomFacts(true).add(todo);
    }

    /**
     * Get the email on the address
     * 
     * @param address
     *            the address
     * @return the email on the address
     */
    public String getAddressEmail(Address address) {
        return getDescriptionForCustomTag(address, "_EMAIL");
    }

    /**
     * Get the mailing list flag on the address
     * 
     * @param address
     *            the address
     * @param listNum
     *            which flag to get for the address
     * @return the mailing list flag on the address
     */
    public String getAddressListFlag(Address address, AddressMailingList listNum) {
        return getDescriptionForCustomTag(address, "_LIST" + (1 + listNum.ordinal()));
    }

    /**
     * Get the private flag on the address
     * 
     * @param address
     *            the address
     * @return the private flag on the address
     */
    public String getAddressPrivateFlag(Address address) {
        return getDescriptionForCustomTag(address, "_PRIV");
    }

    /**
     * Get the value used for sorting an address
     * 
     * @param addr
     *            the address
     * @return the value used for sorting the address
     */
    public String getAddressSortValue(Address addr) {
        return getDescriptionForCustomTag(addr, "_SORT");
    }

    /**
     * Get the private flag on the family event
     * 
     * @param famEvent
     *            the family event
     * @return the private flag on the family event
     */
    public String getFamilyEventPrivateFlag(FamilyEvent famEvent) {
        return getDescriptionForCustomTag(famEvent, "_PRIV");
    }

    /**
     * Get the preferred flag on a member of a family
     * 
     * @param family
     *            the familyMember item
     * @param individual
     *            the individual in the family
     * @return the primary flag on the familyMember item
     */
    public String getFamilyMemberPreferredFlag(Family family, Individual individual) {
        if (family == null) {
            throw new IllegalArgumentException("family is required");
        }
        if (individual == null) {
            throw new IllegalArgumentException("individual is required");
        }
        if (family.getWife() != null && family.getWife().getIndividual().equals(individual)) {
            return getDescriptionForCustomTag(family.getWife(), "_PREF");
        }
        if (family.getHusband() != null && family.getHusband().getIndividual().equals(individual)) {
            return getDescriptionForCustomTag(family.getHusband(), "_PREF");
        }
        if (family.getChildren() != null) {
            for (IndividualReference iRef : family.getChildren()) {
                if (iRef != null && individual.equals(iRef.getIndividual())) {
                    return getDescriptionForCustomTag(iRef, "_PREF");
                }
            }
        }
        throw new IllegalArgumentException("Individual was not found in the supplied family");
    }

    /**
     * Get the private flag on the individual event
     * 
     * @param indEvent
     *            the individual event
     * @return the private flag on the individual event
     */
    public String getIndividualEventPrivateFlag(IndividualEvent indEvent) {
        return getDescriptionForCustomTag(indEvent, "_PRIV");
    }

    /**
     * Get the UID assigned to the individual
     * 
     * @param ind
     *            the individual
     * @return the UID assigned to the individual
     */
    public String getIndividualUID(Individual ind) {
        return getDescriptionForCustomTag(ind, "_UID");
    }

    /**
     * Get the date of the multimedia item
     * 
     * @param mm
     *            the multimedia item
     * @return the date of the multimedia item
     */
    public String getMultimediaDate(Multimedia mm) {
        return getDescriptionForCustomTag(mm, "_DATE");
    }

    /**
     * Get the primary flag on the multimedia item
     * 
     * @param mm
     *            the multimedia item
     * @return the primary flag on the multimedia item
     */
    public String getMultimediaPrimaryFlag(Multimedia mm) {
        return getDescriptionForCustomTag(mm, "_PRIM");
    }

    /**
     * Get the scrapbook tag on the multimedia item
     * 
     * @param mm
     *            the multimedia item
     * @return the scrapbook tag on the multimedia item
     */
    public String getMultimediaScrapbookTag(Multimedia mm) {
        return getDescriptionForCustomTag(mm, "_SCBK");
    }

    /**
     * Get the sound on the multimedia item
     * 
     * @param mm
     *            the multimedia item
     * @return the sound on the multimedia item
     */
    public String getMultimediaSound(Multimedia mm) {
        return getDescriptionForCustomTag(mm, "_SOUND");
    }

    /**
     * Get the type of the multimedia item
     * 
     * @param mm
     *            the multimedia item
     * @return the type of the multimedia item
     */
    public String getMultimediaType(Multimedia mm) {
        return getDescriptionForCustomTag(mm, "_TYPE");
    }

    /**
     * Get the name used (by the individual) at an address
     * 
     * @param addr
     *            the address
     * @return the name used at the address
     */
    public String getNameAtAddress(Address addr) {
        return getDescriptionForCustomTag(addr, "_NAME");
    }

    /**
     * Get the flag indicating a source is to be put in italics
     * 
     * @param src
     *            the source
     * @return the flag indicating a source is to be put in italics
     */
    public String getSourceInItalicsFlag(Source src) {
        return getDescriptionForCustomTag(src, "_ITALIC");
    }

    /**
     * Get the flag indicating a source is to be put in parentheses
     * 
     * @param src
     *            the source
     * @return the flag indicating a source is to be put in parentheses
     */
    public String getSourceInParensFlag(Source src) {
        return getDescriptionForCustomTag(src, "_PAREN");
    }

    /**
     * Get the flag indicating a source is to be put in quotes
     * 
     * @param src
     *            the source
     * @return the flag indicating a source is to be put in quotes
     */
    public String getSourceInQuotesFlag(Source src) {
        return getDescriptionForCustomTag(src, "_QUOTED");
    }

    /**
     * Get the to-do category
     * 
     * @param toDo
     *            the to-do custom fact
     * @return the category for the to-do
     */
    public String getToDoCategory(CustomFact toDo) {
        if (!isNonNullAndHasRequiredTag(toDo, "_TODO")) {
            throw new IllegalArgumentException("toDo is required and must have correct custom tag");
        }
        return getDescriptionForCustomTag(toDo, "_CAT");
    }

    /**
     * Get the to-do closed date
     * 
     * @param toDo
     *            the to-do custom fact
     * @return the closed date for the to-do
     */
    public String getToDoClosedDate(CustomFact toDo) {
        if (!isNonNullAndHasRequiredTag(toDo, "_TODO")) {
            throw new IllegalArgumentException("toDo is required and must have correct custom tag");
        }
        return getDescriptionForCustomTag(toDo, "_CDATE");
    }

    /**
     * Get the to-do description
     * 
     * @param toDo
     *            the to-do custom fact
     * @return the description for the to-do
     */
    public String getToDoDescription(CustomFact toDo) {
        if (!isNonNullAndHasRequiredTag(toDo, "_TODO")) {
            throw new IllegalArgumentException("toDo is required and must have correct custom tag");
        }
        return getDescriptionForCustomTag(toDo, "DESC");
    }

    /**
     * Get the to-do locality
     * 
     * @param toDo
     *            the to-do custom fact
     * @return the locality for the to-do
     */
    public String getToDoLocality(CustomFact toDo) {
        if (!isNonNullAndHasRequiredTag(toDo, "_TODO")) {
            throw new IllegalArgumentException("toDo is required and must have correct custom tag");
        }
        return getDescriptionForCustomTag(toDo, "_LOCL");
    }

    /**
     * Get the to-do reminder date
     * 
     * @param toDo
     *            the to-do custom fact
     * @return the reminder date for the to-do
     */
    public String getToDoReminderDate(CustomFact toDo) {
        if (!isNonNullAndHasRequiredTag(toDo, "_TODO")) {
            throw new IllegalArgumentException("toDo is required and must have correct custom tag");
        }
        return getDescriptionForCustomTag(toDo, "_RDATE");
    }

    /**
     * Get a List of Todo's from an object with custom facts
     * 
     * @param hasCustomFacts
     *            object with custom facts
     * @return a {@link List} of Todo's from an object with custom facts. List will always be non-null, but may be empty. The List
     *         itself will also always be immutable, although the objects in it may not be.
     */
    public List<CustomFact> getToDos(HasCustomFacts hasCustomFacts) {
        return hasCustomFacts.getCustomFactsWithTag("_TODO");
    }

    /**
     * Get whether the address is tagged
     * 
     * @param address
     *            the address
     * @return true if the address is tagged
     */
    public boolean isAddressTagged(Address address) {
        List<CustomFact> cfs = address.getCustomFactsWithTag("_TAG");
        return (cfs != null && !cfs.isEmpty());
    }

    /**
     * Get the had-no-children flag on the family
     * 
     * @param family
     *            the family
     * @return the had-no-children flag on the family
     */
    public boolean isFamilyHadNoChildren(Family family) {
        List<CustomFact> cfs = family.getCustomFactsWithTag("_NONE");
        return (cfs != null && !cfs.isEmpty());
    }

    /**
     * Get whether the individual is tagged
     * 
     * @param individual
     *            the individual
     * @return true if the individual was tagged
     */
    public boolean isIndividualTagged(Individual individual) {
        List<CustomFact> cfs = individual.getCustomFactsWithTag("_TAG");
        return (cfs != null && !cfs.isEmpty());
    }

    /**
     * Get a new custom fact that represents a to-do for that item
     * 
     * @return the to-do custom fact
     */
    public CustomFact newToDo() {
        return new CustomFact("_TODO");
    }

    /**
     * Remove a specific to-do from the supplied item
     * 
     * @param hasCustomFacts
     *            the object with the to-do that you want to remove
     * @param todo
     *            the to-do that you want to remove. It must be <code>equals()</code> to the to-do on the object to be removed.
     * @return the number of to-do's that matched and were removed
     */
    public int removeTodo(HasCustomFacts hasCustomFacts, CustomFact todo) {
        int result = 0;
        if (hasCustomFacts.getCustomFacts() == null) {
            return result;
        }
        int i = 0;
        while (i < hasCustomFacts.getCustomFacts().size()) {
            CustomFact cf = hasCustomFacts.getCustomFacts().get(i);
            if (cf.equals(todo)) {
                hasCustomFacts.getCustomFacts().remove(i);
                result++;
            } else {
                i++;
            }
        }
        return result;
    }

    /**
     * Remove all the Todo's on the object supplied
     * 
     * @param hasCustomFacts
     *            the object to remove Todo's from
     * @return the number of Todo's removed
     */
    public int removeToDos(HasCustomFacts hasCustomFacts) {
        return clearCustomTagsOfType(hasCustomFacts, "_TODO");
    }

    /**
     * Set the address email
     * 
     * @param address
     *            the address
     * @param email
     *            the email on the address. Optional; pass null to remove the email.
     */
    public void setAddressEmail(Address address, String email) {
        setDescriptionForCustomTag(address, "_EMAIL", email);
    }

    /**
     * Set the address mailing list flag
     * 
     * @param address
     *            the address
     * @param listNum
     *            which flag to set for the address
     * @param listFlag
     *            the flag value to put on the address. Optional; pass null to remove the list flag.
     */
    public void setAddressListFlag(Address address, AddressMailingList listNum, String listFlag) {
        setDescriptionForCustomTag(address, "_LIST" + (1 + listNum.ordinal()), listFlag);
    }

    /**
     * Set the address private flag
     * 
     * @param address
     *            the address
     * @param privateFlag
     *            the private flag on the address. Optional; pass null to remove the private flag.
     */
    public void setAddressPrivateFlag(Address address, String privateFlag) {
        setDescriptionForCustomTag(address, "_PRIV", privateFlag);
    }

    /**
     * Set the value used for sorting an address
     * 
     * @param addr
     *            the address
     * @param nameAtAddress
     *            the value used for sorting the address. Optional - pass in null to remove.
     */
    public void setAddressSortValue(Address addr, String nameAtAddress) {
        setDescriptionForCustomTag(addr, "_SORT", nameAtAddress);
    }

    /**
     * Set the family event had-no-children flag
     * 
     * @param family
     *            the family event
     * @param hadNoChildren
     *            the had-no-children flag on the multimedia. Optional; pass null to remove the had-no-children flag.
     */
    public void setAddressTagged(Address family, boolean hadNoChildren) {
        clearCustomTagsOfType(family, "_TAG");
        if (hadNoChildren) {
            CustomFact cf = new CustomFact("_TAG");
            family.getCustomFacts(true).add(cf);
        }
    }

    /**
     * Set the family event private flag
     * 
     * @param famEvent
     *            the family event
     * @param privateFlag
     *            the private flag on the multimedia. Optional; pass null to remove the private flag.
     */
    public void setFamilyEventPrivateFlag(FamilyEvent famEvent, String privateFlag) {
        setDescriptionForCustomTag(famEvent, "_PRIV", privateFlag);
    }

    /**
     * Set the family event had-no-children flag
     * 
     * @param family
     *            the family event
     * @param hadNoChildren
     *            the had-no-children flag on the multimedia. Optional; pass null to remove the had-no-children flag.
     */
    public void setFamilyHadNoChildren(Family family, boolean hadNoChildren) {
        clearCustomTagsOfType(family, "_NONE");
        if (hadNoChildren) {
            CustomFact cf = new CustomFact("_NONE");
            family.getCustomFacts(true).add(cf);
        }
    }

    /**
     * Set the family member preferred flag
     * 
     * @param family
     *            the reference to the family
     * @param individual
     *            the individual
     * @param familyMemberPreferredFlag
     *            the primary flag on the familyMember. Optional; pass null to remove the primary flag.
     */
    public void setFamilyMemberPreferredFlag(Family family, Individual individual, String familyMemberPreferredFlag) {
        if (family == null) {
            throw new IllegalArgumentException("family is required");
        }
        if (individual == null) {
            throw new IllegalArgumentException("individual is required");
        }
        if (family.getWife() != null && family.getWife().getIndividual().equals(individual)) {
            setDescriptionForCustomTag(family.getWife(), "_PREF", familyMemberPreferredFlag);
            return;
        }
        if (family.getHusband() != null && family.getHusband().getIndividual().equals(individual)) {
            setDescriptionForCustomTag(family.getHusband(), "_PREF", familyMemberPreferredFlag);
            return;
        }
        if (family.getChildren() != null) {
            for (IndividualReference iRef : family.getChildren()) {
                if (iRef != null && individual.equals(iRef.getIndividual())) {
                    setDescriptionForCustomTag(iRef, "_PREF", familyMemberPreferredFlag);
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Individual was not found in the supplied family");
    }

    /**
     * Set the individual event private flag
     * 
     * @param indEvent
     *            the individual event
     * @param privateFlag
     *            the private flag on the individual event. Optional; pass null to remove the private flag.
     */
    public void setIndividualEventPrivateFlag(IndividualEvent indEvent, String privateFlag) {
        setDescriptionForCustomTag(indEvent, "_PRIV", privateFlag);
    }

    /**
     * Set tag on individual
     * 
     * @param individual
     *            the individual
     * @param tagged
     *            the had-no-children flag on the multimedia. Optional; pass null to remove the had-no-children flag.
     */
    public void setIndividualTagged(Individual individual, boolean tagged) {
        clearCustomTagsOfType(individual, "_TAG");
        if (tagged) {
            CustomFact cf = new CustomFact("_TAG");
            individual.getCustomFacts(true).add(cf);
        }
    }

    /**
     * Set the UID assigned to the individual
     * 
     * @param ind
     *            the individual
     * @param individualUid
     *            the the UID assigned to the individual. Optional - pass in null to remove.
     */
    public void setIndividualUID(Individual ind, String individualUid) {
        setDescriptionForCustomTag(ind, "_UID", individualUid);
    }

    /**
     * Set the multimedia date
     * 
     * @param mm
     *            the multimedia
     * @param multimediaDate
     *            the date of the multimedia. Optional; pass null to remove the date.
     */
    public void setMultimediaDate(Multimedia mm, String multimediaDate) {
        setDescriptionForCustomTag(mm, "_DATE", multimediaDate);
    }

    /**
     * Set the multimedia primary flag
     * 
     * @param mm
     *            the multimedia
     * @param multimediaPrimaryFlag
     *            the primary flag on the multimedia. Optional; pass null to remove the primary flag.
     */
    public void setMultimediaPrimaryFlag(Multimedia mm, String multimediaPrimaryFlag) {
        setDescriptionForCustomTag(mm, "_PRIM", multimediaPrimaryFlag);
    }

    /**
     * Set the multimedia scrapbook tag
     * 
     * @param mm
     *            the multimedia
     * @param multimediaScrapbookTag
     *            the scrapbook tag on the multimedia. Optional; pass null to remove the scrapbook tag.
     */
    public void setMultimediaScrapbookTag(Multimedia mm, String multimediaScrapbookTag) {
        setDescriptionForCustomTag(mm, "_SCBK", multimediaScrapbookTag);
    }

    /**
     * Set the multimedia sound
     * 
     * @param mm
     *            the multimedia
     * @param multimediaSound
     *            the sound on the multimedia. Optional; pass null to remove the sound.
     */
    public void setMultimediaSound(Multimedia mm, String multimediaSound) {
        setDescriptionForCustomTag(mm, "_SOUND", multimediaSound);
    }

    /**
     * Set the multimedia sound
     * 
     * @param mm
     *            the multimedia
     * @param multimediaType
     *            the type of the multimedia. Optional; pass null to remove the sound.
     */
    public void setMultimediaType(Multimedia mm, String multimediaType) {
        setDescriptionForCustomTag(mm, "_TYPE", multimediaType);
    }

    /**
     * Set the name used (by the individual) at an address
     * 
     * @param addr
     *            the address
     * @param nameAtAddress
     *            the name used at the address. Optional - pass in null to remove.
     */
    public void setNameAtAddress(Address addr, String nameAtAddress) {
        setDescriptionForCustomTag(addr, "_NAME", nameAtAddress);
    }

    /**
     * Set the flag indicating a source is to be put in italics
     * 
     * @param src
     *            the source
     * @param italicsFlag
     *            the flag indicating a source is to be put in italics. Optional; pass in null to remove the flag.
     */
    public void setSourceInItalicsFlag(Source src, String italicsFlag) {
        setDescriptionForCustomTag(src, "_ITALIC", italicsFlag);
    }

    /**
     * Set the flag indicating a source is to be put in parentheses
     * 
     * @param src
     *            the source
     * @param parensFlag
     *            the flag indicating a source is to be put in parentheses. Optional; pass in null to remove the flag.
     */
    public void setSourceInParensFlag(Source src, String parensFlag) {
        setDescriptionForCustomTag(src, "_PAREN", parensFlag);
    }

    /**
     * Set the flag indicating a source is to be put in quotes
     * 
     * @param src
     *            the source
     * @param quotesFlag
     *            the flag indicating a source is to be put in quotes. Optional; pass in null to remove the flag.
     */
    public void setSourceInQuotesFlag(Source src, String quotesFlag) {
        setDescriptionForCustomTag(src, "_QUOTED", quotesFlag);
    }

    /**
     * Sets the to do category.
     *
     * @param toDo
     *            the to do
     * @param category
     *            the category
     */
    public void setToDoCategory(CustomFact toDo, String category) {
        if (!isNonNullAndHasRequiredTag(toDo, "_TODO")) {
            throw new IllegalArgumentException("toDo is required and must have correct custom tag");
        }
        setDescriptionForCustomTag(toDo, "_CAT", category);
    }

    /**
     * Sets the to do closed date.
     *
     * @param toDo
     *            the to-do custom fact
     * @param closedDate
     *            the closed date
     */
    public void setToDoClosedDate(CustomFact toDo, String closedDate) {
        if (!isNonNullAndHasRequiredTag(toDo, "_TODO")) {
            throw new IllegalArgumentException("toDo is required and must have correct custom tag");
        }
        setDescriptionForCustomTag(toDo, "_CDATE", closedDate);
    }

    /**
     * Sets the to do description.
     *
     * @param toDo
     *            the to do
     * @param description
     *            the description
     */
    public void setToDoDescription(CustomFact toDo, String description) {
        if (!isNonNullAndHasRequiredTag(toDo, "_TODO")) {
            throw new IllegalArgumentException("toDo is required and must have correct custom tag");
        }
        setDescriptionForCustomTag(toDo, "DESC", description);
    }

    /**
     * Sets the to do locality.
     *
     * @param toDo
     *            the to do
     * @param locality
     *            the locality
     */
    public void setToDoLocality(CustomFact toDo, String locality) {
        if (!isNonNullAndHasRequiredTag(toDo, "_TODO")) {
            throw new IllegalArgumentException("toDo is required and must have correct custom tag");
        }
        setDescriptionForCustomTag(toDo, "_LOCL", locality);
    }

    /**
     * Sets the to do reminder date.
     *
     * @param toDo
     *            the to-do custom fact
     * @param reminderDate
     *            the reminder date
     */
    public void setToDoReminderDate(CustomFact toDo, String reminderDate) {
        if (!isNonNullAndHasRequiredTag(toDo, "_TODO")) {
            throw new IllegalArgumentException("toDo is required and must have correct custom tag");
        }
        setDescriptionForCustomTag(toDo, "_RDATE", reminderDate);
    }

}
