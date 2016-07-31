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
import static org.junit.Assert.assertTrue;

import org.gedcom4j.model.Individual;
import org.junit.Test;

/**
 * Test for {@link Relationship}
 * 
 * @author frizbog1
 * 
 */
public class RelationshipTest {

    /**
     * Test equals and hashcode after perturbing the chain property
     */
    @Test
    public void testChain() {
        Relationship r1 = new Relationship();
        Relationship r2 = new Relationship();
        r1.getChain().add(new SimpleRelationship());
        assertFalse(r1.equals(r2));
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.getChain().add(new SimpleRelationship());
        assertTrue(r1.equals(r2));
        assertTrue(r1.hashCode() == r2.hashCode());
    }

    /**
     * Test equals and hashcode after perturbing the individual1 property
     */
    @Test
    public void testIndividual1() {
        Relationship r1 = new Relationship();
        Relationship r2 = new Relationship();
        r1.setIndividual1(new Individual());
        assertFalse(r1.equals(r2));
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.setIndividual1(new Individual());
        assertTrue(r1.equals(r2));
        assertTrue(r1.hashCode() == r2.hashCode());
    }

    /**
     * Test equals and hashcode after perturbing the individual2 property
     */
    @Test
    public void testIndividual2() {
        Relationship r1 = new Relationship();
        Relationship r2 = new Relationship();
        r1.setIndividual2(new Individual());
        assertFalse(r1.equals(r2));
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.setIndividual2(new Individual());
        assertTrue(r1.equals(r2));
        assertTrue(r1.hashCode() == r2.hashCode());
    }

    /**
     * Simple test
     */
    @Test
    public void testSimple() {
        Relationship r1 = new Relationship();
        Relationship r2 = new Relationship();
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    /**
     * Test for {@link Relationship#toString()}
     */
    @Test
    public void testToString() {
        Relationship r = new Relationship();
        assertEquals("[], 0 step(s)", r.toString());

        r.setIndividual1(new Individual());
        r.setIndividual2(new Individual());
        SimpleRelationship sr = new SimpleRelationship();
        sr.setName(RelationshipName.FATHER);
        sr.setReverseName(RelationshipName.SON);
        sr.setIndividual1(r.getIndividual1());
        sr.setIndividual2(r.getIndividual2());
        r.getChain().add(sr);

        assertEquals("[Unknown's FATHER Unknown], 1 step(s)", r.toString());
    }

}
