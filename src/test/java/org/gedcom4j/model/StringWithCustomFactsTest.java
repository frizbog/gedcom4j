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

import org.junit.Test;

/**
 * Tests for the {@link StringWithCustomFacts} class. One of the major areas of tesitng is to assert that the toString
 * implementations do not reveal the wrapper class, but only expose the value.
 * 
 * @author frizbog
 */
@SuppressWarnings("PMD.AddEmptyString")
public class StringWithCustomFactsTest {

    /**
     * Test toString when the value is null and custom facts collection is empty
     */
    @Test
    public void testToStringEmptyValueEmptyCustomFacts() {
        StringWithCustomFacts swct = new StringWithCustomFacts();
        swct.value = "";
        swct.getCustomFacts(true);
        assertEquals("", "" + swct);
    }

    /**
     * Test toString when the value is null and custom facts collection is not empty
     */
    @Test
    public void testToStringEmptyValueNonEmptyCustomFacts() {
        StringWithCustomFacts swct = new StringWithCustomFacts();
        swct.value = "";
        swct.getCustomFacts(true).add(new CustomFact("_TEST"));
        assertEquals("", "" + swct);
    }

    /**
     * Test toString when the value and custom facts collection are both null
     */
    @Test
    public void testToStringEmptyValueNullCustomFacts() {
        StringWithCustomFacts swct = new StringWithCustomFacts();
        swct.value = "";
        swct.customFacts = null;
        assertEquals("", "" + swct);
        swct.getCustomFacts(true);
    }

    /**
     * Test toString on a null object
     */
    @Test
    public void testToStringNull() {
        StringWithCustomFacts swct = null;
        assertEquals("null", "" + swct);
    }

    /**
     * Test toString when the value is null and custom facts collection is empty
     */
    @Test
    public void testToStringNullValueEmptyCustomFacts() {
        StringWithCustomFacts swct = new StringWithCustomFacts();
        swct.value = null;
        swct.getCustomFacts(true);
        assertEquals("null", "" + swct);
    }

    /**
     * Test toString when the value is null and custom facts collection is not empty
     */
    @Test
    public void testToStringNullValueNonEmptyCustomFacts() {
        StringWithCustomFacts swct = new StringWithCustomFacts();
        swct.value = null;
        swct.getCustomFacts(true).add(new CustomFact("_TEST"));
        assertEquals("null", "" + swct);
    }

    /**
     * Test toString when the value and custom facts collection are both null
     */
    @Test
    public void testToStringNullValueNullCustomFacts() {
        StringWithCustomFacts swct = new StringWithCustomFacts();
        swct.value = null;
        swct.customFacts = null;
        assertEquals("null", "" + swct);
        swct.getCustomFacts(true);
    }

    /**
     * Test toString when the value is null and custom facts collection is empty
     */
    @Test
    public void testToStringWithValueEmptyCustomFacts() {
        StringWithCustomFacts swct = new StringWithCustomFacts();
        swct.value = "Foo";
        swct.getCustomFacts(true);
        assertEquals("Foo", "" + swct);
    }

    /**
     * Test toString when the value is null and custom facts collection is not empty
     */
    @Test
    public void testToStringWithValueNonEmptyCustomFacts() {
        StringWithCustomFacts swct = new StringWithCustomFacts();
        swct.value = "Foo";
        swct.getCustomFacts(true).add(new CustomFact("_TEST"));
        assertEquals("Foo", "" + swct);
    }

    /**
     * Test toString when the value and custom facts collection are both null
     */
    @Test
    public void testToStringWithValueNullCustomFacts() {
        StringWithCustomFacts swct = new StringWithCustomFacts();
        swct.value = "Foo";
        swct.customFacts = null;
        assertEquals("Foo", "" + swct);
        swct.getCustomFacts(true);
    }

}
