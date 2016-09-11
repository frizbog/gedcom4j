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
 * Test copy constructor for {@link Repository}
 * 
 * @author frizbog
 */
public class RepositoryCopyTest extends AbstractCopyTest {

    /**
     * Test copying a null {@link Repository}, which should never work
     */
    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testCopyNull() {
        new Repository(null);
    }

    /**
     * Test the simplest possible scenario - copy a new default {@link Repository}
     */
    @Test
    public void testSimplestPossible() {
        Repository orig = new Repository();
        Repository copy = new Repository(orig);
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
        Gedcom loadedGedcom = getLoadedGedcom();

        for (Repository original : loadedGedcom.getRepositories().values()) {
            Repository copy = new Repository(original);
            assertNotSame(original, copy);
            assertEquals(original, copy);
        }
    }

    /**
     * Test with values
     */
    @Test
    public void testWithValues() {
        Repository orig = new Repository();
        orig.setAddress(getTestAddress());
        ChangeDate cd = new ChangeDate();
        cd.setDate(new StringWithCustomTags("04 APR 1904"));
        orig.setChangeDate(cd);
        orig.setName(new StringWithCustomTags("Bob's Repository"));
        orig.setRecIdNumber(new StringWithCustomTags("123"));
        orig.setXref("@R1@");
        orig.getNotes(true).add(getTestNote());
        orig.getFaxNumbers(true).add(new StringWithCustomTags("555-1212"));
        orig.getPhoneNumbers(true).add(new StringWithCustomTags("555-1313"));
        orig.getWwwUrls(true).add(new StringWithCustomTags("www.nowhere.com"));
        orig.getEmails(true).add(new StringWithCustomTags("nobody@nowwhere.com"));

        Repository copy = new Repository(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
        assertEquals(orig.toString(), copy.toString());
    }

}
