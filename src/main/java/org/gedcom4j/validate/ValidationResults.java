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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gedcom4j.model.ModelElement;
import org.gedcom4j.validate.Validator.Finding;

/**
 * An object containing the results of a validation.
 * 
 * @author frizbog
 */
public class ValidationResults {

    /** All findings. */
    private final List<Finding> allFindings = new ArrayList<>();

    /** The findings by object. */
    private final Map<ModelElement, List<Finding>> findingsByObject = new HashMap<>();

    /**
     * Add a validation finding.
     *
     * @param vf
     *            the validation finding
     */
    public void add(Finding vf) {
        allFindings.add(vf);
        List<Finding> list = findingsByObject.get(vf.getItemOfConcern());
        if (list == null) {
            list = new ArrayList<>();
            findingsByObject.put(vf.getItemOfConcern(), list);
        }
        list.add(vf);
    }

    /**
     * Clear all results.
     */
    public void clear() {
        allFindings.clear();
        findingsByObject.clear();
    }

    /**
     * Gets all findings.
     *
     * @return all findings
     */
    public List<Finding> getAllFindings() {
        return allFindings;
    }

    /**
     * Gets findings of a given type, by code
     * 
     * @param problemCode
     *            the code to find
     * @return all findings with the provided code
     */
    public List<Finding> getByCode(int problemCode) {
        List<Finding> result = new ArrayList<>();
        for (Finding vf : allFindings) {
            if (vf.getProblemCode() == problemCode) {
                result.add(vf);
            }
        }
        return result;
    }

    /**
     * Gets findings of a given type, by code
     * 
     * @param problemCode
     *            the code to find
     * @return all findings with the provided code
     * @throws IllegalArgumentException
     *             if a null problemCode value is provided
     */
    public List<Finding> getByCode(ProblemCode problemCode) {
        if (problemCode == null) {
            throw new IllegalArgumentException("problemCode is a required argument");
        }
        return getByCode(problemCode.getCode());
    }

    /**
     * Gets findings of a given severity
     * 
     * @param s
     *            the severity to find
     *
     * @return all findings with the provided severity
     * @throws IllegalArgumentException
     *             if a null severity value is provided
     */
    public List<Finding> getBySeverity(Severity s) {
        if (s == null) {
            throw new IllegalArgumentException("Severity is a required argument");
        }
        List<Finding> result = new ArrayList<>();
        for (Finding vf : allFindings) {
            if (vf.getSeverity() == s) {
                result.add(vf);
            }
        }
        return result;
    }

    /**
     * Gets the findings for a specific object. Not hierarchical, and does not consider child objects - only findings on the
     * specific object you supply will be returned.
     * 
     * @param modelElement
     *            the object you want to get findings for
     * @return the findings by object. Always returns a list, though it may be empty
     */
    public List<Finding> getFindingsForObject(ModelElement modelElement) {
        List<Finding> result = findingsByObject.get(modelElement);
        if (result == null) {
            result = new ArrayList<>(0);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(25);
        builder.append("ValidationResults [");
        if (allFindings != null) {
            builder.append("allFindings=");
            builder.append(allFindings);
        }
        builder.append("]");
        return builder.toString();
    }
}
