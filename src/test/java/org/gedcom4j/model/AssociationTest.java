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

import org.junit.Test;

/**
 * @author frizbog1
 * 
 */
public class AssociationTest {

    /**
     * Test method for {@link org.gedcom4j.model.Association#equals(java.lang.Object)} .
     */
    @Test
    @SuppressWarnings("PMD.EqualsNull")
    public void testEqualsObject() {
        Association a1 = new Association();
        assertEquals(a1, a1);

        Association a2 = new Association();
        assertEquals(a1, a2);

        a1.setAssociatedEntityType("Frying Pan");
        assertFalse(a1.equals(a2));
        a2.setAssociatedEntityType("Frying Pan");
        assertEquals(a1, a2);
        a1.setAssociatedEntityType((String) null);
        assertFalse(a1.equals(a2));
        a2.setAssociatedEntityType((String) null);
        assertEquals(a1, a2);

        a1.setAssociatedEntityXref("Frying Pan");
        assertFalse(a1.equals(a2));
        a2.setAssociatedEntityXref("Frying Pan");
        assertEquals(a1, a2);
        a1.setAssociatedEntityXref(null);
        assertFalse(a1.equals(a2));
        a2.setAssociatedEntityXref(null);
        assertEquals(a1, a2);

        a1.setRelationship("Frying Pan");
        assertFalse(a1.equals(a2));
        a2.setRelationship("Frying Pan");
        assertEquals(a1, a2);
        a1.setRelationship((String) null);
        assertFalse(a1.equals(a2));
        a2.setRelationship((String) null);
        assertEquals(a1, a2);

        a1.getCitations(true).add(new CitationWithSource());
        assertFalse(a1.equals(a2));
        a2.getCitations(true).add(new CitationWithSource());
        assertEquals(a1, a2);
        a1.getCitations().clear();
        assertFalse(a1.equals(a2));
        a2.getCitations().clear();
        assertEquals(a1, a2);

        a1.getNoteStructures(true).add(new NoteStructure());
        assertFalse(a1.equals(a2));
        a2.getNoteStructures(true).add(new NoteStructure());
        assertEquals(a1, a2);
        a1.getNoteStructures().clear();
        assertFalse(a1.equals(a2));
        a2.getNoteStructures().clear();
        assertEquals(a1, a2);

        assertFalse(a1.equals(null));
        assertFalse(a1.equals(this));
    }

    /**
     * Test method for {@link org.gedcom4j.model.Association#hashCode()}.
     */
    @Test
    public void testHashCode() {

        Association a1 = new Association();
        Association a2 = new Association();
        assertEquals(a1, a2);

        a1.setAssociatedEntityType("Frying Pan");
        assertFalse(a1.hashCode() == a2.hashCode());
        a2.setAssociatedEntityType("Frying Pan");
        assertEquals(a1.hashCode(), a2.hashCode());
        a1.setAssociatedEntityType((String) null);
        assertFalse(a1.hashCode() == a2.hashCode());
        a2.setAssociatedEntityType((String) null);
        assertEquals(a1.hashCode(), a2.hashCode());

        a1.setAssociatedEntityXref("Frying Pan");
        assertFalse(a1.hashCode() == a2.hashCode());
        a2.setAssociatedEntityXref("Frying Pan");
        assertEquals(a1.hashCode(), a2.hashCode());
        a1.setAssociatedEntityXref(null);
        assertFalse(a1.hashCode() == a2.hashCode());
        a2.setAssociatedEntityXref(null);
        assertEquals(a1.hashCode(), a2.hashCode());

        a1.setRelationship("Frying Pan");
        assertFalse(a1.hashCode() == a2.hashCode());
        a2.setRelationship("Frying Pan");
        assertEquals(a1.hashCode(), a2.hashCode());
        a1.setRelationship((String) null);
        assertFalse(a1.hashCode() == a2.hashCode());
        a2.setRelationship((String) null);
        assertEquals(a1.hashCode(), a2.hashCode());

        a1.getCitations(true).add(new CitationWithSource());
        assertFalse(a1.hashCode() == a2.hashCode());
        a2.getCitations(true).add(new CitationWithSource());
        assertEquals(a1.hashCode(), a2.hashCode());
        a1.getCitations().clear();
        assertFalse(a1.hashCode() == a2.hashCode());
        a2.getCitations().clear();
        assertEquals(a1.hashCode(), a2.hashCode());

        a1.getNoteStructures(true).add(new NoteStructure());
        assertFalse(a1.hashCode() == a2.hashCode());
        a2.getNoteStructures(true).add(new NoteStructure());
        assertEquals(a1.hashCode(), a2.hashCode());
        a1.getNoteStructures().clear();
        assertFalse(a1.hashCode() == a2.hashCode());
        a2.getNoteStructures().clear();
        assertEquals(a1.hashCode(), a2.hashCode());

        assertFalse(a1.hashCode() == hashCode());
    }

}
