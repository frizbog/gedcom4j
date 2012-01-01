package com.mattharrah.gedcom4j.validate;

/**
 * A class that holds information about something wrong with the gedcom data
 * validated by {@link GedcomValidator}.
 * 
 * @author frizbog1
 * 
 */
public class GedcomValidationFinding {
    /**
     * The object that had a problem with it
     */
    public Object itemWithProblem;

    /**
     * A description of the problem
     */
    public String problemDescription;

    /**
     * How severe is the problem
     */
    public Severity severity;

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
    public GedcomValidationFinding(String description, Severity severity,
            Object obj) {
        problemDescription = description;
        this.severity = severity;
        itemWithProblem = obj;
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
