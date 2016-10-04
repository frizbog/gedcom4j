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

import java.util.List;

import org.gedcom4j.exception.ValidationException;
import org.gedcom4j.model.FileReference;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.StringWithCustomFacts;
import org.gedcom4j.model.UserReference;
import org.gedcom4j.model.enumerations.SupportedVersion;
import org.gedcom4j.validate.Validator.Finding;

/**
 * A validator for {@link Multimedia} objects. See {@link Validator} for usage instructions.
 * 
 * @author frizbog1
 * 
 */
class MultimediaValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -4969512119892429424L;

    /**
     * The multimedia being validated
     */
    private final Multimedia mm;

    /**
     * The gedcom version to validate against. There are numerous differences in multimedia records between 5.5 and 5.5.1.
     */
    private StringWithCustomFacts gedcomVersion;

    /**
     * Constructor
     * 
     * @param validator
     *            the root validator
     * @param multimedia
     *            the multimedia object being validated
     */
    MultimediaValidator(Validator validator, Multimedia multimedia) {
        super(validator);
        if (validator == null) {
            throw new ValidationException("Validator passed in to MultimediaValidator constructor was null");
        }
        mm = multimedia;
        if (validator.getGedcom() == null || validator.getGedcom().getHeader() == null || validator.getGedcom().getHeader()
                .getGedcomVersion() == null || validator.getGedcom().getHeader().getGedcomVersion().getVersionNumber() == null) {
            Finding vf = newFinding(mm, Severity.INFO, ProblemCode.UNABLE_TO_DETERMINE_GEDCOM_VERSION, null);
            if (mayRepair(vf)) {
                Multimedia before = new Multimedia(mm);
                gedcomVersion = new StringWithCustomFacts(SupportedVersion.V5_5_1.toString());
                vf.addRepair(new AutoRepair(before, new Multimedia(mm)));
            }
        } else {
            gedcomVersion = validator.getGedcom().getHeader().getGedcomVersion().getVersionNumber();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        validateCommon();
        if (v551()) {
            validate551();
        } else {
            validate55();
        }
    }

    /**
     * Check a file reference
     * 
     * @param fr
     *            the file reference to check
     */
    private void checkFileReference(FileReference fr) {
        mustHaveValue(fr, "format");
        mustHaveValueOrBeOmitted(fr, "mediaType");
        mustHaveValueOrBeOmitted(fr, "title");
        mustHaveValue(fr, "referenceToFile");
    }

    /**
     * Check user references
     */
    private void checkUserReferences() {
        checkUninitializedCollection(mm, "userReferences");
        if (mm.getUserReferences() == null) {
            return;
        }

        List<UserReference> userReferences = mm.getUserReferences();
        checkListOfModelElementsForDups(mm, "userReferences");
        checkListOfModelElementsForNulls(mm, "userReferences");
        for (UserReference u : userReferences) {
            mustHaveValue(u, "referenceNum");
            checkCustomFacts(u);
        }
    }

    /**
     * Check the xref field
     */
    private void checkXref() {
        xrefMustBePresentAndWellFormed(mm);

        // Item should be found in map using the xref as the key
        if (getValidator().getGedcom().getMultimedia().get(mm.getXref()) != mm) {
            newFinding(mm, Severity.ERROR, ProblemCode.CROSS_REFERENCE_NOT_FOUND, "xref");
        }
    }

    /**
     * Convenience method to determine if GEDCOM standard in use is v5.5.1
     * 
     * @return true if and only if GEDCOM standard in use is 5.5.1
     */
    private boolean v551() {
        return SupportedVersion.V5_5_1.toString().equals(gedcomVersion.getValue());
    }

    /**
     * Validate that the multimedia object conforms to GEDCOM 5.5 rules
     */
    private void validate55() {
        mustHaveValue(mm, "blob");
        mustHaveValue(mm, "embeddedMediaFormat");

        // Validate the citations - only allowed in 5.5.1
        if (mm.getCitations() != null && !mm.getCitations().isEmpty()) {
            Finding vf = newFinding(mm, Severity.ERROR, ProblemCode.NOT_ALLOWED_IN_GEDCOM_55, "");
            if (mayRepair(vf)) {
                Multimedia before = new Multimedia(mm);
                before.getCitations().clear();
                vf.addRepair(new AutoRepair(before, new Multimedia(mm)));
            }
        }
    }

    /**
     * Validate that the multimedia object conforms to GEDCOM 5.5.1 rules
     */
    private void validate551() {

        // File references
        checkUninitializedCollection(mm, "fileReferences");
        if (mm.getFileReferences() != null) {
            checkListOfModelElementsForDups(mm, "fileReferences");
            checkListOfModelElementsForNulls(mm, "fileReferences");
            for (FileReference fr : mm.getFileReferences()) {
                checkFileReference(fr);
            }
        }

        // Blobs must be empty in 5.5.1
        if (mm.getBlob() != null && !mm.getBlob().isEmpty()) {
            Finding vf = newFinding(mm, Severity.ERROR, ProblemCode.NOT_ALLOWED_IN_GEDCOM_551, "blob");
            if (mayRepair(vf)) {
                Multimedia before = new Multimedia(mm);
                mm.getBlob(true).clear();
                vf.addRepair(new AutoRepair(before, new Multimedia(mm)));
            }
        }

        // Cannot have an embedded media format in 5.5.1
        if (mm.getEmbeddedMediaFormat() != null) {
            Finding vf = newFinding(mm, Severity.ERROR, ProblemCode.NOT_ALLOWED_IN_GEDCOM_551, "embeddedMediaFormat");
            if (mayRepair(vf)) {
                Multimedia before = new Multimedia(mm);
                mm.setEmbeddedMediaFormat((String) null);
                vf.addRepair(new AutoRepair(before, new Multimedia(mm)));
            }
        }

        checkCitations(mm);

    }

    /**
     * Validate items that are common to both GEDCOM 5.5 and GEDCOM 5.5.1
     */
    private void validateCommon() {
        checkXref();
        mustHaveValueOrBeOmitted(mm, "recIdNumber");
        checkChangeDate(mm.getChangeDate(), mm);
        checkUserReferences();
        checkCitations(mm);
        if (mm.getContinuedObject() != null) {
            new MultimediaValidator(getValidator(), mm.getContinuedObject()).validate();
        }
        checkUninitializedCollection(mm, "blob");
        new NoteStructureListValidator(getValidator(), mm).validate();
    }

}
