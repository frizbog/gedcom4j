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

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.gedcom4j.Options;
import org.gedcom4j.exception.ValidationException;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.GedcomVersion;
import org.gedcom4j.model.Header;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.ModelElement;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.NoteRecord;
import org.gedcom4j.model.Repository;
import org.gedcom4j.model.StringWithCustomFacts;
import org.gedcom4j.model.Submission;
import org.gedcom4j.model.Submitter;
import org.gedcom4j.model.Trailer;
import org.gedcom4j.model.enumerations.SupportedVersion;

/**
 * <p>
 * Validates {@link Gedcom} object graphs.
 * </p>
 * <p>
 * Does a deep traversal over the items in the {@link Gedcom} structure and checks them for problems, errors, etc, which are
 * represented as {@link Finding} objects. These objects contain problem codes, descriptions, severity ratings, and references to
 * the objects that have problems.
 * </p>
 * <p>
 * Typical usage is to instantiate a Validator with the Gedcom being validated, call the {@link #validate()} method, then examine
 * the {@link ValidationResults} object obtained from the {@link #getResults()} method.
 * </p>
 * <p>
 * Users can extend the validation process by writing custom validators and registering them with this validator. To do this, write
 * a class that extends {@link AbstractValidator} and add it to the {@link #supplementaryValidators} collection. When writing a new
 * validator, implement its {@link AbstractValidator#validate()} method which examines the {@link Gedcom} being validated, and for
 * anything that's problematic found, call {@link AbstractValidator#newFinding(ModelElement, Severity, ProblemCode, String)} from
 * there. When the {@link #validate()} method runs on this class, your validator will be called (along with the others in the
 * collection) and your results will be added to the full set of results.
 * </p>
 * 
 * @author frizbog
 * @since 4.0.0
 */
@SuppressWarnings({ "PMD.GodClass", "PMD.TooManyMethods", "PMD.CouplingBetweenObjects" })
public class Validator implements Serializable {

    /**
     * Represents something of interest found by the validation module.
     * 
     * @author frizbog
     */
    public static class Finding implements Serializable {
        /**
         * Serial Version UID
         */
        private static final long serialVersionUID = 2148459753130687833L;

        /**
         * The field name in {@link #itemOfConcern} that had the finding. Optional. If populated, reflection can be used on the
         * {@link #itemOfConcern} object to get specific field values.
         */
        private String fieldNameOfConcern;

        /**
         * The primary item that had the finding in it
         */
        private ModelElement itemOfConcern;

        /**
         * The code for the problem
         */
        private int problemCode;

        /**
         * The description of the problem
         */
        private String problemDescription;

        /**
         * Items that are related to the item of concern that are contributing to or are somehow related to the problem.
         */
        private List<ModelElement> relatedItems = Options.isCollectionInitializationEnabled() ? new ArrayList<ModelElement>(0)
                : null;

        /**
         * The repairs made automatically by the validator
         */
        private List<AutoRepair> repairs = Options.isCollectionInitializationEnabled() ? new ArrayList<AutoRepair>(0) : null;

        /** The severity. */
        private Severity severity;

        /**
         * A stack trace of where in the validation framework the finding originated from
         */
        private final String stackTrace;

        /**
         * Default constructor
         */
        Finding() {
            // Default constructor does nothing
            Writer result = new StringWriter();
            PrintWriter printWriter = new PrintWriter(result);
            new ValidationException().printStackTrace(printWriter);
            stackTrace = result.toString();
        }

        /**
         * Convenience method to add an auto-repair to the finding
         * 
         * @param autoRepair
         *            the auto-repair object to add
         */
        public void addRepair(AutoRepair autoRepair) {
            getRepairs(true).add(autoRepair);
        }

        /**
         * Get the fieldNameOfConcern
         * 
         * @return the fieldNameOfConcern
         */
        public String getFieldNameOfConcern() {
            return fieldNameOfConcern;
        }

        /**
         * Get the itemOfConcern
         * 
         * @return the itemOfConcern
         */
        public ModelElement getItemOfConcern() {
            return itemOfConcern;
        }

        /**
         * Get the problemCode
         * 
         * @return the problemCode
         */
        public int getProblemCode() {
            return problemCode;
        }

        /**
         * Get the problemDescription
         * 
         * @return the problemDescription
         */
        public String getProblemDescription() {
            return problemDescription;
        }

        /**
         * Get the relatedItems
         * 
         * @return the relatedItems
         */
        public List<ModelElement> getRelatedItems() {
            return relatedItems;
        }

        /**
         * Get the relatedItems
         * 
         * @param initializeIfNeeded
         *            initialize the collection if needed prior to returning the value
         * 
         * @return the relatedItems
         */
        public List<ModelElement> getRelatedItems(boolean initializeIfNeeded) {
            if (initializeIfNeeded && relatedItems == null) {
                relatedItems = new ArrayList<>();
            }
            return relatedItems;
        }

        /**
         * Get the repairs
         * 
         * @return the repairs
         */
        public List<AutoRepair> getRepairs() {
            return repairs;
        }

        /**
         * Get the repairs
         * 
         * @param initializeIfNeeded
         *            initialize the collection if needed before returning
         * 
         * @return the repairs
         */
        public List<AutoRepair> getRepairs(boolean initializeIfNeeded) {
            if (initializeIfNeeded && repairs == null) {
                repairs = new ArrayList<>(0);
            }
            return repairs;
        }

        /**
         * Get the severity
         * 
         * @return the severity
         */
        public Severity getSeverity() {
            return severity;
        }

        /**
         * Get the stackTrace
         * 
         * @return the stackTrace
         */
        public String getStackTrace() {
            return stackTrace;
        }

        /**
         * Set the user-defined problem code
         * 
         * @param problemCode
         *            the problem code to set. Must be 1000 or higher. Values below 1000 are reserved for gedcom4j. Values of 1000
         *            or higher are for user-defined validator problems.
         */
        public void setProblemCode(int problemCode) {
            if (problemCode < 0) {
                throw new IllegalArgumentException("Problem code must be a positive integer - received " + problemCode);
            }
            if (problemCode < 1000) {
                throw new IllegalArgumentException("Values under 1000 are reserved for gedcom4j - received " + problemCode);
            }
            this.problemCode = problemCode;
        }

        /**
         * Set the user-defined problem description.
         * 
         * @param problemDescription
         *            the user-defined problemDescription to set
         */
        public void setProblemDescription(String problemDescription) {
            if (problemCode < 1000) {
                throw new IllegalArgumentException(
                        "Cannot set descriptions for problems with codes under 1000, which are reserved for gedcom4j");
            }
            this.problemDescription = problemDescription;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(50);
            builder.append("Finding [");
            if (fieldNameOfConcern != null) {
                builder.append("fieldNameOfConcern=");
                builder.append(fieldNameOfConcern);
                builder.append(", ");
            }
            if (itemOfConcern != null) {
                builder.append("itemOfConcern=");
                builder.append(itemOfConcern);
                builder.append(", ");
            }
            if (severity != null) {
                builder.append("severity=");
                builder.append(severity);
                builder.append(", ");
            }
            builder.append("problemCode=");
            builder.append(problemCode);
            builder.append(", ");
            if (problemDescription != null) {
                builder.append("problemDescription=");
                builder.append(problemDescription);
                builder.append(", ");
            }
            if (relatedItems != null) {
                builder.append("relatedItems=");
                builder.append(relatedItems);
                builder.append(", ");
            }
            if (repairs != null) {
                builder.append("repairs=");
                builder.append(repairs);
            }

            builder.append("]");
            return builder.toString();
        }

        /**
         * Set the fieldNameOfConcern. Deliberately package-private. Outside the validation framework, this field should not be
         * changeable.
         * 
         * @param fieldNameOfConcern
         *            the fieldNameOfConcern to set
         */
        void setFieldNameOfConcern(String fieldNameOfConcern) {
            this.fieldNameOfConcern = fieldNameOfConcern;
        }

        /**
         * Set the item of concern. Deliberately package-private. Outside the validation framework, this field should not be
         * changeable.
         * 
         * @param itemOfConcern
         *            the item of concern
         */
        void setItemOfConcern(ModelElement itemOfConcern) {
            this.itemOfConcern = itemOfConcern;
        }

        /**
         * Set a problem code and description from within gedcom4j. Deliberately package-private to prevent usage outside the
         * gedcom4j framework.
         * 
         * @param pc
         *            the problem code enum entry to use for the problem code and description
         */
        void setProblem(ProblemCode pc) {
            problemCode = pc.getCode();
            problemDescription = pc.getDescription();
        }

        /**
         * Set the related items. Deliberately package-private. Outside the validation framework, this field should not be
         * changeable.
         * 
         * @param relatedItems
         *            the related items to set
         */
        void setRelatedItems(List<ModelElement> relatedItems) {
            this.relatedItems = relatedItems;
        }

        /**
         * Set the repairs. Deliberately package-private. Outside the validation framework, this field should not be changeable.
         * 
         * @param repairs
         *            the collection of repairs
         */
        void setRepairs(List<AutoRepair> repairs) {
            this.repairs = repairs;
        }

        /**
         * Set the severity. Deliberately package-private. Outside the validation framework, this field should not be changeable.
         * 
         * @param severity
         *            the severity to set. Required.
         * @throws IllegalArgumentException
         *             if severity passed in is null
         */
        void setSeverity(Severity severity) {
            if (severity == null) {
                throw new IllegalArgumentException("severity is a required argument.");
            }
            this.severity = severity;
        }

    }

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -5828898693076973667L;

    /**
     * Built-in {@link AutoRepairResponder} implementation that allows everything to be auto-repaired.
     */
    public static final AutoRepairResponder AUTO_REPAIR_ALL = new AutoRepairResponder() {
        /**
         * Serial Version UID
         */
        private static final long serialVersionUID = -7286303303501153069L;

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean mayRepair(Finding repairableValidationFinding) {
            return true;
        }
    };

    /**
     * Built-in {@link AutoRepairResponder} implementation that forbids all auto-repairs.
     */
    public static final AutoRepairResponder AUTO_REPAIR_NONE = new AutoRepairResponder() {
        /**
         * Serial Version UID
         */
        private static final long serialVersionUID = -4162579589370187101L;

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean mayRepair(Finding repairableValidationFinding) {
            return false;
        }
    };

    /**
     * The responder that determines whether the validator is to be allowed to auto-repair a finding. Default is the more
     * conservative value of allowing no auto-repairs.
     */
    private AutoRepairResponder autoRepairResponder = AUTO_REPAIR_NONE;

    /** The gedcom being validated. */
    private final Gedcom gedcom;

    /** The results. */
    private final ValidationResults results = new ValidationResults();

    /**
     * The extra validators. Callers may declare their own validators and register them to be executied
     */
    @SuppressWarnings("checkstyle:WhitespaceAround")
    private final Set<Class<? extends AbstractValidator>> supplementaryValidators = new HashSet<>();

    /**
     * Is the gedcom being validated a version 5.5.1 file? Defaults to true unless we see a version of 5.5 in the file.
     */
    private boolean v551 = true;

    /**
     * Instantiates a new validator.
     *
     * @param gedcom
     *            the gedcom being validated. Required.
     * @throws IllegalArgumentException
     *             if a null Gedcom is passed in.
     */
    @SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
    public Validator(Gedcom gedcom) {
        if (gedcom == null) {
            throw new IllegalArgumentException("gedcom is a required argument");
        }
        this.gedcom = gedcom;

        determineGedcomSpecVersion(gedcom);

        supplementaryValidators.add(DifferentSurnamesThanParentsValidator.class);
        supplementaryValidators.add(BirthsToYoungParentsValidator.class);
        supplementaryValidators.add(BirthsToOldParentsValidator.class);
        supplementaryValidators.add(MarriedAtYoungAgeValidator.class);
        supplementaryValidators.add(FutureBirthOrDeathValidator.class);
        supplementaryValidators.add(MaleWivesFemaleHusbandsValidator.class);
        supplementaryValidators.add(CouplesWithLargeAgeDifferencesValidator.class);
        supplementaryValidators.add(BornBeforeAncestorsValidator.class);
        supplementaryValidators.add(MultipleChildrenWithSameGivenNameValidator.class);
        supplementaryValidators.add(CircularAncestryValidator.class);
        supplementaryValidators.add(SimultaneousBirthsInMultipleLocationsValidator.class);
        supplementaryValidators.add(QuadrupletsAndMoreValidator.class);
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
     * <p>
     * Get the supplementary validators.
     * </p>
     * <p>
     * This collection comes pre-populated with a variety of built-in validators. Users can add their own custom
     * {@link AbstractValidator} implementations to this collection and have them executed at validation time. Users can also remove
     * any supplemental validator they do not want to run during validation.
     * </p>
     * 
     * @return the supplementary validators collection.
     */
    @SuppressWarnings("checkstyle:WhitespaceAround")
    public Set<Class<? extends AbstractValidator>> getSupplementaryValidators() {
        return supplementaryValidators;
    }

    /**
     * Create a finding - automatically adds to the results.
     * 
     * @param itemOfConcern
     *            the item of concern. Required.
     * @param severity
     *            the severity. Required.
     * @param problemCode
     *            the problem code. Required.
     * @param fieldNameOfConcern
     *            the name of the field that has a problmatic value. Optional, but if supplied it needs to exist on the item of
     *            concern as a field
     * @throws IllegalArgumentException
     *             if any of the arguments are null
     * @return the finding just created and added to the results
     */
    public Finding newFinding(ModelElement itemOfConcern, Severity severity, ProblemCode problemCode, String fieldNameOfConcern) {
        if (itemOfConcern == null) {
            throw new IllegalArgumentException("itemOfConcern is a required argument.");
        }
        if (severity == null) {
            throw new IllegalArgumentException("severity is a required argument.");
        }
        if (problemCode == null) {
            throw new IllegalArgumentException("problemCode is a required argument.");
        }
        Finding f = new Finding();
        f.itemOfConcern = itemOfConcern;
        f.severity = severity;
        f.problemCode = problemCode.getCode();
        f.problemDescription = problemCode.getDescription();
        f.fieldNameOfConcern = fieldNameOfConcern;
        results.add(f);
        return f;
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
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Validator [");
        if (results != null) {
            builder.append("results=");
            builder.append(results);
            builder.append(", ");
        }
        if (autoRepairResponder != null) {
            builder.append("autoRepairResponder=");
            if (autoRepairResponder == AUTO_REPAIR_ALL) {
                builder.append("AUTO_REPAIR_ALL");
            } else if (autoRepairResponder == AUTO_REPAIR_NONE) {
                builder.append("AUTO_REPAIR_NONE");
            } else {
                builder.append(autoRepairResponder);
            }
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Validate the gedcom
     */
    @SuppressWarnings("checkstyle:WhitespaceAround")
    public void validate() {
        results.clear();
        checkHeader();
        checkSubmission();
        checkFamilies();
        checkIndividuals();
        checkMultimedia();
        checkNotes();
        checkRepositories();
        checkNotes();
        checkSubmitters();
        if (gedcom.getTrailer() == null) {
            Finding vf = newFinding(gedcom, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, "trailer");
            if (mayRepair(vf)) {
                gedcom.setTrailer(new Trailer());
                vf.addRepair(new AutoRepair(null, new Trailer()));
            }
        }
        for (Class<? extends AbstractValidator> avc : supplementaryValidators) {
            try {
                AbstractValidator av = avc.getConstructor(Validator.class).newInstance(this);
                av.validate();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException | SecurityException e) {
                throw new ValidationException("Unable to instantiate and invoke custom validator " + avc.getName(), e);
            }
        }
    }

    /**
     * Get whether the file being validated is a 5.5.1 file (and thus should be validated to the 5.5.1 spec)
     * 
     * @return whether the file being validated is a 5.5.1 file (and thus should be validated to the 5.5.1 spec)
     */
    protected boolean isV551() {
        return v551;
    }

    /**
     * Check individuals.
     */
    void checkIndividuals() {
        for (Entry<String, Individual> entry : gedcom.getIndividuals().entrySet()) {
            if (entry.getValue() == null || entry.getKey() == null) {
                Finding vf = newFinding(gedcom, Severity.ERROR, ProblemCode.LIST_WITH_NULL_VALUE, "individuals");
                if (mayRepair(vf)) {
                    vf.addRepair(new AutoRepair(null, null));
                    gedcom.getIndividuals().remove(entry.getKey());
                }
            } else {
                new IndividualValidator(this, entry.getValue()).validate();
            }
        }
    }

    /**
     * Check if the finding can be auto-repaired. Delegates to the registered auto-repair responder, if any.
     * 
     * @param validationFinding
     *            the validation finding
     * @return true if the finding may be auto-repaired
     */
    boolean mayRepair(Finding validationFinding) {
        if (autoRepairResponder != null) {
            return autoRepairResponder.mayRepair(validationFinding);
        }
        return false;
    }

    /**
     * Check families.
     */
    private void checkFamilies() {
        for (Entry<String, Family> entry : gedcom.getFamilies().entrySet()) {
            if (entry.getValue() == null || entry.getKey() == null) {
                Finding vf = newFinding(gedcom, Severity.ERROR, ProblemCode.LIST_WITH_NULL_VALUE, "submitter");
                if (mayRepair(vf)) {
                    vf.addRepair(new AutoRepair(null, null));
                    gedcom.getFamilies().remove(entry.getKey());
                }
            } else {
                new FamilyValidator(this, entry.getValue()).validate();
            }
        }
    }

    /**
     * Check the header
     */
    private void checkHeader() {
        if (gedcom.getHeader() == null) {
            Header header = new Header();
            gedcom.setHeader(header);
        }
        new HeaderValidator(this, gedcom.getHeader()).validate();
    }

    /**
     * Check multimedia.
     */
    private void checkMultimedia() {
        for (Entry<String, Multimedia> entry : gedcom.getMultimedia().entrySet()) {
            if (entry.getValue() == null || entry.getKey() == null) {
                Finding vf = newFinding(gedcom, Severity.ERROR, ProblemCode.LIST_WITH_NULL_VALUE, "multimedia");
                if (mayRepair(vf)) {
                    vf.addRepair(new AutoRepair(null, null));
                    gedcom.getMultimedia().remove(entry.getKey());
                }
            } else {
                new MultimediaValidator(this, entry.getValue()).validate();
            }
        }
    }

    /**
     * Check notes
     */
    private void checkNotes() {
        for (NoteRecord note : gedcom.getNotes().values()) {
            if (note == null) {
                newFinding(gedcom, Severity.ERROR, ProblemCode.LIST_WITH_NULL_VALUE, "notes");
                continue;
            }
            // Root level notes should have xrefs
            if (!isSpecified(note.getXref())) {
                newFinding(note, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, "xref");
            }
            new NoteRecordValidator(this, note).validate();
        }
    }

    /**
     * Check all the repositories in the gedcom
     */
    private void checkRepositories() {
        for (Entry<String, Repository> entry : gedcom.getRepositories().entrySet()) {
            if (entry.getValue() == null || entry.getKey() == null) {
                Finding vf = newFinding(gedcom, Severity.ERROR, ProblemCode.LIST_WITH_NULL_VALUE, "repositories");
                if (mayRepair(vf)) {
                    vf.addRepair(new AutoRepair(null, null));
                    gedcom.getRepositories().remove(entry.getKey());
                }
            } else {
                new RepositoryValidator(this, entry.getValue()).validate();
            }
        }
    }

    /**
     * Check submission
     */
    private void checkSubmission() {
        if (gedcom.getSubmission() != null) {
            new SubmissionValidator(this, gedcom.getSubmission()).validate();
        } else {
            Finding vf = newFinding(gedcom, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, "submission");
            if (mayRepair(vf)) {
                gedcom.setSubmission(new Submission("@SUBMISSION@"));
                vf.addRepair(new AutoRepair(null, new Submission(gedcom.getSubmission())));
            }
        }
    }

    /**
     * Check all the submitters in the gedcom
     */
    private void checkSubmitters() {
        for (Entry<String, Submitter> entry : gedcom.getSubmitters().entrySet()) {
            if (entry.getValue() == null || entry.getKey() == null) {
                Finding vf = newFinding(gedcom, Severity.ERROR, ProblemCode.LIST_WITH_NULL_VALUE, "submitter");
                if (mayRepair(vf)) {
                    vf.addRepair(new AutoRepair(null, null));
                    gedcom.getSubmitters().remove(entry.getKey());
                }
            } else {
                new SubmitterValidator(this, entry.getValue()).validate();
            }
        }
    }

    /**
     * Determine the GEDCOM spec to use for validation, based on what the file says
     * 
     * @param g
     *            the gedcom structure being validated
     */
    private void determineGedcomSpecVersion(Gedcom g) {
        Header h = g.getHeader();
        if (h == null || h.getGedcomVersion() == null || h.getGedcomVersion().getVersionNumber() == null) {
            Finding vf = newFinding(h, Severity.INFO, ProblemCode.UNABLE_TO_DETERMINE_GEDCOM_VERSION, null);
            if (mayRepair(vf)) {
                if (h == null) {
                    h = new Header();
                    g.setHeader(h);
                    vf.addRepair(new AutoRepair(null, h));
                }
                GedcomVersion gv = h.getGedcomVersion();
                if (h.getGedcomVersion() == null) {
                    gv = new GedcomVersion();
                    h.setGedcomVersion(gv);
                    vf.addRepair(new AutoRepair(null, gv));
                }
                StringWithCustomFacts vn = gv.getVersionNumber();
                if (vn == null || vn.getValue() == null || !(SupportedVersion.V5_5.equals(vn.getValue()) || SupportedVersion.V5_5_1
                        .equals(vn.getValue()))) {
                    gv.setVersionNumber(SupportedVersion.V5_5_1.toString());
                    vf.addRepair(new AutoRepair(vn, gv.getVersionNumber()));
                }
            }
        } else {
            StringWithCustomFacts gedcomVersion = g.getHeader().getGedcomVersion().getVersionNumber();
            if (SupportedVersion.V5_5.toString().equals(gedcomVersion.getValue())) {
                v551 = false;
            }
        }
    }

    /**
     * Is the string supplied non-null, and has something other than whitespace in it?
     * 
     * @param s
     *            the strings
     * @return true if the string supplied non-null, and has something other than whitespace in it
     */
    private boolean isSpecified(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }

}
