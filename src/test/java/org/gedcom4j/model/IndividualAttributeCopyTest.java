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
 * Test copy constructor for {@link IndividualAttribute}
 * 
 * @author frizbog
 */
public class IndividualAttributeCopyTest extends AbstractCopyTest {

    /**
     * Test copying a null {@link IndividualAttribute}, which should never work
     */
    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testCopyNull() {
        new IndividualAttribute(null);
    }

    /**
     * Test the simplest possible scenario - copy a new default {@link IndividualAttribute}
     */
    @Test
    public void testSimplestPossible() {
        IndividualAttribute orig = new IndividualAttribute();
        IndividualAttribute copy = new IndividualAttribute(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
    }

    /**
     * Test with values
     */
    @Test
    public void testWithValues() {
        IndividualAttribute orig = new IndividualAttribute();
        Address a = new Address();
        a.setAddr1(new StringWithCustomTags("AAA"));
        orig.setAddress(a);
        orig.setAge(new StringWithCustomTags("BBB"));
        orig.setCause(new StringWithCustomTags("CCC"));
        orig.getCustomTags(true).add(getTestCustomTags());
        orig.setDate(new StringWithCustomTags("DDD"));
        orig.setDescription(new StringWithCustomTags("EEE"));
        orig.getNotes(true).add(getTestNote());
        Place p = new Place();
        p.setLatitude(new StringWithCustomTags("50.2N"));
        p.setLongitude(new StringWithCustomTags("172.4W"));
        orig.setPlace(p);
        orig.setReligiousAffiliation(new StringWithCustomTags("FFF"));
        orig.setRespAgency(new StringWithCustomTags("GGG"));
        orig.setSubType(new StringWithCustomTags("HHH"));
        orig.setyNull("Y");

        IndividualAttribute copy = new IndividualAttribute(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
        assertEquals(orig.toString(), copy.toString());
    }

}
