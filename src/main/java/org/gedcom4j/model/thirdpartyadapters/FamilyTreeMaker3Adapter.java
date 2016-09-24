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
@SuppressWarnings("PMD.TooManyMethods")
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
     * Creates a new custom fact about a cause of death
     * 
     * @return the new custom fact about a cause of death
     */
    public CustomFact newCauseOfDeathCustomFact() {
        return new CustomFact("_DCAUSE");
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
     * Creates a new custom fact about a mission
     * 
     * @return the new custom fact about a mission
     */
    public CustomFact newMissionCustomFact() {
        return new CustomFact("_MISN");
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

}