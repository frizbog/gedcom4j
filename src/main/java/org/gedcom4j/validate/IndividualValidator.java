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

import java.util.List;

import org.gedcom4j.Options;
import org.gedcom4j.model.Association;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualAttribute;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.model.Submitter;
import org.gedcom4j.validate.Validator.Finding;

/**
 * A validator for an {@link Individual}. See {@link Validator} for usage information.
 * 
 * @author frizbog1
 * 
 */
class IndividualValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -4569551561960734159L;

    /**
     * The individual being validated
     */
    private final Individual individual;

    /**
     * Constructor
     * 
     * @param validator
     *            the root validator
     * @param individual
     *            the individual being validated
     */
    IndividualValidator(Validator validator, Individual individual) {
        this.validator = validator;
        this.individual = individual;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        xrefMustBePresentAndWellFormed(individual);
        List<PersonalName> names = individual.getNames();
        if (names == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(individual, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, "names");
            initializeCollectionIfAllowed(vf);
        }
        if (names != null) {
            checkListOfModelElementsForDups(individual, "names");
            checkListOfModelElementsForNulls(individual, "names");
            for (PersonalName pn : names) {
                if (pn != null) {
                    new PersonalNameValidator(validator, pn).validate();
                }
            }
        }
        if (individual.getFamiliesWhereChild() == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(individual, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION,
                    "familiesWhereChild");
            initializeCollectionIfAllowed(vf);
        }
        if (individual.getFamiliesWhereChild() != null) {
            checkListOfModelElementsForDups(individual, "familiesWhereChild");
            checkListOfModelElementsForNulls(individual, "familiesWhereChild");
        }
        if (individual.getFamiliesWhereSpouse() != null) {
            checkListOfModelElementsForDups(individual, "familiesWhereSpouse");
            checkListOfModelElementsForNulls(individual, "familiesWhereSpouse");
        }
        checkAliases();
        checkAssociations();
        checkCitations(individual);
        checkIndividualAttributes();
        checkSubmitters();
        checkIndividualEvents();
    }

    /**
     * Validate the {@link Individual#aliases} collection
     * 
     */
    private void checkAliases() {
        if (individual.getAliases() == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(individual, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, "aliases");
            initializeCollectionIfAllowed(vf);
        }
        if (individual.getAliases() != null) {
            checkListOfModelElementsForDups(individual, "aliases");
            checkListOfModelElementsForNulls(individual, "aliases");
            checkStringList(individual, "aliases", false);
        }
    }

    /**
     * Validate the {@link Individual#associations} collection
     */
    private void checkAssociations() {
        if (individual.getAssociations() == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(individual, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, "collections");
            initializeCollectionIfAllowed(vf);
        }
        if (individual.getAssociations() != null) {
            checkListOfModelElementsForDups(individual, "associations");
            checkListOfModelElementsForNulls(individual, "associations");
            for (Association a : individual.getAssociations()) {
                mustHaveValue(a, "associatedEntityType");
                checkAlternateXref(a, "associatedEntityXref");
            }
        }

    }

    /**
     * Validate the {@link Individual#attributes} collection
     */
    private void checkIndividualAttributes() {
        if (individual.getAttributes() == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(individual, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, "attributs");
            initializeCollectionIfAllowed(vf);
        }
        if (individual.getAttributes() != null) {
            checkListOfModelElementsForDups(individual, "attributes");
            checkListOfModelElementsForNulls(individual, "attributes");
            for (IndividualAttribute a : individual.getAttributes()) {
                new IndividualAttributeValidator(validator, a).validate();
            }
        }
    }

    /**
     * Validate the {@link Individual#events} collection
     */
    private void checkIndividualEvents() {
        if (individual.getEvents() == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(individual, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, "events");
            initializeCollectionIfAllowed(vf);
        }
        if (individual.getEvents() != null) {
            checkListOfModelElementsForDups(individual, "events");
            checkListOfModelElementsForNulls(individual, "events");
            for (IndividualEvent a : individual.getEvents()) {
                new EventValidator(validator, a).validate();
            }
        }
    }

    /**
     * Validate the two submitters collections: {@link Individual#ancestorInterest} and {@link Individual#descendantInterest}
     */
    private void checkSubmitters() {
        if (individual.getAncestorInterest() == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(individual, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, "ancestorInterest");
            initializeCollectionIfAllowed(vf);
        }
        if (individual.getAncestorInterest() != null) {
            for (Submitter submitter : individual.getAncestorInterest()) {
                new SubmitterValidator(validator, submitter).validate();
            }
        }
        if (individual.getDescendantInterest() == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(individual, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION,
                    "descendantInterest");
            initializeCollectionIfAllowed(vf);
        }
        if (individual.getDescendantInterest() != null) {
            for (Submitter submitter : individual.getDescendantInterest()) {
                new SubmitterValidator(validator, submitter).validate();
            }
        }

    }

}
