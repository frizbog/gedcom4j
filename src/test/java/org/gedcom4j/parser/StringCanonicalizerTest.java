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
package org.gedcom4j.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Random;

import org.gedcom4j.parser.StringCanonicalizer.CanonicalizedString;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link StringCanonicalizer}
 * 
 * @author frizbog
 */
public class StringCanonicalizerTest {

    /**
     * The class under test
     */
    StringCanonicalizer classUnderTest = new StringCanonicalizer();

    /**
     * A randomizer that always uses the same seed value for repeated pseudo-random sequences
     */
    Random random = new Random(12345);

    /**
     * Reset the canonicalizer before each test
     */
    @Before
    public void setUp() {
        classUnderTest.reset();
    }

    /**
     * Test with a larger population that should exceed the pool size, but some of the entries should be below the usage threshold,
     * which means eviction should get triggered
     */
    @Test
    public void testLargerPopulation() {
        // Load the string pool
        final int numValues = classUnderTest.getMaxPoolSize() + 100;
        for (int values = 0; values < numValues; values++) {
            for (int uses = 0; uses < 20 + random.nextInt(20); uses++) {
                String s = Integer.toString(values);
                String c = classUnderTest.getCanonicalVersion(s);
                assertEquals(s, c);
            }
        }

        assertEquals(1, classUnderTest.numEvictions);
        assertNull(classUnderTest.stringPool.get("499"));
        CanonicalizedString c = classUnderTest.stringPool.get("498");
        assertNotNull(c);
        assertEquals("498", c.value);
        assertEquals(25, c.count);

        // Make sure we get canonical versions
        for (int values = 0; values < numValues; values++) {
            String s = Integer.toString(values);
            String c2 = classUnderTest.getCanonicalVersion(s);
            assertEquals(s, c2);
            StringCanonicalizer.CanonicalizedString cv = classUnderTest.stringPool.get(s);
            if (cv != null) {
                assertSame(cv.value, c2);
            }
        }

    }

    /**
     * Test with a medium population that stays within pool size, varying number of references, and no evictions needed
     */
    @Test
    public void testMediumPopulation() {
        // Load the string pool
        final int numValues = classUnderTest.getMaxPoolSize() - 1;
        String[] canons = new String[numValues];
        for (int values = 0; values < numValues; values++) {
            for (int uses = 0; uses < 25 + random.nextInt(25); uses++) {
                String s = Integer.toString(values);
                canons[values] = classUnderTest.getCanonicalVersion(s);
                assertEquals(s, canons[values]);
            }
        }

        // Make sure we get canonical versions
        for (int values = 0; values < numValues; values++) {
            String s = Integer.toString(values);
            String c = classUnderTest.getCanonicalVersion(s);
            assertEquals(c, canons[values]);
            assertSame(c, canons[values]);
        }
    }

    /**
     * Test with a small population
     */
    @Test
    public void testSmallPopulation() {
        // Load the string pool
        int numValues = 100;
        String[] canons = new String[numValues];
        for (int values = 0; values < numValues; values++) {
            for (int uses = 0; uses < 25 + random.nextInt(25); uses++) {
                String s = Integer.toString(values);
                canons[values] = classUnderTest.getCanonicalVersion(s);
                assertEquals(s, canons[values]);
            }
        }

        // Make sure we get canonical versions
        for (int values = 0; values < numValues; values++) {
            String s = Integer.toString(values);
            String c = classUnderTest.getCanonicalVersion(s);
            assertEquals(c, canons[values]);
            assertSame(c, canons[values]);
        }

    }

    /**
     * Test with a larger population that should exceed the pool size, but some of the entries should be below the usage threshold,
     * which means eviction should get triggered
     */
    @Test
    public void testVeryLargePopulation() {
        // Load the string pool
        final int numValues = classUnderTest.getMaxPoolSize() * 3;
        for (int values = 0; values < numValues; values++) {
            for (int uses = 0; uses < 20 + random.nextInt(20); uses++) {
                String s = Integer.toString(values);
                String c = classUnderTest.getCanonicalVersion(s);
                assertEquals(s, c);
            }
        }

        assertEquals(8, classUnderTest.numEvictions);
        /*
         * Because we used a java.util.Random with a fixed seed, which items remain in the pool should be predictable, so these
         * asserts should be safe
         */
        assertNull(classUnderTest.stringPool.get("499"));
        CanonicalizedString c = classUnderTest.stringPool.get("498");
        assertNotNull(c);
        assertEquals("498", c.value);
        assertEquals(25, c.count);

        // Make sure we get canonical versions
        for (int values = 0; values < numValues; values++) {
            String s = Integer.toString(values);
            StringCanonicalizer.CanonicalizedString cv = classUnderTest.stringPool.get(s);
            if (cv != null) {
                String c2 = classUnderTest.getCanonicalVersion(s);
                assertEquals(s, c2);
                assertSame(cv.value, c2);
            }
        }

    }

}
