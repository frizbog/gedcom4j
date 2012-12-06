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
package org.gedcom4j.validate;

import org.gedcom4j.validate.Severity;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link Severity}
 * 
 * @author frizbog1
 * 
 */
public class SeverityTest {

    /**
     * Test for {@link Severity} and {@link Severity#valueOf(String)}
     */
    @Test
    public void testEnum() {
        Assert.assertEquals(Severity.valueOf("ERROR"), Severity.ERROR);
        Assert.assertEquals(Severity.valueOf("WARNING"), Severity.WARNING);
        Assert.assertEquals(Severity.valueOf("INFO"), Severity.INFO);
    }

    /**
     * Test for {@link Severity#values()}
     */
    @Test
    public void testValues() {
        Assert.assertNotNull(Severity.values());
        Assert.assertEquals(3, Severity.values().length);
    }

}