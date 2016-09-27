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
