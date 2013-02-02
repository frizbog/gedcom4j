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

import java.util.ArrayList;

import org.gedcom4j.model.AbstractCitation;
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

    @Test
    public void test() {
        Gedcom g = TestHelper.getMinimalGedcom();
        rootValidator.autorepair = false;
        rootValidator.gedcom = g;

        Individual i = new Individual();
        i.xref = "@I0001@";
        g.individuals.put(i.xref, i);

        IndividualEvent e = new IndividualEvent();
        i.events.add(e);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "event", "requires", "type");

        e.type = IndividualEventType.BIRTH;
        rootValidator.validate();
        assertNoIssues();

        e.citations = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "citations");
        e.citations = new ArrayList<AbstractCitation>();
        rootValidator.validate();
        assertNoIssues();

        e.emails = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "emails");
        e.emails = new ArrayList<StringWithCustomTags>();
        rootValidator.validate();
        assertNoIssues();

        e.wwwUrls = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "www", "Url");
        e.wwwUrls = new ArrayList<StringWithCustomTags>();
        rootValidator.validate();
        assertNoIssues();

        e.faxNumbers = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "fax", "numbers");
        e.faxNumbers = new ArrayList<StringWithCustomTags>();
        rootValidator.validate();
        assertNoIssues();

        e.phoneNumbers = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "phone", "numbers");
        e.phoneNumbers = new ArrayList<StringWithCustomTags>();
        rootValidator.validate();
        assertNoIssues();

        e.address = new Address();
        e.address.city = new StringWithCustomTags("FryingPanVille");
        rootValidator.validate();
        assertNoIssues();
    }
}
