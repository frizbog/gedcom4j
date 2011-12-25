package com.mattharrah.gedcom4j;

import junit.framework.TestCase;

/**
 * Tests for {@link FamilyEventType}
 * 
 * @author frizbog1
 */
public class FamilyEventTypeTest extends TestCase {

    /**
     * Test for {@link FamilyEventType#getFromTag(String)}
     */
    public void testGetFromTag() {
        assertSame(FamilyEventType.ANNULMENT,
                FamilyEventType.getFromTag("ANUL"));
        assertSame(FamilyEventType.CENSUS, FamilyEventType.getFromTag("CENS"));
        assertSame(FamilyEventType.DIVORCE, FamilyEventType.getFromTag("DIV"));
        assertSame(FamilyEventType.DIVORCE_FILED,
                FamilyEventType.getFromTag("DIVF"));
        assertSame(FamilyEventType.ENGAGEMENT,
                FamilyEventType.getFromTag("ENGA"));
        assertSame(FamilyEventType.MARRIAGE, FamilyEventType.getFromTag("MARR"));
        assertSame(FamilyEventType.MARRIAGE_BANNER,
                FamilyEventType.getFromTag("MARB"));
        assertSame(FamilyEventType.MARRIAGE_CONTRACT,
                FamilyEventType.getFromTag("MARC"));
        assertSame(FamilyEventType.MARRIAGE_LICENSE,
                FamilyEventType.getFromTag("MARL"));
        assertSame(FamilyEventType.MARRIAGE_SETTLEMENT,
                FamilyEventType.getFromTag("MARS"));
        assertSame(FamilyEventType.EVENT, FamilyEventType.getFromTag("EVEN"));
        assertNull(FamilyEventType.getFromTag("BAD"));
        assertNull(FamilyEventType.getFromTag(null));
        assertNull(FamilyEventType.getFromTag(""));
    }

    /**
     * Test for {@link FamilyEventType#isValidTag(String)}
     */
    public void testIsValidTag() {
        assertTrue(FamilyEventType.isValidTag("ANUL"));
        assertTrue(FamilyEventType.isValidTag("CENS"));
        assertTrue(FamilyEventType.isValidTag("DIV"));
        assertTrue(FamilyEventType.isValidTag("ENGA"));
        assertTrue(FamilyEventType.isValidTag("MARR"));
        assertTrue(FamilyEventType.isValidTag("MARB"));
        assertTrue(FamilyEventType.isValidTag("MARC"));
        assertTrue(FamilyEventType.isValidTag("MARL"));
        assertTrue(FamilyEventType.isValidTag("MARS"));
        assertTrue(FamilyEventType.isValidTag("EVEN"));
        assertFalse(FamilyEventType.isValidTag("BAD"));
        assertFalse(FamilyEventType.isValidTag(null));
        assertFalse(FamilyEventType.isValidTag(""));
    }
}
