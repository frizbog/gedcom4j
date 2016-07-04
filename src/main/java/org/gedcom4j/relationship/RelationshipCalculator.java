/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.gedcom4j.relationship;

import static org.gedcom4j.relationship.RelationshipName.*;

import java.util.*;

import org.gedcom4j.model.*;

/**
 * <p>
 * A class for calculating relationships between individuals.
 * </p>
 * <p>
 * Note that the idea of relationships between individuals can be a complex one. Sometimes people can have more than one
 * possible relationship between them, and the relationships between intermediates can be through marriage or blood or
 * adoption.
 * </p>
 * <p>
 * The relationships returned from this class do not have String names like "Father" or "Mother" for several reasons:
 * </p>
 * <ol>
 * <li>The String descriptions would need to be in a language, and I only know English and a <i>very</i> little German,
 * and I don't have the resources or knowledge to do a full internationalization into many languages.</li>
 * <li>There are matters of preference in string descriptions. For example, is my brother's grandson my "great-nephew"
 * or my "grand-nephew?" Does it include a space, a hyphen, or is it all rammed together?</li>
 * <li>Some users may care about step- relationships or adoptive relationships for their purposes, and others may not. A
 * string description could not serve both purposes simultaneously.</li>
 * </ol>
 * <p>
 * Ultimately, how the relationship is described is a presentation-layer concern, and as gedcom4j is a library with no
 * presentation layer, the descriptions are left to the consumer of the library.
 * </p>
 * 
 * @author frizbog1
 */
public class RelationshipCalculator {

    /**
     * The person we are starting from
     */
    private Individual startingIndividual;

    /**
     * The person we are looking for as we traverse the tree
     */
    private Individual targetIndividual;

    /**
     * The list of relationships we've found that matched
     */
    private List<Relationship> relationshipsFound;

    /**
     * The current chain of relationships between individuals we're considering as we traverse the tree. Stated
     * differently, how did we get from individual 1 to the person we're currently working with as we recurse?
     */
    private List<SimpleRelationship> currentChain;

    /**
     * People we have looked at already
     */
    private Set<Individual> lookedAt = new HashSet<Individual>();

    /**
     * <p>
     * Calculate the relationship(s) between two individuals, based on common ancestors (people with no common
     * ancestors, either by blood, marriage, or adoption are considered unrelated). The relationships are then sorted by
     * the number of "hops" between people, and the shortest relationship is found. Then, any relationship longer than
     * that shortest one is removed from the result set, <code>relationshipsFound</code>.
     * </p>
     * <p>
     * Typical usage would be to instantiate a <code>RelationshipCalculator</code> object, call this method with the two
     * people of interest, and then check the <code>relationshipsFound</code> collection to find the most direct
     * relationship(s) between the individuals. If that collection is empty, either the people are not related or the
     * two individuals are the same person.
     * </p>
     * 
     * @param individual1
     *            the first individual
     * @param individual2
     *            the second individual
     * @param simplified
     *            should the list be reduced to a simplified form (for example, should Father of Father be collapsed to
     *            Grandfather)
     */
    public void calculateRelationships(Individual individual1, Individual individual2, boolean simplified) {

        // Clear out the results from last time
        relationshipsFound = new ArrayList<Relationship>();

        // We are starting with the first individual
        startingIndividual = individual1;

        // We are looking for the second individual;
        targetIndividual = individual2;

        // We currently have taken no steps away from individual 1
        currentChain = new ArrayList<SimpleRelationship>();

        lookedAt = new HashSet<Individual>();

        // Start with individual 1 and recurse
        if (individual1 != individual2) { // NOPMD - Deliberately comparing with !=
            examine(individual1);
        }

        if (simplified) {
            for (Relationship r : relationshipsFound) {
                simplifyRelationship(r);
            }
        }
        if (relationshipsFound.size() > 1) {
            // Remove duplicates
            relationshipsFound = new ArrayList<Relationship>(new HashSet<Relationship>(relationshipsFound));
            Collections.sort(relationshipsFound);

            // Of the unique chains, the shortest ones are preferred
            int shortestLength = relationshipsFound.get(0).getChain().size();
            for (int i = relationshipsFound.size() - 1; i >= 0; i--) {
                if (relationshipsFound.get(i).getChain().size() > shortestLength) {
                    relationshipsFound.remove(i);
                }
            }

            // Of chains of equal lengths, the simplest ones are preferred
            int simplestSimplicity = Integer.MAX_VALUE;
            List<Relationship> keepers = new ArrayList<Relationship>();
            for (int i = relationshipsFound.size() - 1; i >= 0; i--) {
                Relationship r = relationshipsFound.get(i);
                int totalSimplicity = r.getTotalSimplicity();
                if (totalSimplicity == simplestSimplicity) {
                    keepers.add(r);
                } else if (totalSimplicity < simplestSimplicity) {
                    keepers.clear();
                    simplestSimplicity = totalSimplicity;
                    keepers.add(r);
                }
            }
            relationshipsFound = keepers;
        }

    }

    /**
     * Get the relationshipsFound
     * 
     * @return the relationshipsFound
     */
    public List<Relationship> getRelationshipsFound() {
        return relationshipsFound;
    }

    /**
     * <p>
     * Collapse down two steps in a chain to a simpler form of it (for example, the son of a father is a brother).
     * </p>
     * <p>
     * Algorithm: Look for two steps in a row with relationships <code>rel1</code> and <code>rel2</code>, describing 3
     * people (A, B, and C). If person A is the <code>rel1</code> of person B, and person B is the <code>rel2</code> of
     * person C, then A is the <code>newRel</code> C, C is the <code>newReverseRel</code> of A), and B drops out
     * entirely.
     * </p>
     * <p>
     * Note that this method stops collapsing after the first collapse, or once the end of the chain is reached. This
     * method should be called repeatedly to completely collapse the chain until no shortening is achieved.
     * </p>
     * 
     * @param chain
     *            the chain being collapsed. Assumed to already be of length 2 or greater. Will fail if shorter than
     *            that.
     * @param rel1
     *            relationship to look for between the first two people
     * @param rel2
     *            relationship to look for between the second two people
     * @param newRel
     *            the new relationship to use instead, collapsing out the 'middleman'
     */
    private void collapse(List<SimpleRelationship> chain, RelationshipName rel1, RelationshipName rel2, RelationshipName newRel) {

        for (int i = 0; i < chain.size() - 1; i++) {
            SimpleRelationship s1 = chain.get(i);
            SimpleRelationship s2 = chain.get(i + 1);

            if (s1.getName() == rel1 && s2.getName() == rel2 && s1.getIndividual2() == s2.getIndividual1()) {
                // Get the reverse relationship
                RelationshipName rr = getReverseRelationship(newRel, s1.getIndividual1().getSex());
                if (rr != null) {
                    // Only collapse if we actually could derive a reverse
                    // relationship
                    s1.setIndividual2(s2.getIndividual2());
                    s1.setName(newRel);
                    s1.setReverseName(rr);
                    chain.remove(i + 1);
                }
            }
        }

    }

    /**
     * Check if the person being examined is the target person. If not, start looking through everyone that person is
     * related to.
     * 
     * @param personBeingExamined
     *            the person who is currently being examined
     */
    private void examine(Individual personBeingExamined) {
        if (personBeingExamined == null) {
            return;
        }
        if (lookedAt.contains(personBeingExamined)) {
            // prevent wasting time checking the same people over and over
            return;
        }
        /*
         * Here I am deliberately using == and not equals(). Rationale: 1) In a given gedcom, there won't be two
         * individuals who are exactly equal without being the same instance...the XREF's would ensure this. 2) Doing an
         * equals() compare is a deep compare, including all its subcollections (notes, etc.), which aren't really what
         * we're trying to do. We aren't looking for someone who evaluates the same as the person - we are looking FOR
         * that exact person. == is a better fit.
         */
        if (personBeingExamined == targetIndividual) { // NOPMD - deliberate use of ==
            /*
             * We've found our target, so make a Relationship object out of the current chain and add it to our result
             * set.
             */
            Relationship r = new Relationship(startingIndividual, targetIndividual, currentChain);
            relationshipsFound.add(r);
        } else {
            lookedAt.add(personBeingExamined);
            /* Not our target, so check relatives, starting with parents */
            for (FamilyChild fc : personBeingExamined.getFamiliesWhereChild()) {
                Family family = fc.getFamily();
                if (!lookedAt.contains(family.getHusband())) {
                    examineFather(personBeingExamined, family.getHusband());
                }
                if (!lookedAt.contains(family.getWife())) {
                    examineMother(personBeingExamined, family.getWife());
                }
            }
            /* Next check spouses */
            for (FamilySpouse fs : personBeingExamined.getFamiliesWhereSpouse()) {
                Family family = fs.getFamily();
                if (family.getHusband() == personBeingExamined) {
                    if (lookedAt.contains(family.getWife())) {
                        continue;
                    }
                    examineWife(personBeingExamined, fs);
                } else if (family.getWife() == personBeingExamined) {
                    if (lookedAt.contains(family.getHusband())) {
                        continue;
                    }
                    examineHusband(personBeingExamined, fs);
                }
                /* and check the children */
                for (Individual c : family.getChildren()) {
                    if (lookedAt.contains(c)) {
                        continue;
                    }
                    if (family.getHusband() == personBeingExamined) { // NOPMD - deliberately using ==, want to check if
                        // same instance
                        examineChild(personBeingExamined, c, FATHER);
                    } else if (family.getWife() == personBeingExamined) { // NOPMD - deliberately using ==, want to
                                                                          // check
                        // if same instance
                        examineChild(personBeingExamined, c, MOTHER);
                    }
                }
            }
        }
    }

    /**
     * Examine the child of a person
     * 
     * @param personBeingExamined
     *            the parent, who
     * @param child
     *            the child
     * @param reverseRelationship
     *            the relationship from the child to the parent
     */
    private void examineChild(Individual personBeingExamined, Individual child, RelationshipName reverseRelationship) {
        SimpleRelationship r = new SimpleRelationship();
        r.setIndividual1(personBeingExamined);
        r.setIndividual2(child);
        if ("M".equals(child.getSex().getValue())) {
            r.setName(SON);
        } else if ("F".equals(child.getSex().getValue())) {
            r.setName(DAUGHTER);
        } else {
            r.setName(CHILD);
        }
        r.setReverseName(reverseRelationship);
        currentChain.add(r);
        examine(r.getIndividual2());
        currentChain.remove(currentChain.size() - 1);
    }

    /**
     * Examine the father
     * 
     * @param personBeingExamined
     *            the person whose parents are being examined
     * @param father
     *            the father
     */
    private void examineFather(Individual personBeingExamined, Individual father) {
        SimpleRelationship r = new SimpleRelationship();
        r.setIndividual1(personBeingExamined);
        r.setIndividual2(father);
        if ("M".equals(personBeingExamined.getSex().getValue())) {
            r.setReverseName(SON);
        } else if ("F".equals(personBeingExamined.getSex().getValue())) {
            r.setReverseName(DAUGHTER);
        } else {
            r.setReverseName(CHILD);
        }
        r.setName(FATHER);
        currentChain.add(r);
        examine(r.getIndividual2());
        currentChain.remove(currentChain.size() - 1);
    }

    /**
     * Examine the wife of the person being examined
     * 
     * @param personBeingExamined
     *            the person being examined
     * @param fs
     *            the {@link FamilySpouse} record to which the personBeingExamined is the husband
     */
    private void examineHusband(Individual personBeingExamined, FamilySpouse fs) {
        SimpleRelationship r = new SimpleRelationship();
        r.setIndividual1(personBeingExamined);
        r.setIndividual2(fs.getFamily().getHusband());
        r.setName(RelationshipName.HUSBAND);
        currentChain.add(r);
        examine(r.getIndividual2());
        currentChain.remove(currentChain.size() - 1);
    }

    /**
     * Examine the mother
     * 
     * @param personBeingExamined
     *            the person whose parents are being examined
     * @param mother
     *            the mother
     */
    private void examineMother(Individual personBeingExamined, Individual mother) {
        SimpleRelationship r = new SimpleRelationship();
        r.setIndividual1(personBeingExamined);
        r.setIndividual2(mother);
        if ("M".equals(personBeingExamined.getSex().getValue())) {
            r.setReverseName(SON);
        } else if ("F".equals(personBeingExamined.getSex().getValue())) {
            r.setReverseName(DAUGHTER);
        } else {
            r.setReverseName(CHILD);
        }
        r.setName(MOTHER);
        currentChain.add(r);
        examine(r.getIndividual2());
        currentChain.remove(currentChain.size() - 1);
    }

    /**
     * Examine the wife of the person being examined
     * 
     * @param personBeingExamined
     *            the person being examined
     * @param fs
     *            the {@link FamilySpouse} record to which the personBeingExamined is the husband
     */
    private void examineWife(Individual personBeingExamined, FamilySpouse fs) {
        SimpleRelationship r = new SimpleRelationship();
        r.setIndividual1(personBeingExamined);
        r.setIndividual2(fs.getFamily().getWife());
        r.setName(RelationshipName.WIFE);
        currentChain.add(r);
        examine(r.getIndividual2());
        currentChain.remove(currentChain.size() - 1);
    }

    /**
     * Get the reverse of a given relationship, based on the gender of the original person. For example, if person A has
     * a brother, the brother's relationship back to person A is either brother (if A is male), sister (if A is female),
     * or sibling (if A's gender is unknown).
     * 
     * @param relationship
     *            original relationship from person to someone else
     * @param sex
     *            the sex of original person
     * @return what the relationship would be back to the original person
     */
    private RelationshipName getReverseRelationship(RelationshipName relationship, StringWithCustomTags sex) {
        if ("M".equals(sex.getValue())) {
            return relationship.reverseForMale;
        }
        if ("F".equals(sex.getValue())) {
            return relationship.reverseForFemale;
        }
        return relationship.reverseForUnknown;
    }

    /**
     * Go through pairs of steps in the chain, seeing if they can be collapsed. Only basic, immediate family
     * relationships are collapsed (like, "my father's son" is "my brother").
     * 
     * @param relationship
     *            the relationship being simplified
     */
    private void simplifyRelationship(Relationship relationship) {

        int previousLength = Integer.MAX_VALUE;
        // You can only simplify a chain that's two or more steps!
        while (relationship.getChain().size() > 1) {
            if (relationship.getChain().size() >= previousLength) {
                // Didn't make any improvement, so we're done simplifying this
                // relationship
                return;
            }
            // Save how long the chain is now, so we can see if we made an
            // improvement.
            previousLength = relationship.getChain().size();

            for (RelationshipName[] rule : SimplificationRules.rules) {
                collapse(relationship.getChain(), rule[0], rule[1], rule[2]);
            }
        }
    }
}
