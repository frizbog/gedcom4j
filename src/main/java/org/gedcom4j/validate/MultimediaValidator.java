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

import org.gedcom4j.Options;
import org.gedcom4j.exception.ValidationException;
import org.gedcom4j.model.AbstractCitation;
import org.gedcom4j.model.FileReference;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.SupportedVersion;
import org.gedcom4j.model.UserReference;

/**
 * A validator for {@link Multimedia} objects. See {@link GedcomValidator} for usage instructions.
 * 
 * @author frizbog1
 * 
 */
class MultimediaValidator extends AbstractValidator {

    /**
     * The multimedia being validated
     */
    private final Multimedia mm;

    /**
     * The gedcom version to validate against. There are numerous differences in multimedia records between 5.5 and 5.5.1.
     */
    private SupportedVersion gedcomVersion;

    /**
     * Constructor
     * 
     * @param validator
     *            the root validator
     * @param multimedia
     *            the multimedia object being validated
     */
    MultimediaValidator(GedcomValidator validator, Multimedia multimedia) {
        this.validator = validator;
        if (validator == null) {
            throw new ValidationException("Root validator passed in to MultimediaValidator constructor was null");
        }
        mm = multimedia;
        if (validator.gedcom == null || validator.gedcom.getHeader() == null || validator.gedcom.getHeader()
                .getGedcomVersion() == null || validator.gedcom.getHeader().getGedcomVersion().getVersionNumber() == null) {
            if (validator.isAutorepairEnabled()) {
                gedcomVersion = SupportedVersion.V5_5_1;
                validator.addInfo("Was not able to determine GEDCOM version - assuming 5.5.1", validator.gedcom);
            } else {
                validator.addError("Was not able to determine GEDCOM version - cannot validate multimedia objects",
                        validator.gedcom);
            }
        } else {
            gedcomVersion = validator.gedcom.getHeader().getGedcomVersion().getVersionNumber();
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
        if (fr == null) {
            if (validator.isAutorepairEnabled()) {
                addError("Null file reference in list of file references in multimedia object - cannot repair", mm);
            } else {
                addError("Null file reference in list of file references in multimedia object", mm);
            }
            return;
        }
        checkRequiredString(fr.getFormat(), "format", fr);
        mustHaveValueOrBeOmitted(fr.getMediaType(), "media type", fr);
        mustHaveValueOrBeOmitted(fr.getTitle(), "title", fr);
        checkRequiredString(fr.getReferenceToFile(), "reference to file", fr);
    }

    /**
     * Check user references
     */
    private void checkUserReferences() {
        List<UserReference> userReferences = mm.getUserReferences();
        if (Options.isCollectionInitializationEnabled() && userReferences == null) {
            if (validator.isAutorepairEnabled()) {
                mm.getUserReferences(true).clear();
                validator.addInfo("List of user references on multimedia object was null - repaired", mm);
            } else {
                validator.addError("List of user references on multimedia object is null", mm);
                return;
            }
        }
        if (validator.isAutorepairEnabled()) {
            int dups = new DuplicateHandler<>(userReferences).process();
            if (dups > 0) {
                validator.addInfo(dups + " duplicate user references found and removed", mm);
            }
        }
        if (userReferences != null) {
            for (UserReference u : userReferences) {
                checkCustomTags(u);
                if (u.getReferenceNum() == null) {
                    if (validator.isAutorepairEnabled()) {
                        addError("User reference is has a null or blank reference number - cannot repair", u);
                    } else {
                        addError("User reference is has a null or blank reference number", u);
                    }
                    continue;
                }
            }
        }
    }

    /**
     * Check the xref field
     */
    private void checkXref() {
        // Xref is required
        if (mm.getXref() == null || mm.getXref().trim().length() == 0) {
            if (validator.isAutorepairEnabled()) {
                addError("Multimedia object must have xref - cannot autorepair", mm);
            } else {
                addError("Multimedia object must have xref", mm);
            }
            return;
        }

        // Item should be found in map using the xref as the key
        if (validator.gedcom.getMultimedia().get(mm.getXref()) != mm) {
            if (validator.isAutorepairEnabled()) {
                validator.gedcom.getMultimedia().put(mm.getXref(), mm);
                validator.addInfo("Multimedia object not keyed by xref in map - repaired", mm);
            } else {
                validator.addError("Multimedia object not keyed by xref in map", mm);
            }
            return;
        }
    }

    /**
     * Convenience method to determine if GEDCOM standard in use is v5.5.1
     * 
     * @return true if and only if GEDCOM standard in use is 5.5.1
     */
    private boolean v551() {
        return SupportedVersion.V5_5_1.equals(gedcomVersion);
    }

    /**
     * Validate that the multimedia object conforms to GEDCOM 5.5 rules
     */
    private void validate55() {
        if (mm.getBlob() == null || mm.getBlob().isEmpty()) {
            if (validator.isAutorepairEnabled()) {
                addError("Embedded media object has an empty blob object - cannot repair", mm);
            } else {
                addError("Embedded media object has an empty blob object", mm);
            }
        }
        checkRequiredString(mm.getEmbeddedMediaFormat(), "embedded media format", mm);

        // Validate the citations - only allowed in 5.5.1
        if (mm.getCitations() != null && !mm.getCitations().isEmpty()) {
            if (validator.isAutorepairEnabled()) {
                mm.getCitations(true).clear();
                validator.addInfo("Citations collection was populated, but not allowed in "
                        + "v5.5 of gedcom - repaired (cleared)", mm);
            } else {
                validator.addError("Citations collection is populated, but not allowed in " + "v5.5 of gedcom", mm);
            }
        }
    }

    /**
     * Validate that the multimedia object conforms to GEDCOM 5.5.1 rules
     */
    private void validate551() {

        // File references
        if (Options.isCollectionInitializationEnabled() && mm.getFileReferences() == null) {
            if (validator.isAutorepairEnabled()) {
                mm.getFileReferences(true).clear();
                validator.addInfo("Multimedia object did not have list of file references - repaired", mm);
            } else {
                validator.addError("Multimedia object does not have list of file references", mm);
                return;
            }
        }
        if (mm.getFileReferences() != null) {
            for (FileReference fr : mm.getFileReferences()) {
                checkFileReference(fr);
            }
        }

        // Blobs must be empty in 5.5.1
        if (mm.getBlob() != null && !mm.getBlob().isEmpty()) {
            if (validator.isAutorepairEnabled()) {
                mm.getBlob().clear();
                addInfo("Embedded media object had a populated blob object, "
                        + "which is not allowed in GEDCOM 5.5.1 - repaired (cleared)", mm);
            } else {
                addError("Embedded media object has a populated blob object, which is not allowed in GEDCOM 5.5.1", mm);
            }
        }

        // Cannot have an embedded media format in 5.5.1
        if (mm.getEmbeddedMediaFormat() != null) {
            if (validator.isAutorepairEnabled()) {
                mm.setEmbeddedMediaFormat(null);
                validator.addInfo("Multimedia object had a format for embedded media, "
                        + "which is not allowed in GEDCOM 5.5.1 - repaired (cleared)", mm);
            } else {
                validator.addError("Multimedia object has a format for embedded media, "
                        + "which is not allowed in GEDCOM 5.5.1", mm);
            }

        }

        // Validate the citations - only allowed in 5.5.1
        if (mm.getCitations() != null) {
            for (AbstractCitation c : mm.getCitations()) {
                new CitationValidator(validator, c).validate();
            }
        }

    }

    /**
     * Validate items that are common to both GEDCOM 5.5 and GEDCOM 5.5.1
     */
    private void validateCommon() {
        checkXref();
        mustHaveValueOrBeOmitted(mm.getRecIdNumber(), "record id number", mm);
        checkChangeDate(mm.getChangeDate(), mm);
        checkUserReferences();
        if (Options.isCollectionInitializationEnabled() && mm.getCitations() == null) {
            if (validator.isAutorepairEnabled()) {
                mm.getCitations(true).clear();
                addInfo("citations collection for multimedia object was null - validator.autorepaired", mm);
            } else {
                addError("citations collection for multimedia object is null", mm);
            }
        }
        if (mm.getContinuedObject() != null) {
            new MultimediaValidator(validator, mm.getContinuedObject()).validate();
        }
        // The blob object should always be instantiated, even for 5.5.1 (in
        // which case it should be an empty collection)
        if (Options.isCollectionInitializationEnabled() && mm.getBlob() == null) {
            if (validator.isAutorepairEnabled()) {
                mm.getBlob(true).clear();
                validator.addInfo("Embedded blob was null - repaired", mm);
            } else {
                validator.addError("Embedded blob is null", mm);
            }
        }

        new NotesValidator(validator, mm, mm.getNotes()).validate();
    }

}
