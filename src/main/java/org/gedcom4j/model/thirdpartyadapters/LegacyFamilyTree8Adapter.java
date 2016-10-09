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

/**
 * <p>
 * A custom tag adapter for Legacy Family Tree 8.
 * </p>
 * <p>
 * As of 1 Oct 2016, Legacy&copy; is a registered trademark of Millennia Corporation, Surprise, AZ 85374. This software is neither
 * written nor endorsed by the authors and copyright holders of Family Historian.
 * </p>
 * 
 * @author frizbog
 */
@SuppressWarnings("PMD.GodClass")
public class LegacyFamilyTree8Adapter extends AbstractThirdPartyAdapter {

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
