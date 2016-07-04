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
package org.gedcom4j.validate;

import org.gedcom4j.model.*;

/**
 * A validator for an {@link Individual}. See {@link GedcomValidator} for usage information.
 * 
 * @author frizbog1
 * 
 */
class IndividualValidator extends AbstractValidator {

    /**
     * The individual being validated
     */
    private final Individual individual;

    /**
     * Constructor
     * 
     * @param gedcomValidator
     *            the root validator
     * @param individual
     *            the individual being validated
     */
    public IndividualValidator(GedcomValidator gedcomValidator, Individual individual) {
        rootValidator = gedcomValidator;
        this.individual = individual;
    }

    @Override
    protected void validate() {
        if (individual == null) {
            addError("Individual is null");
            return;
        }
        checkXref(individual);
        if (individual.getNames() == null) {
            if (rootValidator.autorepair) {
                individual.getNames(true).clear();
                rootValidator.addInfo("Individual " + individual.getXref() + " had no list of names - repaired", individual);
            } else {
                rootValidator.addError("Individual " + individual.getXref() + " has no list of names", individual);
            }
        } else {
            for (PersonalName pn : individual.getNames()) {
                new PersonalNameValidator(rootValidator, pn).validate();
            }
        }
        checkAliases();
        checkAssociations();
        checkCitations();
        checkIndividualAttributes();
        checkSubmitters();
        checkIndividualEvents();
    }

    /**
     * Validate the {@link Individual#aliases} collection
     * 
     */
    private void checkAliases() {
        if (individual.getAliases() == null) {
            if (rootValidator.autorepair) {
                individual.getAliases(true).clear();
                addInfo("aliases collection for individual was null - rootValidator.autorepaired", individual);
            } else {
                addError("aliases collection for individual is null", individual);
            }
        } else {
            checkStringTagList(individual.getAliases(), "aliases on individual", false);
        }
    }

    /**
     * Validate the {@link Individual#associations} collection
     */
    private void checkAssociations() {
        if (individual.getAssociations() == null) {
            if (rootValidator.autorepair) {
                individual.getAssociations(true).clear();
                addInfo("associations collection for individual was null - rootValidator.autorepaired", individual);
            } else {
                addError("associations collection for individual is null", individual);
            }
        } else {
            for (Association a : individual.getAssociations()) {
                if (a == null) {
                    addError("associations collection for individual contains null entry", individual);
                } else {
                    checkRequiredString(a.getAssociatedEntityType(), "associated entity type", a);
                    checkXref(a, "associatedEntityXref");
                }
            }
        }

    }

    /**
     * Validate the {@link Individual#citations} collection
     */
    private void checkCitations() {
        if (individual.getCitations() == null) {
            if (rootValidator.autorepair) {
                individual.getCitations(true).clear();
                addInfo("citations collection for individual was null - rootValidator.autorepaired", individual);
            } else {
                addError("citations collection for individual is null", individual);
            }
        } else {
            for (AbstractCitation c : individual.getCitations()) {
                new CitationValidator(rootValidator, c).validate();
            }
        }
    }

    /**
     * Validate the {@link Individual#attributes} collection
     */
    private void checkIndividualAttributes() {
        if (individual.getAttributes() == null) {
            if (rootValidator.autorepair) {
                individual.getAttributes(true).clear();
                addInfo("attributes collection for individual was null - rootValidator.autorepaired", individual);
            } else {
                addError("attributes collection for individual is null", individual);
            }
        } else {
            for (IndividualAttribute a : individual.getAttributes()) {
                if (a.type == null) {
                    addError("Individual attribute requires a type", a);
                }
            }
        }
    }

    /**
     * Validate the {@link Individual#events} collection
     */
    private void checkIndividualEvents() {
        if (individual.getEvents() == null) {
            if (rootValidator.autorepair) {
                individual.getEvents(true).clear();
                addInfo("events collection for individual was null - rootValidator.autorepaired", individual);
            } else {
                addError("events collection for individual is null", individual);
            }
        } else {
            for (IndividualEvent a : individual.getEvents()) {
                if (a.type == null) {
                    addError("Individual event requires a type", a);
                }
                new EventValidator(rootValidator, a).validate();
            }
        }
    }

    /**
     * Validate the two submitters collections: {@link Individual#ancestorInterest} and
     * {@link Individual#descendantInterest}
     */
    private void checkSubmitters() {
        if (individual.getAncestorInterest() == null) {
            if (rootValidator.autorepair) {
                individual.getAncestorInterest(true).clear();
                addInfo("ancestorInterest collection for individual was null - rootValidator.autorepaired", individual);
            } else {
                addError("ancestorInterest collection for individual is null", individual);
            }
        } else {
            for (Submitter submitter : individual.getAncestorInterest()) {
                new SubmitterValidator(rootValidator, submitter).validate();
            }
        }
        if (individual.getDescendantInterest() == null) {
            if (rootValidator.autorepair) {
                individual.getDescendantInterest(true).clear();
                addInfo("descendantInterest collection for individual was null - rootValidator.autorepaired", individual);
            } else {
                addError("descendantInterest collection for individual is null", individual);
            }
        } else {
            for (Submitter submitter : individual.getDescendantInterest()) {
                new SubmitterValidator(rootValidator, submitter).validate();
            }
        }

    }

}
