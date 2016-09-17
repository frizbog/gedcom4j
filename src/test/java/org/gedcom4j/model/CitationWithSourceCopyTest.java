/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package org.gedcom4j.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Test copy constructor for {@link CitationWithSource}
 * 
 * @author frizbog
 */
public class CitationWithSourceCopyTest extends AbstractCopyTest {

    /**
     * Test copying a null {@link CitationWithSource}, which should never work
     */
    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testCopyNull() {
        new CitationWithSource(null);
    }

    /**
     * Test the simplest possible scenario - copy a new default {@link CitationWithSource}
     */
    @Test
    public void testSimplestPossible() {
        CitationWithSource orig = new CitationWithSource();
        CitationWithSource copy = new CitationWithSource(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
    }

    /**
     * Test with values
     */
    @Test
    public void testWithValues() {
        CitationWithSource orig = new CitationWithSource();
        orig.setCertainty("High");
        orig.setEventCited("You know, that thing that happened");
        orig.setRoleInEvent("You had to be there");
        Source s = new Source();
        s.setXref("@S1@");
        orig.setSource(s);
        orig.setWhereInSource("Page 394");
        CitationData cd = new CitationData();
        List<String> ls = new ArrayList<>();
        ls.add("line 1");
        ls.add("line 2");
        cd.getSourceText(true).add(ls);
        cd.getCustomTags(true).add(getTestCustomTags());
        cd.setEntryDate("30 Jun 1998");
        orig.getData(true).add(cd);

        CitationWithSource copy = new CitationWithSource(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
        assertEquals(orig.toString(), copy.toString());
    }

}
