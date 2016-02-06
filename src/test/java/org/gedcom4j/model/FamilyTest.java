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

import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link Family}
 * 
 * @author frizbog
 */
public class FamilyTest {
    /**
     * First family to test with
     */
    private Family family1;

    /**
     * Second family to test with
     */
    private Family family2;

    /**
     * Set up test fixtures
     */
    @Before
    public void setUp() {

        family1 = new Family();
        FamilySpouse fs1 = new FamilySpouse();
        fs1.family = family1;

        family1.husband = new Individual();
        PersonalName hn1 = new PersonalName();
        hn1.basic = "Fred /Astaire/";
        family1.husband.names.add(hn1);
        family1.husband.familiesWhereSpouse.add(fs1);

        family1.wife = new Individual();
        PersonalName wn1 = new PersonalName();
        wn1.basic = "Ginger /Rogers/";
        family1.wife.names.add(wn1);
        family1.wife.familiesWhereSpouse.add(fs1);

        family2 = new Family();
        FamilySpouse fs2 = new FamilySpouse();
        fs2.family = family2;

        family2.husband = new Individual();
        PersonalName hn2 = new PersonalName();
        hn2.basic = "Fred /Astaire/";
        family2.husband.names.add(hn2);
        family2.husband.familiesWhereSpouse.add(fs2);

        family2.wife = new Individual();
        PersonalName wn2 = new PersonalName();
        wn2.basic = "Ginger /Rogers/";
        family2.wife.names.add(wn2);
        family2.wife.familiesWhereSpouse.add(fs2);
    }

    /**
     * Test method for {@link Family#equals(Object)}.
     */
    @Test
    public void testEquals() {
        assertEquals(family1, family2);
        assertNotSame(family1, family2);
    }

    /**
     * Test method for {@link Family#equals(Object)}.
     */
    @Test
    public void testEqualsNegative() {
        family1.xref = "@F1@";
        family2.xref = "@F2@";
        assertFalse(family1.equals(family2));
    }

    /**
     * Test method for {@link Family#equals(Object)}.
     */
    @Test
    public void testEqualsNew() {
        Family f1 = new Family();
        Family f2 = new Family();
        assertEquals(f1, f2);
        assertNotSame(f1, f2);
    }

    /**
     * Test method for {@link Family#equals(Object)}.
     */
    @Test
    public void testEqualsOtherObjectTypes() {
        Family f1 = new Family();
        assertFalse(f1.equals("Test"));
        assertFalse(f1.equals(new Individual()));
    }

    /**
     * Test method for {@link Family#hashCode()}.
     */
    @Test
    public void testHashCode() {
        assertEquals(family1.hashCode(), family2.hashCode());
        assertNotSame(family1, family2);
    }

    /**
     * Test method for {@link Family#hashCode()}.
     */
    @Test
    public void testHashCodeNegative() {
        family1.xref = "@F1@";
        family2.xref = "@F2@";
        assertFalse(family1.hashCode() == family2.hashCode());
    }

    /**
     * Test method for {@link Family#hashCode()}.
     */
    @Test
    public void testHashCodeNew() {
        Family f1 = new Family();
        Family f2 = new Family();
        assertEquals(f1.hashCode(), f2.hashCode());
        assertNotSame(f1, f2);
    }

    /**
     * Test method for {@link Family#toString()}.
     */
    @Test
    public void testToString() {
        assertEquals("Family [wife=Ginger /Rogers/, spouse of Fred /Astaire/, husband=Fred /Astaire/, spouse of Ginger /Rogers/, children=[], "
                + "submitters=[], ldsSpouseSealings=[], citations=[], multimedia=[], events=[], notes=[], userReferences=[], customTags=[]]",
                family1.toString());
    }

    /**
     * Test method for {@link Family#toString()}.
     */
    @Test
    public void testToStringNew() {
        assertEquals("Family [children=[], submitters=[], ldsSpouseSealings=[], citations=[], multimedia=[], events=[], notes=[], "
                + "userReferences=[], customTags=[]]", new Family().toString());
    }
}
