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
package org.gedcom4j.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

/**
 * Test cases for personal name variations
 * 
 * @author frizbog1
 * 
 */
public class PersonalNameVariationTest {

    /**
     * Test for .equals()
     */
    @Test
    public void testEquals() {
        PersonalNameVariation pnv1 = new PersonalNameVariation();
        PersonalNameVariation pnv2 = new PersonalNameVariation();

        assertNotSame(pnv1, pnv2);
        assertEquals(pnv1, pnv2);

        pnv1.getCitations().add(new CitationWithoutSource());
        assertFalse(pnv1.equals(pnv2));
        pnv1.getCitations().clear();
        assertEquals(pnv1, pnv2);

        pnv1.getCustomTags().add(new StringTree());
        assertFalse(pnv1.equals(pnv2));
        pnv1.getCustomTags().clear();
        assertEquals(pnv1, pnv2);

        pnv1.givenName = new StringWithCustomTags("Yo");
        assertFalse(pnv1.equals(pnv2));
        pnv1.givenName = null;
        assertEquals(pnv1, pnv2);

        pnv1.nickname = new StringWithCustomTags("Yo");
        assertFalse(pnv1.equals(pnv2));
        pnv1.nickname = null;
        assertEquals(pnv1, pnv2);

        pnv1.getNotes().add(new Note());
        assertFalse(pnv1.equals(pnv2));
        pnv1.getNotes().clear();
        assertEquals(pnv1, pnv2);

        pnv1.prefix = new StringWithCustomTags("Yo");
        assertFalse(pnv1.equals(pnv2));
        pnv1.prefix = null;
        assertEquals(pnv1, pnv2);

        pnv1.suffix = new StringWithCustomTags("Yo");
        assertFalse(pnv1.equals(pnv2));
        pnv1.suffix = null;
        assertEquals(pnv1, pnv2);

        pnv1.surname = new StringWithCustomTags("Yo");
        assertFalse(pnv1.equals(pnv2));
        pnv1.surname = null;
        assertEquals(pnv1, pnv2);

        pnv1.surnamePrefix = new StringWithCustomTags("Yo");
        assertFalse(pnv1.equals(pnv2));
        pnv1.surnamePrefix = null;
        assertEquals(pnv1, pnv2);

        pnv1.variation = "Yo";
        assertFalse(pnv1.equals(pnv2));
        pnv1.variation = null;
        assertEquals(pnv1, pnv2);

        pnv1.variationType = new StringWithCustomTags("Yo");
        assertFalse(pnv1.equals(pnv2));
        pnv1.variationType = null;
        assertEquals(pnv1, pnv2);

    }

    /**
     * Test for .hashCode()
     */
    @Test
    public void testHashCode() {
        PersonalNameVariation pnv1 = new PersonalNameVariation();
        PersonalNameVariation pnv2 = new PersonalNameVariation();

        assertNotSame(pnv1, pnv2);
        assertEquals(pnv1.hashCode(), pnv2.hashCode());

        pnv1.getCitations().add(new CitationWithoutSource());
        assertFalse(pnv1.hashCode() == pnv2.hashCode());
        pnv1.getCitations().clear();
        assertEquals(pnv1.hashCode(), pnv2.hashCode());

        pnv1.getCustomTags().add(new StringTree());
        assertFalse(pnv1.hashCode() == pnv2.hashCode());
        pnv1.getCustomTags().clear();
        assertEquals(pnv1.hashCode(), pnv2.hashCode());

        pnv1.givenName = new StringWithCustomTags("Yo");
        assertFalse(pnv1.hashCode() == pnv2.hashCode());
        pnv1.givenName = null;
        assertEquals(pnv1.hashCode(), pnv2.hashCode());

        pnv1.nickname = new StringWithCustomTags("Yo");
        assertFalse(pnv1.hashCode() == pnv2.hashCode());
        pnv1.nickname = null;
        assertEquals(pnv1.hashCode(), pnv2.hashCode());

        pnv1.getNotes().add(new Note());
        assertFalse(pnv1.hashCode() == pnv2.hashCode());
        pnv1.getNotes().clear();
        assertEquals(pnv1.hashCode(), pnv2.hashCode());

        pnv1.prefix = new StringWithCustomTags("Yo");
        assertFalse(pnv1.hashCode() == pnv2.hashCode());
        pnv1.prefix = null;
        assertEquals(pnv1.hashCode(), pnv2.hashCode());

        pnv1.suffix = new StringWithCustomTags("Yo");
        assertFalse(pnv1.hashCode() == pnv2.hashCode());
        pnv1.suffix = null;
        assertEquals(pnv1.hashCode(), pnv2.hashCode());

        pnv1.surname = new StringWithCustomTags("Yo");
        assertFalse(pnv1.hashCode() == pnv2.hashCode());
        pnv1.surname = null;
        assertEquals(pnv1.hashCode(), pnv2.hashCode());

        pnv1.surnamePrefix = new StringWithCustomTags("Yo");
        assertFalse(pnv1.hashCode() == pnv2.hashCode());
        pnv1.surnamePrefix = null;
        assertEquals(pnv1.hashCode(), pnv2.hashCode());

        pnv1.variation = "Yo";
        assertFalse(pnv1.hashCode() == pnv2.hashCode());
        pnv1.variation = null;
        assertEquals(pnv1.hashCode(), pnv2.hashCode());

        pnv1.variationType = new StringWithCustomTags("Yo");
        assertFalse(pnv1.hashCode() == pnv2.hashCode());
        pnv1.variationType = null;
        assertEquals(pnv1.hashCode(), pnv2.hashCode());

    }

    /**
     * Test for .toString()
     */
    @Test
    public void testToString() {
        PersonalNameVariation pnv = new PersonalNameVariation();
        assertEquals("PersonalNameVariation [notes=[], citations=[], customTags=[]]", pnv.toString());

        pnv.variation = "Frying Pan";
        pnv.getCustomTags().add(new StringTree());
        pnv.givenName = new StringWithCustomTags("Now");
        pnv.nickname = new StringWithCustomTags("Is");
        pnv.getNotes().add(new Note());
        pnv.suffix = new StringWithCustomTags("The");
        pnv.surname = new StringWithCustomTags("Time");
        pnv.surnamePrefix = new StringWithCustomTags("For");
        pnv.variationType = new StringWithCustomTags("All");
        assertEquals("PersonalNameVariation [givenName=Now, nickname=Is, surnamePrefix=For, surname=Time, suffix=The, "
                + "notes=[Note [lines=[], citations=[], userReferences=[], customTags=[]]], citations=[], variationType=All, "
                + "variation=Frying Pan, customTags=[Line 0: 0 null null]]", pnv.toString());

    }
}
