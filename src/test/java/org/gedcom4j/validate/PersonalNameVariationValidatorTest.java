/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
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

import org.gedcom4j.model.*;
import org.junit.Test;

/**
 * Test for {@link PersonalNameVariationValidator}
 * 
 * @author frizbog1
 * 
 */
public class PersonalNameVariationValidatorTest extends AbstractValidatorTestCase {

    /**
     * Test for personal name variation validator
     */
    @Test
    public void testOne() {
        Gedcom g = TestHelper.getMinimalGedcom();
        rootValidator.gedcom = g;

        Individual i = new Individual();
        i.setXref("@I00001@");
        g.getIndividuals().put(i.getXref(), i);

        PersonalName pn = new PersonalName();
        i.getNames(true).add(pn);

        pn.setBasic("Bj\u00F8rn /J\u00F8orgen/");

        rootValidator.validate();
        assertNoIssues();

        PersonalNameVariation romanized = new PersonalNameVariation();
        pn.getRomanized().add(romanized);
        romanized.setGivenName(new StringWithCustomTags("Bjorn"));
        romanized.setSurname(new StringWithCustomTags("Jorgen"));
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "variation", "name", "required");
        romanized.setVariation("Bjorn /Jorgen/");
        rootValidator.validate();
        assertNoIssues();

        PersonalNameVariation phonetic = new PersonalNameVariation();
        pn.getPhonetic().add(phonetic);
        phonetic.setGivenName(new StringWithCustomTags("Byorn"));
        phonetic.setSurname(new StringWithCustomTags("Yorgen"));
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "variation", "name", "required");
        phonetic.setVariation("Byorn /Yorgen/");
        rootValidator.validate();
        assertNoIssues();

    }
}
