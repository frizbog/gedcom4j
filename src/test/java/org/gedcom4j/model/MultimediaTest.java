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
 * Test for {@link Multimedia}
 * 
 * @author reckenrod
 * 
 */
public class MultimediaTest {

    /**
     * Test for {@link Multimedia#equals(Object)}
     */
    @Test
    @SuppressWarnings({ "PMD.EqualsNull", "PMD.ExcessiveMethodLength", "PMD.NcssMethodCount" })
    public void testEquals() {
        Multimedia m1 = new Multimedia();
        assertFalse(m1.equals(null));
        assertFalse(m1.equals(new Place()));
        assertEquals(m1, m1);

        Multimedia m2 = new Multimedia();
        assertEquals(m1, m2);

        m1.getCustomFacts(true).add(new CustomFact("TEST"));
        assertFalse(m1.equals(m2));
        m2.getCustomFacts(true).add(new CustomFact("TEST"));
        assertEquals(m1, m2);

        m2.getBlob(true).add("101");
        assertFalse(m1.equals(m2));
        m1.getBlob(true).add("101");
        assertEquals(m1, m2);
        m1.getBlob().clear();
        assertFalse(m1.equals(m2));
        m2.getBlob().clear();
        assertEquals(m1, m2);

        m2.setChangeDate(new ChangeDate());
        assertFalse(m1.equals(m2));
        m1.setChangeDate(new ChangeDate());
        assertEquals(m1, m2);
        m2.setChangeDate((ChangeDate) null);
        assertFalse(m1.equals(m2));
        m1.setChangeDate((ChangeDate) null);
        assertEquals(m1, m2);

        m2.getCitations(true).add(new CitationWithSource());
        assertFalse(m1.equals(m2));
        m1.getCitations(true).add(new CitationWithSource());
        assertEquals(m1, m2);
        m1.getCitations().clear();
        assertFalse(m1.equals(m2));
        m2.getCitations().clear();
        assertEquals(m1, m2);

        m2.setContinuedObject(new MultimediaReference());
        assertFalse(m1.equals(m2));
        m1.setContinuedObject(new MultimediaReference());
        assertEquals(m1, m2);
        m2.setContinuedObject((MultimediaReference) null);
        assertFalse(m1.equals(m2));
        m1.setContinuedObject((MultimediaReference) null);
        assertEquals(m1, m2);

        m2.setEmbeddedMediaFormat("Test");
        assertFalse(m1.equals(m2));
        m1.setEmbeddedMediaFormat("Test");
        assertEquals(m1, m2);
        m2.setEmbeddedMediaFormat((String) null);
        assertFalse(m1.equals(m2));
        m1.setEmbeddedMediaFormat((String) null);
        assertEquals(m1, m2);

        m2.setEmbeddedTitle("Test");
        assertFalse(m1.equals(m2));
        m1.setEmbeddedTitle("Test");
        assertEquals(m1, m2);
        m2.setEmbeddedTitle((String) null);
        assertFalse(m1.equals(m2));
        m1.setEmbeddedTitle((String) null);
        assertEquals(m1, m2);

        m2.getFileReferences(true).add(new FileReference());
        assertFalse(m1.equals(m2));
        m1.getFileReferences(true).add(new FileReference());
        assertEquals(m1, m2);
        m1.getFileReferences().clear();
        assertFalse(m1.equals(m2));
        m2.getFileReferences().clear();
        assertEquals(m1, m2);

        m2.setRecIdNumber("1");
        assertFalse(m1.equals(m2));
        m1.setRecIdNumber("1");
        assertEquals(m1, m2);
        m2.setRecIdNumber((String) null);
        assertFalse(m1.equals(m2));
        m1.setRecIdNumber((String) null);
        assertEquals(m1, m2);

        m2.getUserReferences(true).add(new UserReference());
        assertFalse(m1.equals(m2));
        m1.getUserReferences(true).add(new UserReference());
        assertEquals(m1, m2);
        m1.getUserReferences().clear();
        assertFalse(m1.equals(m2));
        m2.getUserReferences().clear();
        assertEquals(m1, m2);

        m2.setXref("23");
        assertFalse(m1.equals(m2));
        m1.setXref("23");
        assertEquals(m1, m2);
        m2.setXref((String) null);
        assertFalse(m1.equals(m2));
        m1.setXref((String) null);
        assertEquals(m1, m2);
    }
}
