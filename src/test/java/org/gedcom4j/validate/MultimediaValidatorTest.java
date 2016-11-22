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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.gedcom4j.model.CitationWithSource;
import org.gedcom4j.model.CitationWithoutSource;
import org.gedcom4j.model.Header;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.MultimediaReference;
import org.gedcom4j.model.Source;
import org.gedcom4j.model.Submission;
import org.gedcom4j.model.SubmissionReference;
import org.gedcom4j.model.Submitter;
import org.gedcom4j.model.SubmitterReference;
import org.gedcom4j.model.enumerations.SupportedVersion;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link MultimediaValidator}
 * 
 * @author frizbog1
 */
public class MultimediaValidatorTest extends AbstractValidatorTestCase {

    /**
     * Test fixture
     */
    private Multimedia mm;

    /**
     * Set up the test
     */
    @Before
    public void setUp() {
        validator.setAutoRepairResponder(Validator.AUTO_REPAIR_NONE);
        Submitter s = new Submitter();
        s.setXref("@SUBM0001@");
        s.setName("test");
        gedcom.getSubmitters().put(s.getXref(), s);
        gedcom.setSubmission(new Submission("@SUBN0001@"));
        Header h = gedcom.getHeader();
        h.setSubmitterReference(new SubmitterReference(s));
        h.setSubmissionReference(new SubmissionReference(gedcom.getSubmission()));

        mm = new Multimedia();
    }

    /**
     * Test stuff with citations which is not allowed under gedcom 5.5
     */
    @Test
    public void testAutoRepairBlobAndEmbeddedMediaFormat() {
        gedcom.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
        validator.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);

        Multimedia m = new Multimedia();
        m.setXref("@M1@");
        gedcom.getMultimedia().put(m.getXref(), m);
        m.getBlob(true).add("Foo");
        m.setEmbeddedMediaFormat("PNG");

        MultimediaValidator mv = new MultimediaValidator(validator, m);
        assertNotNull(mv);
        mv.validate();
        assertFindingsContain(Severity.ERROR, Multimedia.class, ProblemCode.NOT_ALLOWED_IN_GEDCOM_551.getCode(), "blob");
        assertFindingsContain(Severity.ERROR, Multimedia.class, ProblemCode.NOT_ALLOWED_IN_GEDCOM_551.getCode(),
                "embeddedMediaFormat");
        assertNull(m.getEmbeddedMediaFormat());
        assertTrue(m.getBlob(true).isEmpty());
    }

    /**
     * Test stuff with citations which is not allowed under gedcom 5.5
     */
    @Test
    public void testCitations() {
        gedcom.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);

        Multimedia m = new Multimedia();
        m.setXref("@M1@");
        gedcom.getMultimedia().put(m.getXref(), m);
        m.getBlob(true).add("Foo");
        m.setEmbeddedMediaFormat("PNG");

        m.getCitations(true).add(new CitationWithoutSource());
        CitationWithSource cws = new CitationWithSource();
        cws.setSource(new Source());
        m.getCitations().add(cws);

        validator.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);
        MultimediaValidator mv = new MultimediaValidator(validator, m);
        assertNotNull(mv);

        mv.validate();
        assertFindingsContain(Severity.ERROR, Multimedia.class, ProblemCode.NOT_ALLOWED_IN_GEDCOM_55.getCode(), "citations");
    }

    /**
     * Test stuff with continued objects which is not allowed under gedcom 5.5
     */
    @Test
    public void testContinuedObject() {
        gedcom.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);

        Multimedia m = new Multimedia();
        m.setXref("@M1@");
        gedcom.getMultimedia().put(m.getXref(), m);
        m.getBlob(true).add("Foo");
        m.setEmbeddedMediaFormat("PNG");

        Multimedia m2 = new Multimedia();
        m2.setXref("@M2@");
        gedcom.getMultimedia().put(m2.getXref(), m2);
        m2.getBlob(true).add("Foo");
        m2.setEmbeddedMediaFormat("PNG");

        MultimediaReference mr = new MultimediaReference();
        mr.setMultimedia(m2);
        m.setContinuedObject(mr);

        m2.setContinuedObject(new MultimediaReference());

        MultimediaValidator mv = new MultimediaValidator(validator, m);
        assertNotNull(mv);
        mv.validate();
        assertNoIssues();
    }

    /**
     * Test the stuff for embedded media
     */
    @Test
    public void testEmbeddedMedia() {
        mm.setXref("@MM001@");
        gedcom.getMultimedia().put(mm.getXref(), mm);

        // Blob empty in 5.5.1 is expected
        gedcom.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
        validator.validate();
        assertNoIssues();

        // Blob must be populated in v5.5, and must have a format
        gedcom.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5);
        validator.validate();
        assertFindingsContain(Severity.ERROR, mm, ProblemCode.MISSING_REQUIRED_VALUE, "blob");
        mm.getBlob(true).add("foo");
        mm.setEmbeddedMediaFormat("gif");
        validator.validate();
        assertNoIssues();

        // Blob must be empty in 5.5.1, and embedded media format must be null
        gedcom.getHeader().getGedcomVersion().setVersionNumber(SupportedVersion.V5_5_1);
        validator.validate();
        assertFindingsContain(Severity.ERROR, mm, ProblemCode.NOT_ALLOWED_IN_GEDCOM_551, "blob");
        assertFindingsContain(Severity.ERROR, mm, ProblemCode.NOT_ALLOWED_IN_GEDCOM_551, "embeddedMediaFormat");

        mm.getBlob().clear();
        mm.setEmbeddedMediaFormat((String) null);
        validator.validate();
        assertNoIssues();

    }

    /**
     * Test validating without a gedcom version
     */
    @Test
    public void testNoGedcomVersion1() {
        validator.getGedcom().setHeader(null);
        validator.setAutoRepairResponder(Validator.AUTO_REPAIR_NONE);
        MultimediaValidator mv = new MultimediaValidator(validator, new Multimedia());
        assertNotNull(mv);
        mv.validate();
        assertFindingsContain(Severity.INFO, Multimedia.class, ProblemCode.UNABLE_TO_DETERMINE_GEDCOM_VERSION.getCode(), null);
    }

    /**
     * Test validating without a gedcom version
     */
    @Test
    public void testNoGedcomVersion2() {
        validator.getGedcom().getHeader().setGedcomVersion(null);
        validator.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);
        MultimediaValidator mv = new MultimediaValidator(validator, new Multimedia());
        assertNotNull(mv);
        mv.validate();
        assertFindingsContain(Severity.INFO, Multimedia.class, ProblemCode.UNABLE_TO_DETERMINE_GEDCOM_VERSION.getCode(), null);
    }

    /**
     * Test validating without a gedcom version
     */
    @Test
    public void testNoGedcomVersion3() {
        validator.getGedcom().getHeader().getGedcomVersion().setVersionNumber((String) null);
        validator.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);
        MultimediaValidator mv = new MultimediaValidator(validator, new Multimedia());
        assertNotNull(mv);
        mv.validate();
        assertFindingsContain(Severity.INFO, Multimedia.class, ProblemCode.UNABLE_TO_DETERMINE_GEDCOM_VERSION.getCode(), null);
    }
}
