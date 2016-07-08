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
package org.gedcom4j.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.gedcom4j.Options;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link CitationData}
 * 
 * @author frizbog1
 * 
 */
public class CitationDataTest {

    /**
     * Things to do before each test
     */
    @Before
    public void setUp() {
        Options.resetToDefaults();
    }

    /**
     * Things to do after each test
     */
    @After
    public void tearDown() {
        Options.resetToDefaults();
    }

    /**
     * Test for .equals();
     */
    @Test
    public void testEquals() {
        CitationData c1 = new CitationData();
        CitationData c2 = new CitationData();
        assertNotSame(c1, c2);
        assertEquals(c1, c2);

        c1.customTags = null;
        if (Options.isCollectionInitializationEnabled()) {
            assertFalse(c1.equals(c2));
        } else {
            assertEquals(c1, c2);
        }
        c1.customTags = new ArrayList<StringTree>();
        if (Options.isCollectionInitializationEnabled()) {
            assertEquals(c1, c2);
        } else {
            assertFalse(c1.equals(c2));
        }

        c1.setEntryDate(new StringWithCustomTags("Frying Pan"));
        assertFalse(c1.equals(c2));
        c2.setEntryDate(new StringWithCustomTags("Frying Pan"));
        if (Options.isCollectionInitializationEnabled()) {
            assertEquals(c1, c2);
        } else {
            assertFalse(c1.equals(c2));
        }

        c1.getSourceText(true).add(new ArrayList<String>());
        assertFalse(c1.equals(c2));
        c2.getSourceText(true).add(new ArrayList<String>());
        if (Options.isCollectionInitializationEnabled()) {
            assertEquals(c1, c2);
        } else {
            assertFalse(c1.equals(c2));
        }

    }

    /**
     * Test for .hashCode();
     */
    @Test
    public void testHashCode() {
        CitationData c1 = new CitationData();
        CitationData c2 = new CitationData();
        assertNotSame(c1.hashCode(), c2.hashCode());
        assertEquals(c1.hashCode(), c2.hashCode());

        c1.customTags = null;
        if (Options.isCollectionInitializationEnabled()) {
            assertFalse(c1.hashCode() == c2.hashCode());
        } else {
            assertEquals(c1.hashCode(), c2.hashCode());
        }
        c1.customTags = new ArrayList<StringTree>();
        if (Options.isCollectionInitializationEnabled()) {
            assertEquals(c1.hashCode(), c2.hashCode());
        } else {
            assertTrue(c1.hashCode() != c2.hashCode());
        }

        c1.setEntryDate(new StringWithCustomTags("Frying Pan"));
        assertFalse(c1.hashCode() == c2.hashCode());
        c2.setEntryDate(new StringWithCustomTags("Frying Pan"));
        if (Options.isCollectionInitializationEnabled()) {
            assertEquals(c1.hashCode(), c2.hashCode());
        } else {
            assertTrue(c1.hashCode() != c2.hashCode());
        }

        c1.getSourceText(true).add(new ArrayList<String>());
        assertFalse(c1.hashCode() == c2.hashCode());
        c2.getSourceText(true).add(new ArrayList<String>());
        if (Options.isCollectionInitializationEnabled()) {
            assertEquals(c1.hashCode(), c2.hashCode());
        } else {
            assertTrue(c1.hashCode() != c2.hashCode());
        }

    }

    /**
     * Test for .toString()
     */
    @Test
    public void testToStringNotPrepopulated() {
        Options.setCollectionInitializationEnabled(false);
        CitationData c1 = new CitationData();
        assertEquals("CitationData []", c1.toString());

        c1.customTags = null;
        c1.setEntryDate(new StringWithCustomTags("Frying Pan"));
        c1.getSourceText(true).add(new ArrayList<String>());

        assertEquals("CitationData [entryDate=Frying Pan, sourceText=[[]], ]", c1.toString());
    }

    /**
     * Test for .toString()
     */
    @Test
    public void testToStringPrepopulated() {
        Options.setCollectionInitializationEnabled(true);
        CitationData c1 = new CitationData();
        assertEquals("CitationData [sourceText=[], customTags=[]]", c1.toString());

        c1.customTags = null;
        c1.setEntryDate(new StringWithCustomTags("Frying Pan"));
        c1.getSourceText().add(new ArrayList<String>());

        assertEquals("CitationData [entryDate=Frying Pan, sourceText=[[]], ]", c1.toString());
    }
}
