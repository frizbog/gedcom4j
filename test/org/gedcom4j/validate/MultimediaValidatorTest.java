/*
 * Copyright (c) 2009-2014 Matthew R. Harrah
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
        rootValidator.autorepair = false;
        Submitter s = new Submitter();
        s.xref = "@SUBM0001@";
        s.name = new StringWithCustomTags("test");
        g.submitters.put(s.xref, s);
        g.submission = new Submission("@SUBN0001@");
        Header h = g.header;
        h.submitter = s;
        h.submission = g.submission;

        mm = new Multimedia();
    }

    /**
     * Test the stuff for embedded media
     */
    @Test
    public void testEmbeddedMedia() {
        mm.xref = "@MM001@";
        rootValidator.gedcom.multimedia.put(mm.xref, mm);

        // Blob object must be instantiated regardless of version!
        mm.blob = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "blob", "null");
        mm.blob = new ArrayList<String>();

        // Blob can be empty in 5.5.1
        rootValidator.gedcom.header.gedcomVersion.versionNumber = SupportedVersion.V5_5_1;
        rootValidator.validate();
        assertNoIssues();

        // Blob must be populated in v5.5, and must have a format
        rootValidator.gedcom.header.gedcomVersion.versionNumber = SupportedVersion.V5_5;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "blob", "empty");
        mm.blob.add("foo");
        mm.embeddedMediaFormat = new StringWithCustomTags("gif");
        rootValidator.validate();
        assertNoIssues();

        // Blob must be empty in 5.5.1, and embedded media format must be null
        rootValidator.gedcom.header.gedcomVersion.versionNumber = SupportedVersion.V5_5_1;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "blob", "populated", "5.5.1");
        assertFindingsContain(Severity.ERROR, "embedded", "media", "format", "5.5.1");

        mm.blob.clear();
        mm.embeddedMediaFormat = null;
        rootValidator.validate();
        assertNoIssues();

    }
}
