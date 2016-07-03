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
package org.gedcom4j.io.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.Note;
import org.gedcom4j.parser.GedcomParser;
import org.junit.Test;

/**
 * Test the {@link AnselReader} to ensure it properly splits long input lines on the fly with CONC tags. Only the
 * AnselReader needs to do this, since it's the only reader that works with a fixed character array instead of a
 * StringBuilder.
 * 
 * @author frizbog
 */
public class LongLineReaderTest {

    /**
     * Test with Ansel file
     * 
     * @throws IOException
     *             if the file can't be read
     * @throws GedcomParserException
     *             if the file can't be parsed, or if loading is cancelled
     */
    @Test
    public void testAnsel() throws IOException, GedcomParserException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("sample/superlongline-ansel.ged");
            GedcomParser gp = new GedcomParser();
            AnselReader ar = new AnselReader(gp, fis);

            List<String> strings = new ArrayList<String>();
            String s = ar.nextLine();
            while (s != null) {
                strings.add(s);
                s = ar.nextLine();
            }
            assertNotNull(strings);
            assertEquals("Should say there were 12 lines even though the file only has 11", 12, strings.size());
            assertEquals("0 @N1@ NOTE This is an ridiculously long line that exceeds the GEDCOM maximum line length of 255 characters "
                    + "so that we can test whether the readers can properly introduce CONC tags on the fly and keep going as if "
                    + "everything was ok when the file has lines ", strings.get(9));
            assertEquals("1 CONC that are way too long like this one is, even though there are lots of programs that write non-standard GEDCOM files.", strings
                    .get(10));

            gp = new GedcomParser();
            gp.load("sample/superlongline-ansel.ged");
            assertTrue(gp.errors.isEmpty());
            assertEquals(1, gp.warnings.size());
            Note n = gp.gedcom.notes.get("@N1@");
            assertEquals("This is an ridiculously long line that exceeds the GEDCOM maximum line length of 255 characters "
                    + "so that we can test whether the readers can properly introduce CONC tags on the fly and keep "
                    + "going as if everything was ok when the file has lines that are way too long like this one is, "
                    + "even though there are lots of programs that write non-standard GEDCOM files.", n.lines.get(0));
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

}
