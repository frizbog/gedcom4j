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
package org.gedcom4j.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.validate.Validator;
import org.junit.Test;

/**
 * Test for writing custom tags out (after reading them)
 * 
 * @author frizbog
 */
public class CustomFactsWriterTest {

    /**
     * Test loading a file full of custom tags, rewriting it, and comparing
     * 
     * @throws IOException
     *             if the file cannot be read
     * @throws GedcomParserException
     *             if the file cannot be parsed
     * @throws GedcomWriterException
     *             if the file cannot be (completely) written
     */
    @Test
    @SuppressWarnings("PMD.SystemPrintln")
    public void testCustomFactsWriter() throws IOException, GedcomParserException, GedcomWriterException {
        GedcomParser gp = new GedcomParser();
        gp.setStrictCustomTags(false);
        gp.load("sample/ftmcustomtags.ged");
        Gedcom g = gp.getGedcom();
        assertNotNull(g);

        assertTrue(gp.getErrors().isEmpty());
        assertTrue(gp.getWarnings().isEmpty());

        Validator v = new Validator(g);
        v.validate();

        GedcomWriter gw = new GedcomWriter(g);
        gw.setValidationSuppressed(true);
        gw.write("tmp/ftmcustomtags.ged");

        // Read all the lines of the original
        List<String> original = new ArrayList<>();
        try (FileReader fr = new FileReader("sample/ftmcustomtags.ged"); BufferedReader br = new BufferedReader(fr)) {
            String s = br.readLine();
            while (s != null) {
                original.add(s);
                s = br.readLine();
            }
        }

        // Read all the lines of the copy
        List<String> copy = new ArrayList<>();
        try (FileReader fr = new FileReader("tmp/ftmcustomtags.ged"); BufferedReader br = new BufferedReader(fr)) {
            String s = br.readLine();
            while (s != null) {
                copy.add(s);
                s = br.readLine();
            }
        }

        // Each and every line in one file should be found in the other
        for (String s : original) {
            assertTrue("Could not find original line in copy: " + s, copy.contains(s));
        }
        for (String s : copy) {
            assertTrue("Could not find copy line in original: " + s, original.contains(s));
        }

        // Should have the same number of lines
        assertEquals(original.size(), copy.size());
    }

}
