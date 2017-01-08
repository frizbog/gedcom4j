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
package org.gedcom4j.relationship;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Locale;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.IGedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.query.Finder;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * Test for {@link KinshipNameCalculator} class.
 * </p>
 * <p>
 * Loads a sample relationship file, and checks relationships between a main person in that file (Alex /Zucco/) and the others in
 * the file.
 * </p>
 * 
 * @author frizbog
 */
public class KinshipNameCalculatorTest {

    /**
     * A finder test fixture for the test
     */
    private Finder finder;

    /**
     * Class under test. Always use a locale in the test to ensure we are using the properties file we expect to use.
     */
    KinshipNameCalculator knc = new KinshipNameCalculator(Locale.US);

    /**
     * Set up test fixtures
     * 
     * @throws IOException
     *             if the gedcom file can't be read
     * @throws GedcomParserException
     *             if the gedcom can't be parsed
     */
    @Before
    public void setUp() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/RelationshipTestExtended.ged");
        assertTrue(gp.getErrors().isEmpty());
        assertTrue(gp.getWarnings().isEmpty());

        final IGedcom g = gp.getGedcom();
        assertNotNull(g);
        assertEquals("There are supposed to be 70 people in the gedcom - are you using the right file/file version?", 70, g
                .getIndividuals().size());
        assertEquals("There are supposed to be 31 families in the gedcom - are you using the right file/file version?", 31, g
                .getFamilies().size());
        finder = new Finder(g);
    }

    /**
     * Test an aunt and nephew
     */
    @Test
    public void testAuntsUnclesNiecesNephews() {
        Individual alex = getPerson("Zucco", "Alex");
        Individual betsy = getPerson("Zucco", "Betsy");

        Individual theresa = getPerson("Andrews", "Theresa");
        Individual julie = getPerson("Zucco", "Julie");
        Individual henry = getPerson("Zucco", "Henry");

        assertEquals("Aunt", knc.getRelationshipName(alex, theresa));
        assertEquals("Nephew", knc.getRelationshipName(theresa, alex));

        assertEquals("Aunt", knc.getRelationshipName(betsy, theresa));
        assertEquals("Niece", knc.getRelationshipName(theresa, betsy));

        assertEquals("Aunt", knc.getRelationshipName(alex, julie));
        assertEquals("Nephew", knc.getRelationshipName(julie, alex));

        assertEquals("Aunt", knc.getRelationshipName(betsy, julie));
        assertEquals("Niece", knc.getRelationshipName(julie, betsy));

        assertEquals("Uncle", knc.getRelationshipName(alex, henry));
        assertEquals("Nephew", knc.getRelationshipName(henry, alex));

        assertEquals("Uncle", knc.getRelationshipName(betsy, henry));
        assertEquals("Niece", knc.getRelationshipName(henry, betsy));
    }

    /**
     * Test an aunt and nephew
     */
    @Test
    public void testCousins() {
        Individual alex = getPerson("Zucco", "Alex");
        Individual olivia = getPerson("Zucco", "Olivia");
        Individual peter = getPerson("Zucco", "Peter");
        Individual richard = getPerson("Zucco", "Richard");
        Individual edward = getPerson("Knight", "Edward");
        Individual jerome = getPerson("Knight", "Jerome");
        Individual isaac = getPerson("Knight", "Isaac");

        assertEquals("1st Cousin", knc.getRelationshipName(alex, olivia));
        assertEquals("1st Cousin", knc.getRelationshipName(olivia, alex));

        assertEquals("1st Cousin", knc.getRelationshipName(alex, peter));
        assertEquals("1st Cousin", knc.getRelationshipName(peter, alex));

        assertEquals("1st Cousin 1 time removed", knc.getRelationshipName(alex, richard));
        assertEquals("1st Cousin 1 time removed", knc.getRelationshipName(richard, alex));

        assertEquals("1st Cousin 3 times removed", knc.getRelationshipName(alex, edward));
        assertEquals("1st Cousin 3 times removed", knc.getRelationshipName(edward, alex));

        assertEquals("2nd Cousin 2 times removed", knc.getRelationshipName(alex, jerome));
        assertEquals("2nd Cousin 2 times removed", knc.getRelationshipName(jerome, alex));

        assertEquals("3rd Cousin 1 time removed", knc.getRelationshipName(alex, isaac));
        assertEquals("3rd Cousin 1 time removed", knc.getRelationshipName(isaac, alex));
    }

    /**
     * Test an aunt and nephew
     */
    @Test
    public void testGrandparents() {
        Individual alex = getPerson("Zucco", "Alex");
        Individual betsy = getPerson("Zucco", "Betsy");
        Individual gpa1 = getPerson("Zucco", "George");
        Individual gma1 = getPerson("Smith", "Hannah");
        Individual gpa2 = getPerson("Andrews", "Robert");
        Individual gma2 = getPerson("Jackson", "Sylvia");

        assertEquals("Grandfather", knc.getRelationshipName(alex, gpa1));
        assertEquals("Grandson", knc.getRelationshipName(gpa1, alex));

        assertEquals("Grandfather", knc.getRelationshipName(alex, gpa2));
        assertEquals("Grandson", knc.getRelationshipName(gpa2, alex));

        assertEquals("Grandmother", knc.getRelationshipName(alex, gma1));
        assertEquals("Grandson", knc.getRelationshipName(gma1, alex));

        assertEquals("Grandmother", knc.getRelationshipName(alex, gma2));
        assertEquals("Grandson", knc.getRelationshipName(gma2, alex));

        assertEquals("Grandfather", knc.getRelationshipName(betsy, gpa1));
        assertEquals("Granddaughter", knc.getRelationshipName(gpa1, betsy));

        assertEquals("Grandfather", knc.getRelationshipName(betsy, gpa2));
        assertEquals("Granddaughter", knc.getRelationshipName(gpa2, betsy));

        assertEquals("Grandmother", knc.getRelationshipName(betsy, gma1));
        assertEquals("Granddaughter", knc.getRelationshipName(gma1, betsy));

        assertEquals("Grandmother", knc.getRelationshipName(betsy, gma2));
        assertEquals("Granddaughter", knc.getRelationshipName(gma2, betsy));

        // Reality check at the end
        assertEquals("Wife", knc.getRelationshipName(gpa1, gma1));
        assertEquals("Husband", knc.getRelationshipName(gma1, gpa1));
    }

    /**
     * Test an aunt and nephew
     */
    @Test
    public void testGreatAuntsUnclesNiecesNephews() {
        Individual alex = getPerson("Zucco", "Alex");
        Individual betsy = getPerson("Zucco", "Betsy");
        Individual nancy = getPerson("Andrews", "Nancy");
        Individual sammy = getPerson("Struthers", "Sammy");
        Individual ulysses = getPerson("Knight", "Ulysses");
        Individual robert = getPerson("Knight", "Robert");

        assertEquals("Great-Uncle", knc.getRelationshipName(nancy, sammy));
        assertEquals("Great-Niece", knc.getRelationshipName(sammy, nancy));

        assertEquals("Great-Great-Uncle", knc.getRelationshipName(alex, sammy));
        assertEquals("Great-Great-Nephew", knc.getRelationshipName(sammy, alex));

        assertEquals("Great-Great-Uncle", knc.getRelationshipName(betsy, sammy));
        assertEquals("Great-Great-Niece", knc.getRelationshipName(sammy, betsy));

        assertEquals("3rd Great-Aunt/Uncle", knc.getRelationshipName(alex, ulysses));
        assertEquals("3rd Great-Nephew", knc.getRelationshipName(ulysses, alex));

        assertEquals("3rd Great-Aunt/Uncle", knc.getRelationshipName(betsy, ulysses));
        assertEquals("3rd Great-Niece", knc.getRelationshipName(ulysses, betsy));

        assertEquals("3rd Great-Uncle", knc.getRelationshipName(alex, robert));
        assertEquals("3rd Great-Nephew", knc.getRelationshipName(robert, alex));

        assertEquals("3rd Great-Uncle", knc.getRelationshipName(betsy, robert));
        assertEquals("3rd Great-Niece", knc.getRelationshipName(robert, betsy));
    }

    /**
     * Test an aunt and nephew
     */
    @Test
    public void testGreatGrandparents() {
        Individual alex = getPerson("Zucco", "Alex");
        Individual betsy = getPerson("Zucco", "Betsy");
        Individual ggpa1 = getPerson("Zucco", "Fred");
        Individual ggpa2 = getPerson("Jackson", "Terrence");
        Individual ggpa3 = getPerson("Andrews", "James");
        Individual ggma1 = getPerson("Klinghoffer", "Patty");
        Individual ggma2 = getPerson("Struthers", "Sally");

        assertEquals("Great-Grandfather", knc.getRelationshipName(alex, ggpa1));
        assertEquals("Great-Grandson", knc.getRelationshipName(ggpa1, alex));

        assertEquals("Great-Grandfather", knc.getRelationshipName(alex, ggpa2));
        assertEquals("Great-Grandson", knc.getRelationshipName(ggpa2, alex));

        assertEquals("Great-Grandfather", knc.getRelationshipName(alex, ggpa3));
        assertEquals("Great-Grandson", knc.getRelationshipName(ggpa3, alex));

        assertEquals("Great-Grandmother", knc.getRelationshipName(alex, ggma1));
        assertEquals("Great-Grandson", knc.getRelationshipName(ggma1, alex));

        assertEquals("Great-Grandmother", knc.getRelationshipName(alex, ggma2));
        assertEquals("Great-Grandson", knc.getRelationshipName(ggma2, alex));

        assertEquals("Great-Grandfather", knc.getRelationshipName(betsy, ggpa1));
        assertEquals("Great-Granddaughter", knc.getRelationshipName(ggpa1, betsy));

        assertEquals("Great-Grandfather", knc.getRelationshipName(betsy, ggpa2));
        assertEquals("Great-Granddaughter", knc.getRelationshipName(ggpa2, betsy));

        assertEquals("Great-Grandfather", knc.getRelationshipName(betsy, ggpa3));
        assertEquals("Great-Granddaughter", knc.getRelationshipName(ggpa3, betsy));

        assertEquals("Great-Grandmother", knc.getRelationshipName(betsy, ggma1));
        assertEquals("Great-Granddaughter", knc.getRelationshipName(ggma1, betsy));

        assertEquals("Great-Grandmother", knc.getRelationshipName(betsy, ggma2));
        assertEquals("Great-Granddaughter", knc.getRelationshipName(ggma2, betsy));

        // Reality check at the end
        assertEquals("Wife", knc.getRelationshipName(ggpa3, ggma2));
        assertEquals("Husband", knc.getRelationshipName(ggma2, ggpa3));

    }

    /**
     * Test an aunt and nephew
     */
    @Test
    public void testGreatGreatGrandparents() {
        Individual alex = getPerson("Zucco", "Alex");
        Individual betsy = getPerson("Zucco", "Betsy");
        Individual gggpa1 = getPerson("Struthers", "Steven");
        Individual gggpa2 = getPerson("Jackson", "Ulysses");
        Individual gggma1 = getPerson("Wood", "Abigail");
        Individual gggma2 = getPerson("Knight", "Gladys");

        assertEquals("Great-Great-Grandfather", knc.getRelationshipName(alex, gggpa1));
        assertEquals("Great-Great-Grandson", knc.getRelationshipName(gggpa1, alex));

        assertEquals("Great-Great-Grandfather", knc.getRelationshipName(alex, gggpa2));
        assertEquals("Great-Great-Grandson", knc.getRelationshipName(gggpa2, alex));

        assertEquals("Great-Great-Grandmother", knc.getRelationshipName(alex, gggma1));
        assertEquals("Great-Great-Grandson", knc.getRelationshipName(gggma1, alex));

        assertEquals("Great-Great-Grandmother", knc.getRelationshipName(alex, gggma2));
        assertEquals("Great-Great-Grandson", knc.getRelationshipName(gggma2, alex));

        assertEquals("Great-Great-Grandfather", knc.getRelationshipName(betsy, gggpa1));
        assertEquals("Great-Great-Granddaughter", knc.getRelationshipName(gggpa1, betsy));

        assertEquals("Great-Great-Grandfather", knc.getRelationshipName(betsy, gggpa2));
        assertEquals("Great-Great-Granddaughter", knc.getRelationshipName(gggpa2, betsy));

        assertEquals("Great-Great-Grandmother", knc.getRelationshipName(betsy, gggma1));
        assertEquals("Great-Great-Granddaughter", knc.getRelationshipName(gggma1, betsy));

        assertEquals("Great-Great-Grandmother", knc.getRelationshipName(betsy, gggma2));
        assertEquals("Great-Great-Granddaughter", knc.getRelationshipName(gggma2, betsy));

        // Reality check at the end
        assertEquals("Wife", knc.getRelationshipName(gggpa1, gggma2));
        assertEquals("Husband", knc.getRelationshipName(gggma2, gggpa1));

    }

    /**
     * Test immediate family
     */
    @Test
    public void testImmediateFamily() {
        Individual alex = getPerson("Zucco", "Alex");
        Individual dad = getPerson("Zucco", "Michael");
        Individual mom = getPerson("Andrews", "Nancy");
        Individual bro = getPerson("Zucco", "Charlie");
        Individual sis = getPerson("Zucco", "Betsy");

        assertEquals("Father", knc.getRelationshipName(alex, dad));
        assertEquals("Son", knc.getRelationshipName(dad, alex));

        assertEquals("Mother", knc.getRelationshipName(alex, mom));
        assertEquals("Son", knc.getRelationshipName(mom, alex));

        assertEquals("Brother", knc.getRelationshipName(alex, bro));
        assertEquals("Brother", knc.getRelationshipName(bro, alex));

        assertEquals("Sister", knc.getRelationshipName(alex, sis));
        assertEquals("Brother", knc.getRelationshipName(sis, alex));

        assertEquals("Wife", knc.getRelationshipName(dad, mom));
        assertEquals("Husband", knc.getRelationshipName(mom, dad));

        assertEquals("Father", knc.getRelationshipName(bro, dad));
        assertEquals("Son", knc.getRelationshipName(dad, bro));

        assertEquals("Mother", knc.getRelationshipName(bro, mom));
        assertEquals("Son", knc.getRelationshipName(mom, bro));

        assertEquals("Father", knc.getRelationshipName(sis, dad));
        assertEquals("Daughter", knc.getRelationshipName(dad, sis));

        assertEquals("Mother", knc.getRelationshipName(sis, mom));
        assertEquals("Daughter", knc.getRelationshipName(mom, sis));

    }

    /**
     * Test for situations where people are not blood relatives but are related through spouses
     */
    @Test
    public void testRelatedByMarriage() {
        Individual alex = getPerson("Zucco", "Alex");
        Individual michael = getPerson("Zucco", "Michael");
        Individual nancy = getPerson("Andrews", "Nancy");
        Individual roberta = getPerson("King", "Roberta");
        Individual christina = getPerson("Mathers", "Christina");
        Individual henrietta = getPerson("Holcomb", "Henrietta");
        Individual denise = getPerson("Polanski", "Denise");
        Individual quincy = getPerson("Queen", "Quincy");
        Individual elizabeth = getPerson("Queen", "Elizabeth");
        Individual zoe = getPerson("Olds", "Zoe");
        Individual peter = getPerson("Polanski", "Peter");

        assertEquals("Mother-in-Law", knc.getRelationshipName(alex, zoe));
        assertEquals("Son-in-Law", knc.getRelationshipName(zoe, alex));

        assertEquals("Father-in-Law", knc.getRelationshipName(alex, peter));
        assertEquals("Son-in-Law", knc.getRelationshipName(peter, alex));

        assertEquals("Mother-in-Law", knc.getRelationshipName(denise, nancy));
        assertEquals("Daughter-in-Law", knc.getRelationshipName(nancy, denise));

        assertEquals("Father-in-Law", knc.getRelationshipName(denise, michael));
        assertEquals("Daughter-in-Law", knc.getRelationshipName(michael, denise));

        assertEquals("Sister-in-Law", knc.getRelationshipName(alex, elizabeth));
        assertEquals("Brother-in-Law", knc.getRelationshipName(elizabeth, alex));

        assertEquals("3rd Great-Aunt", knc.getRelationshipName(alex, roberta));
        assertEquals("Husband's 3rd Great-Nephew", knc.getRelationshipName(roberta, alex));

        assertEquals("1st Cousin 2 times removed's Wife", knc.getRelationshipName(alex, christina));
        assertEquals("Husband's 1st Cousin 2 times removed", knc.getRelationshipName(christina, alex));

        assertEquals("Aunt", knc.getRelationshipName(alex, henrietta));
        assertEquals("Husband's Nephew", knc.getRelationshipName(henrietta, alex));

        assertEquals("Wife", knc.getRelationshipName(alex, denise));
        assertEquals("Husband", knc.getRelationshipName(denise, alex));

        assertEquals("3rd Cousin 1 time removed's Husband", knc.getRelationshipName(alex, quincy));
        assertEquals("Wife's 3rd Cousin 1 time removed", knc.getRelationshipName(quincy, alex));

    }

    /**
     * Test a person's relationship to himself
     */
    @Test
    public void testSelf() {
        Individual alex = getPerson("Zucco", "Alex");

        assertEquals("Self", knc.getRelationshipName(alex, alex));
    }

    /**
     * Helper method to get a person and assert they exist
     * 
     * @param surname
     *            the surname of the person we want
     * @param givenName
     *            the given name of the person we want
     * @return the person
     */
    private Individual getPerson(String surname, String givenName) {
        Individual result = finder.findByName(surname, givenName).get(0);
        assertNotNull("Couldn't find " + givenName + " " + surname + " by name in the gedcom", result);
        return result;
    }
}
