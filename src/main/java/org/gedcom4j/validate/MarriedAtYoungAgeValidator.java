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
import org.gedcom4j.model.FamilyEvent;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.enumerations.FamilyEventType;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.parser.DateParser;
import org.gedcom4j.parser.DateParser.ImpreciseDatePreference;
import org.gedcom4j.validate.Validator.Finding;

/**
 * Validator that looks for people that were married very young, which is likely a data error (although less so the older the
 * records).
 * 
 * @author frizbog
 */
public class MarriedAtYoungAgeValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -7265936570396961027L;

    /**
     * Number of milliseconds in sixty years
     */
    private static final long MILLIS_IN_SIXTEEN_YEARS = (long) (16 * 365.25 * 24 * 60 * 60 * 1000);

    /**
     * Date parser
     */
    private final DateParser dp = new DateParser();

    /**
     * Constructor
     * 
     * @param validator
     *            main {@link Validator} class that holds results and orchestrates all validation
     */
    public MarriedAtYoungAgeValidator(Validator validator) {
        super(validator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {

        for (Family f : getValidator().getGedcom().getFamilies().values()) {
            if (f.getHusband() == null || f.getWife() == null || f.getEvents() == null || f.getEvents().isEmpty()) {
                continue;
            }

            // Get the earliest possible marriage date
            FamilyEvent earliestMarriage = null;
            Date earliestMarriageDate = new Date();
            for (FamilyEvent e : f.getEvents()) {
                if (e.getType() == FamilyEventType.MARRIAGE && e.getDate() != null && e.getDate().getValue() != null) {
                    Date d = dp.parse(e.getDate().getValue());
                    if (d != null && d.before(earliestMarriageDate)) {
                        earliestMarriage = e;
                        earliestMarriageDate = d;
                    }
                }
            }
            if (earliestMarriage == null) {
                continue;
            }

            checkHusband(f, earliestMarriage, earliestMarriageDate);
            checkWife(f, earliestMarriage, earliestMarriageDate);
        }
    }

    /**
     * Check the age of the husband at the time of marriage
     * 
     * @param f
     *            the family
     * @param earliestMarriage
     *            the earliest marriage in the family
     * @param earliestMarriageDate
     *            the earliest date for the earliest marriage
     */
    private void checkHusband(Family f, FamilyEvent earliestMarriage, Date earliestMarriageDate) {
        Individual husband = f.getHusband();
        IndividualEvent husbandLatestBirth = getLatestEventOfType(husband, IndividualEventType.BIRTH);
        if (husbandLatestBirth == null) {
            return;
        }
        Date husbandLatestBirthDate = dp.parse(husbandLatestBirth.getDate().getValue(), ImpreciseDatePreference.FAVOR_LATEST);
        if (husbandLatestBirthDate != null) {
            long hDiff = earliestMarriageDate.getTime() - husbandLatestBirthDate.getTime();
            if (hDiff <= MILLIS_IN_SIXTEEN_YEARS) {
                Finding finding = newFinding(earliestMarriage, Severity.WARNING, ProblemCode.HUSBAND_WAS_LESS_THAN_SIXTEEN, "date");
                finding.getRelatedItems(true).add(husband);
            }
        }
    }

    /**
     * Check the age of the wife at the time of marriage
     * 
     * @param f
     *            the family
     * @param earliestMarriage
     *            the earliest marriage in the family
     * @param earliestMarriageDate
     *            the earliest date for the earliest marriage
     */
    private void checkWife(Family f, FamilyEvent earliestMarriage, Date earliestMarriageDate) {
        Individual wife = f.getWife();
        IndividualEvent wifeLatestBirth = getLatestEventOfType(wife, IndividualEventType.BIRTH);
        if (wifeLatestBirth == null) {
            return;
        }
        Date wifeLatestBirthDate = dp.parse(wifeLatestBirth.getDate().getValue(), ImpreciseDatePreference.FAVOR_LATEST);
        if (wifeLatestBirthDate != null) {
            long wDiff = earliestMarriageDate.getTime() - wifeLatestBirthDate.getTime();
            if (wDiff <= MILLIS_IN_SIXTEEN_YEARS) {
                Finding finding = newFinding(earliestMarriage, Severity.WARNING, ProblemCode.WIFE_WAS_LESS_THAN_SIXTEEN, "date");
                finding.getRelatedItems(true).add(wife);
            }
        }
    }

}
