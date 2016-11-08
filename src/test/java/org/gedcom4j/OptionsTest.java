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
package org.gedcom4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Test;

/**
 * Test for {@link Options}
 * 
 * @author frizbog
 *
 */
public class OptionsTest {
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
        Class<Options> clazz = Options.class;
        assertTrue("class must be final", Modifier.isFinal(clazz.getModifiers()));
        assertEquals("There must be only one constructor", 1, clazz.getDeclaredConstructors().length);
        final Constructor<Options> constructor = clazz.getDeclaredConstructor();
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
}
