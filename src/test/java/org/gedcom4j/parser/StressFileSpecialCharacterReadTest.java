/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
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
package org.gedcom4j.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Note;
import org.junit.Before;
import org.junit.Test;

/**
 * A test to load the "stress test" file, (sample/TGC551.ged) and check that some of the weirder elements parsed as
 * expected.
 * 
 * @author frizbog
 */
public class StressFileSpecialCharacterReadTest {

    /**
     * The GEDCOM file loaded from the stress test
     */
    private Gedcom g;

    /**
     * Load the stress test file
     * 
     * @throws IOException
     *             if the file cannot be read
     * @throws GedcomParserException
     *             if the file cannot be parsed
     */
    @Before
    public void setUp() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/TGC551.ged");
        assertTrue(gp.errors.isEmpty());
        g = gp.gedcom;
    }

    /**
     * Check the character set used
     */
    @Test
    public void testCharacterSet() {
        assertEquals("ANSEL", g.getHeader().getCharacterSet().getCharacterSetName().getValue());
    }

    /**
     * Check diacritics. In the source code, diacritics are shown as unicode characters embedded in the string when
     * using combining diacritics. When using pre-composed (single) glyphs, those are shown already composed.
     */
    @Test
    public void testDiacritics() {
        Note note = g.getNotes().get("@N24@");
        assertEquals(179, note.getLines().size());

        // E0 (Unicode: hook above, 0309) low rising tone mark
        assertEqualsCharByChar("     ẢB\u0309C\u0309D\u0309ẺF\u0309G\u0309H\u0309ỈJ\u0309K\u0309L\u0309M\u0309", note.getLines().get(7));
        assertEqualsCharByChar("     N\u0309ỎP\u0309Q\u0309R\u0309S\u0309T\u0309ỦV\u0309W\u0309X\u0309ỶZ\u0309", note.getLines().get(8));
        assertEqualsCharByChar("     ảb\u0309c\u0309d\u0309ẻf\u0309g\u0309h\u0309ỉj\u0309k\u0309l\u0309m\u0309", note.getLines().get(9));
        assertEqualsCharByChar("     n\u0309ỏp\u0309q\u0309r\u0309s\u0309t\u0309ủv\u0309w\u0309x\u0309ỷz\u0309", note.getLines().get(10));

        // E1 (Unicode: grave, 0300) grave accent
        assertEqualsCharByChar("     ÀB\u0300C\u0300D\u0300ÈF\u0300G\u0300H\u0300ÌJ\u0300K\u0300L̀M\u0300", note.getLines().get(13));
        assertEqualsCharByChar("     ǸÒP\u0300Q\u0300R\u0300S\u0300T\u0300ÙV\u0300ẀX\u0300ỲZ\u0300", note.getLines().get(14));
        assertEqualsCharByChar("     àb\u0300c\u0300d\u0300èf\u0300g\u0300h\u0300ìj\u0300k\u0300l\u0300m\u0300", note.getLines().get(15));
        assertEqualsCharByChar("     ǹòp\u0300q\u0300r\u0300s\u0300t\u0300ùv\u0300ẁx\u0300ỳz\u0300", note.getLines().get(16));

        // E2 (Unicode: acute, 0301) acute accent
        assertEqualsCharByChar("     ÁB\u0301ĆD\u0301ÉF\u0301ǴH\u0301ÍJ\u0301ḰĹḾ", note.getLines().get(19));
        assertEqualsCharByChar("     ŃÓṔQ\u0301ŔŚT\u0301ÚV\u0301ẂX\u0301ÝŹ", note.getLines().get(20));
        assertEqualsCharByChar("     áb\u0301ćd\u0301éf\u0301ǵh\u0301íj\u0301ḱĺḿ", note.getLines().get(21));
        assertEqualsCharByChar("     ńóṕq\u0301ŕśt\u0301úv\u0301ẃx\u0301ýź", note.getLines().get(22));

        // E3 (Unicode: circumflex, 0302) circumflex accent
        assertEqualsCharByChar("     ÂB\u0302ĈD\u0302ÊF\u0302ĜĤÎĴK\u0302L\u0302M\u0302", note.getLines().get(25));
        assertEqualsCharByChar("     N\u0302ÔP\u0302Q\u0302R\u0302ŜT\u0302ÛV\u0302ŴX\u0302ŶẐ", note.getLines().get(26));
        assertEqualsCharByChar("     âb\u0302ĉd\u0302êf\u0302ĝĥîĵk\u0302l\u0302m\u0302", note.getLines().get(27));
        assertEqualsCharByChar("     n\u0302ôp\u0302q\u0302r\u0302ŝt\u0302ûv\u0302ŵx\u0302ŷẑ", note.getLines().get(28));

        // E4 (Unicode: tilde, 0303) tilde
        assertEqualsCharByChar("     ÃB\u0303C\u0303D\u0303ẼF\u0303G\u0303H\u0303ĨJ\u0303K\u0303L\u0303M\u0303", note.getLines().get(31));
        assertEqualsCharByChar("     ÑÕP\u0303Q\u0303R\u0303S\u0303T\u0303ŨṼW\u0303X\u0303ỸZ\u0303", note.getLines().get(32));
        assertEqualsCharByChar("     Ñb\u0303c\u0303d\u0303ẽf\u0303g\u0303h\u0303ĩj\u0303k\u0303l\u0303m\u0303", note.getLines().get(33));
        assertEqualsCharByChar("     ñõp\u0303q\u0303r\u0303s\u0303t\u0303ũṽw\u0303x\u0303ỹz\u0303", note.getLines().get(34));

        // E5 (Unicode: macron, 0304) macron
        assertEqualsCharByChar("     ĀB\u0304C\u0304D\u0304ĒF\u0304ḠH\u0304ĪJ\u0304K\u0304L\u0304M\u0304", note.getLines().get(37));
        assertEqualsCharByChar("     N\u0304ŌP\u0304Q\u0304R\u0304S\u0304T\u0304ŪV\u0304W\u0304X\u0304ȲZ\u0304", note.getLines().get(38));
        assertEqualsCharByChar("     āb\u0304c\u0304d\u0304ēf\u0304ḡh\u0304īj\u0304k\u0304l\u0304m\u0304", note.getLines().get(39));
        assertEqualsCharByChar("     n\u0304ōp\u0304q\u0304r\u0304s\u0304t\u0304ūv\u0304w\u0304x\u0304ȳz\u0304", note.getLines().get(40));

        // E6 (Unicode: breve, 0306) breve
        assertEqualsCharByChar("     ĂB\u0306C\u0306D\u0306ĔF\u0306ĞH\u0306ĬJ\u0306K\u0306L\u0306M\u0306", note.getLines().get(43));
        assertEqualsCharByChar("     N\u0306ŎP\u0306Q\u0306R\u0306S\u0306T\u0306ŬV\u0306W\u0306X\u0306Y\u0306Z\u0306", note.getLines().get(44));
        assertEqualsCharByChar("     ăb\u0306c\u0306d\u0306ĕf\u0306ğh\u0306ĭj\u0306k\u0306l\u0306m\u0306", note.getLines().get(45));
        assertEqualsCharByChar("     n\u0306ŏp\u0306q\u0306r\u0306s\u0306t\u0306ŭv\u0306w\u0306x\u0306y\u0306z\u0306", note.getLines().get(46));

        // E7 (Unicode: dot above, 0307) dot above
        assertEqualsCharByChar("     ȦḂĊḊĖḞĠḢİJ\u0307K\u0307L\u0307Ṁ", note.getLines().get(49));
        assertEqualsCharByChar("     ṄȮṖQ\u0307ṘṠṪU\u0307V\u0307ẆẊẎŻ", note.getLines().get(50));
        assertEqualsCharByChar("     ȧḃċḋėḟġḣi\u0307j\u0307k\u0307l\u0307ṁ", note.getLines().get(51));
        assertEqualsCharByChar("     ṅȯṗq\u0307ṙṡṫu\u0307v\u0307ẇẋẏż", note.getLines().get(52));

        // E8 (Unicode: diaeresis, 0308) umlaut (dieresis)
        assertEqualsCharByChar("     ÄB\u0308C\u0308D\u0308ËF\u0308G\u0308ḦÏJ\u0308K\u0308L\u0308M\u0308", note.getLines().get(55));
        assertEqualsCharByChar("     N\u0308ÖP\u0308Q\u0308R\u0308S\u0308T\u0308ÜV\u0308ẄẌŸZ\u0308", note.getLines().get(56));
        assertEqualsCharByChar("     äb\u0308c\u0308d\u0308ëf\u0308g\u0308ḧïj\u0308k\u0308l\u0308m\u0308", note.getLines().get(57));
        assertEqualsCharByChar("     n\u0308öp\u0308q\u0308r\u0308s\u0308ẗüv\u0308ẅẍÿz\u0308", note.getLines().get(58));

        // E9 (Unicode: caron, 030C) hacek - some fonts render this like an upper right apostrophe instead of a normal
        // caron
        assertEqualsCharByChar("     ǍB\u030cČĎĚF\u030cǦȞǏJ\u030cǨĽM\u030c", note.getLines().get(61));
        assertEqualsCharByChar("     ŇǑP\u030cQ\u030cŘŠŤǓV\u030cW\u030cX\u030cY\u030cŽ", note.getLines().get(62));
        assertEqualsCharByChar("     ǎb\u030cčďěf\u030cǧȟǐǰǩľm\u030c", note.getLines().get(63));
        assertEqualsCharByChar("     ňǒp\u030cq\u030cřšťǔv\u030cw\u030cx\u030cy\u030cž", note.getLines().get(64));

        // EA (Unicode: ring above, 030A) circle above (angstrom)
        assertEqualsCharByChar("     ÅB\u030AC\u030AD\u030AE\u030AF\u030AG\u030AH\u030AI\u030AJ\u030AK\u030AL\u030AM\u030A", note.getLines().get(67));
        assertEqualsCharByChar("     N\u030AO\u030AP\u030AQ\u030AR\u030AS\u030AT\u030AŮV\u030AW\u030AX\u030AY\u030AZ\u030A", note.getLines().get(68));
        assertEqualsCharByChar("     åb\u030Ac\u030Ad\u030Ae\u030Af\u030Ag\u030Ah\u030Ai\u030Aj\u030Ak\u030Al\u030Am\u030A", note.getLines().get(69));
        assertEqualsCharByChar("     n\u030Ao\u030Ap\u030Aq\u030Ar\u030As\u030At\u030Aův\u030Aẘx\u030Aẙz\u030A", note.getLines().get(70));

        // EB (Unicode: ligature left half, FE20) ligature, left half
        assertEqualsCharByChar("     A\uFE20B\uFE20C\uFE20D\uFE20E\uFE20F\uFE20G\uFE20H\uFE20I\uFE20J\uFE20K\uFE20L\uFE20M\uFE20", note.getLines().get(73));
        assertEqualsCharByChar("     N\uFE20O\uFE20P\uFE20Q\uFE20R\uFE20S\uFE20T\uFE20U\uFE20V\uFE20W\uFE20X\uFE20Y\uFE20Z\uFE20", note.getLines().get(74));
        assertEqualsCharByChar("     a\uFE20b\uFE20c\uFE20d\uFE20e\uFE20f\uFE20g\uFE20h\uFE20i\uFE20j\uFE20k\uFE20l\uFE20m\uFE20", note.getLines().get(75));
        assertEqualsCharByChar("     n\uFE20o\uFE20p\uFE20q\uFE20r\uFE20s\uFE20t\uFE20u\uFE20v\uFE20w\uFE20x\uFE20y\uFE20z\uFE20", note.getLines().get(76));

        // EC (Unicode: ligature right half, FE21) ligature, right half
        assertEqualsCharByChar("     A\uFE21B\uFE21C\uFE21D\uFE21E\uFE21F\uFE21G\uFE21H\uFE21I\uFE21J\uFE21K\uFE21L\uFE21M\uFE21", note.getLines().get(79));
        assertEqualsCharByChar("     N\uFE21O\uFE21P\uFE21Q\uFE21R\uFE21S\uFE21T\uFE21U\uFE21V\uFE21W\uFE21X\uFE21Y\uFE21Z\uFE21", note.getLines().get(80));
        assertEqualsCharByChar("     a\uFE21b\uFE21c\uFE21d\uFE21e\uFE21f\uFE21g\uFE21h\uFE21i\uFE21j\uFE21k\uFE21l\uFE21m\uFE21", note.getLines().get(81));
        assertEqualsCharByChar("     n\uFE21o\uFE21p\uFE21q\uFE21r\uFE21s\uFE21t\uFE21u\uFE21v\uFE21w\uFE21x\uFE21y\uFE21z\uFE21", note.getLines().get(82));

        // ED (Unicode: comma above right, 0315) high comma, off center
        assertEqualsCharByChar("     A\u0315B\u0315C\u0315D\u0315E\u0315F\u0315G\u0315H\u0315I\u0315J\u0315K\u0315L\u0315M\u0315", note.getLines().get(85));
        assertEqualsCharByChar("     N\u0315O\u0315P\u0315Q\u0315R\u0315S\u0315T\u0315U\u0315V\u0315W\u0315X\u0315Y\u0315Z\u0315", note.getLines().get(86));
        assertEqualsCharByChar("     a\u0315b\u0315c\u0315d\u0315e\u0315f\u0315g\u0315h\u0315i\u0315j\u0315k\u0315l\u0315m\u0315", note.getLines().get(87));
        assertEqualsCharByChar("     n\u0315o\u0315p\u0315q\u0315r\u0315s\u0315t\u0315u\u0315v\u0315w\u0315x\u0315y\u0315z\u0315", note.getLines().get(88));

        // EE (Unicode: double acute, 030B) double acute accent
        assertEqualsCharByChar("     A\u030BB\u030BC\u030BD\u030BE\u030BF\u030BG\u030BH\u030BI\u030BJ\u030BK\u030BL\u030BM\u030B", note.getLines().get(91));
        assertEqualsCharByChar("     N\u030BŐP\u030BQ\u030BR\u030BS\u030BT\u030BŰV\u030BW\u030BX\u030BY\u030BZ\u030B", note.getLines().get(92));
        assertEqualsCharByChar("     a\u030Bb\u030Bc\u030Bd\u030Be\u030Bf\u030Bg\u030Bh\u030Bi\u030Bj\u030Bk\u030Bl\u030Bm\u030B", note.getLines().get(93));
        assertEqualsCharByChar("     n\u030Bőp\u030Bq\u030Br\u030Bs\u030Bt\u030Bűv\u030Bw\u030Bx\u030By\u030Bz\u030B", note.getLines().get(94));

        // EF (Unicode: candrabindu, 0310) candrabindu
        assertEqualsCharByChar("     A\u0310B\u0310C\u0310D\u0310E\u0310F\u0310G\u0310H\u0310I\u0310J\u0310K\u0310L\u0310M\u0310", note.getLines().get(97));
        assertEqualsCharByChar("     N\u0310O\u0310P\u0310Q\u0310R\u0310S\u0310T\u0310U\u0310V\u0310W\u0310X\u0310Y\u0310Z\u0310", note.getLines().get(98));
        assertEqualsCharByChar("     a\u0310b\u0310c\u0310d\u0310e\u0310f\u0310g\u0310h\u0310i\u0310j\u0310k\u0310l\u0310m\u0310", note.getLines().get(99));
        assertEqualsCharByChar("     n\u0310o\u0310p\u0310q\u0310r\u0310s\u0310t\u0310u\u0310v\u0310w\u0310x\u0310y\u0310z\u0310", note.getLines().get(100));

        // F0 (Unicode: cedilla, 0327) cedilla
        assertEqualsCharByChar("     A\u0327B\u0327ÇḐȨF\u0327ĢḨI\u0327J\u0327ĶĻM\u0327", note.getLines().get(103));
        assertEqualsCharByChar("     ŅO\u0327P\u0327Q\u0327ŖŞŢU\u0327V\u0327W\u0327X\u0327Y\u0327Z\u0327", note.getLines().get(104));
        assertEqualsCharByChar("     a\u0327b\u0327çḑȩf\u0327ģḩi\u0327j\u0327ķļm\u0327", note.getLines().get(105));
        assertEqualsCharByChar("     ņo\u0327p\u0327q\u0327ŗşţu\u0327v\u0327w\u0327x\u0327y\u0327z\u0327", note.getLines().get(106));

        // F1 (Unicode: ogonek, 0328) right hook
        assertEqualsCharByChar("     ĄB\u0328C\u0328D\u0328ĘF\u0328G\u0328H\u0328ĮJ\u0328K\u0328L\u0328M\u0328", note.getLines().get(109));
        assertEqualsCharByChar("     N\u0328ǪP\u0328Q\u0328R\u0328S\u0328T\u0328ŲV\u0328W\u0328X\u0328Y\u0328Z\u0328", note.getLines().get(110));
        assertEqualsCharByChar("     ąb\u0328c\u0328d\u0328ęf\u0328g\u0328h\u0328įj\u0328k\u0328l\u0328m\u0328", note.getLines().get(111));
        assertEqualsCharByChar("     n\u0328ǫp\u0328q\u0328r\u0328s\u0328t\u0328ųv\u0328w\u0328x\u0328y\u0328z\u0328", note.getLines().get(112));

        // F2 (Unicode: dot below, 0323) dot below
        assertEqualsCharByChar("     ẠḄC\u0323ḌẸF\u0323G\u0323ḤỊJ\u0323ḲḶṂ", note.getLines().get(115));
        assertEqualsCharByChar("     ṆỌP\u0323Q\u0323ṚṢṬỤṾẈX\u0323ỴẒ", note.getLines().get(116));
        assertEqualsCharByChar("     ạḅc\u0323ḍẹf\u0323g\u0323ḥịj\u0323ḳḷṃ", note.getLines().get(117));
        assertEqualsCharByChar("     ṇọp\u0323q\u0323ṛṣṭụṿẉx\u0323ỵẓ", note.getLines().get(118));

        // F3 (Unicode: diaeresis below, 0324) double dot below
        assertEqualsCharByChar("     A\u0324B\u0324C\u0324D\u0324E\u0324F\u0324G\u0324H\u0324I\u0324J\u0324K\u0324L\u0324M\u0324", note.getLines().get(121));
        assertEqualsCharByChar("     N\u0324O\u0324P\u0324Q\u0324R\u0324S\u0324T\u0324ṲV\u0324W\u0324X\u0324Y\u0324Z\u0324", note.getLines().get(122));
        assertEqualsCharByChar("     a\u0324b\u0324c\u0324d\u0324e\u0324f\u0324g\u0324h\u0324i\u0324j\u0324k\u0324l\u0324m\u0324", note.getLines().get(123));
        assertEqualsCharByChar("     n\u0324o\u0324p\u0324q\u0324r\u0324s\u0324t\u0324ṳv\u0324w\u0324x\u0324y\u0324z\u0324", note.getLines().get(124));

        // F4 (Unicode: ring below, 0325) circle below
        assertEqualsCharByChar("     ḀB\u0325C\u0325D\u0325E\u0325F\u0325G\u0325H\u0325I\u0325J\u0325K\u0325L\u0325M\u0325", note.getLines().get(127));
        assertEqualsCharByChar("     N\u0325O\u0325P\u0325Q\u0325R\u0325S\u0325T\u0325U\u0325V\u0325W\u0325X\u0325Y\u0325Z\u0325", note.getLines().get(128));
        assertEqualsCharByChar("     ḁb\u0325c\u0325d\u0325e\u0325f\u0325g\u0325h\u0325i\u0325j\u0325k\u0325l\u0325m\u0325", note.getLines().get(129));
        assertEqualsCharByChar("     n\u0325o\u0325p\u0325q\u0325r\u0325s\u0325t\u0325u\u0325v\u0325w\u0325x\u0325y\u0325z\u0325", note.getLines().get(130));

        // F5 (Unicode: double low line, 0333) double underscore
        assertEqualsCharByChar("     A\u0333B\u0333C\u0333D\u0333E\u0333F\u0333G\u0333H\u0333I\u0333J\u0333K\u0333L\u0333M\u0333", note.getLines().get(133));
        assertEqualsCharByChar("     N\u0333O\u0333P\u0333Q\u0333R\u0333S\u0333T\u0333U\u0333V\u0333W\u0333X\u0333Y\u0333Z\u0333", note.getLines().get(134));
        assertEqualsCharByChar("     a\u0333b\u0333c\u0333d\u0333e\u0333f\u0333g\u0333h\u0333i\u0333j\u0333k\u0333l\u0333m\u0333", note.getLines().get(135));
        assertEqualsCharByChar("     n\u0333o\u0333p\u0333q\u0333r\u0333s\u0333t\u0333u\u0333v\u0333w\u0333x\u0333y\u0333z\u0333", note.getLines().get(136));

        // F6 (Unicode: line below, 0332) underscore
        assertEqualsCharByChar("     A\u0332B\u0332C\u0332D\u0332E\u0332F\u0332G\u0332H\u0332I\u0332J\u0332K\u0332L\u0332M\u0332", note.getLines().get(139));
        assertEqualsCharByChar("     N\u0332O\u0332P\u0332Q\u0332R\u0332S\u0332T\u0332U\u0332V\u0332W\u0332X\u0332Y\u0332Z\u0332", note.getLines().get(140));
        assertEqualsCharByChar("     a\u0332b\u0332c\u0332d\u0332e\u0332f\u0332g\u0332h\u0332i\u0332j\u0332k\u0332l\u0332m\u0332", note.getLines().get(141));
        assertEqualsCharByChar("     n\u0332o\u0332p\u0332q\u0332r\u0332s\u0332t\u0332u\u0332v\u0332w\u0332x\u0332y\u0332z\u0332", note.getLines().get(142));

        // F7 (Unicode: comma below, 0326) left hook
        assertEqualsCharByChar("     A\u0326B\u0326C\u0326D\u0326E\u0326F\u0326G\u0326H\u0326I\u0326J\u0326K\u0326L\u0326M\u0326", note.getLines().get(145));
        assertEqualsCharByChar("     N\u0326O\u0326P\u0326Q\u0326R\u0326ȘȚU\u0326V\u0326W\u0326X\u0326Y\u0326Z\u0326", note.getLines().get(146));
        assertEqualsCharByChar("     a\u0326b\u0326c\u0326d\u0326e\u0326f\u0326g\u0326h\u0326i\u0326j\u0326k\u0326l\u0326m\u0326", note.getLines().get(147));
        assertEqualsCharByChar("     n\u0326o\u0326p\u0326q\u0326r\u0326șțu\u0326v\u0326w\u0326x\u0326y\u0326z\u0326", note.getLines().get(148));

        // F8 (Unicode: left half ring below, 031C) right cedilla
        assertEqualsCharByChar("     A\u031CB\u031CC\u031CD\u031CE\u031CF\u031CG\u031CH\u031CI\u031CJ\u031CK\u031CL\u031CM\u031C", note.getLines().get(151));
        assertEqualsCharByChar("     N\u031CO\u031CP\u031CQ\u031CR\u031CS\u031CT\u031CU\u031CV\u031CW\u031CX\u031CY\u031CZ\u031C", note.getLines().get(152));
        assertEqualsCharByChar("     a\u031Cb\u031Cc\u031Cd\u031Ce\u031Cf\u031Cg\u031Ch\u031Ci\u031Cj\u031Ck\u031Cl\u031Cm\u031C", note.getLines().get(153));
        assertEqualsCharByChar("     n\u031Co\u031Cp\u031Cq\u031Cr\u031Cs\u031Ct\u031Cu\u031Cv\u031Cw\u031Cx\u031Cy\u031Cz\u031C", note.getLines().get(154));

        // F9 (Unicode: breve below, 032E) half circle below
        assertEqualsCharByChar("     A\u032EB\u032EC\u032ED\u032EE\u032EF\u032EG\u032EḪI\u032EJ\u032EK\u032EL\u032EM\u032E", note.getLines().get(157));
        assertEqualsCharByChar("     N\u032EO\u032EP\u032EQ\u032ER\u032ES\u032ET\u032EU\u032EV\u032EW\u032EX\u032EY\u032EZ\u032E", note.getLines().get(158));
        assertEqualsCharByChar("     a\u032Eb\u032Ec\u032Ed\u032Ee\u032Ef\u032Eg\u032Eḫi\u032Ej\u032Ek\u032El\u032Em\u032E", note.getLines().get(159));
        assertEqualsCharByChar("     n\u032Eo\u032Ep\u032Eq\u032Er\u032Es\u032Et\u032Eu\u032Ev\u032Ew\u032Ex\u032Ey\u032Ez\u032E", note.getLines().get(160));

        // FA (Unicode: double tilde left half, FE22) double tilde, left half
        assertEqualsCharByChar("     A\uFE22B\uFE22C\uFE22D\uFE22E\uFE22F\uFE22G\uFE22H\uFE22I\uFE22J\uFE22K\uFE22L\uFE22M\uFE22", note.getLines().get(163));
        assertEqualsCharByChar("     N\uFE22O\uFE22P\uFE22Q\uFE22R\uFE22S\uFE22T\uFE22U\uFE22V\uFE22W\uFE22X\uFE22Y\uFE22Z\uFE22", note.getLines().get(164));
        assertEqualsCharByChar("     a\uFE22b\uFE22c\uFE22d\uFE22e\uFE22f\uFE22g\uFE22h\uFE22i\uFE22j\uFE22k\uFE22l\uFE22m\uFE22", note.getLines().get(165));
        assertEqualsCharByChar("     n\uFE22o\uFE22p\uFE22q\uFE22r\uFE22s\uFE22t\uFE22u\uFE22v\uFE22w\uFE22x\uFE22y\uFE22z\uFE22", note.getLines().get(166));

        // FB (Unicode: double tilde right half, FE23) double tilde, right half
        assertEqualsCharByChar("     A\uFE23B\uFE23C\uFE23D\uFE23E\uFE23F\uFE23G\uFE23H\uFE23I\uFE23J\uFE23K\uFE23L\uFE23M\uFE23", note.getLines().get(169));
        assertEqualsCharByChar("     N\uFE23O\uFE23P\uFE23Q\uFE23R\uFE23S\uFE23T\uFE23U\uFE23V\uFE23W\uFE23X\uFE23Y\uFE23Z\uFE23", note.getLines().get(170));
        assertEqualsCharByChar("     a\uFE23b\uFE23c\uFE23d\uFE23e\uFE23f\uFE23g\uFE23h\uFE23i\uFE23j\uFE23k\uFE23l\uFE23m\uFE23", note.getLines().get(171));
        assertEqualsCharByChar("     n\uFE23o\uFE23p\uFE23q\uFE23r\uFE23s\uFE23t\uFE23u\uFE23v\uFE23w\uFE23x\uFE23y\uFE23z\uFE23", note.getLines().get(172));

        // FE (Unicode: comma above, 0313) high comma, centered
        assertEqualsCharByChar("     A\u0313B\u0313C\u0313D\u0313E\u0313F\u0313G\u0313H\u0313I\u0313J\u0313K\u0313L\u0313M\u0313", note.getLines().get(175));
        assertEqualsCharByChar("     N\u0313O\u0313P\u0313Q\u0313R\u0313S\u0313T\u0313U\u0313V\u0313W\u0313X\u0313Y\u0313Z\u0313", note.getLines().get(176));
        assertEqualsCharByChar("     a\u0313b\u0313c\u0313d\u0313e\u0313f\u0313g\u0313h\u0313i\u0313j\u0313k\u0313l\u0313m\u0313", note.getLines().get(177));
        assertEqualsCharByChar("     n\u0313o\u0313p\u0313q\u0313r\u0313s\u0313t\u0313u\u0313v\u0313w\u0313x\u0313y\u0313z\u0313", note.getLines().get(178));
    }

    /**
     * Check copyright
     */
    @Test
    public void textCopyright() {
        assertEquals("© 1997 by H. Eichmann, parts © 1999-2000 by J. A. Nairn.", g.getHeader().getCopyrightData().get(0));
    }

    /**
     * Check extended characters
     */
    @Test
    public void textExtended() {
        Note note = g.getNotes().get("@N25@"); // This one has lots of extended characters
        assertEquals(43, note.getLines().size());
        assertEqualsCharByChar("A1 slash l - uppercase (Ł)", note.getLines().get(4));
        assertEqualsCharByChar("A2 slash o - uppercase (Ø)", note.getLines().get(5));
        assertEqualsCharByChar("A3 slash d - uppercase (Đ)", note.getLines().get(6));
        assertEqualsCharByChar("A4 thorn - uppercase (Þ)", note.getLines().get(7));
        assertEqualsCharByChar("A5 ligature ae - uppercase (Æ)", note.getLines().get(8));
        assertEqualsCharByChar("A6 ligature oe - uppercase (Œ)", note.getLines().get(9));
        assertEqualsCharByChar("A7 single prime (ʹ)", note.getLines().get(10));
        assertEqualsCharByChar("A8 middle dot (·)", note.getLines().get(11));
        assertEqualsCharByChar("A9 musical flat (♭)", note.getLines().get(12));
        assertEqualsCharByChar("AA registered sign (®)", note.getLines().get(13));
        assertEqualsCharByChar("AB plus-or-minus (±)", note.getLines().get(14));
        assertEqualsCharByChar("AC hook o - uppercase (Ơ)", note.getLines().get(15));
        assertEqualsCharByChar("AD hook u - uppercase (Ư)", note.getLines().get(16));
        assertEqualsCharByChar("AE left half ring (ʼ)", note.getLines().get(17));
        assertEqualsCharByChar("BO right half ring (ʻ)", note.getLines().get(18));
        assertEqualsCharByChar("B1 slash l - lowercase (ł)", note.getLines().get(19));
        assertEqualsCharByChar("B2 slash o - lowercase (ø)", note.getLines().get(20));
        assertEqualsCharByChar("B3 slash d - lowercase (đ)", note.getLines().get(21));
        assertEqualsCharByChar("B4 thorn - lowercase (þ)", note.getLines().get(22));
        assertEqualsCharByChar("B5 ligature ae - lowercase (æ)", note.getLines().get(23));
        assertEqualsCharByChar("B6 ligature oe - lowercase (œ)", note.getLines().get(24));
        assertEqualsCharByChar("B7 double prime (ʺ)", note.getLines().get(25));
        assertEqualsCharByChar("B8 dotless i - lowercase (ı)", note.getLines().get(26));
        assertEqualsCharByChar("B9 british pound (£)", note.getLines().get(27));
        assertEqualsCharByChar("BA eth (ð)", note.getLines().get(28));
        assertEqualsCharByChar("BC hook o - lowercase (ơ)", note.getLines().get(29));
        assertEqualsCharByChar("BD hook u - lowercase (ư)", note.getLines().get(30));
        assertEqualsCharByChar("BE empty box - LDS extension (□)", note.getLines().get(31));
        assertEqualsCharByChar("BF black box - LDS extensions (■)", note.getLines().get(32));
        assertEqualsCharByChar("CO degree sign (°)", note.getLines().get(33));
        assertEqualsCharByChar("C1 script l (ℓ)", note.getLines().get(34));
        assertEqualsCharByChar("C2 phonograph copyright mark (℗)", note.getLines().get(35));
        assertEqualsCharByChar("C3 copyright symbol (©)", note.getLines().get(36));
        assertEqualsCharByChar("C4 musical sharp (♯)", note.getLines().get(37));
        assertEqualsCharByChar("C5 inverted question mark (¿)", note.getLines().get(38));
        assertEqualsCharByChar("C6 inverted exclamation mark (¡)", note.getLines().get(39));
        assertEqualsCharByChar("CD midline e - LDS extension (e)", note.getLines().get(40));
        assertEqualsCharByChar("CE midline o - LDS extension (o)", note.getLines().get(41));
        assertEqualsCharByChar("CF es zet (ß)", note.getLines().get(42));

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
                    "Character %d of two strings are not the same\nExpect String: %s\nActual String: %s\nExpect Char: %s (%04X)\nActual Char: %s (%04X)\n", i,
                    expected, actual, expected.charAt(i), (int) expected.charAt(i), actual.charAt(i), (int) actual.charAt(i)), expected.charAt(i), actual
                            .charAt(i));
        }
    }
}
