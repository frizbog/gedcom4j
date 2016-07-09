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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test for {@link IndividualAttribute}
 * 
 * @author frizbog
 * 
 */
public class IndividualAttributeTest {

    /**
     * Test for {@link IndividualAttribute#equals(Object)}
     */
    @Test
    public void testEqualsObject() {
        IndividualAttribute i1 = new IndividualAttribute();
        IndividualAttribute i2 = new IndividualAttribute();

        assertNotSame(i1, i2);
        assertEquals(i1, i2);

        i1.address = new Address();
        assertFalse(i1.equals(i2));
        i2.address = new Address();
        assertEquals(i1, i2);

        i1.age = new StringWithCustomTags("One");
        assertFalse(i1.equals(i2));
        i2.age = new StringWithCustomTags("One");
        assertEquals(i1, i2);

        i1.cause = new StringWithCustomTags("Two");
        assertFalse(i1.equals(i2));
        i2.cause = new StringWithCustomTags("Two");
        assertEquals(i1, i2);

        i1.getCitations(true).add(new CitationWithoutSource());
        assertFalse(i1.equals(i2));
        i2.getCitations(true).add(new CitationWithoutSource());
        assertEquals(i1, i2);

        i1.getCustomTags(true).add(new StringTree());
        assertFalse(i1.equals(i2));
        i2.getCustomTags(true).add(new StringTree());
        assertEquals(i1, i2);

        i1.date = new StringWithCustomTags("Three");
        assertFalse(i1.equals(i2));
        i2.date = new StringWithCustomTags("Three");
        assertEquals(i1, i2);

        i1.description = new StringWithCustomTags("Four");
        assertFalse(i1.equals(i2));
        i2.description = new StringWithCustomTags("Four");
        assertEquals(i1, i2);

        i1.getEmails(true).add(new StringWithCustomTags("Five"));
        assertFalse(i1.equals(i2));
        i2.getEmails(true).add(new StringWithCustomTags("Five"));
        assertEquals(i1, i2);

        i1.getFaxNumbers(true).add(new StringWithCustomTags("Six"));
        assertFalse(i1.equals(i2));
        i2.getFaxNumbers(true).add(new StringWithCustomTags("Six"));
        assertEquals(i1, i2);

        i1.getMultimedia(true).add(new Multimedia());
        assertFalse(i1.equals(i2));
        i2.getMultimedia(true).add(new Multimedia());
        assertEquals(i1, i2);

        i1.getNotes(true).add(new Note());
        assertFalse(i1.equals(i2));
        i2.getNotes(true).add(new Note());
        assertEquals(i1, i2);

        i1.getPhoneNumbers(true).add(new StringWithCustomTags("Seven"));
        assertFalse(i1.equals(i2));
        i2.getPhoneNumbers(true).add(new StringWithCustomTags("Seven"));
        assertEquals(i1, i2);

        i1.place = new Place();
        assertFalse(i1.equals(i2));
        i2.place = new Place();
        assertEquals(i1, i2);

        i1.religiousAffiliation = new StringWithCustomTags("Eight");
        assertFalse(i1.equals(i2));
        i2.religiousAffiliation = new StringWithCustomTags("Eight");
        assertEquals(i1, i2);

        i1.respAgency = new StringWithCustomTags("Nine");
        assertFalse(i1.equals(i2));
        i2.respAgency = new StringWithCustomTags("Nine");
        assertEquals(i1, i2);

        i1.restrictionNotice = new StringWithCustomTags("Ten");
        assertFalse(i1.equals(i2));
        i2.restrictionNotice = new StringWithCustomTags("Ten");
        assertEquals(i1, i2);

        i1.subType = new StringWithCustomTags("Eleven");
        assertFalse(i1.equals(i2));
        i2.subType = new StringWithCustomTags("Eleven");
        assertEquals(i1, i2);

        i1.setType(IndividualAttributeType.FACT);
        assertFalse(i1.equals(i2));
        i2.setType(IndividualAttributeType.FACT);
        assertEquals(i1, i2);

        i1.getWwwUrls(true).add(new StringWithCustomTags("Twelve"));
        assertFalse(i1.equals(i2));
        i2.getWwwUrls(true).add(new StringWithCustomTags("Twelve"));
        assertEquals(i1, i2);

        i1.yNull = "Thirteen";
        assertFalse(i1.equals(i2));
        i2.yNull = "Thirteen";
        assertEquals(i1, i2);

    }

    /**
     * Test for {@link IndividualAttribute#hashCode()}
     * 
     */
    @Test
    public void testHashCode() {
        IndividualAttribute i1 = new IndividualAttribute();
        IndividualAttribute i2 = new IndividualAttribute();

        assertNotSame(i1, i2);
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.address = new Address();
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.address = new Address();
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.age = new StringWithCustomTags("One");
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.age = new StringWithCustomTags("One");
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.cause = new StringWithCustomTags("Two");
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.cause = new StringWithCustomTags("Two");
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.getCitations(true).add(new CitationWithoutSource());
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.getCitations(true).add(new CitationWithoutSource());
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.getCustomTags(true).add(new StringTree());
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.getCustomTags(true).add(new StringTree());
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.date = new StringWithCustomTags("Three");
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.date = new StringWithCustomTags("Three");
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.description = new StringWithCustomTags("Four");
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.description = new StringWithCustomTags("Four");
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.getEmails(true).add(new StringWithCustomTags("Five"));
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.getEmails(true).add(new StringWithCustomTags("Five"));
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.getFaxNumbers(true).add(new StringWithCustomTags("Six"));
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.getFaxNumbers(true).add(new StringWithCustomTags("Six"));
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.getMultimedia(true).add(new Multimedia());
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.getMultimedia(true).add(new Multimedia());
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.getNotes(true).add(new Note());
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.getNotes(true).add(new Note());
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.getPhoneNumbers(true).add(new StringWithCustomTags("Seven"));
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.getPhoneNumbers(true).add(new StringWithCustomTags("Seven"));
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.place = new Place();
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.place = new Place();
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.religiousAffiliation = new StringWithCustomTags("Eight");
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.religiousAffiliation = new StringWithCustomTags("Eight");
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.respAgency = new StringWithCustomTags("Nine");
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.respAgency = new StringWithCustomTags("Nine");
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.restrictionNotice = new StringWithCustomTags("Ten");
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.restrictionNotice = new StringWithCustomTags("Ten");
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.subType = new StringWithCustomTags("Eleven");
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.subType = new StringWithCustomTags("Eleven");
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.setType(IndividualAttributeType.FACT);
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.setType(IndividualAttributeType.FACT);
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.getWwwUrls(true).add(new StringWithCustomTags("Twelve"));
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.getWwwUrls(true).add(new StringWithCustomTags("Twelve"));
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.yNull = "Thirteen";
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.yNull = "Thirteen";
        assertEquals(i1.hashCode(), i2.hashCode());

    }

    /**
     * Test for {@link IndividualAttribute#toString()}
     */
    @Test
    public void testToString() {
        IndividualAttribute i = new IndividualAttribute();
        assertEquals("IndividualAttribute []", i.toString());

        i.address = new Address();
        i.age = new StringWithCustomTags("One");
        i.cause = new StringWithCustomTags("Two");
        i.getCitations(true).add(new CitationWithoutSource());
        i.getCustomTags(true).add(new StringTree());
        i.date = new StringWithCustomTags("Three");
        i.description = new StringWithCustomTags("Four");
        i.getEmails(true).add(new StringWithCustomTags("Five"));
        i.getFaxNumbers(true).add(new StringWithCustomTags("Six"));
        i.getMultimedia(true).add(new Multimedia());
        i.getNotes(true).add(new Note());
        i.getPhoneNumbers(true).add(new StringWithCustomTags("Seven"));
        i.place = new Place();
        i.religiousAffiliation = new StringWithCustomTags("Eight");
        i.respAgency = new StringWithCustomTags("Nine");
        i.restrictionNotice = new StringWithCustomTags("Ten");
        i.subType = new StringWithCustomTags("Eleven");
        i.setType(IndividualAttributeType.FACT);
        i.getWwwUrls(true).add(new StringWithCustomTags("Twelve"));
        i.yNull = "Thirteen";

        assertEquals("IndividualAttribute [type=Fact, address=Address [], age=One, cause=Two, citations=[CitationWithoutSource []], "
                + "date=Three, description=Four, emails=[Five], faxNumbers=[Six], multimedia=[Multimedia []], notes=[Note []], "
                + "phoneNumbers=[Seven], place=Place [], religiousAffiliation=Eight, respAgency=Nine, restrictionNotice=Ten, "
                + "subType=Eleven, wwwUrls=[Twelve], yNull=Thirteen, customTags=[Line 0: 0 (null tag) (null value)]]", i.toString());
    }

}
