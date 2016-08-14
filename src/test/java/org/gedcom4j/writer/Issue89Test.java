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

import java.io.ByteArrayOutputStream;

import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.model.*;
import org.gedcom4j.validate.GedcomValidationFinding;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * Test for issue 89 where custom tags are not being emitted by the GedcomWriter
 * 
 * @author frizbog
 */
public class Issue89Test extends TestCase {

    /**
     * Test for issue 89 where custom tags are not being emitted by the GedcomWriter
     * 
     * @throws GedcomWriterException
     */
    @Test
    public void testIssue89() throws GedcomWriterException {
        Gedcom g = new Gedcom();
        g.setSubmission(new Submission("@SUBN0001@"));
        g.getHeader().setSubmission(g.getSubmission());
        Submitter s = new Submitter();
        s.setXref("@SUBM0001@");
        s.setName(new StringWithCustomTags("Joe Tester"));
        g.getSubmitters().put(s.getXref(), s);
        g.getHeader().setSubmitter(s);

        StringTree sct = new StringTree();
        sct.setId("@CT001@");
        sct.setTag("_CUSTSB");
        sct.setValue("Custom Submitter Tag");
        s.getCustomTags(true).add(sct);

        StringTree nct = new StringTree();
        /* Note the level value gets ignored when writing */
        nct.setLevel(999);
        // No ID on this tag
        nct.setTag("_CUSTNM");
        nct.setValue("Custom Name Tag");
        s.getName().getCustomTags(true).add(nct);

        StringTree hct = new StringTree();
        hct.setId("@CT003@");
        hct.setTag("_CUSTHD");
        hct.setValue("Custom Header Tag");
        g.getHeader().getCustomTags(true).add(hct);

        StringTree hct2 = new StringTree();
        hct2.setId("@CT004@");
        hct2.setTag("_CUSTHD2");
        hct2.setValue("Custom Inner Tag inside Custom Header Tag");
        hct.getChildren(true).add(hct2);

        GedcomWriter gw = new GedcomWriter(g);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            gw.write(baos);
        } catch (GedcomWriterException e) {
            if (!gw.getValidationFindings().isEmpty()) {
                System.out.println(this.getClass().getName() + " found " + gw.getValidationFindings().size()
                        + " validation findings:");
                for (GedcomValidationFinding f : gw.getValidationFindings()) {
                    System.out.println(f);
                }
            }
            throw e;
        }
        
        // Normalize line terminators
        String output = baos.toString().replaceAll("\\r\\n","\n");
        assertEquals("0 HEAD\n" + "1 SOUR UNSPECIFIED\n" + "1 SUBM @SUBM0001@\n" + "1 SUBN @SUBN0001@\n" + "1 GEDC\n"
                + "2 VERS 5.5.1\n" + "2 FORM LINEAGE-LINKED\n" + "1 CHAR ANSEL\n" + "1 @CT003@ _CUSTHD Custom Header Tag\n"
                + "2 @CT004@ _CUSTHD2 Custom Inner Tag inside Custom Header Tag\n" + "0 @SUBN0001@ SUBN\n" + "0 @SUBM0001@ SUBM\n"
                + "1 NAME Joe Tester\n" + "2 _CUSTNM Custom Name Tag\n" + "1 @CT001@ _CUSTSB Custom Submitter Tag\n" + "0 TRLR\n",
                output);
    }

}
