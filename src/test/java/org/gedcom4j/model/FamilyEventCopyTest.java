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

import org.gedcom4j.model.enumerations.FamilyEventType;
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
        orig.getNoteStructures(true).add(getTestNoteStructure());
        orig.getFaxNumbers(true).add(new StringWithCustomFacts("555-1212"));
        orig.getPhoneNumbers(true).add(new StringWithCustomFacts("555-1313"));
        orig.getWwwUrls(true).add(new StringWithCustomFacts("www.nowhere.com"));
        orig.getEmails(true).add(new StringWithCustomFacts("nobody@nowwhere.com"));
        orig.setAge("1");
        orig.setCause("Just because");
        orig.setDate("03 MAR 1903");
        orig.setDescription("That thing with the stuff");
        orig.setHusbandAge("123");
        Place p = new Place();
        p.setPlaceName("Right there");
        orig.setPlace(p);
        orig.setReligiousAffiliation("None");
        orig.setRespAgency("The Avengers");
        orig.setRestrictionNotice("Don't tell anyone....sssssshhh!");
        orig.setSubType("mnbvcxz");
        orig.setType(FamilyEventType.EVENT);
        orig.getCitations(true).add(getTestCitation());
        orig.getCustomFacts(true).add(getTestCustomFact());
        orig.setWifeAge("124");
        orig.setYNull("Y");
        Multimedia m = new Multimedia();
        m.setXref("@M123@");
        orig.getMultimedia(true).add(new MultimediaReference(m));

        FamilyEvent copy = new FamilyEvent(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
        assertEquals(orig.toString(), copy.toString());
    }

}
