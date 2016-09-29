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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gedcom4j.model.AbstractEvent;
import org.gedcom4j.model.CustomFact;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.GedcomVersion;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.model.StringWithCustomFacts;
import org.gedcom4j.model.enumerations.IndividualEventType;

/**
 * <p>
 * A custom tag adapter for Family Historian.
 * </p>
 * <p>
 * As of 27 Sep 2016, Family Historian is designed and written by Calico Pie Limited, based in London, England. This software is
 * neither written nor endorsed by the authors and copyright holders of Family Historian.
 * </p>
 * 
 * @author frizbog
 */
@SuppressWarnings({ "PMD.GodClass", "PMD.TooManyMethods" })
public class FamilyHistorianAdapter extends AbstractThirdPartyAdapter {

    /**
     * Add a DNA Marker custom fact to the individual
     * 
     * @param individual
     *            the individual
     * @param dnaMarker
     *            the DNA Marker custom fact. Required.
     * @throws IllegalArgumentException
     *             if the dnaMarker argument is null, does not have the right tag type, and does not have the right tag subtype
     */
    public void addDnaMarker(Individual individual, CustomFact dnaMarker) {
        if (dnaMarker == null) {
            throw new IllegalArgumentException("The dnaMarker argument is required.");
        }
        if (!isNonNullAndHasRequiredTag(dnaMarker, "_DNA")) {
            throw new IllegalArgumentException("The dnaMarker argument had the wrong tag value; expected _DNA, found " + dnaMarker
                    .getTag());
        }
        StringWithCustomFacts type = dnaMarker.getType();
        if (type != null && "DNA Markers".equals(type.getValue())) {
            throw new IllegalArgumentException("The dnaMarker argument had the wrong type value; expected 'DNA Markers', found "
                    + type);
        }
        individual.getCustomFacts(true).add(dnaMarker);
    }

    /**
     * Add an email custom fact to an event or attribute
     * 
     * @param event
     *            the event or attribute
     * @param emailString
     *            the email string
     * @return the newly created email custom fact
     */
    public CustomFact addEmail(AbstractEvent event, String emailString) {
        CustomFact cf = newEmail(emailString);
        event.getCustomFacts(true).add(cf);
        return cf;
    }

    /**
     * Add an email custom fact to another custom fact
     * 
     * @param customFact
     *            the custom fact
     * @param emailString
     *            the email string
     * @return the newly created email custom fact
     */
    public CustomFact addEmail(CustomFact customFact, String emailString) {
        CustomFact cf = newEmail(emailString);
        customFact.getCustomFacts(true).add(cf);
        return cf;
    }

    /**
     * Add a named list to the gedcom
     * 
     * @param gedcom
     *            the gedcom
     * @param namedList
     *            the named list
     */
    public void addNamedList(Gedcom gedcom, CustomFact namedList) {
        if (namedList == null) {
            throw new IllegalArgumentException("namedList cannot be null");
        }
        if (!isNonNullAndHasRequiredTag(namedList, "_LIST")) {
            throw new IllegalArgumentException(
                    "Custom fact supplied in namedList does not have the correct tag. Expected _LIST, found " + namedList.getTag());
        }
        gedcom.getHeader().getCustomFacts(true).add(namedList);
    }

    /**
     * Add an unrelated witness to an event. Unrelated witnesses do not exist in the Individuals map in the {@link Gedcom}.
     * 
     * @param event
     *            the event we are adding to
     * @param unrelatedWitness
     *            the unrelated witness to add
     */
    public void addUnrelatedWitness(IndividualEvent event, CustomFact unrelatedWitness) {
        if (unrelatedWitness == null) {
            throw new IllegalArgumentException("unrelatedWitness cannot be null");
        }
        if (!isNonNullAndHasRequiredTag(unrelatedWitness, "_SHAN")) {
            throw new IllegalArgumentException(
                    "Custom fact supplied in unrelatedWitness does not have the correct tag. Expected _SHAN, found "
                            + unrelatedWitness.getTag());
        }
        event.getCustomFacts(true).add(unrelatedWitness);
    }

    /**
     * Add an witness reference to an event. Witness references are for people who exist in the Individuals map in the
     * {@link Gedcom}.
     * 
     * @param gedcom
     *            the gedcom we're working with
     * 
     * @param event
     *            the event we are adding to
     * @param witnessReference
     *            the witness reference to add
     * @throws IllegalArgumentException
     *             if the witnessReference has the wrong tag, is null, or doesn't contain a valid xref to an individual in the
     *             gedcom
     */
    public void addWitnessReference(Gedcom gedcom, IndividualEvent event, CustomFact witnessReference) {
        if (witnessReference == null) {
            throw new IllegalArgumentException("unrelatedWitness cannot be null");
        }
        if (!isNonNullAndHasRequiredTag(witnessReference, "_SHAR")) {
            throw new IllegalArgumentException(
                    "Custom fact supplied in witnessReference does not have the correct tag. Expected _SHAR, found "
                            + witnessReference.getTag());
        }
        if (witnessReference.getDescription() == null || witnessReference.getDescription().getValue().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "The witnessReference value supplied does not contain a cross-reference to an individual");
        }
        String xref = witnessReference.getDescription().getValue();
        if (gedcom.getIndividuals().get(xref) == null) {
            throw new IllegalArgumentException("Individual referenced in the witnessReference value (xref=" + xref
                    + ") could not be found in the gedcom supplied");
        }
        event.getCustomFacts(true).add(witnessReference);
    }

    /**
     * Get the DNA markers for an individual
     * 
     * @param individual
     *            the individual
     * @return the DNA marker custom fact(s)
     */
    public List<CustomFact> getDnaMarkers(Individual individual) {
        return getCustomTagsWithTagAndType(individual, "_ATTR", "DNA Markers");
    }

    /**
     * Get the emails from an event
     * 
     * @param event
     *            the event or attribute
     * @return the emails from the event
     */
    public List<String> getEmails(AbstractEvent event) {
        List<String> result = new ArrayList<>();
        for (CustomFact cf : event.getCustomFactsWithTag("_EMAIL")) {
            if (cf != null && cf.getDescription() != null) {
                result.add(cf.getDescription().getValue());
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Get the emails from an custom fact
     * 
     * @param customFact
     *            the custom fact
     * @return the emails from the custom fact
     */
    public List<String> getEmails(CustomFact customFact) {
        List<String> result = new ArrayList<>();
        for (CustomFact cf : customFact.getCustomFactsWithTag("_EMAIL")) {
            if (cf != null && cf.getDescription() != null) {
                result.add(cf.getDescription().getValue());
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Get the custom Fact Set Sentence Template for representing the supplied event, fact, or attribute
     * 
     * @param eventFactAttribute
     *            the event, fact, or attribute
     * @return the custom Fact Set Sentence Template for representing the supplied event, fact, or attribute
     */
    public String getFactSetSentenceTemplate(AbstractEvent eventFactAttribute) {
        if (eventFactAttribute.getCustomFacts() != null) {
            for (CustomFact cf : eventFactAttribute.getCustomFacts()) {
                if ("_SENT".equals(cf.getTag())) {
                    return cf.getDescription() == null ? null : cf.getDescription().getValue();
                }
            }
        }
        return null;
    }

    /**
     * Get the custom Fact Set Sentence Template for representing the supplied custom fact
     * 
     * @param customFact
     *            the custom fact
     * @return the custom Fact Set Sentence Template for representing the supplied custom fact
     */
    public String getFactSetSentenceTemplate(CustomFact customFact) {
        if (customFact.getCustomFacts() != null) {
            for (CustomFact cf : customFact.getCustomFacts()) {
                if ("_SENT".equals(cf.getTag())) {
                    return cf.getDescription() == null ? null : cf.getDescription().getValue();
                }
            }
        }
        return null;
    }

    /**
     * Get the flags on an individual
     * 
     * @param individual
     *            the individual
     * @return the flags
     */
    public CustomFact getFlags(Individual individual) {
        List<CustomFact> facts = individual.getCustomFactsWithTag("_FLGS");
        if (!facts.isEmpty()) {
            return facts.get(0);
        }
        return null;
    }

    /**
     * Get a named lists for the supplied gedcom that match as specific name
     * 
     * @param gedcom
     *            the gedcom
     * @param listName
     *            the name of the list you're looking for. Required.
     * @return the named lists in the gedcom that have a name that matches the one you supplied. Always returns a list, though it
     *         may be empty. Usually will have zero or only one item in it.
     */
    public List<CustomFact> getNamedList(Gedcom gedcom, String listName) {
        if (listName == null) {
            throw new IllegalArgumentException("listName is q required argument");
        }
        List<CustomFact> result = new ArrayList<>();
        for (CustomFact cf : gedcom.getHeader().getCustomFactsWithTag("_LIST")) {
            if (cf.getDescription() != null && listName.equals(cf.getDescription().getValue())) {
                result.add(cf);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Get all the named lists for the supplied gedcom
     * 
     * @param gedcom
     *            the gedcom
     * @return all the named lists in the gedcom
     */
    public List<CustomFact> getNamedLists(Gedcom gedcom) {
        return gedcom.getHeader().getCustomFactsWithTag("_LIST");
    }

    /**
     * Get which given name was used by an individual (such as someone going by their middle name instead of their first name)
     * 
     * @param pn
     *            the personal name
     * @return the given name used
     */
    public List<CustomFact> getNameUsed(PersonalName pn) {
        return pn.getCustomFactsWithTag("_USED");
    }

    /**
     * Get the other location for an immigration (from where?) or emigration (to where?) event.
     * 
     * @param immigrationOrEmigrationEvent
     *            the immigration or emigration event. Required, and must be an IMMIgration or EMIGration event type.
     * @return the other location
     * @throws IllegalArgumentException
     *             if immigrationOrEmigrationEvent is null or not an an IMMIgration or EMIGration event
     */
    public String getOtherPlaceName(IndividualEvent immigrationOrEmigrationEvent) {
        if (immigrationOrEmigrationEvent == null) {
            throw new IllegalArgumentException("immigrationOrEmigrationEvent is a required argument");
        }
        if (immigrationOrEmigrationEvent.getType() != IndividualEventType.IMMIGRATION && immigrationOrEmigrationEvent
                .getType() != IndividualEventType.EMIGRATION) {
            throw new IllegalArgumentException("Other place names are only supported on " + IndividualEventType.IMMIGRATION
                    + " and " + IndividualEventType.EMIGRATION + " event types; " + immigrationOrEmigrationEvent.getType()
                    + " was supplied");
        }
        for (CustomFact cf : immigrationOrEmigrationEvent.getCustomFactsWithTag("_PLAC")) {
            if (cf != null && cf.getDescription() != null) {
                return cf.getDescription().getValue();
            }
        }
        return null;
    }

    /**
     * Get the root individual
     * 
     * @param gedcom
     *            the gedcom file
     * 
     * @return the root individual
     */
    public Individual getRootIndividual(Gedcom gedcom) {
        List<CustomFact> cfs = gedcom.getHeader().getCustomFactsWithTag("_ROOT");
        if (cfs != null && !cfs.isEmpty()) {
            CustomFact root = cfs.get(0);
            if (root != null && root.getDescription() != null) {
                return gedcom.getIndividuals().get(root.getDescription().getValue());
            }
        }
        return null;
    }

    /**
     * Get the UID value for the supplied gedcom
     * 
     * @param gedcom
     *            the gedcom
     * @return the UID value for the supplied gedcom
     */
    public String getUID(Gedcom gedcom) {
        List<CustomFact> customFactsWithTag = gedcom.getHeader().getCustomFactsWithTag("_UID");
        if (customFactsWithTag != null && customFactsWithTag.size() == 1 && customFactsWithTag.get(0).getDescription() != null) {
            return customFactsWithTag.get(0).getDescription().getValue();
        }
        return null;
    }

    /**
     * Get a list of witnesses to an event. These witnesses are people who do not exist in the {@link Gedcom} structure as
     * {@link Individual}s.
     * 
     * @param event
     *            the event to find witnesses for
     * @return the list of witnesses
     */
    public List<CustomFact> getUnrelatedWitnesses(IndividualEvent event) {
        return event.getCustomFactsWithTag("_SHAN");
    }

    /**
     * Get the variant export format value for the supplied gedcom
     * 
     * @param gedcom
     *            the gedcom
     * @return the variant export format value for the supplied gedcom
     */
    public String getVariantExportFormat(Gedcom gedcom) {
        GedcomVersion gv = gedcom.getHeader().getGedcomVersion();
        if (gv == null) {
            return null;
        }
        List<CustomFact> customFactsWithTag = gv.getCustomFactsWithTag("_VAR");
        if (customFactsWithTag != null && customFactsWithTag.size() == 1 && customFactsWithTag.get(0).getDescription() != null) {
            return customFactsWithTag.get(0).getDescription().getValue();
        }
        return null;
    }

    /**
     * Get a list of witness references to an event. These witnesses are people who exist in the {@link Gedcom} structure as
     * {@link Individual}s.
     * 
     * @param event
     *            the event to find witnesses for
     * @return the list of witnesses
     */
    public List<CustomFact> getWitnessReferences(IndividualEvent event) {
        return event.getCustomFactsWithTag("_SHAR");
    }

    /**
     * Determine if the custom tag supplied is enabled for editing
     * 
     * @param namedList
     *            the named list
     * @return true if the list is flagged as enabled for editing
     */
    public boolean isEditingEnabled(CustomFact namedList) {
        if (!isNonNullAndHasRequiredTag(namedList, "_LIST")) {
            throw new IllegalArgumentException("namedList supplied is not a named list");
        }
        List<CustomFact> flags = namedList.getCustomFactsWithTag("_FLAG");
        for (CustomFact flag : flags) {
            if (flag != null && flag.getDescription() != null && "E".equals(flag.getDescription().getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Create new flags custom fact
     * 
     * @return the new flags custom fact
     */
    public CustomFact newFlags() {
        return new CustomFact("_FLGS");
    }

    /**
     * Create a new named list custom fact
     * 
     * @param string
     *            the name of the new named list
     * @return the newly created named list
     */
    public CustomFact newNamedList(String string) {
        CustomFact result = new CustomFact("_LIST");
        result.setDescription(string);
        return result;
    }

    /**
     * Create a new unrelated witness custom fact
     * 
     * @param witnessName
     *            the name of the unrelated witness
     * @return the newly created witness
     */
    public CustomFact newUnrelatedWitness(String witnessName) {
        CustomFact result = new CustomFact("_SHAN");
        result.setDescription(witnessName);
        return result;
    }

    /**
     * Create a new witness reference custom fact
     *
     * @param individual
     *            the individual being referenced
     * @return the custom fact created
     */
    public CustomFact newWitnessReference(Individual individual) {
        CustomFact result = new CustomFact("_SHAR");
        result.setDescription(individual.getXref());
        return result;
    }

    /**
     * Remove the DNA Marker custom facts from the individual supplied
     * 
     * @param individual
     *            the individual
     */
    public void removeDnaMarkers(Individual individual) {
        clearCustomTagsOfTypeAndSubType(individual, "_ATTR", "DNA Markers");
    }

    /**
     * Clear the emails from the event or attribute
     * 
     * @param event
     *            the event
     */
    public void removeEmails(AbstractEvent event) {
        clearCustomTagsOfType(event, "_EMAIL");
    }

    /**
     * Clear the emails from the custom fact
     * 
     * @param customFact
     *            the customFact
     */
    public void removeEmails(CustomFact customFact) {
        clearCustomTagsOfType(customFact, "_EMAIL");
    }

    /**
     * Remove the named list from the gedcom. More precisely, removes all named lists from the gedcom that match the supplied name -
     * but since they *should* be unique, it's effectively the same.
     * 
     * @param gedcom
     *            the gedcom
     * @param listName
     *            the named list
     * @return the number of lists actually removed
     */
    public int removeNamedList(Gedcom gedcom, String listName) {
        if (listName == null) {
            throw new IllegalArgumentException("listName is q required argument");
        }
        int result = 0;
        List<CustomFact> customFacts = gedcom.getHeader().getCustomFacts();
        if (customFacts != null) {
            int i = 0;
            while (i < customFacts.size()) {
                CustomFact cf = customFacts.get(i);
                if (cf.getDescription() != null && listName.equals(cf.getDescription().getValue())) {
                    customFacts.remove(i);
                    result++;
                } else {
                    i++;
                }
            }
        }
        return result;
    }

    /**
     * Remove all the named lists from the gedcom
     * 
     * @param gedcom
     *            the gedcom
     */
    public void removeNamedLists(Gedcom gedcom) {
        clearCustomTagsOfType(gedcom.getHeader(), "_LIST");
    }

    /**
     * Remove the unrelated witnesses from an event. Unrelated witness means one who is not in the Individuals map in the
     * {@link Gedcom}.
     * 
     * @param event
     *            the event
     */
    public void removeUnrelatedWitnesses(IndividualEvent event) {
        clearCustomTagsOfType(event, "_SHAN");
    }

    /**
     * Remove the witness references from an event. Witness reference means the witness is someone who is in the Individuals map in
     * the {@link Gedcom}.
     * 
     * @param event
     *            the event
     */
    public void removeWitnessReferences(IndividualEvent event) {
        clearCustomTagsOfType(event, "_SHAR");
    }

    /**
     * Set the editing-enabled flag on the named list
     * 
     * @param namedList
     *            the named list
     * @param enabled
     *            true if the flag should be set to enabled
     */
    public void setEditingEnabled(CustomFact namedList, boolean enabled) {
        if (!isNonNullAndHasRequiredTag(namedList, "_LIST")) {
            throw new IllegalArgumentException("namedList supplied is not a named list");
        }
        clearCustomTagsOfType(namedList, "_FLAG");
        CustomFact flag = new CustomFact("_FLAG");
        flag.setDescription(enabled ? "E" : null);
        namedList.getCustomFacts(true).add(flag);
    }

    /**
     * Set the custom Fact Set Sentence Template for representing the supplied event, fact, or attribute
     * 
     * @param eventFactAttribute
     *            the event, fact, or attribute
     * @param string
     *            the custom Fact Set Sentence Template for representing the supplied event, fact, or attribute. Optional.
     */
    public void setFactSetSentenceTemplate(AbstractEvent eventFactAttribute, String string) {
        clearCustomTagsOfType(eventFactAttribute, "_SENT");
        if (string != null) {
            CustomFact fsst = new CustomFact("_SENT");
            fsst.setDescription(string);
            eventFactAttribute.getCustomFacts(true).add(fsst);
        }
    }

    /**
     * Set the custom Fact Set Sentence Template for representing the supplied custom fact
     * 
     * @param customFact
     *            the event, fact, or attribute
     * @param string
     *            the custom Fact Set Sentence Template for representing the supplied event, fact, or attribute. Optional.
     */
    public void setFactSetSentenceTemplate(CustomFact customFact, String string) {
        clearCustomTagsOfType(customFact, "_SENT");
        if (string != null) {
            CustomFact fsst = new CustomFact("_SENT");
            fsst.setDescription(string);
            customFact.getCustomFacts(true).add(fsst);
        }
    }

    /**
     * Set the flags on an individual
     * 
     * @param individual
     *            the individual
     * @param flags
     *            the flags custom fact
     * @throws IllegalArgumentException
     *             if the flags custom fact is non-null but does not have the correct tag on it
     */
    public void setFlags(Individual individual, CustomFact flags) {
        if (flags != null && !isNonNullAndHasRequiredTag(flags, "_FLGS")) {
            throw new IllegalArgumentException("flags custom fact did not have the expected tag type of _FLGS - found " + flags
                    .getTag());
        }
        clearCustomTagsOfType(individual, "_FLGS");
        if (flags != null) {
            individual.getCustomFacts(true).add(flags);
        }
    }

    /**
     * Get which given name was used by an individual (such as someone going by their middle name instead of their first name)
     * 
     * @param pn
     *            the personal name
     * @param nameUsed
     *            the name used. Optional.
     */
    public void setNameUsed(PersonalName pn, String nameUsed) {
        clearCustomTagsOfType(pn, "_USED");
        if (nameUsed != null) {
            CustomFact customFact = new CustomFact("_USED");
            customFact.setDescription(nameUsed);
            pn.getCustomFacts(true).add(customFact);
        }
    }

    /**
     * Sets the other location on an IMMIgration (from where?) or EMIGration (to where?) event
     * 
     * @param immigrationOrEmigrationEvent
     *            the event. Required, and must be an IMMIgration or EMIGration event.
     * @param otherPlaceName
     *            the name of the other location. Optional.
     */
    public void setOtherPlaceName(IndividualEvent immigrationOrEmigrationEvent, String otherPlaceName) {
        if (immigrationOrEmigrationEvent == null) {
            throw new IllegalArgumentException("immigrationOrEmigrationEvent is a required argument");
        }
        if (immigrationOrEmigrationEvent.getType() != IndividualEventType.IMMIGRATION && immigrationOrEmigrationEvent
                .getType() != IndividualEventType.EMIGRATION) {
            throw new IllegalArgumentException("Other place names are only supported on " + IndividualEventType.IMMIGRATION
                    + " and " + IndividualEventType.EMIGRATION + " event types; " + immigrationOrEmigrationEvent.getType()
                    + " was supplied");
        }
        clearCustomTagsOfType(immigrationOrEmigrationEvent, "_PLAC");
        if (otherPlaceName != null) {
            CustomFact cf = new CustomFact("_PLAC");
            cf.setDescription(otherPlaceName);
            immigrationOrEmigrationEvent.getCustomFacts(true).add(cf);
        }
    }

    /**
     * Set the root individual. The individual must exist in the gedcom already.
     * 
     * @param gedcom
     *            the gedcom file we're working with
     * @param newRootIndividual
     *            the new root individual. Required, and must already exist in the gedcom supplied.
     */
    public void setRootIndividual(Gedcom gedcom, Individual newRootIndividual) {
        if (newRootIndividual == null) {
            throw new IllegalArgumentException("Individual being set as root individual is a required argument");
        }
        Individual i = gedcom.getIndividuals().get(newRootIndividual.getXref());
        if (!newRootIndividual.equals(i)) {
            throw new IllegalArgumentException("Individual being set as root individual does not exist in the supplied gedcom");
        }
        clearCustomTagsOfType(gedcom.getHeader(), "_ROOT");
        CustomFact cf = new CustomFact("_ROOT");
        cf.setDescription(newRootIndividual.getXref());
        gedcom.getHeader().getCustomFacts(true).add(cf);
    }

    /**
     * Set the UID value for the supplied gedcom
     * 
     * @param gedcom
     *            the gedcom
     * @param vef
     *            the UID value to set. Optional.
     */
    public void setUID(Gedcom gedcom, String vef) {
        clearCustomTagsOfType(gedcom.getHeader(), "_UID");
        if (vef != null) {
            CustomFact cf = new CustomFact("_UID");
            cf.setDescription(vef);
            gedcom.getHeader().getCustomFacts(true).add(cf);
        }
    }

    /**
     * Set the variant export format value for the supplied gedcom
     * 
     * @param gedcom
     *            the gedcom
     * @param vef
     *            the variant export format value to set
     */
    public void setVariantExportFormat(Gedcom gedcom, String vef) {
        if (gedcom.getHeader().getGedcomVersion() == null) {
            gedcom.getHeader().setGedcomVersion(new GedcomVersion());
        }
        clearCustomTagsOfType(gedcom.getHeader().getGedcomVersion(), "_VAR");
        if (vef != null) {
            CustomFact cf = new CustomFact("_VAR");
            cf.setDescription(vef);
            gedcom.getHeader().getGedcomVersion().getCustomFacts(true).add(cf);
        }
    }

    /**
     * Create a new email custom fact
     * 
     * @param emailString
     *            the email string
     * @return the new email custom fact
     */
    private CustomFact newEmail(String emailString) {
        CustomFact result = new CustomFact("_EMAIL");
        result.setDescription(emailString);
        return result;
    }
}
