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

import org.gedcom4j.io.Encoding;
import org.gedcom4j.model.CharacterSet;
import org.gedcom4j.model.Corporation;
import org.gedcom4j.model.GedcomVersion;
import org.gedcom4j.model.Header;
import org.gedcom4j.model.SourceSystem;
import org.gedcom4j.model.StringWithCustomTags;

/**
 * Validator for a {@link Header}. See {@link GedcomValidator} for usage
 * information.
 * 
 * @author frizbog1
 * 
 */
class HeaderValidator extends AbstractValidator {

    /**
     * The {@link Header} being validated
     */
    private Header header;

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
     * Check the character set
     */
    private void checkCharacterSet() {
        if (header.characterSet == null) {
            if (rootValidator.autorepair) {
                header.characterSet = new CharacterSet();
                rootValidator.addInfo("Header did not have a character set defined - corrected.", header);
            } else {
                rootValidator.addError("Header has no character set defined", header);
                return;
            }
        }
        if (header.characterSet.characterSetName == null) {
            if (rootValidator.autorepair) {
                header.characterSet.characterSetName = new StringWithCustomTags("ANSEL");
                rootValidator.addInfo("Character set name was not defined", header.characterSet);
            } else {
                rootValidator.addError("Character set name was not defined", header.characterSet);
                return;
            }
        }
        if (!Encoding.isValidCharacterSetName(header.characterSet.characterSetName.value)) {
            rootValidator.addError(
                    "Character set name is not one of the supported encodings ("
                            + Encoding.getSupportedCharacterSetNames() + ")", header.characterSet.characterSetName);
        }
        checkStringWithCustomTags(header.characterSet.characterSetName);
        checkStringWithCustomTags(header.characterSet.versionNum);
    }

    /**
     * Check the source system
     */
    private void checkSourceSystem() {
        SourceSystem ss = header.sourceSystem;
        if (ss == null) {
            if (rootValidator.autorepair) {
                ss = new SourceSystem();
                header.sourceSystem = ss;
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
            if (c.address != null) {
                new AddressValidator(rootValidator, c.address).validate();
            }
            if (c.businessName == null || c.businessName.trim().isEmpty()) {
                if (rootValidator.autorepair) {
                    c.businessName = "UNSPECIFIED";
                    rootValidator.addInfo("Corporation for source system exists but had no name - repaired", c);
                } else {
                    rootValidator.addError("Corporation for source system exists but has no name", c);
                }
            }
        }
        checkStringWithCustomTags(ss.productName);
        if (ss.sourceData != null) {

        }
        if (ss.systemId == null) {
            if (rootValidator.autorepair) {
                ss.systemId = "UNSPECIFIED";
                rootValidator.addInfo("System ID was not specified in source system in header - repaired", ss);
            } else {
                rootValidator.addError("System ID must be specified in source system in header", ss);
            }
        }
        checkStringWithCustomTags(ss.versionNum);
    }

    /**
     * Validate the {@link Header}
     * 
     * @see org.gedcom4j.validate.AbstractValidator#validate()
     */
    @Override
    protected void validate() {
        checkCharacterSet();
        if (header.copyrightData == null) {
            if (rootValidator.autorepair) {
                header.copyrightData = new ArrayList<String>();
                rootValidator.addInfo("Copyright data collection was null - repaired", header);
            } else {
                rootValidator.addError("Copyright data collection is null - must be at least an empty collection",
                        header);
            }
        }
        checkCustomTags(header);
        checkStringWithCustomTags(header.date);
        checkStringWithCustomTags(header.destinationSystem);
        checkStringWithCustomTags(header.fileName);
        if (header.gedcomVersion == null) {
            if (rootValidator.autorepair) {
                header.gedcomVersion = new GedcomVersion();
                rootValidator.addInfo("GEDCOM version in header was null - repaired", header);
            } else {
                rootValidator.addError("GEDCOM version in heaeder must be specified", header);
                return;
            }
        }
        checkCustomTags(header.gedcomVersion);
        checkStringWithCustomTags(header.language);
        if (header.notes == null) {
            if (rootValidator.autorepair) {
                header.notes = new ArrayList<String>();
                rootValidator.addInfo("Notes collection in header was null - repaired", header);
            } else {
                rootValidator.addError("Notes collection in header is null - must be at least an empty collection",
                        header);
            }
        }
        checkStringWithCustomTags(header.placeHierarchy);
        checkSourceSystem();

        /*
         * header.placeHierarchy; header.sourceSystem; header.submission;
         * header.submitter; header.time;
         */
    }
}
