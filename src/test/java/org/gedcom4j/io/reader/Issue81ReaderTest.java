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
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.InMemoryGedcom;
import org.gedcom4j.model.IGedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.NoteStructure;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.parser.GedcomParser;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for issue 81, where characters with diacritical marks were being reported as not loading correctly from files.
 * 
 * @author frizbog
 */
public class Issue81ReaderTest {

    /**
     * The gedcom loaded from the sample file
     */
    private IGedcom g;

    /**
     * Load the gedcom, assert that everything loaded ok. We'll inspect things later.
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if the file can't be parsed
     */
    @Before
    @SuppressWarnings("PMD.SystemPrintln")
    public void setUp() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser(new InMemoryGedcom());
        gp.load("sample/issue81.ged");
        for (String e : gp.getErrors()) {
            System.err.println(e);
        }
        for (String w : gp.getWarnings()) {
            System.err.println(w);
        }
        assertEquals(0, gp.getErrors().size());
        assertEquals(0, gp.getWarnings().size());
        g = gp.getGedcom();
        assertEquals(2, g.getIndividuals().size());
    }

    /**
     * Test for proper concatenation of strings in CONC tags when the diacritic appears at end of line. The word in question is
     * &quot;arr\u00EAt\u00E9&quot;, which is on the fourth line (third CONC line) of the note.
     */
    @Test
    public void testConcDiacriticAtEndOfLine() {
        Individual i = g.getIndividuals().get("@I002@");
        assertNotNull(i);
        assertEquals(2, i.getEvents().size());
        IndividualEvent e = i.getEvents().get(1);
        assertEquals(1, e.getNoteStructures().size());
        NoteStructure note = e.getNoteStructures().get(0);
        assertEquals(6, note.getLines().size());

        assertEquals("Sa grand-m\u00E8re l'a nourrie car sa m\u00E8re \u00E9tait plac\u00E9e nourrice dans une famille de riches. "
                + "Son oncle ( une tante en r\u00E9alit\u00E9 Agueda), allait\u00E9 avec elle, est d\u00E9c\u00E9d\u00E9 ( voir acte). "
                + "Elle croyait qu'il \u00E9tait mort \u00E0 cause d'elle, en lui prenant son lait. "
                + "Pendant la R\u00E9volution espagnole du 18/7/1936 au 1/4/1939, elle a fuit le village, par le seul pont, "
                + "surveill\u00E9 par les Franquistes, elle a travers\u00E9 l'Espagne, avec \"Juliette tenue par la main et "
                + "Victoire dans les bras ou au sein\" je crois ( en Espagne les enfants \u00E9taient allait\u00E9s 3 ans d'apr\u00E8s "
                + "la fille d'Ascension) jusqu'\u00E0 la fronti\u00E8re fran\u00E7aise. Elle a \u00E9t\u00E9 \" bien accueillie \" par les "
                + "Fran\u00E7ais \u00E0 la descente du train et plac\u00E9e dans les camps des r\u00E9fugi\u00E9s espagnols, \u00E0 Verdelais "
                + "le 3/2/1939 pendant 10 mois, Oloron, 2 mois,Gurs 1 mois, Agde, 4 mois, puis \u00E0 No\u00E9 pr\u00E8s de "
                + "Toulouse du 17/2/1941 \u00E0 septembre 1944 ( lettre \u00E9crite par m\u00E9m\u00E9 pour Victoire), elle y retrouve "
                + "son mari qui est "
                /*
                 * This word below is the one that was split between a diacritical and the letter it modifies
                 */
                + "arr\u00EAt\u00E9 "
                /*
                 * This word above is the one that was split between a diacritical and the letter it modifies
                 */
                + "et d\u00E9port\u00E9. Les femmes juives \u00E9taient d\u00E9port\u00E9es aussi. Alexandre na\u00EEt, "
                + "son pr\u00E9nom vient du bar plac\u00E9 pr\u00E8s du camp, elle va travailler dans les fermes voisines, "
                + "Alexandre et ses s\u0153urs sont gard\u00E9s au camp, par les infirmi\u00E8res fran\u00E7aises. De l\u00E0, ils iront "
                + "\u00E0 Boucau rejoindre d'autres Espagnols \u00E0 \"la Petite Espagne\" pr\u00E8s du Pont S\u00E9mard. Sa cousine, "
                + "venue du village, restera \u00E0 Fonsorbes avec son mari, copain de guerre de T\u00E9ofilo. A Boucau, elle "
                + "retrouvera Ascencion. Vincent na\u00EEtra apr\u00E8s le retour de son p\u00E8re prisonnier \u00E0 Dachau. Daniel sera "
                + "con\u00E7u au retour  du sanatorium, dans lequel son p\u00E8re avait \u00E9t\u00E9 soign\u00E9. ", note
                        .getLines().get(0));

        assertEquals("M\u00E9m\u00E9 m'a racont\u00E9 son histoire, le soir en faisant la vaisselle. Il ne faut pas oublier. "
                + "C'est l'histoire de la famille Martin, mais aussi celle de toutes les r\u00E9volutions, car des "
                + "gens ont voulu, que leurs enfants vivent mieux, qu'ils \u00E9chappent \u00E0 la mis\u00E8re. On doit les "
                + "respecter et les aimer pour cela, malgr\u00E9 leurs d\u00E9fauts et leurs diff\u00E9rences. Nicole", note
                        .getLines().get(1));

        assertEquals("", note.getLines().get(2));

        assertEquals(
                "J'ai photographi\u00E9 la calle de soportales car c'est la rue o\u00F9 est n\u00E9e et a v\u00E9cue m\u00E9m\u00E9. Quand "
                        + "elle est n\u00E9e, ses parents n'\u00E9taient pas mari\u00E9s et elle vivait chez les parents de sa m\u00E8re. Corinne",
                note.getLines().get(3));

        assertEquals("", note.getLines().get(4));

        assertEquals(" ", note.getLines().get(5));
    }

    /**
     * Test diacritical marks in an event subtype (name)
     */
    @Test
    public void testEventName() {
        Individual i = g.getIndividuals().get("@I002@");
        assertNotNull(i);
        assertEquals(2, i.getEvents().size());
        assertEquals("lib\u00E9r\u00E9e", i.getEvents().get(0).getSubType().getValue());
        assertEquals("histoire de m\u00E9m\u00E9", i.getEvents().get(1).getSubType().getValue());
    }

    /**
     * Test the corporation name in the source system of the header - not a diacritical but still a special ANSEL character
     */
    @Test
    public void testHeaderCorporation() {
        assertEquals("BSD Concept \u00A9", g.getHeader().getSourceSystem().getCorporation().getBusinessName());
        assertEquals("BSD Concept \u00A9", g.getHeader().getSourceSystem().getCorporation().getBusinessName());
    }

    /**
     * Simple test for name using a diacritical
     */
    @Test
    public void testIndividual1Name() {
        Individual i = g.getIndividuals().get("@I001@");
        assertNotNull(i);
        PersonalName n = i.getNames().get(0);
        assertEquals("Dolor\u00E8s", n.getGivenName().getValue());
        assertEquals("Dolor\u00E8s/./", n.getBasic());
    }

    /**
     * Slightly more complicated test for name using a diacritical
     */
    @Test
    public void testIndividual2Name() {
        Individual i = g.getIndividuals().get("@I002@");
        assertNotNull(i);
        PersonalName n = i.getNames().get(0);
        assertEquals("Therese", n.getGivenName().getValue());
        assertEquals("VACQU\u00C9", n.getSurname().getValue());
        assertEquals("Therese/VACQU\u00C9/", n.getBasic());
    }
}
