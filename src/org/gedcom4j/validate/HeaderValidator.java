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

import org.gedcom4j.io.Encoding;
import org.gedcom4j.model.CharacterSet;
import org.gedcom4j.model.Header;
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
        checkCustomTags(header.characterSet.characterSetName);
        checkCustomTags(header.characterSet.versionNum);
    }

    /**
     * Validate the {@link Header}
     * 
     * @see org.gedcom4j.validate.AbstractValidator#validate()
     */
    @Override
    protected void validate() {
        checkCharacterSet();
        /*
         * header.copyrightData; header.customTags; header.date;
         * header.destinationSystem; header.fileName; header.gedcomVersion;
         * header.language; header.notes; header.placeHierarchy;
         * header.sourceSystem; header.submission; header.submitter;
         * header.time;
         */
    }
}
