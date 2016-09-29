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
import org.gedcom4j.model.Family;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.GedcomVersion;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.model.Repository;
import org.gedcom4j.model.Source;
import org.gedcom4j.model.StringWithCustomFacts;
import org.gedcom4j.model.Submitter;
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
@SuppressWarnings({ "PMD.GodClass", "PMD.TooManyMethods", "PMD.ExcessivePublicCount", "PMD.ExcessiveClassLength" })
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
        if (!isNonNullAndHasRequiredTag(dnaMarker, "_ATTR")) {
            throw new IllegalArgumentException("The dnaMarker argument had the wrong tag value; expected _DNA, found " + dnaMarker
                    .getTag());
        }
        StringWithCustomFacts type = dnaMarker.getType();
        if (type != null && !"DNA Markers".equals(type.getValue())) {
            throw new IllegalArgumentException("The dnaMarker argument had the wrong type value; expected 'DNA Markers', found '"
                    + type + "'");
        }
        individual.getCustomFacts(true).add(dnaMarker);
    }

    /**
     * Add a Elected custom fact to the individual
     * 
     * @param individual
     *            the individual
     * @param elected
     *            the Elected custom fact. Required.
     * @throws IllegalArgumentException
     *             if the elected argument is null, does not have the right tag type, and does not have the right tag subtype
     */
    public void addElected(Individual individual, CustomFact elected) {
        if (elected == null) {
            throw new IllegalArgumentException("The elected argument is required.");
        }
        if (!isNonNullAndHasRequiredTag(elected, "_ATTR")) {
            throw new IllegalArgumentException("The elected argument had the wrong tag value; expected _ATTR, found " + elected
                    .getTag());
        }
        StringWithCustomFacts type = elected.getType();
        if (type != null && !"Elected".equals(type.getValue())) {
            throw new IllegalArgumentException("The elected argument had the wrong type value; expected 'Elected', found '" + type
                    + "'");
        }
        individual.getCustomFacts(true).add(elected);
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
     * Add an email custom fact to a {@link Repository}
     * 
     * @param repo
     *            the repository
     * @param emailString
     *            the email string
     * @return the newly created email custom fact
     */
    public CustomFact addEmail(Repository repo, String emailString) {
        CustomFact cf = newEmail(emailString);
        repo.getCustomFacts(true).add(cf);
        return cf;
    }

    /**
     * Add an email custom fact to a {@link Submitter}
     * 
     * @param submitter
     *            the submitter
     * @param emailString
     *            the email string
     * @return the newly created email custom fact
     */
    public CustomFact addEmail(Submitter submitter, String emailString) {
        CustomFact cf = newEmail(emailString);
        submitter.getCustomFacts(true).add(cf);
        return cf;
    }

    /**
     * Add a Employment custom fact to the individual
     * 
     * @param individual
     *            the individual
     * @param employment
     *            the Employment custom fact. Required.
     * @throws IllegalArgumentException
     *             if the employment argument is null, does not have the right tag type, and does not have the right tag subtype
     */
    public void addEmployment(Individual individual, CustomFact employment) {
        if (employment == null) {
            throw new IllegalArgumentException("The employment argument is required.");
        }
        if (!isNonNullAndHasRequiredTag(employment, "_ATTR")) {
            throw new IllegalArgumentException("The employment argument had the wrong tag value; expected _ATTR, found "
                    + employment.getTag());
        }
        StringWithCustomFacts type = employment.getType();
        if (type != null && !"Employment".equals(type.getValue())) {
            throw new IllegalArgumentException("The employment argument had the wrong type value; expected 'Employment', found '"
                    + type + "'");
        }
        individual.getCustomFacts(true).add(employment);
    }

    /**
     * Add a Height custom fact to the individual
     * 
     * @param individual
     *            the individual
     * @param height
     *            the Height custom fact. Required.
     * @throws IllegalArgumentException
     *             if the height argument is null, does not have the right tag type, and does not have the right tag subtype
     */
    public void addHeight(Individual individual, CustomFact height) {
        if (height == null) {
            throw new IllegalArgumentException("The height argument is required.");
        }
        if (!isNonNullAndHasRequiredTag(height, "_ATTR")) {
            throw new IllegalArgumentException("The height argument had the wrong tag value; expected _ATTR, found " + height
                    .getTag());
        }
        StringWithCustomFacts type = height.getType();
        if (type != null && !"Height".equals(type.getValue())) {
            throw new IllegalArgumentException("The height argument had the wrong type value; expected 'Height', found '" + type
                    + "'");
        }
        individual.getCustomFacts(true).add(height);
    }

    /**
     * Add a Medical Condition custom fact to the individual
     * 
     * @param individual
     *            the individual
     * @param medicalCondition
     *            the Medical Condition custom fact. Required.
     * @throws IllegalArgumentException
     *             if the medicalCondition argument is null, does not have the right tag type, and does not have the right tag
     *             subtype
     */
    public void addMedicalCondition(Individual individual, CustomFact medicalCondition) {
        if (medicalCondition == null) {
            throw new IllegalArgumentException("The medicalCondition argument is required.");
        }
        if (!isNonNullAndHasRequiredTag(medicalCondition, "_ATTR")) {
            throw new IllegalArgumentException("The medicalCondition argument had the wrong tag value; expected _ATTR, found "
                    + medicalCondition.getTag());
        }
        StringWithCustomFacts type = medicalCondition.getType();
        if (type != null && !"Medical Condition".equals(type.getValue())) {
            throw new IllegalArgumentException(
                    "The medicalCondition argument had the wrong type value; expected 'Medical Condition', found '" + type + "'");
        }
        individual.getCustomFacts(true).add(medicalCondition);
    }

    /**
     * Add a Military ID custom fact to the individual
     * 
     * @param individual
     *            the individual
     * @param militaryId
     *            the Military ID custom fact. Required.
     * @throws IllegalArgumentException
     *             if the militaryId argument is null, does not have the right tag type, and does not have the right tag subtype
     */
    public void addMilitaryId(Individual individual, CustomFact militaryId) {
        if (militaryId == null) {
            throw new IllegalArgumentException("The militaryId argument is required.");
        }
        if (!isNonNullAndHasRequiredTag(militaryId, "_ATTR")) {
            throw new IllegalArgumentException("The militaryId argument had the wrong tag value; expected _ATTR, found "
                    + militaryId.getTag());
        }
        StringWithCustomFacts type = militaryId.getType();
        if (type != null && !"Military ID".equals(type.getValue())) {
            throw new IllegalArgumentException("The militaryId argument had the wrong type value; expected 'Military ID', found '"
                    + type + "'");
        }
        individual.getCustomFacts(true).add(militaryId);
    }

    /**
     * Add a Military Service custom fact to the individual
     * 
     * @param individual
     *            the individual
     * @param militaryService
     *            the Military Service custom fact. Required.
     * @throws IllegalArgumentException
     *             if the militaryService argument is null, does not have the right tag type, and does not have the right tag
     *             subtype
     */
    public void addMilitaryService(Individual individual, CustomFact militaryService) {
        if (militaryService == null) {
            throw new IllegalArgumentException("The militaryService argument is required.");
        }
        if (!isNonNullAndHasRequiredTag(militaryService, "_ATTR")) {
            throw new IllegalArgumentException("The militaryService argument had the wrong tag value; expected _ATTR, found "
                    + militaryService.getTag());
        }
        StringWithCustomFacts type = militaryService.getType();
        if (type != null && !"Military Service".equals(type.getValue())) {
            throw new IllegalArgumentException(
                    "The militaryService argument had the wrong type value; expected 'Military Service', found '" + type + "'");
        }
        individual.getCustomFacts(true).add(militaryService);
    }

    /**
     * Add a Mission custom fact to the individual
     * 
     * @param individual
     *            the individual
     * @param mission
     *            the Mission custom fact. Required.
     * @throws IllegalArgumentException
     *             if the mission argument is null, does not have the right tag type, and does not have the right tag subtype
     */
    public void addMission(Individual individual, CustomFact mission) {
        if (mission == null) {
            throw new IllegalArgumentException("The mission argument is required.");
        }
        if (!isNonNullAndHasRequiredTag(mission, "_ATTR")) {
            throw new IllegalArgumentException("The mission argument had the wrong tag value; expected _ATTR, found " + mission
                    .getTag());
        }
        StringWithCustomFacts type = mission.getType();
        if (type != null && !"Mission (LDS)".equals(type.getValue())) {
            throw new IllegalArgumentException("The mission argument had the wrong type value; expected 'Mission (LDS)', found '"
                    + type + "'");
        }
        individual.getCustomFacts(true).add(mission);
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
     * Add a Namesake custom fact to the individual
     * 
     * @param individual
     *            the individual
     * @param namesake
     *            the Namesake custom fact. Required.
     * @throws IllegalArgumentException
     *             if the namesake argument is null, does not have the right tag type, and does not have the right tag subtype
     */
    public void addNamesake(Individual individual, CustomFact namesake) {
        if (namesake == null) {
            throw new IllegalArgumentException("The namesake argument is required.");
        }
        if (!isNonNullAndHasRequiredTag(namesake, "_ATTR")) {
            throw new IllegalArgumentException("The namesake argument had the wrong tag value; expected _ATTR, found " + namesake
                    .getTag());
        }
        StringWithCustomFacts type = namesake.getType();
        if (type != null && !"Namesake".equals(type.getValue())) {
            throw new IllegalArgumentException("The namesake argument had the wrong type value; expected 'Namesake', found '" + type
                    + "'");
        }
        individual.getCustomFacts(true).add(namesake);
    }

    /**
     * Add a Ordinance custom fact to the individual
     * 
     * @param individual
     *            the individual
     * @param ordinance
     *            the Ordinance custom fact. Required.
     * @throws IllegalArgumentException
     *             if the ordinance argument is null, does not have the right tag type, and does not have the right tag subtype
     */
    public void addOrdinance(Individual individual, CustomFact ordinance) {
        if (ordinance == null) {
            throw new IllegalArgumentException("The ordinance argument is required.");
        }
        if (!isNonNullAndHasRequiredTag(ordinance, "_ATTR")) {
            throw new IllegalArgumentException("The ordinance argument had the wrong tag value; expected _ATTR, found " + ordinance
                    .getTag());
        }
        StringWithCustomFacts type = ordinance.getType();
        if (type != null && !"Ordinance".equals(type.getValue())) {
            throw new IllegalArgumentException("The ordinance argument had the wrong type value; expected 'Ordinance', found '"
                    + type + "'");
        }
        individual.getCustomFacts(true).add(ordinance);
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
     * Add an webUrl custom fact to an event or attribute
     * 
     * @param event
     *            the event or attribute
     * @param webUrlString
     *            the webUrl string
     * @return the newly created webUrl custom fact
     */
    public CustomFact addWebUrl(AbstractEvent event, String webUrlString) {
        CustomFact cf = newWebUrl(webUrlString);
        event.getCustomFacts(true).add(cf);
        return cf;
    }

    /**
     * Add an webUrl custom fact to another custom fact
     * 
     * @param customFact
     *            the custom fact
     * @param webUrlString
     *            the webUrl string
     * @return the newly created webUrl custom fact
     */
    public CustomFact addWebUrl(CustomFact customFact, String webUrlString) {
        CustomFact cf = newWebUrl(webUrlString);
        customFact.getCustomFacts(true).add(cf);
        return cf;
    }

    /**
     * Add an webUrl custom fact to a {@link Repository}
     * 
     * @param repo
     *            the repository
     * @param webUrlString
     *            the webUrl string
     * @return the newly created webUrl custom fact
     */
    public CustomFact addWebUrl(Repository repo, String webUrlString) {
        CustomFact cf = newWebUrl(webUrlString);
        repo.getCustomFacts(true).add(cf);
        return cf;
    }

    /**
     * Add an webUrl custom fact to a {@link Submitter}
     * 
     * @param submitter
     *            the submitter
     * @param webUrlString
     *            the webUrl string
     * @return the newly created webUrl custom fact
     */
    public CustomFact addWebUrl(Submitter submitter, String webUrlString) {
        CustomFact cf = newWebUrl(webUrlString);
        submitter.getCustomFacts(true).add(cf);
        return cf;
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
     * Get the Elected for an individual
     * 
     * @param individual
     *            the individual
     * @return the Elected custom fact(s)
     */
    public List<CustomFact> getElected(Individual individual) {
        return getCustomTagsWithTagAndType(individual, "_ATTR", "Elected");
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
     * Get the emails from a {@link Repository}
     * 
     * @param repository
     *            the repository
     * @return the emails from the repository
     */
    public List<String> getEmails(Repository repository) {
        List<String> result = new ArrayList<>();
        for (CustomFact cf : repository.getCustomFactsWithTag("_EMAIL")) {
            if (cf != null && cf.getDescription() != null) {
                result.add(cf.getDescription().getValue());
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Get the emails from a {@link Submitter}
     * 
     * @param submitter
     *            the submitter
     * @return the emails from the submitter
     */
    public List<String> getEmails(Submitter submitter) {
        List<String> result = new ArrayList<>();
        for (CustomFact cf : submitter.getCustomFactsWithTag("_EMAIL")) {
            if (cf != null && cf.getDescription() != null) {
                result.add(cf.getDescription().getValue());
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Get the Employment for an individual
     * 
     * @param individual
     *            the individual
     * @return the Employment custom fact(s)
     */
    public List<CustomFact> getEmployment(Individual individual) {
        return getCustomTagsWithTagAndType(individual, "_ATTR", "Employment");
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
     * Get the family status
     * 
     * @param family
     *            the family
     * @return the family status
     */
    public String getFamilyStatus(Family family) {
        List<CustomFact> stats = family.getCustomFactsWithTag("_STAT");
        if (!stats.isEmpty() && stats.get(0).getDescription() != null) {
            return stats.get(0).getDescription().getValue();
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
     * Get the Height for an individual
     * 
     * @param individual
     *            the individual
     * @return the Height custom fact(s)
     */
    public List<CustomFact> getHeight(Individual individual) {
        return getCustomTagsWithTagAndType(individual, "_ATTR", "Height");
    }

    /**
     * Get the Medical Condition for an individual
     * 
     * @param individual
     *            the individual
     * @return the Medical Condition custom fact(s)
     */
    public List<CustomFact> getMedicalCondition(Individual individual) {
        return getCustomTagsWithTagAndType(individual, "_ATTR", "Medical Condition");
    }

    /**
     * Get the Military ID for an individual
     * 
     * @param individual
     *            the individual
     * @return the Military ID custom fact(s)
     */
    public List<CustomFact> getMilitaryId(Individual individual) {
        return getCustomTagsWithTagAndType(individual, "_ATTR", "Military ID");
    }

    /**
     * Get the Military Service for an individual
     * 
     * @param individual
     *            the individual
     * @return the Military Service custom fact(s)
     */
    public List<CustomFact> getMilitaryService(Individual individual) {
        return getCustomTagsWithTagAndType(individual, "_ATTR", "Military Service");
    }

    /**
     * Get the Mission for an individual
     * 
     * @param individual
     *            the individual
     * @return the Mission custom fact(s)
     */
    public List<CustomFact> getMission(Individual individual) {
        return getCustomTagsWithTagAndType(individual, "_ATTR", "Mission (LDS)");
    }

    /**
     * Get the multimedia date
     * 
     * @param multimedia
     *            the multimedia
     * @return the date of multimedia it is
     */
    public String getMultimediaDate(Multimedia multimedia) {
        for (CustomFact cf : multimedia.getCustomFactsWithTag("_DATE")) {
            if (cf.getDescription() != null) {
                return cf.getDescription().getValue();
            }
        }
        return null;
    }

    /**
     * Get the multimedia file
     * 
     * @param multimedia
     *            the multimedia
     * @return the file of multimedia it is
     */
    public String getMultimediaFile(Multimedia multimedia) {
        for (CustomFact cf : multimedia.getCustomFactsWithTag("_FILE")) {
            if (cf.getDescription() != null) {
                return cf.getDescription().getValue();
            }
        }
        return null;
    }

    /**
     * Get the multimedia keys
     * 
     * @param multimedia
     *            the multimedia
     * @return the keys of multimedia it is
     */
    public String getMultimediaKeys(Multimedia multimedia) {
        for (CustomFact cf : multimedia.getCustomFactsWithTag("_KEYS")) {
            if (cf.getDescription() != null) {
                return cf.getDescription().getValue();
            }
        }
        return null;
    }

    /**
     * Get the multimedia note
     * 
     * @param multimedia
     *            the multimedia
     * @return the note of multimedia it is
     */
    public String getMultimediaNote(Multimedia multimedia) {
        for (CustomFact cf : multimedia.getCustomFactsWithTag("_NOTE")) {
            if (cf.getDescription() != null) {
                return cf.getDescription().getValue();
            }
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
            throw new IllegalArgumentException("listName is a required argument");
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
     * Get the Namesake for an individual
     * 
     * @param individual
     *            the individual
     * @return the Namesake custom fact(s)
     */
    public List<CustomFact> getNamesake(Individual individual) {
        return getCustomTagsWithTagAndType(individual, "_ATTR", "Namesake");
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
     * Get the Ordinances for an individual
     * 
     * @param individual
     *            the individual
     * @return the Ordinance custom fact(s)
     */
    public List<CustomFact> getOrdinances(Individual individual) {
        return getCustomTagsWithTagAndType(individual, "_ATTR", "Ordinance");
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
     * Get the source type
     * 
     * @param src
     *            the source
     * @return the type of source it is
     */
    public String getSourceType(Source src) {
        for (CustomFact cf : src.getCustomFactsWithTag("_TYPE")) {
            if (cf.getDescription() != null) {
                return cf.getDescription().getValue();
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
     * Get the webUrls from an event
     * 
     * @param event
     *            the event or attribute
     * @return the webUrls from the event
     */
    public List<String> getWebUrls(AbstractEvent event) {
        List<String> result = new ArrayList<>();
        for (CustomFact cf : event.getCustomFactsWithTag("_WEB")) {
            if (cf != null && cf.getDescription() != null) {
                result.add(cf.getDescription().getValue());
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Get the webUrls from an custom fact
     * 
     * @param customFact
     *            the custom fact
     * @return the webUrls from the custom fact
     */
    public List<String> getWebUrls(CustomFact customFact) {
        List<String> result = new ArrayList<>();
        for (CustomFact cf : customFact.getCustomFactsWithTag("_WEB")) {
            if (cf != null && cf.getDescription() != null) {
                result.add(cf.getDescription().getValue());
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Get the webUrls from a {@link Repository}
     * 
     * @param repository
     *            the repository
     * @return the webUrls from the repository
     */
    public List<String> getWebUrls(Repository repository) {
        List<String> result = new ArrayList<>();
        for (CustomFact cf : repository.getCustomFactsWithTag("_WEB")) {
            if (cf != null && cf.getDescription() != null) {
                result.add(cf.getDescription().getValue());
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Get the webUrls from a {@link Submitter}
     * 
     * @param submitter
     *            the submitter
     * @return the webUrls from the submitter
     */
    public List<String> getWebUrls(Submitter submitter) {
        List<String> result = new ArrayList<>();
        for (CustomFact cf : submitter.getCustomFactsWithTag("_WEB")) {
            if (cf != null && cf.getDescription() != null) {
                result.add(cf.getDescription().getValue());
            }
        }
        return Collections.unmodifiableList(result);
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
     * Get a new DNA Marker custom fact
     * 
     * @param string
     *            the string for the DNA marker
     * @return the new DNA Marker custom fact
     */
    public CustomFact newDnaMarker(String string) {
        CustomFact result = new CustomFact("_ATTR");
        result.setType("DNA Markers");
        result.setDescription(string);
        return result;
    }

    /**
     * Get a new Elected custom fact
     * 
     * @param string
     *            the string for the Elected
     * @return the new Elected custom fact
     */
    public CustomFact newElected(String string) {
        CustomFact result = new CustomFact("_ATTR");
        result.setType("Elected");
        result.setDescription(string);
        return result;
    }

    /**
     * Get a new Employment custom fact
     * 
     * @param string
     *            the string for the Employment
     * @return the new Employment custom fact
     */
    public CustomFact newEmployment(String string) {
        CustomFact result = new CustomFact("_ATTR");
        result.setType("Employment");
        result.setDescription(string);
        return result;
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
     * Get a new Height custom fact
     * 
     * @param string
     *            the string for the Height
     * @return the new Height custom fact
     */
    public CustomFact newHeight(String string) {
        CustomFact result = new CustomFact("_ATTR");
        result.setType("Height");
        result.setDescription(string);
        return result;
    }

    /**
     * Get a new Medical Condition custom fact
     * 
     * @param string
     *            the string for the Medical Condition
     * @return the new Medical Condition custom fact
     */
    public CustomFact newMedicalCondition(String string) {
        CustomFact result = new CustomFact("_ATTR");
        result.setType("Medical Condition");
        result.setDescription(string);
        return result;
    }

    /**
     * Get a new Military ID custom fact
     * 
     * @param string
     *            the string for the Military ID
     * @return the new Military ID custom fact
     */
    public CustomFact newMilitaryId(String string) {
        CustomFact result = new CustomFact("_ATTR");
        result.setType("Military ID");
        result.setDescription(string);
        return result;
    }

    /**
     * Get a new Military Service custom fact
     * 
     * @param string
     *            the string for the Military Service
     * @return the new Military Service custom fact
     */
    public CustomFact newMilitaryService(String string) {
        CustomFact result = new CustomFact("_ATTR");
        result.setType("Military Service");
        result.setDescription(string);
        return result;
    }

    /**
     * Get a new Mission custom fact
     * 
     * @param string
     *            the string for the Mission
     * @return the new Mission custom fact
     */
    public CustomFact newMission(String string) {
        CustomFact result = new CustomFact("_ATTR");
        result.setType("Mission (LDS)");
        result.setDescription(string);
        return result;
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
     * Get a new Namesake custom fact
     * 
     * @param string
     *            the string for the Namesake
     * @return the new Namesake custom fact
     */
    public CustomFact newNamesake(String string) {
        CustomFact result = new CustomFact("_ATTR");
        result.setType("Namesake");
        result.setDescription(string);
        return result;
    }

    /**
     * Get a new Ordinance custom fact
     * 
     * @param string
     *            the string for the Ordinance
     * @return the new Ordinance custom fact
     */
    public CustomFact newOrdinance(String string) {
        CustomFact result = new CustomFact("_ATTR");
        result.setType("Ordinance");
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
     * Remove the Elected custom facts from the individual supplied
     * 
     * @param individual
     *            the individual
     */
    public void removeElected(Individual individual) {
        clearCustomTagsOfTypeAndSubType(individual, "_ATTR", "Elected");
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
     * Clear the emails from the {@link Repository}
     * 
     * @param repo
     *            the repository
     */
    public void removeEmails(Repository repo) {
        clearCustomTagsOfType(repo, "_EMAIL");
    }

    /**
     * Clear the emails from Submitter
     * 
     * @param submitter
     *            the submitter
     */
    public void removeEmails(Submitter submitter) {
        clearCustomTagsOfType(submitter, "_EMAIL");
    }

    /**
     * Remove the Employment custom facts from the individual supplied
     * 
     * @param individual
     *            the individual
     */
    public void removeEmployment(Individual individual) {
        clearCustomTagsOfTypeAndSubType(individual, "_ATTR", "Employment");
    }

    /**
     * Remove the Height custom facts from the individual supplied
     * 
     * @param individual
     *            the individual
     */
    public void removeHeight(Individual individual) {
        clearCustomTagsOfTypeAndSubType(individual, "_ATTR", "Height");
    }

    /**
     * Remove the Medical Condition custom facts from the individual supplied
     * 
     * @param individual
     *            the individual
     */
    public void removeMedicalCondition(Individual individual) {
        clearCustomTagsOfTypeAndSubType(individual, "_ATTR", "Medical Condition");
    }

    /**
     * Remove the Military ID custom facts from the individual supplied
     * 
     * @param individual
     *            the individual
     */
    public void removeMilitaryId(Individual individual) {
        clearCustomTagsOfTypeAndSubType(individual, "_ATTR", "Military ID");
    }

    /**
     * Remove the Military Service custom facts from the individual supplied
     * 
     * @param individual
     *            the individual
     */
    public void removeMilitaryService(Individual individual) {
        clearCustomTagsOfTypeAndSubType(individual, "_ATTR", "Military Service");
    }

    /**
     * Remove the Mission custom facts from the individual supplied
     * 
     * @param individual
     *            the individual
     */
    public void removeMission(Individual individual) {
        clearCustomTagsOfTypeAndSubType(individual, "_ATTR", "Mission (LDS)");
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
     * Remove the Namesake custom facts from the individual supplied
     * 
     * @param individual
     *            the individual
     */
    public void removeNamesake(Individual individual) {
        clearCustomTagsOfTypeAndSubType(individual, "_ATTR", "Namesake");
    }

    /**
     * Remove the Ordinance custom facts from the individual supplied
     * 
     * @param individual
     *            the individual
     */
    public void removeOrdinances(Individual individual) {
        clearCustomTagsOfTypeAndSubType(individual, "_ATTR", "Ordinance");
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
     * Clear the webUrls from the event or attribute
     * 
     * @param event
     *            the event
     */
    public void removeWebUrls(AbstractEvent event) {
        clearCustomTagsOfType(event, "_WEB");
    }

    /**
     * Clear the webUrls from the custom fact
     * 
     * @param customFact
     *            the customFact
     */
    public void removeWebUrls(CustomFact customFact) {
        clearCustomTagsOfType(customFact, "_WEB");
    }

    /**
     * Clear the webUrls from the {@link Repository}
     * 
     * @param repo
     *            the repository
     */
    public void removeWebUrls(Repository repo) {
        clearCustomTagsOfType(repo, "_WEB");
    }

    /**
     * Clear the webUrls from Submitter
     * 
     * @param submitter
     *            the submitter
     */
    public void removeWebUrls(Submitter submitter) {
        clearCustomTagsOfType(submitter, "_WEB");
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
     * Set the family status
     * 
     * @param family
     *            the family
     * @param familyStatus
     *            the family status. Optional - pass null to remove.
     */
    public void setFamilyStatus(Family family, String familyStatus) {
        clearCustomTagsOfType(family, "_STAT");
        if (familyStatus != null) {
            CustomFact cf = new CustomFact("_STAT");
            cf.setDescription(familyStatus);
            family.getCustomFacts(true).add(cf);
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
     * Set the multimedia date
     * 
     * @param multimedia
     *            the multimedia
     * @param multimediaDate
     *            the multimedia date
     */
    public void setMultimediaDate(Multimedia multimedia, String multimediaDate) {
        clearCustomTagsOfType(multimedia, "_DATE");
        if (multimediaDate != null) {
            CustomFact cf = new CustomFact("_DATE");
            cf.setDescription(multimediaDate);
            multimedia.getCustomFacts(true).add(cf);
        }
    }

    /**
     * Set the multimedia file
     * 
     * @param multimedia
     *            the multimedia
     * @param multimediaFile
     *            the multimedia file
     */
    public void setMultimediaFile(Multimedia multimedia, String multimediaFile) {
        clearCustomTagsOfType(multimedia, "_FILE");
        if (multimediaFile != null) {
            CustomFact cf = new CustomFact("_FILE");
            cf.setDescription(multimediaFile);
            multimedia.getCustomFacts(true).add(cf);
        }
    }

    /**
     * Set the multimedia keys
     * 
     * @param multimedia
     *            the multimedia
     * @param multimediaKeys
     *            the multimedia keys
     */
    public void setMultimediaKeys(Multimedia multimedia, String multimediaKeys) {
        clearCustomTagsOfType(multimedia, "_KEYS");
        if (multimediaKeys != null) {
            CustomFact cf = new CustomFact("_KEYS");
            cf.setDescription(multimediaKeys);
            multimedia.getCustomFacts(true).add(cf);
        }
    }

    /**
     * Set the multimedia note
     * 
     * @param multimedia
     *            the multimedia
     * @param multimediaNote
     *            the multimedia note
     */
    public void setMultimediaNote(Multimedia multimedia, String multimediaNote) {
        clearCustomTagsOfType(multimedia, "_NOTE");
        if (multimediaNote != null) {
            CustomFact cf = new CustomFact("_NOTE");
            cf.setDescription(multimediaNote);
            multimedia.getCustomFacts(true).add(cf);
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
     * Set the source type
     * 
     * @param src
     *            the source
     * @param sourceType
     *            the source type
     */
    public void setSourceType(Source src, String sourceType) {
        clearCustomTagsOfType(src, "_TYPE");
        if (sourceType != null) {
            CustomFact cf = new CustomFact("_TYPE");
            cf.setDescription(sourceType);
            src.getCustomFacts(true).add(cf);
        }
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

    /**
     * Create a new webUrl custom fact
     * 
     * @param webUrlString
     *            the webUrl string
     * @return the new webUrl custom fact
     */
    private CustomFact newWebUrl(String webUrlString) {
        CustomFact result = new CustomFact("_WEB");
        result.setDescription(webUrlString);
        return result;
    }
}
