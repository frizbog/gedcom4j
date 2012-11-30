/*
 * Copyright (c) 2009-2012 Matthew R. Harrah
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
package com.mattharrah.gedcom4j.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for {@link SourceCallNumber}
 * 
 * @author frizbog1
 * 
 */
public class SourceCallNumberTest {
    /**
     * Test equals and hashcode after instantiation
     */
    @Test
    public void testBasic() {
        SourceCallNumber scn1 = new SourceCallNumber();
        SourceCallNumber scn2 = new SourceCallNumber();
        assertTrue(scn1.equals(scn2));
        assertTrue(scn1.hashCode() == scn2.hashCode());
    }

    /**
     * Test equals and hashcode after perturbing the callNumber property
     */
    @Test
    public void testCallNumber() {
        SourceCallNumber scn1 = new SourceCallNumber();
        SourceCallNumber scn2 = new SourceCallNumber();
        scn1.callNumber = "Foo";
        assertFalse(scn1.equals(scn2));
        assertFalse(scn1.hashCode() == scn2.hashCode());
        scn2.callNumber = "Foo";
        assertTrue(scn1.equals(scn2));
        assertTrue(scn1.hashCode() == scn2.hashCode());
    }

    /**
     * Test equals and hashcode after perturbing the mediaType property
     */
    @Test
    public void testMediaType() {
        SourceCallNumber scn1 = new SourceCallNumber();
        SourceCallNumber scn2 = new SourceCallNumber();
        scn1.mediaType = "Foo";
        assertFalse(scn1.equals(scn2));
        assertFalse(scn1.hashCode() == scn2.hashCode());
        scn2.mediaType = "Foo";
        assertTrue(scn1.equals(scn2));
        assertTrue(scn1.hashCode() == scn2.hashCode());
    }
}
