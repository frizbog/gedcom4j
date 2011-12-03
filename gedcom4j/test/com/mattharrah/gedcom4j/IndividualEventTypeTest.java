package com.mattharrah.gedcom4j;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IndividualEventTypeTest {

	/**
	 * Test for {@link IndividualEventType#getFromTag(String)}
	 */
	@Test
	public void testGetFromTag() {
		assertSame(IndividualEventType.PROBATE,
		        IndividualEventType.getFromTag("PROB"));
		assertNull(IndividualEventType.getFromTag(""));
		assertNull(IndividualEventType.getFromTag(null));
	}

	/**
	 * Test for {@link IndividualEventType#isValidTag(String)}
	 */
	@Test
	public void testIsValidTag() {
		assertTrue(IndividualEventType.isValidTag("BAPM"));
		assertFalse("Baptism is BAPM, not BAPT like you might expect",
		        IndividualEventType.isValidTag("BAPT"));
	}
}
