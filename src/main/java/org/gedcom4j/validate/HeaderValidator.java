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
import org.gedcom4j.model.Submitter;
import org.gedcom4j.model.SupportedVersion;
import org.gedcom4j.validate.Validator.Finding;

/**
 * Validator for a {@link Header}. See {@link Validator} for usage information.
 * 
 * @author frizbog1
 * 
 */
class HeaderValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 6219406585813356753L;

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
        mustHaveValueOrBeOmitted(header, "date");
        mustBeDateIfSpecified(header, "date");
        mustHaveValueOrBeOmitted(header, "destinationSystem");
        /*
         * Filename is actually a required field -- but since the writer automatically fills in the filename if it's blank, treating
         * it as optional here
         */
        mustHaveValueOrBeOmitted(header, "fileName");
        checkGedcomVersion();
        mustHaveValueOrBeOmitted(header, "language");
        new NotesListValidator(validator, header).validate();
        mustHaveValueOrBeOmitted(header, "placeHierarchy");
        checkSourceSystem();
        checkSubmitter();
        mustHaveValueOrBeOmitted(header, "time");
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
                header.getCharacterSet().setCharacterSetName("ANSEL");
                vf.addRepair(new AutoRepair(before, new CharacterSet(header.getCharacterSet())));
            } else {
                return;
            }
        }
        if (!Encoding.isValidCharacterSetName(header.getCharacterSet().getCharacterSetName().getValue())) {
            validator.newFinding(header.getCharacterSet().getCharacterSetName(), Severity.ERROR, ProblemCode.ILLEGAL_VALUE,
                    "value");
        }
        mustHaveValueOrBeOmitted(header.getCharacterSet(), "characterSetName");
        mustHaveValueOrBeOmitted(header.getCharacterSet(), "versionNum");
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
            Finding vf = validator.newFinding(header, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, "sourceSystem");
            if (validator.mayRepair(vf)) {
                Header before = new Header(header);
                ss = new SourceSystem();
                header.setSourceSystem(ss);
                vf.addRepair(new AutoRepair(before, new Header(header)));
            } else {
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
            if (c.getBusinessName() == null || !isSpecified(c.getBusinessName())) {
                Finding vf = validator.newFinding(c, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, "businessName");
                if (validator.mayRepair(vf)) {
                    Corporation before = new Corporation(c);
                    c.setBusinessName("UNSPECIFIED");
                    vf.addRepair(new AutoRepair(before, new Corporation(c)));
                }
            }
        }
        mustHaveValueOrBeOmitted(ss, "productName");
        if (ss.getSourceData() != null) {
            HeaderSourceData sd = ss.getSourceData();
            if (sd.getName() == null || sd.getName().trim().length() == 0) {
                Finding vf = validator.newFinding(sd, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, "name");
                if (validator.mayRepair(vf)) {
                    HeaderSourceData before = new HeaderSourceData(sd);
                    sd.setName("UNSPECIFIED");
                    vf.addRepair(new AutoRepair(before, new HeaderSourceData(sd)));
                }
            }
            mustHaveValueOrBeOmitted(sd, "copyright");
            mustBeDateIfSpecified(sd, "publishDate");
            checkCustomTags(sd);
        }
        if (ss.getSystemId() == null) {
            Finding vf = validator.newFinding(ss, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, "systemId");
            if (validator.mayRepair(vf)) {
                SourceSystem before = new SourceSystem(ss);
                ss.setSystemId("UNSPECIFIED");
                vf.addRepair(new AutoRepair(before, new SourceSystem(ss)));
            }
        }
        mustHaveValueOrBeOmitted(ss, "versionNum");
    }

    /**
     * Check if the submitter is present and ok
     */
    private void checkSubmitter() {
        if (header.getSubmitter() == null) {
            Finding vf = validator.newFinding(header, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, "submitter");
            Submitter submitter = null;
            if (validator.getGedcom().getSubmitters() != null && !validator.getGedcom().getSubmitters().isEmpty()) {
                submitter = validator.getGedcom().getSubmitters().values().iterator().next();
            }
            if (submitter != null && validator.mayRepair(vf)) {
                Header before = new Header(header);
                header.setSubmitter(submitter);
                vf.addRepair(new AutoRepair(before, new Header(header)));
            }
        }
        if (header.getSubmitter() != null) {
            new SubmitterValidator(validator, header.getSubmitter()).validate();
        }
    }

}
