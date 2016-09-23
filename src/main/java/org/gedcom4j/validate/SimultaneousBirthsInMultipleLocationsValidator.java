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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.gedcom4j.model.Family;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.IndividualReference;
import org.gedcom4j.model.Place;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.parser.DateParser;
import org.gedcom4j.validate.Validator.Finding;

/**
 * Validator that looks for multiple simultaneous births in different locations.
 * 
 * @author frizbog
 */
public class SimultaneousBirthsInMultipleLocationsValidator extends AbstractValidator {

    /**
     * A birthEvent of a specific person
     */
    private static class Birth {

        /** The person. */
        private Individual person;

        /** The birthEvent. */
        private IndividualEvent birthEvent;

        /**
         * Get the birth event
         * 
         * @return the birth event
         */
        public IndividualEvent getBirthEvent() {
            return birthEvent;
        }

        /**
         * Get the person
         * 
         * @return the person
         */
        public Individual getPerson() {
            return person;
        }

        /**
         * Set the birth event
         * 
         * @param birthEvent
         *            the birth event to set
         */
        public void setBirthEvent(IndividualEvent birthEvent) {
            this.birthEvent = birthEvent;
        }

        /**
         * Set the person
         * 
         * @param person
         *            the person to set
         */
        public void setPerson(Individual person) {
            this.person = person;
        }
    }

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 6667370628936043626L;

    /**
     * Constructor
     * 
     * @param validator
     *            {@link Validator} instance that orchestrates validation and tracks results
     */
    public SimultaneousBirthsInMultipleLocationsValidator(Validator validator) {
        super(validator);
    }

    /**
     * Sort out the children into lists of people, grouping by their (approximate) birth date. Treat people born within 48h of each
     * other as on the same date (twins, triplets, etc.)
     * 
     * @param f
     *            the family
     * @return a Map of Births, keyed (grouped) by approximate birth date
     */
    protected Map<Date, Set<Birth>> groupChildrenByBirthDate(Family f) {
        Map<Date, Set<Birth>> birthsByDate = new HashMap<>();
        DateParser dp = new DateParser();

        for (IndividualReference iRef : f.getChildren()) {
            if (iRef == null) {
                continue;
            }
            Individual i = iRef.getIndividual();
            List<IndividualEvent> birthEvents = i.getEventsOfType(IndividualEventType.BIRTH);
            for (IndividualEvent birthEvent : birthEvents) {
                if (birthEvent.getDate() == null) {
                    continue;
                }
                Date birthDate = dp.parse(birthEvent.getDate().getValue());
                if (birthDate == null) {
                    continue;
                }

                boolean added = false;
                for (Entry<Date, Set<Birth>> birthByDate : birthsByDate.entrySet()) {
                    // Look for any already existing set or people born within 48h of this birthEvent date
                    Date bd = birthByDate.getKey();
                    if (Math.abs(bd.getTime() - birthDate.getTime()) < 48L * 60 * 60 * 1000) {
                        /*
                         * The existing date in the Map is roughly the same as this birthEvent date, so add this birthEvent to that
                         * map entry
                         */
                        Birth b = new Birth();
                        b.setBirthEvent(birthEvent);
                        b.setPerson(i);
                        birthByDate.getValue().add(b);
                        added = true;
                    }
                }
                // If we didn't add the birthEvent to any pre-existing map entry, do it now.
                if (!added) {
                    Set<Birth> birthsOnDate = new HashSet<>();
                    Birth b = new Birth();
                    b.setBirthEvent(birthEvent);
                    b.setPerson(i);
                    birthsOnDate.add(b);
                    birthsByDate.put(birthDate, birthsOnDate);
                }
            }
        }
        return birthsByDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        for (Family f : getValidator().getGedcom().getFamilies().values()) {

            // If there aren't at least 2 children there's nothing to do with this family
            if (f.getChildren() == null || f.getChildren().size() < 2) {
                continue;
            }

            // Go through all the consolidated dates of birthEvent
            for (Entry<Date, Set<Birth>> e : groupChildrenByBirthDate(f).entrySet()) {
                Set<Birth> birthsOnDate = e.getValue();
                // See how many places were listed for the births on this date
                Set<Place> places = new HashSet<>();
                Set<Individual> kids = new HashSet<>();
                for (Birth b : birthsOnDate) {
                    places.add(b.getBirthEvent().getPlace());
                    kids.add(b.getPerson());
                }
                if (places.size() > 1) {
                    // Found multiple places on this date
                    Finding vf = newFinding(f, Severity.WARNING, ProblemCode.SIMULTANEOUS_BIRTHS_IN_MULTIPLE_LOCATIONS, "children");
                    vf.getRelatedItems(true).addAll(kids);
                }
            }

        }
    }

}
