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

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.IGedcom;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.validate.Validator.Finding;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link ValidationResults}
 * 
 * @author frizbog
 */
public class ValidationResultsTest {

    /**
     * Test fixture
     */
    private IGedcom g;

    /**
     * Set up the test fixtures
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws GedcomParserException
     *             when the gedcom cannot be parsed
     */
    @Before
    public void setUp() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/Harry_Potter.ged");
        g = gp.getGedcom();
    }

    /**
     * Test method for {@link ValidationResults#add(Finding)}, {@link ValidationResults#getAllFindings()}, and
     * {@link ValidationResults#clear()}.
     */
    @Test
    public void testAddAndGetAll() {
        Validator v = new Validator(g);
        ValidationResults vr = v.getResults();
        assertEquals(0, vr.getAllFindings().size());
        v.newFinding(g.getIndividuals().get("@I1@"), Severity.INFO, ProblemCode.DUPLICATE_VALUE, null);
        assertEquals(1, vr.getAllFindings().size());
        vr.clear();
        assertEquals(0, vr.getAllFindings().size());
    }

    /**
     * Test method for {@link ValidationResults#getByCode(ProblemCode)}
     */
    @Test
    public void testGetByProblemCodeEnum() {
        Validator v = new Validator(g);
        ValidationResults vr = v.getResults();
        assertEquals(0, vr.getAllFindings().size());
        v.newFinding(g.getIndividuals().get("@I1@"), Severity.INFO, ProblemCode.DUPLICATE_VALUE, null);
        v.newFinding(g.getIndividuals().get("@I2@"), Severity.WARNING, ProblemCode.ILLEGAL_VALUE, null);
        v.newFinding(g.getIndividuals().get("@I3@"), Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, null);
        assertEquals(3, vr.getAllFindings().size());
        assertEquals(1, vr.getByCode(ProblemCode.DUPLICATE_VALUE).size());
        assertEquals(1, vr.getByCode(ProblemCode.ILLEGAL_VALUE).size());
        assertEquals(1, vr.getByCode(ProblemCode.MISSING_REQUIRED_VALUE).size());
        assertEquals(0, vr.getByCode(ProblemCode.CROSS_REFERENCE_NOT_FOUND).size());
    }

    /**
     * Test method for {@link ValidationResults#getByCode(int)}
     */
    @Test
    public void testGetByProblemCodeInt() {
        Validator v = new Validator(g);
        ValidationResults vr = v.getResults();
        assertEquals(0, vr.getAllFindings().size());
        v.newFinding(g.getIndividuals().get("@I1@"), Severity.INFO, ProblemCode.DUPLICATE_VALUE, null);
        v.newFinding(g.getIndividuals().get("@I2@"), Severity.WARNING, ProblemCode.ILLEGAL_VALUE, null);
        v.newFinding(g.getIndividuals().get("@I3@"), Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, null);
        assertEquals(3, vr.getAllFindings().size());
        assertEquals(1, vr.getByCode(ProblemCode.DUPLICATE_VALUE.getCode()).size());
        assertEquals(1, vr.getByCode(ProblemCode.ILLEGAL_VALUE.getCode()).size());
        assertEquals(1, vr.getByCode(ProblemCode.MISSING_REQUIRED_VALUE.getCode()).size());
        assertEquals(0, vr.getByCode(ProblemCode.CROSS_REFERENCE_NOT_FOUND.getCode()).size());
    }

    /**
     * Test method for {@link ValidationResults#getByCode(int)}
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetByProblemCodeIntNull() {
        Validator v = new Validator(g);
        ValidationResults vr = v.getResults();
        vr.getByCode(null);
    }

    /**
     * Test method for {@link ValidationResults#getBySeverity(Severity)}.
     */
    @Test
    public void testGetBySeverity() {
        Validator v = new Validator(g);
        ValidationResults vr = v.getResults();
        assertEquals(0, vr.getAllFindings().size());
        v.newFinding(g.getIndividuals().get("@I1@"), Severity.INFO, ProblemCode.DUPLICATE_VALUE, null);
        v.newFinding(g.getIndividuals().get("@I2@"), Severity.WARNING, ProblemCode.ILLEGAL_VALUE, null);
        v.newFinding(g.getIndividuals().get("@I3@"), Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, null);
        assertEquals(3, vr.getAllFindings().size());
        assertEquals(1, vr.getBySeverity(Severity.INFO).size());
        assertEquals(1, vr.getBySeverity(Severity.WARNING).size());
        assertEquals(1, vr.getBySeverity(Severity.ERROR).size());
    }

    /**
     * Negative test method for {@link ValidationResults#getBySeverity(Severity)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetBySeverityNegative() {
        ValidationResults vr = new ValidationResults();
        vr.getBySeverity(null); // Boom
    }

    /**
     * Test method for {@link ValidationResults#getFindingsForObject(org.gedcom4j.model.ModelElement)}.
     */
    @Test
    public void testGetFindingsForObject() {
        Validator v = new Validator(g);
        ValidationResults vr = v.getResults();
        assertEquals(0, vr.getAllFindings().size());
        v.newFinding(g.getIndividuals().get("@I1@"), Severity.INFO, ProblemCode.DUPLICATE_VALUE, null);
        v.newFinding(g.getIndividuals().get("@I2@"), Severity.WARNING, ProblemCode.ILLEGAL_VALUE, null);
        v.newFinding(g.getIndividuals().get("@I3@"), Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, null);
        assertEquals(3, vr.getAllFindings().size());
        assertEquals(1, vr.getFindingsForObject(g.getIndividuals().get("@I1@")).size());
        assertEquals(1, vr.getFindingsForObject(g.getIndividuals().get("@I2@")).size());
        assertEquals(1, vr.getFindingsForObject(g.getIndividuals().get("@I3@")).size());
        assertEquals(0, vr.getFindingsForObject(g.getIndividuals().get("@I4@")).size());
        assertEquals(0, vr.getFindingsForObject(g.getFamilies().get("@F1@")).size());
    }

    /**
     * Test {@link ValidationResults#toString()}
     */
    @Test
    public void testToString() {
        Validator v = new Validator(g);
        ValidationResults vr = v.getResults();
        assertEquals("ValidationResults [allFindings=[]]", vr.toString());
    }

}
