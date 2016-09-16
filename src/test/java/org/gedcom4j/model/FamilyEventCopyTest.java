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
 * Test copy constructor for {@link FamilyEvent}
 * 
 * @author frizbog
 */
public class FamilyEventCopyTest extends AbstractCopyTest {

    /**
     * Test copying a null {@link FamilyEvent}, which should never work
     */
    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testCopyNull() {
        new FamilyEvent(null);
    }

    /**
     * Test the simplest possible scenario - copy a new default {@link FamilyEvent}
     */
    @Test
    public void testSimplestPossible() {
        FamilyEvent orig = new FamilyEvent();
        FamilyEvent copy = new FamilyEvent(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
    }

    /**
     * Test with values
     */
    @Test
    public void testWithValues() {
        FamilyEvent orig = new FamilyEvent();
        orig.setAddress(getTestAddress());
        orig.getNotes(true).add(getTestNote());
        orig.getFaxNumbers(true).add(new StringWithCustomTags("555-1212"));
        orig.getPhoneNumbers(true).add(new StringWithCustomTags("555-1313"));
        orig.getWwwUrls(true).add(new StringWithCustomTags("www.nowhere.com"));
        orig.getEmails(true).add(new StringWithCustomTags("nobody@nowwhere.com"));
        orig.setAge(new StringWithCustomTags("1"));
        orig.setCause(new StringWithCustomTags("Just because"));
        orig.setDate(new StringWithCustomTags("03 MAR 1903"));
        orig.setDescription(new StringWithCustomTags("That thing with the stuff"));
        orig.setHusbandAge(new StringWithCustomTags("123"));
        Place p = new Place();
        p.setPlaceName("Right there");
        orig.setPlace(p);
        orig.setReligiousAffiliation(new StringWithCustomTags("None"));
        orig.setRespAgency(new StringWithCustomTags("The Avengers"));
        orig.setRestrictionNotice(new StringWithCustomTags("Don't tell anyone....sssssshhh!"));
        orig.setSubType(new StringWithCustomTags("mnbvcxz"));
        orig.setType(FamilyEventType.EVENT);
        orig.getCitations(true).add(getTestCitation());
        orig.getCustomTags(true).add(getTestCustomTags());
        orig.setWifeAge(new StringWithCustomTags("124"));
        orig.setYNull("Y");
        Multimedia m = new Multimedia();
        m.setXref("@M123@");
        orig.getMultimedia(true).add(m);

        FamilyEvent copy = new FamilyEvent(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
        assertEquals(orig.toString(), copy.toString());
    }

}
