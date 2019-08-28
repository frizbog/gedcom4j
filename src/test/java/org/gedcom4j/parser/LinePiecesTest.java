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

import org.gedcom4j.exception.GedcomParserException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test for {@link LinePieces}
 * 
 * @author frizbog
 */
public class LinePiecesTest {

    /**
     * A rule that can be used to check exceptions
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Negative test case when the level is non-numeric
     * 
     * @throws GedcomParserException
     *             if anything goes wrong
     */
    @SuppressWarnings("unused")
    @Test
    public void testLinePiecesNegativeBadLevel() throws GedcomParserException {
        thrown.expect(GedcomParserException.class);
        thrown.expectMessage("Line 5 does not begin with a 1 or 2 digit number for the level followed by a space:");
        new LinePieces("BAD TAGG additional stuff", 5);
    }

    /**
     * Negative test case when the xref is not terminated
     * 
     * @throws GedcomParserException
     *             if anything goes wrong
     */
    @SuppressWarnings("unused")
    @Test
    public void testLinePiecesNegativeBadXref() throws GedcomParserException {
        thrown.expect(GedcomParserException.class);
        thrown.expectMessage("XRef ID begins with @ sign but is not terminated with one on line 5");
        new LinePieces("4 @XREF TAGG additional stuff", 5);
    }

    /**
     * Negative test case when there is no tag
     * 
     * @throws GedcomParserException
     *             if anything goes wrong
     */
    @SuppressWarnings("unused")
    @Test
    public void testLinePiecesNegativeNothingAfterXref() throws GedcomParserException {
        thrown.expect(GedcomParserException.class);
        thrown.expectMessage("All GEDCOM lines are required to have a tag value, but no tag could be found on line 5");
        new LinePieces("4 @XREF@", 5);
    }

    /**
     * Simple happy-path positive test for {@link LinePieces} constructor
     * 
     * @throws GedcomParserException
     *             if anything goes wrong
     */
    @Test
    public void testLinePiecesPositive() throws GedcomParserException {
        LinePieces lp = new LinePieces("4 @XREF@ TAGG additional stuff", 5);
        assertNotNull(lp);
        assertEquals(4, lp.level);
        assertEquals("@XREF@", lp.id);
        assertEquals("TAGG", lp.tag);
        assertEquals("additional stuff", lp.remainder);
    }

    /**
     * Test case when extra space before tag
     *
     * @throws GedcomParserException
     *             if anything goes wrong
     */
    @Test
    public void testLinePiecesWithExtraSpaceBeforeTag() throws GedcomParserException {
        LinePieces lp = new LinePieces("4 @XREF@  TAGG extra space before tag", 5);
        assertNotNull(lp);
        assertEquals(4, lp.level);
        assertEquals("@XREF@", lp.id);
        assertEquals("TAGG", lp.tag);
        assertEquals("extra space before tag", lp.remainder);
    }

    /**
     * Test case when the xref has spaces
     *
     * @throws GedcomParserException
     *             if anything goes wrong
     */

    @Test
    public void testLinePiecesXrefWithSpace() throws GedcomParserException {
        LinePieces lp = new LinePieces("4 @XR EF@ TAGG additional stuff", 5);
        assertNotNull(lp);
        assertEquals(4, lp.level);
        assertEquals("@XR EF@", lp.id);
        assertEquals("TAGG", lp.tag);
        assertEquals("additional stuff", lp.remainder);
    }

    /**
     * Simple happy-path positive test for {@link LinePieces} constructor
     * 
     * @throws GedcomParserException
     *             if anything goes wrong
     */
    @Test
    public void testLinePiecesPositiveNothingAfterTag() throws GedcomParserException {
        LinePieces lp = new LinePieces("4 TAGG", 5);
        assertNotNull(lp);
        assertEquals(4, lp.level);
        assertNull(lp.id);
        assertEquals("TAGG", lp.tag);
        assertNull(lp.remainder);
    }

    /**
     * Simple happy-path positive test for {@link LinePieces} constructor
     * 
     * @throws GedcomParserException
     *             if anything goes wrong
     */
    @Test
    public void testLinePiecesPositiveNoXref() throws GedcomParserException {
        LinePieces lp = new LinePieces("4 TAGG additional stuff", 5);
        assertNotNull(lp);
        assertEquals(4, lp.level);
        assertNull(lp.id);
        assertEquals("TAGG", lp.tag);
        assertEquals("additional stuff", lp.remainder);
    }

}
