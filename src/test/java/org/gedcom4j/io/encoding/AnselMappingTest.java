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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Test;

/**
 * @author frizbog1
 * 
 */
public class AnselMappingTest {

    /**
     * Test that the constructor is private
     * 
     * @throws NoSuchMethodException
     *             if the method can't be found
     * @throws IllegalAccessException
     *             if the method cannot be accessed
     * @throws InvocationTargetException
     *             the reflected target throws an exception
     * @throws InstantiationException
     *             if the class can't be instantiated
     */
    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
            InstantiationException {
        Class<AnselMapping> clazz = AnselMapping.class;
        assertTrue("class must be final", Modifier.isFinal(clazz.getModifiers()));
        assertEquals("There must be only one constructor", 1, clazz.getDeclaredConstructors().length);
        final Constructor<AnselMapping> constructor = clazz.getDeclaredConstructor();
        if (constructor.isAccessible() || !Modifier.isPrivate(constructor.getModifiers())) {
            fail("constructor is not private");
        }
        constructor.setAccessible(true);
        constructor.newInstance();
        constructor.setAccessible(false);
        for (final Method method : clazz.getMethods()) {
            if (!Modifier.isStatic(method.getModifiers()) && method.getDeclaringClass().equals(clazz)) {
                fail("there exists a non-static method:" + method);
            }
        }
    }

    /**
     * Test decoding bytes into characters
     */
    @Test
    public void testDecode() {
        assertEquals("Capital A is ASCII/ANSEL 0x41", 'A', AnselMapping.decode((char) 0x41));
        assertEquals("Lowercase z is ASCII/ANSEL 0x7A", 'z', AnselMapping.decode((char) 0x7A));
        assertEquals("The unicode character for a capital L with a slash through it (U+0141, or \u0141) is ANSEL A1", '\u0141',
                AnselMapping.decode((char) 0xA1));
        assertEquals("The unicode character for a musical flat (U+266D, or \u266D) is ANSEL A9", '\u266D', AnselMapping.decode(
                (char) 0xA9));
    }

    /**
     * Test encoding characters into bytes
     */
    @Test
    public void testEncode() {
        assertEquals("Capital A is ASCII/ANSEL 0x41", 0x41, AnselMapping.encode('A'));
        assertEquals("Lowercase z is ASCII/ANSEL 0x7A", 0x7A, AnselMapping.encode('z'));
        assertEquals("The unicode character for a capital L with a slash through it (U+0141, or \u0141) is ANSEL A1", 0xA1,
                AnselMapping.encode('\u0141'));
        assertEquals("The unicode character for a musical flat (U+266D, or \u266D) is ANSEL A9", 0xA9, AnselMapping.encode(
                '\u266D'));
    }

    /**
     * Test for {@link AnselMapping#isUnicodeCombiningDiacritic(char)}
     */
    @Test
    public void testIsCombiningDiacritic() {
        assertFalse(AnselMapping.isUnicodeCombiningDiacritic('\u02FF'));
        assertTrue(AnselMapping.isUnicodeCombiningDiacritic('\u0300'));
        assertTrue(AnselMapping.isUnicodeCombiningDiacritic('\u0333'));
        assertFalse(AnselMapping.isUnicodeCombiningDiacritic('\u0334'));
        assertFalse(AnselMapping.isUnicodeCombiningDiacritic('\uFE1F'));
        assertTrue(AnselMapping.isUnicodeCombiningDiacritic('\uFE20'));
        assertTrue(AnselMapping.isUnicodeCombiningDiacritic('\uFE23'));
        assertFalse(AnselMapping.isUnicodeCombiningDiacritic('\uFE24'));
    }

    /**
     * Test more decodings
     */
    @Test
    public void testMoreDecoding() {
        assertEquals('\u20AC', AnselMapping.decode('\u00C8'));
        assertEquals('\u0338', AnselMapping.decode('\u00FC'));
        assertEquals('?', AnselMapping.decode('\u00FD'));
    }

    /**
     * Test more encodings
     */
    @Test
    public void testMoreEncoding() {
        assertEquals('\u00CD', AnselMapping.encode('\u0065'));
        assertEquals('\u00CE', AnselMapping.encode('\u006F'));
        assertEquals('\u00FC', AnselMapping.encode('\u0338'));
        assertEquals('\u00C8', AnselMapping.encode('\u20AC'));
    }
}
