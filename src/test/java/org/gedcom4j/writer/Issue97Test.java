package org.gedcom4j.writer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.StringWithCustomTags;
import org.gedcom4j.model.Submission;
import org.gedcom4j.model.Submitter;
import org.junit.Test;

public class Issue97Test {

    @Test
    public void testIssue97() throws GedcomWriterException {
        Gedcom g = new Gedcom();
        g.submission = new Submission("@SUBN0001@");
        g.header.submission = g.submission;
        Submitter s = new Submitter();
        s.xref = "@SUBM0001@";
        // Note the newline in the middle of the value
        s.name = new StringWithCustomTags("Line break in middle of a wo\nrd");
        g.submitters.put(s.xref, s);
        g.header.submitter = s;

        GedcomWriter gw = new GedcomWriter(g);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        gw.write(baos);
        String output = baos.toString();
        System.out.println(output);
        assertEquals("0 HEAD\n" + "1 SOUR UNSPECIFIED\n" + "1 SUBM @SUBM0001@\n" + "1 SUBN @SUBN0001@\n" + "1 GEDC\n" + "2 VERS 5.5.1\n"
                + "2 FORM LINEAGE-LINKED\n" + "1 CHAR ANSEL\n" + "0 @SUBN0001@ SUBN\n" + "0 @SUBM0001@ SUBM\n" + "1 NAME Line break in middle of a wo\n"
                + "2 CONT rd\n" + "0 TRLR\n", output);
    }

}
