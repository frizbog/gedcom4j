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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.gedcom4j.model.Family;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.parser.DateParser;
import org.gedcom4j.parser.DateParser.ImpreciseDatePreference;

/**
 * Validator that finds multiple births of four or more children on the same day. Possible but more likely to be a data entry error.
 * 
 * @author frizbog
 */
public class QuadrupletsAndMoreValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -109510055319633255L;

    /**
     * Constructor
     * 
     * @param validator
     *            the {@link Validator} that orchestrates validation and tracks results
     */
    public QuadrupletsAndMoreValidator(Validator validator) {
        super(validator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        for (Family f : getValidator().getGedcom().getFamilies().values()) {

            // If there aren't at least 4 children there's nothing to do with this family
            if (f.getChildren() == null || f.getChildren().size() < 4) {
                continue;
            }

            /*
             * Sort out the children into lists of people, by their (approximate) birth date. Treat people born within 48h of each
             * other as on the same date (multiple births)
             */
            Map<Date, Set<Individual>> births = new HashMap<>();
            for (Individual i : f.getChildren()) {
                Date birthDate = getEarliestBirthDate(i);
                if (birthDate == null) {
                    continue;
                }
                boolean added = false;
                for (Entry<Date, Set<Individual>> birth : births.entrySet()) {
                    Date d = birth.getKey();
                    // Look for any already existing list within 48h of this birth date
                    if (Math.abs(d.getTime() - birthDate.getTime()) < 48L * 60 * 60 * 1000) {
                        birth.getValue().add(i);
                        added = true;
                    }
                }
                if (!added) {
                    Set<Individual> birthsOnDate = new HashSet<>();
                    birthsOnDate.add(i);
                    births.put(birthDate, birthsOnDate);
                }
            }

            /* Look through the birth dates and see if any have 4 or more individuals born on those dates */
            for (Entry<Date, Set<Individual>> e : births.entrySet()) {
                if (e.getValue().size() >= 4) {
                    // We got a hit, add to results
                    newFinding(f, Severity.WARNING, ProblemCode.MORE_THAN_THREE_CHILDREN_BORN_WITHIN_48_HOURS, "children")
                            .getRelatedItems(true).addAll(e.getValue());
                }
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
            return null;
        }
        DateParser dp = new DateParser();
        return dp.parse(e.getDate().getValue(), ImpreciseDatePreference.FAVOR_EARLIEST);
    }

}
