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
 * Test for {@link CustomFact}
 * 
 * @author frizbog
 */
public class CustomFactTest {

    /**
     * Test for {@link CustomFact#equals(Object)}
     */
    @Test
    @SuppressWarnings("PMD.EqualsNull")
    public void testEquals() {
        CustomFact cf1 = new CustomFact((String) null);
        assertEquals(cf1, cf1);
        assertFalse(cf1.equals(this));
        assertFalse(cf1.equals(null));

        CustomFact cf2 = new CustomFact((String) null);
        assertEquals(cf1, cf2);

        cf2.setChangeDate(new ChangeDate());
        assertFalse(cf1.equals(cf2));
        cf1.setChangeDate(new ChangeDate());
        assertEquals(cf1, cf2);
        cf2.setChangeDate(null);
        assertFalse(cf1.equals(cf2));
        cf2.setChangeDate(new ChangeDate());
        assertEquals(cf1, cf2);

        cf2.getCitations(true);
        assertFalse(cf1.equals(cf2));
        cf1.getCitations(true);
        assertEquals(cf1, cf2);
        cf2.getCitations().add(new CitationWithoutSource());
        assertFalse(cf1.equals(cf2));
        cf1.getCitations().add(new CitationWithoutSource());
        assertEquals(cf1, cf2);

        cf2.setDate("01 OCT 2010");
        assertFalse(cf1.equals(cf2));
        cf1.setDate("01 OCT 2010");
        assertEquals(cf1, cf2);
        cf2.setDate((StringWithCustomFacts) null);
        assertFalse(cf1.equals(cf2));
        cf2.setDate("01 OCT 2010");
        assertEquals(cf1, cf2);

        cf2.setDescription("X");
        assertFalse(cf1.equals(cf2));
        cf1.setDescription("X");
        assertEquals(cf1, cf2);
        cf2.setDescription((StringWithCustomFacts) null);
        assertFalse(cf1.equals(cf2));
        cf2.setDescription("X");
        assertEquals(cf1, cf2);

        cf2.setPlace(new Place());
        assertFalse(cf1.equals(cf2));
        cf1.setPlace(new Place());
        assertEquals(cf1, cf2);
        cf2.setPlace(null);
        assertFalse(cf1.equals(cf2));
        cf2.setPlace(new Place());
        assertEquals(cf1, cf2);

        cf2 = new CustomFact("_X");
        assertFalse(cf1.equals(cf2));
        cf1 = new CustomFact("_X");
        assertEquals(cf1, cf2);
        cf1 = new CustomFact("_Y");
        assertFalse(cf1.equals(cf2));
        cf1 = new CustomFact("_X");
        assertEquals(cf1, cf2);

        cf2.setType("X");
        assertFalse(cf1.equals(cf2));
        cf1.setType("X");
        assertEquals(cf1, cf2);
        cf2.setType((StringWithCustomFacts) null);
        assertFalse(cf1.equals(cf2));
        cf2.setType("X");
        assertEquals(cf1, cf2);

        cf2.setXref("@X@");
        assertFalse(cf1.equals(cf2));
        cf1.setXref("@X@");
        assertEquals(cf1, cf2);
        cf2.setXref(null);
        assertFalse(cf1.equals(cf2));
        cf2.setXref("@X@");
        assertEquals(cf1, cf2);
    }

    /**
     * Test for {@link CustomFact#hashCode()}
     */
    @Test
    public void testHashcode() {
        CustomFact cf1 = new CustomFact((String) null);
        assertEquals(cf1, cf1);
        assertFalse(cf1.hashCode() == hashCode());

        CustomFact cf2 = new CustomFact((String) null);
        assertEquals(cf1, cf2);

        cf2.setChangeDate(new ChangeDate());
        assertFalse(cf1.hashCode() == cf2.hashCode());
        cf1.setChangeDate(new ChangeDate());
        assertEquals(cf1, cf2);
        cf2.setChangeDate(null);
        assertFalse(cf1.hashCode() == cf2.hashCode());
        cf2.setChangeDate(new ChangeDate());
        assertEquals(cf1, cf2);

        cf2.getCitations(true);
        assertFalse(cf1.hashCode() == cf2.hashCode());
        cf1.getCitations(true);
        assertEquals(cf1, cf2);
        cf2.getCitations().add(new CitationWithoutSource());
        assertFalse(cf1.hashCode() == cf2.hashCode());
        cf1.getCitations().add(new CitationWithoutSource());
        assertEquals(cf1, cf2);

        cf2.setDate("01 OCT 2010");
        assertFalse(cf1.hashCode() == cf2.hashCode());
        cf1.setDate("01 OCT 2010");
        assertEquals(cf1, cf2);
        cf2.setDate((StringWithCustomFacts) null);
        assertFalse(cf1.hashCode() == cf2.hashCode());
        cf2.setDate("01 OCT 2010");
        assertEquals(cf1, cf2);

        cf2.setDescription("X");
        assertFalse(cf1.hashCode() == cf2.hashCode());
        cf1.setDescription("X");
        assertEquals(cf1, cf2);
        cf2.setDescription((StringWithCustomFacts) null);
        assertFalse(cf1.hashCode() == cf2.hashCode());
        cf2.setDescription("X");
        assertEquals(cf1, cf2);

        cf2.setPlace(new Place());
        assertFalse(cf1.hashCode() == cf2.hashCode());
        cf1.setPlace(new Place());
        assertEquals(cf1, cf2);
        cf2.setPlace(null);
        assertFalse(cf1.hashCode() == cf2.hashCode());
        cf2.setPlace(new Place());
        assertEquals(cf1, cf2);

        cf2 = new CustomFact("_X");
        assertFalse(cf1.hashCode() == cf2.hashCode());
        cf1 = new CustomFact("_X");
        assertEquals(cf1, cf2);
        cf1 = new CustomFact("_Y");
        assertFalse(cf1.hashCode() == cf2.hashCode());
        cf1 = new CustomFact("_X");
        assertEquals(cf1, cf2);

        cf2.setType("X");
        assertFalse(cf1.hashCode() == cf2.hashCode());
        cf1.setType("X");
        assertEquals(cf1, cf2);
        cf2.setType((StringWithCustomFacts) null);
        assertFalse(cf1.hashCode() == cf2.hashCode());
        cf2.setType("X");
        assertEquals(cf1, cf2);

        cf2.setXref("@X@");
        assertFalse(cf1.hashCode() == cf2.hashCode());
        cf1.setXref("@X@");
        assertEquals(cf1, cf2);
        cf2.setXref(null);
        assertFalse(cf1.hashCode() == cf2.hashCode());
        cf2.setXref("@X@");
        assertEquals(cf1, cf2);
    }

    /**
     * Test for {@link CustomFact#toString()}
     */
    @Test
    public void testToString() {
        CustomFact cf = new CustomFact("_X");
        assertEquals("CustomFact [tag=_X, ]", cf.toString());

        cf.setChangeDate(new ChangeDate());
        cf.setDate("01 OCT 2010");
        cf.setDescription("Foo");
        cf.setPlace(new Place());
        cf.setType("X");
        cf.setXref("@X@");
        assertEquals("CustomFact [changeDate=ChangeDate [], date=01 OCT 2010, description=Foo, "
                + "place=Place [], tag=_X, xref=@X@, type=X, ]", cf.toString());
    }
}
