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
import org.gedcom4j.model.IndividualEvent;
import org.junit.Test;

/**
 * Test for {@link BirthsToYoungParentsValidator}
 * 
 * @author frizbog
 */
public class BirthsToYoungParentsValidatorTest extends AbstractValidatorTestCase {

    /**
     * Test for {@link BirthsToYoungParentsValidator} where there is no problem.
     *
     * @throws IOException
     *             when the file cannot be read
     * @throws GedcomParserException
     *             when the gedcom cannot be parsed
     */
    @Test
    public void testNegative() throws IOException, GedcomParserException {
        loadFile("sample/validatesOk.ged");
        validator.validate();
        assertNoIssues();
    }

    /**
     * Test for {@link BirthsToYoungParentsValidator} that finds the problem.
     *
     * @throws IOException
     *             when the file cannot be read
     * @throws GedcomParserException
     *             when the gedcom cannot be parsed
     */
    @Test
    public void testPositive() throws IOException, GedcomParserException {
        loadFile("sample/problemFile.ged");
        validator.validate();
        assertFindingsContain(Severity.WARNING, IndividualEvent.class, ProblemCode.MOTHER_MAY_NOT_HAVE_BEEN_BORN_YET.getCode(),
                "date");

        assertEquals(1, validator.getResults().getByCode(ProblemCode.MOTHER_MAY_NOT_HAVE_BEEN_BORN_YET).size());
        assertEquals(0, validator.getResults().getByCode(ProblemCode.FATHER_MAY_NOT_HAVE_BEEN_BORN_YET).size());
        assertEquals(2, validator.getResults().getByCode(ProblemCode.MOTHER_WAS_LESS_THAN_SIXTEEN).size());
        assertEquals(2, validator.getResults().getByCode(ProblemCode.FATHER_WAS_LESS_THAN_SIXTEEN).size());
    }

}
