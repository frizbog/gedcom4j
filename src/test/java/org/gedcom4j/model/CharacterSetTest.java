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

import org.junit.Test;

/**
 * @author frizbog1
 * 
 */
public class CharacterSetTest {

    /**
     * Test method for {@link org.gedcom4j.model.CharacterSet#equals(java.lang.Object)} .
     */
    @Test
    public void testEqualsObject() {
        CharacterSet cs1 = new CharacterSet();
        assertEquals(cs1, cs1);

        CharacterSet cs2 = new CharacterSet();
        assertEquals("objects are equal, so equals() should return true", cs1, cs2);
        cs1.setCharacterSetName(new StringWithCustomTags("Frying Pan"));
        assertFalse("objects are not equal, so equals() should not return true", cs1.equals(cs2));
        cs2.setCharacterSetName(new StringWithCustomTags("Frying Pan"));
        assertEquals("objects are equal again, so equals() should return true", cs1, cs2);
        assertFalse(cs1.equals(null));
        assertFalse(cs1.equals(this));
    }

    /**
     * Test method for {@link org.gedcom4j.model.CharacterSet#hashCode()}.
     */
    @Test
    public void testHashCode() {
        CharacterSet cs1 = new CharacterSet();
        CharacterSet cs2 = new CharacterSet();
        assertEquals("objects are equal, so hashcodes should match", cs1.hashCode(), cs2.hashCode());
        cs1.setCharacterSetName(new StringWithCustomTags("Frying Pan"));
        assertFalse("objects are not equal, so hashcodes should not match", cs1.hashCode() == cs2.hashCode());
        cs2.setCharacterSetName(new StringWithCustomTags("Frying Pan"));
        assertEquals("objects are equal again, so hashcodes should match", cs1.hashCode(), cs2.hashCode());
    }

}
