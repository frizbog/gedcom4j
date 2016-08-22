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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author frizbog1
 * 
 */
@SuppressWarnings("PMD.TooManyMethods")
public class FamilyEventTest {

    /**
     * Test equals and hashcode after perturbing address
     */
    @Test
    public void testAddress() {
        FamilyEvent e1 = new FamilyEvent();
        FamilyEvent e2 = new FamilyEvent();

        e1.setAddress(new Address());
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.setAddress(new Address());
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

        e1.setAge(new StringWithCustomTags("Foo"));
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.setAge(new StringWithCustomTags("Foo"));
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

        e1.setCause(new StringWithCustomTags("Foo"));
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.setCause(new StringWithCustomTags("Foo"));
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

        e1.getCitations(true);
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.getCitations(true);
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

        e1.setDate(new StringWithCustomTags("Foo"));
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.setDate(new StringWithCustomTags("Foo"));
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

        e1.setDescription(new StringWithCustomTags("Foo"));
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.setDescription(new StringWithCustomTags("Foo"));
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

        e1.setHusbandAge(new StringWithCustomTags("Foo"));
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.setHusbandAge(new StringWithCustomTags("Foo"));
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

        e1.getMultimedia(true);
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.getMultimedia(true);
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

        e1.getNotes(true);
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.getNotes(true);
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

        e1.getPhoneNumbers(true);
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.getPhoneNumbers(true);
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

        e1.setPlace(new Place());
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.setPlace(new Place());
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

        e1.setRespAgency(new StringWithCustomTags("Foo"));
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.setRespAgency(new StringWithCustomTags("Foo"));
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

        e1.setSubType(new StringWithCustomTags("Foo"));
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.setSubType(new StringWithCustomTags("Foo"));
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

        e1.setType(FamilyEventType.EVENT);
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.setType(FamilyEventType.EVENT);
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

        e1.setWifeAge(new StringWithCustomTags("Foo"));
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.setWifeAge(new StringWithCustomTags("Foo"));
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

        e1.setyNull("Foo");
        assertFalse(e1.equals(e2));
        assertFalse(e1.hashCode() == e2.hashCode());
        e2.setyNull("Foo");
        assertTrue(e1.equals(e2));
        assertTrue(e1.hashCode() == e2.hashCode());
    }

}
