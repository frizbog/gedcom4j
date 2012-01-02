package com.mattharrah.gedcom4j.validate;

import java.util.ArrayList;

import com.mattharrah.gedcom4j.AbstractCitation;
import com.mattharrah.gedcom4j.Association;
import com.mattharrah.gedcom4j.Individual;
import com.mattharrah.gedcom4j.IndividualAttribute;
import com.mattharrah.gedcom4j.IndividualEvent;
import com.mattharrah.gedcom4j.Submitter;

/**
 * A validator for an individual
 * 
 * @author frizbog1
 * 
 */
public class IndividualValidator extends AbstractValidator {

    /**
     * The individul being validated
     */
    private Individual individual;

    /**
     * Constructor
     * 
     * @param gedcomValidator
     *            the root validator
     * @param individual
     *            the individual being validated
     */
    public IndividualValidator(GedcomValidator gedcomValidator,
            Individual individual) {
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
                individual.aliases = new ArrayList<String>();
                addInfo("aliases collection for individual was null - rootValidator.autorepaired",
                        individual);
            } else {
                addError("aliases collection for individual is null",
                        individual);
            }
        } else {
            checkStringList("aliases", individual, false);
        }
    }

    /**
     * Validate the {@link Individual#associations} collection
     */
    private void checkAssociations() {
        if (individual.associations == null) {
            if (rootValidator.autorepair) {
                individual.associations = new ArrayList<Association>();
                addInfo("associations collection for individual was null - rootValidator.autorepaired",
                        individual);
            } else {
                addError("associations collection for individual is null",
                        individual);
            }
        } else {
            for (Association a : individual.associations) {
                if (a == null) {
                    addError(
                            "associations collection for individual contains null entry",
                            individual);
                } else {
                    checkRequiredString(a.associatedEntityType,
                            "associated entity type", a);
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
                addInfo("citations collection for individual was null - rootValidator.autorepaired",
                        individual);
            } else {
                addError("citations collection for individual is null",
                        individual);
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
                addInfo("attributes collection for individual was null - rootValidator.autorepaired",
                        individual);
            } else {
                addError("attributes collection for individual is null",
                        individual);
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
                addInfo("events collection for individual was null - rootValidator.autorepaired",
                        individual);
            } else {
                addError("events collection for individual is null", individual);
            }
        } else {
            for (IndividualEvent a : individual.events) {
                if (a.type == null) {
                    addError("Individual event requires a type", a);
                }
            }
        }
    }

    /**
     * Validate the two submitters collections:
     * {@link Individual#ancestorInterest} and
     * {@link Individual#descendantInterest}
     */
    private void checkSubmitters() {
        if (individual.ancestorInterest == null) {
            if (rootValidator.autorepair) {
                individual.ancestorInterest = new ArrayList<Submitter>();
                addInfo("ancestorInterest collection for individual was null - rootValidator.autorepaired",
                        individual);
            } else {
                addError("ancestorInterest collection for individual is null",
                        individual);
            }
        } else {
            for (Submitter submitter : individual.ancestorInterest) {
                new SubmitterValidator(rootValidator, submitter).validate();
            }
        }
        if (individual.descendantInterest == null) {
            if (rootValidator.autorepair) {
                individual.descendantInterest = new ArrayList<Submitter>();
                addInfo("descendantInterest collection for individual was null - rootValidator.autorepaired",
                        individual);
            } else {
                addError(
                        "descendantInterest collection for individual is null",
                        individual);
            }
        } else {
            for (Submitter submitter : individual.descendantInterest) {
                new SubmitterValidator(rootValidator, submitter).validate();
            }
        }

    }

}
