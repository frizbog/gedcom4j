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
package org.gedcom4j.validate;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.gedcom4j.model.FamilyChild;
import org.gedcom4j.model.Individual;
import org.gedcom4j.validate.Validator.Finding;

/**
 * Validator that finds individuals with surnames that don't match their parents'.
 * 
 * @author frizbog
 */
public class DifferentSurnamesThanParentsValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 7793203956142651668L;

    /**
     * Instantiates a new "different surnames than parents" validator.
     *
     * @param validator
     *            the validator that holds results
     */
    public DifferentSurnamesThanParentsValidator(Validator validator) {
        super(validator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        for (Individual i : getValidator().getGedcom().getIndividuals().values()) {
            if (i.getFamiliesWhereChild() == null || i.getFamiliesWhereChild().isEmpty()) {
                continue;
            }
            Set<String> personSurnames = i.getSurnames();
            Set<String> allParentSurnames = new TreeSet<>();
            Set<Individual> parents = new HashSet<>();
            for (FamilyChild fc : i.getFamiliesWhereChild()) {
                if (fc.getFamily().getHusband() != null) {
                    parents.add(fc.getFamily().getHusband());
                    allParentSurnames.addAll(fc.getFamily().getHusband().getSurnames());
                }
                if (fc.getFamily().getWife() != null) {
                    parents.add(fc.getFamily().getWife());
                    allParentSurnames.addAll(fc.getFamily().getWife().getSurnames());
                }
            }
            if (allParentSurnames.isEmpty()) {
                continue;
            }

            Set<String> commonSurnames = new TreeSet<>(allParentSurnames);
            commonSurnames.retainAll(personSurnames);
            if (commonSurnames.isEmpty()) {
                // Found a problem
                Finding newFinding = newFinding(i, Severity.WARNING, ProblemCode.SURNAMES_DONT_MATCH_PARENTS, "surnames");
                newFinding.getRelatedItems(true).addAll(parents);
            }
        }
    }

}
