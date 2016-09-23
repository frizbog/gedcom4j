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

import org.gedcom4j.model.Family;
import org.gedcom4j.model.FamilyChild;
import org.gedcom4j.model.Individual;
import org.gedcom4j.relationship.Relationship;
import org.gedcom4j.relationship.RelationshipCalculator;
import org.gedcom4j.validate.Validator.Finding;

/**
 * Validator that finds circular ancestral relationships
 * 
 * @author frizbog
 */
public class CircularAncestryValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 8632496421513471225L;

    /**
     * @param validator
     *            the main {@link Validator} that orchestrates validation, tracks results,etc.
     */
    public CircularAncestryValidator(Validator validator) {
        super(validator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        RelationshipCalculator rc = new RelationshipCalculator();
        for (Individual i : getValidator().getGedcom().getIndividuals().values()) {
            if (i.getFamiliesWhereChild() != null) {
                for (FamilyChild fc : i.getFamiliesWhereChild()) {
                    Family f = fc.getFamily();

                    // Check father's side
                    Individual father = (f.getHusband() == null ? null : f.getHusband().getIndividual());
                    if (father != null && father.getAncestors().contains(i)) {
                        Finding finding = newFinding(i, Severity.ERROR, ProblemCode.CIRCULAR_ANCESTRAL_RELATIONSHIP,
                                "familiesWhereChild");
                        rc.calculateRelationships(father, i, false);
                        for (Relationship r : rc.getRelationshipsFound()) {
                            finding.getRelatedItems(true).add(r.getIndividual2());
                        }
                    }

                    // Check mother's side
                    Individual mother = (f.getWife() == null ? null : f.getWife().getIndividual());
                    if (mother != null && mother.getAncestors().contains(i)) {
                        Finding finding = newFinding(i, Severity.ERROR, ProblemCode.CIRCULAR_ANCESTRAL_RELATIONSHIP,
                                "familiesWhereChild");
                        rc.calculateRelationships(mother, i, false);
                        for (Relationship r : rc.getRelationshipsFound()) {
                            finding.getRelatedItems(true).add(r.getIndividual2());
                        }
                    }
                }
            }

        }
    }
}
