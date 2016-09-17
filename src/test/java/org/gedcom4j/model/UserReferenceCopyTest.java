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
 * Test copy constructor for {@link UserReference}
 * 
 * @author frizbog
 */
public class UserReferenceCopyTest extends AbstractCopyTest {

    /**
     * Test copying a null {@link UserReference}, which should never work
     */
    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testCopyNull() {
        new UserReference(null);
    }

    /**
     * Test the simplest possible scenario - copy a new default {@link UserReference}
     */
    @Test
    public void testSimplestPossible() {
        UserReference orig = new UserReference();
        UserReference copy = new UserReference(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
    }

    /**
     * Test with values
     */
    @Test
    public void testWithValues() {
        UserReference orig = new UserReference();
        orig.setReferenceNum("PPP");
        orig.setType("QQQ");
        StringTree ct = new StringTree();
        ct.setTag("_RRR");
        ct.setValue("SSS");
        ct.setLevel(1);
        ct.setXref("@TTT@");
        orig.getCustomTags(true).add(ct);

        UserReference copy = new UserReference(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
        assertEquals(orig.toString(), copy.toString());

        orig.getReferenceNum().setValue("UUU");
        assertEquals("Copy should not change if original does", "PPP", copy.getReferenceNum().getValue());
    }
}
