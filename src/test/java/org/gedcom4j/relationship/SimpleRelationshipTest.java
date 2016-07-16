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
import static org.junit.Assert.assertFalse;

import org.gedcom4j.model.ChangeDate;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.PersonalName;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link SimpleRelationship}
 * 
 * @author frizbog1
 * 
 */
public class SimpleRelationshipTest {

    /**
     * Simple relationship 1 - test fixture
     */
    private SimpleRelationship sr1;

    /**
     * Simple relationship 2 - test fixture
     */
    private SimpleRelationship sr2;

    /**
     * Set up test fixtures
     */
    @Before
    public void setUp() {
        sr1 = new SimpleRelationship();
        sr2 = new SimpleRelationship();
        sr1.setIndividual1(new Individual());
        sr2.setIndividual1(new Individual());
        sr1.setIndividual2(new Individual());
        sr2.setIndividual2(new Individual());
        PersonalName n = new PersonalName();
        n.setBasic("Bill");
        sr1.getIndividual1().getNames(true).add(n);
        n = new PersonalName();
        n.setBasic("Sam");
        sr1.getIndividual2().getNames(true).add(n);
        n = new PersonalName();
        n.setBasic("Bill");
        sr2.getIndividual1().getNames(true).add(n);
        n = new PersonalName();
        n.setBasic("Sam");
        sr2.getIndividual2().getNames(true).add(n);

        sr1.setName(RelationshipName.FATHER);
        sr1.setReverseName(RelationshipName.SON);
        sr2.setName(RelationshipName.FATHER);
        sr2.setReverseName(RelationshipName.SON);

    }

    /**
     * Test method for {@link org.gedcom4j.relationship.SimpleRelationship#equals(java.lang.Object)} .
     */
    @Test
    public void testEqualsObject() {
        assertEquals(sr1, sr2);

        // Perturb the relationship names
        sr1.setName(RelationshipName.CHILD);
        assertFalse(sr1.equals(sr2));
        sr2.setName(RelationshipName.CHILD);
        assertEquals(sr1, sr2);

        // Perturb the relationship reverse names
        sr1.setReverseName(RelationshipName.BROTHER);
        assertFalse(sr1.equals(sr2));
        sr2.setReverseName(RelationshipName.BROTHER);
        assertEquals(sr1, sr2);

        // Perturb the individuals
        sr1.getIndividual1().setChangeDate(new ChangeDate());
        assertFalse(sr1.equals(sr2));
        sr2.getIndividual1().setChangeDate(new ChangeDate());
        assertEquals(sr1, sr2);

        sr1.setIndividual1(null);
        assertFalse(sr1.equals(sr2));
        sr2.setIndividual1(null);
        assertEquals(sr1, sr2);

        sr1.setIndividual2(null);
        assertFalse(sr1.equals(sr2));
        sr2.setIndividual2(null);
        assertEquals(sr1.hashCode(), sr2.hashCode());
    }

    /**
     * Test method for {@link org.gedcom4j.relationship.SimpleRelationship#hashCode()}.
     */
    @Test
    public void testHashCode() {
        assertEquals(sr1.hashCode(), sr2.hashCode());

        // Perturb the relationship names
        sr1.setName(RelationshipName.CHILD);
        assertFalse(sr1.hashCode() == sr2.hashCode());
        sr2.setName(RelationshipName.CHILD);
        assertEquals(sr1.hashCode(), sr2.hashCode());

        // Perturb the relationship reverse names
        sr1.setReverseName(RelationshipName.BROTHER);
        assertFalse(sr1.hashCode() == sr2.hashCode());
        sr2.setReverseName(RelationshipName.BROTHER);
        assertEquals(sr1.hashCode(), sr2.hashCode());

        // Perturb the individuals
        sr1.getIndividual1().setChangeDate(new ChangeDate());
        assertFalse(sr1.hashCode() == sr2.hashCode());
        sr2.getIndividual1().setChangeDate(new ChangeDate());
        assertEquals(sr1.hashCode(), sr2.hashCode());

        sr1.setIndividual1(null);
        assertFalse(sr1.hashCode() == sr2.hashCode());
        sr2.setIndividual1(null);
        assertEquals(sr1.hashCode(), sr2.hashCode());

        sr1.setIndividual2(null);
        assertFalse(sr1.hashCode() == sr2.hashCode());
        sr2.setIndividual2(null);
        assertEquals(sr1.hashCode(), sr2.hashCode());
    }

    /**
     * Test method for {@link org.gedcom4j.relationship.SimpleRelationship#toString()}.
     */
    @Test
    public void testToString() {
        assertEquals("Bill's FATHER Sam", sr1.toString());

        // Perturb the data
        sr1.setName(RelationshipName.FIRST_COUSIN);
        sr1.setReverseName(RelationshipName.FIRST_COUSIN);
        sr1.getIndividual1().setChangeDate(new ChangeDate());

        assertEquals("Bill's FIRST_COUSIN Sam", sr1.toString());

        sr1.setGenerationsRemoved(1);
        assertEquals("Bill's FIRST_COUSIN_ONCE_REMOVED Sam", sr1.toString());

        sr1.setGenerationsRemoved(2);
        assertEquals("Bill's FIRST_COUSIN_TWICE_REMOVED Sam", sr1.toString());

        sr1.setGenerationsRemoved(3);
        assertEquals("Bill's FIRST_COUSIN_3X_REMOVED Sam", sr1.toString());

        sr1.setGenerationsRemoved(4);
        assertEquals("Bill's FIRST_COUSIN_4X_REMOVED Sam", sr1.toString());
    }

}
