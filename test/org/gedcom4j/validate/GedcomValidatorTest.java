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

import java.io.IOException;

import org.gedcom4j.model.Gedcom;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.parser.GedcomParserException;

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

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

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
        assertTrue("There should be no findings on an empty Gedcom", rootValidator.findings.isEmpty());
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
        assertTrue(rootValidator.findings.isEmpty());
    }

}
