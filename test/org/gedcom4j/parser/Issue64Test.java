package org.gedcom4j.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.gedcom4j.writer.GedcomWriter;
import org.gedcom4j.writer.GedcomWriterException;
import org.junit.Test;

/**
 * Test for Issue 64 - the parser should be tolerant of blank lines
 * 
 * @author frizbog
 */
public class Issue64Test {

    /**
     * Test for Issue 64. Load a file with blank lines that were deliberately introduced.
     * 
     * @throws IOException
     *             if there is a problem read/writing data
     * @throws GedcomParserException
     *             if there is a problem parsing the input file
     * @throws GedcomWriterException
     *             if there is problem writing the gedcom out to a string
     */
    @Test
    public void testIssue64() throws IOException, GedcomParserException, GedcomWriterException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/Issue 64.ged");
        assertNotNull(gp.gedcom);
        assertEquals(2, gp.gedcom.individuals.size());
        assertEquals(1, gp.gedcom.families.size());
        GedcomWriter gw = new GedcomWriter(gp.gedcom);
        gw.autorepair = true;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        gw.write(baos);
        assertEquals("Output should be the same as the input file, but without the blank lines", "0 HEAD\n1 SOUR 0\n1 SUBM @SUBM0000@\n1 GEDC\n"
                + "2 VERS 5.5\n2 FORM LINEAGE-LINKED\n1 CHAR ANSEL\n0 @SUBMISSION@ SUBN\n0 @I002@ INDI\n1 NAME Wife /Gedcom/\n"
                + "1 FAMS @F001@\n0 @I001@ INDI\n1 NAME Husband /Gedcom/\n1 FAMS @F001@\n0 @F001@ FAM\n1 HUSB @I001@\n"
                + "1 WIFE @I002@\n0 @SUBM0000@ SUBM\n1 NAME UNSPECIFIED\n0 TRLR\n", new String(baos.toByteArray()));
    }
}
