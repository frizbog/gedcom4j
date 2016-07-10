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
package org.gedcom4j.validator;

import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.StringTree;

/**
 * Main validator class.
 * 
 * @author frizbog
 *
 */
public class Validator {

    /**
     * Will the most simple, obvious, non-destructive errors be automatically fixed? This includes things like creating
     * empty collections where one is expected but only a null reference exists.
     */
    private boolean autorepairEnabled = true;

    /**
     * The findings from validation
     */
    private final List<GedcomValidationFinding> findings = new ArrayList<GedcomValidationFinding>();

    /**
     * Get the findings
     * 
     * @return the findings
     */
    public List<GedcomValidationFinding> getFindings() {
        return findings;
    }

    /**
     * Are there any errors in the findings (so far)?
     * 
     * @return true if there exists at least one finding with severity ERROR
     */
    public boolean hasErrors() {
        for (GedcomValidationFinding finding : findings) {
            if (finding.getSeverity() == Severity.ERROR) {
                return true;
            }
        }
        return false;
    }

    /**
     * Are there any warnings in the findings (so far)?
     * 
     * @return true if there exists at least one finding with severity WARNING
     */
    public boolean hasWarnings() {
        for (GedcomValidationFinding finding : findings) {
            if (finding.getSeverity() == Severity.WARNING) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the autorepair
     * 
     * @return the autorepair
     */
    public boolean isAutorepairEnabled() {
        return autorepairEnabled;
    }

    /**
     * Set the autorepair
     * 
     * @param autorepair
     *            the autorepair to set
     */
    public void setAutorepairEnabled(boolean autorepair) {
        autorepairEnabled = autorepair;
    }

    /**
     * Start the whole validation process
     * 
     * @param g
     *            the gedcom structure to be validated
     */
    public void validate(Gedcom g) {
        g.accept(new ValidationVisitor(this));
    }

    /**
     * Add a new finding of severity ERROR
     * 
     * @param description
     *            the description of the error
     */
    protected void addError(String description) {
        System.out.println(description);
        getFindings().add(new GedcomValidationFinding(description, Severity.ERROR, null));
    }

    /**
     * Validate custom tags
     * 
     * @param customTags
     *            the custom tags
     */
    protected void validateCustomTags(List<StringTree> customTags) {
        System.out.println("Validate the custom tags");
    }

    /**
     * Add a new finding of severity ERROR
     * 
     * @param description
     *            the description of the error
     * @param o
     *            the object in error
     */
    void addError(String description, Object o) {
        System.out.println(description);
        getFindings().add(new GedcomValidationFinding(description, Severity.ERROR, o));
    }

    /**
     * Add a new finding of severity INFO
     * 
     * @param description
     *            the description of the finding
     */
    void addInfo(String description) {
        System.out.println(description);
        getFindings().add(new GedcomValidationFinding(description, Severity.INFO, null));
    }

    /**
     * Add a new finding of severity INFO
     * 
     * @param description
     *            the description of the finding
     * @param o
     *            the object in error
     */
    void addInfo(String description, Object o) {
        System.out.println(description);
        getFindings().add(new GedcomValidationFinding(description, Severity.INFO, o));
    }

    /**
     * Add a new finding of severity WARNING
     * 
     * @param description
     *            the description of the warning
     */
    void addWarning(String description) {
        System.out.println(description);
        getFindings().add(new GedcomValidationFinding(description, Severity.WARNING, null));
    }

    /**
     * Add a new finding of severity WARNING
     * 
     * @param description
     *            the description of the warning
     * @param o
     *            the object in error
     */
    void addWarning(String description, Object o) {
        System.out.println(description);
        getFindings().add(new GedcomValidationFinding(description, Severity.WARNING, o));
    }
}