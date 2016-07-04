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

/**
 * A class that holds information about something wrong with the gedcom data validated by {@link GedcomValidator}.
 * 
 * @author frizbog1
 * 
 */
public class GedcomValidationFinding {
    /**
     * The object that had a problem with it
     */
    private Object itemWithProblem;

    /**
     * A description of the problem
     */
    private String problemDescription;

    /**
     * How severe is the problem
     */
    private Severity severity;

    /**
     * Constructor
     * 
     * @param description
     *            a description of the problem
     * @param severity
     *            the severity of the problem
     * @param obj
     *            the item that has the problem (if applicable)
     */
    GedcomValidationFinding(String description, Severity severity, Object obj) {
        problemDescription = description;
        this.severity = severity;
        itemWithProblem = obj;
    }

    /**
     * Get the itemWithProblem
     * 
     * @return the itemWithProblem
     */
    public Object getItemWithProblem() {
        return itemWithProblem;
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
     * Get the severity
     * 
     * @return the severity
     */
    public Severity getSeverity() {
        return severity;
    }

    /**
     * Set the itemWithProblem
     * 
     * @param itemWithProblem
     *            the itemWithProblem to set
     */
    public void setItemWithProblem(Object itemWithProblem) {
        this.itemWithProblem = itemWithProblem;
    }

    /**
     * Set the problemDescription
     * 
     * @param problemDescription
     *            the problemDescription to set
     */
    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    /**
     * Set the severity
     * 
     * @param severity
     *            the severity to set
     */
    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(severity).append(": ").append(problemDescription);
        if (itemWithProblem != null) {
            sb.append(" (").append(itemWithProblem).append(")");
        }
        return sb.toString();
    }
}
