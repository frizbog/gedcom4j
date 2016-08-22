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

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.junit.Test;

/**
 * A simple test that loads/parses all the files in the sample folder
 * 
 * @author frizbog
 */
public class ParseAllSamplesTest {

    /**
     * Test loading all the sample files
     * 
     * @throws GedcomParserException
     *             if a file can't be parsed
     * @throws IOException
     *             if a file can't be read
     */
    @Test
    public void testLoadAllSamples() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.setStrictCustomTags(false);
        gp.setStrictLineBreaks(false);
        File sampleFolder = new File("sample");
        String[] allFiles = sampleFolder.list(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                // Return the names of the good files
                return name.endsWith(".ged");
            }
        });
        if (allFiles != null) {
            for (String s : allFiles) {
                try {
                    gp.load("sample/" + s);
                    assertNotNull(gp.getGedcom());
                } catch (IOException e) {
                    System.out.println("Loading " + s);
                    throw e;
                } catch (GedcomParserException e) {
                    System.out.println("Loading " + s);
                    throw e;
                }
            }
        }

    }

}
