package com.mattharrah.gedcom4j.validate;

import java.util.Map;

import junit.framework.TestCase;

import com.mattharrah.gedcom4j.Gedcom;
import com.mattharrah.gedcom4j.Individual;

/**
 * Tests for {@link IndividualValidator}
 * 
 * @author frizbog1
 * 
 */
public class IndividualValidatorTest extends TestCase {

    /**
     * Test for {@link GedcomValidator#validateIndividuals()}
     * 
     */
    public void testValidateIndividuals1() {
        Gedcom g = new Gedcom();
        GedcomValidator v = new GedcomValidator(g);
        v.validate();
        assertTrue("There should be no findings on an empty Gedcom",
                v.findings.isEmpty());
    }

    /**
     * Test for {@link GedcomValidator#validateIndividuals()}
     * 
     * @throws GedcomValidationException
     */
    public void testValidateIndividuals2() throws GedcomValidationException {
        Gedcom g = new Gedcom();

        // Deliberately introduce a problem
        Individual i = new Individual();
        i.xref = "FryingPan";
        g.individuals.put("WrongKey", i);

        // Go validate
        GedcomValidator v = new GedcomValidator(g);
        v.validate();

        // Assert stuff
        assertEquals("There should be one finding", 1, v.findings.size());
        GedcomValidationFinding f = v.findings.get(0);
        assertNotNull(f);
        assertNotNull("The finding should have an object attached",
                f.itemWithProblem);
        assertTrue("The object attached should be a Map entry",
                f.itemWithProblem instanceof Map.Entry);
        assertEquals("The finding should be at severity ERROR", Severity.ERROR,
                f.severity);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

}
