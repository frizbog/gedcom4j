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

import org.gedcom4j.model.Gedcom;

/**
 * <p>
 * Validates {@link Gedcom} objects.
 * </p>
 * <p>
 * Does a deep traversal over the items in the {@link Gedcom} structure and checks them for problems, errors, etc, which are
 * represented as {@link ValidationFinding} objects. These objects contain problem codes, descriptions, severity ratings, and
 * references to the objects that have problems.
 * <p>
 * <p>
 * Typical usage is to instantiate a Validator with the Gedcom being validated, call the validate() method, then call the
 * getResults()
 * </p>
 * 
 * @author frizbog
 * @since 4.0.0
 */
public class Validator {

    /**
     * Built-in {@link AutoRepairResponder} implementation that allows everything to be auto-repaired.
     */
    public static final AutoRepairResponder AUTO_REPAIR_ALL = new AutoRepairResponder() {
        /**
         * {@inheritDoc}
         */
        @Override
        public boolean mayRepair(ValidationFinding repairableValidationFinding) {
            return true;
        }
    };

    /**
     * Built-in {@link AutoRepairResponder} implementation that forbids all auto-repairs.
     */
    public static final AutoRepairResponder AUTO_REPAIR_NONE = new AutoRepairResponder() {
        /**
         * {@inheritDoc}
         */
        @Override
        public boolean mayRepair(ValidationFinding repairableValidationFinding) {
            return false;
        }
    };

    /** The gedcom being validated. */
    private final Gedcom gedcom;

    /** The results. */
    private final ValidationResults results = new ValidationResults();

    /**
     * The responder that determines whether the validator is to be allowed to auto-repair a finding. Default is the more
     * conservative value of allowing no auto-repairs.
     */
    private AutoRepairResponder autoRepairResponder = AUTO_REPAIR_NONE;

    /**
     * Instantiates a new validator.
     *
     * @param gedcom
     *            the gedcom being validated
     */
    public Validator(Gedcom gedcom) {
        this.gedcom = gedcom;
    }

    /**
     * Gets the auto repair responder.
     *
     * @return the auto repair responder
     */
    public AutoRepairResponder getAutoRepairResponder() {
        return autoRepairResponder;
    }

    /**
     * Get the gedcom
     * 
     * @return the gedcom
     */
    public Gedcom getGedcom() {
        return gedcom;
    }

    /**
     * Get the results
     * 
     * @return the results
     */
    public ValidationResults getResults() {
        return results;
    }

    /**
     * Sets the auto repair responder.
     *
     * @param autoRepairResponder
     *            the new auto repair responder. Set to null if all auto-repair is to be disabled. You can also use
     */
    public void setAutoRepairResponder(AutoRepairResponder autoRepairResponder) {
        this.autoRepairResponder = autoRepairResponder;
    }

    /**
     * Validate the gedcom
     */
    public void validate() {
        // TODO actually validate something
    }

}
