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
import org.gedcom4j.model.*;
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
        Gedcom g = new Gedcom();
        rootValidator.gedcom = g;
        rootValidator.setAutorepairEnabled(false);
        Submitter s = new Submitter();
        s.setXref("@SUBM0001@");
        s.setName(new StringWithCustomTags("test"));
        g.getSubmitters().put(s.getXref(), s);
        g.setSubmission(new Submission("@SUBN0001@"));
        Header h = g.getHeader();
        h.setSubmitter(s);
        h.setSubmission(g.getSubmission());

        h.setCharacterSet(null);

        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "character set");

        h.setCharacterSet(new CharacterSet());
        rootValidator.validate();
        assertNoIssues();

        h.getCharacterSet().setCharacterSetName(null);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "character set", "name", "not", "defined");

        h.getCharacterSet().setCharacterSetName(new StringWithCustomTags("FRYINGPAN"));
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "character set", "not", "supported");

        h.getCharacterSet().setCharacterSetName(new StringWithCustomTags(Encoding.ASCII.getCharacterSetName()));
        rootValidator.validate();
        assertNoIssues();

    }

    /**
     * Test for copyright data in header
     */
    @Test
    public void testCopyrightData() {
        Gedcom g = new Gedcom();
        rootValidator.gedcom = g;
        rootValidator.setAutorepairEnabled(false);
        Submitter s = new Submitter();
        s.setXref("@SUBM0001@");
        s.setName(new StringWithCustomTags("test"));
        g.getSubmitters().put(s.getXref(), s);
        Header h = g.getHeader();
        h.setSubmitter(s);
        g.setSubmission(new Submission("@SUBN0001@"));
        h.getCopyrightData(true);

        rootValidator.validate();
        assertNoIssues();

    }

    /**
     * Test for Gedcom version in header
     */
    @Test
    public void testGedcomVersion() {
        Gedcom g = new Gedcom();
        rootValidator.gedcom = g;
        rootValidator.setAutorepairEnabled(false);
        Submitter s = new Submitter();
        s.setXref("@SUBM0001@");
        s.setName(new StringWithCustomTags("test"));
        g.getSubmitters().put(s.getXref(), s);
        Header h = g.getHeader();
        h.setSubmitter(s);
        g.setSubmission(new Submission("@SUBN0001@"));
        h.setSubmission(g.getSubmission());

        h.setGedcomVersion(null);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "gedcom", "version");

        h.setGedcomVersion(new GedcomVersion());
        rootValidator.validate();
        assertNoIssues();

        h.getGedcomVersion().setVersionNumber(null);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "gedcom", "version", "number");
    }

    /**
     * Test header without submitters
     */
    @Test
    public void testNoSubmitters() {
        Gedcom g = new Gedcom();
        rootValidator.gedcom = g;
        g.setSubmission(new Submission("@SUBN0001@"));
        g.getHeader().setSubmission(g.getSubmission());

        rootValidator.setAutorepairEnabled(false);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "submitter", "not specified");

        Submitter s = new Submitter();
        s.setXref("@SUBM0001@");
        s.setName(new StringWithCustomTags("test"));
        g.getSubmitters().put(s.getXref(), s);
        Header h = g.getHeader();
        h.setSubmitter(s);

        rootValidator.validate();
        assertNoIssues();

    }

    /**
     * Test for sourceSystem in header
     */
    @Test
    public void testSourceSystem() {
        Gedcom g = new Gedcom();
        rootValidator.gedcom = g;
        rootValidator.setAutorepairEnabled(false);
        Submitter s = new Submitter();
        s.setXref("@SUBM0001@");
        s.setName(new StringWithCustomTags("test"));
        g.getSubmitters().put(s.getXref(), s);
        Header h = g.getHeader();
        h.setSubmitter(s);
        g.setSubmission(new Submission("@SUBN0001@"));
        h.setSubmission(g.getSubmission());

        h.setSourceSystem(null);

        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "source system", "specified");

        h.setSourceSystem(new SourceSystem());
        rootValidator.validate();
        assertNoIssues();

        SourceSystem ss = h.getSourceSystem();
        ss.setCorporation(null);
        rootValidator.validate();
        assertNoIssues();

        ss.setCorporation(new Corporation());
        rootValidator.validate();
        assertNoIssues();

        ss.getCorporation().setBusinessName(null);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "source system", "corporation", "name");

        ss.getCorporation().setBusinessName("Frying Pan");
        rootValidator.validate();
        assertNoIssues();

        ss.setProductName(new StringWithCustomTags("Yo"));
        rootValidator.validate();
        assertNoIssues();

        ss.setProductName(null);
        rootValidator.validate();
        assertNoIssues();

        ss.setSystemId(null);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "source system", "system", "id");

        ss.setSystemId("Test");
        rootValidator.validate();
        assertNoIssues();
    }
}
