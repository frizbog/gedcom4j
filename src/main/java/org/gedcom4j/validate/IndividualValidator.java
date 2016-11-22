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

import org.gedcom4j.model.Association;
import org.gedcom4j.model.FamilyChild;
import org.gedcom4j.model.FamilySpouse;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualAttribute;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.LdsIndividualOrdinance;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.model.Submitter;
import org.gedcom4j.model.enumerations.RestrictionNoticeType;

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
        super(validator);
        this.individual = individual;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        xrefMustBePresentAndWellFormed(individual);
        checkUninitializedCollection(individual, "names");
        List<PersonalName> names = individual.getNames();
        if (names != null) {
            checkListOfModelElementsForDups(individual, "names");
            checkListOfModelElementsForNulls(individual, "names");
            for (PersonalName pn : names) {
                if (pn != null) {
                    new PersonalNameValidator(getValidator(), pn).validate();
                }
            }
        }
        checkUninitializedCollection(individual, "familiesWhereChild");
        if (individual.getFamiliesWhereChild() != null) {
            checkListOfModelElementsForDups(individual, "familiesWhereChild");
            checkListOfModelElementsForNulls(individual, "familiesWhereChild");
            for (FamilyChild fc : individual.getFamiliesWhereChild()) {
                new FamilyChildValidator(getValidator(), fc).validate();
            }
        }
        checkUninitializedCollection(individual, "familiesWhereSpouse");
        if (individual.getFamiliesWhereSpouse() != null) {
            checkListOfModelElementsForDups(individual, "familiesWhereSpouse");
            checkListOfModelElementsForNulls(individual, "familiesWhereSpouse");
            for (FamilySpouse fs : individual.getFamiliesWhereSpouse()) {
                new FamilySpouseValidator(getValidator(), fs).validate();
            }
        }
        if (individual.getRestrictionNotice() != null) {
            mustBeInEnumIfSpecified(RestrictionNoticeType.class, individual, "restrictionNotice");
        }
        checkAliases();
        checkAssociations();
        checkCitations(individual);
        checkIndividualAttributes();
        checkSubmitters();
        checkIndividualEvents();
        checkLdsIndividualOrdinances();
        checkNotes(individual);
        checkCustomFacts(individual);
        checkEmails(individual);
        checkFaxNumbers(individual);
        checkPhoneNumbers(individual);
        checkWwwUrls(individual);
    }

    /**
     * Validate the {@link Individual#aliases} collection
     * 
     */
    private void checkAliases() {
        checkUninitializedCollection(individual, "aliases");
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
        checkUninitializedCollection(individual, "associations");
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
        checkUninitializedCollection(individual, "attributes");
        if (individual.getAttributes() != null) {
            checkListOfModelElementsForDups(individual, "attributes");
            checkListOfModelElementsForNulls(individual, "attributes");
            for (IndividualAttribute a : individual.getAttributes()) {
                new IndividualAttributeValidator(getValidator(), a).validate();
            }
        }
    }

    /**
     * Validate the {@link Individual#events} collection
     */
    private void checkIndividualEvents() {
        checkUninitializedCollection(individual, "events");
        if (individual.getEvents() != null) {
            checkListOfModelElementsForDups(individual, "events");
            checkListOfModelElementsForNulls(individual, "events");
            for (IndividualEvent a : individual.getEvents()) {
                new EventValidator(getValidator(), a).validate();
            }
        }
    }

    /**
     * Validate the LdsIndividualOrinances
     */
    private void checkLdsIndividualOrdinances() {
        checkUninitializedCollection(individual, "ldsIndividualOrdinances");
        if (individual.getLdsIndividualOrdinances() != null) {
            checkListOfModelElementsForDups(individual, "ldsIndividualOrdinances");
            checkListOfModelElementsForNulls(individual, "ldsIndividualOrdinances");
            for (LdsIndividualOrdinance o : individual.getLdsIndividualOrdinances()) {
                new LdsIndividualOrdinanceValidator(getValidator(), o).validate();
            }
        }
    }

    /**
     * Validate the two submitters collections: {@link Individual#ancestorInterest} and {@link Individual#descendantInterest}
     */
    private void checkSubmitters() {
        checkUninitializedCollection(individual, "ancestorInterest");
        if (individual.getAncestorInterest() != null) {
            for (Submitter submitter : individual.getAncestorInterest()) {
                new SubmitterValidator(getValidator(), submitter).validate();
            }
        }
        checkUninitializedCollection(individual, "descendantInterest");
        if (individual.getDescendantInterest() != null) {
            for (Submitter submitter : individual.getDescendantInterest()) {
                new SubmitterValidator(getValidator(), submitter).validate();
            }
        }

    }

}
