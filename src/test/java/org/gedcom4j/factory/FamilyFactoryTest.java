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
package org.gedcom4j.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.gedcom4j.model.Family;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualReference;
import org.junit.Test;

/**
 * Test for {@link FamilyFactory}
 * 
 * @author frizbog
 *
 */
public class FamilyFactoryTest {

    /**
     * Test for {@link FamilyFactory#create(Gedcom, Individual, Individual, Individual...)}
     */
    @SuppressWarnings("deprecation")
    @Test
    public void test() {
        Gedcom g = new Gedcom();
        Individual father = new IndividualFactory().create(g, "Robert", "Tarantino", Sex.MALE, new Date(67, Calendar.MAY, 1),
                "Idaho", new Date(99, Calendar.OCTOBER, 31), "Virginia");
        Individual mother = new IndividualFactory().create(g, "Theresa", "Guliani", Sex.FEMALE, new Date(68, Calendar.SEPTEMBER,
                15), "Idaho", null, null);
        Individual kid1 = new IndividualFactory().create(g, "Bernardo", "Tarantino", Sex.MALE, new Date(93, Calendar.APRIL, 5),
                "Idaho", null, null);
        Individual kid2 = new IndividualFactory().create(g, "Angela", "Tarantino", Sex.MALE, new Date(95, Calendar.DECEMBER, 8),
                "Idaho", null, null);

        Family f = new FamilyFactory().create(g, father, mother, kid1, kid2);

        assertNotNull(f);
        assertNotNull(f.getXref());

        Family f2 = g.getFamilies().get(f.getXref());
        assertSame(f2, f);

        assertSame(father, f.getHusband().getIndividual());
        assertSame(mother, f.getWife().getIndividual());
        assertTrue(f.getChildren().contains(new IndividualReference(kid1)));
        assertTrue(f.getChildren().contains(new IndividualReference(kid2)));
        assertEquals(2, f.getChildren().size());

        assertSame(father.getFamiliesWhereSpouse().get(0).getFamily(), f);
        assertSame(mother.getFamiliesWhereSpouse().get(0).getFamily(), f);
        assertSame(kid1.getFamiliesWhereChild().get(0).getFamily(), f);
        assertSame(kid2.getFamiliesWhereChild().get(0).getFamily(), f);
    }

}
