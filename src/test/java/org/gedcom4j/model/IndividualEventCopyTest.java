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
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

/**
 * Test copy constructor for {@link IndividualEvent}
 * 
 * @author frizbog
 */
public class IndividualEventCopyTest extends AbstractCopyTest {

    /**
     * Test copying a null {@link IndividualEvent}, which should never work
     */
    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testCopyNull() {
        new IndividualEvent(null);
    }

    /**
     * Test the simplest possible scenario - copy a new default {@link IndividualEvent}
     */
    @Test
    public void testSimplestPossible() {
        IndividualEvent orig = new IndividualEvent();
        IndividualEvent copy = new IndividualEvent(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
    }

    /**
     * Test with values
     */
    @Test
    public void testWithValues() {
        IndividualEvent orig = new IndividualEvent();
        orig.setAddress(getTestAddress());
        orig.setAge(new StringWithCustomTags("890"));
        orig.setCause(new StringWithCustomTags("Mom said so"));
        orig.setDate(new StringWithCustomTags("04 SEP 1905"));
        orig.setDescription(new StringWithCustomTags("It was glorious."));
        FamilyChild fc = new FamilyChild();
        fc.setStatus(new StringWithCustomTags("XXX"));
        orig.setFamily(fc);
        orig.getNotes(true).add(getTestNote());
        orig.getFaxNumbers(true).add(new StringWithCustomTags("555-1212"));
        orig.getPhoneNumbers(true).add(new StringWithCustomTags("555-1313"));
        orig.getWwwUrls(true).add(new StringWithCustomTags("www.nowhere.com"));
        orig.getEmails(true).add(new StringWithCustomTags("nobody@nowwhere.com"));
        Place p = new Place();
        p.setPlaceName("Charleston, WV");
        orig.setPlace(p);
        orig.setReligiousAffiliation(new StringWithCustomTags("XXX"));
        orig.setRespAgency(new StringWithCustomTags("YYY"));
        orig.setRestrictionNotice(new StringWithCustomTags("ZZZ"));
        orig.setSubType(new StringWithCustomTags("!@#"));
        orig.setType(IndividualEventType.PROBATE);
        orig.setSubType(new StringWithCustomTags("000"));

        IndividualEvent copy = new IndividualEvent(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
        assertEquals(orig.toString(), copy.toString());
    }

}
