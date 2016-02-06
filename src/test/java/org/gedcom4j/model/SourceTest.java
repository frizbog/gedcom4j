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

import org.junit.Test;

/**
 * Test for {@link Source}
 * 
 * @author frizbog1
 */
public class SourceTest {

    /**
     * Test for {@link Source#equals(Object)}
     */
    @Test
    public void testEqualsObject() {
        Source s1 = new Source("Foo");
        Source s2 = new Source("Foo");
        assertNotSame(s1, s2);
        assertEquals(s1, s2);

        s1.changeDate = new ChangeDate();
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.changeDate = new ChangeDate();
        assertEquals(s1, s2);

        s1.customTags = null;
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.customTags = null;
        assertEquals(s1, s2);

        s1.data = new SourceData();
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.data = new SourceData();
        assertEquals(s1, s2);

        s1.multimedia = null;
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.multimedia = null;
        assertEquals(s1, s2);

        s1.notes = null;
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.notes = null;
        assertEquals(s1, s2);

        s1.originatorsAuthors = null;
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.originatorsAuthors = null;
        assertEquals(s1, s2);

        s1.publicationFacts = null;
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.publicationFacts = null;
        assertEquals(s1, s2);

        s1.recIdNumber = new StringWithCustomTags("Foo");
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.recIdNumber = new StringWithCustomTags("Foo");
        assertEquals(s1, s2);

        s1.repositoryCitation = new RepositoryCitation();
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.repositoryCitation = new RepositoryCitation();
        assertEquals(s1, s2);

        s1.sourceFiledBy = new StringWithCustomTags("Bar");
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.sourceFiledBy = new StringWithCustomTags("Bar");
        assertEquals(s1, s2);

        s1.sourceText = null;
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.sourceText = null;
        assertEquals(s1, s2);

        s1.title = null;
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.title = null;
        assertEquals(s1, s2);

        s1.userReferences = null;
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.userReferences = null;
        assertEquals(s1, s2);

    }

    /**
     * Test for {@link Source#hashCode()}
     */
    @Test
    public void testHashCode() {
        Source s1 = new Source("Foo");
        Source s2 = new Source("Foo");
        assertNotSame(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.changeDate = new ChangeDate();
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.changeDate = new ChangeDate();
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.customTags = null;
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.customTags = null;
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.data = new SourceData();
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.data = new SourceData();
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.multimedia = null;
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.multimedia = null;
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.notes = null;
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.notes = null;
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.originatorsAuthors = null;
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.originatorsAuthors = null;
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.publicationFacts = null;
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.publicationFacts = null;
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.recIdNumber = new StringWithCustomTags("Foo");
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.recIdNumber = new StringWithCustomTags("Foo");
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.repositoryCitation = new RepositoryCitation();
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.repositoryCitation = new RepositoryCitation();
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.sourceFiledBy = new StringWithCustomTags("Bar");
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.sourceFiledBy = new StringWithCustomTags("Bar");
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.sourceText = null;
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.sourceText = null;
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.title = null;
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.title = null;
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.userReferences = null;
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.userReferences = null;
        assertEquals(s1.hashCode(), s2.hashCode());

    }

    /**
     * Test for {@link Source#toString()}
     */
    @Test
    public void testToString() {
        Source s1 = new Source("Foo");
        assertEquals("Source [title=[], notes=[], publicationFacts=[], originatorsAuthors=[], multimedia=[], userReferences=[], "
                + "sourceText=[], xref=Foo, customTags=[]]", s1.toString());

        s1.changeDate = new ChangeDate();
        s1.customTags = null;
        s1.data = new SourceData();
        s1.multimedia = null;
        s1.notes = null;
        s1.originatorsAuthors = null;
        s1.publicationFacts = null;
        s1.recIdNumber = new StringWithCustomTags("Foo");
        s1.repositoryCitation = new RepositoryCitation();
        s1.sourceFiledBy = new StringWithCustomTags("Bar");
        s1.sourceText = null;
        s1.title = null;
        s1.userReferences = null;
        assertEquals("Source [recIdNumber=Foo, sourceFiledBy=Bar, changeDate=ChangeDate [notes=[], customTags=[]], "
                + "data=SourceData [eventsRecorded=[], notes=[], customTags=[]], "
                + "repositoryCitation=RepositoryCitation [notes=[], callNumbers=[], customTags=[]], xref=Foo, ]", s1.toString());

    }

}
