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
package org.gedcom4j.comparators;

import java.util.Date;
import java.util.List;

import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.parser.DateParser;
import org.gedcom4j.parser.DateParser.ImpreciseDatePreference;

/**
 * Comparator for sorting individuals by an event date (usually birth), then last name (surname), then first (given) name. When
 * there are multiple values for any of these three fields, only the preferred (first) value is considered.
 * 
 * @author frizbog1
 * 
 */
public class IndividualsByEventDateLastNameFirstNameComparator extends IndividualByLastNameFirstNameComparator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -8121061183483337581L;

    /**
     * The event type we're sorting by
     */
    private final IndividualEventType eventType;

    /**
     * Date parser
     */
    private final DateParser dp = new DateParser();

    /**
     * The imprecise date handling preference
     */
    private final ImpreciseDatePreference impreciseDatePreference;

    /**
     * Constructor
     * 
     * @param eventType
     *            the event type to sort by
     * @param impreciseDatePreference
     *            how you want imprecise dates (like ranges, or years without months or days) interpreted
     */
    public IndividualsByEventDateLastNameFirstNameComparator(IndividualEventType eventType,
            ImpreciseDatePreference impreciseDatePreference) {
        this.eventType = eventType;
        this.impreciseDatePreference = impreciseDatePreference;
    }

    /**
     * Compare two individuals
     * 
     * @param i1
     *            individual 1
     * @param i2
     *            individual 2
     * @return -1 if i1 &lt; i2, 0 if i1 == i2, 1 if i1 &gt; i2
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Individual i1, Individual i2) {

        Date d1 = getEarliestValueForPreferredBirthDate(i1);
        Date d2 = getEarliestValueForPreferredBirthDate(i2);

        if (d1 == null && d2 != null) {
            return -1;
        }
        if (d1 != null && d2 == null) {
            return 1;
        }
        if (d1 != null && d2 != null) {
            if (d1.before(d2)) {
                return -1;
            }
            if (d2.before(d1)) {
                return 1;
            }
        }

        return super.compare(i1, i2);
    }

    /**
     * Get the earliest value for the preferred (first) birthdate on the individual
     * 
     * @param i
     *            the individual
     * @return the earliest value for the preferred (first) birthdate on the individual
     */
    private Date getEarliestValueForPreferredBirthDate(Individual i) {
        Date result = null;
        List<IndividualEvent> birthDates = i.getEventsOfType(eventType);
        if (birthDates != null && !birthDates.isEmpty()) {
            IndividualEvent bd = birthDates.get(0);
            if (bd != null && bd.getDate() != null && bd.getDate().getValue() != null) {
                result = dp.parse(bd.getDate().getValue(), impreciseDatePreference);
            }
        }
        return result;
    }
}
