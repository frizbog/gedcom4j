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

import org.gedcom4j.model.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link GedcomValidator}
 * 
 * @author frizbog1
 */
public class MultimediaValidatorTest extends AbstractValidatorTestCase {

    /**
     * Test fixture
     */
    private Multimedia mm;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
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

        mm = new Multimedia();
    }

    /**
     * Test the stuff for embedded media
     */
    @Test
    public void testEmbeddedMedia() {
        mm.setXref("@MM001@");
        rootValidator.gedcom.getMultimedia().put(mm.getXref(), mm);

        // Blob can be empty in 5.5.1
        rootValidator.gedcom.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
        rootValidator.validate();
        assertNoIssues();

        // Blob must be populated in v5.5, and must have a format
        rootValidator.gedcom.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "blob", "empty");
        mm.getBlob(true).add("foo");
        mm.setEmbeddedMediaFormat(new StringWithCustomTags("gif"));
        rootValidator.validate();
        assertNoIssues();

        // Blob must be empty in 5.5.1, and embedded media format must be null
        rootValidator.gedcom.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "blob", "populated", "5.5.1");
        assertFindingsContain(Severity.ERROR, "embedded", "media", "format", "5.5.1");

        mm.getBlob().clear();
        mm.setEmbeddedMediaFormat(null);
        rootValidator.validate();
        assertNoIssues();

    }
}
