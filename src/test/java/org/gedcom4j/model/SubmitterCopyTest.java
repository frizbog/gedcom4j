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
 * Test copy constructor for {@link Submitter}
 * 
 * @author frizbog
 */
public class SubmitterCopyTest extends AbstractCopyTest {

    /**
     * Test copying a null {@link Submitter}, which should never work
     */
    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testCopyNull() {
        new Submitter(null);
    }

    /**
     * Test the simplest possible scenario - copy a new default {@link Submitter}
     */
    @Test
    public void testSimplestPossible() {
        Submitter orig = new Submitter();
        Submitter copy = new Submitter(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
    }

    /**
     * Test with values
     */
    @Test
    public void testWithValues() {
        Submitter orig = new Submitter();
        orig.setAddress(getTestAddress());
        orig.getFaxNumbers(true).add(new StringWithCustomTags("555-1212"));
        orig.getPhoneNumbers(true).add(new StringWithCustomTags("555-1313"));
        orig.getWwwUrls(true).add(new StringWithCustomTags("www.nowhere.com"));
        orig.getEmails(true).add(new StringWithCustomTags("nobody@nowwhere.com"));
        ChangeDate cd = new ChangeDate();
        cd.setDate(new StringWithCustomTags("22 FEB 1922"));
        orig.setChangeDate(cd);
        orig.setName(new StringWithCustomTags("Steve /Submitter/"));
        orig.setRecIdNumber(new StringWithCustomTags("123"));
        orig.setRegFileNumber(new StringWithCustomTags("345"));
        orig.setXref("@SBM029@");
        orig.getLanguagePref(true).add(new StringWithCustomTags("English"));
        orig.getLanguagePref(true).add(new StringWithCustomTags("German"));
        orig.getCustomTags(true).add(getTestCustomTags());
        Multimedia m = new Multimedia();
        m.setXref("@M123@");
        m.setRecIdNumber(new StringWithCustomTags("987"));
        orig.getMultimedia(true).add(m);
        UserReference u = new UserReference();
        u.setReferenceNum(new StringWithCustomTags("555"));
        orig.getUserReferences(true).add(u);

        Submitter copy = new Submitter(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
        assertEquals(orig.toString(), copy.toString());
    }

}
