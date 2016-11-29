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
 * Test for {@link PersonalName}
 *
 * @author reckenrod
 * 
 */
public class PersonalNameTest {

    /**
     * Test for {@link PersonalName#equals(Object)}
     */
    @Test
    @SuppressWarnings({ "PMD.EqualsNull", "PMD.ExcessiveMethodLength", "PMD.NcssMethodCount" })
    public void testEquals() {
        PersonalName pn1 = new PersonalName();
        assertFalse(pn1.equals(null));
        assertFalse(pn1.equals(new Place()));
        assertEquals(pn1, pn1);

        PersonalName pn2 = new PersonalName();
        assertEquals(pn1, pn2);

        pn1.getCustomFacts(true).add(new CustomFact("TEST"));
        assertFalse(pn1.equals(pn2));
        pn2.getCustomFacts(true).add(new CustomFact("TEST"));
        assertEquals(pn1, pn2);

        pn2.setBasic("BillWallace");
        assertFalse(pn1.equals(pn2));
        pn1.setBasic("BillWallace");
        assertEquals(pn1, pn2);
        pn2.setBasic((String) null);
        assertFalse(pn1.equals(pn2));
        pn1.setBasic((String) null);
        assertEquals(pn1, pn2);

        pn2.getCitations(true).add(new CitationWithSource());
        assertFalse(pn1.equals(pn2));
        pn1.getCitations(true).add(new CitationWithSource());
        assertEquals(pn1, pn2);
        pn1.getCitations().clear();
        assertFalse(pn1.equals(pn2));
        pn2.getCitations().clear();
        assertEquals(pn1, pn2);

        pn2.setGivenName("William");
        assertFalse(pn1.equals(pn2));
        pn1.setGivenName("William");
        assertEquals(pn1, pn2);
        pn1.setGivenName((String) null);
        assertFalse(pn1.equals(pn2));
        pn2.setGivenName((String) null);
        assertEquals(pn1, pn2);

        pn2.setNickname("Bill");
        assertFalse(pn1.equals(pn2));
        pn1.setNickname("Bill");
        assertEquals(pn1, pn2);
        pn1.setNickname((String) null);
        assertFalse(pn1.equals(pn2));
        pn2.setNickname((String) null);
        assertEquals(pn1, pn2);

        pn2.getPhonetic(true).add(new PersonalNameVariation());
        assertFalse(pn1.equals(pn2));
        pn1.getPhonetic(true).add(new PersonalNameVariation());
        assertEquals(pn1, pn2);
        pn1.getPhonetic().clear();
        assertFalse(pn1.equals(pn2));
        pn2.getPhonetic().clear();
        assertEquals(pn1, pn2);

        pn2.setPrefix("Mr.");
        assertFalse(pn1.equals(pn2));
        pn1.setPrefix("Mr.");
        assertEquals(pn1, pn2);
        pn1.setPrefix((String) null);
        assertFalse(pn1.equals(pn2));
        pn2.setPrefix((String) null);
        assertEquals(pn1, pn2);

        pn2.getRomanized(true).add(new PersonalNameVariation());
        assertFalse(pn1.equals(pn2));
        pn1.getRomanized(true).add(new PersonalNameVariation());
        assertEquals(pn1, pn2);
        pn1.getRomanized().clear();
        assertFalse(pn1.equals(pn2));
        pn2.getRomanized().clear();
        assertEquals(pn1, pn2);

        pn2.setSuffix("Sr.");
        assertFalse(pn1.equals(pn2));
        pn1.setSuffix("Sr.");
        assertEquals(pn1, pn2);
        pn1.setSuffix((String) null);
        assertFalse(pn1.equals(pn2));
        pn2.setSuffix((String) null);
        assertEquals(pn1, pn2);

        pn2.setSurname("Wallace");
        assertFalse(pn1.equals(pn2));
        pn1.setSurname("Wallace");
        assertEquals(pn1, pn2);
        pn1.setSurname((String) null);
        assertFalse(pn1.equals(pn2));
        pn2.setSurname((String) null);
        assertEquals(pn1, pn2);

        pn2.setSurnamePrefix("Mc");
        assertFalse(pn1.equals(pn2));
        pn1.setSurnamePrefix("Mc");
        assertEquals(pn1, pn2);
        pn1.setSurnamePrefix((String) null);
        assertFalse(pn1.equals(pn2));
        pn2.setSurnamePrefix((String) null);
        assertEquals(pn1, pn2);

        pn2.setType("Test");
        assertFalse(pn1.equals(pn2));
        pn1.setType("Test");
        assertEquals(pn1, pn2);
        pn1.setType((String) null);
        assertFalse(pn1.equals(pn2));
        pn2.setType((String) null);
        assertEquals(pn1, pn2);
    }
}