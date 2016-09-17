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
import org.gedcom4j.validate.Validator.Finding;

/**
 * Validator that finds children born to parents who were sixteen or younger.
 * 
 * @author frizbog
 */
public class BirthsToYoungParentsValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 7175832620872775457L;

    /**
     * Number of milliseconds in sixteen years
     */
    private static final long MILLIS_IN_SIXTEEN_YEARS = (long) (16 * 365.25 * 24 * 60 * 60 * 1000);

    /**
     * Constructor
     * 
     * @param v
     *            the Validator that is tracking all the results
     */
    public BirthsToYoungParentsValidator(Validator v) {
        super(v);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        DateParser dp = new DateParser();
        for (Family f : getValidator().getGedcom().getFamilies().values()) {
            // No kids? Not interested
            if (f.getChildren() == null || f.getChildren().isEmpty()) {
                continue;
            }

            Individual husband = f.getHusband();
            IndividualEvent husbandLatestBirth = getLatestEventOfType(husband, IndividualEventType.BIRTH);
            Date husbandLatestBirthDate = null;
            if (husbandLatestBirth != null) {
                husbandLatestBirthDate = dp.parse(husbandLatestBirth.getDate().getValue(), ImpreciseDatePreference.FAVOR_LATEST);
            }

            Individual wife = f.getWife();
            IndividualEvent wifeLatestBirth = getLatestEventOfType(wife, IndividualEventType.BIRTH);
            Date wifeLatestBirthDate = null;
            if (wifeLatestBirth != null) {
                wifeLatestBirthDate = dp.parse(wifeLatestBirth.getDate().getValue(), ImpreciseDatePreference.FAVOR_LATEST);
            }

            // Neither parent has a birth date? Can't calculate, so skip
            if (husbandLatestBirthDate == null && wifeLatestBirthDate == null) {
                continue;
            }

            for (Individual kid : f.getChildren()) {
                IndividualEvent kidEarliestBirth = getEarliestEventOfType(kid, IndividualEventType.BIRTH);
                if (kidEarliestBirth == null) {
                    continue;
                }
                Date kidEarliestBirthDate = dp.parse(kidEarliestBirth.getDate().getValue(), ImpreciseDatePreference.FAVOR_EARLIEST);

                if (wifeLatestBirthDate != null) {
                    long momMillisDiff = kidEarliestBirthDate.getTime() - wifeLatestBirthDate.getTime();
                    if (momMillisDiff <= MILLIS_IN_SIXTEEN_YEARS) {
                        if (wifeLatestBirthDate.after(kidEarliestBirthDate)) {
                            Finding newFinding = newFinding(kidEarliestBirth, Severity.WARNING,
                                    ProblemCode.MOTHER_MAY_NOT_HAVE_BEEN_BORN_YET, "date");
                            newFinding.getRelatedItems(true).add(kid);
                            newFinding.getRelatedItems().add(wife);
                        } else {
                            Finding newFinding = newFinding(kidEarliestBirth, Severity.WARNING,
                                    ProblemCode.MOTHER_WAS_LESS_THAN_SIXTEEN, "date");
                            newFinding.getRelatedItems(true).add(kid);
                            newFinding.getRelatedItems().add(wife);
                        }
                    }
                }
                if (husbandLatestBirth != null && husbandLatestBirthDate != null) {
                    long dadMillisDiff = kidEarliestBirthDate.getTime() - husbandLatestBirthDate.getTime();
                    if (dadMillisDiff <= MILLIS_IN_SIXTEEN_YEARS) {
                        if (husbandLatestBirthDate.after(kidEarliestBirthDate)) {
                            Finding newFinding = newFinding(kidEarliestBirth, Severity.WARNING,
                                    ProblemCode.FATHER_MAY_NOT_HAVE_BEEN_BORN_YET, "date");
                            newFinding.getRelatedItems(true).add(kid);
                            newFinding.getRelatedItems().add(husband);
                        } else {
                            Finding newFinding = newFinding(kidEarliestBirth, Severity.WARNING,
                                    ProblemCode.FATHER_WAS_LESS_THAN_SIXTEEN, "date");
                            newFinding.getRelatedItems(true).add(kid);
                            newFinding.getRelatedItems().add(husband);
                        }
                    }
                }
            }

        }

    }

}
