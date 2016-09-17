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

import org.gedcom4j.io.encoding.Encoding;
import org.gedcom4j.model.CharacterSet;
import org.gedcom4j.model.Corporation;
import org.gedcom4j.model.GedcomVersion;
import org.gedcom4j.model.Header;
import org.gedcom4j.model.SourceSystem;
import org.gedcom4j.model.StringWithCustomTags;
import org.gedcom4j.model.Submission;
import org.gedcom4j.model.Submitter;
import org.junit.Test;

/**
 * Test case for {@link HeaderValidator}
 * 
 * @author mharrah
 * 
 */
public class HeaderValidatorTest extends AbstractValidatorTestCase {

    /**
     * Test for character set in header
     */
    @Test
    public void testCharacterSet() {

        Submitter s = new Submitter();
        s.setXref("@SUBM0001@");
        s.setName("test");
        gedcom.getSubmitters().put(s.getXref(), s);
        gedcom.setSubmission(new Submission("@SUBN0001@"));
        Header h = gedcom.getHeader();
        h.setSubmitter(s);
        h.setSubmission(gedcom.getSubmission());

        h.setCharacterSet(null);

        validator.validate();
        assertFindingsContain(Severity.ERROR, h, ProblemCode.MISSING_REQUIRED_VALUE.getCode(), "characterSet");

        h.setCharacterSet(new CharacterSet());
        validator.validate();
        assertNoIssues();

        h.getCharacterSet().setCharacterSetName((String) null);
        validator.validate();
        assertFindingsContain(Severity.ERROR, h.getCharacterSet(), ProblemCode.MISSING_REQUIRED_VALUE.getCode(),
                "characterSetName");

        h.getCharacterSet().setCharacterSetName("FRYINGPAN");
        validator.validate();
        assertFindingsContain(Severity.ERROR, h.getCharacterSet().getCharacterSetName(), ProblemCode.ILLEGAL_VALUE.getCode(),
                "value");

        h.getCharacterSet().setCharacterSetName(new StringWithCustomTags(Encoding.ASCII.getCharacterSetName()));
        validator.validate();
        assertNoIssues();

    }

    /**
     * Test for copyright data in header
     */
    @Test
    public void testCopyrightData() {
        Submitter s = new Submitter();
        s.setXref("@SUBM0001@");
        s.setName("test");
        gedcom.getSubmitters().put(s.getXref(), s);
        Header h = gedcom.getHeader();
        h.setSubmitter(s);
        gedcom.setSubmission(new Submission("@SUBN0001@"));
        h.getCopyrightData(true);

        validator.validate();
        assertNoIssues();

    }

    /**
     * Test for Gedcom version in header
     */
    @Test
    public void testGedcomVersion() {
        Submitter s = new Submitter();
        s.setXref("@SUBM0001@");
        s.setName("test");
        gedcom.getSubmitters().put(s.getXref(), s);
        Header h = gedcom.getHeader();
        h.setSubmitter(s);
        gedcom.setSubmission(new Submission("@SUBN0001@"));
        h.setSubmission(gedcom.getSubmission());

        h.setGedcomVersion(null);
        validator.validate();
        assertFindingsContain(Severity.ERROR, h, ProblemCode.MISSING_REQUIRED_VALUE.getCode(), "gedcomVersion");

        h.setGedcomVersion(new GedcomVersion());
        validator.validate();
        assertNoIssues();

        h.getGedcomVersion().setVersionNumber(null);
        validator.validate();
        assertFindingsContain(Severity.ERROR, h.getGedcomVersion(), ProblemCode.MISSING_REQUIRED_VALUE.getCode(), "versionNumber");
    }

    /**
     * Test header without submitters
     */
    @Test
    public void testNoSubmitters() {
        gedcom.getHeader().setSubmitter(null);
        validator.validate();
        assertFindingsContain(Severity.ERROR, gedcom.getHeader(), ProblemCode.MISSING_REQUIRED_VALUE.getCode(), "submitter");

        Submitter s = new Submitter();
        s.setXref("@SUBM0001@");
        s.setName("test");
        gedcom.getSubmitters().put(s.getXref(), s);
        Header h = gedcom.getHeader();
        h.setSubmitter(s);

        validator.validate();
        assertNoIssues();

    }

    /**
     * Test for sourceSystem in header
     */
    @Test
    public void testSourceSystem() {
        Submitter s = new Submitter();
        s.setXref("@SUBM0001@");
        s.setName("test");
        gedcom.getSubmitters().put(s.getXref(), s);
        Header h = gedcom.getHeader();
        h.setSubmitter(s);
        gedcom.setSubmission(new Submission("@SUBN0001@"));
        h.setSubmission(gedcom.getSubmission());

        h.setSourceSystem(null);

        validator.validate();
        assertFindingsContain(Severity.ERROR, h, ProblemCode.MISSING_REQUIRED_VALUE.getCode(), "sourceSystem");

        h.setSourceSystem(new SourceSystem());
        validator.validate();
        assertNoIssues();

        SourceSystem ss = h.getSourceSystem();
        ss.setCorporation(null);
        validator.validate();
        assertNoIssues();

        ss.setCorporation(new Corporation());
        validator.validate();
        assertNoIssues();

        ss.getCorporation().setBusinessName(null);
        validator.validate();
        assertFindingsContain(Severity.ERROR, ss.getCorporation(), ProblemCode.MISSING_REQUIRED_VALUE.getCode(), "businessName");

        ss.getCorporation().setBusinessName("Frying Pan");
        validator.validate();
        assertNoIssues();

        ss.setProductName("Yo");
        validator.validate();
        assertNoIssues();

        ss.setProductName((String) null);
        validator.validate();
        assertNoIssues();

        ss.setSystemId(null);
        validator.validate();
        assertFindingsContain(Severity.ERROR, ss, ProblemCode.MISSING_REQUIRED_VALUE.getCode(), "systemId");

        ss.setSystemId("Test");
        validator.validate();
        assertNoIssues();
    }
}
