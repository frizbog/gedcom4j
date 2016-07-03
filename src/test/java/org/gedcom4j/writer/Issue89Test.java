package org.gedcom4j.writer;

import java.io.ByteArrayOutputStream;

import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.model.*;
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
        g.getHeader().submission = g.getSubmission();
        Submitter s = new Submitter();
        s.xref = "@SUBM0001@";
        s.name = new StringWithCustomTags("Joe Tester");
        g.getSubmitters().put(s.xref, s);
        g.getHeader().submitter = s;

        StringTree sct = new StringTree();
        sct.id = "@CT001@";
        sct.tag = "_CUSTSB";
        sct.value = "Custom Submitter Tag";
        s.getCustomTags().add(sct);

        StringTree nct = new StringTree();
        /* Note the level value gets ignored when writing */
        nct.level = 999;
        // No ID on this tag
        nct.tag = "_CUSTNM";
        nct.value = "Custom Name Tag";
        s.name.getCustomTags().add(nct);

        StringTree hct = new StringTree();
        hct.id = "@CT003@";
        hct.tag = "_CUSTHD";
        hct.value = "Custom Header Tag";
        g.getHeader().getCustomTags().add(hct);

        StringTree hct2 = new StringTree();
        hct2.id = "@CT004@";
        hct2.tag = "_CUSTHD2";
        hct2.value = "Custom Inner Tag inside Custom Header Tag";
        hct.children.add(hct2);

        GedcomWriter gw = new GedcomWriter(g);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        gw.write(baos);
        String output = baos.toString();
        assertEquals("0 HEAD\n" + "1 SOUR UNSPECIFIED\n" + "1 SUBM @SUBM0001@\n" + "1 SUBN @SUBN0001@\n" + "1 GEDC\n" + "2 VERS 5.5.1\n"
                + "2 FORM LINEAGE-LINKED\n" + "1 CHAR ANSEL\n" + "1 @CT003@ _CUSTHD Custom Header Tag\n"
                + "2 @CT004@ _CUSTHD2 Custom Inner Tag inside Custom Header Tag\n" + "0 @SUBN0001@ SUBN\n" + "0 @SUBM0001@ SUBM\n" + "1 NAME Joe Tester\n"
                + "2 _CUSTNM Custom Name Tag\n" + "1 @CT001@ _CUSTSB Custom Submitter Tag\n" + "0 TRLR\n", output);
    }

}
