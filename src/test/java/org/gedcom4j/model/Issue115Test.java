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
package org.gedcom4j.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import org.junit.Test;

/**
 * Test for Issue 115, where {@link Individual#getDescendants()} could get a stack overflow error from infinite recursion if there
 * is a cyclical ancestral relationship in the data (for whatever reason).
 * 
 * @author frizbog
 */
public class Issue115Test {

    /**
     * The test fixture
     */
    private final IGedcom g = getGedcomWithCyclicalData();

    /**
     * Test that we get no stack overflow error
     */
    @Test
    public void testIssue115Fix() {
        for (int i = 1; i <= 3; i++) {
            Individual ind = g.getIndividuals().get("@I00" + i + "@");
            assertNotNull(ind);

            Set<Individual> d = ind.getDescendants();
            assertNotNull(d);
            assertEquals("All three generations are ancestors - person is descendant of himself", 3, d.size());

            Set<Individual> a = ind.getAncestors();
            assertNotNull(a);
            assertEquals("All three generations are ancestors - person is ancestor of himself", 3, a.size());
        }
    }

    /**
     * Deliberately build a gedcom with cyclical ancestral relationship.
     * 
     * @return a gedcom with intentionally cyclical relationship data, for test purposes
     */
    private IGedcom getGedcomWithCyclicalData() {
        IGedcom result = new Gedcom();

        Individual i1 = makeIndividual(result, 1);
        Individual i2 = makeIndividual(result, 2);
        Individual i3 = makeIndividual(result, 3);

        makeParentChild(result, i1, i2);
        makeParentChild(result, i2, i3);
        // And here comes the problem
        makeParentChild(result, i3, i1);

        return result;
    }

    /**
     * Make an individual and add them to the gedcom's individuals map
     * 
     * @param gedcom
     *            the gedcom
     * @param genNumber
     *            the generation number
     * @return the individual
     */
    private Individual makeIndividual(IGedcom gedcom, int genNumber) {
        Individual result = new Individual();
        PersonalName n = new PersonalName();
        n.setBasic("George /" + genNumber + "/");
        result.setXref("@I00" + genNumber + "@");
        result.getNames(true).add(n);
        gedcom.getIndividuals().put(result.getXref(), result);
        return result;
    }

    /**
     * Create a family in the gedcom structure, and set one person to be a parent of the second person
     * 
     * @param gedcom
     *            the gedcom we're building up
     * @param i1
     *            individual 1, to be the father
     * @param i2
     *            individual 2, to be the child
     */
    private void makeParentChild(IGedcom gedcom, Individual i1, Individual i2) {
        int fnum = gedcom.getFamilies().size() + 1;
        // Add the family to the gedcom map
        Family f = new Family();
        f.setXref("@F00" + fnum + "@");
        gedcom.getFamilies().put(f.getXref(), f);

        // Set individual 1 to be the father of individual 2
        f.setHusband(new IndividualReference(i1));
        f.getChildren(true).add(new IndividualReference(i2));

        // Set the family on the parent
        FamilySpouse fs = new FamilySpouse();
        fs.setFamily(f);
        i1.getFamiliesWhereSpouse(true).add(fs);

        // Set the family on the child
        FamilyChild fc = new FamilyChild();
        fc.setFamily(f);
        i2.getFamiliesWhereChild(true).add(fc);

    }

}
