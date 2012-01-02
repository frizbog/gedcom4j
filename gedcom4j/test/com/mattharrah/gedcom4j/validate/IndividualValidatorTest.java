package com.mattharrah.gedcom4j.validate;

import java.util.Map;

import com.mattharrah.gedcom4j.Gedcom;
import com.mattharrah.gedcom4j.Individual;

/**
 * Tests for {@link IndividualValidator}
 * 
 * @author frizbog1
 * 
 */
public class IndividualValidatorTest extends AbstractValidatorTestCase {

    /**
     * Test for a default individual (no xref)
     */
    public void testValidateIndividual() {
        Individual i = new Individual();
        AbstractValidator v = new IndividualValidator(rootValidator, i);
        v.validate();
        assertFindingsContain(Severity.ERROR, "xref", "null");
    }

    /**
     * Test for {@link GedcomValidator#validateIndividuals()} with a malformed
     * xref on an individual, which does not match its key in the individuals
     * map
     */
    public void testValidateIndividuals2() {
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
