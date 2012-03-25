/*
 * Copyright (c) 2009-2012 Matthew R. Harrah
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
        auntsUnclesNiecesNephews();
    }

    /**
     * Load the rules for aunts, uncles, nieces, and nephews
     */
    static void auntsUnclesNiecesNephews() {
        rules.add(new RelationshipName[] {
                MOTHER, BROTHER, UNCLE
        });
        rules.add(new RelationshipName[] {
                FATHER, BROTHER, UNCLE
        });
        rules.add(new RelationshipName[] {
                MOTHER, SISTER, AUNT
        });
        rules.add(new RelationshipName[] {
                FATHER, SISTER, AUNT
        });
        rules.add(new RelationshipName[] {
                BROTHER, SON, NEPHEW
        });
        rules.add(new RelationshipName[] {
                BROTHER, DAUGHTER, NIECE
        });
        rules.add(new RelationshipName[] {
                SISTER, SON, NEPHEW
        });
        rules.add(new RelationshipName[] {
                SISTER, DAUGHTER, NIECE
        });
    }

    /**
     * Load the rules for grandparents and grandchildren
     */
    static void grandparentsAndGrandchildren() {
        rules.add(new RelationshipName[] {
                FATHER, FATHER, GRANDFATHER
        });
        rules.add(new RelationshipName[] {
                MOTHER, FATHER, GRANDFATHER
        });
        rules.add(new RelationshipName[] {
                FATHER, MOTHER, GRANDMOTHER
        });
        rules.add(new RelationshipName[] {
                MOTHER, MOTHER, GRANDMOTHER
        });
        rules.add(new RelationshipName[] {
                SON, SON, GRANDSON
        });
        rules.add(new RelationshipName[] {
                SON, DAUGHTER, GRANDDAUGHTER
        });
        rules.add(new RelationshipName[] {
                SON, CHILD, GRANDCHILD
        });
        rules.add(new RelationshipName[] {
                DAUGHTER, SON, GRANDSON
        });
        rules.add(new RelationshipName[] {
                DAUGHTER, DAUGHTER, GRANDDAUGHTER
        });
        rules.add(new RelationshipName[] {
                DAUGHTER, CHILD, GRANDCHILD
        });
    }

    /**
     * Load the rules for great-grandparents and great-grandchildren
     */
    static void greatGrandparentsAndGreatGrandchildren() {
        rules.add(new RelationshipName[] {
                GRANDFATHER, FATHER, GREAT_GRANDFATHER
        });
        rules.add(new RelationshipName[] {
                GRANDMOTHER, FATHER, GREAT_GRANDFATHER
        });
        rules.add(new RelationshipName[] {
                GRANDFATHER, MOTHER, GREAT_GRANDMOTHER
        });
        rules.add(new RelationshipName[] {
                GRANDMOTHER, MOTHER, GREAT_GRANDMOTHER
        });
        rules.add(new RelationshipName[] {
                GRANDSON, SON, GREAT_GRANDSON
        });
        rules.add(new RelationshipName[] {
                GRANDSON, DAUGHTER, GREAT_GRANDDAUGHTER
        });
        rules.add(new RelationshipName[] {
                GRANDSON, CHILD, GREAT_GRANDCHILD
        });
        rules.add(new RelationshipName[] {
                GRANDDAUGHTER, SON, GREAT_GRANDSON
        });
        rules.add(new RelationshipName[] {
                GRANDDAUGHTER, DAUGHTER, GREAT_GRANDDAUGHTER
        });
        rules.add(new RelationshipName[] {
                GRANDDAUGHTER, CHILD, GREAT_GRANDCHILD
        });
        rules.add(new RelationshipName[] {
                GRANDCHILD, SON, GREAT_GRANDSON
        });
        rules.add(new RelationshipName[] {
                GRANDCHILD, DAUGHTER, GREAT_GRANDDAUGHTER
        });
        rules.add(new RelationshipName[] {
                GRANDCHILD, CHILD, GREAT_GRANDCHILD
        });
        rules.add(new RelationshipName[] {
                FATHER, GRANDFATHER, GREAT_GRANDFATHER
        });
        rules.add(new RelationshipName[] {
                MOTHER, GRANDFATHER, GREAT_GRANDFATHER
        });
        rules.add(new RelationshipName[] {
                FATHER, GRANDMOTHER, GREAT_GRANDMOTHER
        });
        rules.add(new RelationshipName[] {
                MOTHER, GRANDMOTHER, GREAT_GRANDMOTHER
        });
        rules.add(new RelationshipName[] {
                SON, GRANDSON, GREAT_GRANDSON
        });
        rules.add(new RelationshipName[] {
                SON, GRANDDAUGHTER, GREAT_GRANDDAUGHTER
        });
        rules.add(new RelationshipName[] {
                SON, GRANDCHILD, GREAT_GRANDCHILD
        });
        rules.add(new RelationshipName[] {
                DAUGHTER, GRANDSON, GREAT_GRANDSON
        });
        rules.add(new RelationshipName[] {
                DAUGHTER, GRANDDAUGHTER, GREAT_GRANDDAUGHTER
        });
        rules.add(new RelationshipName[] {
                DAUGHTER, GRANDCHILD, GREAT_GRANDCHILD
        });
    }

    /**
     * Load the rules for great-great-grandparents and great-great-grandchildren
     */
    static void greatGreatGrandparentsAndGreatGreatGrandchildren() {
        rules.add(new RelationshipName[] {
                GREAT_GRANDFATHER, FATHER, GREAT_GREAT_GRANDFATHER
        });
        rules.add(new RelationshipName[] {
                GREAT_GRANDMOTHER, FATHER, GREAT_GREAT_GRANDFATHER
        });
        rules.add(new RelationshipName[] {
                GREAT_GRANDFATHER, MOTHER, GREAT_GREAT_GRANDMOTHER
        });
        rules.add(new RelationshipName[] {
                GREAT_GRANDMOTHER, MOTHER, GREAT_GREAT_GRANDMOTHER
        });
        rules.add(new RelationshipName[] {
                GREAT_GRANDSON, SON, GREAT_GREAT_GRANDSON
        });
        rules.add(new RelationshipName[] {
                GREAT_GRANDSON, DAUGHTER, GREAT_GREAT_GRANDDAUGHTER
        });
        rules.add(new RelationshipName[] {
                GREAT_GRANDSON, CHILD, GREAT_GREAT_GRANDCHILD
        });
        rules.add(new RelationshipName[] {
                GREAT_GRANDDAUGHTER, SON, GREAT_GREAT_GRANDSON
        });
        rules.add(new RelationshipName[] {
                GREAT_GRANDDAUGHTER, DAUGHTER, GREAT_GREAT_GRANDDAUGHTER
        });
        rules.add(new RelationshipName[] {
                GREAT_GRANDDAUGHTER, CHILD, GREAT_GREAT_GRANDCHILD
        });
        rules.add(new RelationshipName[] {
                GREAT_GRANDCHILD, SON, GREAT_GREAT_GRANDSON
        });
        rules.add(new RelationshipName[] {
                GREAT_GRANDCHILD, DAUGHTER, GREAT_GREAT_GRANDDAUGHTER
        });
        rules.add(new RelationshipName[] {
                GREAT_GRANDCHILD, CHILD, GREAT_GREAT_GRANDCHILD
        });
        rules.add(new RelationshipName[] {
                FATHER, GREAT_GRANDFATHER, GREAT_GREAT_GRANDFATHER
        });
        rules.add(new RelationshipName[] {
                MOTHER, GREAT_GRANDFATHER, GREAT_GREAT_GRANDFATHER
        });
        rules.add(new RelationshipName[] {
                FATHER, GREAT_GRANDMOTHER, GREAT_GREAT_GRANDMOTHER
        });
        rules.add(new RelationshipName[] {
                MOTHER, GREAT_GRANDMOTHER, GREAT_GREAT_GRANDMOTHER
        });
        rules.add(new RelationshipName[] {
                SON, GREAT_GRANDSON, GREAT_GREAT_GRANDSON
        });
        rules.add(new RelationshipName[] {
                SON, GREAT_GRANDDAUGHTER, GREAT_GREAT_GRANDDAUGHTER
        });
        rules.add(new RelationshipName[] {
                SON, GREAT_GRANDCHILD, GREAT_GREAT_GRANDCHILD
        });
        rules.add(new RelationshipName[] {
                DAUGHTER, GREAT_GRANDSON, GREAT_GREAT_GRANDSON
        });
        rules.add(new RelationshipName[] {
                DAUGHTER, GREAT_GRANDDAUGHTER, GREAT_GREAT_GRANDDAUGHTER
        });
        rules.add(new RelationshipName[] {
                DAUGHTER, GREAT_GRANDCHILD, GREAT_GREAT_GRANDCHILD
        });
        rules.add(new RelationshipName[] {
                GRANDMOTHER, GRANDMOTHER, GREAT_GREAT_GRANDMOTHER
        });
        rules.add(new RelationshipName[] {
                GRANDFATHER, GRANDMOTHER, GREAT_GREAT_GRANDMOTHER
        });
        rules.add(new RelationshipName[] {
                GRANDMOTHER, GRANDFATHER, GREAT_GREAT_GRANDFATHER
        });
        rules.add(new RelationshipName[] {
                GRANDFATHER, GRANDFATHER, GREAT_GREAT_GRANDFATHER
        });
        rules.add(new RelationshipName[] {
                GRANDDAUGHTER, GRANDDAUGHTER, GREAT_GREAT_GRANDDAUGHTER
        });
        rules.add(new RelationshipName[] {
                GRANDSON, GRANDDAUGHTER, GREAT_GREAT_GRANDDAUGHTER
        });
        rules.add(new RelationshipName[] {
                GRANDCHILD, GRANDDAUGHTER, GREAT_GREAT_GRANDDAUGHTER
        });
        rules.add(new RelationshipName[] {
                GRANDDAUGHTER, GRANDSON, GREAT_GREAT_GRANDSON
        });
        rules.add(new RelationshipName[] {
                GRANDSON, GRANDSON, GREAT_GREAT_GRANDSON
        });
        rules.add(new RelationshipName[] {
                GRANDCHILD, GRANDSON, GREAT_GREAT_GRANDSON
        });
        rules.add(new RelationshipName[] {
                GRANDDAUGHTER, GRANDCHILD, GREAT_GREAT_GRANDCHILD
        });
        rules.add(new RelationshipName[] {
                GRANDSON, GRANDCHILD, GREAT_GREAT_GRANDCHILD
        });
        rules.add(new RelationshipName[] {
                GRANDCHILD, GRANDCHILD, GREAT_GREAT_GRANDCHILD
        });

    }

    /**
     * Load rules for mothers and fathers
     */
    static void mothersAndFathers() {
        rules.add(new RelationshipName[] {
                FATHER, WIFE, MOTHER
        });
        rules.add(new RelationshipName[] {
                MOTHER, HUSBAND, FATHER
        });
    }

    /**
     * Load rules for siblings
     */
    static void siblings() {
        rules.add(new RelationshipName[] {
                FATHER, SON, BROTHER
        });
        rules.add(new RelationshipName[] {
                MOTHER, SON, BROTHER
        });
        rules.add(new RelationshipName[] {
                FATHER, DAUGHTER, SISTER
        });
        rules.add(new RelationshipName[] {
                MOTHER, DAUGHTER, SISTER
        });
        rules.add(new RelationshipName[] {
                FATHER, CHILD, SIBLING
        });
        rules.add(new RelationshipName[] {
                MOTHER, CHILD, SIBLING
        });
    }

    /**
     * Private constructor prevents instantiation or subclassing
     */
    private SimplificationRules() {
        super();
    }

}
