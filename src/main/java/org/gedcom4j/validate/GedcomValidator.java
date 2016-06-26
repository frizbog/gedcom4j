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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.gedcom4j.model.*;

/**
 * <p>
 * A class to validate the contents of a {@link Gedcom} structure. It is used primarily for those users who wish to
 * create and write GEDCOM files, and is of little importance or use to those who wish only to read/parse GEDCOM files
 * and use their data. Validation is performed automatically prior to writing a GEDCOM file by default (although this
 * can be disabled), and there is support for automatically repairing ("autorepair") issues found.
 * </p>
 * <p>
 * <b>Note that the validation framework is a work in progress and as such, is incompletely implemented at this
 * time.</b>
 * </p>
 * <p>
 * General usage is as follows:
 * </p>
 * <ol>
 * <li>Instantiate a {@link GedcomValidator}, passing the {@link Gedcom} structure to be validated as the argument to
 * the constructor</li>
 * <li>If desired, turn off automatic repairs during validation by setting {@link GedcomValidator#autorepair} to
 * <tt>false</tt>.
 * <li>Call the {@link GedcomValidator#validate()} method.</li>
 * <li>Inspect the {@link GedcomValidator#findings} list, which contains {@link GedcomValidationFinding} objects
 * describing the problems that were found. These will include errors that were fixed by autorepair (with severity of
 * INFO), and those that could not be autorepaired (with severity of ERROR or WARNING).</li>
 * </ol>
 * <p>
 * Note again that by default, validation is performed automatically by the {@link org.gedcom4j.writer.GedcomWriter}
 * class when writing a GEDCOM file out.
 * </p>
 * 
 * <h2>Autorepair</h2>
 * <p>
 * The validation framework, by default and unless disabled, will attempt to automatically repair ("autorepair")
 * problems it finds in the object graph, so that if written as a GEDCOM file, the file written will conform to the
 * GEDCOM spec, as well as to help the developer avoid NullPointerExceptions due to certain items not being
 * instantiated.
 * </p>
 * <p>
 * This section lists a number of the actions taken automatically when autorepair is enabled.
 * </p>
 * <ul>
 * <li>Collection fields (e.g., the language preferences collection on a submitter, or custom tags on those
 * fields/object that support them) are initialized to empty collections if they are null.</li>
 * <li>Certain mandatory fields are given default values. N.B. The values chosen as defaults may not be suitable, so the
 * user is urged to</li>
 * </ul>
 * 
 * @author frizbog1
 */
public class GedcomValidator extends AbstractValidator {

    /**
     * Will the most simple, obvious, non-destructive errors be automatically fixed? This includes things like creating
     * empty collections where one is expected but only a null reference exists.
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
     * Are there any errors in the findings (so far)?
     * 
     * @return true if there exists at least one finding with severity ERROR
     */
    public boolean hasErrors() {
        for (GedcomValidationFinding finding : rootValidator.findings) {
            if (finding.severity == Severity.ERROR) {
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
        for (GedcomValidationFinding finding : rootValidator.findings) {
            if (finding.severity == Severity.WARNING) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validate the gedcom file
     */
    @Override
    public void validate() {
        findings.clear();
        if (gedcom == null) {
            addError("gedcom structure is null");
            return;
        }
        validateSubmitters();
        validateHeader();
        validateIndividuals();
        validateFamilies();
        validateRepositories();
        validateMultimedia();
        validateNotes();
        validateSources();
        validateSubmission(gedcom.submission);
        validateTrailer();
        checkNotes(new ArrayList<Note>(gedcom.notes.values()), gedcom);
    }

    /**
     * Validate the submission substructure under the root gedcom
     * 
     * @param s
     *            the submission substructure to be validated
     */
    void validateSubmission(Submission s) {

        if (s == null) {
            addError("Submission record on root gedcom is null", gedcom);
            return;
        }
        checkXref(s);
        checkOptionalString(s.ancestorsCount, "Ancestor count", s);
        checkOptionalString(s.descendantsCount, "Descendant count", s);
        checkOptionalString(s.nameOfFamilyFile, "Name of family file", s);
        checkOptionalString(s.ordinanceProcessFlag, "Ordinance process flag", s);
        checkOptionalString(s.recIdNumber, "Automated record id", s);
        checkOptionalString(s.templeCode, "Temple code", s);
    }

    /**
     * Validate the families map
     */
    private void validateFamilies() {
        if (gedcom.families == null) {
            if (rootValidator.autorepair) {
                gedcom.families = new HashMap<String, Family>();
                rootValidator.addInfo("Map of families in gedcom was null - repaired", gedcom);
            } else {
                rootValidator.addError("Map of families in gedcom is null", gedcom);
                return;
            }
        }
        for (Entry<String, Family> e : gedcom.families.entrySet()) {
            if (e.getKey() == null) {
                if (rootValidator.autorepair) {
                    rootValidator.addError("Family in map but has null key - cannot repair", e.getValue());
                } else {
                    rootValidator.addError("Family in map but has null key", e.getValue());
                }
                continue;
            }
            Family f = e.getValue();
            if (!e.getKey().equals(f.xref)) {
                if (rootValidator.autorepair) {
                    rootValidator.addError("Family in map not keyed by its xref - cannot repair", f.xref);
                } else {
                    rootValidator.addError("Family in map not keyed by its xref", f.xref);
                }
                continue;
            }
            new FamilyValidator(this, f).validate();
        }
    }

    /**
     * Validate the {@link Gedcom#header} object
     */
    private void validateHeader() {
        if (gedcom.header == null) {
            if (autorepair) {
                gedcom.header = new Header();
                addInfo("Header was null - autorepaired");
            } else {
                addError("GEDCOM Header is null");
                return;
            }
        }
        new HeaderValidator(rootValidator, gedcom.header).validate();
    }

    /**
     * Validate the {@link Gedcom#individuals} collection
     */
    private void validateIndividuals() {
        if (gedcom.individuals == null) {
            if (autorepair) {
                gedcom.individuals = new HashMap<String, Individual>();
                addInfo("Individuals collection was null - autorepaired", gedcom);
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
                addError("Entry in individuals collection is not keyed by the individual's xref", e);
                return;
            }
            new IndividualValidator(rootValidator, e.getValue()).validate();
        }
    }

    /**
     * Validate the collection of {@link Multimedia} objects
     */
    private void validateMultimedia() {
        for (Multimedia m : gedcom.multimedia.values()) {
            MultimediaValidator mv = new MultimediaValidator(this, m);
            mv.validate();
        }
    }

    /**
     * Validates the notes
     */
    private void validateNotes() {
        int i = 0;
        for (Note n : gedcom.notes.values()) {
            i++;
            new NoteValidator(rootValidator, i, n).validate();
        }
    }

    /**
     * Validate the repositories collection
     */
    private void validateRepositories() {
        if (gedcom.repositories == null) {
            if (autorepair) {
                gedcom.repositories = new HashMap<String, Repository>();
                addInfo("Repositories collection on root gedcom was null - autorepaired", gedcom);
                return;
            }
            addError("Repositories collection on root gedcom is null", gedcom);
            return;
        }
        for (Entry<String, Repository> e : gedcom.repositories.entrySet()) {
            if (e.getKey() == null) {
                addError("Entry in repositories collection has null key", e);
                return;
            }
            if (e.getValue() == null) {
                addError("Entry in repositories collection has null value", e);
                return;
            }
            if (!e.getKey().equals(e.getValue().xref)) {
                addError("Entry in repositories collection is not keyed by the Repository's xref", e);
                return;
            }
            new RepositoryValidator(rootValidator, e.getValue()).validate();
        }

    }

    /**
     * Validate the {@link Gedcom#sources} collection
     */
    private void validateSources() {
        if (gedcom.sources == null) {
            if (autorepair) {
                gedcom.sources = new HashMap<String, Source>();
                addInfo("Sources collection was null - autorepaired", gedcom);
            } else {
                addError("Sources collection is null", gedcom);
                return;
            }
        }
        for (Entry<String, Source> e : gedcom.sources.entrySet()) {
            if (e.getKey() == null) {
                addError("Entry in sources collection has null key", e);
                return;
            }
            if (e.getValue() == null) {
                addError("Entry in sources collection has null value", e);
                return;
            }
            if (!e.getKey().equals(e.getValue().xref)) {
                addError("Entry in sources collection is not keyed by the individual's xref", e);
                return;
            }
            new SourceValidator(rootValidator, e.getValue()).validate();
        }
    }

    /**
     * Validate the submitters collection
     */
    private void validateSubmitters() {
        if (gedcom.submitters == null) {
            if (autorepair) {
                gedcom.submitters = new HashMap<String, Submitter>();
                addInfo("Submitters collection was missing on gedcom - repaired", gedcom);
            } else {
                addInfo("Submitters collection is missing on gedcom", gedcom);
                return;
            }
        }
        if (gedcom.submitters.isEmpty()) {
            if (autorepair) {
                Submitter s = new Submitter();
                s.xref = "@SUBM0000@";
                s.name = new StringWithCustomTags("UNSPECIFIED");
                gedcom.submitters.put(s.xref, s);
                addInfo("Submitters collection was empty - repaired", gedcom);
            } else {
                addError("Submitters collection is empty", gedcom);
            }
        }
        for (Submitter s : gedcom.submitters.values()) {
            new SubmitterValidator(rootValidator, s).validate();
        }
    }

    /**
     * Validate the trailer
     */
    private void validateTrailer() {
        if (gedcom.trailer == null) {
            if (rootValidator.autorepair) {
                gedcom.trailer = new Trailer();
                rootValidator.addInfo("Gedcom had no trailer - repaired", gedcom);
            } else {
                rootValidator.addError("Gedcom has no trailer", gedcom);
            }
        }
    }
}
