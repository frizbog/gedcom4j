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
package org.gedcom4j.io.encoding;

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
        String actual = classUnderTest.toUtf16(
                "Sa grand-m\u00E1ere l'a nourrie car sa m\u00E1ere \u00E2etait plac\u00E2ee nourrice dans une famille de riches. "
                        + "Son oncle ( une tante en r\u00E2ealit\u00E2e Agueda), allait\u00E2e avec elle, est d\u00E2ec\u00E2ed\u00E2e ( voir acte). "
                        + "Elle croyait qu'il \u00E2etait mort \u00E1a cause d'elle, en lui pre");
        String expected = "Sa grand-m\u00E8re l'a nourrie car sa m\u00E8re \u00E9tait plac\u00E9e nourrice dans une famille de riches. "
                + "Son oncle ( une tante en r\u00E9alit\u00E9 Agueda), allait\u00E9 avec elle, est d\u00E9c\u00E9d\u00E9 ( voir acte). "
                + "Elle croyait qu'il \u00E9tait mort \u00E0 cause d'elle, en lui pre";
        assertEquals(expected, actual);
    }

    /**
     * This is a simple read test with diacriticals
     */
    @Test
    public void testReadDiacriticalsShort() {
        assertEquals("2 GIVN Dolor\u00E8s", classUnderTest.toUtf16("2 GIVN Dolor\u00E1es"));
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
        assertEquals(
                "Sa grand-m\u00E1ere l'a nourrie car sa m\u00E1ere \u00E2etait plac\u00E2ee nourrice dans une famille de riches. "
                        + "Son oncle ( une tante en r\u00E2ealit\u00E2e Agueda), allait\u00E2e avec elle, est d\u00E2ec\u00E2ed\u00E2e ( voir acte). "
                        + "Elle croyait qu'il \u00E2etait mort \u00E1a cause d'elle, en lui pre", classUnderTest.toAnsel(
                                "Sa grand-m\u00E8re l'a nourrie car sa m\u00E8re \u00E9tait plac\u00E9e nourrice dans une famille de riches. "
                                        + "Son oncle ( une tante en r\u00E9alit\u00E9 Agueda), allait\u00E9 avec elle, est d\u00E9c\u00E9d\u00E9 ( voir acte). "
                                        + "Elle croyait qu'il \u00E9tait mort \u00E0 cause d'elle, en lui pre"));
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
        assertEquals("\u00E0A\u00E0B\u00E0C\u00E0D\u00E0E\u00E0F\u00E0G\u00E0H\u00E0I\u00E0J\u00E0K\u00E0L\u00E0M", classUnderTest
                .toAnsel("\u1EA2B\u0309C\u0309D\u0309\u1EBAF\u0309G\u0309H\u0309\u1EC8J\u0309K\u0309L\u0309M\u0309"));
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
        assertEquals("\u00E1A\u00E1B\u00E1C\u00E1D\u00E1E\u00E1F\u00E1G\u00E1H\u00E1I\u00E1J\u00E1K\u00E1L\u00E1M", classUnderTest
                .toAnsel("\u00C0B\u0300C\u0300D\u0300\u00C8F\u0300G\u0300H\u0300\u00CCJ\u0300K\u0300L\u0300M\u0300"));
    }

    /**
     * This is a short test for extended characters - degree sign
     */
    @Test
    public void testWriteExtendedShort() {
        assertEquals("4 LATI +50\u00C0 3' 1.49\"", classUnderTest.toAnsel("4 LATI +50\u00B0 3' 1.49\""));
    }
}
