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

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.InMemoryGedcom;
import org.gedcom4j.model.IGedcom;
import org.gedcom4j.model.NoteRecord;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A test to load the "stress test" file, (sample/TGC551.ged) and check that some of the weirder elements parsed as expected.
 * 
 * @author frizbog
 */
@SuppressWarnings("PMD.TooManyMethods")
public class StressFileSpecialCharacterReadTest {

    /**
     * The GEDCOM file loaded from the stress test
     */
    private static IGedcom g;

    /**
     * Load the stress test file
     * 
     * @throws IOException
     *             if the file cannot be read
     * @throws GedcomParserException
     *             if the file cannot be parsed
     */
    @BeforeClass
    public static void setUpClass() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser(new InMemoryGedcom());
        gp.load("sample/TGC551.ged");
        g = gp.getGedcom();
    }

    /**
     * Check the character set used
     */
    @Test
    public void testCharacterSet() {
        assertEquals("ANSEL", g.getHeader().getCharacterSet().getCharacterSetName().getValue());
    }

    /**
     * Check diacritics for ANSEL E0.
     */
    @Test
    public void testDiacriticsE0() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // E0 (Unicode: hook above, 0309) low rising tone mark
        assertEqualsCharByChar("     \u1EA2B\u0309C\u0309D\u0309\u1EBAF\u0309G\u0309H\u0309\u1EC8J\u0309K\u0309L\u0309M\u0309", note
                .getLines().get(7));
        assertEqualsCharByChar("     N\u0309\u1ECEP\u0309Q\u0309R\u0309S\u0309T\u0309\u1EE6V\u0309W\u0309X\u0309\u1EF6Z\u0309", note
                .getLines().get(8));
        assertEqualsCharByChar("     \u1EA3b\u0309c\u0309d\u0309\u1EBBf\u0309g\u0309h\u0309\u1EC9j\u0309k\u0309l\u0309m\u0309", note
                .getLines().get(9));
        assertEqualsCharByChar("     n\u0309\u1ECFp\u0309q\u0309r\u0309s\u0309t\u0309\u1EE7v\u0309w\u0309x\u0309\u1EF7z\u0309", note
                .getLines().get(10));

    }

    /**
     * Check diacritics for ANSEL E1.
     */
    @Test
    public void testDiacriticsE1() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // E1 (Unicode: grave, 0300) grave accent
        assertEqualsCharByChar("     \u00C0B\u0300C\u0300D\u0300\u00C8F\u0300G\u0300H\u0300\u00CCJ\u0300K\u0300L\u0300M\u0300", note
                .getLines().get(13));
        assertEqualsCharByChar("     \u01F8\u00D2P\u0300Q\u0300R\u0300S\u0300T\u0300\u00D9V\u0300\u1E80X\u0300\u1EF2Z\u0300", note
                .getLines().get(14));
        assertEqualsCharByChar("     \u00E0b\u0300c\u0300d\u0300\u00E8f\u0300g\u0300h\u0300\u00ECj\u0300k\u0300l\u0300m\u0300", note
                .getLines().get(15));
        assertEqualsCharByChar("     \u01F9\u00F2p\u0300q\u0300r\u0300s\u0300t\u0300\u00F9v\u0300\u1E81x\u0300\u1EF3z\u0300", note
                .getLines().get(16));

    }

    /**
     * Check diacritics for ANSEL E2.
     */
    @Test
    public void testDiacriticsE2() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // E2 (Unicode: acute, 0301) acute accent
        assertEqualsCharByChar("     \u00C1B\u0301\u0106D\u0301\u00C9F\u0301\u01F4H\u0301\u00CDJ\u0301\u1E30\u0139\u1E3E", note
                .getLines().get(19));
        assertEqualsCharByChar("     \u0143\u00D3\u1E54Q\u0301\u0154\u015AT\u0301\u00DAV\u0301\u1E82X\u0301\u00DD\u0179", note
                .getLines().get(20));
        assertEqualsCharByChar("     \u00E1b\u0301\u0107d\u0301\u00E9f\u0301\u01F5h\u0301\u00EDj\u0301\u1E31\u013A\u1E3F", note
                .getLines().get(21));
        assertEqualsCharByChar("     \u0144\u00F3\u1E55q\u0301\u0155\u015Bt\u0301\u00FAv\u0301\u1E83x\u0301\u00FD\u017A", note
                .getLines().get(22));

    }

    /**
     * Check diacritics for ANSEL E3.
     */
    @Test
    public void testDiacriticsE3() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());
        // E3 (Unicode: circumflex, 0302) circumflex accent
        assertEqualsCharByChar("     \u00C2B\u0302\u0108D\u0302\u00CAF\u0302\u011C\u0124\u00CE\u0134K\u0302L\u0302M\u0302", note
                .getLines().get(25));
        assertEqualsCharByChar("     N\u0302\u00D4P\u0302Q\u0302R\u0302\u015CT\u0302\u00DBV\u0302\u0174X\u0302\u0176\u1E90", note
                .getLines().get(26));
        assertEqualsCharByChar("     \u00E2b\u0302\u0109d\u0302\u00EAf\u0302\u011D\u0125\u00EE\u0135k\u0302l\u0302m\u0302", note
                .getLines().get(27));
        assertEqualsCharByChar("     n\u0302\u00F4p\u0302q\u0302r\u0302\u015Dt\u0302\u00FBv\u0302\u0175x\u0302\u0177\u1E91", note
                .getLines().get(28));

    }

    /**
     * Check diacritics for ANSEL E4.
     */
    @Test
    public void testDiacriticsE4() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // E4 (Unicode: tilde, 0303) tilde
        assertEqualsCharByChar("     \u00C3B\u0303C\u0303D\u0303\u1EBCF\u0303G\u0303H\u0303\u0128J\u0303K\u0303L\u0303M\u0303", note
                .getLines().get(31));
        assertEqualsCharByChar("     \u00D1\u00D5P\u0303Q\u0303R\u0303S\u0303T\u0303\u0168\u1E7CW\u0303X\u0303\u1EF8Z\u0303", note
                .getLines().get(32));
        assertEqualsCharByChar("     \u00D1b\u0303c\u0303d\u0303\u1EBDf\u0303g\u0303h\u0303\u0129j\u0303k\u0303l\u0303m\u0303", note
                .getLines().get(33));
        assertEqualsCharByChar("     \u00F1\u00F5p\u0303q\u0303r\u0303s\u0303t\u0303\u0169\u1E7Dw\u0303x\u0303\u1EF9z\u0303", note
                .getLines().get(34));

    }

    /**
     * Check diacritics for ANSEL E5.
     */
    @Test
    public void testDiacriticsE5() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // E5 (Unicode: macron, 0304) macron
        assertEqualsCharByChar("     \u0100B\u0304C\u0304D\u0304\u0112F\u0304\u1E20H\u0304\u012AJ\u0304K\u0304L\u0304M\u0304", note
                .getLines().get(37));
        assertEqualsCharByChar("     N\u0304\u014CP\u0304Q\u0304R\u0304S\u0304T\u0304\u016AV\u0304W\u0304X\u0304\u0232Z\u0304", note
                .getLines().get(38));
        assertEqualsCharByChar("     \u0101b\u0304c\u0304d\u0304\u0113f\u0304\u1E21h\u0304\u012Bj\u0304k\u0304l\u0304m\u0304", note
                .getLines().get(39));
        assertEqualsCharByChar("     n\u0304\u014Dp\u0304q\u0304r\u0304s\u0304t\u0304\u016Bv\u0304w\u0304x\u0304\u0233z\u0304", note
                .getLines().get(40));

    }

    /**
     * Check diacritics for ANSEL E6.
     */
    @Test
    public void testDiacriticsE6() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // E6 (Unicode: breve, 0306) breve
        assertEqualsCharByChar("     \u0102B\u0306C\u0306D\u0306\u0114F\u0306\u011EH\u0306\u012CJ\u0306K\u0306L\u0306M\u0306", note
                .getLines().get(43));
        assertEqualsCharByChar("     N\u0306\u014EP\u0306Q\u0306R\u0306S\u0306T\u0306\u016CV\u0306W\u0306X\u0306Y\u0306Z\u0306",
                note.getLines().get(44));
        assertEqualsCharByChar("     \u0103b\u0306c\u0306d\u0306\u0115f\u0306\u011Fh\u0306\u012Dj\u0306k\u0306l\u0306m\u0306", note
                .getLines().get(45));
        assertEqualsCharByChar("     n\u0306\u014Fp\u0306q\u0306r\u0306s\u0306t\u0306\u016Dv\u0306w\u0306x\u0306y\u0306z\u0306",
                note.getLines().get(46));

    }

    /**
     * Check diacritics for ANSEL E7.
     */
    @Test
    public void testDiacriticsE7() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // E7 (Unicode: dot above, 0307) dot above
        assertEqualsCharByChar("     \u0226\u1E02\u010A\u1E0A\u0116\u1E1E\u0120\u1E22\u0130J\u0307K\u0307L\u0307\u1E40", note
                .getLines().get(49));
        assertEqualsCharByChar("     \u1E44\u022E\u1E56Q\u0307\u1E58\u1E60\u1E6AU\u0307V\u0307\u1E86\u1E8A\u1E8E\u017B", note
                .getLines().get(50));
        assertEqualsCharByChar("     \u0227\u1E03\u010B\u1E0B\u0117\u1E1F\u0121\u1E23i\u0307j\u0307k\u0307l\u0307\u1E41", note
                .getLines().get(51));
        assertEqualsCharByChar("     \u1E45\u022F\u1E57q\u0307\u1E59\u1E61\u1E6Bu\u0307v\u0307\u1E87\u1E8B\u1E8F\u017C", note
                .getLines().get(52));

    }

    /**
     * Check diacritics for ANSEL E8.
     */
    @Test
    public void testDiacriticsE8() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // E8 (Unicode: diaeresis, 0308) umlaut (dieresis)
        assertEqualsCharByChar("     \u00C4B\u0308C\u0308D\u0308\u00CBF\u0308G\u0308\u1E26\u00CFJ\u0308K\u0308L\u0308M\u0308", note
                .getLines().get(55));
        assertEqualsCharByChar("     N\u0308\u00D6P\u0308Q\u0308R\u0308S\u0308T\u0308\u00DCV\u0308\u1E84\u1E8C\u0178Z\u0308", note
                .getLines().get(56));
        assertEqualsCharByChar("     \u00E4b\u0308c\u0308d\u0308\u00EBf\u0308g\u0308\u1E27\u00EFj\u0308k\u0308l\u0308m\u0308", note
                .getLines().get(57));
        assertEqualsCharByChar("     n\u0308\u00F6p\u0308q\u0308r\u0308s\u0308\u1E97\u00FCv\u0308\u1E85\u1E8D\u00FFz\u0308", note
                .getLines().get(58));

    }

    /**
     * Check diacritics for ANSEL E9.
     */
    @Test
    public void testDiacriticsE9() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // E9 (Unicode: caron, 030C) hacek - some fonts render this like an upper right apostrophe instead of a normal
        // caron
        assertEqualsCharByChar("     \u01CDB\u030c\u010C\u010E\u011AF\u030c\u01E6\u021E\u01CFJ\u030c\u01E8\u013DM\u030c", note
                .getLines().get(61));
        assertEqualsCharByChar("     \u0147\u01D1P\u030cQ\u030c\u0158\u0160\u0164\u01D3V\u030cW\u030cX\u030cY\u030c\u017D", note
                .getLines().get(62));
        assertEqualsCharByChar("     \u01CEb\u030c\u010D\u010F\u011Bf\u030c\u01E7\u021F\u01D0\u01F0\u01E9\u013Em\u030c", note
                .getLines().get(63));
        assertEqualsCharByChar("     \u0148\u01D2p\u030cq\u030c\u0159\u0161\u0165\u01D4v\u030cw\u030cx\u030cy\u030c\u017E", note
                .getLines().get(64));

    }

    /**
     * Check diacritics for ANSEL EA.
     */
    @Test
    public void testDiacriticsEA() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // EA (Unicode: ring above, 030A) circle above (angstrom)
        assertEqualsCharByChar("     \u00C5B\u030AC\u030AD\u030AE\u030AF\u030AG\u030AH\u030AI\u030AJ\u030AK\u030AL\u030AM\u030A",
                note.getLines().get(67));
        assertEqualsCharByChar("     N\u030AO\u030AP\u030AQ\u030AR\u030AS\u030AT\u030A\u016EV\u030AW\u030AX\u030AY\u030AZ\u030A",
                note.getLines().get(68));
        assertEqualsCharByChar("     \u00E5b\u030Ac\u030Ad\u030Ae\u030Af\u030Ag\u030Ah\u030Ai\u030Aj\u030Ak\u030Al\u030Am\u030A",
                note.getLines().get(69));
        assertEqualsCharByChar("     n\u030Ao\u030Ap\u030Aq\u030Ar\u030As\u030At\u030A\u016Fv\u030A\u1E98x\u030A\u1E99z\u030A", note
                .getLines().get(70));

    }

    /**
     * Check diacritics for ANSEL EB.
     */
    @Test
    public void testDiacriticsEB() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // EB (Unicode: ligature left half, FE20) ligature, left half
        assertEqualsCharByChar("     A\uFE20B\uFE20C\uFE20D\uFE20E\uFE20F\uFE20G\uFE20H\uFE20I\uFE20J\uFE20K\uFE20L\uFE20M\uFE20",
                note.getLines().get(73));
        assertEqualsCharByChar("     N\uFE20O\uFE20P\uFE20Q\uFE20R\uFE20S\uFE20T\uFE20U\uFE20V\uFE20W\uFE20X\uFE20Y\uFE20Z\uFE20",
                note.getLines().get(74));
        assertEqualsCharByChar("     a\uFE20b\uFE20c\uFE20d\uFE20e\uFE20f\uFE20g\uFE20h\uFE20i\uFE20j\uFE20k\uFE20l\uFE20m\uFE20",
                note.getLines().get(75));
        assertEqualsCharByChar("     n\uFE20o\uFE20p\uFE20q\uFE20r\uFE20s\uFE20t\uFE20u\uFE20v\uFE20w\uFE20x\uFE20y\uFE20z\uFE20",
                note.getLines().get(76));

    }

    /**
     * Check diacritics for ANSEL EC.
     */
    @Test
    public void testDiacriticsEC() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // EC (Unicode: ligature right half, FE21) ligature, right half
        assertEqualsCharByChar("     A\uFE21B\uFE21C\uFE21D\uFE21E\uFE21F\uFE21G\uFE21H\uFE21I\uFE21J\uFE21K\uFE21L\uFE21M\uFE21",
                note.getLines().get(79));
        assertEqualsCharByChar("     N\uFE21O\uFE21P\uFE21Q\uFE21R\uFE21S\uFE21T\uFE21U\uFE21V\uFE21W\uFE21X\uFE21Y\uFE21Z\uFE21",
                note.getLines().get(80));
        assertEqualsCharByChar("     a\uFE21b\uFE21c\uFE21d\uFE21e\uFE21f\uFE21g\uFE21h\uFE21i\uFE21j\uFE21k\uFE21l\uFE21m\uFE21",
                note.getLines().get(81));
        assertEqualsCharByChar("     n\uFE21o\uFE21p\uFE21q\uFE21r\uFE21s\uFE21t\uFE21u\uFE21v\uFE21w\uFE21x\uFE21y\uFE21z\uFE21",
                note.getLines().get(82));

    }

    /**
     * Check diacritics for ENSEL ED.
     */
    @Test
    public void testDiacriticsED() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // ED (Unicode: comma above right, 0315) high comma, off center
        assertEqualsCharByChar("     A\u0315B\u0315C\u0315D\u0315E\u0315F\u0315G\u0315H\u0315I\u0315J\u0315K\u0315L\u0315M\u0315",
                note.getLines().get(85));
        assertEqualsCharByChar("     N\u0315O\u0315P\u0315Q\u0315R\u0315S\u0315T\u0315U\u0315V\u0315W\u0315X\u0315Y\u0315Z\u0315",
                note.getLines().get(86));
        assertEqualsCharByChar("     a\u0315b\u0315c\u0315d\u0315e\u0315f\u0315g\u0315h\u0315i\u0315j\u0315k\u0315l\u0315m\u0315",
                note.getLines().get(87));
        assertEqualsCharByChar("     n\u0315o\u0315p\u0315q\u0315r\u0315s\u0315t\u0315u\u0315v\u0315w\u0315x\u0315y\u0315z\u0315",
                note.getLines().get(88));

    }

    /**
     * Check diacritics for ANSEL EE.
     */
    @Test
    public void testDiacriticsEE() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // EE (Unicode: double acute, 030B) double acute accent
        assertEqualsCharByChar("     A\u030BB\u030BC\u030BD\u030BE\u030BF\u030BG\u030BH\u030BI\u030BJ\u030BK\u030BL\u030BM\u030B",
                note.getLines().get(91));
        assertEqualsCharByChar("     N\u030B\u0150P\u030BQ\u030BR\u030BS\u030BT\u030B\u0170V\u030BW\u030BX\u030BY\u030BZ\u030B",
                note.getLines().get(92));
        assertEqualsCharByChar("     a\u030Bb\u030Bc\u030Bd\u030Be\u030Bf\u030Bg\u030Bh\u030Bi\u030Bj\u030Bk\u030Bl\u030Bm\u030B",
                note.getLines().get(93));
        assertEqualsCharByChar("     n\u030B\u0151p\u030Bq\u030Br\u030Bs\u030Bt\u030B\u0171v\u030Bw\u030Bx\u030By\u030Bz\u030B",
                note.getLines().get(94));

    }

    /**
     * Check diacritics for ANSEL EF.
     */
    @Test
    public void testDiacriticsEF() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // EF (Unicode: candrabindu, 0310) candrabindu
        assertEqualsCharByChar("     A\u0310B\u0310C\u0310D\u0310E\u0310F\u0310G\u0310H\u0310I\u0310J\u0310K\u0310L\u0310M\u0310",
                note.getLines().get(97));
        assertEqualsCharByChar("     N\u0310O\u0310P\u0310Q\u0310R\u0310S\u0310T\u0310U\u0310V\u0310W\u0310X\u0310Y\u0310Z\u0310",
                note.getLines().get(98));
        assertEqualsCharByChar("     a\u0310b\u0310c\u0310d\u0310e\u0310f\u0310g\u0310h\u0310i\u0310j\u0310k\u0310l\u0310m\u0310",
                note.getLines().get(99));
        assertEqualsCharByChar("     n\u0310o\u0310p\u0310q\u0310r\u0310s\u0310t\u0310u\u0310v\u0310w\u0310x\u0310y\u0310z\u0310",
                note.getLines().get(100));

    }

    /**
     * Check diacritics for ANSEL F0.
     */
    @Test
    public void testDiacriticsF0() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // F0 (Unicode: cedilla, 0327) cedilla
        assertEqualsCharByChar("     A\u0327B\u0327\u00C7\u1E10\u0228F\u0327\u0122\u1E28I\u0327J\u0327\u0136\u013BM\u0327", note
                .getLines().get(103));
        assertEqualsCharByChar("     \u0145O\u0327P\u0327Q\u0327\u0156\u015E\u0162U\u0327V\u0327W\u0327X\u0327Y\u0327Z\u0327", note
                .getLines().get(104));
        assertEqualsCharByChar("     a\u0327b\u0327\u00E7\u1E11\u0229f\u0327\u0123\u1E29i\u0327j\u0327\u0137\u013Cm\u0327", note
                .getLines().get(105));
        assertEqualsCharByChar("     \u0146o\u0327p\u0327q\u0327\u0157\u015F\u0163u\u0327v\u0327w\u0327x\u0327y\u0327z\u0327", note
                .getLines().get(106));

    }

    /**
     * Check diacritics for ANSEL F1.
     */
    @Test
    public void testDiacriticsF1() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // F1 (Unicode: ogonek, 0328) right hook
        assertEqualsCharByChar("     \u0104B\u0328C\u0328D\u0328\u0118F\u0328G\u0328H\u0328\u012EJ\u0328K\u0328L\u0328M\u0328", note
                .getLines().get(109));
        assertEqualsCharByChar("     N\u0328\u01EAP\u0328Q\u0328R\u0328S\u0328T\u0328\u0172V\u0328W\u0328X\u0328Y\u0328Z\u0328",
                note.getLines().get(110));
        assertEqualsCharByChar("     \u0105b\u0328c\u0328d\u0328\u0119f\u0328g\u0328h\u0328\u012Fj\u0328k\u0328l\u0328m\u0328", note
                .getLines().get(111));
        assertEqualsCharByChar("     n\u0328\u01EBp\u0328q\u0328r\u0328s\u0328t\u0328\u0173v\u0328w\u0328x\u0328y\u0328z\u0328",
                note.getLines().get(112));

    }

    /**
     * Check diacritics for ANSEL F2.
     */
    @Test
    public void testDiacriticsF2() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // F2 (Unicode: dot below, 0323) dot below
        assertEqualsCharByChar("     \u1EA0\u1E04C\u0323\u1E0C\u1EB8F\u0323G\u0323\u1E24\u1ECAJ\u0323\u1E32\u1E36\u1E42", note
                .getLines().get(115));
        assertEqualsCharByChar("     \u1E46\u1ECCP\u0323Q\u0323\u1E5A\u1E62\u1E6C\u1EE4\u1E7E\u1E88X\u0323\u1EF4\u1E92", note
                .getLines().get(116));
        assertEqualsCharByChar("     \u1EA1\u1E05c\u0323\u1E0D\u1EB9f\u0323g\u0323\u1E25\u1ECBj\u0323\u1E33\u1E37\u1E43", note
                .getLines().get(117));
        assertEqualsCharByChar("     \u1E47\u1ECDp\u0323q\u0323\u1E5B\u1E63\u1E6D\u1EE5\u1E7F\u1E89x\u0323\u1EF5\u1E93", note
                .getLines().get(118));

    }

    /**
     * Check diacritics for ANSEL F3.
     */
    @Test
    public void testDiacriticsF3() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // F3 (Unicode: diaeresis below, 0324) double dot below
        assertEqualsCharByChar("     A\u0324B\u0324C\u0324D\u0324E\u0324F\u0324G\u0324H\u0324I\u0324J\u0324K\u0324L\u0324M\u0324",
                note.getLines().get(121));
        assertEqualsCharByChar("     N\u0324O\u0324P\u0324Q\u0324R\u0324S\u0324T\u0324\u1E72V\u0324W\u0324X\u0324Y\u0324Z\u0324",
                note.getLines().get(122));
        assertEqualsCharByChar("     a\u0324b\u0324c\u0324d\u0324e\u0324f\u0324g\u0324h\u0324i\u0324j\u0324k\u0324l\u0324m\u0324",
                note.getLines().get(123));
        assertEqualsCharByChar("     n\u0324o\u0324p\u0324q\u0324r\u0324s\u0324t\u0324\u1E73v\u0324w\u0324x\u0324y\u0324z\u0324",
                note.getLines().get(124));

    }

    /**
     * Check diacritics for ANSEL F4.
     */
    @Test
    public void testDiacriticsF4() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // F4 (Unicode: ring below, 0325) circle below
        assertEqualsCharByChar("     \u1E00B\u0325C\u0325D\u0325E\u0325F\u0325G\u0325H\u0325I\u0325J\u0325K\u0325L\u0325M\u0325",
                note.getLines().get(127));
        assertEqualsCharByChar("     N\u0325O\u0325P\u0325Q\u0325R\u0325S\u0325T\u0325U\u0325V\u0325W\u0325X\u0325Y\u0325Z\u0325",
                note.getLines().get(128));
        assertEqualsCharByChar("     \u1E01b\u0325c\u0325d\u0325e\u0325f\u0325g\u0325h\u0325i\u0325j\u0325k\u0325l\u0325m\u0325",
                note.getLines().get(129));
        assertEqualsCharByChar("     n\u0325o\u0325p\u0325q\u0325r\u0325s\u0325t\u0325u\u0325v\u0325w\u0325x\u0325y\u0325z\u0325",
                note.getLines().get(130));

    }

    /**
     * Check diacritics for ANSEL F5.
     */
    @Test
    public void testDiacriticsF5() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // F5 (Unicode: double low line, 0333) double underscore
        assertEqualsCharByChar("     A\u0333B\u0333C\u0333D\u0333E\u0333F\u0333G\u0333H\u0333I\u0333J\u0333K\u0333L\u0333M\u0333",
                note.getLines().get(133));
        assertEqualsCharByChar("     N\u0333O\u0333P\u0333Q\u0333R\u0333S\u0333T\u0333U\u0333V\u0333W\u0333X\u0333Y\u0333Z\u0333",
                note.getLines().get(134));
        assertEqualsCharByChar("     a\u0333b\u0333c\u0333d\u0333e\u0333f\u0333g\u0333h\u0333i\u0333j\u0333k\u0333l\u0333m\u0333",
                note.getLines().get(135));
        assertEqualsCharByChar("     n\u0333o\u0333p\u0333q\u0333r\u0333s\u0333t\u0333u\u0333v\u0333w\u0333x\u0333y\u0333z\u0333",
                note.getLines().get(136));

    }

    /**
     * Check diacritics for ANSEL F6.
     */
    @Test
    public void testDiacriticsF6() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // F6 (Unicode: line below, 0332) underscore
        assertEqualsCharByChar("     A\u0332B\u0332C\u0332D\u0332E\u0332F\u0332G\u0332H\u0332I\u0332J\u0332K\u0332L\u0332M\u0332",
                note.getLines().get(139));
        assertEqualsCharByChar("     N\u0332O\u0332P\u0332Q\u0332R\u0332S\u0332T\u0332U\u0332V\u0332W\u0332X\u0332Y\u0332Z\u0332",
                note.getLines().get(140));
        assertEqualsCharByChar("     a\u0332b\u0332c\u0332d\u0332e\u0332f\u0332g\u0332h\u0332i\u0332j\u0332k\u0332l\u0332m\u0332",
                note.getLines().get(141));
        assertEqualsCharByChar("     n\u0332o\u0332p\u0332q\u0332r\u0332s\u0332t\u0332u\u0332v\u0332w\u0332x\u0332y\u0332z\u0332",
                note.getLines().get(142));

    }

    /**
     * Check diacritics for ANSEL F7.
     */
    @Test
    public void testDiacriticsF7() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // F7 (Unicode: comma below, 0326) left hook
        assertEqualsCharByChar("     A\u0326B\u0326C\u0326D\u0326E\u0326F\u0326G\u0326H\u0326I\u0326J\u0326K\u0326L\u0326M\u0326",
                note.getLines().get(145));
        assertEqualsCharByChar("     N\u0326O\u0326P\u0326Q\u0326R\u0326\u0218\u021AU\u0326V\u0326W\u0326X\u0326Y\u0326Z\u0326",
                note.getLines().get(146));
        assertEqualsCharByChar("     a\u0326b\u0326c\u0326d\u0326e\u0326f\u0326g\u0326h\u0326i\u0326j\u0326k\u0326l\u0326m\u0326",
                note.getLines().get(147));
        assertEqualsCharByChar("     n\u0326o\u0326p\u0326q\u0326r\u0326\u0219\u021Bu\u0326v\u0326w\u0326x\u0326y\u0326z\u0326",
                note.getLines().get(148));

    }

    /**
     * Check diacritics for ANSEL F8.
     */
    @Test
    public void testDiacriticsF8() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // F8 (Unicode: left half ring below, 031C) right cedilla
        assertEqualsCharByChar("     A\u031CB\u031CC\u031CD\u031CE\u031CF\u031CG\u031CH\u031CI\u031CJ\u031CK\u031CL\u031CM\u031C",
                note.getLines().get(151));
        assertEqualsCharByChar("     N\u031CO\u031CP\u031CQ\u031CR\u031CS\u031CT\u031CU\u031CV\u031CW\u031CX\u031CY\u031CZ\u031C",
                note.getLines().get(152));
        assertEqualsCharByChar("     a\u031Cb\u031Cc\u031Cd\u031Ce\u031Cf\u031Cg\u031Ch\u031Ci\u031Cj\u031Ck\u031Cl\u031Cm\u031C",
                note.getLines().get(153));
        assertEqualsCharByChar("     n\u031Co\u031Cp\u031Cq\u031Cr\u031Cs\u031Ct\u031Cu\u031Cv\u031Cw\u031Cx\u031Cy\u031Cz\u031C",
                note.getLines().get(154));

    }

    /**
     * Check diacritics for ANSEL F9.
     */
    @Test
    public void testDiacriticsF9() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // F9 (Unicode: breve below, 032E) half circle below
        assertEqualsCharByChar("     A\u032EB\u032EC\u032ED\u032EE\u032EF\u032EG\u032E\u1E2AI\u032EJ\u032EK\u032EL\u032EM\u032E",
                note.getLines().get(157));
        assertEqualsCharByChar("     N\u032EO\u032EP\u032EQ\u032ER\u032ES\u032ET\u032EU\u032EV\u032EW\u032EX\u032EY\u032EZ\u032E",
                note.getLines().get(158));
        assertEqualsCharByChar("     a\u032Eb\u032Ec\u032Ed\u032Ee\u032Ef\u032Eg\u032E\u1E2Bi\u032Ej\u032Ek\u032El\u032Em\u032E",
                note.getLines().get(159));
        assertEqualsCharByChar("     n\u032Eo\u032Ep\u032Eq\u032Er\u032Es\u032Et\u032Eu\u032Ev\u032Ew\u032Ex\u032Ey\u032Ez\u032E",
                note.getLines().get(160));

    }

    /**
     * Check diacritics for ANSEL FA.
     */
    @Test
    public void testDiacriticsFA() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // FA (Unicode: double tilde left half, FE22) double tilde, left half
        assertEqualsCharByChar("     A\uFE22B\uFE22C\uFE22D\uFE22E\uFE22F\uFE22G\uFE22H\uFE22I\uFE22J\uFE22K\uFE22L\uFE22M\uFE22",
                note.getLines().get(163));
        assertEqualsCharByChar("     N\uFE22O\uFE22P\uFE22Q\uFE22R\uFE22S\uFE22T\uFE22U\uFE22V\uFE22W\uFE22X\uFE22Y\uFE22Z\uFE22",
                note.getLines().get(164));
        assertEqualsCharByChar("     a\uFE22b\uFE22c\uFE22d\uFE22e\uFE22f\uFE22g\uFE22h\uFE22i\uFE22j\uFE22k\uFE22l\uFE22m\uFE22",
                note.getLines().get(165));
        assertEqualsCharByChar("     n\uFE22o\uFE22p\uFE22q\uFE22r\uFE22s\uFE22t\uFE22u\uFE22v\uFE22w\uFE22x\uFE22y\uFE22z\uFE22",
                note.getLines().get(166));

    }

    /**
     * Check diacritics for ANSEL FB.
     */
    @Test
    public void testDiacriticsFB() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // FB (Unicode: double tilde right half, FE23) double tilde, right half
        assertEqualsCharByChar("     A\uFE23B\uFE23C\uFE23D\uFE23E\uFE23F\uFE23G\uFE23H\uFE23I\uFE23J\uFE23K\uFE23L\uFE23M\uFE23",
                note.getLines().get(169));
        assertEqualsCharByChar("     N\uFE23O\uFE23P\uFE23Q\uFE23R\uFE23S\uFE23T\uFE23U\uFE23V\uFE23W\uFE23X\uFE23Y\uFE23Z\uFE23",
                note.getLines().get(170));
        assertEqualsCharByChar("     a\uFE23b\uFE23c\uFE23d\uFE23e\uFE23f\uFE23g\uFE23h\uFE23i\uFE23j\uFE23k\uFE23l\uFE23m\uFE23",
                note.getLines().get(171));
        assertEqualsCharByChar("     n\uFE23o\uFE23p\uFE23q\uFE23r\uFE23s\uFE23t\uFE23u\uFE23v\uFE23w\uFE23x\uFE23y\uFE23z\uFE23",
                note.getLines().get(172));

    }

    /**
     * Check diacritics for ANSEL FE.
     */
    @Test
    public void testDiacriticsFE() {
        NoteRecord note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // FE (Unicode: comma above, 0313) high comma, centered
        assertEqualsCharByChar("     A\u0313B\u0313C\u0313D\u0313E\u0313F\u0313G\u0313H\u0313I\u0313J\u0313K\u0313L\u0313M\u0313",
                note.getLines().get(175));
        assertEqualsCharByChar("     N\u0313O\u0313P\u0313Q\u0313R\u0313S\u0313T\u0313U\u0313V\u0313W\u0313X\u0313Y\u0313Z\u0313",
                note.getLines().get(176));
        assertEqualsCharByChar("     a\u0313b\u0313c\u0313d\u0313e\u0313f\u0313g\u0313h\u0313i\u0313j\u0313k\u0313l\u0313m\u0313",
                note.getLines().get(177));
        assertEqualsCharByChar("     n\u0313o\u0313p\u0313q\u0313r\u0313s\u0313t\u0313u\u0313v\u0313w\u0313x\u0313y\u0313z\u0313",
                note.getLines().get(178));
    }

    /**
     * Check copyright
     */
    @Test
    public void textCopyright() {
        assertEquals("\u00A9 1997 by H. Eichmann, parts \u00A9 1999-2000 by J. A. Nairn.", g.getHeader().getCopyrightData().get(0));
    }

    /**
     * Check extended characters
     */
    @Test
    public void textExtended() {
        NoteRecord note = g.getNotes().get("@N25@"); // This one has lots of extended characters
        assertEquals(43, note.getLines().size());
        assertEqualsCharByChar("A1 slash l - uppercase (\u0141)", note.getLines().get(4));
        assertEqualsCharByChar("A2 slash o - uppercase (\u00D8)", note.getLines().get(5));
        assertEqualsCharByChar("A3 slash d - uppercase (\u0110)", note.getLines().get(6));
        assertEqualsCharByChar("A4 thorn - uppercase (\u00DE)", note.getLines().get(7));
        assertEqualsCharByChar("A5 ligature ae - uppercase (\u00C6)", note.getLines().get(8));
        assertEqualsCharByChar("A6 ligature oe - uppercase (\u0152)", note.getLines().get(9));
        assertEqualsCharByChar("A7 single prime (\u02B9)", note.getLines().get(10));
        assertEqualsCharByChar("A8 middle dot (\u00B7)", note.getLines().get(11));
        assertEqualsCharByChar("A9 musical flat (\u266D)", note.getLines().get(12));
        assertEqualsCharByChar("AA registered sign (\u00AE)", note.getLines().get(13));
        assertEqualsCharByChar("AB plus-or-minus (\u00B1)", note.getLines().get(14));
        assertEqualsCharByChar("AC hook o - uppercase (\u01A0)", note.getLines().get(15));
        assertEqualsCharByChar("AD hook u - uppercase (\u01AF)", note.getLines().get(16));
        assertEqualsCharByChar("AE left half ring (\u02BC)", note.getLines().get(17));
        assertEqualsCharByChar("BO right half ring (\u02BB)", note.getLines().get(18));
        assertEqualsCharByChar("B1 slash l - lowercase (\u0142)", note.getLines().get(19));
        assertEqualsCharByChar("B2 slash o - lowercase (\u00F8)", note.getLines().get(20));
        assertEqualsCharByChar("B3 slash d - lowercase (\u0111)", note.getLines().get(21));
        assertEqualsCharByChar("B4 thorn - lowercase (\u00FE)", note.getLines().get(22));
        assertEqualsCharByChar("B5 ligature ae - lowercase (\u00E6)", note.getLines().get(23));
        assertEqualsCharByChar("B6 ligature oe - lowercase (\u0153)", note.getLines().get(24));
        assertEqualsCharByChar("B7 double prime (\u02BA)", note.getLines().get(25));
        assertEqualsCharByChar("B8 dotless i - lowercase (\u0131)", note.getLines().get(26));
        assertEqualsCharByChar("B9 british pound (\u00A3)", note.getLines().get(27));
        assertEqualsCharByChar("BA eth (\u00F0)", note.getLines().get(28));
        assertEqualsCharByChar("BC hook o - lowercase (\u01A1)", note.getLines().get(29));
        assertEqualsCharByChar("BD hook u - lowercase (\u01B0)", note.getLines().get(30));
        assertEqualsCharByChar("BE empty box - LDS extension (\u25A1)", note.getLines().get(31));
        assertEqualsCharByChar("BF black box - LDS extensions (\u25A0)", note.getLines().get(32));
        assertEqualsCharByChar("CO degree sign (\u00B0)", note.getLines().get(33));
        assertEqualsCharByChar("C1 script l (\u2113)", note.getLines().get(34));
        assertEqualsCharByChar("C2 phonograph copyright mark (\u2117)", note.getLines().get(35));
        assertEqualsCharByChar("C3 copyright symbol (\u00A9)", note.getLines().get(36));
        assertEqualsCharByChar("C4 musical sharp (\u266F)", note.getLines().get(37));
        assertEqualsCharByChar("C5 inverted question mark (\u00BF)", note.getLines().get(38));
        assertEqualsCharByChar("C6 inverted exclamation mark (\u00A1)", note.getLines().get(39));
        assertEqualsCharByChar("CD midline e - LDS extension (e)", note.getLines().get(40));
        assertEqualsCharByChar("CE midline o - LDS extension (o)", note.getLines().get(41));
        assertEqualsCharByChar("CF es zet (\u00DF)", note.getLines().get(42));

    }

    /**
     * Helper method to assert string equality, character by character, and points out the differences
     * 
     * @param expected
     *            expected value
     * @param actual
     *            actual value
     */
    private void assertEqualsCharByChar(String expected, String actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        for (int i = 0; i < Math.min(expected.length(), actual.length()); i++) {
            assertEquals(String.format(
                    "Character %d of two strings are not the same\nExpect String: %s\nActual String: %s\nExpect Char: %s (%04X)\nActual Char: %s (%04X)\n",
                    i, expected, actual, expected.charAt(i), (int) expected.charAt(i), actual.charAt(i), (int) actual.charAt(i)),
                    expected.charAt(i), actual.charAt(i));
        }
    }
}
