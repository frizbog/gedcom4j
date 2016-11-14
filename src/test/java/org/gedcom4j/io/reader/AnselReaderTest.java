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
package org.gedcom4j.io.reader;

import static org.junit.Assert.assertEquals;

import org.gedcom4j.exception.GedcomParserException;
import org.junit.Test;

/**
 * Test for {@link AnselReader}
 * 
 * @author frizbog
 */
public class AnselReaderTest {

    /**
     * Test for {@link AnselReader#getLevelFromLine(String)}
     * 
     * @throws GedcomParserException
     *             if there's a problem parsing data
     */
    @Test
    public void testGetLevelFromLine() throws GedcomParserException {
        AnselReader ar = new AnselReader(null, null);
        assertEquals(0, ar.getLevelFromLine("0 the rest doesn't matter"));
        assertEquals(1, ar.getLevelFromLine("1 the rest doesn't matter"));
        assertEquals(1, ar.getLevelFromLine("1 1 1 1 the rest doesn't matter"));
        assertEquals(99, ar.getLevelFromLine("99 1 1 1 the rest doesn't matter"));
    }

    /**
     * Test for {@link AnselReader#getLevelFromLine(String)}
     * 
     * @throws GedcomParserException
     *             if there's a problem parsing data
     */
    @Test(expected = GedcomParserException.class)
    public void testGetLevelFromLineNegative() throws GedcomParserException {
        AnselReader ar = new AnselReader(null, null);
        ar.getLevelFromLine("Doesn't begin with number");
    }

    /**
     * Test for {@link AnselReader#getLevelFromLine(String)}
     * 
     * @throws GedcomParserException
     *             if there's a problem parsing data
     */
    @Test(expected = GedcomParserException.class)
    public void testGetLevelFromLineNegative2() throws GedcomParserException {
        AnselReader ar = new AnselReader(null, null);
        ar.getLevelFromLine("9A Doesn't begin with number followed by space");
    }

    /**
     * Test for {@link AnselReader#getLevelFromLine(String)}
     * 
     * @throws GedcomParserException
     *             if there's a problem parsing data
     */
    @Test(expected = GedcomParserException.class)
    public void testGetLevelFromLineNegative3() throws GedcomParserException {
        AnselReader ar = new AnselReader(null, null);
        ar.getLevelFromLine("99A Doesn't begin with number followed by space");
    }

    /**
     * Test for {@link AnselReader#getLevelFromLine(String)}
     * 
     * @throws GedcomParserException
     *             if there's a problem parsing data
     */
    @Test(expected = GedcomParserException.class)
    public void testGetLevelFromLineNegative4() throws GedcomParserException {
        AnselReader ar = new AnselReader(null, null);
        ar.getLevelFromLine("100 Doesn't begin with number followed by space");
    }

    /**
     * Test for {@link AnselReader#getLevelFromLine(String)}
     * 
     * @throws GedcomParserException
     *             if there's a problem parsing data
     */
    @Test(expected = GedcomParserException.class)
    public void testGetLevelFromLineNegative5() throws GedcomParserException {
        AnselReader ar = new AnselReader(null, null);
        ar.getLevelFromLine("    Doesn't begin with number followed by space - leading spaces");
    }

}
