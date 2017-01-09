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
 * Test for {@link Submitter}
 *
 * @author reckenrod
 * 
 */
public class SubmitterTest {

    /**
     * Test for {@link Submitter#equals(Object)}
     */
    @Test
    @SuppressWarnings({ "PMD.EqualsNull", "PMD.ExcessiveMethodLength", "PMD.NcssMethodCount" })
    public void testEquals() {
        Submitter s1 = new Submitter();
        assertFalse(s1.equals(null));
        assertFalse(s1.equals(new Corporation()));
        assertEquals(s1, s1);

        Submitter s2 = new Submitter();
        assertEquals(s1, s2);

        s2.setChangeDate(new ChangeDate());
        assertFalse(s1.equals(s2));
        s1.setChangeDate(new ChangeDate());
        assertEquals(s1, s2);
        s2.setChangeDate((ChangeDate) null);
        assertFalse(s1.equals(s2));
        s1.setChangeDate((ChangeDate) null);
        assertEquals(s1, s2);

        s2.getLanguagePref(true).add(new StringWithCustomFacts("English"));
        assertFalse(s1.equals(s2));
        s1.getLanguagePref(true).add(new StringWithCustomFacts("English"));
        assertEquals(s1, s2);
        s1.getLanguagePref().clear();
        assertFalse(s1.equals(s2));
        s2.getLanguagePref().clear();
        assertEquals(s1, s2);

        s2.getMultimedia(true).add(new MultimediaReference());
        assertFalse(s1.equals(s2));
        s1.getMultimedia(true).add(new MultimediaReference());
        assertEquals(s1, s2);
        s1.getMultimedia().clear();
        assertFalse(s1.equals(s2));
        s2.getMultimedia().clear();
        assertEquals(s1, s2);

        s2.setName("Donald Duck");
        assertFalse(s1.equals(s2));
        s1.setName("Donald Duck");
        assertEquals(s1, s2);
        s2.setName((String) null);
        assertFalse(s1.equals(s2));
        s1.setName((String) null);
        assertEquals(s1, s2);

        s2.setRecIdNumber("1");
        assertFalse(s1.equals(s2));
        s1.setRecIdNumber("1");
        assertEquals(s1, s2);
        s2.setRecIdNumber((String) null);
        assertFalse(s1.equals(s2));
        s1.setRecIdNumber((String) null);
        assertEquals(s1, s2);

        s2.setRegFileNumber("32");
        assertFalse(s1.equals(s2));
        s1.setRegFileNumber("32");
        assertEquals(s1, s2);
        s2.setRegFileNumber((String) null);
        assertFalse(s1.equals(s2));
        s1.setRegFileNumber((String) null);
        assertEquals(s1, s2);

        s2.getUserReferences(true).add(new UserReference());
        assertFalse(s1.equals(s2));
        s1.getUserReferences(true).add(new UserReference());
        assertEquals(s1, s2);
        s1.getUserReferences().clear();
        assertFalse(s1.equals(s2));
        s2.getUserReferences().clear();
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
