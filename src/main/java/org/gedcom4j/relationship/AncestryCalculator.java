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

import java.util.HashSet;
import java.util.Set;

import org.gedcom4j.model.Family;
import org.gedcom4j.model.FamilyChild;
import org.gedcom4j.model.FamilySpouse;
import org.gedcom4j.model.Individual;

/**
 * A class for doing more advanced ancestry calculations than the basic {@link Individual#getAncestors()} method
 * 
 * @author frizbog1
 * 
 */
@SuppressWarnings("PMD.GodClass")
public class AncestryCalculator {

    /**
     * A flag for internal use to track whether the lowest common ancestor routine added any common ancestors to the result set
     * while it was recursing around
     */
    private boolean addedAnyCommonAncestors;

    /**
     * The set of people who have been checked already to see if they are an ancestor of the first individual. This is to keep
     * things efficient and to prevent infinite recursion and looping.
     */
    private Set<Individual> checkedAlready;

    /**
     * The "target list", or set of ancestors for the first individual. As we traverse the tree up through the second individual's
     * ancestors, we will check each one against this set (hence, "target set") for a match. The first ancestor we find of
     * individual 2 that is also some ancestor of individual 1 is our lowest common ancestor.
     */
    private Set<Individual> targetList;

    /**
     * A count of generations
     */
    private int genCount;

    /**
     * Get the "extended ancestry" of an individual. This is defined (for this method's purposes) as the individual's parents (and
     * step-parents), recursively. Should not include the individual themselves.
     * 
     * @param individual
     *            the individual whose extended ancestry is desired
     * @return the set of all ancestors for the individual, and all their spouses
     */
    public Set<Individual> getExtendedAncestry(Individual individual) {
        Set<Individual> result = new HashSet<>();

        addIndividualAndFamilies(result, individual);
        result.remove(individual);

        return result;
    }

    /**
     * Counts the number of generations between the ancestor and descendant.
     * 
     * @param descendant
     *            the descendant individual
     * @param ancestor
     *            the ancestor of the descendant
     * @return the number of generations separating descendant from ancestor. A parent-child relationship would be 1; a
     *         grandparent-child relationship would be 2. This method should always return a positive integer, or throw an
     *         exception.
     */
    public int getGenerationCount(Individual descendant, Individual ancestor) {
        genCount = 0;

        if (descendant.equals(ancestor)) {
            return 0;
        }

        if (lookForAncestor(descendant, ancestor)) {
            return genCount;
        }
        throw new IllegalArgumentException("Ancestor/descendant relationship not found for " + ancestor + " and " + descendant);
    }

    /**
     * Get a Set of the lowest common ancestors between two individuals
     * 
     * @param individual1
     *            individual 1
     * @param individual2
     *            individual 2
     * @return the set of lowest common ancestors
     */
    public Set<Individual> getLowestCommonAncestors(Individual individual1, Individual individual2) {
        Set<Individual> result = new HashSet<>();

        // Initialize the first iteration of using the lowest-common-ancestor
        // process
        initializeLcaSearch(individual1);
        // All set up, get the lowest common ancestors
        addLowestCommonAncestorsToSet(individual2, result, 0);

        return result;
    }

    /**
     * Add father and all his wives
     * 
     * @param result
     *            the result we are adding to
     * @param fc
     *            the family/child object we're working from
     */
    private void addFatherAndAllHisWives(Set<Individual> result, FamilyChild fc) {
        Individual dad = (fc.getFamily().getHusband() == null ? null : fc.getFamily().getHusband().getIndividual());
        if (dad != null && dad.getFamiliesWhereSpouse() != null) {
            for (FamilySpouse fs : dad.getFamiliesWhereSpouse()) {
                Individual dadsWife = (fs.getFamily().getWife() == null ? null : fs.getFamily().getWife().getIndividual());
                addIndividualAndFamilies(result, dadsWife);
            }
        }
        // And include his extended ancestry as well (recursively)
        addIndividualAndFamilies(result, dad);
    }

    /**
     * Add an individual and the families to which he/she was a child to the result set
     * 
     * @param result
     *            the result set being built
     * @param individual
     *            the individual who we are adding to the results, along with all his/her family members
     */
    private void addIndividualAndFamilies(Set<Individual> result, Individual individual) {
        // Get every family this individual was a child of
        if (individual == null || result.contains(individual)) {
            return;
        }
        result.add(individual);
        if (individual.getFamiliesWhereChild() != null) {
            for (FamilyChild fc : individual.getFamiliesWhereChild()) {
                addFatherAndAllHisWives(result, fc);
                addMotherAndAllHerHusbands(result, fc);
            }
        }
    }

    /**
     * <p>
     * Get a set of common ancestors between a specific individual and a person who we checked earlier.
     * </p>
     * <p>
     * The other person's ancestry is assumed to be in the field <code>ancestorsOfIndividual1</code>, which should be populated
     * prior to the first call to this recursive method.
     * </p>
     * <p>
     * As this method executes and recurses, it tracks who has already been checked in the field <code>checkedAlready</code>. As
     * people are checked, they are added to this set to prevent them from being checked multiple times and causing infinite
     * recursion and looping. This Set should be cleared and/or pre-populated prior to the first call to this recursive method.
     * </p>
     * 
     * @param individual
     *            the person who might have common ancestors with the original person
     * @param set
     *            the set of people we are adding to
     * @param level
     *            the level of recursion we're at
     */
    private void addLowestCommonAncestorsToSet(Individual individual, Set<Individual> set, int level) {

        if (individual == null) {
            return;
        }

        // To prevent infinite recursion and looping and other nastiness
        if (checkedAlready.contains(individual)) {
            return;
        }
        checkedAlready.add(individual);

        // Go through all the individuals parents and their spouses to see
        // if they are in the other person's set of ancestors
        if (individual.getFamiliesWhereChild() != null) {
            for (FamilyChild fc : individual.getFamiliesWhereChild()) {
                // First check dad
                Individual dad = fc.getFamily().getHusband() == null ? null : fc.getFamily().getHusband().getIndividual();
                if (!checkedAlready.contains(dad)) {
                    checkParent(level, set, dad);
                }
                // Now check mom
                Individual mom = fc.getFamily().getWife() == null ? null : fc.getFamily().getWife().getIndividual();
                if (!checkedAlready.contains(mom)) {
                    checkParent(level, set, mom);
                }
            }
        }

        // If we didn't find any common ancestors, recurse up this individual's parents
        if (!addedAnyCommonAncestors && individual.getFamiliesWhereChild() != null) {
            for (FamilyChild fc : individual.getFamiliesWhereChild()) {
                Individual dad = (fc.getFamily().getHusband() == null ? null : fc.getFamily().getHusband().getIndividual());
                if (dad != null && !checkedAlready.contains(dad)) {
                    addLowestCommonAncestorsToSet(dad, set, level + 1);
                }
                Individual mom = (fc.getFamily().getWife() == null ? null : fc.getFamily().getWife().getIndividual());
                if (mom != null && !checkedAlready.contains(mom)) {
                    addLowestCommonAncestorsToSet(mom, set, level + 1);
                }
            }
        }
    }

    /**
     * Add mother and all her husbands
     * 
     * @param result
     *            the result we are adding to
     * @param fc
     *            the family/child object we're working from
     */
    private void addMotherAndAllHerHusbands(Set<Individual> result, FamilyChild fc) {
        Individual mom = (fc.getFamily().getWife() == null ? null : fc.getFamily().getWife().getIndividual());
        if (mom != null && mom.getFamiliesWhereSpouse() != null) {
            for (FamilySpouse fs : mom.getFamiliesWhereSpouse()) {
                Individual momsHusband = (fs.getFamily().getHusband() == null ? null : fs.getFamily().getHusband().getIndividual());
                if (momsHusband != null) {
                    addIndividualAndFamilies(result, momsHusband);
                }
            }
        }
        // And include her extended ancestry as well (recursively)
        addIndividualAndFamilies(result, mom);
    }

    /**
     * Add some common ancestors to the result set
     * 
     * @param set
     *            the result set we are building
     * @param s
     *            the set of common ancestors we are adding
     */
    private void addToResultSet(Set<Individual> set, Set<Individual> s) {
        if (!s.isEmpty()) {
            /*
             * Parent's spouse or the spouse's ancestors in the target list, so add them to the result set
             */
            set.addAll(s);
            addedAnyCommonAncestors = true;
        }
    }

    /**
     * Check the parent in a family to see if he's a common ancestor
     * 
     * @param level
     *            the level we're recursing at
     * @param set
     *            the set of common ancestors we're adding to
     * @param parent
     *            the parent being checked
     * 
     */
    private void checkParent(int level, Set<Individual> set, Individual parent) {

        if (parent == null) {
            return;
        }

        if (targetList.contains(parent)) {
            // Parents is in common, add to result set
            set.add(parent);
            addedAnyCommonAncestors = true;
            return;
        }
        // Parent isn't in common, check their spouses
        if (parent.getFamiliesWhereSpouse() != null) {
            for (FamilySpouse fs : parent.getFamiliesWhereSpouse()) {
                Individual spouse = getSpouse(fs, parent);
                if (spouse == null) {
                    continue;
                }
                if (targetList.contains(spouse)) {
                    // Parent's spouse is in common, add to result set
                    set.add(spouse);
                    addedAnyCommonAncestors = true;
                } else if (!checkedAlready.contains(spouse) && spouse.getFamiliesWhereChild() != null && !spouse
                        .getFamiliesWhereChild().isEmpty()) {
                    Set<Individual> s = new HashSet<>();
                    addLowestCommonAncestorsToSet(spouse, s, level + 1);
                    addToResultSet(set, s);
                }
            }
        }
    }

    /**
     * Get the spouse of an individual in a family
     * 
     * @param fs
     *            the family
     * @param i
     *            the individual to get the spouse for
     * @return the spouse of the individual passed in
     */
    private Individual getSpouse(FamilySpouse fs, Individual i) {
        Family fam = fs.getFamily();
        if (fam.getHusband() != null && fam.getHusband().getIndividual() == i) {
            return (fam.getWife() == null ? null : fam.getWife().getIndividual());
        }
        if (fam.getWife() != null && fam.getWife().getIndividual() == i) {
            return (fam.getHusband() == null ? null : fam.getHusband().getIndividual());
        }
        return null;
    }

    /**
     * Initialize a Lowest-Common-Ancestor search
     * 
     * @param individual1
     *            the first individual in the search
     */
    private void initializeLcaSearch(Individual individual1) {
        targetList = getExtendedAncestry(individual1);
        checkedAlready = new HashSet<>();
        addedAnyCommonAncestors = false;
    }

    /**
     * A recursive method for counting generations between a specific person and an ancestor. Used as the workhorse for
     * {@link #getGenerationCount(Individual, Individual)}. Upon return, {@link #genCount} will equal the number of generations
     * between <code>person</code> and <code>ancestor</code>
     * 
     * @param person
     *            the person currently being examined
     * @param ancestor
     *            the ancestor we are looking for and which stops the recursion
     * @return true if and only if the ancestor has been found for person
     */
    private boolean lookForAncestor(Individual person, Individual ancestor) {
        if (person != null && person.getFamiliesWhereChild() != null) {
            for (FamilyChild fc : person.getFamiliesWhereChild()) {
                Family f = fc.getFamily();
                Individual w = f.getWife() == null ? null : f.getWife().getIndividual();
                Individual h = f.getHusband() == null ? null : f.getHusband().getIndividual();
                if (ancestor.equals(h) || ancestor.equals(w)) {
                    genCount = 1;
                    return true;
                } else if (lookForAncestor(h, ancestor)) {
                    genCount++;
                    return true;
                } else if (lookForAncestor(w, ancestor)) {
                    genCount++;
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;
    }
}
