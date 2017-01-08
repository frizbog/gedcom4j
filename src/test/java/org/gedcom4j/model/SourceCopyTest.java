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

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.junit.Test;

/**
 * Test copy constructor for {@link Source}
 * 
 * @author frizbog
 */
public class SourceCopyTest extends AbstractCopyTest {

    /**
     * Test copying a null {@link Source}, which should never work
     */
    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testCopyNull() {
        new Source((Source) null);
    }

    /**
     * Test the simplest possible scenario - copy a new default {@link Source}
     */
    @Test
    public void testSimplestPossible() {
        Source orig = new Source();
        Source copy = new Source(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
    }

    /**
     * Test with a loaded file
     * 
     * @throws IOException
     *             if the file cannot be read
     * @throws GedcomParserException
     *             if the file cannot be parsed
     */
    @Test
    public void testWithLoadedFile() throws IOException, GedcomParserException {
        IGedcom loadedGedcom = getLoadedGedcom();

        for (Source original : loadedGedcom.getSources().values()) {
            Source copy = new Source(original);
            assertNotSame(original, copy);
            assertEquals(original, copy);
        }
    }

    /**
     * Test with values
     */
    @Test
    public void testWithValues() {
        Source orig = new Source();
        ChangeDate changeDate = new ChangeDate();
        changeDate.setDate("01 JAN 1970");
        orig.setChangeDate(changeDate);
        orig.setRecIdNumber("A");
        orig.setXref("B");
        orig.setSourceFiledBy("foo");
        orig.setSourceText(new MultiStringWithCustomFacts());
        orig.getUserReferences(true).add(new UserReference());
        MultimediaReference m = new MultimediaReference();
        orig.getMultimedia(true).add(m);

        SourceData data = new SourceData();
        data.getNoteStructures(true).add(getTestNoteStructure());
        orig.setData(data);
        RepositoryCitation rc = new RepositoryCitation();
        rc.setRepositoryXref("@R123@");
        SourceCallNumber scn = new SourceCallNumber();
        scn.setCallNumber("890");
        rc.getCallNumbers(true).add(scn);
        orig.setRepositoryCitation(rc);

        Source copy = new Source(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
        assertEquals(orig.toString(), copy.toString());
    }

}
