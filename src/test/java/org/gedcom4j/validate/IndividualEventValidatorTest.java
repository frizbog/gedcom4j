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

import org.gedcom4j.model.Address;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.IndividualEventType;
import org.gedcom4j.model.StringWithCustomTags;
import org.gedcom4j.model.TestHelper;
import org.junit.Test;

/**
 * Test for {@link EventValidator}
 * 
 * @author frizbog1
 * 
 */
public class IndividualEventValidatorTest extends AbstractValidatorTestCase {

    /**
     * Test validation
     */
    @Test
    public void testValidator() {
        Gedcom g = TestHelper.getMinimalGedcom();
        validator = new Validator(g);
        validator.setAutoRepairResponder(Validator.AUTO_REPAIR_NONE);

        Individual i = new Individual();
        i.setXref("@I0001@");
        g.getIndividuals().put(i.getXref(), i);

        IndividualEvent e = new IndividualEvent();
        i.getEvents(true).add(e);
        validator.validate();
        assertFindingsContain(Severity.ERROR, e, ProblemCode.MISSING_REQUIRED_VALUE, "type");

        e.setType(IndividualEventType.BIRTH);
        validator.validate();
        assertNoIssues();

        e.getCitations(true).clear();
        validator.validate();
        assertNoIssues();

        e.getEmails(true).clear();
        validator.validate();
        assertNoIssues();

        e.getWwwUrls(true).clear();
        validator.validate();
        assertNoIssues();

        e.getFaxNumbers(true).clear();
        validator.validate();
        assertNoIssues();

        e.getPhoneNumbers(true).clear();
        validator.validate();
        assertNoIssues();

        e.setAddress(new Address());
        e.getAddress().setCity(new StringWithCustomTags("FryingPanVille"));
        validator.validate();
        assertNoIssues();
    }
}
