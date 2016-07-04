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

import java.util.ArrayList;

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
        if (individual.names == null) {
            if (rootValidator.autorepair) {
                individual.names = new ArrayList<PersonalName>();
                rootValidator.addInfo("Individual " + individual.getXref() + " had no list of names - repaired", individual);
            } else {
                rootValidator.addError("Individual " + individual.getXref() + " has no list of names", individual);
            }
        } else {
            for (PersonalName pn : individual.names) {
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
        if (individual.aliases == null) {
            if (rootValidator.autorepair) {
                individual.aliases = new ArrayList<StringWithCustomTags>();
                addInfo("aliases collection for individual was null - rootValidator.autorepaired", individual);
            } else {
                addError("aliases collection for individual is null", individual);
            }
        } else {
            checkStringTagList(individual.aliases, "aliases on individual", false);
        }
    }

    /**
     * Validate the {@link Individual#associations} collection
     */
    private void checkAssociations() {
        if (individual.associations == null) {
            if (rootValidator.autorepair) {
                individual.associations = new ArrayList<Association>();
                addInfo("associations collection for individual was null - rootValidator.autorepaired", individual);
            } else {
                addError("associations collection for individual is null", individual);
            }
        } else {
            for (Association a : individual.associations) {
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
        if (individual.citations == null) {
            if (rootValidator.autorepair) {
                individual.citations = new ArrayList<AbstractCitation>();
                addInfo("citations collection for individual was null - rootValidator.autorepaired", individual);
            } else {
                addError("citations collection for individual is null", individual);
            }
        } else {
            for (AbstractCitation c : individual.citations) {
                new CitationValidator(rootValidator, c).validate();
            }
        }
    }

    /**
     * Validate the {@link Individual#attributes} collection
     */
    private void checkIndividualAttributes() {
        if (individual.attributes == null) {
            if (rootValidator.autorepair) {
                individual.attributes = new ArrayList<IndividualAttribute>();
                addInfo("attributes collection for individual was null - rootValidator.autorepaired", individual);
            } else {
                addError("attributes collection for individual is null", individual);
            }
        } else {
            for (IndividualAttribute a : individual.attributes) {
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
        if (individual.events == null) {
            if (rootValidator.autorepair) {
                individual.events = new ArrayList<IndividualEvent>();
                addInfo("events collection for individual was null - rootValidator.autorepaired", individual);
            } else {
                addError("events collection for individual is null", individual);
            }
        } else {
            for (IndividualEvent a : individual.events) {
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
        if (individual.ancestorInterest == null) {
            if (rootValidator.autorepair) {
                individual.ancestorInterest = new ArrayList<Submitter>();
                addInfo("ancestorInterest collection for individual was null - rootValidator.autorepaired", individual);
            } else {
                addError("ancestorInterest collection for individual is null", individual);
            }
        } else {
            for (Submitter submitter : individual.ancestorInterest) {
                new SubmitterValidator(rootValidator, submitter).validate();
            }
        }
        if (individual.descendantInterest == null) {
            if (rootValidator.autorepair) {
                individual.descendantInterest = new ArrayList<Submitter>();
                addInfo("descendantInterest collection for individual was null - rootValidator.autorepaired", individual);
            } else {
                addError("descendantInterest collection for individual is null", individual);
            }
        } else {
            for (Submitter submitter : individual.descendantInterest) {
                new SubmitterValidator(rootValidator, submitter).validate();
            }
        }

    }

}
