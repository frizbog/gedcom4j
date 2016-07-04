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

        pnv1.setGivenName(new StringWithCustomTags("Yo"));
        assertFalse(pnv1.equals(pnv2));
        pnv1.setGivenName(null);
        assertEquals(pnv1, pnv2);

        pnv1.setNickname(new StringWithCustomTags("Yo"));
        assertFalse(pnv1.equals(pnv2));
        pnv1.setNickname(null);
        assertEquals(pnv1, pnv2);

        pnv1.getNotes().add(new Note());
        assertFalse(pnv1.equals(pnv2));
        pnv1.getNotes().clear();
        assertEquals(pnv1, pnv2);

        pnv1.setPrefix(new StringWithCustomTags("Yo"));
        assertFalse(pnv1.equals(pnv2));
        pnv1.setPrefix(null);
        assertEquals(pnv1, pnv2);

        pnv1.setSuffix(new StringWithCustomTags("Yo"));
        assertFalse(pnv1.equals(pnv2));
        pnv1.setSuffix(null);
        assertEquals(pnv1, pnv2);

        pnv1.setSurname(new StringWithCustomTags("Yo"));
        assertFalse(pnv1.equals(pnv2));
        pnv1.setSurname(null);
        assertEquals(pnv1, pnv2);

        pnv1.setSurnamePrefix(new StringWithCustomTags("Yo"));
        assertFalse(pnv1.equals(pnv2));
        pnv1.setSurnamePrefix(null);
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

        pnv1.setGivenName(new StringWithCustomTags("Yo"));
        assertFalse(pnv1.hashCode() == pnv2.hashCode());
        pnv1.setGivenName(null);
        assertEquals(pnv1.hashCode(), pnv2.hashCode());

        pnv1.setNickname(new StringWithCustomTags("Yo"));
        assertFalse(pnv1.hashCode() == pnv2.hashCode());
        pnv1.setNickname(null);
        assertEquals(pnv1.hashCode(), pnv2.hashCode());

        pnv1.getNotes().add(new Note());
        assertFalse(pnv1.hashCode() == pnv2.hashCode());
        pnv1.getNotes().clear();
        assertEquals(pnv1.hashCode(), pnv2.hashCode());

        pnv1.setPrefix(new StringWithCustomTags("Yo"));
        assertFalse(pnv1.hashCode() == pnv2.hashCode());
        pnv1.setPrefix(null);
        assertEquals(pnv1.hashCode(), pnv2.hashCode());

        pnv1.setSuffix(new StringWithCustomTags("Yo"));
        assertFalse(pnv1.hashCode() == pnv2.hashCode());
        pnv1.setSuffix(null);
        assertEquals(pnv1.hashCode(), pnv2.hashCode());

        pnv1.setSurname(new StringWithCustomTags("Yo"));
        assertFalse(pnv1.hashCode() == pnv2.hashCode());
        pnv1.setSurname(null);
        assertEquals(pnv1.hashCode(), pnv2.hashCode());

        pnv1.setSurnamePrefix(new StringWithCustomTags("Yo"));
        assertFalse(pnv1.hashCode() == pnv2.hashCode());
        pnv1.setSurnamePrefix(null);
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
        assertEquals("PersonalNameVariation [citations=[], notes=[], customTags=[]]", pnv.toString());

        pnv.variation = "Frying Pan";
        pnv.getCustomTags().add(new StringTree());
        pnv.setGivenName(new StringWithCustomTags("Now"));
        pnv.setNickname(new StringWithCustomTags("Is"));
        pnv.getNotes().add(new Note());
        pnv.setSuffix(new StringWithCustomTags("The"));
        pnv.setSurname(new StringWithCustomTags("Time"));
        pnv.setSurnamePrefix(new StringWithCustomTags("For"));
        pnv.variationType = new StringWithCustomTags("All");
        assertEquals("PersonalNameVariation [citations=[], givenName=Now, nickname=Is, "
                + "notes=[Note [citations=[], lines=[], userReferences=[], customTags=[]]], "
                + "suffix=The, surname=Time, surnamePrefix=For, variation=Frying Pan, variationType=All, customTags=[Line 0: 0 null null]]", pnv.toString());

    }
}
