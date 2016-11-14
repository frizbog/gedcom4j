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
package org.gedcom4j.validate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.gedcom4j.model.Family;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Header;
import org.gedcom4j.model.Source;
import org.junit.Test;

/**
 * Test for the {@link AutoRepair} class
 * 
 * @author frizbog
 *
 */
public class AutoRepairTest {

    /**
     * Test constructor with mixed types
     */
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void mixedTypes() {
        new AutoRepair(new Gedcom(), new Family());
    }

    /**
     * Test for the {@link AutoRepair} class
     */
    @Test
    public void testBasic() {
        Source b = new Source();
        b.setXref("@S123@");
        b.setRecIdNumber("1");

        Source a = new Source(b);
        a.setRecIdNumber("2");

        AutoRepair ar = new AutoRepair(b, a);
        assertNotNull(ar);
        assertFalse(ar.getBefore().equals(ar.getAfter()));
    }

    /**
     * Test {@link AutoRepair#AutoRepair(org.gedcom4j.model.ModelElement, org.gedcom4j.model.ModelElement)} with differing types in
     * the parameters
     */
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorDifferentClasses() {
        new AutoRepair(new Header(), new Gedcom());
    }

    /**
     * Test {@link AutoRepair#equals(Object)}
     */
    @Test
    @SuppressWarnings("PMD.EqualsNull")
    public void testEquals() {
        AutoRepair ar1 = new AutoRepair(new Gedcom(), new Gedcom());
        assertEquals(ar1, ar1);
        assertFalse(ar1.equals(null));

        AutoRepair ar2 = new AutoRepair(new Gedcom(), new Gedcom());
        assertEquals(ar1, ar2);

        ar2 = new AutoRepair(new Header(), new Header());
        assertFalse(ar1.equals(ar2));
        ar1 = new AutoRepair(new Header(), new Header());
        assertEquals(ar1, ar2);

        ar1 = new AutoRepair(null, null);
        ar2 = new AutoRepair(null, null);
        assertEquals(ar1, ar2);
    }

    /**
     * Test {@link AutoRepair#equals(Object)}, {@link AutoRepair#hashCode()}, and {@link AutoRepair#toString()}
     */
    @Test
    public void testEqualsHashcodeToString() {
        AutoRepair ar1 = new AutoRepair(new Gedcom(), new Gedcom());
        assertEquals("AutoRepair [before=Gedcom [header=Header [characterSet=CharacterSet [characterSetName=ANSEL, ], "
                + "gedcomVersion=GedcomVersion [gedcomForm=LINEAGE-LINKED, versionNumber=5.5.1, ], sourceSystem=SourceSystem "
                + "[systemId=UNSPECIFIED, ], submitter=SubmitterReference [submitter=Submitter [name=UNSPECIFIED, "
                + "xref=@SUBMITTER@, ], ], ], families=[], individuals=[], multimedia=[], noteStructures=[], repositories=[], "
                + "sources=[], submission=Submission [xref=@SUBMISSION@, ], submitters=[], trailer=Trailer []], after=Gedcom "
                + "[header=Header [characterSet=CharacterSet [characterSetName=ANSEL, ], gedcomVersion=GedcomVersion "
                + "[gedcomForm=LINEAGE-LINKED, versionNumber=5.5.1, ], sourceSystem=SourceSystem [systemId=UNSPECIFIED, ], "
                + "submitter=SubmitterReference [submitter=Submitter [name=UNSPECIFIED, xref=@SUBMITTER@, ], ], ], "
                + "families=[], individuals=[], multimedia=[], noteStructures=[], repositories=[], sources=[], "
                + "submission=Submission [xref=@SUBMISSION@, ], submitters=[], trailer=Trailer []]]", ar1.toString());

        ar1 = new AutoRepair(new Header(), new Header());
        assertEquals("AutoRepair [before=Header [characterSet=CharacterSet [characterSetName=ANSEL, ], gedcomVersion=GedcomVersion "
                + "[gedcomForm=LINEAGE-LINKED, versionNumber=5.5.1, ], sourceSystem=SourceSystem [systemId=UNSPECIFIED, ], "
                + "submitter=SubmitterReference [submitter=Submitter [name=UNSPECIFIED, xref=@SUBMITTER@, ], ], ], "
                + "after=Header [characterSet=CharacterSet [characterSetName=ANSEL, ], gedcomVersion=GedcomVersion "
                + "[gedcomForm=LINEAGE-LINKED, versionNumber=5.5.1, ], sourceSystem=SourceSystem [systemId=UNSPECIFIED, ], "
                + "submitter=SubmitterReference [submitter=Submitter [name=UNSPECIFIED, xref=@SUBMITTER@, ], ], ]]", ar1
                        .toString());

        ar1 = new AutoRepair(null, null);
        assertEquals("AutoRepair []", ar1.toString());

        ar1 = new AutoRepair(null, new Gedcom());
        assertEquals("AutoRepair [after=Gedcom [header=Header [characterSet=CharacterSet [characterSetName=ANSEL, ], "
                + "gedcomVersion=GedcomVersion [gedcomForm=LINEAGE-LINKED, versionNumber=5.5.1, ], sourceSystem=SourceSystem "
                + "[systemId=UNSPECIFIED, ], submitter=SubmitterReference [submitter=Submitter [name=UNSPECIFIED, "
                + "xref=@SUBMITTER@, ], ], ], families=[], individuals=[], multimedia=[], noteStructures=[], repositories=[], "
                + "sources=[], submission=Submission [xref=@SUBMISSION@, ], submitters=[], trailer=Trailer []]]", ar1.toString());

        ar1 = new AutoRepair(new Header(), null);
        assertEquals(
                "AutoRepair [before=Header [characterSet=CharacterSet [characterSetName=ANSEL, ], gedcomVersion=GedcomVersion ["
                        + "gedcomForm=LINEAGE-LINKED, versionNumber=5.5.1, ], sourceSystem=SourceSystem [systemId=UNSPECIFIED, ], "
                        + "submitter=SubmitterReference [submitter=Submitter [name=UNSPECIFIED, xref=@SUBMITTER@, ], ], ], ]", ar1
                                .toString());
    }

    /**
     * Test {@link AutoRepair#hashCode()}
     */
    @Test
    public void testHashCode() {
        AutoRepair ar1 = new AutoRepair(new Gedcom(), new Gedcom());
        AutoRepair ar2 = new AutoRepair(new Gedcom(), new Gedcom());
        assertEquals(ar1.hashCode(), ar2.hashCode());

        ar2 = new AutoRepair(new Header(), new Header());
        assertFalse(ar1.hashCode() == ar2.hashCode());

        ar1 = new AutoRepair(null, null);
        ar2 = new AutoRepair(null, null);
        assertEquals(ar1.hashCode(), ar2.hashCode());

        ar1 = new AutoRepair(null, new Gedcom());
        assertFalse(ar1.hashCode() == ar2.hashCode());
        ar2 = new AutoRepair(new Header(), null);
        assertFalse(ar1.hashCode() == ar2.hashCode());
    }
}
