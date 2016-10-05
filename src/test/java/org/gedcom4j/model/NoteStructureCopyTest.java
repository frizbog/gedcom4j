package org.gedcom4j.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

/**
 * Test copy constructor for {@link NoteStructure}
 * 
 * @author frizbog
 */
public class NoteStructureCopyTest extends AbstractCopyTest {

    /**
     * Test with lines (an inline note)
     */
    @Test
    public void testLines() {
        NoteStructure orig = new NoteStructure();
        orig.getLines(true).add("Line 1");
        orig.getLines(true).add("Line 2");
        orig.getLines(true).add("Line 3");

        orig.getCustomFacts(true).add(getTestCustomFact());

        // Copy and compare
        NoteStructure copy = new NoteStructure(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);

        assertEquals(orig.toString(), copy.toString());

        orig.getLines().set(0, "0th Line");
        assertEquals("Copy should not change when original does", "Line 1", copy.getLines().get(0));

        orig.getCustomFacts().clear();
        assertEquals("Copy should not change when original does", "_HOWDY", copy.getCustomFacts().get(0).getTag());
    }

    /**
     * Test with lines (an inline note)
     */
    @Test
    public void testNoteRecordReference() {
        NoteStructure orig = new NoteStructure();
        orig.setNoteReference(new NoteRecord("@N0@"));
        orig.getCustomFacts(true).add(getTestCustomFact());

        // Copy and compare
        NoteStructure copy = new NoteStructure(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);

        assertEquals(orig.toString(), copy.toString());

        orig.setNoteReference(new NoteRecord("@N9999999@"));
        assertEquals("Copy should not change when original does", "@N0@", copy.getNoteReference().getXref());

        orig.getCustomFacts().clear();
        assertEquals("Copy should not change when original does", "_HOWDY", copy.getCustomFacts().get(0).getTag());
    }

    /**
     * Test copying a null object
     */
    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testNull() {
        new NoteStructure(null);
    }

    /**
     * Test the simplest possible scenario - copy a new default {@link NoteStructure}
     */
    @Test
    public void testSimplestPossible() {
        NoteStructure orig = new NoteStructure();
        NoteStructure copy = new NoteStructure(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
    }

}
