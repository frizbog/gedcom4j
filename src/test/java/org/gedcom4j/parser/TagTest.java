package org.gedcom4j.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test for {@link Tag}
 * 
 * @author frizbog
 */
public class TagTest {

    /**
     * The expected number of items in the {@link Tag} enum
     */
    private static final int EXPECTED_TAG_COUNT = 92;

    /**
     * Test for {@link Tag#equals(Object)}
     */
    @Test
    public void testEquals() {
        assertTrue(Tag.ABBREVIATION.equals("ABBR"));
        assertTrue(Tag.SUBMISSION.equals("SUBN"));
        assertTrue(Tag.TRAILER.equals("TRLR"));
        assertFalse(Tag.BLOB.equals("FRYINGPAN"));
        assertFalse(Tag.HEADER.equals(null));
    }

    /**
     * Test of tag count
     */
    @Test
    public void testTagCount() {
        assertEquals(EXPECTED_TAG_COUNT, Tag.values().length);
    }

    /**
     * Test of inner value and {@link Tag#toString()}
     */
    @Test
    public void testTagStrings() {
        for (Tag t : Tag.values()) {
            assertNotNull(t);
            assertNotNull(t.tagText);
            assertEquals(t.tagText, t.toString());
        }
    }

    /**
     * Test for {@link Tag#toString()}
     */
    @Test
    public void testToString() {
        assertEquals("FAMS", Tag.FAMILY_WHERE_SPOUSE.toString());
        assertEquals("INDI", Tag.INDIVIDUAL.toString());
    }

    /**
     * Negative test for {@link Tag#valueOf(String)}
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValueOfNegative() {
        Tag.valueOf("I_AM_NOT_DEFINED");
    }

    /**
     * Positive test for {@link Tag#valueOf(String)}
     */
    @Test
    public void testValueOfPositive() {
        assertEquals(Tag.ADDRESS, Tag.valueOf("ADDRESS"));
        assertEquals(Tag.OBJECT_MULTIMEDIA, Tag.valueOf("OBJECT_MULTIMEDIA"));
    }
}
