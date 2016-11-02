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

import org.junit.Test;

/**
 * Test copy constructor for {@link FamilyReference}
 * 
 * @author reckenrod
 */
public class FamilyReferenceCopyTest extends AbstractCopyTest{
	
     /**
     * Test copying a {@link FamilyReference} with no Family, which should never work
     */
    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testCopyWithoutFamily() {
        FamilyReference orig = new FamilyReference();
        FamilyReference copy = new FamilyReference(orig, false);
    }

    /**
     * Test copying a {@link FamilyReference} with a Family and values
     */
    @Test
    public void testWithValues() {
        Family f = new Family();
        FamilyReference orig = new FamilyReference(f);
        orig.setFamily(f);
        orig.getCustomFacts(true).add(getTestCustomFact());

        FamilyReference copy = new FamilyReference(orig, true);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
        assertEquals(orig.toString(), copy.toString());
        assertEquals(orig.hashCode(), copy.hashCode());
        
        orig.getFamily().setNumChildren("2");
        assertFalse("Copy shouldn't match if original changes", orig.equals(copy));
    }
}
