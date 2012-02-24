package com.mattharrah.gedcom4j.validate;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link GedcomValidationFinding}
 * 
 * @author frizbog1
 * 
 */
public class GedcomValidationFindingTest {

    /**
     * Test for
     * {@link GedcomValidationFinding#GedcomValidationFinding(String, Severity, Object)}
     * - nulls as parameters
     */
    @Test
    public void testGedcomValidationFinding1() {
        GedcomValidationFinding gvf = new GedcomValidationFinding(null, null,
                this);
        Assert.assertNotNull(gvf);
        Assert.assertNull(gvf.problemDescription);
        Assert.assertNull(gvf.severity);
        Assert.assertSame(this, gvf.itemWithProblem);
    }

    /**
     * Test for
     * {@link GedcomValidationFinding#GedcomValidationFinding(String, Severity, Object)}
     * - values for parameters
     */
    @Test
    public void testGedcomValidationFinding2() {
        GedcomValidationFinding gvf = new GedcomValidationFinding(
                "testing 1 2 3", Severity.ERROR, this);
        Assert.assertNotNull(gvf);
        Assert.assertEquals("testing 1 2 3", gvf.problemDescription);
        Assert.assertEquals(Severity.ERROR, gvf.severity);
        Assert.assertSame(this, gvf.itemWithProblem);
    }

    /**
     * Test for {@link GedcomValidationFinding#toString()}
     */
    @Test
    public void testToString() {
        GedcomValidationFinding gvf = new GedcomValidationFinding(
                "testing 1 2 3", Severity.ERROR, Integer.valueOf(4));
        Assert.assertEquals("ERROR: testing 1 2 3 (4)", gvf.toString());
    }

}
