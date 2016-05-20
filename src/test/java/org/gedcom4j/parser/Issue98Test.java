package org.gedcom4j.parser;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

/**
 * Test for issue 98 - making sure byte order markers in unicode files parse correctly
 * 
 * @author frizbog
 */
public class Issue98Test {

    /**
     * Test issue 98 - load a UTF-16 file with big-endian byte order marker
     * 
     * @throws GedcomParserException
     *             if the data cannot be parsed
     * @throws IOException
     *             if the data cannot be read
     */
    @Test
    public void testUtf16BigEndianWithByteOrderMarker() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/utf16be.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue(gp.warnings.isEmpty());
    }

    /**
     * Test issue 98 - load a UTF-16 file with big-endian byte order marker
     * 
     * @throws GedcomParserException
     *             if the data cannot be parsed
     * @throws IOException
     *             if the data cannot be read
     */
    @Test
    public void testUtf16LittleEndianWithByteOrderMarker() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/utf16le.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue(gp.warnings.isEmpty());
    }

    /**
     * Test issue 98 - load a UTF16 file with big-endian byte order marker
     * 
     * @throws GedcomParserException
     *             if the data cannot be parsed
     * @throws IOException
     *             if the data cannot be read
     */
    @Test
    public void testUtf8WithByteOrderMarker() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/utf8.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue(gp.warnings.isEmpty());
    }

}
