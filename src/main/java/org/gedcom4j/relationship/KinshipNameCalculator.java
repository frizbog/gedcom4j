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
package org.gedcom4j.relationship;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import org.gedcom4j.model.Family;
import org.gedcom4j.model.FamilySpouse;
import org.gedcom4j.model.Individual;

/**
 * A class for calculating the name of the kinship relationship between two individuals.
 * 
 * @author frizbog
 */
public class KinshipNameCalculator {

    /**
     * The base name of the default properties file with names
     */
    private static final String DEFAULT_BUNDLE_PROPERTIES_NAME = "gedcom4j_relationship_names";

    /**
     * The bundle that gets loaded
     */
    private final ResourceBundle bundle;

    /**
     * Default constructor.
     */
    public KinshipNameCalculator() {
        bundle = ResourceBundle.getBundle(DEFAULT_BUNDLE_PROPERTIES_NAME);
    }

    /**
     * Constructor that takes a specific locale but uses the default properties base name.
     * 
     * @param locale
     *            the locale to use
     */
    public KinshipNameCalculator(Locale locale) {
        bundle = ResourceBundle.getBundle(DEFAULT_BUNDLE_PROPERTIES_NAME, locale);
    }

    /**
     * Constructor that takes a specific properties file base name but uses the default locale
     * 
     * @param bundlePropertiesName
     *            the base name (without .properties extension or any locale suffixes) of the properties file to use
     */
    public KinshipNameCalculator(String bundlePropertiesName) {
        bundle = ResourceBundle.getBundle(bundlePropertiesName);
    }

    /**
     * Constructor that takes a specific properties file base name and a specific locale
     * 
     * @param bundlePropertiesName
     *            the base name (without .properties extension or any locale suffixes) of the properties file to use
     * @param locale
     *            the locale to use
     */
    public KinshipNameCalculator(String bundlePropertiesName, Locale locale) {
        bundle = ResourceBundle.getBundle(bundlePropertiesName, locale);
    }

    /**
     * Get the name of the relationship between two individuals.
     * 
     * @param individual1
     *            the first individual
     * @param individual2
     *            the second individual
     * @return the name of the relationship between the two individuals, or null if the individuals do not share a common ancestor
     *         and/or no suitable name could be determined.
     */
    @SuppressWarnings("PMD.UseStringBufferForStringAppends")
    public String getRelationshipName(Individual individual1, Individual individual2) {
        // First try straight blood relationship
        String result = lookupRelationshipName(individual1, individual2, false);
        if (result != null) {
            return reword(result);
        }
        // Now try if individual2 is a spouse of a blood relative
        for (Individual spouse : getSpousesOf(individual2)) {
            result = lookupRelationshipName(individual1, spouse, true);
            if (result != null) {
                return reword(result);
            }
        }
        // Now try if any of individual1's spouses are related to individual2
        for (Individual spouse : getSpousesOf(individual1)) {
            result = lookupRelationshipName(spouse, individual2, false);
            if (result != null) {
                result = (bundle.getString("spouse.prefix." + getSexCode(individual1)) + " " + result + " " + bundle.getString(
                        "spouse.suffix." + getSexCode(individual1))).trim();
                return reword(result);
            }
        }
        // Now try if any of individual1's spouses are related to individual2's spouses - bottom of the barrel!!
        for (Individual spouse1 : getSpousesOf(individual1)) {
            for (Individual spouse2 : getSpousesOf(individual2)) {
                result = lookupRelationshipName(spouse1, spouse2, true);
                if (result != null) {
                    result = (bundle.getString("spouse.prefix." + getSexCode(individual1)) + " " + result + " " + bundle.getString(
                            "spouse.suffix." + getSexCode(individual1))).trim();
                    return reword(result);
                }
            }
        }
        // We found nothing.
        return null;
    }

    /**
     * Get a single character code for the individual's sex. Defaults to 'u', but could be 'm' or 'f' (case-sensitive).
     * 
     * @param individual
     *            the individual
     * @return a single character code for the individual's sex
     */
    public String getSexCode(Individual individual) {
        String individual2Sex = "u"; // default to unknown, then check what we actually have
        if (individual.getSex() != null && individual.getSex().getValue() != null && !individual.getSex().getValue().trim()
                .isEmpty()) {
            individual2Sex = individual.getSex().getValue().trim().substring(0, 1).toLowerCase(Locale.US);
        }
        return individual2Sex;
    }

    /**
     * Get the spouses of the supplied individual
     * 
     * @param individual
     *            the individual
     * @return a set of all the individual's spouses
     */
    private Set<Individual> getSpousesOf(Individual individual) {
        Set<Individual> result = new HashSet<>();
        if (individual.getFamiliesWhereSpouse() != null) {
            for (FamilySpouse fams : individual.getFamiliesWhereSpouse()) {
                Family f = fams.getFamily();
                if (f == null) {
                    continue;
                }
                if (f.getHusband() != null && individual.equals(f.getHusband().getIndividual()) && f.getWife() != null && f
                        .getWife().getIndividual() != null) {
                    result.add(f.getWife().getIndividual());
                } else if (f.getWife() != null && individual.equals(f.getWife().getIndividual()) && f.getHusband() != null && f
                        .getHusband().getIndividual() != null) {
                    result.add(f.getHusband().getIndividual());
                }
            }
        }
        return result;
    }

    /**
     * Build a resource bundle lookup key and fetch the name of the relationship between two individuals.
     * 
     * @param individual1
     *            the first individual
     * @param individual2
     *            the second individual
     * @param individual2IsSpouse
     *            individual 2 is a spouse of a blood relative
     * @return the name of the relationship between the two individuals, or null if the individuals do not share a common ancestor
     *         and/or no suitable name could be determined.
     */
    private String lookupRelationshipName(Individual individual1, Individual individual2, boolean individual2IsSpouse) {
        if (individual1.equals(individual2)) {
            return bundle.getString("relationship.0.0." + getSexCode(individual2));
        }

        // Need to find the nearest common ancestor of the two people.
        Individual nca;
        Set<Individual> spousesOfIndividual2 = getSpousesOf(individual2);

        // See if either individual is an ancestor or spouse of each other before looking for a third person
        if (individual1.getAncestors().contains(individual2)) {
            nca = individual2;
        } else if (individual2.getAncestors().contains(individual1)) {
            nca = individual1;
        } else if (spousesOfIndividual2.contains(individual1)) {
            return bundle.getString("relationship.0.0." + getSexCode(individual2) + ".spouse");
        } else {
            // Find a nearest common ancestor.
            Set<Individual> lcas = new AncestryCalculator().getLowestCommonAncestors(individual1, individual2);
            if (lcas.isEmpty()) {
                // If we could not find a common ancestor between ind1 and ind2 and ind2's spouses, there's nothing more we can do.
                return null;
            }
            // Get a nearest common ancestor from the set. The first one will do as well as any other in the set.
            nca = lcas.iterator().next();
        }

        // Last check - if no nearest common ancestor, can't do anything
        if (nca == null) {
            return null;
        }

        // Build up the property key
        int gensFrom1toNca = new AncestryCalculator().getGenerationCount(individual1, nca);
        int gensFrom2toNca = new AncestryCalculator().getGenerationCount(individual2, nca);
        StringBuilder propertyName = new StringBuilder("relationship.");
        propertyName.append(gensFrom1toNca);
        propertyName.append(".");
        propertyName.append(gensFrom2toNca);
        propertyName.append(".");
        propertyName.append(getSexCode(individual2));
        if (individual2IsSpouse) {
            // individual 2 is a spouse of a blood relative, not a blood relative themselves
            propertyName.append(".spouse");
        }
        return bundle.getString(propertyName.toString());
    }

    /**
     * Reword the string according to rules found in the resource bundle
     * 
     * @param s
     *            the string
     * @return the reworded string
     */
    private String reword(String s) {
        String result = s;
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String k = keys.nextElement();
            if (!k.startsWith("rewording.")) {
                continue;
            }
            String[] terms = bundle.getString(k).split("\\|");
            result = result.replace(terms[0], terms[1]);
        }
        return result;
    }

}
