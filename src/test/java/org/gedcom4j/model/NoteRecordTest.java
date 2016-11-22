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
 * Test for {@link NoteRecord}
 * 
 * @author frizbog
 */
public class NoteRecordTest {

    /**
     * Test for {@link NoteRecord#equals(Object)}
     */
    @Test
    @SuppressWarnings("PMD.EqualsNull")
    public void testEquals() {
        NoteRecord n1 = new NoteRecord((String) null);
        assertEquals(n1, n1);
        assertFalse(n1.equals(this));
        assertFalse(n1.equals(null));

        NoteRecord n2 = new NoteRecord("@N1@");
        assertFalse(n1.equals(n2));
        n1 = new NoteRecord("@N1@");
        assertEquals(n1, n2);

        n2.setChangeDate(new ChangeDate());
        assertFalse(n1.equals(n2));
        n1.setChangeDate(new ChangeDate());
        assertEquals(n1, n2);
        n2.setChangeDate(null);
        assertFalse(n1.equals(n2));
        n2.setChangeDate(new ChangeDate());
        assertEquals(n1, n2);

        n2.setRecIdNumber("X");
        assertFalse(n1.equals(n2));
        n1.setRecIdNumber("X");
        assertEquals(n1, n2);
        n2.setRecIdNumber((StringWithCustomFacts) null);
        assertFalse(n1.equals(n2));
        n2.setRecIdNumber("X");
        assertEquals(n1, n2);

        n2.setXref("@X@");
        assertFalse(n1.equals(n2));
        n1.setXref("@X@");
        assertEquals(n1, n2);
        n2.setXref(null);
        assertFalse(n1.equals(n2));
        n2.setXref("@X@");
        assertEquals(n1, n2);

        n2.getLines(true);
        assertFalse(n1.equals(n2));
        n1.getLines(true);
        assertEquals(n1, n2);
        n2.getLines(true).add("Foo");
        assertFalse(n1.equals(n2));
        n1.getLines(true).add("Foo");
        assertEquals(n1, n2);

        n2.getCitations(true);
        assertFalse(n1.equals(n2));
        n1.getCitations(true);
        assertEquals(n1, n2);
        n2.getCitations(true).add(new CitationWithoutSource());
        assertFalse(n1.equals(n2));
        n1.getCitations(true).add(new CitationWithoutSource());
        assertEquals(n1, n2);

        n2.getCustomFacts(true);
        assertFalse(n1.equals(n2));
        n1.getCustomFacts(true);
        assertEquals(n1, n2);
        n2.getCustomFacts(true).add(new CustomFact("_X"));
        assertFalse(n1.equals(n2));
        n1.getCustomFacts(true).add(new CustomFact("_X"));
        assertEquals(n1, n2);

        n2.getUserReferences(true);
        assertFalse(n1.equals(n2));
        n1.getUserReferences(true);
        assertEquals(n1, n2);
        n2.getUserReferences(true).add(new UserReference());
        assertFalse(n1.equals(n2));
        n1.getUserReferences(true).add(new UserReference());
        assertEquals(n1, n2);
    }

    /**
     * Test for {@link NoteRecord#hashCode()}
     */
    @Test
    public void testHashcode() {
        NoteRecord n1 = new NoteRecord((String) null);
        assertEquals(n1, n1);
        assertFalse(n1.hashCode() == hashCode());

        NoteRecord n2 = new NoteRecord("@N1@");
        assertFalse(n1.hashCode() == n2.hashCode());
        n1 = new NoteRecord("@N1@");
        assertEquals(n1.hashCode(), n2.hashCode());

        n2.setChangeDate(new ChangeDate());
        assertFalse(n1.hashCode() == n2.hashCode());
        n1.setChangeDate(new ChangeDate());
        assertEquals(n1.hashCode(), n2.hashCode());
        n2.setChangeDate(null);
        assertFalse(n1.hashCode() == n2.hashCode());
        n2.setChangeDate(new ChangeDate());
        assertEquals(n1.hashCode(), n2.hashCode());

        n2.setRecIdNumber("X");
        assertFalse(n1.hashCode() == n2.hashCode());
        n1.setRecIdNumber("X");
        assertEquals(n1.hashCode(), n2.hashCode());
        n2.setRecIdNumber((String) null);
        assertFalse(n1.hashCode() == n2.hashCode());
        n2.setRecIdNumber("X");
        assertEquals(n1.hashCode(), n2.hashCode());

        n2.setXref("@X@");
        assertFalse(n1.hashCode() == n2.hashCode());
        n1.setXref("@X@");
        assertEquals(n1.hashCode(), n2.hashCode());
        n2.setXref(null);
        assertFalse(n1.hashCode() == n2.hashCode());
        n1.setXref(null);
        assertEquals(n1.hashCode(), n2.hashCode());

        n2.getLines(true);
        assertFalse(n1.hashCode() == n2.hashCode());
        n1.getLines(true);
        assertEquals(n1.hashCode(), n2.hashCode());
        n2.getLines(true).add("Foo");
        assertFalse(n1.hashCode() == n2.hashCode());
        n1.getLines(true).add("Foo");
        assertEquals(n1.hashCode(), n2.hashCode());

        n2.getCitations(true);
        assertFalse(n1.hashCode() == n2.hashCode());
        n1.getCitations(true);
        assertEquals(n1.hashCode(), n2.hashCode());
        n2.getCitations(true).add(new CitationWithoutSource());
        assertFalse(n1.hashCode() == n2.hashCode());
        n1.getCitations(true).add(new CitationWithoutSource());
        assertEquals(n1.hashCode(), n2.hashCode());

        n2.getCustomFacts(true);
        assertFalse(n1.hashCode() == n2.hashCode());
        n1.getCustomFacts(true);
        assertEquals(n1.hashCode(), n2.hashCode());
        n2.getCustomFacts(true).add(new CustomFact("_X"));
        assertFalse(n1.hashCode() == n2.hashCode());
        n1.getCustomFacts(true).add(new CustomFact("_X"));
        assertEquals(n1.hashCode(), n2.hashCode());

        n2.getUserReferences(true);
        assertFalse(n1.hashCode() == n2.hashCode());
        n1.getUserReferences(true);
        assertEquals(n1.hashCode(), n2.hashCode());
        n2.getUserReferences(true).add(new UserReference());
        assertFalse(n1.hashCode() == n2.hashCode());
        n1.getUserReferences(true).add(new UserReference());
        assertEquals(n1.hashCode(), n2.hashCode());
    }

    /**
     * Test for {@link NoteRecord#toString()}
     */
    @Test
    public void testToString() {
        NoteRecord n = new NoteRecord("foo");
        n.setXref(null);
        assertEquals("Note []", n.toString());

        n.setChangeDate(new ChangeDate());
        n.setRecIdNumber("X");
        n.setXref("@X@");
        n.getCitations(true).add(new CitationWithoutSource());
        n.getCustomFacts(true).add(new CustomFact("_X"));
        n.getLines(true).add("bar");
        n.getUserReferences(true).add(new UserReference());
        assertEquals("Note [changeDate=ChangeDate [], citations=[CitationWithoutSource []], lines=[bar], "
                + "recIdNumber=X, userReferences=[UserReference []], xref=@X@, customFacts=[CustomFact [tag=_X, ]]]", n.toString());
    }
}
