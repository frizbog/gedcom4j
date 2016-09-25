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

import org.gedcom4j.model.CitationWithSource;
import org.gedcom4j.model.CustomFact;
import org.gedcom4j.model.Individual;

/**
 * <p>
 * A custom tag adapter for Family Tree Maker.
 * </p>
 * <p>
 * As of Sep 19 2016, Family Tree Maker is a registered trademark of The Software MacKiev Company. This software is neither written
 * nor endorsed by the authors and copyright holders of Family Tree Maker.
 * </p>
 * 
 * @author frizbog
 */
@SuppressWarnings({ "PMD.TooManyMethods", "PMD.ExcessivePublicCount", "PMD.GodClass" })
public class FamilyTreeMaker3Adapter extends AbstractThirdPartyAdapter {

    /**
     * Gets the cause of death.
     *
     * @param i
     *            the individual
     * @return the cause of death
     */
    public List<CustomFact> getCauseOfDeath(Individual i) {
        return i.getCustomFactsWithTag("_DCAUSE");
    }

    /**
     * Get certainty justification information
     * 
     * @param cws
     *            the Citation with source
     * @return the certainty justification information for the individual
     */
    public List<CustomFact> getCertaintyJustification(CitationWithSource cws) {
        return cws.getCustomFactsWithTag("_JUST");
    }

    /**
     * Get circumcision information
     * 
     * @param i
     *            the individual
     * @return the circumcision information for the individual
     */
    public List<CustomFact> getCircumcision(Individual i) {
        return i.getCustomFactsWithTag("_CIRC");
    }

    /**
     * Gets the destinations
     *
     * @param i
     *            the individual
     * @return the destinations
     */
    public List<CustomFact> getDestinations(Individual i) {
        return i.getCustomFactsWithTag("_DEST");
    }

    /**
     * Get DNA Marker information
     * 
     * @param i
     *            the individual
     * @return the DNA Marker information for the individual
     */
    public List<CustomFact> getDnaMarkers(Individual i) {
        return i.getCustomFactsWithTag("_DNA");
    }

    /**
     * Get elected information
     * 
     * @param i
     *            the individual
     * @return the elected information for the individual
     */
    public List<CustomFact> getElected(Individual i) {
        return i.getCustomFactsWithTag("_ELEC");
    }

    /**
     * Get employment information
     * 
     * @param i
     *            the individual
     * @return the employment information for the individual
     */
    public List<CustomFact> getEmployment(Individual i) {
        return i.getCustomFactsWithTag("_EMPLOY");
    }

    /**
     * Get excommunication information
     * 
     * @param i
     *            the individual
     * @return the excommunication information for the individual
     */
    public List<CustomFact> getExcommunication(Individual i) {
        return i.getCustomFactsWithTag("_EXCM");
    }

    /**
     * Get funeral information
     * 
     * @param i
     *            the individual
     * @return the funeral information for the individual
     */
    public List<CustomFact> getFuneral(Individual i) {
        return i.getCustomFactsWithTag("_FUN");
    }

    /**
     * Gets the heights
     *
     * @param i
     *            the individual
     * @return the heights
     */
    public List<CustomFact> getHeights(Individual i) {
        return i.getCustomFactsWithTag("_HEIG");
    }

    /**
     * Get initiatory information
     * 
     * @param i
     *            the individual
     * @return the initiatory information for the individual
     */
    public List<CustomFact> getInitiatory(Individual i) {
        return i.getCustomFactsWithTag("_INIT");
    }

    /**
     * Get medical information
     * 
     * @param i
     *            the individual
     * @return the medical information for the individual
     */
    public List<CustomFact> getMedical(Individual i) {
        return i.getCustomFactsWithTag("_MDCL");
    }

    /**
     * Get military information
     * 
     * @param i
     *            the individual
     * @return the military information for the individual
     */
    public List<CustomFact> getMilitary(Individual i) {
        return i.getCustomFactsWithTag("_MILT");
    }

    /**
     * Get military ID information
     * 
     * @param i
     *            the individual
     * @return the military ID information for the individual
     */
    public List<CustomFact> getMilitaryId(Individual i) {
        return i.getCustomFactsWithTag("_MILTID");
    }

    /**
     * Get mission information
     * 
     * @param i
     *            the individual
     * @return the mission information for the individual
     */
    public List<CustomFact> getMission(Individual i) {
        return i.getCustomFactsWithTag("_MISN");
    }

    /**
     * Get namesake information
     * 
     * @param i
     *            the individual
     * @return the namesake information for the individual
     */
    public List<CustomFact> getNamesake(Individual i) {
        return i.getCustomFactsWithTag("_NAMS");
    }

    /**
     * Get ordinance information
     * 
     * @param i
     *            the individual
     * @return the ordinance information for the individual
     */
    public List<CustomFact> getOrdinance(Individual i) {
        return i.getCustomFactsWithTag("_ORDI");
    }

    /**
     * Get origin information
     * 
     * @param i
     *            the individual
     * @return the origin information for the individual
     */
    public List<CustomFact> getOrigin(Individual i) {
        return i.getCustomFactsWithTag("_ORIG");
    }

    /**
     * Get web link information for a source citation
     * 
     * @param cws
     *            citation with source
     * @return the web link(s) for the citation
     */
    public List<CustomFact> getWebLink(CitationWithSource cws) {
        return cws.getCustomFactsWithTag("_LINK");
    }

    /**
     * Get weight information
     * 
     * @param i
     *            the individual
     * @return the weight information for the individual
     */
    public List<CustomFact> getWeight(Individual i) {
        return i.getCustomFactsWithTag("_WEIG");
    }

    /**
     * Creates a new custom fact about a cause of death
     * 
     * @return the new custom fact about a cause of death
     */
    public CustomFact newCauseOfDeathCustomFact() {
        return new CustomFact("_DCAUSE");
    }

    /**
     * Creates a new custom fact about certainty justification
     * 
     * @return the new custom fact about certainty justification
     */
    public CustomFact newCertaintyJustificationCustomFact() {
        return new CustomFact("_JUST");
    }

    /**
     * Creates a new custom fact about a circumcision
     * 
     * @return the new custom fact about a circumcision
     */
    public CustomFact newCircumcisionCustomFact() {
        return new CustomFact("_CIRC");
    }

    /**
     * Creates a new custom fact about a destination
     * 
     * @return the new custom fact about a destination
     */
    public CustomFact newDestinationCustomFact() {
        return new CustomFact("_DEST");
    }

    /**
     * Creates a new custom fact about dna markers
     * 
     * @return the new custom fact about dna markers
     */
    public CustomFact newDnaMarkerCustomFact() {
        return new CustomFact("_DNA");
    }

    /**
     * Creates a new custom fact about an election
     * 
     * @return the new custom fact about an election
     */
    public CustomFact newElectedCustomFact() {
        return new CustomFact("_ELEC");
    }

    /**
     * Creates a new custom fact about an employment
     * 
     * @return the new custom fact about an employment
     */
    public CustomFact newEmploymentCustomFact() {
        return new CustomFact("_EMPLOY");
    }

    /**
     * Creates a new custom fact about an excommunication
     * 
     * @return the new custom fact about an excommunication
     */
    public CustomFact newExcommunicationCustomFact() {
        return new CustomFact("_EXCM");
    }

    /**
     * Creates a new custom fact about a funeral
     * 
     * @return the new custom fact about a funeral
     */
    public CustomFact newFuneralCustomFact() {
        return new CustomFact("_FUN");
    }

    /**
     * Creates a new custom fact about height
     * 
     * @return the new custom fact about height
     */
    public CustomFact newHeightCustomFact() {
        return new CustomFact("_HEIG");
    }

    /**
     * Creates a new custom fact about an initatory
     * 
     * @return the new custom fact about an initiatory
     */
    public CustomFact newInitiatoryCustomFact() {
        return new CustomFact("_INIT");
    }

    /**
     * Creates a new custom fact about a medical
     * 
     * @return the new custom fact about a medical
     */
    public CustomFact newMedicalCustomFact() {
        return new CustomFact("_MDCL");
    }

    /**
     * Creates a new custom fact about a military
     * 
     * @return the new custom fact about a military
     */
    public CustomFact newMilitaryCustomFact() {
        return new CustomFact("_MILT");
    }

    /**
     * Creates a new custom fact about a military ID
     * 
     * @return the new custom fact about a military ID
     */
    public CustomFact newMilitaryIdCustomFact() {
        return new CustomFact("_MILTID");
    }

    /**
     * Creates a new custom fact about a mission
     * 
     * @return the new custom fact about a mission
     */
    public CustomFact newMissionCustomFact() {
        return new CustomFact("_MISN");
    }

    /**
     * Creates a new custom fact about a namesake
     * 
     * @return the new custom fact about a namesake
     */
    public CustomFact newNamesakeCustomFact() {
        return new CustomFact("_NAMS");
    }

    /**
     * Creates a new custom fact about a ordinance
     * 
     * @return the new custom fact about a ordinance
     */
    public CustomFact newOrdinanceCustomFact() {
        return new CustomFact("_ORDI");
    }

    /**
     * Creates a new custom fact about a origin
     * 
     * @return the new custom fact about a origin
     */
    public CustomFact newOriginCustomFact() {
        return new CustomFact("_ORIG");
    }

    /**
     * Creates a new custom fact about a web link on a citation
     * 
     * @return the new custom fact about a web link for a citation
     */
    public CustomFact newWebLinkCustomFact() {
        return new CustomFact("_LINK");
    }

    /**
     * Creates a new custom fact about a weight
     * 
     * @return the new custom fact about a weight
     */
    public CustomFact newWeightCustomFact() {
        return new CustomFact("_WEIG");
    }

    /**
     * Sets the causes of death.
     *
     * @param i
     *            the individual
     * @param causes
     *            the descriptions of the causes of death
     */
    public void setCausesOfDeath(Individual i, List<CustomFact> causes) {
        clearCustomTagsOfType(i, "_DCAUSE");
        i.getCustomFacts(true).addAll(causes);
    }

    /**
     * Set certainty justification information
     * 
     * @param cws
     *            the citation with source
     * @param justifications
     *            the certainty justification information for the citation
     */
    public void setCertaintyJustification(CitationWithSource cws, List<CustomFact> justifications) {
        replaceAllCustomFactsOfTypeWithNewFacts(cws, "_WEIG", justifications);
    }

    /**
     * Set circumcision information
     * 
     * @param i
     *            the individual
     * @param circumcisions
     *            the circumcision information for the individual
     */
    public void setCircumcision(Individual i, List<CustomFact> circumcisions) {
        clearCustomTagsOfType(i, "_CIRC");
        i.getCustomFacts(true).addAll(circumcisions);
    }

    /**
     * Set the destinations
     * 
     * @param i
     *            the individual
     * @param destinations
     *            the destinations
     */
    public void setDestinations(Individual i, List<CustomFact> destinations) {
        replaceAllCustomFactsOfTypeWithNewFacts(i, "_DEST", destinations);
    }

    /**
     * Set DNA Marker information
     * 
     * @param i
     *            the individual
     * @param dnaMarkers
     *            the DNA Marker information for the individual
     */
    public void setDnaMarkers(Individual i, List<CustomFact> dnaMarkers) {
        replaceAllCustomFactsOfTypeWithNewFacts(i, "_DNA", dnaMarkers);
    }

    /**
     * Set elected information
     * 
     * @param i
     *            the individual
     * @param elected
     *            the elected information for the individual
     */
    public void setElected(Individual i, List<CustomFact> elected) {
        replaceAllCustomFactsOfTypeWithNewFacts(i, "_ELEC", elected);
    }

    /**
     * Set employment information
     * 
     * @param i
     *            the individual
     * @param employment
     *            the employment information for the individual
     */
    public void setEmployment(Individual i, List<CustomFact> employment) {
        replaceAllCustomFactsOfTypeWithNewFacts(i, "_EMPLOY", employment);
    }

    /**
     * Set excommunication information
     * 
     * @param i
     *            the individual
     * @param excommunication
     *            the excommunication information for the individual
     */
    public void setExcommunication(Individual i, List<CustomFact> excommunication) {
        replaceAllCustomFactsOfTypeWithNewFacts(i, "_EXCM", excommunication);
    }

    /**
     * Set funeral information
     * 
     * @param i
     *            the individual
     * @param funeral
     *            the funeral information for the individual
     */
    public void setFuneral(Individual i, List<CustomFact> funeral) {
        replaceAllCustomFactsOfTypeWithNewFacts(i, "_FUN", funeral);
    }

    /**
     * Set the heights
     * 
     * @param i
     *            the individual
     * @param heights
     *            the heights
     */
    public void setHeights(Individual i, List<CustomFact> heights) {
        replaceAllCustomFactsOfTypeWithNewFacts(i, "_HEIG", heights);
    }

    /**
     * Set initiatory information
     * 
     * @param i
     *            the individual
     * @param initiatoryInfo
     *            the initiatory information for the individual
     */
    public void setInitiatory(Individual i, List<CustomFact> initiatoryInfo) {
        replaceAllCustomFactsOfTypeWithNewFacts(i, "_INIT", initiatoryInfo);
    }

    /**
     * Set medical information
     * 
     * @param i
     *            the individual
     * @param medical
     *            the medical information for the individual
     */
    public void setMedical(Individual i, List<CustomFact> medical) {
        replaceAllCustomFactsOfTypeWithNewFacts(i, "_MDCL", medical);
    }

    /**
     * Set military information
     * 
     * @param i
     *            the individual
     * @param military
     *            the military information for the individual
     */
    public void setMilitary(Individual i, List<CustomFact> military) {
        replaceAllCustomFactsOfTypeWithNewFacts(i, "_MILT", military);
    }

    /**
     * Set military ID information
     * 
     * @param i
     *            the individual
     * @param militaryIds
     *            the military ID information for the individual
     */
    public void setMilitaryId(Individual i, List<CustomFact> militaryIds) {
        replaceAllCustomFactsOfTypeWithNewFacts(i, "_MILTID", militaryIds);
    }

    /**
     * Set mission information
     * 
     * @param i
     *            the individual
     * @param missions
     *            the mission information for the individual
     */
    public void setMission(Individual i, List<CustomFact> missions) {
        replaceAllCustomFactsOfTypeWithNewFacts(i, "_MISN", missions);
    }

    /**
     * Set namesake information
     * 
     * @param i
     *            the individual
     * @param namesake
     *            the namesake information for the individual
     */
    public void setNamesake(Individual i, List<CustomFact> namesake) {
        replaceAllCustomFactsOfTypeWithNewFacts(i, "_NAMS", namesake);
    }

    /**
     * Set ordinance information
     * 
     * @param i
     *            the individual
     * @param ordinance
     *            the ordinance information for the individual
     */
    public void setOrdinance(Individual i, List<CustomFact> ordinance) {
        replaceAllCustomFactsOfTypeWithNewFacts(i, "_ORDI", ordinance);
    }

    /**
     * Set origin information
     * 
     * @param i
     *            the individual
     * @param origin
     *            the origin information for the individual
     */
    public void setOrigin(Individual i, List<CustomFact> origin) {
        replaceAllCustomFactsOfTypeWithNewFacts(i, "_ORIG", origin);
    }

    /**
     * Set weight information
     * 
     * @param i
     *            the individual
     * @param weight
     *            the weight information for the individual
     */
    public void setWeight(Individual i, List<CustomFact> weight) {
        replaceAllCustomFactsOfTypeWithNewFacts(i, "_WEIG", weight);
    }

}