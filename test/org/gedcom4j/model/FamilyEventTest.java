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
package org.gedcom4j.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author frizbog1
 * 
 */
public class FamilyEventTest {

    /**
     * Test equals and hashcode after perturbing address
     */
    @Test
    public void testAddress() {
        FamilyEvent e1 = new FamilyEvent();
        FamilyEvent e2 = new FamilyEvent();

        e1.address = new Address();
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.address = new Address();
        assertTrue(e1.equals(e2));
        assertTrue(e1.hashCode() == e2.hashCode());
    }

    /**
     * Test equals and hashcode after perturbing age
     */
    @Test
    public void testAge() {
        FamilyEvent e1 = new FamilyEvent();
        FamilyEvent e2 = new FamilyEvent();

        e1.age = new StringWithCustomTags("Foo");
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.age = new StringWithCustomTags("Foo");
        assertTrue(e1.equals(e2));
        assertTrue(e1.hashCode() == e2.hashCode());
    }

    /**
     * Test equals and hashcode after perturbing cause
     */
    @Test
    public void testCause() {
        FamilyEvent e1 = new FamilyEvent();
        FamilyEvent e2 = new FamilyEvent();

        e1.cause = new StringWithCustomTags("Foo");
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.cause = new StringWithCustomTags("Foo");
        assertTrue(e1.equals(e2));
        assertTrue(e1.hashCode() == e2.hashCode());
    }

    /**
     * Test equals and hashcode after perturbing citations
     */
    @Test
    public void testCitations() {
        FamilyEvent e1 = new FamilyEvent();
        FamilyEvent e2 = new FamilyEvent();

        e1.citations = null;
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.citations = null;
        assertTrue(e1.equals(e2));
        assertTrue(e1.hashCode() == e2.hashCode());
    }

    /**
     * Test equals and hashcode after perturbing date
     */
    @Test
    public void testDate() {
        FamilyEvent e1 = new FamilyEvent();
        FamilyEvent e2 = new FamilyEvent();

        e1.date = new StringWithCustomTags("Foo");
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.date = new StringWithCustomTags("Foo");
        assertTrue(e1.equals(e2));
        assertTrue(e1.hashCode() == e2.hashCode());
    }

    /**
     * Test equals and hashcode after perturbing description
     */
    @Test
    public void testDescription() {
        FamilyEvent e1 = new FamilyEvent();
        FamilyEvent e2 = new FamilyEvent();

        e1.description = new StringWithCustomTags("Foo");
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.description = new StringWithCustomTags("Foo");
        assertTrue(e1.equals(e2));
        assertTrue(e1.hashCode() == e2.hashCode());
    }

    /**
     * Test equals and hashcode after perturbing husbandage
     */
    @Test
    public void testHusbandage() {
        FamilyEvent e1 = new FamilyEvent();
        FamilyEvent e2 = new FamilyEvent();

        e1.husbandAge = new StringWithCustomTags("Foo");
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.husbandAge = new StringWithCustomTags("Foo");
        assertTrue(e1.equals(e2));
        assertTrue(e1.hashCode() == e2.hashCode());
    }

    /**
     * Test equals and hashcode after perturbing multimeda
     */
    @Test
    public void testMultimedia() {
        FamilyEvent e1 = new FamilyEvent();
        FamilyEvent e2 = new FamilyEvent();

        e1.multimedia = null;
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.multimedia = null;
        assertTrue(e1.equals(e2));
        assertTrue(e1.hashCode() == e2.hashCode());
    }

    /**
     * Test equals and hashcode after perturbing notes
     */
    @Test
    public void testNotes() {
        FamilyEvent e1 = new FamilyEvent();
        FamilyEvent e2 = new FamilyEvent();

        e1.notes = null;
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.notes = null;
        assertTrue(e1.equals(e2));
        assertTrue(e1.hashCode() == e2.hashCode());
    }

    /**
     * Test equals and hashcode after perturbing phone numbers
     */
    @Test
    public void testPhoneNumbers() {
        FamilyEvent e1 = new FamilyEvent();
        FamilyEvent e2 = new FamilyEvent();

        e1.phoneNumbers = null;
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.phoneNumbers = null;
        assertTrue(e1.equals(e2));
        assertTrue(e1.hashCode() == e2.hashCode());
    }

    /**
     * Test equals and hashcode after perturbing place
     */
    @Test
    public void testPlace() {
        FamilyEvent e1 = new FamilyEvent();
        FamilyEvent e2 = new FamilyEvent();

        e1.place = new Place();
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.place = new Place();
        assertTrue(e1.equals(e2));
        assertTrue(e1.hashCode() == e2.hashCode());
    }

    /**
     * Test equals and hashcode after constructors
     */
    @Test
    public void testPlain() {
        FamilyEvent e1 = new FamilyEvent();
        FamilyEvent e2 = new FamilyEvent();
        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    /**
     * Test equals and hashcode after perturbing responsible agency
     */
    @Test
    public void testRespAgency() {
        FamilyEvent e1 = new FamilyEvent();
        FamilyEvent e2 = new FamilyEvent();

        e1.respAgency = new StringWithCustomTags("Foo");
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.respAgency = new StringWithCustomTags("Foo");
        assertTrue(e1.equals(e2));
        assertTrue(e1.hashCode() == e2.hashCode());
    }

    /**
     * Test equals and hashcode after perturbing subtype
     */
    @Test
    public void testSubtype() {
        FamilyEvent e1 = new FamilyEvent();
        FamilyEvent e2 = new FamilyEvent();

        e1.subType = new StringWithCustomTags("Foo");
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.subType = new StringWithCustomTags("Foo");
        assertTrue(e1.equals(e2));
        assertTrue(e1.hashCode() == e2.hashCode());
    }

    /**
     * Test equals and hashcode after perturbing type
     */
    @Test
    public void testType() {
        FamilyEvent e1 = new FamilyEvent();
        FamilyEvent e2 = new FamilyEvent();

        e1.type = FamilyEventType.EVENT;
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.type = FamilyEventType.EVENT;
        assertTrue(e1.equals(e2));
        assertTrue(e1.hashCode() == e2.hashCode());
    }

    /**
     * Test equals and hashcode after perturbing wife age
     */
    @Test
    public void testWifeAge() {
        FamilyEvent e1 = new FamilyEvent();
        FamilyEvent e2 = new FamilyEvent();

        e1.wifeAge = new StringWithCustomTags("Foo");
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.wifeAge = new StringWithCustomTags("Foo");
        assertTrue(e1.equals(e2));
        assertTrue(e1.hashCode() == e2.hashCode());
    }

    /**
     * Test equals and hashcode after perturbing y/null
     */
    @Test
    public void testYnull() {
        FamilyEvent e1 = new FamilyEvent();
        FamilyEvent e2 = new FamilyEvent();

        e1.yNull = "Foo";
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.yNull = "Foo";
        assertTrue(e1.equals(e2));
        assertTrue(e1.hashCode() == e2.hashCode());
    }

}
