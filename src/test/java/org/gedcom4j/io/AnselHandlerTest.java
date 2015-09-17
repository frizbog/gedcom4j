/*
 * Copyright (c) 2009-2014 Matthew R. Harrah
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
package org.gedcom4j.io;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test for {@link AnselHandler}
 * 
 * @author frizbog
 */
public class AnselHandlerTest {

    /**
     * The class being tested
     */
    AnselHandler classUnderTest = new AnselHandler();

    /**
     * This is a longer read test with diacriticals
     */
    @Test
    public void testReadDiacriticalsLong() {
        assertEquals("Sa grand-mère l'a nourrie car sa mère était placée nourrice dans une famille de riches. "
                + "Son oncle ( une tante en réalité Agueda), allaité avec elle, est décédé ( voir acte). "
                + "Elle croyait qu'il était mort \u00E0 cause d'elle, en lui pre",
                classUnderTest.toUtf16("Sa grand-máere l'a nourrie car sa máere âetait placâee nourrice dans une famille de riches. "
                        + "Son oncle ( une tante en râealitâe Agueda), allaitâe avec elle, est dâecâedâe ( voir acte). "
                        + "Elle croyait qu'il âetait mort áa cause d'elle, en lui pre"));
    }

    /**
     * This is a simple read test with diacriticals
     */
    @Test
    public void testReadDiacriticalsShort() {
        assertEquals("2 GIVN Dolor\u00E8s", classUnderTest.toUtf16("2 GIVN Doloráes"));
    }

    /**
     * This is a longer read test with diacriticals. Note that:
     * <ul>
     * <li>pre-composed diacritics are broken down into combining diacritics</li>
     * <li>combining diacritics are converted from UNICODE to ANSEL characters</li>
     * <li>combining diacritics precede the character modified, as required by ANSEL</li>
     * </ul>
     */
    @Test
    public void testWriteDiacriticalsLong() {
        assertEquals("Sa grand-máere l'a nourrie car sa máere âetait placâee nourrice dans une famille de riches. "
                + "Son oncle ( une tante en râealitâe Agueda), allaitâe avec elle, est dâecâedâe ( voir acte). "
                + "Elle croyait qu'il âetait mort áa cause d'elle, en lui pre",
                classUnderTest.toAnsel("Sa grand-mère l'a nourrie car sa mère était placée nourrice dans une famille de riches. "
                        + "Son oncle ( une tante en réalité Agueda), allaité avec elle, est décédé ( voir acte). "
                        + "Elle croyait qu'il était mort \u00E0 cause d'elle, en lui pre"));
    }

    /**
     * This is a short test for diacritics. Note that:
     * <ul>
     * <li>pre-composed diacritics are broken down into combining diacritics</li>
     * <li>combining diacritics are converted from UNICODE to ANSEL characters</li>
     * <li>combining diacritics precede the character modified, as required by ANSEL</li>
     * </ul>
     */
    @Test
    public void testWriteDiacriticsShort1() {
        assertEquals("\u00E0A\u00E0B\u00E0C\u00E0D\u00E0E\u00E0F\u00E0G\u00E0H\u00E0I\u00E0J\u00E0K\u00E0L\u00E0M",
                classUnderTest.toAnsel("ẢB\u0309C\u0309D\u0309ẺF\u0309G\u0309H\u0309ỈJ\u0309K\u0309L\u0309M\u0309"));
    }

    /**
     * This is a short test for diacritics. Note that:
     * <ul>
     * <li>pre-composed diacritics are broken down into combining diacritics</li>
     * <li>combining diacritics are converted from UNICODE to ANSEL characters</li>
     * <li>combining diacritics precede the character modified, as required by ANSEL</li>
     * </ul>
     */
    @Test
    public void testWriteDiacriticsShort2() {
        assertEquals("áAáBáCáDáEáFáGáHáIáJáKáLáM", classUnderTest.toAnsel("ÀB\u0300C\u0300D\u0300ÈF\u0300G\u0300H\u0300ÌJ\u0300K\u0300L\u0300M\u0300"));
    }

    /**
     * This is a short test for extended characters - degree sign
     */
    @Test
    public void testWriteExtendedShort() {
        assertEquals("4 LATI +50\u00C0 3' 1.49\"", classUnderTest.toAnsel("4 LATI +50° 3' 1.49\""));
    }
}
