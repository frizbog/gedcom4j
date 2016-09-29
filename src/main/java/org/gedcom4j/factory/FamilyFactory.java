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
package org.gedcom4j.factory;

import java.util.Arrays;

import org.gedcom4j.model.Family;
import org.gedcom4j.model.FamilyChild;
import org.gedcom4j.model.FamilySpouse;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;

/**
 * Class to build a {@link Family} object
 * 
 * @author frizbog
 */
public class FamilyFactory {

    /**
     * Create a family, add people to it, and put it in the gedcom
     * 
     * @param g
     *            the gedcom
     * @param father
     *            the father - optional, but if supplied, must already exist in the gedcom (by xref)
     * @param mother
     *            the mother - optional, but if supplied, must already exist in the gedcom (by xref)
     * @param children
     *            the children - optional, but if supplied, must already exist in the gedcom (by xref)
     * @return the family created and added to the gedcom
     */
    public Family create(Gedcom g, Individual father, Individual mother, Individual... children) {

        // Validate that the people passed in exist
        if (father != null && !g.getIndividuals().containsKey(father.getXref())) {
            throw new IllegalArgumentException("Father could not be found by xref in supplied gedcom object: " + father.getXref());
        }
        if (mother != null && !g.getIndividuals().containsKey(mother.getXref())) {
            throw new IllegalArgumentException("Mother could not be found by xref in supplied gedcom object: " + mother.getXref());
        }
        if (children != null) {
            for (Individual kid : children) {
                if (!g.getIndividuals().containsKey(kid.getXref())) {
                    throw new IllegalArgumentException("Child could not be found by xref in supplied gedcom object: " + kid
                            .getXref());
                }
            }
        }

        Family result = new Family();

        // Make an xref and add to gedcom
        for (int xref = g.getFamilies().size(); !g.getFamilies().containsKey("@F" + xref + "@") && result
                .getXref() == null; xref++) {
            result.setXref("@F" + xref + "@");
            g.getFamilies().put(result.getXref(), result);
        }

        // Put the people in the Family record
        result.setHusband(father);
        result.setWife(mother);
        result.getChildren(true).addAll(Arrays.asList(children));

        // And add the family record to the people
        if (father != null) {
            FamilySpouse fams = new FamilySpouse();
            fams.setFamily(result);
            father.getFamiliesWhereSpouse(true).add(fams);
        }
        if (mother != null) {
            FamilySpouse fams = new FamilySpouse();
            fams.setFamily(result);
            mother.getFamiliesWhereSpouse(true).add(fams);
        }
        if (children != null) {
            for (Individual kid : children) {
                FamilyChild famc = new FamilyChild();
                famc.setFamily(result);
                kid.getFamiliesWhereChild(true).add(famc);
            }
        }

        return result;
    }

}
