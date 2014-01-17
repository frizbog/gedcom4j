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

import org.gedcom4j.model.EventRecorded;
import org.gedcom4j.model.Source;
import org.gedcom4j.model.SourceData;
import org.gedcom4j.model.StringWithCustomTags;

/**
 * @author frizbog1
 * 
 */
public class SourceValidatorTest extends AbstractValidatorTestCase {

    /**
     * Test when source has some bad values
     */
    public void testBadSource1() {
        Source src = new Source("bad xref");
        src.recIdNumber = new StringWithCustomTags("");
        AbstractValidator av = new SourceValidator(rootValidator, src);
        av.validate();
        assertFindingsContain(Severity.ERROR, "record id", "source", "blank");
        assertFindingsContain(Severity.ERROR, "xref", "source", "start", "at", "sign");
        assertFindingsContain(Severity.ERROR, "xref", "source", "end", "at", "sign");
    }

    /**
     * Test when source has some bad values
     */
    public void testBadSource2() {
        Source src = new Source("@Test@");
        src.data = new SourceData();
        EventRecorded e = new EventRecorded();
        e.datePeriod = new StringWithCustomTags("anytime");
        src.data.eventsRecorded.add(e);
        AbstractValidator av = new SourceValidator(rootValidator, src);
        av.validate();
        assertNoIssues();
    }

    /**
     * Test for a new {@link Source}
     */
    public void testDefault() {
        Source src = new Source(null);
        AbstractValidator av = new SourceValidator(rootValidator, src);
        av.validate();
        assertFindingsContain(Severity.ERROR, "xref", "required", "null", "blank");
    }

    /**
     * Test when source is null
     */
    public void testNullSource() {
        AbstractValidator av = new SourceValidator(rootValidator, null);
        av.validate();
        assertFindingsContain(Severity.ERROR, "source", "null");
    }
}
