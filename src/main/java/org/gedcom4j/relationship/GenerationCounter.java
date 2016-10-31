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

import org.gedcom4j.model.Family;
import org.gedcom4j.model.FamilyChild;
import org.gedcom4j.model.Individual;

/**
 * A class to count generations between an descendant and his/her ancestor.
 * 
 * @author frizbog
 */
public class GenerationCounter {

    /**
     * <p>
     * Get the number of generations you need to go through to find the provided ancestor. This is defined (for this method's
     * purposes) as the individual's parents (and step-parents), recursively.
     * </p>
     * <p>
     * This is the public, root level method. It recursively calls {@link #getGenCount(Individual, Individual, int)} which is for
     * internal use only.
     * </p>
     * 
     * @param descendant
     *            the individual whose extended ancestry is desired
     * @param lookingFor
     *            the ancestor we are looking for
     * @return the number of generations separating the individual from the person we are looking for. Returns -1 if the person we
     *         are looking for is not an ancestor (or spouse of an ancestor) of the individual.
     */
    public int getGenerationCount(Individual descendant, Individual lookingFor) {
        int result = getGenCount(descendant, lookingFor, 0);
        if (result < 0) {
            throw new IllegalArgumentException("Ancestor/descendant relationship not found for " + lookingFor.getXref() + " and "
                    + descendant.getXref());
        }
        return result;
    }

    /**
     * <p>
     * Get the number of generations you need to go through to find the provided ancestor. This is defined (for this method's
     * purposes) as the individual's parents (and step-parents), recursively.
     * </p>
     * <p>
     * This is the private recursive method for internal use only.
     * </p>
     * 
     * @param descendant
     *            the individual whose ancestors we are checking
     * @param lookingFor
     *            the ancestor we are looking for
     * @param gens
     *            the number of generations we've gone up so far
     * @return the number of generations we've gone up so far
     */
    @SuppressWarnings("PMD.AvoidDeeplyNestedIfStmts")
    private int getGenCount(Individual descendant, Individual lookingFor, int gens) {

        if (descendant.equals(lookingFor)) {
            return gens;
        }

        // Check all the parents (including stepparents)
        if (descendant.getFamiliesWhereChild() != null) {
            for (FamilyChild fc : descendant.getFamiliesWhereChild()) {
                Family f = fc.getFamily();

                // Is the father who we're looking for?
                if (f.getHusband() != null && lookingFor.equals(f.getHusband().getIndividual())) {
                    return gens + 1;
                }

                // Is the mother who we're looking for?
                if (f.getWife() != null && lookingFor.equals(f.getWife().getIndividual())) {
                    return gens + 1;
                }
            }
        }

        // None of the parents and step-parents were the person we were looking for. Time to recurse and check each of THEIR
        // parents.
        if (descendant.getFamiliesWhereChild() != null) {
            for (FamilyChild fc : descendant.getFamiliesWhereChild()) {
                Family f = fc.getFamily();
                if (f.getHusband() != null && f.getHusband().getIndividual() != null) {
                    int j = getGenCount(f.getHusband().getIndividual(), lookingFor, 0);
                    if (j > -1) {
                        return gens + j + 1;
                    }
                }
                if (f.getWife() != null && f.getWife().getIndividual() != null) {
                    int k = getGenCount(f.getWife().getIndividual(), lookingFor, 0);
                    if (k > -1) {
                        return gens + k + 1;
                    }
                }
            }
        }
        return gens - 1;
    }

}
