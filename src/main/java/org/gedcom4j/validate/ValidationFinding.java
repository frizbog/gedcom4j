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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.Options;
import org.gedcom4j.model.ModelElement;

/**
 * Represents something of interest found by the validation module.
 * 
 * @author frizbog
 */
public class ValidationFinding implements Serializable {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 2148459753130687833L;

    /**
     * The primary item that had the finding in it
     */
    private ModelElement itemOfConcern;

    /** The severity. */
    private Severity severity;

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
    private List<ModelElement> relatedItems = (Options.isCollectionInitializationEnabled() ? new ArrayList<ModelElement>(0) : null);

    /**
     * The repairs made automatically by the validator
     */
    private List<AutoRepair> repairs = (Options.isCollectionInitializationEnabled() ? new ArrayList<AutoRepair>(0) : null);

    /**
     * The field name in {@link #itemOfConcern} that had the finding. Optional. If populated, reflection can be used on the
     * {@link #itemOfConcern} object to get specific field values.
     */
    private String fieldNameOfConcern;

    /**
     * Default constructor
     */
    public ValidationFinding() {
        // Default constructor does nothing
    }

    /**
     * Instantiates a new validation finding.
     *
     * @param itemOfConcern
     *            the item of concern. Required.
     * @param severity
     *            the severity. Required.
     * @param problemCode
     *            the problem code. Required.
     * @throws IllegalArgumentException
     *             if any of the arguments are null
     */
    public ValidationFinding(ModelElement itemOfConcern, Severity severity, ProblemCode problemCode) {
        if (itemOfConcern == null) {
            throw new IllegalArgumentException("itemOfConcern is a required argument.");
        }
        if (severity == null) {
            throw new IllegalArgumentException("severity is a required argument.");
        }
        if (problemCode == null) {
            throw new IllegalArgumentException("problemCode is a required argument.");
        }
        this.itemOfConcern = itemOfConcern;
        this.severity = severity;
        this.problemCode = problemCode.ordinal();
        problemDescription = problemCode.getDescription();
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
     * Set the user-defined problem code
     * 
     * @param problemCode
     *            the problem code to set. Must be 1000 or higher. Values below 1000 are reserved for gedcom4j. Values of 1000 or
     *            higher are for user-defined validator problems.
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
     * Set the item of concern. Deliberately package-private. Outside the validation framework, this field should not be changeable.
     * 
     * @param itemOfConcern
     *            the item of concern
     */
    void setItemOfConcern(ModelElement itemOfConcern) {
        this.itemOfConcern = itemOfConcern;
    }

    /**
     * Set a problem code and description from within gedcom4j. Deliberately package-private to prevent usage outside the gedcom4j
     * framework.
     * 
     * @param pc
     *            the problem code enum entry to use for the problem code and description
     */
    void setProblem(ProblemCode pc) {
        problemCode = pc.ordinal();
        problemDescription = pc.getDescription();
    }

    /**
     * Set the related items. Deliberately package-private. Outside the validation framework, this field should not be changeable.
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
