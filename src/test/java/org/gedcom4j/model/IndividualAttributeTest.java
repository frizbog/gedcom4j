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

        i1.citations.add(new CitationWithoutSource());
        assertFalse(i1.equals(i2));
        i2.citations.add(new CitationWithoutSource());
        assertEquals(i1, i2);

        i1.customTags.add(new StringTree());
        assertFalse(i1.equals(i2));
        i2.customTags.add(new StringTree());
        assertEquals(i1, i2);

        i1.date = new StringWithCustomTags("Three");
        assertFalse(i1.equals(i2));
        i2.date = new StringWithCustomTags("Three");
        assertEquals(i1, i2);

        i1.description = new StringWithCustomTags("Four");
        assertFalse(i1.equals(i2));
        i2.description = new StringWithCustomTags("Four");
        assertEquals(i1, i2);

        i1.emails.add(new StringWithCustomTags("Five"));
        assertFalse(i1.equals(i2));
        i2.emails.add(new StringWithCustomTags("Five"));
        assertEquals(i1, i2);

        i1.faxNumbers.add(new StringWithCustomTags("Six"));
        assertFalse(i1.equals(i2));
        i2.faxNumbers.add(new StringWithCustomTags("Six"));
        assertEquals(i1, i2);

        i1.multimedia.add(new Multimedia());
        assertFalse(i1.equals(i2));
        i2.multimedia.add(new Multimedia());
        assertEquals(i1, i2);

        i1.notes.add(new Note());
        assertFalse(i1.equals(i2));
        i2.notes.add(new Note());
        assertEquals(i1, i2);

        i1.phoneNumbers.add(new StringWithCustomTags("Seven"));
        assertFalse(i1.equals(i2));
        i2.phoneNumbers.add(new StringWithCustomTags("Seven"));
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

        i1.type = IndividualAttributeType.FACT;
        assertFalse(i1.equals(i2));
        i2.type = IndividualAttributeType.FACT;
        assertEquals(i1, i2);

        i1.wwwUrls.add(new StringWithCustomTags("Twelve"));
        assertFalse(i1.equals(i2));
        i2.wwwUrls.add(new StringWithCustomTags("Twelve"));
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

        i1.citations.add(new CitationWithoutSource());
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.citations.add(new CitationWithoutSource());
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.customTags.add(new StringTree());
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.customTags.add(new StringTree());
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.date = new StringWithCustomTags("Three");
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.date = new StringWithCustomTags("Three");
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.description = new StringWithCustomTags("Four");
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.description = new StringWithCustomTags("Four");
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.emails.add(new StringWithCustomTags("Five"));
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.emails.add(new StringWithCustomTags("Five"));
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.faxNumbers.add(new StringWithCustomTags("Six"));
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.faxNumbers.add(new StringWithCustomTags("Six"));
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.multimedia.add(new Multimedia());
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.multimedia.add(new Multimedia());
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.notes.add(new Note());
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.notes.add(new Note());
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.phoneNumbers.add(new StringWithCustomTags("Seven"));
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.phoneNumbers.add(new StringWithCustomTags("Seven"));
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

        i1.type = IndividualAttributeType.FACT;
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.type = IndividualAttributeType.FACT;
        assertEquals(i1.hashCode(), i2.hashCode());

        i1.wwwUrls.add(new StringWithCustomTags("Twelve"));
        assertTrue(i1.hashCode() != i2.hashCode());
        i2.wwwUrls.add(new StringWithCustomTags("Twelve"));
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
        assertEquals("IndividualAttribute [type=null]", i.toString());

        i.address = new Address();
        i.age = new StringWithCustomTags("One");
        i.cause = new StringWithCustomTags("Two");
        i.citations.add(new CitationWithoutSource());
        i.customTags.add(new StringTree());
        i.date = new StringWithCustomTags("Three");
        i.description = new StringWithCustomTags("Four");
        i.emails.add(new StringWithCustomTags("Five"));
        i.faxNumbers.add(new StringWithCustomTags("Six"));
        i.multimedia.add(new Multimedia());
        i.notes.add(new Note());
        i.phoneNumbers.add(new StringWithCustomTags("Seven"));
        i.place = new Place();
        i.religiousAffiliation = new StringWithCustomTags("Eight");
        i.respAgency = new StringWithCustomTags("Nine");
        i.restrictionNotice = new StringWithCustomTags("Ten");
        i.subType = new StringWithCustomTags("Eleven");
        i.type = IndividualAttributeType.FACT;
        i.wwwUrls.add(new StringWithCustomTags("Twelve"));
        i.yNull = "Thirteen";

        assertEquals("IndividualAttribute [type=FACT, address=Address [lines=[], customTags=[]], age=One, cause=Two, date=Three, description=Four, "
                + "place=Place [citations=[], notes=[], phonetic=[], romanized=[], customTags=[]], subType=Eleven]", i.toString());
    }

}
