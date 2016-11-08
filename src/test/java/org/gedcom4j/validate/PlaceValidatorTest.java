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

import org.gedcom4j.model.Place;
import org.gedcom4j.model.PlaceNameVariation;
import org.junit.Test;

/**
 * Test for {@link PlaceValidator}
 * 
 * @author frizbog
 */
public class PlaceValidatorTest extends AbstractValidatorTestCase {

    /**
     * Test an unpopulated default {@link Place} object
     */
    @Test
    public void testEmptyPlace() {
        Place p = new Place();
        AbstractValidator sv = new PlaceValidator(validator, p);
        sv.validate();
        assertFindingsContain(Severity.ERROR, p, ProblemCode.MISSING_REQUIRED_VALUE, "placeName");
    }

    /**
     * Test a heavily filled-out {@link Place} object
     */
    @Test
    public void testFilledOutPlace() {
        Place p = new Place();
        p.setPlaceName("Home");
        p.setLatitude("qwerty");
        p.setLongitude("uiop");

        PlaceNameVariation ph = new PlaceNameVariation();
        ph.setVariation("Hoam");
        p.getPhonetic(true).add(ph);

        PlaceNameVariation r = new PlaceNameVariation();
        r.setVariation("Doesn't matter");
        p.getRomanized(true).add(r);

        AbstractValidator sv = new PlaceValidator(validator, p);
        sv.validate();
        assertNoIssues();
    }

    /**
     * Test a minimally filled-out {@link Place} object
     */
    @Test
    public void testMinimalPlace() {
        Place p = new Place();
        p.setPlaceName("Home");
        AbstractValidator sv = new PlaceValidator(validator, p);
        sv.validate();
        assertNoIssues();
    }
}
