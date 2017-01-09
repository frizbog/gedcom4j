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
 * Test for {@link Address}
 * 
 * @author reckenrod
 * 
 */
public class AddressTest {

    /**
     * Test for {@link Address#equals(Object)}
     */
    @Test
    @SuppressWarnings({ "PMD.EqualsNull", "PMD.ExcessiveMethodLength", "PMD.NcssMethodCount" })
    public void testEquals() {
        Address a1 = new Address();
        assertFalse(a1.equals(null));
        assertFalse(a1.equals(new Gedcom()));
        assertEquals(a1, a1);

        Address a2 = new Address();
        assertEquals(a1, a2);

        a1.getCustomFacts(true).add(new CustomFact("TEST"));
        assertFalse(a1.equals(a2));
        a2.getCustomFacts(true).add(new CustomFact("TEST"));
        assertEquals(a1, a2);

        a2.setAddr1("123 Main St.");
        assertFalse(a1.equals(a2));
        a1.setAddr1("123 Main St.");
        assertEquals(a1, a2);
        a1.setAddr1((String) null);
        assertFalse(a1.equals(a2));
        a2.setAddr1((String) null);
        assertEquals(a1, a2);

        a2.setAddr2("Apt. 456");
        assertFalse(a1.equals(a2));
        a1.setAddr2("Apt. 456");
        assertEquals(a1, a2);
        a1.setAddr2((String) null);
        assertFalse(a1.equals(a2));
        a2.setAddr2((String) null);
        assertEquals(a1, a2);

        a2.setAddr3("Room A");
        assertFalse(a1.equals(a2));
        a1.setAddr3("Room A");
        assertEquals(a1, a2);
        a1.setAddr3((String) null);
        assertFalse(a1.equals(a2));
        a2.setAddr3((String) null);
        assertEquals(a1, a2);

        a2.setCity("Anytown");
        assertFalse(a1.equals(a2));
        a1.setCity("Anytown");
        assertEquals(a1, a2);
        a1.setCity((String) null);
        assertFalse(a1.equals(a2));
        a2.setCity((String) null);
        assertEquals(a1, a2);

        a2.setCountry("USA");
        assertFalse(a1.equals(a2));
        a1.setCountry("USA");
        assertEquals(a1, a2);
        a1.setCountry((String) null);
        assertFalse(a1.equals(a2));
        a2.setCountry((String) null);
        assertEquals(a1, a2);

        a2.getLines(true).add("ABC");
        assertFalse(a1.equals(a2));
        a1.getLines(true).add("ABC");
        assertEquals(a1, a2);
        a1.getLines().clear();
        assertFalse(a1.equals(a2));
        a2.getLines().clear();
        assertEquals(a1, a2);

        a2.setPostalCode("12345");
        assertFalse(a1.equals(a2));
        a1.setPostalCode("12345");
        assertEquals(a1, a2);
        a1.setPostalCode((String) null);
        assertFalse(a1.equals(a2));
        a2.setPostalCode((String) null);
        assertEquals(a1, a2);

        a2.setStateProvince("ME");
        assertFalse(a1.equals(a2));
        a1.setStateProvince("ME");
        assertEquals(a1, a2);
        a1.setStateProvince((String) null);
        assertFalse(a1.equals(a2));
        a2.setStateProvince((String) null);
        assertEquals(a1, a2);
    }

    /**
     * Test for {@link Address#hashCode()}
     */
    @Test
    public void testHashCode() {
        Address a1 = new Address();
        assertFalse(a1.hashCode() == new Gedcom().hashCode());
        assertEquals(a1.hashCode(), a1.hashCode());

        Address a2 = new Address();
        assertEquals(a1.hashCode(), a2.hashCode());

        a2 = new Address(a1);
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());

        a1.getCustomFacts(true).add(new CustomFact("TEST"));
        assertFalse(a1.hashCode() == a2.hashCode());
        a2.getCustomFacts(true).add(new CustomFact("TEST"));
        assertEquals(a1.hashCode(), a2.hashCode());
        a1.getCustomFacts().add(null);
        assertFalse(a1.hashCode() == a2.hashCode());
        a2.getCustomFacts().add(null);
        assertEquals(a1.hashCode(), a2.hashCode());

        a2.setAddr1("123 Main St.");
        assertFalse(a1.hashCode() == a2.hashCode());
        a1.setAddr1("123 Main St.");
        assertEquals(a1.hashCode(), a2.hashCode());

        a2.setAddr2("Apt. 456");
        assertFalse(a1.hashCode() == a2.hashCode());
        a1.setAddr2("Apt. 456");
        assertEquals(a1.hashCode(), a2.hashCode());

        a2.setAddr3("Room A");
        assertFalse(a1.hashCode() == a2.hashCode());
        a1.setAddr3("Room A");
        assertEquals(a1.hashCode(), a2.hashCode());

        a2.setCity("Anytown");
        assertFalse(a1.hashCode() == a2.hashCode());
        a1.setCity("Anytown");
        assertEquals(a1.hashCode(), a2.hashCode());

        a2.setCountry("USA");
        assertFalse(a1.hashCode() == a2.hashCode());
        a1.setCountry("USA");
        assertEquals(a1.hashCode(), a2.hashCode());

        a2.getLines(true).add("ABC");
        assertFalse(a1.equals(a2));
        a1.getLines(true).add("ABC");
        assertEquals(a1.hashCode(), a2.hashCode());
        a1.getLines().add(null);
        assertFalse(a1.hashCode() == a2.hashCode());
        a2.getLines().add(null);
        assertEquals(a1.hashCode(), a2.hashCode());

        a2.setPostalCode("12345");
        assertFalse(a1.hashCode() == a2.hashCode());
        a1.setPostalCode("12345");
        assertEquals(a1.hashCode(), a2.hashCode());

        a2.setStateProvince("ME");
        assertFalse(a1.hashCode() == a2.hashCode());
        a1.setStateProvince("ME");
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    /**
     * Test {@link Address#toString()}
     */
    @Test
    public void testToString() {
        Address a1 = new Address();
        a1.setAddr1("123 Main St.");
        a1.setAddr2("Apt. 456");
        a1.setAddr3("Room A");
        a1.setCity("Anytown");
        a1.setStateProvince("ME");
        a1.setCountry("USA");
        a1.setPostalCode("12345");
        a1.getLines(true).add("ABC");
        a1.getCustomFacts(true).add(new CustomFact("TEST"));
        assertEquals("Address [addr1=123 Main St., addr2=Apt. 456, addr3=Room A, city=Anytown, country=USA, lines=[ABC],"
                + " postalCode=12345, stateProvince=ME, customFacts=[CustomFact [tag=TEST, ]]]", a1.toString());
    }
}