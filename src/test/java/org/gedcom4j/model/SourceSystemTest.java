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
public class SourceSystemTest {

    /**
     * Test method for {@link org.gedcom4j.model.SourceSystem#equals(java.lang.Object)} .
     */
    @Test
    public void testEqualsObject() {
        SourceSystem ss1 = new SourceSystem();
        assertEquals(ss1, ss1);
        SourceSystem ss2 = new SourceSystem();
        assertEquals("equals() should be the same for equivalent objects", ss1, ss2);

        ss1.setProductName(new StringWithCustomTags("Frying Pan"));
        assertFalse("They're not equivalent anymore, so equals should be false", ss1.equals(ss2));
        ss2.setProductName(new StringWithCustomTags("Frying Pan"));
        assertEquals("equals() should be the same for equivalent objects", ss1, ss2);
        ss1.setProductName(null);
        assertFalse("They're not equivalent anymore, so equals should be false", ss1.equals(ss2));
        ss2.setProductName(null);
        assertEquals("equals() should be the same for equivalent objects", ss1, ss2);

        ss1.setSystemId("Frying Pan");
        assertFalse("They're not equivalent anymore, so equals should be false", ss1.equals(ss2));
        ss2.setSystemId("Frying Pan");
        assertEquals("equals() should be the same for equivalent objects", ss1, ss2);
        ss1.setSystemId(null);
        assertFalse("They're not equivalent anymore, so equals should be false", ss1.equals(ss2));
        ss2.setSystemId(null);

        assertEquals("equals() should be the same for equivalent objects", ss1, ss2);
        assertFalse(ss1.equals(null));
        assertFalse(ss1.equals(this));
    }

    /**
     * Test method for {@link org.gedcom4j.model.SourceSystem#hashCode()}.
     */
    @Test
    public void testHashCode() {
        SourceSystem ss1 = new SourceSystem();
        SourceSystem ss2 = new SourceSystem();
        assertEquals("Hashcodes should be the same for equivalent objects", ss1.hashCode(), ss2.hashCode());
        ss1.setProductName(new StringWithCustomTags("Frying Pan"));
        assertFalse("They're not equivalent anymore, so the hashcodes should differ", ss1.hashCode() == ss2.hashCode());
        ss2.setProductName(new StringWithCustomTags("Frying Pan"));

        assertEquals("Hashcodes should be the same for equivalent objects", ss1.hashCode(), ss2.hashCode());
    }

}
