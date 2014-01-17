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

import java.io.IOException;

import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.StringWithCustomTags;
import org.gedcom4j.model.Submission;
import org.gedcom4j.model.Submitter;
import org.gedcom4j.model.Trailer;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.parser.GedcomParserException;
import org.junit.Test;

/**
 * Test for {@link GedcomValidator}
 * 
 * @author frizbog1
 */
public class GedcomValidatorTest extends AbstractValidatorTestCase {

    /**
     * The name of the file used for stress-testing the parser
     */
    private static final String SAMPLE_STRESS_TEST_FILENAME = "sample/TGC551.ged";

    /**
     * Test autorepairing
     */
    public void testAutoRepair() {
        Gedcom g = new Gedcom();

        // Deliberately introduce error
        g.individuals = null;

        // Go validate
        GedcomValidator v = new GedcomValidator(g);
        v.autorepair = false;
        v.validate();
        assertNull("Individuals collection should still be null since it was not repaired", g.individuals);
        assertFalse("Whether or not autorepair is on, there should be findings", v.findings.isEmpty());
        for (GedcomValidationFinding f : v.findings) {
            assertEquals("With autorepair off, findings should be at error", Severity.ERROR, f.severity);
        }

        // Do it again, only this time with autorepair on
        v = new GedcomValidator(g);
        v.autorepair = true;
        verbose = true;
        v.validate();
        assertNotNull("Individuals collection should have been repaired", g.individuals);
        assertFalse("Whether or not autorepair is on, there should be findings", v.findings.isEmpty());
        dumpFindings();
        assertSame(v.findings, v.rootValidator.findings);
        for (GedcomValidationFinding f : v.rootValidator.findings) {
            assertEquals("With autorepair on, findings should be at INFO", Severity.INFO, f.severity);
        }
    }

    /**
     * Test for character set in header
     */
    @Test
    public void testTrailer() {
        Gedcom g = new Gedcom();
        rootValidator.gedcom = g;
        rootValidator.autorepair = false;
        Submitter s = new Submitter();
        s.xref = "@SUBM0001@";
        s.name = new StringWithCustomTags("test");
        g.submitters.put(s.xref, s);
        g.submission = new Submission("@SUBN0001@");
        g.header.submitter = s;

        g.trailer = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "trailer");

        g.trailer = new Trailer();
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test for {@link GedcomValidator#validateIndividuals()} with default,
     * empty {@link Gedcom} structure.
     * 
     */
    public void testValidateEmptyGedcom() {
        Gedcom g = new Gedcom();
        rootValidator = new GedcomValidator(g);
        verbose = true;
        rootValidator.validate();
        dumpFindings();
        assertTrue(
                "A new gedcom structure run through the validator with autorepair on should always have at least one finding",
                rootValidator.findings.size() > 0);
        for (GedcomValidationFinding f : rootValidator.findings) {
            assertEquals(
                    "All findings on a new gedcom structure run through the validator with autorepair on should be at level of INFO",
                    Severity.INFO, f.severity);
        }
    }

    /**
     * Validate the stress test file
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if the file can't be parsed
     */
    public void testValidateStressTestFile() throws IOException, GedcomParserException {
        // Load a file
        GedcomParser p = new GedcomParser();
        p.load(SAMPLE_STRESS_TEST_FILENAME);
        assertTrue(p.errors.isEmpty());
        rootValidator = new GedcomValidator(p.gedcom);
        rootValidator.validate();
        dumpFindings();
        /*
         * The stress test file has an error in it - it says it's a 5.5 file,
         * but uses a file-reference type multimedia object, rather than an
         * embedded media file
         */
        assertFindingsContain(Severity.ERROR, "format", "embedded", "media");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

}
