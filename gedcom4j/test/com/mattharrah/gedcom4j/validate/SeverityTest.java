package com.mattharrah.gedcom4j.validate;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link Severity}
 * 
 * @author frizbog1
 * 
 */
public class SeverityTest {

    /**
     * Test for {@link Severity}
     */
    @Test
    public void testEnum() {
        Assert.assertEquals(Severity.valueOf("ERROR"), Severity.ERROR);
        Assert.assertEquals(Severity.valueOf("WARNING"), Severity.WARNING);
        Assert.assertEquals(Severity.valueOf("INFO"), Severity.INFO);
    }

}
