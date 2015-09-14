package org.gedcom4j.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.gedcom4j.model.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for issue 81, where characters with diacritical marks were being reported as not loading correctly from files.
 * 
 * @author frizbog
 */
public class Issue81ParserTest {

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
        assertEquals(2, g.individuals.size());
    }

    /**
     * Test for proper concatenation of strings in CONC tags when the diacritic appears at end of line
     */
    @Test
    public void testConcDiacriticAtEndOfLine() {
        Individual i = g.individuals.get("@I002@");
        assertNotNull(i);
        assertEquals(2, i.events.size());
        IndividualEvent e = i.events.get(1);
        assertEquals(1, e.notes.size());
        Note note = e.notes.get(0);
        assertEquals(6, note.lines.size());
        assertEquals("Sa grand-máere l'a nourrie car sa máere âetait placâee nourrice dans une famille de riches. "
                + "Son oncle ( une tante en râealitâe Agueda), allaitâe avec elle, est dâecâedâe ( voir acte). "
                + "Elle croyait qu'il âetait mort áa cause d'elle, en lui prenant son lait. "
                + "Pendant la Râevolution espagnole du 18/7/1936 au 1/4/1939, elle a fuit le village, "
                + "par le seul pont, surveillâe par les Franquistes, elle a traversâe l'Espagne, "
                + "avec \"Juliette tenue par la main et Victoire dans les bras ou au sein\" je crois "
                + "( en Espagne les enfants âetaient allaitâes 3 ans d'apráes la fille d'Ascension) "
                + "jusqu'áa la frontiáere franðcaise. Elle a âetâe \" bien accueillie \" par les "
                + "Franðcais áa la descente du train et placâee dans les camps des râefugiâes "
                + "espagnols, áa Verdelais le 3/2/1939 pendant 10 mois, Oloron, 2 mois,Gurs 1 mois, "
                + "Agde, 4 mois, puis áa Noâe práes de Toulouse du 17/2/1941 áa septembre 1944 "
                + "( lettre âecrite par mâemâe pour Victoire), elle y retrouve son mari qui est "
                + "arrãetâe et dâeportâe. Les femmes juives âetaient dâeportâees aussi. "
                + "Alexandre naãit, son prâenom vient du bar placâe práes du camp, elle va travailler "
                + "dans les fermes voisines, Alexandre et ses s¶urs sont gardâes au camp, par les "
                + "infirmiáeres franðcaises. De láa, ils iront áa Boucau rejoindre d'autres "
                + "Espagnols áa \"la Petite Espagne\" práes du Pont Sâemard. Sa cousine, venue du "
                + "village, restera áa Fonsorbes avec son mari, copain de guerre de Tâeofilo. "
                + "A Boucau, elle retrouvera Ascencion. Vincent naãitra apráes le retour de son páere "
                + "prisonnier áa Dachau. Daniel sera conðcu au retour  du sanatorium, dans lequel son páere avait âetâe soignâe.", note.lines.get(0));
        assertEquals("Mâemâe m'a racontâe son histoire, le soir en faisant la vaisselle. Il ne faut pas oublier. "
                + "C'est l'histoire de la famille Martin, mais aussi celle de toutes les râevolutions, "
                + "car des gens ont voulu, que leurs enfants vivent mieux, qu'ils âechappent áa la misáere. "
                + "On doit les respecter et les aimer pour cela, malgrâe leurs dâefauts et leurs diffâerences. Nicole", note.lines.get(1));
        assertEquals("", note.lines.get(2));
        assertEquals("J'ai photographiâe la calle de soportales car c'est la rue oáu est nâee et a vâecue mâemâe. "
                + "Quand elle est nâee, ses parents n'âetaient pas mariâes et elle vivait chez les parents de sa máere. Corinne", note.lines.get(3));
        assertEquals("", note.lines.get(4));
        assertEquals("", note.lines.get(5));
    }

    /**
     * Test diacritical marks in an event subtype (name)
     */
    @Test
    public void testEventName() {
        Individual i = g.individuals.get("@I002@");
        assertNotNull(i);
        assertEquals(2, i.events.size());
        assertEquals("libâerâee", i.events.get(0).subType.value);
        assertEquals("histoire de mâemâe", i.events.get(1).subType.value);
    }

    /**
     * Test the corporation name in the source system of the header
     */
    @Test
    public void testHeaderCorporation() {
        assertEquals("BSD Concept Ã", g.header.sourceSystem.corporation.businessName);
    }

    /**
     * Simple test for name
     */
    @Test
    public void testIndividual1Name() {
        Individual i = g.individuals.get("@I001@");
        assertNotNull(i);
        PersonalName n = i.names.get(0);
        assertEquals("Doloráes", n.givenName.value);
        assertEquals("Doloráes/./", n.basic);
    }

    /**
     * Slightly more complicated test for name
     */
    @Test
    public void testIndividual2Name() {
        Individual i = g.individuals.get("@I002@");
        assertNotNull(i);
        PersonalName n = i.names.get(0);
        assertEquals("Therese", n.givenName.value);
        assertEquals("VACQUâE", n.surname.value);
        assertEquals("Therese/VACQUâE/", n.basic);
    }
}
