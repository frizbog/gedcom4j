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
import java.util.List;
import java.util.Map.Entry;

import org.gedcom4j.model.Family;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Header;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.Note;
import org.gedcom4j.model.Repository;
import org.gedcom4j.model.Source;
import org.gedcom4j.model.StringWithCustomTags;
import org.gedcom4j.model.Submitter;
import org.gedcom4j.model.Trailer;

/**
 * <p>
 * A class to validate the contents of a {@link Gedcom} structure. It is used primarily for those users who wish to create and write
 * GEDCOM files, and is of little importance or use to those who wish only to read/parse GEDCOM files and use their data. Validation
 * is performed automatically prior to writing a GEDCOM file by default (although this can be disabled), and there is support for
 * automatically repairing ("autorepair") issues found.
 * </p>
 * <p>
 * <b>Note that the validation framework is a work in progress and as such, is incompletely implemented at this time.</b>
 * </p>
 * <p>
 * General usage is as follows:
 * </p>
 * <ol>
 * <li>Instantiate a {@link GedcomValidator}, passing the {@link Gedcom} structure to be validated as the argument to the
 * constructor</li>
 * <li>If desired, turn off automatic repairs during validation by setting {@link GedcomValidator#autorepairEnabled} to
 * <tt>false</tt>.
 * <li>Call the {@link GedcomValidator#validate()} method.</li>
 * <li>Inspect the {@link GedcomValidator#findings} list, which contains {@link GedcomValidationFinding} objects describing the
 * problems that were found. These will include errors that were fixed by autorepair (with severity of INFO), and those that could
 * not be autorepaired (with severity of ERROR or WARNING).</li>
 * </ol>
 * <p>
 * Note again that by default, validation is performed automatically by the {@link org.gedcom4j.writer.GedcomWriter} class when
 * writing a GEDCOM file out.
 * </p>
 * 
 * <h2>Autorepair</h2>
 * <p>
 * The validation framework, by default and unless disabled, will attempt to automatically repair ("autorepair") problems it finds
 * in the object graph, so that if written as a GEDCOM file, the file written will conform to the GEDCOM spec, as well as to help
 * the developer avoid NullPointerExceptions due to certain items not being instantiated (if they have so selected in the
 * {@link org.gedcom4j.Options} class.
 * </p>
 * 
 * @deprecated use Validator instead
 * @author frizbog1
 */
@Deprecated
@SuppressWarnings({ "PMD.GodClass", "PMD.TooManyMethods" })
public class GedcomValidator extends AbstractValidator {

    /**
     * Will the most simple, obvious, non-destructive errors be automatically fixed? This includes things like creating empty
     * collections where one is expected but only a null reference exists.
     */
    private boolean autorepairEnabled = true;

    /**
     * The findings from validation
     */
    private final List<GedcomValidationFinding> findings = new ArrayList<>();

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
        validator = this;
    }

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
        for (GedcomValidationFinding finding : validator.findings) {
            if (finding.getSeverity() == Severity.ERROR) {
                return true;
            }
        }
        return false;
    }

    /**
     * Are there any INFO level items in the findings (so far)?
     * 
     * @return true if there exists at least one finding with severity INFO
     */
    public boolean hasInfo() {
        for (GedcomValidationFinding finding : validator.findings) {
            if (finding.getSeverity() == Severity.INFO) {
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
        for (GedcomValidationFinding finding : validator.findings) {
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
        validateSubmission(gedcom.getSubmission());
        validateTrailer();
        new NotesValidator(validator, gedcom, new ArrayList<>(gedcom.getNotes().values())).validate();
    }

    /**
     * Validate the families map
     */
    private void validateFamilies() {
        for (Entry<String, Family> e : gedcom.getFamilies().entrySet()) {
            if (e.getKey() == null) {
                if (validator.autorepairEnabled) {
                    validator.addError("Family in map but has null key - cannot repair", e.getValue());
                } else {
                    validator.addError("Family in map but has null key", e.getValue());
                }
                continue;
            }
            Family f = e.getValue();
            if (!e.getKey().equals(f.getXref())) {
                if (validator.autorepairEnabled) {
                    validator.addError("Family in map not keyed by its xref - cannot repair", f.getXref());
                } else {
                    validator.addError("Family in map not keyed by its xref", f.getXref());
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
        if (gedcom.getHeader() == null) {
            if (autorepairEnabled) {
                gedcom.setHeader(new Header());
                addInfo("Header was null - autorepaired");
            } else {
                addError("GEDCOM Header is null");
                return;
            }
        }
        new HeaderValidator(validator, gedcom.getHeader()).validate();
    }

    /**
     * Validate the {@link Gedcom#individuals} collection
     */
    private void validateIndividuals() {
        for (Entry<String, Individual> e : gedcom.getIndividuals().entrySet()) {
            if (e.getKey() == null) {
                addError("Entry in individuals collection has null key", e);
                return;
            }
            if (e.getValue() == null) {
                addError("Entry in individuals collection has null value", e);
                return;
            }
            if (!e.getKey().equals(e.getValue().getXref())) {
                addError("Entry in individuals collection is not keyed by the individual's xref", e);
                return;
            }
            new IndividualValidator(validator, e.getValue()).validate();
        }
    }

    /**
     * Validate the collection of {@link Multimedia} objects
     */
    private void validateMultimedia() {
        if (gedcom.getMultimedia() != null) {
            for (Multimedia m : gedcom.getMultimedia().values()) {
                MultimediaValidator mv = new MultimediaValidator(this, m);
                mv.validate();
            }
        }
    }

    /**
     * Validates the notes
     */
    private void validateNotes() {
        int i = 0;
        for (Note n : gedcom.getNotes().values()) {
            i++;
            new NoteValidator(validator, i, n).validate();
        }
    }

    /**
     * Validate the repositories collection
     */
    private void validateRepositories() {
        for (Entry<String, Repository> e : gedcom.getRepositories().entrySet()) {
            if (e.getKey() == null) {
                addError("Entry in repositories collection has null key", e);
                return;
            }
            if (e.getValue() == null) {
                addError("Entry in repositories collection has null value", e);
                return;
            }
            if (!e.getKey().equals(e.getValue().getXref())) {
                addError("Entry in repositories collection is not keyed by the Repository's xref", e);
                return;
            }
            new RepositoryValidator(validator, e.getValue()).validate();
        }

    }

    /**
     * Validate the {@link Gedcom#sources} collection
     */
    private void validateSources() {
        for (Entry<String, Source> e : gedcom.getSources().entrySet()) {
            if (e.getKey() == null) {
                addError("Entry in sources collection has null key", e);
                return;
            }
            if (e.getValue() == null) {
                addError("Entry in sources collection has null value", e);
                return;
            }
            if (!e.getKey().equals(e.getValue().getXref())) {
                addError("Entry in sources collection is not keyed by the individual's xref", e);
                return;
            }
            new SourceValidator(validator, e.getValue()).validate();
        }
    }

    /**
     * Validate the submitters collection
     */
    private void validateSubmitters() {
        if (gedcom.getSubmitters().isEmpty()) {
            if (autorepairEnabled) {
                Submitter s = new Submitter();
                s.setXref("@SUBM0000@");
                s.setName(new StringWithCustomTags("UNSPECIFIED"));
                gedcom.getSubmitters().put(s.getXref(), s);
                addInfo("Submitters collection was empty - repaired", gedcom);
            } else {
                addError("Submitters collection is empty", gedcom);
            }
        }
        for (Submitter s : gedcom.getSubmitters().values()) {
            new SubmitterValidator(validator, s).validate();
        }
    }

    /**
     * Validate the trailer
     */
    private void validateTrailer() {
        if (gedcom.getTrailer() == null) {
            if (validator.autorepairEnabled) {
                gedcom.setTrailer(new Trailer());
                validator.addInfo("Gedcom had no trailer - repaired", gedcom);
            } else {
                validator.addError("Gedcom has no trailer", gedcom);
            }
        }
    }
}
