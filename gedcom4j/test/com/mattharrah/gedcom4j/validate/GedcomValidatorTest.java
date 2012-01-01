package com.mattharrah.gedcom4j.validate;

import java.io.IOException;
import java.util.Map;

import junit.framework.TestCase;

import com.mattharrah.gedcom4j.Gedcom;
import com.mattharrah.gedcom4j.Individual;
import com.mattharrah.gedcom4j.parser.GedcomParser;
import com.mattharrah.gedcom4j.parser.GedcomParserException;

/**
 * Test for {@link GedcomValidator}
 * 
 * @author frizbog1
 */
public class GedcomValidatorTest extends TestCase {

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
        GedcomValidator v = new GedcomValidator(p.gedcom);
        v.validate();
        assertTrue(v.findings.isEmpty());
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

}
