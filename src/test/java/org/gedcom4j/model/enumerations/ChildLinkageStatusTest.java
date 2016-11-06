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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Tests for {@link ChildLinkageStatus}
 * 
 * @author reckenrod
 */
public class ChildLinkageStatusTest {

    /**
     * Test for {@link ChildLinkageStatus#getForCode(String)}
     */
    @Test
    public void testGetForCode() {
        assertSame(ChildLinkageStatus.CHALLENGED, ChildLinkageStatus.getForCode("challenged"));
        assertSame(ChildLinkageStatus.DISPROVEN, ChildLinkageStatus.getForCode("disproven"));
        assertSame(ChildLinkageStatus.PROVEN, ChildLinkageStatus.getForCode("proven"));
        assertNull(ChildLinkageStatus.getForCode("BAD"));
        assertNull(ChildLinkageStatus.getForCode(null));
        assertNull(ChildLinkageStatus.getForCode(""));
    }
    
    /**
     * Test for {@link ChildLinkageStatus#getCode()}
     */
    @Test
    public void testGetCode() {
        assertSame(ChildLinkageStatus.CHALLENGED.getCode(), "challenged");
        assertSame(ChildLinkageStatus.DISPROVEN.getCode(), "disproven");
        assertSame(ChildLinkageStatus.PROVEN.getCode(), "proven");
    }
    
    /**
     * Test for {@link ChildLinkageStatus#getDescription()}
     */
    @Test
    public void testGetDescription() {
        assertSame(ChildLinkageStatus.CHALLENGED.getDescription(), "suspect but not proven or disproven");
        assertSame(ChildLinkageStatus.DISPROVEN.getDescription(), "there has been a challenge but linkage was disproven");
        assertSame(ChildLinkageStatus.PROVEN.getDescription(), "there has been a challenge but linkage was proven");
    }
    
    /**
     * Test for {@link ChildLinkageStatus#toString()}
     */
    @Test
    public void testToString() {
        assertSame(ChildLinkageStatus.CHALLENGED.getCode(), ChildLinkageStatus.CHALLENGED.toString());
        assertSame(ChildLinkageStatus.DISPROVEN.getCode(), ChildLinkageStatus.DISPROVEN.toString());
        assertSame(ChildLinkageStatus.PROVEN.getCode(), ChildLinkageStatus.PROVEN.toString());
    }
}
