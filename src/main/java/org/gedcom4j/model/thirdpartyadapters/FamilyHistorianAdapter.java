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
import java.util.List;

import org.gedcom4j.model.CustomFact;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.GedcomVersion;
import org.gedcom4j.model.Individual;

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
public class FamilyHistorianAdapter extends AbstractThirdPartyAdapter {

    /**
     * Get a specific named lists for the supplied gedcom
     * 
     * @param gedcom
     *            the gedcom
     * @param listName
     *            the name of the list you're looking for. Required.
     * @return the named lists in the gedcom that have a name that matches the one you supplied. Always returns a list, though it
     *         may be empty.
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
        return result;
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
        clearCustomTagsOfType(gedcom.getHeader(), "_ROOT");
        Individual i = gedcom.getIndividuals().get(newRootIndividual.getXref());
        if (!newRootIndividual.equals(i)) {
            throw new IllegalArgumentException("Individual being set as root individual does not exist in the supplied gedcom");
        }
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
     *            the UID value to set
     */
    public void setUID(Gedcom gedcom, String vef) {
        clearCustomTagsOfType(gedcom.getHeader(), "_UID");
        CustomFact cf = new CustomFact("_UID");
        cf.setDescription(vef);
        gedcom.getHeader().getCustomFacts(true).add(cf);
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
        CustomFact cf = new CustomFact("_VAR");
        cf.setDescription(vef);
        gedcom.getHeader().getGedcomVersion().getCustomFacts(true).add(cf);
    }
}
