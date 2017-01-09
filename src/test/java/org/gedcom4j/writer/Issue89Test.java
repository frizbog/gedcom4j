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
import org.gedcom4j.model.CustomFact;
import org.gedcom4j.model.InMemoryGedcom;
import org.gedcom4j.model.IGedcom;
import org.gedcom4j.model.Submission;
import org.gedcom4j.model.SubmissionReference;
import org.gedcom4j.model.Submitter;
import org.gedcom4j.model.SubmitterReference;
import org.gedcom4j.validate.Validator.Finding;
import org.junit.Test;

/**
 * Test for issue 89 where custom tags are not being emitted by the GedcomWriter
 * 
 * @author frizbog
 */
public class Issue89Test {

    /**
     * Test for issue 89 where custom tags are not being emitted by the GedcomWriter
     * 
     * @throws GedcomWriterException
     *             if the data cannot be written
     */
    @Test
    @SuppressWarnings("PMD.SystemPrintln")
    public void testIssue89() throws GedcomWriterException {
        IGedcom g = new InMemoryGedcom();
        g.setSubmission(new Submission("@SUBN0001@"));
        g.getHeader().setSubmissionReference(new SubmissionReference(g.getSubmission()));
        Submitter s = new Submitter();
        s.setXref("@SUBM0001@");
        s.setName("Joe Tester");
        g.getSubmitters().put(s.getXref(), s);
        g.getHeader().setSubmitterReference(new SubmitterReference(s));

        CustomFact cf = new CustomFact("_CUSTSB");
        cf.setXref("@CT001@");
        cf.setDescription("Custom Submitter Tag");
        s.getCustomFacts(true).add(cf);

        CustomFact cf2 = new CustomFact("_CUSTNM");
        // No ID on this tag
        cf2.setDescription("Custom Name Tag");
        s.getName().getCustomFacts(true).add(cf2);

        CustomFact cf3 = new CustomFact("_CUSTHD");
        cf3.setXref("@CT003@");
        cf3.setDescription("Custom Header Tag");
        g.getHeader().getCustomFacts(true).add(cf3);

        CustomFact cf4 = new CustomFact("_CUSTHD2");
        cf4.setXref("@CT004@");
        cf4.setDescription("Custom Inner Tag inside Custom Header Tag");
        cf3.getCustomFacts(true).add(cf4);

        GedcomWriter gw = new GedcomWriter(g);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            gw.write(baos);
        } catch (GedcomWriterException e) {
            if (!gw.getValidator().getResults().getAllFindings().isEmpty()) {
                System.out.println(this.getClass().getName() + " found " + gw.getValidator().getResults().getAllFindings().size()
                        + " validation findings:");
                for (Finding f : gw.getValidator().getResults().getAllFindings()) {
                    System.out.println(f);
                }
            }
            throw e;
        }

        // Normalize line terminators
        String output = baos.toString().replaceAll("\\r\\n", "\n");
        assertEquals("0 HEAD\n" + "1 SOUR UNSPECIFIED\n" + "1 SUBM @SUBM0001@\n" + "1 SUBN @SUBN0001@\n" + "1 GEDC\n"
                + "2 VERS 5.5.1\n" + "2 FORM LINEAGE-LINKED\n" + "1 CHAR ANSEL\n" + "1 @CT003@ _CUSTHD Custom Header Tag\n"
                + "2 @CT004@ _CUSTHD2 Custom Inner Tag inside Custom Header Tag\n" + "0 @SUBN0001@ SUBN\n" + "0 @SUBM0001@ SUBM\n"
                + "1 NAME Joe Tester\n" + "2 _CUSTNM Custom Name Tag\n" + "1 @CT001@ _CUSTSB Custom Submitter Tag\n" + "0 TRLR\n",
                output);
    }

}
