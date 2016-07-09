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

        s1.setChangeDate(new ChangeDate());
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.setChangeDate(new ChangeDate());
        assertEquals(s1, s2);

        s1.customTags = null;
        assertEquals(s1, s2);
        s1.getCustomTags(true).clear();
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.getCustomTags(true).clear();
        assertEquals(s1, s2);

        s1.setData(new SourceData());
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.setData(new SourceData());
        assertEquals(s1, s2);

        s1.getMultimedia(true).add(new Multimedia());
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.getMultimedia(true).add(new Multimedia());
        assertEquals(s1, s2);

        s1.getNotes(true).add(new Note());
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.getNotes(true).add(new Note());
        assertEquals(s1, s2);

        s1.getOriginatorsAuthors(true).add("qweqwe");
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.getOriginatorsAuthors(true).add("qweqwe");
        assertEquals(s1, s2);

        s1.getPublicationFacts(true).add("foo");
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.getPublicationFacts(true).add("foo");
        assertEquals(s1, s2);

        s1.setRecIdNumber(new StringWithCustomTags("Foo"));
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.setRecIdNumber(new StringWithCustomTags("Foo"));
        assertEquals(s1, s2);

        s1.setRepositoryCitation(new RepositoryCitation());
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.setRepositoryCitation(new RepositoryCitation());
        assertEquals(s1, s2);

        s1.setSourceFiledBy(new StringWithCustomTags("Bar"));
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.setSourceFiledBy(new StringWithCustomTags("Bar"));
        assertEquals(s1, s2);

        s1.getSourceText(true).add("bar");
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.getSourceText(true).add("bar");
        assertEquals(s1, s2);

        s1.getTitle(true).add("baz");
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.getTitle(true).add("baz");
        assertEquals(s1, s2);

        s1.getUserReferences(true).add(new UserReference());
        assertTrue(s1.hashCode() != s2.hashCode());
        s2.getUserReferences(true).add(new UserReference());
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

        s1.setChangeDate(new ChangeDate());
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.setChangeDate(new ChangeDate());
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.customTags = null;
        assertEquals(s1.hashCode(), s2.hashCode());
        s1.getCustomTags(true).clear();
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.getCustomTags(true).clear();
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.setData(new SourceData());
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.setData(new SourceData());
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.getMultimedia(true).add(new Multimedia());
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.getMultimedia(true).add(new Multimedia());
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.getNotes(true).add(new Note());
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.getNotes(true).add(new Note());
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.getOriginatorsAuthors(true).add("foo");
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.getOriginatorsAuthors(true).add("foo");
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.getPublicationFacts(true).add("bar");
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.getPublicationFacts(true).add("bar");
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.setRecIdNumber(new StringWithCustomTags("Foo"));
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.setRecIdNumber(new StringWithCustomTags("Foo"));
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.setRepositoryCitation(new RepositoryCitation());
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.setRepositoryCitation(new RepositoryCitation());
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.setSourceFiledBy(new StringWithCustomTags("Bar"));
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.setSourceFiledBy(new StringWithCustomTags("Bar"));
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.getSourceText(true).add("baz");
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.getSourceText(true).add("baz");
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.getTitle(true).add("bat");
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.getTitle(true).add("bat");
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.getUserReferences(true).add(new UserReference());
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.getUserReferences(true).add(new UserReference());
        assertEquals(s1.hashCode(), s2.hashCode());

    }

    /**
     * Test for {@link Source#toString()}
     */
    @Test
    public void testToString() {
        Source s1 = new Source("Foo");
        assertEquals("Source [xref=Foo, ]", s1.toString());

        s1.setChangeDate(new ChangeDate());
        s1.customTags = null;
        s1.setData(new SourceData());
        s1.getMultimedia(true).clear();
        s1.getNotes(true).add(new Note());
        s1.getOriginatorsAuthors(true).clear();
        s1.getPublicationFacts(true).clear();
        s1.setRecIdNumber(new StringWithCustomTags("Foo"));
        s1.setRepositoryCitation(new RepositoryCitation());
        s1.setSourceFiledBy(new StringWithCustomTags("Bar"));
        s1.getSourceText(true).clear();
        s1.getTitle(true).clear();
        s1.getUserReferences(true).clear();
        assertEquals("Source [changeDate=ChangeDate [], data=SourceData [], multimedia=[], notes=[Note []], originatorsAuthors=[], "
                + "publicationFacts=[], recIdNumber=Foo, repositoryCitation=RepositoryCitation [], sourceFiledBy=Bar, "
                + "sourceText=[], title=[], userReferences=[], xref=Foo, ]", s1.toString());

    }

}
