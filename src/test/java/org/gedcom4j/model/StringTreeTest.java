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
package org.gedcom4j.model;

import static org.junit.Assert.*;

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.parser.GedcomParser;
import org.junit.Test;

/**
 * Test for {@link StringTree}
 * 
 * @author frizbog
 * 
 */
public class StringTreeTest {
    /**
     * TEst for {@link StringTree#equals(Object)}
     */
    @Test
    public void testEquals() {
        StringTree st1 = new StringTree();
        StringTree st2 = new StringTree();

        assertNotSame(st1, st2);
        assertEquals(st1, st2);

        st1.children.add(new StringTree());
        assertFalse(st1.equals(st2));
        st2.children.add(new StringTree());
        assertEquals(st1, st2);

        st1.level = 1;
        assertFalse(st1.equals(st2));
        st2.level = 1;
        assertEquals(st1, st2);

        st1.id = "Frying Pan";
        assertFalse(st1.equals(st2));
        st2.id = "Frying Pan";
        assertEquals(st1, st2);

        st1.lineNum = 2;
        assertFalse(st1.equals(st2));
        st2.lineNum = 2;
        assertEquals(st1, st2);

        st1.value = "Test";
        assertFalse(st1.equals(st2));
        st2.value = "Test";
        assertEquals(st1, st2);
    }

    /**
     * TEst for {@link StringTree#hashCode()}
     */
    @Test
    public void testHashCode() {
        StringTree st1 = new StringTree();
        StringTree st2 = new StringTree();

        assertNotSame(st1, st2);
        assertEquals(st1.hashCode(), st2.hashCode());

        st1.children.add(new StringTree());
        assertTrue(st1.hashCode() != st2.hashCode());
        st2.children.add(new StringTree());
        assertEquals(st1.hashCode(), st2.hashCode());

        st1.level = 1;
        assertTrue(st1.hashCode() != st2.hashCode());
        st2.level = 1;
        assertEquals(st1.hashCode(), st2.hashCode());

        st1.id = "Frying Pan";
        assertTrue(st1.hashCode() != st2.hashCode());
        st2.id = "Frying Pan";
        assertEquals(st1.hashCode(), st2.hashCode());

        st1.lineNum = 2;
        assertTrue(st1.hashCode() != st2.hashCode());
        st2.lineNum = 2;
        assertEquals(st1.hashCode(), st2.hashCode());

        st1.value = "Test";
        assertTrue(st1.hashCode() != st2.hashCode());
        st2.value = "Test";
        assertEquals(st1.hashCode(), st2.hashCode());
    }

    /**
     * TEst for {@link StringTree#toString()}
     */
    @Test
    public void testToString() {
        StringTree st = new StringTree();
        assertEquals("Line 0: 0 null null", st.toString());

        st.children.add(new StringTree());
        st.level = 1;
        st.id = "Frying Pan";
        st.lineNum = 2;
        st.value = "Test";
        assertEquals("Line 2: 1 Frying Pan null Test\nLine 0: 0 null null", st.toString());
    }

    /**
     * Test to try and replicate reported <a href="https://github.com/frizbog/gedcom4j/issues/60">issue 60</a>. Loads a
     * large GEDCOM file, and invokes toString() and hashCode() on loads of StringTree objects in it.
     * 
     * @throws GedcomParserException
     * @throws IOException
     */
    @Test
    public void testToStringBigFile() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/5.5.1 sample 1.ged");
        Gedcom g = gp.gedcom;

        for (Family f : g.getFamilies().values()) {
            assertNotNull(f.getCustomTags().toString());
            assertFalse(0 == f.getCustomTags().hashCode());

            assertNotNull(f.toString());
            assertFalse(0 == f.hashCode());
        }
        for (Individual i : g.getIndividuals().values()) {
            assertNotNull(i.getCustomTags().toString());
            assertFalse(0 == i.getCustomTags().hashCode());

            assertNotNull(i.toString());
            assertFalse(0 == i.hashCode());
        }
        for (Multimedia m : g.getMultimedia().values()) {
            assertNotNull(m.getCustomTags().toString());
            assertFalse(0 == m.getCustomTags().hashCode());

            assertNotNull(m.toString());
            assertFalse(0 == m.hashCode());
        }
        for (Note n : g.getNotes().values()) {
            assertNotNull(n.getCustomTags().toString());
            assertFalse(0 == n.getCustomTags().hashCode());

            assertNotNull(n.toString());
            assertFalse(0 == n.hashCode());
        }
    }

}
