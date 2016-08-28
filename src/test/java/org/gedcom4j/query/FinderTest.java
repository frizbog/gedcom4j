package org.gedcom4j.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEventType;
import org.gedcom4j.parser.GedcomParser;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link Finder}
 * 
 * @author frizbog
 */
public class FinderTest {

    /**
     * Class under test
     */
    private Finder classUnderTest;

    /**
     * Set up test fixtures
     * 
     * @throws GedcomParserException
     *             if the file cannot be parsed
     * @throws IOException
     *             if the file cannot be read
     */
    @Before
    public void setUp() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/5.5.1 sample 1.ged");
        Gedcom gedcom = gp.getGedcom();
        classUnderTest = new Finder(gedcom);
    }

    /**
     * Test for {@link Finder#findByEvent(org.gedcom4j.model.IndividualEventType, java.util.Date, java.util.Date)}
     */
    @Test
    public void testFindByEventIndividualEventTypeDateDate() {
        @SuppressWarnings("deprecation")
        Date d1 = new Date(1825 - 1900, Calendar.MARCH, 28);
        @SuppressWarnings("deprecation")
        Date d2 = new Date(1825 - 1900, Calendar.MARCH, 30);
        Set<Individual> matches = classUnderTest.findByEvent(IndividualEventType.BIRTH, d1, d2);
        assertNotNull(matches);
        assertEquals(1, matches.size());
        Individual i = matches.iterator().next();
        assertEquals("Stephen /Walley/", i.getNames().get(0).getBasic());
    }

    /**
     * Test for {@link Finder#findByEvent(org.gedcom4j.model.IndividualEventType, String, String)}
     */
    @Test
    public void testFindByEventIndividualEventTypeStringString() {
        Set<Individual> matches = classUnderTest.findByEvent(IndividualEventType.BIRTH, "29 Mar 1825", "29 Mar 1825");
        assertNotNull(matches);
        assertEquals(1, matches.size());
        Individual i = matches.iterator().next();
        assertEquals("Stephen /Walley/", i.getNames().get(0).getBasic());
    }

    /**
     * Test for {@link Finder#findByName(String, String)}
     */
    @Test
    public void testFindByNameStringStringNegative() {
        List<Individual> matches = classUnderTest.findByName("Willis", "Edmund Henry");
        assertNotNull(matches);
        assertEquals(0, matches.size());
    }

    /**
     * Test for {@link Finder#findByName(String, String)}
     */
    @Test
    public void testFindByNameStringStringPositive() {
        List<Individual> matches = classUnderTest.findByName("Walley", "Richard Pedley");
        assertNotNull(matches);
        assertEquals(2, matches.size());
        assertNotNull(matches.get(0).getNames());
        assertEquals(1, matches.get(0).getNames().size());
        assertNotNull(matches.get(0).getNames().get(0));
        assertEquals("Richard Pedley /Walley/", matches.get(0).getNames().get(0).getBasic());
    }

    /**
     * Test for {@link Finder#findByName(String, String, String, String)}
     */
    @Test
    public void testFindByNameStringStringStringStringNegative() {
        List<Individual> matches = classUnderTest.findByName("Jr.", "Willis", "Edmund Henry", "");
        assertNotNull(matches);
        assertEquals(0, matches.size());
    }

    /**
     * Test for {@link Finder#findByName(String, String, String, String)}
     */
    @Test
    public void testFindByNameStringStringStringStringPositive1() {
        List<Individual> matches = classUnderTest.findByName(", Jd", "Walley", "Richard Pedley", "");
        assertNotNull(matches);
        assertEquals(2, matches.size());
        assertNotNull(matches.get(0));
        assertEquals("Richard Pedley /Walley/", matches.get(0).getNames().get(0).getBasic());
        assertNotNull(matches.get(1));
        assertEquals("Richard Pedley /Walley/, Jp", matches.get(1).getNames().get(0).getBasic());
    }

    /**
     * Test for {@link Finder#findByName(String, String, String, String)}
     */
    @Test
    public void testFindByNameStringStringStringStringPositive2() {
        List<Individual> matches = classUnderTest.findByName("", "Pedley", "Julian", "Dr");
        assertNotNull(matches);
        assertEquals(1, matches.size());
        assertNotNull(matches.get(0));
        assertEquals("Julian /Pedley/", matches.get(0).getNames().get(0).getBasic());
        assertEquals("Dr", matches.get(0).getNames().get(0).getPrefix().getValue());
    }

}
