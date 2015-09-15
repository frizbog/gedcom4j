/*
 * Copyright (c) 2009-2015 Matthew R. Harrah
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
import static org.junit.Assert.assertTrue;

import java.io.IOException;

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
        assertEquals("ANSEL", g.header.characterSet.characterSetName.value);
    }

    /**
     * Check copyright
     */
    @Test
    public void textCopyright() {
        assertEquals("© 1997 by H. Eichmann, parts © 1999-2000 by J. A. Nairn.", g.header.copyrightData.get(0));
    }

    /**
     * Check diacritics
     */
    @Test
    public void textDiacritics() {
        Note note = g.notes.get("@N24@");
        assertEquals(179, note.lines.size());

        // E0 (Unicode: hook above, 0309) low rising tone mark
        assertEquals("     ẢB̉C̉D̉ẺF̉G̉H̉ỈJ̉K̉L̉M̉", note.lines.get(7));
        assertEquals("     N̉ỌP̉Q̉R̉S̉T̉ỦV̉W̉X̉ỲZ̉", note.lines.get(8));
        assertEquals("     ảb̉c̉d̉ẻf̉g̉h̉ỉj̉k̉l̉m̉", note.lines.get(9));
        assertEquals("     n̉ọp̉q̉r̉s̉t̉ủv̉w̉x̉ỳz̉", note.lines.get(10));

        // E1 (Unicode: grave, 0300) grave accent
        assertEquals("     ÀB̀C̀D̀ÈF̀G̀H̀ÌJ̀K̀L̀M̀", note.lines.get(13));
        assertEquals("     ǸÒP̀Q̀R̀S̀T̀ÙV̀ẀX̀ỲZ̀", note.lines.get(14));
        assertEquals("     àb̀c̀d̀èf̀g̀h̀ìj̀k̀l̀m̀", note.lines.get(15));
        assertEquals("     ǹòp̀q̀r̀s̀t̀ùv̀ẁx̀ỳz̀", note.lines.get(16));

        // E2 (Unicode: acute, 0301) acute accent
        assertEquals("     ÁB́ĆD́ÉF́ǴH́ÍJ́ḰĹḾ", note.lines.get(19));
        assertEquals("     ŃÓṔQ́ŔŚT́ÚV́ẂX́ÝŹ", note.lines.get(20));
        assertEquals("     áb́ćd́éf́ǵh́íj́ḱĺḿ", note.lines.get(21));
        assertEquals("     ńíṕq́ŕśt́úv́ẃx́ýź", note.lines.get(22));

        // E3 (Unicode: circumflex, 0302) circumflex accent
        assertEquals("     ÂB̂ĈD̂ÊF̂ĜĤÎĴK̂L̂M̂", note.lines.get(25));
        assertEquals("     N̂ÔP̂Q̂R̂ŜT̂ÛV̂ŴX̂ŶẐ", note.lines.get(26));
        assertEquals("     âb̂ĉd̂êf̂ĝĥîĵk̂l̂m̂", note.lines.get(27));
        assertEquals("     n̂ôp̂q̂r̂ŝt̂ûv̂ŵx̂ŷẑ", note.lines.get(28));

        // E4 (Unicode: tilde, 0303) tilde
        assertEquals("     ÃB̃C̃D̃ẼF̃G̃H̃ĨJ̃K̃L̃M̃", note.lines.get(31));
        assertEquals("     ÑÕP̃Q̃R̃S̃T̃ŨṼW̃X̃ỸZ̃", note.lines.get(32));
        assertEquals("     Ñb̃c̃d̃ẽf̃g̃h̃ĩj̃k̃l̃m̃", note.lines.get(33));
        assertEquals("     ñõp̃q̃r̃s̃t̃ũṽw̃x̃ỹz̃", note.lines.get(34));

        // E5 (Unicode: macron, 0304) macron
        assertEquals("     ĀB̄C̄D̄ĒF̄ḠH̄ĪJ̄K̄L̄M̄", note.lines.get(37));
        assertEquals("     N̄ŌP̄Q̄R̄S̄T̄ŪV̄W̄X̄ȲZ̄", note.lines.get(38));
        assertEquals("     āb̄c̄d̄ēf̄ḡh̄īj̄k̄l̄m̄", note.lines.get(39));
        assertEquals("     n̄ōp̄q̄r̄s̄t̄ūv̄w̄x̄ȳz̄", note.lines.get(40));

        // E6 (Unicode: breve, 0306) breve
        assertEquals("     ĂB̆C̆D̆ĔF̆ĞH̆ĬJ̆K̆L̆M̆", note.lines.get(43));
        assertEquals("     N̆ŎP̆Q̆R̆S̆T̆ŬV̆W̆X̆Y̆Z̆", note.lines.get(44));
        assertEquals("     ăb̆c̆d̆ĕf̆ğh̆ĭj̆k̆l̆m̆", note.lines.get(45));
        assertEquals("     n̆ŏp̆q̆r̆s̆t̆ŭv̆w̆x̆y̆z̆", note.lines.get(46));

        // TODO - Fix this - doesn't look right!!!!
        // E7 (Unicode: dot above, 0307) dot above
        assertEquals("     ǍḂĊĎĒḞĜĤĨĴĶĹṀ", note.lines.get(49));
        assertEquals("     ŃŌṖQ̇ŔŚŢŨV̇ŴẊŶŹ", note.lines.get(50));
        assertEquals("     ǎḃċďēḟĝĥĩĵķĺṁ", note.lines.get(51));
        assertEquals("     ńōṗq̇ŕśţũv̇ŵẋŷź", note.lines.get(52));

        // E8 (Unicode: diaeresis, 0308) umlaut (dieresis)
        assertEquals("     ÄB̈C̈D̈ËF̈G̈ḦÏJ̈K̈L̈M̈", note.lines.get(55));
        assertEquals("     N̈ÖP̈Q̈R̈S̈T̈ÜV̈ẄẌŸZ̈", note.lines.get(56));
        assertEquals("     äb̈c̈d̈ëf̈g̈ḧïj̈k̈l̈m̈", note.lines.get(57));
        assertEquals("     n̈öp̈q̈r̈s̈ẗüv̈ẅẍÿz̈", note.lines.get(58));

        // E9 (Unicode: caron, 030C) hacek
        assertEquals("     ǍB̌ČĎĚF̌ǦȞǏJ̌ǨĽM̌", note.lines.get(61));
        assertEquals("     ŇǑP̌Q̌ŘŠŤǓV̌W̌X̌Y̌Ž", note.lines.get(62));
        assertEquals("     ǎb̌čďěf̌ǧȟǐǰǩľm̌", note.lines.get(63));
        assertEquals("     ňǒp̌q̌řšťǔv̌w̌x̌y̌ž", note.lines.get(64));

        // EA (Unicode: ring above, 030A) circle above (angstrom)
        assertEquals("     ÅB̊C̊D̊E̊F̊G̊H̊I̊J̊K̊L̊M̊", note.lines.get(67));
        assertEquals("     N̊O̊P̊Q̊R̊S̊T̊ŮV̊W̊X̊Y̊Z̊", note.lines.get(68));
        assertEquals("     åb̊c̊d̊e̊f̊g̊h̊i̊j̊k̊l̊m̊", note.lines.get(69));
        assertEquals("     n̊o̊p̊q̊r̊s̊t̊ův̊ẘx̊ẙz̊", note.lines.get(70));

        // EB (Unicode: ligature left half, FE20) ligature, left half
        assertEquals("     A︠B︠C︠D︠E︠F︠G︠H︠I︠J︠K︠L︠M︠", note.lines.get(73));
        assertEquals("     N︠O︠P︠Q︠R︠S︠T︠U︠V︠W︠X︠Y︠Z︠", note.lines.get(74));
        assertEquals("     a︠b︠c︠d︠e︠f︠g︠h︠i︠j︠k︠l︠m︠", note.lines.get(75));
        assertEquals("     n︠o︠p︠q︠r︠s︠t︠u︠v︠w︠x︠y︠z︠", note.lines.get(76));

        // EC (Unicode: ligature right half, FE21) ligature, right half
        assertEquals("     A︡B︡C︡D︡E︡F︡G︡H︡I︡J︡K︡L︡M︡", note.lines.get(79));
        assertEquals("     N︡O︡P︡Q︡R︡S︡T︡U︡V︡W︡X︡Y︡Z︡", note.lines.get(80));
        assertEquals("     a︡b︡c︡d︡e︡f︡g︡h︡i︡j︡k︡l︡m︡", note.lines.get(81));
        assertEquals("     n︡o︡p︡q︡r︡s︡t︡u︡v︡w︡x︡y︡z︡", note.lines.get(82));

        // ED (Unicode: comma above right, 0315) high comma, off center
        assertEquals("     A̕B̕C̕D̕E̕F̕G̕H̕I̕J̕K̕L̕M̕", note.lines.get(85));
        assertEquals("     N̕O̕P̕Q̕R̕S̕T̕U̕V̕W̕X̕Y̕Z̕", note.lines.get(86));
        assertEquals("     a̕b̕c̕d̕e̕f̕g̕h̕i̕j̕k̕l̕m̕", note.lines.get(87));
        assertEquals("     n̕o̕p̕q̕r̕s̕t̕u̕v̕w̕x̕y̕z̕", note.lines.get(88));

        // EE (Unicode: double acute, 030B) double acute accent
        assertEquals("     A̋B̋C̋D̋E̋F̋G̋H̋I̋J̋K̋L̋M̋", note.lines.get(91));
        assertEquals("     N̋ŐP̋Q̋R̋S̋T̋ŰV̋W̋X̋Y̋Z̋", note.lines.get(92));
        assertEquals("     a̋b̋c̋d̋e̋f̋g̋h̋i̋j̋k̋l̋m̋", note.lines.get(93));
        assertEquals("     n̋őp̋q̋r̋s̋t̋űv̋w̋x̋y̋z̋", note.lines.get(94));

        // EF (Unicode: candrabindu, 0310) candrabindu
        assertEquals("     A̐B̐C̐D̐E̐F̐G̐H̐I̐J̐K̐L̐M̐", note.lines.get(97));
        assertEquals("     N̐O̐P̐Q̐R̐S̐T̐U̐V̐W̐X̐Y̐Z̐", note.lines.get(98));
        assertEquals("     a̐b̐c̐d̐e̐f̐g̐h̐i̐j̐k̐l̐m̐", note.lines.get(99));
        assertEquals("     n̐o̐p̐q̐r̐s̐t̐u̐v̐w̐x̐y̐z̐", note.lines.get(100));

        // F0 (Unicode: cedilla, 0327) cedilla
        assertEquals("     A̧B̧ÇḐȨF̧ĢḨI̧J̧ĶĻM̧", note.lines.get(103));
        assertEquals("     ŅO̧P̧Q̧ŖŞŢU̧V̧W̧X̧Y̧Z̧", note.lines.get(104));
        assertEquals("     a̧b̧çḑȩf̧ģḩi̧j̧ķļm̧", note.lines.get(105));
        assertEquals("     ņo̧p̧q̧ŗşţu̧v̧w̧x̧y̧z̧", note.lines.get(106));

        // TODO - Double check this - is this right?
        // F1 (Unicode: ogonek, 0328) right hook
        assertEquals("     ĄB̨C̨D̨ĘF̨G̨H̨ĮJ̨K̨L̨M̨", note.lines.get(109));
        assertEquals("     N̨ǪP̨Q̨R̨S̨T̨ŲV̨W̨X̨Y̨Z̨", note.lines.get(110));
        assertEquals("     ąb̨c̨d̨ęf̨g̨h̨įj̨k̨l̨m̨", note.lines.get(111));
        assertEquals("     n̨ǫp̨q̨r̨s̨t̨ųv̨w̨x̨y̨z̨", note.lines.get(112));

        // F2 (Unicode: dot below, 0323) dot below
        assertEquals("     ẠḄC̣ḌẸF̣G̣ḤỊJ̣ḲḶṂ", note.lines.get(115));
        assertEquals("     ṆỌP̣Q̣ṚṢṬỤṾẈX̣ỴẒ", note.lines.get(116));
        assertEquals("     ạḅc̣ḍẹf̣g̣ḥịj̣ḳḷṃ", note.lines.get(117));
        assertEquals("     ṇọp̣q̣ṛṣṭụṿẉx̣ỵẓ", note.lines.get(118));

        // F3 (Unicode: diaeresis below, 0324) double dot below
        assertEquals("     A̤B̤C̤D̤E̤F̤G̤H̤I̤J̤K̤L̤M̤", note.lines.get(121));
        assertEquals("     N̤O̤P̤Q̤R̤S̤T̤ṲV̤W̤X̤Y̤Z̤", note.lines.get(122));
        assertEquals("     a̤b̤c̤d̤e̤f̤g̤h̤i̤j̤k̤l̤m̤", note.lines.get(123));
        assertEquals("     n̤o̤p̤q̤r̤s̤t̤ṳv̤w̤x̤y̤z̤", note.lines.get(124));

        // F4 (Unicode: ring below, 0325) circle below
        assertEquals("     ḀB̥C̥D̥E̥F̥G̥H̥I̥J̥K̥L̥M̥", note.lines.get(127));
        assertEquals("     N̥O̥P̥Q̥R̥S̥T̥U̥V̥W̥X̥Y̥Z̥", note.lines.get(128));
        assertEquals("     ḁb̥c̥d̥e̥f̥g̥h̥i̥j̥k̥l̥m̥", note.lines.get(129));
        assertEquals("     n̥o̥p̥q̥r̥s̥t̥u̥v̥w̥x̥y̥z̥", note.lines.get(130));

        // F5 (Unicode: double low line, 0333) double underscore
        assertEquals("     A̳B̳C̳D̳E̳F̳G̳H̳I̳J̳K̳L̳M̳", note.lines.get(133));
        assertEquals("     N̳O̳P̳Q̳R̳S̳T̳U̳V̳W̳X̳Y̳Z̳", note.lines.get(134));
        assertEquals("     a̳b̳c̳d̳e̳f̳g̳h̳i̳j̳k̳l̳m̳", note.lines.get(135));
        assertEquals("     n̳o̳p̳q̳r̳s̳t̳u̳v̳w̳x̳y̳z̳", note.lines.get(136));

        // F6 (Unicode: line below, 0332) underscore
        assertEquals("     A̲B̲C̲D̲E̲F̲G̲H̲I̲J̲K̲L̲M̲", note.lines.get(139));
        assertEquals("     N̲O̲P̲Q̲R̲S̲T̲U̲V̲W̲X̲Y̲Z̲", note.lines.get(140));
        assertEquals("     a̲b̲c̲d̲e̲f̲g̲h̲i̲j̲k̲l̲m̲", note.lines.get(141));
        assertEquals("     n̲o̲p̲q̲r̲s̲t̲u̲v̲w̲x̲y̲z̲", note.lines.get(142));

        // F7 (Unicode: comma below, 0326) left hook
        assertEquals("     A̦B̦C̦D̦E̦F̦G̦H̦I̦J̦K̦L̦M̦", note.lines.get(145));
        assertEquals("     N̦O̦P̦Q̦R̦ȘȚU̦V̦W̦X̦Y̦Z̦", note.lines.get(146));
        assertEquals("     a̦b̦c̦d̦e̦f̦g̦h̦i̦j̦k̦l̦m̦", note.lines.get(147));
        assertEquals("     n̦o̦p̦q̦r̦șțu̦v̦w̦x̦y̦z̦", note.lines.get(148));

        // F8 (Unicode: left half ring below, 031C) right cedilla
        assertEquals("     A̜B̜C̜D̜E̜F̜G̜H̜I̜J̜K̜L̜M̜", note.lines.get(151));
        assertEquals("     N̜O̜P̜Q̜R̜S̜T̜U̜V̜W̜X̜Y̜Z̜", note.lines.get(152));
        assertEquals("     a̜b̜c̜d̜e̜f̜g̜h̜i̜j̜k̜l̜m̜", note.lines.get(153));
        assertEquals("     n̜o̜p̜q̜r̜s̜t̜u̜v̜w̜x̜y̜z̜", note.lines.get(154));

        // F9 (Unicode: breve below, 032E) half circle below
        assertEquals("     A̮B̮C̮D̮E̮F̮G̮ḪI̮J̮K̮L̮M̮", note.lines.get(157));
        assertEquals("     N̮O̮P̮Q̮R̮S̮T̮U̮V̮W̮X̮Y̮Z̮", note.lines.get(158));
        assertEquals("     a̮b̮c̮d̮e̮f̮g̮ḫi̮j̮k̮l̮m̮", note.lines.get(159));
        assertEquals("     n̮o̮p̮q̮r̮s̮t̮u̮v̮w̮x̮y̮z̮", note.lines.get(160));

        // FA (Unicode: double tilde left half, FE22) double tilde, left half
        assertEquals("     A︢B︢C︢D︢E︢F︢G︢H︢I︢J︢K︢L︢M︢", note.lines.get(163));
        assertEquals("     N︢O︢P︢Q︢R︢S︢T︢U︢V︢W︢X︢Y︢Z︢", note.lines.get(164));
        assertEquals("     a︢b︢c︢d︢e︢f︢g︢h︢i︢j︢k︢l︢m︢", note.lines.get(165));
        assertEquals("     n︢o︢p︢q︢r︢s︢t︢u︢v︢w︢x︢y︢z︢", note.lines.get(166));

        // FB (Unicode: double tilde right half, FE23) double tilde, right half
        assertEquals("     A︣B︣C︣D︣E︣F︣G︣H︣I︣J︣K︣L︣M︣", note.lines.get(169));
        assertEquals("     N︣O︣P︣Q︣R︣S︣T︣U︣V︣W︣X︣Y︣Z︣", note.lines.get(170));
        assertEquals("     a︣b︣c︣d︣e︣f︣g︣h︣i︣j︣k︣l︣m︣", note.lines.get(171));
        assertEquals("     n︣o︣p︣q︣r︣s︣t︣u︣v︣w︣x︣y︣z︣", note.lines.get(172));

        // FE (Unicode: comma above, 0313) high comma, centered
        assertEquals("     A̓B̓C̓D̓E̓F̓G̓H̓I̓J̓K̓L̓M̓", note.lines.get(175));
        assertEquals("     N̓O̓P̓Q̓R̓S̓T̓U̓V̓W̓X̓Y̓Z̓", note.lines.get(176));
        assertEquals("     a̓b̓c̓d̓e̓f̓g̓h̓i̓j̓k̓l̓m̓", note.lines.get(177));
        assertEquals("     n̓o̓p̓q̓r̓s̓t̓u̓v̓w̓x̓y̓z̓", note.lines.get(178));
    }

    /**
     * Check extended characters
     */
    @Test
    public void textExtended() {
        Note note = g.notes.get("@N25@"); // This one has lots of extended characters
        assertEquals(43, note.lines.size());
        assertEquals("A1 slash l - uppercase (Ł)", note.lines.get(4));
        assertEquals("A2 slash o - uppercase (Ø)", note.lines.get(5));
        assertEquals("A3 slash d - uppercase (Đ)", note.lines.get(6));
        assertEquals("A4 thorn - uppercase (Þ)", note.lines.get(7));
        assertEquals("A5 ligature ae - uppercase (Æ)", note.lines.get(8));
        assertEquals("A6 ligature oe - uppercase (Œ)", note.lines.get(9));
        assertEquals("A7 single prime (ʹ)", note.lines.get(10));
        assertEquals("A8 middle dot (·)", note.lines.get(11));
        assertEquals("A9 musical flat (♭)", note.lines.get(12));
        assertEquals("AA registered sign (®)", note.lines.get(13));
        assertEquals("AB plus-or-minus (±)", note.lines.get(14));
        assertEquals("AC hook o - uppercase (Ơ)", note.lines.get(15));
        assertEquals("AD hook u - uppercase (Ư)", note.lines.get(16));
        assertEquals("AE left half ring (ʼ)", note.lines.get(17));
        assertEquals("BO right half ring (ʻ)", note.lines.get(18));
        assertEquals("B1 slash l - lowercase (ł)", note.lines.get(19));
        assertEquals("B2 slash o - lowercase (ø)", note.lines.get(20));
        assertEquals("B3 slash d - lowercase (đ)", note.lines.get(21));
        assertEquals("B4 thorn - lowercase (þ)", note.lines.get(22));
        assertEquals("B5 ligature ae - lowercase (æ)", note.lines.get(23));
        assertEquals("B6 ligature oe - lowercase (œ)", note.lines.get(24));
        assertEquals("B7 double prime (ʺ)", note.lines.get(25));
        assertEquals("B8 dotless i - lowercase (ı)", note.lines.get(26));
        assertEquals("B9 british pound (£)", note.lines.get(27));
        assertEquals("BA eth (ð)", note.lines.get(28));
        assertEquals("BC hook o - lowercase (ơ)", note.lines.get(29));
        assertEquals("BD hook u - lowercase (ư)", note.lines.get(30));
        assertEquals("BE empty box - LDS extension (□)", note.lines.get(31));
        assertEquals("BF black box - LDS extensions (■)", note.lines.get(32));
        assertEquals("CO degree sign (°)", note.lines.get(33));
        assertEquals("C1 script l (ℓ)", note.lines.get(34));
        assertEquals("C2 phonograph copyright mark (℗)", note.lines.get(35));
        assertEquals("C3 copyright symbol (©)", note.lines.get(36));
        assertEquals("C4 musical sharp (♯)", note.lines.get(37));
        assertEquals("C5 inverted question mark (¿)", note.lines.get(38));
        assertEquals("C6 inverted exclamation mark (¡)", note.lines.get(39));
        assertEquals("CD midline e - LDS extension (e)", note.lines.get(40));
        assertEquals("CE midline o - LDS extension (o)", note.lines.get(41));
        assertEquals("CF es zet (ß)", note.lines.get(42));

    }
}
