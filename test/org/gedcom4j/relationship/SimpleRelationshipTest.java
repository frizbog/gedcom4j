/*
 * Copyright (c) 2009-2014 Matthew R. Harrah
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
        sr1.individual1 = new Individual();
        sr2.individual1 = new Individual();
        sr1.individual2 = new Individual();
        sr2.individual2 = new Individual();
        PersonalName n = new PersonalName();
        n.basic = "Bill";
        sr1.individual1.names.add(n);
        n = new PersonalName();
        n.basic = "Sam";
        sr1.individual2.names.add(n);
        n = new PersonalName();
        n.basic = "Bill";
        sr2.individual1.names.add(n);
        n = new PersonalName();
        n.basic = "Sam";
        sr2.individual2.names.add(n);

        sr1.name = RelationshipName.FATHER;
        sr1.reverseName = RelationshipName.SON;
        sr2.name = RelationshipName.FATHER;
        sr2.reverseName = RelationshipName.SON;

    }

    /**
     * Test method for
     * {@link org.gedcom4j.relationship.SimpleRelationship#equals(java.lang.Object)}
     * .
     */
    @Test
    public void testEqualsObject() {
        assertEquals(sr1, sr2);

        // Perturb the relationship names
        sr1.name = RelationshipName.CHILD;
        assertFalse(sr1.equals(sr2));
        sr2.name = RelationshipName.CHILD;
        assertEquals(sr1, sr2);

        // Perturb the relationship reverse names
        sr1.reverseName = RelationshipName.BROTHER;
        assertFalse(sr1.equals(sr2));
        sr2.reverseName = RelationshipName.BROTHER;
        assertEquals(sr1, sr2);

        // Perturb the individuals
        sr1.individual1.changeDate = new ChangeDate();
        assertFalse(sr1.equals(sr2));
        sr2.individual1.changeDate = new ChangeDate();
        assertEquals(sr1, sr2);

        sr1.individual1 = null;
        assertFalse(sr1.equals(sr2));
        sr2.individual1 = null;
        assertEquals(sr1, sr2);

        sr1.individual2 = null;
        assertFalse(sr1.equals(sr2));
        sr2.individual2 = null;
        assertEquals(sr1.hashCode(), sr2.hashCode());
    }

    /**
     * Test method for
     * {@link org.gedcom4j.relationship.SimpleRelationship#hashCode()}.
     */
    @Test
    public void testHashCode() {
        assertEquals(sr1.hashCode(), sr2.hashCode());

        // Perturb the relationship names
        sr1.name = RelationshipName.CHILD;
        assertFalse(sr1.hashCode() == sr2.hashCode());
        sr2.name = RelationshipName.CHILD;
        assertEquals(sr1.hashCode(), sr2.hashCode());

        // Perturb the relationship reverse names
        sr1.reverseName = RelationshipName.BROTHER;
        assertFalse(sr1.hashCode() == sr2.hashCode());
        sr2.reverseName = RelationshipName.BROTHER;
        assertEquals(sr1.hashCode(), sr2.hashCode());

        // Perturb the individuals
        sr1.individual1.changeDate = new ChangeDate();
        assertFalse(sr1.hashCode() == sr2.hashCode());
        sr2.individual1.changeDate = new ChangeDate();
        assertEquals(sr1.hashCode(), sr2.hashCode());

        sr1.individual1 = null;
        assertFalse(sr1.hashCode() == sr2.hashCode());
        sr2.individual1 = null;
        assertEquals(sr1.hashCode(), sr2.hashCode());

        sr1.individual2 = null;
        assertFalse(sr1.hashCode() == sr2.hashCode());
        sr2.individual2 = null;
        assertEquals(sr1.hashCode(), sr2.hashCode());
    }

    /**
     * Test method for
     * {@link org.gedcom4j.relationship.SimpleRelationship#toString()}.
     */
    @Test
    public void testToString() {
        assertEquals("Bill's FATHER Sam", sr1.toString());

        // Perturb the data
        sr1.name = RelationshipName.FIRST_COUSIN;
        sr1.reverseName = RelationshipName.FIRST_COUSIN;
        sr1.individual1.changeDate = new ChangeDate();

        assertEquals("Bill's FIRST_COUSIN Sam", sr1.toString());

        sr1.generationsRemoved = 1;
        assertEquals("Bill's FIRST_COUSIN_ONCE_REMOVED Sam", sr1.toString());

        sr1.generationsRemoved = 2;
        assertEquals("Bill's FIRST_COUSIN_TWICE_REMOVED Sam", sr1.toString());

        sr1.generationsRemoved = 3;
        assertEquals("Bill's FIRST_COUSIN_3X_REMOVED Sam", sr1.toString());

        sr1.generationsRemoved = 4;
        assertEquals("Bill's FIRST_COUSIN_4X_REMOVED Sam", sr1.toString());
    }

}
