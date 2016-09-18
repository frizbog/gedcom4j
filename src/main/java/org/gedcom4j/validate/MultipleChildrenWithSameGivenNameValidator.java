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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.gedcom4j.model.Family;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.parser.DateParser;
import org.gedcom4j.parser.DateParser.ImpreciseDatePreference;
import org.gedcom4j.validate.Validator.Finding;

/**
 * Validator that looks for multiple children in a family that have the same given names AND had overlapping lifespans.
 * 
 * @author frizbog
 */
public class MultipleChildrenWithSameGivenNameValidator extends AbstractValidator {

    /** A date about 10000 years into the future, to use when there is no date but still want to do comparisons without nulls */
    private static final Date FAR_IN_THE_FUTURE = new Date(10000L * 365 * 24 * 60 * 60 * 1000);

    /** A date about 10000 years ago, to use when there is no date but still want to do comparisons without nulls */
    private static final Date FAR_IN_THE_PAST = new Date(-10000L * 365 * 24 * 60 * 60 * 1000);

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -3991408212675640583L;

    /**
     * Constructor
     *
     * @param validator
     *            the main {@link Validator} that orchestrates validation and tracks results
     */
    public MultipleChildrenWithSameGivenNameValidator(Validator validator) {
        super(validator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        for (Family f : getValidator().getGedcom().getFamilies().values()) {
            if (f.getChildren() == null) {
                continue;
            }

            // Build a map of each first name and all the kids who have that first name
            Map<String, Set<Individual>> kidsByFirstName = getKidsByGivenName(f);

            // Check the map for any names with more than one kid in the family who has it
            for (Entry<String, Set<Individual>> e : kidsByFirstName.entrySet()) {
                if (e.getValue().size() > 1 && isSpecified(e.getKey())) {
                    // Multiple kids with the same given name.

                    checkOverlappingLifespans(e.getValue(), f);

                }
            }
        }
    }

    /**
     * Check overlapping lifespans - any pair of kids with the same name with overlapping lifespans will be a finding.
     *
     * @param kidsWithSameGivenName
     *            the kids with same given name
     * @param f
     *            the family involved
     */
    private void checkOverlappingLifespans(Set<Individual> kidsWithSameGivenName, Family f) {
        List<Individual> peopleWithSameGivenName = new ArrayList<>(kidsWithSameGivenName);

        // Go through the unique pairs in the list
        for (int m = 0; m < peopleWithSameGivenName.size() - 1; m++) {
            Individual i1 = peopleWithSameGivenName.get(m);

            Date i1birth = getEarliestBirthDate(i1);
            Date i1death = getLatestDeathDate(i1);

            for (int n = m + 1; n < peopleWithSameGivenName.size(); n++) {
                Individual i2 = peopleWithSameGivenName.get(n);

                Date i2birth = getEarliestBirthDate(i2);
                Date i2death = getLatestDeathDate(i2);

                boolean i1LifeBeforeI2Born = i1birth.before(i2birth) && i1death.before(i2birth);
                boolean i2LifeBeforeI1Born = i2birth.before(i1birth) && i2death.before(i1birth);
                boolean i1LifeAfterI2Died = i1birth.after(i2death) && i1death.after(i2death);
                boolean i2LifeAfterI1Died = i2birth.after(i1death) && i2death.after(i1death);
                if (i1LifeBeforeI2Born || i2LifeBeforeI1Born || i1LifeAfterI2Died || i2LifeAfterI1Died) {
                    continue;
                }
                // There was overlap, log a finding
                Finding newFinding = newFinding(f, Severity.WARNING, ProblemCode.MULTIPLE_CHILDREN_WITH_SAME_GIVEN_NAME,
                        "children");
                newFinding.getRelatedItems(true).add(i1);
                newFinding.getRelatedItems(true).add(i2);
            }
        }

    }

    /**
     * Gets the earliest birth date.
     *
     * @param ind
     *            the ind
     * @return the earliest birth date, or the earliest possible date if no parseable birth date could be found
     */
    private Date getEarliestBirthDate(Individual ind) {
        IndividualEvent e = getEarliestEventOfType(ind, IndividualEventType.BIRTH);
        if (e == null || e.getDate() == null || e.getDate().getValue() == null) {
            return FAR_IN_THE_PAST;
        }
        DateParser dp = new DateParser();
        Date d = dp.parse(e.getDate().getValue(), ImpreciseDatePreference.FAVOR_EARLIEST);
        if (d == null) {
            return FAR_IN_THE_PAST;
        }
        return d;
    }

    /**
     * Get a map of kids keyed by their given names
     * 
     * @param f
     *            the family
     * @return a map of kids keyed by their given names
     */
    private Map<String, Set<Individual>> getKidsByGivenName(Family f) {
        Map<String, Set<Individual>> result = new HashMap<>();
        for (Individual kid : f.getChildren()) {
            for (PersonalName pn : kid.getNames(true)) {
                String gn = null;
                if (pn.getGivenName() != null && isSpecified(pn.getGivenName().getValue())) {
                    gn = pn.getGivenName().getValue();
                } else if (isSpecified(pn.getBasic())) {
                    gn = pn.getBasic().trim();
                    int firstSlash = gn.indexOf('/');
                    if (firstSlash > 0) {
                        gn = gn.substring(0, firstSlash).trim();
                    } else {
                        gn = null;
                    }
                }
                if (isSpecified(gn)) {
                    Set<Individual> kidsWithThisFirstName = result.get(gn);
                    if (kidsWithThisFirstName == null) {
                        kidsWithThisFirstName = new HashSet<>();
                        result.put(gn, kidsWithThisFirstName);
                    }
                    kidsWithThisFirstName.add(kid);
                }
            }
        }
        return result;
    }

    /**
     * Gets the latest death date. Uses a burial date if no death date is available.
     *
     * @param ind
     *            the ind
     * @return the latest death date, or the latest possible date if no parseable death date could be found
     */
    private Date getLatestDeathDate(Individual ind) {
        IndividualEvent e = getLatestEventOfType(ind, IndividualEventType.DEATH);
        if (e == null || e.getDate() == null || e.getDate().getValue() == null) {
            e = getLatestEventOfType(ind, IndividualEventType.BURIAL);
        }
        if (e == null || e.getDate() == null || e.getDate().getValue() == null) {
            return FAR_IN_THE_FUTURE;
        }
        DateParser dp = new DateParser();
        Date d = dp.parse(e.getDate().getValue(), ImpreciseDatePreference.FAVOR_LATEST);
        if (d == null) {
            return FAR_IN_THE_FUTURE;
        }
        return d;
    }

}
