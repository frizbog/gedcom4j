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

import org.gedcom4j.Options;
import org.gedcom4j.io.encoding.Encoding;
import org.gedcom4j.model.CharacterSet;
import org.gedcom4j.model.Corporation;
import org.gedcom4j.model.GedcomVersion;
import org.gedcom4j.model.Header;
import org.gedcom4j.model.HeaderSourceData;
import org.gedcom4j.model.SourceSystem;
import org.gedcom4j.model.StringWithCustomTags;
import org.gedcom4j.model.SupportedVersion;
import org.gedcom4j.validate.Validator.Finding;

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
     * @param validator
     *            the main validator
     * @param header
     *            the {@link Header} being validated
     */
    HeaderValidator(Validator validator, Header header) {
        this.validator = validator;
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
        if (header.getCopyrightData() == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(header, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, "copyrightData");
            if (validator.mayRepair(vf)) {
                Header before = new Header(header);
                header.getCopyrightData(true).clear();
                vf.addRepair(new AutoRepair(before, new Header(header)));
            }
        }
        checkCustomTags(header);
        stringWithCustomTagsMustHaveValueOrBeOmitted(header, "date");
        stringWithCustomTagsMustHaveValueOrBeOmitted(header, "destinationSystem");
        /*
         * Filename is actually a required field -- but since the writer automatically fills in the filename if it's blank, treating
         * it as optional here
         */
        stringWithCustomTagsMustHaveValueOrBeOmitted(header, "fileName");
        checkGedcomVersion();
        stringWithCustomTagsMustHaveValueOrBeOmitted(header, "language");
        new NotesValidator(validator, header, header.getNotes()).validate();
        stringWithCustomTagsMustHaveValueOrBeOmitted(header, "placeHierarchy");
        checkSourceSystem();
        if (header.getSubmitter() == null) {
            if (validator.mayRepair(vf)) {
                if (validator.gedcom.getSubmitters() == null || validator.gedcom.getSubmitters().isEmpty()) {
                    validator.addError("Submitter not specified in header, and autorepair could not "
                            + "find a submitter to select as default", header);
                } else {
                    // Take the first submitter from the collection and set that
                    // as the primary submitter in the header
                    header.setSubmitter(validator.gedcom.getSubmitters().values().iterator().next());
                }
            } else {
                validator.addError("Submitter not specified in header", header);
            }
            return;
        }
        new SubmitterValidator(validator, header.getSubmitter()).validate();
        if (header.getSubmission() != null) {
            validator.validateSubmission(header.getSubmission());
        }
        stringWithCustomTagsMustHaveValueOrBeOmitted(header, "time");
    }

    /**
     * Check the character set
     */
    private void checkCharacterSet() {
        if (header.getCharacterSet() == null) {
            Finding vf = validator.newFinding(header, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, "characterSet");
            if (validator.mayRepair(vf)) {
                Header before = new Header(header);
                header.setCharacterSet(new CharacterSet());
                vf.addRepair(new AutoRepair(before, new Header(header)));
            } else {
                return;
            }
        }
        if (header.getCharacterSet().getCharacterSetName() == null) {
            Finding vf = validator.newFinding(header.getCharacterSet(), Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE,
                    "characterSetName");
            if (validator.mayRepair(vf)) {
                CharacterSet before = new CharacterSet(header.getCharacterSet());
                header.getCharacterSet().setCharacterSetName(new StringWithCustomTags("ANSEL"));
                vf.addRepair(new AutoRepair(before, new CharacterSet(header.getCharacterSet())));
            } else {
                return;
            }
        }
        if (!Encoding.isValidCharacterSetName(header.getCharacterSet().getCharacterSetName().getValue())) {
            validator.newFinding(header.getCharacterSet().getCharacterSetName(), Severity.ERROR, ProblemCode.ILLEGAL_VALUE,
                    "value");
        }
        stringWithCustomTagsMustHaveValueOrBeOmitted(header.getCharacterSet(), "characterSetName");
        stringWithCustomTagsMustHaveValueOrBeOmitted(header.getCharacterSet(), "versionNum");
        checkCustomTags(header.getCharacterSet());
    }

    /**
     * Check the gedcom version
     */
    private void checkGedcomVersion() {
        GedcomVersion gv = header.getGedcomVersion();
        if (gv == null) {
            Finding vf = validator.newFinding(header, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, "gedcomVersion");
            if (validator.mayRepair(vf)) {
                Header before = new Header(header);
                header.setGedcomVersion(new GedcomVersion());
                vf.addRepair(new AutoRepair(before, new Header(header)));
            }
        } else {
            if (gv.getVersionNumber() == null) {
                Finding vf = validator.newFinding(gv, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, "versionNumber");
                if (validator.mayRepair(vf)) {
                    GedcomVersion before = new GedcomVersion(gv);
                    gv.setVersionNumber(SupportedVersion.V5_5_1);
                    vf.addRepair(new AutoRepair(before, new GedcomVersion(gv)));
                }
            }
            checkCustomTags(gv);
        }
    }

    /**
     * Check the source system
     */
    private void checkSourceSystem() {
        SourceSystem ss = header.getSourceSystem();
        if (ss == null) {
            if (validator.mayRepair(vf)) {
                ss = new SourceSystem();
                header.setSourceSystem(ss);
                validator.addInfo("No source system specified in header - repaired", header);
            } else {
                validator.addError("No source system specified in header", header);
                return;
            }
        }
        checkCustomTags(ss);
        if (ss.getCorporation() != null) {
            Corporation c = ss.getCorporation();
            checkCustomTags(c);
            if (c.getAddress() != null) {
                new AddressValidator(validator, c.getAddress()).validate();
            }
            if (c.getBusinessName() == null || c.getBusinessName().trim().length() == 0) {
                if (validator.mayRepair(vf)) {
                    c.setBusinessName("UNSPECIFIED");
                    validator.addInfo("Corporation for source system exists but had no name - repaired", c);
                } else {
                    validator.addError("Corporation for source system exists but has no name", c);
                }
            }
        }
        stringWithCustomTagsMustHaveValueOrBeOmitted(ss, "productName");
        if (ss.getSourceData() != null) {
            HeaderSourceData sd = ss.getSourceData();
            if (sd.getName() == null || sd.getName().trim().length() == 0) {
                if (validator.mayRepair(vf)) {
                    sd.setName("UNSPECIFIED");
                    validator.addInfo("Source data was specified for source system, "
                            + "but name of source data was not specified - repaired", sd);
                } else {
                    validator.addError("Source data is specified for source system, " + "but name of source data is not specified",
                            sd);
                }

            }
            stringWithCustomTagsMustHaveValueOrBeOmitted(sd, "copyright");
            stringWithCustomTagsMustHaveValueOrBeOmitted(sd, "publishDate");
            checkCustomTags(sd);
        }
        if (ss.getSystemId() == null) {
            if (validator.mayRepair(vf)) {
                ss.setSystemId("UNSPECIFIED");
                validator.addInfo("System ID was not specified in source system in header - repaired", ss);
            } else {
                validator.addError("System ID must be specified in source system in header", ss);
            }
        }
        stringWithCustomTagsMustHaveValueOrBeOmitted(ss, "versionNum");
    }

}
