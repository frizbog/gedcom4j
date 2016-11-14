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
    @SuppressWarnings({ "PMD.ExcessiveMethodLength", "PMD.NcssMethodCount" })
    public void testEqualsObject() {
        Source s1 = new Source("Foo");
        Source s2 = new Source("Foo");
        assertNotSame(s1, s2);
        assertEquals(s1, s2);

        ChangeDate cd = new ChangeDate();
        s2.setChangeDate(cd);
        assertFalse(s1.equals(s2));
        s1.setChangeDate(new ChangeDate());
        assertEquals(s1, s2);
        cd.setDate("01 OCT 1900");
        assertFalse(s1.equals(s2));
        cd.setDate((StringWithCustomFacts) null);
        assertEquals(s1, s2);

        s1.customFacts = null;
        assertEquals(s1, s2);
        s1.getCustomFacts(true).clear();
        assertFalse(s1.equals(s2));
        s2.getCustomFacts(true).clear();
        assertEquals(s1, s2);
        s1.getCustomFacts(true).add(new CustomFact("_X"));
        assertFalse(s1.equals(s2));
        s1.getCustomFacts(true).clear();
        assertEquals(s1, s2);

        s2.setData(new SourceData());
        assertFalse(s1.equals(s2));
        s1.setData(new SourceData());
        assertEquals(s1, s2);
        s2.setData(null);
        assertFalse(s1.equals(s2));
        s2.setData(new SourceData());
        assertEquals(s1, s2);

        s2.getMultimedia(true);
        assertFalse(s1.equals(s2));
        s1.getMultimedia(true);
        assertEquals(s1, s2);
        s1.getMultimedia(true).add(new MultimediaReference(new Multimedia()));
        assertFalse(s1.equals(s2));
        s2.getMultimedia(true).add(new MultimediaReference(new Multimedia()));
        assertEquals(s1, s2);

        s1.getNoteStructures(true).add(new NoteStructure());
        assertFalse(s1.equals(s2));
        s2.getNoteStructures(true).add(new NoteStructure());
        assertEquals(s1, s2);

        s2.setOriginatorsAuthors(new MultiStringWithCustomFacts());
        assertFalse(s1.equals(s2));
        s1.setOriginatorsAuthors(new MultiStringWithCustomFacts());
        assertEquals(s1, s2);
        s2.setOriginatorsAuthors(null);
        assertFalse(s1.equals(s2));
        s2.setOriginatorsAuthors(new MultiStringWithCustomFacts());
        s1.getOriginatorsAuthors().getLines(true).add("qweqwe");
        assertFalse(s1.equals(s2));
        s2.getOriginatorsAuthors().getLines(true).add("qweqwe");
        assertEquals(s1, s2);

        s1.setPublicationFacts(new MultiStringWithCustomFacts());
        assertFalse(s1.equals(s2));
        s2.setPublicationFacts(new MultiStringWithCustomFacts());
        assertEquals(s1, s2);
        s1.setPublicationFacts(null);
        assertFalse(s1.equals(s2));
        s1.setPublicationFacts(new MultiStringWithCustomFacts());
        s1.getPublicationFacts().getLines(true).add("foo");
        assertFalse(s1.equals(s2));
        s2.getPublicationFacts().getLines(true).add("foo");
        assertEquals(s1, s2);

        s1.setRecIdNumber("Foo");
        assertFalse(s1.equals(s2));
        s2.setRecIdNumber("Foo");
        assertEquals(s1, s2);
        s1.setRecIdNumber((StringWithCustomFacts) null);
        assertFalse(s1.equals(s2));
        s1.setRecIdNumber("Foo");
        assertEquals(s1, s2);
        s1.setRepositoryCitation(new RepositoryCitation());
        assertFalse(s1.equals(s2));
        s2.setRepositoryCitation(new RepositoryCitation());
        assertEquals(s1, s2);
        s1.setRepositoryCitation(null);
        assertFalse(s1.equals(s2));
        s1.setRepositoryCitation(new RepositoryCitation());
        assertEquals(s1, s2);

        s1.setSourceFiledBy("Bar");
        assertFalse(s1.equals(s2));
        s2.setSourceFiledBy("Bar");
        assertEquals(s1, s2);
        s1.setSourceFiledBy((StringWithCustomFacts) null);
        assertFalse(s1.equals(s2));
        s1.setSourceFiledBy("Bar");
        assertEquals(s1, s2);

        s1.setSourceText(new MultiStringWithCustomFacts());
        assertFalse(s1.equals(s2));
        s2.setSourceText(new MultiStringWithCustomFacts());
        assertEquals(s1, s2);
        s1.setSourceText(null);
        assertFalse(s1.equals(s2));
        s1.setSourceText(new MultiStringWithCustomFacts());
        s1.getSourceText().getLines(true).add("bar");
        assertFalse(s1.equals(s2));
        s2.getSourceText().getLines(true).add("bar");
        assertEquals(s1, s2);

        s2.setTitle(new MultiStringWithCustomFacts());
        assertFalse(s1.equals(s2));
        s1.setTitle(new MultiStringWithCustomFacts());
        assertEquals(s1, s2);
        s1.getTitle().getLines(true).add("baz");
        assertFalse(s1.equals(s2));
        s2.getTitle().getLines(true).add("baz");
        assertEquals(s1, s2);

        s2.getUserReferences(true);
        assertFalse(s1.equals(s2));
        s1.getUserReferences(true);
        assertEquals(s1, s2);
        s2.getUserReferences(true).add(new UserReference());
        assertFalse(s1.equals(s2));
        s1.getUserReferences(true).add(new UserReference());
        assertEquals(s1, s2);

        s1.setXref(null);
        s2.setXref("@S1@");
        assertFalse(s1.equals(s2));
        s1.setXref("@S1@");
        assertEquals(s1, s2);
        s2.setXref("@S5@");
        assertFalse(s1.equals(s2));
        s1.setXref("@S5@");
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

        s1.customFacts = null;
        assertEquals(s1.hashCode(), s2.hashCode());
        s1.getCustomFacts(true).clear();
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.getCustomFacts(true).clear();
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.setData(new SourceData());
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.setData(new SourceData());
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.getMultimedia(true).add(new MultimediaReference(new Multimedia()));
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.getMultimedia(true).add(new MultimediaReference(new Multimedia()));
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.getNoteStructures(true).add(new NoteStructure());
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.getNoteStructures(true).add(new NoteStructure());
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.setOriginatorsAuthors(new MultiStringWithCustomFacts());
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.setOriginatorsAuthors(new MultiStringWithCustomFacts());
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.getOriginatorsAuthors().getLines(true).add("foo");
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.getOriginatorsAuthors().getLines(true).add("foo");
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.setPublicationFacts(new MultiStringWithCustomFacts());
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.setPublicationFacts(new MultiStringWithCustomFacts());
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.getPublicationFacts().getLines(true).add("bar");
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.getPublicationFacts().getLines(true).add("bar");
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.setRecIdNumber("Foo");
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.setRecIdNumber("Foo");
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.setRepositoryCitation(new RepositoryCitation());
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.setRepositoryCitation(new RepositoryCitation());
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.setSourceFiledBy("Bar");
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.setSourceFiledBy("Bar");
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.setSourceText(new MultiStringWithCustomFacts());
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.setSourceText(new MultiStringWithCustomFacts());
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.getSourceText().getLines(true).add("baz");
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.getSourceText().getLines(true).add("baz");
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.setTitle(new MultiStringWithCustomFacts());
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.setTitle(new MultiStringWithCustomFacts());
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.getTitle().getLines(true).add("bat");
        assertFalse(s1.hashCode() == s2.hashCode());
        s2.getTitle().getLines(true).add("bat");
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
        s1.customFacts = null;
        s1.setData(new SourceData());
        s1.getMultimedia(true).clear();
        s1.getNoteStructures(true).add(new NoteStructure());
        s1.setOriginatorsAuthors(new MultiStringWithCustomFacts());
        s1.getOriginatorsAuthors().getLines(true).clear();
        s1.setPublicationFacts(new MultiStringWithCustomFacts());
        s1.getPublicationFacts().getLines(true).clear();
        s1.setRecIdNumber("Foo");
        s1.setRepositoryCitation(new RepositoryCitation());
        s1.setSourceFiledBy("Bar");
        s1.setSourceText(new MultiStringWithCustomFacts());
        s1.getSourceText().getLines(true).clear();
        s1.setTitle(new MultiStringWithCustomFacts());
        s1.getTitle().getLines(true).clear();
        s1.getUserReferences(true).clear();
        assertEquals("Source [changeDate=ChangeDate [], data=SourceData [], multimedia=[], noteStructures=[NoteStructure []], "
                + "originatorsAuthors=MultiStringWithCustomFacts [lines=[], ], publicationFacts=MultiStringWithCustomFacts [lines=[], ], "
                + "recIdNumber=Foo, repositoryCitation=RepositoryCitation [], sourceFiledBy=Bar, "
                + "sourceText=MultiStringWithCustomFacts [lines=[], ], title=MultiStringWithCustomFacts [lines=[], ], "
                + "userReferences=[], xref=Foo, ]", s1.toString());

    }

}
