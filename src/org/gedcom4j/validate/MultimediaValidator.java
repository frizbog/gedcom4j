/*
 * Copyright (c) 2009-2013 Matthew R. Harrah
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

import org.gedcom4j.model.AbstractCitation;
import org.gedcom4j.model.FileReference;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.SupportedVersion;
import org.gedcom4j.model.UserReference;

/**
 * A validator for {@link Multimedia} objects. See {@link GedcomValidator} for
 * usage instructions.
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
     * The gedcom version to validate against. There are numerous differences in
     * multimedia records between 5.5 and 5.5.1.
     */
    private SupportedVersion gedcomVersion;

    /**
     * Constructor
     * 
     * @param rootValidator
     *            the root validator
     * @param multimedia
     *            the multimedia object being validated
     */
    public MultimediaValidator(GedcomValidator rootValidator, Multimedia multimedia) {
        this.rootValidator = rootValidator;
        if (rootValidator == null) {
            throw new GedcomValidationException("Root validator passed in to MultimediaValidator constructor was null");
        }
        mm = multimedia;
        if (rootValidator.gedcom == null || rootValidator.gedcom.header == null
                || rootValidator.gedcom.header.gedcomVersion == null
                || rootValidator.gedcom.header.gedcomVersion.versionNumber == null) {
            if (rootValidator.autorepair) {
                gedcomVersion = SupportedVersion.V5_5_1;
                rootValidator
                        .addInfo("Was not able to determine GEDCOM version - assuming 5.5.1", rootValidator.gedcom);
            } else {
                rootValidator.addError("Was not able to determine GEDCOM version - cannot validate multimedia objects",
                        rootValidator.gedcom);
            }
        } else {
            gedcomVersion = rootValidator.gedcom.header.gedcomVersion.versionNumber;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gedcom4j.validate.AbstractValidator#validate()
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
            if (rootValidator.autorepair) {
                addError("Null file reference in list of file references in multimedia object - cannot repair", mm);
            } else {
                addError("Null file reference in list of file references in multimedia object", mm);
            }
            return;
        }
        checkRequiredString(fr.format, "format", fr);
        checkOptionalString(fr.mediaType, "media type", fr);
        checkOptionalString(fr.title, "title", fr);
        checkRequiredString(fr.referenceToFile, "reference to file", fr);
    }

    /**
     * Check user references
     */
    private void checkUserReferences() {
        if (mm.userReferences == null) {
            if (rootValidator.autorepair) {
                mm.userReferences = new ArrayList<UserReference>();
                rootValidator.addInfo("List of user references on multimedia object was null - repaired", mm);
            } else {
                rootValidator.addError("List of user references on multimedia object is null", mm);
                return;
            }
        }
        for (UserReference u : mm.userReferences) {
            checkCustomTags(u);
            if (u.referenceNum == null) {
                if (rootValidator.autorepair) {
                    addError("User reference is has a null or blank reference number - cannot repair", u);
                } else {
                    addError("User reference is has a null or blank reference number", u);
                }
                continue;
            }
        }
    }

    /**
     * Check the xref field
     */
    private void checkXref() {
        // Xref is required
        if (mm.xref == null || mm.xref.trim().length() == 0) {
            if (rootValidator.autorepair) {
                addError("Multimedia object must have xref - cannot autorepair", mm);
            } else {
                addError("Multimedia object must have xref", mm);
            }
            return;
        }

        // Item should be found in map using the xref as the key
        if (rootValidator.gedcom.multimedia.get(mm.xref) != mm) {
            if (rootValidator.autorepair) {
                rootValidator.gedcom.multimedia.put(mm.xref, mm);
                rootValidator.addInfo("Multimedia object not keyed by xref in map - repaired", mm);
            } else {
                rootValidator.addError("Multimedia object not keyed by xref in map", mm);
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
        if (mm.blob.isEmpty()) {
            if (rootValidator.autorepair) {
                addError("Embedded media object has an empty blob object - cannot repair", mm);
            } else {
                addError("Embedded media object has an empty blob object", mm);
            }
        }
        checkRequiredString(mm.embeddedMediaFormat, "embedded media format", mm);

        // Validate the citations - only allowed in 5.5.1
        if (!mm.citations.isEmpty()) {
            if (rootValidator.autorepair) {
                mm.citations.clear();
                rootValidator.addInfo("Citations collection was populated, but not allowed in "
                        + "v5.5 of gedcom - repaired (cleared)", mm);
            } else {
                rootValidator.addError("Citations collection is populated, but not allowed in " + "v5.5 of gedcom", mm);
            }
        }
    }

    /**
     * Validate that the multimedia object conforms to GEDCOM 5.5.1 rules
     */
    private void validate551() {

        // File references
        if (mm.fileReferences == null) {
            if (rootValidator.autorepair) {
                mm.fileReferences = new ArrayList<FileReference>();
                rootValidator.addInfo("Multimedia object did not have list of file references - repaired", mm);
            } else {
                rootValidator.addError("Multimedia object does not have list of file references", mm);
                return;
            }
        }
        for (FileReference fr : mm.fileReferences) {
            checkFileReference(fr);
        }

        // Blobs must be empty in 5.5.1
        if (mm.blob != null && !mm.blob.isEmpty()) {
            if (rootValidator.autorepair) {
                mm.blob.clear();
                addInfo("Embedded media object had a populated blob object, "
                        + "which is not allowed in GEDCOM 5.5.1 - repaired (cleared)", mm);
            } else {
                addError("Embedded media object has a populated blob object, which is not allowed in GEDCOM 5.5.1", mm);
            }
        }

        // Cannot have an embedded media format in 5.5.1
        if (mm.embeddedMediaFormat != null) {
            if (rootValidator.autorepair) {
                mm.embeddedMediaFormat = null;
                rootValidator.addInfo("Multimedia object had a format for embedded media, "
                        + "which is not allowed in GEDCOM 5.5.1 - repaired (cleared)", mm);
            } else {
                rootValidator.addError("Multimedia object has a format for embedded media, "
                        + "which is not allowed in GEDCOM 5.5.1", mm);
            }

        }

        // Validate the citations - only allowed in 5.5.1
        for (AbstractCitation c : mm.citations) {
            new CitationValidator(rootValidator, c).validate();
        }

    }

    /**
     * Validate items that are common to both GEDCOM 5.5 and GEDCOM 5.5.1
     */
    private void validateCommon() {
        checkXref();
        checkOptionalString(mm.recIdNumber, "record id number", mm);
        checkChangeDate(mm.changeDate, mm);
        checkUserReferences();
        if (mm.citations == null) {
            if (rootValidator.autorepair) {
                mm.citations = new ArrayList<AbstractCitation>();
                addInfo("citations collection for multimedia object was null - rootValidator.autorepaired", mm);
            } else {
                addError("citations collection for multimedia object is null", mm);
            }
        }
        if (mm.continuedObject != null) {
            new MultimediaValidator(rootValidator, mm.continuedObject).validate();
        }
        // The blob object should always be instantiated, even for 5.5.1 (in
        // which case it should be an empty collection)
        if (mm.blob == null) {
            if (rootValidator.autorepair) {
                mm.blob = new ArrayList<String>();
                rootValidator.addInfo("Embedded blob was null - repaired", mm);
            } else {
                rootValidator.addError("Embedded blob is null", mm);
            }
        }

        new NotesValidator(rootValidator, mm, mm.notes).validate();
    }

}
