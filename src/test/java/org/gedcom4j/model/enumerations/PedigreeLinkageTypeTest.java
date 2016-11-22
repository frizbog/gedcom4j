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
package org.gedcom4j.model.enumerations;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test for {@link PedigreeLinkageType}
 * 
 * @author frizbog
 */
public class PedigreeLinkageTypeTest {

    /**
     * Test {@link PedigreeLinkageType#getDescription()}
     */
    @Test
    public void testDescription() {
        assertEquals("indicates adoptive parents", PedigreeLinkageType.ADOPTED.getDescription());
    }

    /**
     * Test {@link PedigreeLinkageType#getForCode(String)}
     */
    @Test
    public void testGetForCode() {
        assertEquals(PedigreeLinkageType.ADOPTED, PedigreeLinkageType.getForCode("adopted"));
        assertEquals(PedigreeLinkageType.BIRTH, PedigreeLinkageType.getForCode("birth"));
        assertEquals(PedigreeLinkageType.FOSTER, PedigreeLinkageType.getForCode("foster"));
        assertEquals(PedigreeLinkageType.SEALING, PedigreeLinkageType.getForCode("sealing"));
        assertEquals(null, PedigreeLinkageType.getForCode("undefined"));
    }

    /**
     * Test {@link PedigreeLinkageType#toString()}
     */
    @Test
    public void testToString() {
        assertEquals("adopted", PedigreeLinkageType.ADOPTED.toString());
    }

}
