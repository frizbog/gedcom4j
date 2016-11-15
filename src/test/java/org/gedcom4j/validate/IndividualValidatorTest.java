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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.gedcom4j.model.Association;
import org.gedcom4j.model.CustomFact;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.StringWithCustomFacts;
import org.gedcom4j.model.TestHelper;
import org.gedcom4j.validate.Validator.Finding;
import org.junit.Test;

/**
 * Tests for {@link IndividualValidator}
 * 
 * @author frizbog1
 * 
 */
public class IndividualValidatorTest extends AbstractValidatorTestCase {

    /**
     * Test aliases
     */
    @Test
    public void testAliases() {
        Individual i = new Individual();
        i.setXref("@I1@");
        i.getAliases(true).add(new StringWithCustomFacts("Bubba"));
        i.getAliases(true).add(null);
        i.getAliases(true).add(new StringWithCustomFacts(""));
        validator.getGedcom().getIndividuals().put(i.getXref(), i);
        AbstractValidator v = new IndividualValidator(validator, i);
        v.validate();

        assertFindingsContain(Severity.ERROR, Individual.class, ProblemCode.LIST_WITH_NULL_VALUE.getCode(), "aliases");
    }

    /**
     * test associations
     */
    @Test
    public void testAssociations() {
        Individual i = new Individual();
        Association a = new Association();
        i.getAssociations(true).add(a);
        AbstractValidator v = new IndividualValidator(validator, i);
        v.validate();
        assertFindingsContain(Severity.ERROR, i, ProblemCode.MISSING_REQUIRED_VALUE, "xref");

        a.setAssociatedEntityXref(" ");
        v.validate();
        assertFindingsContain(Severity.ERROR, i, ProblemCode.MISSING_REQUIRED_VALUE, "xref");

        a.setAssociatedEntityXref("BADFORMAT");
        v.validate();
        assertFindingsContain(Severity.ERROR, i, ProblemCode.MISSING_REQUIRED_VALUE, "xref");

        a.setAssociatedEntityXref("@I1@");
        v.validate();
        assertFindingsContain(Severity.ERROR, i, ProblemCode.MISSING_REQUIRED_VALUE, "xref");
    }

    /**
     * Test custom facts
     */
    @Test
    public void testCustomFacts1() {
        Individual i = new Individual();
        i.setXref("@I1@");
        i.getCustomFacts(true).add(null);
        i.getCustomFacts(true).add(new CustomFact("_X"));
        i.getCustomFacts(true).add(null);

        validator.setAutoRepairResponder(Validator.AUTO_REPAIR_NONE);
        AbstractValidator v = new IndividualValidator(validator, i);
        v.validate();

        assertFindingsContain(Severity.ERROR, Individual.class, ProblemCode.LIST_WITH_NULL_VALUE.getCode(), "customFacts");

        validator.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);
        v = new IndividualValidator(validator, i);
        v.validate();

        assertFindingsContain(Severity.ERROR, Individual.class, ProblemCode.LIST_WITH_NULL_VALUE.getCode(), "customFacts");
    }

    /**
     * Test emails
     */
    @Test
    public void testEmails() {
        Individual i = new Individual();
        i.setXref("@I1@");
        i.getEmails(true).add(new StringWithCustomFacts("bad email format"));
        validator.getGedcom().getIndividuals().put(i.getXref(), i);
        AbstractValidator v = new IndividualValidator(validator, i);
        v.validate();

        assertFindingsContain(Severity.WARNING, StringWithCustomFacts.class, ProblemCode.NOT_VALID_EMAIL_ADDRESS.getCode(),
                "value");
    }

    /**
     * Test for a default individual (no xref)
     */
    @Test
    public void testValidateIndividual() {
        Individual i = new Individual();
        AbstractValidator v = new IndividualValidator(validator, i);
        v.validate();
        assertFindingsContain(Severity.ERROR, i, ProblemCode.MISSING_REQUIRED_VALUE, "xref");
    }

    /**
     * Test for {@link Validator#checkIndividuals()} with a malformed xref on an individual, which does not match its key in the
     * individuals map
     */
    @Test
    public void testValidateIndividuals2() {
        Gedcom g = TestHelper.getMinimalGedcom();

        // Deliberately introduce a problem
        Individual i = new Individual();
        i.setXref("FryingPan");
        g.getIndividuals().put("WrongKey", i);

        // Go validate
        validator = new Validator(g);
        validator.validate();

        // Assert stuff
        int errorsCount = 0;
        for (Finding f : validator.getResults().getAllFindings()) {
            assertNotNull(f);
            assertNotNull("The finding should have an object attached", f.getItemOfConcern());
            if (f.getSeverity() == Severity.ERROR) {
                errorsCount++;
                assertTrue("The " + f.getItemOfConcern().getClass().getName() + " object attached should be a Map entry", f
                        .getItemOfConcern() instanceof Individual);
            }
        }
        assertEquals("There should be one finding of severity ERROR", 1, errorsCount);
    }

}
