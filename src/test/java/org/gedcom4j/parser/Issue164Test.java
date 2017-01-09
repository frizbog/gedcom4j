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
package org.gedcom4j.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.InMemoryGedcom;
import org.gedcom4j.model.IGedcom;
import org.junit.Test;

/**
 * Originally Issue 164 at Github noted that successive calls to {@link GedcomParser} did not create new {@link InMemoryGedcom} objects on
 * successive calls. Beginning with v5.0.0 of gedcom4j, GedcomParser no longer acts as a factory class, and the {@link IGedcom} that
 * is populated with data is now supplied by the class that instantiates the parser.
 * 
 * @author frizbog
 */
public class Issue164Test {

    /**
     * The test
     * 
     * @throws IOException
     *             if the sample file cannot be read
     * @throws GedcomParserException
     *             if the sample file cannot be parsed
     */
    @Test
    public void test() throws IOException, GedcomParserException {
        GedcomParser oneParser = new GedcomParser(new InMemoryGedcom());
        oneParser.load("sample/minimal55.ged");
        IGedcom g1 = oneParser.getGedcom();
        assertNotNull(g1);

        // Load file again
        oneParser.load("sample/minimal55.ged");
        IGedcom g2 = oneParser.getGedcom();
        assertNotNull(g2);

        assertEquals(g1, g2);
        assertSame(g1, g2);

        GedcomParser anotherParser = new GedcomParser(new InMemoryGedcom());
        anotherParser.load("sample/minimal55.ged");
        g2 = anotherParser.getGedcom();
        assertNotNull(g2);

        assertEquals(g1, g2);
        assertNotSame(g1, g2);
    }

}
