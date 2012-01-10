package com.mattharrah.gedcom4j.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.mattharrah.gedcom4j.Gedcom;
import com.mattharrah.gedcom4j.Individual;

/**
 * A class to validate the contents of a {@link Gedcom} structure
 * 
 * @author frizbog1
 */
public class GedcomValidator extends AbstractValidator {

    /**
     * Will the most simple, obvious, non-destructive errors be automatically
     * fixed? This includes things like creating empty collections where one is
     * expected but only a null reference exists.
     */
    public boolean autorepair = true;

    /**
     * The findings from validation
     */
    public List<GedcomValidationFinding> findings = new ArrayList<GedcomValidationFinding>();

    /**
     * The gedcom structure being validated
     */
    protected Gedcom gedcom = null;

    /**
     * Constructor
     * 
     * @param gedcom
     *            the gedcom structure being validated
     */
    public GedcomValidator(Gedcom gedcom) {
        this.gedcom = gedcom;
        rootValidator = this;
    }

    /**
     * Validate the gedcom file
     */
    @Override
    public void validate() {
        if (gedcom == null) {
            addError("gedcom structure is null");
            return;
        }
        validateIndividuals();
    }

    /**
     * Validate the {@link Gedcom#individuals} collection
     */
    void validateIndividuals() {
        if (gedcom.individuals == null) {
            if (autorepair) {
                gedcom.individuals = new HashMap<String, Individual>();
                addInfo("Individuals collection was null - autorepaired",
                        gedcom);
            } else {
                addError("Individuals collection is null", gedcom);
                return;
            }
        }
        for (Entry<String, Individual> e : gedcom.individuals.entrySet()) {
            if (e.getKey() == null) {
                addError("Entry in individuals collection has null key", e);
                return;
            }
            if (e.getValue() == null) {
                addError("Entry in individuals collection has null value", e);
                return;
            }
            if (!e.getKey().equals(e.getValue().xref)) {
                addError(
                        "Entry in individuals collection is not keyed by the individual's xref",
                        e);
                return;
            }
            new IndividualValidator(this, e.getValue()).validate();
        }
    }

}
