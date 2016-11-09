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

import java.util.Date;

import org.junit.Test;

/**
 * Test for {@link Header}
 * 
 * @author frizbog
 */
public class HeaderTest {

    /**
     * Test for {@link Header#equals(Object)}
     */
    @Test
    @SuppressWarnings({ "PMD.EqualsNull", "PMD.ExcessiveMethodLength", "PMD.NcssMethodCount" })
    public void testEquals() {
        Header h1 = new Header();
        assertFalse(h1.equals(null));
        assertFalse(h1.equals(new Date()));
        assertEquals(h1, h1);

        Header h2 = new Header();
        assertEquals(h1, h2);

        h2.getCopyrightData(true).clear();
        assertFalse(h1.equals(h2));
        h1.getCopyrightData(true).clear();
        assertEquals(h1, h2);
        h2.getCopyrightData().add("Foo Bar Baz Bat");
        assertFalse(h1.equals(h2));
        h1.getCopyrightData().add("Foo Bar Baz Bat");
        assertEquals(h1, h2);

        h1.setCharacterSet(null);
        assertFalse(h1.equals(h2));
        h2.setCharacterSet(null);
        assertEquals(h1, h2);
        h1.setCharacterSet(new CharacterSet());
        assertFalse(h1.equals(h2));
        h2.setCharacterSet(new CharacterSet());
        assertEquals(h1, h2);

        h1.setDate((StringWithCustomFacts) null);
        h2.setDate("Foo");
        assertFalse(h1.equals(h2));
        h1.setDate("Bar");
        assertFalse(h1.equals(h2));
        h1.setDate((String) null);
        assertFalse(h1.equals(h2));
        h1.setDate("Foo");
        assertEquals(h1, h2);

        h1.setDestinationSystem((StringWithCustomFacts) null);
        h2.setDestinationSystem("Foo");
        assertFalse(h1.equals(h2));
        h1.setDestinationSystem("Bar");
        assertFalse(h1.equals(h2));
        h1.setDestinationSystem((String) null);
        assertFalse(h1.equals(h2));
        h1.setDestinationSystem("Foo");
        assertEquals(h1, h2);

        h1.setFileName((StringWithCustomFacts) null);
        h2.setFileName("Foo");
        assertFalse(h1.equals(h2));
        h1.setFileName("Bar");
        assertFalse(h1.equals(h2));
        h1.setFileName((String) null);
        assertFalse(h1.equals(h2));
        h1.setFileName("Foo");
        assertEquals(h1, h2);

        h2.setGedcomVersion(null);
        assertFalse(h1.equals(h2));
        h1.setGedcomVersion(null);
        assertEquals(h1, h2);
        h2.setGedcomVersion(new GedcomVersion());
        assertFalse(h1.equals(h2));
        h1.setGedcomVersion(new GedcomVersion());
        assertEquals(h1, h2);

        h1.setLanguage((StringWithCustomFacts) null);
        h2.setLanguage("Bar");
        assertFalse(h1.equals(h2));
        h1.setLanguage("Foo");
        assertFalse(h1.equals(h2));
        h1.setLanguage((String) null);
        assertFalse(h1.equals(h2));
        h1.setLanguage("Bar");
        assertEquals(h1, h2);

        h1.setPlaceHierarchy((StringWithCustomFacts) null);
        h2.setPlaceHierarchy("Baz");
        assertFalse(h1.equals(h2));
        h1.setPlaceHierarchy("Foo");
        assertFalse(h1.equals(h2));
        h1.setPlaceHierarchy((String) null);
        assertFalse(h1.equals(h2));
        h1.setPlaceHierarchy("Baz");
        assertEquals(h1, h2);

        h2.setSourceSystem(null);
        assertFalse(h1.equals(h2));
        h1.setSourceSystem(null);
        assertEquals(h1, h2);
        h2.setSourceSystem(new SourceSystem());
        assertFalse(h1.equals(h2));
        h1.setSourceSystem(new SourceSystem());
        assertEquals(h1, h2);

        h1.setSubmissionReference(null);
        h2.setSubmissionReference(new SubmissionReference());
        assertFalse(h1.equals(h2));
        h1.setSubmissionReference(new SubmissionReference());
        assertEquals(h1, h2);
        h2.setSubmissionReference(null);
        assertFalse(h1.equals(h2));
        h1.setSubmissionReference(null);
        assertEquals(h1, h2);

        h1.setSubmitterReference(null);
        h2.setSubmitterReference(new SubmitterReference());
        assertFalse(h1.equals(h2));
        h1.setSubmitterReference(new SubmitterReference());
        assertEquals(h1, h2);
        h2.setSubmitterReference(null);
        assertFalse(h1.equals(h2));
        h1.setSubmitterReference(null);
        assertEquals(h1, h2);

        h1.setTime((StringWithCustomFacts) null);
        h2.setTime("Bat");
        assertFalse(h1.equals(h2));
        h1.setTime("Foo");
        assertFalse(h1.equals(h2));
        h1.setTime((String) null);
        assertFalse(h1.equals(h2));
        h1.setTime("Bat");
        assertEquals(h1, h2);
    }

    /**
     * Test for {@link Header#hashCode()}
     */
    @Test
    public void testHashCode() {
        Header h1 = new Header();
        assertFalse(h1.hashCode() == new Gedcom().hashCode());
        assertEquals(h1.hashCode(), h1.hashCode());

        Header h2 = new Header();
        assertEquals(h1.hashCode(), h2.hashCode());

        h2 = new Header(h1);
        assertEquals(h1, h2);
        assertEquals(h1.hashCode(), h2.hashCode());

        h1.getCopyrightData(true).add("Foo");
        assertFalse(h1.hashCode() == h2.hashCode());
        h2.getCopyrightData(true).add("Foo");
        assertEquals(h1.hashCode(), h2.hashCode());

        h1.setCharacterSet(null);
        assertFalse(h1.hashCode() == h2.hashCode());
        h2.setCharacterSet(null);
        assertEquals(h1.hashCode(), h2.hashCode());

        h2.setDate("Foo");
        assertFalse(h1.hashCode() == h2.hashCode());
        h1.setDate("Foo");
        assertEquals(h1.hashCode(), h2.hashCode());

        h2.setDestinationSystem("Foo");
        assertFalse(h1.hashCode() == h2.hashCode());
        h1.setDestinationSystem("Foo");
        assertEquals(h1.hashCode(), h2.hashCode());

        h2.setFileName("Foo");
        assertFalse(h1.hashCode() == h2.hashCode());
        h1.setFileName("Foo");
        assertEquals(h1.hashCode(), h2.hashCode());

        h2.setGedcomVersion(null);
        assertFalse(h1.hashCode() == h2.hashCode());
        h1.setGedcomVersion(null);
        assertEquals(h1.hashCode(), h2.hashCode());
        h2.setGedcomVersion(new GedcomVersion());
        assertFalse(h1.hashCode() == h2.hashCode());
        h1.setGedcomVersion(new GedcomVersion());
        assertEquals(h1.hashCode(), h2.hashCode());
        h2.setGedcomVersion(null);
        assertFalse(h1.hashCode() == h2.hashCode());
        h1.setGedcomVersion(null);
        assertEquals(h1.hashCode(), h2.hashCode());

        h2.setLanguage("Bar");
        assertFalse(h1.hashCode() == h2.hashCode());
        h1.setLanguage("Bar");
        assertEquals(h1.hashCode(), h2.hashCode());

        h2.setPlaceHierarchy("Baz");
        assertFalse(h1.hashCode() == h2.hashCode());
        h1.setPlaceHierarchy("Baz");
        assertEquals(h1.hashCode(), h2.hashCode());

        h2.setSourceSystem(null);
        assertFalse(h1.hashCode() == h2.hashCode());
        h1.setSourceSystem(null);
        assertEquals(h1.hashCode(), h2.hashCode());
        h2.setSourceSystem(new SourceSystem());
        assertFalse(h1.hashCode() == h2.hashCode());
        h1.setSourceSystem(new SourceSystem());
        assertEquals(h1.hashCode(), h2.hashCode());
        h2.setSourceSystem(null);
        assertFalse(h1.hashCode() == h2.hashCode());
        h1.setSourceSystem(null);
        assertEquals(h1.hashCode(), h2.hashCode());

        h2.setSubmissionReference(new SubmissionReference());
        assertFalse(h1.hashCode() == h2.hashCode());
        h1.setSubmissionReference(new SubmissionReference());
        assertEquals(h1.hashCode(), h2.hashCode());
        h2.setSubmissionReference(null);
        assertFalse(h1.hashCode() == h2.hashCode());
        h1.setSubmissionReference(null);
        assertEquals(h1.hashCode(), h2.hashCode());

        h2.setSubmitterReference(null);
        assertFalse(h1.hashCode() == h2.hashCode());
        h1.setSubmitterReference(null);
        assertEquals(h1.hashCode(), h2.hashCode());
        h2.setSubmitterReference(new SubmitterReference());
        assertFalse(h1.hashCode() == h2.hashCode());
        h1.setSubmitterReference(new SubmitterReference());
        assertEquals(h1.hashCode(), h2.hashCode());

        h2.setTime("Bat");
        assertFalse(h1.hashCode() == h2.hashCode());
        h1.setTime("Bat");
        assertEquals(h1.hashCode(), h2.hashCode());

        h2 = new Header(h1);
        assertEquals(h1, h2);
        assertEquals(h1.hashCode(), h2.hashCode());
    }

    /**
     * Test {@link Header#toString()}
     */
    @Test
    public void testToString() {
        Header h1 = new Header();
        h1.setCharacterSet(null);
        h1.setDate("Foo");
        h1.setDestinationSystem("Foo");
        h1.setFileName("Foo");
        h1.setGedcomVersion(null);
        h1.setLanguage("Bar");
        h1.setPlaceHierarchy("Baz");
        h1.setSourceSystem(null);
        h1.setSubmitterReference(null);
        h1.setTime("Bat");
        assertEquals("Header [date=Foo, destinationSystem=Foo, fileName=Foo, language=Bar, placeHierarchy=Baz, time=Bat, ]", h1
                .toString());
    }
}
