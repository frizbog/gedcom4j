package com.mattharrah.gedcom4j.validate;

import java.io.IOException;

import com.mattharrah.gedcom4j.model.Gedcom;
import com.mattharrah.gedcom4j.parser.GedcomParser;
import com.mattharrah.gedcom4j.parser.GedcomParserException;

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
        assertNull(
                "Individuals collection should still be null since it was not repaired",
                g.individuals);
        assertFalse(
                "Whether or not autorepair is on, there should be findings",
                v.findings.isEmpty());
        for (GedcomValidationFinding f : v.findings) {
            assertEquals("With autorepair off, findings should be at error",
                    Severity.ERROR, f.severity);
        }

        // Do it again, only this time with autorepair on
        v = new GedcomValidator(g);
        v.autorepair = true;
        v.validate();
        assertNotNull("Individuals collection should have been repaired",
                g.individuals);
        assertFalse(
                "Whether or not autorepair is on, there should be findings",
                v.findings.isEmpty());
        for (GedcomValidationFinding f : v.findings) {
            assertEquals("With autorepair off, findings should be at INFO",
                    Severity.INFO, f.severity);
        }
    }

    /**
     * Test for {@link GedcomValidator#validateIndividuals()} with default,
     * empty {@link Gedcom} structure.
     * 
     */
    public void testValidateEmptyGedcom() {
        Gedcom g = new Gedcom();
        GedcomValidator v = new GedcomValidator(g);
        v.validate();
        dumpFindings();
        assertTrue("There should be no findings on an empty Gedcom",
                v.findings.isEmpty());
    }

    /**
     * Validate the stress test file
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if the file can't be parsed
     */
    public void testValidateStressTestFile() throws IOException,
            GedcomParserException {
        // Load a file
        GedcomParser p = new GedcomParser();
        p.load(SAMPLE_STRESS_TEST_FILENAME);
        rootValidator = new GedcomValidator(p.gedcom);
        rootValidator.validate();
        dumpFindings();
        assertTrue(rootValidator.findings.isEmpty());
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

}
