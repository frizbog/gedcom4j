/*
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
package com.mattharrah.gedcom4j.relationship;

import static com.mattharrah.gedcom4j.relationship.RelationshipName.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that holds rules for simplifying complex relationships into shorter
 * ones
 */
public final class SimplificationRules {

    /**
     * <p>
     * A list of 3-element arrays, containing rules on how to collapse
     * complicated relationships down into simpler forms. Each row is an array
     * of three {@link RelationshipName}s; let's call them r1, r2, and r3. Let's
     * also assume there are three people: A, B, and C. If A is related to B by
     * r1, and B is related to C by r2, then A is related to C as r3 and B can
     * be removed.
     * </p>
     * <p>
     * Example: Rule= <code>[FATHER, SON, BROTHER]</code>. Bob's FATHER is Jim;
     * Jim's SON is Fred; therefore Bob's BROTHER is Fred.
     * </p>
     * <p>
     * The order that these triplets appear in the list is the order in which
     * the rules are applied. Rules that build on previous relationship
     * simplifications should appear later in the list.
     * </p>
     */
    static List<RelationshipName[]> rules = new ArrayList<RelationshipName[]>();

    /* static initializer to load the list */
    static {
        mothersAndFathers();
        siblings();
        grandparentsAndGrandchildren();
        greatGrandparentsAndGreatGrandchildren();
        greatGreatGrandparentsAndGreatGreatGrandchildren();
        greatGreatGreatGrandparentsAndGreatGreatGreatGrandchildren();
        auntsUnclesNiecesNephews();
    }

    /**
     * Load the rules for aunts, uncles, nieces, and nephews
     */
    static void auntsUnclesNiecesNephews() {
        newRule(MOTHER, BROTHER, UNCLE);
        newRule(FATHER, BROTHER, UNCLE);
        newRule(MOTHER, SISTER, AUNT);
        newRule(FATHER, SISTER, AUNT);
        newRule(BROTHER, SON, NEPHEW);
        newRule(BROTHER, DAUGHTER, NIECE);
        newRule(SISTER, SON, NEPHEW);
        newRule(SISTER, DAUGHTER, NIECE);
    }

    /**
     * Load the rules for grandparents and grandchildren
     */
    static void grandparentsAndGrandchildren() {
        newRule(FATHER, FATHER, GRANDFATHER);
        newRule(MOTHER, FATHER, GRANDFATHER);
        newRule(FATHER, MOTHER, GRANDMOTHER);
        newRule(MOTHER, MOTHER, GRANDMOTHER);
        newRule(SON, SON, GRANDSON);
        newRule(SON, DAUGHTER, GRANDDAUGHTER);
        newRule(SON, CHILD, GRANDCHILD);
        newRule(DAUGHTER, SON, GRANDSON);
        newRule(DAUGHTER, DAUGHTER, GRANDDAUGHTER);
        newRule(DAUGHTER, CHILD, GRANDCHILD);
    }

    /**
     * Load the rules for great-grandparents and great-grandchildren
     */
    static void greatGrandparentsAndGreatGrandchildren() {
        newRule(GRANDFATHER, FATHER, GREAT_GRANDFATHER);
        newRule(GRANDMOTHER, FATHER, GREAT_GRANDFATHER);
        newRule(GRANDFATHER, MOTHER, GREAT_GRANDMOTHER);
        newRule(GRANDMOTHER, MOTHER, GREAT_GRANDMOTHER);
        newRule(GRANDSON, SON, GREAT_GRANDSON);
        newRule(GRANDSON, DAUGHTER, GREAT_GRANDDAUGHTER);
        newRule(GRANDSON, CHILD, GREAT_GRANDCHILD);
        newRule(GRANDDAUGHTER, SON, GREAT_GRANDSON);
        newRule(GRANDDAUGHTER, DAUGHTER, GREAT_GRANDDAUGHTER);
        newRule(GRANDDAUGHTER, CHILD, GREAT_GRANDCHILD);
        newRule(GRANDCHILD, SON, GREAT_GRANDSON);
        newRule(GRANDCHILD, DAUGHTER, GREAT_GRANDDAUGHTER);
        newRule(GRANDCHILD, CHILD, GREAT_GRANDCHILD);
        newRule(FATHER, GRANDFATHER, GREAT_GRANDFATHER);
        newRule(MOTHER, GRANDFATHER, GREAT_GRANDFATHER);
        newRule(FATHER, GRANDMOTHER, GREAT_GRANDMOTHER);
        newRule(MOTHER, GRANDMOTHER, GREAT_GRANDMOTHER);
        newRule(SON, GRANDSON, GREAT_GRANDSON);
        newRule(SON, GRANDDAUGHTER, GREAT_GRANDDAUGHTER);
        newRule(SON, GRANDCHILD, GREAT_GRANDCHILD);
        newRule(DAUGHTER, GRANDSON, GREAT_GRANDSON);
        newRule(DAUGHTER, GRANDDAUGHTER, GREAT_GRANDDAUGHTER);
        newRule(DAUGHTER, GRANDCHILD, GREAT_GRANDCHILD);
    }

    /**
     * Load the rules for great-great-grandparents and great-great-grandchildren
     */
    static void greatGreatGrandparentsAndGreatGreatGrandchildren() {
        newRule(GREAT_GRANDFATHER, FATHER, GREAT_GREAT_GRANDFATHER);
        newRule(GREAT_GRANDMOTHER, FATHER, GREAT_GREAT_GRANDFATHER);
        newRule(GREAT_GRANDFATHER, MOTHER, GREAT_GREAT_GRANDMOTHER);
        newRule(GREAT_GRANDMOTHER, MOTHER, GREAT_GREAT_GRANDMOTHER);
        newRule(GREAT_GRANDSON, SON, GREAT_GREAT_GRANDSON);
        newRule(GREAT_GRANDSON, DAUGHTER, GREAT_GREAT_GRANDDAUGHTER);
        newRule(GREAT_GRANDSON, CHILD, GREAT_GREAT_GRANDCHILD);
        newRule(GREAT_GRANDDAUGHTER, SON, GREAT_GREAT_GRANDSON);
        newRule(GREAT_GRANDDAUGHTER, DAUGHTER, GREAT_GREAT_GRANDDAUGHTER);
        newRule(GREAT_GRANDDAUGHTER, CHILD, GREAT_GREAT_GRANDCHILD);
        newRule(GREAT_GRANDCHILD, SON, GREAT_GREAT_GRANDSON);
        newRule(GREAT_GRANDCHILD, DAUGHTER, GREAT_GREAT_GRANDDAUGHTER);
        newRule(GREAT_GRANDCHILD, CHILD, GREAT_GREAT_GRANDCHILD);
        newRule(FATHER, GREAT_GRANDFATHER, GREAT_GREAT_GRANDFATHER);
        newRule(MOTHER, GREAT_GRANDFATHER, GREAT_GREAT_GRANDFATHER);
        newRule(FATHER, GREAT_GRANDMOTHER, GREAT_GREAT_GRANDMOTHER);
        newRule(MOTHER, GREAT_GRANDMOTHER, GREAT_GREAT_GRANDMOTHER);
        newRule(SON, GREAT_GRANDSON, GREAT_GREAT_GRANDSON);
        newRule(SON, GREAT_GRANDDAUGHTER, GREAT_GREAT_GRANDDAUGHTER);
        newRule(SON, GREAT_GRANDCHILD, GREAT_GREAT_GRANDCHILD);
        newRule(DAUGHTER, GREAT_GRANDSON, GREAT_GREAT_GRANDSON);
        newRule(DAUGHTER, GREAT_GRANDDAUGHTER, GREAT_GREAT_GRANDDAUGHTER);
        newRule(DAUGHTER, GREAT_GRANDCHILD, GREAT_GREAT_GRANDCHILD);
        newRule(GRANDMOTHER, GRANDMOTHER, GREAT_GREAT_GRANDMOTHER);
        newRule(GRANDFATHER, GRANDMOTHER, GREAT_GREAT_GRANDMOTHER);
        newRule(GRANDMOTHER, GRANDFATHER, GREAT_GREAT_GRANDFATHER);
        newRule(GRANDFATHER, GRANDFATHER, GREAT_GREAT_GRANDFATHER);
        newRule(GRANDDAUGHTER, GRANDDAUGHTER, GREAT_GREAT_GRANDDAUGHTER);
        newRule(GRANDSON, GRANDDAUGHTER, GREAT_GREAT_GRANDDAUGHTER);
        newRule(GRANDCHILD, GRANDDAUGHTER, GREAT_GREAT_GRANDDAUGHTER);
        newRule(GRANDDAUGHTER, GRANDSON, GREAT_GREAT_GRANDSON);
        newRule(GRANDSON, GRANDSON, GREAT_GREAT_GRANDSON);
        newRule(GRANDCHILD, GRANDSON, GREAT_GREAT_GRANDSON);
        newRule(GRANDDAUGHTER, GRANDCHILD, GREAT_GREAT_GRANDCHILD);
        newRule(GRANDSON, GRANDCHILD, GREAT_GREAT_GRANDCHILD);
        newRule(GRANDCHILD, GRANDCHILD, GREAT_GREAT_GRANDCHILD);
    }

    /**
     * Load rules for mothers and fathers
     */
    static void mothersAndFathers() {
        newRule(FATHER, WIFE, MOTHER);
        newRule(MOTHER, HUSBAND, FATHER);
    }

    /**
     * Load rules for siblings
     */
    static void siblings() {
        newRule(FATHER, SON, BROTHER);
        newRule(MOTHER, SON, BROTHER);
        newRule(FATHER, DAUGHTER, SISTER);
        newRule(MOTHER, DAUGHTER, SISTER);
        newRule(FATHER, CHILD, SIBLING);
        newRule(MOTHER, CHILD, SIBLING);
    }

    /**
     * 
     */
    private static void greatGreatGreatGrandparentsAndGreatGreatGreatGrandchildren() {
        newRule(GREAT_GRANDFATHER, GRANDFATHER, GREAT_GREAT_GREAT_GRANDFATHER);
        newRule(GREAT_GRANDMOTHER, GRANDFATHER, GREAT_GREAT_GREAT_GRANDFATHER);
        newRule(GREAT_GRANDFATHER, GRANDMOTHER, GREAT_GREAT_GREAT_GRANDMOTHER);
        newRule(GREAT_GRANDMOTHER, GRANDMOTHER, GREAT_GREAT_GREAT_GRANDMOTHER);
        newRule(GREAT_GRANDSON, GRANDSON, GREAT_GREAT_GREAT_GRANDSON);
        newRule(GREAT_GRANDSON, GRANDDAUGHTER, GREAT_GREAT_GREAT_GRANDDAUGHTER);
        newRule(GREAT_GRANDSON, GRANDCHILD, GREAT_GREAT_GREAT_GRANDCHILD);
        newRule(GREAT_GRANDDAUGHTER, GRANDSON, GREAT_GREAT_GREAT_GRANDSON);
        newRule(GREAT_GRANDDAUGHTER, GRANDDAUGHTER,
                GREAT_GREAT_GREAT_GRANDDAUGHTER);
        newRule(GREAT_GRANDDAUGHTER, GRANDCHILD, GREAT_GREAT_GREAT_GRANDCHILD);
        newRule(GREAT_GRANDCHILD, GRANDSON, GREAT_GREAT_GREAT_GRANDSON);
        newRule(GREAT_GRANDCHILD, GRANDDAUGHTER,
                GREAT_GREAT_GREAT_GRANDDAUGHTER);
        newRule(GREAT_GRANDCHILD, GRANDCHILD, GREAT_GREAT_GREAT_GRANDCHILD);
        newRule(GRANDFATHER, GREAT_GRANDFATHER, GREAT_GREAT_GREAT_GRANDFATHER);
        newRule(GRANDMOTHER, GREAT_GRANDFATHER, GREAT_GREAT_GREAT_GRANDFATHER);
        newRule(GRANDFATHER, GREAT_GRANDMOTHER, GREAT_GREAT_GREAT_GRANDMOTHER);
        newRule(GRANDMOTHER, GREAT_GRANDMOTHER, GREAT_GREAT_GREAT_GRANDMOTHER);
        newRule(GRANDSON, GREAT_GRANDSON, GREAT_GREAT_GREAT_GRANDSON);
        newRule(GRANDSON, GREAT_GRANDDAUGHTER, GREAT_GREAT_GREAT_GRANDDAUGHTER);
        newRule(GRANDSON, GREAT_GRANDCHILD, GREAT_GREAT_GREAT_GRANDCHILD);
        newRule(GRANDDAUGHTER, GREAT_GRANDSON, GREAT_GREAT_GREAT_GRANDSON);
        newRule(GRANDDAUGHTER, GREAT_GRANDDAUGHTER,
                GREAT_GREAT_GREAT_GRANDDAUGHTER);
        newRule(GRANDDAUGHTER, GREAT_GRANDCHILD, GREAT_GREAT_GREAT_GRANDCHILD);

        newRule(GREAT_GREAT_GRANDFATHER, FATHER, GREAT_GREAT_GREAT_GRANDFATHER);
        newRule(GREAT_GREAT_GRANDMOTHER, FATHER, GREAT_GREAT_GREAT_GRANDFATHER);
        newRule(GREAT_GREAT_GRANDFATHER, MOTHER, GREAT_GREAT_GREAT_GRANDMOTHER);
        newRule(GREAT_GREAT_GRANDMOTHER, MOTHER, GREAT_GREAT_GREAT_GRANDMOTHER);
        newRule(GREAT_GREAT_GRANDSON, SON, GREAT_GREAT_GREAT_GRANDSON);
        newRule(GREAT_GREAT_GRANDSON, DAUGHTER, GREAT_GREAT_GREAT_GRANDDAUGHTER);
        newRule(GREAT_GREAT_GRANDSON, CHILD, GREAT_GREAT_GREAT_GRANDCHILD);
        newRule(GREAT_GREAT_GRANDDAUGHTER, SON, GREAT_GREAT_GREAT_GRANDSON);
        newRule(GREAT_GREAT_GRANDDAUGHTER, DAUGHTER,
                GREAT_GREAT_GREAT_GRANDDAUGHTER);
        newRule(GREAT_GREAT_GRANDDAUGHTER, CHILD, GREAT_GREAT_GREAT_GRANDCHILD);
        newRule(GREAT_GREAT_GRANDCHILD, SON, GREAT_GREAT_GREAT_GRANDSON);
        newRule(GREAT_GREAT_GRANDCHILD, DAUGHTER,
                GREAT_GREAT_GREAT_GRANDDAUGHTER);
        newRule(GREAT_GREAT_GRANDCHILD, CHILD, GREAT_GREAT_GREAT_GRANDCHILD);
        newRule(FATHER, GREAT_GREAT_GRANDFATHER, GREAT_GREAT_GREAT_GRANDFATHER);
        newRule(MOTHER, GREAT_GREAT_GRANDFATHER, GREAT_GREAT_GREAT_GRANDFATHER);
        newRule(FATHER, GREAT_GREAT_GRANDMOTHER, GREAT_GREAT_GREAT_GRANDMOTHER);
        newRule(MOTHER, GREAT_GREAT_GRANDMOTHER, GREAT_GREAT_GREAT_GRANDMOTHER);
        newRule(SON, GREAT_GREAT_GRANDSON, GREAT_GREAT_GREAT_GRANDSON);
        newRule(SON, GREAT_GREAT_GRANDDAUGHTER, GREAT_GREAT_GREAT_GRANDDAUGHTER);
        newRule(SON, GREAT_GREAT_GRANDCHILD, GREAT_GREAT_GREAT_GRANDCHILD);
        newRule(DAUGHTER, GREAT_GREAT_GRANDSON, GREAT_GREAT_GREAT_GRANDSON);
        newRule(DAUGHTER, GREAT_GREAT_GRANDDAUGHTER,
                GREAT_GREAT_GREAT_GRANDDAUGHTER);
        newRule(DAUGHTER, GREAT_GREAT_GRANDCHILD, GREAT_GREAT_GREAT_GRANDCHILD);

        newRule(GREAT_GRANDMOTHER, GRANDMOTHER, GREAT_GREAT_GREAT_GRANDMOTHER);
        newRule(GREAT_GRANDFATHER, GRANDMOTHER, GREAT_GREAT_GREAT_GRANDMOTHER);
        newRule(GREAT_GRANDMOTHER, GRANDFATHER, GREAT_GREAT_GREAT_GRANDFATHER);
        newRule(GREAT_GRANDFATHER, GRANDFATHER, GREAT_GREAT_GREAT_GRANDFATHER);
        newRule(GREAT_GRANDDAUGHTER, GRANDDAUGHTER,
                GREAT_GREAT_GREAT_GRANDDAUGHTER);
        newRule(GREAT_GRANDSON, GRANDDAUGHTER, GREAT_GREAT_GREAT_GRANDDAUGHTER);
        newRule(GREAT_GRANDCHILD, GRANDDAUGHTER,
                GREAT_GREAT_GREAT_GRANDDAUGHTER);
        newRule(GREAT_GRANDDAUGHTER, GRANDSON, GREAT_GREAT_GREAT_GRANDSON);
        newRule(GREAT_GRANDSON, GRANDSON, GREAT_GREAT_GREAT_GRANDSON);
        newRule(GREAT_GRANDCHILD, GRANDSON, GREAT_GREAT_GREAT_GRANDSON);
        newRule(GREAT_GRANDDAUGHTER, GRANDCHILD, GREAT_GREAT_GREAT_GRANDCHILD);
        newRule(GREAT_GRANDSON, GRANDCHILD, GREAT_GREAT_GREAT_GRANDCHILD);
        newRule(GREAT_GRANDCHILD, GRANDCHILD, GREAT_GREAT_GREAT_GRANDCHILD);

        newRule(GRANDMOTHER, GREAT_GRANDMOTHER, GREAT_GREAT_GREAT_GRANDMOTHER);
        newRule(GRANDFATHER, GREAT_GRANDMOTHER, GREAT_GREAT_GREAT_GRANDMOTHER);
        newRule(GRANDMOTHER, GREAT_GRANDFATHER, GREAT_GREAT_GREAT_GRANDFATHER);
        newRule(GRANDFATHER, GREAT_GRANDFATHER, GREAT_GREAT_GREAT_GRANDFATHER);
        newRule(GRANDDAUGHTER, GREAT_GRANDDAUGHTER,
                GREAT_GREAT_GREAT_GRANDDAUGHTER);
        newRule(GRANDSON, GREAT_GRANDDAUGHTER, GREAT_GREAT_GREAT_GRANDDAUGHTER);
        newRule(GRANDCHILD, GREAT_GRANDDAUGHTER,
                GREAT_GREAT_GREAT_GRANDDAUGHTER);
        newRule(GRANDDAUGHTER, GREAT_GRANDSON, GREAT_GREAT_GREAT_GRANDSON);
        newRule(GRANDSON, GREAT_GRANDSON, GREAT_GREAT_GREAT_GRANDSON);
        newRule(GRANDCHILD, GREAT_GRANDSON, GREAT_GREAT_GREAT_GRANDSON);
        newRule(GRANDDAUGHTER, GREAT_GRANDCHILD, GREAT_GREAT_GREAT_GRANDCHILD);
        newRule(GRANDSON, GREAT_GRANDCHILD, GREAT_GREAT_GREAT_GRANDCHILD);
        newRule(GRANDCHILD, GREAT_GRANDCHILD, GREAT_GREAT_GREAT_GRANDCHILD);

    }

    /**
     * Convenience method to keep code brief
     * 
     * @param r1
     *            relationship1
     * @param r2
     *            relationship2
     * @param r3
     *            relationship3
     * @return the three relationships as a rule
     */
    private static void newRule(RelationshipName r1, RelationshipName r2,
            RelationshipName r3) {
        RelationshipName[] r = new RelationshipName[3];
        r[0] = r1;
        r[1] = r2;
        r[2] = r3;
        rules.add(r);
    }

    /**
     * Private constructor prevents instantiation or subclassing
     */
    private SimplificationRules() {
        super();
    }

}
