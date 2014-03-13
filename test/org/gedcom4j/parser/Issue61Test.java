package org.gedcom4j.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.StringTree;
import org.junit.Test;

/**
 * Test for <a href="https://github.com/frizbog/gedcom4j/issues/61">issue 61</a> which allows unknown tags to be treated
 * as custom tags, as an option.
 * 
 * @author frizbog
 */
public class Issue61Test {

    /**
     * Test loading a file with bad tags using loose custom tag handling.
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if there's a parsing problem
     */
    @Test
    public void testIssue61Loose() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.strictCustomTags = false;
        gp.load("sample/Harry_Potter.ged");
        Gedcom g = gp.gedcom;
        assertNotNull(g);
        assertTrue(gp.errors.isEmpty());
        for (Individual i : g.individuals.values()) {
            assertNotNull(i);
            assertNotNull(i.customTags);
            assertFalse("Individual " + i + " has no custom tags", i.customTags.isEmpty());
            for (StringTree ct : i.customTags) {
                assertTrue("Custom tag should be WAND or MUGL, but is " + ct.value, "WAND".equals(ct.tag) || "MUGL".equals(ct.tag));
                if ("WAND".equals(ct.tag)) {
                    assertNotNull(ct.value);
                    assertFalse(ct.value.trim().isEmpty());
                }
                if ("MUGL".equals(ct.tag)) {
                    assertNull(ct.value);
                }
            }
        }
    }

    /**
     * Test loading a file with bad tags using strict custom tag handling.
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if there's a parsing problem
     */
    @Test
    public void testIssue61Strict() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.strictCustomTags = true;
        gp.load("sample/Harry_Potter.ged");
        Gedcom g = gp.gedcom;
        assertNotNull(g);
        assertTrue(!gp.errors.isEmpty());
        for (String e : gp.errors) {
            // These are the bad tags that were deliberately introduced
            assertTrue(e.contains("WAND") || e.contains("MUGL"));
        }
        for (Individual i : g.individuals.values()) {
            assertNotNull(i);
            assertNotNull(i.customTags);
            assertTrue(i.customTags.isEmpty());
        }
    }
}
