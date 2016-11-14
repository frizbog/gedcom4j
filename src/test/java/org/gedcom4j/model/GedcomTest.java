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
package org.gedcom4j.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.gedcom4j.Options;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.jcip.annotations.NotThreadSafe;

/**
 * @author frizbog1
 * 
 */
@NotThreadSafe
public class GedcomTest {

    /**
     * Reset options to defaults before testing
     */
    @Before
    public void setUp() {
        Options.resetToDefaults();
    }

    /**
     * Reset options to defaults after testing
     */
    @After
    public void tearDown() {
        Options.resetToDefaults();
    }

    /**
     * Test method for {@link org.gedcom4j.model.Gedcom#equals(java.lang.Object)}.
     */
    @Test
    @SuppressWarnings("PMD.EqualsNull")
    public void testEqualsObject() {
        Gedcom g1 = new Gedcom();
        assertEquals(g1, g1);
        Gedcom g2 = new Gedcom();
        assertEquals("objects are equal, so equals() should return true", g1, g2);
        g1.setTrailer(null);
        assertFalse("objects are no longer equal, so equals() should return false", g1.equals(g2));
        g2.setTrailer(null);
        assertEquals("objects are equal again, so equals() should return true again", g1, g2);
        g1.getIndividuals().put("FryingPan", new Individual());
        assertFalse("objects are no longer equal, so equals() should return false", g1.equals(g2));
        g2.getIndividuals().put("FryingPan", new Individual());
        assertEquals("objects are equal again, so equals() should return true again", g1, g2);
        assertFalse(g1.equals(null));
        assertFalse(g1.equals(this));
    }

    /**
     * Test method for {@link org.gedcom4j.model.Gedcom#equals(java.lang.Object)}.
     */
    @Test
    @SuppressWarnings("PMD.EqualsNull")
    public void testEqualsObject2() {
        Gedcom g1 = new Gedcom();
        assertEquals(g1, g1);
        assertFalse(g1.equals(new Header()));

        Gedcom g2 = new Gedcom();
        assertEquals(g1, g2);

        g1.getIndividuals().put("X", new Individual());
        assertFalse(g1.equals(g2));
        g2.getIndividuals().put("X", new Individual());
        assertEquals(g2, g2);

        g1.getFamilies().put("X", new Family());
        assertFalse(g1.equals(g2));
        g2.getFamilies().put("X", new Family());
        assertEquals(g2, g2);

        g1.getNotes().put("X", new NoteRecord("X"));
        assertFalse(g1.equals(g2));
        g2.getNotes().put("X", new NoteRecord("X"));
        assertEquals(g2, g2);

        g1.getMultimedia().put("X", new Multimedia());
        assertFalse(g1.equals(g2));
        g2.getMultimedia().put("X", new Multimedia());
        assertEquals(g2, g2);

        g1.getRepositories().put("X", new Repository());
        assertFalse(g1.equals(g2));
        g2.getRepositories().put("X", new Repository());
        assertEquals(g2, g2);

        g1.getSources().put("X", new Source());
        assertFalse(g1.equals(g2));
        g2.getSources().put("X", new Source());
        assertEquals(g2, g2);

        g1.getSubmitters().put("X", new Submitter());
        assertFalse(g1.equals(g2));
        g2.getSubmitters().put("X", new Submitter());
        assertEquals(g2, g2);

        g1.setSubmission(new Submission());
        assertFalse(g1.equals(g2));
        g2.setSubmission(new Submission());
        assertEquals(g2, g2);

        g2.setTrailer(null);
        assertFalse(g1.equals(g2));
        g1.setTrailer(null);
        assertEquals(g2, g2);
        g2.setTrailer(new Trailer());
        assertFalse(g1.equals(g2));
        g1.setTrailer(new Trailer());
        assertEquals(g2, g2);
    }

    /**
     * Test method for {@link org.gedcom4j.model.Gedcom#hashCode()}.
     */
    @Test
    public void testHashCode() {
        Gedcom g1 = new Gedcom();
        Gedcom g2 = new Gedcom();
        assertEquals("objects are equal, so hashcodes should equal", g1.hashCode(), g2.hashCode());
        g1.setTrailer(null);
        assertFalse("objects are no longer equal, so hashcodes should no longer equal", g1.hashCode() == g2.hashCode());
        g2.setTrailer(null);
        assertEquals("objects are equal again, so hashcodes should equal again", g1.hashCode(), g2.hashCode());
        g1.getIndividuals().put("FryingPan", new Individual());
        assertFalse("objects are no longer equal, so hashcodes should no longer equal", g1.hashCode() == g2.hashCode());
        g2.getIndividuals().put("FryingPan", new Individual());
        assertEquals("objects are equal again, so hashcodes should equal again", g1.hashCode(), g2.hashCode());
    }

    /**
     * Test for {@link Gedcom#hashCode()}
     */
    @Test
    public void testHashCode2() {
        Gedcom g1 = new Gedcom();
        Gedcom g2 = new Gedcom();
        assertEquals(g2.hashCode(), g2.hashCode());

        g1.getIndividuals().put("X", new Individual());
        assertFalse(g1.hashCode() == g2.hashCode());
        g2.getIndividuals().put("X", new Individual());
        assertEquals(g2.hashCode(), g2.hashCode());

        g1.getFamilies().put("X", new Family());
        assertFalse(g1.hashCode() == g2.hashCode());
        g2.getFamilies().put("X", new Family());
        assertEquals(g2.hashCode(), g2.hashCode());

        g1.getNotes().put("X", new NoteRecord("X"));
        assertFalse(g1.hashCode() == g2.hashCode());
        g2.getNotes().put("X", new NoteRecord("X"));
        assertEquals(g2.hashCode(), g2.hashCode());

        g1.getMultimedia().put("X", new Multimedia());
        assertFalse(g1.hashCode() == g2.hashCode());
        g2.getMultimedia().put("X", new Multimedia());
        assertEquals(g2.hashCode(), g2.hashCode());

        g1.getRepositories().put("X", new Repository());
        assertFalse(g1.hashCode() == g2.hashCode());
        g2.getRepositories().put("X", new Repository());
        assertEquals(g2.hashCode(), g2.hashCode());

        g1.getSources().put("X", new Source());
        assertFalse(g1.hashCode() == g2.hashCode());
        g2.getSources().put("X", new Source());
        assertEquals(g2.hashCode(), g2.hashCode());

        g1.getSubmitters().put("X", new Submitter());
        assertFalse(g1.hashCode() == g2.hashCode());
        g2.getSubmitters().put("X", new Submitter());
        assertEquals(g2.hashCode(), g2.hashCode());

        g1.setSubmission(new Submission());
        assertFalse(g1.hashCode() == g2.hashCode());
        g2.setSubmission(new Submission());
        assertEquals(g2.hashCode(), g2.hashCode());

        g2.setTrailer(null);
        assertFalse(g1.hashCode() == g2.hashCode());
        g1.setTrailer(null);
        assertEquals(g2.hashCode(), g2.hashCode());
        g2.setTrailer(new Trailer());
        assertFalse(g1.hashCode() == g2.hashCode());
        g1.setTrailer(new Trailer());
        assertEquals(g2.hashCode(), g2.hashCode());

    }

    /**
     * Maps should be initialized
     */
    @Test
    public void testInitialized() {
        Options.setCollectionInitializationEnabled(true);
        Gedcom g = new Gedcom();
        assertNotNull(g);
        assertNotNull(g.getFamilies());
        assertNotNull(g.getIndividuals());
        assertNotNull(g.getMultimedia());
        assertNotNull(g.getNotes());
        assertNotNull(g.getRepositories());
        assertNotNull(g.getSources());
        assertNotNull(g.getSubmitters());
    }
}
