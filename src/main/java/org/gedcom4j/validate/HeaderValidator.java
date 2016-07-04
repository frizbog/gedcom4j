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

import org.gedcom4j.io.encoding.Encoding;
import org.gedcom4j.model.*;

/**
 * Validator for a {@link Header}. See {@link GedcomValidator} for usage information.
 * 
 * @author frizbog1
 * 
 */
class HeaderValidator extends AbstractValidator {

    /**
     * The {@link Header} being validated
     */
    private final Header header;

    /**
     * Constructor.
     * 
     * @param gedcomValidator
     *            the main validator
     * @param header
     *            the {@link Header} being validated
     */
    public HeaderValidator(GedcomValidator gedcomValidator, Header header) {
        rootValidator = gedcomValidator;
        this.header = header;
    }

    /**
     * Validate the {@link Header}
     * 
     * @see org.gedcom4j.validate.AbstractValidator#validate()
     */
    @Override
    protected void validate() {
        checkCharacterSet();
        if (header.getCopyrightData() == null) {
            if (rootValidator.autorepair) {
                header.getCopyrightData().clear();
                rootValidator.addInfo("Copyright data collection was null - repaired", header);
            } else {
                rootValidator.addError("Copyright data collection is null - must be at least an empty collection", header);
            }
        }
        checkCustomTags(header);
        checkOptionalString(header.getDate(), "date", header);
        checkOptionalString(header.getDestinationSystem(), "destination system", header);
        /*
         * Filename is actually a required field -- but since the writer automatically fills in the filename if it's
         * blank, treating it as optional here
         */
        checkOptionalString(header.getFileName(), "filename", header);
        if (header.getGedcomVersion() == null) {
            if (rootValidator.autorepair) {
                header.setGedcomVersion(new GedcomVersion());
                rootValidator.addInfo("GEDCOM version in header was null - repaired", header);
            } else {
                rootValidator.addError("GEDCOM version in header must be specified", header);
                return;
            }
        }
        if (header.getGedcomVersion().getVersionNumber() == null) {
            if (rootValidator.autorepair) {
                header.getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
                rootValidator.addInfo("GEDCOM version number in header was null - repaired", header);
            } else {
                rootValidator.addError("GEDCOM version number in header must be specified", header);
                return;
            }
        }
        checkCustomTags(header.getGedcomVersion());
        checkOptionalString(header.getLanguage(), "language", header);
        new NotesValidator(rootValidator, header, header.getNotes()).validate();
        checkOptionalString(header.getPlaceHierarchy(), "place hierarchy", header);
        checkSourceSystem();
        if (header.getSubmitter() == null) {
            if (rootValidator.autorepair) {
                if (rootValidator.gedcom.getSubmitters() == null || rootValidator.gedcom.getSubmitters().isEmpty()) {
                    rootValidator.addError("Submitter not specified in header, and autorepair could not " + "find a submitter to select as default", header);
                } else {
                    // Take the first submitter from the collection and set that
                    // as the primary submitter in the header
                    header.setSubmitter(rootValidator.gedcom.getSubmitters().values().iterator().next());
                }
            } else {
                rootValidator.addError("Submitter not specified in header", header);
            }
            return;
        }
        new SubmitterValidator(rootValidator, header.getSubmitter()).validate();
        if (header.getSubmission() != null) {
            rootValidator.validateSubmission(header.getSubmission());
        }
        checkOptionalString(header.getTime(), "time", header);
    }

    /**
     * Check the character set
     */
    private void checkCharacterSet() {
        if (header.getCharacterSet() == null) {
            if (rootValidator.autorepair) {
                header.setCharacterSet(new CharacterSet());
                rootValidator.addInfo("Header did not have a character set defined - corrected.", header);
            } else {
                rootValidator.addError("Header has no character set defined", header);
                return;
            }
        }
        if (header.getCharacterSet().getCharacterSetName() == null) {
            if (rootValidator.autorepair) {
                header.getCharacterSet().setCharacterSetName(new StringWithCustomTags("ANSEL"));
                rootValidator.addInfo("Character set name was not defined", header.getCharacterSet());
            } else {
                rootValidator.addError("Character set name was not defined", header.getCharacterSet());
                return;
            }
        }
        if (!Encoding.isValidCharacterSetName(header.getCharacterSet().getCharacterSetName().getValue())) {
            rootValidator.addError("Character set name is not one of the supported encodings (" + Encoding.getSupportedCharacterSetNames() + ")", header
                    .getCharacterSet().getCharacterSetName());
        }
        checkOptionalString(header.getCharacterSet().getCharacterSetName(), "character set name", header.getCharacterSet());
        checkOptionalString(header.getCharacterSet().getVersionNum(), "character set version number", header.getCharacterSet());
        checkCustomTags(header.getCharacterSet());
    }

    /**
     * Check the source system
     */
    private void checkSourceSystem() {
        SourceSystem ss = header.getSourceSystem();
        if (ss == null) {
            if (rootValidator.autorepair) {
                ss = new SourceSystem();
                header.setSourceSystem(ss);
                rootValidator.addInfo("No source system specified in header - repaired", header);
            } else {
                rootValidator.addError("No source system specified in header", header);
                return;
            }
        }
        checkCustomTags(ss);
        if (ss.corporation != null) {
            Corporation c = ss.corporation;
            checkCustomTags(c);
            if (c.getAddress() != null) {
                new AddressValidator(rootValidator, c.getAddress()).validate();
            }
            if (c.getBusinessName() == null || c.getBusinessName().trim().length() == 0) {
                if (rootValidator.autorepair) {
                    c.setBusinessName("UNSPECIFIED");
                    rootValidator.addInfo("Corporation for source system exists but had no name - repaired", c);
                } else {
                    rootValidator.addError("Corporation for source system exists but has no name", c);
                }
            }
        }
        checkOptionalString(ss.productName, "product name", ss);
        if (ss.sourceData != null) {
            HeaderSourceData sd = ss.sourceData;
            if (sd.getName() == null || sd.getName().trim().length() == 0) {
                if (rootValidator.autorepair) {
                    sd.setName("UNSPECIFIED");
                    rootValidator.addInfo("Source data was specified for source system, " + "but name of source data was not specified - repaired", sd);
                } else {
                    rootValidator.addError("Source data is specified for source system, " + "but name of source data is not specified", sd);
                }

            }
            checkOptionalString(sd.getCopyright(), "copyright", sd);
            checkOptionalString(sd.getPublishDate(), "publish date", sd);
            checkCustomTags(sd);
        }
        if (ss.systemId == null) {
            if (rootValidator.autorepair) {
                ss.systemId = "UNSPECIFIED";
                rootValidator.addInfo("System ID was not specified in source system in header - repaired", ss);
            } else {
                rootValidator.addError("System ID must be specified in source system in header", ss);
            }
        }
        checkOptionalString(ss.versionNum, "source system version number", ss);
    }
}
