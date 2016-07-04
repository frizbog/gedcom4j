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
package org.gedcom4j.io.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.*;
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
    private Gedcom g;

    /**
     * Load the gedcom, assert that everything loaded ok. We'll inspect things later.
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if the file can't be parsed
     */
    @Before
    public void setUp() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/issue81.ged");
        for (String e : gp.errors) {
            System.err.println(e);
        }
        for (String w : gp.warnings) {
            System.err.println(w);
        }
        assertEquals(0, gp.errors.size());
        assertEquals(0, gp.warnings.size());
        g = gp.gedcom;
        assertEquals(2, g.getIndividuals().size());
    }

    /**
     * Test for proper concatenation of strings in CONC tags when the diacritic appears at end of line. The word in
     * question is &quot;arrêté&quot;, which is on the fourth line (third CONC line) of the note.
     */
    @Test
    public void testConcDiacriticAtEndOfLine() {
        Individual i = g.getIndividuals().get("@I002@");
        assertNotNull(i);
        assertEquals(2, i.getEvents().size());
        IndividualEvent e = i.getEvents().get(1);
        assertEquals(1, e.getNotes().size());
        Note note = e.getNotes().get(0);
        assertEquals(6, note.getLines().size());

        assertEquals("Sa grand-mère l'a nourrie car sa mère était placée nourrice dans une famille de riches. "
                + "Son oncle ( une tante en réalité Agueda), allaité avec elle, est décédé ( voir acte). "
                + "Elle croyait qu'il était mort à cause d'elle, en lui prenant son lait. "
                + "Pendant la Révolution espagnole du 18/7/1936 au 1/4/1939, elle a fuit le village, par le seul pont, "
                + "surveillé par les Franquistes, elle a traversé l'Espagne, avec \"Juliette tenue par la main et "
                + "Victoire dans les bras ou au sein\" je crois ( en Espagne les enfants étaient allaités 3 ans d'après "
                + "la fille d'Ascension) jusqu'à la frontière française. Elle a été \" bien accueillie \" par les "
                + "Français à la descente du train et placée dans les camps des réfugiés espagnols, à Verdelais "
                + "le 3/2/1939 pendant 10 mois, Oloron, 2 mois,Gurs 1 mois, Agde, 4 mois, puis à Noé près de "
                + "Toulouse du 17/2/1941 à septembre 1944 ( lettre écrite par mémé pour Victoire), elle y retrouve " + "son mari qui est "
                /*
                 * This word below is the one that was split between a diacritical and the letter it modifies
                 */
                + "arrêté "
                /*
                 * This word above is the one that was split between a diacritical and the letter it modifies
                 */
                + "et déporté. Les femmes juives étaient déportées aussi. Alexandre naît, "
                + "son prénom vient du bar placé près du camp, elle va travailler dans les fermes voisines, "
                + "Alexandre et ses sœurs sont gardés au camp, par les infirmières françaises. De là, ils iront "
                + "à Boucau rejoindre d'autres Espagnols à \"la Petite Espagne\" près du Pont Sémard. Sa cousine, "
                + "venue du village, restera à Fonsorbes avec son mari, copain de guerre de Téofilo. A Boucau, elle "
                + "retrouvera Ascencion. Vincent naîtra après le retour de son père prisonnier à Dachau. Daniel sera "
                + "conçu au retour  du sanatorium, dans lequel son père avait été soigné. ", note.getLines().get(0));

        assertEquals("Mémé m'a raconté son histoire, le soir en faisant la vaisselle. Il ne faut pas oublier. "
                + "C'est l'histoire de la famille Martin, mais aussi celle de toutes les révolutions, car des "
                + "gens ont voulu, que leurs enfants vivent mieux, qu'ils échappent à la misère. On doit les "
                + "respecter et les aimer pour cela, malgré leurs défauts et leurs différences. Nicole", note.getLines().get(1));

        assertEquals("", note.getLines().get(2));

        assertEquals("J'ai photographié la calle de soportales car c'est la rue où est née et a vécue mémé. Quand "
                + "elle est née, ses parents n'étaient pas mariés et elle vivait chez les parents de sa mère. Corinne", note.getLines().get(3));

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
     * Test the corporation name in the source system of the header - not a diacritical but still a special ANSEL
     * character
     */
    @Test
    public void testHeaderCorporation() {
        assertEquals("BSD Concept \u00A9", g.getHeader().getSourceSystem().getCorporation().getBusinessName());
        assertEquals("BSD Concept ©", g.getHeader().getSourceSystem().getCorporation().getBusinessName());
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
