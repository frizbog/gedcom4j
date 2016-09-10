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

import org.junit.Test;

/**
 * Test copy constructor for {@link Multimedia}
 * 
 * @author frizbog
 */
public class MultimediaCopyTest extends AbstractCopyTest {

    /**
     * Test copying a null {@link Multimedia}, which should never work
     */
    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testCopyNull() {
        new Multimedia(null);
    }

    /**
     * Test the simplest possible scenario - copy a new default {@link Multimedia}
     */
    @Test
    public void testSimplestPossible() {
        Multimedia orig = new Multimedia();
        Multimedia copy = new Multimedia(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
    }

    /**
     * Test with values
     */
    @Test
    public void testWithValues() {
        Multimedia orig = new Multimedia();
        ChangeDate cd = new ChangeDate();
        cd.setDate(new StringWithCustomTags("1 JAN 1911"));
        orig.setChangeDate(cd);
        orig.setContinuedObject(new Multimedia());
        orig.setEmbeddedMediaFormat(new StringWithCustomTags("MPG"));
        orig.setEmbeddedTitle(new StringWithCustomTags("Movie.mpg"));
        orig.setRecIdNumber(new StringWithCustomTags("123"));
        orig.setXref("@M1@");
        orig.getBlob(true).add("qqwpeoiqpwoeiqpoweiqpowiepqowiepqowiepqowi");
        orig.getBlob().add("asdlkajsdlkajlaksjdlaksjdlaskjda");
        orig.getBlob().add("zxmcnbzmxncbmxnvbmnxbx");
        orig.getCitations(true).add(getTestCitation());
        orig.getCustomTags(true).add(getTestCustomTags());
        FileReference fr = new FileReference();
        fr.setFormat(new StringWithCustomTags("MPG"));
        fr.setMediaType(new StringWithCustomTags("Movie"));
        fr.setReferenceToFile(new StringWithCustomTags("foo.mpg"));
        fr.setTitle(new StringWithCustomTags("Super movie!"));
        orig.getFileReferences(true).add(fr);
        orig.getNotes(true).add(getTestNote());
        UserReference u = new UserReference();
        u.setReferenceNum(new StringWithCustomTags("123"));
        orig.getUserReferences(true).add(u);

        Multimedia copy = new Multimedia(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
        assertEquals(orig.toString(), copy.toString());
    }

}
