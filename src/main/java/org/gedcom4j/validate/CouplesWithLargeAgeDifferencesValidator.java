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

import java.util.Date;

import org.gedcom4j.model.Family;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.parser.DateParser;
import org.gedcom4j.parser.DateParser.ImpreciseDatePreference;

/**
 * Validator that looks for couples who have an age difference of 15 years or more.
 * 
 * @author frizbog
 */
public class CouplesWithLargeAgeDifferencesValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -602931269419442765L;

    /**
     * Number of milliseconds in sixty years
     */
    private static final long MILLIS_IN_FIFTEEN_YEARS = (long) (15 * 365.25 * 24 * 60 * 60 * 1000);

    /**
     * Constructor
     * 
     * @param validator
     *            the {@link Validator} that orchestrates validation and tracks results
     */
    public CouplesWithLargeAgeDifferencesValidator(Validator validator) {
        super(validator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        DateParser dp = new DateParser();

        for (Family f : getValidator().getGedcom().getFamilies().values()) {
            if (f == null || f.getHusband() == null || f.getWife() == null) {
                continue;
            }

            Individual husband = (f.getHusband() == null ? null : f.getHusband().getIndividual());
            IndividualEvent husbandLatestBirth = getLatestEventOfType(husband, IndividualEventType.BIRTH);
            Date husbandLatestBirthDate = null;
            if (husbandLatestBirth != null) {
                husbandLatestBirthDate = dp.parse(husbandLatestBirth.getDate().getValue(), ImpreciseDatePreference.FAVOR_LATEST);
            }

            Individual wife = (f.getWife() == null ? null : f.getWife().getIndividual());
            IndividualEvent wifeLatestBirth = getLatestEventOfType(wife, IndividualEventType.BIRTH);
            Date wifeLatestBirthDate = null;
            if (wifeLatestBirth != null) {
                wifeLatestBirthDate = dp.parse(wifeLatestBirth.getDate().getValue(), ImpreciseDatePreference.FAVOR_LATEST);
            }

            // Both spouses need a birth date to proceed
            if ((husbandLatestBirthDate == null || wifeLatestBirthDate == null)) {
                continue;
            }

            long diff = Math.abs(husbandLatestBirthDate.getTime() - wifeLatestBirthDate.getTime());

            if (diff >= MILLIS_IN_FIFTEEN_YEARS) {
                newFinding(f, Severity.WARNING, ProblemCode.COUPLE_MORE_THAN_FIFTEEN_YEARS_AGE_DIFFERENCE, null);
            }
        }

    }

}
