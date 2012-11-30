package com.mattharrah.gedcom4j.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for the individual event type
 * 
 * @author frizbog1
 * 
 */
public class IndividualEventTypeTest {

    /**
     * Test for {@link IndividualEventType#getFromTag(String)}
     */
    @Test
    public void testGetFromTag() {
        Assert.assertSame(IndividualEventType.PROBATE,
                IndividualEventType.getFromTag("PROB"));
        Assert.assertNull(IndividualEventType.getFromTag(""));
        Assert.assertNull(IndividualEventType.getFromTag(null));
    }

    /**
     * Test for {@link IndividualEventType#isValidTag(String)}
     */
    @Test
    public void testIsValidTag() {
        Assert.assertTrue(IndividualEventType.isValidTag("BAPM"));
        Assert.assertFalse("Baptism is BAPM, not BAPT like you might expect",
                IndividualEventType.isValidTag("BAPT"));
    }
}
