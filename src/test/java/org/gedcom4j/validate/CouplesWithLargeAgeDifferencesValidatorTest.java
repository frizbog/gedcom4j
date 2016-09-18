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

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.Family;
import org.junit.Test;

/**
 * Test for {@link CouplesWithLargeAgeDifferencesValidator}
 * 
 * @author frizbog
 */
public class CouplesWithLargeAgeDifferencesValidatorTest extends AbstractValidatorTestCase {

    /**
     * Test for {@link CouplesWithLargeAgeDifferencesValidator} where there is no problem.
     *
     * @throws IOException
     *             when the file cannot be read
     * @throws GedcomParserException
     *             when the gedcom cannot be parsed
     */
    @Test
    public void testNegative() throws IOException, GedcomParserException {
        loadFile("sample/john_of_sea_20101009.ged");
        validator.validate();
        assertNoIssues();
    }

    /**
     * Test for {@link CouplesWithLargeAgeDifferencesValidator} that finds the problem.
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
        assertFindingsContain(Severity.WARNING, Family.class, ProblemCode.COUPLE_MORE_THAN_FIFTEEN_YEARS_AGE_DIFFERENCE.getCode(),
                null);
    }

}
