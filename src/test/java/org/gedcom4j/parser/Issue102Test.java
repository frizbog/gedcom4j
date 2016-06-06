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
package org.gedcom4j.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.gedcom4j.model.StringTree;
import org.gedcom4j.model.Submitter;
import org.junit.Test;

/**
 * Test for issue 102, where it was reported that gedcom4j only supported single-digit line levels, and the spec allows
 * for numbers up to 99.
 * 
 * @author frizbog
 *
 */
public class Issue102Test {

    /**
     * The maximum number of levels of custom tags. Since the custom tags start at level 2, and the spec allows 99
     * levels, the max depth we can have here is 97
     */
    private static final int MAX_DEPTH = 97;

    /**
     * Test for a file with 100 levels (non-compliant with spec) and with strict line breaks turned off. In this case,
     * the line beginning with the three digit number should be treated as a continuation of the previous line, and a
     * warning issued.
     * 
     * @throws IOException
     *             if the file cannot be read
     * @throws GedcomParserException
     *             if the file cannot be parsed
     * 
     */
    @Test
    public void test100LevelsRelaxed() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.strictLineBreaks = false;
        gp.load("sample/issue102_100levels.ged");
        assertEquals(0, gp.errors.size());
        assertEquals(1, gp.warnings.size());
        assertEquals("Line 108 did not begin with a level and tag, so it was treated as a non-standard continuation of the previous line.", gp.warnings.get(0));
    }

    /**
     * Test for a file with 100 levels (non-compliant with spec) and with strict line breaks turned on. In this case, a
     * {@link GedcomParserException} is expected
     * 
     * @throws IOException
     *             if the file cannot be read
     * @throws GedcomParserException
     *             if the file cannot be parsed
     * 
     */
    @Test
    public void test100LevelsStrict() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.strictLineBreaks = true;
        try {
            gp.load("sample/issue102_100levels.ged");
            fail("Expected a " + GedcomParserException.class.getName());
        } catch (GedcomParserException e) {
            assertEquals("Line 108 does not begin with a 1 or 2 digit number for the level followed by a space: "
                    + "100 _CUST Custom tag to test three-digit levels.  This line should cause a failure.", e.getMessage());
        }
    }

    /**
     * Test for a file with 99 levels - should load ok.
     * 
     * @throws IOException
     *             if the file cannot be read
     * @throws GedcomParserException
     *             if the file cannot be parsed
     * 
     */
    @Test
    public void test99Levels() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/issue102_99levels.ged");
        assertTrue(gp.errors.isEmpty());
        assertTrue(gp.warnings.isEmpty());
        Submitter submitter = gp.gedcom.submitters.get("@SUBM001@");
        assertNotNull(submitter);
        assertNotNull(submitter.name);
        List<StringTree> customTags = submitter.name.customTags;
        assertNotNull(customTags);
        assertEquals(1, customTags.size());
        assertCustomTagRecursively(customTags.get(0), MAX_DEPTH);
    }

    /**
     * Recursively assert that there is a single custom tag, as expected, up to a maximum depth
     * 
     * @param customTags
     *            the StringTree that has the custom tags
     * @param remaining
     *            the number of times left to recurse
     */
    private void assertCustomTagRecursively(StringTree customTags, int remaining) {
        assertEquals(99 - remaining, customTags.level);
        if (remaining <= 0) {
            return;
        }
        assertNotNull("With " + remaining + " levels remaining, customTags was null", customTags);
        assertNotNull("With " + remaining + " levels remaining, customTags had no children", customTags.children);
        assertEquals("With " + remaining + " levels remaining, customTags did not have exactly one child. ", 1, customTags.children.size());
        StringTree newCustomTags = customTags.children.get(0);
        assertNotNull(newCustomTags);
        assertCustomTagRecursively(newCustomTags, remaining - 1);
    }
}
