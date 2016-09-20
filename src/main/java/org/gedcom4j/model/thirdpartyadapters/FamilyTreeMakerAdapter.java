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
public class FamilyTreeMakerAdapter extends AbstractCustomTagsAdapter {

    /**
     * Gets the cause of death.
     *
     * @param i
     *            the individual
     * @return the cause of death
     */
    public List<String> getCauseOfDeath(Individual i) {
        return getDescriptions(i, "_DCAUSE");
    }

    /**
     * Get circumcision information
     * 
     * @param i
     *            the individual
     * @return the circumcision information for the individual
     */
    public List<DatePlaceDescription> getCircumcision(Individual i) {
        return getDatePlaceDescriptions(i, "_CIRC");
    }

    /**
     * Gets the destinations
     *
     * @param i
     *            the individual
     * @return the destinations
     */
    public List<String> getDestinations(Individual i) {
        return getDescriptions(i, "_DEST");
    }

    /**
     * Get DNA Marker information
     * 
     * @param i
     *            the individual
     * @return the DNA Marker information for the individual
     */
    public List<DatePlaceDescription> getDnaMarkers(Individual i) {
        return getDatePlaceDescriptions(i, "_DNA");
    }

    /**
     * Get elected information
     * 
     * @param i
     *            the individual
     * @return the elected information for the individual
     */
    public List<DatePlaceDescription> getElected(Individual i) {
        return getDatePlaceDescriptions(i, "_ELEC");
    }

    /**
     * Get employment information
     * 
     * @param i
     *            the individual
     * @return the employment information for the individual
     */
    public List<DatePlaceDescription> getEmployment(Individual i) {
        return getDatePlaceDescriptions(i, "_EMPLOY");
    }

    /**
     * Get excommunication information
     * 
     * @param i
     *            the individual
     * @return the excommunication information for the individual
     */
    public List<DatePlaceDescription> getExcommunication(Individual i) {
        return getDatePlaceDescriptions(i, "_EXCM");
    }

    /**
     * Get funeral information
     * 
     * @param i
     *            the individual
     * @return the funeral information for the individual
     */
    public List<DatePlaceDescription> getFuneral(Individual i) {
        return getDatePlaceDescriptions(i, "_FUN");
    }

    /**
     * Gets the heights
     *
     * @param i
     *            the individual
     * @return the heights
     */
    public List<String> getHeights(Individual i) {
        return getDescriptions(i, "_HEIG");
    }

    /**
     * Get initiatory information
     * 
     * @param i
     *            the individual
     * @return the initiatory information for the individual
     */
    public List<DatePlaceDescription> getInitiatory(Individual i) {
        return getDatePlaceDescriptions(i, "_INIT");
    }

    /**
     * Get mission information
     * 
     * @param i
     *            the individual
     * @return the mission information for the individual
     */
    public List<DatePlaceDescription> getMission(Individual i) {
        return getDatePlaceDescriptions(i, "_MISN");
    }

    /**
     * Sets the causes of death.
     *
     * @param i
     *            the individual
     * @param descriptions
     *            the descriptions of the causes of death
     */
    public void setCausesOfDeath(Individual i, List<String> descriptions) {
        setAllDescriptions(i, "_DCAUSE", descriptions);
    }

    /**
     * Set circumcision information
     * 
     * @param i
     *            the individual
     * @param circumcisions
     *            the circumcision information for the individual
     */
    public void setCircumcision(Individual i, List<DatePlaceDescription> circumcisions) {
        setAllDatePlaceDescription(i, "_CIRC", circumcisions);
    }

    /**
     * Set the destinations
     * 
     * @param i
     *            the individual
     * @param destinations
     *            the destinations
     */
    public void setDestinations(Individual i, List<String> destinations) {
        setAllDescriptions(i, "_DEST", destinations);
    }

    /**
     * Set DNA Marker information
     * 
     * @param i
     *            the individual
     * @param dnaMarkers
     *            the DNA Marker information for the individual
     */
    public void setDnaMarkers(Individual i, List<DatePlaceDescription> dnaMarkers) {
        setAllDatePlaceDescription(i, "_DNA", dnaMarkers);
    }

    /**
     * Set elected information
     * 
     * @param i
     *            the individual
     * @param elected
     *            the elected information for the individual
     */
    public void setElected(Individual i, List<DatePlaceDescription> elected) {
        setAllDatePlaceDescription(i, "_ELEC", elected);
    }

    /**
     * Set employment information
     * 
     * @param i
     *            the individual
     * @param employment
     *            the employment information for the individual
     */
    public void setEmployment(Individual i, List<DatePlaceDescription> employment) {
        setAllDatePlaceDescription(i, "_EMPLOY", employment);
    }

    /**
     * Set excommunication information
     * 
     * @param i
     *            the individual
     * @param excommunication
     *            the excommunication information for the individual
     */
    public void setExcommunication(Individual i, List<DatePlaceDescription> excommunication) {
        setAllDatePlaceDescription(i, "_EXCM", excommunication);
    }

    /**
     * Set funeral information
     * 
     * @param i
     *            the individual
     * @param funeral
     *            the funeral information for the individual
     */
    public void setFuneral(Individual i, List<DatePlaceDescription> funeral) {
        setAllDatePlaceDescription(i, "_FUN", funeral);
    }

    /**
     * Set the heights
     * 
     * @param i
     *            the individual
     * @param heights
     *            the heights
     */
    public void setHeights(Individual i, List<String> heights) {
        setAllDescriptions(i, "_HEIG", heights);
    }

    /**
     * Set initiatory information
     * 
     * @param i
     *            the individual
     * @param initiatorys
     *            the initiatory information for the individual
     */
    public void setInitiatory(Individual i, List<DatePlaceDescription> initiatorys) {
        setAllDatePlaceDescription(i, "_INIT", initiatorys);
    }

    /**
     * Set mission information
     * 
     * @param i
     *            the individual
     * @param missions
     *            the mission information for the individual
     */
    public void setMission(Individual i, List<DatePlaceDescription> missions) {
        setAllDatePlaceDescription(i, "_MISN", missions);
    }

}
