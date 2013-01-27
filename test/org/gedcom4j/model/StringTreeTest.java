package org.gedcom4j.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

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

}
