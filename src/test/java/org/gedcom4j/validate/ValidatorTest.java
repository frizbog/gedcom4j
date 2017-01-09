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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.InMemoryGedcom;
import org.gedcom4j.model.GedcomVersion;
import org.gedcom4j.model.Header;
import org.gedcom4j.model.IGedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.NoteRecord;
import org.gedcom4j.model.Repository;
import org.gedcom4j.model.Source;
import org.gedcom4j.model.Submitter;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.validate.Validator.Finding;
import org.junit.Test;

/**
 * Test for {@link Validator} class's basic methods, and not the actual validation itself. That's spread among a lot of other
 * classes in the package...but from the top of the object hierarchy, try {@link ValidatorGedcomStructureTest}.
 * 
 * @author frizbog
 */
@SuppressWarnings("PMD.TooManyMethods")
public class ValidatorTest implements AutoRepairResponder {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 8324174994665255952L;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean mayRepair(Finding repairableValidationFinding) {
        return false;
    }

    /**
     * Test for constructor with a null argument, which is forbidden
     */
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullGedcom() {
        new Validator(null); // boom
    }

    /**
     * Test method for {@link Validator#getAutoRepairResponder()}.
     */
    @Test
    public void testGetAutoRepairResponder() {
        Validator v = new Validator(new InMemoryGedcom());
        assertNotNull(v);
        assertSame(Validator.AUTO_REPAIR_NONE, v.getAutoRepairResponder());
    }

    /**
     * Test method for {@link Validator#getGedcom()}.
     */
    @Test
    public void testGetGedcom() {
        InMemoryGedcom g = new InMemoryGedcom();
        Validator v = new Validator(g);
        assertNotNull(v);
        assertSame(g, v.getGedcom());
    }

    /**
     * Test method for {@link Validator#getResults()}.
     */
    @Test
    public void testGetResults() {
        InMemoryGedcom g = new InMemoryGedcom();
        Validator v = new Validator(g);
        assertNotNull(v.getResults().getAllFindings());
        assertEquals(0, v.getResults().getAllFindings().size());
        v.newFinding(g, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, null);
        assertEquals(1, v.getResults().getAllFindings().size());
    }

    /**
     * Test with a goofy gedcom structure full of nulls and stuff
     */
    @Test
    public void testGoofyGedcom() {
        InMemoryGedcom g = new InMemoryGedcom();
        g.getIndividuals().put("Foo", null);
        g.getIndividuals().put(null, new Individual());
        g.getFamilies().put("Foo", null);
        g.getFamilies().put(null, new Family());
        g.getSubmitters().put("Foo", null);
        g.getSubmitters().put(null, new Submitter());
        g.getRepositories().put("Foo", null);
        g.getRepositories().put(null, new Repository());
        g.getNotes().put("Foo", null);
        g.getNotes().put(null, new NoteRecord("Foo"));
        g.getSources().put("Foo", null);
        g.getSources().put(null, new Source());
        g.getMultimedia().put("Foo", null);
        g.getMultimedia().put(null, new Multimedia());
        g.setSubmission(null);

        g.setHeader(null);

        Validator v = new Validator(g);
        v.setAutoRepairResponder(Validator.AUTO_REPAIR_NONE);
        v.validate();
        assertEquals(15, v.getResults().getAllFindings().size());

        v.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);
        v.validate();
        assertEquals(15, v.getResults().getAllFindings().size());

        v.setAutoRepairResponder(Validator.AUTO_REPAIR_NONE);
        v.validate();
        assertEquals(4, v.getResults().getAllFindings().size());
    }

    /**
     * Test with a goofy gedcom structure full of nulls and stuff
     */
    @Test
    public void testGoofyGedcomHeader() {
        InMemoryGedcom g = new InMemoryGedcom();
        g.setHeader(null);

        Validator v = new Validator(g);
        v.setAutoRepairResponder(Validator.AUTO_REPAIR_NONE);
        v.validate();
        assertEquals(0, v.getResults().getAllFindings().size());

        Header h = new Header();
        h.setGedcomVersion(null);
        g.setHeader(h);
        v.validate();
        assertEquals(1, v.getResults().getAllFindings().size());
        v.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);
        v.validate();
        assertEquals(1, v.getResults().getAllFindings().size());

        h = new Header();
        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber((String) null);
        h.setGedcomVersion(gv);
        g.setHeader(h);
        v.validate();
        assertEquals(1, v.getResults().getAllFindings().size());
        v.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);
        v.validate();
        assertEquals(0, v.getResults().getAllFindings().size());
    }

    /**
     * Test {@link Validator#isSpecified(String)}
     */
    @Test
    public void testIsSpecified() {
        Validator v = new Validator(new InMemoryGedcom());
        assertTrue(v.isSpecified("Foo"));
        assertTrue(v.isSpecified("Trailing Space        "));
        assertTrue(v.isSpecified("            Leading Space"));
        assertFalse(v.isSpecified(null));
        assertFalse(v.isSpecified(""));
        assertFalse(v.isSpecified("      "));
        assertFalse(v.isSpecified("  \t \n \r  "));

    }

    /**
     * Test method for {@link Validator#toString()}.
     */
    @Test
    public void testNullItems() {
        InMemoryGedcom g = new InMemoryGedcom();
        g.setHeader(null);
        g.setTrailer(null);
        g.setSubmission(null);
        g.getFamilies().put(null, null);
        g.getIndividuals().put(null, null);
        g.getMultimedia().put(null, null);
        g.getSources().put(null, null);
        g.getRepositories().put(null, null);
        g.getNotes().put(null, null);
        g.getSubmitters().put(null, null);

        Validator v = new Validator(g);
        assertEquals("Validator [results=ValidationResults [allFindings=[Finding [itemOfConcern=InMemoryGedcom [families=[null=null], "
                + "individuals=[null=null], multimedia=[null=null], noteStructures=[null=null], repositories=[null=null], "
                + "sources=[null=null], submission=null, submitters=[null=null], ], severity=INFO, problemCode=10, "
                + "problemDescription=Unable to determine GEDCOM version - assuming v5.5.1, ]]], autoRepairResponder=AUTO_REPAIR_NONE]",
                v.toString());
    }

    /**
     * Test method for {@link Validator#setAutoRepairResponder(AutoRepairResponder)}.
     */
    @Test
    public void testSetAutoRepairResponder() {
        InMemoryGedcom g = new InMemoryGedcom();
        Validator v = new Validator(g);
        assertSame(Validator.AUTO_REPAIR_NONE, v.getAutoRepairResponder());
        v.setAutoRepairResponder(this);
        assertSame(this, v.getAutoRepairResponder());
    }

    /**
     * Test method for {@link Validator#toString()}.
     * 
     * @throws GedcomParserException
     *             if the sample file cannot be parsed
     * @throws IOException
     *             if the sample file cannot be read
     */
    @Test
    public void testToString() throws IOException, GedcomParserException {
        IGedcom g = new InMemoryGedcom();
        Validator v = new Validator(g);
        assertEquals("Validator [results=ValidationResults [allFindings=[]], autoRepairResponder=AUTO_REPAIR_NONE]", v.toString());

        GedcomParser gp = new GedcomParser(new InMemoryGedcom());
        gp.load("sample/RelationshipTest.ged");
        g = gp.getGedcom();
        v = new Validator(g);
        v.validate();
        assertEquals("Validator [results=ValidationResults [allFindings=["
                + "Finding [fieldNameOfConcern=value, itemOfConcern=ANSI, severity=ERROR, problemCode=2, "
                + "problemDescription=Value supplied is not allowed, ], "
                + "Finding [fieldNameOfConcern=aliases, itemOfConcern=Patty /Klinghoffer/ aka NoHusband aka NoHusband, "
                + "severity=ERROR, problemCode=1, problemDescription=Value is a duplicate, ], "
                + "Finding [fieldNameOfConcern=aliases, itemOfConcern=Patty /Klinghoffer/ aka NoHusband aka NoHusband, "
                + "severity=ERROR, problemCode=1, problemDescription=Value is a duplicate, ], "
                + "Finding [fieldNameOfConcern=surnames, itemOfConcern=Hannah /Smith/ aka UnknownFather, spouse of George /Zucco/, "
                + "child of Patty /Klinghoffer/ and unknown, severity=WARNING, problemCode=14, "
                + "problemDescription=Child has surname(s) that do not match those of either parent, "
                + "relatedItems=[Patty /Klinghoffer/ aka NoHusband aka NoHusband], ]]], autoRepairResponder=AUTO_REPAIR_NONE]", v
                        .toString());

        v.setAutoRepairResponder(Validator.AUTO_REPAIR_ALL);
        v.validate();
        assertEquals("Validator [results=ValidationResults [allFindings=["
                + "Finding [fieldNameOfConcern=value, itemOfConcern=ANSI, severity=ERROR, problemCode=2, "
                + "problemDescription=Value supplied is not allowed, ], "
                + "Finding [fieldNameOfConcern=aliases, itemOfConcern=Patty /Klinghoffer/ aka NoHusband, severity=ERROR, problemCode=1, "
                + "problemDescription=Value is a duplicate, "
                + "repairs=[AutoRepair [before=Patty /Klinghoffer/ aka NoHusband aka NoHusband, after=Patty /Klinghoffer/ aka NoHusband]]], "
                + "Finding [fieldNameOfConcern=surnames, itemOfConcern=Hannah /Smith/ aka UnknownFather, spouse of George /Zucco/, "
                + "child of Patty /Klinghoffer/ and unknown, severity=WARNING, problemCode=14, "
                + "problemDescription=Child has surname(s) that do not match those of either parent, "
                + "relatedItems=[Patty /Klinghoffer/ aka NoHusband], ]]], autoRepairResponder=AUTO_REPAIR_ALL]", v.toString());
    }
}
