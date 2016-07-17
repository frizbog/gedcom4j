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

import java.io.ByteArrayOutputStream;

import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.StringWithCustomTags;
import org.gedcom4j.model.Submission;
import org.gedcom4j.model.Submitter;
import org.junit.Test;

/**
 * Test for issue 97, where line breaks in SUBMITTER_TEXT values do not trigger CONT tags when writing GEDCOMs
 * 
 * @author frizbog
 *
 */
public class Issue97Test {

    /**
     * Test the fix for Issue 97
     * 
     * @throws GedcomWriterException
     */
    @Test
    public void testIssue97() throws GedcomWriterException {
        Gedcom g = new Gedcom();
        g.setSubmission(new Submission("@SUBN0001@"));
        g.getHeader().setSubmission(g.getSubmission());
        Submitter s = new Submitter();
        s.setXref("@SUBM0001@");
        // Note the newline in the middle of the value
        s.setName(new StringWithCustomTags("Line break in middle of a wo\nrd"));
        g.getSubmitters().put(s.getXref(), s);
        g.getHeader().setSubmitter(s);

        GedcomWriter gw = new GedcomWriter(g);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        gw.write(baos);
        String output = baos.toString();
        assertEquals("0 HEAD\n" + "1 SOUR UNSPECIFIED\n" + "1 SUBM @SUBM0001@\n" + "1 SUBN @SUBN0001@\n" + "1 GEDC\n"
                + "2 VERS 5.5.1\n" + "2 FORM LINEAGE-LINKED\n" + "1 CHAR ANSEL\n" + "0 @SUBN0001@ SUBN\n" + "0 @SUBM0001@ SUBM\n"
                + "1 NAME Line break in middle of a wo\n" + "2 CONT rd\n" + "0 TRLR\n", output);
    }

}
