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
package org.gedcom4j.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Test for {@link Submission}
 *
 * @author reckenrod
 * 
 */
public class SubmissionTest {

    /**
     * Test for {@link Submission#equals(Object)}
     */
    @Test
    @SuppressWarnings({ "PMD.EqualsNull", "PMD.ExcessiveMethodLength", "PMD.NcssMethodCount" })
    public void testEquals() {
        Submission s1 = new Submission();
        assertFalse(s1.equals(null));
        assertFalse(s1.equals(new Corporation()));
        assertEquals(s1, s1);

        Submission s2 = new Submission();
        assertEquals(s1, s2);

        s2.setAncestorsCount("13");
        assertFalse(s1.equals(s2));
        s1.setAncestorsCount("13");
        assertEquals(s1, s2);
        s2.setAncestorsCount((String) null);
        assertFalse(s1.equals(s2));
        s1.setAncestorsCount((String) null);
        assertEquals(s1, s2);

        s2.setDescendantsCount("2");
        assertFalse(s1.equals(s2));
        s1.setDescendantsCount("2");
        assertEquals(s1, s2);
        s2.setDescendantsCount((String) null);
        assertFalse(s1.equals(s2));
        s1.setDescendantsCount((String) null);
        assertEquals(s1, s2);

        s2.setNameOfFamilyFile("Smith");
        assertFalse(s1.equals(s2));
        s1.setNameOfFamilyFile("Smith");
        assertEquals(s1, s2);
        s2.setNameOfFamilyFile((String) null);
        assertFalse(s1.equals(s2));
        s1.setNameOfFamilyFile((String) null);
        assertEquals(s1, s2);

        s2.setOrdinanceProcessFlag("Test");
        assertFalse(s1.equals(s2));
        s1.setOrdinanceProcessFlag("Test");
        assertEquals(s1, s2);
        s2.setOrdinanceProcessFlag((String) null);
        assertFalse(s1.equals(s2));
        s1.setOrdinanceProcessFlag((String) null);
        assertEquals(s1, s2);

        s2.setRecIdNumber("1");
        assertFalse(s1.equals(s2));
        s1.setRecIdNumber("1");
        assertEquals(s1, s2);
        s2.setRecIdNumber((String) null);
        assertFalse(s1.equals(s2));
        s1.setRecIdNumber((String) null);
        assertEquals(s1, s2);

        s2.setSubmitter(new Submitter());
        assertFalse(s1.equals(s2));
        s1.setSubmitter(new Submitter());
        assertEquals(s1, s2);
        s2.setSubmitter((Submitter) null);
        assertFalse(s1.equals(s2));
        s1.setSubmitter((Submitter) null);
        assertEquals(s1, s2);

        s2.setTempleCode("T101");
        assertFalse(s1.equals(s2));
        s1.setTempleCode("T101");
        assertEquals(s1, s2);
        s2.setTempleCode((String) null);
        assertFalse(s1.equals(s2));
        s1.setTempleCode((String) null);
        assertEquals(s1, s2);

        s2.setXref("23");
        assertFalse(s1.equals(s2));
        s1.setXref("23");
        assertEquals(s1, s2);
        s2.setXref((String) null);
        assertFalse(s1.equals(s2));
        s1.setXref((String) null);
        assertEquals(s1, s2);
    }
}
