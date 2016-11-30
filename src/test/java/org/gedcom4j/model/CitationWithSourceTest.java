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
import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Test for {@link CitationWithSource}
 * 
 * @author reckenrod
 * 
 */
public class CitationWithSourceTest {

    /**
     * Test for {@link CitationWithSource#equals(Object)}
     */
    @Test
    @SuppressWarnings({ "PMD.EqualsNull", "PMD.ExcessiveMethodLength", "PMD.NcssMethodCount" })
    public void testEquals() {
        CitationWithSource cws1 = new CitationWithSource();
        assertFalse(cws1.equals(null));
        assertFalse(cws1.equals(new Place()));
        assertEquals(cws1, cws1);

        CitationWithSource cws2 = new CitationWithSource();
        assertEquals(cws1, cws2);

        cws1.getCustomFacts(true).add(new CustomFact("TEST"));
        assertFalse(cws1.equals(cws2));
        cws2.getCustomFacts(true).add(new CustomFact("TEST"));
        assertEquals(cws1, cws2);

        cws2.getData(true).add(new CitationData());
        assertFalse(cws1.equals(cws2));
        cws1.getData(true).add(new CitationData());
        assertEquals(cws1, cws2);
        cws1.getData().clear();
        assertFalse(cws1.equals(cws2));
        cws2.getData().clear();
        assertEquals(cws1, cws2);

        cws2.setEventCited("Birth");
        assertFalse(cws1.equals(cws2));
        cws1.setEventCited("Birth");
        assertEquals(cws1, cws2);
        cws2.setEventCited((String) null);
        assertFalse(cws1.equals(cws2));
        cws1.setEventCited((String) null);
        assertEquals(cws1, cws2);

        cws2.setRoleInEvent("Father");
        assertFalse(cws1.equals(cws2));
        cws1.setRoleInEvent("Father");
        assertEquals(cws1, cws2);
        cws2.setRoleInEvent((String) null);
        assertFalse(cws1.equals(cws2));
        cws1.setRoleInEvent((String) null);
        assertEquals(cws1, cws2);

        cws2.setSource(new Source());
        assertFalse(cws1.equals(cws2));
        cws1.setSource(new Source());
        assertEquals(cws1, cws2);
        cws2.setSource((Source) null);
        assertFalse(cws1.equals(cws2));
        cws1.setSource((Source) null);
        assertEquals(cws1, cws2);

        cws2.setWhereInSource("Page 9");
        assertFalse(cws1.equals(cws2));
        cws1.setWhereInSource("Page 9");
        assertEquals(cws1, cws2);
        cws2.setWhereInSource((String) null);
        assertFalse(cws1.equals(cws2));
        cws1.setWhereInSource((String) null);
        assertEquals(cws1, cws2);
    }
}
